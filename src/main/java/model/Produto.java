package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "produto")
@Data
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do produto é obrigatório")
    @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
    @Column(nullable = false, length = 150)
    private String nome;

    @NotNull(message = "Taxa de juros anual é obrigatória")
    @DecimalMin(value = "0.0", inclusive = false, message = "Taxa de juros deve ser maior que zero")
    @DecimalMax(value = "100.0", message = "Taxa de juros deve ser no máximo 100%")
    @Column(name = "taxa_juros_anual", nullable = false)
    private Double taxaJurosAnual;

    @NotNull(message = "Prazo máximo é obrigatório")
    @Min(value = 1, message = "Prazo máximo deve ser pelo menos 1 mês")
    @Max(value = 500, message = "Prazo máximo deve ser no máximo 500 meses")
    @Column(name = "prazo_maximo_meses", nullable = false)
    private Integer prazoMaximoMeses;
}
