package com.exchange.service;

import com.exchange.domain.entity.ExchangeRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateService {
    
    // ===== MÉTODOS BÁSICOS =====
    
    /**
     * Busca a taxa de câmbio ativa para o par de moedas
     */
    ExchangeRate getActiveRate(String fromCurrencyPrefix, String toCurrencyPrefix);
    
    /**
     * Busca a taxa de câmbio ativa para o par de moedas (retorna Optional)
     */
    Optional<ExchangeRate> findActiveRate(String fromCurrencyPrefix, String toCurrencyPrefix);
    
    /**
     * Busca a taxa de câmbio ativa mais recente
     */
    ExchangeRate getLatestActiveRate(String fromCurrencyPrefix, String toCurrencyPrefix);
    
    /**
     * Cria uma nova taxa de câmbio
     */
    ExchangeRate saveRate(String fromCurrencyPrefix, String toCurrencyPrefix, BigDecimal rate);
    
    /**
     * Atualiza uma taxa de câmbio existente
     */
    ExchangeRate updateRate(String fromCurrencyPrefix, String toCurrencyPrefix, 
                           BigDecimal rate, Boolean isActive);
    
    /**
     * Desativa uma taxa de câmbio
     */
    void deactivateRate(String fromCurrencyPrefix, String toCurrencyPrefix);
    
    /**
     * Lista taxas ativas com paginação
     */
    Page<ExchangeRate> getActiveRates(Pageable pageable);
    
    // ===== MÉTODOS CUSTOMIZADOS =====
    
    /**
     * Busca taxas com filtros avançados
     */
    Page<ExchangeRate> getExchangeRatesWithFilters(String fromPrefix, String toPrefix, 
                                                  Boolean activeOnly, Pageable pageable);
} 