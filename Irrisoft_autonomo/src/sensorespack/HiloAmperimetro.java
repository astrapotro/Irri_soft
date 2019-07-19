package sensorespack;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import jssc.SerialPort;
import irrisoftpack.ConexionDB;
import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;
import irrisoftpack.SerialDriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import alertaspack.GestorAlertas;

import valvulaspack.Valvula;

public class HiloAmperimetro implements Runnable , PropertyChangeListener{

    private static Logger logger = LogManager.getLogger(HiloAmperimetro.class
	    .getName());
    // Habría que cambiarlo para que fuera dinamico, ahora esta puesto para la
    // bt2
    protected SerialDriver serialcon;

    // protected String puerto;
    protected int tipoplaca, len, leo, i, repeticion = 0;
    protected int numplaca;

    // Mascara para desechar el bit 7 de la respuesta de la bt2
    protected static byte mascara = (byte) 0b01111111;

    private boolean terminar = false;
    //public boolean test;
    // test = IR.test;
    // public boolean inicial=false;
    private boolean mci = true;
   

    // public int tipovalv;
    // public int numvalv;

    public Valvula valv;
    private Sensor sens;

    private int lectura = 0, consumoresidual = 0;
    private int diferencia_consum = 0;

    public ConexionDB connDB = new ConexionDB();
    private int valvsabiertas;

    private GestorAlertas gestorAlertas;
    private Irrisoft IR;
    private boolean rearmarAmp = false;
    
    

    public HiloAmperimetro(SerialDriver serial, Sensor sens, int tipo) {
	super();
	this.serialcon = serial;
	this.tipoplaca = tipo;
	this.sens = sens;
	gestorAlertas = GestorAlertas.getInstance();
	this.IR = Irrisoft.window;
	//this.test = IR.test;

    }

    public void run() {

	// Duermo para dar tiempo
	try {

	    Thread.sleep(10000 + (tipoplaca * 1000));

	    Thread.sleep(IrrisoftConstantes.DELAY_SENSOR_RUN
		    + (tipoplaca * IrrisoftConstantes.DELAY_FREC_LECT));

	} catch (InterruptedException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	}

	if (!serialcon.serialPort.isOpened()) {
	    SerialPort serialPort = new SerialPort(
		    serialcon.serialPort.getPortName());
	    serialcon.setSerialPort(serialPort);
	    serialcon.conectaserial(tipoplaca);
	}

	valvsabiertas = IR.valvsabiertas(tipoplaca);
	// Calculo la intensidad de la línea sin ninguna valvula abierta
	if (valvsabiertas == 0) {
	    serialcon.cogesemaforo(tipoplaca);
	    // serialcon.resetsinsemaforo(tipoplaca);
	    serialcon.consultconsum(serialcon.serialPort, sens);
	    serialcon.leeresp(true, tipoplaca, true, "0", 0);
	    consumoresidual = Integer.parseInt(sens.getLectura());
	    // serialcon.resetsinsemaforo(tipoplaca);
	    serialcon.sueltasemaforo(tipoplaca);
	    if (logger.isInfoEnabled()) {
		logger.info("Consumo residual: " + consumoresidual);
	    }
	}

	while (true) {
	    // Dejo que muera el hilo si irrisoft ha sido rearmado

	    if (rearmarAmp){

		logger.info("Termino el hilo de Amperimetro porque se ha rearmado Irrisoft");
		return;
	    }

	    valvsabiertas = IR.valvsabiertas(tipoplaca);

	    if (IR.test == false){

		    if (IR.test == true)
			return;

		    automatico();

		
	    } else{

		test();
	    }

	}

    }

    /**
     * 
     */
    public void automatico() {

	if (logger.isInfoEnabled()) {
	    logger.info(("Estoy en hiloAMPERIMETRO: AUTOMATICO, sensor:"
		    + sens.getNum_sensor() + " placa " + sens.getTipo_placa()
		    + " " + sens.getNum_placa()));
	}
	// Calcula la intensidad

	calculaintauto();
    }

