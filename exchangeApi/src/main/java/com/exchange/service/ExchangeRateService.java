package com.exchange.service;

import com.exchange.domain.entity.ExchangeRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExchangeRateService {
    
    // ===== MÉTODOS BÁSICOS =====
    
    /**
     * Busca a taxa de câmbio ativa para o par de moedas e data especificados
     */
    ExchangeRate getActiveRate(String fromCurrencyPrefix, String toCurrencyPrefix, LocalDate effectiveDate);
    
    /**
     * Busca a taxa de câmbio ativa mais recente
     */
    ExchangeRate getLatestActiveRate(String fromCurrencyPrefix, String toCurrencyPrefix);
    
    /**
     * Cria uma nova taxa de câmbio
     */
    ExchangeRate saveRate(String fromCurrencyPrefix, String toCurrencyPrefix, BigDecimal rate, LocalDate effectiveDate);
    
    /**
     * Desativa uma taxa de câmbio
     */
    void deactivateRate(String fromCurrencyPrefix, String toCurrencyPrefix, LocalDate effectiveDate);
    
    /**
     * Lista taxas ativas com paginação
     */
    Page<ExchangeRate> getActiveRates(Pageable pageable);
    
    /**
     * Busca taxas por período
     */
    List<ExchangeRate> getRatesByPeriod(String fromCurrencyPrefix, String toCurrencyPrefix, 
                                      LocalDate startDate, LocalDate endDate);
    
    // ===== MÉTODOS CUSTOMIZADOS =====
    
    /**
     * Busca taxas com filtros avançados
     */
    Page<ExchangeRate> getExchangeRatesWithFilters(String fromPrefix, String toPrefix, 
                                                  LocalDate startDate, LocalDate endDate, 
                                                  Boolean activeOnly, Pageable pageable);
    
    /**
     * Busca histórico de taxas para um par de moedas
     */
    List<ExchangeRate> getRateHistory(String fromPrefix, String toPrefix, 
                                    LocalDate startDate, LocalDate endDate);
    
    /**
     * Busca taxa média em um período
     */
    Double getAverageRate(String fromPrefix, String toPrefix, 
                         LocalDate startDate, LocalDate endDate);
    
    /**
     * Busca taxa mínima em um período
     */
    Double getMinRate(String fromPrefix, String toPrefix, 
                     LocalDate startDate, LocalDate endDate);
    
    /**
     * Busca taxa máxima em um período
     */
    Double getMaxRate(String fromPrefix, String toPrefix, 
                     LocalDate startDate, LocalDate endDate);
    
    /**
     * Busca taxas que sofreram variação significativa
     */
    List<ExchangeRate> getRatesWithSignificantVariation(String fromPrefix, String toPrefix, 
                                                       Double variationThreshold,
                                                       LocalDate startDate, LocalDate endDate);
} 