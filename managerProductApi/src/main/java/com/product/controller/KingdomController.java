package com.product.controller;

import com.product.domain.dto.KingdomRequest;
import com.product.domain.dto.KingdomResponse;
import com.product.domain.entity.Kingdom;
import com.product.domain.mapper.KingdomMapper;
import com.product.service.KingdomService;
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

import java.util.List;

/**
 * Controller para gerenciamento de Reinos
 */
@RestController
@RequestMapping("/api/v1/kingdoms")
@Tag(name = "Kingdom", description = "APIs para gerenciamento de Reinos")
public class KingdomController {
    
    @Autowired
    private KingdomService kingdomService;
    
    /**
     * Criar um novo reino
     */
    @PostMapping
    @Operation(summary = "Criar novo reino", description = "Cria um novo reino com os dados fornecidos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reino criado com sucesso",
                    content = @Content(schema = @Schema(implementation = KingdomResponse.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Reino já existe")
    })
    public ResponseEntity<KingdomResponse> createKingdom(
            @Valid @RequestBody KingdomRequest request) {
        
        Kingdom kingdom = kingdomService.createKingdom(
            request.getName(),
            request.getDescription(),
            request.getQualityRate(),
            request.getIsOwner()
        );
        
        KingdomResponse response = KingdomMapper.kingdomToKingdomResponse(kingdom);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Buscar reino por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar reino por ID", description = "Retorna um reino específico pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reino encontrado",
                    content = @Content(schema = @Schema(implementation = KingdomResponse.class))),
        @ApiResponse(responseCode = "404", description = "Reino não encontrado")
    })
    public ResponseEntity<KingdomResponse> getKingdomById(
            @Parameter(description = "ID do reino") @PathVariable Long id) {
        
        Kingdom kingdom = kingdomService.getKingdomById(id);
        KingdomResponse response = KingdomMapper.kingdomToKingdomResponse(kingdom);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Buscar reino por nome
     */
    @GetMapping("/name/{name}")
    @Operation(summary = "Buscar reino por nome", description = "Retorna um reino específico pelo nome")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reino encontrado",
                    content = @Content(schema = @Schema(implementation = KingdomResponse.class))),
        @ApiResponse(responseCode = "404", description = "Reino não encontrado")
    })
    public ResponseEntity<KingdomResponse> getKingdomByName(
            @Parameter(description = "Nome do reino") @PathVariable String name) {
        
        Kingdom kingdom = kingdomService.getKingdomByName(name);
        KingdomResponse response = KingdomMapper.kingdomToKingdomResponse(kingdom);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Listar todos os reinos ativos
     */
    @GetMapping
    @Operation(summary = "Listar reinos", description = "Retorna uma lista paginada de reinos ativos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de reinos retornada com sucesso")
    })
    public ResponseEntity<Page<KingdomResponse>> getAllKingdoms(
            @Parameter(description = "Número da página (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Direção da ordenação") @RequestParam(defaultValue = "ASC") String sortDir) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDir.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Kingdom> kingdoms = kingdomService.getAllActiveKingdoms(pageable);
        Page<KingdomResponse> response = kingdoms.map(KingdomMapper::kingdomToKingdomResponse);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Listar reinos proprietários
     */
    @GetMapping("/owners")
    @Operation(summary = "Listar reinos proprietários", description = "Retorna todos os reinos proprietários")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de reinos proprietários retornada com sucesso")
    })
    public ResponseEntity<List<KingdomResponse>> getOwnerKingdoms() {
        
        List<Kingdom> kingdoms = kingdomService.getOwnerKingdoms();
        List<KingdomResponse> response = KingdomMapper.kingdomListToKingdomResponseList(kingdoms);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Listar reinos por qualidade
     */
    @GetMapping("/quality/{quality}")
    @Operation(summary = "Listar reinos por qualidade", description = "Retorna reinos com qualidade específica (high/low)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de reinos retornada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Qualidade inválida")
    })
    public ResponseEntity<List<KingdomResponse>> getKingdomsByQuality(
            @Parameter(description = "Qualidade (high/low)") @PathVariable String quality) {
        
        List<Kingdom> kingdoms;
        if ("high".equalsIgnoreCase(quality)) {
            kingdoms = kingdomService.getHighQualityKingdoms();
        } else if ("low".equalsIgnoreCase(quality)) {
            kingdoms = kingdomService.getLowQualityKingdoms();
        } else {
            return ResponseEntity.badRequest().build();
        }
        
        List<KingdomResponse> response = KingdomMapper.kingdomListToKingdomResponseList(kingdoms);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Atualizar reino
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar reino", description = "Atualiza um reino existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reino atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = KingdomResponse.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Reino não encontrado")
    })
    public ResponseEntity<KingdomResponse> updateKingdom(
            @Parameter(description = "ID do reino") @PathVariable Long id,
            @Valid @RequestBody KingdomRequest request) {
        
        Kingdom kingdom = kingdomService.updateKingdom(
            id,
            request.getName(),
            request.getDescription(),
            request.getQualityRate(),
            request.getIsOwner()
        );
        
        KingdomResponse response = KingdomMapper.kingdomToKingdomResponse(kingdom);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Ativar reino
     */
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Ativar reino", description = "Ativa um reino desativado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reino ativado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Reino não encontrado")
    })
    public ResponseEntity<Void> activateKingdom(
            @Parameter(description = "ID do reino") @PathVariable Long id) {
        
        kingdomService.activateKingdom(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Desativar reino
     */
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Desativar reino", description = "Desativa um reino ativo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reino desativado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Reino não encontrado")
    })
    public ResponseEntity<Void> deactivateKingdom(
            @Parameter(description = "ID do reino") @PathVariable Long id) {
        
        kingdomService.deactivateKingdom(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Deletar reino (soft delete)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar reino", description = "Remove um reino (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Reino removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Reino não encontrado")
    })
    public ResponseEntity<Void> deleteKingdom(
            @Parameter(description = "ID do reino") @PathVariable Long id) {
        
        kingdomService.deleteKingdom(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Contar reinos ativos
     */
    @GetMapping("/count/active")
    @Operation(summary = "Contar reinos ativos", description = "Retorna o número de reinos ativos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contagem retornada com sucesso")
    })
    public ResponseEntity<Long> countActiveKingdoms() {
        
        long count = kingdomService.countActiveKingdoms();
        return ResponseEntity.ok(count);
    }
    
    /**
     * Contar reinos proprietários
     */
    @GetMapping("/count/owners")
    @Operation(summary = "Contar reinos proprietários", description = "Retorna o número de reinos proprietários")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contagem retornada com sucesso")
    })
    public ResponseEntity<Long> countOwnerKingdoms() {
        
        long count = kingdomService.countOwnerKingdoms();
        return ResponseEntity.ok(count);
    }
} 