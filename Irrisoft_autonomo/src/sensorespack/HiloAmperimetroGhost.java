package sensorespack;

import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HiloAmperimetroGhost extends Sensor implements Runnable {

    private static Logger logger = LogManager
	    .getLogger(HiloAmperimetroGhost.class.getName());
    private int consumotot = 0;
    private int consumoresidual = 0;
    // private boolean inicial;
    private Sensor sens;
    private Irrisoft IR;

    public HiloAmperimetroGhost(Sensor sens) {
	this.sens = sens;
	this.IR = Irrisoft.window;

    }

    public void run() {

	while (true) {

	    if (IR.test == true) {

		test();
	    }

	    try {
		Thread.sleep(IrrisoftConstantes.DELAY_SENSOR_2SEG);
	    } catch (InterruptedException e) {
		if(logger.isErrorEnabled()){
		    logger.error(e.getMessage());
		}
	    }
	}

    }

    /**
     * 
     */
    public void test() {

	if (logger.isInfoEnabled()) {
	    logger.info(("Estoy en hiloamperimetroGHOST: TEST"));
	    logger.info("HILOGHOST: " + sens.getHilosens().getId());
	}

	if (IR.haycaudalimetro) {
	    synchronized (sens.getHilosens()) {
		try {
		    sens.getHilosens().wait();
		} catch (InterruptedException e) {
		    if(logger.isErrorEnabled()){
			logger.error(e.getMessage());
		    }
		}
	    }
	} else {
	    try {
		Thread.sleep(IrrisoftConstantes.DELAY_SENSOR_25SEG);
	    } catch (InterruptedException e) {
		if(logger.isErrorEnabled()){
		    logger.error(e.getMessage());
		}
	    }
	}
	if (logger.isInfoEnabled()) {
	    logger.info("NUMVALV en GHOST: " + sens.getValvula());

	    logger.info(("Estoy en hiloamperimetroGHOST: TEST , y me acaban de despertar !!!"));
	}
	calculaintest();

    }

    /**
     * 
     */
    public void calculaintest() {

	if (logger.isInfoEnabled()) {
	    logger.info("Intensidad GHOST: " + consumotot);
	    logger.info("Caudal em GHOST: " + sens.getLecturacau());
	}
	IR.paneltest.progressBar.setValue(25);

	if (!IR.inicial) {
	    IR.paneltest.lblIntenDat.setText(Integer
		    .toString(consumotot - consumoresidual) + " mA");

	    // Activo los botones
	    IR.paneltest.btnAtras.setEnabled(true);
	    IR.paneltest.btnTestearMCI.setEnabled(true);
	    IR.paneltest.btntesteoini.setEnabled(true);
	    IR.paneltest.btnTestearBT2.setEnabled(true);
	    IR.paneltest.comboBoxMCI.setEnabled(true);
	    IR.paneltest.comboBoxBt2.setEnabled(true);
	    IR.paneltest.btnAtras.setText("Atras");

	} else
	    IR.paneltest.lblamp.setText(Integer
		    .toString(consumotot - consumoresidual) + " mA");

	if (IR.inicial) {

	    // Escribo registro en la BBDD
	    escribeconsum(Integer.toString(sens.getValvula()));

	} else {

	    IR.paneltest.lbltesteando
		    .setText("Testeo de valvula OK");
	   
	 // Escribo registro en la BBDD
	    escribeconsum(Integer.toString(sens.getValvula()));
	}

    }

    /**
     * 
     */
    public void calculaintauto() {

    }

    /**
     * Inserta el consumo de la valvula en la BBDD Local.
     * @param valvula
     */
    public void escribeconsum(String valvula) {

	IR.getConn().insertalconsumostest(
		Integer.toString(sens.getValvula()), sens.getLecturacau(),
		consumotot);
	IR.getConn().insertarconsumostest(
		Integer.toString(sens.getValvula()), sens.getLecturacau(),
		consumotot);

    }

}