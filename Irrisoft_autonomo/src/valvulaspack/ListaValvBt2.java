package valvulaspack;

import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;

import java.math.BigInteger;
import java.util.LinkedList;

public class ListaValvBt2 {

    private LinkedList<Valvula> valvulas;
    private Irrisoft IR;

    public ListaValvBt2() {
	valvulas = new LinkedList<Valvula>();
	this.IR = Irrisoft.window;
    }

    /**
     * Añado valvulas BT2 a la lista de valvulas.
     * 
     * @param valv
     */
    public void addvalvbt2(Valvula valv) {

	valvulas.add(valv);
    }

    /**
     * Cojo valvulas dela lista segun la posicion.
     * 
     * @param i
     * @return
     */
    public Valvula getvalvbt2(int i) {
	if ( i>-1)
	    return valvulas.get(i);
	else{
	    Valvula valv = new Valvula();
	    valv.setAbierta(false);
	    valv.setCodelecvalv("-1");
	    return valv;
	}
    }

    /**
     * Cojo la valvula segun el numero de electrovalvula.
     * 
     * @param numelecvalv
     * @return
     */
    public Valvula getvalvbt2(String numelecvalv) {

	int index = 0;
	int numValBT2 = Integer.parseInt(numelecvalv);

	if (IrrisoftConstantes.BT2_1000 <= numValBT2
		&& IrrisoftConstantes.BT2_2000 >= numValBT2) {
	    for (int i = 0; i < IR.valvsbt2.getsizeof(); i++) {
		if (IR.valvsbt2.getvalvbt2(i).getCodelecvalv()
			.contentEquals(numelecvalv)) {
		    index = i;
		    IR.valvsbt2.getvalvbt2(i).setIndex(index);
		    break;
		}
		else
		    index=-1;

	    }
	} else if (IrrisoftConstantes.BT2_2000 <= numValBT2
		&& IrrisoftConstantes.BT2_3000 >= numValBT2) {
	    for (int i = 0; i < IR.valvsbt22.getsizeof(); i++) {
		if (IR.valvsbt22.getvalvbt2(i).getCodelecvalv()
			.contentEquals(numelecvalv)) {
		    index = i;
		    IR.valvsbt22.getvalvbt2(i).setIndex(index);
		    break;
		}
		else
		    index=-1;
	    }
	} else if (IrrisoftConstantes.BT2_3000 <= numValBT2
		&& IrrisoftConstantes.BT2_4000 >= numValBT2) {
	    for (int i = 0; i < IR.valvsbt23.getsizeof(); i++) {
		if (IR.valvsbt23.getvalvbt2(i).getCodelecvalv()
			.contentEquals(numelecvalv)) {
		    index = i;
		    IR.valvsbt23.getvalvbt2(i).setIndex(index);
		   break;
		}
		else
		    index=-1;
	    }
	} else if (IrrisoftConstantes.BT2_4000 <= numValBT2) {
	    for (int i = 0; i < IR.valvsbt24.getsizeof(); i++) {
		if (IR.valvsbt24.getvalvbt2(i).getCodelecvalv()
			.contentEquals(numelecvalv)) {
		    index = i;
		    IR.valvsbt24.getvalvbt2(i).setIndex(index);
		    break;
		}
		else
		    index=-1;
	    }
	}

	
	    return getvalvbt2(index);

    }

    /**
     * Cojo la valvula segun el tipo y el deco.
     * 
     * @param tipo
     * @param deco
     * @return
     */
    public Valvula getvalvbt2(int tipo, int deco) {

	int index = 0;

	if (IrrisoftConstantes.PLACA_BT2_5 == tipo) {

	    for (int i = 0; i < IR.valvsbt2.getsizeof(); i++) {
		if (IR.valvsbt2.getvalvbt2(i).getDeco() == deco) {
		    index = i;
		    IR.valvsbt2.getvalvbt2(i).setIndex(index);
		    break;
		}
		else
		    index=-1;
	    }
	} else if (IrrisoftConstantes.PLACA_BT2_6 == tipo) {
	    for (int i = 0; i < IR.valvsbt22.getsizeof(); i++) {
		if (IR.valvsbt22.getvalvbt2(i).getDeco() == deco) {
		    index = i;
		    IR.valvsbt22.getvalvbt2(i).setIndex(index);
		    break;
		}
		else
		    index=-1;
	    }
	} else if (IrrisoftConstantes.PLACA_BT2_7 == tipo) {
	    for (int i = 0; i < IR.valvsbt23.getsizeof(); i++) {
		if (IR.valvsbt23.getvalvbt2(i).getDeco() == deco) {
		    index = i;
		    IR.valvsbt23.getvalvbt2(i).setIndex(index);
		    break;
		}
		else
		    index=-1;
	    }
	} else if (IrrisoftConstantes.PLACA_BT2_8 == tipo) {
	    for (int i = 0; i < IR.valvsbt24.getsizeof(); i++) {
		if (IR.valvsbt24.getvalvbt2(i).getDeco() == deco) {
		    index = i;
		    IR.valvsbt24.getvalvbt2(i).setIndex(index);
		   break;
		}
		else
		    index=-1;
	    }
	}
	
	 return getvalvbt2(index);
    }

    /**
     * Me devuelve el tamaño de la lista de valvulas.
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
     * Elimino valvulas de memoria de la lista de valvulas.
     * 
     * @param i
     */
    public void remove(int i) {
	valvulas.remove(i);

    }
}
