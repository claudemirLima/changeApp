package com.exchange.domain.dto;

import com.exchange.domain.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateResponse {
    private CurrencyInfo fromCurrency;
    private CurrencyInfo toCurrency;
    private BigDecimal rate;
    private LocalDate effectiveDate;
    private LocalDateTime lastUpdated;
    
    // Guard Rail
    private TransactionStatus status;
    private String reason;
    private BigDecimal riskScore;
    private List<String> warnings;
    private List<String> recommendations;
    
    // Construtor customizado
    public ExchangeRateResponse(CurrencyInfo fromCurrency, CurrencyInfo toCurrency, BigDecimal rate, 
                               LocalDate effectiveDate, LocalDateTime lastUpdated) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.rate = rate;
        this.effectiveDate = effectiveDate;
        this.lastUpdated = lastUpdated;
    }
} 