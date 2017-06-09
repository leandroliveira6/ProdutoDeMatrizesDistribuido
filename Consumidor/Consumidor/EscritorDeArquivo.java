/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Consumidor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Leandro
 */
public class EscritorDeArquivo {

    private BufferedWriter arquivo;

    public EscritorDeArquivo(String caminho) throws IOException {
        arquivo = new BufferedWriter(new FileWriter(caminho, true));
    }

    public void escreve(String texto) throws IOException {
        arquivo.write(texto);
    }

    public void fecha() throws IOException {
        arquivo.close();
    }
}
