package com.exchange.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionRequest {
    private String fromCurrencyCode;
    private String toCurrencyCode;
    private BigDecimal amount;
    private Long productId;
    private LocalDate conversionDate;
    
    // Construtor customizado para conversão básica
    public ConversionRequest(String fromCurrencyCode, String toCurrencyCode, BigDecimal amount) {
        this.fromCurrencyCode = fromCurrencyCode;
        this.toCurrencyCode = toCurrencyCode;
        this.amount = amount;
    }
} 