package rest.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import repository.ProdutoRepository;
import rest.dto.ProdutoDTO;
import model.Produto;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@Path("/produtos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Produtos Financeiros", description = "Operações relacionadas a produtos financeiros")
public class ProdutoResource {

    private ProdutoRepository produtoRepository;

    @Inject
    public ProdutoResource(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @GET
    @Operation(
        summary = "Listar produtos disponíveis",
        description = "Retorna a lista de todos os produtos financeiros disponíveis para simulação de empréstimo"
    )
    public Response listarProdutos() {
        List<Produto> produtos = produtoRepository.listAll();

        List<ProdutoDTO> produtosDTO = produtos.stream()
            .map(produto -> ProdutoDTO.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .taxaJurosAnual(produto.getTaxaJurosAnual())
                .prazoMaximoMeses(produto.getPrazoMaximoMeses())
                .build())
            .collect(Collectors.toList());

        return Response.ok(produtosDTO).build();
    }
}
