package com.transaction.domain.dto;

import com.transaction.domain.enums.TransactionStatus;
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
public class ConversionResponse {
    private UUID eventId;
    private UUID commandId;
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
} 