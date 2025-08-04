package com.transaction.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionMessageCommand {
    private UUID commandId;
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
} 