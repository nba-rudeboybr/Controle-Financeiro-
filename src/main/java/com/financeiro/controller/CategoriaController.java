package com.financeiro.controller;

import com.financeiro.dto.CategoriaDTO;
import com.financeiro.model.TipoTransacao;
import com.financeiro.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciar Categorias de transações.
 */
@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categorias", description = "Endpoints para gerenciamento de categorias de transações")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    @Operation(summary = "Criar nova categoria", description = "Cria uma nova categoria de transações")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Nome da categoria já existe")
    })
    public ResponseEntity<CategoriaDTO> criar(@Valid @RequestBody CategoriaDTO dto) {
        CategoriaDTO response = categoriaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar todas as categorias", description = "Retorna lista com todas as categorias")
    @ApiResponse(responseCode = "200", description = "Lista de categorias retornada")
    public ResponseEntity<List<CategoriaDTO>> listarTodas() {
        List<CategoriaDTO> categorias = categoriaService.listarTodas();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar categorias por tipo", description = "Retorna categorias filtradas por tipo")
    @ApiResponse(responseCode = "200", description = "Lista de categorias do tipo especificado")
    public ResponseEntity<List<CategoriaDTO>> listarPorTipo(
            @Parameter(description = "Tipo (RECEITA ou DESPESA)") @PathVariable TipoTransacao tipo) {
        List<CategoriaDTO> categorias = categoriaService.listarPorTipo(tipo);
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoria por ID", description = "Retorna uma categoria específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<CategoriaDTO> buscarPorId(
            @Parameter(description = "ID da categoria") @PathVariable Long id) {
        CategoriaDTO categoria = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria", description = "Atualiza os dados de uma categoria existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<CategoriaDTO> atualizar(
            @Parameter(description = "ID da categoria") @PathVariable Long id,
            @Valid @RequestBody CategoriaDTO dto) {
        CategoriaDTO response = categoriaService.atualizar(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar categoria", description = "Remove uma categoria do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Categoria deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da categoria") @PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

