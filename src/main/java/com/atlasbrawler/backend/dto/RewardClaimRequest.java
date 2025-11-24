package com.atlasbrawler.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RewardClaimRequest {
    
    @NotNull(message = "Reward ID is required")
    private Long rewardId;
    
    @NotBlank(message = "Wallet address is required")
    private String walletAddress;
    
    @NotBlank(message = "Signature is required")
    private String signature;
}
