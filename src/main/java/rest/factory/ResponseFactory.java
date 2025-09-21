package rest.factory;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import rest.dto.ResponseErrorDTO;

import java.time.LocalDateTime;

public class ResponseFactory {

    /*
    A ideia é que ResponseFactory seja apenas uma classe que guarda métodos relacionados a criação
    de objetos. É QUASE um Singleton, mas não tem instância.
     */
    private ResponseFactory(){}

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
                .path(uriInfo.getPath())
                .build();

        return Response.status(status)
                .entity(errorResponse)
                .build();
    }
}