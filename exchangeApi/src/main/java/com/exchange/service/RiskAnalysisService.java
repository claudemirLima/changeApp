package com.exchange.service;

import com.exchange.domain.dto.ConversionRequest;
import com.exchange.domain.dto.ConversionResponse;
import com.exchange.domain.entity.ExchangeRate;

public interface RiskAnalysisService {
    
    /**
     * Analisa o risco de uma transação de conversão
     */
    void analyzeRisk(ConversionResponse response, ExchangeRate exchangeRate, ConversionRequest request);
    
    /**
     * Calcula o score de risco baseado na variação da taxa
     */
    java.math.BigDecimal calculateRiskScore(java.math.BigDecimal baseRate, java.math.BigDecimal currentRate);
} 