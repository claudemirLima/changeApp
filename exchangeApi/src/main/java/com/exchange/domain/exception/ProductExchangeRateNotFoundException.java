package com.exchange.domain.exception;

public class ProductExchangeRateNotFoundException extends RuntimeException {
    
    public ProductExchangeRateNotFoundException(String message) {
        super(message);
    }
    
    public ProductExchangeRateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 