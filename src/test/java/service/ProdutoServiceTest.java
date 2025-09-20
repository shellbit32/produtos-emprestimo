package service;

import model.Produto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import rest.dto.ProdutoDTO;
import rest.dto.RequestProdutoDTO;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto = new Produto();
    private RequestProdutoDTO requestProdutoDTO = new RequestProdutoDTO();

    @Test
    void converterParaDTO() {
        ProdutoDTO resultado = produtoService.converterParaDTO(produto);
        assertNotNull(resultado);
    }

    @Test
    void atualizarDadosDoProduto() {
        produtoService.atualizarDadosDoProduto(produto, requestProdutoDTO);
    }
}