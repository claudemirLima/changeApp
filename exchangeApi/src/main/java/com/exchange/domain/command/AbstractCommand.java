package com.exchange.domain.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractCommand implements Serializable {
    @JsonProperty("command_id")
    protected String commandId;
    
    @JsonProperty("correlation_id")
    protected String correlationId;
    
    @JsonProperty("timestamp")
    protected String timestamp;
} 