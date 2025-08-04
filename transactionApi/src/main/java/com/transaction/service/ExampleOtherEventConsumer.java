package com.transaction.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * EXEMPLO: Consumer para outros tipos de eventos usando factory específica.
 * 
 * Este é um exemplo de como você pode criar consumers para outros tipos
 * de eventos usando a mesma abordagem de factories específicas.
 * 
 * Para usar este padrão:
 * 1. Crie a classe do evento (ex: TransactionEvent, PaymentEvent, etc.)
 * 2. Adicione a factory no KafkaConsumerConfig
 * 3. Crie o @Bean no KafkaConsumerConfig
 * 4. Use o containerFactory específico no @KafkaListener
 */
@Service
@Slf4j
public class ExampleOtherEventConsumer {

    /**
     * EXEMPLO: Listener para TransactionEvent usando factory específica.
     * 
     * containerFactory = "transactionEventListenerContainerFactory"
     * -> Usa a factory específica para TransactionEvent
     * 
     * @Payload TransactionEvent event -> Recebe o objeto diretamente tipado
     */
    /*
    @KafkaListener(
        topics = "transaction-events",
        groupId = "transaction-api-group",
        containerFactory = "transactionEventListenerContainerFactory"
    )
    public void handleTransactionEvent(
            @Payload TransactionEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.OFFSET) Long offset,
            Acknowledgment ack) {
        
        try {
            log.info("Recebido evento de transação da partição {} offset {}", partition, offset);
            log.info("Evento recebido: {}", event);
            
            // Processar o evento diretamente
            // Não precisa fazer conversão manual!
            
            ack.acknowledge();
            
            log.info("Evento de transação processado com sucesso");
            
        } catch (Exception e) {
            log.error("Erro ao processar evento de transação", e);
        }
    }
    */

    /**
     * EXEMPLO: Como você pode criar um listener para um evento customizado.
     * 
     * Para implementar este exemplo:
     * 1. Crie a classe PaymentEvent
     * 2. Adicione no KafkaConsumerConfig:
     *    - paymentEventConsumerFactory()
     *    - paymentEventListenerContainerFactory()
     * 3. Use o containerFactory específico
     */
    /*
    @KafkaListener(
        topics = "payment-events",
        groupId = "transaction-api-group",
        containerFactory = "paymentEventListenerContainerFactory"
    )
    public void handlePaymentEvent(
            @Payload PaymentEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
            @Header(KafkaHeaders.OFFSET) Long offset,
            Acknowledgment ack) {
        
        try {
            log.info("Recebido evento de pagamento: {}", event);
            
            // Processar o evento diretamente
            // Não precisa fazer conversão manual!
            
            ack.acknowledge();
            
        } catch (Exception e) {
            log.error("Erro ao processar evento de pagamento", e);
        }
    }
    */
} 