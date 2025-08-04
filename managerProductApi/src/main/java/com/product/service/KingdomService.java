package com.product.service;

import com.product.domain.entity.Kingdom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interface para serviços de gerenciamento de Reinos
 */
public interface KingdomService {
    

    /**
     * Cria um novo reino
     */
    Kingdom createKingdom(String name, String description, BigDecimal qualityRate, Boolean isOwner);
    
    /**
     * Busca um reino por ID
     */
    Kingdom getKingdomById(Long id);
    
    /**
     * Busca um reino por nome
     */
    Kingdom getKingdomByName(String name);
    
    /**
     * Atualiza um reino existente
     */
    Kingdom updateKingdom(Long id, String name, String description, BigDecimal qualityRate, Boolean isOwner);
    
    /**
     * Ativa um reino desativado
     */
    void activateKingdom(Long id);
    
    /**
     * Desativa um reino ativo
     */
    void deactivateKingdom(Long id);
    
    /**
     * Remove um reino (soft delete)
     */
    void deleteKingdom(Long id);
    
    /**
     * Lista todos os reinos ativos com paginação
     */
    Page<Kingdom> getAllActiveKingdoms(Pageable pageable);


} 