package com.exchange.service.impl;

import com.exchange.domain.dto.CurrencyInfo;
import com.exchange.domain.dto.PageRequest;
import com.exchange.domain.dto.PageResponse;
import com.exchange.domain.entity.Currency;
import com.exchange.domain.exception.CurrencyAlreadyExistsException;
import com.exchange.domain.exception.CurrencyNotFoundException;
import com.exchange.domain.exception.CurrencyOperationException;
import com.exchange.repository.CurrencyRepository;
import com.exchange.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    
    @Autowired
    private CurrencyRepository currencyRepository;
    
    @Override
    @Transactional(readOnly = true)
    public PageResponse<CurrencyInfo> getCurrencies(Boolean activeOnly, PageRequest pageRequest) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(pageRequest.getPage(), pageRequest.getSize());
        
        Page<Currency> currencyPage;
        if (activeOnly != null && activeOnly) {
            currencyPage = currencyRepository.findByIsActiveTrue(pageable);
        } else {
            currencyPage = currencyRepository.findAll(pageable);
        }
        
        List<CurrencyInfo> currencyInfos = currencyPage.getContent().stream()
            .map(this::convertToCurrencyInfo)
            .collect(Collectors.toList());
        
        return new PageResponse<>(
            currencyInfos,
            currencyPage.getNumber(),
            currencyPage.getSize(),
            currencyPage.getTotalElements(),
            currencyPage.getTotalPages(),
            currencyPage.isFirst(),
            currencyPage.isLast()
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public CurrencyInfo getCurrencyByPrefix(String prefix) {
        Currency currency = getActiveCurrencyByPrefix(prefix);
        return convertToCurrencyInfo(currency);
    }
    
    @Override
    @Transactional
    public CurrencyInfo createCurrency(CurrencyInfo currencyInfo) {
        Currency currency = new Currency();
        currency.setPrefix(currencyInfo.getCode());
        currency.setName(currencyInfo.getName());
        currency.setDescription(currencyInfo.getDescription());
        
        Currency savedCurrency = createCurrencyEntity(currency);
        return convertToCurrencyInfo(savedCurrency);
    }
    
    @Override
    @Transactional
    public CurrencyInfo updateCurrency(String prefix, CurrencyInfo currencyInfo) {
        Currency updatedCurrency = updateCurrencyEntity(prefix, convertToCurrency(currencyInfo));
        return convertToCurrencyInfo(updatedCurrency);
    }
    
    @Override
    @Transactional
    public void deactivateCurrency(String prefix) {
        deactivateCurrencyEntity(prefix);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Currency getActiveCurrencyByPrefix(String prefix) {
        return currencyRepository.findByPrefixAndIsActiveTrue(prefix)
            .orElseThrow(() -> new CurrencyNotFoundException("Moeda ativa não encontrada: " + prefix));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Currency> getActiveCurrencies(Pageable pageable) {
        return currencyRepository.findByIsActiveTrue(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsActiveCurrency(String prefix) {
        return currencyRepository.existsByPrefixAndIsActiveTrue(prefix);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Currency> getCurrenciesByPrefix(String prefix) {
        return currencyRepository.findByPrefix(prefix);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Currency> getCurrencyByPrefixAndActive(String prefix, Boolean isActive) {
        return currencyRepository.findByPrefixAndIsActive(prefix, isActive);
    }
    
    // Métodos privados para operações com entidade Currency
    @Transactional
    public Currency createCurrencyEntity(Currency currency) {
        validateCurrency(currency);
        
        try {
            currency.setIsActive(true);
            currency.setDeactivatedAt(null);
            return currencyRepository.save(currency);
            
        } catch (DataIntegrityViolationException e) {
            throw new CurrencyAlreadyExistsException(
                "Já existe uma moeda ativa com o prefix: " + currency.getPrefix()
            );
        }
    }
    
    @Transactional
    public Currency updateCurrencyEntity(String prefix, Currency updatedCurrency) {
        Currency existingCurrency = getActiveCurrencyByPrefix(prefix);
        
        // Atualizar campos permitidos
        existingCurrency.setName(updatedCurrency.getName());
        existingCurrency.setDescription(updatedCurrency.getDescription());
        
        return currencyRepository.save(existingCurrency);
    }
    
    @Transactional
    public void deactivateCurrencyEntity(String prefix) {
        Currency activeCurrency = getActiveCurrencyByPrefix(prefix);
        
        // Criar nova entrada inativa
        Currency inactiveCurrency = new Currency();
        inactiveCurrency.setPrefix(activeCurrency.getPrefix());
        inactiveCurrency.setIsActive(false);
        inactiveCurrency.setName(activeCurrency.getName());
        inactiveCurrency.setDescription(activeCurrency.getDescription());
        inactiveCurrency.setDeactivatedAt(LocalDateTime.now());
        
        try {
            // Salvar versão inativa
            currencyRepository.save(inactiveCurrency);
            
            // Remover versão ativa
            currencyRepository.delete(activeCurrency);
            
        } catch (DataIntegrityViolationException e) {
            throw new CurrencyOperationException(
                "Erro ao desativar moeda: " + prefix
            );
        }
    }
    
    // Métodos de conversão
    private CurrencyInfo convertToCurrencyInfo(Currency currency) {
        CurrencyInfo currencyInfo = new CurrencyInfo();
        currencyInfo.setCode(currency.getPrefix());
        currencyInfo.setName(currency.getName());
        currencyInfo.setDescription(currency.getDescription());
        return currencyInfo;
    }
    
    private Currency convertToCurrency(CurrencyInfo currencyInfo) {
        Currency currency = new Currency();
        currency.setPrefix(currencyInfo.getCode());
        currency.setName(currencyInfo.getName());
        currency.setDescription(currencyInfo.getDescription());
        return currency;
    }
    
    // Validações básicas
    private void validateCurrency(Currency currency) {
        if (currency == null) {
            throw new IllegalArgumentException("Moeda não pode ser nula");
        }
        
        if (currency.getPrefix() == null || currency.getPrefix().trim().isEmpty()) {
            throw new IllegalArgumentException("Prefix da moeda é obrigatório");
        }
        
        if (currency.getName() == null || currency.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da moeda é obrigatório");
        }
        
        if (currency.getPrefix().length() > 10) {
            throw new IllegalArgumentException("Prefix da moeda deve ter no máximo 10 caracteres");
        }
        
        if (currency.getName().length() > 100) {
            throw new IllegalArgumentException("Nome da moeda deve ter no máximo 100 caracteres");
        }
    }
} 