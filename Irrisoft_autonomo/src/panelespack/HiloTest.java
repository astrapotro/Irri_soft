package panelespack;

import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import alertaspack.GestorAlertas;

import sensorespack.HiloCaudalimetro;
import sensorespack.ListaSensores;
import sensorespack.Sensor;
import valvulaspack.ListaValvBt2;
import valvulaspack.ListaValvMci;
import valvulaspack.Valvula;

public class HiloTest implements Runnable {

    private static Logger logger = LogManager.getLogger(HiloTest.class
	    .getName());
    public boolean inicial;

    public int numplaca;
    // public int numplaca;
    public int numvalv = 0;

    private GestorAlertas gestorAlertas;

    private HiloCaudalimetro hilocau;

    private Irrisoft IR;


    // Tiempo de llenado de circuito hidraulico (estimado a 5 minutos)

    //private long t_llenado_circuito = 300000;
   // private long t_llenado_circuito = 15000;

    /**
     * @param inicial
     * @param numplaca
     */
    public HiloTest(boolean inicial, int numplaca) {

	this.inicial = inicial;
	this.numplaca = numplaca;
	// this.hilocau=hilocau;
	// this.hiloamp=hiloamp;
	gestorAlertas = GestorAlertas.getInstance();

	this.IR = Irrisoft.window;
	this.hilocau = IR.getHilocau();


    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
	// TODO Auto-generated method stub
	empiezatest();
    }

