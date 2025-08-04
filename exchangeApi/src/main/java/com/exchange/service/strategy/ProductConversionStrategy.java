package com.exchange.service.strategy;

import com.exchange.domain.dto.ConversionRequest;
import com.exchange.domain.dto.ConversionResponse;
import com.exchange.domain.entity.ExchangeRate;
import com.exchange.domain.entity.ProductExchangeRate;
import com.exchange.domain.enums.TransactionStatus;
import com.exchange.service.ExchangeRateService;
import com.exchange.service.ProductExchangeRateService;
import com.exchange.service.ProductApiService;
import com.exchange.service.impl.RiskAnalysisServiceImpl;
import com.exchange.util.ConversionCalculator;
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
    
    @Autowired
    private ProductApiService productApiService;
    
    @Autowired
    private ConversionCalculator conversionCalculator;
    
    @Override
    public ConversionResponse convert(ConversionRequest request) {
        // Buscar informações do produto
        var productInfo = productApiService.getProductInfo(request.getProductId());
        if (productInfo == null) {
            return createErrorResponse("Produto não encontrado: " + request.getProductId());
        }
        
        // Buscar informações do reino
        var kingdomInfo = productApiService.getKingdomInfo(productInfo.getKingdomId());
        if (kingdomInfo == null) {
            return createErrorResponse("Reino não encontrado: " + productInfo.getKingdomId());
        }
        
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
        
        // Calcular conversão usando o utilitário
        BigDecimal originalQuantity = BigDecimal.valueOf(request.getQuantityProduct());
        BigDecimal convertedAmount = conversionCalculator.calculateProductConversion(
            originalQuantity, baseRate, productRate, productInfo, kingdomInfo);
        
        // Criar response
        ConversionResponse response = new ConversionResponse(
            convertedAmount,
            baseRate.getRate(),
            request.getFromCurrencyCode(),
            request.getToCurrencyCode()
        );
        
        // Aplicar análise de risco
        riskAnalysisService.analyzeRisk(response, baseRate.getRate(), request, BigDecimal.ONE);
        
        return response;
    }
    
    @Override
    public boolean supports(ConversionRequest request) {
        // Estratégia de produto suporta apenas conversões com productId
        return request.getProductId() != null && request.getProductId() > 0;
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