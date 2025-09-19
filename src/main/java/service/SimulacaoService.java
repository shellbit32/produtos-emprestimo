package service;

import jakarta.enterprise.context.ApplicationScoped;

import rest.dto.ParcelaSimulacaoDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SimulacaoService {

    /**
     * Calcula a taxa de juros efetiva mensal usando a fórmula: i = (1 + ianual)^(1/12) - 1
     * @param taxaJurosAnual Taxa anual em percentual (ex: 18.0 para 18%)
     * @return Taxa mensal em decimal
     */
    public BigDecimal calcularTaxaJurosEfetivaMensal(Double taxaJurosAnual) {
        // Converter para decimal (18% = 0.18)
        double taxaAnualDecimal = taxaJurosAnual / 100.0;

        // Aplicar fórmula: (1 + ianual)^(1/12) - 1
        double taxaMensalDecimal = Math.pow(1.0 + taxaAnualDecimal, 1.0/12.0) - 1.0;

        return BigDecimal.valueOf(taxaMensalDecimal).setScale(6, RoundingMode.HALF_UP);
    }

    /**
     * Calcula a parcela mensal usando o Sistema Price (parcelas fixas)
     * Fórmula: PMT = PV * [i * (1 + i)^n] / [(1 + i)^n - 1]
     * @param valorPresente Valor do empréstimo
     * @param taxaMensal Taxa mensal em decimal
     * @param numParcelas Número de parcelas
     * @return Valor da parcela mensal
     */
    public BigDecimal calcularParcelaMensal(BigDecimal valorPresente, BigDecimal taxaMensal, Integer numParcelas) {
        if (taxaMensal.compareTo(BigDecimal.ZERO) == 0) {
            // Se taxa for zero, parcela = valor / número de parcelas
            return valorPresente.divide(BigDecimal.valueOf(numParcelas), 2, RoundingMode.HALF_UP);
        }

        double pv = valorPresente.doubleValue();
        double i = taxaMensal.doubleValue();
        int n = numParcelas;

        // Calcular (1 + i)^n
        double fator = Math.pow(1.0 + i, n);

        // Aplicar fórmula do Sistema Price
        double pmt = pv * (i * fator) / (fator - 1.0);

        return BigDecimal.valueOf(pmt).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula o valor total a ser pago
     * @param parcelaMensal Valor da parcela mensal
     * @param numParcelas Número de parcelas
     * @return Valor total com juros
     */
    public BigDecimal calcularValorTotalComJuros(BigDecimal parcelaMensal, Integer numParcelas) {
        return parcelaMensal.multiply(BigDecimal.valueOf(numParcelas));
    }

    /**
     * Gera a memória de cálculo mês a mês
     * @param valorInicial Valor do empréstimo
     * @param parcelaMensal Valor da parcela mensal
     * @param taxaMensal Taxa mensal em decimal
     * @param numParcelas Número de parcelas
     * @return Lista com os dados de cada parcela
     */
    public List<ParcelaSimulacaoDTO> gerarMemoriaCalculo(
        BigDecimal valorInicial,
        BigDecimal parcelaMensal,
        BigDecimal taxaMensal,
        Integer numParcelas
    ) {
        List<ParcelaSimulacaoDTO> memoriaCalculo = new ArrayList<>();
        BigDecimal saldoDevedor = valorInicial;

        for (int mes = 1; mes <= numParcelas; mes++) {
            BigDecimal saldoDevedorInicial = saldoDevedor.setScale(2, RoundingMode.HALF_UP);

            // Calcular juros do mês
            BigDecimal juros = saldoDevedor.multiply(taxaMensal).setScale(2, RoundingMode.HALF_UP);

            // Calcular amortização
            BigDecimal amortizacao = parcelaMensal.subtract(juros).setScale(2, RoundingMode.HALF_UP);

            // Calcular novo saldo devedor
            saldoDevedor = saldoDevedor.subtract(amortizacao);

            // Para a última parcela, ajustar para que o saldo final seja zero
            if (mes == numParcelas) {
                saldoDevedor = BigDecimal.ZERO;
            }

            ParcelaSimulacaoDTO parcela = ParcelaSimulacaoDTO.builder()
                    .mes(mes)
                    .saldoDevedorInicial(saldoDevedorInicial)
                    .juros(juros)
                    .amortizacao(amortizacao)
                    .saldoDevedorFinal(saldoDevedor.setScale(2, RoundingMode.HALF_UP))
                    .build();

            memoriaCalculo.add(parcela);
        }

        return memoriaCalculo;
    }
}
