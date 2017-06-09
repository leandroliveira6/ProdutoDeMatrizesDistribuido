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

    double resultado;
    Semaphore passe;

    public CallbackImpl() {
        passe = new Semaphore(0);
    }

    @Override
    public void entregaResultado(double resultado) throws RemoteException {
        this.resultado = resultado;
        passe.release();
    }

    public double obtemResultado() throws InterruptedException {
        passe.acquire();
        return resultado;
    }
}
