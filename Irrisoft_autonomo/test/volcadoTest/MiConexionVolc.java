package volcadoTest;

import irrisoftpack.SerialDriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import alertaspack.GestorAlertas;

import volcadopack.ConexionVolc;

public class MiConexionVolc extends ConexionVolc{

    public MiConexionVolc() {
	
	// TODO Auto-generated constructor stub
    }

    private static Logger logger = LogManager.getLogger(MiConexionVolc.class
	    .getName());
    
    public boolean tareaEcha = false;
    public boolean entraPrograma = false;
    public boolean tareaInsertada = true;
    public boolean borradaTarea = true;
    public boolean insertadoSens = true;
    public boolean escrito = false;
    private static GestorAlertas ga;
//    public MiConexionVolc() {
//	// TODO Auto-generated constructor stub
//    }
    
//    public void tarea(){
//	logger.warn("Entra en tarea de miConexionVolc");
//	tareaEcha = true;
//    }
    
    public void programa(){
	logger.warn("Entra en programa de miConexionVolc");
	entraPrograma = true;
	
    }
    
    public boolean Insertarea(){
	logger.warn("Entra en tarea de miConexionVolc");
	return tareaInsertada;
    }
    
    public boolean Borratarea(){
	logger.warn("Entra en BorrarTarea de miConexionVolc");
	return borradaTarea;
    }
    
    public void escribeip(){
	logger.warn("Entra en escribeIP de miConexionVolc");
	escrito = true;
	
    }

}
