package com.financeiro.dto;

import com.financeiro.model.Categoria;
import com.financeiro.model.TipoTransacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para Categoria de transações.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados de uma categoria de transações")
public class CategoriaDTO {

    @Schema(description = "ID único da categoria", example = "1")
    private Long id;

    @NotBlank(message = "O nome da categoria é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    @Schema(description = "Nome da categoria", example = "Alimentação")
    private String nome;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    @Schema(description = "Descrição da categoria", example = "Gastos com alimentação e restaurantes")
    private String descricao;

    @NotNull(message = "O tipo da categoria é obrigatório")
    @Schema(description = "Tipo da categoria (RECEITA ou DESPESA)", example = "DESPESA")
    private TipoTransacao tipo;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Cor inválida (formato: #RRGGBB)")
    @Schema(description = "Cor em hexadecimal", example = "#FF5733")
    private String cor;

    public static CategoriaDTO fromEntity(Categoria categoria) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(categoria.getId());
        dto.setNome(categoria.getNome());
        dto.setDescricao(categoria.getDescricao());
        dto.setTipo(categoria.getTipo());
        dto.setCor(categoria.getCor());
        return dto;
    }

    public Categoria toEntity() {
        Categoria categoria = new Categoria();
        categoria.setId(this.id);
        categoria.setNome(this.nome);
        categoria.setDescricao(this.descricao);
        categoria.setTipo(this.tipo);
        categoria.setCor(this.cor);
        return categoria;
    }
}

