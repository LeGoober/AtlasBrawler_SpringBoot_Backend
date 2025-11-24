package com.atlasbrawler.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PlayerRegistrationRequest {
    
    @NotBlank(message = "Wallet address is required")
    @Pattern(regexp = "^0x[0-9a-fA-F]{40}$", message = "Invalid Ethereum address format")
    private String walletAddress;
    
    @Size(max = 50, message = "Username must be less than 50 characters")
    private String username;
    
    @NotBlank(message = "Signature is required")
    private String signature;
    
    @NotBlank(message = "Message is required")
    private String message;
}
