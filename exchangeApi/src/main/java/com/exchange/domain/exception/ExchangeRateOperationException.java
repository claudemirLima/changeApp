package com.exchange.domain.exception;

public class ExchangeRateOperationException extends RuntimeException {
    
    public ExchangeRateOperationException(String message) {
        super(message);
    }
    
    public ExchangeRateOperationException(String message, Throwable cause) {
        super(message, cause);
    }
} 