package tareapack;

import java.util.Calendar;

import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;
import irrisoftpack.SerialDriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import valvulaspack.Valvula;

public class HiloTarea implements Runnable {

    private static Logger logger = LogManager.getLogger(HiloTarea.class
	    .getName());

    private TareaManual tarea;

    private SerialDriver serialcon;

    private Valvula valvu;

    private int tiempoaux = 0;

    private Irrisoft IR;

    /**
     * Constructor para poder pasarle la tarea como argumento
     * 
     * @param tarea
     */
    public HiloTarea(TareaManual tarea) {
	this.tarea = tarea;
	this.IR = Irrisoft.window;
    }

    @Override
    public synchronized void run() {

	if (logger.isInfoEnabled()) {
	    logger.info("Hilotarea:" + tarea.getCodelecvalv());
	}
	// COMPRUEBO para que PLACA es la tarea !!
	Integer valv = Integer.parseInt(tarea.getCodelecvalv());

	if (IrrisoftConstantes.MCI_200 > valv) {
	    serialcon = IR.getSerie1();
	    valvu = IR.valvsmci.getvalvmci(tarea.getCodelecvalv());
	    accionvalv(Irrisoft.config.getMci(), 1);
	} else if (IrrisoftConstantes.MCI_200 < valv
		&& IrrisoftConstantes.MCI_300 > valv) {
	    serialcon = IR.getSerie2();
	    valvu = IR.valvsmci2.getvalvmci(tarea.getCodelecvalv());
	    accionvalv(Irrisoft.config.getMci2(), 2);
	} else if (IrrisoftConstantes.MCI_300 < valv
		&& IrrisoftConstantes.MCI_400 > valv) {
	    serialcon = IR.getSerie3();
	    valvu = IR.valvsmci3.getvalvmci(tarea.getCodelecvalv());
	    accionvalv(Irrisoft.config.getMci3(), 3);
	} else if (IrrisoftConstantes.MCI_400 < valv
		&& IrrisoftConstantes.MCI_500 > valv) {
	    serialcon = IR.getSerie4();
	    valvu = IR.valvsmci4.getvalvmci(tarea.getCodelecvalv());
	    accionvalv(Irrisoft.config.getMci4(), 4);
	} else if (IrrisoftConstantes.BT2_1000 < valv
		&& IrrisoftConstantes.BT2_2000 > valv) {
	    serialcon = IR.getSerie5();
	    valvu = IR.valvsbt2.getvalvbt2(tarea.getCodelecvalv());
	    accionvalv(Irrisoft.config.getBt2(), 5);
	} else if (IrrisoftConstantes.BT2_2000 < valv
		&& IrrisoftConstantes.BT2_3000 > valv) {
	    serialcon = IR.getSerie6();
	    valvu = IR.valvsbt22.getvalvbt2(tarea.getCodelecvalv());
	    accionvalv(Irrisoft.config.getBt22(), 6);
	} else if (IrrisoftConstantes.BT2_3000 < valv
		&& IrrisoftConstantes.BT2_4000 > valv) {
	    serialcon = IR.getSerie7();
	    valvu = IR.valvsbt23.getvalvbt2(tarea.getCodelecvalv());
	    accionvalv(Irrisoft.config.getBt23(), 7);
	} else if (IrrisoftConstantes.BT2_4000 < valv
		&& IrrisoftConstantes.SAMCLA > valv) {
	    serialcon = IR.getSerie8();
	    valvu = IR.valvsbt24.getvalvbt2(tarea.getCodelecvalv());
	    accionvalv(Irrisoft.config.getBt24(), 8);
	} else if (IrrisoftConstantes.SAMCLA < valv) {

	    valvu = IR.valvsamcla.getvalvsamcla(tarea.getCodelecvalv());
	    // serialcon=valvu.getSerie();
	    accionvalv_samcla(-1);
	}
    }

