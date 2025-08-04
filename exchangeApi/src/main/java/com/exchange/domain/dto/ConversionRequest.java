package com.exchange.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionRequest {
    private String transactionId;
    private String fromCurrencyCode;
    private String toCurrencyCode;
    private Integer quantityProduct;
    private Float quantityCurrency;
    private Long productId;
    private Long kingdomId;
    private LocalDate conversionDate;

} 