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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço contendo a lógica de negócio para Transações Financeiras.
 * Demonstra uso de injeção de dependências via construtor e baixo acoplamento.
 */
@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final CategoriaRepository categoriaRepository;

    public TransacaoService(TransacaoRepository transacaoRepository, CategoriaRepository categoriaRepository) {
        this.transacaoRepository = transacaoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public TransacaoResponseDTO criar(TransacaoRequestDTO request) {
        Transacao transacao = new Transacao();
        transacao.setDescricao(request.getDescricao());
        transacao.setValor(request.getValor());
        transacao.setTipo(request.getTipo());
        transacao.setData(request.getData());
        transacao.setObservacoes(request.getObservacoes());

        if (request.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", request.getCategoriaId()));
            transacao.setCategoria(categoria);
        }

        Transacao transacaoSalva = transacaoRepository.save(transacao);
        return TransacaoResponseDTO.fromEntity(transacaoSalva);
    }

    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> listarTodas() {
        return transacaoRepository.findAll().stream()
            .map(TransacaoResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TransacaoResponseDTO buscarPorId(Long id) {
        Transacao transacao = transacaoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Transação", id));
        return TransacaoResponseDTO.fromEntity(transacao);
    }

    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> buscarPorTipo(TipoTransacao tipo) {
        return transacaoRepository.findByTipo(tipo).stream()
            .map(TransacaoResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return transacaoRepository.findByDataBetween(dataInicio, dataFim).stream()
            .map(TransacaoResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> buscarPorCategoria(Long categoriaId) {
        if (!categoriaRepository.existsById(categoriaId)) {
            throw new ResourceNotFoundException("Categoria", categoriaId);
        }
        return transacaoRepository.findByCategoriaId(categoriaId).stream()
            .map(TransacaoResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> buscarPorDescricao(String descricao) {
        return transacaoRepository.findByDescricaoContainingIgnoreCase(descricao).stream()
            .map(TransacaoResponseDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResumoFinanceiroDTO obterResumo(LocalDate dataInicio, LocalDate dataFim) {
        BigDecimal totalReceitas = transacaoRepository.calcularTotalReceitas(dataInicio, dataFim);
        BigDecimal totalDespesas = transacaoRepository.calcularTotalDespesas(dataInicio, dataFim);
        BigDecimal saldo = totalReceitas.subtract(totalDespesas);
        
        List<Transacao> transacoes = transacaoRepository.findByDataBetween(dataInicio, dataFim);
        
        ResumoFinanceiroDTO resumo = new ResumoFinanceiroDTO();
        resumo.setTotalReceitas(totalReceitas);
        resumo.setTotalDespesas(totalDespesas);
        resumo.setSaldo(saldo);
        resumo.setQuantidadeTransacoes((long) transacoes.size());
        resumo.setDataInicio(dataInicio);
        resumo.setDataFim(dataFim);
        
        return resumo;
    }

    @Transactional
    public TransacaoResponseDTO atualizar(Long id, TransacaoRequestDTO request) {
        Transacao transacao = transacaoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Transação", id));

        transacao.setDescricao(request.getDescricao());
        transacao.setValor(request.getValor());
        transacao.setTipo(request.getTipo());
        transacao.setData(request.getData());
        transacao.setObservacoes(request.getObservacoes());

        if (request.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", request.getCategoriaId()));
            transacao.setCategoria(categoria);
        } else {
            transacao.setCategoria(null);
        }

        Transacao transacaoAtualizada = transacaoRepository.save(transacao);
        return TransacaoResponseDTO.fromEntity(transacaoAtualizada);
    }

    @Transactional
    public void deletar(Long id) {
        if (!transacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transação", id);
        }
        transacaoRepository.deleteById(id);
    }
}

