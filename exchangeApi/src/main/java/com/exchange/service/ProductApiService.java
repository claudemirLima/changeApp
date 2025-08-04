package com.exchange.service;

import com.exchange.domain.dto.ProductInfo;
import com.exchange.domain.dto.KingdomInfo;

public interface ProductApiService {
    
    /**
     * Busca informações do produto na API de produtos
     */
    ProductInfo getProductInfo(Long productId);
    
    /**
     * Busca informações do reino na API de produtos
     */
    KingdomInfo getKingdomInfo(Long kingdomId);
} 