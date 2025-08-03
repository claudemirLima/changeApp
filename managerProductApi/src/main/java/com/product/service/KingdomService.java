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
    
    // ===== MÉTODOS BÁSICOS =====
    
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
    
    /**
     * Lista todos os reinos ativos
     */
    List<Kingdom> getAllActiveKingdoms();
    
    // ===== MÉTODOS DE CONSULTA ESPECÍFICA =====
    
    /**
     * Lista todos os reinos proprietários
     */
    List<Kingdom> getOwnerKingdoms();
    
    /**
     * Lista reinos de alta qualidade (qualityRate >= 1.5)
     */
    List<Kingdom> getHighQualityKingdoms();
    
    /**
     * Lista reinos de baixa qualidade (qualityRate <= 0.5)
     */
    List<Kingdom> getLowQualityKingdoms();
    
    /**
     * Busca reinos por range de qualidade
     */
    List<Kingdom> getKingdomsByQualityRange(BigDecimal minQuality, BigDecimal maxQuality);
    
    // ===== MÉTODOS DE CONTAGEM =====
    
    /**
     * Conta o número de reinos ativos
     */
    long countActiveKingdoms();
    
    /**
     * Conta o número de reinos proprietários
     */
    long countOwnerKingdoms();
    
    /**
     * Conta o número de reinos de alta qualidade
     */
    long countHighQualityKingdoms();
    
    /**
     * Conta o número de reinos de baixa qualidade
     */
    long countLowQualityKingdoms();
    
    // ===== MÉTODOS DE VALIDAÇÃO =====
    
    /**
     * Verifica se um reino existe e está ativo
     */
    boolean existsAndActive(Long id);
    
    /**
     * Verifica se um reino com o nome existe
     */
    boolean existsByName(String name);
    
    /**
     * Verifica se existe algum reino proprietário
     */
    boolean hasOwnerKingdom();
    
    /**
     * Verifica se um reino é proprietário
     */
    boolean isOwnerKingdom(Long id);
} 