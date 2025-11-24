package com.atlasbrawler.backend.controller;

import com.atlasbrawler.backend.domain.Reward;
import com.atlasbrawler.backend.dto.GameSessionRequest;
import com.atlasbrawler.backend.service.RewardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class GameController {
    
    private final RewardService rewardService;
    
    public GameController(RewardService rewardService) {
        this.rewardService = rewardService;
    }
    
    @PostMapping("/session/complete")
    public ResponseEntity<Reward> completeGameSession(
            @Valid @RequestBody GameSessionRequest request) {
        Reward reward = rewardService.processGameSession(request);
        return ResponseEntity.ok(reward);
    }
}
