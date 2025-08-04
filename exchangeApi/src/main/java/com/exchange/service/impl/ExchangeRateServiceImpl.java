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
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;
    
    @Autowired
    private CurrencyService currencyService;
    
    // ===== MÉTODOS BÁSICOS =====
    
    @Override
    @Transactional(readOnly = true)
    public ExchangeRate getActiveRate(String fromCurrencyPrefix, String toCurrencyPrefix) {
        // Validar se as moedas existem
        currencyService.getActiveCurrencyByPrefix(fromCurrencyPrefix);
        currencyService.getActiveCurrencyByPrefix(toCurrencyPrefix);
        
        return exchangeRateRepository.findLatestActiveByPrefixes(fromCurrencyPrefix, toCurrencyPrefix)
            .orElseThrow(() -> new ExchangeRateNotFoundException(
                "Taxa de câmbio não encontrada para " + fromCurrencyPrefix + " → " + toCurrencyPrefix
            ));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<ExchangeRate> findActiveRate(String fromCurrencyPrefix, String toCurrencyPrefix) {
        try {
            // Validar se as moedas existem
            currencyService.getActiveCurrencyByPrefix(fromCurrencyPrefix);
            currencyService.getActiveCurrencyByPrefix(toCurrencyPrefix);
            
            return exchangeRateRepository.findLatestActiveByPrefixes(fromCurrencyPrefix, toCurrencyPrefix);
        } catch (Exception e) {
            // Se as moedas não existem, retorna Optional vazio
            return Optional.empty();
        }
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
    public ExchangeRate saveRate(String fromCurrencyPrefix, String toCurrencyPrefix, BigDecimal rate) {
        // Validar se as moedas existem
        currencyService.getActiveCurrencyByPrefix(fromCurrencyPrefix);
        currencyService.getActiveCurrencyByPrefix(toCurrencyPrefix);
        
        // Validar taxa
        validateRate(rate);
        
        // Verificar se já existe taxa ativa
        if (exchangeRateRepository.findLatestActiveByPrefixes(fromCurrencyPrefix, toCurrencyPrefix).isPresent()) {
            throw new ExchangeRateAlreadyExistsException(
                "Já existe uma taxa ativa para " + fromCurrencyPrefix + " → " + toCurrencyPrefix
            );
        }
        
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setFromCurrencyPrefix(fromCurrencyPrefix);
        exchangeRate.setToCurrencyPrefix(toCurrencyPrefix);
        exchangeRate.setRate(rate);
        exchangeRate.setIsActive(true);
        exchangeRate.setDeactivatedAt(null);
        
        return exchangeRateRepository.save(exchangeRate);
    }
    
    @Override
    @Transactional
    public ExchangeRate updateRate(String fromCurrencyPrefix, String toCurrencyPrefix, 
                                 BigDecimal rate, Boolean isActive) {
        // Validar se as moedas existem
        currencyService.getActiveCurrencyByPrefix(fromCurrencyPrefix);
        currencyService.getActiveCurrencyByPrefix(toCurrencyPrefix);
        
        // Validar taxa
        validateRate(rate);
        
        // Buscar taxa existente
        ExchangeRate existingRate = getActiveRate(fromCurrencyPrefix, toCurrencyPrefix);

        try {
            // Desativar taxa anterior
            existingRate.setIsActive(isActive != null ? isActive : true);
            existingRate.setDeactivatedAt(isActive != null && !isActive ? LocalDateTime.now() : null);
            existingRate.setRate(rate);
            existingRate.setDeactivatedAt(LocalDateTime.now());
            exchangeRateRepository.save(existingRate);
            
            // Salvar nova taxa
            return exchangeRateRepository.save(existingRate);
            
        } catch (DataIntegrityViolationException e) {
            throw new ExchangeRateOperationException("Erro ao atualizar taxa de câmbio: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void deactivateRate(String fromCurrencyPrefix, String toCurrencyPrefix) {
        ExchangeRate activeRate = getActiveRate(fromCurrencyPrefix, toCurrencyPrefix);
        
        // Criar nova entrada inativa
        ExchangeRate inactiveRate = new ExchangeRate();
        inactiveRate.setFromCurrencyPrefix(activeRate.getFromCurrencyPrefix());
        inactiveRate.setToCurrencyPrefix(activeRate.getToCurrencyPrefix());
        inactiveRate.setRate(activeRate.getRate());
        inactiveRate.setIsActive(false);
        inactiveRate.setDeactivatedAt(LocalDateTime.now());
        
        try {
            // Salvar versão inativa
            exchangeRateRepository.save(inactiveRate);
            
            // Desativar versão ativa
            activeRate.setIsActive(false);
            activeRate.setDeactivatedAt(LocalDateTime.now());
            exchangeRateRepository.save(activeRate);
            
        } catch (DataIntegrityViolationException e) {
            throw new ExchangeRateOperationException("Erro ao desativar taxa de câmbio: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ExchangeRate> getActiveRates(Pageable pageable) {
        return exchangeRateRepository.findActiveWithPagination(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ExchangeRate> getExchangeRatesWithFilters(String fromPrefix, String toPrefix, 
                                                        Boolean activeOnly, Pageable pageable) {
        return exchangeRateRepository.findExchangeRatesWithFilters(fromPrefix, toPrefix, activeOnly, pageable);
    }
    
    private void validateRate(BigDecimal rate) {
        if (rate == null || rate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Taxa deve ser maior que zero");
        }
    }
} 