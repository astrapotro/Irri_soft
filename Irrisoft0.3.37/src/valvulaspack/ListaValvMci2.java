package valvulaspack;

import java.util.ArrayList;
import java.util.List;

public class ListaValvMci2 {
	
	//TODO HAY que inicializar la lista con TODAS las valvulasd existentes !!!

	private static ListaValvMci2 mListaValvMci;
	protected List <Valvula> valvulas;
	
	private ListaValvMci2()
	{
		valvulas=new ArrayList <Valvula>();
	}
	
	public static  ListaValvMci2 getInstance()
	{
		if ( mListaValvMci==null)
			 mListaValvMci=new ListaValvMci2();
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
