package com.product.domain.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * DTO para requisições de criação e atualização de Produto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    
    @NotBlank(message = "Nome do produto é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String name;
    
    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String description;
    
    @NotBlank(message = "Categoria é obrigatória")
    @Size(min = 2, max = 50, message = "Categoria deve ter entre 2 e 50 caracteres")
    private String category;
    
    @NotNull(message = "Valor base é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor base deve ser pelo menos 0.01")
    @DecimalMax(value = "9999999.99", message = "Valor base deve ser no máximo 9999999.99")
    private BigDecimal baseValue;
    
    @NotNull(message = "Quantificador de demanda é obrigatório")
    @DecimalMin(value = "0.1", message = "Quantificador de demanda deve ser pelo menos 0.1")
    @DecimalMax(value = "10.0", message = "Quantificador de demanda deve ser no máximo 10.0")
    private BigDecimal demandQuantifier;
    
    @NotNull(message = "Qualificador de qualidade é obrigatório")
    @DecimalMin(value = "0.1", message = "Qualificador de qualidade deve ser pelo menos 0.1")
    @DecimalMax(value = "10.0", message = "Qualificador de qualidade deve ser no máximo 10.0")
    private BigDecimal qualityQualifier;
    
    @NotNull(message = "ID do reino é obrigatório")
    @Min(value = 1, message = "ID do reino deve ser maior que 0")
    private Long kingdomId;
} 