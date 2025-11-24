package com.atlasbrawler.backend.service;

import com.atlasbrawler.backend.domain.Player;
import com.atlasbrawler.backend.domain.Reward;
import com.atlasbrawler.backend.domain.enums.RewardStatus;
import com.atlasbrawler.backend.domain.enums.RewardType;
import com.atlasbrawler.backend.dto.GameSessionRequest;
import com.atlasbrawler.backend.dto.RewardClaimRequest;
import com.atlasbrawler.backend.repository.PlayerRepository;
import com.atlasbrawler.backend.repository.RewardRepository;
import com.atlasbrawler.backend.util.SignatureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RewardService {
    
    private static final Logger logger = LoggerFactory.getLogger(RewardService.class);
    private static final BigDecimal SOFT_TOKEN_PER_WAVE = new BigDecimal("10");
    private static final BigDecimal CUSD_REWARD_BASE = new BigDecimal("0.01");
    
    private final RewardRepository rewardRepository;
    private final PlayerRepository playerRepository;
    private final SignatureUtil signatureUtil;
    private final BlockchainService blockchainService;
    
    public RewardService(RewardRepository rewardRepository,
                        PlayerRepository playerRepository,
                        SignatureUtil signatureUtil,
                        BlockchainService blockchainService) {
        this.rewardRepository = rewardRepository;
        this.playerRepository = playerRepository;
        this.signatureUtil = signatureUtil;
        this.blockchainService = blockchainService;
    }
    
    @Transactional
    public Reward processGameSession(GameSessionRequest request) {
        Player player = playerRepository.findByWalletAddress(request.getWalletAddress())
            .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        
        player.incrementGamesPlayed();
        
        if (Boolean.TRUE.equals(request.getIsWin())) {
            player.incrementWins();
        }
        
        if (request.getScore() > player.getHighScore()) {
            player.setHighScore(request.getScore());
        }
        
        BigDecimal softTokenReward = SOFT_TOKEN_PER_WAVE.multiply(
            new BigDecimal(request.getWavesSurvived())
        );
        player.addSoftTokens(softTokenReward);
        
        Reward reward = Reward.builder()
            .player(player)
            .rewardType(RewardType.WAVE_SURVIVAL)
            .amount(CUSD_REWARD_BASE)
            .status(RewardStatus.PENDING)
            .wavesSurvived(request.getWavesSurvived())
            .score(request.getScore())
            .reason("Wave survival reward")
            .build();
        
        playerRepository.save(player);
        reward = rewardRepository.save(reward);
        
        logger.info("Created reward {} for player {}", reward.getId(), player.getWalletAddress());
        
        return reward;
    }
    
    @Transactional
    public Reward claimReward(RewardClaimRequest request) {
        Reward reward = rewardRepository.findById(request.getRewardId())
            .orElseThrow(() -> new IllegalArgumentException("Reward not found"));
        
        if (reward.getStatus() != RewardStatus.PENDING) {
            throw new IllegalStateException("Reward already claimed or invalid");
        }
        
        if (!reward.getPlayer().getWalletAddress().equalsIgnoreCase(request.getWalletAddress())) {
            throw new SecurityException("Unauthorized claim attempt");
        }
        
        if (!signatureUtil.verifySignature(
            "Claim reward: " + request.getRewardId(),
            request.getSignature(),
            request.getWalletAddress())) {
            throw new SecurityException("Invalid signature");
        }
        
        try {
            String txHash = blockchainService.transferReward(
                request.getWalletAddress(),
                reward.getAmount()
            );
            
            reward.markAsClaimed(txHash);
            reward = rewardRepository.save(reward);
            
            logger.info("Reward {} claimed successfully. TX: {}", reward.getId(), txHash);
            
            return reward;
        } catch (Exception e) {
            logger.error("Failed to claim reward {}", request.getRewardId(), e);
            reward.markAsFailed();
            rewardRepository.save(reward);
            throw new RuntimeException("Failed to process claim", e);
        }
    }
    
    public List<Reward> getPendingRewards(String walletAddress) {
        Player player = playerRepository.findByWalletAddress(walletAddress)
            .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        return rewardRepository.findByPlayerIdAndStatus(player.getId(), RewardStatus.PENDING);
    }
}
