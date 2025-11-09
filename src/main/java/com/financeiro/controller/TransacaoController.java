package com.financeiro.controller;

import com.financeiro.dto.ResumoFinanceiroDTO;
import com.financeiro.dto.TransacaoRequestDTO;
import com.financeiro.dto.TransacaoResponseDTO;
import com.financeiro.model.TipoTransacao;
import com.financeiro.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller REST para gerenciar Transações Financeiras.
 * Implementa 9 rotas com diferentes verbos HTTP (requisito: mínimo 6).
 */
@RestController
@RequestMapping("/api/transacoes")
@Tag(name = "Transações", description = "Endpoints para gerenciamento de transações financeiras")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    /**
     * Rota 1: POST /api/transacoes - Criar uma nova transação
     */
    @PostMapping
    @Operation(summary = "Criar nova transação", description = "Registra uma nova transação financeira (receita ou despesa)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transação criada com sucesso",
            content = @Content(schema = @Schema(implementation = TransacaoResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<TransacaoResponseDTO> criar(@Valid @RequestBody TransacaoRequestDTO request) {
        TransacaoResponseDTO response = transacaoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Rota 2: GET /api/transacoes - Listar todas as transações
     */
    @GetMapping
    @Operation(summary = "Listar todas as transações", description = "Retorna lista completa de transações")
    @ApiResponse(responseCode = "200", description = "Lista de transações retornada")
    public ResponseEntity<List<TransacaoResponseDTO>> listarTodas() {
        List<TransacaoResponseDTO> transacoes = transacaoService.listarTodas();
        return ResponseEntity.ok(transacoes);
    }

    /**
     * Rota 3: GET /api/transacoes/{id} - Buscar transação por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar transação por ID", description = "Retorna uma transação específica pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transação encontrada",
            content = @Content(schema = @Schema(implementation = TransacaoResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    public ResponseEntity<TransacaoResponseDTO> buscarPorId(
            @Parameter(description = "ID da transação") @PathVariable Long id) {
        TransacaoResponseDTO transacao = transacaoService.buscarPorId(id);
        return ResponseEntity.ok(transacao);
    }

    /**
     * Rota 4: PUT /api/transacoes/{id} - Atualizar uma transação
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar transação", description = "Atualiza os dados de uma transação existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transação atualizada com sucesso",
            content = @Content(schema = @Schema(implementation = TransacaoResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    public ResponseEntity<TransacaoResponseDTO> atualizar(
            @Parameter(description = "ID da transação") @PathVariable Long id,
            @Valid @RequestBody TransacaoRequestDTO request) {
        TransacaoResponseDTO response = transacaoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Rota 5: DELETE /api/transacoes/{id} - Deletar uma transação
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar transação", description = "Remove uma transação do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Transação deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da transação") @PathVariable Long id) {
        transacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Rota 6: GET /api/transacoes/tipo/{tipo} - Buscar transações por tipo
     */
    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Buscar transações por tipo", description = "Retorna transações filtradas por tipo (RECEITA ou DESPESA)")
    @ApiResponse(responseCode = "200", description = "Lista de transações do tipo especificado")
    public ResponseEntity<List<TransacaoResponseDTO>> buscarPorTipo(
            @Parameter(description = "Tipo da transação (RECEITA ou DESPESA)") @PathVariable TipoTransacao tipo) {
        List<TransacaoResponseDTO> transacoes = transacaoService.buscarPorTipo(tipo);
        return ResponseEntity.ok(transacoes);
    }

    /**
     * Rota 7: GET /api/transacoes/periodo - Buscar transações por período
     */
    @GetMapping("/periodo")
    @Operation(summary = "Buscar transações por período", description = "Retorna transações entre duas datas")
    @ApiResponse(responseCode = "200", description = "Lista de transações do período")
    public ResponseEntity<List<TransacaoResponseDTO>> buscarPorPeriodo(
            @Parameter(description = "Data inicial (formato: YYYY-MM-DD)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Data final (formato: YYYY-MM-DD)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<TransacaoResponseDTO> transacoes = transacaoService.buscarPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(transacoes);
    }

    /**
     * Rota 8: GET /api/transacoes/categoria/{categoriaId} - Buscar por categoria
     */
    @GetMapping("/categoria/{categoriaId}")
    @Operation(summary = "Buscar transações por categoria", description = "Retorna transações de uma categoria específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de transações da categoria"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    public ResponseEntity<List<TransacaoResponseDTO>> buscarPorCategoria(
            @Parameter(description = "ID da categoria") @PathVariable Long categoriaId) {
        List<TransacaoResponseDTO> transacoes = transacaoService.buscarPorCategoria(categoriaId);
        return ResponseEntity.ok(transacoes);
    }

    /**
     * Rota 9: GET /api/transacoes/resumo - Obter resumo financeiro
     */
    @GetMapping("/resumo")
    @Operation(summary = "Obter resumo financeiro", 
               description = "Retorna resumo com receitas, despesas e saldo de um período")
    @ApiResponse(responseCode = "200", description = "Resumo financeiro calculado",
        content = @Content(schema = @Schema(implementation = ResumoFinanceiroDTO.class)))
    public ResponseEntity<ResumoFinanceiroDTO> obterResumo(
            @Parameter(description = "Data inicial (formato: YYYY-MM-DD)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Data final (formato: YYYY-MM-DD)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        ResumoFinanceiroDTO resumo = transacaoService.obterResumo(dataInicio, dataFim);
        return ResponseEntity.ok(resumo);
    }

    /**
     * Rota 10: GET /api/transacoes/buscar - Buscar por descrição
     */
    @GetMapping("/buscar")
    @Operation(summary = "Buscar transações por descrição", description = "Busca transações que contenham o texto na descrição")
    @ApiResponse(responseCode = "200", description = "Lista de transações encontradas")
    public ResponseEntity<List<TransacaoResponseDTO>> buscarPorDescricao(
            @Parameter(description = "Texto para buscar na descrição") @RequestParam String texto) {
        List<TransacaoResponseDTO> transacoes = transacaoService.buscarPorDescricao(texto);
        return ResponseEntity.ok(transacoes);
    }
}

