package com.exchange.controller;

import com.exchange.domain.dto.ExchangeRateRequest;
import com.exchange.domain.dto.ExchangeRateResponse;
import com.exchange.domain.dto.ExchangeRateSimpleResponse;
import com.exchange.domain.dto.ErrorResponse;
import com.exchange.domain.dto.PageRequest;
import com.exchange.domain.dto.PageResponse;
import com.exchange.domain.entity.ExchangeRate;
import com.exchange.domain.mapper.ExchangeRateMapper;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/exchange-rates")
@Tag(name = "Taxas de Câmbio", description = "Endpoints para gestão de taxas de câmbio")
public class ExchangeRateController {
    
    @Autowired
    private ExchangeRateService exchangeRateService;
    
    @Autowired
    private ExchangeRateMapper exchangeRateMapper;
    
    @GetMapping
    @Operation(
        summary = "Listar taxas de câmbio",
        description = "Retorna lista paginada de taxas de câmbio com filtros opcionais (resposta simplificada)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Taxas listadas com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PageResponse.class),
                examples = @ExampleObject(
                    name = "Lista de Taxas Simplificada",
                    value = """
                    {
                      "content": [
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
                          "lastUpdated": "2024-01-15T10:30:00"
                        }
                      ],
                      "page": 0,
                      "size": 20,
                      "totalElements": 1,
                      "totalPages": 1,
                      "first": true,
                      "last": true
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos")
    })
    public ResponseEntity<PageResponse<ExchangeRateSimpleResponse>> getExchangeRates(
        @Parameter(description = "Código da moeda de origem") @RequestParam(value = "fromCurrency", required = false) String fromCurrency,
        @Parameter(description = "Código da moeda de destino") @RequestParam(value = "toCurrency", required = false) String toCurrency,
        @Parameter(description = "Se deve retornar apenas taxas ativas", example = "true") 
        @RequestParam(value = "activeOnly", defaultValue = "true") Boolean activeOnly,
        @Parameter(description = "Parâmetros de paginação") com.exchange.domain.dto.PageRequest pageRequest
    ) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
            pageRequest.getPage(), 
            pageRequest.getSize()
        );
        
        Page<ExchangeRate> ratesPage = exchangeRateService.getExchangeRatesWithFilters(
            fromCurrency, toCurrency,  activeOnly, pageable
        );
        
        List<ExchangeRateSimpleResponse> responses = exchangeRateMapper.entityListToSimpleResponseList(ratesPage.getContent());
        
        PageResponse<ExchangeRateSimpleResponse> pageResponse = new PageResponse<>(
            responses,
            ratesPage.getNumber(),
            ratesPage.getSize(),
            ratesPage.getTotalElements(),
            ratesPage.getTotalPages(),
            ratesPage.isFirst(),
            ratesPage.isLast()
        );
        
        return ResponseEntity.ok(pageResponse);
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
        @ApiResponse(
            responseCode = "404", 
            description = "Taxa não encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Taxa Não Encontrada",
                    value = """
                    {
                      "message": "Taxa de câmbio não encontrada",
                      "details": "Não foi encontrada uma taxa ativa para ORO → INVALID",
                      "suggestions": [
                        "Verifique se os códigos das moedas estão corretos",
                        "Consulte a lista de moedas disponíveis",
                        "Verifique se existe uma taxa ativa para este par"
                      ],
                      "action": "Use GET /api/v1/currencies para ver moedas disponíveis",
                      "timestamp": "2024-01-15T10:30:00"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<?> getExchangeRate(
        @Parameter(description = "Código da moeda de origem", example = "ORO") 
        @PathVariable String fromCurrency,
        @Parameter(description = "Código da moeda de destino", example = "TIB") 
        @PathVariable String toCurrency
    ) {
        var rateOpt = exchangeRateService.findActiveRate(fromCurrency, toCurrency);
        
        if (rateOpt.isPresent()) {
            ExchangeRateResponse response = exchangeRateMapper.entityToResponse(rateOpt.get());
            return ResponseEntity.ok(response);
        } else {
            ErrorResponse errorResponse = createErrorResponse(
                "Taxa de câmbio não encontrada",
                "Não foi encontrada uma taxa ativa para " + fromCurrency + " → " + toCurrency,
                Arrays.asList(
                    "Verifique se os códigos das moedas estão corretos",
                    "Consulte a lista de moedas disponíveis",
                    "Verifique se existe uma taxa ativa para este par"
                ),
                "Use GET /api/v1/currencies para ver moedas disponíveis"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
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
                      "isActive": true
                    }
                    """
                )
            )
        )
        @RequestBody ExchangeRateRequest request
    ) {
        ExchangeRate entity = exchangeRateMapper.requestToEntity(request);
        ExchangeRate savedRate = exchangeRateService.saveRate(
            entity.getFromCurrencyPrefix(), 
            entity.getToCurrencyPrefix(), 
            entity.getRate()
        );
        ExchangeRateResponse response = exchangeRateMapper.entityToResponse(savedRate);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/{fromCurrency}/{toCurrency}")
    @Operation(
        summary = "Atualizar taxa de câmbio",
        description = "Atualiza uma taxa de câmbio existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Taxa atualizada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExchangeRateResponse.class),
                examples = @ExampleObject(
                    name = "Taxa Atualizada",
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
                      "rate": 2.8,
                      "lastUpdated": "2024-01-15T10:30:00",
                      "status": "APPROVED",
                      "reason": "Taxa ativa e válida",
                      "riskScore": 0.2,
                      "warnings": [],
                      "recommendations": []
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Taxa não encontrada")
    })
    public ResponseEntity<ExchangeRateResponse> updateExchangeRate(
        @Parameter(description = "Código da moeda de origem") @PathVariable String fromCurrency,
        @Parameter(description = "Código da moeda de destino") @PathVariable String toCurrency,
        @Parameter(
            description = "Dados da taxa de câmbio para atualização",
            required = true,
            content = @Content(
                schema = @Schema(implementation = ExchangeRateRequest.class),
                examples = @ExampleObject(
                    name = "Atualizar Taxa",
                    value = """
                    {
                      "fromCurrencyCode": "ORO",
                      "toCurrencyCode": "TIB",
                      "rate": 2.8,
                      "isActive": true
                    }
                    """
                )
            )
        )
        @RequestBody ExchangeRateRequest request
    ) {
        ExchangeRate entity = exchangeRateMapper.requestToEntity(request);
        ExchangeRate updatedRate = exchangeRateService.updateRate(
            fromCurrency, 
            toCurrency, 
            entity.getRate(),
            entity.getIsActive()
        );
        ExchangeRateResponse response = exchangeRateMapper.entityToResponse(updatedRate);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{fromCurrency}/{toCurrency}")
    @Operation(
        summary = "Desativar taxa de câmbio",
        description = "Desativa uma taxa de câmbio (soft delete)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Taxa desativada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Taxa não encontrada")
    })
    public ResponseEntity<Void> deactivateExchangeRate(
        @Parameter(description = "Código da moeda de origem") @PathVariable String fromCurrency,
        @Parameter(description = "Código da moeda de destino") @PathVariable String toCurrency
    ) {
        exchangeRateService.deactivateRate(fromCurrency, toCurrency);
        return ResponseEntity.noContent().build();
    }

    /**
     * Cria uma resposta de erro padronizada
     */
    private ErrorResponse createErrorResponse(String message, String details, 
                                           List<String> suggestions, String action) {
        return new ErrorResponse(message, details, suggestions, action);
    }
    

} 