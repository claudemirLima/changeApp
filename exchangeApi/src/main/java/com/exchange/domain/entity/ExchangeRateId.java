package com.exchange.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateId implements Serializable {
    
    @Column(name = "from_currency_prefix", nullable = false, length = 10)
    private String fromCurrencyPrefix;
    
    @Column(name = "to_currency_prefix", nullable = false, length = 10)
    private String toCurrencyPrefix;
    
    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
} 