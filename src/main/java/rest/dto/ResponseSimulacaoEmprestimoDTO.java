package rest.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ResponseSimulacaoEmprestimoDTO {
    private ProdutoDTO produto;
    private BigDecimal valorSolicitado;
    private Integer prazoMeses;
    private BigDecimal taxaJurosEfetivaMensal;
    private BigDecimal valorTotalComJuros;
    private BigDecimal parcelaMensal;
    private List<ParcelaSimulacaoDTO> memoriaCalculo;
}
