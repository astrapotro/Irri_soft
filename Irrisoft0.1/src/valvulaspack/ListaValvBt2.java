package valvulaspack;

import java.util.ArrayList;
import java.util.List;

public class ListaValvBt2 {


	private static ListaValvBt2 mListaValvBt2;
	private List <Valvula> valvulas;
	
	private ListaValvBt2()
	{
		valvulas=new ArrayList <Valvula>();
	}
	
	public static ListaValvBt2 getInstance()
	{
		if ( mListaValvBt2==null)
			 mListaValvBt2=new ListaValvBt2();
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
