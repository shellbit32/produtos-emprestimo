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
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import rest.dto.RequestProdutoDTO;
import service.ProdutoService;

import java.util.List;

@Path("/produtos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Produtos Financeiros", description = "Operações relacionadas a produtos financeiros")
public class ProdutoResource {

    private ProdutoRepository produtoRepository;
    private ProdutoService produtoService;

    @Inject
    public ProdutoResource(ProdutoRepository produtoRepository, ProdutoService produtoService) {
        this.produtoRepository = produtoRepository;
        this.produtoService = produtoService;
    }

    @GET
    @Operation(
        summary = "Listar produtos disponíveis",
        description = "Retorna a lista de todos os produtos financeiros disponíveis para simulação de empréstimo"
    )
    @APIResponse(
        responseCode = "200",
        description = "Lista de produtos retornada com sucesso",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(type = SchemaType.ARRAY, implementation = ProdutoDTO.class)
        )
    )
    public Response listarProdutos() {
        List<Produto> produtos = produtoRepository.listAll();
        List<ProdutoDTO> produtosDTO = produtos.stream()
            .map(produtoService::converterParaDTO)
            .toList();

        return Response.ok(produtosDTO).build();
    }

    @GET
    @Path("{id}")
    @Operation(
            summary = "Listar dados de produto por ID",
            description = "Retorna dados de produto financeiro disponível para simulação de empréstimo"
    )
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
            description = "Dados do produto retornados com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProdutoDTO.class)
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Produto não encontrado"
        )
    })
    public Response detalharProduto(
            @Parameter(description = "ID do produto", example = "1", required = true)
            @PathParam("id") Long idProduto) {
        Produto produto = produtoRepository.findById(idProduto);
        if (produto == null) {
            return criarRespostaProdutoNaoEncontrado();
        }

        ProdutoDTO produtoDto = produtoService.converterParaDTO(produto);
        return Response.ok(produtoDto).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    @Operation(
            summary = "Deletar produto por ID",
            description = "Remove um produto financeiro do sistema"
    )
    @APIResponses(value = {
        @APIResponse(
            responseCode = "204",
            description = "Produto deletado com sucesso"
        ),
        @APIResponse(
            responseCode = "404",
            description = "Produto não encontrado"
        )
    })
    public Response deletarProduto(
            @Parameter(description = "ID do produto", example = "1", required = true)
            @PathParam("id") Long idProduto) {
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
    @APIResponses(value = {
        @APIResponse(
            responseCode = "204",
            description = "Produto atualizado com sucesso"
        ),
        @APIResponse(
            responseCode = "404",
            description = "Produto não encontrado"
        )
    })
    public Response atualizarProduto(
            @Parameter(description = "ID do produto", example = "1", required = true)
            @PathParam("id") Long idProduto,
            @RequestBody(description = "Dados do produto a ser atualizado", required = true)
            RequestProdutoDTO requestProdutoDTO) {
        Produto produto = produtoRepository.findById(idProduto);
        if (produto == null){
            return criarRespostaProdutoNaoEncontrado();
        }

        produtoService.atualizarDadosDoProduto(produto, requestProdutoDTO);
        return Response.noContent().build();
    }

    @POST
    @Transactional
    @Operation(
            summary = "Criar novo produto",
            description = "Cria um novo produto financeiro no sistema e retorna os dados do produto criado"
    )
    @APIResponse(
        responseCode = "201",
        description = "Produto criado com sucesso",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ProdutoDTO.class)
        )
    )
    public Response criarProduto(
            @RequestBody(description = "Dados do produto a ser criado", required = true)
            RequestProdutoDTO requestProdutoDTO){
        Produto produto = new Produto();
        produtoService.atualizarDadosDoProduto(produto, requestProdutoDTO);
        produtoRepository.persist(produto);
        return Response.status(Response.Status.CREATED).entity(produtoService.converterParaDTO(produto)).build();
    }

    private Response criarRespostaProdutoNaoEncontrado() {
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Produto não encontrado")
                .build();
    }

}
