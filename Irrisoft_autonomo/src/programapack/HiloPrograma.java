package programapack;

import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;
import irrisoftpack.SerialDriver;

import java.sql.Time;
import java.sql.Timestamp;

import java.util.Calendar;
import java.util.Iterator;

import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import valvulaspack.Valvula;

import org.joda.time.DateTime;

import alertaspack.GestorAlertas;

public class HiloPrograma implements Runnable {

    private static Logger logger = LogManager.getLogger(HiloPrograma.class
	    .getName());
    private TareaProg tar;
    private Programacion prog;
    private int indicelista;
    private Valvula valv;
    // private String horact;
    // private String horaprog;
    //
    // //private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    // private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    // private Date ahora=null;
    // private Date horaprogini=null;
    private DateTime datefin;

    private int tiempoexec, auxcuota;

    private SerialDriver serialcon;

    private GestorAlertas gestorAlertas;

    private Irrisoft IR;

    // private long tiempoini,lapso;
    // private double lapsoriego;
    // private boolean ultima = false;
    // private boolean pasadadehorafin = false;

    /**
     * Constructor para poder pasarle la tarea como argumento
     * 
     * @param tar
     * @param z
     * @param prog
     */
    public HiloPrograma(TareaProg tar, int z, Programacion prog) {
	this.tar = tar;
	this.indicelista = z;
	this.prog = prog;
	gestorAlertas = GestorAlertas.getInstance();
	this.IR = Irrisoft.window;
    }

    @Override
    public synchronized void run() {

	// Le subo la prioridad a tope
	Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

	int valvu = Integer.parseInt(tar.getCodelecvalv());

	if (IrrisoftConstantes.MCI_200 > valvu) {
	    serialcon = IR.getSerie1();
	    valv = IR.valvsmci.getvalvmci(tar.getCodelecvalv());
	} else if (IrrisoftConstantes.MCI_200 < valvu
		&& IrrisoftConstantes.MCI_300 > valvu) {
	    serialcon = IR.getSerie2();
	    valv = IR.valvsmci2.getvalvmci(tar.getCodelecvalv());
	} else if (IrrisoftConstantes.MCI_300 < valvu
		&& IrrisoftConstantes.MCI_400 > valvu) {
	    serialcon = IR.getSerie3();
	    valv = IR.valvsmci3.getvalvmci(tar.getCodelecvalv());
	} else if (IrrisoftConstantes.MCI_400 < valvu
		&& IrrisoftConstantes.MCI_500 > valvu) {
	    serialcon = IR.getSerie4();
	    valv = IR.valvsmci4.getvalvmci(tar.getCodelecvalv());
	} else if (IrrisoftConstantes.BT2_1000 < valvu
		&& IrrisoftConstantes.BT2_2000 > valvu) {
	    serialcon = IR.getSerie5();
	    valv = IR.valvsbt2.getvalvbt2(tar.getCodelecvalv());
	} else if (IrrisoftConstantes.BT2_2000 < valvu
		&& IrrisoftConstantes.BT2_3000 > valvu) {
	    serialcon = IR.getSerie6();
	    valv = IR.valvsbt22.getvalvbt2(tar.getCodelecvalv());
	} else if (IrrisoftConstantes.BT2_3000 < valvu
		&& IrrisoftConstantes.BT2_4000 > valvu) {
	    serialcon = IR.getSerie7();
	    valv = IR.valvsbt23.getvalvbt2(tar.getCodelecvalv());
	} else if (IrrisoftConstantes.BT2_4000 < valvu
		&& IrrisoftConstantes.SAMCLA > valvu) {
	    serialcon = IR.getSerie8();
	    valv = IR.valvsbt24.getvalvbt2(tar.getCodelecvalv());
	} else if (IrrisoftConstantes.SAMCLA < valvu) {
	    valv = IR.valvsamcla.getvalvsamcla(tar.getCodelecvalv());
	}

	// Asocio la prog y la tarea correspondiente a la valvula
	valv.setTareaasoc(tar.getIdtarea());
	valv.setProgasoc(tar.getIdprog());

	// Para que coja las de otra hora
	// Hago esperar a que le avise el hilo anterior si no es la primera
	// tarea !
	if (indicelista != 0) {
	    if (logger.isInfoEnabled()) {
		logger.info("El hilo " + tar.getHilotar() + " de la valvula "
			+ tar.getCodelecvalv() + " se queda ESPERANDO.");
	    }
	    try {
		synchronized (tar.hilotar) {
		    tar.hilotar.wait();
		}

	    } catch (InterruptedException e) {
		if (logger.isErrorEnabled()) {
		    logger.error(e.getMessage());
		}
	    }

	}

	// /////// Empiezo a gestionar el programa si no se ha pasado de hora
	// (la primera siempre entra)
	// if(!pasadadehorafin )
	adminbloque(tar, indicelista);

    }

