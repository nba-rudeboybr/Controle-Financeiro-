package com.financeiro.model;

/**
 * Enum representando os tipos de transação financeira.
 * Demonstra conceito de abstração e tipo enumerado.
 */
public enum TipoTransacao {
    RECEITA("Receita"),
    DESPESA("Despesa");

    private final String descricao;

    TipoTransacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

