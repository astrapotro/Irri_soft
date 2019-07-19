package valvulaspack;


import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;

import java.util.LinkedList;



public class ListaValvSamcla {
    // TODO HAY que inicializar la lista con TODAS las valvulas existentes !!!

    protected LinkedList<Valvula> valvulas;
    private Irrisoft IR;

    public ListaValvSamcla() {
	valvulas = new LinkedList<Valvula>();
	this.IR = Irrisoft.window;
    }

    /**
     * Añado valvulas MCI a la lista de valvulas.
     * 
     * @param valv
     */
    public void addvalvsamcla(Valvula valv) {

	valvulas.add(valv);
    }

    /**
     * Cojo valvulas segun el numero de electrovalvula (INT).
     * 
     * @param numelecvalv
     * @return
     */
    public Valvula getvalvsamcla(int numelecvalv) {

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
    public Valvula getvalvsamcla(String numelecvalv) {

	int index = 0;
	int numVal = Integer.parseInt(numelecvalv);

	    for (int i = 0; i < IR.valvsamcla.getsizeof(); i++) {
		if (IR.valvsamcla.getvalvsamcla(i).getCodelecvalv()
			.contentEquals(numelecvalv)) {
		    index = i;
		    IR.valvsamcla.getvalvsamcla(i).setIndex(index);
		    break;
		}
		else
		    index=-1;
	    }
	
	return getvalvsamcla(index);
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
