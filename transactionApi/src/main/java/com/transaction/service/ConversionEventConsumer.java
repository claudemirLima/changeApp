package com.transaction.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Map;
import com.transaction.domain.dto.ConversionMessageEvent;

/**
 * Consumer para eventos de conversão usando factory específica.
 * 
 * VANTAGENS desta abordagem:
 * 1. Deserialização automática para o tipo correto (ConversionMessageEvent)
 * 2. Não precisa fazer cast manual ou conversão de Map
 * 3. Type safety em tempo de compilação
 * 4. Melhor performance (sem conversão manual)
 * 5. Código mais limpo e legível
 */
@Service
@Slf4j
public class ConversionEventConsumer {

    /**
     * Consome eventos de resultado de conversão usando factory específica.
     * 
     * containerFactory = "conversionMessageEventListenerContainerFactory" 
     * -> Usa a factory específica para ConversionMessageEvent
     * 
     * @Payload ConversionMessageEvent event -> Recebe o objeto diretamente tipado
     */
    @KafkaListener(
        topics = "conversion-events",
        groupId = "transaction-api-group",
        containerFactory = "conversionMessageEventListenerContainerFactory"
    )
    public void handleConversionEvent(
            @Payload ConversionMessageEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.OFFSET) Long offset,
            Acknowledgment ack) {
        
        try {
            log.info("Recebido evento de conversão: {} da partição {} offset {}", 
                    event.getEventId(), partition, offset);
            log.info("Evento recebido: {}", event);
            
            // Processar o evento de conversão
            processConversionEvent(event);
            
            // Confirmar o processamento
            ack.acknowledge();
            
            log.info("Evento de conversão processado com sucesso: {}", event.getEventId());
            
        } catch (Exception e) {
            log.error("Erro ao processar evento de conversão: {}", event.getEventId(), e);
            // Em caso de erro, não confirma o offset para reprocessamento
            // ack.acknowledge(); // Comentado para permitir reprocessamento
        }
    }
    
    private void processConversionEvent(ConversionMessageEvent event) {
        // Aqui você implementaria a lógica para processar o resultado da conversão
        // Por exemplo:
        // - Atualizar o status da transação
        // - Notificar o usuário
        // - Executar ações baseadas no resultado
        
        String status = event.getStatus().toString();
        Boolean canProceed = event.isCanProceed();
        
        log.info("Processando conversão - Status: {}, Pode prosseguir: {}", status, canProceed);
        
        // TODO: Implementar lógica específica do transactionApi
        // - Atualizar transação no MongoDB
        // - Enviar notificações
        // - Executar ações de negócio
    }
} 