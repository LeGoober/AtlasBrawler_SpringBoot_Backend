package com.atlasbrawler.backend.domain;

import com.atlasbrawler.backend.domain.enums.RewardStatus;
import com.atlasbrawler.backend.domain.enums.RewardType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rewards")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reward {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RewardType rewardType;
    
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private RewardStatus status = RewardStatus.PENDING;
    
    @Column(length = 500)
    private String reason;
    
    @Column(length = 66)
    private String transactionHash;
    
    @Column
    private Integer wavesSurvived;
    
    @Column
    private Integer score;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column
    private LocalDateTime claimedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public void markAsClaimed(String txHash) {
        this.status = RewardStatus.CLAIMED;
        this.transactionHash = txHash;
        this.claimedAt = LocalDateTime.now();
    }
    
    public void markAsFailed() {
        this.status = RewardStatus.FAILED;
    }
}
