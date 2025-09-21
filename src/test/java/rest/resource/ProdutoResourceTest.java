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

    private Produto produto = new Produto();
    private ProdutoDTO produtoDTO = new ProdutoDTO();
    private RequestProdutoDTO requestProdutoDTO = new RequestProdutoDTO();

    @Test
    void listarProdutos() {
        when(produtoRepository.listAll()).thenReturn(Arrays.asList(produto));
        when(produtoService.converterParaDTO(any())).thenReturn(produtoDTO);

        Response response = produtoResource.listarProdutos();

        assertEquals(200, response.getStatus());
    }

    @Test
    void detalharProduto() {
        when(produtoRepository.findById(1L)).thenReturn(produto);
        when(produtoService.converterParaDTO(produto)).thenReturn(produtoDTO);

        Response response = produtoResource.detalharProduto(1L, uriInfo);

        assertEquals(200, response.getStatus());
    }

    @Test
    void detalharProduto_NotFound() {
        when(produtoRepository.findById(1L)).thenReturn(null);

        Response response = produtoResource.detalharProduto(1L, uriInfo);

        assertEquals(404, response.getStatus());
    }

    @Test
    void criarProduto() {
        when(produtoService.converterParaDTO(any())).thenReturn(produtoDTO);

        Response response = produtoResource.criarProduto(requestProdutoDTO);

        assertEquals(201, response.getStatus());
    }

    @Test
    void atualizarProduto() {
        when(produtoRepository.findById(1L)).thenReturn(produto);

        Response response = produtoResource.atualizarProduto(1L, requestProdutoDTO, uriInfo);

        assertEquals(204, response.getStatus());
    }

    @Test
    void atualizarProduto_NotFound() {
        when(produtoRepository.findById(1L)).thenReturn(null);

        Response response = produtoResource.atualizarProduto(1L, requestProdutoDTO, uriInfo);

        assertEquals(404, response.getStatus());
    }

    @Test
    void deletarProduto() {
        when(produtoRepository.findById(1L)).thenReturn(produto);

        Response response = produtoResource.deletarProduto(1L, uriInfo);

        assertEquals(204, response.getStatus());
    }

    @Test
    void deletarProduto_NotFound() {
        when(produtoRepository.findById(1L)).thenReturn(null);

        Response response = produtoResource.deletarProduto(1L, uriInfo);

        assertEquals(404, response.getStatus());
    }
}