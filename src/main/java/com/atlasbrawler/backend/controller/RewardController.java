package com.atlasbrawler.backend.controller;

import com.atlasbrawler.backend.domain.Reward;
import com.atlasbrawler.backend.dto.RewardClaimRequest;
import com.atlasbrawler.backend.service.RewardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rewards")
public class RewardController {
    
    private final RewardService rewardService;
    
    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }
    
    @PostMapping("/claim")
    public ResponseEntity<Reward> claimReward(@Valid @RequestBody RewardClaimRequest request) {
        Reward reward = rewardService.claimReward(request);
        return ResponseEntity.ok(reward);
    }
    
    @GetMapping("/pending/{walletAddress}")
    public ResponseEntity<List<Reward>> getPendingRewards(@PathVariable String walletAddress) {
        List<Reward> rewards = rewardService.getPendingRewards(walletAddress);
        return ResponseEntity.ok(rewards);
    }
}
