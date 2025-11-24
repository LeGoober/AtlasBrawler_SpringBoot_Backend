package com.atlasbrawler.backend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "skater_cards")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkaterCard {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;
    
    @Column(nullable = false, length = 50)
    @Builder.Default
    private String boardType = "DEFAULT";
    
    @Column(nullable = false)
    @Builder.Default
    private Integer speedStat = 50;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer trickStat = 50;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer balanceStat = 50;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer strengthStat = 50;
    
    @Column(length = 20)
    @Builder.Default
    private String stance = "REGULAR";
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isShareable = true;
    
    @Column(length = 500)
    private String nftTokenUri;
    
    @Column
    private Long nftTokenId;
    
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
    
    public Integer getOverallRating() {
        return (speedStat + trickStat + balanceStat + strengthStat) / 4;
    }
}
