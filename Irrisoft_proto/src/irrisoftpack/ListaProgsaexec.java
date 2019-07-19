package irrisoftpack;

import java.util.ArrayList;
import java.util.List;

public class ListaProgsaexec {

	private static ListaProgsaexec mListatareasexec;
	private List <Programacion> programas;
	
	protected int anadedelay=0;
	
	private ListaProgsaexec()
	{
		programas=new ArrayList <Programacion>();
	}
	
	public synchronized static ListaProgsaexec getInstance()
	{
		if (mListatareasexec==null)
			mListatareasexec=new ListaProgsaexec();
		return mListatareasexec;
	}
	

	protected  synchronized void addprog(Programacion prog){
		
		programas.add(prog);
	}
	
	protected synchronized Programacion getprog(int pos){
		
		return programas.get(pos);
	}

	public List<Programacion> getprogramas() {
		return programas;
	}
	

	protected synchronized void delhilotar(int pos){
		
		programas.remove(pos);
	}


	
}
