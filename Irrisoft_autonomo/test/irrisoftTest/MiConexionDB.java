package irrisoftTest;

import irrisoftpack.ConexionDB;

import java.util.ArrayList;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import programapack.Programacion;
import programapack.TareaProg;

import tareapack.TareaManual;

public class MiConexionDB extends ConexionDB {

    private static Logger logger = LogManager.getLogger(MiConexionDB.class
	    .getName());
    public boolean ejecutada = false;
    public boolean cerradas = false;
    public boolean ejecutaHilo = false;
    public boolean creaTareasProg = false;
    public boolean borrado = true;
    public boolean borrada = true;
    public boolean progEjecutado = false;
    public boolean fechado = true;
    public boolean datosProg = false;
    public boolean tipoProg = false;
    public boolean entraPrograma = false;
    public boolean entraTarea = false;
    public boolean insertalConsumo = false;
    public boolean insertarConsumo = false;
    public boolean escribeLConsumo = false;
    public boolean escribeRConsumo = false;
    public boolean insertViento = false;
    public boolean insertarTemp = false;
    public boolean actualizaValv = false;
    public boolean actualizadaTarea = false;

    public MiConexionDB() {
	// TODO Auto-generated constructor stub
	super();
    }

    
    public synchronized void actualizadeberes(int id, String fechahoy) {
	// TODO Auto-generated method stub
	super.actualizadeberes(id, fechahoy);
    }

    public void cierratodasvalvs() {
	logger.warn("Entra en cierratodasvalvs de miConexionDB");
	cerradas = true;
    }

    public void ejecutarea(TareaManual tarea) {
	logger.warn("Entra en ejecuta tarea de miConexionDB");
	ejecutada = true;
    }

    public void ejecutatareahilo(TareaManual tarea) {
	logger.warn("Entra en ejecuta tarea hilo de miConexionDB");
	ejecutaHilo = true;
    }

    public void creatareasprog(Programacion prog) {
	logger.warn("Entra en creaTareasProg de miConexionDB");
	creaTareasProg = true;
    }

    public boolean Borratarea(TareaManual taream) {
	logger.warn("Entra en BorrarTarea(tar) de miConexionDB");
	return borrado;

    }

    public boolean Borratarea() {
	logger.warn("Entra por Borratarea() de miConexionDB");
	return borrada;
	
    }
    public void ejecutareaprog(TareaProg tareaprog, int z,
	    Programacion prog){
	logger.warn("Entra en ejecuta tarea prog de miConexionDB");
	progEjecutado = true;
	
    }
    
    public boolean esfecha(int id, String tipo, Date fechain, Date fechafin,
	    Date hoy, ArrayList<Integer> dias){
	logger.warn("Entra en esfecha de miConexionDB");
	return fechado;
    }
    
    public void datosprog(Programacion prog){
	logger.warn("Entra en datosProg de miConexionDB");
	datosProg = true;
	
    }
    
    public void tipoprog(Programacion prog, Date hoy){
	logger.warn("Entra en tipoProg de miConexionDB");
	tipoProg = true;
	
    }

    public void tarea(){
	logger.warn("Entra en tarea de MiConexionDB");
	entraTarea = true;
	
    }
    
//    public void programa(){
//	logger.warn("Entra en programa de MiConexionDB");
//	entraPrograma = true;
//	
//    }
    //Metodo insertalconsumostest de ConexionDB
    //Metodo insertalconsumostest de HiloAmperimetro y HiloAmperimetroGhost
    public void insertalconsumostest(String valvula, float caudal,
	    int intensidad){
	logger.warn("Entra en insertalConsumosTest de MiConexionDB");
	insertalConsumo = true;
    }
    //Metodo insertarconsumostest de ConexionDB
    //Metodo insertarconsumostest de HiloAmperimetro y HiloAmperimetroGhost
    public void insertarconsumostest(String valvula, float caudal,
	    int intensidad) {
	logger.warn("Entra en insertarConsumoTest de MiConexionDB");
	insertarConsumo = true;
    }
    //Metodo sobrescribelconsumostest de ConexionDB
    //Metodo sobrescribelconsumostest de HiloAmperimetro y HiloAmperimetroGhost
    public void sobrescribelconsumostest(String valvula, float caudal,
	    int intensidad) {
	logger.warn("Entra en sobrescribeLConsumoTest de MiConexionDB");
	escribeLConsumo = true;
    }
    //Metodo sobrescriberconsumostest de ConexionDB
    //Metodo sobrescriberconsumostest de HiloAmperimetro y HiloAmperimetroGhost
    public void sobrescriberconsumostest(String valvula, float caudal,
	    int intensidad) {
	logger.warn("Entra en sobrescribeRConsumoTest de MiConexionDB");
	escribeRConsumo = true;
    }
    //Metodo "run()" de HiloPluviometro.
    public void insertaregviento(String codsens, Double vel){
	logger.warn("Entra en insertarRegViento de MiConexionDB");
	insertViento = true;
	
    }
    //Metodo "run()" de HiloTemperatura.
    public void insertaregtemp(String codsens, Double temp, String uni){
	logger.warn("Entra en insertarRegTemp de MiConexionDB");
	insertarTemp = true;
    }
    
    public void acualizaestvalv(String codelec, int estado){
	logger.warn("Entra en actualizaestadovalvula de MiConexionDB");
	actualizaValv = true;
    }
    //Metodo "eslahora()" de HiloPrograma
    public void actualizatareaprogpasadas(TareaProg tareaprog,boolean todasahechas){
	logger.warn("Entra en actuliza tareas Prog pasadas de MiConexionDB");
	actualizadaTarea = true;
    }

}
