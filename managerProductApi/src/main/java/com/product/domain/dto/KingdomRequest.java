package com.product.domain.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * DTO para requisições de criação e atualização de Reino
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KingdomRequest {
    
    @NotBlank(message = "Nome do reino é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String name;
    
    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String description;
    
    @NotNull(message = "Rate de qualidade é obrigatório")
    @DecimalMin(value = "0.1", message = "Rate de qualidade deve ser pelo menos 0.1")
    @DecimalMax(value = "10.0", message = "Rate de qualidade deve ser no máximo 10.0")
    private BigDecimal qualityRate;
    
    @NotNull(message = "Indicador de proprietário é obrigatório")
    private Boolean isOwner;
} 