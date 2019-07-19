package irrisoftpack;

import java.awt.Color;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import alertaspack.Alerta;
import alertaspack.GestorAlertas;

import programapack.HiloPrograma;
import programapack.ListaProgsaexec;
import programapack.Programacion;
import programapack.TareaProg;
import sensorespack.HiloCaudalimetro;
import sensorespack.LecturasSensor;
import sensorespack.Sensor;
import tareapack.HiloTarea;
import tareapack.TareaManual;
import valvulaspack.Valvula;

public class ConexionDB {

    private static Logger logger = LogManager.getLogger(ConexionDB.class
	    .getName());

    protected TareaManual tarea;
    protected TareaProg tar;
    protected Connection conn = null;
    protected Timestamp fechapri, fechactual;
    protected DateTime hora;
    protected Programacion prog;
    protected HiloTarea hilotarea;
    protected HiloPrograma hiloprog;
    protected boolean conectado = false;
    protected LinkedList<Programacion> listaprogs = ListaProgsaexec
	    .getInstance().getprogramas();
    protected static SimpleDateFormat formatterfecha = new SimpleDateFormat(
	    "yyyy-MM-dd");
    protected static SimpleDateFormat formatfecha = new SimpleDateFormat(
	    "dd-MM-yyyy HH:mm");
    protected ArrayList<Integer> diass = new ArrayList<Integer>();
    protected ArrayList<Date> diasd = new ArrayList<Date>();
    protected int id, idds;
    protected String leidat;
    protected int tiempoexec;

    public boolean leidop = false;
    public boolean existe = false;

    public String hoyes;
    Date hoy = new Date();
    Date fechain = new Date();
    Date fechafin = new Date();
    protected boolean apagarvalvs;

    private Irrisoft IR;
    public boolean flagCaudalimetro = true;

    public ConexionDB() {
	this.IR = Irrisoft.window;

    }

    // METODOS DE CONEXION Y DESCONEXION DE LA BBDD LOCAL
    // /////////////////////////////////////////
    /**
     * Gestión conexiones a las BBDD Me conecto a la BBDD local.
     * 
     * @return
     */
    public boolean conectal() {

	if (conectado == false) {
	    try {
		Class.forName("com.mysql.jdbc.Driver");
		String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost()
			+ ":" + Irrisoft.config.getPuerto() + "/"
			+ Irrisoft.config.getDb() + "?autoReconnect=true";

		DriverManager.setLoginTimeout(5);
		conn = DriverManager
			.getConnection(urllocal, Irrisoft.config.getUsuario(),
				Irrisoft.config.getPass());

		conectado = true;
		IR.lblstatusl.setForeground(new Color(0, 128, 0));
		IR.lblstatusl.setText("Conectado");

	    } catch (SQLException | ClassNotFoundException e) {
		if (e instanceof SQLException && logger.isErrorEnabled()) {
		    logger.error("Error en la Base de Datos: " + e.getMessage());
		}
		if (e instanceof ClassNotFoundException
			&& logger.isErrorEnabled()) {
		    logger.error("Error en conectar DB Local " + e.getMessage());
		}
		IR.lblstatusl.setForeground(Color.RED);
		IR.lblstatusl.setText("Desconectado");
		IR.cierraDBhilo();

	    }
	}

	return conectado;

    }

