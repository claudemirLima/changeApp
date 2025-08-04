package com.transaction.service;

import com.transaction.domain.dto.ConversionRequest;
import com.transaction.domain.dto.ConversionResponse;
import java.util.UUID;

public interface ConversionService {
    
    /**
     * Envia uma requisição de conversão para processamento assíncrono
     */
    ConversionResponse requestConversion(ConversionRequest request);
    
    /**
     * Consulta o status de uma conversão pelo commandId
     */
    ConversionResponse getConversionStatus(UUID commandId);
    
    /**
     * Processa evento de resultado de conversão recebido do Kafka
     */
    void processConversionEvent(Object event);
} 