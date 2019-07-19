package irrisoftpack;

//import gnu.io.CommPort;
//import gnu.io.CommPortIdentifier;
//import gnu.io.NoSuchPortException;
//import gnu.io.PortInUseException;
//import gnu.io.SerialPort;
//import gnu.io.SerialPortEvent;
//import gnu.io.SerialPortEventListener;
//import gnu.io.UnsupportedCommOperationException;

//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
import java.util.Arrays;

import javax.swing.JOptionPane;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sensorespack.Sensor;
import valvulaspack.Valvula;
import alertaspack.GestorAlertas;
//(import javax.swing.text.BadLocationException;
//import javax.comm.NoSuchPortException;

//////////
//////////
////////// 
////////////////
//////////////// Sólo funciona en linux con la libreria rxtx-2.2pre2-bins. Con inferiores casca el .so !!!!!!
//////////////// Con linux socat -d -d pty,raw pty,raw para emular puertos serie. 
//////////////// Cada terminal abierta se abre en un pts nuevo. OJO !!

public class SerialDriver { // implements SerialPortEventListener{

    // final static String ESTADO="ESTADO: ";

    private static Logger logger = LogManager.getLogger(SerialDriver.class
	    .getName());

    public SerialPort serialPort;
    public String device = "nulo";
    public String ver;
    // public OutputStream out;
    // public InputStream in;
    public boolean conectado = false;
    protected int consumo;

    public byte[] bufferconsum = new byte[6];
    public int[] bufferconsumint = new int[6];
    // public byte[] buffer = new byte[5];
    public int[] bufferint = new int[5];
    public ConexionDB con = new ConexionDB();

    private GestorAlertas gestorAlertas;

    private Irrisoft IR;

    private Sensor sens;

    public String port;
    public int tipo = 0;

    protected boolean respuestalost = false;

    // public byte[] readBuffer = new byte[80];
    // public Valvula valvu;

    // Bytes para la BT2
    // Para decirle
    protected String TXLEN = "04", TXSTN2, TXCHK, TXSLV, TXCMD;
    // Para recibir
    protected byte RXLEN, RXSTN1, RXSTN2, RXCHK, RXSLV, RXHDL, RXCMD, RXCPL;

