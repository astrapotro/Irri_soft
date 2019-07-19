package volcadoTest;

import static org.junit.Assert.*;

import irrisoftpack.ConexionDB;
import irrisoftpack.Irrisoft;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

import javax.swing.JLabel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import alertaspack.GestorAlertas;

import volcadopack.ConexionVolc;
import volcadopack.Conf;
import volcadopack.Programacion;
import volcadopack.TareaVolc;
import volcadopack.Valvula;


public class ConexionVolcTest {
    
    private static Logger logger = LogManager.getLogger(ConexionVolcTest.class
	    .getName());

    private static GestorAlertas ga;
    
    private TareaVolc tarea;
    private Programacion prog;
    private Valvula valv;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
	ga=GestorAlertas.getInstance();
	
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
	ga=null;
    }
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	Irrisoft.window= new Irrisoft();
	Irrisoft.window.lblstatusr.getForeground();
	Irrisoft.window.lblstatusr.getText();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

     //@Test //Tarea 1,2,4,6 y 3 Bien, 17 y 18 sin probar pero los metodos
    // funcionan correctamente: ACABADO
    public void testTarea() {
	MiConexionVolc miconV = new MiConexionVolc();
//	Irrisoft.window = new Irrisoft();
//	Irrisoft.window.lblstatusr.getForeground();
//	Irrisoft.window.lblstatusr.getText();
	miconV.conectar();
	Irrisoft.window.volcado.setCon(miconV);
	Irrisoft.config.setIdrasp("7777");
	miconV.conectal();
	Connection conn = null;
	try {
	    Class.forName("com.mysql.jdbc.Driver");

	    String urllocal = "jdbc:mysql://" + "serviciosgis.com" + ":"
		    + "5528" + "/" + "prugestdropper";

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal, "irrigest", "13riego");
	    // Tarea 3 = Programa
//	     PreparedStatement sentenciapre =
//	     conn.prepareStatement("INSERT INTO t02_tareaexec(IDTAREAEXEC,CODPROG,NUMERESTA,IDDSTAREA,FCEXEC,VALOR,VALOR2) VALUES (?,?,?,?,?,?,?)");
//	     sentenciapre.setInt(1, 200);
//	     sentenciapre.setString(2, "7777");
//	     sentenciapre.setString(3, "1001");
//	     sentenciapre.setInt(4, 3);
//	     sentenciapre.setDate(5, null);
//	     sentenciapre.setInt(6, 5);
//	     sentenciapre.setInt(7, 10);

	    // //Tarea 1,2,4,6 = Tarea
	    // PreparedStatement sentenciapre =
	    // conn.prepareStatement("INSERT INTO t02_tareaexec(IDTAREAEXEC,CODPROG,NUMERESTA,IDDSTAREA,FCEXEC,VALOR,VALOR2) VALUES (?,?,?,?,?,?,?)");
	    // sentenciapre.setInt(1, 201);
	    // sentenciapre.setString(2, "7777");
	    // sentenciapre.setString(3, "1001");
	    // sentenciapre.setInt(4, 4);
	    // sentenciapre.setDate(5, null);
	    // sentenciapre.setInt(6, 5);
	    // sentenciapre.setInt(7, 10);

//	     sentenciapre.executeUpdate();
//	     sentenciapre.close();
	    //
	} catch (SQLException e) {
	    e.getMessage();
	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}
	//Al cambiar de paquete no puedo acceder al metodo PROTECTED
	//miconV.tarea();
	// Assert de que llega a Programa IDDSTAREA = 3
//	 assertTrue(miconV.entraPrograma);

	// Assert de que llega a Tarea IDDSTAREA = 1,2,4,6
