package programaTest;

import static org.junit.Assert.*;

import irrisoftTest.MiConexionDB;
import irrisoftTest.MiIrrisoft;
import irrisoftTest.MiSerialDriver;
import irrisoftpack.Irrisoft;
import irrisoftpack.SerialDriver;

import java.awt.Panel;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JTextPane;

import org.joda.time.DateTime;

import org.junit.Test;

import panelespack.PanelBt2;
import programapack.HiloPrograma;
import programapack.Programacion;
import programapack.TareaProg;

import tareapack.TareaManual;
import valvulaspack.ListaValvBt2;
import valvulaspack.ListaValvMci;
import valvulaspack.Valvula;

public class HiloProgramaTest {

    public Valvula valv;

    // @Test //Echo y Bien:ACABADO, Cambiar el metodo adminbloque a PUBLIC
    public void testrun() {
	// Objetos
	TareaProg tar = new TareaProg();
	Programacion pro = new Programacion();
	// Variables y constructor
	int z = 0;
	MiHiloPrograma miHp = new MiHiloPrograma(tar, z, pro);
	tar.setCodelecvalv("1001");
	// Inicializacion de Irrisoft
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.setSerie5(new SerialDriver());
	Irrisoft.window.valvsbt2 = new ListaValvBt2();
	// Llamada al metodo y assert
	miHp.run();
	assertTrue(miHp.administrar);

    }

     //@Test //Echo y Bien:ACABADO //He cambiado en metodo adminbloque a public
    // y
    // creado la clase MiHiloPrograma2
    public void testadminbloque1() {
	// Objetos
	TareaProg tar = new TareaProg();
	int z = 0;
	Programacion prog = new Programacion();
	MiHiloPrograma2 miHp2 = new MiHiloPrograma2(tar, z, prog);
	// Variables
	tar.setTipobloque("S");
	tar.setCodelecvalv("1001");
	tar.setBloque(5);
	tar.setIdtarea(100);
	ArrayList<TareaProg> tareas = new ArrayList<TareaProg>();
	tareas.add(tar);
	prog.setTareasprog(tareas);
	// Llamada al metodo y assert
	// miHp2.adminbloque(tar, z);
	assertTrue(miHp2.eshora);

    }

    // @Test //Echo y Bien:ACABADO //He cambiado en metodo adminbloque a public
    // y
    // creado la clase MiHiloPrograma2
    public void testadminbloque2() {
	// Objetos
	TareaProg tar = new TareaProg();
	Programacion prog = new Programacion();
	// Variables y constructor
	tar.setTipobloque("P");
	tar.setCodelecvalv("1001");
	tar.setBloque(5);
	tar.setIdtarea(100);
	ArrayList<TareaProg> tareas = new ArrayList<TareaProg>();
	tareas.add(tar);
	prog.setTareasprog(tareas);
	int z = 0;
	MiHiloPrograma2 miHp2 = new MiHiloPrograma2(tar, z, prog);
	// Llamada y assert
	// miHp2.adminbloque(tar, z);
	assertTrue(miHp2.eshora);

    }

     //@Test //Echo y Bien,He cambiado el metodo pintainfo a publico y utiliza
    // MiHiloPrograma3
     //CASO 1: Tarea pasada de fecha.
    public void testeslahora() throws ParseException {
	// Objetos
	TareaProg tar = new TareaProg();
	Programacion pro = new Programacion();
	// Variables y constructor
	tar.setCodelecvalv("1001");

	SimpleDateFormat formatterfecha = new SimpleDateFormat("yyyy-MM-dd");
	String fecha = "2014-11-06";
	Date a = formatterfecha.parse(fecha);

	SimpleDateFormat formatterfecha1 = new SimpleDateFormat("yyyy-MM-dd");
	String fecha1 = "2014-11-08";
	Date b = formatterfecha1.parse(fecha1);
	int z = 0;

	tar.setFechaini(a);
	tar.setFechafin(b);
	pro.setIdprograma(150);

	MiHiloPrograma3 miHp3 = new MiHiloPrograma3(tar, z, pro);
	// Inicializacion de Irrisoft
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.textPane = new JTextPane();
	// Llamada al metodo y assert
	miHp3.eslahora(tar);

    }
    
    //@Test  //Echo y Bien: He cambiado pintainfo a PUBLIC y utiliza MiHiloPrograma3
    //Caso 2: Se regaran memos valvulas.
    //Hay que introducir las fechas:
    public void testeslahora2() throws ParseException{
	TareaProg tar = new TareaProg();
	Programacion pro = new Programacion();
	// Variables y constructor
	tar.setCodelecvalv("1001");

	SimpleDateFormat formatterfecha = new SimpleDateFormat("yyyy-MM-dd");
	String fecha = "2014-11-15";
	Date a = formatterfecha.parse(fecha);

	SimpleDateFormat formatterfecha1 = new SimpleDateFormat("yyyy-MM-dd");
	String fecha1 = "2014-11-20";
	Date b = formatterfecha1.parse(fecha1);
	int z = 0;

	tar.setFechaini(a);
	tar.setFechafin(b);
	pro.setIdprograma(150);

	MiHiloPrograma3 miHp3 = new MiHiloPrograma3(tar, z, pro);
	// Inicializacion de Irrisoft
	MiConexionDB cone = new MiConexionDB();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.hiloescucha.setConnDB(cone);
	Irrisoft.window.textPane = new JTextPane();
	// Llamada al metodo y assert
	miHp3.eslahora(tar);
	
    }
    
