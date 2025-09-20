package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import rest.dto.ParcelaSimulacaoDTO;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SimulacaoServiceTest {

    @InjectMocks
    private SimulacaoService simulacaoService;

    @Test
    void calcularTaxaJurosEfetivaMensal() {
        BigDecimal resultado = simulacaoService.calcularTaxaJurosEfetivaMensal(12.0);
        assertNotNull(resultado);
    }

    @Test
    void calcularParcelaMensal() {
        BigDecimal resultado = simulacaoService.calcularParcelaMensal(
                new BigDecimal("1000"), new BigDecimal("0.01"), 10);
        assertNotNull(resultado);
    }

    @Test
    void calcularValorTotalComJuros() {
        BigDecimal resultado = simulacaoService.calcularValorTotalComJuros(
                new BigDecimal("100"), 10);
        assertEquals(new BigDecimal("1000"), resultado);
    }

    @Test
    void gerarMemoriaCalculo() {
        List<ParcelaSimulacaoDTO> resultado = simulacaoService.gerarMemoriaCalculo(
                new BigDecimal("1000"), new BigDecimal("100"), new BigDecimal("0.01"), 3);
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
    }
}