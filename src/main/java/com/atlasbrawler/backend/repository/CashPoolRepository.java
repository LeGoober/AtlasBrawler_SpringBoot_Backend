package com.atlasbrawler.backend.repository;

import com.atlasbrawler.backend.domain.CashPool;
import com.atlasbrawler.backend.domain.enums.CashPoolStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashPoolRepository extends JpaRepository<CashPool, Long> {
    
    List<CashPool> findByCreatorId(Long creatorId);
    
    List<CashPool> findByStatus(CashPoolStatus status);
    
    List<CashPool> findByStatusOrderByCreatedAtDesc(CashPoolStatus status);
}
