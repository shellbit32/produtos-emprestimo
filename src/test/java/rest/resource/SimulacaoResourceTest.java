package rest.resource;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import model.Produto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.ProdutoRepository;
import rest.dto.ParcelaSimulacaoDTO;
import rest.dto.RequestSimulacaoEmprestimoDTO;
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

    private Produto produto = new Produto();
    private RequestSimulacaoEmprestimoDTO requestSimulacao = RequestSimulacaoEmprestimoDTO.builder()
            .idProduto(1L).valorSolicitado(new BigDecimal("1000")).prazoMeses(10).build();
    private ParcelaSimulacaoDTO parcelaSimulacao = new ParcelaSimulacaoDTO();

    @Test
    void simularEmprestimo() {
        produto.setPrazoMaximoMeses(24);
        when(produtoRepository.findById(any())).thenReturn(produto);
        when(simulacaoService.calcularTaxaJurosEfetivaMensal(any())).thenReturn(new BigDecimal("0.01"));
        when(simulacaoService.calcularParcelaMensal(any(), any(), any())).thenReturn(new BigDecimal("100"));
        when(simulacaoService.calcularValorTotalComJuros(any(), any())).thenReturn(new BigDecimal("1000"));
        when(simulacaoService.gerarMemoriaCalculo(any(), any(), any(), any())).thenReturn(Arrays.asList(parcelaSimulacao));

        Response response = simulacaoResource.simularEmprestimo(requestSimulacao, uriInfo);

        assertEquals(200, response.getStatus());
    }

    @Test
    void simularEmprestimo_ProdutoNotFound() {
        when(produtoRepository.findById(any())).thenReturn(null);

        Response response = simulacaoResource.simularEmprestimo(requestSimulacao, uriInfo);

        assertEquals(404, response.getStatus());
    }
}