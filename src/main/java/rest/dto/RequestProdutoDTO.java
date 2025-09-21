package rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para criação ou atualização de produto financeiro")
public class RequestProdutoDTO {

    @NotBlank(message = "Nome do produto é obrigatório")
    @Schema(description = "Nome do produto financeiro", example = "Empréstimo Pessoal", required = true)
    private String nome;

    @NotNull(message = "Taxa de juros anual é obrigatória")
    @Positive(message = "Taxa de juros anual deve ser positiva")
    @Schema(description = "Taxa de juros anual do produto (em percentual)", example = "18.0", required = true)
    private Double taxaJurosAnual;

    @NotNull(message = "Prazo máximo em meses é obrigatório")
    @Min(value = 1, message = "Prazo máximo deve ser de pelo menos 1 mês")
    @Schema(description = "Prazo máximo em meses para pagamento", example = "24", required = true)
    private Integer prazoMaximoMeses;
}
