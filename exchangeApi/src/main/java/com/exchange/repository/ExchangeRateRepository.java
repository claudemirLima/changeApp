package com.exchange.repository;

import com.exchange.domain.entity.ExchangeRate;
import com.exchange.domain.entity.ExchangeRateId;
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
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, ExchangeRateId> {
    
    
    
    /**
     * Buscar taxa ativa por prefix e data
     */
    @Query(value = "SELECT * FROM exchange_rates WHERE from_currency_prefix = :fromPrefix " +
                   "AND to_currency_prefix = :toPrefix AND created_at = :createdAt " +
                   "AND is_active = true", nativeQuery = true)
    Optional<ExchangeRate> findActiveByPrefixesAndDate(
        @Param("fromPrefix") String fromPrefix,
        @Param("toPrefix") String toPrefix,
        @Param("createdAt") LocalDate createdAt
    );
    
    /**
     * Buscar taxa ativa mais recente
     */
    @Query(value = "SELECT * FROM exchange_rates WHERE from_currency_prefix = :fromPrefix " +
                   "AND to_currency_prefix = :toPrefix AND is_active = true " +
                   "ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
    Optional<ExchangeRate> findLatestActiveByPrefixes(
        @Param("fromPrefix") String fromPrefix,
        @Param("toPrefix") String toPrefix
    );
    
    /**
     * Listar taxas ativas com paginação
     */
    @Query(value = "SELECT * FROM exchange_rates WHERE is_active = true " +
                   "ORDER BY created_at DESC", nativeQuery = true)
    Page<ExchangeRate> findActiveWithPagination(Pageable pageable);
    
    /**
     * Buscar taxas por período
     */
    @Query(value = "SELECT * FROM exchange_rates WHERE from_currency_prefix = :fromPrefix " +
                   "AND to_currency_prefix = :toPrefix AND is_active = true " +
                   "AND created_at BETWEEN :startDate AND :endDate " +
                   "ORDER BY created_at DESC", nativeQuery = true)
    List<ExchangeRate> findActiveByPrefixesAndPeriod(
        @Param("fromPrefix") String fromPrefix,
        @Param("toPrefix") String toPrefix,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    /**
     * Verificar se existe taxa ativa
     */
    @Query(value = "SELECT COUNT(*) > 0 FROM exchange_rates WHERE from_currency_prefix = :fromPrefix " +
                   "AND to_currency_prefix = :toPrefix AND created_at = :createdAt " +
                   "AND is_active = true", nativeQuery = true)
    boolean existsActiveByPrefixesAndDate(
        @Param("fromPrefix") String fromPrefix,
        @Param("toPrefix") String toPrefix,
        @Param("createdAt") LocalDate createdAt
    );
    
    /**
     * Contar taxas ativas
     */
    @Query(value = "SELECT COUNT(*) FROM exchange_rates WHERE is_active = true", nativeQuery = true)
    long countActive();
    
    /**
     * Contar taxas inativas
     */
    @Query(value = "SELECT COUNT(*) FROM exchange_rates WHERE is_active = false", nativeQuery = true)
    long countInactive();
    
    
    
    /**
     * Busca taxas com filtros avançados
     */
    @Query(value = """
        SELECT * FROM exchange_rates 
        WHERE from_currency_prefix = :fromPrefix
        AND to_currency_prefix = :toPrefix
        AND is_active = :activeOnly
        ORDER BY created_at DESC
        """, nativeQuery = true)
    Page<ExchangeRate> findExchangeRatesWithFilters(
        @Param("fromPrefix") String fromPrefix,
        @Param("toPrefix") String toPrefix,
        @Param("activeOnly") Boolean activeOnly,
        Pageable pageable
    );
    
    /**
     * Busca histórico de taxas para um par de moedas
     */
    @Query(value = """
        SELECT * FROM exchange_rates 
        WHERE from_currency_prefix = :fromPrefix 
        AND to_currency_prefix = :toPrefix 
        AND is_active = true
        AND created_at >= :startDate
        AND created_at <= :endDate
        ORDER BY created_at DESC
        """, nativeQuery = true)
    List<ExchangeRate> findRateHistory(
            @Param("fromPrefix") String fromPrefix,
            @Param("toPrefix") String toPrefix,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    
    /**
     * Busca taxa média em um período
     */
    @Query(value = """
        SELECT AVG(rate) FROM exchange_rates 
        WHERE from_currency_prefix = :fromPrefix 
        AND to_currency_prefix = :toPrefix 
        AND is_active = true
        AND created_at >= :startDate
        AND created_at <= :endDate
        """, nativeQuery = true)
    Double findAverageRate(
        @Param("fromPrefix") String fromPrefix,
        @Param("toPrefix") String toPrefix,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    /**
     * Busca taxa mínima em um período
     */
    @Query(value = """
        SELECT MIN(rate) FROM exchange_rates 
        WHERE from_currency_prefix = :fromPrefix 
        AND to_currency_prefix = :toPrefix 
        AND is_active = true
        AND created_at >= :startDate
        AND created_at <= :endDate
        """, nativeQuery = true)
    Double findMinRate(
        @Param("fromPrefix") String fromPrefix,
        @Param("toPrefix") String toPrefix,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    /**
     * Busca taxa máxima em um período
     */
    @Query(value = """
        SELECT MAX(rate) FROM exchange_rates 
        WHERE from_currency_prefix = :fromPrefix 
        AND to_currency_prefix = :toPrefix 
        AND is_active = true
        AND created_at >= :startDate
        AND created_at <= :endDate
        """, nativeQuery = true)
    Double findMaxRate(
        @Param("fromPrefix") String fromPrefix,
        @Param("toPrefix") String toPrefix,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
} 