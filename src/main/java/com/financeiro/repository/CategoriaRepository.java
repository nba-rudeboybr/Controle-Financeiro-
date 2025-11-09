package com.financeiro.repository;

import com.financeiro.model.Categoria;
import com.financeiro.model.TipoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para acesso aos dados de Categoria.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    /**
     * Busca categoria por nome.
     */
    Optional<Categoria> findByNome(String nome);

    /**
     * Busca categorias por tipo.
     */
    List<Categoria> findByTipo(TipoTransacao tipo);

    /**
     * Verifica se existe categoria com o nome especificado.
     */
    boolean existsByNome(String nome);
}

