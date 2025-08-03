package com.exchange.controller;

import com.exchange.domain.dto.ConversionRequest;
import com.exchange.domain.dto.ConversionResponse;
import com.exchange.service.ConversionService;
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
                examples = @ExampleObject(
                    name = "Erro de Validação",
                    value = """
                    {
                      "timestamp": "2024-01-15T10:30:00",
                      "status": 400,
                      "error": "Bad Request",
                      "message": "Valor deve ser maior que zero"
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
    public ResponseEntity<ConversionResponse> convert(
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
        ConversionResponse response = conversionService.convert(request);
        
        // Retorna 422 se a conversão não foi aprovada
        if (!response.isCanProceed()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/product")
    @Operation(
        summary = "Converter moeda por produto",
        description = "Realiza conversão de moeda considerando multiplicadores específicos do produto"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Conversão realizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
        @ApiResponse(responseCode = "422", description = "Conversão não aprovada pelo guard rail")
    })
    public ResponseEntity<ConversionResponse> convertWithProduct(
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
        ConversionResponse response = conversionService.convert(request);
        
        if (!response.isCanProceed()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }
        
        return ResponseEntity.ok(response);
    }
} 