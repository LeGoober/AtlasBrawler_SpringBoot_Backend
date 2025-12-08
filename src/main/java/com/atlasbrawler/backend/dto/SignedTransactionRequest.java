package com.atlasbrawler.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignedTransactionRequest {

    @NotBlank(message = "Wallet address is required")
    @Pattern(regexp = "^0x[0-9a-fA-F]{40}$", message = "Invalid Ethereum address format")
    private String walletAddress;

    @NotBlank(message = "Signed transaction is required")
    private String signedTransaction;
}
