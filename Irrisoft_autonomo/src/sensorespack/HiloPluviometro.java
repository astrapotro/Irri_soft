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

public class HiloPluviometro extends Sensor implements Runnable,
	PropertyChangeListener {

    private static Logger logger = LogManager.getLogger(HiloPluviometro.class
	    .getName());

    private SerialDriver serialcon;
    private Sensor sens;
    protected byte[] bufferResp = new byte[6];
    protected int[] bufferrespint = new int[6];
    byte[] churro = new byte[6];
    private Irrisoft IR;
    public ConexionDB conDB = new ConexionDB();
    private boolean rearmarPlu = false;
    private boolean r = true;
    Calendar cal;
    //private long tiempoini;

    // Creo la lista de lectura del sensor
    LecturasSensor lectura = new LecturasSensor();
    ArrayList<LecturasSensor> arrayLecturas = new ArrayList<LecturasSensor>();

    public HiloPluviometro(SerialDriver serial, Sensor sens) {

	this.serialcon = serial;
	this.sens = sens;
	this.IR = Irrisoft.window;
    }

    /**
     * Calculo la lluvia que hay.La guardo en las lecturas y lo guardo en la
     * BBDD Remota (GIS).
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {

	//DecimalFormat df = new DecimalFormat("#.##");
	//long tiempoini;

	while (true) {

	    if (rearmarPlu) {
		logger.warn("Termino el hilo de Pluviometro porque se ha rearmado Irrisoft");
		return;
	    }

	    //tiempoini = (System.nanoTime());
	    // Cojo los datos a enviar por el puerto serie.
	    churro = datosInicio();
	    // Duermo el tiempo requerido
	    logger.info("La frecuencia de lectura del Pluviometro es: "
		    + sens.getFrec_lect());

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
		    // serialcon.sueltasemaforo(sens.getNum_placa());
		    // RECONECTO
		    // serialcon.purga_puerto(sens.getNum_placa(),
		    // serialcon.serialPort.getPortName());
		}

		//Recibo la lectura por el puerto serie.
		bufferResp = serialcon.serialPort.readBytes(6, 4000);
		//Trato la lectura para tener mi dato.
		lectura = sacarLecturas(bufferResp);

		// Escribo en el panel la lectura
		// ponelecturas(sens);

		// Solo mando cuando esta lloviendo
		if (lectura.getLluvia() >= 1) {
		    // Creo la lectura a enviar a GIS.
		    LecturasSensor lecturaFinal = cogerLectura(lectura);
		    // Envio la lectura a GIS.
		    enviarLecturasGIS(lecturaFinal);
		}

	    } catch (SerialPortTimeoutException | SerialPortException e) {
		if (logger.isErrorEnabled()) {
		    if (e instanceof SerialPortTimeoutException)
			logger.error("TIMEOUUUUUT en la lectura del buffer serie: "
				+ e.getMessage());
		    else if (e instanceof SerialPortException)
			logger.error("Error de Puerto serie: " + e.getMessage());
		}

		serialcon.sueltasemaforo(sens.getNum_placa());
	    }
	    serialcon.sueltasemaforo(sens.getNum_placa());
	}

    }

    private LecturasSensor sacarLecturas(byte[] bufferresp2) {
	// Le quito el signo alos bytes
	for (int j = 0; j < bufferrespint.length; j++) {

	    bufferrespint[j] = bufferresp2[j] & 0xFF;

	}

	int medicion = ((bufferrespint[4] * 65536) + (bufferrespint[3] * 256))
		+ bufferrespint[2];

	if (logger.isInfoEnabled()) {
	    logger.info("Comando_Pluviometro: " + bufferrespint[0]);
	    logger.info("Borna: " + bufferrespint[1]);
	    logger.info("Parametro1: " + bufferrespint[2]);
	    logger.info("PArametro2: " + bufferrespint[3]);
	    logger.info("Parametro3: " + bufferrespint[4]);
	    logger.info("Checksum: " + bufferrespint[5]);
	}

	lectura.setLluvia(medicion * sens.getK());

	logger.warn("Valor de Pluviometro: " + lectura.getLluvia());

	// Meto las lecturas al sensor
	sens.setPulsos(medicion);
	sens.setLectura(lectura.getLluvia());

	return lectura;
    }

    /**
     * Envio la lectura de lluvia a GIS.
     * 
     * @param lecturaFinal
     */
    private void enviarLecturasGIS(LecturasSensor lecturaFinal) {
	// Valido la conectividad con la BBDD Remota.
	r = ValidarConexion();
	if (r == false) {
	    logger.warn("Lluvia encolada: Valor: " + lecturaFinal.getLluvia()
		    + " con Fecha: " + lecturaFinal.getFecha());
	    // No hay conectividad y guardo la lectura en memoria.
	    arrayLecturas.add(lectura);
	    // No hay conectividad y guardo la lectura en BBDD Local.
	    conDB.insertarLecturasBBDD(lecturaFinal);
	} else {
	    // Escribo la cata en GIS
	    if (logger.isInfoEnabled()) {
		logger.info("Se va a escribir un nuevo registro de pluviometría en la pasarela");
	    }
	    IR.hiloescucha.connDB.insertareglluvia(
		    lecturaFinal.getNombreSensor(), lecturaFinal.getLluvia(),
		    lecturaFinal.getFecha());
	}

    }

    /**
     * Creo la lectura a enviar a GIS.
     * 
     * @param lect
     * @return
     */
    private LecturasSensor cogerLectura(LecturasSensor lect) {
	cal = Calendar.getInstance();
	java.sql.Timestamp datelluvia = new java.sql.Timestamp(
		cal.getTimeInMillis());

	LecturasSensor lectLluvia = new LecturasSensor();
	lectLluvia.setNombreSensor(sens.getNum_sensor());
	lectLluvia.setFecha(datelluvia);
	lectLluvia.setRiego(0);
	lectLluvia.setVelocidadAnemometro(0);
	lectLluvia.setLluvia(lect.getLluvia());
	lectLluvia.setTemperatura(0);
	lectLluvia.setIntru(false);
	return lectLluvia;
    }

    /**
     * Cojo los datos preguntar por el puerto serie.
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

	// Duermno para que le de tiempo a generar el puerto ¿Revisar?
	try {
	    Thread.sleep(IrrisoftConstantes.DELAY_SENSOR_RUN);
	} catch (InterruptedException e1) {
	    if (logger.isErrorEnabled()) {
		logger.error("Hilo interrumpido: " + e1.getMessage());
	    }
	}

	if (logger.isInfoEnabled()) {
	    logger.info("Churro_Pluviometro = " + comando + ", " + borna + ", "
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

    private void dormirSensor() {
	try {
	    Thread.sleep(sens.getFrec_lect()
		    * IrrisoftConstantes.DELAY_FREC_LECT);
	} catch (InterruptedException e1) {
	    if (logger.isErrorEnabled()) {
		logger.error("Hilo interrumpido: " + e1.getMessage());
	    }
	}
    }

    /**
     * Inserto lecturas de lluvia no enviadas por falta de conectividad o apagon
     * de luz.
     * 
     * @return
     */
    public synchronized boolean insertarLluviaNoEnviada() {
	boolean r = true;

	if (arrayLecturas.isEmpty()) {
	    // Envio lecturas de lluvia desde la BBDD Local.
	    conDB.enviarRegSensoresBBDD();
	}
	// Envio lecturas de lluvia desde memoria.
	Iterator<LecturasSensor> itea = arrayLecturas.iterator();
	while (itea.hasNext()) {
	    LecturasSensor lec = itea.next();

	    r = IR.hiloescucha.connDB.insertareglluvia(lec.getNombreSensor(),
		    lec.getLluvia(), lec.getFecha());

	    if (r == true) {
		// Una vez enviada la lectura, se borra de la BBDD Local.
		conDB.borrarRegistrosBBDD(lec);
		// Una vez enviada la lectura, se borra de memoria.
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
	    this.rearmarPlu = true;
	}
	// Listener para controlar la conexion Remota
	else if ("true".contains(nombreCampo)) {
	    insertarLluviaNoEnviada();
	}

    }

}