    /**
     * Abro una valvula de una tarea.
     * 
     * @param puerto
     * @param tipoplaca
     * @return
     */
    public synchronized boolean accionvalv(String puerto, int tipoplaca) {

	boolean fallo = false;
	
	tiempoaux = tarea.getDuracion() - tarea.getTiemporegado() - 4;

	IR.panelbt2.abremaestra();

	fallo = serialcon.abrevalv(valvu);

	gestvalv(valvu.getNum_placa(), true);

	if (logger.isInfoEnabled()) {
	    logger.info("Hilo " + tarea.getHilotar() + " durmiendo: "
		    + tarea.getDuracion());
	}
	// Duermo el proceso tanto como sea + ajuste de segundos (A REVISAR
	// 3)

	while (tiempoaux > 3) {

	    // Duermo 5 segundos
	    java.util.concurrent.locks.LockSupport.parkNanos(10000000000L);
	    tiempoaux = tiempoaux - 10;
	    tarea.setTiemporegado(tarea.getDuracion() - tiempoaux);
	    IR.hiloescucha.connDB.ponetiemporegado(null, tarea,
		    tarea.getTiemporegado());

	}

	fallo = serialcon.cierravalv(valvu.getCodelecvalv(), tipoplaca);

	gestvalv(tipoplaca, false);
	IR.panelbt2.cierramaestra();

	// Llamo a borratarea
	if (IR.hiloescucha.connDB.Borratarea(tarea)) {
	    IR.escribetextPane("\nTarea " + tarea.getIdtarea()
		    + " borrada correctamente.", IR.normal, false);
	}

	return true;
    }

    public synchronized void accionvalv_samcla(int tipoplaca) {

	//Calculo la diferencia de tiempo entre ahora y el timestamp de la tarea
	long dif_tiempo = (Calendar.getInstance().getTime().getTime()
		- tarea.getFcexec().getTime())/1000L;
	tiempoaux = tarea.getDuracion() - (int) dif_tiempo;

	
	//Sólo acciono si hay que regar + de 1 minuto ( si es menos NO SE PUEDE)
	if (tiempoaux>60){
	    
	    	    gestvalv(valvu.getNum_placa(), true);
        	    IR.panelsamcla.interruptor(true, valvu,
        	    IR.panelsamcla.duracion_samcla(tiempoaux), false);
        	    gestvalv(tipoplaca, false);
        	
	}else{
	    logger.warn("Se ha pasado el tiempo de riego establecido para la electroválvula.\nNo se hará nada!!");
	}

	// Llamo a borratarea
	if (IR.hiloescucha.connDB.Borratarea(tarea)) {
	    IR.escribetextPane("\nTarea " + tarea.getIdtarea()
		    + " borrada correctamente.", IR.normal, false);
	}

    }

