package com.financeiro.service;

import com.financeiro.dto.CategoriaDTO;
import com.financeiro.exception.BusinessException;
import com.financeiro.exception.ResourceNotFoundException;
import com.financeiro.model.Categoria;
import com.financeiro.model.TipoTransacao;
import com.financeiro.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço contendo a lógica de negócio para Categorias.
 */
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public CategoriaDTO criar(CategoriaDTO dto) {
        if (categoriaRepository.existsByNome(dto.getNome())) {
            throw new BusinessException("Já existe uma categoria com o nome: " + dto.getNome());
        }
        Categoria categoria = dto.toEntity();
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        return CategoriaDTO.fromEntity(categoriaSalva);
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarTodas() {
        return categoriaRepository.findAll().stream()
            .map(CategoriaDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarPorTipo(TipoTransacao tipo) {
        return categoriaRepository.findByTipo(tipo).stream()
            .map(CategoriaDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaDTO buscarPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        return CategoriaDTO.fromEntity(categoria);
    }

    @Transactional
    public CategoriaDTO atualizar(Long id, CategoriaDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));

        if (!categoria.getNome().equals(dto.getNome()) && categoriaRepository.existsByNome(dto.getNome())) {
            throw new BusinessException("Já existe uma categoria com o nome: " + dto.getNome());
        }

        categoria.setNome(dto.getNome());
        categoria.setDescricao(dto.getDescricao());
        categoria.setTipo(dto.getTipo());
        categoria.setCor(dto.getCor());

        Categoria categoriaAtualizada = categoriaRepository.save(categoria);
        return CategoriaDTO.fromEntity(categoriaAtualizada);
    }

    @Transactional
    public void deletar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria", id);
        }
        categoriaRepository.deleteById(id);
    }
}

