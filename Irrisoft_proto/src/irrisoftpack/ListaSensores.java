package irrisoftpack;

import java.util.ArrayList;
import java.util.List;

public class ListaSensores {


	private static ListaSensores mListaSensores;
	private List <Sensor> sensores;
	
	private ListaSensores()
	{
		sensores=new ArrayList <Sensor>();
	}
	
	public static ListaSensores getInstance()
	{
		if ( mListaSensores==null)
			 mListaSensores=new ListaSensores();
		return  mListaSensores;
	}
}
