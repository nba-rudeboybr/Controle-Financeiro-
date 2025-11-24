package com.financeiro.service;

import com.financeiro.dto.CategoriaDTO;
import com.financeiro.exception.BusinessException;
import com.financeiro.exception.ResourceNotFoundException;
import com.financeiro.model.Categoria;
import com.financeiro.model.TipoTransacao;
import com.financeiro.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para CategoriaService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do CategoriaService")
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;
    private CategoriaDTO categoriaDTO;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Alimentação");
        categoria.setDescricao("Gastos com alimentação");
        categoria.setTipo(TipoTransacao.DESPESA);
        categoria.setCor("#FF5733");

        categoriaDTO = new CategoriaDTO();
        categoriaDTO.setNome("Alimentação");
        categoriaDTO.setDescricao("Gastos com alimentação");
        categoriaDTO.setTipo(TipoTransacao.DESPESA);
        categoriaDTO.setCor("#FF5733");
    }

    @Test
    @DisplayName("Deve criar uma categoria com sucesso")
    void deveCriarCategoriaComSucesso() {
        // Arrange
        when(categoriaRepository.existsByNome(anyString())).thenReturn(false);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        // Act
        CategoriaDTO response = categoriaService.criar(categoriaDTO);

        // Assert
        assertNotNull(response);
        assertEquals("Alimentação", response.getNome());
        assertEquals(TipoTransacao.DESPESA, response.getTipo());
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar categoria com nome duplicado")
    void deveLancarExcecaoAoCriarCategoriaComNomeDuplicado() {
        // Arrange
        when(categoriaRepository.existsByNome(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(BusinessException.class, 
            () -> categoriaService.criar(categoriaDTO));
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Deve listar todas as categorias")
    void deveListarTodasAsCategorias() {
        // Arrange
        List<Categoria> categorias = Arrays.asList(categoria);
        when(categoriaRepository.findAll()).thenReturn(categorias);

        // Act
        List<CategoriaDTO> response = categoriaService.listarTodas();

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Alimentação", response.get(0).getNome());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar categoria por ID com sucesso")
    void deveBuscarCategoriaPorIdComSucesso() {
        // Arrange
        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.of(categoria));

        // Act
        CategoriaDTO response = categoriaService.buscarPorId(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Alimentação", response.getNome());
        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar categoria inexistente")
    void deveLancarExcecaoAoBuscarCategoriaInexistente() {
        // Arrange
        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> categoriaService.buscarPorId(999L));
    }

    @Test
    @DisplayName("Deve listar categorias por tipo")
    void deveListarCategoriasPorTipo() {
        // Arrange
        List<Categoria> categorias = Arrays.asList(categoria);
        when(categoriaRepository.findByTipo(TipoTransacao.DESPESA)).thenReturn(categorias);

        // Act
        List<CategoriaDTO> response = categoriaService.listarPorTipo(TipoTransacao.DESPESA);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(TipoTransacao.DESPESA, response.get(0).getTipo());
        verify(categoriaRepository, times(1)).findByTipo(TipoTransacao.DESPESA);
    }

    @Test
    @DisplayName("Deve atualizar categoria com sucesso")
    void deveAtualizarCategoriaComSucesso() {
        // Arrange
        categoriaDTO.setNome("Alimentação Atualizada");
        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.of(categoria));
        when(categoriaRepository.existsByNome(anyString())).thenReturn(false);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        // Act
        CategoriaDTO response = categoriaService.atualizar(1L, categoriaDTO);

        // Assert
        assertNotNull(response);
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar categoria inexistente")
    void deveLancarExcecaoAoAtualizarCategoriaInexistente() {
        // Arrange
        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> categoriaService.atualizar(999L, categoriaDTO));
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar categoria com nome duplicado")
    void deveLancarExcecaoAoAtualizarCategoriaComNomeDuplicado() {
        // Arrange
        Categoria outraCategoria = new Categoria();
        outraCategoria.setId(2L);
        outraCategoria.setNome("Outra Categoria");
        
        categoriaDTO.setNome("Outra Categoria");
        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.of(categoria));
        when(categoriaRepository.existsByNome("Outra Categoria")).thenReturn(true);

        // Act & Assert
        assertThrows(BusinessException.class, 
            () -> categoriaService.atualizar(1L, categoriaDTO));
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Deve atualizar categoria mantendo o mesmo nome")
    void deveAtualizarCategoriaMantendoMesmoNome() {
        // Arrange
        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        // Act
        CategoriaDTO response = categoriaService.atualizar(1L, categoriaDTO);

        // Assert
        assertNotNull(response);
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Deve deletar categoria com sucesso")
    void deveDeletarCategoriaComSucesso() {
        // Arrange
        when(categoriaRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(categoriaRepository).deleteById(anyLong());

        // Act
        categoriaService.deletar(1L);

        // Assert
        verify(categoriaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar categoria inexistente")
    void deveLancarExcecaoAoDeletarCategoriaInexistente() {
        // Arrange
        when(categoriaRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> categoriaService.deletar(999L));
        verify(categoriaRepository, never()).deleteById(anyLong());
    }
}

