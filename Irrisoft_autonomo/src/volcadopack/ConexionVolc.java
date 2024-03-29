package volcadopack;

import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.util.text.BasicTextEncryptor;

import sensorespack.HiloAmperimetro;
import sensorespack.HiloCaudalimetro;
import sensorespack.Sensor;
import alertaspack.GestorAlertas;

public class ConexionVolc {

    private static Logger logger = LogManager.getLogger(ConexionVolc.class
	    .getName());

    // Parámetros de conexión y conexiones
    public Connection connr;
    Connection connl;
    public Conf config = null; // Lo he cambiado de protected a public.//CAMBIO
    String linea = null;

    private TareaVolc tarea;
    private Valvula valv;
    private Programacion prog;

    public boolean conectador = false;
    public boolean conectadol = false;
    private Irrisoft IR;
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(
	    this);
    private boolean sincronizar = true;

    public ConexionVolc() {
	GestorAlertas ga = GestorAlertas.getInstance();
	addPropertyChangeListener("true", ga);
	this.IR = Irrisoft.window;
	this.tarea = new TareaVolc();
	this.valv = new Valvula();
	this.prog = new Programacion();
    }

    //METODOS DE TAREAS Y PROGRAMAS BBDD REMOTA
    /**
     * Comprueba TAREAS pendientes y si las hay las copia a la local y las borra
     * de la pasarela
     */
    synchronized protected void tarea() {

	// IR.rearmar = false;
	CallableStatement cs = null, cs1 = null, cs2 = null;
	ResultSet rs = null, rs1 = null, rs2 = null;
	try {
	    // Consulto tareas pendientes de ejecutar en remoto
	    conectar();
	    cs = connr.prepareCall("{ call P002_S_TAREAEXEC(?,?) }");
	    cs.setString("V_CODPROG", Irrisoft.config.getIdrasp());
	    cs.setString("V_BORRADO", "N");
	    cs.setQueryTimeout(10);

	    rs = cs.executeQuery();
	    while (rs.next()) {
		int iddstarea = rs.getInt("IDDSTAREA");
		tarea.setIddstarea(iddstarea);
		int idtareaexec = rs.getInt("IDTAREAEXEC");
		tarea.setIdtareaexec(idtareaexec);

		if (logger.isInfoEnabled()) {
		    logger.info("Tarea " + idtareaexec + " " + iddstarea
			    + " para este programador");
		}

		// /MIRO QUÉ TAREA ES !!

		// Si la tarea es leer una programación (3) llamo a programa
		// para que la gestione.
		if (IrrisoftConstantes.LEER_PROGRAMACION == iddstarea) {

		    // TODO REVISAAAAR
		    programa();
		    IR.hiloescucha.connDB.leidop = false;
		    if (logger.isInfoEnabled()) {
			logger.info("Nuevo programa encontrado !");
		    }

		}
		// Si no, es una tarea manual(1,2), un cambio de cuota (6), un
		// apagado(4)
		else if (IrrisoftConstantes.ABRIR_ESTACION == iddstarea
			|| IrrisoftConstantes.CERRAR_ESTACION == iddstarea
			|| IrrisoftConstantes.RECALCULO_PROGRAMACION == iddstarea
			|| IrrisoftConstantes.APAGAR_PROGRAMADOR == iddstarea
			|| IrrisoftConstantes.CANCELAR_PROGRAMA == iddstarea) {

		    String codprog = rs.getString("CODPROG");
		    tarea.setCodprog(codprog);
		    String numeresta = rs.getString("NUMERESTA");
		    tarea.setCodelecvalv(numeresta);
		    // Set IDDSTAREA tendria que hacerlo
		    // int iddstarea = rs.getInt("IDDSTAREA");
		    tarea.setIddstarea(iddstarea);
		    Timestamp fcexec = rs.getTimestamp("FCEXEC");
		    tarea.setFcexec(fcexec);
		    int valor = rs.getInt("VALOR");
		    tarea.setValor(valor);
		    int valor2 = rs.getInt("VALOR2");
		    tarea.setValor2(valor2);
		    String codtarea = rs.getString("CODTAREA");
		    tarea.setCodtarea(codtarea);
		    String dstarea = rs.getString("DSTAREA");
		    tarea.setDstarea(dstarea);

		    // Compruebo que la tarea no existe en local !!
		    // Cojo la última tarea de la tabla local
		    // PreparedStatement sentenciaprel = connl
		    // .prepareStatement("SELECT MAX(idtarea),fcexec FROM tarea");
		    // ResultSet resultadosl = sentenciaprel.executeQuery();
		    //
		    // while (resultadosl.next()) {
		    // if (logger.isInfoEnabled()) {
		    // logger.info("dentro del while de max local");
		    // }
		    // tarea.setIdtarealocal(resultadosl.getInt(1));
		    // tarea.setFcloc(resultadosl.getTimestamp(2));

		    // Si la tarealocal es menor es que la tareaexec no está
		    // en la bbdd local, con lo que la inserto
		    // Comparo las fechas también por si la pasarela se ha
		    // reiniciado
		    // if (tarea.getIdtarealocal() < tarea.getIdtareaexec()
		    // || tarea.getFcloc().before(tarea.getFcexec())) {
		    if (Insertarea()) {
			// Aki tendría que borrar de la t02_
			if (Borratarea()) {

			    if (logger.isInfoEnabled()) {
				logger.info("Tarea borrada de la pasarela correctamente !");
			    }
			}
		    } else {
			if (logger.isErrorEnabled()) {
			    logger.error("Error al insertar la tarea de la bbdd remota a la local !!");
			}
		    }
		    // }

		    // }
		    // sentenciaprel.close();
		    // resultadosl.close();
		}
		// La tarea es recoger la CONFIGURACIÓN INICIAL
		else if (IrrisoftConstantes.CONFIG_PROGRAMADOR == iddstarea
			|| IrrisoftConstantes.CONFIG_SENSORES == iddstarea
			|| IrrisoftConstantes.CONFIG_ESTACION == iddstarea) {

		    IR.rearmar = true;

		    if (IrrisoftConstantes.CONFIG_PROGRAMADOR == iddstarea) {
			// setSincronizar(false);
			Borratarea();

		    } else if (IrrisoftConstantes.CONFIG_SENSORES == iddstarea) {

			// SENSORES

			cs1 = IR.volcado.con.connr
				.prepareCall("{ call P062_S_CONFINISENSORES(?)}");
			cs1.setString("V_CODPROG", Irrisoft.config.getIdrasp());

			rs1 = cs1.executeQuery();

			int i = 0;

			while (rs1.next()) {

			    Sensor sensor = new Sensor();

			    sensor.setCodprog(rs1.getString(2));
			    sensor.setNum_placa(rs1.getInt(3));
			    sensor.setTipo_placa(rs1.getString(4));
			    sensor.setNum_sensor(rs1.getString(5));
			    sensor.setNum_borna(rs1.getInt(6));
			    sensor.setUni_med(rs1.getString(7));
			    sensor.setUni_sal(rs1.getString(8));
			    sensor.setRang_med_min(rs1.getDouble(9));
			    sensor.setRang_med_max(rs1.getDouble(10));
			    sensor.setRang_sal_min(rs1.getDouble(11));
			    sensor.setRang_sal_max(rs1.getDouble(12));
			    sensor.setMed_umbral_min(rs1.getDouble(13));
			    sensor.setMed_umbral_max(rs1.getDouble(14));
			    sensor.setFrec_lect(rs1.getInt(15));
			    sensor.setFrec_env(rs1.getInt(16));
			    sensor.setK(rs1.getInt(17));
			    sensor.setError_sup(rs1.getInt(18));
			    sensor.setError_inf(rs1.getInt(19));
			    sensor.setT_max_riego(rs1.getInt(20));
			    sensor.setNum_est_prop(rs1.getString(21));
			    sensor.setNum_est_asoc(rs1.getString(22));

			    if (i == 0)
				// Borro lo que haya en la tabla
				borratabla("conf_ini_sens");

			    insertarConf_ini_sens(sensor.getCodprog(),
				    sensor.getNum_placa(),
				    sensor.getTipo_placa(),
				    sensor.getNum_sensor(),
				    sensor.getNum_borna(), sensor.getUni_med(),
				    sensor.getUni_sal(),
				    sensor.getRang_med_min(),
				    sensor.getRang_med_max(),
				    sensor.getRang_sal_min(),
				    sensor.getRang_sal_max(),
				    sensor.getMed_umbral_min(),
				    sensor.getMed_umbral_max(),
				    sensor.getFrec_lect(),
				    sensor.getFrec_env(), sensor.getK(),
				    sensor.getError_sup(),
				    sensor.getError_inf(),
				    sensor.getT_max_riego(),
				    sensor.getNum_est_prop(),
				    sensor.getNum_est_asoc());

			    i++;
			}
			rs1.close();
			Borratarea();

		    }
		    if (IrrisoftConstantes.CONFIG_ESTACION == tarea
			    .getIddstarea()) {

			cs2 = connr.prepareCall("{ call P068_S_CONFINIESTACION(?)}");
			cs2.setString("V_CODPROG", Irrisoft.config.getIdrasp());
			rs2 = cs2.executeQuery();

			int i = 0;
			while (rs2.next()) {
			    int idconfiniestacion = rs2
				    .getInt("IDCONFINIESTACION");
			    String codprog = rs2.getString("CODPROG");
			    String numeresta_ini = rs2.getString("NUMERESTA");
			    int deco = rs2.getInt("DECO");
			    int maestra = rs2.getInt("MAESTRA");
			    int lacth = rs2.getInt("LATCH");
			    int goteo = rs2.getInt("GOTEO");
			    // int estado = rs2.getInt("ESTADO");

			    if (i == 0)
				// Borro lo que haya en la tabla de
				// configuración inicial programador
				borratabla("conf_ini_prog");

			    insertarConf_ini_prog(idconfiniestacion, codprog,
				    numeresta_ini, deco, maestra, lacth, goteo);

			    i++;
			}
			rs2.close();
			Borratarea();

		    }

		}

	    }
	    rs.close();

	    // AKi rearmo Irrisoft con los datos propagados de la sincronización
	    // con GIS.
	    if (IR.rearmar) {
		logger.info("Voy a sincronizar el Programador");
		setSincronizar(false);
		try {
		    Thread.sleep(30000);
		} catch (InterruptedException e) {
		    if (logger.isErrorEnabled()) {
			logger.error("Hilo de rearme interrumpido: "
				+ e.getMessage());
		    }
		}
		IR.leerconfini();
	    }

	} catch (SQLException e) {

	    if (logger.isErrorEnabled()) {
		logger.error("Error 136");
		logger.error("Error de Base de Datos: " + e.getMessage());
	    }
	    conectador = false;
	    return;
	} finally {
	    try {
		cs.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		cierrar();
	    }
	}

    }

