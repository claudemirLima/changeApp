package com.exchange.domain.mapper;

import com.exchange.domain.dto.CurrencyInfo;
import com.exchange.domain.entity.Currency;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversão entre Currency (entidade) e DTOs
 */
public class CurrencyMapper {
    
    /**
     * Converte Currency para CurrencyInfo
     */
    public static CurrencyInfo entityToDto(Currency entity) {
        if (entity == null) {
            return null;
        }
        
        CurrencyInfo dto = new CurrencyInfo();
        dto.setCode(entity.getPrefix());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        
        return dto;
    }
    
    /**
     * Converte CurrencyInfo para Currency
     */
    public static Currency dtoToEntity(CurrencyInfo dto) {
        if (dto == null) {
            return null;
        }
        
        Currency entity = new Currency();
        entity.setPrefix(dto.getCode());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setIsActive(true);
        
        return entity;
    }
    
    /**
     * Converte lista de Currency para lista de CurrencyInfo
     */
    public static List<CurrencyInfo> entityListToDtoList(List<Currency> entities) {
        if (entities == null) {
            return List.of();
        }
        
        return entities.stream()
            .map(CurrencyMapper::entityToDto)
            .collect(Collectors.toList());
    }
    
    /**
     * Converte lista de CurrencyInfo para lista de Currency
     */
    public static List<Currency> dtoListToEntityList(List<CurrencyInfo> dtos) {
        if (dtos == null) {
            return List.of();
        }
        
        return dtos.stream()
            .map(CurrencyMapper::dtoToEntity)
            .collect(Collectors.toList());
    }
    
    /**
     * Converte CurrencyInfo para Currency (com validações)
     */
    public static Currency dtoToEntityWithValidation(CurrencyInfo dto) {
        Currency entity = dtoToEntity(dto);
        
        // Validações básicas
        if (entity.getPrefix() == null || entity.getPrefix().trim().isEmpty()) {
            throw new IllegalArgumentException("Código da moeda é obrigatório");
        }
        
        if (entity.getName() == null || entity.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da moeda é obrigatório");
        }
        
        if (entity.getPrefix().length() > 10) {
            throw new IllegalArgumentException("Código da moeda deve ter no máximo 10 caracteres");
        }
        
        if (entity.getName().length() > 100) {
            throw new IllegalArgumentException("Nome da moeda deve ter no máximo 100 caracteres");
        }
        
        return entity;
    }
} 