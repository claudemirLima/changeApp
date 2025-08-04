package com.exchange.config;

import com.exchange.domain.command.ConversionCommand;
import com.exchange.domain.dto.ConversionRequest;
import com.exchange.domain.dto.ConversionResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

@Log4j2
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.consumer.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id:exchange-api-group}")
    protected String groupId;

    // Factory genérica para Object (mantida para compatibilidade)
    public ConsumerFactory<String, Object> consumerFactory() {
        final Map<String, Object> configProps = new HashMap<>();

        final JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("*");

        configProps.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(GROUP_ID_CONFIG, groupId);
        configProps.put(MAX_POLL_RECORDS_CONFIG, "1");
        configProps.put(SESSION_TIMEOUT_MS_CONFIG, "60000");
        configProps.put(PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.RoundRobinAssignor");

        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());

        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), jsonDeserializer);
    }

    // Factory específica para ConversionCommand
    public ConsumerFactory<String, ConversionCommand> conversionCommandConsumerFactory() {
        final Map<String, Object> configProps = new HashMap<>();

        final JsonDeserializer<ConversionCommand> jsonDeserializer = new JsonDeserializer<>(ConversionCommand.class);
        jsonDeserializer.addTrustedPackages("*");

        configProps.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(GROUP_ID_CONFIG, groupId);
        configProps.put(MAX_POLL_RECORDS_CONFIG, "1");
        configProps.put(SESSION_TIMEOUT_MS_CONFIG, "60000");
        configProps.put(PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.RoundRobinAssignor");

        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());

        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), jsonDeserializer);
    }

    // Exemplo: Factory específica para ConversionRequest (se necessário)
    public ConsumerFactory<String, ConversionRequest> conversionRequestConsumerFactory() {
        final Map<String, Object> configProps = new HashMap<>();

        final JsonDeserializer<ConversionRequest> jsonDeserializer = new JsonDeserializer<>(ConversionRequest.class);
        jsonDeserializer.addTrustedPackages("*");

        configProps.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(GROUP_ID_CONFIG, groupId);
        configProps.put(MAX_POLL_RECORDS_CONFIG, "1");
        configProps.put(SESSION_TIMEOUT_MS_CONFIG, "60000");
        configProps.put(PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.RoundRobinAssignor");

        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());

        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), jsonDeserializer);
    }

    // Método genérico para criar factories específicas para qualquer tipo
    public <T> ConsumerFactory<String, T> createTypedConsumerFactory(Class<T> targetType) {
        final Map<String, Object> configProps = new HashMap<>();

        final JsonDeserializer<T> jsonDeserializer = new JsonDeserializer<>(targetType);
        jsonDeserializer.addTrustedPackages("*");

        configProps.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(GROUP_ID_CONFIG, groupId);
        configProps.put(MAX_POLL_RECORDS_CONFIG, "1");
        configProps.put(SESSION_TIMEOUT_MS_CONFIG, "60000");
        configProps.put(PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.RoundRobinAssignor");

        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());

        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConcurrency(1);
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(false);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ConversionCommand> conversionCommandListenerContainerFactory() {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, ConversionCommand>();
        factory.setConcurrency(1);
        factory.setConsumerFactory(conversionCommandConsumerFactory());
        factory.setBatchListener(false);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ConversionRequest> conversionRequestListenerContainerFactory() {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, ConversionRequest>();
        factory.setConcurrency(1);
        factory.setConsumerFactory(conversionRequestConsumerFactory());
        factory.setBatchListener(false);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    // Método genérico para criar container factories específicas
    public <T> ConcurrentKafkaListenerContainerFactory<String, T> createTypedListenerContainerFactory(Class<T> targetType) {
        final var factory = new ConcurrentKafkaListenerContainerFactory<String, T>();
        factory.setConcurrency(1);
        factory.setConsumerFactory(createTypedConsumerFactory(targetType));
        factory.setBatchListener(false);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }
} 