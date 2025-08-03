package com.transaction.domain.enums;

/**
 * Enum para status das transações
 */
public enum TransactionStatus {
    REQUESTED("Solicitada"),
    APPROVED("Aprovada"),
    NOT_APPROVED("Não aprovada"),
    WARNING("Aviso");
    
    private final String description;
    
    TransactionStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
} 