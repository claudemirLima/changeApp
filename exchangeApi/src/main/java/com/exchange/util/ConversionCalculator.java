package com.exchange.util;

import com.exchange.domain.dto.ProductInfo;
import com.exchange.domain.dto.KingdomInfo;
import com.exchange.domain.entity.ExchangeRate;
import com.exchange.domain.entity.ProductExchangeRate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ConversionCalculator {
    
    /**
     * Calcula conversão de moeda considerando dados do reino
     */
    public BigDecimal calculateCurrencyConversion(
            BigDecimal originalQuantity,
            ExchangeRate exchangeRate,
            KingdomInfo kingdomInfo) {
        
        // Calcular multiplicadores baseados no reino
        BigDecimal kingdomQualityMultiplier = kingdomInfo.getQualityRate();
        
        // Aplicar bônus se o reino for owner
        BigDecimal ownerBonus = kingdomInfo.getIsOwner() ? new BigDecimal("1.1") : BigDecimal.ONE;
        
        // Calcular conversão com multiplicadores do reino
        return originalQuantity
                .multiply(exchangeRate.getRate())
                .multiply(kingdomQualityMultiplier)
                .multiply(ownerBonus)
                .setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Calcula conversão de produto considerando dados do produto e reino
     */
    public BigDecimal calculateProductConversion(
            BigDecimal originalQuantity,
            ExchangeRate baseRate,
            ProductExchangeRate productRate,
            ProductInfo productInfo,
            KingdomInfo kingdomInfo) {
        
        // Definir taxa e multiplicador base
        BigDecimal finalRate = baseRate.getRate();
        BigDecimal productMultiplier = BigDecimal.ONE;
        
        if (productRate != null) {
            finalRate = productRate.getBaseRate();
            productMultiplier = productRate.getProductMultiplier();
        }
        
        // Calcular multiplicadores baseados no produto e reino
        BigDecimal demandMultiplier = productInfo.getDemandQuantifier();
        BigDecimal qualityMultiplier = productInfo.getQualityQualifier();
        BigDecimal kingdomQualityMultiplier = kingdomInfo.getQualityRate();
        
        // Aplicar bônus se o reino for owner
        BigDecimal ownerBonus = kingdomInfo.getIsOwner() ? new BigDecimal("1.1") : BigDecimal.ONE;
        
        // Calcular conversão com todos os multiplicadores
        return originalQuantity
                .multiply(finalRate)
                .multiply(productMultiplier)
                .multiply(demandMultiplier)
                .multiply(qualityMultiplier)
                .multiply(kingdomQualityMultiplier)
                .multiply(ownerBonus)
                .setScale(2, RoundingMode.HALF_UP);
    }
} 