/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Consumidor;

import java.rmi.RemoteException;
import Interfaces.Configuracao;

/**
 *
 * @author Leandro
 */
public class ConfiguracaoImpl implements Configuracao {

    private int intervalo;

    public ConfiguracaoImpl(int intervalo) {
        this.intervalo = intervalo;
    }

    synchronized public int obtemIntervalo() {
        return intervalo;
    }

    @Override
    synchronized public void aplicaIntervalo(int intervalo) throws RemoteException {
        this.intervalo = intervalo;
    }
}
