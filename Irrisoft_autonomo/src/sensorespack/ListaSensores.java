package sensorespack;

import java.util.LinkedList;

public class ListaSensores {

    private static ListaSensores mListaSensores;
    private LinkedList<Sensor> sensores;

    private ListaSensores() {
	sensores = new LinkedList<Sensor>();
    }

 
    public static ListaSensores getInstance() {
	if (mListaSensores == null)
	    mListaSensores = new ListaSensores();
	return mListaSensores;
    }

    /**
     * Te devuelve una lista de sensores.
     * @return
     */
    public LinkedList<Sensor> getsens() {
	return sensores;
    }

    /**
     * Te devuelve un sensor de la lista de sensores
     * introduciendo unos parametros
     * @param numplaca
     * @param tipo
     * @param tipoplacanum
     * @return
     */
    public Sensor getsens(int numplaca, int tipo) {
	int i = 0;
	int z = 0;

	for (i = 0; i < sensores.size(); i++) {
	    if (sensores.get(i).getNum_placa() == numplaca
		    && sensores.get(i).getTipo() == tipo )
		z = i;
	}

	return sensores.get(z);
    }

}
