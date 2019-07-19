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

public class HiloAnemometro extends Sensor implements Runnable,
	PropertyChangeListener {

    private static Logger logger = LogManager.getLogger(HiloAnemometro.class
	    .getName());

    private SerialDriver serialcon;
    private Sensor sens;

    protected byte[] bufferResp = new byte[6];
    protected int[] bufferrespint = new int[6];
    byte[] churro = new byte[6];

    private Irrisoft IR;
    private boolean rearmarAne = false;
    private boolean r = true;
    Calendar cal;
    ConexionDB conDB = new ConexionDB();
    private long tiempoini;

    // Creo la lista de lectura del sensor
    LecturasSensor lectura = new LecturasSensor();
    ArrayList<LecturasSensor> arrayLecturas = new ArrayList<LecturasSensor>();

    public HiloAnemometro(SerialDriver serial, Sensor sens) {
	this.serialcon = serial;
	this.sens = sens;
	this.IR = Irrisoft.window;
    }

    /**
     * Calculo la velocidad del viento. Lo guardo en lecturas y en la BBDD
     * Remota (GIS).
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {

	// DecimalFormat df = new DecimalFormat("#.###");
	int frecuencia = 0;

	while (true) {

	    if (rearmarAne) {
		logger.warn("Termino el hilo de Anemometro porque se ha rearmado Irrisoft");
		return;
	    }
	    tiempoini = (System.nanoTime());
	    // Cojo los datos a enviar por el puerto serie.
	    churro = datosInicio();

	    // Duermo el tiempo requerido
	    logger.info("La freceuncia de lectura de Anemometro es: "
		    + sens.getFrec_lect());

	    // Duermo el sensor el un tiempo, para recibir otra lectura.
	    dormirSensor();

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
		    // RECONECTO
		    // serialcon.purga_puerto(sens.getNum_placa(),
		    // serialcon.serialPort.getPortName());
		}

		// Leo la respuesta
		bufferResp = serialcon.serialPort.readBytes(6, 4000);
		//Trato las lecturas para tener el dato.
		lectura = sacarLecturas(bufferResp);
		
		// Escribo en el panel la lectura
		// ponelecturas(sens);

		// Irrisoft.window.panelecturasens.getLblectane().setText(medicion+" pulsos , velocidad "+velocidad+
		// " m/s");
		frecuencia = (int) (frecuencia + ((System.nanoTime() - tiempoini) / Math
			.pow(10, 9)));
		if (logger.isWarnEnabled()) {
		    logger.warn("Tiempo pasado bucle hiloAnemometro: "
			    + frecuencia);
		}

		// Insertar registro en la pasarela
		if (frecuencia >= sens.getFrec_env()) {
		    // Creo la lectura para enviar a GIS.
		    LecturasSensor lecturaFinal = cogerLectura(lectura);
		    // Envio la lectura a GIS.
		    enviarLecturasGIS(lecturaFinal);

		    frecuencia = 0;
		}

	    } catch (SerialPortTimeoutException | SerialPortException e) {
		if (logger.isErrorEnabled()) {
		    if (e instanceof SerialPortTimeoutException)
			logger.error("TIMEOUUUUUT en la lectura del buffer serie: "
				+ e.getMessage());
		    else if (e instanceof SerialPortException)
			logger.error(e.getMessage());
		}
		serialcon.sueltasemaforo(sens.getNum_placa());
	    }
	    serialcon.sueltasemaforo(sens.getNum_placa());
	}

    }

    /**
     * Trato las lecturas para tener mis datos.
     * @param bufferResp2
     * @return
     */
    private LecturasSensor sacarLecturas(byte[] bufferResp2) {
	// Le quito el signo a los bytes.
	for (int j = 0; j < bufferrespint.length; j++) {

	    bufferrespint[j] = bufferResp2[j] & 0xFF;

	}

	int medicion = ((bufferrespint[4] * 65536) + (bufferrespint[3] * 256))
		+ bufferrespint[2];

	if (logger.isInfoEnabled()) {
	    logger.info("Comando_Anemometro: " + bufferrespint[0]);
	    logger.info("Borna: " + bufferrespint[1]);
	    logger.info("Parametro1: " + bufferrespint[2]);
	    logger.info("PArametro2: " + bufferrespint[3]);
	    logger.info("Parametro3: " + bufferrespint[4]);
	    logger.info("Checksum: " + bufferrespint[5]);
	}

	lectura.setVelocidadAnemometro((((((sens.getRang_med_max() - sens
		.getRang_med_min()) / (sens.getRang_sal_max() - sens
		.getRang_sal_min())) * (medicion - sens.getRang_sal_min())) + sens
		.getRang_med_min()) / ((System.nanoTime() - tiempoini) / Math
		.pow(10, 9))));

	// Le pongo sólo tres decimales
	lectura.setVelocidadAnemometro(Math.rint(lectura
		.getVelocidadAnemometro() * 1000) / 1000);

	logger.warn("Valor del Anemometro: " + lectura.getVelocidadAnemometro());
	// Meto las lecturas al sensor
	sens.setPulsos(medicion);
	sens.setLectura(lectura.getVelocidadAnemometro());

	return lectura;
    }

    /**
     * Cojo los datos iniciales para preguntar al puerto serie.
     * 
     * @return
     */
    private byte[] datosInicio() {
	byte comando = 1;
	byte borna = (byte) sens.getNum_borna();
	byte parametro1 = 0;
	byte parametro2 = 0;
	byte parametro3 = 0;
	byte checksum = (byte) (comando + borna + parametro1 + parametro2 + parametro3);

	// Duermo para que le de tiempo a generar el puerto
	try {
	    Thread.sleep(IrrisoftConstantes.DELAY_SENSOR_RUN);
	} catch (InterruptedException e1) {
	    if (logger.isErrorEnabled()) {
		logger.error("Hilo interrumpido: " + e1.getMessage());
	    }
	}

	if (logger.isInfoEnabled()) {
	    logger.info("Churro_Anemometro = " + comando + ", " + borna + ", "
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
	    Thread.sleep(sens.getFrec_lect()
		    * IrrisoftConstantes.DELAY_FREC_LECT);
	} catch (InterruptedException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Hilo interrumpido: " + e.getMessage());
	    }
	}
    }

    /**
     * Envio las lecturas de lluvia a GIS.
     * 
     * @param lecturaFinal
     */
    private void enviarLecturasGIS(LecturasSensor lecturaFinal) {
	// Valido si hay conexion con la BBDD Remota.
	r = sens.ValidarConexion();
	if (r == false) {
	    logger.warn("Viento encolado: Valor: "
		    + lecturaFinal.getVelocidadAnemometro() + " con Fecha: "
		    + lecturaFinal.getFecha());
	    // No hay conexion y guardo la lectura en memoria.
	    arrayLecturas.add(lecturaFinal);
	    // No hay lectura y guardo la lectura en BBDD Local.
	    conDB.insertarLecturasBBDD(lecturaFinal);
	} else {

	    // Escribo la cata en GIS
	    if (logger.isInfoEnabled()) {
		logger.info("Se va a escribir un nuevo registro de velocidad de viento en la pasarela");
	    }

	    IR.hiloescucha.connDB.insertaregviento(
		    lecturaFinal.getNombreSensor(),
		    lecturaFinal.getVelocidadAnemometro(),
		    lecturaFinal.getFecha());
	}

    }

    /**
     * Creo la lectura de viento para enviar a GIS.
     * 
     * @param lect
     * @return
     */
    private LecturasSensor cogerLectura(LecturasSensor lect) {
	LecturasSensor lecViento = new LecturasSensor();
	cal = Calendar.getInstance();
	java.sql.Timestamp dateViento = new java.sql.Timestamp(
		cal.getTimeInMillis());
	lecViento.setNombreSensor(sens.getNum_sensor());
	lecViento.setFecha(dateViento);
	lecViento.setRiego(0);
	lecViento.setVelocidadAnemometro(lect.getVelocidadAnemometro());
	lecViento.setLluvia(0);
	lecViento.setTemperatura(0);
	lecViento.setIntru(false);
	return lecViento;
    }

    /**
     * Inserto las lecturas de viento no enviadas a GIS por falta de
     * conectividad o apagon de luz.
     * 
     * @return
     */
    public synchronized boolean insertarVelocidadNoEnviada() {
	boolean r = true;

	if (arrayLecturas.isEmpty()) {
	    // Envio lecturas de viento desde la BBDD Local.
	    conDB.enviarRegSensoresBBDD();
	}
	// Envio lecturas de viento desde memoria
	Iterator<LecturasSensor> itea = arrayLecturas.iterator();
	while (itea.hasNext()) {
	    LecturasSensor lec = itea.next();

	    r = IR.hiloescucha.connDB.insertaregviento(lec.getNombreSensor(),
		    lec.getVelocidadAnemometro(), lec.getFecha());

	    if (r == true) {
		// Una vez enviada la lectura, la borro de la BBDD Local.
		conDB.borrarRegistrosBBDD(lec);
		// Una vez enviada la lectura, la borro de memoria,
		itea.remove();
	    }
	}
	return r;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
	String nombreCampo = evt.getPropertyName();

	// Listener para sincronizar el Proramador
	if ("false".contains(nombreCampo)) {
	    this.rearmarAne = true;
	}
	// Listener para controlar la conexion Remota
	else if ("true".contains(nombreCampo)) {
	    insertarVelocidadNoEnviada();
	}

    }

}
