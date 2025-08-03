package com.exchange.domain.exception;

public class ProductExchangeRateOperationException extends RuntimeException {
    
    public ProductExchangeRateOperationException(String message) {
        super(message);
    }
    
    public ProductExchangeRateOperationException(String message, Throwable cause) {
        super(message, cause);
    }
} 