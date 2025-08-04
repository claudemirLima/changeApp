package com.product.domain.exception;

/**
 * Exceção lançada quando um produto já existe
 */
public class ProductAlreadyExistsException extends RuntimeException {

    public ProductAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ProductAlreadyExistsException(String name) {
        super("Já existe um produto com o nome: " + name);
    }
    
    public ProductAlreadyExistsException(String name, Long kingdomId) {
        super("Já existe um produto com o nome '" + name + "' no reino ID: " + kingdomId);
    }
} 