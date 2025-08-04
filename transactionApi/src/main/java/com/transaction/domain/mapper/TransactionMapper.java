package com.transaction.domain.mapper;

import com.transaction.domain.dto.NewTransactionRequest;
import com.transaction.domain.dto.TransactionRequest;
import com.transaction.domain.dto.TransactionResponse;
import com.transaction.domain.entity.Transaction;
import com.transaction.domain.enums.TransactionStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Mapper para conversões entre DTOs e entidades de Transaction
 */
public class TransactionMapper {

    /**
     * Converte TransactionRequest para Transaction
     */
    public static Transaction requestTonewTransaction(NewTransactionRequest request) {
        if (request == null) {
            return null;
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setType(request.getType());
        transaction.setFromCurrencyPrefix(request.getFromCurrencyPrefix());
        transaction.setToCurrencyPrefix(request.getToCurrencyPrefix());
        transaction.setFromProductId(request.getFromProductId());
        transaction.setToProductId(request.getToProductId());
        transaction.setQuantityProduct(request.getQuantityProduct());
        transaction.setQuantityCurrency(request.getQuantityCurrency());
        transaction.setKingdomId(request.getKingdomId());
        transaction.setKingdomName(request.getKingdomName());
        transaction.setReason(request.getReason());
        transaction.setStatus(TransactionStatus.REQUESTED);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        return transaction;
    }

    /**
     * Converte TransactionRequest para Transaction
     */
    public static Transaction requestToTransaction(TransactionRequest request) {
        if (request == null) {
            return null;
        }
        
        Transaction transaction = new Transaction();
        transaction.setType(request.getType());
        transaction.setFromCurrencyPrefix(request.getFromCurrencyPrefix());
        transaction.setToCurrencyPrefix(request.getToCurrencyPrefix());
        transaction.setExchangeRate(request.getExchangeRate());
        transaction.setQuantityProduct(request.getQuantityProduct());
        transaction.setQuantityCurrency(request.getQuantityCurrency());
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
        response.setConvertedAmount(transaction.getConvertedAmount());
        response.setQuantityProduct(transaction.getQuantityProduct());
        response.setQuantityCurrency(transaction.getQuantityCurrency());
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