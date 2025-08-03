package com.product.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para respostas de Reino
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KingdomResponse {
    
    private Long id;
    private String name;
    private String description;
    private BigDecimal qualityRate;
    private Boolean isOwner;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 