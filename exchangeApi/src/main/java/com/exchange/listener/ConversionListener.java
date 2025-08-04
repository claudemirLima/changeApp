package com.exchange.listener;

import com.exchange.domain.command.ConversionCommand;
import com.exchange.domain.dto.ConversionMessageEvent;
import com.exchange.domain.event.GenericApplicationEvent;
import com.exchange.service.ConversionService;
import com.exchange.util.SendMessege;
import com.exchange.domain.dto.ConversionRequest;
import com.exchange.domain.dto.ConversionResponse;
import com.exchange.domain.enums.TransactionStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Listener para comandos de conversão usando factory específica.
 * 
 * VANTAGENS desta abordagem:
 * 1. Deserialização automática para o tipo correto (ConversionCommand)
 * 2. Não precisa fazer cast manual ou conversão de Map
 * 3. Type safety em tempo de compilação
 * 4. Melhor performance (sem conversão manual)
 * 5. Código mais limpo e legível
 * 
 * Para criar factories para outros tipos:
 * 1. Adicione o método no KafkaConsumerConfig
 * 2. Crie o @Bean correspondente
 * 3. Use o containerFactory específico no @KafkaListener
 */
@Service
@Log4j2
public class ConversionListener {
    
    @Autowired
    private ConversionService conversionService;
    
    @Autowired
    private SendMessege sendCommand;

    /**
     * Listener que recebe diretamente ConversionCommand sem conversão manual.
     *
     */
    @KafkaListener(
        topics = "conversion-commands",
        groupId = "exchange-api-group",
        containerFactory = "conversionCommandListenerContainerFactory"
    )
    public void handleConversionCommand(@Payload ConversionCommand command, 
                                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                                       @Header(KafkaHeaders.OFFSET) Long offset,
                                       Acknowledgment ack) {
        
        try {
            log.info("Recebido comando de conversão da partição {} offset {}", partition, offset);
            log.info("Comando recebido: {}", command);
            
            // Converter para ConversionRequest
            ConversionRequest request = convertToConversionRequest(command);
            
            // Processar a conversão
            ConversionResponse response = conversionService.convert(request);
            
            // Criar evento de resultado
            ConversionMessageEvent event = createConversionEvent(command, response);
            
            // Enviar evento
            sendCommand.sendConversionEvent(event);
            
            // Confirmar processamento
            ack.acknowledge();
            
            log.info("Comando de conversão processado com sucesso: {}", command.getCommandId());
            
        } catch (Exception e) {
            log.error("Erro ao processar comando de conversão", e);
            
            // Enviar evento de erro
            try {
                ConversionMessageEvent errorEvent = createErrorEvent(command, e.getMessage());
                sendCommand.sendConversionEvent(errorEvent);
            } catch (Exception ex) {
                log.error("Erro ao enviar evento de erro", ex);
            }
        }
    }
    
    private ConversionRequest convertToConversionRequest(ConversionCommand command) {
        ConversionRequest request = new ConversionRequest();
        request.setTransactionId(command.getTransactionId());
        request.setFromCurrencyCode(command.getFromCurrencyCode());
        request.setToCurrencyCode(command.getToCurrencyCode());
        request.setQuantityProduct(command.getQuantityProduct());
        request.setQuantityCurrency(command.getQuantityCurrency());
        request.setProductId(command.getProductId());
        request.setKingdomId(command.getKingdomId());
        // Converter string para LocalDate se necessário
        return request;
    }
    
    private ConversionMessageEvent createConversionEvent(ConversionCommand command, ConversionResponse response) {
        return new ConversionMessageEvent(command.getCommandId(), command.getCorrelationId(), response);
    }
    
    private ConversionMessageEvent createErrorEvent(ConversionCommand command, String errorMessage) {
        return new ConversionMessageEvent(command.getCommandId(), command.getCorrelationId(), errorMessage, TransactionStatus.NOT_APPROVED);
    }
} 