//	assertTrue(miconV.tareaInsertada);
//	assertTrue(miconV.borradaTarea);

    }

     //@Test //Echo y Bien:ACABADO
    public void testinsertarConf_ini_sens() throws NoSuchMethodException,
	    SecurityException, IllegalAccessException,
	    IllegalArgumentException, InvocationTargetException {
	ConexionVolc conV = new ConexionVolc();
	conV.config = new Conf();
	conV.leerconf(conV.getConfig());
	conV.conectal();
	// Variables
	String codprog = "7777";
	int numplac = 5;
	String tipoplac = "Bt2";
	String numsens = "n_sens";
	int numborna = 4;
	String umed = "v_um";
	String usal = "v_us";
	Double rmmin = 2.2;
	Double rmmax = 10.2;
	Double rsmin = 2.5;
	Double rsmax = 10.5;
	Double mumin = 2.7;
	Double mumax = 10.7;
	int freql = 5;
	int freqe = 6;
	int k = 7;
	int errors = 8;
	int errori = 9;
	int tmax = 10;
	String numestp = "nsp";
	String numesta = "nsa";
	boolean salida = true;
	// Metodo privado
	Method metodo = conV.getClass().getDeclaredMethod(
		"insertarConf_ini_sens", String.class, int.class, String.class,
		String.class, int.class, String.class, String.class,
		double.class, double.class, double.class, double.class,
		double.class, double.class, int.class, int.class, int.class,
		int.class, int.class, int.class, String.class, String.class);
	metodo.setAccessible(true);
	boolean output = (boolean) metodo.invoke(conV, codprog, numplac,
		tipoplac, numsens, numborna, umed, usal, rmmin, rmmax, rsmin,
		rsmax, mumin, mumax, freql, freqe, k, errors, errori, tmax,
		numestp, numesta);
	assertTrue(salida == output);
    }

     //@Test//Echo y Bien:ACABADO
    public void testinsertarConf_ini_prog() throws NoSuchMethodException,
	    SecurityException, IllegalAccessException,
	    IllegalArgumentException, InvocationTargetException {
	ConexionVolc conV = new ConexionVolc();
	conV.config = new Conf();
	conV.leerconf(conV.getConfig());
	conV.conectal();
	// Variables
	int configuracion = 1;
	String codigo = "7777";
	String estacion = "1030";
	int deco = 1;
	int maestra = 1;
	int latch = 0;
	int goteo = 0;
	boolean salida = true;
	// Metodo Privado
	Method metodo = conV.getClass().getDeclaredMethod(
		"insertarConf_ini_prog", int.class, String.class, String.class,
		int.class, int.class, int.class, int.class);
	metodo.setAccessible(true);
	boolean output = (boolean) metodo.invoke(conV, configuracion, codigo,
		estacion, deco, maestra, latch, goteo);
	assertTrue(salida == output);
    }

     //@Test //Procedimiento Alamacenado//Echo y Bien:ACABADO
    public void testBorrarTarea() {
	ConexionVolc conV = new ConexionVolc();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusr.getForeground();
	Irrisoft.window.lblstatusr.getText();
	conV.conectar();
	// Irrisoft.window.volcado.getCon();
	Irrisoft.window.volcado.setCon(conV);
	Connection conn = null;

	try {

	    Class.forName("com.mysql.jdbc.Driver");

	    String urllocal = "jdbc:mysql://" + "serviciosgis.com" + ":"
		    + "5528" + "/" + "prugestdropper";

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal, "irrigest", "13riego");
	    PreparedStatement sentenciapre = conn
		    .prepareStatement("INSERT INTO t02_tareaexec (IDTAREAEXEC,CODPROG,NUMERESTA,IDDSTAREA,FCEXEC,VALOR,VALOR2) VALUES (?,?,?,?,?,?,?)");
	    sentenciapre.setInt(1, 50);
	    sentenciapre.setString(2, "7777");
	    sentenciapre.setString(3, "1001");
	    sentenciapre.setInt(4, 8);
	    sentenciapre.setDate(5, null);
	    sentenciapre.setInt(6, 5);
	    sentenciapre.setInt(7, 10);
	    sentenciapre.executeUpdate();
	    sentenciapre.close();

	} catch (SQLException e) {
	    e.getMessage();
	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}

	//TareaVolc tarea = new TareaVolc();
	tarea.setIdtareaexec(50);
	//Al cambiar de paquete no puedo acceder al metodo PROTECTED
	//assertTrue(conV.Borratarea() == true);

    }

     //@Test //Hecho y Bien:ACABADO
    public void testInsertarTarea() {

	ConexionVolc conV = new ConexionVolc();
	conV.config = new Conf();
	conV.leerconf(conV.getConfig());
	conV.conectal();
	tarea.setIdtareaexec(26);
	tarea.setCodprog("500");
	tarea.setCodelecvalv("prueba");
	tarea.setIddstarea(2);
	tarea.setFcexec(null);
	tarea.setValor(3);
	tarea.setValor2(4);
	tarea.setCodtarea("prue");
	tarea.setDstarea("prueba");
	//Al cambiar de paquete no puedo acceder al metodo PROTECTED
	//assertTrue(conV.Insertarea() == true);
    }

    // @Test //Procedimiento Almacenado //Echo y Bien//Assert
    public void testPrograma() {
	ConexionVolc conV = new ConexionVolc();
//	Programacion prog = new Programacion();
//	TareaVolc tarea = new TareaVolc();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusr.getForeground();
	Irrisoft.window.lblstatusr.getText();
	conV.conectar();
	Irrisoft.window.volcado.setCon(conV);
	Irrisoft.config.setIdrasp("7777");
	conV.conectal();
	prog.setIdprogramal(1);
	tarea.setIdtareaexec(4);
	//Al cambiar de paquete no puedo acceder al metodo PROTECTED
	//conV.programa();

    }

