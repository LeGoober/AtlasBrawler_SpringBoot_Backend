package com.atlasbrawler.backend.service;

import com.atlasbrawler.backend.util.BlockchainUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

@Service
public class BlockchainService {
    
    private static final Logger logger = LoggerFactory.getLogger(BlockchainService.class);
    
    private final Web3j web3j;
    private final Credentials credentials;
    private final BlockchainUtil blockchainUtil;
    
    @Value("${celo.contract.reward.address:}")
    private String rewardContractAddress;
    
    public BlockchainService(Web3j web3j, 
                           Credentials credentials,
                           BlockchainUtil blockchainUtil) {
        this.web3j = web3j;
        this.credentials = credentials;
        this.blockchainUtil = blockchainUtil;
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
}
