/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Consumidor;

import Interfaces.Callback;
import java.rmi.RemoteException;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Leandro
 */
public class CallbackImpl implements Callback {

    double resultado[];
    Semaphore passe;
    int quantidade, posicao;

    public CallbackImpl(int quantidade) {
        passe = new Semaphore(0);
        resultado = new double[quantidade];
        this.quantidade = quantidade;
    }

    @Override
    public void entregaResultado(int posicao, double resultado) throws RemoteException {
        this.resultado[posicao] = resultado;
        passe.release();
    }

    public double[] obtemResultado() throws InterruptedException {
        passe.acquire(quantidade);
        return resultado;
    }
}
