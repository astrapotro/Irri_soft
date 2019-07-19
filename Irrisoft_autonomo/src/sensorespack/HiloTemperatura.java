package sensorespack;

import irrisoftpack.ConexionDB;
import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;
import irrisoftpack.SerialDriver;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import alertaspack.GestorAlertas;

public class HiloTemperatura extends Sensor implements Runnable,
	PropertyChangeListener {

    private static Logger logger = LogManager.getLogger(HiloTemperatura.class
	    .getName());
    private SerialDriver serialcon;
    private Sensor sens;

    int[] buffer = new int[6];
    byte[] bufferResp = new byte[6];
    byte[] churro = new byte[6];

    private Irrisoft IR;
    private boolean rearmarTem = false;
    private boolean r = true;
    Calendar cal;
    public ConexionDB conDB = new ConexionDB();
    private long tiempoini;

    // Creo la lista de lectura del sensor
    LecturasSensor lectura = new LecturasSensor();
    ArrayList<LecturasSensor> arrayLecturas = new ArrayList<LecturasSensor>();

    public HiloTemperatura(SerialDriver serial, Sensor sens) {

	this.serialcon = serial;
	this.sens = sens;
	this.IR = Irrisoft.window;
    }

    /**
     * Este es el Hilo principal de Sensor de Temperatura.
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {

	//DecimalFormat df = new DecimalFormat("#.##");
	int frecuencia = 0;

	while (true) {

	    if (rearmarTem) {
		logger.warn("Termino el hilo de Temperatura porque se ha rearmado Irrisoft");
		return;
	    }

	    tiempoini = (System.nanoTime());
	    // Cojo los datos a enviar al puerto serie.
	    churro = datosInicio();

	    logger.info("La frecuencia de lectura Temperatura es: "
		    + sens.getFrec_lect());
	    // Duermo para recibir otra lectura.
	    dormirSensor();

	    // Escribo al puerto serie y recibo el churro de bytes y trato los
	    // datos.

	    try {
		serialcon.cogesemaforo(sens.getNum_placa());
		// Purgo el puerto
		serialcon.serialPort.purgePort(SerialPort.PURGE_RXCLEAR
			| SerialPort.PURGE_TXCLEAR);
		
		if (serialcon.serialPort.writeBytes(churro)) {
		    if (logger.isInfoEnabled()) {
			logger.info("Comando mandado al puerto serie !"
				+ Arrays.toString(churro));
		    }
		} else {
		    if (logger.isErrorEnabled()) {
			logger.error("Fallo en mandar comando al puerto serie! "
				+ Arrays.toString(churro));
		    }
		    // serialcon.sueltasemaforo(sens.getNum_placa());
		    // RECONECTO
		    serialcon.purga_puerto(sens.getNum_placa(),
			    serialcon.serialPort.getPortName());
		}
		
		
		
		bufferResp = serialcon.serialPort.readBytes(6, 4000);

		lectura = sacarLecturas(bufferResp);

		// Escribo en el panel la lectura
		// ponelecturas(sens);

		frecuencia = (int) (frecuencia + ((System.nanoTime() - tiempoini) / Math
			.pow(10, 9)));
		if (logger.isWarnEnabled()) {
		    logger.warn("Tiempo pasado bucle hilotemp: " + frecuencia);
		}
		
		// Miro si ha pasado una hora y si ha pasado mando la lectura a
		// GIS
		if (frecuencia >= sens.getFrec_env()) {

		    // Creo la lectura de temperatura para enviar.
		    LecturasSensor lecturaFinal = cogerLectura(lectura);
		    //Envio la lectura de temperatura a GIS.
		    enviarLecturasGIS(lecturaFinal);
		    //Ponemos la frecuencia a cero.
		    frecuencia = 0;
		}

	    } catch (SerialPortException | SerialPortTimeoutException e) {
		if (logger.isErrorEnabled()) {
		    if (e instanceof SerialPortTimeoutException) {
			logger.error("TIMEOUUUUUT en la lectura del buffer serie: "
				+ e.getMessage());
			IR.escribetextPane(
				"\nTIMEOUUUUUT en la lectura del buffer serie temperatura",
				IR.normal, false);

		    } else if (e instanceof SerialPortException)
			logger.error("Error de Puerto Serie: " + e.getMessage());
		}
		serialcon.sueltasemaforo(sens.getNum_placa());
	    }
	    serialcon.sueltasemaforo(sens.getNum_placa());

	}

    }

    /**
     * Trato las lecturas para tener mis datos.
     * @param churro2
     * @return
     */
    private LecturasSensor sacarLecturas(byte[] churro2) {
	// Le quito el signo alos bytes
	for (int j = 0; j < buffer.length; j++) {
	    buffer[j] = churro2[j] & 0xFF;
	}

	int medicion = (buffer[3] * 256) + buffer[2];
	if (logger.isInfoEnabled()) {

	    logger.info("Comando_Temperatura: " + buffer[0]);
	    logger.info("Borna: " + buffer[1]);
	    logger.info("Parametro1: " + buffer[2]);
	    logger.info("PArametro2: " + buffer[3]);
	    logger.info("Parametro3: " + buffer[4]);
	    logger.info("Checksum: " + buffer[5]);
	}

	// Voltaje de referencia tiene que ir al sensor, ahora a mano
	// 4.998
	// && sens.getVoltaje()
	double voltaje = ((double) medicion / 1023) * 4.998;
	// * IR.voltaje_ref;

	// Pasar el voltaje a la medida
	lectura.setTemperatura(((sens.getRang_med_max() - sens
		.getRang_med_min()) / (sens.getRang_sal_max() - sens
		.getRang_sal_min()))
		* (voltaje - sens.getRang_sal_min())
		+ sens.getRang_med_min());

	if (logger.isInfoEnabled()) {
	    logger.info("Total que la medicion es: " + medicion);
	    logger.info("Voltaje recibido: " + voltaje);
	    logger.info("La temperatura actual es de: "
		    + lectura.getTemperatura() + " ºC");
	}

	voltaje = Math.rint(voltaje * 100) / 100;
	lectura.setTemperatura(Math.rint(lectura.getTemperatura() * 10) / 10);

	logger.warn("Valor de Temperatura: " + lectura.getTemperatura());

	// Meto las lectura al sensor para el listener de los paneles y
	// la
	// grafica
	sens.setVoltaje(voltaje);
	sens.setLectura(lectura.getTemperatura());
	// sens.setLectura(Double.toString(medida));
	
	return lectura;
	
    }

    /**
     * Cojo los datos a enviar por el puerto serie.
     */
    private byte[] datosInicio() {

	byte comando = 1;
	byte borna = (byte) sens.getNum_borna();
	byte parametro1 = 0;
	byte parametro2 = 0;
	byte parametro3 = 0;
	byte checksum = (byte) (comando + borna + parametro1 + parametro2 + parametro3);

	// Duermno para que le de tiempo a generar el puerto ¿Revisar?
	try {
	    Thread.sleep(IrrisoftConstantes.DELAY_SENSOR_RUN);
	} catch (InterruptedException e1) {
	    if (logger.isErrorEnabled()) {
		logger.error("Hilo interrumpido: " + e1.getMessage());
	    }
	}

	if (logger.isInfoEnabled()) {
	    logger.info("Churro_Temperatura = " + comando + ", " + borna + ", "
		    + parametro1 + ", " + parametro2 + ", " + parametro3 + ", "
		    + checksum);
	}

	churro[0] = comando;
	churro[1] = borna;
	churro[2] = parametro1;
	churro[3] = parametro2;
	churro[4] = parametro3;
	churro[5] = checksum;

	if (!serialcon.serialPort.isOpened()) {
	    SerialPort serialPort = new SerialPort(
		    serialcon.serialPort.getPortName());
	    serialcon.setSerialPort(serialPort);
	    serialcon.conectaserial(sens.getNum_placa());
	}
	return churro;

    }

    /**
     * Duermo el sensor un tiempo (frec_lect)
     */
    private void dormirSensor() {
	try {
	    Thread.sleep(sens.getFrec_lect()* IrrisoftConstantes.DELAY_FREC_LECT);
	} catch (InterruptedException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Hilo interrumpido: " + e.getMessage());
	    }
	}
    }

    /**
     * Envio la lectura a GIS:
     * 
     * @param lecturaFinal
     */
    private void enviarLecturasGIS(LecturasSensor lecturaFinal) {
	// Valido si hay conexion para enviar la lectura a GIS.
	r = ValidarConexion();
	if (r == false) {
	    logger.warn("Temperatura encolada: Valor: "
		    + lecturaFinal.getTemperatura() + " Fecha: "
		    + lecturaFinal.getFecha());
	    // No hay conexion remota y guardo la lectura en memoria.
	    arrayLecturas.add(lecturaFinal);
	    // No hay conexion remota y guardo la lectura en BBDD Local.
	    conDB.insertarLecturasBBDD(lecturaFinal);

	} else {

	    if (logger.isInfoEnabled()) {
		logger.info("Se va a escribir un nuevo registro de temperatura en la pasarela");
	    }
	    // Introduzco una lectura de temperatura en GIS.
	    
	    IR.hiloescucha.connDB.insertaregtemp(
		    lecturaFinal.getNombreSensor(),
		    lecturaFinal.getTemperatura(),
		    lecturaFinal.getFecha());

	}

    }

    /**
     * Creo la lectura de temperatura a enviar a GIS.
     * 
     * @return
     */
    private LecturasSensor cogerLectura(LecturasSensor lect) {
	cal = Calendar.getInstance();
	java.sql.Timestamp datetemperatura = new java.sql.Timestamp(
		cal.getTimeInMillis());

	LecturasSensor lectTemperatura = new LecturasSensor();
	lectTemperatura.setNombreSensor(sens.getNum_sensor());
	lectTemperatura.setFecha(datetemperatura);
	lectTemperatura.setRiego(0);
	lectTemperatura.setVelocidadAnemometro(0);
	lectTemperatura.setLluvia(0);
	lectTemperatura.setTemperatura(lect.getTemperatura());
	lectTemperatura.setUnidad_Med(sens.getUni_med().substring(1, 1));

	return lectTemperatura;
    }

    /**
     * Envio las lecturas de temperatura no enviadas, por falta de conectividad
     * y apagon de luz.
     * 
     * @return
     */
    public synchronized boolean insertarTemperaturaNoEnviadas() {
	boolean r = true;

	if (arrayLecturas.isEmpty()) {
	    // Envio lecturas de temperatura desde BBDD Local.
	    conDB.enviarRegSensoresBBDD();
	}
	// Envio lecturas de temperatura desde memoria.
	Iterator<LecturasSensor> itea = arrayLecturas.iterator();
	while (itea.hasNext()) {

	    LecturasSensor lec = itea.next();

	    r = IR.hiloescucha.connDB.insertaregtemp(lec.getNombreSensor(),
		    lec.getTemperatura(), lec.getFecha());
	    if (r == true) {
		// Una vez enviada la lectura, la borro de la BBDD Local.
		conDB.borrarRegistrosBBDD(lec);
		// Una vez enviada la lectura, la borro de memoria.
		itea.remove();
	    }
	}
	return r;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
	String nombreCampo = evt.getPropertyName();

	// Listener para sincronizar el programador.
	if ("false".contains(nombreCampo)) {
	    this.rearmarTem = true;
	}
	// Listener para controlar la conexion Remota.
	else if ("true".contains(nombreCampo)) {
	    insertarTemperaturaNoEnviadas();
	}
    }

}
