package com.exchange.controller;

import com.exchange.domain.dto.ConversionRequest;
import com.exchange.domain.dto.ConversionResponse;
import com.exchange.domain.dto.ErrorResponse;
import com.exchange.service.ConversionService;
import com.exchange.domain.exception.CurrencyNotFoundException;
import com.exchange.domain.exception.ExchangeRateNotFoundException;
import com.exchange.domain.exception.ProductExchangeRateNotFoundException;
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
@RequestMapping("/api/v1/convert")
@Tag(name = "Conversão de Moedas", description = "Endpoints para conversão de moedas")
public class ConversionController {
    
    @Autowired
    private ConversionService conversionService;
    
    @PostMapping
    @Operation(
        summary = "Converter moeda",
        description = "Realiza conversão de moeda usando a estratégia mais apropriada baseada nos parâmetros fornecidos"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Conversão realizada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ConversionResponse.class),
                examples = @ExampleObject(
                    name = "Conversão Aprovada",
                    value = """
                    {
                      "originalAmount": 100.00,
                      "convertedAmount": 250.00,
                      "rate": 2.5,
                      "fromCurrencyCode": "ORO",
                      "toCurrencyCode": "TIB",
                      "status": "APPROVED",
                      "reason": "Taxa dentro dos parâmetros normais",
                      "riskScore": 0.2,
                      "warnings": [],
                      "recommendations": ["Transação recomendada"],
                      "canProceed": true,
                      "requiresApproval": false
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parâmetros inválidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Parâmetros Inválidos",
                    value = """
                    {
                      "message": "Parâmetros inválidos",
                      "details": "Valor deve ser maior que zero",
                      "suggestions": [
                        "Verifique se todos os campos obrigatórios estão preenchidos",
                        "Valor deve ser maior que zero",
                        "Moedas de origem e destino não podem ser iguais",
                        "Códigos das moedas são obrigatórios"
                      ],
                      "action": "Corrija os dados e tente novamente",
                      "timestamp": "2024-01-15T10:30:00"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Moeda ou taxa não encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Moeda Não Encontrada",
                    value = """
                    {
                      "message": "Moeda não encontrada",
                      "details": "Moeda ativa não encontrada: INVALID",
                      "suggestions": [
                        "Verifique se os códigos das moedas estão corretos",
                        "Consulte a lista de moedas disponíveis",
                        "Verifique se as moedas estão ativas"
                      ],
                      "action": "Use GET /api/v1/currencies para ver moedas disponíveis",
                      "timestamp": "2024-01-15T10:30:00"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "422",
            description = "Conversão não aprovada pelo guard rail",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Conversão Não Aprovada",
                    value = """
                    {
                      "originalAmount": 100.00,
                      "convertedAmount": 150.00,
                      "rate": 1.5,
                      "fromCurrencyCode": "ORO",
                      "toCurrencyCode": "TIB",
                      "status": "NOT_APPROVED",
                      "reason": "Taxa muito desfavorável (40.0% de variação)",
                      "riskScore": 0.8,
                      "warnings": ["Taxa anormalmente desfavorável"],
                      "recommendations": ["Aguarde melhor momento para conversão"],
                      "canProceed": false,
                      "requiresApproval": false
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<?> convert(
        @Parameter(
            description = "Dados da conversão",
            required = true,
            content = @Content(
                schema = @Schema(implementation = ConversionRequest.class),
                examples = @ExampleObject(
                    name = "Conversão Simples",
                    value = """
                    {
                      "fromCurrencyCode": "ORO",
                      "toCurrencyCode": "TIB",
                      "amount": 100.00
                    }
                    """
                )
            )
        )
        @RequestBody ConversionRequest request
    ) {
        try {
            ConversionResponse response = conversionService.convert(request);
            
            // Retorna 422 se a conversão não foi aprovada
            if (!response.isCanProceed()) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = createErrorResponse(
                "Parâmetros inválidos",
                e.getMessage(),
                Arrays.asList(
                    "Verifique se todos os campos obrigatórios estão preenchidos",
                    "Valor deve ser maior que zero",
                    "Moedas de origem e destino não podem ser iguais",
                    "Códigos das moedas são obrigatórios"
                ),
                "Corrija os dados e tente novamente"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            
        } catch (CurrencyNotFoundException e) {
            ErrorResponse errorResponse = createErrorResponse(
                "Moeda não encontrada",
                e.getMessage(),
                Arrays.asList(
                    "Verifique se os códigos das moedas estão corretos",
                    "Consulte a lista de moedas disponíveis",
                    "Verifique se as moedas estão ativas"
                ),
                "Use GET /api/v1/currencies para ver moedas disponíveis"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            
        } catch (ExchangeRateNotFoundException e) {
            ErrorResponse errorResponse = createErrorResponse(
                "Taxa de câmbio não encontrada",
                e.getMessage(),
                Arrays.asList(
                    "Verifique se existe uma taxa ativa para este par de moedas",
                    "Consulte as taxas de câmbio disponíveis",
                    "Verifique se as moedas estão ativas"
                ),
                "Use GET /api/v1/exchange-rates para ver taxas disponíveis"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            
        } catch (ProductExchangeRateNotFoundException e) {
            ErrorResponse errorResponse = createErrorResponse(
                "Taxa de produto não encontrada",
                e.getMessage(),
                Arrays.asList(
                    "Verifique se existe uma taxa específica para este produto",
                    "Consulte as taxas de produto disponíveis",
                    "Verifique se o produto está ativo"
                ),
                "Use GET /api/v1/products para ver produtos disponíveis"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            
        } catch (Exception e) {
            ErrorResponse errorResponse = createErrorResponse(
                "Erro interno",
                "Erro inesperado na conversão: " + e.getMessage(),
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
    
    @PostMapping("/product")
    @Operation(
        summary = "Converter moeda por produto",
        description = "Realiza conversão de moeda considerando multiplicadores específicos do produto"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Conversão realizada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ConversionResponse.class),
                examples = @ExampleObject(
                    name = "Conversão com Produto Aprovada",
                    value = """
                    {
                      "originalAmount": 100.00,
                      "convertedAmount": 275.00,
                      "rate": 2.5,
                      "fromCurrencyCode": "ORO",
                      "toCurrencyCode": "TIB",
                      "status": "APPROVED",
                      "reason": "Taxa dentro dos parâmetros normais",
                      "riskScore": 0.2,
                      "warnings": [],
                      "recommendations": ["Transação recomendada"],
                      "canProceed": true,
                      "requiresApproval": false
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Parâmetros inválidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Parâmetros Inválidos",
                    value = """
                    {
                      "message": "Parâmetros inválidos",
                      "details": "ID do produto é obrigatório para conversão com produto",
                      "suggestions": [
                        "Verifique se todos os campos obrigatórios estão preenchidos",
                        "Valor deve ser maior que zero",
                        "Moedas de origem e destino não podem ser iguais",
                        "Códigos das moedas são obrigatórios",
                        "ID do produto é obrigatório para conversão com produto"
                      ],
                      "action": "Corrija os dados e tente novamente",
                      "timestamp": "2024-01-15T10:30:00"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Moeda, taxa ou produto não encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "Taxa de Produto Não Encontrada",
                    value = """
                    {
                      "message": "Taxa de produto não encontrada",
                      "details": "Taxa de produto não encontrada para produto ID: 999",
                      "suggestions": [
                        "Verifique se existe uma taxa específica para este produto",
                        "Consulte as taxas de produto disponíveis",
                        "Verifique se o produto está ativo"
                      ],
                      "action": "Use GET /api/v1/products para ver produtos disponíveis",
                      "timestamp": "2024-01-15T10:30:00"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "422", 
            description = "Conversão não aprovada pelo guard rail",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ConversionResponse.class),
                examples = @ExampleObject(
                    name = "Conversão Não Aprovada",
                    value = """
                    {
                      "originalAmount": 100.00,
                      "convertedAmount": 150.00,
                      "rate": 1.5,
                      "fromCurrencyCode": "ORO",
                      "toCurrencyCode": "TIB",
                      "status": "NOT_APPROVED",
                      "reason": "Taxa muito desfavorável (40.0% de variação)",
                      "riskScore": 0.8,
                      "warnings": ["Taxa anormalmente desfavorável"],
                      "recommendations": ["Aguarde melhor momento para conversão"],
                      "canProceed": false,
                      "requiresApproval": false
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<?> convertWithProduct(
        @Parameter(
            description = "Dados da conversão com produto",
            required = true,
            content = @Content(
                schema = @Schema(implementation = ConversionRequest.class),
                examples = @ExampleObject(
                    name = "Conversão com Produto",
                    value = """
                    {
                      "fromCurrencyCode": "ORO",
                      "toCurrencyCode": "TIB",
                      "amount": 100.00,
                      "productId": 1,
                      "conversionDate": "2024-01-15"
                    }
                    """
                )
            )
        )
        @RequestBody ConversionRequest request
    ) {
        try {
            ConversionResponse response = conversionService.convert(request);
            
            if (!response.isCanProceed()) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = createErrorResponse(
                "Parâmetros inválidos",
                e.getMessage(),
                Arrays.asList(
                    "Verifique se todos os campos obrigatórios estão preenchidos",
                    "Valor deve ser maior que zero",
                    "Moedas de origem e destino não podem ser iguais",
                    "Códigos das moedas são obrigatórios",
                    "ID do produto é obrigatório para conversão com produto"
                ),
                "Corrija os dados e tente novamente"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            
        } catch (CurrencyNotFoundException e) {
            ErrorResponse errorResponse = createErrorResponse(
                "Moeda não encontrada",
                e.getMessage(),
                Arrays.asList(
                    "Verifique se os códigos das moedas estão corretos",
                    "Consulte a lista de moedas disponíveis",
                    "Verifique se as moedas estão ativas"
                ),
                "Use GET /api/v1/currencies para ver moedas disponíveis"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            
        } catch (ExchangeRateNotFoundException e) {
            ErrorResponse errorResponse = createErrorResponse(
                "Taxa de câmbio não encontrada",
                e.getMessage(),
                Arrays.asList(
                    "Verifique se existe uma taxa ativa para este par de moedas",
                    "Consulte as taxas de câmbio disponíveis",
                    "Verifique se as moedas estão ativas"
                ),
                "Use GET /api/v1/exchange-rates para ver taxas disponíveis"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            
        } catch (ProductExchangeRateNotFoundException e) {
            ErrorResponse errorResponse = createErrorResponse(
                "Taxa de produto não encontrada",
                e.getMessage(),
                Arrays.asList(
                    "Verifique se existe uma taxa específica para este produto",
                    "Consulte as taxas de produto disponíveis",
                    "Verifique se o produto está ativo"
                ),
                "Use GET /api/v1/products para ver produtos disponíveis"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            
        } catch (Exception e) {
            ErrorResponse errorResponse = createErrorResponse(
                "Erro interno",
                "Erro inesperado na conversão com produto: " + e.getMessage(),
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
    
    // ===== MÉTODOS UTILITÁRIOS =====
    
    /**
     * Cria uma resposta de erro padronizada
     */
    private ErrorResponse createErrorResponse(String message, String details, 
                                           List<String> suggestions, String action) {
        return new ErrorResponse(message, details, suggestions, action);
    }
} 