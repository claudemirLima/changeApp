package com.exchange.domain.enums;

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