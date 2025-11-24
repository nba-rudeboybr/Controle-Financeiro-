package com.financeiro.repository;

import com.financeiro.model.TipoTransacao;
import com.financeiro.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository para acesso aos dados de Transação.
 * Utiliza Spring Data JPA para abstração do acesso ao banco.
 */
@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    /**
     * Busca transações por tipo.
     */
    List<Transacao> findByTipo(TipoTransacao tipo);

    /**
     * Busca transações por período.
     */
    List<Transacao> findByDataBetween(LocalDate dataInicio, LocalDate dataFim);

    /**
     * Busca transações por categoria.
     */
    List<Transacao> findByCategoriaId(Long categoriaId);

    /**
     * Calcula total de receitas em um período.
     */
    @Query("SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t WHERE t.tipo = 'RECEITA' AND t.data BETWEEN :dataInicio AND :dataFim")
    BigDecimal calcularTotalReceitas(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    /**
     * Calcula total de despesas em um período.
     */
    @Query("SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t WHERE t.tipo = 'DESPESA' AND t.data BETWEEN :dataInicio AND :dataFim")
    BigDecimal calcularTotalDespesas(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    /**
     * Busca transações por descrição.
     */
    List<Transacao> findByDescricaoContainingIgnoreCase(String descricao);
}

