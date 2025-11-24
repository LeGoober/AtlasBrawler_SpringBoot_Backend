package com.atlasbrawler.backend.factory;

import com.atlasbrawler.backend.domain.Player;
import com.atlasbrawler.backend.domain.Reward;
import com.atlasbrawler.backend.domain.enums.RewardStatus;
import com.atlasbrawler.backend.domain.enums.RewardType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RewardFactory {
    
    public Reward createWaveSurvivalReward(Player player, Integer wavesSurvived, 
                                          Integer score, BigDecimal amount) {
        return Reward.builder()
            .player(player)
            .rewardType(RewardType.WAVE_SURVIVAL)
            .amount(amount)
            .status(RewardStatus.PENDING)
            .wavesSurvived(wavesSurvived)
            .score(score)
            .reason("Wave survival reward")
            .build();
    }
    
    public Reward createCashPoolReward(Player player, BigDecimal amount) {
        return Reward.builder()
            .player(player)
            .rewardType(RewardType.CASH_POOL)
            .amount(amount)
            .status(RewardStatus.PENDING)
            .reason("Cash pool reward")
            .build();
    }
}
