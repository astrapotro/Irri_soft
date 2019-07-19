package irrisoftpack;

import java.util.ArrayList;
import java.util.List;

public class ListaHilos {

	
	private static ListaHilos mListahilos;
	private List <Thread> hilos;
	
	private ListaHilos()
	{
		hilos=new ArrayList <Thread>();
	}
	
	public synchronized static ListaHilos getInstance()
	{
		if (mListahilos==null)
			mListahilos=new ListaHilos();
		return mListahilos;
	}
	
	protected synchronized void addhilo(Thread hilo){
		
		hilos.add(hilo);
	}
	
	protected synchronized Thread gethilo(int pos){
		
		return hilos.get(pos);
	}
	
	protected synchronized void delhilo(int pos){
		
		hilos.remove(pos);
	}
	
	
}
