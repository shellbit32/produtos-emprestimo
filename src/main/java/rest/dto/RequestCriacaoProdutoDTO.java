package rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestCriacaoProdutoDTO {
    private String nome;
    private Double taxaJurosAnual;
    private Integer prazoMaximoMeses;
}
