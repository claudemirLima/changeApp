package com.transaction.domain.enums;

/**
 * Enum para tipos de transações
 */
public enum TransactionType {
    CONVERSION("Conversão de Moeda"),
    EXCHANGE("Troca de Produtos");
    
    private final String description;
    
    TransactionType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
} 