    /**
     * Cierro la conexion a la BBDD local.
     * 
     * @return
     */
    public boolean desconectal() {
	boolean cerrado = false;
	if (conn != null) {
	    try {
		conn.close();

		IR.lblstatusl.setForeground(Color.RED);
		IR.lblstatusl.setText("Desconectado");
		cerrado = true;
		conectado = false;
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error a la hora de desconectar la BD Local: "
			    + e.getMessage());
		}
		cerrado = false;
	    }
	}

	return cerrado;
    }

    // CREACION Y GESTION DE TAREAS /////////////////////////////////////////
    /**
     * Comprueba TAREAS pendientes, synchronized para que no puedan acceder
     * varioshilos a la vez
     */
    protected synchronized void tarea() {
	tarea = null;

	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    conectal();
	    // Consulto tareas pendientes de ejecutar
	    ps = conn.prepareStatement("select * from tarea where CODPROG="
		    + Irrisoft.config.getIdrasp());
	    rs = ps.executeQuery();

	    while (rs.next()) {
		// controlar si es tarea manual o programación.
		idds = rs.getInt(4);
		// id = resultados.getInt(1);

		leidat = rs.getString(10);
		tiempoexec = rs.getInt(11);

		// TODO Añadir campo leída en BBDD local para quitar esta
		// condición que crea problemas cuando GIS reinicia el servidor
		// Compruebo que es una tarea nueva !!
		// Aki tiene que ir la comprobación de leído y
		// if (IR.idtareaexec < id) {

		if (leidat.contains("N")) {

		    // if (idds != 6)
		    // IR.idtareaexec = id;

		    // Si son tareas manuales
		    if (IrrisoftConstantes.ABRIR_ESTACION == idds
			    || IrrisoftConstantes.CERRAR_ESTACION == idds
			    || IrrisoftConstantes.RECALCULO_PROGRAMACION == idds
			    || IrrisoftConstantes.APAGAR_PROGRAMADOR == idds) {

			tarea = new TareaManual();

			tarea.setIddstarea(rs.getInt(4));
			tarea.setIdtarea(rs.getInt(1));
			tarea.setCodprog(rs.getString(2));
			tarea.setCodelecvalv(rs.getString(3));
			tarea.setFcexec(rs.getTimestamp(5));
			tarea.setValor(rs.getInt(6));
			tarea.setValorstr(rs.getInt(7));
			tarea.setCodtarea(rs.getString(8));
			tarea.setDstarea(rs.getString(9));
			// Añado estos campos para controlar si se ha leido y el
			// tiempo que se ha ejecutado.
			tarea.setLeida("S");
			ponertareamanualeida(tarea, false);
			tarea.setTiemporegado(tiempoexec);

			IR.escribetextPane(
				"\n Nueva tarea (" + tarea.getIdtarea()
					+ ") recibida: " + tarea.getIddstarea()
					+ " " + tarea.getDstarea(), IR.negrita,
				false);

			if (idds != IrrisoftConstantes.APAGAR_PROGRAMADOR) {

			    if (tarea.getCodelecvalv() == null) {
				if (IrrisoftConstantes.CERRAR_ESTACION == idds) {
				    apagarvalvs = true;
				}
			    } else {
				if (Integer.parseInt(tarea.getCodelecvalv()) < 10)
				    tarea.setCodelecvalv("0"
					    + tarea.getCodelecvalv());
			    }
			}

			if (IrrisoftConstantes.ABRIR_ESTACION == tarea
				.getIddstarea()) {
			    fechapri = tarea.getFcexec();
			    tarea.setFcloc(fechapri);

			    ejecutarea(tarea);

			    // Lo pongo para que le de tiempo a ejecutar tareas
			    // simultaneas (Ajustable)(A REVISAR) !!
			    try {
				Thread.sleep(IrrisoftConstantes.DELAY_TAREA_ABRIR);
			    } catch (InterruptedException e) {
				if (logger.isErrorEnabled()) {
				    logger.error("El Hilo donde esta conexiondb ha sido interrumpido "
					    + e.getMessage());
				}
			    }

			} else if (IrrisoftConstantes.CERRAR_ESTACION == tarea
				.getIddstarea()) { // Es cerrar
			    // valvulas
			    logger.warn("Entra en idds 2");
			    if (apagarvalvs) {// Si es apagar todas las
					      // estaciones !!!
				IR.escribetextPane(
					"\n Atención se van a cerrar todas las valvulas!",
					IR.normal, false);

				IR.frmIrrisoft.repaint();
				cierratodasvalvs();
			    }

			} else if (IrrisoftConstantes.APAGAR_PROGRAMADOR == tarea
				.getIddstarea()) { // Es apagar el
			    // programador...

			    if (logger.isInfoEnabled()) {
				logger.info("APAGANDO el sistema...");
			    }

			    IR.escribetextPane(
				    "\n Atención se va a apagar el sistema!",
				    IR.normal, false);

			    IR.escribetextPane(
				    "\n  Se cerrarán todas las valvulas!",
				    IR.normal, false);

			    IR.frmIrrisoft.repaint();
			    try {
				// TODO Habría que guardar el estado antes de
				// apagar o algo ¿?
				cierratodasvalvs();

				String[] cmd = new String[] { "/bin/sh",
					Irrisoft.comandoapaga };
				Runtime.getRuntime().exec(cmd);

			    } catch (IOException e) {
				if (logger.isErrorEnabled()) {
				    logger.error(" " + e.getMessage());
				}
			    }

			} else if (IrrisoftConstantes.RECALCULO_PROGRAMACION == tarea
				.getIddstarea()) { // Es un cambio
			    // de cuota

			    if (logger.isInfoEnabled()) {
				logger.info("Es un cambio de cuota!!");
			    }

			    // Hay que distinguir si la programación es
			    // AUTOMÁTICA o MANUAL
			    // Recorro la lista de programas y asocio la nueva
			    // cuota a este programa.

			    for (int i = 0; i < listaprogs.size(); i++) {
				if (logger.isInfoEnabled()) {
				    logger.info("Programa: "
					    + listaprogs.get(i).getIdprograma());
				}

				if (listaprogs.get(i).getIdprograma() == tarea
					.getValorstr()) {
				    if (logger.isInfoEnabled()) {
					logger.info("Couta actual: "
						+ listaprogs.get(i).getCuota());
					logger.info("Nueva cuota ("
						+ tarea.getValor()
						+ ") encontrada para la programacion "
						+ listaprogs.get(i)
							.getIdprograma());
				    }

				    IR.escribetextPane(
					    "\n Nueva cuota ("
						    + tarea.getValor()
						    + ") encontrada para la prog "
						    + listaprogs.get(i)
							    .getIdprograma(),
					    IR.italic, false);

				    listaprogs.get(i)
					    .setCuota(tarea.getValor());

				    for (int j = 0; j < listaprogs.get(i)
					    .getTareasprog().size(); j++) {
					listaprogs.get(i).getTareasprog()
						.get(j)
						.setCuota(tarea.getValor());
					listaprogs.get(i).getTareasprog()
						.get(j).setCambiocuota(true);

				    }

				    if (logger.isWarnEnabled()) {
					logger.warn("Cuota cambiada: "
						+ listaprogs.get(i).getCuota());
				    }
				}

			    }
			    // llamo a borrartarea...
			    Borratarea(tarea);
			}
		    }
		    tarea = null;
		} // del if dela comprobación si existe
	    }
	    rs.close();

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error 368");
		logger.error("Error en la sentencia de tarea" + e.getMessage());
	    }
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		desconectal();
	    }
	}
    }

    /**
     * Cierro valvula desde tarea.
     */
    protected void cierratodasvalvs() {

	Valvula v;

	for (Iterator<Valvula> iter = IR.valvsabiertastot.iterator(); iter
		.hasNext();) {
	    v = iter.next();
	    iter.remove();

	    if (IrrisoftConstantes.PLACA_MCI_1 == v.getNum_placa()) {
		IR.panelmci.interruptor(v, v.getImgasoc(), IR.panelmci.panel1);
	    } else if (IrrisoftConstantes.PLACA_MCI_2 == v.getNum_placa()) {
		IR.panelmci.interruptor(v, v.getImgasoc(), IR.panelmci.panel2);
	    } else if (IrrisoftConstantes.PLACA_MCI_3 == v.getNum_placa()) {
		IR.panelmci.interruptor(v, v.getImgasoc(), IR.panelmci.panel3);
	    } else if (IrrisoftConstantes.PLACA_MCI_4 == v.getNum_placa()) {
		IR.panelmci.interruptor(v, v.getImgasoc(), IR.panelmci.panel4);
	    } else if (IrrisoftConstantes.PLACA_BT2_5 == v.getNum_placa())
		IR.panelbt2.interruptor(v, 2, 5);
	    else if (IrrisoftConstantes.PLACA_BT2_6 == v.getNum_placa())
		IR.panelbt2.interruptor(v, 2, 6);
	    else if (IrrisoftConstantes.PLACA_BT2_7 == v.getNum_placa())
		IR.panelbt2.interruptor(v, 2, 7);
	    else if (IrrisoftConstantes.PLACA_BT2_8 == v.getNum_placa())
		IR.panelbt2.interruptor(v, 2, 8);
	    else if (IrrisoftConstantes.PLACA_SAMCLA == v.getNum_placa())
		IR.panelsamcla.interruptor(false, v, "1", false);
	}
	// Borro la tarea de cerrar las programaciones.Irrisoft.config.getBt22()
	// Además borro todas las tareas de apertura que haya en la tabla tarea
	// !!
	Borratarea(tarea);
	Borratarea();
    }

    /**
     * Ejecuta tareas manuales pendientes
     * 
     * @param taream
     */
    protected synchronized void ejecutarea(TareaManual taream) {

	taream.setDuracion(taream.getValor());
	int tareaCod = Integer.parseInt(taream.getCodelecvalv());

	Valvula valv = new Valvula();

	if (logger.isInfoEnabled()) {
	    logger.info("Ejecuta valv: " + taream.getCodelecvalv());
	}

	// / MCIs
	if (IrrisoftConstantes.MCI_200 > tareaCod) {
	    // Cojo la valvula a tratar
	    valv = IR.valvsmci.getvalvmci(taream.getCodelecvalv());
	} else if (IrrisoftConstantes.MCI_200 < tareaCod
		&& IrrisoftConstantes.MCI_300 > tareaCod) {
	    valv = IR.valvsmci2.getvalvmci(taream.getCodelecvalv());
	} else if (IrrisoftConstantes.MCI_300 < tareaCod
		&& IrrisoftConstantes.MCI_400 > tareaCod) {
	    valv = IR.valvsmci3.getvalvmci(taream.getCodelecvalv());
	} else if (IrrisoftConstantes.MCI_400 < tareaCod
		&& IrrisoftConstantes.BT2_1000 > tareaCod) {
	    valv = IR.valvsmci4.getvalvmci(taream.getCodelecvalv());
	}

	// BT2s
	else if (IrrisoftConstantes.BT2_1000 < tareaCod
		&& IrrisoftConstantes.SAMCLA > tareaCod) {

	    if (IrrisoftConstantes.BT2_2000 > tareaCod) {
		// Cojo la valvula a tratar
		valv = IR.valvsbt2.getvalvbt2(taream.getCodelecvalv());
	    } else if (IrrisoftConstantes.BT2_2000 < tareaCod
		    && IrrisoftConstantes.BT2_3000 > tareaCod) {
		valv = IR.valvsbt22.getvalvbt2(taream.getCodelecvalv());
	    } else if (IrrisoftConstantes.BT2_3000 < tareaCod
		    && IrrisoftConstantes.BT2_4000 > tareaCod) {
		valv = IR.valvsbt23.getvalvbt2(taream.getCodelecvalv());
	    } else if (IrrisoftConstantes.BT2_4000 < tareaCod) {
		valv = IR.valvsbt24.getvalvbt2(taream.getCodelecvalv());
	    }

	}
	// SAMCLA
	else if (IrrisoftConstantes.SAMCLA < tareaCod) {

	    valv = IR.valvsamcla.getvalvsamcla(tarea.getCodelecvalv());

	}

	// Compruebo si la valvula ya está abierta por otra tarea y la
	// desecho!!
	if (valv.isAbierta()) {
	    // Compruebo si la valvula ya está abierta por otra tarea y la
	    // desecho!!
	    IR.escribetextPane(
		    "\nLa válvula " + taream.getCodelecvalv()
			    + " ya se encuentra abierta por \nla tarea "
			    + valv.getTareaasoc(), IR.normal, false);

	} else {
	    if (Integer.parseInt(valv.getCodelecvalv()) != -1)
		ejecutatareahilo(taream);
	    else {
		IR.escribetextPane(
			"\nLa tarea " + taream.getIdtarea()
				+ " no se puede ejecutar: \nLa válvula "
				+ taream.getCodelecvalv() + " no existe.",
			IR.italic, false);
		IR.frmIrrisoft.repaint();
		// Llamo a borratarea
		if (IR.hiloescucha.connDB.Borratarea(taream)) {
		    IR.escribetextPane("\nTarea " + taream.getIdtarea()
			    + " borrada correctamente.", IR.normal, false);
		}
	    }
	}
    }

    /**
     * Borra tareas de la BBDD local por ID de tarea.
     * 
     * @param taream
     * @return
     */
    public synchronized boolean Borratarea(TareaManual taream) {

	// Cuidado con borrar tareas diferentes a las solicitadas
	PreparedStatement ps = null;
	Boolean borrada = false;

	try {
	    conectal();
	    ps = conn.prepareStatement("DELETE FROM tarea WHERE idtarea=?");
	    ps.setInt(1, taream.getIdtarea());
	    ps.executeUpdate();
	    borrada = true;
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error en la sentencia de Borrar Tarea"
			+ e.getMessage());
		logger.error("No se ha podido borrar la tarea en la BBDD local.");
	    }

	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		desconectal();
	    }
	}
	tar = null;
	return borrada;
    }

    /**
     * Borra tareas de la BBDD local por IDDSTAREA Y CODPROG.
     * 
     * @return
     */
    public synchronized boolean Borratarea() {

	PreparedStatement ps = null;
	Boolean borrada = false;

	try {
	    conectal();
	    ps = conn
		    .prepareStatement("DELETE FROM tarea WHERE iddstarea='1' AND codprog=?");
	    ps.setString(1, Irrisoft.config.getIdrasp());
	    ps.executeUpdate();
	    borrada = true;
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error en la sentencia de BorraTarea"
			+ e.getMessage());
		logger.error("No se han podido borrar las tareas en la BBDD local.");
	    }
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		desconectal();
	    }
	}

	return borrada;
    }

    /**
     * Voy ejecuntado las tareas con sus hilos correspondientes.
     * 
     * @param taream
     */
    public synchronized void ejecutatareahilo(TareaManual taream) {

	// if (getHilotarea() == null) {
	HiloTarea ht = new HiloTarea(taream);

	setHilotarea(ht);

	Thread th = new Thread(ht);
	th.setName("TareaM " + th.getId());
	taream.setHilotar(th);
	taream.hilotar.start();
    }

    // CREACION Y GESTION DE PROGRAMAS /////////////////////////////////
    /**
     * Comprueba PROGRAMACIONES pendientes y si las hay las copia a la local y
     * borra la tarea y demases de la paserela
     */
    protected void programa() {

	PreparedStatement ps = null;
	ResultSet rs = null;
	boolean mirarFecha = true;

	hoyes = new Timestamp(Calendar.getInstance().getTime().getTime())
		.toString();
	hoyes = hoyes.substring(0, 10);

	hora = new DateTime();

	try {
	    hoy = formatterfecha.parse(hoyes);
	} catch (ParseException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error a la hora de parsear la fecha."
			+ e.getMessage());
	    }
	}
	// Metodo mandar los consumos diarios de agua entre las 11 y las 12 de la mañana
	if (hora.getHourOfDay() >= 0 && hora.getHourOfDay() < 1) {
	    // Miro si hay alguna fila de consumo diario insertada
	    mirarFecha = devolverFilaConsumo(hoy);
	    if (mirarFecha == false) {
		if (flagCaudalimetro == true) {
		    //Envio el consumo diario
		    enviarRegSensoresBBDD();
		    IR.hilocau.flag = true;
		    flagCaudalimetro = false;
		}
	    } else {
		flagCaudalimetro = true;
	    }
	}

	// Para que siga si el día ha cambiado
	if (hoy.getTime() > IR.hoy.getTime()) {

	    leidop = false;
	    IR.hoy = hoy;
	    if (logger.isInfoEnabled()) {
		logger.info("Ha cambiado el día");

	    }
	}

	if (logger.isTraceEnabled()) {
	    logger.info("LeidoP: " + leidop);
	}

	if (leidop == false) {

	    try {
		conectal();
		// Consulto programaciones pendientes de ejecutar
		ps = conn
			.prepareStatement("select * from programa where CODPROG="
				+ Irrisoft.config.getIdrasp());
		rs = ps.executeQuery();

		while (rs.next()) {

		    existe = false;
		    diass.clear();

		    // Controlo que la prog no ha sido tratada y si no la trato
		    // Poner a null al final para liberar memoria!!!!!
		    int id = rs.getInt(1);
		    fechain = rs.getDate(4);
		    fechafin = rs.getDate(5);
		    Date ultimosdeberes = rs.getDate(22);
		    String tipo = rs.getString(8);
		    String activo = rs.getString(6);

		    // Solo entro si el programa está activo y si está entre los
		    // rangos de fecha
		    if (activo
			    .contentEquals(IrrisoftConstantes.PROGRAMA_ACTIVO)
			    && (hoy.getTime() >= fechain.getTime())
			    && (hoy.getTime() <= fechafin.getTime())) {

			if (logger.isInfoEnabled()) {
			    logger.info("El programa " + rs.getInt(1)
				    + " está activo y hoy le toca ejecutarse.");
			}
			// Miro si el programa existe en la listaprogs
			//
			for (int i = 0; i < listaprogs.size(); i++) {

			    if (listaprogs.get(i).getIdprograma() == id) {
				if (logger.isInfoEnabled()) {
				    logger.info("El programa " + id
					    + " ya existe en memoria.");
				}
				existe = true;
			    }
			}

			// Si la programacion no existe en memoria
			if (!existe) {

			    // inserto los dias en memoria
			    for (int i = 0; i < 7; i++) {
				diass.add(rs.getInt(i + 9));
			    }

			    if (esfecha(id, tipo, fechain, fechafin, hoy, diass)) {

				if (ultimosdeberes == null
					|| (ultimosdeberes.before(hoy))) {
				    if (logger.isInfoEnabled()) {
					logger.info("Creo objeto prog");

				    }
				    prog = new Programacion();
				    prog.setIdprograma(rs.getInt(1));
				    prog.setFcinicio(rs.getDate(4));
				    prog.setFcFin(rs.getDate(5));
				    prog.setLeido(rs.getString(20));
				    prog.setCodprograma(rs.getString(2));
				    prog.setDsprograma(rs.getString(3));
				    prog.setActivo(rs.getString(6));
				    prog.setCodprog(rs.getString(7));
				    prog.setTipo(rs.getString(8));
				    prog.setDial(rs.getInt(9));
				    prog.setDiam(rs.getInt(10));
				    prog.setDiax(rs.getInt(11));
				    prog.setDiaj(rs.getInt(12));
				    prog.setDiav(rs.getInt(13));
				    prog.setDias(rs.getInt(14));
				    prog.setDiad(rs.getInt(15));
				    prog.setModo(rs.getString(16));
				    prog.setModoini(rs.getString(17));
				    prog.setPbloque(rs.getInt(18));
				    prog.setCuota(rs.getInt(19));
				    prog.setEnmarcha(rs.getString(21));
				    prog.setUltdeberes(rs.getDate(22));

				    datosprog(prog);
				    tipoprog(prog, hoy);
				} else {
				    logger.info("La programación " + id
					    + " ya se ha ejecutado hoy: " + hoy);
				}

			    }

			}

		    }
		    // Si se ha pasado la fecha de ejecución
		    if (fechafin.before(hoy)) {
			if (logger.isInfoEnabled()) {
			    logger.info("Se ha pasado la fechafin de la programación "
				    + id
				    + " , ultdeberes: "
				    + ultimosdeberes
				    + "\nSe borrará de la BBDD local");
			}
			// ZUMBARME programacion de la BBDD
			borraprogramacion(id);
		    }

		    // Para que le de tiempo a presentar en el textarea todo
		    // correctamente
		    try {
			Thread.sleep(IrrisoftConstantes.TIEMPO_EJECUCION_MEDIO_SEG);
		    } catch (InterruptedException e) {
			if (logger.isErrorEnabled()) {
			    logger.error("Error, Hilo interrumpido: "
				    + e.getMessage());
			}
		    }

		}// del while

		leidop = true;
		rs.close();

	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error 776");
		    logger.error("No estás conectado con la BBDD");
		    logger.error("Error en la sentencia de Programa"
			    + e.getMessage());
		}
	    } finally {
		try {
		    ps.close();
		} catch (SQLException e) {
		    if (logger.isErrorEnabled()) {
			logger.error("Error en la sentencia." + e.getMessage());
		    }
		    desconectal();
		}
	    }

	}// LEIDOP

	Date ahora = new Date(Calendar.getInstance().getTime().getTime());
	ArrayList<TareaProg> tareasnohechas = new ArrayList<TareaProg>();

	// recorro las listas de programaciones para ver si alguna ha terminado
	// y proceder a su borrad

	for (int i = 0; i < listaprogs.size(); i++) {
	    // Recorro lista tareas
	    if (logger.isInfoEnabled()) {
		logger.info("Size listatareasProg de prog "
			+ listaprogs.get(i).getIdprograma() + ": "
			+ listaprogs.get(i).getTareasprog().size());
	    }

	    int conthecha = 0;

	    // recorro la lista de tareas del programa i
	    for (int j = 0; j < listaprogs.get(i).getTareasprog().size(); j++) {

		if (listaprogs.get(i).getTareasprog().get(j).isHecha() == 1) {

		    conthecha++;
		}
	    }// for de tarea

	    if ((conthecha != 0 && conthecha == listaprogs.get(i)
		    .getTareasprog().size())) {

		if (logger.isInfoEnabled()) {
		    logger.info("Conthecha = " + conthecha);
		    logger.info("Todas las taras hechas. Las borro, prog "
			    + listaprogs.get(i).getIdprograma());
		}
		// Actualizo el estado de enmarcha a N.
		// actualizarEstadoN();
		// borro BBDD local
		actualizadeberes(
			listaprogs.get(i).getIdprograma(),
			formatterfecha.format(listaprogs.get(i).getTareasprog()
				.get(0).getFechaini()));

		borratareaprog(listaprogs.get(i).getIdprograma());

		// Para que el gc me los quite de la memoria
		listaprogs.get(i).getValvuprog().clear();
		listaprogs.get(i).getDays().clear();
		listaprogs.get(i).getHorasfin().clear();
		listaprogs.get(i).getHorasini().clear();
		listaprogs.get(i).getTareasprog().clear();

		if (logger.isWarnEnabled()) {
		    logger.warn("Programa borrado de memoria: "
			    + listaprogs.get(i).getIdprograma());
		}

		listaprogs.set(i, null);
		listaprogs.remove(i);

		leidop = false;

	    }
	    // Se ha quedado alguna sin hacer y se genera alarma de no hecha
	    else if (new Date(listaprogs.get(i).getTareasprog()
		    .get(listaprogs.get(i).getTareasprog().size() - 1)
		    .getFechafin().getTime() + 10000).before(ahora)) {

		for (int j = 0; j < listaprogs.get(i).getTareasprog().size(); j++) {

		    if (listaprogs.get(i).getTareasprog().get(j).isHecha() == 0) {
			// Generaría el texto de la alarma con el código
			// electroválvula
			tareasnohechas.add(listaprogs.get(i).getTareasprog()
				.get(j));
		    }
		}
		// Actualizo el estado de enmarcha a N.
		// actualizarEstadoN();
		actualizadeberes(

			listaprogs.get(i).getIdprograma(),
			formatterfecha.format(listaprogs.get(i).getTareasprog()
				.get(0).getFechaini()));
		borratareaprog(listaprogs.get(i).getIdprograma());

		if (logger.isWarnEnabled()) {
		    logger.warn("Programa borrado de memoria: "
			    + listaprogs.get(i).getIdprograma());
		    // TODO Aki tengo que generar y enviar a GI la alarma
		    logger.fatal("ALARMA ! No se han terminado todas las tareas.");
		    for (int z = 0; z < tareasnohechas.size(); z++) {

			logger.fatal("Valvula "
				+ tareasnohechas.get(z).getCodelecvalv()
				+ " del bloque "
				+ tareasnohechas.get(z).getBloque()
				+ " de la hora de inicio "
				+ tareasnohechas.get(z).getHoraini());

		    }
		}
		// Para que el gc me los quite de la memoria
		listaprogs.get(i).getValvuprog().clear();
		listaprogs.get(i).getDays().clear();
		listaprogs.get(i).getHorasfin().clear();
		listaprogs.get(i).getHorasini().clear();
		listaprogs.get(i).getTareasprog().clear();
		listaprogs.set(i, null);
		listaprogs.remove(i);

		leidop = false;

	    }

	}

    }

    /**
     * Miro si hoy es el dia para regar, ya sea diario o semanal.
     * 
     * @param id
     * @param tipo
     * @param fechain
     * @param fechafin
     * @param hoy
     * @param dias
     * @return
     */
    private boolean esfecha(int id, String tipo, Date fechain, Date fechafin,
	    Date hoy, ArrayList<Integer> dias) {

	logger.info("fechain: " + fechain);
	logger.info("fechafin: " + fechafin);
	logger.info("hoy: " + hoy);

	boolean eshoy = false;
	PreparedStatement ps = null;
	ResultSet rs = null;

	if (tipo.compareTo(IrrisoftConstantes.PROG_DIARIA) == 0) {

	    // Recorro días de programacion y miro si la fecha es hoy
	    // Consulto y guardo los días de ejecución
	    try {
		conectal();
		diasd.clear();
		ps = conn
			.prepareStatement("select * from dias where IDPROGRAMA="
				+ id);
		rs = ps.executeQuery();
		while (rs.next()) {
		    diasd.add(rs.getDate(5));
		}
		rs.close();

	    } catch (SQLException e1) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia SQL" + e1.getMessage());
		}
	    } finally {
		try {
		    ps.close();
		} catch (SQLException e) {
		    if (logger.isErrorEnabled()) {
			logger.error("Error en la sentencia." + e.getMessage());
		    }
		    desconectal();
		}
	    }

	    for (int e = 0; e < diasd.size(); e++) {
		// Si la tarea es de hoy
		if (diasd.get(e).compareTo(hoy) == 0) {
		    eshoy = true;

		}

	    }

	} else if (tipo.compareTo(IrrisoftConstantes.PROG_SEMANAL) == 0) {

	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(hoy);
	    int dia = calendar.get(Calendar.DAY_OF_WEEK);

	    if (logger.isInfoEnabled()) {
		logger.info("Es semanal,día: " + dia);
	    }
	    // Bucle para saber el día de la semana:
	    for (int i = 0; i < dias.size(); i++) {
		if (dias.get(i) == 1) {
		    if (dia == 1) {
			if (dias.get(6) == 1)
			    eshoy = true;
		    } else if (dia == i + 2) {
			eshoy = true;
		    }
		}
	    }
	}

	if (logger.isInfoEnabled()) {
	    logger.info("Es hoy: " + eshoy);
	}
	return eshoy;
    }

    /**
     * Miro el tipo de programa que es, para luego poder crear los programas.
     * 
     * @param prog
     * @param hoy
     */
    private void tipoprog(Programacion prog, Date hoy) {

	if (prog.getTipo().compareTo(IrrisoftConstantes.PROG_DIARIA) == 0) {

	    // Recorro días de programacion y miro si la fecha es hoy
	    for (int e = 0; e < prog.getDays().size(); e++) {
		// Si la tarea es de hoy
		if (((prog.getDays().get(e)).equals(hoy))) {
		    creatareasprog(prog);
		}
	    }

	} else if (prog.getTipo().compareTo(IrrisoftConstantes.PROG_SEMANAL) == 0) {

	    // Compruebo si el día de hoy está marcado para hacer
	    logger.warn(prog.getFcinicio());
	    logger.warn(hoy);
	    logger.warn(prog.getFcinicio().equals(hoy));

	    if ((prog.getFcinicio().before(hoy) && prog.getFcFin().after(hoy))
		    || (prog.getFcinicio().equals(hoy))
		    || (prog.getFcFin().equals(hoy))) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(hoy);
		int dia = calendar.get(Calendar.DAY_OF_WEEK);

		// Switch para saber el día:
		if (prog.getDiad() == 1) {
		    // Domingo
		    if (dia == 1) {
			creatareasprog(prog);
		    }
		}
		if (prog.getDial() == 1) {
		    // Lunes
		    if (dia == 2) {
			creatareasprog(prog);
		    }

		}
		if (prog.getDiam() == 1) {
		    // Martes
		    if (dia == 3) {
			creatareasprog(prog);
		    }
		}
		if (prog.getDiax() == 1) {
		    // Miercoles
		    if (dia == 4) {
			creatareasprog(prog);
		    }
		}
		if (prog.getDiaj() == 1) {
		    // Jueves
		    if (dia == 5) {
			creatareasprog(prog);
		    }
		}
		if (prog.getDiav() == 1) {
		    // Viernes
		    if (dia == 6) {
			creatareasprog(prog);
		    }
		}
		if (prog.getDias() == 1) {
		    // Sabado
		    if (dia == 7) {
			creatareasprog(prog);
		    }
		}

	    }

	}
    }

    /**
     * Miro los datos a valvulas, dias y horas correspondientes a cada
     * proramacion.
     * 
     * @param prog
     */
    private void datosprog(Programacion prog) {

	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    conectal();
	    // Consulto y guardo las valvulas asociadas a la programacion
	    ps = conn
		    .prepareStatement("select * from valvulas where IDPROGRAMA="
			    + prog.getIdprograma());
	    rs = ps.executeQuery();

	    while (rs.next()) {
		Valvula valv = new Valvula();
		valv.setCodelecvalv(rs.getString(5));
		valv.setProgasoc(prog.getIdprograma());
		valv.setDuracion(rs.getInt(6));
		valv.setBloque(rs.getInt(7));
		prog.getValvuprog().add(valv);
	    }

	    // Consulto y guardo los días de ejecución
	    ps = conn.prepareStatement("select * from dias where IDPROGRAMA="
		    + prog.getIdprograma());
	    rs = ps.executeQuery();

	    while (rs.next()) {
		prog.getDays().add(rs.getDate(5));
	    }

	    // Consulto y guardo las horas de ejecución
	    ps = conn.prepareStatement("select * from horas where IDPROGRAMA="
		    + prog.getIdprograma());
	    rs = ps.executeQuery();

	    while (rs.next()) {
		prog.getHorasini().add(rs.getTime(5));
		prog.getHorasfin().add(rs.getTime(6));
	    }
	    rs.close();
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error de Base de Datos." + e.getMessage());
	    }

	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		desconectal();
	    }
	}

    }

    /**
     * Creo las tareasprog para ese dia. Las tareas que se van a ejecutar en el
     * dia.
     * 
     * @param prog
     */
    protected synchronized void creatareasprog(Programacion prog) {

	PreparedStatement ps = null;

	ResultSet rs = null, rs1 = null;

	boolean pasato = false;

	Calendar cal = Calendar.getInstance();
	cal.setTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
	TimeZone tz = TimeZone.getTimeZone("Europe/Madrid");
	cal.setTimeZone(tz);
	DateTime dateact = new DateTime(cal.get(Calendar.YEAR),
		cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH),
		cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));

	DateTime dateini;
	DateTime datefin;
	Calendar calen = Calendar.getInstance();

	for (int k = 0; k < prog.getValvuprog().size(); k++) {

	    for (int j = 0; j < prog.getHorasini().size(); j++) {

		if (j == 0)
		    pasato = false;
		if (j > 0)
		    pasato = true;

		tar = new TareaProg();
		tar.setIdprog(prog.getIdprograma());
		tar.setCodelecvalv(prog.getValvuprog().get(k).getCodelecvalv());
		tar.setFechaini(prog.getFcinicio());
		tar.setHoraini(prog.getHorasini().get(j));
		tar.setHorafin(new Time(prog.getHorasfin().get(j).getTime()));
		tar.setDuracion(prog.getValvuprog().get(k).getDuracion());
		tar.setDuracionini(prog.getValvuprog().get(k).getDuracion());
		tar.setHecha(0);
		tar.setBloque(prog.getValvuprog().get(k).getBloque());
		tar.setTipobloque(prog.getModoini());
		tar.setCuota(prog.getCuota());

		// if (prog.getTareasprog().size()==0 || pasato){
		// System.out.println("Es la primera !!!");
		// tar.setHoraini_auto(prog.getHorasini().get(j));
		// }
		// else{
		// System.out.println("SUMA : "+new
		// Time(prog.getTareasprog().get(prog.getTareasprog().size()-1).getHoraini().getTime()+
		// (prog.getTareasprog().get(prog.getTareasprog().size()-1).getDuracion()*1000)));
		// System.out.println("horaini anterior :"+prog.getTareasprog().get(prog.getTareasprog().size()-1).getHoraini());
		// System.out.println("duracion anterior :"+prog.getTareasprog().get(prog.getTareasprog().size()-1).getDuracion());
		//
		// tar.setHoraini(new
		// Time(prog.getTareasprog().get(prog.getTareasprog().size()-1).getHoraini().getTime()+
		// (prog.getTareasprog().get(prog.getTareasprog().size()-1).getDuracion()*1000)));
		// }

		// System.out.println("Horaini: "+tar.getHoraini());

		// Mapeo de fechas y horas DATES!!
		calen.setTimeZone(tz);
		calen.setTime(tar.getHorafin());
		datefin = new DateTime(cal.get(Calendar.YEAR),
			cal.get(Calendar.MONTH) + 1,
			cal.get(Calendar.DAY_OF_MONTH),
			calen.get(Calendar.HOUR_OF_DAY),
			calen.get(Calendar.MINUTE), calen.get(Calendar.SECOND));

		calen.setTime(tar.getHoraini());
		dateini = new DateTime(cal.get(Calendar.YEAR),
			cal.get(Calendar.MONTH) + 1,
			cal.get(Calendar.DAY_OF_MONTH),
			calen.get(Calendar.HOUR_OF_DAY),
			calen.get(Calendar.MINUTE), calen.get(Calendar.SECOND));

		// Le sumo un día a fechafin para que me cambie el día
		if (datefin.getHourOfDay() < dateini.getHourOfDay()) {
		    datefin = datefin.plusDays(1);
		    logger.info("Sumo un día a fechafin , porque termina al día siguiente");
		}

		tar.setFechaini(dateini.toDate());
		tar.setFechafin(datefin.toDate());

		logger.info("DATEFIN: " + datefin.toDate());
		logger.info("DATEINI: " + dateini.toDate());
		logger.info("DATEACT:" + dateact.toDate());

		int codVal = Integer.parseInt(tar.getCodelecvalv());

		// MCI
		if (IrrisoftConstantes.MCI_200 > codVal) {
		    tar.setTipoplaca(1);
		    tar.setPuertoserie(Irrisoft.config.getMci());
		} else if (IrrisoftConstantes.MCI_200 < codVal
			&& IrrisoftConstantes.MCI_300 > codVal) {
		    tar.setTipoplaca(2);
		    tar.setPuertoserie(Irrisoft.config.getMci2());
		} else if (IrrisoftConstantes.MCI_300 < codVal
			&& IrrisoftConstantes.MCI_400 > codVal) {
		    tar.setTipoplaca(3);
		    tar.setPuertoserie(Irrisoft.config.getMci3());
		} else if (IrrisoftConstantes.MCI_400 < codVal
			&& IrrisoftConstantes.MCI_500 > codVal) {
		    tar.setTipoplaca(4);
		    tar.setPuertoserie(Irrisoft.config.getMci4());
		}

		// BT2
		else if (IrrisoftConstantes.BT2_1000 < codVal
			&& IrrisoftConstantes.BT2_2000 > codVal) {
		    tar.setTipoplaca(5);
		    tar.setPuertoserie(Irrisoft.config.getBt2());
		} else if (IrrisoftConstantes.BT2_2000 < codVal
			&& IrrisoftConstantes.BT2_3000 > codVal) {
		    tar.setTipoplaca(6);
		    tar.setPuertoserie(Irrisoft.config.getBt22());
		} else if (IrrisoftConstantes.BT2_3000 < codVal
			&& IrrisoftConstantes.BT2_4000 > codVal) {
		    tar.setTipoplaca(7);
		    tar.setPuertoserie(Irrisoft.config.getBt23());
		} else if (IrrisoftConstantes.BT2_4000 < codVal
			&& IrrisoftConstantes.SAMCLA > codVal) {
		    tar.setTipoplaca(8);
		    tar.setPuertoserie(Irrisoft.config.getBt24());
		}

		// Samcla
		else if (IrrisoftConstantes.SAMCLA < codVal) {
		    tar.setTipoplaca(-1);
		    // tar.setPuertoserie(Irrisoft.config.getBt24());
		}

		// La añado a la lista de tareasa de la programación
		prog.getTareasprog().add(tar);

	    }

	}

	// Ordeno la lista de tareas por horasini
	Collections.sort(prog.getTareasprog());

	try {
	    conectal();
	    // Tareaprogdia tiene la programación ???
	    ps = conn
		    .prepareStatement("select * from tareasprogdia where idprog=? ");
	    ps.setInt(1, prog.getIdprograma());
	    rs = ps.executeQuery();

	    // Si no hay nada para esta programacion en tareaprogdia inserto

	    if (!rs.next()) {

		// Inserto en tareaprogdia
		for (int d = 0; d < prog.getTareasprog().size(); d++) {

		    try {

			ps = conn
				.prepareStatement("INSERT INTO tareasprogdia (idprog, horaini, duracion,"
					+ " hecha, codelecvalv) "
					+ "VALUES (?,?,?,?,?)");
			ps.setInt(1, prog.getTareasprog().get(d).getIdprog());
			ps.setTime(2, prog.getTareasprog().get(d).getHoraini());
			ps.setLong(3, prog.getTareasprog().get(d).getDuracion());
			ps.setInt(4, prog.getTareasprog().get(d).isHecha());
			ps.setString(5, prog.getTareasprog().get(d)
				.getCodelecvalv());
			ps.executeUpdate();

		    } catch (SQLException e2) {
			if (logger.isErrorEnabled()) {
			    logger.error("No se ha podido actualizar la tareaprog en la BBDD local.");
			    logger.error("Error de Base de Datos"
				    + e2.getMessage());
			}

		    }

		}
	    }

	    // Asocio id de tareaprogdia con la tarea en memoria
	    ps = conn
		    .prepareStatement("select * from tareasprogdia where idprog=? ");
	    ps.setInt(1, prog.getIdprograma());
	    rs = ps.executeQuery();

	    int ultimahecha = -1;

	    // Recorro la lista de tareas para ejecutarlas o no
	    for (int z = 0; z < prog.getTareasprog().size(); z++) {

		if (rs.next()) {
		    prog.getTareasprog().get(z).setIdtarea(rs.getInt(1));
		}

		cogetiemporegado(prog.getTareasprog().get(z), null);

		if (logger.isInfoEnabled()) {
		    logger.info("tareaprog: " + tar.getIdtarea() + ", "
			    + tar.getTiemporegado());
		}

		// Está pendiente de hacer??
		ps = conn
			.prepareStatement("select hecha from tareasprogdia where idtarea= ?");
		ps.setInt(1, prog.getTareasprog().get(z).getIdtarea());
		rs1 = ps.executeQuery();

		if (rs1.next())
		    prog.getTareasprog().get(z).setHecha(rs1.getInt(1));
		rs1.close();

		// Si no está hecha
		if (prog.getTareasprog().get(z).isHecha() == 0) {

		    // Compruebo si la tarea tiene un hilo asociado
		    if (prog.getTareasprog().get(z).getHilotar() == null) {

			if (logger.isInfoEnabled()) {
			    logger.info("Se va a ejecutar la tarea");
			}
			ejecutareaprog(prog.getTareasprog().get(z), z, prog);
		    }

		} else {
		    ultimahecha = z;
		}
	    }

	    ps.close();
	    rs.close();

	    // Añado el prog a la lista de programas
	    listaprogs.add(prog);

	    if (ultimahecha != -1) {
		System.out.println("Pasa por el aviso");
		// Aviso al siguiente hilo en el caso de que entre en medio de
		// alguna ejecución (se ha ido la luz)
		Thread.sleep(2000);
		synchronized (prog.getTareasprog().get(ultimahecha + 1)
			.getHilotar()) {
		    prog.getTareasprog().get(ultimahecha + 1).getHilotar()
			    .notify();
		}
	    }
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error de Base de Datos" + e.getMessage());
	    }
	} catch (InterruptedException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Hilo interrumpido: " + e.getMessage());
	    }
	}
    }

    /**
     * Gestión tareas de programaciones y de programaciones de hoy
     * 
     * @param tareaprog
     * @param z
     * @param prog
     */
    protected synchronized void ejecutareaprog(TareaProg tareaprog, int z,
	    Programacion prog) {

	// ejecuto la tarea en un nuevo hilo

	hiloprog = new HiloPrograma(tareaprog, z, prog);
	Thread th = new Thread(hiloprog);
	th.setName("Tareaprog " + th.getId());
	tareaprog.setHilotar(th);

	if (logger.isInfoEnabled()) {
	    logger.info("HILOTAR:" + tareaprog.getCodelecvalv() + " "
		    + tareaprog.getHilotar());
	}
	tareaprog.hilotar.start();

    }

    /**
     * Borro las tareas de la programacion creada.
     * 
     * @param id
     */
    public void borratareaprog(int id) {
	PreparedStatement ps = null;
	try {
	    conectal();
	    ps = conn
		    .prepareStatement("DELETE from tareasprogdia WHERE idprog=? ");
	    ps.setInt(1, id);
	    ps.executeUpdate();

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido actualizar la tarea de la programacion a hecha.");
		logger.error("Error de Base de Datos" + e.getMessage());
	    }
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		desconectal();
	    }
	}
    }

    /**
     * Borro programaciones de memoria.
     * 
     * @param id
     */
    public void borraprogramacion(int id) {
	// PreparedStatement sentenciapre;
	// try {
	// if(logger.isTraceEnabled())
	// {
	// logger.trace("Hola");
	// }
	// conectal();
	// sentenciapre =
	// conn.prepareStatement("DELETE from programa WHERE idprograma="+id);
	// sentenciapre.executeUpdate();
	//
	// ////Borro datos valvulas, horas y fechas de la programacion tb
	// sentenciapre =
	// conn.prepareStatement("DELETE from dias WHERE idprograma="+id);
	// sentenciapre.executeUpdate();
	// sentenciapre =
	// conn.prepareStatement("DELETE from horas WHERE idprograma="+id);
	// sentenciapre.executeUpdate();
	// sentenciapre =
	// conn.prepareStatement("DELETE from valvulas WHERE idprograma="+id);
	// sentenciapre.executeUpdate();
	//
	// sentenciapre.close();

	// } catch (SQLException e) {
	// e.printStackTrace();
	// if(logger.isTraceEnabled())
	// {
	// logger.trace("No se ha podido borrar la programacion de la bbdd local.");
	// }
	// }

	// Borro programacion de listaprogs en memoria
	for (int i = 0; i < listaprogs.size(); i++) {
	    if (listaprogs.get(i).getIdprograma() == id) {
		listaprogs.remove(i);
		logger.warn("Me ha borrado de memoria");
	    }
	}
    }

    /**
     * Actualizo los ultimos deberes de un programa.
     * 
     * @param id
     * @param fechahoy
     */
    public synchronized void actualizadeberes(int id, String fechahoy) {
	PreparedStatement ps = null;
	try {
	    conectal();
	    logger.info("Estoy en ACTUALIZADEBERES y hoy es " + fechahoy);
	    ps = conn
		    .prepareStatement("UPDATE programa set ultdeberes= ? WHERE IDPROGRAMA= ?");
	    ps.setDate(1, java.sql.Date.valueOf(fechahoy));
	    ps.setInt(2, id);
	    ps.executeUpdate();

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido actualizar la programacion a ultdeberes.");
		logger.error("Error de Base de Datos:" + e.getMessage());
	    }
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		desconectal();
	    }
	}
    }

    /**
     * Actualizo las tareas del dia, para poder volver a crear tareas.
     * 
     * @param tareaprog
     * @param todasahechas
     */
    public void actualizatareaprogpasadas(TareaProg tareaprog,
	    boolean todasahechas) {

	PreparedStatement ps = null;
	try {
	    if (logger.isInfoEnabled()) {
		logger.info("ACTUALIZO: " + tareaprog.getHoraini());
	    }

	    conectal();
	    if (todasahechas) {
		ps = conn
			.prepareStatement("UPDATE tareasprogdia set hecha='1' WHERE idprog=? ");
		ps.setInt(1, tareaprog.getIdprog());
		ps.executeUpdate();
	    } else {
		ps = conn
			.prepareStatement("UPDATE tareasprogdia set hecha='1' WHERE idtarea=?");
		ps.setInt(1, tareaprog.getIdtarea());
		ps.executeUpdate();
		tareaprog.setHecha(1);
	    }

	} catch (SQLException e) {

	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido actualizar la tarea de la programacion a hecha.");
		logger.error("Error de Base de Datos:" + e.getMessage());
	    }
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		desconectal();
	    }
	}
    }

    /**
     * Cojo el tiempo de regado de la BBDD local, de cada valvula.
     * 
     * @param tareaprog
     */
    public void cogetiemporegado(TareaProg tareaprog, TareaManual tareamanu) {
	PreparedStatement ps = null;
	ResultSet rs = null;

	try {
	    conectal();
	    if (tareamanu == null) { // Si es una tarea de programación
		ps = conn
			.prepareStatement("select tiempoexec from tareasprogdia WHERE idtarea= ? ");
		ps.setInt(1, tareaprog.getIdtarea());
		ps.executeQuery();
		rs = ps.executeQuery();

		while (rs.next()) {
		    tareaprog.setTiemporegado(rs.getInt(1));
		}
		rs.close();
	    } else if (tareaprog == null) {

		ps = conn
			.prepareStatement("select TIEMPOEXEC from tarea WHERE IDTAREA= ? ");
		ps.setInt(1, tareamanu.getIdtarea());
		ps.executeQuery();
		rs = ps.executeQuery();

		while (rs.next()) {
		    tareamanu.setTiemporegado(rs.getInt(1));
		}
		rs.close();

	    }
	} catch (SQLException e) {

	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido actualizar la tarea de la programacion a hecha.");
		logger.error("Error de Base de Datos:" + e.getMessage());
	    }
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		desconectal();
	    }
	}
    }

    /**
     * Voy actualizando el tiempo de regado de cada tarea.
     * 
     * @param tareaprog
     * @param tiempoexec
     */
    public void ponetiemporegado(TareaProg tareaprog, TareaManual tareamanu,
	    int tiempoexec) {
	PreparedStatement ps = null;

	try {
	    conectal();
	    if (tareamanu == null) { // Si es una tarea de una programación

		ps = conn
			.prepareStatement("UPDATE tareasprogdia set tiempoexec= ? WHERE idtarea= ?");
		ps.setInt(1, tiempoexec);
		ps.setInt(2, tareaprog.getIdtarea());
		ps.executeUpdate();
	    } else if (tareaprog == null) { // si es una tarea manual
		ps = conn
			.prepareStatement("UPDATE tarea set TIEMPOEXEC= ? WHERE IDTAREA= ? ");
		ps.setInt(1, tiempoexec);
		ps.setInt(2, tareamanu.getIdtarea());
		ps.executeUpdate();
	    }
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido actualizar la tarea de la programacion a hecha.");
		logger.error("Error de Base de Datos:" + e.getMessage());
	    }
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		desconectal();
	    }
	}
    }

    /**
     * Actualiza el estado de las tareas en la BBDD Local.
     * 
     * @param codelec
     * @param estado
     */

    public void ponertareamanualeida(TareaManual tar, boolean inicio) {
	PreparedStatement ps = null;
	// conectal();
	try {
	    conectal();
	    if (!inicio) { // Si es en la ejecución normal

		ps = conn
			.prepareStatement("UPDATE tarea set LEIDA='S' WHERE idtarea=?");
		ps.setInt(1, tar.getIdtarea());
		ps.executeUpdate();

	    } else if (inicio) // si el programador se ha reiniciado o se ha ido
			       // la luz
	    {
		ps = conn.prepareStatement("UPDATE tarea set LEIDA='N';");
		ps.executeUpdate();
	    }

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error de Base de Datos:" + e.getMessage());
	    }
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		desconectal();
	    }
	}
    }

    /**
     * Actualizo el estado de la valvula en GIS.
     * 
     * @param codelec
     * @param estado
     */
    public void acualizaestvalv(String codelec, int estado) {
	logger.warn("Actualizo valvula: " + codelec + " y el estado es: "
		+ estado);

	CallableStatement cs = null;

	try {
	    IR.volcado.getCon().conectar();
	    cs = IR.volcado.getCon().getConnr()
		    .prepareCall("{ call P074_U_ESTADO(?,?,?) }");
	    cs.setString("V_CODPROG", Irrisoft.config.getIdrasp());
	    cs.setString("V_NUMERESTA", codelec);
	    cs.setInt("V_ESTADO", estado);
	    cs.execute();
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error de Base de Datos:" + e.getMessage());
	    }
	} finally {
	    try {
		cs.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		IR.volcado.getCon().cierrar();
	    }
	}
    }

    // METODOS RELACIONADOS CON LOS CONSUMOS MODELOS DE LAS VALVULAS
    // ////////////////////////////
    /**
     * Insertar los consumos de las valvulas en la BBDD Remota (GIS.).
     * 
     * @param valvula
     * @param caudal
     * @param intensidad
     */
    public void insertarconsumostest(String valvula, float caudal,
	    int intensidad) {

	CallableStatement cs = null;

	try {
	    cs = IR.volcado.getCon().getConnr()
		    .prepareCall("{ call P057_I_CONSUMO(?,?,?,?,?) }");
	    cs.setString("V_CODPROG", Irrisoft.config.getIdrasp());
	    cs.setString("V_CODESTACION", valvula);
	    cs.setFloat("V_CAUDAL", caudal);
	    cs.setFloat("V_CONSUMELEC", intensidad);
	    cs.setTimestamp("V_FECHA", new Timestamp(Calendar.getInstance()
		    .getTime().getTime()));
	    cs.execute();
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error de Base de Datos:" + e.getMessage());
	    }
	} finally {
	    try {
		cs.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		IR.volcado.getCon().cierrar();
	    }
	}

    }

    /**
     * Sobre escribe los consumos de las valvulas en la BBDD Remota (GIS).
     * 
     * @param valvula
     * @param caudal
     * @param intensidad
     */
    public void sobrescriberconsumostest(String valvula, float caudal,
	    int intensidad) {

	CallableStatement cs = null;

	try {
	    cs = IR.volcado.getCon().getConnr()
		    .prepareCall("{ call P057_U_CONSUMO(?,?,?,?) }");
	    cs.setString("V_CODPROG", Irrisoft.config.getIdrasp());
	    cs.setString("V_CODESTACION", valvula);
	    cs.setFloat("V_CAUDAL", caudal);
	    cs.setFloat("V_CONSUMELEC", intensidad);
	    cs.execute();
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error de Base de Datos:" + e.getMessage());
	    }
	} finally {
	    try {
		cs.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		IR.volcado.getCon().cierrar();
	    }
	}

    }

    /**
     * Borra los consumos de la tabla en la BBDD Remota (GIS).
     */
    public void borrarconsumtest() {

	CallableStatement cs = null;

	try {
	    cs = IR.volcado.getCon().getConnr()
		    .prepareCall("{ call P057_D_CONSUMO(?) }");
	    cs.setString("V_CODPROG", Irrisoft.config.getIdrasp());
	    cs.execute();
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error de Base de Datos:" + e.getMessage());
	    }
	} finally {
	    try {
		cs.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		IR.volcado.getCon().cierrar();
	    }
	}

    }

    /**
     * Borra los consumos de la tabla.
     */
    public void borralconsumtest() {
	PreparedStatement ps = null;
	// conectal();
	try {
	    conectal();
	    ps = conn.prepareStatement("DELETE from modelconsum");
	    ps.executeUpdate();

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido borrar los consumos: "
			+ e.getMessage());
	    }
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		desconectal();
	    }
	}
    }

    /**
     * Inserta el consumo de la valvula en la BBDD local.
     * 
     * @param valvula
     * @param caudal
     * @param intensidad
     */
    public void insertalconsumostest(String valvula, float caudal,
	    int intensidad) {

	PreparedStatement ps = null;

	try {
	    conectal();
	    ps = conn
		    .prepareStatement("INSERT INTO modelconsum (CODPROG, CODELECVALV, CAUDAL, INTENSIDAD,FECHA) VALUES (?,?,?,?,?)"
			    + " ON DUPLICATE KEY UPDATE CAUDAL=VALUES(CAUDAL), INTENSIDAD=VALUES(INTENSIDAD), FECHA=VALUES(FECHA)");

	    ps.setString(1, Irrisoft.config.getIdrasp());
	    ps.setString(2, valvula);
	    ps.setFloat(3, caudal);
	    ps.setInt(4, intensidad);
	    ps.setTimestamp(5, new Timestamp(Calendar.getInstance().getTime()
		    .getTime()));
	    ps.executeUpdate();

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido insertar los consumos: "
			+ e.getMessage());
	    }
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		desconectal();
	    }
	}

    }

    /**
     * Sobre escribe el consumo de la valvula en la BBDD local.
     * 
     * @param valvula
     * @param caudal
     * @param intensidad
     */
    public void sobrescribelconsumostest(String valvula, float caudal,
	    int intensidad) {

	PreparedStatement ps = null;
	try {

	    conectal();
	    ps = conn
		    .prepareStatement("UPDATE modelconsum set CAUDAL=?, INTENSIDAD=?, FECHA=? WHERE CODELECVALV=?");
	    ps.setFloat(1, caudal);
	    ps.setInt(2, intensidad);
	    ps.setTimestamp(3, new Timestamp(Calendar.getInstance().getTime()
		    .getTime()));
	    ps.setString(4, valvula);
	    ps.executeUpdate();

	} catch (SQLException e) {

	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido sobre escribir los consumos: "
			+ e.getMessage());
	    }
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		desconectal();
	    }
	}

    }

    /**
     * Coge el caudal y la intensidad de la BBDD local. y colocamos los valores
     * en las variables
     * 
     * @param pos
     * @param valv
     */
    public void recogeconsummod(int pos, String valv) {

	PreparedStatement ps = null;
	ResultSet rs = null;

	try {

	    conectal();

	    ps = conn
		    .prepareStatement("SELECT CAUDAL , INTENSIDAD from modelconsum WHERE CODELECVALV= ? AND CODPROG=? ");
	    ps.setString(1, valv);
	    ps.setString(2, Irrisoft.config.getIdrasp());
	    rs = ps.executeQuery();
	    int va = Integer.parseInt(valv);
	    while (rs.next()) {
		// Si es MCI
		if (IrrisoftConstantes.MCI_200 > va) {
		    IR.valvsmci.getvalvmci(valv).setCaudalmod(rs.getFloat(1));
		    IR.valvsmci.getvalvmci(valv).setIntensidadmod(rs.getInt(2));
		}
		// Si es MCI2
		else if ((IrrisoftConstantes.MCI_200 < va && IrrisoftConstantes.MCI_300 > va)) {
		    IR.valvsmci2.getvalvmci(valv).setCaudalmod(rs.getFloat(1));
		    IR.valvsmci2.getvalvmci(valv)
			    .setIntensidadmod(rs.getInt(2));
		}
		// Si es MCI3
		else if ((IrrisoftConstantes.MCI_300 < va && IrrisoftConstantes.MCI_400 > va)) {
		    IR.valvsmci3.getvalvmci(valv).setCaudalmod(rs.getFloat(1));
		    IR.valvsmci3.getvalvmci(valv)
			    .setIntensidadmod(rs.getInt(2));
		}
		// Si es MCI4
		else if ((IrrisoftConstantes.MCI_400 < va && IrrisoftConstantes.MCI_500 > va)) {
		    IR.valvsmci4.getvalvmci(valv).setCaudalmod(rs.getFloat(1));
		    IR.valvsmci4.getvalvmci(valv)
			    .setIntensidadmod(rs.getInt(2));
		}
		// Si es BT2
		else if ((IrrisoftConstantes.BT2_1000 < va && IrrisoftConstantes.BT2_2000 > va)) {
		    IR.valvsbt2.getvalvbt2(valv).setCaudalmod(rs.getFloat(1));
		    IR.valvsbt2.getvalvbt2(valv).setIntensidadmod(rs.getInt(2));
		}
		// Si es BT22
		else if ((IrrisoftConstantes.BT2_2000 < va && IrrisoftConstantes.BT2_3000 > va)) {
		    IR.valvsbt22.getvalvbt2(valv).setCaudalmod(rs.getFloat(1));
		    IR.valvsbt22.getvalvbt2(valv)
			    .setIntensidadmod(rs.getInt(2));
		}
		// Si es BT23
		else if ((IrrisoftConstantes.BT2_3000 < va && IrrisoftConstantes.BT2_4000 > va)) {
		    IR.valvsbt23.getvalvbt2(valv).setCaudalmod(rs.getFloat(1));
		    IR.valvsbt23.getvalvbt2(valv)
			    .setIntensidadmod(rs.getInt(2));
		}
		// Si es BT24
		else if (IrrisoftConstantes.BT2_4000 < va) {
		    IR.valvsbt24.getvalvbt2(valv).setCaudalmod(rs.getFloat(1));
		    IR.valvsbt24.getvalvbt2(valv)
			    .setIntensidadmod(rs.getInt(2));
		}
	    }
	    rs.close();
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("No se han podido leer los consumos");
		logger.error("Error en la Base de datos:" + e.getMessage());
	    }
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		desconectal();
	    }
	}

    }

    // LECTURA DE SENSORES A GIS
    // //////////////////////////////////////////////////////////////
    /**
     * Inserto la temperatura en la BBDD Remota (GIS).
     * 
     * @param codsens
     * @param temp
     * @param uni
     */
    public boolean insertaregtemp(String codsens, Double temp, Timestamp fecha) {

	boolean insRegTemp = true;
	CallableStatement cs = null;

	try {
	    IR.volcado.getCon().conectar();
	    cs = IR.volcado.getCon().getConnr()
		    .prepareCall("{ call P058_I_REGTEMP(?,?,?,?) }");
	    cs.setString("V_CODPROG", Irrisoft.config.getIdrasp());
	    cs.setString("V_CODSENSOR", codsens);
	    cs.setDouble("V_TEMPERATURA", temp);
	    cs.setTimestamp("V_FECHA", fecha);
	    cs.execute();

	    logger.warn("Inserto registro de temperatura en GIS.");

	} catch (SQLException | NullPointerException e) {
	    if (logger.isErrorEnabled()) {
		if (e instanceof SQLException)
		    logger.error("Error de Base de Datos:" + e.getMessage());
		else if (e instanceof NullPointerException)
		    logger.error("Error de NullPointerException de BBDD Remota: "
			    + e.getMessage());
	    }
	    insRegTemp = false;
	} finally {
	    try {
		cs.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error a la hora de cerrar la conexion con la BBDD Remota:"
			    + e.getMessage());
		}
		IR.volcado.getCon().cierrar();
	    }
	}
	return insRegTemp;

    }

    /**
     * Inserto el viento en la BBDD Remota (GIS).
     * 
     * @param codsens
     * @param vel
     */
    public boolean insertaregviento(String codsens, Double vel, Timestamp fecha) {

	boolean insRegVien = true;
	CallableStatement cs = null;

	try {
	    IR.volcado.getCon().conectar();
	    cs = IR.volcado.getCon().getConnr()
		    .prepareCall("{ call P059_I_REGVIENTO(?,?,?,?) }");
	    cs.setString("V_CODPROG", Irrisoft.config.getIdrasp());
	    cs.setString("V_CODSENSOR", codsens);
	    cs.setDouble("V_CATA", vel);
	    cs.setTimestamp("V_FECHA", fecha);
	    cs.execute();

	    logger.warn("Inserto registro de viento en GIS.");

	} catch (SQLException | NullPointerException e) {
	    if (logger.isErrorEnabled()) {
		if (e instanceof SQLException)
		    logger.error("Error de sentencia SQL:" + e.getMessage());
		else if (e instanceof NullPointerException)
		    logger.error("Error de NullPointerException en BBDD Remota: "
			    + e.getMessage());
	    }
	    insRegVien = false;
	} finally {
	    try {
		cs.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error a la hora de cerrar la conexion con la BBDD Remota."
			    + e.getMessage());
		}
		IR.volcado.getCon().cierrar();
	    }
	}
	return insRegVien;

    }

    /**
     * Inserto la lluvia en la BBDD Remota (GIS).
     * 
     * @param codsens
     * @param lluvia
     */
    public boolean insertareglluvia(String codsens, int lluvia, Timestamp fecha) {

	boolean insRegLlu = true;
	CallableStatement cs = null;
	try {
	    IR.volcado.getCon().conectar();
	    cs = IR.volcado.getCon().getConnr()
		    .prepareCall("{ call P060_I_REGLLUVIA(?,?,?,?) }");
	    cs.setString("V_CODPROG", Irrisoft.config.getIdrasp());
	    cs.setString("V_CODSENSOR", codsens);
	    cs.setDouble("V_LLUVIA", lluvia);
	    cs.setTimestamp("V_FECHA", fecha);
	    cs.execute();
	    logger.warn("Inserto registro de lluvia en GIS.");

	} catch (SQLException | NullPointerException e) {
	    if (logger.isErrorEnabled()) {
		if (e instanceof SQLException)
		    logger.error("Error de sentencia SQL:" + e.getMessage());
		else if (e instanceof NullPointerException)
		    logger.error("Error de NullPointerException BBDD Remota: "
			    + e.getMessage());
	    }
	    insRegLlu = false;
	} finally {
	    try {
		cs.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error a la hora de cerrar la conexion con la BBDD Remota."
			    + e.getMessage());
		}
		IR.volcado.getCon().cierrar();
	    }
	}
	return insRegLlu;
    }

    /**
     * Metodo para enviar el registro de agua diario. FALTA EL PROCEDIMIENTO EN
     * GIS.
     * 
     * @param codsens
     * @param consumo
     * @param fecha
     */

    public boolean insertarRegConsumo(int consumo, Timestamp fecha) {

	boolean insRegCon = true;
	CallableStatement cs = null;

	try {
	    IR.volcado.getCon().conectar();
	    cs = IR.volcado.getCon().getConnr()
		    .prepareCall("{ call P112_I_REGCONSUMO(?,?,?) }");

	    cs.setString("V_CODPROG", Irrisoft.config.getIdrasp());
	    cs.setTimestamp("V_FECHA", fecha);
	    cs.setInt("V_VALOR", consumo);

	    cs.execute();
	    logger.warn("Registro de consumo diario de agua enviado a GIS");
	} catch (SQLException | NullPointerException e) {
	    if (logger.isErrorEnabled()) {
		if (e instanceof SQLException)
		    logger.error("Error de SQL: " + e.getMessage());
		else if (e instanceof NullPointerException)
		    logger.error("Error de NullPointerException BBDD Remota: "
			    + e.getMessage());

	    }
	    insRegCon = false;
	} finally {
	    try {
		cs.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error a la de cerrar la conexion de la BBDD Remota: "
			    + e.getMessage());
		}

		IR.volcado.getCon().cierrar();

	    }
	}
	return insRegCon;
    }

    /**
     * Inserto el valor del sensor de intrusion en GIS. FALTA EL PROCEDIMIENTO
     * ALMACENADO EN GIS.
     * 
     * @param codsens
     * @param intru
     * @param fecha
     */
    public boolean insertarRegIntrusion(String codsens, boolean intru,
	    Timestamp fecha) {

	boolean insRegInt = true;
	CallableStatement cs = null;

	try {
	    IR.volcado.getCon().conectar();

	    cs = IR.volcado.getCon().getConnr().prepareCall("");
	    cs.setString("", Irrisoft.config.getIdrasp());
	    cs.setString("V_CODSENSOR", codsens);
	    cs.setBoolean("", intru);
	    cs.setTimestamp("V_FECHA", fecha);
	    cs.execute();

	    logger.warn("Registro de intrusion enviado a GIS.");

	} catch (SQLException | NullPointerException e) {
	    if (logger.isErrorEnabled()) {
		if (e instanceof SQLException)
		    logger.error("Error de sentencia SQL: " + e.getMessage());
		else if (e instanceof NullPointerException)
		    logger.error("Error de NullPointerException de BBDD Remota: "
			    + e.getMessage());
	    }
	    insRegInt = false;
	} finally {
	    try {
		cs.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error a la hora de cerrar la conexion con la BBDD Remota: "
			    + e.getMessage());
		}
		IR.volcado.getCon().cierrar();
	    }
	}
	return insRegInt;
    }

    // METODOS PARA ALERTAS BBDD LOCAL Y GIS POR SI SE PIERDE CONEXION Y LUZ
    // //////////////////////////////////////////
    /**
     * Inserto la Alerta en la BBDD Remota GIS.
     * 
     * @param alerta
     * @return
     */
    public synchronized boolean insertarAlertaGIS(Alerta alerta) {
	boolean insAlerta = true;
	CallableStatement cs = null;
	try {

	    IR.volcado.getCon().conectar();
	    // Inserto la alerta en GIS
	    cs = IR.volcado.getCon().getConnr()
		    .prepareCall("{ call P046_I_ALERTA(?,?,?,?) }");

	    cs.setTimestamp("V_FCALERTA", alerta.getFechaAlerta());
	    cs.setInt("V_CODALERTA", alerta.getCodAlerta());
	    cs.setString("V_CODPROG", Irrisoft.config.getIdrasp());
	    cs.setInt("V_IDUSUARIOAUDIT", 0);
	    cs.execute();

	    logger.warn("Alerta " + alerta.getCodAlerta()
		    + " enviada a GIS, Fecha: " + alerta.getFechaAlerta());

	} catch (SQLException | NullPointerException e) {
	    if (logger.isErrorEnabled()) {
		if (e instanceof SQLException)
		    logger.error("Error en la sentencia SQL: " + e.getMessage());
		else if (e instanceof NullPointerException)
		    logger.error("Error de NullPointerException de BBDD Remota");
	    }
	    insAlerta = false;
	} finally {
	    try {
		cs.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error con el procedimiento almacenado P046_I_AERTA: "
			    + e.getMessage());
		}
		IR.volcado.getCon().cierrar();
	    }
	}
	return insAlerta;

    }

    /**
     * Inserto alertas en la BBDD Local.
     * 
     * @param alerta
     * @return
     */
    public synchronized boolean insertarAlertasBBDD(Alerta alerta) {

	PreparedStatement ps = null;

	try {
	    conectal();
	    ps = conn
		    .prepareStatement("INSERT INTO alerta (CODPROG,COD_ALERTA,FECHA) VALUES (?,?,?)");
	    ps.setString(1, Irrisoft.config.getIdrasp());
	    ps.setInt(2, alerta.getCodAlerta());
	    ps.setTimestamp(3, alerta.getFechaAlerta());
	    int i = ps.executeUpdate();

	    logger.warn("Alerta" + alerta.getCodAlerta()
		    + " insertada en la BBDD Local, Fecha: "
		    + alerta.getFechaAlerta());
	    return true;
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido insertar la alarma en la BBDD Local");
		logger.error("Error de Base de Datos: " + e.getMessage());
	    }
	    return false;
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error a la hora de cerrar la conexion con la BBDD Local:"
			    + e.getMessage());
		}
	    }
	    desconectal();
	}
    }

    /**
     * Borrar alertas de la BBDD Local.
     * 
     * @param alerta
     * @return
     */
    public synchronized boolean borrarAlertaBBDD(Alerta alerta) {

	PreparedStatement ps = null;

	try {
	    conectal();
	    ps = conn
		    .prepareStatement("DELETE from alerta where COD_ALERTA = ? and FECHA = ?");
	    ps.setInt(1, alerta.getCodAlerta());
	    ps.setTimestamp(2, alerta.getFechaAlerta());
	    ps.executeUpdate();
	    logger.warn("Alerta: " + alerta.getCodAlerta()
		    + " borrada de la BBDD Local con fecha: "
		    + alerta.getFechaAlerta());

	} catch (SQLException | NullPointerException e) {
	    if (logger.isErrorEnabled()) {
		if (e instanceof SQLException) {
		    logger.error("No se ha podido borrar la alarma de la BBDD Local");
		    logger.error("Error de BBDD Local " + e.getMessage());
		} else if (e instanceof NullPointerException)
		    logger.error("Error de NullPointerException en BBDD Local: "
			    + e.getMessage());
	    }
	    return false;
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error a la hora de cerrar la conexion con la BBDD Local: "
			    + e.getMessage());
		}
		desconectal();
	    }
	}

	return true;
    }

    /**
     * Envio alertas desde la BBDD Local a GIS y ademas borro esas alertas de la
     * BBDD Local.
     * 
     * @return
     */
    public synchronized boolean cargarBBDDalertas() {

	PreparedStatement ps = null;
	ResultSet rs = null;
	boolean val = true;

	try {
	    conectal();
	    ps = conn
		    .prepareStatement("SELECT ID_ALERTA, COD_ALERTA, FECHA FROM alerta");
	    rs = ps.executeQuery();

	    while (rs.next()) {
		Alerta alertaEnviar = new Alerta();
		int codigo = rs.getInt("COD_ALERTA");
		Timestamp fecha = rs.getTimestamp("FECHA");
		alertaEnviar.setCodAlerta(codigo);
		alertaEnviar.setFechaAlerta(fecha);
		// Inserto la alerta en GIS.
		val = insertarAlertaGIS(alertaEnviar);
		// Borro la alerta de la BBDD Local.
		if (val == true) {
		    borrarAlertaBBDD(alertaEnviar);
		}
	    }
	    rs.close();
	} catch (SQLException | NullPointerException e) {
	    if (logger.isErrorEnabled()) {
		if (e instanceof SQLException)
		    logger.error("Error de BBDD Local: " + e.getMessage());
		else if (e instanceof NullPointerException)
		    logger.error("Error de NullPointerException BBDD Local: "
			    + e.getMessage());
	    }
	    return false;
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia SQL: " + e.getMessage());
		}
		desconectal();
	    }

	}

	return true;

    }

    // METODOS PARA LECTURAS DE SENSORES BBDD LOCAL Y GIS POR SI SE PIERDE
    // CONEXION Y LUZ ////////////////////////////////////////////////////
    /**
     * Metodo para insertar lecturas de sensores en la BBDD Local.
     * 
     * @param lect
     * @return
     */
    public synchronized boolean insertarLecturasBBDD(LecturasSensor lect) {

	PreparedStatement ps = null;
	int riego = lect.getRiego();
	Timestamp fecha = lect.getFecha();
	try {
	    conectal();
	    ps = conn
		    .prepareStatement("INSERT INTO lecturas (CODPROG,NUM_SENSOR,FECHA,RIEGO,VIENTO,LLUVIA,TEMP,INTRU) VALUES (?,?,?,?,?,?,?,?) "
			    + "ON DUPLICATE KEY UPDATE RIEGO=VALUES(RIEGO), FECHA=VALUES(FECHA)");
	    ps.setString(1, Irrisoft.config.getIdrasp());
	    ps.setString(2, lect.getNombreSensor());
	    ps.setTimestamp(3, fecha);
	    ps.setInt(4, riego);
	    ps.setFloat(5, (float) lect.getVelocidadAnemometro());
	    ps.setInt(6, lect.getLluvia());
	    ps.setFloat(7, (float) lect.getTemperatura());
	    ps.setBoolean(8, lect.isIntru());
	    ps.executeUpdate();

	    logger.warn("Registro de sensor: " + lect.getNombreSensor()
		    + "  con Fecha: " + lect.getFecha()
		    + " introducido BBDD Local");
	    return true;

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error de Base de Datos: " + e.getMessage());
	    }
	    return false;
	} finally {
	    try {
		ps.close();

	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error(e.getMessage());
		}
		desconectal();
	    }
	}
    }

    /**
     * Metodo para borrar lecturas de sensores de la BBDD Local.
     * 
     * @param lect
     * @return
     */
    public synchronized boolean borrarRegistrosBBDD(LecturasSensor lect) {

	PreparedStatement ps = null;
	try {
	    conectal();
	    ps = conn
		    .prepareStatement("DELETE FROM lecturas WHERE CODPROG=? AND NUM_SENSOR=? AND FECHA=?");

	    ps.setString(1, Irrisoft.config.getIdrasp());
	    ps.setString(2, lect.getNombreSensor());
	    ps.setTimestamp(3, lect.getFecha());
	    ps.executeUpdate();
	    logger.warn("Borrado lectura de sensor: " + lect.getNombreSensor()
		    + " con Fecha: " + lect.getFecha()
		    + " eliminado de la BBDD Local");
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error de Base de Datos: " + e.getMessage());
	    }
	    return false;
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia SQL: " + e.getMessage());
		}
		desconectal();
	    }
	}

	return true;
    }

    /**
     * Metodo para enviar las lecturas de sensores que estan en la BBDD Local
     * por que se ha ido la luz.
     * 
     * @return
     */
    public synchronized boolean enviarRegSensoresBBDD() {
	PreparedStatement ps = null;
	ResultSet rs = null;
	boolean val = true;

	try {
	    conectal();
	    ps = conn.prepareStatement("SELECT * FROM lecturas");
	    rs = ps.executeQuery();

	    while (rs.next()) {
		LecturasSensor lectu = new LecturasSensor();
//		String codProg = rs.getString("CODPROG");
		String nombre = rs.getString("NUM_SENSOR");
		lectu.setNombreSensor(nombre);
		Timestamp fecha = rs.getTimestamp("FECHA");
		lectu.setFecha(fecha);
		int riego = rs.getInt("RIEGO");
		lectu.setRiego(riego);
		float viento = rs.getFloat("VIENTO");
		lectu.setVelocidadAnemometro(viento);
		int lluvia = rs.getInt("LLUVIA");
		lectu.setLluvia(lluvia);
		float temp = rs.getFloat("TEMP");
		lectu.setTemperatura(temp);
		boolean intru = rs.getBoolean("INTRU");
		lectu.setIntru(intru);
		
		if (riego != 0 && fecha.compareTo(IR.hoy)!=0) {
		    // Mando la lectura de riego diario a GIS.
		    val = insertarRegConsumo(riego, fecha);
		    // Una vez enviada la lectura a GIS la borro de la BBDD
		    // Local.
		    if (val == true) {
			borrarRegistrosBBDD(lectu);
		    }
		} else if (viento != 0) {
		    // Mando la lectura de viento a GIS.
		    val = insertaregviento(nombre, (double) viento, fecha);
		    // Una vez enviada la lectura a GIS la borro de la BBDD
		    // Local.
		    if (val == true) {
			borrarRegistrosBBDD(lectu);
		    }
		} else if (lluvia != 0) {
		    // Mando la lectura de lluvia a GIS
		    val = insertareglluvia(nombre, lluvia, fecha);
		    // Una vez enviada la lectura a GIS la borro de la BBDD
		    // Local.
		    if (val == true) {
			borrarRegistrosBBDD(lectu);
		    }
		} else if (temp != 0) {
		    // Mando la lectura de temp a GIS.

		    val = insertaregtemp(nombre, (double) temp, fecha);
		    // Una vez enviada la lectura a GIS la borro de la BBDD
		    // Local.
		    if (val == true) {
			borrarRegistrosBBDD(lectu);
		    }
		} else if (intru != false) {
		    // Mando la lectura de intrusion a GIS.
		    // val = insertarRegIntrusion(nombre, intru, fecha);
		    // // Una vez enviada la lectura a GIS la borro de la BBDD
		    // // Local.
		    // if(val == true){
		    // borrarRegistrosBBDD(lectu);
		    // }
		}
	    }
	} catch (SQLException | NullPointerException e) {
	    if (logger.isErrorEnabled()) {
		if (e instanceof SQLException)
		    logger.error("Error en sentencia SQL: " + e.getMessage());
		else if (e instanceof NullPointerException)
		    logger.error("Error de NullPointerExceptioin en BBDD Local: "
			    + e.getMessage());
	    }
	    return false;
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error a la hora de cerrar la conexion con la BBDD Local: "
			    + e.getMessage());
		}
		desconectal();
	    }

	}
	return true;
    }

    /**
     * Me devuelve los pulsosdiarios acumulados en la BBDD Local. Este metodo se
     * utiliza si se va la luz.
     * 
     * @param nombreSens
     * @return
     */
    public int devolverRiego(String nombreSens, Timestamp fecha) {
	int riego = 0;
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {
	    conectal();
	    ps = conn
		    .prepareStatement("SELECT RIEGO FROM lecturas where NUM_SENSOR = ? AND FECHA = ?");
	    ps.setString(1, nombreSens);
	    ps.setTimestamp(2, fecha);
	    rs = ps.executeQuery();
	    while (rs.next()) {
		riego = rs.getInt("RIEGO");
	    }
	    rs.close();
	    ps.close();
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error de Base de Datos: " + e.getMessage());
	    }
	    desconectal();
	}
	return riego;
    }

    /**
     * Me devuelve la fecha de la ultima fila de los pulsos.
     * 
     * @param nombreSens
     * @return
     */
    public Timestamp devolverFecha(String nombreSens) {
	Timestamp fecha = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {
	    conectal();
	    ps = conn
		    .prepareStatement("SELECT FECHA FROM lecturas where NUM_SENSOR = ?");
	    ps.setString(1, nombreSens);
	    rs = ps.executeQuery();
	    while (rs.next()) {
		fecha = rs.getTimestamp("FECHA");
	    }
	    rs.close();
	    ps.close();
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error de Base de Datos: " + e.getMessage());
	    }
	    desconectal();
	}
	return fecha;
    }

    /**
     * Metodo para saber si hay alguna fila de consumo diario en la tabla.
     * 
     * @param hoyLocal
     * @return
     */
    public boolean devolverFilaConsumo(Date hoyLocal) {
	boolean filaConsumo = true;

	Timestamp fechaBBDD = null;
	int riegoBBDD = 0;
	String nombre = null;
	// Variables para Fechas
	Date diaHoy = new Date();
	String fechaCambia = null;

	PreparedStatement ps = null;
	ResultSet rs = null;
	try {
	    conectal();
	    ps = conn.prepareStatement("SELECT FECHA,RIEGO,NUM_SENSOR FROM lecturas");
	    rs = ps.executeQuery();
	    while (rs.next()) {
		fechaBBDD = rs.getTimestamp("FECHA");
		fechaCambia = fechaBBDD.toString();
		fechaCambia = fechaCambia.substring(0, 10);
		try {
		    diaHoy = formatterfecha.parse(fechaCambia);
		} catch (ParseException e) {
		    if (logger.isErrorEnabled()) {
			logger.error("Error en el parseo de la fecha: "
				+ e.getMessage());
		    }
		}
		riegoBBDD = rs.getInt("RIEGO");
		nombre = rs.getString("NUM_SENSOR");

	    }

	    if (riegoBBDD != 0 && diaHoy.compareTo(hoyLocal)==0 && nombre.contains("Pu") ) {
		filaConsumo = true;
	    } else {

		filaConsumo = false;
	    }
	} catch (SQLException | NullPointerException e) {
	    if (logger.isErrorEnabled()) {
		if (e instanceof SQLException)
		    logger.error("Error de sentencia SQL: " + e.getMessage());
		else if (e instanceof NullPointerException)
		    logger.error("Error de NullPointerException en BBDD Local: "
			    + e.getMessage());
	    }
	    desconectal();
	}

	return filaConsumo;
    }
