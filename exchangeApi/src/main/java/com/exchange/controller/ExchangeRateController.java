package com.exchange.controller;

import com.exchange.domain.dto.ExchangeRateRequest;
import com.exchange.domain.dto.ExchangeRateResponse;
import com.exchange.domain.dto.PageRequest;
import com.exchange.domain.dto.PageResponse;
import com.exchange.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/exchange-rates")
@Tag(name = "Taxas de Câmbio", description = "Endpoints para gestão de taxas de câmbio")
public class ExchangeRateController {
    
    @Autowired
    private ExchangeRateService exchangeRateService;
    
    @GetMapping
    @Operation(
        summary = "Listar taxas de câmbio",
        description = "Retorna lista paginada de taxas de câmbio com filtros opcionais"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Taxas listadas com sucesso"),
        @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos")
    })
    public ResponseEntity<PageResponse<ExchangeRateResponse>> getExchangeRates(
        @Parameter(description = "Código da moeda de origem") @RequestParam(required = false) String fromCurrency,
        @Parameter(description = "Código da moeda de destino") @RequestParam(required = false) String toCurrency,
        @Parameter(description = "Data efetiva da taxa") 
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate effectiveDate,
        @Parameter(description = "Se deve retornar apenas taxas ativas", example = "true") 
        @RequestParam(defaultValue = "true") Boolean activeOnly,
        @Parameter(description = "Parâmetros de paginação") PageRequest pageRequest
    ) {
        // TODO: Implementar quando ExchangeRateService estiver pronto
        return ResponseEntity.ok(new PageResponse<>());
    }
    
    @GetMapping("/{fromCurrency}/{toCurrency}")
    @Operation(
        summary = "Buscar taxa de câmbio específica",
        description = "Retorna a taxa de câmbio ativa para um par de moedas específico"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Taxa encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExchangeRateResponse.class),
                examples = @ExampleObject(
                    name = "Taxa ORO para TIB",
                    value = """
                    {
                      "fromCurrency": {
                        "code": "ORO",
                        "name": "Ouro Real",
                        "description": "Moeda oficial do reino SRM"
                      },
                      "toCurrency": {
                        "code": "TIB",
                        "name": "Tibar",
                        "description": "Moeda dos anões"
                      },
                      "rate": 2.5,
                      "effectiveDate": "2024-01-15",
                      "lastUpdated": "2024-01-15T10:30:00",
                      "status": "APPROVED",
                      "reason": "Taxa dentro dos parâmetros normais",
                      "riskScore": 0.2,
                      "warnings": [],
                      "recommendations": ["Taxa estável"]
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Taxa não encontrada")
    })
    public ResponseEntity<ExchangeRateResponse> getExchangeRate(
        @Parameter(description = "Código da moeda de origem", example = "ORO") 
        @PathVariable String fromCurrency,
        @Parameter(description = "Código da moeda de destino", example = "TIB") 
        @PathVariable String toCurrency,
        @Parameter(description = "Data efetiva (opcional, usa data atual se não informada)") 
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate effectiveDate
    ) {
        // TODO: Implementar quando ExchangeRateService estiver pronto
        return ResponseEntity.ok(new ExchangeRateResponse());
    }
    
    @PostMapping
    @Operation(
        summary = "Criar nova taxa de câmbio",
        description = "Cria uma nova taxa de câmbio para um par de moedas"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Taxa criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Taxa já existe para a data")
    })
    public ResponseEntity<ExchangeRateResponse> createExchangeRate(
        @Parameter(
            description = "Dados da taxa de câmbio",
            required = true,
            content = @Content(
                schema = @Schema(implementation = ExchangeRateRequest.class),
                examples = @ExampleObject(
                    name = "Nova Taxa",
                    value = """
                    {
                      "fromCurrencyCode": "ORO",
                      "toCurrencyCode": "TIB",
                      "rate": 2.5,
                      "effectiveDate": "2024-01-15"
                    }
                    """
                )
            )
        )
        @RequestBody ExchangeRateRequest request
    ) {
        // TODO: Implementar quando ExchangeRateService estiver pronto
        return ResponseEntity.status(HttpStatus.CREATED).body(new ExchangeRateResponse());
    }
    
    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar taxa de câmbio",
        description = "Atualiza uma taxa de câmbio existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Taxa atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Taxa não encontrada")
    })
    public ResponseEntity<ExchangeRateResponse> updateExchangeRate(
        @Parameter(description = "ID da taxa de câmbio") @PathVariable Long id,
        @RequestBody ExchangeRateRequest request
    ) {
        // TODO: Implementar quando ExchangeRateService estiver pronto
        return ResponseEntity.ok(new ExchangeRateResponse());
    }
    
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Desativar taxa de câmbio",
        description = "Desativa uma taxa de câmbio (soft delete)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Taxa desativada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Taxa não encontrada")
    })
    public ResponseEntity<Void> deactivateExchangeRate(
        @Parameter(description = "ID da taxa de câmbio") @PathVariable Long id
    ) {
        // TODO: Implementar quando ExchangeRateService estiver pronto
        return ResponseEntity.noContent().build();
    }
} 