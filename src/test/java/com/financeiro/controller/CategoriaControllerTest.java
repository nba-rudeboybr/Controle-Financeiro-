package com.financeiro.controller;

import com.financeiro.dto.CategoriaDTO;
import com.financeiro.exception.BusinessException;
import com.financeiro.exception.ResourceNotFoundException;
import com.financeiro.model.TipoTransacao;
import com.financeiro.service.CategoriaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes unitários para CategoriaController.
 */
@WebMvcTest(CategoriaController.class)
@DisplayName("Testes do CategoriaController")
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoriaService categoriaService;

    private CategoriaDTO categoriaDTO;

    @BeforeEach
    void setUp() {
        categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(1L);
        categoriaDTO.setNome("Alimentação");
        categoriaDTO.setDescricao("Gastos com alimentação");
        categoriaDTO.setTipo(TipoTransacao.DESPESA);
        categoriaDTO.setCor("#FF5733");
    }

    @Test
    @DisplayName("POST /api/categorias - Deve criar categoria com sucesso")
    void deveCriarCategoriaComSucesso() throws Exception {
        // Arrange
        when(categoriaService.criar(any(CategoriaDTO.class))).thenReturn(categoriaDTO);

        // Act & Assert
        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoriaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Alimentação"))
                .andExpect(jsonPath("$.tipo").value("DESPESA"));
    }

    @Test
    @DisplayName("POST /api/categorias - Deve retornar 400 para dados inválidos")
    void deveRetornar400ParaDadosInvalidos() throws Exception {
        // Arrange
        categoriaDTO.setNome(""); // Nome vazio (inválido)

        // Act & Assert
        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoriaDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/categorias - Deve retornar 400 para categoria duplicada")
    void deveRetornar400ParaCategoriaDuplicada() throws Exception {
        // Arrange
        when(categoriaService.criar(any(CategoriaDTO.class)))
            .thenThrow(new BusinessException("Já existe uma categoria com o nome: Alimentação"));

        // Act & Assert
        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoriaDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/categorias - Deve listar todas as categorias")
    void deveListarTodasAsCategorias() throws Exception {
        // Arrange
        List<CategoriaDTO> categorias = Arrays.asList(categoriaDTO);
        when(categoriaService.listarTodas()).thenReturn(categorias);

        // Act & Assert
        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Alimentação"))
                .andExpect(jsonPath("$[0].tipo").value("DESPESA"));
    }

    @Test
    @DisplayName("GET /api/categorias/{id} - Deve buscar categoria por ID")
    void deveBuscarCategoriaPorId() throws Exception {
        // Arrange
        when(categoriaService.buscarPorId(anyLong())).thenReturn(categoriaDTO);

        // Act & Assert
        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Alimentação"));
    }

    @Test
    @DisplayName("GET /api/categorias/{id} - Deve retornar 404 para categoria inexistente")
    void deveRetornar404ParaCategoriaInexistente() throws Exception {
        // Arrange
        when(categoriaService.buscarPorId(anyLong()))
            .thenThrow(new ResourceNotFoundException("Categoria", 999L));

        // Act & Assert
        mockMvc.perform(get("/api/categorias/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/categorias/tipo/{tipo} - Deve listar categorias por tipo")
    void deveListarCategoriasPorTipo() throws Exception {
        // Arrange
        List<CategoriaDTO> categorias = Arrays.asList(categoriaDTO);
        when(categoriaService.listarPorTipo(TipoTransacao.DESPESA)).thenReturn(categorias);

        // Act & Assert
        mockMvc.perform(get("/api/categorias/tipo/DESPESA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("DESPESA"));
    }

    @Test
    @DisplayName("PUT /api/categorias/{id} - Deve atualizar categoria com sucesso")
    void deveAtualizarCategoriaComSucesso() throws Exception {
        // Arrange
        categoriaDTO.setNome("Alimentação Atualizada");
        when(categoriaService.atualizar(anyLong(), any(CategoriaDTO.class)))
            .thenReturn(categoriaDTO);

        // Act & Assert
        mockMvc.perform(put("/api/categorias/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoriaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Alimentação Atualizada"));
    }

    @Test
    @DisplayName("PUT /api/categorias/{id} - Deve retornar 404 para categoria inexistente")
    void deveRetornar404AoAtualizarCategoriaInexistente() throws Exception {
        // Arrange
        when(categoriaService.atualizar(anyLong(), any(CategoriaDTO.class)))
            .thenThrow(new ResourceNotFoundException("Categoria", 999L));

        // Act & Assert
        mockMvc.perform(put("/api/categorias/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoriaDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/categorias/{id} - Deve deletar categoria com sucesso")
    void deveDeletarCategoriaComSucesso() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/categorias/{id} - Deve retornar 404 para categoria inexistente")
    void deveRetornar404AoDeletarCategoriaInexistente() throws Exception {
        // Arrange
        org.mockito.Mockito.doThrow(new ResourceNotFoundException("Categoria", 999L))
            .when(categoriaService).deletar(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/categorias/999"))
                .andExpect(status().isNotFound());
    }
}

