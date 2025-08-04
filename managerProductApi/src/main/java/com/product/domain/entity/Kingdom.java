package com.product.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que representa um Reino no sistema
 */
@Entity
@Table(name = "kingdoms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Kingdom {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;
    
    @Column(name = "description", length = 500)
    private String description;
    
    @Column(name = "quality_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal qualityRate = BigDecimal.ONE;
    
    @Column(name = "is_owner", nullable = false)
    private Boolean isOwner = false;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "deactivated_at")
    private LocalDateTime deactivatedAt;

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
    
    public boolean isOwnerKingdom() {
        return Boolean.TRUE.equals(this.isOwner);
    }
    
    public boolean hasHighQuality() {
        return this.qualityRate != null && this.qualityRate.compareTo(BigDecimal.valueOf(1.5)) >= 0;
    }
    
    public boolean hasLowQuality() {
        return this.qualityRate != null && this.qualityRate.compareTo(BigDecimal.valueOf(0.5)) <= 0;
    }
} 