package com.exchange.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionMessageCommand {
    private String commandId;
    private String transactionId;
    private String fromCurrencyCode;
    private String toCurrencyCode;
    private Integer quantityProduct;
    private Float quantityCurrency;
    private Long productId;
    private Long kingdomId;
    private LocalDate conversionDate;
    private LocalDateTime timestamp;
    private String correlationId;
    
    // Construtor para conversão básica
    public ConversionMessageCommand(String fromCurrencyCode, String toCurrencyCode, Float quantityCurrency) {
        this.commandId = UUID.randomUUID().toString();
        this.fromCurrencyCode = fromCurrencyCode;
        this.toCurrencyCode = toCurrencyCode;
        this.quantityCurrency = quantityCurrency;
        this.timestamp = LocalDateTime.now();
        this.correlationId = UUID.randomUUID().toString();
    }
    
    // Construtor para conversão com produto
    public ConversionMessageCommand(String fromCurrencyCode, String toCurrencyCode, Integer quantityProduct, 
                                  Long productId, LocalDate conversionDate) {
        this.commandId = UUID.randomUUID().toString();
        this.fromCurrencyCode = fromCurrencyCode;
        this.toCurrencyCode = toCurrencyCode;
        this.quantityProduct = quantityProduct;
        this.productId = productId;
        this.conversionDate = conversionDate;
        this.timestamp = LocalDateTime.now();
        this.correlationId = UUID.randomUUID().toString();
    }
} 