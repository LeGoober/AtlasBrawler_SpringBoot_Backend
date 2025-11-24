package com.atlasbrawler.backend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cash_pool_contributions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashPoolContribution {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cash_pool_id", nullable = false)
    private CashPool cashPool;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contributor_id", nullable = false)
    private Player contributor;
    
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;
    
    @Column(length = 66)
    private String transactionHash;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime contributedAt;
    
    @PrePersist
    protected void onCreate() {
        contributedAt = LocalDateTime.now();
    }
}
