package com.product.domain.exception;

/**
 * Exceção lançada quando um reino já existe
 */
public class KingdomAlreadyExistsException extends RuntimeException {
    
    public KingdomAlreadyExistsException(String message) {
        super(message);
    }
    
    public KingdomAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public KingdomAlreadyExistsException(String name) {
        super("Já existe um reino com o nome: " + name);
    }
} 