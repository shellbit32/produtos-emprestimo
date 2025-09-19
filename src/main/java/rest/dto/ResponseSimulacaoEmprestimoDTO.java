package rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resultado da simulação de empréstimo")
public class ResponseSimulacaoEmprestimoDTO {

    @Schema(description = "Dados do produto financeiro utilizado na simulação")
    private ProdutoDTO produto;

    @Schema(description = "Valor solicitado para o empréstimo", example = "10000.00")
    private BigDecimal valorSolicitado;

    @Schema(description = "Prazo em meses para pagamento", example = "12")
    private Integer prazoMeses;

    @Schema(description = "Taxa de juros efetiva mensal aplicada", example = "0.015")
    private BigDecimal taxaJurosEfetivaMensal;

    @Schema(description = "Valor total a ser pago incluindo juros", example = "11956.18")
    private BigDecimal valorTotalComJuros;

    @Schema(description = "Valor da parcela mensal", example = "996.35")
    private BigDecimal parcelaMensal;

    @Schema(description = "Memória de cálculo detalhada")
    private List<ParcelaSimulacaoDTO> memoriaCalculo;
}
