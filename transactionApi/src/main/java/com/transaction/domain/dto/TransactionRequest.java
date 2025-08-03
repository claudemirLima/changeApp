package com.transaction.domain.dto;

import com.transaction.domain.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * DTO para requisições de criação de transações
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    
    @NotBlank(message = "Transaction ID é obrigatório")
    private String transactionId;         // UUID do ExchangeApi
    
    @NotNull(message = "Tipo de transação é obrigatório")
    private TransactionType type;
    
    @NotNull(message = "Valor original é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor original deve ser pelo menos 0.01")
    private BigDecimal originalAmount;
    
    @NotBlank(message = "Moeda de origem é obrigatória")
    @Size(min = 2, max = 10, message = "Moeda de origem deve ter entre 2 e 10 caracteres")
    private String fromCurrencyPrefix;
    
    @NotBlank(message = "Moeda de destino é obrigatória")
    @Size(min = 2, max = 10, message = "Moeda de destino deve ter entre 2 e 10 caracteres")
    private String toCurrencyPrefix;
    
    @NotNull(message = "Taxa de câmbio é obrigatória")
    @DecimalMin(value = "0.0001", message = "Taxa de câmbio deve ser pelo menos 0.0001")
    private BigDecimal exchangeRate;
    
    // Para EXCHANGE
    private Long fromProductId;           // opcional
    private String fromProductName;       // opcional
    private Long toProductId;             // opcional
    private String toProductName;         // opcional
    
    // Reino
    private Long kingdomId;               // opcional
    private String kingdomName;           // opcional
    
    @Size(max = 500, message = "Motivo deve ter no máximo 500 caracteres")
    private String reason;
} 