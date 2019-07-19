package irrisoftTest;

import static org.junit.Assert.*;

import irrisoftpack.ConexionDB;
import irrisoftpack.Conf;
import irrisoftpack.Irrisoft;

import java.awt.Color;
import java.awt.Panel;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import javax.print.attribute.standard.DateTimeAtCompleted;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.internal.runners.TestClass;

import alertaspack.GestorAlertas;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import panelespack.PanelBt2;
import panelespack.Panelmci;
import programaTest.MiHiloPrograma2;
import programapack.ListaProgsaexec;
import programapack.Programacion;
import programapack.TareaProg;

import tareaTest.MiHiloTarea;
import tareapack.TareaManual;
import valvulaspack.ListaValvBt2;
import valvulaspack.ListaValvMci;
import valvulaspack.Valvula;
import volcadopack.ConexionVolc;
import volcadopack.Volcado;

public class ConexionDBTest extends ConexionVolc {

    private static Logger logger = LogManager.getLogger(ConexionDBTest.class
	    .getName());

    TareaManual tarea;
    Programacion progra;
    TareaProg taprog;
    public String fechahoy;
    public boolean todashechas;
    public boolean echa1 = true;
    public LinkedList<Programacion> listaprogs = ListaProgsaexec.getInstance()
	    .getprogramas();
    public Date hoyDiario;
    public Date hoy;

    // @Test //Echo y Bien: ACABADO
    public void testconectal() {
	ConexionDB conexion = new ConexionDB();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	conexion.conectal();
	// Al cambiar de paquete, no puedo acceder al atributo ya que es
	// PROTECTED
	// assertTrue(conexion.conectado);
	// CASO DE FALLO
	// assertFalse(conexion.conectado);

    }

    // @Test //Hecho y Bien: ACABADO
    public void testdesconectal() {
	ConexionDB conexion = new ConexionDB();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	conexion.conectal();
	// CASO 1
	// assertTrue(conexion.desconectal());
	// CASO 2
	// conexion.desconectal();
	// assertFalse(conexion.conectado);
	// CASO 3 ESTE ES DE FALLO
	conexion.desconectal();
	boolean salida = true;
	// Al cambiar de paquete, no puedo acceder al atributo ya que es
	// PROTECTED
	// assertEquals(conexion.conectado,salida );

    }

