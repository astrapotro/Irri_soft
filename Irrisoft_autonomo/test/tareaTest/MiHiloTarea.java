package tareaTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tareapack.HiloTarea;
import tareapack.TareaManual;

public class MiHiloTarea extends HiloTarea {
    private static Logger logger = LogManager.getLogger(MiHiloTarea.class
	    .getName());
    public boolean accionar = true;
    public boolean runHiloTarea = false;
    public boolean gestionado = false;

    public MiHiloTarea(TareaManual tarea) {
	super(tarea);
	// TODO Auto-generated constructor stub
    }

    public void run() {
	logger.warn("Entra en run de MiHiloTarea");
	runHiloTarea = true;
    }

//    public boolean accionvalv(String puerto, int tipoplaca) {
//	logger.warn("Entra en accionvalv de MiHiloTarea");
//	return accionar;
//
//    }
    
    public void gestvalv(int tipo, boolean accionabrir){
	logger.warn("Entra en gestvavl de MiHiloTarea");
	gestionado = true;
	
    }

}
