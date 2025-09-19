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
@Schema(description = "Detalhes de uma parcela na memória de cálculo")
public class ParcelaSimulacaoDTO {

    @Schema(description = "Número do mês da parcela", example = "1")
    private Integer mes;

    @Schema(description = "Saldo devedor no início do mês", example = "10000.00")
    private BigDecimal saldoDevedorInicial;

    @Schema(description = "Valor dos juros da parcela", example = "150.00")
    private BigDecimal juros;

    @Schema(description = "Valor da amortização da parcela", example = "846.35")
    private BigDecimal amortizacao;

    @Schema(description = "Saldo devedor no final do mês", example = "9153.65")
    private BigDecimal saldoDevedorFinal;
}
