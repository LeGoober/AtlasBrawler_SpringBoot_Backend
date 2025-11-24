package com.atlasbrawler.backend.factory;

import com.atlasbrawler.backend.domain.Player;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PlayerFactory {
    
    public Player createDefaultPlayer(String walletAddress, String username) {
        return Player.builder()
            .walletAddress(walletAddress)
            .username(username)
            .softTokenBalance(BigDecimal.ZERO)
            .cUSDBalance(BigDecimal.ZERO)
            .totalGamesPlayed(0)
            .totalWins(0)
            .highScore(0)
            .build();
    }
}
