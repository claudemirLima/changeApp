package com.exchange.service;

import com.exchange.domain.dto.ConversionMessageCommand;
import com.exchange.domain.dto.ConversionMessageEvent;
import com.exchange.domain.dto.ConversionRequest;
import com.exchange.domain.enums.TransactionStatus;
import com.exchange.util.SendMessege;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConversionCommandConsumer {

    @Autowired
    private ConversionService conversionService;
    
    @Autowired
    private SendMessege eventProducer;

    /**
     * Consome comandos de conversão e processa de forma assíncrona
     */
    @KafkaListener(
        topics = "conversion-commands",
        groupId = "exchange-api-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleConversionCommand(
            @Payload ConversionMessageCommand command,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.OFFSET) Long offset,
            Acknowledgment ack) {
        
        try {
            log.info("Recebido comando de conversão: {} da partição {} offset {}", 
                    command.getCommandId(), partition, offset);
            
            // Converter o comando para ConversionRequest
            ConversionRequest request = convertToConversionRequest(command);
            
            // Processar a conversão
            var response = conversionService.convert(request);
            
            // Criar e enviar evento de resultado
            ConversionMessageEvent event = new ConversionMessageEvent(
                command.getCommandId(),
                command.getCorrelationId(),
                response
            );
            
            eventProducer.sendConversionEvent(event);
            
            // Confirmar o processamento
            ack.acknowledge();
            
            log.info("Comando de conversão processado com sucesso: {}", command.getCommandId());
            
        } catch (Exception e) {
            log.error("Erro ao processar comando de conversão: {}", command.getCommandId(), e);
            
            // Enviar evento de erro
            try {
                ConversionMessageEvent errorEvent = new ConversionMessageEvent(
                    command.getCommandId(),
                    command.getCorrelationId(),
                    "Erro ao processar conversão: " + e.getMessage(),
                    TransactionStatus.NOT_APPROVED
                );
                
                eventProducer.sendConversionEvent(errorEvent);
            } catch (Exception ex) {
                log.error("Erro ao enviar evento de erro", ex);
            }

        }
    }
    
    private ConversionRequest convertToConversionRequest(ConversionMessageCommand command) {
        ConversionRequest request = new ConversionRequest();
        
        request.setTransactionId(command.getTransactionId());
        request.setFromCurrencyCode(command.getFromCurrencyCode());
        request.setToCurrencyCode(command.getToCurrencyCode());
        request.setQuantityProduct(command.getQuantityProduct());
        request.setQuantityCurrency(command.getQuantityCurrency());
        request.setProductId(command.getProductId());
        request.setKingdomId(command.getKingdomId());
        request.setConversionDate(command.getConversionDate());
        
        return request;
    }
} 