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
import rest.dto.ResponseSimulacaoEmprestimoDTO;
import rest.dto.ProdutoDTO;
import rest.dto.ParcelaSimulacaoDTO;
import service.SimulacaoService;
import model.Produto;

import java.math.BigDecimal;
import java.util.List;

@Path("/simulacoes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SimulacaoResource {

    private ProdutoRepository produtoRepository;
    private SimulacaoService simulacaoService;

    @Inject
    public SimulacaoResource(ProdutoRepository produtoRepository, SimulacaoService simulacaoService){
        this.produtoRepository = produtoRepository;
        this.simulacaoService = simulacaoService;
    }

    @GET
    public Response simularEmprestimo(RequestSimulacaoEmprestimoDTO request){
        // Buscar o produto pelo ID
        Produto produto = produtoRepository.findById(request.getIdProduto());
        if (produto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Produto não encontrado")
                    .build();
        }

        // Validar se o prazo não excede o máximo do produto
        if (request.getPrazoMeses() > produto.getPrazoMaximoMeses()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Prazo solicitado excede o máximo permitido para este produto")
                    .build();
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
