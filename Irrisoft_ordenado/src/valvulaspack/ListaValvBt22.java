package valvulaspack;

import java.util.ArrayList;
import java.util.List;

public class ListaValvBt22 {


	private static ListaValvBt22 mListaValvBt2;
	private List <Valvula> valvulas;
	
	private ListaValvBt22()
	{
		valvulas=new ArrayList <Valvula>();
	}
	
	public static ListaValvBt22 getInstance()
	{
		if ( mListaValvBt2==null)
			 mListaValvBt2=new ListaValvBt22();
		return  mListaValvBt2;
	}

	
	public void addvalvbt2 (Valvula valv){
		
		valvulas.add(valv);
	}
	
	public Valvula getvalvbt2 (int numelecvalv){
		
		return valvulas.get(numelecvalv);
	}
	
	protected Valvula getvalvbt2 (String numelecvalv){
		
		return valvulas.get(Integer.parseInt(numelecvalv));
	}
	
	
}