package com.exchange.domain.dto;

import com.exchange.domain.enums.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionData {
    
    private UUID transactionId;
    private BigDecimal originalAmount;
    private BigDecimal convertedAmount;
    private BigDecimal rate;
    private String fromCurrencyCode;
    private String toCurrencyCode;
    private Long productId; // opcional
    
    // Guard Rail
    private TransactionStatus status;
    private String reason;
    private BigDecimal riskScore;
    private List<String> warnings;
    private List<String> recommendations;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expiresAt;
    
    // Construtor customizado
    public TransactionData(UUID transactionId, BigDecimal originalAmount, BigDecimal convertedAmount, 
                          BigDecimal rate, String fromCurrencyCode, String toCurrencyCode, 
                          Long productId, TransactionStatus status, String reason, 
                          BigDecimal riskScore, List<String> warnings, List<String> recommendations) {
        this.transactionId = transactionId;
        this.originalAmount = originalAmount;
        this.convertedAmount = convertedAmount;
        this.rate = rate;
        this.fromCurrencyCode = fromCurrencyCode;
        this.toCurrencyCode = toCurrencyCode;
        this.productId = productId;
        this.status = status;
        this.reason = reason;
        this.riskScore = riskScore;
        this.warnings = warnings;
        this.recommendations = recommendations;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusMinutes(30);
    }
} 