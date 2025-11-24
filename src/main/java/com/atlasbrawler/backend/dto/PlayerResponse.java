package com.atlasbrawler.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PlayerResponse {
    private Long id;
    private String walletAddress;
    private String username;
    private BigDecimal softTokenBalance;
    private BigDecimal cUSDBalance;
    private Integer totalGamesPlayed;
    private Integer totalWins;
    private Integer highScore;
}
