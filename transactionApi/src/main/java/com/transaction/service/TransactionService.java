package com.transaction.service;

import com.transaction.domain.dto.NewTransactionRequest;
import com.transaction.domain.dto.TransactionRequest;
import com.transaction.domain.dto.TransactionResponse;
import com.transaction.domain.entity.Transaction;
import com.transaction.domain.enums.TransactionStatus;
import com.transaction.domain.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface para serviços de transações
 */
public interface TransactionService {
    
    // CRUD básico
    TransactionResponse createTransaction(NewTransactionRequest request);
    Optional<TransactionResponse> getTransactionById(String transactionId);
    List<TransactionResponse> getAllTransactions();
    Page<TransactionResponse> getAllTransactions(Pageable pageable);
    TransactionResponse updateTransaction(String transactionId, TransactionRequest request);
    void deleteTransaction(String transactionId);
    
    // Buscar por status
    List<TransactionResponse> getTransactionsByStatus(TransactionStatus status);
    Page<TransactionResponse> getTransactionsByStatus(TransactionStatus status, Pageable pageable);
    
    // Buscar por tipo
    List<TransactionResponse> getTransactionsByType(TransactionType type);
    Page<TransactionResponse> getTransactionsByType(TransactionType type, Pageable pageable);
    
    // Buscar por tipo e status
    List<TransactionResponse> getTransactionsByTypeAndStatus(TransactionType type, TransactionStatus status);
    Page<TransactionResponse> getTransactionsByTypeAndStatus(TransactionType type, TransactionStatus status, Pageable pageable);
    
    // Buscar por moeda
    List<TransactionResponse> getTransactionsByFromCurrency(String fromCurrencyPrefix);
    List<TransactionResponse> getTransactionsByToCurrency(String toCurrencyPrefix);
    
    // Buscar por reino
    List<TransactionResponse> getTransactionsByKingdom(Long kingdomId);
    Page<TransactionResponse> getTransactionsByKingdom(Long kingdomId, Pageable pageable);
    
    // Buscar por produto
    List<TransactionResponse> getTransactionsByFromProduct(Long fromProductId);
    List<TransactionResponse> getTransactionsByToProduct(Long toProductId);
    
    // Buscar por período
    List<TransactionResponse> getTransactionsByPeriod(LocalDateTime startDate, LocalDateTime endDate);
    Page<TransactionResponse> getTransactionsByPeriod(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // Buscar por valor
    List<TransactionResponse> getTransactionsByAmountRange(BigDecimal minAmount, BigDecimal maxAmount);
    List<TransactionResponse> getTransactionsByMinAmount(BigDecimal minAmount);
    List<TransactionResponse> getTransactionsByMaxAmount(BigDecimal maxAmount);
    
    // Ações de status
    TransactionResponse approveTransaction(String transactionId);
    TransactionResponse rejectTransaction(String transactionId);
    TransactionResponse completeTransaction(String transactionId);
    
    // Validações
    boolean existsTransaction(String transactionId);
    boolean isValidTransaction(String transactionId);
    
    // Estatísticas
    long countTransactionsByStatus(TransactionStatus status);
    long countTransactionsByType(TransactionType type);
    long countTransactionsByKingdom(Long kingdomId);
    BigDecimal getTotalVolumeByPeriod(LocalDateTime startDate, LocalDateTime endDate);
    BigDecimal getAverageAmountByPeriod(LocalDateTime startDate, LocalDateTime endDate);
} 