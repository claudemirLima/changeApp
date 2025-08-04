package com.transaction.service.impl;

import com.transaction.domain.dto.ConversionRequest;
import com.transaction.domain.dto.NewTransactionRequest;
import com.transaction.domain.dto.TransactionRequest;
import com.transaction.domain.dto.TransactionResponse;
import com.transaction.domain.entity.Transaction;
import com.transaction.domain.enums.TransactionStatus;
import com.transaction.domain.enums.TransactionType;
import com.transaction.domain.mapper.TransactionMapper;
import com.transaction.repository.TransactionRepository;
import com.transaction.service.ConversionService;
import com.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de transações
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TransactionServiceImpl implements TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final ConversionService conversionService;
    
    @Override
    public TransactionResponse createTransaction(NewTransactionRequest request) {
        // Criar entidade usando mapper
        Transaction transaction = TransactionMapper.requestTonewTransaction(request);
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transação criada com sucesso: {}", savedTransaction.getTransactionId());

        try {
            ConversionRequest conversionRequest = new ConversionRequest();
            conversionRequest.setFromCurrencyCode(request.getFromCurrencyPrefix());
            conversionRequest.setToCurrencyCode(request.getToCurrencyPrefix());
            conversionRequest.setProductId(request.getFromProductId());
            conversionRequest.setTransactionId(savedTransaction.getTransactionId());


            // Definir quantidade baseada no tipo de transação
            if (request.getQuantityProduct() != null) {
                conversionRequest.setQuantityProduct(request.getQuantityProduct());
            }
            if (request.getQuantityCurrency() != null) {
                conversionRequest.setQuantityCurrency(request.getQuantityCurrency());
            }
            
            // Definir reino
            if (request.getKingdomId() != null) {
                conversionRequest.setKingdomId(request.getKingdomId());
            }

            // Enviar comando de conversão via ConversionService
            conversionService.requestConversion(conversionRequest);

            log.info("Comando de conversão enviado para transação: {}", savedTransaction.getTransactionId());

        } catch (Exception e) {
            log.error("Erro ao enviar comando de conversão para transação: {}", savedTransaction.getTransactionId(), e);
            // Não falha a criação da transação se a conversão falhar
        }

        return TransactionMapper.transactionToResponse(savedTransaction);
    }
    
    @Override
    public Optional<TransactionResponse> getTransactionById(String transactionId) {
        log.debug("Buscando transação por ID: {}", transactionId);
        return transactionRepository.findByTransactionId(transactionId)
                .map(TransactionMapper::transactionToResponse);
    }

    @Override
    public List<TransactionResponse> getAllTransactions() {
        log.debug("Buscando todas as transações");
        return transactionRepository.findAll().stream()
                .map(TransactionMapper::transactionToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<TransactionResponse> getAllTransactions(Pageable pageable) {
        log.debug("Buscando transações paginadas");
        return transactionRepository.findAll(pageable)
                .map(TransactionMapper::transactionToResponse);
    }
    
    @Override
    public TransactionResponse updateTransaction(String transactionId, TransactionRequest request) {
        log.info("Atualizando transação: {}", transactionId);
        
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada: " + transactionId));
        
        // Atualizar campos usando mapper
        TransactionMapper.updateTransactionFromRequest(transaction, request);
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transação atualizada com sucesso: {}", savedTransaction.getTransactionId());
        
        return TransactionMapper.transactionToResponse(savedTransaction);
    }
    
    @Override
    public void deleteTransaction(String transactionId) {
        log.info("Deletando transação: {}", transactionId);
        
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada: " + transactionId));
        
        transactionRepository.delete(transaction);
        log.info("Transação deletada com sucesso: {}", transactionId);
    }
    
    @Override
    public List<TransactionResponse> getTransactionsByStatus(TransactionStatus status) {
        log.debug("Buscando transações por status: {}", status);
        return transactionRepository.findByStatus(status).stream()
                .map(TransactionMapper::transactionToResponse)
                .collect(Collectors.toList());
    }

    
    @Override
    public TransactionResponse approveTransaction(String transactionId) {
        log.info("Aprovando transação: {}", transactionId);
        
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada: " + transactionId));
        
        transaction.approve();
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        log.info("Transação aprovada com sucesso: {}", transactionId);
        return TransactionMapper.transactionToResponse(savedTransaction);
    }
    
    @Override
    public TransactionResponse rejectTransaction(String transactionId) {
        log.info("Rejeitando transação: {}", transactionId);
        
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada: " + transactionId));
        
        transaction.reject();
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        log.info("Transação rejeitada com sucesso: {}", transactionId);
        return TransactionMapper.transactionToResponse(savedTransaction);
    }
    
    @Override
    public TransactionResponse completeTransaction(String transactionId) {
        log.info("Completando transação: {}", transactionId);
        
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada: " + transactionId));
        
        transaction.complete();
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        log.info("Transação completada com sucesso: {}", transactionId);
        return TransactionMapper.transactionToResponse(savedTransaction);
    }

    /**
     * Envia comando de conversão para processamento via Kafka
     */
    public void sendConversionCommand(ConversionRequest conversionRequest) {
        try {
            log.info("Enviando comando de conversão para moedas {} -> {}", 
                    conversionRequest.getFromCurrencyCode(), conversionRequest.getToCurrencyCode());
            
            // Enviar comando de conversão via ConversionService
            conversionService.requestConversion(conversionRequest);
            
            log.info("Comando de conversão enviado com sucesso");
            
        } catch (Exception e) {
            log.error("Erro ao enviar comando de conversão", e);
            throw new RuntimeException("Erro ao enviar comando de conversão: " + e.getMessage());
        }
    }
} 