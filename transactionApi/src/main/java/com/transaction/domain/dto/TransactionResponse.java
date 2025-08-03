package com.transaction.domain.dto;

import com.transaction.domain.enums.TransactionStatus;
import com.transaction.domain.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para respostas de transações
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    
    private String transactionId;         // UUID do ExchangeApi
    private TransactionType type;
    private TransactionStatus status;
    private BigDecimal originalAmount;
    private BigDecimal convertedAmount;
    private String fromCurrencyPrefix;
    private String toCurrencyPrefix;
    private BigDecimal exchangeRate;
    
    // Produtos (para EXCHANGE)
    private Long fromProductId;
    private String fromProductName;
    private Long toProductId;
    private String toProductName;
    
    // Reino
    private Long kingdomId;
    private String kingdomName;
    
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
} 