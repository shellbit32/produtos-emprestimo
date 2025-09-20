package service;

import jakarta.enterprise.context.ApplicationScoped;
import model.Produto;
import rest.dto.ProdutoDTO;
import rest.dto.RequestProdutoDTO;

@ApplicationScoped
public class ProdutoService {

    public ProdutoDTO converterParaDTO(Produto produto) {
        return ProdutoDTO.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .taxaJurosAnual(produto.getTaxaJurosAnual())
                .prazoMaximoMeses(produto.getPrazoMaximoMeses())
                .build();
    }

    public void atualizarDadosDoProduto(Produto produto, RequestProdutoDTO produtoDTO) {
        produto.setNome(produtoDTO.getNome());
        produto.setTaxaJurosAnual(produtoDTO.getTaxaJurosAnual());
        produto.setPrazoMaximoMeses(produtoDTO.getPrazoMaximoMeses());
    }
}
