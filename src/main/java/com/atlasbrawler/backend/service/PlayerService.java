package com.atlasbrawler.backend.service;

import com.atlasbrawler.backend.domain.Player;
import com.atlasbrawler.backend.domain.SkaterCard;
import com.atlasbrawler.backend.dto.PlayerRegistrationRequest;
import com.atlasbrawler.backend.dto.PlayerResponse;
import com.atlasbrawler.backend.repository.PlayerRepository;
import com.atlasbrawler.backend.repository.SkaterCardRepository;
import com.atlasbrawler.backend.util.SignatureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;
    private final SkaterCardRepository skaterCardRepository;
    private final SignatureUtil signatureUtil;

    public PlayerService(PlayerRepository playerRepository,
            SkaterCardRepository skaterCardRepository,
            SignatureUtil signatureUtil) {
        this.playerRepository = playerRepository;
        this.skaterCardRepository = skaterCardRepository;
        this.signatureUtil = signatureUtil;
    }

    @Transactional
    public PlayerResponse registerPlayer(PlayerRegistrationRequest request) {
        if (!signatureUtil.isValidAddress(request.getWalletAddress())) {
            throw new IllegalArgumentException("Invalid wallet address format");
        }

        if (!signatureUtil.verifySignature(request.getMessage(),
                request.getSignature(),
                request.getWalletAddress())) {
            throw new SecurityException("Invalid signature");
        }

        if (playerRepository.existsByWalletAddress(request.getWalletAddress())) {
            throw new IllegalArgumentException("Player already registered");
        }

        Player player = Player.builder()
                .walletAddress(request.getWalletAddress())
                .username(request.getUsername())
                .softTokenBalance(BigDecimal.ZERO)
                .cUSDBalance(BigDecimal.ZERO)
                .totalGamesPlayed(0)
                .totalWins(0)
                .highScore(0)
                .build();

        player = playerRepository.save(player);

        SkaterCard defaultCard = SkaterCard.builder()
                .player(player)
                .boardType("DEFAULT")
                .speedStat(50)
                .trickStat(50)
                .balanceStat(50)
                .strengthStat(50)
                .stance("REGULAR")
                .isShareable(true)
                .build();

        skaterCardRepository.save(defaultCard);

        logger.info("Registered new player: {}", player.getWalletAddress());

        return mapToResponse(player);
    }

    public PlayerResponse getPlayerByWallet(String walletAddress) {
        Player player = playerRepository.findByWalletAddress(walletAddress)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));
        return mapToResponse(player);
    }

    @Transactional
    public void addSoftTokens(String walletAddress, BigDecimal amount) {
        Player player = playerRepository.findByWalletAddress(walletAddress)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
        player.addSoftTokens(amount);
        playerRepository.save(player);
    }

    private PlayerResponse mapToResponse(Player player) {
        return PlayerResponse.builder()
                .id(player.getId())
                .walletAddress(player.getWalletAddress())
                .username(player.getUsername())
                .softTokenBalance(player.getSoftTokenBalance())
                .cUSDBalance(player.getCUSDBalance())
                .totalGamesPlayed(player.getTotalGamesPlayed())
                .totalWins(player.getTotalWins())
                .highScore(player.getHighScore())
                .build();
    }
}
