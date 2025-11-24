package com.atlasbrawler.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

@Configuration
public class Web3jConfig {
    
    @Value("${celo.rpc.url}")
    private String rpcUrl;
    
    @Value("${celo.wallet.private.key:${CELO_PRIVATE_KEY:}}")
    private String privateKey;
    
    @Value("${celo.chain.id}")
    private Long chainId;
    
    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(rpcUrl));
    }
    
    @Bean
    public Credentials credentials() {
        if (privateKey == null || privateKey.isEmpty()) {
            throw new IllegalStateException("Celo private key not configured. Set property 'celo.wallet.private.key' or the environment variable 'CELO_PRIVATE_KEY'.");
        }
        return Credentials.create(privateKey);
    }
    
    @Bean
    public DefaultGasProvider gasProvider() {
        return new DefaultGasProvider();
    }
    
    @Bean
    public Long chainId() {
        return chainId;
    }
}
