package com.product.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para respostas de Produto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    
    private Long id;
    private String name;
    private String description;
    private String category;
    private BigDecimal baseValue;
    private BigDecimal demandQuantifier;
    private BigDecimal qualityQualifier;
    private BigDecimal finalValue;
    private BigDecimal totalMultiplier;
    private KingdomResponse kingdom;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 