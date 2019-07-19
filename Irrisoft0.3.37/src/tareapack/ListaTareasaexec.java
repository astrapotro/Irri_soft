package tareapack;


import java.util.ArrayList;




public class ListaTareasaexec {

	private static ListaTareasaexec mListatareasexec;
	private ArrayList <Tarea> tareas;
	
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

	public ArrayList<Tarea> getTareas() {
		return tareas;
	}
	
	protected synchronized void addhilotar(Thread hilotar,int pos){
		
		tareas.get(pos).setHilotar(hilotar);
	}
	
	public synchronized void delhilotar(int pos){
		
		tareas.remove(pos);
	}

	public synchronized void delhilotar(Tarea tar){
		
		for (int i=0; i<tareas.size();i++){
			if (tareas.get(i).getIdtarea()==tar.getIdtarea())
				tareas.remove(i);
		}
		
		
	}
	
}
