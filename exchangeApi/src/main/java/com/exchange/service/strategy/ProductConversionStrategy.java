package com.exchange.service.strategy;

import com.exchange.domain.dto.ConversionRequest;
import com.exchange.domain.dto.ConversionResponse;
import com.exchange.domain.entity.ExchangeRate;
import com.exchange.domain.entity.ProductExchangeRate;
import com.exchange.domain.enums.TransactionStatus;
import com.exchange.service.ExchangeRateService;
import com.exchange.service.ProductExchangeRateService;
import com.exchange.service.impl.RiskAnalysisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class ProductConversionStrategy implements ConversionStrategy {
    
    @Autowired
    private ExchangeRateService exchangeRateService;
    
    @Autowired
    private ProductExchangeRateService productExchangeRateService;
    
    @Autowired
    private RiskAnalysisServiceImpl riskAnalysisService;
    
    @Override
    public ConversionResponse convert(ConversionRequest request) {
        // Buscar taxa de câmbio base
        ExchangeRate baseRate = exchangeRateService.getActiveRate(
            request.getFromCurrencyCode(), 
            request.getToCurrencyCode()
        );
        
        if (baseRate == null) {
            return createErrorResponse("Taxa de câmbio base não encontrada");
        }
        
        // Buscar multiplicador específico do produto
        ProductExchangeRate productRate = productExchangeRateService.getActiveProductRate(
            request.getProductId(),
            request.getFromCurrencyCode(),
            request.getToCurrencyCode()
        );
        
        BigDecimal finalRate = baseRate.getRate();
        BigDecimal productMultiplier = BigDecimal.ONE;
        
        if (productRate != null) {
            finalRate = productRate.getBaseRate();
            productMultiplier = productRate.getProductMultiplier();
        }
        
        // Calcular conversão com multiplicador do produto
        BigDecimal convertedAmount = request.getAmount()
            .multiply(finalRate)
            .multiply(productMultiplier)
            .setScale(2, RoundingMode.HALF_UP);
        
        // Criar response
        ConversionResponse response = new ConversionResponse(
            request.getAmount(),
            convertedAmount,
            finalRate,
            request.getFromCurrencyCode(),
            request.getToCurrencyCode()
        );
        
        // Aplicar análise de risco
        riskAnalysisService.analyzeRisk(response, finalRate, request, productMultiplier);
        
        return response;
    }
    
    @Override
    public boolean supports(ConversionRequest request) {
        // Estratégia de produto suporta conversões com productId
        return request.getProductId() != null && request.getProductId() > 0;
    }
    
    @Override
    public int getPriority() {
        return 50; // Prioridade média - mais específica que a padrão
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