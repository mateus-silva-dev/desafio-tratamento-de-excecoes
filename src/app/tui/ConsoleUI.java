package app.tui;

import exceptions.ContaNaoEncontradaException;
import exceptions.SaldoInsuficienteException;
import exceptions.ValorInvalidoException;
import model.ContaBancaria;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleUI {

    private static final Scanner input = new Scanner(System.in);
    static ContaBancaria conta = null;

    public static void iniciar() {
        while (true) {
            try {
                exibirMenuPrincipal();
                int opcao = Integer.parseInt(lerEntrada());
                if (opcao == 0) break;
                processarOpcao(opcao);
            } catch (Exception e) {
                System.out.println(">> [ ERRO ] " + e.getMessage());
                System.out.println("Pressione ENTER para continuar...");
                input.nextLine();
            } finally {
                System.out.println("- - - - - - - - - - - - -");
            }
        }
    }

    private static void processarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> conta = criarConta();
            case 2 -> realizarDeposito();
            case 3 -> realizarSaque();
            case 4 -> consultarSaldo();
            case 0 -> System.exit(0);
            default -> System.out.println("Opção inválida!");
        }
    }

    private static void exibirMenuPrincipal() {
        System.out.println();
        System.out.print("""
                - - - - - - - - - - - - -
                    SISTEMA BANCÁRIO
                - - - - - - - - - - - - -
                [1] Criar Conta
                [2] Realizar Depósito
                [3] Realizar Saque
                [4] Consultar Saldo
                [0] Sair
                - - - - - - - - - - - - -
                """);
        System.out.print("Escolha uma opção: ");
    }

    private static ContaBancaria criarConta() {
        System.out.println();
        System.out.print("""
                - - - - - - - - - - - - -
                       CRIAR CONTA
                - - - - - - - - - - - - -
                """);
        System.out.print("Nome do titular: ");
        String nome = lerEntrada();
        System.out.print("Numero da conta: ");
        String conta = lerEntrada();
        System.out.print("Saldo inicial: ");
        BigDecimal saldo = new BigDecimal(lerEntrada());
        return ContaBancaria.criar(nome, conta, saldo);
    }

    private static void realizarDeposito() {
        verificarContaExistente();
        System.out.println();
        System.out.print("""
                - - - - - - - - - - - - -
                         DEPÓSITO
                - - - - - - - - - - - - -
                """);
        boolean sucesso = false;
        while (!sucesso) {
            System.out.print("Qual valor deseja depositar? R$");
            try {
                conta.depositar(lerValorNumerico());
                sucesso = true;
                System.out.println(">> [ SUCESSO ] Depósito realizado!");
            } catch (NumberFormatException e) {
                System.out.println("Formato numérico inválido.");
            } catch (ValorInvalidoException e) {
                System.out.println(">> [ NEGADO ] " + e.getMessage());
                System.out.print("Deseja tentar novamente? (s/n)");
                if (lerEntrada().equalsIgnoreCase("N")) break;
            }
        }
        System.out.println("Operação finalizada!");
    }

    private static void realizarSaque() {
        verificarContaExistente();
        System.out.println();
        System.out.print("""
                - - - - - - - - - - - - -
                         SAQUE
                - - - - - - - - - - - - -
                """);
        boolean sucesso = false;
        while (!sucesso) {
            System.out.print("Qual valor deseja sacar? R$");
            try {
                conta.sacar(lerValorNumerico());
                sucesso = true;
                System.out.println(">> [ SUCESSO ] Saque realizado!");
            } catch (NumberFormatException e) {
                System.out.println(">> [ ERRO ] Formato numérico inválido.");
            } catch (SaldoInsuficienteException | ValorInvalidoException e) {
                System.out.println(">> [ ERRO ] " + e.getMessage());
                System.out.print("Deseja tentar novamente? (s/n)");
                if (lerEntrada().equalsIgnoreCase("N")) break;
            }
        }
        System.out.println("Operação finalizada!");
    }

    private static void consultarSaldo() {
        verificarContaExistente();
        System.out.println();
        System.out.print("""
                - - - - - - - - - - - - -
                          SALDO
                - - - - - - - - - - - - -
                """);
        conta.consultarSaldo();
    }

    public static String lerEntrada() {
        return input.nextLine();
    }

    private static void verificarContaExistente() {
        if (conta == null)
            throw new ContaNaoEncontradaException();
    }

    private static BigDecimal lerValorNumerico() throws NumberFormatException {
        return new BigDecimal(input.nextLine().replace(",", "."));
    }

}
