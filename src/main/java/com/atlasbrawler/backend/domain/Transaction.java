package com.atlasbrawler.backend.domain;

import com.atlasbrawler.backend.domain.enums.TransactionStatus;
import com.atlasbrawler.backend.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TransactionType transactionType;
    
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;
    
    @Column(length = 66)
    private String transactionHash;
    
    @Column(length = 42)
    private String fromAddress;
    
    @Column(length = 42)
    private String toAddress;
    
    @Column(length = 500)
    private String description;
    
    @Column
    private Long blockNumber;
    
    @Column
    private Integer gasUsed;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column
    private LocalDateTime confirmedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    public void markAsConfirmed(String txHash, Long blockNum) {
        this.status = TransactionStatus.CONFIRMED;
        this.transactionHash = txHash;
        this.blockNumber = blockNum;
        this.confirmedAt = LocalDateTime.now();
    }
    
    public void markAsFailed() {
        this.status = TransactionStatus.FAILED;
        this.confirmedAt = LocalDateTime.now();
    }
}
