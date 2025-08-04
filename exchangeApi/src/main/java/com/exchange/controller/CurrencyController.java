package com.exchange.controller;

import com.exchange.domain.dto.CurrencyInfo;
import com.exchange.domain.dto.ErrorResponse;
import com.exchange.domain.dto.PageRequest;
import com.exchange.domain.dto.PageResponse;
import com.exchange.domain.exception.CurrencyAlreadyExistsException;
import com.exchange.domain.entity.Currency;
import com.exchange.domain.mapper.CurrencyMapper;
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

import java.util.Arrays;
import java.util.List;

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
        @RequestParam(value = "activeOnly", defaultValue = "true") Boolean activeOnly,
        @Parameter(description = "Parâmetros de paginação") PageRequest pageRequest
    ) {
        PageResponse<CurrencyInfo> currencies = currencyService.getCurrencies(activeOnly, pageRequest);
        return ResponseEntity.ok(currencies);
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
        @ApiResponse(
            responseCode = "404", 
            description = "Moeda não encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Moeda Não Encontrada",
                    value = """
                    {
                      "message": "Moeda não encontrada",
                      "details": "Moeda com código INVALID não foi encontrada",
                      "suggestions": [
                        "Verifique o código da moeda",
                        "Consulte a lista de moedas disponíveis",
                        "Verifique se a moeda está ativa"
                      ],
                      "action": "Use GET /api/v1/currencies para ver moedas disponíveis",
                      "timestamp": "2024-01-15T10:30:00"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<?> getCurrency(
        @Parameter(description = "Código da moeda", example = "ORO") 
        @PathVariable String code
    ) {
        var currencyOpt = currencyService.findCurrencyByPrefix(code);
        
        if (currencyOpt.isPresent()) {
            return ResponseEntity.ok(currencyOpt.get());
        } else {
            ErrorResponse errorResponse = createErrorResponse(
                "Moeda não encontrada",
                "Moeda com código " + code + " não foi encontrada",
                Arrays.asList(
                    "Verifique o código da moeda",
                    "Consulte a lista de moedas disponíveis",
                    "Verifique se a moeda está ativa"
                ),
                "Use GET /api/v1/currencies para ver moedas disponíveis"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
    
    @PostMapping
    @Operation(
        summary = "Criar nova moeda",
        description = "Cria uma nova moeda no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Moeda criada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CurrencyInfo.class),
                examples = @ExampleObject(
                    name = "Moeda Criada",
                    value = """
                    {
                      "code": "TIB",
                      "name": "Tibar",
                      "description": "Moeda dos anões"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Dados inválidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Dados Inválidos",
                    value = """
                    {
                      "message": "Dados inválidos",
                      "details": "Código da moeda é obrigatório",
                      "suggestions": [
                        "Verifique se todos os campos obrigatórios estão preenchidos",
                        "Código da moeda deve ter no máximo 10 caracteres",
                        "Nome da moeda deve ter no máximo 100 caracteres",
                        "Código e nome da moeda são obrigatórios"
                      ],
                      "action": "Corrija os dados e tente novamente",
                      "timestamp": "2024-01-15T10:30:00"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "Moeda já existe",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Moeda Já Existe",
                    value = """
                    {
                      "message": "Moeda já existe",
                      "details": "Já existe uma moeda ativa com o prefix: ORO",
                      "suggestions": [
                        "Verifique se já existe uma moeda com este código",
                        "Use um código único para a nova moeda",
                        "Consulte a lista de moedas existentes"
                      ],
                      "action": "Use GET /api/v1/currencies para ver moedas existentes",
                      "timestamp": "2024-01-15T10:30:00"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<?> createCurrency(
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
        try {
            // Usar mapper para validação e conversão
            Currency entity = CurrencyMapper.dtoToEntityWithValidation(currencyInfo);
            CurrencyInfo createdCurrency = currencyService.createCurrency(currencyInfo);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCurrency);
            
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = createErrorResponse(
                "Dados inválidos",
                e.getMessage(),
                Arrays.asList(
                    "Verifique se todos os campos obrigatórios estão preenchidos",
                    "Código da moeda deve ter no máximo 10 caracteres",
                    "Nome da moeda deve ter no máximo 100 caracteres",
                    "Código e nome da moeda são obrigatórios"
                ),
                "Corrija os dados e tente novamente"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            
        } catch (CurrencyAlreadyExistsException e) {
            ErrorResponse errorResponse = createErrorResponse(
                "Moeda já existe",
                e.getMessage(),
                Arrays.asList(
                    "Verifique se já existe uma moeda com este código",
                    "Use um código único para a nova moeda",
                    "Consulte a lista de moedas existentes"
                ),
                "Use GET /api/v1/currencies para ver moedas existentes"
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
            
        } catch (Exception e) {
            ErrorResponse errorResponse = createErrorResponse(
                "Erro interno",
                "Erro inesperado ao criar moeda: " + e.getMessage(),
                Arrays.asList(
                    "Tente novamente em alguns instantes",
                    "Verifique se os dados estão corretos",
                    "Entre em contato com o suporte se o problema persistir"
                ),
                "Tente novamente ou entre em contato com o suporte"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
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
        CurrencyInfo updatedCurrency = currencyService.updateCurrency(code, currencyInfo);
        return ResponseEntity.ok(updatedCurrency);
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
        currencyService.deactivateCurrency(code);
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