package com.product.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que representa um Produto no sistema
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "description", length = 500)
    private String description;
    
    @Column(name = "category", nullable = false, length = 50)
    private String category;
    
    @Column(name = "base_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseValue;
    
    @Column(name = "demand_quantifier", nullable = false, precision = 5, scale = 2)
    private BigDecimal demandQuantifier = BigDecimal.ONE;
    
    @Column(name = "quality_qualifier", nullable = false, precision = 5, scale = 2)
    private BigDecimal qualityQualifier = BigDecimal.ONE;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kingdom_id", nullable = false)
    private Kingdom kingdom;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "deactivated_at")
    private LocalDateTime deactivatedAt;
    
    // Construtor customizado
    public Product(String name, String description, String category, BigDecimal baseValue, 
                  BigDecimal demandQuantifier, BigDecimal qualityQualifier, Kingdom kingdom) {
        this();
        this.name = name;
        this.description = description;
        this.category = category;
        this.baseValue = baseValue;
        this.demandQuantifier = demandQuantifier;
        this.qualityQualifier = qualityQualifier;
        this.kingdom = kingdom;
    }
    
    // Construtor padrão com inicialização
    public Product() {
        this.createdAt = LocalDateTime.now();
    }
    
    // Métodos de lifecycle
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Métodos de negócio
    public void deactivate() {
        this.isActive = false;
        this.deactivatedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void activate() {
        this.isActive = true;
        this.deactivatedAt = null;
        this.updatedAt = LocalDateTime.now();
    }
    
    public BigDecimal calculateFinalValue() {
        return baseValue.multiply(calculateTotalMultiplier());
    }
    
    public boolean isHighDemand() {
        return this.demandQuantifier != null && 
               this.demandQuantifier.compareTo(BigDecimal.valueOf(2.0)) >= 0;
    }
    
    public boolean isLowDemand() {
        return this.demandQuantifier != null && 
               this.demandQuantifier.compareTo(BigDecimal.valueOf(0.5)) <= 0;
    }
    
    public boolean isHighQuality() {
        return this.qualityQualifier != null && 
               this.qualityQualifier.compareTo(BigDecimal.valueOf(2.0)) >= 0;
    }
    
    public boolean isLowQuality() {
        return this.qualityQualifier != null && 
               this.qualityQualifier.compareTo(BigDecimal.valueOf(0.5)) <= 0;
    }
    
    public boolean isFromOwnerKingdom() {
        return this.kingdom != null && Boolean.TRUE.equals(this.kingdom.getIsOwner());
    }
    
    public BigDecimal calculateTotalMultiplier() {
        BigDecimal kingdomMultiplier = kingdom != null ? kingdom.getQualityRate() : BigDecimal.ONE;
        BigDecimal ownerMultiplier = isFromOwnerKingdom() ? BigDecimal.valueOf(1.2) : BigDecimal.ONE;
        return demandQuantifier
            .multiply(qualityQualifier)
            .multiply(kingdomMultiplier)
            .multiply(ownerMultiplier);
    }
} 