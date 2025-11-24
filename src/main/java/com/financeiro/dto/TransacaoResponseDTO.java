package com.financeiro.dto;

import com.financeiro.model.TipoTransacao;
import com.financeiro.model.Transacao;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para respostas contendo informações de Transação.
 * Demonstra encapsulamento ao expor apenas dados necessários.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados de resposta de uma transação")
public class TransacaoResponseDTO {

    @Schema(description = "ID único da transação", example = "1")
    private Long id;

    @Schema(description = "Descrição da transação", example = "Almoço no restaurante")
    private String descricao;

    @Schema(description = "Valor da transação", example = "150.50")
    private BigDecimal valor;

    @Schema(description = "Tipo da transação", example = "DESPESA")
    private TipoTransacao tipo;

    @Schema(description = "Data da transação", example = "2025-10-28")
    private LocalDate data;

    @Schema(description = "Nome da categoria", example = "Alimentação")
    private String categoriaNome;

    @Schema(description = "Cor da categoria", example = "#FF5733")
    private String categoriaCor;

    @Schema(description = "Observações", example = "Pagamento via cartão de crédito")
    private String observacoes;

    @Schema(description = "Data de criação do registro")
    private LocalDateTime criadoEm;

    @Schema(description = "Data da última atualização")
    private LocalDateTime atualizadoEm;

    /**
     * Converte uma entidade Transacao para DTO.
     */
    public static TransacaoResponseDTO fromEntity(Transacao transacao) {
        TransacaoResponseDTO dto = new TransacaoResponseDTO();
        dto.setId(transacao.getId());
        dto.setDescricao(transacao.getDescricao());
        dto.setValor(transacao.getValor());
        dto.setTipo(transacao.getTipo());
        dto.setData(transacao.getData());
        dto.setObservacoes(transacao.getObservacoes());
        dto.setCriadoEm(transacao.getCriadoEm());
        dto.setAtualizadoEm(transacao.getAtualizadoEm());
        
        if (transacao.getCategoria() != null) {
            dto.setCategoriaNome(transacao.getCategoria().getNome());
            dto.setCategoriaCor(transacao.getCategoria().getCor());
        }
        
        return dto;
    }
}

