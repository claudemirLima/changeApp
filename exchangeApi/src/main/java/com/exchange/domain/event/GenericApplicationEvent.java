package com.exchange.domain.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericApplicationEvent implements Serializable {
    
    @JsonProperty("event_name")
    private String eventName;
    
    @JsonProperty("event_data")
    private Map<String, Object> eventData;
    
    @JsonProperty("timestamp")
    private String timestamp;
    
    @JsonProperty("correlation_id")
    private String correlationId;
    
    @JsonProperty("command_id")
    private String commandId;

} 