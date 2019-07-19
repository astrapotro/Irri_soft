package irrisoftpack;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HiloEscucha implements Runnable {

    private static Logger logger = LogManager.getLogger(HiloEscucha.class
	    .getName());

    // Conexion
    public ConexionDB connDB = new ConexionDB();
    protected Runtime r = Runtime.getRuntime();
    protected long memory;
    protected int abiertasmci, abiertasmci2, abiertasbt2, abiertasbt22;
    protected static SimpleDateFormat formatfecha = new SimpleDateFormat(
	    "dd-MM-yyyy HH:mm");

    private boolean terminar = false;
    public int vuelta = 1;

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run() LLamo a tarea y a programa de la BBDD
     *      local.
     */
    @Override
    public void run() {

	
	Irrisoft.window.abreDBhilo();

	compruebacon();

	// Para que le de tiempo a levantar el programa
	try {
	    Thread.sleep(IrrisoftConstantes.DELAY_LEVANTAR_PROGRAMA_10SEG);
	} catch (InterruptedException e1) {
	    if (logger.isErrorEnabled()) {
		logger.error(e1.getMessage());
	    }
	}

	
	//Inicializo a NO leídas todas las tareas que haya en la BBDDD local
	connDB.ponertareamanualeida(null, true);

	while (terminar != true) {

	    Irrisoft.window.lblfecha.setText(formatfecha.format(Calendar
		    .getInstance().getTime()));

	    compruebacon();

	    try {
		connDB.tarea();
		Thread.sleep(IrrisoftConstantes.DELAY_TAREA_PROGRAMASEG); 
		connDB.programa();
		if (logger.isInfoEnabled()) {
		    logger.info("Hiloescucha durmiendo");
		}
		// Miro las valvulas que tengo y no tengo abiertas

		if (logger.isWarnEnabled()) {
		    if (Irrisoft.window.valvsabiertastot.size() > 0)
			logger.warn("Valvulas abiertas:"
				+ Irrisoft.window.valvsabiertastot.size());

		}

		// Invoco al garbage collector para liberar memoria !!
		if (vuelta == 750) {
		    r.gc();
		    vuelta = 0;
		    if (logger.isWarnEnabled()) {
			logger.warn("Garbage collector pasado !");
		    }
		}

	    } catch (InterruptedException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Hilo interrumpido: " +e.getMessage());
		}
	    }
	    vuelta++;
	}
    }

    /**
     * Compruebo la conexion.
     */
    private void compruebacon() {

	try {

	    if (!connDB.conn.isValid(10)) {
		connDB.desconectal();
		connDB.conectado = false;
		connDB.conectal();

	    }

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	}

    }

    public ConexionDB getConnDB() {
	return connDB;
    }

    public void setConnDB(ConexionDB connDB) {
	this.connDB = connDB;
    }

    public void setTerminar(boolean terminar) {
	this.terminar = terminar;
    }

    public boolean getTerminar() {
	return terminar;
    }
}
