package rest.resource;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import repository.ProdutoRepository;
import rest.dto.ProdutoDTO;
import model.Produto;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import rest.dto.RequestCriacaoProdutoDTO;

import java.util.List;

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
            .toList();

        return Response.ok(produtosDTO).build();
    }

    @GET
    @Path("{id}")
    @Operation(
            summary = "Listar dados de produto por ID",
            description = "Retorna dados de produto financeiro disponível para simulação de empréstimo"
    )
    public Response detalharProduto(@PathParam("id") Long idProduto) {
        Produto produto = produtoRepository.findById(idProduto);

        if (produto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Produto não encontrado")
                    .build();
        }

        ProdutoDTO produtoDto = ProdutoDTO.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .taxaJurosAnual(produto.getTaxaJurosAnual())
                .prazoMaximoMeses(produto.getPrazoMaximoMeses())
                .build();

        return Response.ok(produtoDto).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deletarProduto(@PathParam("id") Long idProduto) {
        Produto produto = produtoRepository.findById(idProduto);

        if (produto == null){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Produto não encontrado")
                    .build();
        }

        produtoRepository.delete(produto);

        return Response.noContent().build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    // Look again at the name for RequestCriacaoProdutoDTO since it's not used just for creation anymore
    public Response atualizarProduto(@PathParam("id") Long idProduto, RequestCriacaoProdutoDTO requestCriacaoProdutoDTO) {
        Produto produto = produtoRepository.findById(idProduto);

        if (produto == null){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Produto não encontrado")
                    .build();
        }

        produto.setNome(requestCriacaoProdutoDTO.getNome());
        produto.setTaxaJurosAnual(requestCriacaoProdutoDTO.getTaxaJurosAnual());
        produto.setPrazoMaximoMeses(requestCriacaoProdutoDTO.getPrazoMaximoMeses());

        return Response.noContent().build();
    }

    @POST
    @Transactional
    public Response criarProduto(RequestCriacaoProdutoDTO requestCriacaoProdutoDTO){

        /*
            Use some constraint violation here. Using jakarta.validation.Validator
            and jakarta.validation.ConstraintViolation ; if there is a violation,
            create some DTO for a response error. All of that IF that is good practice
         */

        Produto produto = new Produto();
        produto.setNome(requestCriacaoProdutoDTO.getNome());
        produto.setTaxaJurosAnual(requestCriacaoProdutoDTO.getTaxaJurosAnual());
        produto.setPrazoMaximoMeses(requestCriacaoProdutoDTO.getPrazoMaximoMeses());

        return Response.status(Response.Status.CREATED).entity(produto).build();
    }
}
