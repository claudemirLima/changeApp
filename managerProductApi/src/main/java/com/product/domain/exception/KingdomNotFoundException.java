package com.product.domain.exception;

/**
 * Exceção lançada quando um reino não é encontrado
 */
public class KingdomNotFoundException extends RuntimeException {
    
    public KingdomNotFoundException(String message) {
        super(message);
    }
    
    public KingdomNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public KingdomNotFoundException(Long id) {
        super("Reino não encontrado com ID: " + id);
    }
    
    public KingdomNotFoundException(String name, boolean byName) {
        super("Reino não encontrado com nome: " + name);
    }
} 