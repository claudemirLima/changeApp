package com.product.domain.exception;

/**
 * Exceção lançada para erros gerais de operação de produto
 */
public class ProductOperationException extends RuntimeException {
    
    public ProductOperationException(String message) {
        super(message);
    }
    
    public ProductOperationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ProductOperationException(String operation, String reason) {
        super("Erro na operação '" + operation + "': " + reason);
    }
} 