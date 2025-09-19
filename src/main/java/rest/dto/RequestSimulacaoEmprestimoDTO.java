package rest.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RequestSimulacaoEmprestimoDTO {
    private Long idProduto;
    private BigDecimal valorSolicitado;
    private Integer prazoMeses;
}
