package com.exchange.service.impl;

import com.exchange.domain.dto.ConversionRequest;
import com.exchange.domain.dto.ConversionResponse;
import com.exchange.domain.dto.TransactionData;
import com.exchange.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {
    
    private static final String TRANSACTION_KEY_PREFIX = "transaction:";
    private static final Duration TTL = Duration.ofMinutes(30);
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Override
    public void saveTransaction(TransactionData transactionData) {
        String key = buildTransactionKey(transactionData.getTransactionId());
        redisTemplate.opsForValue().set(key, transactionData, TTL);
    }
    
    @Override
    public TransactionData getTransaction(UUID transactionId) {
        String key = buildTransactionKey(transactionId);
        return (TransactionData) redisTemplate.opsForValue().get(key);
    }
    
    @Override
    public void deleteTransaction(UUID transactionId) {
        String key = buildTransactionKey(transactionId);
        redisTemplate.delete(key);
    }
    
    @Override
    public boolean existsTransaction(UUID transactionId) {
        String key = buildTransactionKey(transactionId);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
    
    @Override
    public UUID generateTransactionId() {
        return UUID.randomUUID();
    }
    
    @Override
    public UUID createAndSaveTransactionData(ConversionRequest request, ConversionResponse response) {
        UUID transactionId = generateTransactionId();
        TransactionData transactionData = createTransactionData(transactionId, request, response);
        saveTransaction(transactionData);
        return transactionId;
    }
    
    @Override
    public TransactionData createTransactionData(ConversionRequest request, ConversionResponse response) {
        UUID transactionId = generateTransactionId();
        return createTransactionData(transactionId, request, response);
    }
    
    @Override
    public TransactionData createTransactionData(UUID transactionId, ConversionRequest request, ConversionResponse response) {
        return new TransactionData(
            transactionId,
            response.getConvertedAmount(),
            response.getRate(),
            response.getFromCurrencyCode(),
            response.getToCurrencyCode(),
            request.getProductId(),
            response.getStatus(),
            response.getReason(),
            response.getRiskScore(),
            response.getWarnings(),
            response.getRecommendations()
        );
    }
    
    private String buildTransactionKey(UUID transactionId) {
        return TRANSACTION_KEY_PREFIX + transactionId.toString();
    }
} 