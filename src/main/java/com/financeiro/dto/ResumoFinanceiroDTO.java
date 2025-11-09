package com.financeiro.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para resumo financeiro com estatísticas.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resumo financeiro com estatísticas")
public class ResumoFinanceiroDTO {

    @Schema(description = "Total de receitas", example = "5000.00")
    private BigDecimal totalReceitas;

    @Schema(description = "Total de despesas", example = "3200.50")
    private BigDecimal totalDespesas;

    @Schema(description = "Saldo (receitas - despesas)", example = "1799.50")
    private BigDecimal saldo;

    @Schema(description = "Quantidade de transações", example = "25")
    private Long quantidadeTransacoes;

    @Schema(description = "Data inicial do período", example = "2025-10-01")
    private LocalDate dataInicio;

    @Schema(description = "Data final do período", example = "2025-10-31")
    private LocalDate dataFim;
}

