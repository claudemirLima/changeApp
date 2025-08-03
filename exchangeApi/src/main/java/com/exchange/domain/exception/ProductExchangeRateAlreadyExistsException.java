package com.exchange.domain.exception;

public class ProductExchangeRateAlreadyExistsException extends RuntimeException {
    
    public ProductExchangeRateAlreadyExistsException(String message) {
        super(message);
    }
    
    public ProductExchangeRateAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
} 