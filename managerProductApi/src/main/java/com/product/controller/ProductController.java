package com.product.controller;

import com.product.domain.dto.ProductRequest;
import com.product.domain.dto.ProductResponse;
import com.product.domain.entity.Product;
import com.product.domain.mapper.ProductMapper;
import com.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller para gerenciamento de Produtos
 */
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product", description = "APIs para gerenciamento de Produtos")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    /**
     * Criar um novo produto
     */
    @PostMapping
    @Operation(summary = "Criar novo produto", description = "Cria um novo produto com os dados fornecidos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Reino não encontrado"),
        @ApiResponse(responseCode = "409", description = "Produto já existe")
    })
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {
        
        Product product = productService.createProduct(
            request.getName(),
            request.getDescription(),
            request.getCategory(),
            request.getBaseValue(),
            request.getDemandQuantifier(),
            request.getQualityQualifier(),
            request.getKingdomId()
        );
        
        ProductResponse response = ProductMapper.productToProductResponse(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Buscar produto por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna um produto específico pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto encontrado",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProductResponse> getProductById(
            @Parameter(description = "ID do produto") @PathVariable Long id) {
        
        Product product = productService.getProductById(id);
        ProductResponse response = ProductMapper.productToProductResponse(product);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Buscar produto por nome
     */
    @GetMapping("/name/{name}")
    @Operation(summary = "Buscar produto por nome", description = "Retorna um produto específico pelo nome")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto encontrado",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProductResponse> getProductByName(
            @Parameter(description = "Nome do produto") @PathVariable String name) {
        
        Product product = productService.getProductByName(name);
        ProductResponse response = ProductMapper.productToProductResponse(product);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Listar todos os produtos ativos
     */
    @GetMapping
    @Operation(summary = "Listar produtos", description = "Retorna uma lista paginada de produtos ativos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    })
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @Parameter(description = "Número da página (0-based)") @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação") @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @Parameter(description = "Direção da ordenação") @RequestParam(value = "sortDir", defaultValue = "ASC") String sortDir) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDir.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Product> products = productService.getAllActiveProducts(pageable);
        Page<ProductResponse> response = products.map(ProductMapper::productToProductResponse);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Listar produtos por categoria
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "Listar produtos por categoria", description = "Retorna produtos de uma categoria específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    })
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(
            @Parameter(description = "Categoria dos produtos") @PathVariable String category) {
        
        List<Product> products = productService.getProductsByCategory(category);
        List<ProductResponse> response = ProductMapper.productListToProductResponseList(products);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Listar produtos por reino
     */
    @GetMapping("/kingdom/{kingdomId}")
    @Operation(summary = "Listar produtos por reino", description = "Retorna produtos de um reino específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Reino não encontrado")
    })
    public ResponseEntity<List<ProductResponse>> getProductsByKingdom(
            @Parameter(description = "ID do reino") @PathVariable Long kingdomId) {
        
        List<Product> products = productService.getProductsByKingdom(kingdomId);
        List<ProductResponse> response = ProductMapper.productListToProductResponseList(products);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Listar produtos de alta demanda
     */
    @GetMapping("/high-demand")
    @Operation(summary = "Listar produtos de alta demanda", description = "Retorna produtos com demanda quantifier >= 1.5")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    })
    public ResponseEntity<List<ProductResponse>> getHighDemandProducts() {
        
        List<Product> products = productService.getHighDemandProducts();
        List<ProductResponse> response = ProductMapper.productListToProductResponseList(products);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Listar produtos de alta qualidade
     */
    @GetMapping("/high-quality")
    @Operation(summary = "Listar produtos de alta qualidade", description = "Retorna produtos com quality qualifier >= 1.5")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    })
    public ResponseEntity<List<ProductResponse>> getHighQualityProducts() {
        
        List<Product> products = productService.getHighQualityProducts();
        List<ProductResponse> response = ProductMapper.productListToProductResponseList(products);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Listar produtos de reinos proprietários
     */
    @GetMapping("/owner-kingdoms")
    @Operation(summary = "Listar produtos de reinos proprietários", description = "Retorna produtos de reinos que são proprietários")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    })
    public ResponseEntity<List<ProductResponse>> getProductsFromOwnerKingdoms() {
        
        List<Product> products = productService.getProductsFromOwnerKingdoms();
        List<ProductResponse> response = ProductMapper.productListToProductResponseList(products);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Buscar produtos por valor final (range)
     */
    @GetMapping("/value-range")
    @Operation(summary = "Buscar produtos por valor final", description = "Retorna produtos com valor final dentro do range especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    })
    public ResponseEntity<List<ProductResponse>> getProductsByValueRange(
            @Parameter(description = "Valor mínimo") @RequestParam(value = "minValue", defaultValue = "0") Double minValue,
            @Parameter(description = "Valor máximo") @RequestParam(value = "maxValue", defaultValue = "9999999") Double maxValue) {
        
        List<Product> products = productService.getProductsByFinalValueRange(
            BigDecimal.valueOf(minValue), BigDecimal.valueOf(maxValue));
        
        List<ProductResponse> responses = ProductMapper.productListToProductResponseList(products);
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Atualizar produto
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", description = "Atualiza um produto existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProductResponse> updateProduct(
            @Parameter(description = "ID do produto") @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        
        Product product = productService.updateProduct(
            id,
            request.getName(),
            request.getDescription(),
            request.getCategory(),
            request.getBaseValue(),
            request.getDemandQuantifier(),
            request.getQualityQualifier(),
            request.getKingdomId()
        );
        
        ProductResponse response = ProductMapper.productToProductResponse(product);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Ativar produto
     */
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Ativar produto", description = "Ativa um produto desativado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto ativado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Void> activateProduct(
            @Parameter(description = "ID do produto") @PathVariable Long id) {
        
        productService.activateProduct(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Desativar produto
     */
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Desativar produto", description = "Desativa um produto ativo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto desativado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Void> deactivateProduct(
            @Parameter(description = "ID do produto") @PathVariable Long id) {
        
        productService.deactivateProduct(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Deletar produto (soft delete)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar produto", description = "Remove um produto (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Produto removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID do produto") @PathVariable Long id) {
        
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Contar produtos ativos
     */
    @GetMapping("/count/active")
    @Operation(summary = "Contar produtos ativos", description = "Retorna o número de produtos ativos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contagem retornada com sucesso")
    })
    public ResponseEntity<Long> countActiveProducts() {
        
        long count = productService.countActiveProducts();
        return ResponseEntity.ok(count);
    }
    
    /**
     * Contar produtos por categoria
     */
    @GetMapping("/count/category/{category}")
    @Operation(summary = "Contar produtos por categoria", description = "Retorna o número de produtos de uma categoria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contagem retornada com sucesso")
    })
    public ResponseEntity<Long> countProductsByCategory(
            @Parameter(description = "Categoria dos produtos") @PathVariable String category) {
        
        long count = productService.countProductsByCategory(category);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Contar produtos por reino
     */
    @GetMapping("/count/kingdom/{kingdomId}")
    @Operation(summary = "Contar produtos por reino", description = "Retorna o número de produtos de um reino")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contagem retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Reino não encontrado")
    })
    public ResponseEntity<Long> countProductsByKingdom(
            @Parameter(description = "ID do reino") @PathVariable Long kingdomId) {
        
        long count = productService.countProductsByKingdom(kingdomId);
        return ResponseEntity.ok(count);
    }
} 