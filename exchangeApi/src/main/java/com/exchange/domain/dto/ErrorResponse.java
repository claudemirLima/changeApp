package com.exchange.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO genérico para respostas de erro
 * Pode ser reutilizado por qualquer controller
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    private String message;
    private String details;
    private List<String> suggestions;
    private String action;
    private LocalDateTime timestamp;
    
    /**
     * Construtor simplificado para casos básicos
     */
    public ErrorResponse(String message, String details) {
        this.message = message;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
    
    /**
     * Construtor completo
     */
    public ErrorResponse(String message, String details, List<String> suggestions, String action) {
        this.message = message;
        this.details = details;
        this.suggestions = suggestions;
        this.action = action;
        this.timestamp = LocalDateTime.now();
    }
} 