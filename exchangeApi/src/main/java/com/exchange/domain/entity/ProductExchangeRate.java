package com.exchange.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_exchange_rates")
@IdClass(ProductExchangeRateId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductExchangeRate {
    
    @Id
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Id
    @Column(name = "from_currency_prefix", nullable = false, length = 10)
    private String fromCurrencyPrefix;
    
    @Id
    @Column(name = "to_currency_prefix", nullable = false, length = 10)
    private String toCurrencyPrefix;
    
    @Id
    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;
    
    @Id
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "base_rate", nullable = false, precision = 10, scale = 4)
    private BigDecimal baseRate;
    
    @Column(name = "product_multiplier", nullable = false, precision = 5, scale = 2)
    private BigDecimal productMultiplier;
    
    @Column(name = "deactivated_at")
    private LocalDateTime deactivatedAt;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Construtor customizado
    public ProductExchangeRate(Long productId, String fromCurrencyPrefix, String toCurrencyPrefix, 
                              BigDecimal baseRate, BigDecimal productMultiplier, LocalDate effectiveDate) {
        this.productId = productId;
        this.fromCurrencyPrefix = fromCurrencyPrefix;
        this.toCurrencyPrefix = toCurrencyPrefix;
        this.baseRate = baseRate;
        this.productMultiplier = productMultiplier;
        this.effectiveDate = effectiveDate;
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