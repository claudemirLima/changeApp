package com.exchange.service;

import com.exchange.domain.dto.ConversionRequest;
import com.exchange.domain.dto.ConversionResponse;
import com.exchange.domain.dto.TransactionData;
import java.util.UUID;

public interface TransactionService {
    
    /**
     * Salva uma transação no Redis com TTL de 30 minutos
     */
    void saveTransaction(TransactionData transactionData);
    
    /**
     * Busca uma transação pelo UUID
     * Retorna null se não existir (expirada ou não encontrada)
     */
    TransactionData getTransaction(UUID transactionId);
    
    /**
     * Remove uma transação do Redis
     */
    void deleteTransaction(UUID transactionId);
    
    /**
     * Verifica se uma transação existe
     */
    boolean existsTransaction(UUID transactionId);
    
    /**
     * Gera um UUID único para transação
     */
    UUID generateTransactionId();
    
    /**
     * Cria e salva um TransactionData no Redis baseado em uma conversão
     */
    UUID createAndSaveTransactionData(ConversionRequest request, ConversionResponse response);
    
    /**
     * Cria um TransactionData sem salvar no Redis
     */
    TransactionData createTransactionData(ConversionRequest request, ConversionResponse response);
    
    /**
     * Cria um TransactionData com UUID específico
     */
    TransactionData createTransactionData(UUID transactionId, ConversionRequest request, ConversionResponse response);
} 