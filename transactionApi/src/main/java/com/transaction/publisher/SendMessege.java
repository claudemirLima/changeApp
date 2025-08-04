package com.transaction.publisher;

import com.transaction.domain.command.ConversionCommand;
import com.transaction.domain.dto.ConversionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SendMessege {

    @Autowired
    @Qualifier("conversionCommandKafkaTemplate")
    private KafkaTemplate<String, ConversionCommand> conversionCommandTemplate;

    @Value("${kafka.topic.conversion.command:conversion-commands}")
    private String topicConversionCommand;

    /**
     * Envia comando de conversão usando objeto tipado.
     * 
     * @Qualifier("conversionCommandKafkaTemplate") 
     * -> Usa o template específico para ConversionCommand
     * 
     * @param conversionRequest Request de conversão
     */
    public void sendConversionCommand(ConversionRequest conversionRequest) {
        // Criar o comando tipado diretamente
        ConversionCommand command = ConversionCommand.builder()
                .commandId(UUID.randomUUID())
                .correlationId(UUID.randomUUID().toString())
                .timestamp(LocalDateTime.now().toString())
                .transactionId(conversionRequest.getTransactionId())
                .fromCurrencyCode(conversionRequest.getFromCurrencyCode())
                .toCurrencyCode(conversionRequest.getToCurrencyCode())
                .quantityProduct(conversionRequest.getQuantityProduct())
                .quantityCurrency(conversionRequest.getQuantityCurrency())
                .productId(conversionRequest.getProductId())
                .kingdomId(conversionRequest.getKingdomId())
                .conversionDate(conversionRequest.getConversionDate() != null ? 
                    conversionRequest.getConversionDate().toString() : LocalDateTime.now().toLocalDate().toString())
                .build();

        Message<ConversionCommand> message = MessageBuilder
                .withPayload(command)
                .setHeader(KafkaHeaders.TOPIC, topicConversionCommand)
                .setHeader("eventName", "ConversionCommand")
                .build();
        
        conversionCommandTemplate.send(message);
    }
} 