package com.product.service.impl;

import com.product.domain.entity.Kingdom;
import com.product.domain.exception.KingdomAlreadyExistsException;
import com.product.domain.exception.KingdomNotFoundException;
import com.product.domain.exception.KingdomOperationException;
import com.product.domain.mapper.KingdomMapper;
import com.product.repository.KingdomRepository;
import com.product.service.KingdomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementação do serviço de gerenciamento de Reinos
 */
@Service
public class KingdomServiceImpl implements KingdomService {
    
    @Autowired
    private KingdomRepository kingdomRepository;
    
    // ===== MÉTODOS BÁSICOS =====
    
    @Override
    @Transactional
    public Kingdom createKingdom(String name, String description, BigDecimal qualityRate, Boolean isOwner) {
        // Verificar se já existe reino com o nome
        if (kingdomRepository.existsActiveByName(name)) {
            throw new KingdomAlreadyExistsException(name);
        }
        
        // Criar novo reino
        Kingdom kingdom = Kingdom.builder()
                .name(name)
                .createdAt(LocalDateTime.now())
                .description(description)
                .isOwner(isOwner)
                .qualityRate(qualityRate)
                .build();

        
        try {
            return kingdomRepository.save(kingdom);
        } catch (DataIntegrityViolationException e) {
            throw new KingdomOperationException("criação", "Erro de integridade de dados");
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Kingdom getKingdomById(Long id) {
        return kingdomRepository.findById(id)
            .orElseThrow(() -> new KingdomNotFoundException(id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Kingdom getKingdomByName(String name) {
        return kingdomRepository.findActiveByName(name)
            .orElseThrow(() -> new KingdomNotFoundException(name, true));
    }
    
    @Override
    @Transactional
    public Kingdom updateKingdom(Long id, String name, String description, BigDecimal qualityRate, Boolean isOwner) {
        // Buscar reino existente
        Kingdom kingdom = getKingdomById(id);
        
        // Verificar se o nome foi alterado e se já existe outro reino com o novo nome
        if (!kingdom.getName().equals(name) && kingdomRepository.existsActiveByName(name)) {
            throw new KingdomAlreadyExistsException(name);
        }
        
        // Atualizar dados
        kingdom.setName(name);
        kingdom.setDescription(description);
        kingdom.setQualityRate(qualityRate);
        kingdom.setIsOwner(isOwner);
        
        try {
            return kingdomRepository.save(kingdom);
        } catch (DataIntegrityViolationException e) {
            throw new KingdomOperationException("atualização", "Erro de integridade de dados");
        }
    }
    
    @Override
    @Transactional
    public void activateKingdom(Long id) {
        Kingdom kingdom = getKingdomById(id);
        kingdom.activate();
        kingdomRepository.save(kingdom);
    }
    
    @Override
    @Transactional
    public void deactivateKingdom(Long id) {
        Kingdom kingdom = getKingdomById(id);
        kingdom.deactivate();
        kingdomRepository.save(kingdom);
    }
    
    @Override
    @Transactional
    public void deleteKingdom(Long id) {
        Kingdom kingdom = getKingdomById(id);
        kingdom.deactivate();
        kingdomRepository.save(kingdom);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Kingdom> getAllActiveKingdoms(Pageable pageable) {
        return kingdomRepository.findAllActiveWithPagination(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Kingdom> getAllActiveKingdoms() {
        return kingdomRepository.findAllActive();
    }
    
    // ===== MÉTODOS DE CONSULTA ESPECÍFICA =====
    
    @Override
    @Transactional(readOnly = true)
    public List<Kingdom> getOwnerKingdoms() {
        return kingdomRepository.findOwnerKingdoms();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Kingdom> getHighQualityKingdoms() {
        return kingdomRepository.findHighQualityKingdoms();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Kingdom> getLowQualityKingdoms() {
        return kingdomRepository.findLowQualityKingdoms();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Kingdom> getKingdomsByQualityRange(BigDecimal minQuality, BigDecimal maxQuality) {
        if (minQuality == null || maxQuality == null) {
            throw new IllegalArgumentException("Range de qualidade não pode ser nulo");
        }
        
        if (minQuality.compareTo(maxQuality) > 0) {
            throw new IllegalArgumentException("Qualidade mínima não pode ser maior que a máxima");
        }
        
        return kingdomRepository.findByQualityRange(minQuality, maxQuality);
    }
    
    // ===== MÉTODOS DE CONTAGEM =====
    
    @Override
    @Transactional(readOnly = true)
    public long countActiveKingdoms() {
        return kingdomRepository.countActive();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countOwnerKingdoms() {
        return kingdomRepository.countOwnerKingdoms();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countHighQualityKingdoms() {
        return kingdomRepository.countHighQualityKingdoms();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countLowQualityKingdoms() {
        return kingdomRepository.countLowQualityKingdoms();
    }
    
    // ===== MÉTODOS DE VALIDAÇÃO =====
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsAndActive(Long id) {
        return kingdomRepository.existsActiveById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return kingdomRepository.existsActiveByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasOwnerKingdom() {
        return kingdomRepository.hasOwnerKingdom();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isOwnerKingdom(Long id) {
        return kingdomRepository.isOwnerKingdom(id)
            .orElse(false);
    }
    
    // ===== MÉTODOS PRIVADOS =====
    
    /**
     * Valida os dados de entrada para criação/atualização de reino
     */
    private void validateKingdomData(String name, BigDecimal qualityRate, Boolean isOwner) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do reino é obrigatório");
        }
        
        if (name.trim().length() < 2) {
            throw new IllegalArgumentException("Nome do reino deve ter pelo menos 2 caracteres");
        }
        
        if (name.trim().length() > 100) {
            throw new IllegalArgumentException("Nome do reino deve ter no máximo 100 caracteres");
        }
        
        if (qualityRate == null) {
            throw new IllegalArgumentException("Rate de qualidade é obrigatório");
        }
        
        if (qualityRate.compareTo(BigDecimal.valueOf(0.1)) < 0) {
            throw new IllegalArgumentException("Rate de qualidade deve ser pelo menos 0.1");
        }
        
        if (qualityRate.compareTo(BigDecimal.valueOf(10.0)) > 0) {
            throw new IllegalArgumentException("Rate de qualidade deve ser no máximo 10.0");
        }
        
        if (isOwner == null) {
            throw new IllegalArgumentException("Indicador de proprietário é obrigatório");
        }
    }
} 