///////////////////////////////////////////////////////////////////////

    public boolean insertafechatarea_auto(TareaProg tar) {

	PreparedStatement ps = null;

	Timestamp fecha = new Timestamp(Calendar.getInstance().getTime()
		.getTime());
	// Meto la fecha de inicia a la tareaprog si es autóbnoma (por temas de
	// perdida de corriente)
	tar.setFechaini_auto(fecha);
	try {
	    conectal();
	    ps = conn
		    .prepareStatement("UPDATE tareasprogdia set fechaini_auto =? where idtarea=?");
	    ps.setTimestamp(1, fecha);
	    ps.setInt(2, tar.getIdtarea());
	    ps.executeUpdate();

	    logger.warn("fecha de tarea autonoma updateada OK");
	    return true;

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error de Base de Datos: " + e.getMessage());
	    }
	    return false;
	} finally {
	    try {
		ps.close();

	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error(e.getMessage());
		}
	    }
	}

    }

    // GETTER Y SETTER
    public HiloTarea getHilotarea() {
	return hilotarea;
    }

    public void setHilotarea(HiloTarea hilotarea) {
	this.hilotarea = hilotarea;
    }

    public HiloPrograma getHiloprog() {
	return hiloprog;
    }

    public void setHiloprog(HiloPrograma hiloprog) {
	this.hiloprog = hiloprog;
    }

}