    /**
     * 
     */
    public void calculaintauto() {

	if (IR.test)
	    return;

	// Duermo 10 segundos para que no sea continuo
	// TODO Hay que hacerlo para que coja la frec_lect
	try {
	    Thread.sleep(IrrisoftConstantes.DELAY_SENSOR_30SEG
		    + (tipoplaca * IrrisoftConstantes.DELAY_FREC_LECT));
	} catch (InterruptedException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	}

	if (IR.test)
	    return;
// OJO
//	if (!serialcon.conectado) {
//	    SerialPort serialPort = new SerialPort(
//		    serialcon.serialPort.getPortName());
//	    serialcon.setSerialPort(serialPort);
//	    serialcon.conectaserial(tipoplaca);
//	}

	serialcon.cogesemaforo(tipoplaca);
	serialcon.consultconsum(serialcon.serialPort, sens);
	serialcon.leeresp(true, tipoplaca, true, "0", 0);
	lectura = Integer.parseInt(sens.getLectura());

	// Añado el consumo a la lista de consumos de la placa correspondiente
//	if (sens.getNum_placa() > 4)
//	    IR.panelecturasbt2.listaconsumosamp[sens.getNum_placa() - 5] = lectura;
//	else if (sens.getNum_placa() < 5 && sens.getNum_placa() > 0)
//	    IR.panelecturasmci.listaconsumosamp[sens.getNum_placa() - 1] = lectura;
//	else if (sens.getNum_placa() == 0)
//	    IR.panelecturasens.listaconsumosamp[sens.getNum_placa()] = lectura;

	serialcon.sueltasemaforo(tipoplaca);
	diferencia_consum = lectura - consumoresidual;
	// Mete la lectura en el sensor
	sens.setLectura(Integer.toString(lectura));

	if (logger.isInfoEnabled()) {
	    logger.info("La intensidad en "
		    + serialcon.serialPort.getPortName() + " es "
		    + (lectura - consumoresidual) + " mA");
	}

	// Calculo cuanta intensidad hay en la línea de la placa correspondiente
	int sumatorioint = 0;
	for (Valvula v : IR.listavalvsabiertas(tipoplaca)) {

	    if (logger.isInfoEnabled()) {
		logger.info(v.getNum_placa() + " - " + sens.getNum_placa());
	    }
	    if (v.getNum_placa() == sens.getNum_placa()) {
		sumatorioint = sumatorioint + v.getIntensidadmod();
		if (logger.isInfoEnabled()) {
		    logger.info("SUMATORIO INTENSIDAD: " + sumatorioint);
		}

	    }

	}

	// AKI comparo las medidas obtenidas comparandolas con las modelos

	if (sumatorioint == 0 && consumoresidual >= 0) {
	    if (logger.isInfoEnabled()) {
		logger.info("Consumo en la línea normal, " + consumoresidual
			+ " mA");
	    }
	} else {

	    // Le resto la intensidad residual
	    // sumatorioint = sumatorioint - consumoresidual;
	    // Calculo la diferencia con el modelo
	    int diferenciaint = sumatorioint
		    - Math.abs((lectura - consumoresidual));

	    if (logger.isInfoEnabled()) {
		logger.info("diferenciaint = " + diferenciaint);
	    }
	    // Se calcula un margen de diferencia para las alarmas del 10%
	    // (variable??)
	    if (Math.abs(diferenciaint) >= (sumatorioint * 10) / 100) {
		if (diferenciaint < 0) {
		    if (logger.isWarnEnabled()) {
			logger.warn("Hay un problema en el consumo de intensidad. Estás consumiendo "
				+ Math.abs(diferenciaint) + " mA de más");
		    }
		    // Genero alarma: Consumo electrico excesivo en estaciones.
		    gestorAlertas.insertarAlarma(2007);

		    IR.escribetextPane(
			    "\n Alerta! Consumo " + Math.abs(diferenciaint)
				    + " mA de más", IR.normal, false);

		} else if (diferenciaint > 0) {
		    if (logger.isWarnEnabled()) {
			logger.warn("Hay un problema en el consumo de intensidad. Estás consumiendo "
				+ Math.abs(diferenciaint) + " mA de menos");
		    }
		    // Genero alarma: Consumo electrico escaso en estaciones.
		    gestorAlertas.insertarAlarma(2008);

		    IR.escribetextPane(
			    "\n Alerta! Consumo " + Math.abs(diferenciaint)
				    + " mA de menos", IR.normal, false);

		}
	    }
	}
    }

