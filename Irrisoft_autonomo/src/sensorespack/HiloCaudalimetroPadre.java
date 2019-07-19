package sensorespack;

import irrisoftpack.ConexionDB;
import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;
import irrisoftpack.SerialDriver;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
//import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import alertaspack.GestorAlertas;

import valvulaspack.Valvula;

public class HiloCaudalimetroPadre implements Runnable, PropertyChangeListener {


    private static Logger logger = LogManager.getLogger(HiloCaudalimetro.class
	    .getName());
    // Habría que cambiarlo para que fuera dinamico, ahora esta puesto para la
    // bt2
    protected SerialDriver serialcon;
    // protected String puerto;
    protected int tipoplaca, len, leo, i, repeticion = 0;
    protected int numplaca;
    protected int valvsabiertasant = 0, valvsabiertas = 0;
    protected int[] pulsos = new int[2];
    protected int pulso = 0;
    protected int nuevospulsos = 0;
    protected byte[] bufferresp = new byte[6];

    // TODO El byte 5 se ha de poner con sens.getnumborna para múltiples
    // caudalimetros!!!
    protected byte[] buftrans = { (byte) 0x06, (byte) 0x00, (byte) 0x03,
	    (byte) 0x00, (byte) 0x01, (byte) 0x0A };

    // Mascara para desechar el bit 7 de la respuesta de la bt2
    protected static byte mascara = (byte) 0b01111111;

    protected int vueltasens = 0;

    private int tiempoduerme = 0;
    private double lapsotiempo = 0;
    // public boolean test;
    // = IR.test;
    // private boolean regando = false;
    // private boolean mci = true;
    // public boolean inicial=false;
    public int pulsosparciales = 0;
    public int vuelta = 0;
    public int tipovalv;
    public int numvalv;
    private long tiempoini;
    public Valvula valv;
    private Sensor sens;

    private float caudal;

    public boolean primera = true;
    public boolean first = true;
    // private boolean pasado = false;

    public ConexionDB connDB = new ConexionDB();
    private int pulsosdiarios = 0;

    private GestorAlertas gestorAlertas;
    // private long tiempo_est_circuito = 60000;

    private Irrisoft IR;
    private boolean rearmarCau = false;
    Calendar cal;
    LecturasSensor lectura = new LecturasSensor();
    public static boolean flag;
    public Timestamp fechaRiego = null;
    // private boolean terminar;

    public boolean circuito_vacio = true;
    protected static SimpleDateFormat formatterfecha = new SimpleDateFormat(
	    "yyyy-MM-dd");
   

    protected DecimalFormat df = new DecimalFormat("#.###");

    public HiloCaudalimetroPadre(SerialDriver serial, int tipo, int tipovalv,
	    Sensor sens) {

	this.serialcon = serial;
	this.tipoplaca = tipo;
	this.tipovalv = tipovalv;
	this.sens = sens;
	gestorAlertas = GestorAlertas.getInstance();
	this.IR = Irrisoft.window;

    }

    public void run() {

	
	pulsos[0] = 0;
	pulsos[1] = 0;

	if (!serialcon.serialPort.isOpened()) {
	    SerialPort serialPort = new SerialPort(
		    serialcon.serialPort.getPortName());
	    serialcon.setSerialPort(serialPort);
	    serialcon.conectaserial(tipoplaca);
	}

	while (true) {

	    // Dejo que muera el hilo si irrisoft ha sido rearmado

	    if (rearmarCau) {
		logger.info("Termino el hilo de Caudalimetro porque se ha rearmado Irrisoft");
		return;
	    }

	    if (IR.test == false) {

		// while (IR.test != true) {
		automatico();
		// }
	    } else if (IR.test == true) {

		// // while (Irrisoft.window.test != true) {
		// automatico();
		// // }

		// Espero hasta que el hilo test me avise de que el circuito
		// está lleno
		if (circuito_vacio) {

		    System.out
			    .println("EN HILOTEST antes de esperar al hilotest");

		    // // aviso al hilotest
		    // synchronized (Irrisoft.window.paneltest.hilotest) {
		    // Irrisoft.window.paneltest.hilotest.notify();
		    // }

		    synchronized (IR.hilocau) {
			try {
			    IR.hilocau.wait();
			} catch (InterruptedException e) {
			    if (logger.isErrorEnabled()) {
				logger.error("Hilo Interrumpido: "
					+ e.getMessage());
			    }
			}
		    }

		    // Duermo 10 segs para que le de tiempo a hilotest a iniciar
		    // el encendido
		    try {
			Thread.sleep(IrrisoftConstantes.DELAY_SENSOR_10SEG);
		    } catch (InterruptedException e) {
			if (logger.isErrorEnabled()) {
			    logger.error("Hilo Interrumpido: " + e.getMessage());
			}
		    }

		    circuito_vacio = false;
		}

		test();

	    }
	}
    }

