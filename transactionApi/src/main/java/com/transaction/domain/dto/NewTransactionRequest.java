package com.transaction.domain.dto;

import com.transaction.domain.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para requisições de criação de transações
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewTransactionRequest {
    
    @NotNull(message = "Tipo de transação é obrigatório")
    private TransactionType type;

    private Integer quantityProduct;

    private Float quantityCurrency;

    @NotBlank(message = "Moeda de origem é obrigatória")
    @Size(min = 2, max = 10, message = "Moeda de origem deve ter entre 2 e 10 caracteres")
    private String fromCurrencyPrefix;
    
    @NotBlank(message = "Moeda de destino é obrigatória")
    @Size(min = 2, max = 10, message = "Moeda de destino deve ter entre 2 e 10 caracteres")
    private String toCurrencyPrefix;

    // Para EXCHANGE
    private Long fromProductId;           // opcional
    private Long toProductId;             // opcional
    
    // Reino
    private Long kingdomId;               // opcional
    private String kingdomName;           // opcional
    
    @Size(max = 500, message = "Motivo deve ter no máximo 500 caracteres")
    private String reason;
} 