    /**
     * @param tipo
     * @param accionabrir
     */
    private void gestvalv(int tipo, boolean accionabrir) {

	if (logger.isInfoEnabled()) {
	    logger.info("COD elecvalv: " + tarea.getCodelecvalv());
	}
	if (IrrisoftConstantes.PLACA_MCI_1 == tipo
		|| IrrisoftConstantes.PLACA_MCI_2 == tipo
		|| IrrisoftConstantes.PLACA_MCI_3 == tipo
		|| IrrisoftConstantes.PLACA_MCI_4 == tipo) {

	    if (IrrisoftConstantes.PLACA_MCI_1 == tipo) {// Si es la primera
							 // placa MCI
		if (accionabrir) {
		    tarea.setRegando(true);
		    // Actualizo estado de la valvula y la ascio con la tarea
		    // (id)
		    valvu.setAbierta(true);
		    valvu.setTareaasoc(tarea.getIdtarea());
		    // Repinto
		    IR.panelmci.interruptor(valvu, valvu.getImgasoc(), true,
			    IR.panelmci.panel1);
		    // La meto en Valvsabiertas
		    IR.addvalvsabiertas(valvu, tipo);

		} else {
		    tarea.setRegando(false);
		    // Reinicio idtarea de la valvula
		    valvu.setTareaasoc(0);
		    valvu.setAbierta(false);

		    // Repinto
		    IR.panelmci.interruptor(valvu, valvu.getImgasoc(), true,
			    IR.panelmci.panel1);

		    // Sea la valvula que sea la quito de la lista de valvulas
		    // abiertas
		    IR.quitarvalvabiertas(valvu, tipo);
		    // Tb quito la tarea de la lista de tareasaexec
		    // ListaTareasaexec.getInstance().delhilotar(tarea);
		}

	    } else if (IrrisoftConstantes.PLACA_MCI_2 == tipo) {// Si es la
								// segunda placa
								// MCI
		if (accionabrir) {
		    tarea.setRegando(true);
		    // Actualizo estado de la valvula y la ascio con la tarea
		    // (id)
		    IR.valvsmci2.getvalvmci(tarea.getCodelecvalv()).setAbierta(
			    true);
		    IR.valvsmci2.getvalvmci(tarea.getCodelecvalv())
			    .setTareaasoc(tarea.getIdtarea());
		    // Repinto
		    IR.panelmci.interruptor(IR.valvsmci2.getvalvmci(tarea
			    .getCodelecvalv()),
			    IR.valvsmci2.getvalvmci(tarea.getCodelecvalv())
				    .getImgasoc(), true, IR.panelmci.panel2);
		    // La meto en Valvsabiertas
		    IR.addvalvsabiertas(valvu, tipo);

		} else {
		    tarea.setRegando(false);
		    // Reinicio idtarea de la valvula
		    IR.valvsmci2.getvalvmci(tarea.getCodelecvalv())
			    .setTareaasoc(0);
		    IR.valvsmci2.getvalvmci(tarea.getCodelecvalv()).setAbierta(
			    false);

		    // Repinto
		    IR.panelmci.interruptor(IR.valvsmci2.getvalvmci(tarea
			    .getCodelecvalv()),
			    IR.valvsmci2.getvalvmci(tarea.getCodelecvalv())
				    .getImgasoc(), true, IR.panelmci.panel2);

		    // Sea la valvula que sea la quito de la lista de valvulas
		    // abiertas
		    IR.quitarvalvabiertas(valvu, tipo);
		    // Tb quito la tarea de la lista de tareasaexec
		    // ListaTareasaexec.getInstance().delhilotar(tarea);
		}
	    } else if (IrrisoftConstantes.PLACA_MCI_3 == tipo) {// Si es la
								// tercera placa
								// MCI
		if (accionabrir) {
		    tarea.setRegando(true);
		    // Actualizo estado de la valvula y la ascio con la tarea
		    // (id)
		    IR.valvsmci3.getvalvmci(tarea.getCodelecvalv()).setAbierta(
			    true);
		    IR.valvsmci3.getvalvmci(tarea.getCodelecvalv())
			    .setTareaasoc(tarea.getIdtarea());
		    // Repinto
		    IR.panelmci.interruptor(IR.valvsmci3.getvalvmci(tarea
			    .getCodelecvalv()),
			    IR.valvsmci3.getvalvmci(tarea.getCodelecvalv())
				    .getImgasoc(), true, IR.panelmci.panel3);
		    // La meto en Valvsabiertas
		    IR.addvalvsabiertas(valvu, tipo);

		} else {
		    tarea.setRegando(false);
		    // Reinicio idtarea de la valvula
		    IR.valvsmci3.getvalvmci(tarea.getCodelecvalv())
			    .setTareaasoc(0);
		    IR.valvsmci3.getvalvmci(tarea.getCodelecvalv()).setAbierta(
			    false);

		    // Repinto
		    IR.panelmci.interruptor(IR.valvsmci3.getvalvmci(tarea
			    .getCodelecvalv()),
			    IR.valvsmci3.getvalvmci(tarea.getCodelecvalv())
				    .getImgasoc(), true, IR.panelmci.panel3);

		    // Sea la valvula que sea la quito de la lista de valvulas
		    // abiertas
		    IR.quitarvalvabiertas(valvu, tipo);
		    // Tb quito la tarea de la lista de tareasaexec
		    // ListaTareasaexec.getInstance().delhilotar(tarea);
		}
	    } else if (IrrisoftConstantes.PLACA_MCI_4 == tipo) {// Si es la
								// cuarta placa
								// MCI
		if (accionabrir) {
		    tarea.setRegando(true);
		    // Actualizo estado de la valvula y la ascio con la tarea
		    // (id)
		    IR.valvsmci4.getvalvmci(tarea.getCodelecvalv()).setAbierta(
			    true);
		    IR.valvsmci4.getvalvmci(tarea.getCodelecvalv())
			    .setTareaasoc(tarea.getIdtarea());
		    // Repinto
		    IR.panelmci.interruptor(IR.valvsmci4.getvalvmci(tarea
			    .getCodelecvalv()),
			    IR.valvsmci4.getvalvmci(tarea.getCodelecvalv())
				    .getImgasoc(), true, IR.panelmci.panel4);
		    // La meto en Valvsabiertas
		    IR.addvalvsabiertas(valvu, tipo);

		} else {
		    tarea.setRegando(false);
		    // Reinicio idtarea de la valvula
		    IR.valvsmci4.getvalvmci(tarea.getCodelecvalv())
			    .setTareaasoc(0);
		    IR.valvsmci4.getvalvmci(tarea.getCodelecvalv()).setAbierta(
			    false);

		    // Repinto
		    IR.panelmci.interruptor(IR.valvsmci4.getvalvmci(tarea
			    .getCodelecvalv()),
			    IR.valvsmci4.getvalvmci(tarea.getCodelecvalv())
				    .getImgasoc(), true, IR.panelmci.panel4);

		    // Sea la valvula que sea la quito de la lista de valvulas
		    // abiertas
		    IR.quitarvalvabiertas(valvu, tipo);
		    // Tb quito la tarea de la lista de tareasaexec
		    // ListaTareasaexec.getInstance().delhilotar(tarea);
		}
	    }

	} else if (tipo == IrrisoftConstantes.PLACA_BT2_5
		|| tipo == IrrisoftConstantes.PLACA_BT2_6
		|| tipo == IrrisoftConstantes.PLACA_BT2_7
		|| tipo == IrrisoftConstantes.PLACA_BT2_8) {

	    int codelecvalv = 0;

	    if (IrrisoftConstantes.PLACA_BT2_5 == tipo) {
		codelecvalv = IR.valvsbt2.getvalvbt2(tarea.getCodelecvalv())
			.getIndex();
		if (accionabrir) {
		    tarea.setRegando(true);
		    // Actualizo estado de la valvula
		    valvu.setAbierta(true);
		    valvu.setTareaasoc(tarea.getIdtarea());
		    // La meto en Valvsabiertas
		    IR.addvalvsabiertas(valvu, tipo);

		} else {
		    tarea.setRegando(false);
		    // Actualizo estado de la valvula
		    valvu.setAbierta(false);
		    valvu.setTareaasoc(0);
		    // Asocio una idtarea con la valvula
		    // IR.valvsbt2.getvalvbt2(codelecvalv).setTareaasoc(tarea.getIdtarea());

		    // Sea la valvula que sea la quito de la lista de valvulas
		    // abiertas
		    IR.quitarvalvabiertas(valvu, tipo);
		    // Tb quito la tarea de la lista de tareasaexec
		    // ListaTareasaexec.getInstance().delhilotar(tarea);
		}

	    } else if (IrrisoftConstantes.PLACA_BT2_6 == tipo) {
		codelecvalv = IR.valvsbt22.getvalvbt2(tarea.getCodelecvalv())
			.getIndex();

		if (accionabrir) {
		    tarea.setRegando(true);
		    // Actualizo estado de la valvula
		    valvu.setAbierta(true);
		    valvu.setTareaasoc(tarea.getIdtarea());
		    // La meto en Valvsabiertas
		    IR.addvalvsabiertas(valvu, tipo);

		} else {
		    tarea.setRegando(false);
		    // Actualizo estado de la valvula
		    valvu.setAbierta(false);
		    valvu.setTareaasoc(0);
		    // Asocio una idtarea con la valvula
		    // IR.valvsbt2.getvalvbt2(codelecvalv).setTareaasoc(tarea.getIdtarea());

		    // Sea la valvula que sea la quito de la lista de valvulas
		    // abiertas
		    IR.quitarvalvabiertas(valvu, tipo);
		    // Tb quito la tarea de la lista de tareasaexec
		    // ListaTareasaexec.getInstance().delhilotar(tarea);
		}
	    } else if (IrrisoftConstantes.PLACA_BT2_7 == tipo) {
		codelecvalv = IR.valvsbt23.getvalvbt2(tarea.getCodelecvalv())
			.getIndex();

		if (accionabrir) {
		    tarea.setRegando(true);
		    // Actualizo estado de la valvula
		    valvu.setAbierta(true);
		    valvu.setTareaasoc(tarea.getIdtarea());
		    // La meto en Valvsabiertas
		    IR.addvalvsabiertas(valvu, tipo);

		} else {
		    tarea.setRegando(false);
		    // Actualizo estado de la valvula
		    valvu.setAbierta(false);
		    valvu.setTareaasoc(0);
		    // Asocio una idtarea con la valvula
		    // IR.valvsbt2.getvalvbt2(codelecvalv).setTareaasoc(tarea.getIdtarea());

		    // Sea la valvula que sea la quito de la lista de valvulas
		    // abiertas
		    IR.quitarvalvabiertas(valvu, tipo);
		    // Tb quito la tarea de la lista de tareasaexec
		    // ListaTareasaexec.getInstance().delhilotar(tarea);
		}
	    } else if (IrrisoftConstantes.PLACA_BT2_8 == tipo) {
		codelecvalv = IR.valvsbt24.getvalvbt2(tarea.getCodelecvalv())
			.getIndex();

		if (accionabrir) {
		    tarea.setRegando(true);
		    // Actualizo estado de la valvula
		    valvu.setAbierta(true);
		    valvu.setTareaasoc(tarea.getIdtarea());
		    // La meto en Valvsabiertas
		    IR.addvalvsabiertas(valvu, tipo);

		} else {
		    tarea.setRegando(false);
		    // Actualizo estado de la valvula
		    valvu.setAbierta(false);
		    valvu.setTareaasoc(0);
		    // Asocio una idtarea con la valvula
		    // IR.valvsbt2.getvalvbt2(codelecvalv).setTareaasoc(tarea.getIdtarea());

		    // Sea la valvula que sea la quito de la lista de valvulas
		    // abiertas
		    IR.quitarvalvabiertas(valvu, tipo);
		    // Tb quito la tarea de la lista de tareasaexec
		    // ListaTareasaexec.getInstance().delhilotar(tarea);
		}
	    }

	} else if (tipo == IrrisoftConstantes.PLACA_SAMCLA) {

	    if (accionabrir) {
		tarea.setRegando(true);
		// Actualizo estado de la valvula
		valvu.setAbierta(true);
		valvu.setTareaasoc(tarea.getIdtarea());
		// La meto en Valvsabiertas
		IR.addvalvsabiertas(valvu, tipo);

	    } else {
		tarea.setRegando(false);
		// Actualizo estado de la valvula
		valvu.setAbierta(false);
		valvu.setTareaasoc(0);

		// Sea la valvula que sea la quito de la lista de valvulas
		// abiertas
		IR.quitarvalvabiertas(valvu, tipo);
		// Tb quito la tarea de la lista de tareasaexec
		// ListaTareasaexec.getInstance().delhilotar(tarea);
	    }

	}
    }

    // /////////////////
    // GETTER Y SETTER
    // /////////////////

    public Valvula getValvu() {
	return valvu;
    }

    public void setValvu(Valvula valvu) {
	this.valvu = valvu;
    }

    public SerialDriver getSerialcon() {
	return serialcon;
    }

    public void setSerialcon(SerialDriver serialcon) {
	this.serialcon = serialcon;
    }

}
