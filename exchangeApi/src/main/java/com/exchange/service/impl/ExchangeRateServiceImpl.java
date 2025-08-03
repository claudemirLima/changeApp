package com.exchange.service.impl;

import com.exchange.domain.entity.ExchangeRate;
import com.exchange.domain.exception.ExchangeRateAlreadyExistsException;
import com.exchange.domain.exception.ExchangeRateNotFoundException;
import com.exchange.domain.exception.ExchangeRateOperationException;
import com.exchange.repository.ExchangeRateRepository;
import com.exchange.service.CurrencyService;
import com.exchange.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;
    
    @Autowired
    private CurrencyService currencyService;
    
    // ===== MÉTODOS BÁSICOS =====
    
    @Override
    @Transactional(readOnly = true)
    public ExchangeRate getActiveRate(String fromCurrencyPrefix, String toCurrencyPrefix, LocalDate effectiveDate) {
        // Validar se as moedas existem
        currencyService.getActiveCurrencyByPrefix(fromCurrencyPrefix);
        currencyService.getActiveCurrencyByPrefix(toCurrencyPrefix);
        
        return exchangeRateRepository.findActiveByPrefixesAndDate(fromCurrencyPrefix, toCurrencyPrefix, effectiveDate)
            .orElseGet(() -> getLatestActiveRate(fromCurrencyPrefix, toCurrencyPrefix));
    }
    
    @Override
    @Transactional(readOnly = true)
    public ExchangeRate getLatestActiveRate(String fromCurrencyPrefix, String toCurrencyPrefix) {
        return exchangeRateRepository.findLatestActiveByPrefixes(fromCurrencyPrefix, toCurrencyPrefix)
            .orElseThrow(() -> new ExchangeRateNotFoundException(
                "Taxa de câmbio não encontrada para " + fromCurrencyPrefix + " → " + toCurrencyPrefix
            ));
    }
    
    @Override
    @Transactional
    public ExchangeRate saveRate(String fromCurrencyPrefix, String toCurrencyPrefix, 
                               BigDecimal rate, LocalDate effectiveDate) {
        // Validar se as moedas existem
        currencyService.getActiveCurrencyByPrefix(fromCurrencyPrefix);
        currencyService.getActiveCurrencyByPrefix(toCurrencyPrefix);
        
        // Validar taxa
        validateRate(rate);
        
        // Verificar se já existe taxa ativa para esta data
        if (exchangeRateRepository.existsActiveByPrefixesAndDate(fromCurrencyPrefix, toCurrencyPrefix, effectiveDate)) {
            throw new ExchangeRateAlreadyExistsException(
                "Já existe uma taxa ativa para " + fromCurrencyPrefix + " → " + toCurrencyPrefix + 
                " na data " + effectiveDate
            );
        }
        
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setFromCurrencyPrefix(fromCurrencyPrefix);
        exchangeRate.setToCurrencyPrefix(toCurrencyPrefix);
        exchangeRate.setRate(rate);
        exchangeRate.setEffectiveDate(effectiveDate);
        exchangeRate.setIsActive(true);
        exchangeRate.setDeactivatedAt(null);
        
        return exchangeRateRepository.save(exchangeRate);
    }
    
    @Override
    @Transactional
    public void deactivateRate(String fromCurrencyPrefix, String toCurrencyPrefix, LocalDate effectiveDate) {
        ExchangeRate activeRate = getActiveRate(fromCurrencyPrefix, toCurrencyPrefix, effectiveDate);
        
        // Criar nova entrada inativa
        ExchangeRate inactiveRate = new ExchangeRate();
        inactiveRate.setFromCurrencyPrefix(activeRate.getFromCurrencyPrefix());
        inactiveRate.setToCurrencyPrefix(activeRate.getToCurrencyPrefix());
        inactiveRate.setRate(activeRate.getRate());
        inactiveRate.setEffectiveDate(activeRate.getEffectiveDate());
        inactiveRate.setIsActive(false);
        inactiveRate.setDeactivatedAt(LocalDateTime.now());
        
        try {
            // Salvar versão inativa
            exchangeRateRepository.save(inactiveRate);
            
            // Remover versão ativa
            exchangeRateRepository.delete(activeRate);
            
        } catch (DataIntegrityViolationException e) {
            throw new ExchangeRateOperationException(
                "Erro ao desativar taxa de câmbio"
            );
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ExchangeRate> getActiveRates(Pageable pageable) {
        return exchangeRateRepository.findActiveWithPagination(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ExchangeRate> getRatesByPeriod(String fromCurrencyPrefix, String toCurrencyPrefix, 
                                             LocalDate startDate, LocalDate endDate) {
        return exchangeRateRepository.findActiveByPrefixesAndPeriod(fromCurrencyPrefix, toCurrencyPrefix, startDate, endDate);
    }
    
    // ===== MÉTODOS CUSTOMIZADOS =====
    
    @Override
    @Transactional(readOnly = true)
    public Page<ExchangeRate> getExchangeRatesWithFilters(String fromPrefix, String toPrefix, 
                                                        LocalDate startDate, LocalDate endDate, 
                                                        Boolean activeOnly, Pageable pageable) {
        return exchangeRateRepository.findExchangeRatesWithFilters(fromPrefix, toPrefix, startDate, endDate, activeOnly, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ExchangeRate> getRateHistory(String fromPrefix, String toPrefix, 
                                           LocalDate startDate, LocalDate endDate) {
        return exchangeRateRepository.findRateHistory(fromPrefix, toPrefix, startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getAverageRate(String fromPrefix, String toPrefix, 
                               LocalDate startDate, LocalDate endDate) {
        return exchangeRateRepository.findAverageRate(fromPrefix, toPrefix, startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getMinRate(String fromPrefix, String toPrefix, 
                           LocalDate startDate, LocalDate endDate) {
        return exchangeRateRepository.findMinRate(fromPrefix, toPrefix, startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Double getMaxRate(String fromPrefix, String toPrefix, 
                           LocalDate startDate, LocalDate endDate) {
        return exchangeRateRepository.findMaxRate(fromPrefix, toPrefix, startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ExchangeRate> getRatesWithSignificantVariation(String fromPrefix, String toPrefix, 
                                                             Double variationThreshold,
                                                             LocalDate startDate, LocalDate endDate) {
        return exchangeRateRepository.findRatesWithSignificantVariation(fromPrefix, toPrefix, variationThreshold, startDate, endDate);
    }
    
    // ===== MÉTODOS PRIVADOS =====
    
    // Validações
    private void validateRate(BigDecimal rate) {
        if (rate == null) {
            throw new IllegalArgumentException("Taxa de câmbio não pode ser nula");
        }
        
        if (rate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Taxa de câmbio deve ser maior que zero");
        }
        
        if (rate.compareTo(new BigDecimal("1000000")) > 0) {
            throw new IllegalArgumentException("Taxa de câmbio muito alta");
        }
    }
} 