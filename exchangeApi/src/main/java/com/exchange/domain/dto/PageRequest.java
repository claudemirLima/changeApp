package com.exchange.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Parâmetros de paginação")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest {
    
    @Schema(description = "Número da página (baseado em 0)", example = "0", defaultValue = "0")
    private Integer page = 0;
    
    @Schema(description = "Tamanho da página", example = "20", defaultValue = "20")
    private Integer size = 20;
    
    @Schema(description = "Campo para ordenação", example = "createdAt")
    private String sortBy;
    
    @Schema(description = "Direção da ordenação (ASC ou DESC)", example = "DESC", allowableValues = {"ASC", "DESC"})
    private String sortDirection = "DESC";
    
    // Construtor customizado
    public PageRequest(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }
} 