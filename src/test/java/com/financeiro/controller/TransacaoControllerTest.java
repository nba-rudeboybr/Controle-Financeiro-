package com.financeiro.controller;

import com.financeiro.dto.ResumoFinanceiroDTO;
import com.financeiro.dto.TransacaoRequestDTO;
import com.financeiro.dto.TransacaoResponseDTO;
import com.financeiro.exception.ResourceNotFoundException;
import com.financeiro.model.TipoTransacao;
import com.financeiro.service.TransacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes unitários para TransacaoController.
 */
@WebMvcTest(TransacaoController.class)
@DisplayName("Testes do TransacaoController")
class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransacaoService transacaoService;

    private TransacaoRequestDTO transacaoRequest;
    private TransacaoResponseDTO transacaoResponse;

    @BeforeEach
    void setUp() {
        transacaoRequest = new TransacaoRequestDTO();
        transacaoRequest.setDescricao("Almoço no restaurante");
        transacaoRequest.setValor(new BigDecimal("150.50"));
        transacaoRequest.setTipo(TipoTransacao.DESPESA);
        transacaoRequest.setData(LocalDate.now());
        transacaoRequest.setCategoriaId(1L);
        transacaoRequest.setObservacoes("Pagamento via cartão");

        transacaoResponse = new TransacaoResponseDTO();
        transacaoResponse.setId(1L);
        transacaoResponse.setDescricao("Almoço no restaurante");
        transacaoResponse.setValor(new BigDecimal("150.50"));
        transacaoResponse.setTipo(TipoTransacao.DESPESA);
        transacaoResponse.setData(LocalDate.now());
        transacaoResponse.setCategoriaNome("Alimentação");
        transacaoResponse.setCategoriaCor("#FF5733");
        transacaoResponse.setObservacoes("Pagamento via cartão");
        transacaoResponse.setCriadoEm(LocalDateTime.now());
        transacaoResponse.setAtualizadoEm(LocalDateTime.now());
    }

    @Test
    @DisplayName("POST /api/transacoes - Deve criar transação com sucesso")
    void deveCriarTransacaoComSucesso() throws Exception {
        // Arrange
        when(transacaoService.criar(any(TransacaoRequestDTO.class))).thenReturn(transacaoResponse);

        // Act & Assert
        mockMvc.perform(post("/api/transacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transacaoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Almoço no restaurante"))
                .andExpect(jsonPath("$.valor").value(150.50));
    }

    @Test
    @DisplayName("POST /api/transacoes - Deve retornar 400 para dados inválidos")
    void deveRetornar400ParaDadosInvalidos() throws Exception {
        // Arrange
        transacaoRequest.setDescricao(""); // Descrição vazia (inválido)

        // Act & Assert
        mockMvc.perform(post("/api/transacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transacaoRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/transacoes - Deve listar todas as transações")
    void deveListarTodasAsTransacoes() throws Exception {
        // Arrange
        List<TransacaoResponseDTO> transacoes = Arrays.asList(transacaoResponse);
        when(transacaoService.listarTodas()).thenReturn(transacoes);

        // Act & Assert
        mockMvc.perform(get("/api/transacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Almoço no restaurante"));
    }

    @Test
    @DisplayName("GET /api/transacoes/{id} - Deve buscar transação por ID")
    void deveBuscarTransacaoPorId() throws Exception {
        // Arrange
        when(transacaoService.buscarPorId(anyLong())).thenReturn(transacaoResponse);

        // Act & Assert
        mockMvc.perform(get("/api/transacoes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("Almoço no restaurante"));
    }

    @Test
    @DisplayName("GET /api/transacoes/{id} - Deve retornar 404 para transação inexistente")
    void deveRetornar404ParaTransacaoInexistente() throws Exception {
        // Arrange
        when(transacaoService.buscarPorId(anyLong()))
            .thenThrow(new ResourceNotFoundException("Transação", 999L));

        // Act & Assert
        mockMvc.perform(get("/api/transacoes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/transacoes/{id} - Deve atualizar transação com sucesso")
    void deveAtualizarTransacaoComSucesso() throws Exception {
        // Arrange
        when(transacaoService.atualizar(anyLong(), any(TransacaoRequestDTO.class)))
            .thenReturn(transacaoResponse);

        // Act & Assert
        mockMvc.perform(put("/api/transacoes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transacaoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Almoço no restaurante"));
    }

    @Test
    @DisplayName("DELETE /api/transacoes/{id} - Deve deletar transação com sucesso")
    void deveDeletarTransacaoComSucesso() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/transacoes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/transacoes/tipo/{tipo} - Deve buscar transações por tipo")
    void deveBuscarTransacoesPorTipo() throws Exception {
        // Arrange
        List<TransacaoResponseDTO> transacoes = Arrays.asList(transacaoResponse);
        when(transacaoService.buscarPorTipo(TipoTransacao.DESPESA)).thenReturn(transacoes);

        // Act & Assert
        mockMvc.perform(get("/api/transacoes/tipo/DESPESA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("DESPESA"));
    }

    @Test
    @DisplayName("GET /api/transacoes/resumo - Deve obter resumo financeiro")
    void deveObterResumoFinanceiro() throws Exception {
        // Arrange
        ResumoFinanceiroDTO resumo = new ResumoFinanceiroDTO();
        resumo.setTotalReceitas(new BigDecimal("5000.00"));
        resumo.setTotalDespesas(new BigDecimal("3000.00"));
        resumo.setSaldo(new BigDecimal("2000.00"));
        resumo.setQuantidadeTransacoes(10L);
        
        when(transacaoService.obterResumo(any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(resumo);

        // Act & Assert
        mockMvc.perform(get("/api/transacoes/resumo")
                .param("dataInicio", "2025-10-01")
                .param("dataFim", "2025-10-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalReceitas").value(5000.00))
                .andExpect(jsonPath("$.totalDespesas").value(3000.00))
                .andExpect(jsonPath("$.saldo").value(2000.00));
    }

    @Test
    @DisplayName("GET /api/transacoes/periodo - Deve buscar transações por período")
    void deveBuscarTransacoesPorPeriodo() throws Exception {
        // Arrange
        List<TransacaoResponseDTO> transacoes = Arrays.asList(transacaoResponse);
        when(transacaoService.buscarPorPeriodo(any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(transacoes);

        // Act & Assert
        mockMvc.perform(get("/api/transacoes/periodo")
                .param("dataInicio", "2025-10-01")
                .param("dataFim", "2025-10-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Almoço no restaurante"));
    }

    @Test
    @DisplayName("GET /api/transacoes/categoria/{categoriaId} - Deve buscar transações por categoria")
    void deveBuscarTransacoesPorCategoria() throws Exception {
        // Arrange
        List<TransacaoResponseDTO> transacoes = Arrays.asList(transacaoResponse);
        when(transacaoService.buscarPorCategoria(anyLong())).thenReturn(transacoes);

        // Act & Assert
        mockMvc.perform(get("/api/transacoes/categoria/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoriaNome").value("Alimentação"));
    }

    @Test
    @DisplayName("GET /api/transacoes/buscar - Deve buscar transações por descrição")
    void deveBuscarTransacoesPorDescricao() throws Exception {
        // Arrange
        List<TransacaoResponseDTO> transacoes = Arrays.asList(transacaoResponse);
        when(transacaoService.buscarPorDescricao(anyString())).thenReturn(transacoes);

        // Act & Assert
        mockMvc.perform(get("/api/transacoes/buscar")
                .param("texto", "almoço"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao").value("Almoço no restaurante"));
    }

    @Test
    @DisplayName("DELETE /api/transacoes/{id} - Deve retornar 404 para transação inexistente")
    void deveRetornar404AoDeletarTransacaoInexistente() throws Exception {
        // Arrange
        org.mockito.Mockito.doThrow(new ResourceNotFoundException("Transação", 999L))
            .when(transacaoService).deletar(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/transacoes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/transacoes/{id} - Deve retornar 404 para transação inexistente")
    void deveRetornar404AoAtualizarTransacaoInexistente() throws Exception {
        // Arrange
        when(transacaoService.atualizar(anyLong(), any(TransacaoRequestDTO.class)))
            .thenThrow(new ResourceNotFoundException("Transação", 999L));

        // Act & Assert
        mockMvc.perform(put("/api/transacoes/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transacaoRequest)))
                .andExpect(status().isNotFound());
    }
}

