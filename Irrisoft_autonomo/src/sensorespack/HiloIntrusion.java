package sensorespack;

import irrisoftpack.ConexionDB;
import irrisoftpack.Irrisoft;
import irrisoftpack.SerialDriver;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HiloIntrusion extends Sensor implements Runnable, PropertyChangeListener{
    
    private static Logger logger = LogManager.getLogger(HiloIntrusion.class
	    .getName());
    private SerialDriver serialcon;
    private Sensor sens;
    private Irrisoft IR;
    private boolean rearmarIntru = false;
    public ConexionDB conDB = new ConexionDB();
    LecturasSensor lectura = new LecturasSensor();
    ArrayList<LecturasSensor> arrayLecturas = new ArrayList<LecturasSensor>();
    
    public HiloIntrusion(SerialDriver serial, Sensor sens){
	this.serialcon = serial;
	this.sens = sens;
	this.IR = Irrisoft.window;
    }

    public void run() {
	
	while(true){
	    if (rearmarIntru) {
		logger.warn("Termino el hilo de Intrusion porque se ha rearmado Irrisoft");
		return;
	    }
	}


    }
    
    public synchronized boolean insertarIntrusionNoEnviadas(){
	boolean r = true;
	
	if(arrayLecturas.isEmpty()){
	    conDB.enviarRegSensoresBBDD();
	}
	
	Iterator<LecturasSensor> itea = arrayLecturas.iterator();
	while(itea.hasNext()){
	    LecturasSensor lec = itea.next();
	    
	    r = IR.hiloescucha.connDB.insertarRegIntrusion(lec.getNombreSensor(), lec.isIntru(), lec.getFecha());
	    
	    if(r = false){
		//Una vez envidada la lectura, la borro de BBDD Local.
		conDB.borrarRegistrosBBDD(lec);
		//Una vez borrada de BBDD Local, la borro de memoria.
		itea.remove();
	    }
	}
	return r;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
	String nombreCampo = evt.getPropertyName();

	if ("false".contains(nombreCampo)) {
	    this.rearmarIntru = true;
	}else if("true".contains(nombreCampo)){
	    insertarIntrusionNoEnviadas();
	}
    }

}