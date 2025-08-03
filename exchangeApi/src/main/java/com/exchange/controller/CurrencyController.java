package com.exchange.controller;

import com.exchange.domain.dto.CurrencyInfo;
import com.exchange.domain.dto.PageRequest;
import com.exchange.domain.dto.PageResponse;
import com.exchange.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/currencies")
@Tag(name = "Moedas", description = "Endpoints para gestão de moedas")
public class CurrencyController {
    
    @Autowired
    private CurrencyService currencyService;
    
    @GetMapping
    @Operation(
        summary = "Listar moedas",
        description = "Retorna lista paginada de moedas com filtros opcionais"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Moedas listadas com sucesso"),
        @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos")
    })
    public ResponseEntity<PageResponse<CurrencyInfo>> getCurrencies(
        @Parameter(description = "Se deve retornar apenas moedas ativas", example = "true") 
        @RequestParam(defaultValue = "true") Boolean activeOnly,
        @Parameter(description = "Parâmetros de paginação") PageRequest pageRequest
    ) {
        // TODO: Implementar quando CurrencyService estiver pronto
        return ResponseEntity.ok(new PageResponse<>());
    }
    
    @GetMapping("/{code}")
    @Operation(
        summary = "Buscar moeda por código",
        description = "Retorna os detalhes de uma moeda específica"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Moeda encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CurrencyInfo.class),
                examples = @ExampleObject(
                    name = "Ouro Real",
                    value = """
                    {
                      "code": "ORO",
                      "name": "Ouro Real",
                      "description": "Moeda oficial do reino SRM"
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Moeda não encontrada")
    })
    public ResponseEntity<CurrencyInfo> getCurrency(
        @Parameter(description = "Código da moeda", example = "ORO") 
        @PathVariable String code
    ) {
        // TODO: Implementar quando CurrencyService estiver pronto
        return ResponseEntity.ok(new CurrencyInfo());
    }
    
    @PostMapping
    @Operation(
        summary = "Criar nova moeda",
        description = "Cria uma nova moeda no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Moeda criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Moeda já existe")
    })
    public ResponseEntity<CurrencyInfo> createCurrency(
        @Parameter(
            description = "Dados da moeda",
            required = true,
            content = @Content(
                schema = @Schema(implementation = CurrencyInfo.class),
                examples = @ExampleObject(
                    name = "Nova Moeda",
                    value = """
                    {
                      "code": "TIB",
                      "name": "Tibar",
                      "description": "Moeda dos anões"
                    }
                    """
                )
            )
        )
        @RequestBody CurrencyInfo currencyInfo
    ) {
        // TODO: Implementar quando CurrencyService estiver pronto
        return ResponseEntity.status(HttpStatus.CREATED).body(new CurrencyInfo());
    }
    
    @PutMapping("/{code}")
    @Operation(
        summary = "Atualizar moeda",
        description = "Atualiza uma moeda existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Moeda atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Moeda não encontrada")
    })
    public ResponseEntity<CurrencyInfo> updateCurrency(
        @Parameter(description = "Código da moeda") @PathVariable String code,
        @RequestBody CurrencyInfo currencyInfo
    ) {
        // TODO: Implementar quando CurrencyService estiver pronto
        return ResponseEntity.ok(new CurrencyInfo());
    }
    
    @DeleteMapping("/{code}")
    @Operation(
        summary = "Desativar moeda",
        description = "Desativa uma moeda (soft delete)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Moeda desativada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Moeda não encontrada")
    })
    public ResponseEntity<Void> deactivateCurrency(
        @Parameter(description = "Código da moeda") @PathVariable String code
    ) {
        // TODO: Implementar quando CurrencyService estiver pronto
        return ResponseEntity.noContent().build();
    }
} 