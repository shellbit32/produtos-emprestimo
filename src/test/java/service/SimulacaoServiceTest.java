package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import rest.dto.ParcelaSimulacaoDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SimulacaoServiceTest {

    @InjectMocks
    private SimulacaoService simulacaoService;

    private static final BigDecimal VALOR_EMPRESTIMO = new BigDecimal("5000.00");
    private static final BigDecimal TAXA_MENSAL = new BigDecimal("0.02");
    private static final BigDecimal PARCELA_MENSAL = new BigDecimal("500.00");
    private static final Integer PRAZO_MESES = 10;
    private static final Double TAXA_ANUAL = 24.0;
    private static final Integer PRAZO_CURTO = 3;

    @Test
    void calcularTaxaJurosEfetivaMensal_deveCalcularCorretamente() {
        BigDecimal resultado = simulacaoService.calcularTaxaJurosEfetivaMensal(TAXA_ANUAL);

        assertNotNull(resultado);
        assertTrue(resultado.compareTo(BigDecimal.ZERO) > 0);
        // Taxa mensal deve ser menor que a anual
        assertTrue(resultado.compareTo(new BigDecimal(TAXA_ANUAL.toString())) < 0);
    }

    @Test
    void calcularTaxaJurosEfetivaMensal_comTaxaZero_deveRetornarZero() {
        BigDecimal resultado = simulacaoService.calcularTaxaJurosEfetivaMensal(0.0);

        assertNotNull(resultado);
        assertEquals(BigDecimal.ZERO.setScale(resultado.scale()), resultado);
    }

    @Test
    void calcularParcelaMensal_deveCalcularCorretamente() {
        BigDecimal resultado = simulacaoService.calcularParcelaMensal(
                VALOR_EMPRESTIMO, TAXA_MENSAL, PRAZO_MESES);

        assertNotNull(resultado);
        assertTrue(resultado.compareTo(BigDecimal.ZERO) > 0);
        // Parcela deve ser menor que o valor total do empréstimo
        assertTrue(resultado.compareTo(VALOR_EMPRESTIMO) < 0);
    }

    @Test
    void calcularParcelaMensal_comTaxaZero_deveRetornarValorDividido() {
        BigDecimal taxaZero = BigDecimal.ZERO;
        BigDecimal valorEsperado = VALOR_EMPRESTIMO.divide(new BigDecimal(PRAZO_MESES), 2, RoundingMode.HALF_UP);

        BigDecimal resultado = simulacaoService.calcularParcelaMensal(
                VALOR_EMPRESTIMO, taxaZero, PRAZO_MESES);

        assertNotNull(resultado);
        assertEquals(valorEsperado, resultado);
    }

    @Test
    void calcularValorTotalComJuros_deveCalcularCorretamente() {
        BigDecimal resultado = simulacaoService.calcularValorTotalComJuros(
                PARCELA_MENSAL, PRAZO_MESES);

        BigDecimal valorEsperado = PARCELA_MENSAL.multiply(new BigDecimal(PRAZO_MESES));
        assertEquals(valorEsperado, resultado);
    }

    @Test
    void calcularValorTotalComJuros_comParcelaZero_deveRetornarZero() {
        BigDecimal parcelaZero = BigDecimal.ZERO;

        BigDecimal resultado = simulacaoService.calcularValorTotalComJuros(
                parcelaZero, PRAZO_MESES);

        assertEquals(BigDecimal.ZERO.setScale(resultado.scale()), resultado);
    }

    @Test
    void gerarMemoriaCalculo_deveGerarListaCorreta() {
        List<ParcelaSimulacaoDTO> resultado = simulacaoService.gerarMemoriaCalculo(
                VALOR_EMPRESTIMO, PARCELA_MENSAL, TAXA_MENSAL, PRAZO_CURTO);

        assertNotNull(resultado);
        assertEquals(PRAZO_CURTO, resultado.size());

        // Verificar primeira parcela
        ParcelaSimulacaoDTO primeiraParcela = resultado.getFirst();
        assertEquals(1, primeiraParcela.getMes());
        assertEquals(VALOR_EMPRESTIMO, primeiraParcela.getSaldoDevedorInicial());

        // Verificar última parcela
        ParcelaSimulacaoDTO ultimaParcela = resultado.get(PRAZO_CURTO - 1);
        assertEquals(PRAZO_CURTO, ultimaParcela.getMes());
        assertEquals(BigDecimal.ZERO.setScale(ultimaParcela.getSaldoDevedorFinal().scale()),
                    ultimaParcela.getSaldoDevedorFinal());
    }

    @Test
    void gerarMemoriaCalculo_comPrazoZero_deveRetornarListaVazia() {
        List<ParcelaSimulacaoDTO> resultado = simulacaoService.gerarMemoriaCalculo(
                VALOR_EMPRESTIMO, PARCELA_MENSAL, TAXA_MENSAL, 0);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}