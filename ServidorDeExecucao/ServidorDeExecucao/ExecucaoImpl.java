/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServidorDeExecucao;

import Interfaces.Execucao;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Ingrid, Leandro
 */
public class ExecucaoImpl implements Execucao {

    private ExecutorService es;

    public ExecucaoImpl(int quantidade) {
        es = Executors.newFixedThreadPool(quantidade);
    }

    @Override
    public void execute(Runnable tarefa) throws RemoteException {
        es.execute(tarefa);
    }
}
