package rest.dto;

import lombok.Data;

@Data
public class ProdutoDTO {
    private Long id;
    private String nome;
    private Double taxaJurosAnual;
    private Integer prazoMaximoMeses;
}
