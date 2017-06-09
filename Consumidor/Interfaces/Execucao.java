/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Leandro
 */
public interface Execucao extends Remote {

    public void execute(Runnable tarefa) throws RemoteException;
}
