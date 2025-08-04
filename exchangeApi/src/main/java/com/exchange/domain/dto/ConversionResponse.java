package com.exchange.domain.dto;

import com.exchange.domain.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionResponse {
    private BigDecimal convertedAmount;
    private BigDecimal rate;
    private String fromCurrencyCode;
    private String toCurrencyCode;
    
    // Guard Rail
    private TransactionStatus status;
    private String reason;
    private BigDecimal riskScore;
    private List<String> warnings;
    private List<String> recommendations;
    
    // Ações disponíveis
    private boolean canProceed;
    private boolean requiresApproval;
    
    // Para transações REQUESTED
    private UUID transactionId;
    private LocalDateTime expiresAt;
    private String confirmationUrl;
    
    // Construtor customizado para conversão básica
    public ConversionResponse(BigDecimal convertedAmount, BigDecimal rate,
                            String fromCurrencyCode, String toCurrencyCode) {
        this.convertedAmount = convertedAmount;
        this.rate = rate;
        this.fromCurrencyCode = fromCurrencyCode;
        this.toCurrencyCode = toCurrencyCode;
    }
} 