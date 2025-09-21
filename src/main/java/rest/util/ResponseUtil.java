package rest.util;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import rest.dto.ResponseErrorDTO;

import java.time.LocalDateTime;

public class ResponseUtil {

    public static Response produtoNaoEncontrado(UriInfo uriInfo) {
        return criarRespostaErro(
            Response.Status.NOT_FOUND,
            "Produto não encontrado",
            uriInfo
        );
    }

    public static Response criarRespostaErro(Response.Status status, String mensagem, UriInfo uriInfo) {
        ResponseErrorDTO errorResponse = ResponseErrorDTO.builder()
                .status(status.getStatusCode())
                .message(mensagem)
                .timestamp(LocalDateTime.now())
                .path(uriInfo != null ? uriInfo.getPath() : "")
                .build();

        return Response.status(status)
                .entity(errorResponse)
                .build();
    }
}