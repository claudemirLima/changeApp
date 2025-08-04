package com.transaction.service.impl;

import com.transaction.domain.dto.ConversionRequest;
import com.transaction.domain.dto.ConversionResponse;
import com.transaction.service.ConversionService;
import com.transaction.publisher.SendMessege;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class ConversionServiceImpl implements ConversionService {

    @Autowired
    private SendMessege sendCommand;

    @Override
    public ConversionResponse requestConversion(ConversionRequest request) {
        try {
            // Gerar IDs únicos
            UUID commandId = UUID.randomUUID();
            String correlationId = UUID.randomUUID().toString();
            request.setConversionDate(java.time.LocalDate.now());
            // Enviar comando para o Kafka
            sendCommand.sendConversionCommand(request);
            
            // Criar resposta inicial
            ConversionResponse response = new ConversionResponse();
            response.setCommandId(commandId);
            response.setCorrelationId(correlationId);
            response.setStatus(com.transaction.domain.enums.TransactionStatus.REQUESTED);
            

            
            log.info("Comando de conversão enviado: {} para moedas {} -> {}", 
                    commandId, request.getFromCurrencyCode(), request.getToCurrencyCode());
            
            return response;
            
        } catch (Exception e) {
            log.error("Erro ao enviar comando de conversão", e);
            throw new RuntimeException("Erro ao processar conversão: " + e.getMessage());
        }
    }

    @Override
    public ConversionResponse getConversionStatus(UUID commandId) {
        // Implementação simplificada sem Redis
        ConversionResponse response = new ConversionResponse();
        response.setCommandId(commandId);
        response.setStatus(com.transaction.domain.enums.TransactionStatus.REQUESTED);
        return response;
    }

    @Override
    public void processConversionEvent(Object event) {
        try {
            if (event instanceof Map) {
                Map<String, Object> eventMap = (Map<String, Object>) event;
                
                String commandIdStr = (String) eventMap.get("commandId");
                if (commandIdStr != null) {
                    UUID commandId = UUID.fromString(commandIdStr);
                    
                    // Criar resposta com dados do evento
                    ConversionResponse response = createResponseFromEvent(eventMap);
                    
                    log.info("Evento de conversão processado: {} - Status: {}", 
                            commandId, response.getStatus());
                }
            }
        } catch (Exception e) {
            log.error("Erro ao processar evento de conversão", e);
        }
    }
    

    
    private ConversionResponse createResponseFromEvent(Map<String, Object> event) {
        ConversionResponse response = new ConversionResponse();
        
        response.setCommandId(UUID.fromString((String) event.get("commandId")));
        response.setCorrelationId((String) event.get("correlationId"));
        response.setStatus(com.transaction.domain.enums.TransactionStatus.APPROVED);
        

        if (event.get("convertedAmount") != null) {
            response.setConvertedAmount(new java.math.BigDecimal(event.get("convertedAmount").toString()));
        }
        if (event.get("rate") != null) {
            response.setRate(new java.math.BigDecimal(event.get("rate").toString()));
        }
        
        response.setFromCurrencyCode((String) event.get("fromCurrencyCode"));
        response.setToCurrencyCode((String) event.get("toCurrencyCode"));
        response.setReason((String) event.get("reason"));
        
        if (event.get("riskScore") != null) {
            response.setRiskScore(new java.math.BigDecimal(event.get("riskScore").toString()));
        }
        
        response.setCanProceed((Boolean) event.get("canProceed"));
        response.setRequiresApproval((Boolean) event.get("requiresApproval"));
        
        return response;
    }
} 