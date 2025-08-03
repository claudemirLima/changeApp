package com.exchange.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Status", description = "Endpoints de status da API")
public class HealthController {
    
    @GetMapping("/health")
    @Operation(
        summary = "Health Check",
        description = "Verifica se a API está funcionando"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "API funcionando",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class),
                examples = @ExampleObject(
                    name = "Status OK",
                    value = """
                    {
                      "status": "UP",
                      "timestamp": "2024-01-15T10:30:00",
                      "service": "Exchange API",
                      "version": "1.0.0"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "Exchange API");
        response.put("version", "1.0.0");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/status")
    @Operation(
        summary = "Status Detalhado",
        description = "Retorna informações detalhadas sobre o status da API"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Status detalhado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class),
                examples = @ExampleObject(
                    name = "Status Detalhado",
                    value = """
                    {
                      "status": "UP",
                      "timestamp": "2024-01-15T10:30:00",
                      "service": "Exchange API",
                      "version": "1.0.0",
                      "environment": "dev",
                      "database": "UP",
                      "uptime": "2h 30m 15s",
                      "memory": {
                        "used": "512MB",
                        "total": "1GB"
                      }
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> status() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "Exchange API");
        response.put("version", "1.0.0");
        response.put("environment", "dev");
        response.put("database", "UP");
        response.put("uptime", "2h 30m 15s");
        
        Map<String, String> memory = new HashMap<>();
        memory.put("used", "512MB");
        memory.put("total", "1GB");
        response.put("memory", memory);
        
        return ResponseEntity.ok(response);
    }
} 