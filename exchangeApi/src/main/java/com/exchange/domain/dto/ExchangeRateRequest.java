package com.exchange.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateRequest {
    private String fromCurrencyCode;
    private String toCurrencyCode;
    private BigDecimal rate;
    private Boolean isActive;
} 