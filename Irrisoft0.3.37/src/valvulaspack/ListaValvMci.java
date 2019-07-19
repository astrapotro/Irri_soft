package valvulaspack;

import java.util.ArrayList;
import java.util.List;

public class ListaValvMci {
	
	//TODO HAY que inicializar la lista con TODAS las valvulasd existentes !!!

	private static ListaValvMci mListaValvMci;
	protected List <Valvula> valvulas;
	
	
	private ListaValvMci()
	{
		valvulas=new ArrayList <Valvula>();
	}
	
	public static  ListaValvMci getInstance()
	{
		if ( mListaValvMci==null)
			 mListaValvMci=new ListaValvMci();
		return  mListaValvMci;
	}
	

	public void addvalvmci(Valvula valv){
		
		valvulas.add(valv);
	}
	
	public Valvula getvalvmci(int numelecvalv){
		
		return valvulas.get(numelecvalv);
	}
	
	public Valvula getvalvmci (String numelecvalv){
		
		return valvulas.get(Integer.parseInt(numelecvalv)-1);
	}
	
	public int getsizeof (){
		return valvulas.size();
	}


	
}
