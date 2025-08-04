package com.exchange.service;

import com.exchange.domain.dto.ConversionRequest;
import com.exchange.domain.dto.ConversionResponse;
import com.exchange.domain.enums.TransactionStatus;
import com.exchange.service.TransactionService;
import com.exchange.service.strategy.ConversionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

import java.util.List;

@Service
public class ConversionService {
    
    @Autowired
    private List<ConversionStrategy> conversionStrategies;
    
    @Autowired
    private TransactionService transactionService;
    
    /**
     * Executa a conversão usando a estratégia mais apropriada
     */
    public ConversionResponse convert(ConversionRequest request) {
        // Validar request
        validateRequest(request);
        
        // Encontrar a estratégia mais apropriada
        ConversionStrategy strategy = findBestStrategy(request);
        
        if (strategy == null) {
            return createErrorResponse("Nenhuma estratégia de conversão disponível para os parâmetros fornecidos");
        }
        
        // Executar conversão
        ConversionResponse response = strategy.convert(request);
        
        // Se a conversão resultou em status REQUESTED, criar TransactionData
        if (response.getStatus().equals(TransactionStatus.REQUESTED)){
            UUID transactionId = transactionService.createAndSaveTransactionData(request, response);
            response.setTransactionId(transactionId);
            response.setExpiresAt(java.time.LocalDateTime.now().plusMinutes(30));
            response.setConfirmationUrl("/api/v1/transactions/" + transactionId + "/confirm");
        }
        return response;
    }
    
    /**
     * Encontra a estratégia mais apropriada baseada no suporte
     */
    private ConversionStrategy findBestStrategy(ConversionRequest request) {
        return conversionStrategies.stream()
            .filter(strategy -> strategy.supports(request))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Valida o request de conversão
     */
    private void validateRequest(ConversionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request de conversão não pode ser nulo");
        }
        
        if (request.getFromCurrencyCode() == null || request.getFromCurrencyCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Código da moeda de origem é obrigatório");
        }
        
        if (request.getToCurrencyCode() == null || request.getToCurrencyCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Código da moeda de destino é obrigatório");
        }
        
        if (request.getFromCurrencyCode().equals(request.getToCurrencyCode())) {
            throw new IllegalArgumentException("Moedas de origem e destino não podem ser iguais");
        }
    }
    
    /**
     * Cria response de erro
     */
    private ConversionResponse createErrorResponse(String reason) {
        ConversionResponse response = new ConversionResponse();
        response.setStatus(com.exchange.domain.enums.TransactionStatus.NOT_APPROVED);
        response.setReason(reason);
        response.setCanProceed(false);
        response.setWarnings(java.util.List.of(reason));
        response.setRecommendations(java.util.List.of("Verifique os parâmetros da conversão"));
        return response;
    }
} 