package com.atlasbrawler.backend.service;

import com.atlasbrawler.backend.dto.SignedTransactionRequest;
import com.atlasbrawler.backend.dto.TransactionResponse;
import com.atlasbrawler.backend.util.BlockchainUtil;
import com.atlasbrawler.backend.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Keys;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.SignedRawTransaction;
import org.web3j.crypto.Hash;
import org.web3j.crypto.ECDSASignature;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class BlockchainService {
    
    private static final Logger logger = LoggerFactory.getLogger(BlockchainService.class);
    
    private final Web3j web3j;
    private final Credentials credentials;
    private final BlockchainUtil blockchainUtil;
    private final PlayerService playerService;

    @Value("${celo.contract.reward.address:}")
    private String rewardContractAddress;

    public BlockchainService(Web3j web3j,
    Credentials credentials,
                        BlockchainUtil blockchainUtil,
                        PlayerService playerService) {
    this.web3j = web3j;
        this.credentials = credentials;
        this.blockchainUtil = blockchainUtil;
        this.playerService = playerService;
    }
    
    public String transferReward(String toAddress, BigDecimal amountInCUSD) {
        try {
            logger.info("Transferring {} cUSD to {}", amountInCUSD, toAddress);
            
            TransactionReceipt receipt = Transfer.sendFunds(
                web3j,
                credentials,
                toAddress,
                amountInCUSD,
                Convert.Unit.ETHER
            ).send();
            
            String txHash = receipt.getTransactionHash();
            logger.info("Transfer successful. TX: {}", txHash);
            
            return txHash;
        } catch (Exception e) {
            logger.error("Failed to transfer reward to {}", toAddress, e);
            throw new RuntimeException("Blockchain transfer failed", e);
        }
    }
    
    public BigDecimal getBalance(String address) {
        try {
            BigDecimal balance = blockchainUtil.weiToEther(
                web3j.ethGetBalance(address, org.web3j.protocol.core.DefaultBlockParameterName.LATEST)
                    .send()
                    .getBalance()
            );
            
            logger.debug("Balance for {}: {} cUSD", address, balance);
            return balance;
            } catch (Exception e) {
            logger.error("Failed to get balance for {}", address, e);
            return BigDecimal.ZERO;
            }
            }

    /**
     * Processes a signed transaction from MiniPay/WalletConnect.
     * Verifies the signer matches the player's wallet address,
     * sends the transaction to the network, and returns the receipt.
     */
    public TransactionResponse processSignedTransaction(SignedTransactionRequest request) {
        try {
            logger.info("Processing signed transaction for wallet: {}", request.getWalletAddress());

            // Decode the signed transaction to verify signer
            org.web3j.crypto.RawTransaction rawTx = org.web3j.crypto.TransactionDecoder.decode(request.getSignedTransaction());
            if (rawTx == null) {
                throw new IllegalArgumentException("Invalid signed transaction");
            }

            if (!(rawTx instanceof SignedRawTransaction)) {
                throw new IllegalArgumentException("Transaction is not signed");
            }

            SignedRawTransaction signedTx = (SignedRawTransaction) rawTx;
            Sign.SignatureData signatureData = signedTx.getSignatureData();

            // Send the signed transaction
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(request.getSignedTransaction()).send();
            if (ethSendTransaction.hasError()) {
                throw new RuntimeException("Transaction failed: " + ethSendTransaction.getError().getMessage());
            }

            String txHash = ethSendTransaction.getTransactionHash();
            logger.info("Transaction sent. TX: {}", txHash);

            // Wait for receipt (optional, but good for confirmation)
            TransactionReceipt receipt = null;
            int attempts = 0;
            while (receipt == null && attempts < 30) { // Wait up to 30 seconds
                try {
                    Thread.sleep(1000);
                    receipt = web3j.ethGetTransactionReceipt(txHash).send().getTransactionReceipt().orElse(null);
                    attempts++;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            if (receipt == null) {
                logger.warn("Transaction receipt not found within timeout for TX: {}", txHash);
            }

            // Recover signer address from signature (CRITICAL for security)
            byte[] encoded = TransactionEncoder.encode(rawTx);
            byte[] messageHash = Hash.sha3(encoded);

            // Calculate header from v (byte[])
            int header = 0;
            for (byte b : signatureData.getV()) {
                header = (header << 8) + (b & 0xFF);
            }

            int recId;
            if (header == 27 || header == 28) {
                recId = header - 27;
            } else if (header >= 35) {
                recId = (header - 35) % 2;
            } else {
                throw new IllegalArgumentException("Invalid v value in signature");
            }

            ECDSASignature sig = new ECDSASignature(
                    new BigInteger(1, signatureData.getR()),
                    new BigInteger(1, signatureData.getS())
            );

            BigInteger key = Sign.recoverFromSignature(recId, sig, messageHash);
            if (key == null) {
                throw new RuntimeException("Could not recover public key from signature");
            }

            String recoveredAddress = "0x" + Keys.getAddress(key);

            if (!recoveredAddress.equalsIgnoreCase(request.getWalletAddress())) {
                throw new SecurityException("Signature verification failed: signer does not match wallet");
            }

            BigDecimal amount = blockchainUtil.weiToEther(rawTx.getValue());
            String toAddress = rawTx.getTo();

            if (receipt != null && receipt.isStatusOK() && toAddress != null &&
                toAddress.equalsIgnoreCase(credentials.getAddress())) {
                playerService.addCUSDBalance(request.getWalletAddress(), amount);
                logger.info("Deposited {} cUSD from {}", amount, request.getWalletAddress());
            }

            return TransactionResponse.builder()
                    .transactionHash(txHash)
                    .status(receipt != null && receipt.isStatusOK() ? "CONFIRMED" : "PENDING")
                    .amount(amount)
                    .fromAddress(recoveredAddress)
                    .toAddress(toAddress != null ? toAddress : "contract-creation")
                    .build();

        } catch (Exception e) {
            logger.error("Failed to process signed transaction for {}", request.getWalletAddress(), e);
            throw new RuntimeException("Failed to process signed transaction", e);
        }
    }
}