/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Produtor;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.Callable;

/**
 *
 * @author Leandro
 */
public class LeitorDeArquivo implements Callable<ArrayList<double[][]>> {

    ArrayList<double[][]> matrizes;
    String caminho;
    int n, quantidade;

    public LeitorDeArquivo(String caminho, int n, int quantidade) {
        this.caminho = caminho;
        this.n = n;
        this.quantidade = quantidade;
        matrizes = new ArrayList<>();
    }

    @Override
    public ArrayList<double[][]> call() throws Exception {
        System.out.println("Thread LDA em execução: " + Thread.currentThread().getName());
        Scanner arquivo = new Scanner(new FileReader(caminho)); //FileNotFoundException;
        for (int k = 0; k < quantidade; k++) {
            double[][] matriz = new double[n][n];
            for (int i = 0; i < n; i++) {
                try {
                    String linha = arquivo.nextLine(); //NoSuchElementException
                    String colunas[] = linha.split(" ");
                    for (int j = 0; j < n; j++) {
                        matriz[i][j] = Integer.parseInt(colunas[j]); //Exception
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("ERRO! Arquivo incompleto. Mensagem: " + e.getMessage());
                    System.exit(1);
                }
            }
            matrizes.add(matriz);
        }
        arquivo.close();
        return matrizes;
    }
}
