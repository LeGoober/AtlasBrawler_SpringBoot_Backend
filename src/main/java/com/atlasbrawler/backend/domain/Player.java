package com.atlasbrawler.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "players")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Wallet address is required")
    @Column(unique = true, nullable = false, length = 42)
    private String walletAddress;
    
    @Column(length = 50)
    private String username;
    
    @Column(nullable = false)
    @Builder.Default
    private BigDecimal softTokenBalance = BigDecimal.ZERO;
    
    @Column(nullable = false)
    @Builder.Default
    private BigDecimal cUSDBalance = BigDecimal.ZERO;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer totalGamesPlayed = 0;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer totalWins = 0;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer highScore = 0;
    
    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SkaterCard skaterCard;
    
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Transaction> transactions = new ArrayList<>();
    
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Reward> rewards = new ArrayList<>();
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public void addSoftTokens(BigDecimal amount) {
        this.softTokenBalance = this.softTokenBalance.add(amount);
    }

    public void deductSoftTokens(BigDecimal amount) {
        this.softTokenBalance = this.softTokenBalance.subtract(amount);
    }

    public void addCUSDBalance(BigDecimal amount) {
        this.cUSDBalance = this.cUSDBalance.add(amount);
    }

    public void deductCUSDBalance(BigDecimal amount) {
        this.cUSDBalance = this.cUSDBalance.subtract(amount);
    }
    
    public void incrementGamesPlayed() {
        this.totalGamesPlayed++;
    }
    
    public void incrementWins() {
        this.totalWins++;
    }
}
