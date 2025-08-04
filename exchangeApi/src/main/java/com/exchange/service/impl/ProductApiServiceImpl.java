package com.exchange.service.impl;

import com.exchange.domain.dto.ProductInfo;
import com.exchange.domain.dto.KingdomInfo;
import com.exchange.service.ProductApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ProductApiServiceImpl implements ProductApiService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${app.product-api.base-url}")
    private String productApiBaseUrl;
    
    @Override
    public ProductInfo getProductInfo(Long productId) {
        try {
            String url = productApiBaseUrl + "/api/v1/products/" + productId;
            log.info("Buscando informações do produto: {}", url);
            
            ProductInfo productInfo = restTemplate.getForObject(url, ProductInfo.class);
            
            if (productInfo != null) {
                log.info("Produto encontrado: {} - Demanda: {}, Qualidade: {}", 
                        productInfo.getName(), productInfo.getDemandQuantifier(), productInfo.getQualityQualifier());
            }
            
            return productInfo;
            
        } catch (Exception e) {
            log.error("Erro ao buscar informações do produto: {}", productId, e);
            return null;
        }
    }
    
    @Override
    public KingdomInfo getKingdomInfo(Long kingdomId) {
        try {
            String url = productApiBaseUrl + "/api/v1/kingdoms/" + kingdomId;
            log.info("Buscando informações do reino: {}", url);
            
            KingdomInfo kingdomInfo = restTemplate.getForObject(url, KingdomInfo.class);
            
            if (kingdomInfo != null) {
                log.info("Reino encontrado: {} - Taxa de qualidade: {}, É owner: {}", 
                        kingdomInfo.getName(), kingdomInfo.getQualityRate(), kingdomInfo.getIsOwner());
            }
            
            return kingdomInfo;
            
        } catch (Exception e) {
            log.error("Erro ao buscar informações do reino: {}", kingdomId, e);
            return null;
        }
    }
} 