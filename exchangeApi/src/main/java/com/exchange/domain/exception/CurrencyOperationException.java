package com.exchange.domain.exception;

public class CurrencyOperationException extends RuntimeException {
    
    public CurrencyOperationException(String message) {
        super(message);
    }
    
    public CurrencyOperationException(String message, Throwable cause) {
        super(message, cause);
    }
} 