package com.atlasbrawler.backend.repository;

import com.atlasbrawler.backend.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    
    Optional<Player> findByWalletAddress(String walletAddress);
    
    boolean existsByWalletAddress(String walletAddress);
    
    Optional<Player> findByUsername(String username);
}
