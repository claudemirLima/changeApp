package com.exchange.service;

import com.exchange.domain.entity.ProductExchangeRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ProductExchangeRateService {
    
    /**
     * Busca a taxa de câmbio ativa para um produto específico
     */
    ProductExchangeRate getActiveProductRate(Long productId, String fromCurrencyPrefix, String toCurrencyPrefix, LocalDate effectiveDate);
    
    /**
     * Busca a taxa de câmbio ativa mais recente para um produto
     */
    ProductExchangeRate getLatestActiveProductRate(Long productId, String fromCurrencyPrefix, String toCurrencyPrefix);
    
    /**
     * Cria uma nova taxa de câmbio para produto
     */
    ProductExchangeRate saveProductRate(Long productId, String fromCurrencyPrefix, String toCurrencyPrefix, 
                                      BigDecimal baseRate, BigDecimal productMultiplier, LocalDate effectiveDate);
    
    /**
     * Desativa uma taxa de câmbio para produto
     */
    void deactivateProductRate(Long productId, String fromCurrencyPrefix, String toCurrencyPrefix, LocalDate effectiveDate);
    
    /**
     * Lista taxas ativas por produto
     */
    List<ProductExchangeRate> getActiveRatesByProduct(Long productId);
    
    /**
     * Lista taxas por produto com paginação
     */
    Page<ProductExchangeRate> getRatesByProduct(Long productId, Pageable pageable);
    
    /**
     * Lista todas as taxas ativas com paginação
     */
    Page<ProductExchangeRate> getActiveRates(Pageable pageable);
    
    /**
     * Busca taxas por produto e período
     */
    List<ProductExchangeRate> getRatesByProductAndPeriod(Long productId, String fromCurrencyPrefix, String toCurrencyPrefix, 
                                                       LocalDate startDate, LocalDate endDate);
} 