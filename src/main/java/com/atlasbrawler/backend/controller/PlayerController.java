package com.atlasbrawler.backend.controller;

import com.atlasbrawler.backend.dto.PlayerRegistrationRequest;
import com.atlasbrawler.backend.dto.PlayerResponse;
import com.atlasbrawler.backend.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/players")
public class PlayerController {
    
    private final PlayerService playerService;
    
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<PlayerResponse> registerPlayer(
            @Valid @RequestBody PlayerRegistrationRequest request) {
        PlayerResponse response = playerService.registerPlayer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{walletAddress}")
    public ResponseEntity<PlayerResponse> getPlayer(@PathVariable String walletAddress) {
        PlayerResponse response = playerService.getPlayerByWallet(walletAddress);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{walletAddress}/balance")
    public ResponseEntity<PlayerResponse> getBalance(@PathVariable String walletAddress) {
        PlayerResponse response = playerService.getPlayerByWallet(walletAddress);
        return ResponseEntity.ok(response);
    }
}
