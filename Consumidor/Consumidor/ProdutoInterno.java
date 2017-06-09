/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Consumidor;

import java.io.Serializable;
import Interfaces.Callback;
import java.rmi.RemoteException;

/**
 *
 * @author Leandro
 */
public class ProdutoInterno implements Runnable, Serializable {
	
	private static final long serialVersionUID = 1L;
	private final double[] v1, v2;
    Callback cremo;

    public ProdutoInterno(Callback cremo, double v1[], double v2[]) {
        this.v1 = v1.clone();
        this.v2 = v2.clone();
        this.cremo = cremo;
    }

    @Override
    public void run() {
        //System.out.println("Thread PI em execução: " + Thread.currentThread().getName());
        try {
            cremo.entregaResultado(calcular());
        } catch (RemoteException ex) {
            System.out.println("ERRO! Problema ao entregar o resultado, encerrando a thread...");
            System.exit(1);
        }
    }

    public double calcular() {
        double resultado = 0;
        for (int i = 0; i < v1.length; i++) {
            resultado += v1[i] * v2[i];
        }
        return resultado;
    }
}
