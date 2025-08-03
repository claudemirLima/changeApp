package com.product.repository;

import com.product.domain.entity.Kingdom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de persistência de Kingdom
 */
@Repository
public interface KingdomRepository extends JpaRepository<Kingdom, Long> {
    
    // ===== MÉTODOS BÁSICOS =====
    
    /**
     * Busca reino ativo por nome
     */
    @Query(value = "SELECT * FROM kingdoms WHERE name = :name AND is_active = true", nativeQuery = true)
    Optional<Kingdom> findActiveByName(@Param("name") String name);
    
    /**
     * Verifica se existe reino ativo com o nome
     */
    @Query(value = "SELECT COUNT(*) > 0 FROM kingdoms WHERE name = :name AND is_active = true", nativeQuery = true)
    boolean existsActiveByName(@Param("name") String name);
    
    /**
     * Lista todos os reinos ativos
     */
    @Query(value = "SELECT * FROM kingdoms WHERE is_active = true ORDER BY name", nativeQuery = true)
    List<Kingdom> findAllActive();
    
    /**
     * Lista reinos ativos com paginação
     */
    @Query(value = "SELECT * FROM kingdoms WHERE is_active = true", nativeQuery = true)
    Page<Kingdom> findAllActiveWithPagination(Pageable pageable);
    
    // ===== MÉTODOS DE CONSULTA ESPECÍFICA =====
    
    /**
     * Lista reinos proprietários
     */
    @Query(value = "SELECT * FROM kingdoms WHERE is_owner = true AND is_active = true ORDER BY name", nativeQuery = true)
    List<Kingdom> findOwnerKingdoms();
    
    /**
     * Lista reinos de alta qualidade
     */
    @Query(value = "SELECT * FROM kingdoms WHERE quality_rate >= 1.5 AND is_active = true ORDER BY quality_rate DESC", nativeQuery = true)
    List<Kingdom> findHighQualityKingdoms();
    
    /**
     * Lista reinos de baixa qualidade
     */
    @Query(value = "SELECT * FROM kingdoms WHERE quality_rate <= 0.5 AND is_active = true ORDER BY quality_rate ASC", nativeQuery = true)
    List<Kingdom> findLowQualityKingdoms();
    
    /**
     * Busca reinos por range de qualidade
     */
    @Query(value = "SELECT * FROM kingdoms WHERE quality_rate BETWEEN :minQuality AND :maxQuality AND is_active = true ORDER BY quality_rate DESC", nativeQuery = true)
    List<Kingdom> findByQualityRange(@Param("minQuality") BigDecimal minQuality, @Param("maxQuality") BigDecimal maxQuality);
    
    // ===== MÉTODOS DE CONTAGEM =====
    
    /**
     * Conta reinos ativos
     */
    @Query(value = "SELECT COUNT(*) FROM kingdoms WHERE is_active = true", nativeQuery = true)
    long countActive();
    
    /**
     * Conta reinos proprietários
     */
    @Query(value = "SELECT COUNT(*) FROM kingdoms WHERE is_owner = true AND is_active = true", nativeQuery = true)
    long countOwnerKingdoms();
    
    /**
     * Conta reinos de alta qualidade
     */
    @Query(value = "SELECT COUNT(*) FROM kingdoms WHERE quality_rate >= 1.5 AND is_active = true", nativeQuery = true)
    long countHighQualityKingdoms();
    
    /**
     * Conta reinos de baixa qualidade
     */
    @Query(value = "SELECT COUNT(*) FROM kingdoms WHERE quality_rate <= 0.5 AND is_active = true", nativeQuery = true)
    long countLowQualityKingdoms();
    
    // ===== MÉTODOS DE VALIDAÇÃO =====
    
    /**
     * Verifica se existe reino ativo com o ID
     */
    @Query(value = "SELECT COUNT(*) > 0 FROM kingdoms WHERE id = :id AND is_active = true", nativeQuery = true)
    boolean existsActiveById(@Param("id") Long id);
    
    /**
     * Verifica se existe algum reino proprietário
     */
    @Query(value = "SELECT COUNT(*) > 0 FROM kingdoms WHERE is_owner = true AND is_active = true", nativeQuery = true)
    boolean hasOwnerKingdom();
    
    /**
     * Verifica se um reino é proprietário
     */
    @Query(value = "SELECT is_owner FROM kingdoms WHERE id = :id AND is_active = true", nativeQuery = true)
    Optional<Boolean> isOwnerKingdom(@Param("id") Long id);
} 