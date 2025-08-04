package com.exchange.util;

import com.exchange.domain.command.ConversionCommand;
import com.exchange.domain.dto.ConversionMessageEvent;
import com.exchange.domain.dto.ConversionRequest;
import com.exchange.domain.event.GenericApplicationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SendMessege {

    @Autowired
    private KafkaTemplate<String, Object> template;

    @Value("${kafka.topic.conversion.command:conversion-commands}")
    private String topicConversionCommand;

    @Value("${kafka.topic.conversion.event:conversion-events}")
    private String topicConversionEvent;

    public void sendConversionCommand(ConversionRequest conversionRequest) {
        var command = ConversionCommand.builder()
                .commandId(UUID.randomUUID().toString())
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
        
        template.send(message);
    }

    public void sendConversionEvent(ConversionMessageEvent event) {
        Message<ConversionMessageEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, topicConversionEvent)
                .setHeader("eventName", event.getEventName())
                .build();

        template.send(message);
    }
} 