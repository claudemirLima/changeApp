package com.transaction.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionRequestResponse {
    private UUID commandId;
    private String correlationId;
    private String status;
    private String message;
    private LocalDateTime requestedAt;
    private String statusUrl;
    
    public ConversionRequestResponse(UUID commandId, String correlationId) {
        this.commandId = commandId;
        this.correlationId = correlationId;
        this.status = "REQUESTED";
        this.message = "Conversão enviada para processamento";
        this.requestedAt = LocalDateTime.now();
        this.statusUrl = "/api/v1/transactions/conversion/status/" + commandId;
    }
} 