    // @Test //IDDS = 1 // Echo y Bien: ACABADO
    public void testtarea1() {

	MiConexionDB miconexion = new MiConexionDB();
	// ConexionDB conexion = new ConexionDB();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.config.setIdrasp("7777");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	Connection conn = null;
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    // LOCAL
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    PreparedStatement sentenciapre = conn
		    .prepareStatement("INSERT INTO tarea (IDTAREA,CODPROG,NUMERESTA,IDDSTAREA,FCEXEC,VALOR,VALOR2,CODTAREA,DSTAREA)  VALUES (?,?,?,?,?,?,?,?,?)");
	    sentenciapre.setInt(1, 28);
	    sentenciapre.setString(2, "7777");
	    sentenciapre.setString(3, "1001");
	    sentenciapre.setInt(4, 1);
	    sentenciapre.setString(5, "2014-9-9");
	    sentenciapre.setInt(6, 50);
	    sentenciapre.setString(7, null);
	    sentenciapre.setString(8, "T001");
	    sentenciapre.setString(9, "idds1");
	    sentenciapre.executeUpdate();
	    sentenciapre.close();

	} catch (SQLException e) {
	    e.getMessage();
	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}
	miconexion.conectal();
	miconexion.tarea();
	assertTrue(miconexion.ejecutada);
    }

    // @Test //IDDS = 2 //Echo y Bien:ACABADO
    public void testtarea2() {

	MiConexionDB miconexion = new MiConexionDB();
	// ConexionDB conexion = new ConexionDB();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.config.setIdrasp("7777");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	Connection conn = null;
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    // LOCAL
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    PreparedStatement sentenciapre = conn
		    .prepareStatement("INSERT INTO tarea (IDTAREA,CODPROG,NUMERESTA,IDDSTAREA,FCEXEC,VALOR,VALOR2,CODTAREA,DSTAREA)  VALUES (?,?,?,?,?,?,?,?,?)");
	    sentenciapre.setInt(1, 29);
	    sentenciapre.setString(2, "7777");
	    sentenciapre.setString(3, "null");
	    sentenciapre.setInt(4, 2);
	    sentenciapre.setString(5, "2014-9-10");
	    sentenciapre.setInt(6, 50);
	    sentenciapre.setString(7, null);
	    sentenciapre.setString(8, "T002");
	    sentenciapre.setString(9, "idds2");
	    sentenciapre.executeUpdate();
	    sentenciapre.close();
	} catch (SQLException e) {
	    e.getMessage();
	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}
	miconexion.conectal();
	miconexion.tarea();
	assertTrue(miconexion.cerradas);
    }

    // @Test //IDDS = 4 // Echo y Bien:ACABADO
    public void testtarea4() {

	// ConexionDB conexion = new ConexionDB();
	MiConexionDB miconexion = new MiConexionDB();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.config.setIdrasp("7777");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	Connection conn = null;
	try {

	    Class.forName("com.mysql.jdbc.Driver");
	    // LOCAL
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    PreparedStatement sentenciapre = conn
		    .prepareStatement("INSERT INTO tarea (IDTAREA,CODPROG,NUMERESTA,IDDSTAREA,FCEXEC,VALOR,VALOR2,CODTAREA,DSTAREA)  VALUES (?,?,?,?,?,?,?,?,?)");
	    sentenciapre.setInt(1, 30);
	    sentenciapre.setString(2, "7777");
	    sentenciapre.setString(3, "1001");
	    sentenciapre.setInt(4, 4);
	    sentenciapre.setString(5, "2014-9-11");
	    sentenciapre.setInt(6, 50);
	    sentenciapre.setString(7, null);
	    sentenciapre.setString(8, "T004");
	    sentenciapre.setString(9, "idds4");
	    sentenciapre.executeUpdate();
	    sentenciapre.close();

	} catch (SQLException e) {
	    e.getMessage();
	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}
	miconexion.conectal();
	miconexion.tarea();
	assertTrue(miconexion.cerradas);

    }

    // @Test //IDDS = 6 //ECHO Y BIEN:ACABADO
    public void testtarea6() {
	ConexionDB conexion = new ConexionDB();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.config.setIdrasp("7777");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	Connection conn = null;
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    // LOCAL
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    PreparedStatement sentenciapre = conn
		    .prepareStatement("INSERT INTO tarea (IDTAREA,CODPROG,NUMERESTA,IDDSTAREA,FCEXEC,VALOR,VALOR2,CODTAREA,DSTAREA)  VALUES (?,?,?,?,?,?,?,?,?)");
	    sentenciapre.setInt(1, 31);
	    sentenciapre.setString(2, "7777");
	    sentenciapre.setString(3, "1001");
	    sentenciapre.setInt(4, 6);
	    sentenciapre.setString(5, "2014-9-12");
	    sentenciapre.setInt(6, 50);
	    sentenciapre.setString(7, "80");
	    sentenciapre.setString(8, "T006");
	    sentenciapre.setString(9, "idds6");
	    sentenciapre.executeUpdate();
	    sentenciapre.close();
	} catch (SQLException e) {
	    e.getMessage();
	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}
	conexion.conectal();
	Programacion prog = new Programacion();
	listaprogs.add(prog);
	listaprogs.get(0).setIdprograma(80);
	listaprogs.get(0).setCuota(30);
	// Al cambiar de paquete, no puedo acceder ya que es un metodo PROTECTED
	// conexion.tarea();

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    // LOCAL
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    PreparedStatement sentenciapre1 = conn
		    .prepareStatement("SELECT CODPROG from tarea where CODPROG=?");
	    sentenciapre1.setString(1, "7777");
	    ResultSet rs = sentenciapre1.executeQuery();
	    assert (rs.next());
	    rs.close();
	    sentenciapre1.close();

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	} catch (ClassNotFoundException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }

	}

    }

    // @Test //Echo y Bien:ACABADO
    public void testcierratodavalvs() {
	// ConexionDB conexion = new ConexionDB();
	MiConexionDB miconexion = new MiConexionDB();
	Valvula valv = new Valvula();
	Irrisoft.window = new Irrisoft();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.window.valvsabiertastot = new LinkedHashSet<Valvula>();
	Irrisoft.window.valvsabiertastot.add(valv);
	valv.setNum_placa(5);
	// Semaforo semaforo = new Semaforo(2);
	// valv.setSemaforo(semaforo);
	Irrisoft.window.panelbt2 = PanelBt2.getInstance();
	miconexion.conectal();
	miconexion.cierratodasvalvs();
	assertTrue(miconexion.borrada);

    }

    // @Test //Echo y Bien: ACABADO, EL METODO EJECCUTATAREAHILO HAY QUE PONERLO
    // PUBLIC
    public void testejecutarea() {
	// ConexionDB conexion = new ConexionDB();
	MiConexionDB miconexion = new MiConexionDB();
	TareaManual TM = new TareaManual();
	TM.setDuracion(260);
	TM.setCodelecvalv("1001");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.valvsbt2 = new ListaValvBt2();
	miconexion.ejecutarea(TM);
	assertTrue(miconexion.ejecutaHilo);

    }

    // @Test //Echo y Bien: ACABADO
    public void testBorratarea1() {
	ConexionDB conexion = new ConexionDB();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.config.setIdrasp("7777");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	Connection conn = null;
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    // LOCAL
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    PreparedStatement sentenciapre = conn
		    .prepareStatement("INSERT INTO tarea (IDTAREA,CODPROG,NUMERESTA,IDDSTAREA,FCEXEC,VALOR,VALOR2,CODTAREA,DSTAREA)  VALUES (?,?,?,?,?,?,?,?,?)");
	    sentenciapre.setInt(1, 30);
	    sentenciapre.setString(2, "7777");
	    sentenciapre.setString(3, "1001");
	    sentenciapre.setInt(4, 1);
	    sentenciapre.setString(5, "1986-1-1");
	    sentenciapre.setInt(6, 50);
	    sentenciapre.setString(7, null);
	    sentenciapre.setString(8, null);
	    sentenciapre.setString(9, null);
	    sentenciapre.executeUpdate();
	    sentenciapre.close();

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	} catch (ClassNotFoundException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	}
	conexion.conectal();
	TareaManual Taream = new TareaManual();
	Taream.setIdtarea(30);
	assertTrue(conexion.Borratarea(Taream) == true);

    }

    // @Test //Echo y Bien: ACABADO
    public void testBorratarea2() {
	ConexionDB conexion = new ConexionDB();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	Connection conn = null;
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    // LOCAL
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    PreparedStatement sentenciapre = conn
		    .prepareStatement("INSERT INTO tarea (CODPROG,NUMERESTA,IDDSTAREA,FCEXEC,VALOR,VALOR2,CODTAREA,DSTAREA)  VALUES (?,?,?,?,?,?,?,?)");
	    sentenciapre.setString(1, "7777");
	    sentenciapre.setString(2, "1001");
	    sentenciapre.setInt(3, 1);
	    sentenciapre.setString(4, "1986-1-1");
	    sentenciapre.setInt(5, 50);
	    sentenciapre.setString(6, null);
	    sentenciapre.setString(7, null);
	    sentenciapre.setString(8, null);
	    sentenciapre.executeUpdate();
	    sentenciapre.close();

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }

	} catch (ClassNotFoundException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	}
	conexion.conectal();
	Irrisoft.config.setIdrasp("7777");
	assertTrue(conexion.Borratarea() == true);

    }

    // @Test //Echo y Bien:
    public void testejecutatareahilo() throws NoSuchMethodException,
	    SecurityException, IllegalAccessException,
	    IllegalArgumentException, InvocationTargetException {
	ConexionDB conexion = new ConexionDB();
	TareaManual tm = new TareaManual();
	tm.setCodelecvalv("1001");
	// Acceso Privado
	// Method metodo =
	// conexion.getClass().getDeclaredMethod("ejecutatareahilo",
	// TareaManual.class);
	// metodo.setAccessible(true);
	// metodo.invoke(conexion, tm);
	// Acceso Publico
	MiHiloTarea mh = new MiHiloTarea(tm);
	conexion.setHilotarea(mh);
	conexion.ejecutatareahilo(tm);

    }

    // @Test //Echo y Bien:ACABADO, para el caso de que no haya nada en memoria.
    // Hay que poner datosProg, tipoProg y esfecha a PUBLIC
    public void testprograma() {
	// ConexionDB conexion = new ConexionDB();
	MiConexionDB miconexion = new MiConexionDB();
	Irrisoft.window = new Irrisoft();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	Connection conn = null;
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    // java.sql.Date fecha = java.sql.Date.valueOf("2014-10-31");
	    // FECHA ACTUAL SQL
	    java.util.Date utilDate = new java.util.Date();
	    long lnMilisegundos = utilDate.getTime();
	    java.sql.Date fecha = new java.sql.Date(lnMilisegundos);

	    PreparedStatement sentenciapre = conn
		    .prepareStatement("INSERT INTO `programa`(`IDPROGRAMA`, `CODPROGRAMA`, `DSPROGRAMA`, `FCINICIO`, `FCFIN`, `ACTIVO`, `CODPROG`, `TIPO`, `DIAL`, `DIAM`, `DIAX`, `DIAJ`, `DIAV`, `DIAS`, `DIAD`, `MODO`, `MODOINI`, `PBLOQUE`, `CUOTA`, `LEIDO`, `ENMARCHA`, `ultdeberes`)"
			    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	    sentenciapre.setInt(1, 500);
	    sentenciapre.setString(2, "Pru");
	    sentenciapre.setString(3, "Pruprog");
	    sentenciapre.setDate(4, fecha);
	    sentenciapre.setDate(5, fecha);
	    sentenciapre.setString(6, "S");
	    sentenciapre.setString(7, "7777");
	    sentenciapre.setString(8, "D");
	    sentenciapre.setInt(9, 0);
	    sentenciapre.setInt(10, 1);
	    sentenciapre.setInt(11, 0);
	    sentenciapre.setInt(12, 0);
	    sentenciapre.setInt(13, 0);
	    sentenciapre.setInt(14, 0);
	    sentenciapre.setInt(15, 0);
	    sentenciapre.setString(16, "A");
	    sentenciapre.setString(17, "S");
	    sentenciapre.setInt(18, 1);
	    sentenciapre.setInt(19, 100);
	    sentenciapre.setString(20, "N");
	    sentenciapre.setString(21, "N");
	    sentenciapre.setDate(22, null);
	    sentenciapre.executeUpdate();
	    sentenciapre.close();

	} catch (SQLException e) {
	    e.getMessage();
	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}

	miconexion.conectal();
	Irrisoft.config.setIdrasp("7777");
	Irrisoft.window.hoy = new Date();
	// Al cambiar de paquete, no puedo accede ya que es un metodo PROTECTED
	// miconexion.programa();
	assertTrue(miconexion.tipoProg);

    }

    // @Test //Echo y Bien: ACABADO//Diario
    public void testesfecha1() throws NoSuchMethodException, SecurityException,
	    IllegalAccessException, IllegalArgumentException,
	    InvocationTargetException, ParseException {
	ConexionDB conexion = new ConexionDB();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	Connection conn = null;
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    // FECHA ACTUAL SQL
	    java.util.Date utilDate = new java.util.Date();
	    long lnMilisegundos = utilDate.getTime();
	    java.sql.Date fecha = new java.sql.Date(lnMilisegundos);
	    // Introduzco la fila en la tabla dias
	    PreparedStatement sentenciapre = conn
		    .prepareStatement("INSERT INTO dias (CODPROG,IDPROGRAMA,FECHA)  VALUES (?,?,?)");
	    sentenciapre.setString(1, "7777");
	    sentenciapre.setInt(2, 381);
	    sentenciapre.setDate(3, fecha);
	    sentenciapre.executeUpdate();
	    sentenciapre.close();

	} catch (SQLException e) {
	    e.getMessage();
	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}

	conexion.conectal();

	// Variables
	int id = 381;
	String tipo = "D";
	Date fechain = new Date();
	Date fechafin = new Date();
	Date a = new Date();
	ArrayList<Date> diasd = new ArrayList<Date>();
	diasd.add(a);
	diasd.set(0, a);

	SimpleDateFormat formatterfecha = new SimpleDateFormat("yyyy-MM-dd");
	String fecha = "2014-31-10";
	fecha = new Timestamp(Calendar.getInstance().getTime().getTime())
		.toString();
	fecha = fecha.substring(0, 10);
	hoyDiario = formatterfecha.parse(fecha);

	// El valor de salida
	boolean input = true;
	// Llamada a metodo privado.
	Method metodo = conexion.getClass().getDeclaredMethod("esfecha",
		int.class, String.class, Date.class, Date.class, Date.class,
		ArrayList.class);
	metodo.setAccessible(true);
	boolean output = (boolean) metodo.invoke(conexion, id, tipo, fechain,
		fechafin, hoyDiario, diasd);
	assertEquals(input, output);

    }

    // @Test //Echo y Bien: ACABADO//Semanal
    public void testesfecha2() throws NoSuchMethodException, SecurityException,
	    IllegalAccessException, IllegalArgumentException,
	    InvocationTargetException, ParseException {
	ConexionDB conexion = new ConexionDB();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	conexion.conectal();

	// Variables
	int id = 382;
	String tipo = "S";
	java.util.Date fechain = new java.util.Date();
	java.util.Date fechafin = new java.util.Date();
	Date hoy = new Date();
	int lunes = 1;
	int martes = 1;
	int miercoles = 1;
	int jueves = 1;
	int viernes = 1;
	int sabado = 1;
	int domingo = 1;
	ArrayList<Integer> diasd = new ArrayList<Integer>();
	diasd.add(lunes);
	diasd.add(martes);
	diasd.add(miercoles);
	diasd.add(jueves);
	diasd.add(viernes);
	diasd.add(sabado);
	diasd.add(domingo);
	diasd.get(sabado);
	// Valor de salida
	boolean input = true;
	// Llamada al metodo privado.
	Method metodo = conexion.getClass().getDeclaredMethod("esfecha",
		int.class, String.class, Date.class, Date.class, Date.class,
		ArrayList.class);
	metodo.setAccessible(true);
	boolean output = (boolean) metodo.invoke(conexion, id, tipo, fechain,
		fechafin, hoy, diasd);
	assertEquals(input, output);
    }

    // @Test //Diario//Echo y Bien: //Cambiar el metodo a PUBLICO
    public void testtipoprog1() throws NoSuchMethodException,
	    SecurityException, IllegalAccessException,
	    IllegalArgumentException, InvocationTargetException, ParseException {
	// Objeto para acceso Privado o Publico
	ConexionDB conexion = new ConexionDB();
	// Objeto para acceso Publico
	MiConexionDB miconexion = new MiConexionDB();
	Programacion prog = new Programacion();
	prog.setTipo("D");
	java.util.Date hoy = new java.util.Date();
	java.sql.Date fecha = new java.sql.Date(hoy.getTime());
	ArrayList<java.sql.Date> days = new ArrayList<>();
	days.add(fecha);
	prog.setDays(days);
	// Metodo Privado
	Method metodo = conexion.getClass().getDeclaredMethod("tipoprog",
		Programacion.class, Date.class);
	metodo.setAccessible(true);
	metodo.invoke(conexion, prog, hoy);
	// Acesso Publico y Assert
	// miconexion.tipoprog(prog, hoy);
	// assertTrue(miconexion.creaTareasProg);
    }

    // @Test //Semanal //Echo y Bien:ACABADO // Cambiar el metodo a PUBLICO
    public void testtipoprog2() throws NoSuchMethodException,
	    SecurityException, IllegalAccessException,
	    IllegalArgumentException, InvocationTargetException, ParseException {
	// Objetos
	ConexionDB conexion = new ConexionDB();
	MiConexionDB miconexion = new MiConexionDB();
	Programacion prog = new Programacion();
	// Variables
	prog.setTipo("S");
	// Fechas
	java.util.Date hoy = new java.util.Date();
	java.sql.Date fecha = new java.sql.Date(hoy.getTime());
	prog.setFcinicio(fecha);
	// Pongo el dia que es para activarlo
	prog.setDiav(1);
	// Acesso Privado
	Method metodo = conexion.getClass().getDeclaredMethod("tipoprog",
		Programacion.class, Date.class);
	metodo.setAccessible(true);
	metodo.invoke(conexion, prog, hoy);
	// Acesso Publico y Assert
	// miconexion.tipoprog(prog, hoy);
	// assertTrue(miconexion.creaTareasProg);

    }

    // @Test //Echo y Bien:ACABADO //Cambiar el metodo a PUBLICO
    public void testdatosprog() throws NoSuchMethodException,
	    SecurityException, IllegalAccessException,
	    IllegalArgumentException, InvocationTargetException, ParseException {
	ConexionDB conexion = new ConexionDB();
	Programacion prog = new Programacion();
	// VARIABLES PARA EL ASSERT
	int duracion = 60;
	DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
	Date fecha1 = df.parse("08/10/2014");
	Time horainicio = new Time(10, 00, 00);
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	conexion.conectal();

	Connection conn = null;
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    // LOCAL
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    String date = "2014-10-08";
	    java.sql.Date fecha = java.sql.Date.valueOf(date);

	    PreparedStatement sentenciapre = conn
		    .prepareStatement("INSERT INTO valvulas (CODPROG,IDPROGVALV,IDPROGRAMA,CODELECVALV,DURACION,BLOQUE) VALUES (?,?,?,?,?,?)");
	    sentenciapre.setString(1, "7777");
	    sentenciapre.setInt(2, 1);
	    sentenciapre.setInt(3, 50);
	    sentenciapre.setString(4, "1001");
	    sentenciapre.setInt(5, 60);
	    sentenciapre.setInt(6, 1);
	    sentenciapre.executeUpdate();

	    sentenciapre = conn
		    .prepareStatement("INSERT INTO dias (CODPROG,IDPROGDIAS,IDPROGRAMA,FECHA) VALUES (?,?,?,?)");
	    sentenciapre.setString(1, "7777");
	    sentenciapre.setInt(2, 1);
	    sentenciapre.setInt(3, 50);
	    sentenciapre.setDate(4, fecha);
	    sentenciapre.executeUpdate();

	    sentenciapre = conn
		    .prepareStatement("INSERT INTO horas (CODPROG,IDPROGHORAS,IDPROGRAMA,HRINICIO,HRFIN,ULTEJECUCION) VALUES (?,?,?,?,?,?)");
	    sentenciapre.setString(1, "7777");
	    sentenciapre.setInt(2, 1);
	    sentenciapre.setInt(3, 50);
	    sentenciapre.setString(4, "10:00:00");
	    sentenciapre.setString(5, "10:30:00");
	    sentenciapre.setDate(6, fecha);
	    sentenciapre.executeUpdate();
	    sentenciapre.close();

	} catch (SQLException e) {
	    e.getMessage();
	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}
	prog.setIdprograma(50);
	// ACCESO PRIVADO
	// Method metodo = conexion.getClass().getDeclaredMethod("datosprog",
	// Programacion.class);
	// metodo.setAccessible(true);
	// metodo.invoke(conexion, prog);
	// ACCESO PUBLICO
	// conexion.datosprog(prog);
	Connection conn1 = null;

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    // LOCAL
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn1 = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    PreparedStatement sentenciapre1 = conn1
		    .prepareStatement("SELECT DURACION from valvulas where IDPROGRAMA = ?");
	    sentenciapre1.setInt(1, 50);
	    ResultSet rs = sentenciapre1.executeQuery();
	    while (rs.next()) {
		int a = rs.getInt(1);
		assertEquals(a, duracion);
		logger.warn("Duracion igual");
	    }
	    rs.close();

	    sentenciapre1 = conn1
		    .prepareStatement("SELECT FECHA from dias where IDPROGRAMA = ?");
	    sentenciapre1.setInt(1, 50);
	    ResultSet rs1 = sentenciapre1.executeQuery();
	    while (rs1.next()) {
		Date b = rs1.getDate(1);
		assertEquals(b, fecha1);
		logger.warn("Fecha igual");
	    }
	    rs1.close();

	    sentenciapre1 = conn1
		    .prepareStatement("SELECT HRINICIO from horas where IDPROGRAMA = ?");
	    sentenciapre1.setInt(1, 50);
	    ResultSet rs2 = sentenciapre1.executeQuery();
	    while (rs2.next()) {
		Time c = rs2.getTime(1);
		assertEquals(c, horainicio);
		logger.warn("Hora inicio igual");
	    }
	    rs2.close();
	    sentenciapre1.close();

	} catch (SQLException e) {
	    e.getMessage();
	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}

    }

    // @Test //REVISAR
    public void testcreatareasprog() {
	ConexionDB conexion = new ConexionDB();
	MiConexionDB miconexion = new MiConexionDB();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	miconexion.conectal();
	Programacion pro = new Programacion();
	// Array de valvulas
	Valvula valv = new Valvula();
	ArrayList<Valvula> valvuprog = new ArrayList<Valvula>();
	valvuprog.add(valv);
	pro.setValvuprog(valvuprog);
	// Array de horas inicio
	Time hi = new Time(1000);
	ArrayList<Time> horasini = new ArrayList<Time>();
	horasini.add(hi);
	pro.setHorasini(horasini);
	// Array de horas fin
	Time hf = new Time(200);
	ArrayList<Time> horasfin = new ArrayList<Time>();
	horasfin.add(hf);
	pro.setHorasfin(horasfin);

	// Coloco los valores
	TareaProg tar = new TareaProg();
	tar.setIdprog(30);
	valv.setCodelecvalv("1001");
	Date b = new Date(2014 - 10 - 15);
	tar.setFechaini(b);
	// tar.setHorafin(hf);
	tar.setDuracion(1200L);
	tar.setDuracionini(600);
	tar.setHecha(0);
	tar.setBloque(1);
	tar.setTipobloque("S");
	tar.setCuota(100);
	tar.setTipoplaca(5);

	tar.setPuertoserie(Irrisoft.config.getBt2());

	miconexion.creatareasprog(pro);
	assertTrue(miconexion.progEjecutado);

    }

    // @Test //Echo y Bien:ACABADO
    public void testejecutareaprog() {
	// Objetos
	TareaProg tprog = new TareaProg();
	Programacion pro = new Programacion();
	ConexionDB conexion = new ConexionDB();
	int z = 0;
	MiHiloPrograma2 miHp2 = new MiHiloPrograma2(tprog, z, pro);
	Valvula valv = new Valvula();
	// MiConexionDB miconexion = new MiConexionDB();
	// Variables
	valv.setCodelecvalv("1");
	tprog.setCodelecvalv("1");
	conexion.setHiloprog(miHp2);
	// Al cambiar de paquete, no puedo acceder ya que es un metodo PROTECTED
	// conexion.ejecutareaprog(tprog, z, pro);
    }

    // @Test //Echo y Bien:ACABADO
    public void testborratareaprog() {

	ConexionDB conexion = new ConexionDB();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	Connection conn = null;
	// preparacion
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    // LOCAL
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    PreparedStatement sentenciapre = conn
		    .prepareStatement("INSERT INTO tareasprogdia (idprog,horaini,duracion,hecha,tiempoexec,codelecvalv)  VALUES (?,?,?,?,?,?)");
	    sentenciapre.setInt(1, 500);
	    sentenciapre.setString(2, "10:00:00");
	    sentenciapre.setInt(3, 60);
	    sentenciapre.setInt(4, 0);
	    sentenciapre.setInt(5, 60);
	    sentenciapre.setString(6, "1001");
	    sentenciapre.executeUpdate();
	    sentenciapre.close();

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }

	} catch (ClassNotFoundException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }

	}

	// test
	conexion.borratareaprog(500);
	// comprobacion
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    // LOCAL
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    PreparedStatement sentenciapre1 = conn
		    .prepareStatement("SELECT idprog from tareasprogdia where idprog=?");
	    sentenciapre1.setInt(1, 500);
	    ResultSet rs = sentenciapre1.executeQuery();
	    // assert
	    assert (rs.next());
	    logger.warn("Se borra la tarea");
	    rs.close();
	    sentenciapre1.close();

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }

	} catch (ClassNotFoundException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	}

    }

    // @Test //Echo y Bien:ACABADO
    public void testborraprogramacion() {
	ConexionDB conexion = new ConexionDB();
	LinkedList<Programacion> listaprogs = ListaProgsaexec.getInstance()
		.getprogramas();
	Programacion prog = new Programacion();
	int d = 340;
	prog.setIdprograma(d);
	listaprogs.add(prog);
	conexion.borraprogramacion(d);
	assertTrue(listaprogs.size() == 0);

    }

    //@Test
    // Echo y Bien:ACABADO
    public void testactualizadeberes() {

	MiConexionDB conexion = new MiConexionDB();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	String fechahoy = new java.sql.Date(System.currentTimeMillis())
		.toString();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();

	Connection conn = null;
	// preparacion
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    // LOCAL
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    String date = "2015-10-10";
	    java.sql.Date fecha = java.sql.Date.valueOf(date);

	    PreparedStatement sentenciapre = conn
		    .prepareStatement("INSERT INTO `programa`(`IDPROGRAMA`, `CODPROGRAMA`, `DSPROGRAMA`, `FCINICIO`, `FCFIN`, `ACTIVO`, `CODPROG`, `TIPO`, `DIAL`, `DIAM`, `DIAX`, `DIAJ`, `DIAV`, `DIAS`, `DIAD`, `MODO`, `MODOINI`, `PBLOQUE`, `CUOTA`, `LEIDO`, `ENMARCHA`, `ultdeberes`)"
			    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	    sentenciapre.setInt(1, 501);
	    sentenciapre.setString(2, "actdeb");
	    sentenciapre.setString(3, "adsa");
	    sentenciapre.setDate(4, fecha);
	    sentenciapre.setDate(5, fecha);
	    sentenciapre.setString(6, "S");
	    sentenciapre.setString(7, "7777");
	    sentenciapre.setString(8, "D");
	    sentenciapre.setInt(9, 0);
	    sentenciapre.setInt(10, 0);
	    sentenciapre.setInt(11, 0);
	    sentenciapre.setInt(12, 0);
	    sentenciapre.setInt(13, 0);
	    sentenciapre.setInt(14, 0);
	    sentenciapre.setInt(15, 0);
	    sentenciapre.setString(16, "A");
	    sentenciapre.setString(17, "S");
	    sentenciapre.setInt(18, 1);
	    sentenciapre.setInt(19, 100);
	    sentenciapre.setString(20, "N");
	    sentenciapre.setString(21, "N");
	    sentenciapre.setDate(22, null);
	    sentenciapre.executeUpdate();
	    sentenciapre.close();

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }

	} catch (ClassNotFoundException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	}
	// Al cambiar de paquete no puedo acceder, ya que es un metodo PROTECTED

	conexion.actualizadeberes(501, fechahoy);

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    // LOCAL
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    String date = new java.sql.Date(System.currentTimeMillis())
		    .toString();
	    java.sql.Date fechaprueba = java.sql.Date.valueOf(date);

	    PreparedStatement sentenciapre1 = conn
		    .prepareStatement("SELECT ultdeberes from programa where IDPROGRAMA=?");
	    sentenciapre1.setInt(1, 501);
	    ResultSet rs = sentenciapre1.executeQuery();
	    while (rs.next()) {
		Date a = rs.getDate(1);
		assertEquals(a, fechaprueba);
		logger.warn("Actualizado");
	    }
	    rs.close();
	    sentenciapre1.close();

	} catch (SQLException e) {
	    e.getMessage();

	} catch (ClassNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.getMessage();
	}
    }

    // @Test //Echo y Bien:ACABADO
    public void testactualizatareaprogpasadas() {
	ConexionDB conexion = new ConexionDB();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	Connection conn = null;

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    // LOCAL
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    PreparedStatement sentenciapre = conn
		    .prepareStatement("INSERT INTO tareasprogdia (idtarea,idprog,horaini,duracion,hecha,tiempoexec,codelecvalv)  VALUES (?,?,?,?,?,?,?)");
	    sentenciapre.setInt(1, 30);
	    sentenciapre.setInt(2, 500);
	    sentenciapre.setString(3, "10:00:00");
	    sentenciapre.setInt(4, 60);
	    sentenciapre.setInt(5, 0);
	    sentenciapre.setInt(6, 60);
	    sentenciapre.setString(7, "1001");
	    sentenciapre.executeUpdate();
	    sentenciapre.close();

	} catch (SQLException e) {
	    e.getMessage();

	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}

	TareaProg tprog = new TareaProg();
	// Caso de IDPROG
	tprog.setIdprog(500);
	boolean todas1 = true;
	conexion.actualizatareaprogpasadas(tprog, todas1);
	assertNotNull(tprog.getHecha());

	// Caso de IDTAREA
	// tprog.setIdtarea(30);
	// boolean todas1 = false;
	// conexion.actualizatareaprogpasadas(tprog, todas1);
	// int a = 1;
	// assertEquals(tprog.getHecha(), a);
	logger.warn("Actualizado tareasProg Pasadas");
    }

    // @Test //Echo y Bien:ACABADO
    public void testcogetiemporegado() {
	ConexionDB conexion = new ConexionDB();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	Connection conn = null;

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    // LOCAL
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    PreparedStatement sentenciapre = conn
		    .prepareStatement("INSERT INTO tareasprogdia (idtarea,idprog,horaini,duracion,hecha,tiempoexec,codelecvalv)  VALUES (?,?,?,?,?,?,?)");
	    sentenciapre.setInt(1, 30);
	    sentenciapre.setInt(2, 500);
	    sentenciapre.setString(3, "10:00:00");
	    sentenciapre.setInt(4, 60);
	    sentenciapre.setInt(5, 0);
	    sentenciapre.setInt(6, 60);
	    sentenciapre.setString(7, "1001");
	    sentenciapre.executeUpdate();
	    sentenciapre.close();

	} catch (SQLException e) {
	    e.getMessage();

	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}
	TareaProg tprog = new TareaProg();
	TareaManual tmanu = null;
	tprog.setIdtarea(30);
	conexion.cogetiemporegado(tprog, tmanu);
	int a = 60;
	// assert
	assertEquals(tprog.getTiemporegado(), a);
	logger.warn("Bien cogido el tiempo de regado");

    }

    // @Test //Echo y Bien:ACABADO
    public void testponertiemporegado() {
	ConexionDB conexion = new ConexionDB();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	Connection conn = null;

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    // LOCAL
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    PreparedStatement sentenciapre = conn
		    .prepareStatement("INSERT INTO tareasprogdia (idtarea,idprog,horaini,duracion,hecha,tiempoexec,codelecvalv)  VALUES (?,?,?,?,?,?,?)");
	    sentenciapre.setInt(1, 30);
	    sentenciapre.setInt(2, 500);
	    sentenciapre.setString(3, "10:00:00");
	    sentenciapre.setInt(4, 60);
	    sentenciapre.setInt(5, 0);
	    sentenciapre.setInt(6, 60);
	    sentenciapre.setString(7, "1001");
	    sentenciapre.executeUpdate();
	    sentenciapre.close();

	} catch (SQLException e) {
	    e.getMessage();

	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}

	TareaProg tprog = new TareaProg();
	TareaManual tmanu = null;

	tprog.setIdtarea(30);
	int tiempo = 45;
	conexion.ponetiemporegado(tprog, tmanu, tiempo);

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    // LOCAL
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    PreparedStatement sentenciapre1 = conn
		    .prepareStatement("SELECT tiempoexec from tareasprogdia where idtarea=?");
	    sentenciapre1.setInt(1, 30);
	    ResultSet rs = sentenciapre1.executeQuery();
	    while (rs.next()) {
		int a = rs.getInt(1);
		assertEquals(a, tiempo);
		logger.warn("Bien puesto el tiempo de regado");
	    }
	    rs.close();
	    sentenciapre1.close();

	} catch (SQLException e) {
	    e.getMessage();
	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}

    }

     @Test //Echo y Bien:ACABADO
    public void testacualizaestvalv() {
	ConexionDB conexion = new ConexionDB();
	String codelec = "1003";
	int estado = 1;
	Irrisoft.window = new Irrisoft();
//	Irrisoft.window.lblstatusr.getForeground();
//	Irrisoft.window.lblstatusr.getText();
	this.conectar();
	Irrisoft.window.volcado.getCon().setConnr(this.getConnr());
	Irrisoft.config = new Conf();
	Irrisoft.config.setIdrasp("7777");
	// Al cambiar de paquete, no puedo acceder ya que es un metodo PROTECTED
	conexion.acualizaestvalv(codelec, estado);
//	Connection conn = null;

//	try {
//	    Class.forName("com.mysql.jdbc.Driver");
//
//	    String urllocal = "jdbc:mysql://" + "serviciosgis.com" + ":"
//		    + "5528" + "/" + "prugestdropper";
//
//	    DriverManager.setLoginTimeout(5);
//
//	    conn = DriverManager.getConnection(urllocal, "irrigest", "13riego");
//
//	    PreparedStatement sentenciapre = conn
//		    .prepareStatement("SELECT ESTADO from t18_confiniestacion where CODPROG=? AND NUMERESTA=?");
//	    sentenciapre.setString(1, "7777");
//	    sentenciapre.setString(2, "1003");
//	    ResultSet rs = sentenciapre.executeQuery();
//	    while (rs.next()) {
//		int a = rs.getInt(1);
//		assertTrue(a == estado);
//		logger.warn("Actualizado el estado de la valvula");
//	    }
//	    rs.close();
//	    sentenciapre.close();
//
//	} catch (SQLException e) {
//	    e.getMessage();
//	} catch (ClassNotFoundException e) {
//	    e.getMessage();
//	}

    }

    // @Test //Echo y Bien:ACABADO
    public void testinsertarconsumostest() {
	ConexionDB conexion = new ConexionDB();
	String valvula = "1001";
	float caudal = 1.11f;
	int intensidad = 300;
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusr.getForeground();
	Irrisoft.window.lblstatusr.getText();
	this.conectar();
	Irrisoft.window.volcado.getCon().setConnr(this.getConnr());
	Irrisoft.config = new Conf();
	Irrisoft.config.setIdrasp("7777");
	conexion.insertarconsumostest(valvula, caudal, intensidad);
	Connection conn = null;

	try {
	    Class.forName("com.mysql.jdbc.Driver");

	    String urllocal = "jdbc:mysql://" + "serviciosgis.com" + ":"
		    + "5528" + "/" + "prugestdropper";

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal, "irrigest", "13riego");

	    PreparedStatement sentenciapre = conn
		    .prepareStatement("SELECT CAUDAL from t14_consumo where CODESTACION=?");
	    sentenciapre.setString(1, "1001");
	    ResultSet rs = sentenciapre.executeQuery();
	    while (rs.next()) {
		float a = rs.getFloat(1);
		assertTrue(a == caudal);
		logger.warn("Bien insertado el consumo");
	    }
	    rs.close();
	    sentenciapre.close();

	} catch (SQLException e) {
	    e.getMessage();
	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}

    }

    // @Test //Echo y Bien:ACABADO
    public void testsobrescriberconsumostest() {
	ConexionDB conexion = new ConexionDB();
	String valvula = "1001";
	String valvula1 = "1001";
	float caudal = 3.33f;
	float caudal1 = 6.66f;
	int intensidad = 3;
	int intensidad1 = 6;
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusr.getForeground();
	Irrisoft.window.lblstatusr.getText();
	this.conectar();
	Irrisoft.window.volcado.getCon().setConnr(this.getConnr());
	Irrisoft.config = new Conf();
	Irrisoft.config.setIdrasp("7777");
	conexion.insertarconsumostest(valvula, caudal, intensidad);
	//conexion.sobrescriberconsumostest(valvula1, caudal1, intensidad1);
	Connection conn = null;

	try {
	    Class.forName("com.mysql.jdbc.Driver");

	    String urllocal = "jdbc:mysql://" + "serviciosgis.com" + ":"
		    + "5528" + "/" + "prugestdropper";

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal, "irrigest", "13riego");

	    PreparedStatement sentenciapre = conn
		    .prepareStatement("SELECT CAUDAL,CONSUMELEC from t14_consumo where CODESTACION=?");
	    sentenciapre.setString(1, "1001");

	    ResultSet rs = sentenciapre.executeQuery();
	    while (rs.next()) {
		float a = rs.getFloat(1);
		int b = rs.getInt(2);
		assertTrue(a == caudal1);
		assertTrue(b == intensidad1);
		logger.warn("Bien sobre escrito el consumo");
	    }
	    rs.close();
	    sentenciapre.close();

	} catch (SQLException e) {
	    e.getMessage();
	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}
    }

    // @Test //Echo y Bien:ACABADO
    public void testborrarconsumtest() {
	ConexionDB conexion = new ConexionDB();
	String valvula = "1001";
	float caudal = 5.55f;
	int intensidad = 10;
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusr.getForeground();
	Irrisoft.window.lblstatusr.getText();
	this.conectar();
	Irrisoft.window.volcado.getCon().setConnr(this.getConnr());
	Irrisoft.config = new Conf();
	Irrisoft.config.setIdrasp("7777");
	conexion.insertarconsumostest(valvula, caudal, intensidad);
	conexion.borrarconsumtest();
	Connection conn = null;

	try {
	    Class.forName("com.mysql.jdbc.Driver");

	    String urllocal = "jdbc:mysql://" + "serviciosgis.com" + ":"
		    + "5528" + "/" + "prugestdropper";

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal, "irrigest", "13riego");

	    PreparedStatement sentenciapre = conn
		    .prepareStatement("SELECT CODPROG from t14_consumo where CODPROG=?");
	    sentenciapre.setString(1, "7777");
	    ResultSet rs = sentenciapre.executeQuery();
	    assert (rs.next());
	    logger.warn("Consumo borrado");
	    rs.close();
	    sentenciapre.close();

	} catch (SQLException e) {
	    e.getMessage();
	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}
    }

    // @Test //Echo y Bien:ACABADO
    public void testborralconsumtest() {
	ConexionDB conexion = new ConexionDB();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.config.setIdrasp("7777");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	Connection conn = null;

	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    // LOCAL
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());
	    PreparedStatement sentenciapre = conn
		    .prepareStatement("INSERT INTO modelconsum (CODPROG,CODELECVALV,CAUDAL,INTENSIDAD,FECHA)  VALUES (?,?,?,?,?)");
	    sentenciapre.setString(1, "7777");
	    sentenciapre.setString(2, "1001");
	    sentenciapre.setFloat(3, 11.1f);
	    sentenciapre.setInt(4, 60);
	    sentenciapre.setString(5, "2014-09-07");
	    sentenciapre.executeUpdate();
	    sentenciapre.close();

	} catch (SQLException e) {
	    e.getMessage();

	} catch (ClassNotFoundException e) {
	    e.getMessage();

	}
	conexion.borralconsumtest();
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    // LOCAL
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    PreparedStatement sentenciapre1 = conn
		    .prepareStatement("SELECT CODPROG from modelconsum where CODPROG=?");
	    sentenciapre1.setString(1, "7777");
	    ResultSet rs = sentenciapre1.executeQuery();
	    assert (rs.next());
	    logger.warn("Borrado consumo local");
	    rs.close();
	    sentenciapre1.close();

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }

	} catch (ClassNotFoundException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	}

    }

    // @Test //Echo y Bien:ACABADO
    public void testinsertalconsumostest() {
	ConexionDB conexion = new ConexionDB();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.config.setIdrasp("7777");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	Connection conn = null;
	String valvula = "1001";
	float caudal = 50;
	int intensidad = 90;
	conexion.insertalconsumostest(valvula, caudal, intensidad);
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    // LOCAL
	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    PreparedStatement sentenciapre1 = conn
		    .prepareStatement("SELECT CAUDAL from modelconsum where CODPROG=?");
	    sentenciapre1.setString(1, "7777");
	    ResultSet rs = sentenciapre1.executeQuery();
	    // assertNotNull(rs.next());
	    while (rs.next()) {
		float a = rs.getFloat(1);
		assertTrue(a == caudal);
		logger.warn("Consumo insertado en la local");
	    }
	    rs.close();
	    sentenciapre1.close();
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }

	} catch (ClassNotFoundException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	}

    }

    // @Test //Echo y Bien:ACABADO
    public void testsobrescribelconsumostest() {
	ConexionDB conexion = new ConexionDB();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	String valvula = "1001";
	float caudal = 10;
	int intensidad = 20;
	conexion.insertalconsumostest(valvula, caudal, intensidad);
	String valvula1 = "1001";
	float caudal1 = 20;
	int intensidad1 = 80;
	//conexion.sobrescribelconsumostest(valvula1, caudal1, intensidad1);
	Connection conn = null;

	try {
	    Class.forName("com.mysql.jdbc.Driver");

	    String urllocal = "jdbc:mysql://" + Irrisoft.config.getHost() + ":"
		    + Irrisoft.config.getPuerto() + "/"
		    + Irrisoft.config.getDb();

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal,
		    Irrisoft.config.getUsuario(), Irrisoft.config.getPass());

	    PreparedStatement sentenciapre1 = conn
		    .prepareStatement("SELECT CAUDAL from modelconsum where CODPROG=?");
	    sentenciapre1.setString(1, "null");
	    ResultSet rs = sentenciapre1.executeQuery();
	    while (rs.next()) {
		float a = rs.getFloat(1);
		assertTrue(a == caudal1);
		logger.warn("Consumo sobreescrito en local");
	    }
	    rs.close();
	    sentenciapre1.close();

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }

	} catch (ClassNotFoundException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	}

    }

    // @Test //Echo y Bien:ACABADO
    public void testrecogeconsummod() {
	ConexionDB conexion = new ConexionDB();
	Irrisoft.window = new Irrisoft();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	int pos = 20;
	String valvula = "1001";
	Irrisoft.config.setIdrasp("7777");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.valvsbt2 = new ListaValvBt2();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	conexion.recogeconsummod(pos, valvula);
	assertNotNull(Irrisoft.window.valvsbt2);

    }

    // @Test //Echo y Bien: ACABADO, hay algun error en el procedimiento.
    public void testinsertaregtemp() {
	ConexionDB conexion = new ConexionDB();
	String codsens = "Pruebatemp";
	Double temp = 35.3;
	String uni = "c";
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusr.getForeground();
	Irrisoft.window.lblstatusr.getText();
	this.conectar();
	Irrisoft.window.volcado.getCon().setConnr(this.getConnr());
	Irrisoft.config = new Conf();
	Irrisoft.config.setIdrasp("7777");
	//conexion.insertaregtemp(codsens, temp, uni);
	Connection conn = null;

	try {
	    Class.forName("com.mysql.jdbc.Driver");

	    String urllocal = "jdbc:mysql://" + "serviciosgis.com" + ":"
		    + "5528" + "/" + "prugestdropper";

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal, "irrigest", "13riego");

	    PreparedStatement sentenciapre = conn
		    .prepareStatement("SELECT TEMPERATURA from t15_regtemp where CODSENSOR=?");
	    sentenciapre.setString(1, "Pruebatemp");
	    ResultSet rs = sentenciapre.executeQuery();
	    while (rs.next()) {
		double a = rs.getDouble(1);
		assertTrue(a == temp);
		logger.warn("Temperatura insertada");
	    }
	    rs.close();
	    sentenciapre.close();

	} catch (SQLException e) {
	    e.getMessage();
	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}
    }

    // @Test //Echo y Bien:ACABADO
    public void testinsertaregviento() {
	ConexionDB conexion = new ConexionDB();
	String codsens = "Pruebaviento";
	Double vel = 22.2;
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusr.getForeground();
	Irrisoft.window.lblstatusr.getText();
	this.conectar();
	Irrisoft.window.volcado.getCon().setConnr(this.getConnr());
	Irrisoft.config = new Conf();
	Irrisoft.config.setIdrasp("7777");
	//conexion.insertaregviento(codsens, vel);

	Connection conn = null;
	try {
	    Class.forName("com.mysql.jdbc.Driver");

	    String urllocal = "jdbc:mysql://" + "serviciosgis.com" + ":"
		    + "5528" + "/" + "prugestdropper";

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal, "irrigest", "13riego");

	    PreparedStatement sentenciapre = conn
		    .prepareStatement("SELECT CATA from t16_regviento where CODSENSOR=?");
	    sentenciapre.setString(1, "Pruebaviento");
	    ResultSet rs = sentenciapre.executeQuery();
	    while (rs.next()) {
		double a = rs.getDouble(1);
		assertTrue(a == vel);
		logger.warn("Viento insertado");
	    }
	    rs.close();
	    sentenciapre.close();

	} catch (SQLException e) {
	    e.getMessage();
	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}
    }

    // @Test //Echo y Bien:ACABADO
    public void testinsertareglluvia() {
	ConexionDB conexion = new ConexionDB();
	String codsens = "PruebaULT";
	int lluvia = 20;
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusr.getForeground();
	Irrisoft.window.lblstatusr.getText();
	this.conectar();
	Irrisoft.window.volcado.getCon().setConnr(this.getConnr());
	Irrisoft.config = new Conf();
	Irrisoft.config.setIdrasp("7777");
	//conexion.insertareglluvia(codsens, lluvia);

	Connection conn = null;
	try {

	    Class.forName("com.mysql.jdbc.Driver");

	    String urllocal = "jdbc:mysql://" + "serviciosgis.com" + ":"
		    + "5528" + "/" + "prugestdropper";

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal, "irrigest", "13riego");
	    PreparedStatement sentenciapre = conn
		    .prepareStatement("SELECT LLUVIA from t17_reglluvia where CODSENSOR=?");
	    sentenciapre.setString(1, "PruebaULT");
	    ResultSet rs = sentenciapre.executeQuery();
	    while (rs.next()) {
		int a = rs.getInt(1);
		assertTrue(a == lluvia);
		logger.warn("Lluvia insertada");
	    }
	    rs.close();
	    sentenciapre.close();

	} catch (SQLException e) {
	    e.getMessage();
	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}
    }

}
