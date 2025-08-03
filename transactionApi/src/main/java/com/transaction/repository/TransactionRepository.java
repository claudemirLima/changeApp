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
    
    // Buscar por tipo
    List<Transaction> findByType(TransactionType type);
    Page<Transaction> findByType(TransactionType type, Pageable pageable);
    
    // Buscar por tipo e status
    List<Transaction> findByTypeAndStatus(TransactionType type, TransactionStatus status);
    Page<Transaction> findByTypeAndStatus(TransactionType type, TransactionStatus status, Pageable pageable);
    
    // Buscar por moeda de origem
    List<Transaction> findByFromCurrencyPrefix(String fromCurrencyPrefix);
    Page<Transaction> findByFromCurrencyPrefix(String fromCurrencyPrefix, Pageable pageable);
    
    // Buscar por moeda de destino
    List<Transaction> findByToCurrencyPrefix(String toCurrencyPrefix);
    Page<Transaction> findByToCurrencyPrefix(String toCurrencyPrefix, Pageable pageable);
    
    // Buscar por reino
    List<Transaction> findByKingdomId(Long kingdomId);
    Page<Transaction> findByKingdomId(Long kingdomId, Pageable pageable);
    
    // Buscar por produto origem
    List<Transaction> findByFromProductId(Long fromProductId);
    
    // Buscar por produto destino
    List<Transaction> findByToProductId(Long toProductId);
    
    // Buscar por período
    @Query("{'createdAt': {$gte: ?0, $lte: ?1}}")
    List<Transaction> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("{'createdAt': {$gte: ?0, $lte: ?1}}")
    Page<Transaction> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // Buscar por valor mínimo
    @Query("{'originalAmount': {$gte: ?0}}")
    List<Transaction> findByOriginalAmountGreaterThanEqual(BigDecimal amount);
    
    // Buscar por valor máximo
    @Query("{'originalAmount': {$lte: ?0}}")
    List<Transaction> findByOriginalAmountLessThanEqual(BigDecimal amount);
    
    // Buscar por valor entre
    @Query("{'originalAmount': {$gte: ?0, $lte: ?1}}")
    List<Transaction> findByOriginalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);
    
    // Verificar se existe por transactionId
    boolean existsByTransactionId(String transactionId);
    
    // Contar por status
    long countByStatus(TransactionStatus status);
    
    // Contar por tipo
    long countByType(TransactionType type);
    
    // Contar por reino
    long countByKingdomId(Long kingdomId);
} 