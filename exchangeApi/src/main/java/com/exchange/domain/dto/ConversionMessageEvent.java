package com.exchange.domain.dto;

import com.exchange.domain.enums.TransactionStatus;
import com.exchange.domain.event.GenericApplicationEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversionMessageEvent extends GenericApplicationEvent {
    private UUID eventId;
    private String commandId;
    private String transactionId;
    private String correlationId;
    
    // Dados da conversão
    private BigDecimal convertedAmount;
    private BigDecimal rate;
    private String fromCurrencyCode;
    private String toCurrencyCode;
    
    // Status e resultado
    private TransactionStatus status;
    private String reason;
    private BigDecimal riskScore;
    private List<String> warnings;
    private List<String> recommendations;
    private boolean canProceed;
    private boolean requiresApproval;
    
    // Metadados do evento
    private LocalDateTime processedAt;
    private LocalDateTime expiresAt;
    private String confirmationUrl;
    
    // Construtor para evento de sucesso
    public ConversionMessageEvent(String commandId, String correlationId, ConversionResponse response) {
        this.eventId = UUID.randomUUID();
        this.commandId = commandId;
        this.correlationId = correlationId;
        this.transactionId = response.getTransactionId() != null ? response.getTransactionId().toString() : null;
        
        this.convertedAmount = response.getConvertedAmount();
        this.rate = response.getRate();
        this.fromCurrencyCode = response.getFromCurrencyCode();
        this.toCurrencyCode = response.getToCurrencyCode();
        
        this.status = response.getStatus();
        this.reason = response.getReason();
        this.riskScore = response.getRiskScore();
        this.warnings = response.getWarnings();
        this.recommendations = response.getRecommendations();
        this.canProceed = response.isCanProceed();
        this.requiresApproval = response.isRequiresApproval();
        
        this.processedAt = LocalDateTime.now();
        this.expiresAt = response.getExpiresAt();
        this.confirmationUrl = response.getConfirmationUrl();
    }
    
    // Construtor para evento de erro
    public ConversionMessageEvent(String commandId, String correlationId, String errorMessage,
                                TransactionStatus status) {
        this.eventId = UUID.randomUUID();
        this.commandId = commandId;
        this.correlationId = correlationId;
        this.status = status;
        this.reason = errorMessage;
        this.canProceed = false;
        this.processedAt = LocalDateTime.now();
    }
} 