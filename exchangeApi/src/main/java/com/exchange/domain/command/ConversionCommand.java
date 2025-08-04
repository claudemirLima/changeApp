package com.exchange.domain.command;

import com.exchange.domain.dto.ConversionRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversionCommand extends AbstractCommand {

    @JsonProperty("transaction_id")
    public String transactionId;
    
    @JsonProperty("from_currency_code")
    public String fromCurrencyCode;
    
    @JsonProperty("to_currency_code")
    public String toCurrencyCode;
    
    @JsonProperty("quantity_product")
    public Integer quantityProduct;
    
    @JsonProperty("quantity_currency")
    public Float quantityCurrency;
    
    @JsonProperty("product_id")
    public Long productId;
    
    @JsonProperty("kingdom_id")
    public Long kingdomId;
    
    @JsonProperty("conversion_date")
    public String conversionDate;

    @Builder
    ConversionCommand(String commandId, String correlationId, String timestamp,
                      String transactionId, String fromCurrencyCode, String toCurrencyCode,
                      Integer quantityProduct, Float quantityCurrency, Long productId, 
                      Long kingdomId, String conversionDate) {
        super(commandId, correlationId, timestamp);
        this.transactionId = transactionId;
        this.fromCurrencyCode = fromCurrencyCode;
        this.toCurrencyCode = toCurrencyCode;
        this.quantityProduct = quantityProduct;
        this.quantityCurrency = quantityCurrency;
        this.productId = productId;
        this.kingdomId = kingdomId;
        this.conversionDate = conversionDate;
    }
} 