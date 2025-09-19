package rest.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import repository.ProdutoRepository;
import rest.dto.RequestSimulacaoEmprestimoDTO;

@Path("/simulacoes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SimulacaoResource {

    private ProdutoRepository produtoRepository;

    @Inject
    public SimulacaoResource(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }

    @GET
    public Response simularEmprestimo(RequestSimulacaoEmprestimoDTO request){
        /*
        Realizar cálculo de empréstimo e retornar um ResponseSimulacaoEmprestimoDTO já montado
         */
        return Response.ok(request).build();
    }
}
