package programapack;


import java.util.LinkedList;


public class ListaProgsaexec {

	private static ListaProgsaexec mListatareasexec;
	private LinkedList <Programacion> programas;
	
	protected int anadedelay=0;
	
	private ListaProgsaexec()
	{
		programas=new LinkedList <Programacion>();
	}
	
	public synchronized static ListaProgsaexec getInstance()
	{
		if (mListatareasexec==null)
			mListatareasexec=new ListaProgsaexec();
		return mListatareasexec;
	}
	

	public synchronized void addprog(Programacion prog){
		
		programas.add(prog);
	}
	
	protected synchronized Programacion getprog(int pos){
		
		return programas.get(pos);
	}

	public LinkedList<Programacion> getprogramas() {
		return programas;
	}
	

	protected synchronized void delproglist(int pos){
		
		programas.remove(pos);
	}


	
}
