package com.financeiro.service;

import com.financeiro.dto.ResumoFinanceiroDTO;
import com.financeiro.dto.TransacaoRequestDTO;
import com.financeiro.dto.TransacaoResponseDTO;
import com.financeiro.exception.ResourceNotFoundException;
import com.financeiro.model.Categoria;
import com.financeiro.model.TipoTransacao;
import com.financeiro.model.Transacao;
import com.financeiro.repository.CategoriaRepository;
import com.financeiro.repository.TransacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para TransacaoService.
 * Utiliza JUnit 5 e Mockito para mockar dependências.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do TransacaoService")
class TransacaoServiceTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private TransacaoService transacaoService;

    private Transacao transacao;
    private Categoria categoria;
    private TransacaoRequestDTO transacaoRequest;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Alimentação");
        categoria.setTipo(TipoTransacao.DESPESA);
        categoria.setCor("#FF5733");

        transacao = new Transacao();
        transacao.setId(1L);
        transacao.setDescricao("Almoço no restaurante");
        transacao.setValor(new BigDecimal("150.50"));
        transacao.setTipo(TipoTransacao.DESPESA);
        transacao.setData(LocalDate.now());
        transacao.setCategoria(categoria);
        transacao.setObservacoes("Pagamento via cartão");
        transacao.setCriadoEm(LocalDateTime.now());
        transacao.setAtualizadoEm(LocalDateTime.now());

        transacaoRequest = new TransacaoRequestDTO();
        transacaoRequest.setDescricao("Almoço no restaurante");
        transacaoRequest.setValor(new BigDecimal("150.50"));
        transacaoRequest.setTipo(TipoTransacao.DESPESA);
        transacaoRequest.setData(LocalDate.now());
        transacaoRequest.setCategoriaId(1L);
        transacaoRequest.setObservacoes("Pagamento via cartão");
    }

    @Test
    @DisplayName("Deve criar uma transação com sucesso")
    void deveCriarTransacaoComSucesso() {
        // Arrange
        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.of(categoria));
        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);

        // Act
        TransacaoResponseDTO response = transacaoService.criar(transacaoRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Almoço no restaurante", response.getDescricao());
        assertEquals(new BigDecimal("150.50"), response.getValor());
        assertEquals(TipoTransacao.DESPESA, response.getTipo());
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar transação com categoria inexistente")
    void deveLancarExcecaoAoCriarTransacaoComCategoriaInexistente() {
        // Arrange
        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> transacaoService.criar(transacaoRequest));
    }

    @Test
    @DisplayName("Deve listar todas as transações")
    void deveListarTodasAsTransacoes() {
        // Arrange
        List<Transacao> transacoes = Arrays.asList(transacao);
        when(transacaoRepository.findAll()).thenReturn(transacoes);

        // Act
        List<TransacaoResponseDTO> response = transacaoService.listarTodas();

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Almoço no restaurante", response.get(0).getDescricao());
        verify(transacaoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar transação por ID com sucesso")
    void deveBuscarTransacaoPorIdComSucesso() {
        // Arrange
        when(transacaoRepository.findById(anyLong())).thenReturn(Optional.of(transacao));

        // Act
        TransacaoResponseDTO response = transacaoService.buscarPorId(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Almoço no restaurante", response.getDescricao());
        verify(transacaoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar transação inexistente")
    void deveLancarExcecaoAoBuscarTransacaoInexistente() {
        // Arrange
        when(transacaoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> transacaoService.buscarPorId(999L));
    }

    @Test
    @DisplayName("Deve buscar transações por tipo")
    void deveBuscarTransacoesPorTipo() {
        // Arrange
        List<Transacao> transacoes = Arrays.asList(transacao);
        when(transacaoRepository.findByTipo(TipoTransacao.DESPESA)).thenReturn(transacoes);

        // Act
        List<TransacaoResponseDTO> response = transacaoService.buscarPorTipo(TipoTransacao.DESPESA);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(TipoTransacao.DESPESA, response.get(0).getTipo());
    }

    @Test
    @DisplayName("Deve buscar transações por período")
    void deveBuscarTransacoesPorPeriodo() {
        // Arrange
        LocalDate dataInicio = LocalDate.now().minusDays(7);
        LocalDate dataFim = LocalDate.now();
        List<Transacao> transacoes = Arrays.asList(transacao);
        when(transacaoRepository.findByDataBetween(dataInicio, dataFim)).thenReturn(transacoes);

        // Act
        List<TransacaoResponseDTO> response = transacaoService.buscarPorPeriodo(dataInicio, dataFim);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    @DisplayName("Deve buscar transações por categoria")
    void deveBuscarTransacoesPorCategoria() {
        // Arrange
        List<Transacao> transacoes = Arrays.asList(transacao);
        when(categoriaRepository.existsById(anyLong())).thenReturn(true);
        when(transacaoRepository.findByCategoriaId(anyLong())).thenReturn(transacoes);

        // Act
        List<TransacaoResponseDTO> response = transacaoService.buscarPorCategoria(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    @DisplayName("Deve buscar transações por descrição")
    void deveBuscarTransacoesPorDescricao() {
        // Arrange
        List<Transacao> transacoes = Arrays.asList(transacao);
        when(transacaoRepository.findByDescricaoContainingIgnoreCase("almoço")).thenReturn(transacoes);

        // Act
        List<TransacaoResponseDTO> response = transacaoService.buscarPorDescricao("almoço");

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());
        assertTrue(response.get(0).getDescricao().toLowerCase().contains("almoço"));
    }

    @Test
    @DisplayName("Deve obter resumo financeiro")
    void deveObterResumoFinanceiro() {
        // Arrange
        LocalDate dataInicio = LocalDate.now().minusDays(30);
        LocalDate dataFim = LocalDate.now();
        BigDecimal totalReceitas = new BigDecimal("5000.00");
        BigDecimal totalDespesas = new BigDecimal("3000.00");
        List<Transacao> transacoes = Arrays.asList(transacao);

        when(transacaoRepository.calcularTotalReceitas(dataInicio, dataFim)).thenReturn(totalReceitas);
        when(transacaoRepository.calcularTotalDespesas(dataInicio, dataFim)).thenReturn(totalDespesas);
        when(transacaoRepository.findByDataBetween(dataInicio, dataFim)).thenReturn(transacoes);

        // Act
        ResumoFinanceiroDTO resumo = transacaoService.obterResumo(dataInicio, dataFim);

        // Assert
        assertNotNull(resumo);
        assertEquals(totalReceitas, resumo.getTotalReceitas());
        assertEquals(totalDespesas, resumo.getTotalDespesas());
        assertEquals(new BigDecimal("2000.00"), resumo.getSaldo());
        assertEquals(1L, resumo.getQuantidadeTransacoes());
    }

    @Test
    @DisplayName("Deve atualizar transação com sucesso")
    void deveAtualizarTransacaoComSucesso() {
        // Arrange
        when(transacaoRepository.findById(anyLong())).thenReturn(Optional.of(transacao));
        when(categoriaRepository.findById(anyLong())).thenReturn(Optional.of(categoria));
        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);

        // Act
        TransacaoResponseDTO response = transacaoService.atualizar(1L, transacaoRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Almoço no restaurante", response.getDescricao());
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar transação inexistente")
    void deveLancarExcecaoAoAtualizarTransacaoInexistente() {
        // Arrange
        when(transacaoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> transacaoService.atualizar(999L, transacaoRequest));
    }

    @Test
    @DisplayName("Deve deletar transação com sucesso")
    void deveDeletarTransacaoComSucesso() {
        // Arrange
        when(transacaoRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(transacaoRepository).deleteById(anyLong());

        // Act
        transacaoService.deletar(1L);

        // Assert
        verify(transacaoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar transação inexistente")
    void deveLancarExcecaoAoDeletarTransacaoInexistente() {
        // Arrange
        when(transacaoRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
            () -> transacaoService.deletar(999L));
    }
}

