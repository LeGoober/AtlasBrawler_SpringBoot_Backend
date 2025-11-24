package com.atlasbrawler.backend.repository;

import com.atlasbrawler.backend.domain.Reward;
import com.atlasbrawler.backend.domain.enums.RewardStatus;
import com.atlasbrawler.backend.domain.enums.RewardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
    
    List<Reward> findByPlayerIdAndStatus(Long playerId, RewardStatus status);
    
    List<Reward> findByPlayerId(Long playerId);
    
    List<Reward> findByPlayerIdAndRewardType(Long playerId, RewardType rewardType);
    
    List<Reward> findByStatus(RewardStatus status);
}
