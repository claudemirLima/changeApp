package com.product.service;

import com.product.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interface para serviços de gerenciamento de Produtos
 */
public interface ProductService {

    /**
     * Cria um novo produto
     */
    Product createProduct(String name, String description, String category, 
                        BigDecimal baseValue, BigDecimal demandQuantifier, 
                        BigDecimal qualityQualifier, Long kingdomId);
    
    /**
     * Busca um produto por ID
     */
    Product getProductById(Long id);
    
    /**
     * Busca um produto por nome
     */
    Product getProductByName(String name);
    
    /**
     * Atualiza um produto existente
     */
    Product updateProduct(Long id, String name, String description, String category,
                        BigDecimal baseValue, BigDecimal demandQuantifier,
                        BigDecimal qualityQualifier, Long kingdomId);
    
    /**
     * Ativa um produto desativado
     */
    void activateProduct(Long id);
    
    /**
     * Desativa um produto ativo
     */
    void deactivateProduct(Long id);
    
    /**
     * Remove um produto (soft delete)
     */
    void deleteProduct(Long id);
    
    /**
     * Lista todos os produtos ativos com paginação
     */
    Page<Product> getAllActiveProducts(Pageable pageable);
    
    /**
     * Lista todos os produtos ativos
     */
    List<Product> getAllActiveProducts();
    
    // ===== MÉTODOS DE CONSULTA ESPECÍFICA =====
    
    /**
     * Lista produtos por categoria
     */
    List<Product> getProductsByCategory(String category);
    
    /**
     * Lista produtos por reino
     */
    List<Product> getProductsByKingdom(Long kingdomId);
    
    /**
     * Lista produtos de alta demanda (demandQuantifier >= 2.0)
     */
    List<Product> getHighDemandProducts();
    
    /**
     * Lista produtos de baixa demanda (demandQuantifier <= 0.5)
     */
    List<Product> getLowDemandProducts();
    
    /**
     * Lista produtos de alta qualidade (qualityQualifier >= 2.0)
     */
    List<Product> getHighQualityProducts();
    
    /**
     * Lista produtos de baixa qualidade (qualityQualifier <= 0.5)
     */
    List<Product> getLowQualityProducts();
    
    /**
     * Lista produtos de reinos proprietários
     */
    List<Product> getProductsFromOwnerKingdoms();
    
    /**
     * Busca produtos por range de valor base
     */
    List<Product> getProductsByBaseValueRange(BigDecimal minValue, BigDecimal maxValue);
    
    /**
     * Busca produtos por range de valor final calculado
     */
    List<Product> getProductsByFinalValueRange(BigDecimal minValue, BigDecimal maxValue);
    
    /**
     * Busca produtos por range de demanda
     */
    List<Product> getProductsByDemandRange(BigDecimal minDemand, BigDecimal maxDemand);
    
    /**
     * Busca produtos por range de qualidade
     */
    List<Product> getProductsByQualityRange(BigDecimal minQuality, BigDecimal maxQuality);

    /**
     * Conta o número de produtos ativos
     */
    long countActiveProducts();
    
    /**
     * Conta produtos por categoria
     */
    long countProductsByCategory(String category);
    
    /**
     * Conta produtos por reino
     */
    long countProductsByKingdom(Long kingdomId);
    
    /**
     * Conta produtos de alta demanda
     */
    long countHighDemandProducts();
    
    /**
     * Conta produtos de baixa demanda
     */
    long countLowDemandProducts();
    
    /**
     * Conta produtos de alta qualidade
     */
    long countHighQualityProducts();
    
    /**
     * Conta produtos de baixa qualidade
     */
    long countLowQualityProducts();
    
    /**
     * Conta produtos de reinos proprietários
     */
    long countProductsFromOwnerKingdoms();
    
    // ===== MÉTODOS DE VALIDAÇÃO =====
    
    /**
     * Verifica se um produto existe e está ativo
     */
    boolean existsAndActive(Long id);
    
    /**
     * Verifica se um produto com o nome existe
     */
    boolean existsByName(String name);
    
    /**
     * Verifica se um produto com o nome existe no reino
     */
    boolean existsByNameAndKingdom(String name, Long kingdomId);
    
    /**
     * Verifica se um produto é de um reino proprietário
     */
    boolean isFromOwnerKingdom(Long id);
    
    /**
     * Verifica se um produto tem alta demanda
     */
    boolean isHighDemand(Long id);
    
    /**
     * Verifica se um produto tem alta qualidade
     */
    boolean isHighQuality(Long id);
} 