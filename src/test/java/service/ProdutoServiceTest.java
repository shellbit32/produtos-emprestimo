package service;

import model.Produto;
import org.junit.jupiter.api.BeforeEach;
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

    private Produto produto;
    private RequestProdutoDTO requestProdutoDTO;

    private static final Long ID_PRODUTO = 1L;
    private static final String NOME_ORIGINAL = "Empréstimo Pessoal";
    private static final Double TAXA_ORIGINAL = 18.0;
    private static final Integer PRAZO_ORIGINAL = 24;
    private static final String NOME_NOVO = "Empréstimo Empresarial";
    private static final Double TAXA_NOVA = 15.0;
    private static final Integer PRAZO_NOVO = 36;

    @BeforeEach
    void setUp() {
        produto = new Produto();
        produto.setId(ID_PRODUTO);
        produto.setNome(NOME_ORIGINAL);
        produto.setTaxaJurosAnual(TAXA_ORIGINAL);
        produto.setPrazoMaximoMeses(PRAZO_ORIGINAL);

        requestProdutoDTO = RequestProdutoDTO.builder()
                .nome(NOME_NOVO)
                .taxaJurosAnual(TAXA_NOVA)
                .prazoMaximoMeses(PRAZO_NOVO)
                .build();
    }

    @Test
    void converterParaDTO_deveConverterCorretamente() {
        ProdutoDTO resultado = produtoService.converterParaDTO(produto);

        assertNotNull(resultado);
        assertEquals(produto.getId(), resultado.getId());
        assertEquals(produto.getNome(), resultado.getNome());
        assertEquals(produto.getTaxaJurosAnual(), resultado.getTaxaJurosAnual());
        assertEquals(produto.getPrazoMaximoMeses(), resultado.getPrazoMaximoMeses());
    }

    @Test
    void atualizarDadosDoProduto_deveAtualizarTodosOsCampos() {
        produtoService.atualizarDadosDoProduto(produto, requestProdutoDTO);

        assertEquals(NOME_NOVO, produto.getNome());
        assertEquals(TAXA_NOVA, produto.getTaxaJurosAnual());
        assertEquals(PRAZO_NOVO, produto.getPrazoMaximoMeses());
        assertEquals(ID_PRODUTO, produto.getId()); // ID não deve mudar
    }
}