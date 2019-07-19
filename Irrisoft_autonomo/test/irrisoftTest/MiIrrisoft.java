package irrisoftTest;

import irrisoftpack.Irrisoft;

import java.util.LinkedHashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tareaTest.MiHiloTarea;
import tareapack.TareaManual;
import valvulaspack.Valvula;

public class MiIrrisoft extends Irrisoft{
    
    private static Logger logger = LogManager.getLogger(MiIrrisoft.class
	    .getName());
    public boolean quitaValvAbiertas = false;

    
    public MiIrrisoft() {
	
	// TODO Auto-generated constructor stub
    }
    
    public void quitarvalvabiertas(Valvula val, int tipo){
	logger.warn("Entra en quitarvalvabiertas de MiIrrisoft");
	quitaValvAbiertas = true;
	
    }
    
    public int addvalvsabiertas(Valvula val, int tipo){
	logger.warn("Entra en addvalvsabiertas de MiIrrisoft");
	int abiertas = 1;
	return abiertas;
    }
    //Metodo para clase hiloCaudalimetro, metodo automatico.
    //Metodo para clase Irrisoft, metodo valvsabiertas.
    //Metodo run() de HiloAmperimetro
    public int valvsabiertas(int tipo){
	logger.warn("Entra en valvsabiertas de MiIrrisoft");
	int valvAbi = 5;
	return valvAbi;
    }
    
    public LinkedHashSet<Valvula> listavalvsabiertas(int tipo){
	logger.warn("Entra eb lista valvs abiertas de MiIrrisoft");
	Valvula valv = new Valvula();
	LinkedHashSet<Valvula> list = new LinkedHashSet<>();
	list.add(valv);
	return list;
    }
    

}
