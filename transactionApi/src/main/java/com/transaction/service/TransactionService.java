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

    
    // Ações de status
    TransactionResponse approveTransaction(String transactionId);
    TransactionResponse rejectTransaction(String transactionId);
    TransactionResponse completeTransaction(String transactionId);

} 