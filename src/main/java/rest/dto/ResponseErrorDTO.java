package rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta para erros da API")
public class ResponseErrorDTO {

    @Schema(description = "Código de status HTTP", example = "404")
    private int status;

    @Schema(description = "Mensagem de erro", example = "Produto não encontrado")
    private String message;

    @Schema(description = "Timestamp do erro")
    private LocalDateTime timestamp;

    @Schema(description = "Caminho da requisição que gerou o erro", example = "/produtos/999")
    private String path;
}