    protected byte[] reset = { (byte) 0x00, (byte) 0x00, (byte) 0x00,
	    (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
	    (byte) 0x00, (byte) 0x00 };

    public SerialDriver() {
	super();

	gestorAlertas = GestorAlertas.getInstance();
	this.IR = Irrisoft.window;

    }

    /**
     * Conecto el puerto serie, dependiendo de tipo de placa que sea.
     * 
     * @param tipo
     */
    public void conectaserial(int tipo) {

	// Antes de utilizar este metodo, es requisito configurar el puerto.

	this.tipo = tipo;
	// cogesemaforo(tipo);
	//
	// if (!conectado){
	// //Por si la placa no tiene conexión propago el error y no
	// semaforobt2ejecuto
	// nada.
	// while (portIdentifier == null){
	// try {
	// portIdentifier = CommPortIdentifier.getPortIdentifier(puerto);
	// } catch (NoSuchPortException e1) {
	// //TODO Controlar en el panelpral que hay conexión con las placas
	// antes de que el error se propague!!!
	// //logger.log(Level.WARNING,
	// "No hay conexion con el puerto "+puerto+"\nEl usb de la placa está conectado?",
	// e1);
	// IR.textArea.append("\nNo hay conexion con el puerto "+puerto+"\nEl usb de la placa está conectado?");
	// throw e1;
	// }
	//
	// }
	//
	// if ( portIdentifier.isCurrentlyOwned() ){
	// if(logger.isWarnEnabled())
	// {
	// logger.warn("Error: El puerto "+puerto+
	// " está siendo utilizado por otro proceso "+portIdentifier.getCurrentOwner());
	// }
	// //logger.warning(ESTADO+"Error: El puerto "+puerto+
	// " está siendo utilizado por otro proceso");
	// // try {
	// // Thread.sleep(5000);
	// // } catch (InterruptedException e) {
	// // // TODO Auto-generated catch block
	// // e.printStackTrace(); semaforobt2 = new Semaforo(1);
	// // }
	// }
	// // else
	// // {
	// CommPort commPort = null;
	// try {
	// commPort = portIdentifier.open(this.getClass().getName(),1000);
	// } catch (PortInUseException e) {
	// //logger.log(Level.WARNING,
	// "Puerto ya en uso "+portIdentifier.getName(), e);
	// e.printStackTrace();
	// }
	//
	// if ( commPort instanceof SerialPort )
	// {
	//
	// serialPort = (SerialPort) commPort; semaforobt2 = new Semaforo(1);
	// conectado=true;
	//
	// //TODO Si algo falla con los serie QUITAR
	// // serialPort.disableReceiveTimeout();
	// // try {
	// // serialPort.enableReceiveThreshold(1);
	// // } catch (UnsupportedCommOperationException e) {
	// // //logger.log(Level.WARNING,
	// "operación comunicaciones no soportada", e);
	// // e.printStackTrace();
	// // }
	//
	//
	//
	// //
	// //CONFIGURACION de LA CONEXION SERIE con varios ifs para cada placa
	// !!
	// try {
	// if (tipo==1 || tipo ==2 ||tipo ==3 || tipo==4){
	//
	// serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
	// //}else if (tipo==3 || tipo ==4){
	// }else{
	//
	// serialPort.setSerialPortParams(1200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
	// }
	// } catch (UnsupportedCommOperationException e) {
	// //logger.log(Level.WARNING, "operación comunicaciones no soportada",
	// e);
	// e.printStackTrace();
	// }
	//
	//
	// try {
	// out = serialPort.getOutputStream();
	// in = serialPort.getInputStream();
	//
	//
	// } catch (IOException e) {
	// //logger.log(Level.WARNING, "Excepción E/S", e);
	// e.printStackTrace();
	// }
	//
	// }
	// else
	// {
	// //logger.warning("Error: Only serial ports are handled by this example.");
	// }
	// // }
	//
	//
	// //logger.exiting(this.getClass().getCanonicalName(),
	// "conectaserial");
	// }
	//
	// //Añado el manejador de eventos
	// setSerialEventHandler(serialPort);
	// try {
	// serialPort.addEventListener(this);
	// } catch (TooManyListenersException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// serialPort.notifyOnDataAvailable(true);
	//
	// sueltasemaforo(tipo);

	// JSSC

	// Esta linea la he comentado y luego he realizado el setserialPort
	// serialPort = new SerialPort(puerto);

	cogesemaforo(tipo);

	// Si es una placa de samcla
	if (tipo == -1) {

	    // No hago nada

	} else {
	    try {

		serialPort.openPort();

		if (IrrisoftConstantes.PLACA_SENSORES_0 == tipo
			|| IrrisoftConstantes.PLACA_MCI_1 == tipo
			|| IrrisoftConstantes.PLACA_MCI_2 == tipo
			|| IrrisoftConstantes.PLACA_MCI_3 == tipo
			|| IrrisoftConstantes.PLACA_MCI_4 == tipo) {

		    serialPort.setParams(9600, 8, 1, 0);
		    serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
		    serialPort.setDTR(false);

		} else {
		    serialPort.setParams(1200, 8, 1, 0);
		    serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
		    serialPort.setDTR(false);

		}

		port = serialPort.getPortName();
		conectado = true;
		if (logger.isWarnEnabled()) {
		    logger.warn("PUERTO " + serialPort.getPortName()
			    + " abierto OK");
		}

	    } catch (SerialPortException ex) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error de conexión con puerto serie: "
			    + ex.getMessage());

		}
		// Genero alarma: Fallo de conexion con placa
		// controladora(Standby).
		gestorAlertas.insertarAlarma(2009);

	    }
	}
	sueltasemaforo(tipo);
    }

    /**
     * Desconecto el puerto serie.
     * 
     * @param tipo
     */
    public void desconectaserial(int tipo) {

	cogesemaforo(tipo);

	try {
	    if (serialPort.closePort()) {
	    }
	} catch (SerialPortException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error de Puerto Serie: " + e.getMessage());
	    }
	}
	conectado = false;
	sueltasemaforo(tipo);
    }

    /**
     * Abro valvulas, tengo en cuenta si es MCI o BT2.
     * 
     * @param valv
     * @return
     */
    public boolean abrevalv(Valvula valv) {
	logger.warn("Abro la valvula: " + valv.getCodelecvalv());

	boolean fallo = false;

	int tipo = valv.getNum_placa();

	String codelecvalv = valv.getCodelecvalv();

	// /////////////////////////////MCIs
	if (IrrisoftConstantes.PLACA_MCI_1 == tipo
		|| IrrisoftConstantes.PLACA_MCI_2 == tipo
		|| IrrisoftConstantes.PLACA_MCI_3 == tipo
		|| IrrisoftConstantes.PLACA_MCI_4 == tipo) {

	    // GONTXI
	    interruptormci(1, valv);

	    // /////////// Para las placas de Jaime
	    //
	    // codelecvalv = Integer.toString(Integer.parseInt(codelecvalv)
	    // - (tipo * 100));
	    //
	    // if (Integer.parseInt(codelecvalv) < 10)
	    // codelecvalv = "0" + codelecvalv;
	    //
	    // // TODO Para la placa de Jaime, para Gontzi¿?
	    //
	    // String comando = "01" + codelecvalv;
	    //
	    // if (logger.isInfoEnabled()) {
	    // logger.info("El comando es " + comando);
	    // // logger.warn(serialPort.getName());
	    // }
	    //
	    // // out.write(comando.getBytes());
	    //
	    // try {
	    // if (serialPort.writeBytes(comando.getBytes())) {
	    //
	    // if (logger.isInfoEnabled()) {
	    // logger.info("Comando mandado al puerto serie !"
	    // + comando);
	    // if(valv.getProgasoc() == 0){
	    // GestorAlarmas.insertarAlarma(3019);
	    // }
	    // }
	    // } else {
	    // if (logger.isErrorEnabled()) {
	    // logger.error("Fallo en mandar comando al puerto serie! "
	    // + comando);
	    // }
	    // sueltasemaforo(tipo);
	    // // RECONECTO
	    // reconecta(tipo, serialPort.getPortName());
	    //
	    // }
	    // } catch (SerialPortException e) {
	    // // TODO Auto-generated catch block
	    // e.printStackTrace();
	    // }

	    // /////////////////////////////////BT2s

	} else {
	    if (IrrisoftConstantes.PLACA_BT2_5 == tipo
		    || IrrisoftConstantes.PLACA_BT2_6 == tipo
		    || IrrisoftConstantes.PLACA_BT2_7 == tipo
		    || IrrisoftConstantes.PLACA_BT2_8 == tipo) {

		if (!valv.isAbierta()) {
		    fallo = interruptorbt2(codelecvalv, valv.getDeco(), true,
			    tipo, valv.isMaestra());
		}

	    }
	}

	// Actualizo estado de la valvula en la pasarela
	if (!fallo) {
	    // Genero alarma: Encendido manual de estacion (IRRISOFT)
	    if (valv.getProgasoc() == 0 & valv.isMaestra() == 0) {
		gestorAlertas.insertarAlarma(3019);
	    }

	    IR.hiloescucha.connDB.acualizaestvalv(valv.getCodelecvalv(), 1);
	    // IR.addvalvsabiertas(valv, valv.getNum_placa());

	    if (valv.isMaestra() == 0) {

		IR.escribetextPane("\n Valv. " + codelecvalv
			+ " abierta, prog " + valv.getProgasoc() + " \t\t\t",
			IR.normal, true);

		IR.escribelog("Valv. " + codelecvalv + " abierta.\t Prog "
			+ valv.getProgasoc() + " , ");

	    } else if (valv.isMaestra() == 1) {

		if (!valv.isAbierta()){
        		IR.escribetextPane("\n Valv. maestra " + codelecvalv
        			+ " abierta. " + "\t\t", IR.normal, true);
        
        		IR.escribelog("Valv. maestra " + codelecvalv
        			+ " abierta.\t Prog " + valv.getProgasoc() + " , ");
		}
	    }
	}
	return fallo;

    }

    /**
     * Cierro valvulas, dependiendo si es MCI o BT2.
     * 
     * @param codelecvalv
     * @param tipo
     * @return
     */
    public boolean cierravalv(String codelecvalv, int tipo) {
	logger.warn("Cierro la valvula " + codelecvalv + " del tipo: " + tipo);

	boolean estado = false;
	Valvula valv = null;

	// /////////////////////////////////////////////MCIs
	if (IrrisoftConstantes.PLACA_MCI_1 == tipo
		|| IrrisoftConstantes.PLACA_MCI_2 == tipo
		|| IrrisoftConstantes.PLACA_MCI_3 == tipo
		|| IrrisoftConstantes.PLACA_MCI_4 == tipo) {

	    // Para la placa de Jaime, para Gontzi¿?
	    if (logger.isInfoEnabled()) {
		logger.info("Cierravalv MCIIIIIII");
	    }

	    if (IrrisoftConstantes.PLACA_MCI_1 == tipo)
		valv = IR.valvsmci.getvalvmci(codelecvalv);
	    else if (IrrisoftConstantes.PLACA_MCI_2 == tipo)
		valv = IR.valvsmci2.getvalvmci(codelecvalv);
	    else if (IrrisoftConstantes.PLACA_MCI_3 == tipo)
		valv = IR.valvsmci3.getvalvmci(codelecvalv);
	    else if (IrrisoftConstantes.PLACA_MCI_4 == tipo)
		valv = IR.valvsmci4.getvalvmci(codelecvalv);

	    // GONTXI
	    interruptormci(2, valv);

	    // /////////// Para las placas de Jaime
	    // codelecvalv = Integer.toString(Integer.parseInt(codelecvalv)
	    // - (tipo * 100));
	    //
	    // if (Integer.parseInt(codelecvalv) < 10)
	    // codelecvalv = "0" + codelecvalv;
	    //
	    // String comando = "02" + codelecvalv;
	    //
	    // try {
	    // // out.write(comando.getBytes());
	    //
	    // if (serialPort.writeBytes(comando.getBytes())) {
	    // if (logger.isInfoEnabled()) {
	    // logger.info("Comando mandado al puerto serie !"
	    // + comando);
	    // //Genero alarma: Apagado manual de estacion (Irrisoft)
	    // if(valv.getProgasoc() == 0){
	    // GestorAlarmas.insertarAlarma(3020);
	    // }
	    // }
	    // } else {
	    // if (logger.isErrorEnabled()) {
	    // logger.error("Fallo en mandar comando al puerto serie! "
	    // + comando);
	    // }
	    // // sueltasemaforo(tipo);
	    // // RECONECTO
	    // reconecta(tipo, serialPort.getPortName());
	    // }
	    //
	    // if (logger.isInfoEnabled()) {
	    // logger.info("COmando cierre: " + comando);
	    // }
	    //
	    // } catch (SerialPortException e) {
	    // // logger.log(Level.WARNING, "Excepción E/S", e);
	    // e.printStackTrace();
	    // }

	    // //////////////////////////////////////////////BT2s
	} else {
	    if (IrrisoftConstantes.PLACA_BT2_5 == tipo) {
		int i = IR.valvsbt2.getvalvbt2(codelecvalv).getIndex();
		valv = IR.valvsbt2.getvalvbt2(i);
	    } else if (IrrisoftConstantes.PLACA_BT2_6 == tipo) {

		int i = IR.valvsbt22.getvalvbt2(codelecvalv).getIndex();
		valv = IR.valvsbt22.getvalvbt2(i);

	    } else if (IrrisoftConstantes.PLACA_BT2_7 == tipo) {

		int i = IR.valvsbt23.getvalvbt2(codelecvalv).getIndex();
		valv = IR.valvsbt23.getvalvbt2(i);

	    } else if (IrrisoftConstantes.PLACA_BT2_8 == tipo) {
		int i = IR.valvsbt24.getvalvbt2(codelecvalv).getIndex();
		valv = IR.valvsbt24.getvalvbt2(i);

	    }

	    if (valv.isAbierta()) {
		estado = interruptorbt2(codelecvalv, valv.getDeco(), false,
			tipo, valv.isMaestra());
	    }
	}
	logger.warn("VALOR FALLO: " + estado);
	
	if (!estado) {

	    // Genero alarma: Apagado manual de estacion (IRRISOFT)
	    if (valv.getProgasoc() == 0 & valv.isMaestra() == 0) {
		gestorAlertas.insertarAlarma(3020);
	    }
	    IR.hiloescucha.connDB.acualizaestvalv(codelecvalv, 0);

	    // IR.quitarvalvabiertas(valv, valv.getNum_placa());
	    if (valv.isMaestra() == 0) {

		IR.escribetextPane("\n Valv. " + codelecvalv
			+ " cerrada, prog " + valv.getProgasoc() + "\t\t\t",
			IR.normal, true);

		IR.escribelog("Valv. " + codelecvalv + " cerrada, prog "
			+ valv.getProgasoc() + "\t");

		// && valv.isAbierta()
	    } else if (valv.isMaestra() == 1) {
		
        		IR.escribetextPane("\n Valv. maestra " + codelecvalv
        			+ " cerrada. " + "\t\t", IR.normal, true);
        
        		IR.escribelog("Valv. maestra" + codelecvalv + " cerrada, prog "
        			+ valv.getProgasoc() + "\t");
		
	    }
	}
	return estado;

    }

    /**
     * Abro y cierro valvulas MCI.
     * 
     * @param comando
     * @param valv
     */
    public void interruptormci(int comando, Valvula valv) {

	int p2 = 0;
	byte[] buftrans = { 0, 0, 0, 0, 0, 0 };
	int numborna = Integer.parseInt(valv.getCodelecvalv()) - (tipo * 100);

	cogesemaforo(valv.getNum_placa());

	// Compruebo si es latch
	if (valv.isLatch() == 0)
	    p2 = 85;
	else
	    p2 = 170;

	// Si es una apertura o cierre
	if (comando < 3) {

	    buftrans[0] = (byte) 0x02;
	    buftrans[1] = (byte) comando;
	    buftrans[2] = (byte) numborna;
	    buftrans[3] = (byte) p2;
	    buftrans[4] = (byte) 0x00;
	    buftrans[5] = (byte) (2 + comando + numborna + p2);

	}
	// Si es un apagado
	else if (comando == 3) {
	    buftrans[0] = (byte) 0x02;
	    buftrans[1] = (byte) comando;
	    buftrans[2] = (byte) numborna;
	    buftrans[3] = (byte) p2;
	    buftrans[4] = (byte) 0x00;
	    buftrans[5] = (byte) (2 + comando + numborna + p2 + buftrans[4]);

	}

	logger.info("Comando manda a la placa de de gontxi: " + buftrans[0]
		+ ", " + buftrans[1] + ", " + buftrans[2] + ", " + buftrans[3]
		+ ", " + buftrans[4] + ", " + buftrans[5]);
	try {
	    if (serialPort.writeBytes(buftrans)) {
		if (logger.isInfoEnabled()) {
		    logger.info("Comando mandado al puerto serie !"
			    + Arrays.toString(buftrans));
		}
	    } else {
		if (logger.isErrorEnabled()) {
		    logger.error("Fallo en mandar comando al puerto serie! "
			    + Arrays.toString(buftrans));
		}

		IR.escribetextPane(
			"\nTIMEOUUUUUT en la lectura del buffer serie interruptorMCI",
			IR.normal, false);

		// RECONECTO
		// purga_puerto(tipo, serialPort.getPortName());
		respuestalost = true;
	    }

	} catch (SerialPortException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error de Puerto Serie: " + e.getMessage());
	    }
	}
	sueltasemaforo(valv.getNum_placa());

    }

    /**
     * Reseteo de la BT2.
     * 
     * @param tipo
     */
    public synchronized void reset(int tipo) {

	cogesemaforo(tipo);

	try {

	    if (serialPort.writeBytes(reset)) {
		if (logger.isInfoEnabled()) {
		    logger.info("Comando reset mandado al puerto serie !");
		}

	    } else {
		if (logger.isErrorEnabled()) {
		    logger.error("Fallo en mandar comando reset al puerto serie! ");
		}
		// RECONECTO
		// purga_puerto(tipo, serialPort.getPortName());
	    }

	    IR.panelbt2.textTx.setText(IR.getHex(reset, true));
	    IR.panelbt2.textRx.setText(IR.getHex(reset, true));
	    IR.panelbt2.lblInfo.setText("Bt2 reseteada !");
	    IR.panelbt2.lblconsum.setText("");

	} catch (SerialPortException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error de Puerto Serie: " + e.getMessage());
	    }
	} finally {

	    // // Duermo para dar tiempo a la bt a recibir otra orden
	    try {
		Thread.sleep(IrrisoftConstantes.DELAY_200MSEG);
	    } catch (InterruptedException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error, Hilo interrumpido: " + e.getMessage());
		}
	    }

	    sueltasemaforo(tipo);
	}

    }

    /**
     * Reseteo de la BT2 sin semaforo.
     * 
     * @param tipo
     */
    public synchronized void resetsinsemaforo(int tipo) {

	// RESETEO la BT2
	try {
	    if (serialPort.writeBytes(reset)) {
		if (logger.isInfoEnabled()) {
		    logger.info("Comando reset mandado al puerto serie !");
		}

	    } else {
		if (logger.isErrorEnabled()) {
		    logger.error("Fallo en mandar comando reset al puerto serie! ");
		}
		// RECONECTO
		// purga_puerto(tipo, serialPort.getPortName());
	    }
	} catch (SerialPortException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error de Puerto Serie: " + e.getMessage());
	    }
	} finally {
	    // // Duermo para dar tiempo a la bt a recibir otra orden
	    try {
		Thread.sleep(IrrisoftConstantes.DELAY_200MSEG);
	    } catch (InterruptedException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error, Hilo interrumpido :" + e.getMessage());
		}
	    }
	}

    }

    /**
     * En actuaBT2 para abrir o cerrar una valvula de BT2.
     * 
     * @param codelecvalv
     * @param dec
     * @param abrir
     * @param tipo
     * @param maes
     * @return
     */
    protected boolean interruptorbt2(String codelecvalv, int dec,
	    boolean abrir, int tipo, int maes) {
	logger.warn("Entra en actua BT2 para abrir o cerrar valvula");

	boolean fallo;

	cogesemaforo(tipo);

	TXSLV = "00";

	if (abrir) {
	    TXCMD = "03";
	} else {
	    TXCMD = "04";
	}

	// Abro el deco asociado a codelecvalv

	// Le quito el signo y convierto el deco a byte
	byte estacion = (byte) (dec & 0xFF);

	// Sumatorio checksum, le quito el signo y lo parseo a byte
	byte checksum = (byte) ((Integer.parseInt(TXCMD)
		+ Integer.parseInt(TXLEN) + dec + Integer.parseInt(TXSLV)) & 0xFF);

	byte[] buftrans = { (byte) Integer.parseInt(TXCMD),
		(byte) Integer.parseInt(TXSLV), (byte) Integer.parseInt(TXLEN),
		(byte) 0x00, estacion, (byte) 0x00, checksum };

	IR.panelbt2.textTx.setText(IR.getHex(buftrans, true));

	try {

	    if (serialPort.writeBytes(buftrans)) {
		if (logger.isInfoEnabled()) {
		    logger.info("Comando mandado al puerto serie !"
			    + serialPort.getPortName()
			    + Arrays.toString(buftrans));
		}

	    } else {
		if (logger.isErrorEnabled()) {
		    logger.error("Fallo en mandar comando al puerto serie! "
			    + Arrays.toString(buftrans));
		}
		// RECONECTO
		// reconecta(tipo, serialPort.getPortName());
	    }

	} catch (SerialPortException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error de Puerto Serie: " + e.getMessage());
	    }
	}

	fallo = leeresp(false, tipo, abrir, codelecvalv, maes);

	// Duermo para dar tiempo a la bt a recibir otra orden
	try {
	    Thread.sleep(IrrisoftConstantes.DELAY_200MSEG);
	} catch (InterruptedException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error, Hilo interrumpido: " + e.getMessage());
	    }
	}
	
	//Consultar el consumo del amperímetro
	consultconsum(serialPort, sens);
	leeresp(true, tipo, abrir, codelecvalv, maes);

	// Duermo para que le de tiempo a la BT2 a recibir otra orden
	if (maes == 1) {
	    try {
		Thread.sleep(IrrisoftConstantes.DELAY_ACTUABT_150MSEG);
	    } catch (InterruptedException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error, Hilo interrumpido: " + e.getMessage());
		}
	    }
	}

	sueltasemaforo(tipo);
	return fallo;
    }

    /**
     * Leer respuetas de las BT2 y las MCI.
     * 
     * @param consum
     * @param tipo
     * @param abrir
     * @param codelecvalv
     * @param maes
     * @return
     */
    public synchronized boolean leeresp(boolean consum, int tipo,
	    boolean abrir, String codelecvalv, int maes) {

	// Arreglar tema de que no salga maestra cerrada OK !!

	boolean fallo = false;
	respuestalost = false;
	byte[] buffer = new byte[5];

	// Reinicio los buffers
	for (int i = 0; i < buffer.length; i++) {
	    buffer[i] = 0;
	}

	for (int i = 0; i < bufferconsum.length; i++) {
	    bufferconsum[i] = 0;
	}

	// Si la placa es una BT2
	if (tipo >= 5) {
	    IR.panelbt2.repaint();

	    if (!consum) {
		IR.panelbt2.lblInfo.setText("");
		IR.panelbt2.repaint();

		// Jssc
		try {
		    buffer = serialPort.readBytes(5, 4000);
		} catch (SerialPortTimeoutException e) {
		    if (logger.isErrorEnabled()) {
			logger.error("Error tiempo lectura de Puerto Serie: "
				+ e.getMessage());
		    }

		    if (abrir) {
			if (logger.isWarnEnabled()) {
			    logger.warn("TIMEOUUUUUT en la lectura del buffer serie en apertura o cierre");
			}

			IR.escribetextPane(
				"\nTIMEOUUUUUT en la lectura del buffer serie apertura",
				IR.normal, false);

		    } else {
			if (logger.isWarnEnabled()) {
			    logger.warn("TIMEOUUUUUT en la lectura del buffer serie en cierre");
			}

			IR.escribetextPane(
				"\nTIMEOUUUUUT en la lectura del buffer serie cierre",
				IR.normal, false);

		    }

		    // RECONECTO
		    // purga_puerto(tipo, serialPort.getPortName());
		    respuestalost = true;

		} catch (SerialPortException e) {
		    if (logger.isErrorEnabled()) {
			logger.error("Error de Puerto Serie: " + e.getMessage());
		    }
		}

		// Lo paso a unsigned para que no me de valores raros negativos
		for (int i = 0; i < bufferint.length; i++) {
		    bufferint[i] = buffer[i] & 0xFF;
		}

		String s = String.format("%8s",
			Integer.toBinaryString(bufferint[3])).replace(' ', '0');

		// Estos bits de CPL son los que se utilizan para comprobar las
		// respuestas.
		logger.warn("BIT 0 de CPL: " + s.charAt(7));
		logger.warn("BIT 1 de CPL: " + s.charAt(6));
		logger.warn("BIT 2 de CPL: " + s.charAt(5));
		logger.warn("BIT 3 de CPL: " + s.charAt(4));
		logger.warn("BIT 4 de CPL: " + s.charAt(3));
		logger.info("byte respuesta cpl: " + s);

		if (s.charAt(7) == '0') {
		    fallo = true;
		    logger.warn("ERROR: Comando incorrectamente ejecutado en la valvula: "
			    + codelecvalv);
		    // Coloco en el panel la informacion, segun la respuesta del
		    // byte CPL.
		    if (!consum && abrir) {
			if (maes == 0)
			    IR.panelbt2.lblEstado
				    .setText("\nNo se ha podido abrir la valvula "
					    + codelecvalv
					    + " en la bt2 "
					    + (tipo - 4));

		    } else if (!consum && !abrir) {

			if (maes == 0)
			    IR.panelbt2.lblEstado
				    .setText("\nNo se ha podido cerrar la valvula "
					    + codelecvalv
					    + " en la bt2 "
					    + (tipo - 4));

		    }
		    // Escribo en el panel.
		    IR.escribetextPane("\nValvula: " + codelecvalv
			    + ", comando incorrectamente ejecutado por la BT2",
			    IR.normal, false);
		} else {
		    fallo = false;
		    logger.warn("CORRECTO: Comando correctamente ejecutado en la valvula: "
			    + codelecvalv);
		    // Coloco en el panel la informacion, segun la respuesta del
		    // byte CPL.
		    if (!consum && abrir) {

			if (maes == 0)
			    IR.panelbt2.lblEstado.setText("\nValvula "
				    + codelecvalv + " en la bt2 " + (tipo - 4)
				    + " abierta OK");

		    } else if (!consum && !abrir) {

			if (maes == 0)
			    IR.panelbt2.lblEstado.setText("\nValvula "
				    + codelecvalv + " en la bt2 " + (tipo - 4)
				    + "  cerrada OK");

		    }
		    // Escribo en el panel.
		    // IR.escribetextPane("\nValvula: " + codelecvalv
		    // + ", comando correctamente ejecutado por la BT2",
		    // IR.normal, false);
		}

		if (s.charAt(4) == '1') {
		    IR.panelbt2.lblInfo
			    .setText("Field line reseteable fuse has been operated");
		}
		if (s.charAt(3) == '1') {
		    IR.panelbt2.lblInfo
			    .setText("Sobrecarga en la linea > 1.6A , " + TXCMD);

		}

	    } else {

		// Jssc
		try {
		    bufferconsum = serialPort.readBytes(6, 4000);
		} catch (SerialPortException | SerialPortTimeoutException e) {
		    if (logger.isErrorEnabled()) {
			if (e instanceof SerialPortException)
			    logger.error("Error de Puerto Serie: "
				    + e.getMessage());
			else if (e instanceof SerialPortTimeoutException) {
			    logger.error("TIMEOUUUUUT en la lectura del buffer serie en consum: "
				    + e.getMessage());
			    IR.escribetextPane(
				    "\nTIMEOUUUUUT en la lectura del buffer serie consum",
				    IR.normal, false);

			}
		    }
		    // purga_puerto(tipo, serialPort.getPortName());
		    respuestalost = true;
		}

		// Lo paso a unsigned para que no me de valores raros negativos
		for (int i = 0; i < bufferconsumint.length; i++) {
		    bufferconsumint[i] = bufferconsum[i] & 0xFF;
		}

		if (logger.isInfoEnabled()) {
		    logger.info("BUFFERCONSUM: " + bufferconsum.length);
		    logger.info(bufferconsumint[0]);
		    logger.info(bufferconsumint[1]);
		    logger.info(bufferconsumint[2]);
		    logger.info(bufferconsumint[3]);
		    logger.info(bufferconsumint[4]);
		    logger.info(bufferconsumint[5]);
		}

	    }

	    // Si no hay que leer consumo
	    if (consum == false) {
		if (!respuestalost)
		    IR.panelbt2.textRx.setText(IR.getHex(buffer, true));
	    } else { // si hay que leer el consumo intensidad

		if (!respuestalost) {

		    consumo = (int) ((bufferconsumint[3] * 256 + bufferconsumint[4]));

		    if (logger.isInfoEnabled()) {
			logger.info("TIPO en leerresp: " + tipo + " "
				+ IR.panelbt2.getStnselec());
		    }

		    if (tipo == IR.panelbt2.getStnselec())
			IR.panelbt2.lblconsum
				.setText("Consumo acomulado en la línea: "
					+ consumo + " mA");

		    sens.setLectura(Integer.toString(consumo));
		}
	    }

	    // }

	}
	// Si la placa es la de sensores de gontzi 50ms entre pedir lectura y
	// leer respuesta

	else if (tipo < 5) {
	    // Leo de la placa de sensores de gontzi AMPERIOS
	    try {

		try {
		    Thread.sleep(IrrisoftConstantes.DELAY_LEERESP_50MSEG);
		} catch (InterruptedException e) {
		    if (logger.isErrorEnabled()) {
			logger.error("Error, Hilo interrumpido: "
				+ e.getMessage());
		    }
		}

		buffer = serialPort.readBytes(6, 4000);
		// Le quito el signo alos bytes
		for (int j = 0; j < buffer.length; j++) {
		    bufferconsumint[j] = buffer[j] & 0xFF;
		}

		int medicion = ((bufferconsumint[3] * 256) + bufferconsumint[2]);

		if (logger.isWarnEnabled()) {
		    logger.warn("ComandoAmpSens: " + bufferconsumint[0]);
		    logger.warn("Borna: " + bufferconsumint[1]);
		    logger.warn("Lectura1: " + bufferconsumint[2]);
		    logger.warn("Lectura2: " + bufferconsumint[3]);
		    logger.warn("Lectura3: " + bufferconsumint[4]);
		    logger.warn("Checksum: " + bufferconsumint[5]);
		}

		// Pasar el voltaje a la medida
		consumo = (int) ((((((sens.getRang_med_max() - sens
			.getRang_med_min()) / (sens.getRang_sal_max() - sens
			.getRang_sal_min()))
			* ((medicion / 1023) * 5) - sens.getRang_sal_min()) + sens
			.getRang_med_min())) * 1000 / Math.sqrt(2));

		sens.setLectura(Integer.toString(medicion));
		logger.warn("SerialDriver: " + sens.getLectura());

	    } catch (SerialPortException | SerialPortTimeoutException e) {
		if (logger.isErrorEnabled()) {
		    if (e instanceof SerialPortException)
			logger.error("Error de Puerto Serie: " + e.getMessage());
		    else if (e instanceof SerialPortTimeoutException)
			logger.error("TIMEOUUUUUT en la lectura del buffer serie: "
				+ e.getMessage());
		}
		sueltasemaforo(tipo);
	    }
	}
	return fallo;

    }

    /**
     * Consulto el consumo de la controladora o la BT2.
     * 
     * @param serial
     * @param sensor
     */
    public synchronized void consultconsum(SerialPort serial, Sensor sensor) {

	sens = sensor;

	if (logger.isWarnEnabled()) {
	    logger.warn(serial.getPortName());
	}

	// purgo el puerto para quitar posible mierdilla
	purga_puerto(this.tipo, serial.getPortName());

	// try {
	// Thread.sleep(IrrisoftConstantes.DELAY_CONSULTCON_200MSEG);
	// } catch (InterruptedException e) {
	// if(logger.isErrorEnabled()){
	// logger.error("Error, Hilo interrumpido: " +e.getMessage());
	// }
	// }

	if (serial.getPortName().contains("controladora")
		|| serial.getPortName().contains("sensores")) {

	    // TODO Aki hay que preguntarle a Gontzi

	    byte[] buftrans = { (byte) 0x01, (byte) sensor.getNum_borna(),
		    (byte) 0x00, (byte) 0x00, (byte) 0x00,
		    (byte) (1 + sensor.getNum_borna()) };

	    try {
		if (serial.writeBytes(buftrans)) {
		    if (logger.isInfoEnabled()) {
			logger.info("Comando mandado al puerto serie !"
				+ Arrays.toString(buftrans));
		    }
		} else {
		    if (logger.isErrorEnabled()) {
			logger.error("Fallo en mandar comando al puerto serie! "
				+ Arrays.toString(buftrans));
		    }

		    IR.escribetextPane(
			    "\nTIMEOUUUUUT en la lectura del buffer serie controladora/sensores consultconsum",
			    IR.normal, false);

		    // RECONECTO
		    // reconecta(tipo, serial.getPortName());
		    respuestalost = true;
		}

	    } catch (SerialPortException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error de Puerto Serie: " + e.getMessage());
		}
	    }

	} else if (serial.getPortName().contains("ftdi")) {

	    byte[] buftrans = { (byte) 0x0B, (byte) 0x00, (byte) 0x01,
		    (byte) 0x0C };

	    try {

		if (serial.writeBytes(buftrans)) {
		    if (logger.isInfoEnabled()) {
			logger.info("Comando mandado al puerto serie !"
				+ Arrays.toString(buftrans));
		    }
		} else {
		    if (logger.isErrorEnabled()) {
			logger.error("Fallo en mandar comando al puerto serie! "
				+ Arrays.toString(buftrans));
		    }
		    IR.escribetextPane(
			    "\nTIMEOUUUUUT en la lectura del buffer serie ftdi consultconsum",
			    IR.normal, false);

		    // RECONECTO
		    // reconecta(tipo, serial.getPortName());
		    respuestalost = true;
		}

	    } catch (SerialPortException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error de Puerto Serie: " + e.getMessage());
		}
	    }
	}
    }

    /**
     * Reconecto el puerto de las BT2, lo que hago es purgar el puerto.
     * 
     * @param tipop
     * @param puerto
     */
    public void purga_puerto(int tipop, String puerto) {
	if (logger.isInfoEnabled()) {
	    logger.info("Purgo el puerto: " + puerto);
	}
	// si es una bt2
	// if (tipop > 4)
	// resetsinsemaforo(tipo);

	// purgo el puerto para quitar posible mierdilla
	try {
	    serialPort.purgePort(SerialPort.PURGE_RXCLEAR
		    | SerialPort.PURGE_TXCLEAR);
	} catch (SerialPortException e1) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error de Puerto Serie: " + e1.getMessage());
	    }
	}
    }

    /**
     * Activo la BT2.
     * 
     * @param serial
     * @param sens
     */
    public void activabt(SerialPort serial, Sensor sens) {

	byte[] buftrans = { (byte) 0x08, (byte) 0x00, (byte) 0x01, (byte) 0x09 };
	cogesemaforo(this.tipo);

	// purgo el puerto para quitar posible mierdilla
	purga_puerto(this.tipo, serial.getPortName());

	try {

	    // Pregunto la versión del firmware de la bt y lo pongo en el panel
	    // bt2

	    if (serial.writeBytes(buftrans)) {
		if (logger.isInfoEnabled()) {
		    logger.info("Comando mandado al puerto serie !"
			    + Arrays.toString(buftrans));
		}

	    } else {
		if (logger.isErrorEnabled()) {
		    logger.error("Fallo en mandar comando al puerto serie! "
			    + Arrays.toString(buftrans));
		}
		IR.escribetextPane(
			"\nTIMEOUUUUUT en la lectura del buffer serie versión firmware",
			IR.normal, false);

		// RECONECTO
		// reconecta(tipo, serial.getPortName());

	    }

	    byte[] bufferver = serial.readBytes(5, 9000);
	    int vers = bufferver[3] & 0xFF;

	    if (logger.isInfoEnabled()) {
		logger.info("VERSION FIRM: " + vers);
	    }

	    ver = Integer.toString(vers);
	    ver = ver.substring(0, 1) + "." + ver.substring(1, 2);

	    // Duermo para dar tiempo a la bt a recibir otra orden
	    try {
		Thread.sleep(IrrisoftConstantes.DELAY_ACTIVABT_60MSEG);
	    } catch (InterruptedException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error, Hilo interrumpido: " + e.getMessage());
		}
	    }

	    // Activo la línea de la BT
	    consultconsum(serial, sens);
	    bufferconsum = serial.readBytes(6, 10000);

	} catch (SerialPortException | SerialPortTimeoutException e) {
	    if (logger.isErrorEnabled()) {
		if (e instanceof SerialPortException)
		    logger.error("Error de Puerto Serie: " + e.getMessage());
		else if (e instanceof SerialPortTimeoutException) {
		    logger.error("TIMEOUUUT de tiempo de lectura de Puerto Serie activabt: "
			    + e.getMessage());
		    IR.escribetextPane(
			    "\nTIMEOUUUUUT en la lectura de activabt",
			    IR.normal, false);
		}
		// RECONECTO
		purga_puerto(this.tipo, serialPort.getPortName());
		respuestalost = true;
	    }
	} finally {
	    sueltasemaforo(this.tipo);
	}

    }

    /**
     * Pregunto que valvulas de la BT2 estan abiertas.
     * 
     * @param serialp
     * @param tipo
     */
    public void cualesabiertasbt(SerialPort serialp, int tipo) {

	byte slot = 0;
	int chk = 0;
	String valvsopen = "";
	boolean sinopen = false;
	byte[] buffer = new byte[5];

	cogesemaforo(tipo);

	for (int i = 0; i < 10; i++) {

	    // Duermo para dar tiempo a la bt a recibir otra orden
	    try {
		Thread.sleep(IrrisoftConstantes.DELAY_200MSEG);
	    } catch (InterruptedException e) {
		if (logger.isErrorEnabled()) {
		    logger.error(e.getMessage());
		}
	    }

	    slot = (byte) i;
	    chk = 21 + 2 + slot;

	    byte[] buftrans = { (byte) 0x15, (byte) 0x00, (byte) 0x02,
		    (byte) slot, (byte) chk };

	    try {

		if (serialp.writeBytes(buftrans)) {
		    if (logger.isInfoEnabled()) {
			logger.info("Comando mandado a cualesAbiertasbt al puerto serie !"
				+ Arrays.toString(buftrans));
		    }

		} else {
		    if (logger.isErrorEnabled()) {
			logger.error("Fallo en mandar comando a cualesaAbiertasbt al puerto serie! "
				+ Arrays.toString(buftrans));
		    }
		    IR.escribetextPane(
			    "\nTIMEOUUUUUT en la lectura de cualesAbiertasbt",
			    IR.normal, false);

		    // RECONECTO
		    // purga_puerto(tipo, serialp.getPortName());
		    respuestalost = true;
		}

	    } catch (SerialPortException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error de Puerto Serie: " + e.getMessage());
		}
	    }

	    // Leo la respuesta
	    try {
		buffer = serialp.readBytes(6, 9000);
	    } catch (SerialPortTimeoutException | SerialPortException e) {
		if (logger.isErrorEnabled()) {
		    if (e instanceof SerialPortTimeoutException) {
			logger.error("TIMEOUUUUUT en la lectura del buffer serie en cualesAbiertasbt: "
				+ e.getMessage());
			IR.escribetextPane(
				"\nTIMEOUUUUUT en la lectura de cualesAbiertasbt",
				IR.normal, false);
		    } else if (e instanceof SerialPortException) {
			logger.error("Error de Puerto Serie: " + e.getMessage());
		    }
		}
		// purga_puerto(tipo, serialp.getPortName());
		break;
	    }

	    // Si no hay ninguna abierta salgo del bucle
	    if ((buffer[3] == 0 && buffer[4] == 0)) {

		// Info
		if (i == 0) {
		    JOptionPane.showMessageDialog(IR.frmIrrisoft,
			    "No hay ninguna estación abierta\n\n",
			    "Estaciones abiertas?",
			    JOptionPane.INFORMATION_MESSAGE);
		    sinopen = true;
		}
		break;
	    }

	    if (logger.isInfoEnabled()) {

		logger.info("VALVULAS ABIERTAS en slot " + slot + ": "
			+ buffer[3] + " , " + buffer[4]);
	    }
	    Valvula valv = new Valvula();

	    if (IrrisoftConstantes.PLACA_BT2_5 == tipo)
		valv = IR.valvsbt2.getvalvbt2(tipo, buffer[4]);
	    else if (IrrisoftConstantes.PLACA_BT2_6 == tipo)
		valv = IR.valvsbt22.getvalvbt2(tipo, buffer[4]);
	    else if (IrrisoftConstantes.PLACA_BT2_7 == tipo)
		valv = IR.valvsbt23.getvalvbt2(tipo, buffer[4]);
	    else if (IrrisoftConstantes.PLACA_BT2_8 == tipo)
		valv = IR.valvsbt24.getvalvbt2(tipo, buffer[4]);

	    valv.setAbierta(true);
	    valvsopen = valvsopen
		    + ("         " + valv.getCodelecvalv() + " deco " + valv
			    .getDeco()) + "\n";

	}

	// Info
	if (!sinopen)
	    JOptionPane.showMessageDialog(IR.frmIrrisoft, valvsopen + "\n\n",
		    "Estaciones abiertas?", JOptionPane.INFORMATION_MESSAGE);

	sueltasemaforo(tipo);

    }

    /**
     * Cierra las valvulas abiertas de la bt2 al principio.
     * 
     * @param serialp
     * @param tipo
     */
    public void cerrarAbiertasAlInicio(SerialPort serialp, int tipo) {

	byte slot = 0;// Hay 10 slots
	int chk = 0; // Valor del checksum
	// String valvsopen = "";
	// boolean sinopen = false;
	boolean señal = true; // CAMBIO
	int i = 0;// CAMBIO
	byte[] buffer = new byte[5];
	byte decoAux = 0;// CAMBIO

	// purgo el puerto para quitar posible mierdilla
	purga_puerto(tipo, serialp.getPortName());

	// Reviso los 10 slots
	// for (int i = 9; i>=0; i--) {
	// Para las MCIs, todavia sin implementar.
	if (IrrisoftConstantes.PLACA_MCI_1 == tipo
		|| IrrisoftConstantes.PLACA_MCI_2 == tipo
		|| IrrisoftConstantes.PLACA_MCI_3 == tipo
		|| IrrisoftConstantes.PLACA_MCI_4 == tipo) {

	    String comando = "0";

	    try {
		if (serialp.writeBytes(comando.getBytes())) {
		    if (logger.isInfoEnabled()) {
			logger.info("Comando mandado al puerto serie !"
				+ Arrays.toString(comando.getBytes()));
		    }
		} else {
		    if (logger.isErrorEnabled()) {
			logger.error("Fallo en mandar comando al puerto serie! "
				+ Arrays.toString(comando.getBytes()));
		    }
		    // Reconecto el puerto serie
		    // reconecta(tipo, serialp.getPortName());

		}

	    } catch (SerialPortException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error de Puerto Serie: " + e.getMessage());
		}
	    }

	} else if (IrrisoftConstantes.PLACA_BT2_5 == tipo
		|| IrrisoftConstantes.PLACA_BT2_6 == tipo
		|| IrrisoftConstantes.PLACA_BT2_7 == tipo
		|| IrrisoftConstantes.PLACA_BT2_8 == tipo) {

	    while (señal) { // CAMBIO

		// Duermo para dar tiempo a la bt a recibir otra orden
		try {
		    Thread.sleep(IrrisoftConstantes.DELAY_200MSEG * 2);
		} catch (InterruptedException e) {
		    if (logger.isErrorEnabled()) {
			logger.error("Error, Hilo interrumpido: "
				+ e.getMessage());
		    }
		}

		cogesemaforo(tipo);
		slot = (byte) i;
		chk = 21 + 2 + slot;

		byte[] buftrans = { (byte) 0x15, (byte) 0x00, (byte) 0x02,
			(byte) slot, (byte) chk };

		try {
		    if (serialp.writeBytes(buftrans)) {

			if (logger.isInfoEnabled()) {
			    logger.info("Comando mandado a cualesAbiertasInicio al puerto serie !"
				    + Arrays.toString(buftrans));
			}

		    } else {
			if (logger.isErrorEnabled()) {
			    logger.error("Fallo en mandar comando a cualesAbiertasInicio al puerto serie! "
				    + Arrays.toString(buftrans));
			}
			IR.escribetextPane(
				"\nTIMEOUUUUUT en la lectura de cualesabiertasinicio",
				IR.normal, false);

			// RECONECTO
			purga_puerto(tipo, serialp.getPortName());
			respuestalost = true;
		    }

		} catch (SerialPortException e) {
		    if (logger.isErrorEnabled()) {
			logger.error("Error de Puerto Serie: " + e.getMessage());
		    }
		    sueltasemaforo(tipo);
		    break;

		}

		// Leo la respuesta
		try {
		    buffer = serialp.readBytes(6, 9000);

		    if (buffer[4] != decoAux) {
			decoAux = buffer[4];
		    } else {
			señal = false;
		    }

		} catch (SerialPortTimeoutException | SerialPortException e) {
		    if (logger.isErrorEnabled()) {
			if (e instanceof SerialPortTimeoutException) {
			    logger.error("TIMEOUUUUUT en la lectura del buffer serie en cualesAbiertasInicio: "
				    + e.getMessage());
			    IR.escribetextPane(
				    "\nTIMEOUUUUUT en la lectura de cualesAbiertasInicio",
				    IR.normal, false);
			} else if (e instanceof SerialPortException) {
			    logger.error("Error de Puerto Serie: "
				    + e.getMessage());
			}
		    }
		    purga_puerto(tipo, serialp.getPortName());
		    sueltasemaforo(tipo);

		    break;
		}

		sueltasemaforo(tipo);

		// Aki cierro las electroválvulas que la bt2 me dice que hay
		// abiertas
		if (buffer[4] != 0) {

		    Valvula valv = new Valvula();

		    if (IrrisoftConstantes.PLACA_BT2_5 == tipo) {
			valv = IR.valvsbt2.getvalvbt2(tipo, buffer[4]);
			if (valv != null) {
			    valv.setAbierta(true);
			    cierravalv(valv.getCodelecvalv(), tipo);
			    valv.setAbierta(false);
			}
		    } else if (IrrisoftConstantes.PLACA_BT2_6 == tipo) {
			valv = IR.valvsbt22.getvalvbt2(tipo, buffer[4]);
			if (valv != null) {
			    valv.setAbierta(true);
			    cierravalv(valv.getCodelecvalv(), tipo);
			    valv.setAbierta(false);
			}
		    } else if (IrrisoftConstantes.PLACA_BT2_7 == tipo) {
			valv = IR.valvsbt23.getvalvbt2(tipo, buffer[4]);
			if (valv != null) {
			    valv.setAbierta(true);
			    cierravalv(valv.getCodelecvalv(), tipo);
			    valv.setAbierta(false);
			}
		    } else if (IrrisoftConstantes.PLACA_BT2_8 == tipo) {
			valv = IR.valvsbt24.getvalvbt2(tipo, buffer[4]);
			if (valv != null) {
			    valv.setAbierta(true);
			    cierravalv(valv.getCodelecvalv(), tipo);
			    valv.setAbierta(false);
			}
		    }
		}
	    }
	}

    }

    /**
     * Cojo el semaforo.
     * 
     * @param tipo
     */
    public void cogesemaforo(int tipo) {
	logger.warn("Coge el semaforo");

	try {
	    if (IrrisoftConstantes.PLACA_MCI_1 == tipo) {
		IR.semaforomci.take();
	    } else if (IrrisoftConstantes.PLACA_MCI_2 == tipo) {
		IR.semaforomci2.take();
	    } else if (IrrisoftConstantes.PLACA_MCI_3 == tipo) {
		IR.semaforomci3.take();
	    } else if (IrrisoftConstantes.PLACA_MCI_4 == tipo) {
		IR.semaforomci4.take();
	    } else if (IrrisoftConstantes.PLACA_BT2_5 == tipo) {
		IR.semaforobt2.take();
	    } else if (IrrisoftConstantes.PLACA_BT2_6 == tipo) {
		IR.semaforobt22.take();
	    } else if (IrrisoftConstantes.PLACA_BT2_7 == tipo) {
		IR.semaforobt23.take();
	    } else if (IrrisoftConstantes.PLACA_BT2_8 == tipo) {
		IR.semaforobt24.take();
	    } else if (IrrisoftConstantes.PLACA_SENSORES_0 == tipo) {
		IR.semaforosens.take();
	    } else if (IrrisoftConstantes.PLACA_SAMCLA == tipo) {
		IR.semaforosamcla.take();
	    }

	} catch (InterruptedException e1) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error, Hilo interrumpido: " + e1.getMessage());
	    }
	    return;
	}

    }

    /**
     * Suelto el semaforo.
     * 
     * @param tipo
     */
    public void sueltasemaforo(int tipo) {
	logger.warn("Suelta el semaforo");

	// // Duermo para dar tiempo a la bt a recibir otra orden
	try {
	    Thread.sleep(IrrisoftConstantes.DELAY_SOLTAR_SEMAFORO);
	} catch (InterruptedException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error, Hilo interrumpido: " + e.getMessage());
	    }
	}

	try {
	    if (IrrisoftConstantes.PLACA_MCI_1 == tipo) {
		IR.semaforomci.release();
	    } else if (IrrisoftConstantes.PLACA_MCI_2 == tipo) {
		IR.semaforomci2.release();
	    } else if (IrrisoftConstantes.PLACA_MCI_3 == tipo) {
		IR.semaforomci3.release();
	    } else if (IrrisoftConstantes.PLACA_MCI_4 == tipo) {
		IR.semaforomci4.release();
	    } else if (IrrisoftConstantes.PLACA_BT2_5 == tipo) {
		IR.semaforobt2.release();
	    } else if (IrrisoftConstantes.PLACA_BT2_6 == tipo) {
		IR.semaforobt22.release();
	    } else if (IrrisoftConstantes.PLACA_BT2_7 == tipo) {
		IR.semaforobt23.release();
	    } else if (IrrisoftConstantes.PLACA_BT2_8 == tipo) {
		IR.semaforobt24.release();
	    } else if (IrrisoftConstantes.PLACA_SENSORES_0 == tipo) {
		IR.semaforosens.release();
	    } else if (IrrisoftConstantes.PLACA_SAMCLA == tipo) {
		IR.semaforosamcla.release();
	    }
	} catch (InterruptedException e1) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error, Hilo interrumpido: " + e1.getMessage());
	    }
	    return;
	}
    }

    // ////////////////
    // GETTER Y SETTER
    // ////////////////
    public void setSerialPort(SerialPort serial) throws NullPointerException {
	if (serial != null) {
	    this.serialPort = serial;
	} else
	    throw new NullPointerException("Puerto serie no encontrado");
    }

}
