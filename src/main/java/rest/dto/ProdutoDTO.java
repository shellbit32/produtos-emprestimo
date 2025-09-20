package rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados do produto financeiro")
public class ProdutoDTO {

    @Schema(description = "ID único do produto", example = "1")
    private Long id;

    @Schema(description = "Nome do produto financeiro", example = "Empréstimo Pessoal")
    private String nome;

    @Schema(description = "Taxa de juros anual do produto (em percentual)", example = "18.0")
    private Double taxaJurosAnual;

    @Schema(description = "Prazo máximo em meses para pagamento", example = "24")
    private Integer prazoMaximoMeses;
}
