package volcadopack;

import java.sql.SQLException;

import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import alertaspack.GestorAlertas;

public class Volcado implements Runnable {

    public Volcado() {
	super();
	setCon(null);
    }

    // ////////////////////////////////////////
    // LA diferencia con el volcado normal es que ésta mantiene la conexión con
    // la remota ABIERTA para mejorar el consumo
    // de ancho de banda !!!
    //
    // HAce falta una tabla nueva en la pasarela que asocie programaciones con
    // cuotas y COPIAR su contenido a la local cada minuto

    private static Logger logger = LogManager
	    .getLogger(Volcado.class.getName());
    public ConexionVolc con;

    private boolean terminar = false;

    @Override
    public void run() {

	// Duermo para que de tiempo a sakis3g a levantar la ppp0
	try {
	    Thread.sleep(IrrisoftConstantes.DELAY_LEVANTAR_PROGRAMA_10SEG);
	} catch (InterruptedException e1) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error: Hilo interrumpido: " +e1.getMessage());
	    }
	}

	while (terminar != true) {

	    // Conecto con las BBDD
	    con.conectar();
	    con.conectal();

	    compruebacon();

	    // Compruebo que la conexión de red está viva
	    try {

		if (con.conectadol == true && con.conectador == true) {

		    // Pongo el texto del botón como tiene que estar
		    if (Irrisoft.window.btnEmpezar.getText().contains(
			    "Arrancar")) {

			Irrisoft.window.btnEmpezar.setText("Parar BBDDs");
		    }

		    con.tarea();// Compruebo si hay tareas y programaciones
				// pendientes !

		    compruebacon();
		}
	    } catch (Exception e) {
		if (logger.isErrorEnabled()) {
		    logger.error(e.getMessage());
		}
	    }

	    try {
		Thread.sleep(con.config.getTiempo() * IrrisoftConstantes.DELAY_FREC_LECT); // Aki el proceso
							     // duerme lo que
							     // diga el archivo
							     // de configuración

	    } catch (InterruptedException e) {
		if (logger.isErrorEnabled()) {
		    logger.error(e.getMessage());
		}
	    }

	    if (logger.isInfoEnabled()) {
		logger.info("VOLCADO pasado, " + con.conectador);
		logger.info("LOCAL pasado, " + con.conectadol);
	    }
	}
    }

    /**
     * Comprueba la conexion con la BBDD Remota y Local.
     */
    private void compruebacon() {

	try {

	    if (con.connl != null && !con.connl.isValid(3)) {
		con.cierral();
		con.conectadol = false;
	    }

	    if (con.connr != null && !con.connr.isValid(3)) {
		con.cierrar();
		con.conectador = false;
	    }

	} catch (SQLException e) {
	    if(logger.isErrorEnabled()){
		logger.error(e.getMessage());
	    }
	}

    }

    // METODOS GETTER Y SETTER
    public ConexionVolc getCon() {
	return con;
    }

    public void setCon(ConexionVolc con) {
	if (con == null) {
	    this.con = new ConexionVolc();
	} else
	    this.con = con;
	if (logger.isInfoEnabled()) {
	    logger.info("Se ha creado una nueva conexión !!!!");
	}
    }

    public boolean isTerminar() {
	return terminar;
    }

    public void setTerminar(boolean terminar) {
	this.terminar = terminar;
    }

}
