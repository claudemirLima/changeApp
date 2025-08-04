package com.exchange.domain.mapper;

import com.exchange.domain.dto.CurrencyInfo;
import com.exchange.domain.dto.ExchangeRateRequest;
import com.exchange.domain.dto.ExchangeRateResponse;
import com.exchange.domain.dto.ExchangeRateSimpleResponse;
import com.exchange.domain.entity.ExchangeRate;
import com.exchange.domain.enums.TransactionStatus;
import com.exchange.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversão entre ExchangeRate (entidade) e DTOs
 * Conversão manual com busca de informações das moedas
 */
@Component
public class ExchangeRateMapper {
    
    @Autowired
    private CurrencyService currencyService;
    
    /**
     * Converte ExchangeRateRequest para ExchangeRate
     */
    public ExchangeRate requestToEntity(ExchangeRateRequest request) {
        if (request == null) {
            return null;
        }
        
        ExchangeRate entity = new ExchangeRate();
        entity.setFromCurrencyPrefix(request.getFromCurrencyCode());
        entity.setToCurrencyPrefix(request.getToCurrencyCode());
        entity.setRate(request.getRate());
        entity.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        
        return entity;
    }
    
    /**
     * Converte ExchangeRate para ExchangeRateResponse
     */
    public ExchangeRateResponse entityToResponse(ExchangeRate entity) {
        if (entity == null) {
            return null;
        }
        
        ExchangeRateResponse response = new ExchangeRateResponse();
        
        // Dados básicos
        response.setRate(entity.getRate());
        response.setLastUpdated(entity.getUpdatedAt() != null ? entity.getUpdatedAt() : entity.getCreatedAt());

        CurrencyInfo fromCurrency = currencyService.getCurrencyByPrefix(entity.getFromCurrencyPrefix());
        CurrencyInfo toCurrency = currencyService.getCurrencyByPrefix(entity.getToCurrencyPrefix());
        response.setFromCurrency(fromCurrency);
        response.setToCurrency(toCurrency);


        return response;
    }
    
    /**
     * Converte ExchangeRate para ExchangeRateSimpleResponse (sem guard rail)
     */
    public ExchangeRateSimpleResponse entityToSimpleResponse(ExchangeRate entity) {
        if (entity == null) {
            return null;
        }
        
        ExchangeRateSimpleResponse response = new ExchangeRateSimpleResponse();
        
        // Dados básicos
        response.setRate(entity.getRate());
        response.setLastUpdated(entity.getUpdatedAt() != null ? entity.getUpdatedAt() : entity.getCreatedAt());
        
        // Informações das moedas
        CurrencyInfo fromCurrency = currencyService.getCurrencyByPrefix(entity.getFromCurrencyPrefix());
        CurrencyInfo toCurrency = currencyService.getCurrencyByPrefix(entity.getToCurrencyPrefix());
        response.setFromCurrency(fromCurrency);
        response.setToCurrency(toCurrency);
        
        return response;
    }
    
    /**
     * Converte lista de ExchangeRate para lista de ExchangeRateSimpleResponse
     */
    public List<ExchangeRateSimpleResponse> entityListToSimpleResponseList(List<ExchangeRate> entities) {
        if (entities == null) {
            return new ArrayList<>();
        }
        
        return entities.stream()
            .map(this::entityToSimpleResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Converte lista de ExchangeRate para lista de ExchangeRateResponse
     */
    public List<ExchangeRateResponse> entityListToResponseList(List<ExchangeRate> entities) {
        if (entities == null) {
            return new ArrayList<>();
        }
        
        return entities.stream()
            .map(this::entityToResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Converte ExchangeRateResponse para ExchangeRate (para casos específicos)
     */
    public ExchangeRate responseToEntity(ExchangeRateResponse response) {
        if (response == null) {
            return null;
        }
        
        ExchangeRate entity = new ExchangeRate();
        
        // Dados básicos
        entity.setRate(response.getRate());
        
        // Informações das moedas (se disponíveis)
        if (response.getFromCurrency() != null) {
            entity.setFromCurrencyPrefix(response.getFromCurrency().getCode());
        }
        if (response.getToCurrency() != null) {
            entity.setToCurrencyPrefix(response.getToCurrency().getCode());
        }
        
        // Status
        entity.setIsActive(response.getStatus() == TransactionStatus.APPROVED);
        
        return entity;
    }
    
    /**
     * Converte lista de ExchangeRateResponse para lista de ExchangeRate
     */
    public List<ExchangeRate> responseListToEntityList(List<ExchangeRateResponse> responses) {
        if (responses == null) {
            return new ArrayList<>();
        }
        
        return responses.stream()
            .map(this::responseToEntity)
            .collect(Collectors.toList());
    }
}