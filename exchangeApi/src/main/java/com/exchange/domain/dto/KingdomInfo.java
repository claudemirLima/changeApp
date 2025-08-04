package com.exchange.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KingdomInfo {
    private Long id;
    private String name;
    private BigDecimal qualityRate;
    private Boolean isOwner;
} 