    /**
     * 
     */
    public void test() {

	if (logger.isInfoEnabled()) {
	    logger.info(("Estoy en hiloamperimetro: TEST"));
	    logger.info(numplaca + " " + sens.getNum_placa());
	}

	if (IR.haycaudalimetro) {
	    synchronized (sens.getHilosens()) {
		try {
		    sens.getHilosens().wait();
		} catch (InterruptedException e) {
		    if (logger.isErrorEnabled()) {
			logger.error(e.getMessage());
		    }
		}
	    }
	} else {
	    try {
		Thread.sleep(IrrisoftConstantes.DELAY_SENSOR_25SEG);
	    } catch (InterruptedException e) {
		if (logger.isErrorEnabled()) {
		    logger.error(e.getMessage());
		}
	    }
	}
	if (logger.isInfoEnabled()) {
	    logger.info(("Estoy en hiloamperimetro: TEST , y me acaban de despertar !!"));
	}

	// Vuelvo si me han despertado y ya no hay que hacer el test
	if (IR.test == false)
	    return;

	calculaintest();
    }

    /**
     * 
     */
    public void calculaintest() {

	if (!serialcon.conectado) {
	    SerialPort serialPort = new SerialPort(
		    serialcon.serialPort.getPortName());
	    serialcon.setSerialPort(serialPort);
	    serialcon.conectaserial(tipoplaca);
	}

	serialcon.cogesemaforo(tipoplaca);
	serialcon.consultconsum(serialcon.serialPort, sens);
	serialcon.leeresp(true, this.tipoplaca, true, "0", 0);
	lectura = Integer.parseInt(sens.getLectura());
	serialcon.sueltasemaforo(tipoplaca);

	if (logger.isInfoEnabled()) {
	    logger.info("Intensidad: " + lectura);
	    logger.info("Caudal: " + sens.getLecturacau());
	}
	IR.paneltest.progressBar.setValue(25);

	if (!IR.inicial) {
	    IR.paneltest.lblIntenDat.setText(Integer.toString(lectura
		    - consumoresidual)
		    + " mA");

	    // Activo los botones
	    IR.paneltest.btnAtras.setEnabled(true);
	    IR.paneltest.btnTestearMCI.setEnabled(true);
	    IR.paneltest.btntesteoini.setEnabled(true);
	    IR.paneltest.btnTestearBT2.setEnabled(true);
	    IR.paneltest.comboBoxMCI.setEnabled(true);
	    IR.paneltest.comboBoxBt2.setEnabled(true);
	    IR.paneltest.btnAtras.setText("Atras");

	} else
	    IR.paneltest.lblamp.setText(Integer.toString(lectura
		    - consumoresidual)
		    + " mA");

	if (IR.inicial) {
	 // escribir registro en la BBDD
	    escribeconsum(Integer.toString(sens.getValvula()));

	    try {
		Thread.sleep(IrrisoftConstantes.DELAY_SENSOR_5SEG);
	    } catch (InterruptedException e) {
		if (logger.isErrorEnabled()) {
		    logger.error(e.getMessage());
		}
	    }

	} else {

	    IR.paneltest.lbltesteando.setText("Testeo de valvula OK");
	    // escribir registro en la BBDD
	    escribeconsum(Integer.toString(sens.getValvula()));
	}

    }

//    /**
     /** Escribe el consumo el la BBDD Remota (GIS) y Local.
     * 
     * @param valvula
     */
    public void escribeconsum(String valvula) {

	IR.getConn().insertalconsumostest(Integer.toString(sens.getValvula()),
		sens.getLecturacau(), lectura - consumoresidual);
	IR.getConn().insertarconsumostest(Integer.toString(sens.getValvula()),
		sens.getLecturacau(), lectura - consumoresidual);

    }


    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
	String nombreCampo = evt.getPropertyName();
	
	if("false".contains(nombreCampo)){
	    this.rearmarAmp = true;
	}
	
    }
    
    
    
    // ////////////////////
    // GETTERS Y SETTERS
    // ////////////////////

    public void setTerminar(boolean terminar) {
	this.terminar = terminar;
    }

    public boolean getTerminar() {
	return terminar;
    }

//    public boolean isTest() {
//	return test;
//    }
//
//    public void setTest(boolean test) {
//	this.test = test;
//    }

    public boolean isMci() {
	return mci;
    }

    public void setMci(boolean mci) {
	this.mci = mci;
    }

    public int getValvsabiertas() {
	return valvsabiertas;
    }

    public void setValvsabiertas(int valvsabiertas) {
	this.valvsabiertas = valvsabiertas;
    }

 


}
