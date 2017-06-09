package Configurador;

import Interfaces.Configuracao;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Ingrid, Leandro
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     * @throws java.rmi.AlreadyBoundException
     * @throws java.rmi.NotBoundException
     * @throws java.rmi.RemoteException
     */
    public static void main(String[] args) throws RemoteException, AlreadyBoundException, NotBoundException, IOException, InterruptedException {
        Scanner input = new Scanner(System.in);
        boolean sair = false;
        int avisos = 0;
        String escolha;

        while (true) {
            try {
                System.out.println("Deseja alterar o intervalo do produtor (P) ou do consumidor (C)? (Outra letra para poder sair)");
                escolha = input.nextLine().trim().toUpperCase();
                switch (escolha) {
                    case "P":
                        configuraNovoIntervalo(input, args[0], args[1], "Produtor");
                        break;
                    case "C":
                        configuraNovoIntervalo(input, args[2], args[3], "Consumidor");
                        break;
                    default:
                        System.out.println("Quer sair? (S para sair)");
                        escolha = input.nextLine().trim().toUpperCase();
                        if (escolha.equals("S")) {
                            sair = true;
                        }
                }
                if (sair == true) {
                    break;
                }
            } catch (InputMismatchException ex) {
                System.out.println("ERRO! N˙mero ou caracter inv·lido.");
                input.nextLine(); //consome o \n do buffer
            } catch (RemoteException | NotBoundException ex) {
            	avisos++;
                System.out.print("ERRO! Problemas ao se conectar/buscar/outros no registro.");
                if (avisos > 2) {
                	System.out.println();
                    break;
                } else {
                	System.out.println(" Aguardando 10s...");
                	sleep(10000);
                }
            }
        }
        System.out.println("CONCLUÕçDO! Configurador terminou.");
    }

    private static void configuraNovoIntervalo(Scanner input, String host, String porta, String nome) throws RemoteException, NotBoundException {
        Registry registro = LocateRegistry.getRegistry(host, Integer.parseInt(porta)); //RemoteException
        Configuracao remoto = (Configuracao) registro.lookup("Configuracao"); //ConnectException, RemoteException, NotBoundException
        int intervalo;
        System.out.println("Qual o intervalo de tempo desejado para o " + nome + " em ms?");
        intervalo = input.nextInt(); //InputMismatchException
        input.nextLine(); //consome o \n do buffer
        if (intervalo < 0) {
            intervalo = 0;
        }
        remoto.aplicaIntervalo(intervalo);
    }
}
