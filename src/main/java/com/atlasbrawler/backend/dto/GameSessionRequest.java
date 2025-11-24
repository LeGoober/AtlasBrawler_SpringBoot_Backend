package com.atlasbrawler.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GameSessionRequest {
    
    @NotBlank(message = "Wallet address is required")
    private String walletAddress;
    
    @NotNull(message = "Score is required")
    @Min(value = 0, message = "Score must be positive")
    private Integer score;
    
    @NotNull(message = "Waves survived is required")
    @Min(value = 0, message = "Waves survived must be positive")
    private Integer wavesSurvived;
    
    private Boolean isWin;
}