    // TODO Método supérfluo????
    /**
     * Miro si la tarea es en Serie o Paralelo y si es su hora de ejecutarse.
     * 
     * @param tar
     * @param z
     */
    private void adminbloque(TareaProg tar, int z) {

	if (logger.isInfoEnabled()) {
	    logger.info("TAREA: " + prog.getTareasprog().get(z).getIdtarea()
		    + " Valvula "
		    + prog.getTareasprog().get(z).getCodelecvalv() + ", bloque"
		    + prog.getTareasprog().get(z).getBloque());
	}
	// miro si es en serie o en paralelo
	if (tar.getTipobloque().equalsIgnoreCase(
		IrrisoftConstantes.BLOQUE_SERIE)) {
	    if (logger.isInfoEnabled()) {
		logger.info("Es SERIEs");
	    }
	    eslahora(tar);
	} else if (tar.getTipobloque().equalsIgnoreCase(
		IrrisoftConstantes.BLOQUE_PARALELO)) {
	    if (logger.isInfoEnabled()) {
		logger.info("Es PARALELOs");
	    }
	    eslahora(tar);
	}

	else {

	    logger.error("Tipo de adminbloque no encontrado");
	}

    }

    /**
     * La tarea progr espera el tiempo necesario hasta su ejecución.
     * 
     * @param tar
     */
    public void eslahora(TareaProg tar) {

	// Para calcular el delay de espera de la tarea hasta su ejecución

	Calendar cal = Calendar.getInstance();

	cal.setTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
	TimeZone tz = TimeZone.getTimeZone("Europe/Madrid");
	cal.setTimeZone(tz);
	DateTime dateact = new DateTime(cal.get(Calendar.YEAR),
		cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH),
		cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
		cal.get(Calendar.SECOND));

	DateTime dateini;
	dateini = new DateTime(tar.getFechaini());
	datefin = new DateTime(tar.getFechafin());

	logger.info("DATEFIN eslahora: " + datefin);
	logger.info("DATEINI eslahora: " + dateini);

	long delay = dateini.toDate().getTime() - dateact.toDate().getTime();
	long queda = delay;

	logger.info("DELAY: " + delay);

