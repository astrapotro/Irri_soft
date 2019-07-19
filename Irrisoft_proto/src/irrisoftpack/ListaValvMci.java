package irrisoftpack;

import java.util.ArrayList;
import java.util.List;

public class ListaValvMci {
	
	//TODO HAY que inicializar la lista con TODAS las valvulasd existentes !!!

	private static ListaValvMci mListaValvMci;
	private List <Valvula> valvulas;
	
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
	

	protected void addvalvmci(Valvula valv){
		
		valvulas.add(valv);
	}
	
	protected Valvula getvalvmci(int numelecvalv){
		
		return valvulas.get(numelecvalv);
	}
	
	protected Valvula getvalvmci (String numelecvalv){
		
		return valvulas.get(Integer.parseInt(numelecvalv));
	}


	
}
