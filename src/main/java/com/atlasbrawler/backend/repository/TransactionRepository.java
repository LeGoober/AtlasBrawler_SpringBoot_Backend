package com.atlasbrawler.backend.repository;

import com.atlasbrawler.backend.domain.Transaction;
import com.atlasbrawler.backend.domain.enums.TransactionStatus;
import com.atlasbrawler.backend.domain.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findByPlayerId(Long playerId);
    
    List<Transaction> findByPlayerIdAndStatus(Long playerId, TransactionStatus status);
    
    Optional<Transaction> findByTransactionHash(String transactionHash);
    
    List<Transaction> findByTransactionType(TransactionType transactionType);
    
    List<Transaction> findByStatus(TransactionStatus status);
}
