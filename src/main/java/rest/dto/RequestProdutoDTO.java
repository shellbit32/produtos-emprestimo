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
@Schema(description = "Dados para criação ou atualização de produto financeiro")
public class RequestProdutoDTO {

    @Schema(description = "Nome do produto financeiro", example = "Empréstimo Pessoal", required = true)
    private String nome;

    @Schema(description = "Taxa de juros anual do produto (em decimal)", example = "0.18", required = true)
    private Double taxaJurosAnual;

    @Schema(description = "Prazo máximo em meses para pagamento", example = "24", required = true)
    private Integer prazoMaximoMeses;
}