    /**
     * Borro una tarea de la BBDD remota.
     * 
     * @return
     */
    synchronized protected boolean Borratarea() {

	logger.info("Borro tarea en la t02 con ID " + tarea.getIdtareaexec());

	CallableStatement cs = null;

	try {
	    conectar();
	    cs = connr.prepareCall("{ call P025_D_DSTAREA(?,?) }");
	    cs.setInt("V_IDTAREAEXEC", tarea.getIdtareaexec());
	    cs.setInt("V_IDUSUARIOAUDIT", 0);
	    cs.executeUpdate();

	    return true;
	} catch (SQLException e) {

	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido borrar la tarea en la BBDD remota.");
		logger.error(e.getMessage());
	    }
	    return false;
	} finally {
	    try {
		cs.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		cierrar();
	    }
	}

    }

    /**
     * Inserto tareas en la BBDD local.
     * 
     * @return
     */
    synchronized protected boolean Insertarea() {

	logger.info("Inserto tarea " + tarea.getIdtareaexec() + " en la local");

	PreparedStatement ps = null;

	try {
	    conectal();
	    ps = connl
		    .prepareStatement("INSERT INTO tarea(idtarea, codprog, numeresta,iddstarea,fcexec,valor,valor2,codtarea,dstarea)"
			    + "VALUES (?,?,?,?,?,?,?,?,?)");
	    ps.setInt(1, tarea.getIdtareaexec());
	    ps.setString(2, tarea.getCodprog());
	    ps.setString(3, tarea.getCodelecvalv());
	    ps.setInt(4, tarea.getIddstarea());
	    ps.setTimestamp(5, tarea.getFcexec());
	    ps.setInt(6, tarea.getValor());
	    ps.setString(7, Integer.toString(tarea.getValor2()));
	    ps.setString(8, tarea.getCodtarea());
	    ps.setString(9, tarea.getDstarea());
	    ps.executeUpdate();
	    return true;

	} catch (SQLException e) {

	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido actualizar la tarea en la BBDD local.");
		logger.error(e.getMessage());
	    }
	    return false;
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		cierral();
	    }
	}

    }

    /**
     * Comprueba PROGRAMACIONES pendientes y si las hay las copia a la local y
     * borra la tarea y demases de la paserela
     */
    protected void programa() {

	CallableStatement cs = null, cs1 = null, cs2 = null, cs3 = null;
	ResultSet rs = null, rs1 = null, rs2 = null, rs3 = null;
	try {
	    conectar();
	    cs = connr.prepareCall("{ call P104_S_PROGRAMA(?) }");
	    cs.setString("V_CODPROG", Irrisoft.config.getIdrasp());

	    rs = cs.executeQuery();
	    while (rs.next()) {
		int idprograma = rs.getInt("IDPROGRAMA");
		prog.setIdprograma(idprograma);
		String codprograma = rs.getString("CODPROGRAMA");
		prog.setCodprograma(codprograma);
		String dsprograma = rs.getString("DSPROGRAMA");
		prog.setDsprograma(dsprograma);
		Date fcinicio = rs.getDate("FCINICIO");
		prog.setFcinicio(fcinicio);
		Date fcfin = rs.getDate("FCFIN");
		prog.setFcFin(fcfin);
		String activo = rs.getString("ACTIVO");
		prog.setActivo(activo);
		String codprog = rs.getString("CODPROG");
		prog.setCodprog(codprog);
		String tipo = rs.getString("TIPO");
		prog.setTipo(tipo);
		int dial = rs.getInt("DIAL");
		prog.setDial(dial);
		int diam = rs.getInt("DIAM");
		prog.setDiam(diam);
		int diax = rs.getInt("DIAX");
		prog.setDiax(diax);
		int diaj = rs.getInt("DIAJ");
		prog.setDiav(diaj);
		int diav = rs.getInt("DIAV");
		prog.setDiav(diav);
		int dias = rs.getInt("DIAS");
		prog.setDias(dias);
		int diad = rs.getInt("DIAD");
		prog.setDiad(diad);
		String modo = rs.getString("MODO");
		prog.setModo(modo);
		String modoini = rs.getString("MODOINI");
		prog.setModoini(modoini);
		int pbloque = rs.getInt("PBLOQUE");
		prog.setPbloque(pbloque);
		int cuota = rs.getInt("CUOTA");
		prog.setCuota(cuota);
		String leido = rs.getString("LEIDO");
		prog.setLeido(leido);
		String enmarcha = rs.getString("ENMARCHA");
		prog.setEnmarcha(enmarcha);

		if (logger.isInfoEnabled()) {
		    logger.info("\nDescripcion: " + prog.getDsprograma());
		    logger.info(prog.getCodprograma());
		    logger.info(prog.getFcinicio());
		}

		// Compruebo que la programacion no existe en local !!
		// Cojo la última progamacion de la tabla
		// PreparedStatement sentenciaprel = connl
		// .prepareStatement("SELECT MAX(idprograma)  FROM programa");
		// ResultSet resultadosl = sentenciaprel.executeQuery();
		//
		// while (resultadosl.next()) {
		// prog.setIdprogramal(resultadosl.getInt(1));

		// Cojo el dia

		cs1 = connr.prepareCall("{ call P105_S_PROGDIAS(?)}");
		cs1.setInt("V_IDPROGRAMA", idprograma);

		rs1 = cs1.executeQuery();
		while (rs1.next()) {
		    Date fecha = rs1.getDate("FECHA");
		    prog.setDia(fecha);

		    if (logger.isInfoEnabled()) {
			logger.info("Volcado guarda el dia " + fecha);
		    }

		    if (!insertadia()) {
			if (logger.isErrorEnabled()) {
			    logger.error("Error al insertar el dia en la local");
			}
		    } else {

		    }

		}

		rs1.close();

		// Cojo las horas y las guardo

		cs2 = connr.prepareCall("{ call P106_S_PROGHORAS(?)}");
		cs2.setInt("V_IDPROGRAMA", idprograma);

		rs2 = cs2.executeQuery();
		while (rs2.next()) {
		    Time hrinicio = rs2.getTime("HRINICIO");
		    Time hrfin = rs2.getTime("HRFIN");

		    prog.setHoraini(hrinicio.toString());
		    prog.setHorafin(hrfin.toString());

		    if (logger.isInfoEnabled()) {
			logger.info("Volcado guarda las horas " + hrinicio
				+ " " + hrfin);
		    }
		    if (!insertahoras()) {
			if (logger.isErrorEnabled()) {
			    logger.error("Error al insertar las horas en la local");
			}
		    } else {

		    }

		}
		rs2.close();

		// Cojo las valvulas y las guardo

		cs3 = connr.prepareCall("{ call P107_S_PROGVALV(?)}");
		cs3.setInt("V_IDPROGRAMA", idprograma);

		rs3 = cs3.executeQuery();
		while (rs3.next()) {
		    String numeresta = rs3.getString("NUMERESTA");
		    int duracion = rs3.getInt("DURACION");
		    int bloque = rs3.getInt("BLOQUE");
		    valv.setCodelecvalv(numeresta);
		    valv.setDuracion(duracion);
		    valv.setBloque(bloque);

		    if (logger.isInfoEnabled()) {
			logger.info("Volcado guarda las valvulas " + numeresta);
		    }
		    if (!insertavalv()) {
			if (logger.isErrorEnabled()) {
			    logger.error("Error al insertar las horas en la local");
			}
		    }

		    // }
		}
		rs3.close();

		// OJO cambiado
		// if(prog.getIdprogramal()<idprograma){

		if (insertaprog()) {

		    borraprog();
		    Borratarea();

		} else {

		    if (logger.isErrorEnabled()) {

			logger.error("Error al insertar la tarea de la bbdd remota a la local !!");

		    }

		}
		// }

		// }

	    }
	    rs.close();

	} catch (SQLException e) {

	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	    conectador = false;
	    return;
	} finally {
	    try {
		cs.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		cierrar();
	    }
	}

    }

    /**
     * Inserto programas en la BBDD local.
     * 
     * @return
     */
    protected boolean insertaprog() {
	if (logger.isInfoEnabled()) {
	    logger.info("Aki inserto programacion en la local");
	}

	PreparedStatement ps = null;
	try {
	    conectal();
	    ps = connl
		    .prepareStatement("INSERT INTO `programa`(`IDPROGRAMA`, `CODPROGRAMA`, `DSPROGRAMA`, `FCINICIO`, `FCFIN`, `ACTIVO`, `CODPROG`, "
			    + "`TIPO`, `DIAL`, `DIAM`, `DIAX`, `DIAJ`, `DIAV`, `DIAS`, `DIAD`, `MODO`, `MODOINI`, `PBLOQUE`, `CUOTA`, `LEIDO`, `ENMARCHA`)"
			    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	    ps.setInt(1, prog.getIdprograma());
	    ps.setString(2, prog.getCodprograma());
	    ps.setString(3, prog.getDsprograma());
	    ps.setDate(4, prog.getFcinicio());
	    ps.setDate(5, prog.getFcFin());
	    ps.setString(6, prog.getActivo());
	    ps.setString(7, prog.getCodprog());
	    ps.setString(8, prog.getTipo());
	    ps.setInt(9, prog.getDial());
	    ps.setInt(10, prog.getDiam());
	    ps.setInt(11, prog.getDiax());
	    ps.setInt(12, prog.getDiaj());
	    ps.setInt(13, prog.getDiav());
	    ps.setInt(14, prog.getDias());
	    ps.setInt(15, prog.getDiad());
	    ps.setString(16, prog.getModo());
	    ps.setString(17, prog.getModoini());
	    ps.setInt(18, prog.getPbloque());
	    ps.setInt(19, prog.getCuota());
	    ps.setString(20, prog.getLeido());
	    ps.setString(21, prog.getEnmarcha());
	    ps.executeUpdate();

	    return true;
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido actualizar la programacion en la BBDD local.");
		logger.error(e.getMessage());
	    }
	    return false;
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		cierral();
	    }
	}

    }

    /**
     * Borro programa de la t10_programas de la BBDD Remota (GIS).
     * 
     * @return
     */
    protected boolean borraprog() {

	CallableStatement cs = null;

	try {
	    conectar();
	    cs = connr.prepareCall("{ call P108_U_PROGRAMA(?,?,?)}");
	    cs.setInt("V_IDPROGRAMA", prog.getIdprograma());
	    cs.setString("V_LEIDO", "S");
	    cs.setString("V_ENMARCHA", "N");
	    cs.executeUpdate();
	    if (logger.isInfoEnabled()) {
		logger.info("Programa ya leido y borrado de la pasarela");
	    }
	    return true;
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido borrar el programa en la BBDD remota.");
		logger.error(e.getMessage());
	    }
	    return false;
	} finally {
	    try {
		cs.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		cierrar();
	    }
	}
    }

    /**
     * Inserto dias en la BBDD local.
     * 
     * @return
     */
    protected boolean insertadia() {
	if (logger.isInfoEnabled()) {
	    logger.info("Aki inserto dias en la local");
	}

	PreparedStatement ps = null;

	try {
	    conectal();
	    ps = connl.prepareStatement("INSERT INTO dias (codprog,idprograma,fecha) VALUES(?,?,?)");
	    ps.setString(1, prog.getCodprog());
	    ps.setInt(2, prog.getIdprograma());
	    ps.setDate(3, prog.getDia());
	    ps.executeUpdate();
	    return true;
	} catch (SQLException e) {

	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido actualizar el dia en la BBDD local.");
		logger.error(e.getMessage());
	    }
	    return false;
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		cierral();
	    }
	}

    }

    /**
     * Inserto horas en la BBDD local.
     * 
     * @return
     */
    protected boolean insertahoras() {
	if (logger.isInfoEnabled()) {
	    logger.info("Aki inserto horas en la local");
	}

	PreparedStatement ps = null;
	try {
	    conectal();
	    ps = connl.prepareStatement("INSERT INTO horas (codprog,idprograma, hrinicio,hrfin) VALUES (?,?,?,?)");
	    ps.setString(1, prog.getCodprog());
	    ps.setInt(2, prog.getIdprograma());
	    ps.setString(3, prog.getHoraini());
	    ps.setString(4, prog.getHorafin());
	    ps.executeUpdate();
	    return true;
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido actualizar las horas en la BBDD local.");
		logger.error(e.getMessage());
	    }
	    return false;
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		cierral();
	    }
	}

    }

    /**
     * Inserto valvulas en la BBDD local.
     * 
     * @return
     */
    protected boolean insertavalv() {
	if (logger.isInfoEnabled()) {
	    logger.info("Aki inserto valvulas en la local");
	}

	PreparedStatement ps = null;
	try {
	    conectal();
	    ps = connl.prepareStatement("INSERT INTO valvulas (codprog,idprograma,codelecvalv,duracion,bloque) VALUES (?,?,?,?,?)");
	    ps.setString(1, prog.getCodprog());
	    ps.setInt(2, prog.getIdprograma());
	    ps.setString(3, valv.getCodelecvalv());
	    ps.setInt(4, valv.getDuracion());
	    ps.setInt(5, valv.getBloque());
	    ps.executeUpdate();

	    return true;
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido actualizar valvulas en la BBDD local.");
		logger.error(e.getMessage());
	    }
	    return false;
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		cierral();
	    }
	}

    }

    //METODOS RELACIONADOS CON CONEXIONES A BBDD, ABRIR Y CERRAR
    /**
     * Conexion con la BBDD Remota.
     * 
     * @return
     */
    public boolean conectar() {

	if (config == null) {
	    config = new Conf();
	    leerconf(config); // Cargo la configuracion del archivo
			      // Volcado.properties
	}

	if (conectador == false) {
	    // if(isConectador() ==false){

	    try {
		Class.forName("com.mysql.jdbc.Driver");

		String urlremota = "jdbc:mysql://" + config.getHostr() + ":"
			+ config.getPuertor() + "/" + config.getDbr()
			+ "?autoReconnect=true";
		connr = DriverManager.getConnection(urlremota,
			config.getUserr(), config.getPassr());

		IR.lblstatusr.setForeground(new Color(0, 128, 0));
		IR.lblstatusr.setText("Conectado");
		//Aviso a los Listener de que hay conexion a la BBDD Remota(GIS).
		setConectador(true);
		calculaip();

	    } catch (ClassNotFoundException | SQLException e) {
		if (logger.isErrorEnabled()) {
		    if(e instanceof ClassNotFoundException)
		    logger.error("Error de Conectividad: " +e.getMessage());
		    else if(e instanceof SQLException)
			logger.error("Error de sentencia SQL: " +e.getMessage());
		}

		if (conectador)
		    cierrar();

		try {
		    Thread.sleep(IrrisoftConstantes.DELAY_TAREA_5SEG);
		} catch (InterruptedException e1) {

		    if (logger.isErrorEnabled()) {
			logger.error("Hilo de conectividad interrrumpido: " +e1.getMessage());
		    }
		}

		conectador = false;
		IR.ip = null;
		IR.lblstatusr.setForeground(Color.RED);
		IR.lblstatusr.setText("Desconectado");

	    }

	}
	return conectador;

    }

    /**
     * Conexion con la BBDD Local.
     * 
     * @return
     */
    public boolean conectal() {

	if (conectadol == false) {
	    try {
		Class.forName("com.mysql.jdbc.Driver");
		String urllocal = "jdbc:mysql://" + config.getHostl() + ":"
			+ config.getPuertol() + "/" + config.getDbl()
			+ "?autoReconnect=true";
		connl = DriverManager.getConnection(urllocal,
			config.getUsuariol(), config.getPassl());
		conectadol = true;

	    } catch (ClassNotFoundException | SQLException e) {

		if (logger.isErrorEnabled()) {
		    if(e instanceof ClassNotFoundException)
		    logger.error("Error de conectividad: " +e.getMessage());
		    else if(e instanceof SQLException)
			logger.error("Error de sentencia SQL: " +e.getMessage());
		}

		if (conectadol)
		    cierral();

		try {
		    Thread.sleep(IrrisoftConstantes.DELAY_TAREA_5SEG);
		} catch (InterruptedException e1) {

		    if (logger.isErrorEnabled()) {
			logger.error("Hilo de conectividad interrumpido: " +e1.getMessage());
		    }
		}

		conectadol = false;
		IR.lblstatusl.setForeground(Color.RED);
		IR.lblstatusl.setText("Desconectado");

	    }
	}
	return conectadol;

    }

    /**
     * Cierro la conexion con la BBDD Remota y lo pongo a Desconectado.
     */
    public void cierrar() {
	if (connr != null) {
	    try {

		connr.close();

		IR.lblstatusr.setForeground(Color.RED);
		IR.lblstatusr.setText("Desconectado");
		conectador = false;

	    } catch (Exception e) {
		if (logger.isErrorEnabled()) {
		    logger.error(e.getMessage());
		}

	    }
	}
    }

    /**
     * Cierro la conexion con la BBDD local y lo pongo a Desconectado.
     */
    public void cierral() {
	if (connl != null) {
	    try {
		connl.close();

		IR.lblstatusl.setForeground(Color.RED);
		IR.lblstatusl.setText("Desconectado");
		conectadol = false;

	    } catch (Exception e) {
		if (logger.isErrorEnabled()) {
		    logger.error(e.getMessage());
		}
	    }
	}
    }

    //METODO RELACIONADO CON IP
    /**
     * Calculo la IP del programador en la interfaz ppp0
     */
    public void calculaip() {

	try {

	    Enumeration<NetworkInterface> n = NetworkInterface
		    .getNetworkInterfaces();

	    for (; n.hasMoreElements();) {
		NetworkInterface e = n.nextElement();

		if (logger.isInfoEnabled()) {
		    logger.info(e.getDisplayName());
		}

		// if (e.getDisplayName().contentEquals("ppp0")
		// || e.getDisplayName().contentEquals("wlan0")) {
		if (e.getDisplayName().contentEquals("ppp0")) {
		    Enumeration<InetAddress> a = e.getInetAddresses();
		    for (; a.hasMoreElements();) {
			InetAddress addr = a.nextElement();
			if (IR.ip != null) {
			    if (addr.getHostAddress().contentEquals(IR.ip)) {
			    } else {
				// Para que sólo escriba las IPStack4
				if (!addr.getHostAddress().contains(":")) {
				    IR.ip = addr.getHostAddress();
				    escribeip();
				}
			    }
			}

			else {

			    // Para que sólo escriba las IPStack4
			    if (!addr.getHostAddress().contains(":")) {
				IR.ip = addr.getHostAddress();
				escribeip();
			    }
			}
		    }
		}
	    }
	} catch (SocketException e1) {
	    if (logger.isErrorEnabled()) {
		logger.error(e1.getMessage());
	    }
	}
    }

    /**
     * Escribo la IP del programador en la BBDD Remota (GIS).
     */
    protected void escribeip() {

	CallableStatement cs = null;

	try {
	    cs = connr.prepareCall("{ call P061_U_PROGRAMADOR(?,?) }");

	    cs.setString("V_CODPROG", Irrisoft.config.getIdrasp());
	    cs.setString("V_IPPROG", IR.ip);
	    cs.execute();
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	} finally {
	    try {
		cs.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		cierrar();
	    }
	}
    }

    //METODOS RELACIONADOS CON CONFIGURACION INICIAL
    /**
     * Leo la configuracion del archivo Volvado.properties que contiene los
     * datos de configuracion de la BBDD local y la BBDD Remota.
     * 
     * @param config
     */
    public void leerconf(Conf config) {

	Properties propiedades = new Properties() {
	    @Override
	    public synchronized Enumeration<Object> keys() {
		return Collections.enumeration(new TreeSet<Object>(super
			.keySet()));
	    }
	};
	InputStream lecturaVolcado = null;
	BasicTextEncryptor text = new BasicTextEncryptor();
	String a, b, c, d, e, f, g, h, i, j, k, l, m = null;

	try {
	    lecturaVolcado = new FileInputStream("Volcado.properties");
	    propiedades.load(lecturaVolcado);

	    if (propiedades.getProperty("FLAG").equals("si")) {
		// Coloco la contraseña de
		text.setPassword(IrrisoftConstantes.PASSWORD);

		f = text.encrypt(propiedades.getProperty("FLAG"));
		propiedades.setProperty("FLAG", f);

		l = text.encrypt(propiedades.getProperty("Remota.FLAG_CAMBIOS"));
		propiedades.setProperty("Remota.FLAG_CAMBIOS", l);

		config.setHostr(propiedades
			.getProperty("Remota.Conexion.HOSTR"));
		// a = text.encrypt(propiedades.getProperty("HOSTR"));
		// propiedades.setProperty("HOSTR", a);

		config.setPuertor(Integer.parseInt(propiedades
			.getProperty("Remota.Conexion.PUERTOR")));
		// b = text.encrypt(propiedades.getProperty("PUERTOR"));
		// propiedades.setProperty("PUERTOR", b);

		config.setDbr(propiedades.getProperty("Remota.DBR"));
		// c = text.encrypt(propiedades.getProperty("DBR"));
		// propiedades.setProperty("DBR", c);

		config.setUserr(propiedades
			.getProperty("Remota.Login.USUARIOR"));
		// d = text.encrypt(propiedades.getProperty("USUARIOR"));
		// propiedades.setProperty("USUARIOR", d);

		config.setPassr(propiedades.getProperty("Remota.Login.PASSR"));
		// e = text.encrypt(propiedades.getProperty("PASSR"));
		// propiedades.setProperty("PASSR", e);

		config.setHostl(propiedades.getProperty("Local.Conexion.HOSTL"));
		g = text.encrypt(propiedades
			.getProperty("Local.Conexion.HOSTL"));
		propiedades.setProperty("Local.Conexion.HOSTL", g);

		config.setPuertol(Integer.parseInt(propiedades
			.getProperty("Local.Conexion.PUERTOL")));
		h = text.encrypt(propiedades
			.getProperty("Local.Conexion.PUERTOL"));
		propiedades.setProperty("Local.Conexion.PUERTOL", h);

		config.setDbl(propiedades.getProperty("Local.DBL"));
		i = text.encrypt(propiedades.getProperty("Local.DBL"));
		propiedades.setProperty("Local.DBL", i);

		config.setUsuariol(propiedades
			.getProperty("Local.Login.USUARIOL"));
		j = text.encrypt(propiedades
			.getProperty("Local.Login.USUARIOL"));
		propiedades.setProperty("Local.Login.USUARIOL", j);

		config.setPassl(propiedades.getProperty("Local.Login.PASSL"));
		k = text.encrypt(propiedades.getProperty("Local.Login.PASSL"));
		propiedades.setProperty("Local.Login.PASSL", k);

		config.setTiempo(Integer.parseInt(propiedades
			.getProperty("TIEMPO")));
		// Fichero de salida y guardo los datos.
		OutputStream os = new FileOutputStream("Volcado.properties");
		propiedades.store(os, null);

	    } else if (propiedades.getProperty("Remota.FLAG_CAMBIOS").equals(
		    "si")) {

		text.setPassword(IrrisoftConstantes.PASSWORD);

		f = text.encrypt(propiedades.getProperty("Remota.FLAG_CAMBIOS"));
		propiedades.setProperty("Remota.FLAG_CAMBIOS", f);

		config.setHostr(propiedades
			.getProperty("Remota.Conexion.HOSTR"));
		// a = text.encrypt(propiedades.getProperty("HOSTR"));
		// propiedades.setProperty("FLAG_CAMBIOS", a);

		config.setPuertor(Integer.parseInt(propiedades
			.getProperty("Remota.Conexion.PUERTOR")));
		// b = text.encrypt(propiedades.getProperty("PUERTOR"));
		// propiedades.setProperty("PUERTOR", b);

		config.setDbr(propiedades.getProperty("Remota.DBR"));
		// c = text.encrypt(propiedades.getProperty("DBR"));
		// propiedades.setProperty("DBR", c);

		config.setUserr(propiedades
			.getProperty("Remota.Login.USUARIOR"));
		// d = text.encrypt(propiedades.getProperty("USUARIOR"));
		// propiedades.setProperty("USUARIOR", d);

		config.setPassr(propiedades.getProperty("Remota.Login.PASSR"));
		// e = text.encrypt(propiedades.getProperty("PASSR"));
		// propiedades.setProperty("PASSR", e);

		g = text.decrypt(propiedades
			.getProperty("Local.Conexion.HOSTL"));
		propiedades.setProperty("Local.Conexion.HOSTL", g);
		config.setHostl(propiedades.getProperty("Local.Conexion.HOSTL"));
		g = text.encrypt(propiedades
			.getProperty("Local.Conexion.HOSTL"));
		propiedades.setProperty("Local.Conexion.HOSTL", g);

		h = text.decrypt(propiedades
			.getProperty("Local.Conexion.PUERTOL"));
		propiedades.setProperty("Local.Conexion.PUERTOL", h);
		config.setPuertol(Integer.parseInt(propiedades
			.getProperty("Local.Conexion.PUERTOL")));
		h = text.encrypt(propiedades
			.getProperty("Local.Conexion.PUERTOL"));
		propiedades.setProperty("Local.Conexion.PUERTOL", h);

		i = text.decrypt(propiedades.getProperty("Local.DBL"));
		propiedades.setProperty("Local.DBL", i);
		config.setDbl(propiedades.getProperty("Local.DBL"));
		i = text.encrypt(propiedades.getProperty("Local.DBL"));
		propiedades.setProperty("Local.DBL", i);

		j = text.decrypt(propiedades
			.getProperty("Local.Login.USUARIOL"));
		propiedades.setProperty("Local.Login.USUARIOL", j);
		config.setUsuariol(propiedades
			.getProperty("Local.Login.USUARIOL"));
		j = text.encrypt(propiedades
			.getProperty("Local.Login.USUARIOL"));
		propiedades.setProperty("Local.Login.USUARIOL", j);

		k = text.decrypt(propiedades.getProperty("Local.Login.PASSL"));
		propiedades.setProperty("Local.Login.PASSL", k);
		config.setPassl(propiedades.getProperty("Local.Login.PASSL"));
		k = text.encrypt(propiedades.getProperty("Local.Login.PASSL"));
		propiedades.setProperty("Local.Login.PASSL", k);

		config.setTiempo(Integer.parseInt(propiedades
			.getProperty("TIEMPO")));

		OutputStream os = new FileOutputStream("Volcado.properties");
		propiedades.store(os, null);

	    } else {

		text.setPassword(IrrisoftConstantes.PASSWORD);

		// a = text.decrypt(propiedades.getProperty("HOSTR"));
		// propiedades.setProperty("HOSTR", a);
		config.setHostr(propiedades
			.getProperty("Remota.Conexion.HOSTR"));

		// b = text.decrypt(propiedades.getProperty("PUERTOR"));
		// propiedades.setProperty("PUERTOR", b);
		config.setPuertor(Integer.parseInt(propiedades
			.getProperty("Remota.Conexion.PUERTOR")));

		// c = text.decrypt(propiedades.getProperty("DBR"));
		// propiedades.setProperty("DBR", c);
		config.setDbr(propiedades.getProperty("Remota.DBR"));

		// d = text.decrypt(propiedades.getProperty("USUARIOR"));
		// propiedades.setProperty("USUARIOR", d);
		config.setUserr(propiedades
			.getProperty("Remota.Login.USUARIOR"));

		// e = text.decrypt(propiedades.getProperty("PASSR"));
		// propiedades.setProperty("PASSR", e);
		config.setPassr(propiedades.getProperty("Remota.Login.PASSR"));

		String hostl = text.decrypt(propiedades
			.getProperty("Local.Conexion.HOSTL"));
		propiedades.setProperty("Local.Conexion.HOSTL", hostl);
		config.setHostl(propiedades.getProperty("Local.Conexion.HOSTL"));

		String puertol = text.decrypt(propiedades
			.getProperty("Local.Conexion.PUERTOL"));
		propiedades.setProperty("Local.Conexion.PUERTOL", puertol);
		config.setPuertol(Integer.parseInt(propiedades
			.getProperty("Local.Conexion.PUERTOL")));

		String dbl = text.decrypt(propiedades.getProperty("Local.DBL"));
		propiedades.setProperty("Local.DBL", dbl);
		config.setDbl(propiedades.getProperty("Local.DBL"));

		String usuariol = text.decrypt(propiedades
			.getProperty("Local.Login.USUARIOL"));
		propiedades.setProperty("Local.Login.USUARIOL", usuariol);
		config.setUsuariol(propiedades
			.getProperty("Local.Login.USUARIOL"));

		String passl = text.decrypt(propiedades
			.getProperty("Local.Login.PASSL"));
		propiedades.setProperty("Local.Login.PASSL", passl);
		config.setPassl(propiedades.getProperty("Local.Login.PASSL"));

		config.setTiempo(Integer.parseInt(propiedades
			.getProperty("TIEMPO")));

	    }

	} catch (FileNotFoundException ex) {
	    ex.getMessage();
	    if (logger.isErrorEnabled()) {
		logger.error("No existe el fichero de configuración !! \n Se sale !");
		logger.error(ex.getMessage());
	    }

	} catch (IOException ex1) {

	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido leer el archivo !! \n Se sale !");
		logger.error(ex1.getMessage());
	    }
	}
	try {
	    lecturaVolcado.close();

	} catch (Exception ex2) {
	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido cerrar la lectura de archivo !!");
		logger.error(ex2.getMessage());
	    }
	}

    }

    /**
     * Inserto la configuracion inicial de los sensores que tiene el programador
     * en la BBDD local.
     * 
     * @param codprog
     * @param num_placa
     * @param tipo_placa
     * @param num_sensor
     * @param num_borna
     * @param uni_med
     * @param uni_sal
     * @param rang_med_min
     * @param rang_med_max
     * @param rang_sal_min
     * @param rang_sal_max
     * @param med_umbral_min
     * @param med_umbral_max
     * @param frec_lect
     * @param frec_env
     * @param k
     * @param error_sup
     * @param error_inf
     * @param t_max_riego
     * @param num_est_prop
     * @param num_est_asoc
     * @return
     */
    private boolean insertarConf_ini_sens(String codprog, int num_placa,
	    String tipo_placa, String num_sensor, int num_borna,
	    String uni_med, String uni_sal, double rang_med_min,
	    double rang_med_max, double rang_sal_min, double rang_sal_max,
	    double med_umbral_min, double med_umbral_max, int frec_lect,
	    int frec_env, int k, int error_sup, int error_inf, int t_max_riego,
	    String num_est_prop, String num_est_asoc) {

	if (logger.isInfoEnabled()) {
	    logger.info("Aki inserto configuración de sensores !");
	}

	PreparedStatement ps = null;

	try {
	    ps = connl
		    .prepareStatement("INSERT INTO conf_ini_sens (codprog, num_placa, tipo_placa,"
			    + "num_sensor, num_borna, uni_med,uni_sal, rang_med_min,rang_med_max,rang_sal_min,rang_sal_max,med_umbral_min,med_umbral_max,frec_lect, "
			    + "frec_env,k,error_sup,error_inf,t_max_riego,num_est_prop,num_est_asoc) "
			    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) "
			    + "ON DUPLICATE KEY UPDATE "
			    + "num_placa= VALUES(num_placa),"
			    + "tipo_placa= VALUES(tipo_placa),"
			    + "num_sensor= VALUES(num_sensor),"
			    + "num_borna= VALUES(num_borna),"
			    + "uni_med= VALUES(uni_med),"
			    + "uni_sal= VALUES(uni_sal),"
			    + "rang_med_min= VALUES(rang_med_min),"
			    + "rang_med_max= VALUES(rang_med_max),"
			    + "rang_sal_min= VALUES(rang_sal_min),"
			    + "rang_sal_max= VALUES(rang_sal_max),"
			    + "med_umbral_min= VALUES(med_umbral_min),"
			    + "med_umbral_max= VALUES(med_umbral_max),"
			    + "frec_lect= VALUES(frec_lect),"
			    + "frec_env= VALUES(frec_env),"
			    + "k= VALUES(k),"
			    + "error_sup= VALUES(error_sup),"
			    + "error_inf= VALUES(error_inf),"
			    + "t_max_riego= VALUES(t_max_riego),"
			    + "num_est_prop= VALUES(num_est_prop),"
			    + "num_est_asoc= VALUES(num_est_asoc)");

	    ps.setString(1, codprog);
	    ps.setInt(2, num_placa);
	    ps.setString(3, tipo_placa);
	    ps.setString(4, num_sensor);
	    ps.setInt(5, num_borna);
	    ps.setString(6, uni_med);
	    ps.setString(7, uni_sal);
	    ps.setDouble(8, rang_med_min);
	    ps.setDouble(9, rang_med_max);
	    ps.setDouble(10, rang_sal_min);
	    ps.setDouble(11, rang_sal_max);
	    ps.setDouble(12, med_umbral_min);
	    ps.setDouble(13, med_umbral_max);
	    ps.setInt(14, frec_lect);
	    ps.setInt(15, frec_env);
	    ps.setInt(16, k);
	    ps.setInt(17, error_sup);
	    ps.setInt(18, error_inf);
	    ps.setInt(19, t_max_riego);
	    ps.setString(20, num_est_prop);
	    ps.setString(21, num_est_asoc);

	    ps.executeUpdate();
	    return true;

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido actualizar la configuración inicial de sensores en la BBDD local.");
		logger.error(e.getMessage());
	    }
	    return false;
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia SQL: " + e.getMessage());
		}
		cierral();
	    }
	}

    }

    /**
     * Inserto la configuracion inicial de valulas del programador en la BBDD
     * local.
     * 
     * @param configuracion
     * @param codigo
     * @param estacion
     * @param deco
     * @param maestra
     * @param latch
     * @param goteo
     * @return
     */
    private boolean insertarConf_ini_prog(int configuracion, String codigo,
	    String estacion, int deco, int maestra, int latch, int goteo) {

	if (logger.isInfoEnabled()) {
	    logger.info("Aki inserto configuración de los programadores !");
	}

	PreparedStatement ps = null;
	try {
	    ps = connl
		    .prepareStatement("INSERT INTO conf_ini_prog (id_conf_ini_prog,codprog,num_estacion,deco,maestra,latch,goteo) "
			    + "VALUES (?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE deco=VALUES(deco),maestra = VALUES(maestra),latch=VALUES(latch),goteo=VALUES(goteo)");
	    ps.setInt(1, configuracion);
	    ps.setString(2, codigo);
	    ps.setString(3, estacion);
	    ps.setInt(4, deco);
	    ps.setInt(5, maestra);
	    ps.setInt(6, latch);
	    ps.setInt(7, goteo);
	    ps.executeUpdate();
	    return true;

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido introcucir la configuracion inicial del programador");
		logger.error(e.getMessage());
	    }
	    return false;
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		cierral();
	    }
	}

    }

    /**
     * Elimino la tabla entera del String que se pase por campo.
     * 
     * @param tabla
     * @return
     */
    private boolean borratabla(String tabla) {

	PreparedStatement ps = null;

	try {
	    ps = connl.prepareStatement("delete from " + tabla);

	    ps.executeUpdate();
	    return true;

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido borrar la tabla " + tabla);
		logger.error(e.getMessage());
	    }
	    return false;
	} finally {
	    try {
		ps.close();
	    } catch (SQLException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Error en la sentencia." + e.getMessage());
		}
		cierral();
	    }
	}

    }

    // ////////////////////////
    //GETTERS Y SETTERS
    // ////////////////////////
    /**
     * Cojo el valor de la Conexion Remota
     * @return
     */
    public Connection getConnr() {
	return connr;
    }

    /**
     * Coloco el valor de la conexion Remota.
     * @param connr
     */
    public void setConnr(Connection connr) {

	this.connr = connr;
    }

    public Conf getConfig() {
	return config;
    }

    public void setConfig(Conf config) {
	this.config = config;
    }

    //LOS METODOS SET PARA EL LISTENER
    /**
     * Cojo el valor de la variable conectador.
     * @return
     */
    public boolean isConectador() {
	return conectador;
    }

    /**
     * Seteo la variable conectador para avisar al Gestor de Alertas, que hay
     * conectividad.
     * 
     * @param conectadoRe
     */
    public void setConectador(boolean conectadoRe) {
	boolean vieja = this.conectador;
	this.conectador = conectadoRe;
	changeSupport.firePropertyChange("true", vieja, this.conectador);
    }

    /**
     * Cojo la variable de la variable sincronizar.
     * @return
     */
    public boolean isSincronizar() {
	return sincronizar;
    }

    /**
     * Seteo la variable para avisar a los hilos de que eliminen los hilos.
     * 
     * @param sincronizarRe
     */
    public void setSincronizar(boolean sincronizarRe) {
	boolean vieja = this.sincronizar;
	this.sincronizar = sincronizarRe;
	changeSupport.firePropertyChange("false", vieja, this.sincronizar);
    }

    //PROPERTY CHANGE LISTENER
    /**
     * Añado el listener a la clase.
     * 
     * @param campo
     * @param listener
     */
    public void addPropertyChangeListener(String campo,
	    PropertyChangeListener listener) {
	logger.info("Estoy en addPropertyChangeListener de Conexion Volcado");
	this.changeSupport.addPropertyChangeListener(campo, listener);

    }

    /**
     * Elimino el listener de la clase.
     * 
     * @param campo
     * @param listener
     */
    public void removePropertyChangeListener(String campo,
	    PropertyChangeListener listener) {
	logger.info("Estoy en removePropertyChangeListener de Conexion Volcado");
	this.changeSupport.removePropertyChangeListener(campo, listener);
    }

}
