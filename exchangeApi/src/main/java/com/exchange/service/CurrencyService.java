package com.exchange.service;

import com.exchange.domain.dto.CurrencyInfo;
import com.exchange.domain.dto.PageRequest;
import com.exchange.domain.dto.PageResponse;
import com.exchange.domain.entity.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CurrencyService {
    
    /**
     * Lista moedas com paginação
     */
    PageResponse<CurrencyInfo> getCurrencies(Boolean activeOnly, PageRequest pageRequest);
    
    /**
     * Busca moeda ativa por prefix
     */
    CurrencyInfo getCurrencyByPrefix(String prefix);
    
    /**
     * Cria nova moeda
     */
    CurrencyInfo createCurrency(CurrencyInfo currencyInfo);
    
    /**
     * Atualiza moeda existente
     */
    CurrencyInfo updateCurrency(String prefix, CurrencyInfo currencyInfo);
    
    /**
     * Desativa moeda
     */
    void deactivateCurrency(String prefix);
    
    /**
     * Busca entidade Currency ativa por prefix
     */
    Currency getActiveCurrencyByPrefix(String prefix);
    
    /**
     * Lista moedas ativas com paginação
     */
    Page<Currency> getActiveCurrencies(Pageable pageable);
    
    /**
     * Verifica se existe moeda ativa com o prefix
     */
    boolean existsActiveCurrency(String prefix);
    
    /**
     * Busca moedas por prefix (ativa ou inativa)
     */
    List<Currency> getCurrenciesByPrefix(String prefix);
    
    /**
     * Busca moeda por prefix e status
     */
    Optional<Currency> getCurrencyByPrefixAndActive(String prefix, Boolean isActive);
} 