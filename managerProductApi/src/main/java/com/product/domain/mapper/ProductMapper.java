package com.product.domain.mapper;

import com.product.domain.dto.ProductRequest;
import com.product.domain.dto.ProductResponse;
import com.product.domain.entity.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper manual para conversões entre Product e seus DTOs
 */
public class ProductMapper {
    
    /**
     * Converte Product para ProductResponse
     */
    public static ProductResponse productToProductResponse(Product product) {
        if (product == null) {
            return null;
        }
        
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setCategory(product.getCategory());
        response.setBaseValue(product.getBaseValue());
        response.setDemandQuantifier(product.getDemandQuantifier());
        response.setQualityQualifier(product.getQualityQualifier());
        response.setFinalValue(product.calculateFinalValue());
        response.setTotalMultiplier(product.calculateTotalMultiplier());
        response.setKingdom(KingdomMapper.kingdomToKingdomResponse(product.getKingdom()));
        response.setIsActive(product.getIsActive());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        
        return response;
    }
    
    /**
     * Converte lista de Product para lista de ProductResponse
     */
    public static List<ProductResponse> productListToProductResponseList(List<Product> products) {
        if (products == null) {
            return null;
        }
        
        return products.stream()
            .map(ProductMapper::productToProductResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Converte ProductRequest para Product (criação)
     */
    public static Product productRequestToProduct(ProductRequest request) {
        if (request == null) {
            return null;
        }
        
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setBaseValue(request.getBaseValue());
        product.setDemandQuantifier(request.getDemandQuantifier());
        product.setQualityQualifier(request.getQualityQualifier());
        // kingdom será definido no service
        product.setIsActive(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(null);
        product.setDeactivatedAt(null);
        
        return product;
    }
    
    /**
     * Atualiza Product com dados do ProductRequest
     */
    public static void updateProductFromRequest(ProductRequest request, Product product) {
        if (request == null || product == null) {
            return;
        }
        
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setBaseValue(request.getBaseValue());
        product.setDemandQuantifier(request.getDemandQuantifier());
        product.setQualityQualifier(request.getQualityQualifier());
        // kingdom será definido no service
        product.setUpdatedAt(LocalDateTime.now());
    }
    
    /**
     * Cria um novo Product a partir de um existente (para cópia)
     */
    public static Product copyProduct(Product product) {
        if (product == null) {
            return null;
        }
        
        Product copy = new Product();
        copy.setName(product.getName());
        copy.setDescription(product.getDescription());
        copy.setCategory(product.getCategory());
        copy.setBaseValue(product.getBaseValue());
        copy.setDemandQuantifier(product.getDemandQuantifier());
        copy.setQualityQualifier(product.getQualityQualifier());
        copy.setKingdom(product.getKingdom());
        copy.setIsActive(product.getIsActive());
        copy.setCreatedAt(LocalDateTime.now());
        copy.setUpdatedAt(null);
        copy.setDeactivatedAt(null);
        
        return copy;
    }
    
    /**
     * Converte Product para ProductResponse com validações
     */
    public static ProductResponse productToProductResponseWithValidation(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product não pode ser nulo");
        }
        
        return productToProductResponse(product);
    }
    
    /**
     * Converte ProductRequest para Product com validações
     */
    public static Product productRequestToProductWithValidation(ProductRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("ProductRequest não pode ser nulo");
        }
        
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }
        
        if (request.getCategory() == null || request.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }
        
        if (request.getBaseValue() == null) {
            throw new IllegalArgumentException("Valor base é obrigatório");
        }
        
        if (request.getDemandQuantifier() == null) {
            throw new IllegalArgumentException("Quantificador de demanda é obrigatório");
        }
        
        if (request.getQualityQualifier() == null) {
            throw new IllegalArgumentException("Qualificador de qualidade é obrigatório");
        }
        
        if (request.getKingdomId() == null) {
            throw new IllegalArgumentException("ID do reino é obrigatório");
        }
        
        return productRequestToProduct(request);
    }
} 