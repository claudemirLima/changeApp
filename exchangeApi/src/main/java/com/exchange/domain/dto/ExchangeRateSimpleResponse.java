package com.exchange.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO simplificado para resposta de ExchangeRate
 * Sem campos de guard rail (status, riskScore, etc.)
 */
public class ExchangeRateSimpleResponse {
    
    private CurrencyInfo fromCurrency;
    private CurrencyInfo toCurrency;
    private BigDecimal rate;
    private LocalDateTime lastUpdated;
    
    // Construtores
    public ExchangeRateSimpleResponse() {}
    
    public ExchangeRateSimpleResponse(CurrencyInfo fromCurrency, CurrencyInfo toCurrency, 
                                    BigDecimal rate, LocalDateTime lastUpdated) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.rate = rate;
        this.lastUpdated = lastUpdated;
    }
    
    // Getters e Setters
    public CurrencyInfo getFromCurrency() {
        return fromCurrency;
    }
    
    public void setFromCurrency(CurrencyInfo fromCurrency) {
        this.fromCurrency = fromCurrency;
    }
    
    public CurrencyInfo getToCurrency() {
        return toCurrency;
    }
    
    public void setToCurrency(CurrencyInfo toCurrency) {
        this.toCurrency = toCurrency;
    }
    
    public BigDecimal getRate() {
        return rate;
    }
    
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    @Override
    public String toString() {
        return "ExchangeRateSimpleResponse{" +
                "fromCurrency=" + fromCurrency +
                ", toCurrency=" + toCurrency +
                ", rate=" + rate +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
} 