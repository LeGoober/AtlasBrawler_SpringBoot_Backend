package com.atlasbrawler.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransactionResponse {
    private String transactionHash;
    private String status;
    private BigDecimal amount;
    private String fromAddress;
    private String toAddress;
}
