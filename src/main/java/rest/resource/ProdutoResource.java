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
import rest.dto.RequestProdutoDTO;

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
            .map(this::converterParaDTO)
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
            return criarRespostaProdutoNaoEncontrado();
        }

        ProdutoDTO produtoDto = converterParaDTO(produto);
        return Response.ok(produtoDto).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    @Operation(
            summary = "Deletar produto por ID",
            description = "Remove um produto financeiro do sistema"
    )
    public Response deletarProduto(@PathParam("id") Long idProduto) {
        Produto produto = produtoRepository.findById(idProduto);
        if (produto == null){
            return criarRespostaProdutoNaoEncontrado();
        }

        produtoRepository.delete(produto);
        return Response.noContent().build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    @Operation(
            summary = "Atualizar produto por ID",
            description = "Atualiza os dados de um produto financeiro existente no sistema"
    )
    public Response atualizarProduto(@PathParam("id") Long idProduto, RequestProdutoDTO requestProdutoDTO) {
        Produto produto = produtoRepository.findById(idProduto);
        if (produto == null){
            return criarRespostaProdutoNaoEncontrado();
        }

        atualizarDadosDoProduto(produto, requestProdutoDTO);
        return Response.noContent().build();
    }

    @POST
    @Transactional
    @Operation(
            summary = "Criar novo produto",
            description = "Cria um novo produto financeiro no sistema e retorna os dados do produto criado"
    )
    public Response criarProduto(RequestProdutoDTO requestProdutoDTO){
        Produto produto = new Produto();
        atualizarDadosDoProduto(produto, requestProdutoDTO);
        produtoRepository.persist(produto);
        return Response.status(Response.Status.CREATED).entity(converterParaDTO(produto)).build();
    }

    private Response criarRespostaProdutoNaoEncontrado() {
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Produto não encontrado")
                .build();
    }

    private ProdutoDTO converterParaDTO(Produto produto) {
        return ProdutoDTO.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .taxaJurosAnual(produto.getTaxaJurosAnual())
                .prazoMaximoMeses(produto.getPrazoMaximoMeses())
                .build();
    }

    private void atualizarDadosDoProduto(Produto produto, RequestProdutoDTO produtoDTO) {
        produto.setNome(produtoDTO.getNome());
        produto.setTaxaJurosAnual(produtoDTO.getTaxaJurosAnual());
        produto.setPrazoMaximoMeses(produtoDTO.getPrazoMaximoMeses());
    }

}
