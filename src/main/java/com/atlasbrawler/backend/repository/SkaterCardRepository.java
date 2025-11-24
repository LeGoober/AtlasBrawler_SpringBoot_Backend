package com.atlasbrawler.backend.repository;

import com.atlasbrawler.backend.domain.SkaterCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkaterCardRepository extends JpaRepository<SkaterCard, Long> {
    
    Optional<SkaterCard> findByPlayerId(Long playerId);
    
    Optional<SkaterCard> findByNftTokenId(Long nftTokenId);
}
