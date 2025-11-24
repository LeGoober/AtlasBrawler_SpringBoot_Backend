package com.atlasbrawler.backend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
public class BlockchainUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(BlockchainUtil.class);
    private final Web3j web3j;
    
    public BlockchainUtil(Web3j web3j) {
        this.web3j = web3j;
    }
    
    public BigInteger weiToGwei(BigInteger wei) {
        return wei.divide(BigInteger.valueOf(1_000_000_000));
    }
    
    public BigInteger gweiToWei(BigInteger gwei) {
        return gwei.multiply(BigInteger.valueOf(1_000_000_000));
    }
    
    public BigDecimal weiToEther(BigInteger wei) {
        return Convert.fromWei(new BigDecimal(wei), Convert.Unit.ETHER);
    }
    
    public BigInteger etherToWei(BigDecimal ether) {
        return Convert.toWei(ether, Convert.Unit.ETHER).toBigInteger();
    }
    
    public CompletableFuture<Optional<TransactionReceipt>> waitForTransactionReceipt(String transactionHash) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                int attempts = 0;
                int maxAttempts = 40;
                
                while (attempts < maxAttempts) {
                    Optional<TransactionReceipt> receipt = web3j
                        .ethGetTransactionReceipt(transactionHash)
                        .send()
                        .getTransactionReceipt();
                    
                    if (receipt.isPresent()) {
                        return receipt;
                    }
                    
                    Thread.sleep(3000);
                    attempts++;
                }
                
                logger.warn("Transaction receipt not found after {} attempts for tx: {}", maxAttempts, transactionHash);
                return Optional.empty();
            } catch (Exception e) {
                logger.error("Error waiting for transaction receipt: {}", transactionHash, e);
                return Optional.empty();
            }
        });
    }
    
    public CompletableFuture<BigInteger> getCurrentBlockNumber() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return web3j.ethBlockNumber().send().getBlockNumber();
            } catch (Exception e) {
                logger.error("Error getting current block number", e);
                return BigInteger.ZERO;
            }
        });
    }
}
