package com.financeiro.dto;

import com.financeiro.model.TipoTransacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para requisições de criação/atualização de Transação.
 * Implementa validações de entrada usando Bean Validation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para criação ou atualização de uma transação")
public class TransacaoRequestDTO {

    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 3, max = 200, message = "A descrição deve ter entre 3 e 200 caracteres")
    @Schema(description = "Descrição da transação", example = "Almoço no restaurante")
    private String descricao;

    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    @Digits(integer = 8, fraction = 2, message = "Valor inválido (máximo: 99999999.99)")
    @Schema(description = "Valor da transação", example = "150.50")
    private BigDecimal valor;

    @NotNull(message = "O tipo da transação é obrigatório")
    @Schema(description = "Tipo da transação (RECEITA ou DESPESA)", example = "DESPESA")
    private TipoTransacao tipo;

    @NotNull(message = "A data é obrigatória")
    @PastOrPresent(message = "A data não pode ser futura")
    @Schema(description = "Data da transação", example = "2025-10-28")
    private LocalDate data;

    @Schema(description = "ID da categoria da transação", example = "1")
    private Long categoriaId;

    @Size(max = 1000, message = "As observações devem ter no máximo 1000 caracteres")
    @Schema(description = "Observações adicionais", example = "Pagamento via cartão de crédito")
    private String observacoes;
}

