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
import rest.dto.ProdutoDTO;
import rest.dto.RequestProdutoDTO;
import service.ProdutoService;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoResourceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private UriInfo uriInfo;

    @InjectMocks
    private ProdutoResource produtoResource;

    private Produto produto;
    private ProdutoDTO produtoDTO;
    private RequestProdutoDTO requestProdutoDTO;

    private static final Long ID_EXISTENTE = 1L;
    private static final Long ID_INEXISTENTE = 999L;
    private static final String URI_PATH = "/produtos/";

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

        requestProdutoDTO = RequestProdutoDTO.builder()
                .nome("Empréstimo Empresarial")
                .taxaJurosAnual(12.5)
                .prazoMaximoMeses(36)
                .build();
    }

    @Test
    void listarProdutos_deveRetornarListaDeProdutos() {
        when(produtoRepository.listAll()).thenReturn(Arrays.asList(produto));
        when(produtoService.converterParaDTO(any())).thenReturn(produtoDTO);

        Response response = produtoResource.listarProdutos();

        assertEquals(200, response.getStatus());
        assertNotNull(response.getEntity());
        verify(produtoRepository).listAll();
        verify(produtoService).converterParaDTO(produto);
    }

    @Test
    void detalharProduto_deveRetornarProduto() {
        when(produtoRepository.findById(ID_EXISTENTE)).thenReturn(produto);
        when(produtoService.converterParaDTO(produto)).thenReturn(produtoDTO);

        Response response = produtoResource.detalharProduto(ID_EXISTENTE, uriInfo);

        assertEquals(200, response.getStatus());
        ProdutoDTO responseBody = (ProdutoDTO) response.getEntity();
        assertEquals(produtoDTO.getId(), responseBody.getId());
        assertEquals(produtoDTO.getNome(), responseBody.getNome());
    }

    @Test
    void detalharProduto_NotFound() {
        when(produtoRepository.findById(ID_INEXISTENTE)).thenReturn(null);
        when(uriInfo.getPath()).thenReturn(URI_PATH + ID_INEXISTENTE);

        Response response = produtoResource.detalharProduto(ID_INEXISTENTE, uriInfo);

        assertEquals(404, response.getStatus());
    }

    @Test
    void criarProduto_deveCriarERetornarProduto() {
        /*
        No response, é retornada uma entity resultado de ProdutoService::converterParaDTO, contendo
        o que foi criado.
        Para verificar se o que foi criado bate com o que foi enviado, basta comparar os dados retornados
        pelo ProdutoService::converterParaDTO com o que foi enviado no ProdutoResource::criarProduto.
         */
        ProdutoDTO produtoCriadoDTO = ProdutoDTO.builder()
                .id(ID_EXISTENTE)
                .nome(requestProdutoDTO.getNome())
                .taxaJurosAnual(requestProdutoDTO.getTaxaJurosAnual())
                .prazoMaximoMeses(requestProdutoDTO.getPrazoMaximoMeses())
                .build();
        when(produtoService.converterParaDTO(any())).thenReturn(produtoCriadoDTO);

        Response response = produtoResource.criarProduto(requestProdutoDTO);
        ProdutoDTO responseBody = (ProdutoDTO) response.getEntity();

        assertEquals(201, response.getStatus());
        assertEquals(requestProdutoDTO.getNome(), responseBody.getNome());
        assertEquals(requestProdutoDTO.getTaxaJurosAnual(), responseBody.getTaxaJurosAnual());
        assertEquals(requestProdutoDTO.getPrazoMaximoMeses(), responseBody.getPrazoMaximoMeses());
    }

    @Test
    void atualizarProduto_deveAtualizarProduto() {
        when(produtoRepository.findById(ID_EXISTENTE)).thenReturn(produto);

        Response response = produtoResource.atualizarProduto(ID_EXISTENTE, requestProdutoDTO, uriInfo);

        assertEquals(204, response.getStatus());
        verify(produtoService).atualizarDadosDoProduto(produto, requestProdutoDTO);
    }

    @Test
    void atualizarProduto_NotFound() {
        when(produtoRepository.findById(ID_INEXISTENTE)).thenReturn(null);
        when(uriInfo.getPath()).thenReturn(URI_PATH + ID_INEXISTENTE);

        Response response = produtoResource.atualizarProduto(ID_INEXISTENTE, requestProdutoDTO, uriInfo);

        assertEquals(404, response.getStatus());
    }

    @Test
    void deletarProduto() {
        when(produtoRepository.findById(ID_EXISTENTE)).thenReturn(produto);

        Response response = produtoResource.deletarProduto(ID_EXISTENTE, uriInfo);

        assertEquals(204, response.getStatus());
        verify(produtoRepository).delete(produto);
    }

    @Test
    void deletarProduto_NotFound() {
        when(produtoRepository.findById(ID_INEXISTENTE)).thenReturn(null);
        when(uriInfo.getPath()).thenReturn(URI_PATH + ID_INEXISTENTE);

        Response response = produtoResource.deletarProduto(ID_INEXISTENTE, uriInfo);

        assertEquals(404, response.getStatus());
        verify(produtoRepository, never()).delete(any());
    }
}