package com.product.repository;

import com.product.domain.entity.Product;
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
 * Repository para operações de persistência de Product
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // ===== MÉTODOS BÁSICOS =====
    
    /**
     * Busca produto ativo por nome
     */
    @Query(value = "SELECT * FROM products WHERE name = :name AND is_active = true", nativeQuery = true)
    Optional<Product> findActiveByName(@Param("name") String name);
    
    /**
     * Verifica se existe produto ativo com o nome
     */
    @Query(value = "SELECT COUNT(*) > 0 FROM products WHERE name = :name AND is_active = true", nativeQuery = true)
    boolean existsActiveByName(@Param("name") String name);
    
    /**
     * Verifica se existe produto ativo com o nome no reino
     */
    @Query(value = "SELECT COUNT(*) > 0 FROM products WHERE name = :name AND kingdom_id = :kingdomId AND is_active = true", nativeQuery = true)
    boolean existsActiveByNameAndKingdom(@Param("name") String name, @Param("kingdomId") Long kingdomId);
    
    /**
     * Lista todos os produtos ativos
     */
    @Query(value = "SELECT * FROM products WHERE is_active = true ORDER BY name", nativeQuery = true)
    List<Product> findAllActive();
    
    /**
     * Lista produtos ativos com paginação
     */
    @Query(value = "SELECT * FROM products WHERE is_active = true", nativeQuery = true)
    Page<Product> findAllActiveWithPagination(Pageable pageable);
    
    // ===== MÉTODOS DE CONSULTA ESPECÍFICA =====
    
    /**
     * Lista produtos por categoria
     */
    @Query(value = "SELECT * FROM products WHERE category = :category AND is_active = true ORDER BY name", nativeQuery = true)
    List<Product> findByCategory(@Param("category") String category);
    
    /**
     * Lista produtos por reino
     */
    @Query(value = "SELECT * FROM products WHERE kingdom_id = :kingdomId AND is_active = true ORDER BY name", nativeQuery = true)
    List<Product> findByKingdomId(@Param("kingdomId") Long kingdomId);
    
    /**
     * Lista produtos de alta demanda
     */
    @Query(value = "SELECT * FROM products WHERE demand_quantifier >= 2.0 AND is_active = true ORDER BY demand_quantifier DESC", nativeQuery = true)
    List<Product> findHighDemandProducts();
    
    /**
     * Lista produtos de baixa demanda
     */
    @Query(value = "SELECT * FROM products WHERE demand_quantifier <= 0.5 AND is_active = true ORDER BY demand_quantifier ASC", nativeQuery = true)
    List<Product> findLowDemandProducts();
    
    /**
     * Lista produtos de alta qualidade
     */
    @Query(value = "SELECT * FROM products WHERE quality_qualifier >= 2.0 AND is_active = true ORDER BY quality_qualifier DESC", nativeQuery = true)
    List<Product> findHighQualityProducts();
    
    /**
     * Lista produtos de baixa qualidade
     */
    @Query(value = "SELECT * FROM products WHERE quality_qualifier <= 0.5 AND is_active = true ORDER BY quality_qualifier ASC", nativeQuery = true)
    List<Product> findLowQualityProducts();
    
    /**
     * Lista produtos de reinos proprietários
     */
    @Query(value = """
        SELECT p.* FROM products p 
        INNER JOIN kingdoms k ON p.kingdom_id = k.id 
        WHERE k.is_owner = true AND p.is_active = true AND k.is_active = true 
        ORDER BY p.name
        """, nativeQuery = true)
    List<Product> findProductsFromOwnerKingdoms();
    
    /**
     * Busca produtos por range de valor base
     */
    @Query(value = "SELECT * FROM products WHERE base_value BETWEEN :minValue AND :maxValue AND is_active = true ORDER BY base_value DESC", nativeQuery = true)
    List<Product> findByBaseValueRange(@Param("minValue") BigDecimal minValue, @Param("maxValue") BigDecimal maxValue);
    
    /**
     * Busca produtos por range de demanda
     */
    @Query(value = "SELECT * FROM products WHERE demand_quantifier BETWEEN :minDemand AND :maxDemand AND is_active = true ORDER BY demand_quantifier DESC", nativeQuery = true)
    List<Product> findByDemandRange(@Param("minDemand") BigDecimal minDemand, @Param("maxDemand") BigDecimal maxDemand);
    
    /**
     * Busca produtos por range de qualidade
     */
    @Query(value = "SELECT * FROM products WHERE quality_qualifier BETWEEN :minQuality AND :maxQuality AND is_active = true ORDER BY quality_qualifier DESC", nativeQuery = true)
    List<Product> findByQualityRange(@Param("minQuality") BigDecimal minQuality, @Param("maxQuality") BigDecimal maxQuality);
    
    // ===== MÉTODOS DE CONTAGEM =====
    
    /**
     * Conta produtos ativos
     */
    @Query(value = "SELECT COUNT(*) FROM products WHERE is_active = true", nativeQuery = true)
    long countActive();
    
    /**
     * Conta produtos por categoria
     */
    @Query(value = "SELECT COUNT(*) FROM products WHERE category = :category AND is_active = true", nativeQuery = true)
    long countByCategory(@Param("category") String category);
    
    /**
     * Conta produtos por reino
     */
    @Query(value = "SELECT COUNT(*) FROM products WHERE kingdom_id = :kingdomId AND is_active = true", nativeQuery = true)
    long countByKingdomId(@Param("kingdomId") Long kingdomId);
    
    /**
     * Conta produtos de alta demanda
     */
    @Query(value = "SELECT COUNT(*) FROM products WHERE demand_quantifier >= 2.0 AND is_active = true", nativeQuery = true)
    long countHighDemandProducts();
    
    /**
     * Conta produtos de baixa demanda
     */
    @Query(value = "SELECT COUNT(*) FROM products WHERE demand_quantifier <= 0.5 AND is_active = true", nativeQuery = true)
    long countLowDemandProducts();
    
    /**
     * Conta produtos de alta qualidade
     */
    @Query(value = "SELECT COUNT(*) FROM products WHERE quality_qualifier >= 2.0 AND is_active = true", nativeQuery = true)
    long countHighQualityProducts();
    
    /**
     * Conta produtos de baixa qualidade
     */
    @Query(value = "SELECT COUNT(*) FROM products WHERE quality_qualifier <= 0.5 AND is_active = true", nativeQuery = true)
    long countLowQualityProducts();
    
    /**
     * Conta produtos de reinos proprietários
     */
    @Query(value = """
        SELECT COUNT(*) FROM products p 
        INNER JOIN kingdoms k ON p.kingdom_id = k.id 
        WHERE k.is_owner = true AND p.is_active = true AND k.is_active = true
        """, nativeQuery = true)
    long countProductsFromOwnerKingdoms();
    
    // ===== MÉTODOS DE VALIDAÇÃO =====
    
    /**
     * Verifica se existe produto ativo com o ID
     */
    @Query(value = "SELECT COUNT(*) > 0 FROM products WHERE id = :id AND is_active = true", nativeQuery = true)
    boolean existsActiveById(@Param("id") Long id);
    
    /**
     * Verifica se um produto é de um reino proprietário
     */
    @Query(value = """
        SELECT COUNT(*) > 0 FROM products p 
        INNER JOIN kingdoms k ON p.kingdom_id = k.id 
        WHERE p.id = :productId AND k.is_owner = true AND p.is_active = true AND k.is_active = true
        """, nativeQuery = true)
    boolean isFromOwnerKingdom(@Param("productId") Long productId);
    
    /**
     * Verifica se um produto tem alta demanda
     */
    @Query(value = "SELECT demand_quantifier >= 2.0 FROM products WHERE id = :id AND is_active = true", nativeQuery = true)
    Optional<Boolean> isHighDemand(@Param("id") Long id);
    
    /**
     * Verifica se um produto tem alta qualidade
     */
    @Query(value = "SELECT quality_qualifier >= 2.0 FROM products WHERE id = :id AND is_active = true", nativeQuery = true)
    Optional<Boolean> isHighQuality(@Param("id") Long id);
} 