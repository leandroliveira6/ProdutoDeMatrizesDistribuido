package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Ingrid, Leandro
 */
public interface Execucao extends Remote {

    public void execute(Runnable tarefa) throws RemoteException;
}
