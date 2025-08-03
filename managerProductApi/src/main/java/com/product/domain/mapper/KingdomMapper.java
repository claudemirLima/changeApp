package com.product.domain.mapper;

import com.product.domain.dto.KingdomRequest;
import com.product.domain.dto.KingdomResponse;
import com.product.domain.entity.Kingdom;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper manual para conversões entre Kingdom e seus DTOs
 */
public class KingdomMapper {
    
    /**
     * Converte Kingdom para KingdomResponse
     */
    public static KingdomResponse kingdomToKingdomResponse(Kingdom kingdom) {
        if (kingdom == null) {
            return null;
        }
        
        KingdomResponse response = new KingdomResponse();
        response.setId(kingdom.getId());
        response.setName(kingdom.getName());
        response.setDescription(kingdom.getDescription());
        response.setQualityRate(kingdom.getQualityRate());
        response.setIsOwner(kingdom.getIsOwner());
        response.setIsActive(kingdom.getIsActive());
        response.setCreatedAt(kingdom.getCreatedAt());
        response.setUpdatedAt(kingdom.getUpdatedAt());
        
        return response;
    }
    
    /**
     * Converte lista de Kingdom para lista de KingdomResponse
     */
    public static List<KingdomResponse> kingdomListToKingdomResponseList(List<Kingdom> kingdoms) {
        if (kingdoms == null) {
            return null;
        }
        
        return kingdoms.stream()
            .map(KingdomMapper::kingdomToKingdomResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Converte KingdomRequest para Kingdom (criação)
     */
    public static Kingdom kingdomRequestToKingdom(KingdomRequest request) {
        if (request == null) {
            return null;
        }
        
        Kingdom kingdom = new Kingdom();
        kingdom.setName(request.getName());
        kingdom.setDescription(request.getDescription());
        kingdom.setQualityRate(request.getQualityRate());
        kingdom.setIsOwner(request.getIsOwner());
        kingdom.setIsActive(true);
        kingdom.setCreatedAt(LocalDateTime.now());
        kingdom.setUpdatedAt(null);
        kingdom.setDeactivatedAt(null);
        
        return kingdom;
    }
    
    /**
     * Atualiza Kingdom com dados do KingdomRequest
     */
    public static void updateKingdomFromRequest(KingdomRequest request, Kingdom kingdom) {
        if (request == null || kingdom == null) {
            return;
        }
        
        kingdom.setName(request.getName());
        kingdom.setDescription(request.getDescription());
        kingdom.setQualityRate(request.getQualityRate());
        kingdom.setIsOwner(request.getIsOwner());
        kingdom.setUpdatedAt(LocalDateTime.now());
    }
    
    /**
     * Cria um novo Kingdom a partir de um existente (para cópia)
     */
    public static Kingdom copyKingdom(Kingdom kingdom) {
        if (kingdom == null) {
            return null;
        }
        
        Kingdom copy = new Kingdom();
        copy.setName(kingdom.getName());
        copy.setDescription(kingdom.getDescription());
        copy.setQualityRate(kingdom.getQualityRate());
        copy.setIsOwner(kingdom.getIsOwner());
        copy.setIsActive(kingdom.getIsActive());
        copy.setCreatedAt(LocalDateTime.now());
        copy.setUpdatedAt(null);
        copy.setDeactivatedAt(null);
        
        return copy;
    }
    
    /**
     * Converte Kingdom para KingdomResponse com validações
     */
    public static KingdomResponse kingdomToKingdomResponseWithValidation(Kingdom kingdom) {
        if (kingdom == null) {
            throw new IllegalArgumentException("Kingdom não pode ser nulo");
        }
        
        return kingdomToKingdomResponse(kingdom);
    }
    
    /**
     * Converte KingdomRequest para Kingdom com validações
     */
    public static Kingdom kingdomRequestToKingdomWithValidation(KingdomRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("KingdomRequest não pode ser nulo");
        }
        
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do reino é obrigatório");
        }
        
        if (request.getQualityRate() == null) {
            throw new IllegalArgumentException("Rate de qualidade é obrigatório");
        }
        
        if (request.getIsOwner() == null) {
            throw new IllegalArgumentException("Indicador de proprietário é obrigatório");
        }
        
        return kingdomRequestToKingdom(request);
    }
} 