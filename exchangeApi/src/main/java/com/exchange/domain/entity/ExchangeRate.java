package com.exchange.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "exchange_rates")
@IdClass(ExchangeRateId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {
    
    @Id
    @Column(name = "from_currency_prefix", nullable = false, length = 10)
    private String fromCurrencyPrefix;
    
    @Id
    @Column(name = "to_currency_prefix", nullable = false, length = 10)
    private String toCurrencyPrefix;
    
    @Id
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "rate", nullable = false, precision = 10, scale = 4)
    private BigDecimal rate;
    
    @Column(name = "deactivated_at")
    private LocalDateTime deactivatedAt;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Construtor customizado
    public ExchangeRate(String fromCurrencyPrefix, String toCurrencyPrefix, BigDecimal rate) {
        this.fromCurrencyPrefix = fromCurrencyPrefix;
        this.toCurrencyPrefix = toCurrencyPrefix;
        this.rate = rate;
        this.isActive = true;
    }
    
    // Métodos de lifecycle
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 