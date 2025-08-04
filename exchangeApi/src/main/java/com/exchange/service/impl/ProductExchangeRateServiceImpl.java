package com.exchange.service.impl;

import com.exchange.domain.entity.ProductExchangeRate;
import com.exchange.domain.exception.ProductExchangeRateAlreadyExistsException;
import com.exchange.domain.exception.ProductExchangeRateNotFoundException;
import com.exchange.domain.exception.ProductExchangeRateOperationException;
import com.exchange.repository.ProductExchangeRateRepository;
import com.exchange.service.CurrencyService;
import com.exchange.service.ProductExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductExchangeRateServiceImpl implements ProductExchangeRateService {
    
    @Autowired
    private ProductExchangeRateRepository productExchangeRateRepository;
    
    @Autowired
    private CurrencyService currencyService;
    
    @Override
    @Transactional(readOnly = true)
    public ProductExchangeRate getActiveProductRate(Long productId, String fromCurrencyPrefix, String toCurrencyPrefix) {
        // Validar se as moedas existem
        currencyService.getActiveCurrencyByPrefix(fromCurrencyPrefix);
        currencyService.getActiveCurrencyByPrefix(toCurrencyPrefix);
        
        return productExchangeRateRepository.findLatestActiveByProductAndPrefixes(productId, fromCurrencyPrefix, toCurrencyPrefix)
            .orElseThrow(() -> new ProductExchangeRateNotFoundException(
                "Taxa de câmbio para produto não encontrada: Produto " + productId + 
                ", " + fromCurrencyPrefix + " → " + toCurrencyPrefix
            ));
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProductExchangeRate getLatestActiveProductRate(Long productId, String fromCurrencyPrefix, String toCurrencyPrefix) {
        return productExchangeRateRepository.findLatestActiveByProductAndPrefixes(productId, fromCurrencyPrefix, toCurrencyPrefix)
            .orElseThrow(() -> new ProductExchangeRateNotFoundException(
                "Taxa de câmbio para produto não encontrada: Produto " + productId + 
                ", " + fromCurrencyPrefix + " → " + toCurrencyPrefix
            ));
    }
    
    @Override
    @Transactional
    public ProductExchangeRate saveProductRate(Long productId, String fromCurrencyPrefix, String toCurrencyPrefix, 
                                             BigDecimal baseRate, BigDecimal productMultiplier) {
        // Validar se as moedas existem
        currencyService.getActiveCurrencyByPrefix(fromCurrencyPrefix);
        currencyService.getActiveCurrencyByPrefix(toCurrencyPrefix);
        
        // Validar parâmetros
        validateProductRate(baseRate, productMultiplier);
        
        // Verificar se já existe taxa ativa para este produto
        if (productExchangeRateRepository.findLatestActiveByProductAndPrefixes(productId, fromCurrencyPrefix, toCurrencyPrefix).isPresent()) {
            throw new ProductExchangeRateAlreadyExistsException(
                "Já existe uma taxa ativa para o produto " + productId + 
                " (" + fromCurrencyPrefix + " → " + toCurrencyPrefix + ")"
            );
        }
        
        ProductExchangeRate productExchangeRate = new ProductExchangeRate();
        productExchangeRate.setProductId(productId);
        productExchangeRate.setFromCurrencyPrefix(fromCurrencyPrefix);
        productExchangeRate.setToCurrencyPrefix(toCurrencyPrefix);
        productExchangeRate.setBaseRate(baseRate);
        productExchangeRate.setProductMultiplier(productMultiplier);
        productExchangeRate.setIsActive(true);
        productExchangeRate.setDeactivatedAt(null);
        
        return productExchangeRateRepository.save(productExchangeRate);
    }
    
    @Override
    @Transactional
    public void deactivateProductRate(Long productId, String fromCurrencyPrefix, String toCurrencyPrefix) {
        ProductExchangeRate activeRate = getActiveProductRate(productId, fromCurrencyPrefix, toCurrencyPrefix);
        
        // Criar nova entrada inativa
        ProductExchangeRate inactiveRate = new ProductExchangeRate();
        inactiveRate.setProductId(activeRate.getProductId());
        inactiveRate.setFromCurrencyPrefix(activeRate.getFromCurrencyPrefix());
        inactiveRate.setToCurrencyPrefix(activeRate.getToCurrencyPrefix());
        inactiveRate.setBaseRate(activeRate.getBaseRate());
        inactiveRate.setProductMultiplier(activeRate.getProductMultiplier());
        inactiveRate.setIsActive(false);
        inactiveRate.setDeactivatedAt(LocalDateTime.now());
        
        try {
            // Salvar versão inativa
            productExchangeRateRepository.save(inactiveRate);
            
            // Desativar versão ativa
            activeRate.setIsActive(false);
            activeRate.setDeactivatedAt(LocalDateTime.now());
            productExchangeRateRepository.save(activeRate);
            
        } catch (DataIntegrityViolationException e) {
            throw new ProductExchangeRateOperationException("Erro ao desativar taxa de câmbio para produto: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductExchangeRate> getActiveRatesByProduct(Long productId) {
        return productExchangeRateRepository.findActiveByProductId(productId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductExchangeRate> getRatesByProduct(Long productId, Pageable pageable) {
        return productExchangeRateRepository.findByProductIdWithPagination(productId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductExchangeRate> getActiveRates(Pageable pageable) {
        return productExchangeRateRepository.findActiveWithPagination(pageable);
    }
    
    // ===== MÉTODOS PRIVADOS =====
    
    private void validateProductRate(BigDecimal baseRate, BigDecimal productMultiplier) {
        if (baseRate == null || baseRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Taxa base deve ser maior que zero");
        }
        
        if (productMultiplier == null || productMultiplier.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Multiplicador do produto deve ser maior que zero");
        }
    }
} 