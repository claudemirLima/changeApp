package com.exchange.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyId implements Serializable {
    
    @Column(name = "prefix", nullable = false, length = 10)
    private String prefix;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
} 