    //@Test //REVISAR //He pintainfo a PUBLIC y utiliza MiHiloPrograma3
    //CASO 3: Se queda esperando a que llege su hora.
    public void testeslahora3() throws ParseException{
	TareaProg tar = new TareaProg();
	Programacion pro = new Programacion();
	// Variables y constructor
	tar.setCodelecvalv("1001");

	SimpleDateFormat formatterfecha = new SimpleDateFormat("yyyy-MM-dd");
	String fecha = "2014-11-20";
	Date a = formatterfecha.parse(fecha);

	SimpleDateFormat formatterfecha1 = new SimpleDateFormat("yyyy-MM-dd");
	String fecha1 = "2014-11-25";
	Date b = formatterfecha1.parse(fecha1);
	int z = 0;

	tar.setFechaini(a);
	tar.setFechafin(b);
	pro.setIdprograma(150);

	MiHiloPrograma3 miHp3 = new MiHiloPrograma3(tar, z, pro);
	// Inicializacion de Irrisoft
	MiConexionDB cone = new MiConexionDB();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.hiloescucha.setConnDB(cone);
	Irrisoft.window.textPane = new JTextPane();
	// Llamada al metodo y assert
	miHp3.eslahora(tar);
    }

    // @Test //Echo y Bien:ACABADO // He cambiado los metodos abrevalv, regando
    // y
    // cierravalv de private a public y he creado la clase MiHiloPrograma3
    public void testaccionvalv() {
	// Objetos
	TareaProg tar = new TareaProg();
	Programacion pro = new Programacion();
	// Variables y constructor
	tar.setCodelecvalv("1001");
	tar.setBloque(1);
	int z = 0;
	MiHiloPrograma3 miHp3 = new MiHiloPrograma3(tar, z, pro);
	// Llamada al metodo y assert
	miHp3.accionvalv(tar);
	assertTrue(miHp3.abrir);
	assertTrue(miHp3.regar);
	assertTrue(miHp3.cerrar);

    }

    // @Test //Echo y Bien:ACABADO //Cambiar el metodo abrevalv a PUBLIC
    // Falta el assert
    public void testabrevalv() throws NoSuchMethodException, SecurityException,
	    IllegalAccessException, IllegalArgumentException,
	    InvocationTargetException {
	// Objetos
	TareaProg tar = new TareaProg();
	Programacion pro = new Programacion();
	MiSerialDriver sd = new MiSerialDriver();
	MiIrrisoft.window = new MiIrrisoft();
	MiIrrisoft.window.panelbt2 = PanelBt2.getInstance();
	// Variable y constructor
	int z = 0;
	MiHiloPrograma hp = new MiHiloPrograma(tar, z, pro);
	Valvula valv = new Valvula();
	hp.setValv(valv);
	ArrayList<TareaProg> tareasprog = new ArrayList<>();
	tareasprog.add(tar);
	pro.setTareasprog(tareasprog);
	hp.setProg(pro);
	hp.setIndicelista(z);
	// Acceso Privado
	// Method metodo = hp.getClass().getDeclaredMethod("abrevalv",
	// TareaProg.class, SerialDriver.class);
	// metodo.setAccessible(true);
	// metodo.invoke(hp, tar, sd);
	// Acceso Publico
	// hp.abrevalv(tar, sd);

    }

    // @Test //Echo y Bien:ACABADO, Cambiar el metodo cierravalv a PUBLIC y
    // Falta el assert
    public void testcierravalv() throws NoSuchMethodException,
	    SecurityException, IllegalAccessException,
	    IllegalArgumentException, InvocationTargetException {
	// Objetos
	TareaProg tar = new TareaProg();
	Programacion pro = new Programacion();
	MiSerialDriver sd = new MiSerialDriver();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.panelbt2 = PanelBt2.getInstance();
	int z = 0;
	MiHiloPrograma hp = new MiHiloPrograma(tar, z, pro);
	// Variables y constructor
	ArrayList<TareaProg> tareas = new ArrayList<TareaProg>();
	tareas.add(tar);
	pro.setTareasprog(tareas);
	hp.setProg(pro);
	hp.setIndicelista(z);
	tar.setCodelecvalv("1001");
	tar.setTipoplaca(5);

	// Acceso Privado
	// Method metodo = hp.getClass().getDeclaredMethod("cierravalv",
	// TareaProg.class, SerialDriver.class);
	// metodo.setAccessible(true);
	// metodo.invoke(hp, tar, sd);

	// Acceso Publico
	// hp.cierravalv(tar,sd);

    }

