package com.transaction.domain.mapper;

import com.transaction.domain.dto.TransactionRequest;
import com.transaction.domain.dto.TransactionResponse;
import com.transaction.domain.entity.Transaction;
import com.transaction.domain.enums.TransactionStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversões entre DTOs e entidades de Transaction
 */
public class TransactionMapper {
    
    /**
     * Converte TransactionRequest para Transaction
     */
    public static Transaction requestToTransaction(TransactionRequest request) {
        if (request == null) {
            return null;
        }
        
        Transaction transaction = new Transaction();
        transaction.setTransactionId(request.getTransactionId());
        transaction.setType(request.getType());
        transaction.setOriginalAmount(request.getOriginalAmount());
        transaction.setFromCurrencyPrefix(request.getFromCurrencyPrefix());
        transaction.setToCurrencyPrefix(request.getToCurrencyPrefix());
        transaction.setExchangeRate(request.getExchangeRate());
        transaction.setFromProductId(request.getFromProductId());
        transaction.setFromProductName(request.getFromProductName());
        transaction.setToProductId(request.getToProductId());
        transaction.setToProductName(request.getToProductName());
        transaction.setKingdomId(request.getKingdomId());
        transaction.setKingdomName(request.getKingdomName());
        transaction.setReason(request.getReason());
        transaction.setStatus(TransactionStatus.REQUESTED);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        
        // Calcular valor convertido
        if (request.getOriginalAmount() != null && request.getExchangeRate() != null) {
            transaction.setConvertedAmount(request.getOriginalAmount().multiply(request.getExchangeRate()));
        }
        
        return transaction;
    }
    
    /**
     * Converte Transaction para TransactionResponse
     */
    public static TransactionResponse transactionToResponse(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        
        TransactionResponse response = new TransactionResponse();
        response.setTransactionId(transaction.getTransactionId());
        response.setType(transaction.getType());
        response.setStatus(transaction.getStatus());
        response.setOriginalAmount(transaction.getOriginalAmount());
        response.setConvertedAmount(transaction.getConvertedAmount());
        response.setFromCurrencyPrefix(transaction.getFromCurrencyPrefix());
        response.setToCurrencyPrefix(transaction.getToCurrencyPrefix());
        response.setExchangeRate(transaction.getExchangeRate());
        response.setFromProductId(transaction.getFromProductId());
        response.setFromProductName(transaction.getFromProductName());
        response.setToProductId(transaction.getToProductId());
        response.setToProductName(transaction.getToProductName());
        response.setKingdomId(transaction.getKingdomId());
        response.setKingdomName(transaction.getKingdomName());
        response.setReason(transaction.getReason());
        response.setCreatedAt(transaction.getCreatedAt());
        response.setCompletedAt(transaction.getCompletedAt());
        
        return response;
    }
    
    /**
     * Converte lista de Transaction para lista de TransactionResponse
     */
    public static List<TransactionResponse> transactionListToResponseList(List<Transaction> transactions) {
        if (transactions == null) {
            return null;
        }
        
        return transactions.stream()
                .map(TransactionMapper::transactionToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Atualiza Transaction com dados do TransactionRequest
     */
    public static void updateTransactionFromRequest(Transaction transaction, TransactionRequest request) {
        if (transaction == null || request == null) {
            return;
        }
        
        transaction.setType(request.getType());
        transaction.setOriginalAmount(request.getOriginalAmount());
        transaction.setFromCurrencyPrefix(request.getFromCurrencyPrefix());
        transaction.setToCurrencyPrefix(request.getToCurrencyPrefix());
        transaction.setExchangeRate(request.getExchangeRate());
        transaction.setFromProductId(request.getFromProductId());
        transaction.setFromProductName(request.getFromProductName());
        transaction.setToProductId(request.getToProductId());
        transaction.setToProductName(request.getToProductName());
        transaction.setKingdomId(request.getKingdomId());
        transaction.setKingdomName(request.getKingdomName());
        transaction.setReason(request.getReason());
        transaction.setUpdatedAt(LocalDateTime.now());
        
        // Recalcular valor convertido
        if (request.getOriginalAmount() != null && request.getExchangeRate() != null) {
            transaction.setConvertedAmount(request.getOriginalAmount().multiply(request.getExchangeRate()));
        }
    }
    
    /**
     * Cria uma cópia de Transaction
     */
    public static Transaction copyTransaction(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        
        Transaction copy = new Transaction();
        copy.setTransactionId(transaction.getTransactionId());
        copy.setType(transaction.getType());
        copy.setStatus(transaction.getStatus());
        copy.setOriginalAmount(transaction.getOriginalAmount());
        copy.setConvertedAmount(transaction.getConvertedAmount());
        copy.setFromCurrencyPrefix(transaction.getFromCurrencyPrefix());
        copy.setToCurrencyPrefix(transaction.getToCurrencyPrefix());
        copy.setExchangeRate(transaction.getExchangeRate());
        copy.setFromProductId(transaction.getFromProductId());
        copy.setFromProductName(transaction.getFromProductName());
        copy.setToProductId(transaction.getToProductId());
        copy.setToProductName(transaction.getToProductName());
        copy.setKingdomId(transaction.getKingdomId());
        copy.setKingdomName(transaction.getKingdomName());
        copy.setReason(transaction.getReason());
        copy.setCreatedAt(transaction.getCreatedAt());
        copy.setUpdatedAt(transaction.getUpdatedAt());
        copy.setCompletedAt(transaction.getCompletedAt());
        
        return copy;
    }
} 