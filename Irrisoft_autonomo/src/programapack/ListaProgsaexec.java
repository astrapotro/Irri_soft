package programapack;

import java.util.LinkedList;

public class ListaProgsaexec {

    private static ListaProgsaexec mListatareasexec;
    private LinkedList<Programacion> programas;

    protected int anadedelay = 0;

    private ListaProgsaexec() {
	programas = new LinkedList<Programacion>();
    }


    public synchronized static ListaProgsaexec getInstance() {
	if (mListatareasexec == null)
	    mListatareasexec = new ListaProgsaexec();
	return mListatareasexec;
    }

    /**
     * Añado programas a la lista de programas.
     * @param prog
     */
    public synchronized void addprog(Programacion prog) {

	programas.add(prog);
    }

    /**
     *Cojo programas segun la posicion de la 
     *lista de programas.
     *@param pos
     * @return
     */
    protected synchronized Programacion getprog(int pos) {

	return programas.get(pos);
    }

    /**
     * @return
     */
    public LinkedList<Programacion> getprogramas() {
	return programas;
    }

    /**
     * Elimino programas de la lista de programas
     * segun la posicion.
     * @param pos
     */
    protected synchronized void delproglist(int pos) {

	programas.remove(pos);
    }

}
