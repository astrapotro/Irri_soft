package valvulaspack;

import java.util.LinkedList;

public class ListaPlacasGhost {
    
    // TODO HAY que inicializar la lista con TODAS las valvulas existentes !!!

    // private static ListaValvMci mListaValvMci;
    protected LinkedList<PlacaGhost> placas;

    public ListaPlacasGhost() {
	placas = new LinkedList<PlacaGhost>();
    }

    // public static ListaValvMci getInstance()
    // {
    // if ( mListaValvMci==null)
    // mListaValvMci=new ListaValvMci();
    // return mListaValvMci;
    // }
    //

    /**
     * @param plac
     */
    public void addplaca(PlacaGhost plac) {

	placas.add(plac);
    }

    /**
     * @param numplac
     * @return
     */
    public PlacaGhost getplaca(int numplac) {
	return placas.get(numplac);
    }

    /**
     * @return
     */
    public int getsizeof() {
	return placas.size();
    }

    /**
     * @return
     */
    public LinkedList <PlacaGhost> getplacas(){
	return placas;
    }

}
