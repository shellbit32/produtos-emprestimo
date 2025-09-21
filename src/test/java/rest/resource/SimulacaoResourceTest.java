package rest.resource;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.ProdutoRepository;
import rest.dto.ParcelaSimulacaoDTO;
import rest.dto.ProdutoDTO;
import rest.dto.RequestSimulacaoEmprestimoDTO;
import rest.dto.ResponseSimulacaoEmprestimoDTO;
import service.ProdutoService;
import service.SimulacaoService;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimulacaoResourceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private SimulacaoService simulacaoService;

    @Mock
    private UriInfo uriInfo;

    @InjectMocks
    private SimulacaoResource simulacaoResource;

    private Produto produto;
    private ProdutoDTO produtoDTO;
    private RequestSimulacaoEmprestimoDTO requestSimulacao;
    private ParcelaSimulacaoDTO parcelaSimulacao;

    private static final Long ID_EXISTENTE = 1L;
    private static final Long ID_INEXISTENTE = 999L;
    private static final String URI_PATH = "/simulacoes";

    @BeforeEach
    void setUp() {
        produto = new Produto();
        produto.setId(ID_EXISTENTE);
        produto.setNome("Empréstimo Pessoal");
        produto.setTaxaJurosAnual(18.0);
        produto.setPrazoMaximoMeses(24);

        produtoDTO = ProdutoDTO.builder()
                .id(ID_EXISTENTE)
                .nome("Empréstimo Pessoal")
                .taxaJurosAnual(18.0)
                .prazoMaximoMeses(24)
                .build();

        requestSimulacao = RequestSimulacaoEmprestimoDTO.builder()
                .idProduto(ID_EXISTENTE)
                .valorSolicitado(new BigDecimal("5000.00"))
                .prazoMeses(10)
                .build();

        parcelaSimulacao = ParcelaSimulacaoDTO.builder()
                .mes(1)
                .saldoDevedorInicial(new BigDecimal("5000.00"))
                .juros(new BigDecimal("100.00"))
                .amortizacao(new BigDecimal("400.00"))
                .saldoDevedorFinal(new BigDecimal("4600.00"))
                .build();
    }

    @Test
    void simularEmprestimo_deveRetornarSimulacao() {
        when(produtoRepository.findById(ID_EXISTENTE)).thenReturn(produto);
        when(simulacaoService.calcularTaxaJurosEfetivaMensal(produto.getTaxaJurosAnual())).thenReturn(new BigDecimal("0.02"));
        when(simulacaoService.calcularParcelaMensal(any(), any(), any())).thenReturn(new BigDecimal("500.00"));
        when(simulacaoService.calcularValorTotalComJuros(any(), any())).thenReturn(new BigDecimal("5000.00"));
        when(simulacaoService.gerarMemoriaCalculo(any(), any(), any(), any())).thenReturn(Arrays.asList(parcelaSimulacao));

        Response response = simulacaoResource.simularEmprestimo(requestSimulacao, uriInfo);

        assertEquals(200, response.getStatus());
        ResponseSimulacaoEmprestimoDTO responseBody = (ResponseSimulacaoEmprestimoDTO) response.getEntity();
        assertNotNull(responseBody);
        assertEquals(requestSimulacao.getValorSolicitado(), responseBody.getValorSolicitado());
        assertEquals(requestSimulacao.getPrazoMeses(), responseBody.getPrazoMeses());
    }

    @Test
    void simularEmprestimo_produtoNotFound() {
        when(produtoRepository.findById(ID_INEXISTENTE)).thenReturn(null);
        when(uriInfo.getPath()).thenReturn(URI_PATH);

        RequestSimulacaoEmprestimoDTO requestInexistente = RequestSimulacaoEmprestimoDTO.builder()
                .idProduto(ID_INEXISTENTE)
                .valorSolicitado(new BigDecimal("5000.00"))
                .prazoMeses(10)
                .build();

        Response response = simulacaoResource.simularEmprestimo(requestInexistente, uriInfo);

        assertEquals(404, response.getStatus());
    }

    @Test
    void simularEmprestimo_prazoExcedido() {
        when(produtoRepository.findById(ID_EXISTENTE)).thenReturn(produto);
        when(uriInfo.getPath()).thenReturn(URI_PATH);

        RequestSimulacaoEmprestimoDTO requestPrazoExcedido = RequestSimulacaoEmprestimoDTO.builder()
                .idProduto(ID_EXISTENTE)
                .valorSolicitado(new BigDecimal("5000.00"))
                .prazoMeses(produto.getPrazoMaximoMeses() + 1) // Maior que prazoMaximoMeses
                .build();

        Response response = simulacaoResource.simularEmprestimo(requestPrazoExcedido, uriInfo);

        assertEquals(400, response.getStatus());
    }
}