package rest.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ParcelaSimulacaoDTO {
    private Integer mes;
    private BigDecimal saldoDevedorInicial;
    private BigDecimal juros;
    private BigDecimal amortizacao;
    private BigDecimal saldoDevedorFinal;
}
