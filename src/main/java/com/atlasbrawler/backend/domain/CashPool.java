package com.atlasbrawler.backend.domain;

import com.atlasbrawler.backend.domain.enums.CashPoolStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cash_pools")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashPool {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String poolName;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false, precision = 19, scale = 4)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    @Column(nullable = false, precision = 19, scale = 4)
    @Builder.Default
    private BigDecimal targetAmount = BigDecimal.ZERO;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private CashPoolStatus status = CashPoolStatus.ACTIVE;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private Player creator;
    
    @OneToMany(mappedBy = "cashPool", cascade = CascadeType.ALL)
    @Builder.Default
    private List<CashPoolContribution> contributions = new ArrayList<>();
    
    @Column
    private LocalDateTime expiresAt;
    
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
    
    public void addContribution(BigDecimal amount) {
        this.totalAmount = this.totalAmount.add(amount);
    }
    
    public boolean isTargetReached() {
        return this.totalAmount.compareTo(this.targetAmount) >= 0;
    }
}
