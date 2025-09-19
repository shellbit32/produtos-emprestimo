package rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseSimulacaoEmprestimoDTO {
    private ProdutoDTO produto;
    private BigDecimal valorSolicitado;
    private Integer prazoMeses;
    private BigDecimal taxaJurosEfetivaMensal;
    private BigDecimal valorTotalComJuros;
    private BigDecimal parcelaMensal;
    private List<ParcelaSimulacaoDTO> memoriaCalculo;
}
