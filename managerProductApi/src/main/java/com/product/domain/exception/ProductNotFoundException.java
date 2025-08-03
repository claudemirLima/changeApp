package com.product.domain.exception;

/**
 * Exceção lançada quando um produto não é encontrado
 */
public class ProductNotFoundException extends RuntimeException {
    
    public ProductNotFoundException(String message) {
        super(message);
    }
    
    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ProductNotFoundException(Long id) {
        super("Produto não encontrado com ID: " + id);
    }
    
    public ProductNotFoundException(String name, boolean byName) {
        super("Produto não encontrado com nome: " + name);
    }
} 