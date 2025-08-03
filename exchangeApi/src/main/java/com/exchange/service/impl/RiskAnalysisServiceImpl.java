package com.exchange.service.impl;

import com.exchange.domain.dto.ConversionRequest;
import com.exchange.domain.dto.ConversionResponse;
import com.exchange.domain.entity.ExchangeRate;
import com.exchange.domain.enums.TransactionStatus;
import com.exchange.service.RiskAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class RiskAnalysisServiceImpl implements RiskAnalysisService {
    
    @Override
    public void analyzeRisk(ConversionResponse response, ExchangeRate exchangeRate, ConversionRequest request) {
        analyzeRisk(response, exchangeRate.getRate(), request, BigDecimal.ONE);
    }
    
    /**
     * Analisa o risco considerando multiplicadores específicos (produto, reino, etc.)
     */
    public void analyzeRisk(ConversionResponse response, BigDecimal finalRate, ConversionRequest request, BigDecimal multiplier) {
        // Análise básica de risco
        BigDecimal baseRate = getBaseRate(request.getFromCurrencyCode(), request.getToCurrencyCode());
        BigDecimal variation = calculateVariation(baseRate, finalRate);
        BigDecimal riskScore = calculateRiskScore(variation);
        
        // Ajustar risco baseado no multiplicador
        if (multiplier.compareTo(new BigDecimal("1.5")) > 0) {
            riskScore = riskScore.add(new BigDecimal("0.1")); // Aumentar risco para multiplicadores altos
        }
        
        response.setRiskScore(riskScore);
        
        if (riskScore.compareTo(new BigDecimal("0.7")) > 0) {
            setNotApprovedStatus(response, variation, multiplier);
        } else if (riskScore.compareTo(new BigDecimal("0.4")) > 0) {
            setWarningStatus(response, variation, multiplier);
        } else {
            // Transação ok, mas precisa confirmação
            setRequestedStatus(response, request);
        }
    }
    
    @Override
    public BigDecimal calculateRiskScore(BigDecimal baseRate, BigDecimal currentRate) {
        BigDecimal variation = calculateVariation(baseRate, currentRate);
        return variation.abs().min(BigDecimal.ONE);
    }
    
    private void setNotApprovedStatus(ConversionResponse response, BigDecimal variation, BigDecimal multiplier) {
        response.setStatus(TransactionStatus.NOT_APPROVED);
        response.setReason("Taxa muito desfavorável (" + 
                         variation.multiply(new BigDecimal("100")).setScale(1) + "% de variação)");
        response.setCanProceed(false);
        
        List<String> warnings = new ArrayList<>();
        warnings.add("Taxa anormalmente desfavorável");
        if (multiplier.compareTo(BigDecimal.ONE) != 0) {
            warnings.add("Multiplicador aplicado: " + multiplier);
        }
        response.setWarnings(warnings);
        
        response.setRecommendations(List.of("Aguarde melhor momento para conversão"));
    }
    
    private void setWarningStatus(ConversionResponse response, BigDecimal variation, BigDecimal multiplier) {
        response.setStatus(TransactionStatus.WARNING);
        response.setReason("Taxa fora do normal (" + 
                         variation.multiply(new BigDecimal("100")).setScale(1) + "% de variação)");
        response.setCanProceed(true);
        response.setRequiresApproval(true);
        
        List<String> warnings = new ArrayList<>();
        warnings.add("Taxa elevada");
        if (multiplier.compareTo(BigDecimal.ONE) != 0) {
            warnings.add("Multiplicador aplicado: " + multiplier);
        }
        response.setWarnings(warnings);
        
        response.setRecommendations(List.of("Confirme se o valor está correto"));
    }
    
    private void setRequestedStatus(ConversionResponse response, ConversionRequest request) {
        response.setStatus(TransactionStatus.REQUESTED);
        response.setReason("Transação aprovada, aguardando confirmação");
        response.setCanProceed(true);
        response.setRequiresApproval(true);
        
        // TransactionData será criado pelo ConversionService se necessário
        
        response.setWarnings(new ArrayList<>());
        response.setRecommendations(Arrays.asList(
            "Confirme a transação para finalizar",
            "A transação expira em 30 minutos"
        ));
    }
    
    private void setApprovedStatus(ConversionResponse response) {
        response.setStatus(TransactionStatus.APPROVED);
        response.setReason("Taxa dentro dos parâmetros normais");
        response.setCanProceed(true);
        response.setWarnings(new ArrayList<>());
        response.setRecommendations(List.of("Transação recomendada"));
    }
    
    private BigDecimal getBaseRate(String fromCurrency, String toCurrency) {
        // Taxa base: 1 Ouro Real = 2.5 Tibares
        if ("ORO".equals(fromCurrency) && "TIB".equals(toCurrency)) {
            return new BigDecimal("2.5");
        } else if ("TIB".equals(fromCurrency) && "ORO".equals(toCurrency)) {
            return new BigDecimal("0.4");
        }
        return BigDecimal.ONE; // Fallback
    }
    
    private BigDecimal calculateVariation(BigDecimal baseRate, BigDecimal currentRate) {
        if (baseRate.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return currentRate.subtract(baseRate).divide(baseRate, 4, RoundingMode.HALF_UP);
    }
    
    private BigDecimal calculateRiskScore(BigDecimal variation) {
        return variation.abs().min(BigDecimal.ONE);
    }
} 