    /**
     * 
     */
    public void automatico() {

	if (IR.test == true)
	    return;

	try {

	    // Mirar valvulas abiertas
	    valvsabiertas = IR.valvsabiertas(0);
	    valvsabiertasant = valvsabiertas;

	    if (logger.isInfoEnabled()) {
		logger.info("Valvsabiertas: " + valvsabiertasant);
	    }

	    // ////////////// Si no hay ninguna abierta
	    if (valvsabiertas == 0) {

		// ?¿?¿?¿?
		vuelta = -1;
		pulsosparciales = 0;
		tiempoini = System.nanoTime();

		// TODO Tendría que ser periodo lectura de BBDD local (60
		tiempoduerme = 20;

		while (tiempoduerme > 0) {

		    Thread.sleep(IrrisoftConstantes.DELAY_SENSOR_10SEG);
		    tiempoduerme = tiempoduerme - 10;

		    // Si se va a hacer un test()
		    if (IR.test == true)
			return;

		    Thread.sleep(10000);
		    tiempoduerme = tiempoduerme - 10;

		    // Miro valvulas abiertas
		    valvsabiertas = IR.valvsabiertas(0);

		    // si hay alguna abierta salgo del bucle
		    if (valvsabiertas > 0)
			return;
		}

		if (tiempoduerme == 0) {

		    if (logger.isInfoEnabled()) {
			logger.info("Estoy en automatico sin valvs abiertas");
		    }

		    int pul = cuentapulsoslapso();

		    // calculacaudal();

		    if (pul >= 1 && valvsabiertas == 0 && !first
			    && nuevospulsos != 0) {
			// hay una fuga de agua ya que no hay valvulas
			// abiertas y estoy recibiendo pulsos

			pulso = pulso + pul;

			IR.escribetextPane("\n\tALARMA! FUGA de AGUA ",
				IR.negrita, false);
			IR.escribetextPane("en el sistema hidráulico",
				IR.normal, false);
			IR.escribetextPane(
				"\n\tValvulas abiertas=0 , consumo acomulado: "
					+ pulso
					* sens.getK()
					+ " ls\n\t\t\t\t"
					+ new Timestamp(Calendar.getInstance()
						.getTime().getTime()),
				IR.normal, false);

			logger.error("\n\tALARMA! FUGA de AGUA en el sistema hidráulico\n\tValvulas abiertas=0 , consumo acomulado: "
				+ pulso
				* sens.getK()
				+ " ls\n\t\t\t"
				+ new Timestamp(Calendar.getInstance()
					.getTime().getTime()));

			// Genero alarma: Fuga de agua en instalacion o fallo de
			// valvula maestra.
			gestorAlertas.insertarAlarma(1002);
			// calculacaudalauto();

		    } else if (pulsos[1] == 0) {
			logger.info("Ya no hay consumo de agua. OK");
			pulso = 0;

		    } else if (pul == 0) {

			logger.info("No hay pulsos !!");
		    }

		    first = false;

		}
		// vuelta=1;
	    }

	    // ////////////////////// Si hay alguna abierta
	    else if (valvsabiertas > 0) {
		// TODO Tendra que ser dinámico el periodo lectura de BBDD local
		// (65)
		tiempoduerme = 65;
		vuelta = -1;

		if (logger.isInfoEnabled()) {
		    logger.info("estoy en automatico con valvs abiertas");
		}

		while (tiempoduerme > 0) {

		    if (IR.test == true)
			return;

		    if (vuelta > -1) {
			Thread.sleep(IrrisoftConstantes.DELAY_SENSOR_5SEG);
			tiempoduerme = tiempoduerme - 5;
		    }

		    vuelta++;

		    if (logger.isInfoEnabled()) {
			logger.info("Vuelta en automatico con valvs abiertas: "
				+ vuelta);
		    }
		    // Miro valvulas abiertas
		    valvsabiertas = IR.valvsabiertas(0);

		    System.out.println(valvsabiertas + "Va , "
			    + valvsabiertasant + "Vaant");
		    //
		    if (valvsabiertas == valvsabiertasant) {

			// Si es la primera vuelta
			if (vuelta == 0) {
			    if (logger.isInfoEnabled()) {
				logger.info("VUELTA 0");
			    }
			    // Para que desestime los
			    // primeros 60 segundos -MODIFICABLE !!!! TODO
			    // CAMBIAAAAR
			    // Thread.sleep(tiempo_est_circuito);

			    cuentapulsoslapso();

			}

			// Si ha pasado un minuto desdela primera vuelta
			else if (vuelta == 13) {
			    if (logger.isInfoEnabled()) {
				logger.info("VUELTA 13");
			    }
			    cuentapulsoslapso();

			    if (pulsosparciales >= 25) {

				// Calcular el consumo
				calculacaudalauto();
			    } else {
				tiempoduerme = 60;
				continue;
			    }

			}

			// Si han pasado 2 minutos
			else if (vuelta == 25) {

			    if (logger.isInfoEnabled()) {
				logger.info("VUELTA 25");
			    }

			    cuentapulsoslapso();

			    if (pulsosparciales >= 3) {
				// Calcular el consumo
				calculacaudalauto();
			    } else {
				tiempoduerme = 60;
				continue;
			    }
			}
			// Si han pasado más de 2 minutos
			else if (vuelta > 25) {
			    if (logger.isInfoEnabled()) {
				logger.info("VUELTA > 25");
			    }
			    cuentapulsoslapso();

			    if (pulsosparciales >= 3) {
				// calcular el consumo
				calculacaudalauto();
			    } else {
				tiempoduerme = 5;
				continue;
			    }
			}

		    } else {

			// Tiempo de margen para que estabilice el agua
			Thread.sleep(IrrisoftConstantes.DELAY_EST_CIRCUITO);

			// Si se va a hacer un test()
			if (IR.test == true)
			    return;

			if (logger.isInfoEnabled()) {
			    logger.info("VALVSABIERTAS != VALVSABIERTASANT");
			}

			long i = IrrisoftConstantes.DELAY_EST_CIRCUITO;

			while (i >= 0) {
			    Thread.sleep(10000);
			    i = i - 10000;
			    // Si se va a hacer un test()
			    if (IR.test == true) {
				System.out
					.println("Se sale del bucle estabilizador circuito");
				return;
			    }
			}
			// Tiempo de margen para que estabilice el agua
			// Thread.sleep(tiempo_est_circuito);

			return;
		    }

		}

	    }

	} catch (InterruptedException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Han interrumpido el hilo: " + e.getMessage());
		e.printStackTrace();
	    }

	    IR.escribetextPane("\nError en la lectura de pulsos caudalimetro",
		    IR.normal, false);
	    return;
	}

    }

    /**
     * 
     */
    public void test() {

	// Me salgo si no hay que seguir haciendo el test
	if (IR.test == false)
	    return;

	logger.error("Vuelta de test :" + vuelta);

	if (primera) {

	    // Para que haga un retardo para estabilizar el caudal
	    // en la
	    // línea hidraúlica

	    try {
		int i = 0;
		IR.paneltest.lbltesteando.setText("Estabilizando, espere ...");
		IR.paneltest.progressBar.setValue(0);
		IR.paneltest.progressBar
			.setMaximum((int) IrrisoftConstantes.DELAY_EST_CIRCUITO / 1000);
		while (i < (int) IrrisoftConstantes.DELAY_EST_CIRCUITO / 1000) {
		    Thread.sleep(10000);
		    i = i + 10;
		    IR.paneltest.progressBar.setValue(i);
		}

		IR.paneltest.progressBar.setValue(0);
	    } catch (InterruptedException e) {
		if (logger.isErrorEnabled()) {
		    logger.error(e.getMessage());
		}
	    }

	    primera = false;
	    IR.paneltest.progressBar.setMaximum(25);
	}

	IR.paneltest.lbltesteando.setText("TESTEANDO, espere ...");
	// Me salgo si no hay que seguir haciendo el test
	if (IR.test == false)
	    return;

	if (logger.isInfoEnabled()) {
	    logger.info(("Estoy en hilocaudalimetro: TEST"));
	    logger.info("Pulsos parciales: " + pulsosparciales);
	}

	vuelta++;

	IR.paneltest.progressBar.setValue(pulsosparciales);

	try {

	    cuentapulsoslapso();

	    if (logger.isInfoEnabled())
		logger.info("Lapsotiempo: " + lapsotiempo);

	} catch (Exception e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	    IR.escribetextPane("\nCancelada lectura de pulsos caudalimetro",
		    IR.normal, false);
	    return;
	}

	if (logger.isInfoEnabled()) {
	    logger.info("Tiempo transcurrido: " + lapsotiempo * vuelta);
	}

	if ((lapsotiempo * vuelta) <= 60) {

	    if (pulsosparciales >= 25) {
		if (logger.isInfoEnabled()) {
		    logger.info("< 60");
		}
		calculacaudaltest(tipovalv);
	    }
	} else if ((lapsotiempo * vuelta) > 60 && (lapsotiempo * vuelta) <= 120) {

	    if (pulsosparciales >= 25) {
		if (logger.isInfoEnabled()) {
		    logger.info("< 120");
		}
		calculacaudaltest(tipovalv);

	    }
	} else if ((lapsotiempo * vuelta) > 120
		&& (lapsotiempo * vuelta) <= 240) {

	    if (pulsosparciales > 3) {
		if (logger.isInfoEnabled()) {
		    logger.info(">120");
		}
		calculacaudaltest(tipovalv);
	    }
	} else if ((lapsotiempo * vuelta) > 240) {

	    if (pulsosparciales > 3) {
		if (logger.isInfoEnabled()) {
		    logger.info(">240");
		}
		calculacaudaltest(tipovalv);
	    } else {
		logger.error("No hay lecturas de agua. Se pasa a la siguiente valvula. ");
		calculacaudaltest(tipovalv);
	    }

	}

    }

    /**
     * @return
     */
    public int cuentapulsoslapso() {

	int puls = 0;
	long tiempoin = (System.nanoTime());
	puls = cuentapulsos();
	long lapso = System.nanoTime() - tiempoin;
	lapsotiempo = lapso / Math.pow(10, 9);

	return puls;

    }

    /**
     * @return
     */
    public int cuentapulsos() {

	Date dateFechaRiego = new Date();
	nuevospulsos = 0;

	if (logger.isInfoEnabled()) {
	    logger.info("Hola estoy en cuentapulsos");
	}

	// Si el caudalimetro está acoplado a una bt2
	if (tipoplaca >= 5) {

	    try {

		Thread.sleep(IrrisoftConstantes.DELAY_100MSEG);

		serialcon.cogesemaforo(tipoplaca);
		serialcon.serialPort.writeBytes(buftrans);
		bufferresp = serialcon.serialPort.readBytes(6, 4000);

		pulsos[0] = pulsos[1];
		pulsos[1] = bufferresp[4] & mascara;

		if (logger.isInfoEnabled()) {
		    logger.info("Respuesta de pulsos acomulados:" + pulsos[1]
			    + " tipoplaca " + tipoplaca);
		}

		// Segunda lectura porque la primera se desestima (da 0
		// pulsos??? a arreglar por tonyck???)
		if (pulsos[1] == 0) {
		    Thread.sleep(IrrisoftConstantes.DELAY_100MSEG);
		    serialcon.serialPort.writeBytes(buftrans);
		    bufferresp = serialcon.serialPort.readBytes(6, 4000);

		    pulsos[0] = pulsos[1];
		    pulsos[1] = bufferresp[4] & mascara;

		    if (logger.isInfoEnabled()) {
			logger.info("Respuesta de pulsos acomulados2:"
				+ pulsos[1] + " tipoplaca " + tipoplaca);
		    }
		}

		String s = ("0000000" + Integer
			.toBinaryString(0xFF & pulsos[1])).replaceAll(
			".*(.{8})$", "$1");
		if (logger.isInfoEnabled()) {
		    logger.info("Respuesta de pulsos acomulados (bits):" + s);
		}

		nuevospulsos = (pulsos[1] - pulsos[0]);

		if (vuelta == 0) {

		    tiempoini = System.nanoTime();
		    pulsosparciales = pulsos[1];
		    nuevospulsos = pulsos[1];
		}

	    } catch (SerialPortException e) {
		if (logger.isErrorEnabled()) {
		    logger.error(e.getMessage());
		}
	    } catch (SerialPortTimeoutException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Fallo en leer respuesta del puerto serie! "
			    + buftrans.toString());
		    logger.error(e.getMessage());
		}

		IR.escribetextPane(
			"\nTIMEOUUUUUT en la lectura del buffer serie caudal",
			IR.normal, false);

		// RECONECTO
		// serialcon.purga_puerto(tipoplaca,
		// serialcon.serialPort.getPortName());

	    }

	    catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	    finally {

		serialcon.sueltasemaforo(tipoplaca);
	    }

	}

	// Si el caudalimetro está conectado a una placa de gontzi
	else if (tipoplaca < 5) {

	    try {
		Thread.sleep(IrrisoftConstantes.DELAY_SENSOR_3SEG);
	    } catch (InterruptedException e1) {
		if (logger.isErrorEnabled()) {
		    logger.error(e1.getMessage());
		}
	    }

	    serialcon.cogesemaforo(tipoplaca);
	    serialcon.consultconsum(serialcon.serialPort, sens);

	    try {

		bufferresp = serialcon.serialPort.readBytes(6, 4000);

		// Le quito el signo alos bytes
		for (int j = 0; j < bufferresp.length; j++) {

		    bufferresp[j] = (byte) (bufferresp[j] & 0xFF);

		}

		int medicion = ((bufferresp[4] * 65536) + (bufferresp[3] * 256))
			+ bufferresp[2];

		if (logger.isInfoEnabled()) {
		    logger.info("Comando: " + bufferresp[0]);
		    logger.info("Borna: " + bufferresp[1]);
		    logger.info("Lectura1: " + bufferresp[2]);
		    logger.info("Lectura2: " + bufferresp[3]);
		    logger.info("Lectura3: " + bufferresp[4]);
		    logger.info("Checksum: " + bufferresp[5]);
		}

		pulsos[0] = pulsos[1];
		pulsos[1] = medicion;

		nuevospulsos = pulsos[1];

		// result = (pulsos[1] - pulsos[0]);

		// Pulsos totales
		if (vuelta == 0) {
		    tiempoini = System.nanoTime();
		    pulsosparciales = pulsos[1];
		    nuevospulsos = pulsos[1];

		}

	    } catch (SerialPortTimeoutException | SerialPortException e) {
		if (logger.isWarnEnabled()) {
		    logger.warn("TIMEOUUUUUT en la lectura del buffer serie.");
		    logger.warn(e.getMessage());
		}

		// RECONECTO
		// serialcon.purga_puerto(tipoplaca,
		// serialcon.serialPort.getPortName());

	    } finally {

		serialcon.sueltasemaforo(tipoplaca);
	    }
	}

	// ////////////////////
	// System.out.println("pulsos[0] "+pulsos[0]+" , pulsos[1] "+pulsos[1]+
	// " ,  nuevospulsos "+nuevospulsos);

	if (pulsos[1] != 0) {

	    if (nuevospulsos < 0) {

		vueltasens++;
		nuevospulsos = 128 + (pulsos[1] - pulsos[0]);

		// OOJOOO
		nuevospulsos = Math.abs(nuevospulsos);

		if (logger.isInfoEnabled()) {
		    logger.info("Pulsos en este bucle (Vuelta " + vueltasens
			    + " : " + nuevospulsos);
		}

		// Pulsos totales

		if (vuelta == 0) {
		    pulsosdiarios = pulsosdiarios + nuevospulsos;
		    pulsosparciales = 0;
		    nuevospulsos = 0;
		} else {
		    pulsosparciales = pulsosparciales + (int) nuevospulsos;
		    pulsosdiarios = pulsosdiarios + nuevospulsos;
		}

	    } else {
		if (logger.isInfoEnabled()) {
		    logger.info("Pulsos en este bucle: " + nuevospulsos
			    + " y pulsos diarios: " + pulsosdiarios);
		}

		// Cojo los pulsos diarios de la BBDD Local y la fecha de la
		// primera inserccion.
		// Si es la primera vez que pasa le pongo la fecha.
		if (pulsosdiarios == 0 && flag == false) {
		    fechaRiego = IR.hiloescucha.connDB.devolverFecha(sens.getNum_sensor());
		    
		    if(fechaRiego != null){
        		    String fechaP = fechaRiego.toString();
        		    fechaP = fechaP.substring(0, 10);
        		    
        		    try {
        			dateFechaRiego = formatterfecha.parse(fechaP);
        		    } catch (ParseException e) {
        			if(logger.isErrorEnabled()){
        			    logger.error("Error en el parseo del String: " +e.getMessage());
        			}
        		    }
		    }
		    
		    if (fechaRiego == null) {
			cal = Calendar.getInstance();
			java.sql.Timestamp dateRiego = new java.sql.Timestamp(
				cal.getTimeInMillis());
			fechaRiego = dateRiego;


		    } else if(dateFechaRiego.compareTo(IR.hoy)!=0){

			cal = Calendar.getInstance();
			java.sql.Timestamp dateRiego = new java.sql.Timestamp(
				cal.getTimeInMillis());
			fechaRiego = dateRiego;
		    }
		    
		    
		    pulsosdiarios = IR.hiloescucha.connDB.devolverRiego(sens.getNum_sensor(), fechaRiego);
		    pulsosdiarios = (pulsosdiarios / sens.getK());
		    
		    
		    
		}

		// Pulsos totales
		if (vuelta == 0) {
		    pulsosparciales = 0;
		    pulsosdiarios = pulsosdiarios + nuevospulsos;
		}

		else {
		    pulsosparciales = pulsosparciales + (int) nuevospulsos;
		    pulsosdiarios = pulsosdiarios + nuevospulsos;
		}
	    }

	    if (!first) {
		// Guardo la lectura de los nuevos pulsos para guardarlos en la
		// BBDD
		// Local.
		int consumoAcu = pulsosdiarios * sens.getK();
		// Cojo la fecha del sistema para insertar una nueva fila.
		if (flag == true) {
		    cal = Calendar.getInstance();
		    java.sql.Timestamp dateRiego = new java.sql.Timestamp(
			    cal.getTimeInMillis());
		    // Pongo el valor de dateRiego en la variable de memoria
		    fechaRiego = dateRiego;
		}
		// Cojo la fecha de memoria o de la BBDD Local para actualizar
		// la tabla;
		LecturasSensor lectCaudalimetro = new LecturasSensor();
		lectCaudalimetro.setNombreSensor(sens.getNum_sensor());
		lectCaudalimetro.setFecha(fechaRiego);
		lectCaudalimetro.setRiego(consumoAcu);
		lectCaudalimetro.setVelocidadAnemometro(0);
		lectCaudalimetro.setLluvia(0);
		lectCaudalimetro.setTemperatura(0);
		lectCaudalimetro.setIntru(false);

		// Inserto o cambio el valor de la fila de riego.
		IR.hiloescucha.connDB.insertarLecturasBBDD(lectCaudalimetro);
		flag = false;
	    }
	} else
	    nuevospulsos = 0;

	// Calculo el caudal siempre que no sea un test !
	if (!IR.test)
	    calculacaudal();

	// Si es la primera vez que entra en el caudalimetro
	if (first) {
	    nuevospulsos = 0;
	    pulsosparciales = 0;
	    pulsosdiarios = 0;
	    first = false;
	}

	if (logger.isInfoEnabled()) {
	    logger.info("Pulsos nuevos: " + nuevospulsos);
	    logger.info("Pulsos parciales: " + pulsosparciales);
	    logger.info("Pulsos totales diarios: " + pulsosdiarios);

	}

	return nuevospulsos;

    }

    /**
     * @param tipovalv
     */
    public void calculacaudaltest(int tipovalv) {

	numvalv = IR.paneltest.HiloTest.numvalv;
	numplaca = IR.paneltest.HiloTest.numplaca;

	if (logger.isInfoEnabled()) {
	    logger.info("NUMVALV en calculacaudaltest: " + numvalv);
	    logger.info("numplaca en calculacaudaltest: " + numplaca);
	    logger.info("Caudal con tiempotestvalv: "
		    + ((System.nanoTime() - tiempoini) / Math.pow(10, 9)));
	}

	calculacaudal();

	if (!IR.inicial) {
	    IR.paneltest.lblPulsosDat
		    .setText(Integer.toString(pulsosparciales));
	    IR.paneltest.lblCauDat.setText(caudal + " l/seg");
	} else {
	    IR.paneltest.lblpul.setText(Integer.toString(pulsosparciales));
	    IR.paneltest.lblls.setText(caudal + " l/seg");

	}

	// Aviso al hiloamperimetro correspondiente para que mida la intensidad
	// que hay, y despues cierro
	// if (logger.isInfoEnabled()) {
	// logger.info("NUMPLACA en HiloCaudalimetro" +
	// numplaca+"  , "+sens.getNum_placa());
	// }

	valv = new Valvula();

	for (Iterator<Valvula> iter = IR.valvsabiertastot.iterator(); iter
		.hasNext();) {

	    valv = iter.next();

	    System.out.println("VALV: " + valv.getCodelecvalv()
		    + " , numplaca " + valv.getNum_placa());
	    if (valv.getCodelecvalv().contentEquals(Integer.toString(numvalv))
		    && valv.isMaestra() == 0)
		break;
	    else
		valv = null;
	}

	if (valv != null) {
	    System.out.println("Valvula: " + valv.getCodelecvalv()
		    + " , numplaca " + valv.getNum_placa());

	    // AVISO al amperimetro correspondiente
	    Sensor sensor = ListaSensores.getInstance().getsens(
		    valv.getNum_placa(), 3);
	    System.out.println("Sensor al que avisa el caudalimetro: "
		    + sensor.getNum_sensor() + " , "
		    + sensor.getHilosens().getName());

	    synchronized (sensor.getHilosens()) {
		sensor.setLecturacau(caudal);
		sensor.setValvula(numvalv);
		sensor.getHilosens().notify();
	    }

	} else {
	    logger.info("La valvula no existe !");
	}
	// Duermo para que le de tiempo a la bt2 a recibir otra orden
	try {
	    Thread.sleep(IrrisoftConstantes.DELAY_200MSEG);
	} catch (InterruptedException e1) {
	    if (logger.isErrorEnabled()) {
		logger.error(e1.getMessage());
	    }
	}

	// Si es una valvulamci
	if (tipovalv == 1) {

	    // cierro la valvula requerida
	    if (IrrisoftConstantes.PLACA_MCI_1 == numplaca) {
		IR.panelmci.interruptor(IR.valvsmci.getvalvmci(numvalv - 101),
			IR.valvsmci.getvalvmci(numvalv - 101).getImgasoc(),
			IR.panelmci.panel1);
		IR.valvsmci.getvalvmci(numvalv - 101).setCaudalmod(caudal);

	    }
	    if (IrrisoftConstantes.PLACA_MCI_2 == numplaca) {
		IR.panelmci.interruptor(IR.valvsmci2.getvalvmci(numvalv - 201),
			IR.valvsmci2.getvalvmci(numvalv - 201).getImgasoc(),
			IR.panelmci.panel2);
		IR.valvsmci2.getvalvmci(numvalv - 201).setCaudalmod(caudal);
	    }
	    if (IrrisoftConstantes.PLACA_MCI_3 == numplaca) {
		IR.panelmci.interruptor(IR.valvsmci3.getvalvmci(numvalv - 301),
			IR.valvsmci3.getvalvmci(numvalv - 301).getImgasoc(),
			IR.panelmci.panel3);
		IR.valvsmci3.getvalvmci(numvalv - 301).setCaudalmod(caudal);
	    }
	    if (IrrisoftConstantes.PLACA_MCI_4 == numplaca) {
		IR.panelmci.interruptor(IR.valvsmci4.getvalvmci(numvalv - 401),
			IR.valvsmci4.getvalvmci(numvalv - 401).getImgasoc(),
			IR.panelmci.panel4);
		IR.valvsmci4.getvalvmci(numvalv - 401).setCaudalmod(caudal);
	    }

	}
	// Si es una valvulabt2
	else if (tipovalv == 2) {
	    // cierro la valvula requerida
	    if (IrrisoftConstantes.BT2_2000 > numvalv) {
		IR.panelbt2
			.interruptor(IR.valvsbt2.getvalvbt2(Integer
				.toString(numvalv)), 2, 3);
		IR.valvsbt2.getvalvbt2(Integer.toString(numvalv)).setCaudalmod(
			caudal);
	    }
	    if (IrrisoftConstantes.BT2_3000 > numvalv
		    && IrrisoftConstantes.BT2_2000 < numvalv) {
		IR.panelbt2.interruptor(
			IR.valvsbt22.getvalvbt2(Integer.toString(numvalv)), 2,
			4);
		IR.valvsbt22.getvalvbt2(Integer.toString(numvalv))
			.setCaudalmod(caudal);
	    }
	    if (IrrisoftConstantes.BT2_4000 > numvalv
		    && IrrisoftConstantes.BT2_3000 < numvalv) {
		IR.panelbt2.interruptor(
			IR.valvsbt23.getvalvbt2(Integer.toString(numvalv)), 2,
			5);
		IR.valvsbt23.getvalvbt2(Integer.toString(numvalv))
			.setCaudalmod(caudal);
	    }
	    if (IrrisoftConstantes.BT2_4000 < numvalv) {
		IR.panelbt2.interruptor(
			IR.valvsbt24.getvalvbt2(Integer.toString(numvalv)), 2,
			6);
		IR.valvsbt24.getvalvbt2(Integer.toString(numvalv))
			.setCaudalmod(caudal);
	    }
	}

	if (!IR.inicial) {

	    // Activo los botones
	    IR.paneltest.btnAtras.setEnabled(true);
	    IR.paneltest.btnTestearMCI.setEnabled(true);
	    IR.paneltest.btntesteoini.setEnabled(true);
	    IR.paneltest.btnTestearBT2.setEnabled(true);
	    IR.paneltest.comboBoxMCI.setEnabled(true);
	    IR.paneltest.comboBoxBt2.setEnabled(true);
	    IR.paneltest.btnAtras.setText("Atras");

	}

	// para que no tenga en cuenta los pulsos hasta que se vacíe la tubería
	// !!

	IR.paneltest.lbltesteando.setText("Testeo de valvula OK");

	vuelta = -1;
	pulsosparciales = 0;

	// Para que se pueda ver la info en el panel
	try {
	    Thread.sleep(IrrisoftConstantes.DELAY_SENSOR_10SEG);
	} catch (InterruptedException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	}

	// Aviso al hilo del test para que continue con su ejecución
	synchronized (IR.paneltest.hilotest) {
	    IR.paneltest.hilotest.notify();
	}

	// Para que le de tiempo a ejecutar a hilotest
	try {
	    Thread.sleep(IrrisoftConstantes.DELAY_SENSOR_3SEG);
	} catch (InterruptedException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	}

    }

    /**
     * 
     */
    public void calculacaudalauto() {

	calculacaudal();

	float sumatoriocaudal = 0;
	String abiertas = "";
	int i = 0;

	for (Valvula v : IR.valvsabiertastot) {
	    i++;
	    if (v.isMaestra() == 0) {
		if (i == IR.valvsabiertastot.size())
		    abiertas = abiertas + v.getCodelecvalv();
		else
		    abiertas = abiertas + v.getCodelecvalv() + ", ";

		sumatoriocaudal = sumatoriocaudal + v.getCaudalmod();
	    }

	}

	// AKI comparo las medidas obtenidas comparandolas con las modelos
	if (logger.isInfoEnabled()) {
	    logger.info("SUMATORIO CAUDAL: " + sumatoriocaudal);
	}

	float diferenciacau = sumatoriocaudal - caudal;
	if (logger.isInfoEnabled()) {
	    logger.info("diferenciacau = " + diferenciacau);
	}
	// Se calcula un margen de diferencia para las alarmas del 10%
	// (variable??)
	if (Math.abs(diferenciacau) >= (sumatoriocaudal * 10) / 100) {
	    if (diferenciacau < 0) {
		if (logger.isWarnEnabled()) {
		    logger.warn("Hay un problema en el consumo de agua. Estás consumiendo "
			    + Math.abs(diferenciacau)
			    + " l/s de más\nVálvulas " + abiertas);
		}
		// Genero alarma: Fuga de agua en estaciones.
		gestorAlertas.insertarAlarma(2001);

		IR.escribetextPane(
			"\nHay un problema en el consumo de agua.\nEstás consumiendo "
				+ Math.abs(diferenciacau)
				+ " l/s de más\nVálvulas " + abiertas,
			IR.normal, false);

	    } else if (diferenciacau > 0) {
		if (logger.isWarnEnabled()) {
		    logger.warn("Hay un problema en el consumo de agua. Estás consumiendo "
			    + Math.abs(diferenciacau)
			    + " l/s de menos\nVálvulas " + abiertas);
		}
		// Genero alarma: Obstrucción mecánica en estacios ¿?
		gestorAlertas.insertarAlarma(2004);

		IR.escribetextPane(
			"\nHay un problema en el consumo de agua.\nEstás consumiendo "
				+ Math.abs(diferenciacau)
				+ " l/s de menos\nVálvulas " + abiertas,
			IR.normal, false);

	    }
	}

    }

    /**
     * 
     */
    private void calculacaudal() {

	if (logger.isInfoEnabled()) {
	    logger.info("Tiempo calculo consumo: "
		    + ((System.nanoTime() - tiempoini) / Math.pow(10, 9)));
	}

	caudal = (float) (Math
		.rint(((((pulsosparciales) * (sens.getK())) / ((System
			.nanoTime() - tiempoini) / Math.pow(10, 9))) * 1000)) / 1000);

	if (logger.isInfoEnabled()) {
	    logger.info("El caudal es de : " + caudal + " l/s");
	}

	// if (vuelta > 1 || vuelta == -1 && !first) {

	if (!first) {

	    // Añado el consumo a la lista de consumos cau de la placa
	    // correspondiente
	    // if (sens.getNum_placa() > 4)
	    // IR.panelecturasbt2.listacaudales[sens.getNum_placa() - 5] =
	    // caudal;
	    // else if (sens.getNum_placa() < 5 && sens.getNum_placa() > 0)
	    // IR.panelecturasmci.listacaudales[sens.getNum_placa() - 1] =
	    // caudal;
	    // else if (sens.getNum_placa() == 0)
	    // IR.panelecturasens.listacaudales[sens.getNum_placa()] = caudal;
	    //
	    // // Añado el consumo a la lista de consumos pul de la placa
	    // // correspondiente
	    // if (sens.getNum_placa() > 4)
	    // IR.panelecturasbt2.listapulsoscau[sens.getNum_placa() - 5] =
	    // nuevospulsos;
	    // else if (sens.getNum_placa() < 5 && sens.getNum_placa() > 0)
	    // IR.panelecturasmci.listaconsumosamp[sens.getNum_placa() - 1] =
	    // nuevospulsos;
	    // else if (sens.getNum_placa() == 0)
	    // IR.panelecturasens.listaconsumosamp[sens.getNum_placa()] =
	    // nuevospulsos;

	    // Meto las lecturas al sensor
	    sens.setPulsos(pulsosparciales);
	    sens.setLectura(df.format(caudal));
	    logger.warn("Lectura: " + sens.getLectura() + " , pulsos: "
		    + sens.getPulsos());

	    // Escribo en el panel la lectura
	    // ponelecturas(sens);

	}

    }

    // //////////////////
    // GETTERS Y SETTERS
    // //////////////////
    // public void setTerminar(boolean terminar) {
    // this.terminar = terminar;
    // }
    //
    // public boolean getTerminar() {
    // return terminar;
    // }

    // public boolean isTest() {
    // return test;
    // }
    //
    // public void setTest(boolean test) {
    // this.test = test;
    // }

    public int getPulsostot() {
	return pulsosparciales;
    }

    public void setPulsostot(int pulsostot) {
	this.pulsosparciales = pulsostot;
    }

    // public boolean isMci() {
    // return mci;
    // }
    //
    // public void setMci(boolean mci) {
    // this.mci = mci;
    // }

    public int getVuelta() {
	return vuelta;
    }

    public void setVuelta(int vuelta) {
	this.vuelta = vuelta;
    }

    public int getValvsabiertas() {
	return valvsabiertas;
    }

    public void setValvsabiertas(int valvsabiertas) {
	this.valvsabiertas = valvsabiertas;
    }

    public boolean isFlag() {
	return flag;
    }

    public void setFlag(boolean flag) {
	this.flag = flag;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
	// Evento para sincronizacion del programador.
	String nombreCampo = evt.getPropertyName();
	if ("false".contains(nombreCampo)) {
	    this.rearmarCau = true;
	}
    }

}