	if (dateini.isAfter(dateact)) {

	    if (indicelista == 0) {
		if (logger.isInfoEnabled()) {
		    logger.info("Programación (" + prog.getIdprograma()
			    + ") encontrada hoy, " + IR.hoyes);
		}

		IR.escribetextPane("\nProgramación (" + prog.getIdprograma()
			+ ") encontrada hoy, " + IR.hoyes + ": ", IR.negrita,
			false);
	    }
	    pintainfo(tar, false, Integer.toString(dateact.getHourOfDay())
		    + ":" + Integer.toString(dateact.getMinuteOfHour()) + ":"
		    + Integer.toString(dateact.getSecondOfMinute()));

	    IR.frmIrrisoft.repaint();

	    // Duermo el hilo hasta que sea la hora
	    try {
		if (delay > 0L) {

		    if (logger.isInfoEnabled()) {
			logger.info("Duermo hasta horaini "
				+ tar.getCodelecvalv());
		    }

		    // MIRAR LA HORA cada 10 segs y si es menos dormir lo que
		    // quede...

		    while (queda > 7000L) {
			cal.setTime(new Timestamp(Calendar.getInstance()
				.getTime().getTime()));
			dateact = new DateTime(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE),
				cal.get(Calendar.SECOND));

			if (dateini.isBefore(dateact)) {
			    if (logger.isInfoEnabled()) {
				logger.info("Ya ha llegado la hora y voy a regar");
			    }
			    break;
			}

			if (queda > 10000L) {
			    Thread.sleep(IrrisoftConstantes.DELAY_ESLAHORA_10SEG);
			    queda = queda - 10000L;

			} else {
			    if (logger.isInfoEnabled()) {
				logger.info("\nQuedan " + queda / 1000
					+ " segundos para abrir valvula "
					+ tar.getCodelecvalv() + "\n");
			    }

			    if (queda <= 7000L) {
				break;
			    } else {

				Thread.sleep(1000L);
				queda = queda - 1000L;
			    }
			}
		    }

		    // Genero alarma: Programa ya en funcionamiento.
		    gestorAlertas.insertarAlarma(2010);
		    accionvalv(tar);
		}

	    } catch (InterruptedException e) {
		if (logger.isErrorEnabled()) {
		    logger.error(e.getMessage());
		}
	    }
	} else if (datefin.isAfter(dateact)) {

	    if (logger.isWarnEnabled()) {
		logger.warn("Se ha pasado la hora de inicio. Se regarán menos valvúlas "
			+ tar.getHoraini());
	    }

	    if (indicelista == 0) {
		IR.escribetextPane("\nProgramación (" + prog.getIdprograma()
			+ ") encontrada hoy y empieza con retraso, " + IR.hoyes
			+ ": ", IR.negrita, false);
		// Genero alarma: Programa ya en funcionamiento
		gestorAlertas.insertarAlarma(2010);
	    }

	    // TODO Si hay que recalcular duración para válvula esto hay que
	    // revisarlo !!!

	    // Pongo el tiempo que lleva la tarea regado si es que lleva
	    pintainfo(tar, true, Integer.toString(dateact.getHourOfDay()) + ":"
		    + Integer.toString(dateact.getMinuteOfHour()) + ":"
		    + Integer.toString(dateact.getSecondOfMinute()));

	    IR.frmIrrisoft.repaint();

	    if (tar.isAbierta()) {
		regando(tar, valv);

		cierravalv(tar, serialcon);
		if (logger.isInfoEnabled()) {
		    logger.info("Cierro valvula " + tar.getCodelecvalv());
		}

	    } else {

		accionvalv(tar);
	    }
	} else {

	    logger.warn("La tarea de la válvula" + tar.getCodelecvalv()
		    + " se ha pasado de hora");

	    // Si se ha pasado de horafin, es decir no TIENE que regar pero si
	    // avisar a la siguiente !!

	    // aviso al siguiente hilo
	    // si el programa tiene más de una valvula
	    if (prog.getTareasprog().size() > 1) {
		if (indicelista < prog.getTareasprog().size() - 1) {
		    synchronized (prog.getTareasprog().get(indicelista + 1)
			    .getHilotar()) {
			prog.getTareasprog().get(indicelista + 1).getHilotar()
				.notify();
		    }
		}
	    }
	    return; // Para que no me marque la tarea a hecha y pueda generar
		    // una alarma !

	}

	// //Aki se ha pasado de hora con lo que la marco a hecha
	IR.hiloescucha.connDB.actualizatareaprogpasadas(tar, false);

	IR.frmIrrisoft.repaint();

    }

    /**
     * 
     * @param tar
     */
    public void accionvalv(TareaProg tar) {

	if (logger.isInfoEnabled()) {
	    logger.info("TAR: " + tar.getCodelecvalv() + ", bloque: "
		    + tar.getBloque() + " , tipoplaca: " + tar.getTipoplaca());
	}

	// Aviso a los hilos que tengan el mismo bloque para las prog
	// PARALELAS!!
	for (TareaProg p : prog.getTareasprog()) {

	    if (p.getBloque() == tar.getBloque()
		    && !p.getCodelecvalv().equals(tar.getCodelecvalv())
		    && tar.getHoraini().compareTo(p.getHoraini()) == 0) {

		synchronized (p.getHilotar()) {
		    p.getHilotar().notify();
		}

	    }
	}

	// Lo separo en dos métodos para tener un semaforo en la escritura y
	// otro en la lectura !!!
	abrevalv(tar, serialcon);

	regando(tar, valv);

	cierravalv(tar, serialcon);

    }

    /**
     * Aviso al serialDriver para que habra la valvula y actualizo su estado.
     * 
     * @param tar
     * @param serialcon
     */
    private boolean abrevalv(TareaProg tar, SerialDriver serialcon) {

	boolean fallo = false;
	
	IR.panelbt2.abremaestra();

	valv.setProgasoc(tar.getIdprog());

	if (tar.getTipoplaca() != -1) {// NO es autónoma
	    fallo = serialcon.abrevalv(valv);
	    actestvalv(tar, true);
	    // TODO La línea siguiente es superflúa?????
	    prog.getTareasprog().get(indicelista).setAbierta(true);

	} else {

	    // Es samcla o autónoma
	    IR.hiloescucha.connDB.insertafechatarea_auto(tar);
	    // Calculo la diferencia de tiempo entre ahora y el timestamp de la
	    // tarea
	    long dif_tiempo = (Calendar.getInstance().getTime().getTime() - tar
		    .getFechaini_auto().getTime()) / 1000L;

	    int tiempoaux = (int) (tar.getDuracion() - dif_tiempo);
	    // System.out.println("Tiempoaux en hiloprograma "+tiempoaux);

	    // Sólo acciono si hay que regar + de 1 minuto ( si es menos NO SE
	    // PUEDE)
	    if (tiempoaux >= 60) {

		fallo = IR.panelsamcla.interruptor(true, valv,
			IR.panelsamcla.duracion_samcla(tiempoaux), true);
		actestvalv(tar, true);
		// TODO La línea siguiente es superflúa?????
		prog.getTareasprog().get(indicelista).setAbierta(true);

	    } else {
		logger.warn("Se ha pasado el tiempo de riego establecido para la electroválvula.\nNo se hará nada!!");
	    }
	}
	
	return fallo;

    }

    /**
     * Aviso al SerialDriver para que cierre la valvula y actualizo su estado.
     * 
     * @param tar
     * @param serialcon
     */
    private boolean  cierravalv(TareaProg tar, SerialDriver serialcon) {

	boolean fallo = false;
	
	// aviso al siguiente hilo
	// si el programa tiene más de una valvula
	if (prog.getTareasprog().size() > 1) {
	    // Si no es la última valvula
	    if (indicelista < prog.getTareasprog().size() - 1) {
		synchronized (prog.getTareasprog().get(indicelista + 1)
			.getHilotar()) {
		    prog.getTareasprog().get(indicelista + 1).getHilotar()
			    .notify();
		}
	    }
	}

	try {
	    Thread.sleep(IrrisoftConstantes.DELAY_CIERRAVALV);
	} catch (InterruptedException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Hilo interrumpido: " + e.getMessage());
	    }
	}

	if (tar.getTipoplaca() != -1)
	    fallo = serialcon.cierravalv(tar.getCodelecvalv(), tar.getTipoplaca());
	else {

	    // Para que le de tiempo a abrir a la siguiente
	    try {
		Thread.sleep(6000);
	    } catch (InterruptedException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Hilo interrumpido: " + e.getMessage());
		}

	    }

	    fallo = IR.panelsamcla.interruptor(false, valv,
		    IR.panelsamcla.duracion_samcla(60), true);

	}

	if (!fallo)
	    actestvalv(tar, false);

	// Para que sólo me cierre la última válvula de la programación
	if (logger.isInfoEnabled()) {
	    logger.info("SIZE prog.GETTAREASPROG "
		    + prog.getTareasprog().size());
	    logger.info("SIZE valvsabiertastot " + IR.valvsabiertastot.size());
	}

	for (Iterator<Valvula> iter = IR.valvsabiertastot.iterator(); iter
		.hasNext();) {
	    System.out.println("Valvula" + iter.next().getCodelecvalv());
	}

	System.out.println("Maestras: " + IR.maestras);

	if (IR.maestra != null)
	    IR.panelbt2.cierramaestra();
	
	return fallo;

    }

    /**
     * @param tar
     */
    private void regando(TareaProg tar, Valvula valv) {

	int minuto = 0;

	try {
	    // Suelto el semarofo cerrojo
	    // semaforo(tar.getTipoplaca(),false);
	    tiempoexec = 0;

	    while (calculoduracion(tar) > 0) {

		Calendar cal = Calendar.getInstance();
		// cal.setTime(new
		// Timestamp(Calendar.getInstance().getTime().getTime()));
		TimeZone tz = TimeZone.getTimeZone("Europe/Madrid");
		cal.setTimeZone(tz);
		DateTime dateactual = new DateTime(cal.get(Calendar.YEAR),
			cal.get(Calendar.MONTH) + 1,
			cal.get(Calendar.DAY_OF_MONTH),
			cal.get(Calendar.HOUR_OF_DAY),
			cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));

		// logger.warn("DATEACTUAL regando: "+dateactual);
		// logger.warn("DATEFIN regando: "+datefin);

		// se ha pasado de la hora final de riego, lo paro !!
		// if (ahora.after(tar.getHorafin())){

		if (dateactual.isAfter(datefin) || dateactual.isEqual(datefin)) {

		    if (logger.isInfoEnabled()) {
			logger.info("Corto porque se ha salido de la ventana de riego; datefin: "
				+ datefin);
		    }
		    tar.setDuracion(0);
		    auxcuota = 0;
		    tar.setHecha(1);

		    cierravalv(tar, serialcon);

		}

		if (auxcuota > 60) {

		    // Comprobar funcionamiento mejorado del tiempo
		    // System.out.println("Duermo "+Thread.currentThread().getId());
		    java.util.concurrent.locks.LockSupport
			    .parkNanos(10000000000L);
		    tiempoexec = tiempoexec + 10;
		    minuto = minuto + 10;
		    tar.setTiemporegado(tiempoexec);

		    // Solo miro y actualizo si la valvula está abierta y sólo
		    // cada minuto

		    if (minuto == 60) {
			minuto = 0;
			if (valv.isAbierta())
			    IR.hiloescucha.connDB.ponetiemporegado(tar, null,
				    tar.getTiemporegado());
		    }

		} else if (auxcuota <= 60 && auxcuota != 0) {

		    Thread.sleep(IrrisoftConstantes.DELAY_REGANDO); // SON
								    // MILISEGUNDOOOOOOOOOOOOOOS
		    // tiempoexec=tiempoexec+(int)tar.getDuracion();
		    tiempoexec = tiempoexec + 10;
		    tar.setTiemporegado(tiempoexec);

		}
	    }
	    
	    // Solo miro y actualizo si la valvula está abierta
	    if (valv.isAbierta())
		IR.hiloescucha.connDB.ponetiemporegado(tar, null,
			tar.getTiemporegado());

	} catch (InterruptedException e) {
	    if (logger.isErrorEnabled())
		logger.error(e.getMessage());

	}

    }

    /**
     * Calculo la duracion de regado de las valvulas.
     * 
     * @param tar
     * @return
     */
    private int calculoduracion(TareaProg tar) {
	if (logger.isInfoEnabled()) {
	    logger.info("Duracion antes " + tar.getDuracion());
	}

	if (tiempoexec == 0) {
	    // Si es de la BT2 le resto unos segundos a la duracion para que no
	    // se me pase de madre por el retardo físico
	    if (Integer.parseInt(tar.getCodelecvalv()) > 1000) {
		tar.setDuracionini((int) (tar.getDuracion() - tar
			.getTiemporegado()) - 5); // -5
		tar.setDuracion((int) (tar.getDuracion() - tar
			.getTiemporegado()) - 5);
	    } else {
		tar.setDuracionini((int) (tar.getDuracion() - tar
			.getTiemporegado()) - 3); // -3
		tar.setDuracion((int) (tar.getDuracion() - tar
			.getTiemporegado()) - 3);
	    }

	    auxcuota = tar.getDuracionini();

	} else {

	    // Cuotas
	    if (logger.isInfoEnabled()) {
		logger.info("cuota en duracion: " + tar.getCuota());
	    }
	    if (tar.getDuracion() != 0) {

		// Para que entre cuando hay un cambio de cuota
		if (tar.isCambiocuota() && prog.getModo().contentEquals("A")) {
		    // Aki hay que hacer que una vez recibido el cambio de cuota
		    // no entre a recalcular la duración otra vez !!
		    tar.setDuracion(((tar.getDuracionini() * tar.getCuota()) / 100));

		    auxcuota = (int) tar.getDuracion();

		    tar.setCambiocuota(false);

		    if (logger.isInfoEnabled()) {
			logger.info("duracion tarea: " + tar.getDuracion());
			logger.info("duracionini tarea: "
				+ tar.getDuracionini());
		    }

		} else {
		    auxcuota = (int) (tar.getDuracion() - tar.getTiemporegado());
		}

		if (logger.isInfoEnabled()) {
		    logger.info("Duracion con cuota: " + auxcuota);
		}
	    }
	}

	// return (int) tar.getDuracion();
	return auxcuota;
	// double duracion = redondeoduracion(prog);
    }

    /**
     * Actualizo el estado de las valvulas en memoria.
     * 
     * @param tar
     * @param accionabrir
     * @throws ValvNoDefinida
     */
    public void actestvalv(TareaProg tar, boolean accionabrir)
	    throws ValvNoDefinida {

	if (valv == null) {
	    throw new ValvNoDefinida(
		    "No se puede actualizar estado de valvula porque no existe"); // Valvula
										  // valv
										  // =
										  // new
										  // Valvula();
	}
	// String numvalvu = tar.getCodelecvalv();
	int tipoPlac = tar.getTipoplaca();
	System.out.println("TIPOPLACA=" + tipoPlac);

	if (IrrisoftConstantes.PLACA_MCI_1 == tipoPlac
		|| IrrisoftConstantes.PLACA_MCI_2 == tipoPlac
		|| IrrisoftConstantes.PLACA_MCI_3 == tipoPlac
		|| IrrisoftConstantes.PLACA_MCI_4 == tipoPlac) {
	    if (IrrisoftConstantes.PLACA_MCI_1 == tipoPlac) {// Si es la primera
							     // placa MCI
		// valv=IR.valvsmci.getvalvmci(tar.getCodelecvalv());

		if (accionabrir) {
		    if (logger.isInfoEnabled()) {
			logger.info(IR.valvsmci);
		    }
		    // Actualizo estado de la valvula y la asocio con la tarea
		    // (id) y a un programa
		    valv.setAbierta(true);
		    valv.setTareaasoc(tar.getIdtarea());
		    valv.setProgasoc(tar.getIdprog());
		    // Repinto
		    IR.panelmci.interruptor(valv, valv.getImgasoc(), true,
			    IR.panelmci.panel1);
		    // La meto en Valvsabiertas
		    IR.addvalvsabiertas(valv, tipoPlac);
		} else {
		    // Reinicio idtarea de la valvula

		    valv.setAbierta(false);
		    valv.setTareaasoc(0);
		    valv.setProgasoc(0);
		    tar.setAbierta(false);
		    tar.setHecha(1);

		    // Repinto
		    IR.panelmci.interruptor(valv, valv.getImgasoc(), true,
			    IR.panelmci.panel1);
		    // Sea la valvula que sea la quito de la lista de valvulas
		    // abiertas
		    IR.quitarvalvabiertas(valv, tipoPlac);
		    //
		    // prog.getTareasprog().remove(indicelista);
		    // Tb quito la tarea de la lista de tareasaexec
		    // ListaTareasaexec.getInstance().delhilotar(tar);
		}
	    } else if (IrrisoftConstantes.PLACA_MCI_2 == tipoPlac) {// Si es la
								    // segunda
								    // placa MCI

		if (accionabrir) {
		    if (logger.isInfoEnabled()) {
			logger.info(IR.valvsmci2);
		    }
		    // Actualizo estado de la valvula y la asocio con la tarea
		    // (id) y a un programa
		    valv.setAbierta(true);
		    valv.setTareaasoc(tar.getIdtarea());
		    valv.setProgasoc(tar.getIdprog());
		    // Repinto
		    IR.panelmci.interruptor(valv, valv.getImgasoc(), true,
			    IR.panelmci.panel2);
		    // La meto en Valvsabiertas
		    IR.addvalvsabiertas(valv, tipoPlac);
		} else {
		    // Reinicio idtarea de la valvula
		    valv.setAbierta(false);
		    valv.setTareaasoc(0);
		    valv.setProgasoc(0);
		    tar.setAbierta(false);
		    tar.setHecha(1);

		    // Repinto
		    IR.panelmci.interruptor(valv, valv.getImgasoc(), true,
			    IR.panelmci.panel2);
		    // Sea la valvula que sea la quito de la lista de valvulas
		    // abiertas
		    IR.quitarvalvabiertas(valv, tipoPlac);
		    //
		    // prog.getTareasprog().remove(indicelista);
		    // Tb quito la tarea de la lista de tareasaexec
		    // ListaTareasaexec.getInstance().delhilotar(tar);
		}

	    } else if (IrrisoftConstantes.PLACA_MCI_3 == tipoPlac) {// Si es la
								    // tercera
								    // placa MCI

		if (accionabrir) {
		    if (logger.isInfoEnabled()) {
			logger.info(IR.valvsmci3);
		    }
		    // Actualizo estado de la valvula y la asocio con la tarea
		    // (id) y a un programa
		    valv.setAbierta(true);
		    valv.setTareaasoc(tar.getIdtarea());
		    valv.setProgasoc(tar.getIdprog());
		    // Repinto
		    IR.panelmci.interruptor(valv, valv.getImgasoc(), true,
			    IR.panelmci.panel3);
		    // La meto en Valvsabiertas
		    IR.addvalvsabiertas(valv, tipoPlac);
		} else {
		    // Reinicio idtarea de la valvula
		    valv.setAbierta(false);
		    valv.setTareaasoc(0);
		    valv.setProgasoc(0);
		    tar.setAbierta(false);
		    tar.setHecha(1);

		    // Repinto
		    IR.panelmci.interruptor(valv, valv.getImgasoc(), true,
			    IR.panelmci.panel3);
		    // Sea la valvula que sea la quito de la lista de valvulas
		    // abiertas
		    IR.quitarvalvabiertas(valv, tipoPlac);
		    //
		    // prog.getTareasprog().remove(indicelista);
		    // Tb quito la tarea de la lista de tareasaexec
		    // ListaTareasaexec.getInstance().delhilotar(tar);
		}
	    } else if (IrrisoftConstantes.PLACA_MCI_4 == tipoPlac) {// Si es la
								    // segunda
								    // placa MCI

		if (accionabrir) {
		    if (logger.isInfoEnabled()) {
			logger.info(IR.valvsmci4);
		    }
		    // Actualizo estado de la valvula y la asocio con la tarea
		    // (id) y a un programa
		    valv.setAbierta(true);
		    valv.setTareaasoc(tar.getIdtarea());
		    valv.setProgasoc(tar.getIdprog());
		    // Repinto
		    IR.panelmci.interruptor(valv, valv.getImgasoc(), true,
			    IR.panelmci.panel4);
		    // La meto en Valvsabiertas
		    IR.addvalvsabiertas(valv, tipoPlac);
		} else {
		    // Reinicio idtarea de la valvula
		    valv.setAbierta(false);
		    valv.setTareaasoc(0);
		    valv.setProgasoc(0);
		    tar.setAbierta(false);
		    tar.setHecha(1);

		    // Repinto
		    IR.panelmci.interruptor(valv, valv.getImgasoc(), true,
			    IR.panelmci.panel4);
		    // Sea la valvula que sea la quito de la lista de valvulas
		    // abiertas
		    IR.quitarvalvabiertas(valv, tipoPlac);
		    //
		    // prog.getTareasprog().remove(indicelista);
		    // Tb quito la tarea de la lista de tareasaexec
		    // ListaTareasaexec.getInstance().delhilotar(tar);
		}
	    }

	} else if (IrrisoftConstantes.PLACA_BT2_5 == tipoPlac
		|| IrrisoftConstantes.PLACA_BT2_6 == tipoPlac
		|| IrrisoftConstantes.PLACA_BT2_7 == tipoPlac
		|| IrrisoftConstantes.PLACA_BT2_8 == tipoPlac) { // Para
	    // la
	    // BT2
	    // int codelecvalv=0;

	    // if(tar.getTipoplaca()==5){

	    // valv=IR.valvsbt2.getvalvbt2(tar.getCodelecvalv());
	    // codelecvalv =
	    // IR.valvsbt2.getvalvbt2(tar.getCodelecvalv()).getIndex();
	    if (accionabrir) {
		// Actualizo estado de la valvula
		valv.setAbierta(true);
		valv.setTareaasoc(tar.getIdtarea());
		valv.setProgasoc(tar.getIdprog());
		// La meto en Valvsabiertas
		IR.addvalvsabiertas(valv, tipoPlac);

	    } else {
		// Actualizo estado de la valvula
		valv.setAbierta(false);
		valv.setTareaasoc(0);
		valv.setProgasoc(0);
		tar.setAbierta(false);
		tar.setHecha(1);
		// Asocio una idtarea con la valvula
		// IR.valvsbt2.getvalvbt2(codelecvalv).setTareaasoc(tar.getIdtarea());
		// Sea la valvula que sea la quito de la lista de valvulas
		// abiertas
		IR.quitarvalvabiertas(valv, tipoPlac);
		// Tb quito la tarea de la lista de tareasaexec
		// ListaTareasaexec.getInstance().delhilotar(tar);
	    }
	}

	else if (IrrisoftConstantes.PLACA_SAMCLA == tipoPlac) {
	    System.out.println("HOLA estoy en actuaestado hiloprograma !!");
	    if (accionabrir) {
		// Actualizo estado de la valvula
		valv.setAbierta(true);
		valv.setTareaasoc(tar.getIdtarea());
		valv.setProgasoc(tar.getIdprog());
		// La meto en Valvsabiertas
		IR.addvalvsabiertas(valv, tipoPlac);

	    } else {
		// Actualizo estado de la valvula
		valv.setAbierta(false);
		valv.setTareaasoc(0);
		valv.setProgasoc(0);
		tar.setAbierta(false);
		tar.setHecha(1);
		// Asocio una idtarea con la valvula
		// Sea la valvula que sea la quito de la lista de valvulas
		// abiertas
		IR.quitarvalvabiertas(valv, tipoPlac);

	    }

	}
    }

    /**
     * Voy informando del estado de la valvula.
     * 
     * @param tar
     * @param pasada
     * @param horactual
     */
    private void pintainfo(TareaProg tar, boolean pasada, String horactual) {
	Time horaini = null;

	if (pasada)
	    horaini = Time.valueOf(horactual);
	else
	    horaini = tar.getHoraini();
	if (logger.isInfoEnabled()) {
	    logger.info(horaini.toString().substring(0, 8) + " c "
		    + "abrir valvula " + tar.getCodelecvalv() + " "
		    + (tar.duracion * tar.getCuota()) / 100 + " sgs");
	}

	IR.escribetextPane("\n   " + horaini.toString().substring(0, 8)
		+ " hrs, " + "abrir valvula " + tar.getCodelecvalv() + " "
		+ (tar.duracion * tar.getCuota()) / 100 + " sgs", IR.italic,
		false);

	IR.frmIrrisoft.repaint();

	tar = null;
	horactual = null;
	horaini = null;
    }

    // //////////////////////////
    // GETTER Y SETTER
    // //////////////////////////

    public Valvula getValv() {
	return valv;
    }

    public void setValv(Valvula valv) {
	this.valv = valv;
    }

    public Programacion getProg() {
	return prog;
    }

    public void setProg(Programacion prog) {
	this.prog = prog;
    }

    public int getIndicelista() {
	return indicelista;
    }

    public void setIndicelista(int indicelista) {
	this.indicelista = indicelista;
    }
}