     //@Test // REVISAR; 
     //He puesto a PUBLIC cierravalv,regando,calculoduracion.
    public void testregando() {
	// Objetos
	TareaProg tar = new TareaProg();
	Programacion pro = new Programacion();
	// Variables y constructor
	tar.setTipoplaca(5);
	tar.setCodelecvalv("1001");
	Valvula valv = new Valvula();
	valv.setCodelecvalv("1001");
	int z = 0;
	MiHiloPrograma miHp = new MiHiloPrograma(tar, z, pro);
	// Inicializar Irrisoft
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.valvsbt2 = new ListaValvBt2();
	Irrisoft.window.valvsbt2.addvalvbt2(valv);
	Irrisoft.window.valvsbt2.getvalvbt2("1001");
	// Llamada al metodo
	 //miHp.regando(tar);

    }

//     @Test //Echo y Bien:ACABADO
    public void testcalculoduracion() throws NoSuchMethodException,
	    SecurityException, IllegalAccessException,
	    IllegalArgumentException, InvocationTargetException {
	// Objetos
	TareaProg tar = new TareaProg();
	Programacion prog = new Programacion();
	// Variables y constructor
	int input = 65;
	tar.setDuracion(100);
	tar.setCodelecvalv("1001");
	tar.setTiemporegado(30);
	int z = 0;
	HiloPrograma hp = new HiloPrograma(tar, z, prog);
	// Acceso Privado y assert
	Method metodo = hp.getClass().getDeclaredMethod("calculoduracion",
		TareaProg.class);
	metodo.setAccessible(true);
	int output = (int) metodo.invoke(hp, tar);
	assertEquals(input, output);
    }

    // @Test //Echo y Bien:ACABADO // AñadirValvulasAbiertas //Falta el assert
    public void testactestvalv() {
	//Objetos y constructores
	TareaProg tar = new TareaProg();
	Programacion pro = new Programacion();
	MiIrrisoft.window = new MiIrrisoft();
	int z = 0;
	HiloPrograma hp = new HiloPrograma(tar, z, pro);
	//Variables
	tar.setTipoplaca(5);
	tar.setIdtarea(1);
	tar.setIdprog(1);
	boolean accionabrir = true;
	Valvula valv = new Valvula();
	hp.setValv(valv);
	//Llamada al metodo
	hp.actestvalv(tar, accionabrir);

    }

//     @Test//Echo y Bien:ACABADO // QuitarValvulasAbiertas//Falta el assert
    public void testactestvalv1() {
	// Objetos
	TareaProg tar = new TareaProg();
	Programacion pro = new Programacion();
	MiIrrisoft.window = new MiIrrisoft();
	int z = 0;
	HiloPrograma hp = new HiloPrograma(tar, z, pro);
	// Variables
	tar.setTipoplaca(5);
	boolean accionabrir = false;
	// Añado una valvula nueva
	Valvula valv = new Valvula();
	hp.setValv(valv);
	valv.setCodelecvalv("1001");
	// Llamada al metodo
	hp.actestvalv(tar, accionabrir);

    }

     //@Test //Echo y Bien:ACABADO //Falta el assert.
    public void testpintainfo1() throws NoSuchMethodException,
	    SecurityException, IllegalAccessException,
	    IllegalArgumentException, InvocationTargetException {
	// Objetos
	TareaProg tar = new TareaProg();
	Programacion prog = new Programacion();
	// Variables y constructor
	boolean pasado = true;
	String hora = "15:00:00";
	tar.setCodelecvalv("1001");
	tar.setDuracion(60);
	tar.setCuota(60);
	int z = 0;
	HiloPrograma hp = new HiloPrograma(tar, z, prog);
	// Inicializar Irrisoft
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.textPane = new JTextPane();
	// Acceso Privado
	Method metodo = hp.getClass().getDeclaredMethod("pintainfo",
		TareaProg.class, boolean.class, String.class);
	metodo.setAccessible(true);
	metodo.invoke(hp, tar, pasado, hora);

    }

     //@Test //Echo y Bien:ACABADO // Falta el assert
    public void testpintainfo2() throws NoSuchMethodException,
	    SecurityException, IllegalAccessException,
	    IllegalArgumentException, InvocationTargetException {

	TareaProg tar = new TareaProg();
	Programacion prog = new Programacion();
	int z = 0;
	boolean pasado = false;
	String hora = "15:00:00";
	tar.setCodelecvalv("1001");
	tar.setDuracion(60);
	tar.setCuota(60);
	tar.setHoraini(new Time(15, 20, 00));
	HiloPrograma hp = new HiloPrograma(tar, z, prog);
	// Inicializar Irrisoft
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.textPane = new JTextPane();
	Irrisoft.window.frmIrrisoft = new JFrame();
	Method metodo = hp.getClass().getDeclaredMethod("pintainfo",
		TareaProg.class, boolean.class, String.class);
	metodo.setAccessible(true);
	metodo.invoke(hp, tar, pasado, hora);

    }

}
