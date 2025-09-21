package rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para solicitação de simulação de empréstimo")
public class RequestSimulacaoEmprestimoDTO {

    @NotNull(message = "ID do produto é obrigatório")
    @Positive(message = "ID do produto deve ser positivo")
    @Schema(description = "ID do produto financeiro", example = "1", required = true)
    private Long idProduto;

    @NotNull(message = "Valor solicitado é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor solicitado deve ser maior que zero")
    @Schema(description = "Valor solicitado para o empréstimo", example = "10000.00", required = true)
    private BigDecimal valorSolicitado;

    @NotNull(message = "Prazo em meses é obrigatório")
    @Min(value = 1, message = "Prazo deve ser de pelo menos 1 mês")
    @Schema(description = "Prazo em meses para pagamento", example = "12", required = true)
    private Integer prazoMeses;
}
