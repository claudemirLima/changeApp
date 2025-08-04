package com.transaction.controller;

import com.transaction.domain.dto.TransactionRequest;
import com.transaction.domain.dto.TransactionResponse;
import com.transaction.domain.enums.TransactionStatus;
import com.transaction.domain.enums.TransactionType;
import com.transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller para operações com transações
 */
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Transactions", description = "API para gerenciamento de transações")
public class TransactionController {
    
    private final TransactionService transactionService;
    
    @PostMapping
    @Operation(summary = "Criar nova transação", description = "Cria uma nova transação no sistema")
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionRequest request) {
        log.info("Recebendo requisição para criar transação: {}", request.getTransactionId());
        
        TransactionResponse response = transactionService.createTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{transactionId}")
    @Operation(summary = "Buscar transação por ID", description = "Busca uma transação específica pelo transactionId")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @Parameter(description = "ID da transação") @PathVariable String transactionId) {
        log.debug("Buscando transação por ID: {}", transactionId);
        
        return transactionService.getTransactionById(transactionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @Operation(summary = "Listar transações", description = "Lista todas as transações com paginação e filtros")
    public ResponseEntity<Page<TransactionResponse>> getAllTransactions(
            @Parameter(description = "Número da página") @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(value = "size", defaultValue = "20") int size,
            @Parameter(description = "Campo para ordenação") @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Direção da ordenação") @RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection) {
        
        log.debug("Listando transações - página: {}, tamanho: {}", page, size);
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<TransactionResponse> transactions = transactionService.getAllTransactions(pageable);
        return ResponseEntity.ok(transactions);
    }
    
    @PutMapping("/{transactionId}")
    @Operation(summary = "Atualizar transação", description = "Atualiza uma transação existente")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @Parameter(description = "ID da transação") @PathVariable String transactionId,
            @Valid @RequestBody TransactionRequest request) {
        log.info("Atualizando transação: {}", transactionId);
        
        TransactionResponse response = transactionService.updateTransaction(transactionId, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{transactionId}")
    @Operation(summary = "Deletar transação", description = "Remove uma transação do sistema")
    public ResponseEntity<Void> deleteTransaction(
            @Parameter(description = "ID da transação") @PathVariable String transactionId) {
        log.info("Deletando transação: {}", transactionId);
        
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{transactionId}/approve")
    @Operation(summary = "Aprovar transação", description = "Aprova uma transação pendente")
    public ResponseEntity<TransactionResponse> approveTransaction(
            @Parameter(description = "ID da transação") @PathVariable String transactionId) {
        log.info("Aprovando transação: {}", transactionId);
        
        TransactionResponse response = transactionService.approveTransaction(transactionId);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{transactionId}/reject")
    @Operation(summary = "Rejeitar transação", description = "Rejeita uma transação pendente")
    public ResponseEntity<TransactionResponse> rejectTransaction(
            @Parameter(description = "ID da transação") @PathVariable String transactionId) {
        log.info("Rejeitando transação: {}", transactionId);
        
        TransactionResponse response = transactionService.rejectTransaction(transactionId);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{transactionId}/complete")
    @Operation(summary = "Completar transação", description = "Marca uma transação como completada")
    public ResponseEntity<TransactionResponse> completeTransaction(
            @Parameter(description = "ID da transação") @PathVariable String transactionId) {
        log.info("Completando transação: {}", transactionId);
        
        TransactionResponse response = transactionService.completeTransaction(transactionId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar por status", description = "Lista transações por status específico")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByStatus(
            @Parameter(description = "Status da transação") @PathVariable TransactionStatus status) {
        log.debug("Buscando transações por status: {}", status);
        
        List<TransactionResponse> transactions = transactionService.getTransactionsByStatus(status);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/type/{type}")
    @Operation(summary = "Buscar por tipo", description = "Lista transações por tipo específico")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByType(
            @Parameter(description = "Tipo da transação") @PathVariable TransactionType type) {
        log.debug("Buscando transações por tipo: {}", type);
        
        List<TransactionResponse> transactions = transactionService.getTransactionsByType(type);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/currency/from/{currencyPrefix}")
    @Operation(summary = "Buscar por moeda origem", description = "Lista transações por moeda de origem")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByFromCurrency(
            @Parameter(description = "Prefixo da moeda origem") @PathVariable String currencyPrefix) {
        log.debug("Buscando transações por moeda origem: {}", currencyPrefix);
        
        List<TransactionResponse> transactions = transactionService.getTransactionsByFromCurrency(currencyPrefix);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/currency/to/{currencyPrefix}")
    @Operation(summary = "Buscar por moeda destino", description = "Lista transações por moeda de destino")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByToCurrency(
            @Parameter(description = "Prefixo da moeda destino") @PathVariable String currencyPrefix) {
        log.debug("Buscando transações por moeda destino: {}", currencyPrefix);
        
        List<TransactionResponse> transactions = transactionService.getTransactionsByToCurrency(currencyPrefix);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/kingdom/{kingdomId}")
    @Operation(summary = "Buscar por reino", description = "Lista transações por reino específico")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByKingdom(
            @Parameter(description = "ID do reino") @PathVariable Long kingdomId) {
        log.debug("Buscando transações por reino: {}", kingdomId);
        
        List<TransactionResponse> transactions = transactionService.getTransactionsByKingdom(kingdomId);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/product/from/{productId}")
    @Operation(summary = "Buscar por produto origem", description = "Lista transações por produto de origem")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByFromProduct(
            @Parameter(description = "ID do produto origem") @PathVariable Long productId) {
        log.debug("Buscando transações por produto origem: {}", productId);
        
        List<TransactionResponse> transactions = transactionService.getTransactionsByFromProduct(productId);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/product/to/{productId}")
    @Operation(summary = "Buscar por produto destino", description = "Lista transações por produto de destino")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByToProduct(
            @Parameter(description = "ID do produto destino") @PathVariable Long productId) {
        log.debug("Buscando transações por produto destino: {}", productId);
        
        List<TransactionResponse> transactions = transactionService.getTransactionsByToProduct(productId);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/amount/range")
    @Operation(summary = "Buscar por faixa de valor", description = "Lista transações por faixa de valor")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAmountRange(
            @Parameter(description = "Valor mínimo") @RequestParam(value = "minAmount") BigDecimal minAmount,
            @Parameter(description = "Valor máximo") @RequestParam(value = "maxAmount") BigDecimal maxAmount) {
        log.debug("Buscando transações por faixa de valor: {} até {}", minAmount, maxAmount);
        
        List<TransactionResponse> transactions = transactionService.getTransactionsByAmountRange(minAmount, maxAmount);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/stats/count/status/{status}")
    @Operation(summary = "Contar por status", description = "Retorna o número de transações por status")
    public ResponseEntity<Long> countTransactionsByStatus(
            @Parameter(description = "Status da transação") @PathVariable TransactionStatus status) {
        log.debug("Contando transações por status: {}", status);
        
        long count = transactionService.countTransactionsByStatus(status);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/stats/count/type/{type}")
    @Operation(summary = "Contar por tipo", description = "Retorna o número de transações por tipo")
    public ResponseEntity<Long> countTransactionsByType(
            @Parameter(description = "Tipo da transação") @PathVariable TransactionType type) {
        log.debug("Contando transações por tipo: {}", type);
        
        long count = transactionService.countTransactionsByType(type);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/stats/count/kingdom/{kingdomId}")
    @Operation(summary = "Contar por reino", description = "Retorna o número de transações por reino")
    public ResponseEntity<Long> countTransactionsByKingdom(
            @Parameter(description = "ID do reino") @PathVariable Long kingdomId) {
        log.debug("Contando transações por reino: {}", kingdomId);
        
        long count = transactionService.countTransactionsByKingdom(kingdomId);
        return ResponseEntity.ok(count);
    }
} 