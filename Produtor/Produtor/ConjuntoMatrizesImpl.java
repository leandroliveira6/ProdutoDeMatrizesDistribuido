package Produtor;

import Interfaces.ConjuntoMatrizes;
import java.io.Serializable;
import java.util.ArrayList;

/**
*
* @author Leandro
*/
public class ConjuntoMatrizesImpl implements ConjuntoMatrizes, Serializable {
	
	private static final long serialVersionUID = 1L;
	private ArrayList<double[][]> matrizes;
	
	public ConjuntoMatrizesImpl(ArrayList<double[][]> matrizes){
		this.matrizes = matrizes;
	}

	@Override
	public ArrayList<double[][]> obtemArrayList() {
		return matrizes;
	}
}
