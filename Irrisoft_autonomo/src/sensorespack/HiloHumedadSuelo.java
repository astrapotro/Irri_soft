package sensorespack;

import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;
import irrisoftpack.SerialDriver;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import alertaspack.GestorAlertas;
import valvulaspack.Valvula;

public class HiloHumedadSuelo extends Sensor implements Runnable,
	PropertyChangeListener {

    private static Logger logger = LogManager.getLogger(HiloHumedadSuelo.class
	    .getName());

    // protected SerialPort serialPort;
    // protected OutputStream out;
    // protected InputStream in;
    protected SerialDriver serialcon;
    // protected String puerto;
    protected int len, leo;
    protected Sensor sens;
    protected ArrayList<Valvula> valvulas;
    protected byte[] bufferresp = new byte[6];
    private boolean respuestalost = false;
    private int[] bufferrespsinsigno = new int[6];
    private GestorAlertas gestorAlertas;
    private Irrisoft IR;
    private boolean rearmarHum = false;

    public HiloHumedadSuelo(SerialDriver serialDriver, Sensor sens) {
	this.serialcon = serialDriver;
	this.sens = sens;
	gestorAlertas = GestorAlertas.getInstance();
	this.IR = Irrisoft.window;
    }

    /**
     * Eligo si va para la placas MCI y BT2.
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {

	while (true) {
	    if (rearmarHum) {
		logger.warn("Termino el hilo de Humedad porque se ha rearmado Irrisoft");
		return;
	    }

	    try {
		if (logger.isInfoEnabled()) {
		    logger.info(("Estoy en hilohumedadsuelo"));
		}
		// TODO Tiempo variable cambiarlo por tiempo de lectura
		// Thread.sleep(sens.getFrec_lect()*1000);
		Thread.sleep(IrrisoftConstantes.DELAY_SENSOR_30SEG);

	    } catch (InterruptedException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Hilo interrumpido:" +e.getMessage());
		}
	    }

	    if (sens.getNum_placa() > 4)
		leehumedadbt();
	    else
		leehumedadgon();

	}

    }

    /**
     * Lee la humedad para una MCI.
     */
    private void leehumedadgon() {

	int tiemporegado = 0;
	
	byte[] buftrans = { (byte) 0x01, (byte) sens.getNum_borna(),
		(byte) 0x00, (byte) 0x00, (byte) 0x00,
		(byte) (1 + sens.getNum_borna()) };

	if (logger.isInfoEnabled()) {
	    logger.info("Hola toy en leehumedad GON " + sens.getNum_placa());
	}

	respuestalost = false;

	try {
	    serialcon.cogesemaforo(sens.getNum_placa());
	    if (serialcon.serialPort.writeBytes(buftrans)) {
		if (logger.isInfoEnabled()) {
		    logger.info("Comando mandado al puerto serie !"
			    + Arrays.toString(buftrans)); // buftrans.toString());
		}
	    } else {
		if (logger.isErrorEnabled()) {
		    logger.error("Fallo en mandar comando al puerto serie! "
			    + Arrays.toString(buftrans)); // buftrans.toString());
		}
		// RECONECTO
		// serialcon.purga_puerto(sens.getNum_placa(),
		// serialcon.serialPort.getPortName());
		respuestalost = true;
	    }

	    bufferresp = serialcon.serialPort.readBytes(6, 4000);

	} catch (SerialPortException | SerialPortTimeoutException e) {
	    if (logger.isErrorEnabled()) {
		if(e instanceof SerialPortException)
		logger.error(e.getMessage());
		else if(e instanceof SerialPortTimeoutException){
		    logger.error("TIMEOUUUUUT en la lectura del buffer serie humedad: " +e.getMessage());
		    IR.escribetextPane(
			    "\nTIMEOUUUUUT en la lectura del buffer serie humedad",
			    IR.normal, false);
		    // RECONECTO
		    // serialcon.purga_puerto(sens.getNum_placa(),
		    // serialcon.serialPort.getPortName());
		    respuestalost = true;
		}
	    }
	}finally {

	    serialcon.sueltasemaforo(sens.getNum_placa());

	}

	// Le quito el signo alos bytes
	for (int j = 0; j < bufferresp.length; j++) {

	    bufferrespsinsigno[j] = bufferresp[j] & 0xFF;

	}

	if (logger.isInfoEnabled()) {
	    logger.info("Comando: " + bufferrespsinsigno[0]);
	    logger.info("Borna: " + bufferrespsinsigno[1]);
	    logger.info("Lectura1: " + bufferrespsinsigno[2]);
	    logger.info("Lectura2: " + bufferrespsinsigno[3]);
	    logger.info("Lectura3: " + bufferrespsinsigno[4]);
	    logger.info("Checksum: " + bufferrespsinsigno[5]);
	}

	int medicion = ((bufferrespsinsigno[3] * 256) + bufferrespsinsigno[2]);

	// Voltaje de referencia tiene que ir al sensor, ahora a mano 4.998
	double voltaje = ((double) medicion / 1023) * 4.998;

	// Pasar el voltaje a la medida
	double medida = ((sens.getRang_med_max() - sens.getRang_med_min()) / (sens
		.getRang_sal_max() - sens.getRang_sal_min()))
		* (voltaje - sens.getRang_sal_min()) + sens.getRang_med_min();

	voltaje = Math.rint(voltaje * 100) / 100;
	medida = Math.rint(medida * 10) / 10;

	logger.info("Medición: " + medicion);
	logger.info("Voltaje: " + voltaje);

	// Meto las lecturas al sensor
	sens.setVoltaje(voltaje);
	sens.setLectura(Double.toString(medida));

	// Escribo en el panel la lectura
	// ponelecturas(sens);

	if (!respuestalost) {

	    if (logger.isInfoEnabled()) {
		logger.info("La humedad del suelo es de : " + medida + " %");
	    }
	    // Compruebo si la humedad está en el rango adecuado
	    if (medida < sens.getMed_umbral_min()) {

		if (logger.isWarnEnabled()) {
		    logger.warn("La humedad es menor que la apropiada para la planta!!");
		}
		// Genero alarma: Humedad de suelo escasa en estaciones.
		gestorAlertas.insertarAlarma(3011);
		// Abro las valvs asociadas
		interruptor(1);

		// Lo sigo regando mientras no haya alcanzado la humedad
		// adecuada o mientras no haya regado el tiempo max
		while (medida < sens.getMed_umbral_max()) {

		    if ((sens.getT_max_riego() <= tiemporegado)
			    || medida > sens.getMed_umbral_max()) {
			// Genero alarma: Sensor de humedad tiemp max de riego
			// alcanzado.
			gestorAlertas.insertarAlarma(3033);
			break;
		    } else {
			// Duermo 20seg
			try {
			    Thread.sleep(IrrisoftConstantes.DELAY_SENSOR_20SEG);
			} catch (InterruptedException e) {
			    if (logger.isErrorEnabled()) {
				logger.error("Hilo interrumpido: " +e.getMessage());
			    }
			}
			// Leo la homedad
			try {

			    try {
				serialcon.cogesemaforo(sens.getNum_placa());
				if (serialcon.serialPort.writeBytes(buftrans)) {

				    if (logger.isInfoEnabled()) {
					logger.info("Comando mandado al puerto serie !"
						+ Arrays.toString(buftrans)); // buftrans.toString());
				    }
				} else {
				    if (logger.isErrorEnabled()) {
					logger.error("Fallo en mandar comando al puerto serie! "
						+ Arrays.toString(buftrans)); // buftrans.toString());
				    }

				    // RECONECTO
				    // serialcon.purga_puerto(tipoplaca,
				    // serialcon.serialPort.getPortName());
				}

				bufferresp = serialcon.serialPort.readBytes(6,
					4000);

				// Le quito el signo alos bytes
				for (int j = 0; j < bufferresp.length; j++) {

				    bufferrespsinsigno[j] = bufferresp[j] & 0xFF;

				}

				medicion = (bufferrespsinsigno[3] * 256)
					+ bufferrespsinsigno[2];

				// Voltaje de referencia tiene que ir al sensor,
				// ahora a mano 4.998
				voltaje = ((double) medicion / 1023) * 4.998;

				// Pasar el voltaje a la medida
				medida = ((sens.getRang_med_max() - sens
					.getRang_med_min()) / (sens
					.getRang_sal_max() - sens
					.getRang_sal_min()))
					* (voltaje - sens.getRang_sal_min())
					+ sens.getRang_med_min();

				voltaje = Math.rint(voltaje * 100) / 100;
				medida = Math.rint(medida * 10) / 10;

				logger.info("Medición: " + medicion);
				logger.info("Voltaje: " + voltaje);

				// Meto las lecturas al sensor
				sens.setVoltaje(voltaje);
				sens.setLectura(Double.toString(medida));

				// Escribo en el panel la lectura
				// ponelecturas(sens);

			    } catch (SerialPortException | SerialPortTimeoutException e1) {
				if (logger.isErrorEnabled()) {
				    if(e1 instanceof SerialPortException)
				    logger.error(e1.getMessage());
				    else if(e1 instanceof SerialPortTimeoutException){
					logger.warn("TIMEOUUUUUT en la lectura del buffer serie: " +e1.getMessage());
					IR.escribetextPane("\nTIMEOUUUUUT en la lectura del buffer serie humedad",
						IR.normal, false);
				    }
				}
			    } finally {
				serialcon.sueltasemaforo(sens.getNum_placa());
			    }

			} catch (Exception e) {
			    if (logger.isErrorEnabled()) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			    }
			}
		    }
		    tiemporegado = tiemporegado + 20;
		    if (logger.isInfoEnabled()) {
			logger.info("Humedad de suelo: " + medida + "%");
		    }
		}
		// Cierro las valvs asociadas
		interruptor(2);
	    }
	}
    }

    /**
     * Lee la humedad para una BT2.
     */
    public void leehumedadbt() {

	int tiemporegado = 0;

	// TODO El byte 5 se ha de poner con sens.getnumborna !!!
	byte[] buftrans = { (byte) 0x06, (byte) 0x00, (byte) 0x03, (byte) 0x00,
		(byte) sens.getNum_borna(),
		(byte) (6 + 3 + sens.getNum_borna()) };

	if (logger.isInfoEnabled()) {
	    logger.info("Hola toy en leehumedad BT" + sens.getNum_placa());
	}

	respuestalost = false;

	try {
	    serialcon.cogesemaforo(sens.getNum_placa());
	    if (serialcon.serialPort.writeBytes(buftrans)) {
		if (logger.isInfoEnabled()) {
		    logger.info("Comando mandado al puerto serie !"
			    + Arrays.toString(buftrans)); // buftrans.toString());
		}
	    } else {
		if (logger.isErrorEnabled()) {
		    logger.error("Fallo en mandar comando al puerto serie! "
			    + Arrays.toString(buftrans)); // buftrans.toString());
		}
		// RECONECTO
		// serialcon.purga_puerto(sens.getNum_placa(),
		// serialcon.serialPort.getPortName());
		respuestalost = true;
	    }

	    bufferresp = serialcon.serialPort.readBytes(6, 4000);

	    // Reseteo la bt2
	    // serialcon.resetsinsemaforo(sens.getNum_placa());

	} catch (SerialPortException | SerialPortTimeoutException e) {
	    if (logger.isErrorEnabled()) {
		if(e instanceof SerialPortException)
		logger.error(e.getMessage());
		else if(e instanceof SerialPortTimeoutException){
		    logger.error("TIMEOUUUUUT en la lectura del buffer serie humedad: " +e.getMessage());
		    IR.escribetextPane(
			    "\nTIMEOUUUUUT en la lectura del buffer serie humedad",IR.normal, false);
		    respuestalost = true;
		}
	    }
	}finally {

	    // RECONECTO
	    // serialcon.reconecta
	    // (sens.getNum_placa(),serialcon.serialPort.getPortName());
	    serialcon.sueltasemaforo(sens.getNum_placa());

	}

	if (!respuestalost) {

	    if (logger.isInfoEnabled()) {
		logger.info("La humedad del suelo es de : " + bufferresp[4]
			+ " %");
	    }
	    sens.setLectura(Integer.toString(bufferresp[4]));
	    // Compruebo si la humedad está en el rango adecuado
	    if (bufferresp[4] < sens.getMed_umbral_min()) {

		if (logger.isWarnEnabled()) {
		    logger.warn("La humedad es menor que la apropiada para la planta!!");
		}
		// Genero alarma: Humedad de suelo escaso en estaciones.
		gestorAlertas.insertarAlarma(3011);
		// Abro las valvs asociadas
		interruptor(1);

		// Lo sigo regando mientras no haya alcanzado la humedad
		// adecuada o mientras no haya regado el tiempo max
		while (bufferresp[4] < sens.getMed_umbral_max()) {
		    logger.warn(tiemporegado);
		    if ((sens.getT_max_riego() <= tiemporegado)
			    || (bufferresp[4] > sens.getMed_umbral_max())) {
			// Genero alarma: Sensor de humedad tiempo max de riego.
			gestorAlertas.insertarAlarma(3033);
			break;
		    } else {
			// Duermo 20seg
			try {
			    Thread.sleep(IrrisoftConstantes.DELAY_SENSOR_20SEG);
			} catch (InterruptedException e) {
			    if (logger.isErrorEnabled()) {
				logger.error(e.getMessage());
			    }
			}

			// Leo la homedad
			try {

			    try {
				serialcon.cogesemaforo(sens.getNum_placa());
				if (serialcon.serialPort.writeBytes(buftrans)) {

				    if (logger.isInfoEnabled()) {
					logger.info("Comando mandado al puerto serie !"
						+ Arrays.toString(buftrans)); 
				    }
				} else {
				    if (logger.isErrorEnabled()) {
					logger.error("Fallo en mandar comando al puerto serie! "
						+ Arrays.toString(buftrans)); 
				    }
				    // serialcon.sueltasemaforo(sens.getNum_placa());
				    // RECONECTO
				    // serialcon.purga_puerto(sens.getNum_placa(),
				    // serialcon.serialPort.getPortName());
				}

				bufferresp = serialcon.serialPort.readBytes(6,
					4000);

			    } catch (SerialPortException | SerialPortTimeoutException e) {
				if (logger.isErrorEnabled()) {
				    if(e instanceof SerialPortException)
				    logger.error(e.getMessage());
				    else if(e instanceof SerialPortTimeoutException){
					logger.error("TIMEOUUUUUT en la lectura del buffer serie: " +e.getMessage());
					IR.escribetextPane("\nTIMEOUUUUUT en la lectura del buffer serie humedad",IR.normal, false);
				    }
				}
			    }finally {

				// RECONECTO
				// serialcon.reconecta
				// (sens.getNum_placa(),serialcon.serialPort.getPortName());
				serialcon.sueltasemaforo(sens.getNum_placa());
			    }
			} catch (Exception e) {
			    if (logger.isErrorEnabled()) {
				logger.error(e.getMessage());
				logger.error(e.getStackTrace());
			    }
			}
		    }
		    tiemporegado = tiemporegado + 20;
		    if (logger.isInfoEnabled()) {
			logger.info("Humedad de suelo: " + bufferresp[4] + "%");
		    }
		    sens.setLectura(Integer.toString(bufferresp[4]));
		}
		// Cierro las valvs asociadas
		interruptor(2);

	    }

	}
    }

    /**
     * Para abrir o cerrar las válvulas asociadas
     * @param accion
     */
    private void interruptor(int accion) {

	Valvula valvu = new Valvula();

	if (accion == 1) {

	    IR.escribetextPane(
		    "\n La tierra está seca!!\n Se abrirán las válvulas asociadas:\n  "
			    + sens.getNum_est_prop() + ","
			    + sens.getNum_est_asoc(), IR.normal, false);

	} else
	    IR.escribetextPane(
		    "\n La tierra ya está húmeda!!\n Se cerrarán las válvulas asociadas:\n  "
			    + sens.getNum_est_prop() + ","
			    + sens.getNum_est_asoc(), IR.normal, false);

	// Abrevalvulas
	for (int i = 0; i < sens.getValvsassoc().size(); i++) {

	    int valv = sens.getValvsassoc().get(i);

	    if (logger.isInfoEnabled()) {
		logger.info("Valvula asociada: " + valv);
	    }

	    if (valv > 0) {
		// La valvula es MCI
		if (IrrisoftConstantes.BT2_1000 > valv) {

		    if (IrrisoftConstantes.MCI_200 > valv) {
			valvu = IR.valvsmci.getvalvmci(valv - 101);
			// if (!valvu.isAbierta())
			IR.panelmci.interruptor(valvu, valvu.getImgasoc(),
				IR.panelmci.panel1);
		    } else if (IrrisoftConstantes.MCI_200 < valv
			    && IrrisoftConstantes.MCI_300 > valv) {
			valvu = IR.valvsmci2.getvalvmci(valv - 201);
			// if (!valvu.isAbierta())
			IR.panelmci.interruptor(valvu, valvu.getImgasoc(),
				IR.panelmci.panel2);
		    } else if (IrrisoftConstantes.MCI_300 < valv
			    && IrrisoftConstantes.MCI_400 > valv) {
			valvu = IR.valvsmci3.getvalvmci(valv - 301);
			// if (!valvu.isAbierta())
			IR.panelmci.interruptor(valvu, valvu.getImgasoc(),
				IR.panelmci.panel3);
		    } else if (IrrisoftConstantes.MCI_400 < valv) {
			valvu = IR.valvsmci4.getvalvmci(valv - 401);
			// if (!valvu.isAbierta())
			IR.panelmci.interruptor(valvu, valvu.getImgasoc(),
				IR.panelmci.panel4);
		    }
		}

		// La valvula es BT2
		else if (IrrisoftConstantes.BT2_1000 < valv) {

		    if (IrrisoftConstantes.BT2_2000 > valv) {
			valvu = IR.valvsbt2.getvalvbt2(Integer.toString(valv));
			// if (!valvu.isAbierta())
			IR.panelbt2.interruptor(valvu, accion, 5);
		    } else if (IrrisoftConstantes.BT2_2000 < valv
			    && IrrisoftConstantes.BT2_3000 > valv) {
			valvu = IR.valvsbt22.getvalvbt2(Integer.toString(valv));
			// if (!valvu.isAbierta())
			IR.panelbt2.interruptor(valvu, accion, 6);
		    } else if (IrrisoftConstantes.BT2_3000 < valv
			    && IrrisoftConstantes.BT2_4000 > valv) {
			valvu = IR.valvsbt23.getvalvbt2(Integer.toString(valv));
			// if (!valvu.isAbierta())
			IR.panelbt2.interruptor(valvu, accion, 7);
		    } else if (IrrisoftConstantes.BT2_4000 < valv) {
			valvu = IR.valvsbt24.getvalvbt2(Integer.toString(valv));
			// if (!valvu.isAbierta())
			IR.panelbt2.interruptor(valvu, accion, 8);
		    }
		}
	    }
	}
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

	String nombreCampo = evt.getPropertyName();

	//Listener para sincronizar el programador.
	if ("false".contains(nombreCampo)) {
	    this.rearmarHum = true;
	}
    }

}
