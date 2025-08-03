package com.exchange.repository;

import com.exchange.domain.entity.Currency;
import com.exchange.domain.entity.CurrencyId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, CurrencyId> {
    
    /**
     * Busca moeda ativa por prefix
     */
    Optional<Currency> findByPrefixAndIsActiveTrue(String prefix);
    
    /**
     * Verificar se existe moeda ativa com o prefix
     */
    boolean existsByPrefixAndIsActiveTrue(String prefix);
    
    /**
     * Lista todas as moedas ativas
     */
    List<Currency> findByIsActiveTrue();
    
    /**
     * Lista moedas ativas com paginação
     */
    Page<Currency> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Lista moedas com paginação e filtro por status
     */
    Page<Currency> findByIsActive(Boolean isActive, Pageable pageable);
    
    /**
     * Busca moeda por prefix (ativa ou inativa)
     */
    List<Currency> findByPrefix(String prefix);
    
    /**
     * Verificar se existe moeda com o prefix (ativa ou inativa)
     */
    boolean existsByPrefix(String prefix);
    
    /**
     * Buscar por chave composta
     */
    Optional<Currency> findByPrefixAndIsActive(String prefix, Boolean isActive);
    
    /**
     * Conta moedas ativas
     */
    long countByIsActiveTrue();
    
    /**
     * Conta moedas inativas
     */
    long countByIsActiveFalse();
} 