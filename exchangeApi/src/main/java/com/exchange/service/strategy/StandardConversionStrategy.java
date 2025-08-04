package com.exchange.service.strategy;

import com.exchange.domain.dto.ConversionRequest;
import com.exchange.domain.dto.ConversionResponse;
import com.exchange.domain.entity.Currency;
import com.exchange.domain.entity.ExchangeRate;
import com.exchange.domain.enums.TransactionStatus;
import com.exchange.service.ExchangeRateService;
import com.exchange.service.ProductApiService;
import com.exchange.service.RiskAnalysisService;
import com.exchange.service.impl.RiskAnalysisServiceImpl;
import com.exchange.util.ConversionCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class StandardConversionStrategy implements ConversionStrategy {
    
    @Autowired
    private ExchangeRateService exchangeRateService;
    
    @Autowired
    private RiskAnalysisServiceImpl riskAnalysisService;
    
    @Autowired
    private ProductApiService productApiService;
    
    @Autowired
    private ConversionCalculator conversionCalculator;
    
    @Override
    public ConversionResponse convert(ConversionRequest request) {
        // Buscar informações do reino
        var kingdomInfo = productApiService.getKingdomInfo(request.getKingdomId());
        if (kingdomInfo == null) {
            return createErrorResponse("Reino não encontrado: " + request.getKingdomId());
        }
        
        // Buscar taxa de câmbio
        ExchangeRate exchangeRate = exchangeRateService.getActiveRate(
            request.getFromCurrencyCode(), 
            request.getToCurrencyCode()
        );
        
        if (exchangeRate == null) {
            return createErrorResponse("Taxa de câmbio não encontrada para o período solicitado");
        }
        
        // Calcular conversão usando o utilitário
        BigDecimal originalQuantity = BigDecimal.valueOf(request.getQuantityCurrency());
        BigDecimal convertedAmount = conversionCalculator.calculateCurrencyConversion(
            originalQuantity, exchangeRate, kingdomInfo);
        
        // Criar response básico
        ConversionResponse response = new ConversionResponse(
            convertedAmount,
            exchangeRate.getRate(),
            request.getFromCurrencyCode(),
            request.getToCurrencyCode()
        );
        
        // Aplicar análise de risco
        riskAnalysisService.analyzeRisk(response, exchangeRate, request);
        
        return response;
    }
    
    @Override
    public boolean supports(ConversionRequest request) {
        // Estratégia padrão suporta apenas conversões de moeda
        // (sem produto específico)
        return request.getProductId() == null || Long.valueOf(0).equals(request.getProductId());
    }

    private ConversionResponse createErrorResponse(String reason) {
        ConversionResponse response = new ConversionResponse();
        response.setStatus(TransactionStatus.NOT_APPROVED);
        response.setReason(reason);
        response.setCanProceed(false);
        response.setWarnings(List.of(reason));
        response.setRecommendations(List.of("Verifique os parâmetros da conversão"));
        return response;
    }
} 