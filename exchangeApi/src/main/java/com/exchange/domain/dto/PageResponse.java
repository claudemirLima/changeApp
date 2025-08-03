package com.exchange.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Schema(description = "Resposta paginada")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    
    @Schema(description = "Lista de itens")
    private List<T> content;
    
    @Schema(description = "Número da página atual", example = "0")
    private Integer page;
    
    @Schema(description = "Tamanho da página", example = "20")
    private Integer size;
    
    @Schema(description = "Total de elementos", example = "100")
    private Long totalElements;
    
    @Schema(description = "Total de páginas", example = "5")
    private Integer totalPages;
    
    @Schema(description = "Se é a primeira página", example = "true")
    private Boolean first;
    
    @Schema(description = "Se é a última página", example = "false")
    private Boolean last;
} 