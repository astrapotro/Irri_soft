package valvulaspack;

import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;

import java.util.LinkedList;

public class ListaValvMci {

    // TODO HAY que inicializar la lista con TODAS las valvulas existentes !!!

    protected LinkedList<Valvula> valvulas;
    private Irrisoft IR;

    public ListaValvMci() {
	valvulas = new LinkedList<Valvula>();
	this.IR = Irrisoft.window;
    }

    /**
     * Añado valvulas MCI a la lista de valvulas.
     * 
     * @param valv
     */
    public void addvalvmci(Valvula valv) {

	valvulas.add(valv);
    }

    /**
     * Cojo valvulas segun el numero de electrovalvula (INT).
     * 
     * @param numelecvalv
     * @return
     */
    public Valvula getvalvmci(int numelecvalv) {

	if ( numelecvalv>-1)
	    return valvulas.get(numelecvalv);
	else{
	    Valvula valv = new Valvula();
	    valv.setAbierta(false);
	    valv.setCodelecvalv("-1");
	    return valv;
	}
    }

    /**
     * Cojo valvulas segun el numero de electrovalvula (STRING).
     * 
     * @param numelecvalv
     * @return
     */
    public Valvula getvalvmci(String numelecvalv) {

	int index = 0;
	int numValMCI = Integer.parseInt(numelecvalv);

	if (IrrisoftConstantes.MCI_200 > numValMCI) {
	    for (int i = 0; i < IR.valvsmci.getsizeof(); i++) {
		if (IR.valvsmci.getvalvmci(i).getCodelecvalv()
			.contentEquals(numelecvalv)) {
		    index = i;
		    IR.valvsmci.getvalvmci(i).setIndex(index);
		    break;
		}
		else
		    index=-1;
	    }
	} else if (IrrisoftConstantes.MCI_200 < numValMCI
		&& IrrisoftConstantes.MCI_300 > numValMCI) {
	    for (int i = 0; i < IR.valvsmci2.getsizeof(); i++) {
		if (IR.valvsmci2.getvalvmci(i).getCodelecvalv()
			.contentEquals(numelecvalv)) {
		    index = i;
		    IR.valvsmci2.getvalvmci(i).setIndex(index);
		    break;
		}
		else
		    index=-1;
	    }
	} else if (IrrisoftConstantes.MCI_300 < numValMCI
		&& IrrisoftConstantes.MCI_400 > numValMCI) {
	    for (int i = 0; i < IR.valvsmci3.getsizeof(); i++) {
		if (IR.valvsmci3.getvalvmci(i).getCodelecvalv()
			.contentEquals(numelecvalv)) {
		    index = i;
		    IR.valvsmci3.getvalvmci(i).setIndex(index);
		    break;
		}
		else
		    index=-1;
	    }
	} else if (IrrisoftConstantes.MCI_400 < numValMCI) {
	    for (int i = 0; i < IR.valvsmci4.getsizeof(); i++) {
		if (IR.valvsmci4.getvalvmci(i).getCodelecvalv()
			.contentEquals(numelecvalv)) {
		    index = i;
		    IR.valvsmci4.getvalvmci(i).setIndex(index);
		    break;
		}
		else
		    index=-1;
	    }
	}

	return getvalvmci(index);
    }

    /**
     * Me devuelve el tamaño de la lista de valvulas MCI.
     * 
     * @return
     */
    public int getsizeof() {
	return valvulas.size();
    }

    /**
     * @return
     */
    public LinkedList<Valvula> getvalvulas() {

	return valvulas;
    }

    /**
     * Elimino valuvlas de memoria segun la posicion.
     * 
     * @param i
     */
    public void remove(int i) {
	valvulas.remove(i);

    }

}
