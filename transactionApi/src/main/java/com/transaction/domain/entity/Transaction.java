package com.transaction.domain.entity;

import com.transaction.domain.enums.TransactionStatus;
import com.transaction.domain.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que representa uma Transação no sistema
 */
@Document(collection = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    
    @Id
    @Field("transaction_id")
    private String transactionId;         // UUID do ExchangeApi como chave primária
    
    // Dados da Transação
    @Field("original_amount")
    private BigDecimal originalAmount;     // Valor original
    
    @Field("converted_amount")
    private BigDecimal convertedAmount;    // Valor convertido
    
    @Field("from_currency_prefix")
    private String fromCurrencyPrefix;     // Moeda origem
    
    @Field("to_currency_prefix")
    private String toCurrencyPrefix;       // Moeda destino
    
    @Field("exchange_rate")
    private BigDecimal exchangeRate;       // Taxa aplicada
    
    // Produto Origem (para EXCHANGE)
    @Field("from_product_id")
    private Long fromProductId;           // ID do produto origem
    
    @Field("from_product_name")
    private String fromProductName;       // Nome do produto origem
    
    // Produto Destino (para EXCHANGE)
    @Field("to_product_id")
    private Long toProductId;             // ID do produto destino
    
    @Field("to_product_name")
    private String toProductName;         // Nome do produto destino
    
    // Reino
    @Field("kingdom_id")
    private Long kingdomId;               // ID do reino
    
    @Field("kingdom_name")
    private String kingdomName;           // Nome do reino
    
    // Tipo e Status
    private TransactionType type;         // CONVERSION, EXCHANGE
    private TransactionStatus status;     // REQUESTED, APPROVED, NOT_APPROVED, WARNING
    private String reason;                // Motivo da transação
    
    // Timestamps
    @Field("created_at")
    private LocalDateTime createdAt;
    
    @Field("updated_at")
    private LocalDateTime updatedAt;
    
    @Field("completed_at")
    private LocalDateTime completedAt;
    
    // Construtor customizado
    public Transaction(String transactionId, TransactionType type, BigDecimal originalAmount, 
                      String fromCurrencyPrefix, String toCurrencyPrefix, BigDecimal exchangeRate) {
        this.transactionId = transactionId;
        this.type = type;
        this.originalAmount = originalAmount;
        this.fromCurrencyPrefix = fromCurrencyPrefix;
        this.toCurrencyPrefix = toCurrencyPrefix;
        this.exchangeRate = exchangeRate;
        this.status = TransactionStatus.REQUESTED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Métodos de negócio
    public void approve() {
        this.status = TransactionStatus.APPROVED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void reject() {
        this.status = TransactionStatus.NOT_APPROVED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void complete() {
        this.status = TransactionStatus.APPROVED;
        this.completedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isConversion() {
        return TransactionType.CONVERSION.equals(this.type);
    }
    
    public boolean isExchange() {
        return TransactionType.EXCHANGE.equals(this.type);
    }
    
    public boolean isPending() {
        return TransactionStatus.REQUESTED.equals(this.status);
    }
    
    public boolean isApproved() {
        return TransactionStatus.APPROVED.equals(this.status);
    }
} 