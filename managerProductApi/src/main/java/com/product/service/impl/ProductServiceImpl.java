package com.product.service.impl;

import com.product.domain.entity.Kingdom;
import com.product.domain.entity.Product;
import com.product.domain.exception.ProductAlreadyExistsException;
import com.product.domain.exception.ProductNotFoundException;
import com.product.domain.exception.ProductOperationException;
import com.product.repository.ProductRepository;
import com.product.service.KingdomService;
import com.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementação do serviço de gerenciamento de Produtos
 */
@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private KingdomService kingdomService;

    
    @Override
    @Transactional
    public Product createProduct(String name, String description, String category, 
                               BigDecimal baseValue, BigDecimal demandQuantifier, 
                               BigDecimal qualityQualifier, Long kingdomId) {
        // Verificar se o reino existe e está ativo
        Kingdom kingdom = kingdomService.getKingdomById(kingdomId);
        if (!kingdom.getIsActive()) {
            throw new ProductOperationException("criação", "Reino não está ativo");
        }
        
        // Verificar se já existe produto com o nome no reino
        if (productRepository.existsActiveByNameAndKingdom(name, kingdomId)) {
            throw new ProductAlreadyExistsException(name, kingdomId);
        }
        
        // Criar novo produto
        Product product = Product.builder()
                .name(name)
                .description(description)
                .category(category)
                .baseValue(baseValue)
                .demandQuantifier(demandQuantifier)
                .qualityQualifier(qualityQualifier)
                .kingdom(kingdom)
                .build();

        
        try {
            return productRepository.save(product);
        } catch (DataIntegrityViolationException e) {
            throw new ProductOperationException("criação", "Erro de integridade de dados");
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Product getProductByName(String name) {
        return productRepository.findActiveByName(name)
            .orElseThrow(() -> new ProductNotFoundException(name, true));
    }
    
    @Override
    @Transactional
    public Product updateProduct(Long id, String name, String description, String category,
                               BigDecimal baseValue, BigDecimal demandQuantifier,
                               BigDecimal qualityQualifier, Long kingdomId) {
        // Buscar produto existente
        Product product = getProductById(id);
        
        // Verificar se o reino existe e está ativo
        Kingdom kingdom = kingdomService.getKingdomById(kingdomId);
        if (!kingdom.getIsActive()) {
            throw new ProductOperationException("atualização", "Reino não está ativo");
        }
        
        // Verificar se o nome foi alterado e se já existe outro produto com o novo nome no reino
        if (!product.getName().equals(name) && productRepository.existsActiveByNameAndKingdom(name, kingdomId)) {
            throw new ProductAlreadyExistsException(name, kingdomId);
        }
        
        // Atualizar dados
        product.setName(name);
        product.setDescription(description);
        product.setCategory(category);
        product.setBaseValue(baseValue);
        product.setDemandQuantifier(demandQuantifier);
        product.setQualityQualifier(qualityQualifier);
        product.setKingdom(kingdom);
        
        try {
            return productRepository.save(product);
        } catch (DataIntegrityViolationException e) {
            throw new ProductOperationException("atualização", "Erro de integridade de dados");
        }
    }
    
    @Override
    @Transactional
    public void activateProduct(Long id) {
        Product product = getProductById(id);
        product.activate();
        productRepository.save(product);
    }
    
    @Override
    @Transactional
    public void deactivateProduct(Long id) {
        Product product = getProductById(id);
        product.deactivate();
        productRepository.save(product);
    }
    
    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.deactivate();
        productRepository.save(product);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Product> getAllActiveProducts(Pageable pageable) {
        return productRepository.findAllActiveWithPagination(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllActiveProducts() {
        return productRepository.findAllActive();
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }
        return productRepository.findByCategory(category.trim());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByKingdom(Long kingdomId) {
        if (kingdomId == null) {
            throw new IllegalArgumentException("ID do reino é obrigatório");
        }
        
        // Verificar se o reino existe
        kingdomService.getKingdomById(kingdomId);
        
        return productRepository.findByKingdomId(kingdomId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getHighDemandProducts() {
        return productRepository.findHighDemandProducts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getLowDemandProducts() {
        return productRepository.findLowDemandProducts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getHighQualityProducts() {
        return productRepository.findHighQualityProducts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getLowQualityProducts() {
        return productRepository.findLowQualityProducts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsFromOwnerKingdoms() {
        return productRepository.findProductsFromOwnerKingdoms();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByBaseValueRange(BigDecimal minValue, BigDecimal maxValue) {
        validateRange(minValue, maxValue, "valor base");
        return productRepository.findByBaseValueRange(minValue, maxValue);
    }

    @Override
    public List<Product> getProductsByFinalValueRange(BigDecimal minValue, BigDecimal maxValue) {
        return List.of();
    }


    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByDemandRange(BigDecimal minDemand, BigDecimal maxDemand) {
        validateRange(minDemand, maxDemand, "demanda");
        return productRepository.findByDemandRange(minDemand, maxDemand);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByQualityRange(BigDecimal minQuality, BigDecimal maxQuality) {
        validateRange(minQuality, maxQuality, "qualidade");
        return productRepository.findByQualityRange(minQuality, maxQuality);
    }
    
    // ===== MÉTODOS DE CONTAGEM =====
    
    @Override
    @Transactional(readOnly = true)
    public long countActiveProducts() {
        return productRepository.countActive();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countProductsByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }
        return productRepository.countByCategory(category.trim());
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countProductsByKingdom(Long kingdomId) {
        if (kingdomId == null) {
            throw new IllegalArgumentException("ID do reino é obrigatório");
        }
        
        // Verificar se o reino existe
        kingdomService.getKingdomById(kingdomId);
        
        return productRepository.countByKingdomId(kingdomId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countHighDemandProducts() {
        return productRepository.countHighDemandProducts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countLowDemandProducts() {
        return productRepository.countLowDemandProducts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countHighQualityProducts() {
        return productRepository.countHighQualityProducts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countLowQualityProducts() {
        return productRepository.countLowQualityProducts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countProductsFromOwnerKingdoms() {
        return productRepository.countProductsFromOwnerKingdoms();
    }
    
    // ===== MÉTODOS DE VALIDAÇÃO =====
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsAndActive(Long id) {
        return productRepository.existsActiveById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return productRepository.existsActiveByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameAndKingdom(String name, Long kingdomId) {
        return productRepository.existsActiveByNameAndKingdom(name, kingdomId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isFromOwnerKingdom(Long id) {
        return productRepository.isFromOwnerKingdom(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isHighDemand(Long id) {
        return productRepository.isHighDemand(id)
            .orElse(false);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isHighQuality(Long id) {
        return productRepository.isHighQuality(id)
            .orElse(false);
    }
    
    // ===== MÉTODOS PRIVADOS =====
    
    /**
     * Valida range de valores
     */
    private void validateRange(BigDecimal min, BigDecimal max, String fieldName) {
        if (min == null || max == null) {
            throw new IllegalArgumentException("Range de " + fieldName + " não pode ser nulo");
        }
        
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("Valor mínimo de " + fieldName + " não pode ser maior que o máximo");
        }
        
        if (min.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor mínimo de " + fieldName + " não pode ser negativo");
        }
    }
} 