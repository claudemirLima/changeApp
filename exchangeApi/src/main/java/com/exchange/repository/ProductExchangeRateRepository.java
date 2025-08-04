package com.exchange.repository;

import com.exchange.domain.entity.ProductExchangeRate;
import com.exchange.domain.entity.ProductExchangeRateId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductExchangeRateRepository extends JpaRepository<ProductExchangeRate, ProductExchangeRateId> {
    
    /**
     * Buscar taxa ativa por produto, prefix e data
     */
    @Query(value = "SELECT * FROM product_exchange_rates WHERE product_id = :productId " +
                   "AND from_currency_prefix = :fromPrefix AND to_currency_prefix = :toPrefix " +
                   "AND created_at = :createdAt AND is_active = true", nativeQuery = true)
    Optional<ProductExchangeRate> findActiveByProductAndPrefixesAndDate(
        @Param("productId") Long productId,
        @Param("fromPrefix") String fromPrefix,
        @Param("toPrefix") String toPrefix,
        @Param("createdAt") LocalDate createdAt
    );
    
    /**
     * Buscar taxa ativa mais recente por produto
     */
    @Query(value = "SELECT * FROM product_exchange_rates WHERE product_id = :productId " +
                   "AND from_currency_prefix = :fromPrefix AND to_currency_prefix = :toPrefix " +
                   "AND is_active = true ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
    Optional<ProductExchangeRate> findLatestActiveByProductAndPrefixes(
        @Param("productId") Long productId,
        @Param("fromPrefix") String fromPrefix,
        @Param("toPrefix") String toPrefix
    );
    
    /**
     * Listar taxas ativas por produto
     */
    @Query(value = "SELECT * FROM product_exchange_rates WHERE product_id = :productId " +
                   "AND is_active = true ORDER BY created_at DESC", nativeQuery = true)
    List<ProductExchangeRate> findActiveByProductId(@Param("productId") Long productId);
    
    /**
     * Listar taxas por produto com paginação
     */
    @Query(value = "SELECT * FROM product_exchange_rates WHERE product_id = :productId " +
                   "ORDER BY created_at DESC", nativeQuery = true)
    Page<ProductExchangeRate> findByProductIdWithPagination(
        @Param("productId") Long productId, Pageable pageable);
    
    /**
     * Listar taxas ativas com paginação
     */
    @Query(value = "SELECT * FROM product_exchange_rates WHERE is_active = true " +
                   "ORDER BY created_at DESC", nativeQuery = true)
    Page<ProductExchangeRate> findActiveWithPagination(Pageable pageable);
    
    /**
     * Buscar taxas por produto e período
     */
    @Query(value = "SELECT * FROM product_exchange_rates WHERE product_id = :productId " +
                   "AND from_currency_prefix = :fromPrefix AND to_currency_prefix = :toPrefix " +
                   "AND is_active = true AND created_at BETWEEN :startDate AND :endDate " +
                   "ORDER BY created_at DESC", nativeQuery = true)
    List<ProductExchangeRate> findActiveByProductAndPrefixesAndPeriod(
        @Param("productId") Long productId,
        @Param("fromPrefix") String fromPrefix,
        @Param("toPrefix") String toPrefix,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    /**
     * Verificar se existe taxa ativa
     */
    @Query(value = "SELECT COUNT(*) > 0 FROM product_exchange_rates WHERE product_id = :productId " +
                   "AND from_currency_prefix = :fromPrefix AND to_currency_prefix = :toPrefix " +
                   "AND created_at = :createdAt AND is_active = true", nativeQuery = true)
    boolean existsActiveByProductAndPrefixesAndDate(
        @Param("productId") Long productId,
        @Param("fromPrefix") String fromPrefix,
        @Param("toPrefix") String toPrefix,
        @Param("createdAt") LocalDate createdAt
    );
    
    /**
     * Contar taxas ativas por produto
     */
    @Query(value = "SELECT COUNT(*) FROM product_exchange_rates WHERE product_id = :productId " +
                   "AND is_active = true", nativeQuery = true)
    long countActiveByProductId(@Param("productId") Long productId);
    
    /**
     * Contar taxas inativas por produto
     */
    @Query(value = "SELECT COUNT(*) FROM product_exchange_rates WHERE product_id = :productId " +
                   "AND is_active = false", nativeQuery = true)
    long countInactiveByProductId(@Param("productId") Long productId);
    
    /**
     * Contar taxas ativas
     */
    @Query(value = "SELECT COUNT(*) FROM product_exchange_rates WHERE is_active = true", nativeQuery = true)
    long countActive();
    
    /**
     * Contar taxas inativas
     */
    @Query(value = "SELECT COUNT(*) FROM product_exchange_rates WHERE is_active = false", nativeQuery = true)
    long countInactive();
} 