    /**
     * 
     */
    public void empiezatest() {

	Valvula valv = null;

	inicializavars(true);

	if (IrrisoftConstantes.PLACA_MCI_1 == numplaca)
	    valv = IR.valvsmci.getvalvmci(numvalv - 101);
	else if (IrrisoftConstantes.PLACA_MCI_2 == numplaca)
	    valv = IR.valvsmci2.getvalvmci(numvalv - 201);
	else if (IrrisoftConstantes.PLACA_MCI_3 == numplaca)
	    valv = IR.valvsmci3.getvalvmci(numvalv - 301);
	else if (IrrisoftConstantes.PLACA_MCI_4 == numplaca)
	    valv = IR.valvsmci4.getvalvmci(numvalv - 401);
	else if (IrrisoftConstantes.PLACA_BT2_5 == numplaca)
	    valv = IR.valvsbt2.getvalvbt2(Integer.toString(numvalv));
	else if (IrrisoftConstantes.PLACA_BT2_6 == numplaca)
	    valv = IR.valvsbt22.getvalvbt2(Integer.toString(numvalv));
	else if (IrrisoftConstantes.PLACA_BT2_7 == numplaca)
	    valv = IR.valvsbt23.getvalvbt2(Integer.toString(numvalv));
	else if (IrrisoftConstantes.PLACA_BT2_8 == numplaca)
	    valv = IR.valvsbt24.getvalvbt2(Integer.toString(numvalv));

	try {

	    IR.test = true;
	    
	    // Es un testeo particular
	    if (!inicial) {
		
		
		IR.inicial = false;
		IR.hilocau.circuito_vacio = true;

		gestorAlertas.insertarAlarma(3009);

		// Abroo Maestraa
		if (IR.maestra != null) {
		    IR.panelbt2.abremaestra();

		    IR.paneltest.lbltesteando
			    .setText("Llenando circuito, espere ...");

		    // Duermo para que le de tiempo de llenar el circuito
		    int i = 0;
		    IR.paneltest.progressBar.setValue(0);
		    IR.paneltest.progressBar.setMaximum((int)IrrisoftConstantes.DELAY_LLENADO_CIRCUITO/1000);
		    while (i<(int)IrrisoftConstantes.DELAY_LLENADO_CIRCUITO/1000){
			Thread.sleep(10000);
			i= i + 10;
			IR.paneltest.progressBar.setValue(i);	
		    }
		    
		    // aviso al caudalimetro
		    synchronized (IR.hilocau) {
			IR.hilocau.notify();
		    }

		}

		// Es una valvula mci
		if (IrrisoftConstantes.PLACA_MCI_1 == numplaca
			|| IrrisoftConstantes.PLACA_MCI_2 == numplaca
			|| IrrisoftConstantes.PLACA_MCI_3 == numplaca
			|| IrrisoftConstantes.PLACA_MCI_4 == numplaca) {

		    if (hilocau != null)
			hilocau.tipovalv = 1;

		    // abro la valvula requerida
		    if (logger.isInfoEnabled()) {
			logger.info("NUMVALV: " + numvalv);
		    }
		    // MCI1
		    if (IrrisoftConstantes.PLACA_MCI_1 == numplaca) {
			IR.panelbt2.conserie = IR.getSerie1();

			IR.panelmci.interruptor(valv, valv.getImgasoc(),
				IR.panelmci.panel1);

		    }
		    // MCI2
		    else if (IrrisoftConstantes.PLACA_MCI_2 == numplaca) {
			IR.panelbt2.conserie = IR.getSerie2();

			IR.panelmci.interruptor(valv, valv.getImgasoc(),
				IR.panelmci.panel2);

		    }
		    // MCI3
		    else if (IrrisoftConstantes.PLACA_MCI_3 == numplaca) {
			IR.panelbt2.conserie = IR.getSerie3();

			IR.panelmci.interruptor(valv, valv.getImgasoc(),
				IR.panelmci.panel3);

		    }
		    // MCI4
		    else if (IrrisoftConstantes.PLACA_MCI_4 == numplaca) {
			IR.panelbt2.conserie = IR.getSerie4();

			IR.panelmci.interruptor(valv, valv.getImgasoc(),
				IR.panelmci.panel4);

		    }
		}

		// Es una valvula bt2
		else if (numplaca > 4) {

		    if (hilocau != null)
			hilocau.tipovalv = 2;

		    // abro la valvula requerida
		    // BT2
		    if (IrrisoftConstantes.BT2_2000 > numvalv) {
			IR.panelbt2.conserie = IR.getSerie5();

			IR.panelbt2.interruptor(valv, 1, 5);

		    }

		    // BT22
		    else if (IrrisoftConstantes.BT2_2000 < numvalv
			    && IrrisoftConstantes.BT2_3000 > numvalv) {
			IR.panelbt2.conserie = IR.getSerie6();

			IR.panelbt2.interruptor(valv, 1, 6);

		    }
		    // BT23
		    else if (IrrisoftConstantes.BT2_3000 < numvalv
			    && IrrisoftConstantes.BT2_4000 > numvalv) {
			IR.panelbt2.conserie = IR.getSerie7();

			IR.panelbt2.interruptor(valv, 1, 7);

		    }
		    // BT24
		    else if (IrrisoftConstantes.BT2_4000 < numvalv) {
			IR.panelbt2.conserie = IR.getSerie8();

			IR.panelbt2.interruptor(valv, 1, 8);

		    }

		}

		//Espero a que el caudalimetro me avise
		//if (IR.test == true) {
		    synchronized (IR.paneltest.hilotest) {
			IR.paneltest.hilotest.wait();
		    }

		//}

		// Cieroo Maestraaa
		if (IR.maestra != null)
		    IR.panelbt2.cierramaestra();

		inicializavars(false);

		gestorAlertas.insertarAlarma(3010);

		// Es el testeo inicial
	    } else if (inicial) {

		IR.inicial = true;
		IR.hilocau.circuito_vacio = true;
		gestorAlertas.insertarAlarma(3007);

		// Abroo Maestraa
		if (IR.maestra != null) {
		    IR.panelbt2.abremaestra();

		    IR.paneltest.lbltesteando
			    .setText("Llenando circuito, espere ...");

		 // Duermo para que le de tiempo de llenar el circuito
		    int i = 0;
		    IR.paneltest.progressBar.setValue(0);
		    IR.paneltest.progressBar.setMaximum((int)IrrisoftConstantes.DELAY_LLENADO_CIRCUITO/1000);
		    while (i<(int)IrrisoftConstantes.DELAY_LLENADO_CIRCUITO/1000){
			
			if (!IR.test)
			    return;
			Thread.sleep(10000);
			if (!IR.test)
			    return;
			
			i= i + 10;
			IR.paneltest.progressBar.setValue(i);	
		    }
		   
		   
		    
		    // aviso al caudalimetro
		    synchronized (IR.hilocau) {
			IR.hilocau.notify();
		    }

		}

		// Borro el contenido de la tabla modelconsum (ya que se va a
		// hacer un testeo inicial)
		IR.getConn().borralconsumtest();
		// tb en la pasarela
		IR.getConn().borrarconsumtest();

		// TODO Aquí hay que añadir un jdialog entre cada placa por si
		// el técnico de campo tiene que irse y hacerlo al día siguiente

		// //
		// JOptionPane
		// .showMessageDialog(
		// IR.frmIrrisoft,
		// "\nEsperando a que pulses aceptar para continuar",
		// "Esperando ...",
		// JOptionPane.INFORMATION_MESSAGE);
		//
		// logger.error("IEEEUP");

		// MCIs
		if (IR.valvsmci != null)
		    bucleinimci(IR.valvsmci, IR.panelmci.panel1);

		if (IR.valvsmci2 != null)
		    bucleinimci(IR.valvsmci2, IR.panelmci.panel2);

		if (IR.valvsmci3 != null)
		    bucleinimci(IR.valvsmci3, IR.panelmci.panel3);

		if (IR.valvsmci4 != null)
		    bucleinimci(IR.valvsmci4, IR.panelmci.panel4);

		// BT2s
		if (IR.valvsbt2 != null)
		    bucleinibt2(IR.valvsbt2);

		if (IR.valvsbt22 != null)
		    bucleinibt2(IR.valvsbt22);

		if (IR.valvsbt23 != null)
		    bucleinibt2(IR.valvsbt23);

		if (IR.valvsbt24 != null)
		    bucleinibt2(IR.valvsbt24);

		// Cieroo Maestraaa
		if (IR.maestra != null)
		    IR.panelbt2.cierramaestra();

		inicializavars(false);

		JOptionPane
			.showMessageDialog(
				IR.frmIrrisoft,
				"Testeo inicial finalizado correctamente!\nSe han escrito los caudales en la tabla modelo.");

		gestorAlertas.insertarAlarma(3008);

	    }
	    

	} catch (InterruptedException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error, Hilo interrumpido: " +e.getMessage());
	    }
	}

    }

    /**
     * @param valvs
     * @param panel
     */
    public void bucleinimci(ListaValvMci valvs, JPanel panel) {

	for (int i = 0; i < valvs.getsizeof(); i++) {

	    // Me salgo si ya no hay que hacer el test
	    if (IR.test == false)
		return;

	    inicializavars(true);
	    hilocau.tipovalv = 1;
	    numvalv = Integer.parseInt(valvs.getvalvmci(i).getCodelecvalv());
	    numplaca = valvs.getvalvmci(i).getNum_placa();

	    logger.info("Valvula " + valvs.getvalvmci(i).getCodelecvalv()
		    + " , size valvs:" + valvs.getsizeof() + " , i:" + i);

	    // Abro sólo si no es maestra
	    if (valvs.getvalvmci(i).isMaestra() == 0) {
		// abro la valvula requerida
		// MCI
		IR.panelmci.interruptor(valvs.getvalvmci(i), valvs
			.getvalvmci(i).getImgasoc(), panel);
		//IR.paneltest.lbltesteando.setText("Testeando, espere ...");
		//hilocau.setTest(true);
		IR.paneltest.lblvalv.setText(Integer.toString((i + 1)
			+ (100 * numplaca)));

		synchronized (IR.paneltest.hilotest) {
		    try {
			IR.paneltest.hilotest.wait();
		    } catch (InterruptedException e) {
			if (logger.isErrorEnabled()) {
			    logger.error("Error, Hilo interrumpido: " +e.getMessage());
			}
		    }
		}
	    }
	}
    }

    /**
     * @param valvs
     */
    public void bucleinibt2(ListaValvBt2 valvs) {

	for (int i = 0; i < valvs.getsizeof(); i++) {

	    // Me salgo si ya no hay que hacer el test
	    if (IR.test == false)
		return;

	    inicializavars(true);
	    hilocau.tipovalv = 2;
	    numvalv = Integer.parseInt(valvs.getvalvbt2(i).getCodelecvalv());
	    numplaca = valvs.getvalvbt2(i).getNum_placa();

	    if (logger.isInfoEnabled()) {
		logger.info("BUCLE FOR HILOTEST: " + numvalv + " " + numplaca);
	    }
	    // abro la valvula requerida
	    // BT2

	    // Abro sólo si no es maestra
	    if (valvs.getvalvbt2(i).isMaestra() == 0) {

		if (IrrisoftConstantes.BT2_2000 > numvalv)
		    IR.panelbt2.interruptor(valvs.getvalvbt2(i), 1, 5);

		// BT22
		if (IrrisoftConstantes.BT2_2000 < numvalv
			&& IrrisoftConstantes.BT2_3000 > numvalv)
		    IR.panelbt2.interruptor(valvs.getvalvbt2(i), 1, 6);

		// BT23
		if (IrrisoftConstantes.BT2_3000 < numvalv
			&& IrrisoftConstantes.BT2_4000 > numvalv)
		    IR.panelbt2.interruptor(valvs.getvalvbt2(i), 1, 7);

		// BT24
		if (IrrisoftConstantes.BT2_4000 < numvalv)
		    IR.panelbt2.interruptor(valvs.getvalvbt2(i), 1, 8);

		//IR.paneltest.lbltesteando.setText("Testeando, espere ...");

		IR.paneltest.lblvalv.setText(Integer.toString(numvalv));

		synchronized (IR.paneltest.hilotest) {
		    try {
			IR.paneltest.hilotest.wait();
		    } catch (InterruptedException e) {
			if (logger.isErrorEnabled()) {
			    logger.error("Error, Hilo interrumpido: " +e.getMessage());
			}
		    }
		}
		if (logger.isInfoEnabled()) {
		    logger.info("Soy hiloTEST y me acaban de despertar!");
		}
	    }

	}

    }

    /**
     * @param empezar
     */
    public void inicializavars(boolean empezar) {

	// Inicializa las variables del paneltest

	if (empezar) {

	    IR.paneltest.progressBar.setValue(0);
	    IR.paneltest.lbltesteando.setText("Testeando, espere...");
	    IR.paneltest.lblPulsosDat.setText("");
	    IR.paneltest.lblCauDat.setText("");
	    IR.paneltest.lblIntenDat.setText("");
	    IR.paneltest.lblls.setText("");
	    IR.paneltest.lblamp.setText("");
	    IR.paneltest.lblpul.setText("");
	    IR.paneltest.lblvalv.setText("");
	    IR.paneltest.lbltesteando.setText("");


	    //hilocau.setMci(false);
	    hilocau.setPulsostot(0);
	    hilocau.setVuelta(-1);
	    hilocau.primera = true;


	    IR.paneltest.btnAtras.setText("Cancelar");
	    IR.paneltest.btnTestearMCI.setEnabled(false);
	    IR.paneltest.btntesteoini.setEnabled(false);
	    IR.paneltest.btnTestearBT2.setEnabled(false);
	    IR.paneltest.comboBoxMCI.setEnabled(false);
	    IR.paneltest.comboBoxBt2.setEnabled(false);
	    

	} else {
	    
	    IR.test = false;

	    if (inicial) {
		IR.paneltest.lblPulsosDat.setText("");
		IR.paneltest.lblCauDat.setText("");
		IR.paneltest.lblIntenDat.setText("");
		IR.paneltest.lblls.setText("");
		IR.paneltest.lblamp.setText("");
		IR.paneltest.lblpul.setText("");
		IR.paneltest.lblvalv.setText("");
		IR.paneltest.lbltesteando.setText("");
		IR.paneltest.lblvalv.setText("");
		IR.paneltest.lbltesteando.setText("");
		IR.paneltest.progressBar.setValue(0);
		IR.inicial = false;
	    }

	    IR.paneltest.btnAtras.setText("Atras");
	    IR.paneltest.btnAtras.setEnabled(true);

	    if (IR.valvsmci != null) {
		IR.paneltest.btnTestearMCI.setEnabled(true);
		IR.paneltest.comboBoxMCI.setEnabled(true);
	    }

	    if (IR.valvsbt2 != null) {
		IR.paneltest.btnTestearBT2.setEnabled(true);
		IR.paneltest.comboBoxBt2.setEnabled(true);
	    }

	    IR.paneltest.btntesteoini.setEnabled(true);
	    IR.paneltest.lbltesteando.setText("");
	    IR.paneltest.progressBar.setValue(0);

	    IR.paneltest.HiloTest.numplaca = 0;

	   
	    
//	    //Aviso a los hilos de amperimetros que se han quedado en estado wait
	    for(int i=0; i<IR.sensores.size();i++){
		
		if (IR.sensores.get(i).getTipo()==3){
		    synchronized (IR.sensores.get(i).getHilosens()) {
				IR.sensores.get(i).getHilosens().notify();
	        	}
		}
	    }

	    // Aviso a los sensores que estén esperando de que el test ha
	    // acabado
//	    for (int i = 0; i < IR.sensores.size(); i++) {
//
//		Sensor sensor = IR.sensores.get(i);
//
//		synchronized (sensor.getHilosens()) {
//		    sensor.getHilosens().notify();
//		}
//	    }

	    hilocau.vuelta = -1;
	    hilocau.pulsosparciales = 0;
	    hilocau.primera = true;
	    

	}

	IR.paneltest.repaint();
    }

}
