package com.atlasbrawler.backend.repository;

import com.atlasbrawler.backend.domain.CashPoolContribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashPoolContributionRepository extends JpaRepository<CashPoolContribution, Long> {
    
    List<CashPoolContribution> findByCashPoolId(Long cashPoolId);
    
    List<CashPoolContribution> findByContributorId(Long contributorId);
}
