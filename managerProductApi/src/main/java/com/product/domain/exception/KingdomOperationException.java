package com.product.domain.exception;

/**
 * Exceção lançada para erros gerais de operação de reino
 */
public class KingdomOperationException extends RuntimeException {
    
    public KingdomOperationException(String message) {
        super(message);
    }
    
    public KingdomOperationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public KingdomOperationException(String operation, String reason) {
        super("Erro na operação '" + operation + "': " + reason);
    }
} 