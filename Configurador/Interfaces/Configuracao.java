package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Ingrid, Leandro
 */
public interface Configuracao extends Remote {

    public void aplicaIntervalo(int intervalo) throws RemoteException;
}
