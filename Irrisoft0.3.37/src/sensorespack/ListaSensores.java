package sensorespack;


import java.util.LinkedList;
import java.util.List;

public class ListaSensores {


	private static ListaSensores mListaSensores;
	private LinkedList <Sensor> sensores;
	
	private ListaSensores()
	{
		sensores=new LinkedList <Sensor>();
	}
	
	public static ListaSensores getInstance()
	{
		if ( mListaSensores==null)
			 mListaSensores=new ListaSensores();
		return  mListaSensores;
	}
	
	
	public List<Sensor> getsens (){
		return sensores;
	}
	
	public void addsensor (Sensor sens){
		sensores.add(sens);
	}
	
}
