/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Produtor;

import Interfaces.ConjuntoMatrizes;
import Interfaces.Produtor;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Leandro
 */
public class ProdutorImpl implements Produtor {

    private final ArrayList<ArrayList<double[][]>> matrizes;

    public ProdutorImpl() {
        matrizes = new ArrayList<>();
    }

    synchronized public void adicionaMatrizes(ArrayList<double[][]> matrizes) {
        if (!matrizes.isEmpty()) { //quantidade de matrizes precisa ser maior que zero
            this.matrizes.add(matrizes);
            //System.out.println("Conjunto de matrizes adicionado! Qtd=" + matrizes.size());
        }
    }

    @Override
    synchronized public ConjuntoMatrizes obtemMatrizes() throws RemoteException {
        if (!matrizes.isEmpty()) {
            return new ConjuntoMatrizesImpl(matrizes.remove(0));
        }
        return null;
    }
}
