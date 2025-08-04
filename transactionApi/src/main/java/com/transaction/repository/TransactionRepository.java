package com.transaction.repository;

import com.transaction.domain.entity.Transaction;
import com.transaction.domain.enums.TransactionStatus;
import com.transaction.domain.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

/**
 * Repository para operações com transações no MongoDB
 */
@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    
    // Buscar por transactionId
    Optional<Transaction> findByTransactionId(String transactionId);
    
    // Buscar por status
    List<Transaction> findByStatus(TransactionStatus status);
    Page<Transaction> findByStatus(TransactionStatus status, Pageable pageable);

} 