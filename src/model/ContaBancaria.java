package model;

import exceptions.ContaInvalidaException;
import exceptions.SaldoInsuficienteException;
import exceptions.ValorInvalidoException;

import java.math.BigDecimal;

public class ContaBancaria {
    private final String nome;
    private final String numero;
    private BigDecimal saldo;

    private ContaBancaria(String nome, String numero, BigDecimal saldo) {
        this.nome = nome;
        this.numero = numero;
        this.saldo = saldo;
    }

    public static ContaBancaria criar(String nome, String numero, BigDecimal saldo) {
        validarCriacao(nome, numero, saldo);
        return new ContaBancaria(nome, numero, saldo);
    }

    public void depositar(BigDecimal valorDeposito) {
        validarDeposito(valorDeposito);
        this.saldo = this.saldo.add(valorDeposito);
    }

    public void sacar(BigDecimal valorSaque) {
        validarSaque(valorSaque);
        this.saldo = this.saldo.subtract(valorSaque);
    }

    public void consultarSaldo() {
        System.out.printf("""
                CLIENTE:  %S
                CONTA:    %S
                SALDO:    R$ %.2f
                """, nome, numero, saldo);
    }

    private static void validarCriacao(String nome, String numero, BigDecimal saldo) {
        if (nome == null || nome.isBlank())
            throw new ContaInvalidaException("Nome inválido.");
        if (numero == null || !numero.matches("\\d+"))
            throw new ContaInvalidaException("A conta deve conter apenas números.");
        if (saldo.compareTo(BigDecimal.ZERO) < 0)
            throw new ContaInvalidaException("O saldo inicial não pode ser negativo.");
    }

    private void validarDeposito(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0)
            throw new ValorInvalidoException("O valor de depósito deve ser maior que zero.");
    }

    private void validarSaque(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0)
            throw new ValorInvalidoException("O valor do saque deve ser maior que zero.");
        if (this.saldo.compareTo(valor) < 0)
            throw new SaldoInsuficienteException("Saldo insuficiente.");
    }
}
