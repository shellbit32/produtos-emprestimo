package rest.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import repository.ProdutoRepository;
import rest.dto.RequestSimulacaoEmprestimoDTO;
import rest.dto.ResponseSimulacaoEmprestimoDTO;
import rest.dto.ProdutoDTO;
import rest.dto.ParcelaSimulacaoDTO;
import service.ProdutoService;
import service.SimulacaoService;
import rest.util.ResponseUtil;
import model.Produto;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.math.BigDecimal;
import java.util.List;

@Path("/simulacoes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Simulação de Empréstimo", description = "Operações para simulação de empréstimos")
public class SimulacaoResource {

    private ProdutoRepository produtoRepository;
    private SimulacaoService simulacaoService;

    @Inject
    public SimulacaoResource(ProdutoRepository produtoRepository,
                             SimulacaoService simulacaoService,
                             ProdutoService produtoService){
        this.produtoRepository = produtoRepository;
        this.simulacaoService = simulacaoService;
    }

    @POST
    @Operation(
        summary = "Simular empréstimo",
        description = "Realiza a simulação de um empréstimo baseado no produto selecionado, valor solicitado e prazo desejado. " +
                     "Retorna informações detalhadas incluindo parcela mensal, valor total com juros e memória de cálculo."
    )
    @APIResponses(value = {
        @APIResponse(
            responseCode = "200",
            description = "Simulação realizada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ResponseSimulacaoEmprestimoDTO.class)
            )
        ),
        @APIResponse(
            responseCode = "400",
            // Por enquanto só isso retorna 400
            description = "Prazo solicitado excede o máximo permitido para este produto"
        ),
        @APIResponse(
            responseCode = "404",
            description = "Produto não encontrado"
        )
    })
    public Response simularEmprestimo(
            @Valid @RequestBody(description = "Dados da simulação de empréstimo", required = true)
            RequestSimulacaoEmprestimoDTO request,
            @Context UriInfo uriInfo){
        // Buscar o produto pelo ID
        Produto produto = produtoRepository.findById(request.getIdProduto());
        if (produto == null) {
            return ResponseUtil.produtoNaoEncontrado(uriInfo);
        }

        // Validar se o prazo não excede o máximo do produto
        if (request.getPrazoMeses() > produto.getPrazoMaximoMeses()) {
            return ResponseUtil.criarRespostaErro(
                Response.Status.BAD_REQUEST,
                "Prazo solicitado excede o máximo permitido para este produto",
                uriInfo
            );
        }

        // Calcular taxa de juros efetiva mensal
        BigDecimal taxaJurosEfetivaMensal = simulacaoService.calcularTaxaJurosEfetivaMensal(produto.getTaxaJurosAnual());

        // Calcular parcela mensal
        BigDecimal parcelaMensal = simulacaoService.calcularParcelaMensal(
                request.getValorSolicitado(),
                taxaJurosEfetivaMensal,
                request.getPrazoMeses()
        );

        // Calcular valor total com juros
        BigDecimal valorTotalComJuros = simulacaoService.calcularValorTotalComJuros(
                parcelaMensal,
                request.getPrazoMeses()
        );

        // Gerar memória de cálculo (tabela de amortização)
        List<ParcelaSimulacaoDTO> memoriaCalculo = simulacaoService.gerarMemoriaCalculo(
                request.getValorSolicitado(),
                parcelaMensal,
                taxaJurosEfetivaMensal,
                request.getPrazoMeses()
        );

        // Converter Produto para ProdutoDTO
        ProdutoDTO produtoDTO = ProdutoDTO.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .taxaJurosAnual(produto.getTaxaJurosAnual())
                .prazoMaximoMeses(produto.getPrazoMaximoMeses())
                .build();

        // Montar response
        ResponseSimulacaoEmprestimoDTO response = ResponseSimulacaoEmprestimoDTO.builder()
                .produto(produtoDTO)
                .valorSolicitado(request.getValorSolicitado())
                .prazoMeses(request.getPrazoMeses())
                .taxaJurosEfetivaMensal(taxaJurosEfetivaMensal)
                .valorTotalComJuros(valorTotalComJuros)
                .parcelaMensal(parcelaMensal)
                .memoriaCalculo(memoriaCalculo)
                .build();

        return Response.ok(response).build();
    }
}
