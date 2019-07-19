package tareapack;

import java.util.ArrayList;
import java.util.List;


public class ListaTareasaexec {

	private static ListaTareasaexec mListatareasexec;
	private List <Tarea> tareas;
	
	private ListaTareasaexec()
	{
		tareas=new ArrayList <Tarea>();
	}
	
	public synchronized static ListaTareasaexec getInstance()
	{
		if (mListatareasexec==null)
			mListatareasexec=new ListaTareasaexec();
		return mListatareasexec;
	}
	

	public synchronized void addtarea(Tarea tarea){
		
		tareas.add(tarea);
	}
	
	public synchronized Tarea gettarea(int pos){
		
		return tareas.get(pos);
	}

	public List<Tarea> getTareas() {
		return tareas;
	}
	
	protected synchronized void addhilotar(Thread hilotar,int pos){
		
		tareas.get(pos).setHilotar(hilotar);
	}
	
	public synchronized void delhilotar(int pos){
		
		tareas.remove(pos);
	}


	
}
