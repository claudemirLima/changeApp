package com.transaction.service.impl;

import com.transaction.domain.dto.TransactionRequest;
import com.transaction.domain.dto.TransactionResponse;
import com.transaction.domain.entity.Transaction;
import com.transaction.domain.enums.TransactionStatus;
import com.transaction.domain.enums.TransactionType;
import com.transaction.domain.mapper.TransactionMapper;
import com.transaction.repository.TransactionRepository;
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
    
    @Override
    public TransactionResponse createTransaction(TransactionRequest request) {
        log.info("Criando transação com ID: {}", request.getTransactionId());
        
        // Verificar se já existe
        if (transactionRepository.existsByTransactionId(request.getTransactionId())) {
            throw new RuntimeException("Transação com ID " + request.getTransactionId() + " já existe");
        }
        
        // Criar entidade usando mapper
        Transaction transaction = TransactionMapper.requestToTransaction(request);
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transação criada com sucesso: {}", savedTransaction.getTransactionId());
        
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
    public Page<TransactionResponse> getTransactionsByStatus(TransactionStatus status, Pageable pageable) {
        log.debug("Buscando transações por status paginadas: {}", status);
        return transactionRepository.findByStatus(status, pageable)
                .map(TransactionMapper::transactionToResponse);
    }
    
    @Override
    public List<TransactionResponse> getTransactionsByType(TransactionType type) {
        log.debug("Buscando transações por tipo: {}", type);
        return transactionRepository.findByType(type).stream()
                .map(TransactionMapper::transactionToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<TransactionResponse> getTransactionsByType(TransactionType type, Pageable pageable) {
        log.debug("Buscando transações por tipo paginadas: {}", type);
        return transactionRepository.findByType(type, pageable)
                .map(TransactionMapper::transactionToResponse);
    }
    
    @Override
    public List<TransactionResponse> getTransactionsByTypeAndStatus(TransactionType type, TransactionStatus status) {
        log.debug("Buscando transações por tipo e status: {} - {}", type, status);
        return transactionRepository.findByTypeAndStatus(type, status).stream()
                .map(TransactionMapper::transactionToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<TransactionResponse> getTransactionsByTypeAndStatus(TransactionType type, TransactionStatus status, Pageable pageable) {
        log.debug("Buscando transações por tipo e status paginadas: {} - {}", type, status);
        return transactionRepository.findByTypeAndStatus(type, status, pageable)
                .map(TransactionMapper::transactionToResponse);
    }
    
    @Override
    public List<TransactionResponse> getTransactionsByFromCurrency(String fromCurrencyPrefix) {
        log.debug("Buscando transações por moeda origem: {}", fromCurrencyPrefix);
        return transactionRepository.findByFromCurrencyPrefix(fromCurrencyPrefix).stream()
                .map(TransactionMapper::transactionToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TransactionResponse> getTransactionsByToCurrency(String toCurrencyPrefix) {
        log.debug("Buscando transações por moeda destino: {}", toCurrencyPrefix);
        return transactionRepository.findByToCurrencyPrefix(toCurrencyPrefix).stream()
                .map(TransactionMapper::transactionToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TransactionResponse> getTransactionsByKingdom(Long kingdomId) {
        log.debug("Buscando transações por reino: {}", kingdomId);
        return transactionRepository.findByKingdomId(kingdomId).stream()
                .map(TransactionMapper::transactionToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<TransactionResponse> getTransactionsByKingdom(Long kingdomId, Pageable pageable) {
        log.debug("Buscando transações por reino paginadas: {}", kingdomId);
        return transactionRepository.findByKingdomId(kingdomId, pageable)
                .map(TransactionMapper::transactionToResponse);
    }
    
    @Override
    public List<TransactionResponse> getTransactionsByFromProduct(Long fromProductId) {
        log.debug("Buscando transações por produto origem: {}", fromProductId);
        return transactionRepository.findByFromProductId(fromProductId).stream()
                .map(TransactionMapper::transactionToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TransactionResponse> getTransactionsByToProduct(Long toProductId) {
        log.debug("Buscando transações por produto destino: {}", toProductId);
        return transactionRepository.findByToProductId(toProductId).stream()
                .map(TransactionMapper::transactionToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TransactionResponse> getTransactionsByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Buscando transações por período: {} até {}", startDate, endDate);
        return transactionRepository.findByCreatedAtBetween(startDate, endDate).stream()
                .map(TransactionMapper::transactionToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<TransactionResponse> getTransactionsByPeriod(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        log.debug("Buscando transações por período paginadas: {} até {}", startDate, endDate);
        return transactionRepository.findByCreatedAtBetween(startDate, endDate, pageable)
                .map(TransactionMapper::transactionToResponse);
    }
    
    @Override
    public List<TransactionResponse> getTransactionsByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        log.debug("Buscando transações por faixa de valor: {} até {}", minAmount, maxAmount);
        return transactionRepository.findByOriginalAmountBetween(minAmount, maxAmount).stream()
                .map(TransactionMapper::transactionToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TransactionResponse> getTransactionsByMinAmount(BigDecimal minAmount) {
        log.debug("Buscando transações por valor mínimo: {}", minAmount);
        return transactionRepository.findByOriginalAmountGreaterThanEqual(minAmount).stream()
                .map(TransactionMapper::transactionToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TransactionResponse> getTransactionsByMaxAmount(BigDecimal maxAmount) {
        log.debug("Buscando transações por valor máximo: {}", maxAmount);
        return transactionRepository.findByOriginalAmountLessThanEqual(maxAmount).stream()
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
    
    @Override
    public boolean existsTransaction(String transactionId) {
        return transactionRepository.existsByTransactionId(transactionId);
    }
    
    @Override
    public boolean isValidTransaction(String transactionId) {
        return transactionRepository.findByTransactionId(transactionId)
                .map(transaction -> TransactionStatus.APPROVED.equals(transaction.getStatus()))
                .orElse(false);
    }
    
    @Override
    public long countTransactionsByStatus(TransactionStatus status) {
        return transactionRepository.countByStatus(status);
    }
    
    @Override
    public long countTransactionsByType(TransactionType type) {
        return transactionRepository.countByType(type);
    }
    
    @Override
    public long countTransactionsByKingdom(Long kingdomId) {
        return transactionRepository.countByKingdomId(kingdomId);
    }
    
    @Override
    public BigDecimal getTotalVolumeByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByCreatedAtBetween(startDate, endDate).stream()
                .map(Transaction::getOriginalAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    @Override
    public BigDecimal getAverageAmountByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = transactionRepository.findByCreatedAtBetween(startDate, endDate);
        
        if (transactions.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal total = transactions.stream()
                .map(Transaction::getOriginalAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return total.divide(BigDecimal.valueOf(transactions.size()), 2, BigDecimal.ROUND_HALF_UP);
    }
} 