package com.exchange.service.strategy;

import com.exchange.domain.dto.ConversionRequest;
import com.exchange.domain.dto.ConversionResponse;

public interface ConversionStrategy {
    
    /**
     * Executa a conversão baseada na estratégia específica
     */
    ConversionResponse convert(ConversionRequest request);
    
    /**
     * Verifica se esta estratégia suporta o tipo de conversão solicitada
     */
    boolean supports(ConversionRequest request);
    
    /**
     * Retorna a prioridade da estratégia (menor número = maior prioridade)
     */
    int getPriority();
} 