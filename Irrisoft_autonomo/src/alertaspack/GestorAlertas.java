package alertaspack;

import irrisoftpack.ConexionDB;
import irrisoftpack.Irrisoft;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GestorAlertas implements PropertyChangeListener {

    private static Logger logger = LogManager.getLogger(GestorAlertas.class
	    .getName());
    private static ArrayList<Alerta> alertas = new ArrayList<Alerta>();
    private static GestorAlertas instance;
    public ConexionDB conDB = new ConexionDB();

    private GestorAlertas() {
	super();

    }

    /**
     * Patrón Singleton
     * 
     * @return Gestor de Alertas
     */
    public static GestorAlertas getInstance() {
	if (instance == null) {
	    return new GestorAlertas();
	}
	return instance;
    }

    /**
     * Inserta las Alertas en GIS. Si no hay conectividad con la BBDD Remota, 
     * guarda la alerta en memoria y BBDD Local. 
     * Cuando tenga conectividad la envía a la BBDD Remota.
     * @param codAlerta
     */
    public synchronized boolean insertarAlarma(int codAlerta) {

	Calendar cal = Calendar.getInstance();
	java.sql.Timestamp dateAlerta = new java.sql.Timestamp(
		cal.getTimeInMillis());

	Alerta alerAux = new Alerta();
	alerAux.setCodAlerta(codAlerta);
	String descripcion = alerAux.toString();
	alerAux.setFechaAlerta(dateAlerta);
	alerAux.setDescripcion(descripcion);

	boolean r = true;
	//Compruebo si hay salida a Internet.
	r = validar();

	if (r == false) {
	    logger.warn("Alerta " + codAlerta + " encolada: " + descripcion
		    + " Fecha: " + dateAlerta);
	    //Si no hay salida a Internet, guardo la alerta en memoria.
	    alertas.add(alerAux);
	    //Si no hay salida a Internet, guardo la alerta en BBDD Local.
	    conDB.insertarAlertasBBDD(alerAux);
	    return r;
	}
	r = conDB.insertarAlertaGIS(alerAux);
	return r;
    }

    /**
     * Valida si hay conectividad con la BBDD Remota (GIS),
     * para enviar la alerta.
     * 
     * @return True si conectada, False si no.
     */
    public static synchronized boolean validar() {
	boolean r = true;

	if (Irrisoft.window.volcado.getCon().getConnr() == null) {
	    r = false;
	} else if (Irrisoft.window.volcado.getCon().conectador == false) {
	    r = false;
	}
	return r;
    }

    /**
     * Envía las alarmas temporalmente guardadas en memoria a la BBDD Remota (GIS).
     * Si no puede enviarlas, estas se mantienen en memoria.
     * 
     * @return True si se pudieron enviar. False en caso que no pudiera.
     */
    public synchronized boolean insertaAlarmaNoEnviadas() {
	boolean r = true;
	if (alertas.isEmpty()) {
	    //Si se ha ido la luz y tengo alerta
	    //en la BBDD Local las envio a GIS.
	    conDB.cargarBBDDalertas();
	}
	Iterator<Alerta> itea = alertas.iterator();

	while (itea.hasNext()) {
	    Alerta aux = itea.next();
	    
	    r = conDB.insertarAlertaGIS(aux);
	    if (r == true){
		//Borro la alerta de la BBDD Local.
		conDB.borrarAlertaBBDD(aux);
		//Elimino la alerta insertada de memoria.
		itea.remove();
		
	    }
	    } 
	return r;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
	String nombreCampo = evt.getPropertyName();
	//Cuando el estado de la Conexion Remota es true.
	if("true".contains(nombreCampo)){
	    insertaAlarmaNoEnviadas();
	}
	
    }

}
