package rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParcelaSimulacaoDTO {
    private Integer mes;
    private BigDecimal saldoDevedorInicial;
    private BigDecimal juros;
    private BigDecimal amortizacao;
    private BigDecimal saldoDevedorFinal;
}
