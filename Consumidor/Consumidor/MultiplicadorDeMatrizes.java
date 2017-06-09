/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Consumidor;

import Interfaces.Callback;
import Interfaces.Execucao;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Leandro
 */
public class MultiplicadorDeMatrizes {

    public MultiplicadorDeMatrizes() {
    }

    public void multiplicaMatrizes(Execucao remotoExecucao, ArrayList<double[][]> matrizes) throws RemoteException, InterruptedException, IOException {
        System.out.println("MM em execução: " + Thread.currentThread().getName());
        EscritorDeArquivo arquivo;
        double resultado[][], retorno[] = null;
        CallbackImpl cimpl;
        Callback cremo;
        int n;

        if (matrizes != null && !matrizes.isEmpty()) {
            n = matrizes.get(0).length; //quantidade de linhas
        } else {
            return;
        }

        arquivo = new EscritorDeArquivo("resultados.txt");
        resultado = matrizes.remove(0);
        if (!matrizes.isEmpty()) {
            cimpl = new CallbackImpl(n * n);
            cremo = (Callback) UnicastRemoteObject.exportObject(cimpl, 0); //RemoteException

            for (double[][] matriz : matrizes) {
                transpor(matriz);
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        remotoExecucao.execute(new ProdutoInterno(n * i + j, resultado[i], matriz[j], cremo));
                    }
                }
                transpor(matriz); //matriz original

                try {
                    retorno = cimpl.obtemResultado(); //InterruptedException
                    //PS não tratamos o caso de não ser retornado algo (timeout), 
                    //estamos supondo que a rede está perfeitamente funcional, sempre
                } catch (InterruptedException ex) {
                    System.out.println("ERRO! Thread interrompida antes de completar o cálculo (" + ex.getClass().getSimpleName() + "), encerrando a aplicação...");
                    System.exit(1);
                }
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        resultado[i][j] = retorno[n * i + j];
                    }
                }
            }
        }

        //escrever resultado em um arquivo
        for (int i = 0; i < n; i++) {
            String s = Arrays.toString(resultado[i]) + "\r\n";
            System.out.print(s);
            arquivo.escreve(s);
        }
        arquivo.escreve("\r\n");
        arquivo.fecha();
    }

    public static void transpor(double m[][]) {
        for (int i = 0; i < m.length; i++) {
            for (int j = i + 1; j < m.length; j++) {
                double tmp;
                tmp = m[i][j];
                m[i][j] = m[j][i];
                m[j][i] = tmp;
            }
        }
    }
}
