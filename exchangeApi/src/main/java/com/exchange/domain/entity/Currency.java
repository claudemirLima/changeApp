package com.exchange.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "currencies")
@IdClass(CurrencyId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {
    
    @Id
    @Column(name = "prefix", nullable = false, length = 10)
    private String prefix;
    
    @Id
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "deactivated_at")
    private LocalDateTime deactivatedAt;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Construtor customizado
    public Currency(String prefix, String name, String description) {
        this.prefix = prefix;
        this.name = name;
        this.description = description;
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