//     @Test //Hecho y Bien:ACABADO
    public void testInsertaprog() {

	Date fini = new Date(0);
	Date ffin = new Date(1);
	ConexionVolc conV = new ConexionVolc();
	conV.config = new Conf();
	conV.leerconf(conV.getConfig());
	conV.conectal();
	prog.setIdprograma(320);
	prog.setCodprograma("prueInsPro");
	prog.setDsprograma("pruebaInsProg");
	prog.setFcinicio(fini);
	prog.setFcFin(ffin);
	prog.setActivo("S");
	prog.setCodprog("7777");
	prog.setTipo("s");
	prog.setDial(0);
	prog.setDiam(0);
	prog.setDiax(0);
	prog.setDiaj(0);
	prog.setDiav(0);
	prog.setDias(0);
	prog.setDiad(1);
	prog.setModo("A");
	prog.setModoini("S");
	prog.setPbloque(1);
	prog.setCuota(100);
	prog.setLeido("N");
	prog.setEnmarcha("N");
	//Al cambiar de paquete no puedo acceder al metodo PROTECTED
	//assertTrue(conV.insertaprog() == true);

    }

     //@Test //Procedimiento Almacenado //Hecho y Bien:ACABADO
    public void testBorrarprog() {
	ConexionVolc conV = new ConexionVolc();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusr.getForeground();
	Irrisoft.window.lblstatusr.getText();
	conV.conectar();
	Irrisoft.window.volcado.setCon(conV);
	Connection conn = null;

	try {
	    Class.forName("com.mysql.jdbc.Driver");

	    String urllocal = "jdbc:mysql://" + "serviciosgis.com" + ":"
		    + "5528" + "/" + "prugestdropper";

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal, "irrigest", "13riego");

	    String date = "2014-09-09";
	    java.sql.Date fecha = java.sql.Date.valueOf(date);

	    PreparedStatement sentenciapre = conn
		    .prepareStatement("INSERT INTO t10_programa (IDPROGRAMA,CODPROGRAMA,DSPROGRAMA,FCINICIO,FCFIN,ACTIVO,CODPROG"
			    + "TIPO,DIAL,DIAM,DIAX,DIAJ,DIAV,DIAS,DIAD,MODO,MODOINI,PBLOQUE,CUOTA,LEIDO,ENMARCHA,LLUVIA) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	    sentenciapre.setInt(1, 42);
	    sentenciapre.setString(2, "borrarp");
	    sentenciapre.setString(3, "borrarprog");
	    sentenciapre.setDate(4, fecha);
	    sentenciapre.setDate(5, fecha);
	    sentenciapre.setString(6, "S");
	    sentenciapre.setString(7, "7777");
	    sentenciapre.setString(8, "S");
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
	    sentenciapre.setInt(19, 10);
	    sentenciapre.setString(20, "N");
	    sentenciapre.setString(21, "S");
	    sentenciapre.setInt(22, 10);

	} catch (SQLException e) {
	    e.getMessage();
	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}

	//Programacion prog = new Programacion();
	prog.setIdprograma(42);
	//Al cambiar de paquete no puedo acceder al metodo PROTECTED
	//assertTrue(conV.borraprog() == true);

    }

     //@Test //Hecho y Bien:ACABADO
    public void testInsetardia() {

	Date fecha = new Date(2);
	ConexionVolc conV = new ConexionVolc();
	conV.config = new Conf();
	conV.leerconf(conV.getConfig());
	conV.conectal();
	conV.getConfig().setIdrasp("7777");
	prog.setIdprograma(320);
	prog.setDia(fecha);
	//Al cambiar de paquete no puedo acceder al metodo PROTECTED
	//assertTrue(conV.insertadia() == true);

    }

     //@Test //Hecho y Bien:ACABADO
    public void testInsertarhoras() {
	Time hini = new Time(4);
	Time hfin = new Time(5);
	ConexionVolc conV = new ConexionVolc();
	conV.config = new Conf();
	conV.leerconf(conV.getConfig());
	conV.conectal();
	conV.getConfig().setIdrasp("7777");
	prog.setIdprograma(320);
	prog.setHoraini(hini.toString());
	prog.setHorafin(hfin.toString());
	//Al cambiar de paquete no puedo acceder al metodo PROTECTED
	//assertTrue(conV.insertahoras() == true);

    }

     //@Test //Hecho y Bien.
    public void testInsertarvalv() {
	ConexionVolc conV = new ConexionVolc();
	conV.config = new Conf();
	conV.leerconf(conV.getConfig());
	conV.conectal();
	conV.getConfig().setIdrasp("7777");
	prog.setIdprograma(320);
	valv.setCodelecvalv("200");
	valv.setDuracion(30);
	valv.setBloque(5);
	//Al cambiar de paquete no puedo acceder al metodo PROTECTED
	//assertTrue(conV.insertavalv() == true);

    }

     //@Test //Hecho y Bien:ACABADO
    public void testConectar() {
	ConexionVolc conV = new ConexionVolc();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusr.getForeground();
	Irrisoft.window.lblstatusr.getText();
	conV.conectar();
	assertTrue(conV.conectador);

    }

     //@Test //Hecho y Bien:ACABADO
    public void testConectal() {
	ConexionVolc conV = new ConexionVolc();
	conV.config = new Conf();
	conV.leerconf(conV.getConfig());
	conV.conectal();
	assertTrue(conV.conectadol == true);

    }

     //@Test //Hecho y Bien:ACABADO
    public void testCierrar() {

	ConexionVolc conV = new ConexionVolc();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusr.getForeground();
	Irrisoft.window.lblstatusr.getText();
	conV.conectar();
	conV.cierrar();
	assertFalse(conV.conectador);

    }

     //@Test //Hecho y Bien:ACABADO
    public void testCierral() {
	ConexionVolc conV = new ConexionVolc();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusl.getForeground();
	Irrisoft.window.lblstatusl.getText();
	conV.config = new Conf();
	conV.leerconf(conV.getConfig());
	conV.conectal();
	conV.cierral();
	assertFalse(conV.conectadol);

    }

     //@Test //REVISAR
    public void testCalculaip() {
	MiConexionVolc miconV = new MiConexionVolc();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.ip = new String();
	Irrisoft.window.volcado.setCon(miconV);
	Irrisoft.config.setIdrasp("7777");
	Irrisoft.window.lblstatusr.getForeground();
	Irrisoft.window.lblstatusr.getText();
	miconV.conectar();
	miconV.calculaip();
	//assertTrue(miconV.escrito);

    }

     //@Test // Procedimiento Almacenado //Echo y Bien:ACABADO
    public void testEscribeip() {
	ConexionVolc conV = new ConexionVolc();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusr.getForeground();
	Irrisoft.window.lblstatusr.getText();
	conV.conectar();
	String ipPrueba = "10.0.0.7";
	Irrisoft.window.volcado.setCon(conV);
	Irrisoft.config.setIdrasp("7777");
	Irrisoft.window.ip = ipPrueba;
	//Al cambiar de paquete no puedo acceder al metodo PROTECTED
	//conV.escribeip();
	Connection conn = null;

	try {
	    Class.forName("com.mysql.jdbc.Driver");

	    String urllocal = "jdbc:mysql://" + "serviciosgis.com" + ":"
		    + "5528" + "/" + "prugestdropper";

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal, "irrigest", "13riego");
	    PreparedStatement sentenciapre = conn
		    .prepareStatement("SELECT IPPROG from t03_programador where CODPROG = ?");
	    sentenciapre.setString(1, "7777");
	    ResultSet rs = sentenciapre.executeQuery();
	    while (rs.next()) {
		String ipremota = rs.getString(1);
		assertEquals(ipPrueba, ipremota);
		logger.warn("Ha escrito bien la IP");
	    }

	} catch (SQLException e) {
	    e.getMessage();
	} catch (ClassNotFoundException e) {
	    e.getMessage();
	}

    }

     @Test //Hecho y Bien.
    // Lo unico he modificado el archivo de volcado para acceder la base de
    // datos remota que he creado.
    public void testLeerconf() {
	ConexionVolc conV = new ConexionVolc();
	Irrisoft.config.setIdrasp("7777");
	conV.config = new Conf();
	conV.leerconf(conV.getConfig());

	// Caso Malo
	// assertEquals(conV.getConfig().getIdrasp(), 8888);
	// assertEquals(conV.getConfig().getHostr(), "localhost");
	// assertEquals(conV.getConfig().getPuertor(), 3306);
	// assertEquals(conV.getConfig().getDbr(), "gestdropper");
	// assertEquals(conV.getConfig().getUserr(), "gestor");
	// assertEquals(conV.getConfig().getPassr(), "gestor");
	// assertEquals(conV.getConfig().getHostl(), "localhost");
	// assertEquals(conV.getConfig().getPuertol(), 3306);
	// assertEquals(conV.getConfig().getDbl(), "irrisoft");
	// assertEquals(conV.getConfig().getUsuariol(), "gestor");
	// assertEquals(conV.getConfig().getPassl(), "gestor");
	// assertEquals(conV.getConfig().getTiempo(), 5);

	// Caso Bueno
	assertEquals(conV.getConfig().getIdrasp(), "7777");
	assertEquals(conV.getConfig().getHostr(), "www.serviciosgis.com");
	assertEquals(conV.getConfig().getPuertor(), 5528);
	assertEquals(conV.getConfig().getDbr(), "prugestdropper");
	assertEquals(conV.getConfig().getUserr(), "irrigest");
	assertEquals(conV.getConfig().getPassr(), "13riego");
	assertEquals(conV.getConfig().getHostl(), "localhost");
	assertEquals(conV.getConfig().getPuertol(), 3306);
	assertEquals(conV.getConfig().getDbl(), "irrisoft");
	assertEquals(conV.getConfig().getUsuariol(), "gestor");
	assertEquals(conV.getConfig().getPassl(), "gestor");
	assertEquals(conV.getConfig().getTiempo(), 5);

    }

}
