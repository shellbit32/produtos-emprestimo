package rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para solicitação de simulação de empréstimo")
public class RequestSimulacaoEmprestimoDTO {

    @Schema(description = "ID do produto financeiro", example = "1", required = true)
    private Long idProduto;

    @Schema(description = "Valor solicitado para o empréstimo", example = "10000.00", required = true)
    private BigDecimal valorSolicitado;

    @Schema(description = "Prazo em meses para pagamento", example = "12", required = true)
    private Integer prazoMeses;
}
