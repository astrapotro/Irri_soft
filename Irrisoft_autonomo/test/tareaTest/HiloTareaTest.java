package tareaTest;

import static org.junit.Assert.*;

import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import irrisoftTest.MiConexionDB;
import irrisoftTest.MiIrrisoft;
import irrisoftTest.MiSerialDriver;
import irrisoftTest.MiSerialPort;
import irrisoftpack.Conf;
import irrisoftpack.Irrisoft;
import irrisoftpack.SerialDriver;

import org.junit.Before;
import org.junit.Test;

import panelespack.PanelBt2;

import tareapack.HiloTarea;
import tareapack.TareaManual;
import valvulaspack.ListaValvBt2;
import valvulaspack.ListaValvMci;
import valvulaspack.Valvula;

public class HiloTareaTest {

    public SerialDriver sd;
    public Valvula valv;
    public boolean salida = true;

    // @Test //Echo y Bien:ACABADO, Utiliza accionvalv de MiHiloTarea
    public synchronized void testRun() {
	// Creo objetos
	TareaManual tm = new TareaManual();
	// HiloTarea ht = new HiloTarea(tm);
	MiHiloTarea miht = new MiHiloTarea(tm);
	Valvula valv = new Valvula();
	// Inicializo Irrisoft
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.valvsbt2 = new ListaValvBt2();
	Irrisoft.window.valvsbt2.addvalvbt2(valv);
	Irrisoft.window.valvsbt2.addvalvbt2(valv);
	Irrisoft.window.valvsbt2.addvalvbt2(valv);
	Irrisoft.window.valvsbt2.getvalvbt2(1).setCodelecvalv("1001");
	Irrisoft.window.config = new Conf();
	Irrisoft.window.config.setBt2("1001");
	// Defino variables
	tm.setCodelecvalv("1001");
	// Acceso al metodo
	miht.run();
	assertTrue(miht.accionar);

    }

     //@Test//Revisar: llega hasta el ultima llamada
     //Cambiar a PUBLIC gestvalv
    public void testaccionvalv() {
	// Declaro objetos
	Valvula valv = new Valvula();
	TareaManual tm = new TareaManual();
	MiHiloTarea ha = new MiHiloTarea(tm);
	MiSerialDriver sd = new MiSerialDriver();
	// Inicializo Irrisoft
	MiIrrisoft.window = new MiIrrisoft();
	MiConexionDB cone = new MiConexionDB();
	MiIrrisoft.window.hiloescucha.setConnDB(cone);
	MiIrrisoft.window.panelbt2 = PanelBt2.getInstance();
	// Defino variables
	ha.setSerialcon(sd);
	ha.setValvu(valv);
	tm.setDuracion(10);
	valv.setNum_placa(5);
	// Defino paramentros del metodo
	int tipoplaca = 5;
	// Acceso Publico
	assertTrue(ha.accionvalv(Irrisoft.config.getBt2(), tipoplaca));
    }

    // @Test //Echo y Bien:ACABADO, Cambiar metodo gestvalv a PUBLIC
    public void testgestvalv() throws NoSuchMethodException, SecurityException,
	    IllegalAccessException, IllegalArgumentException,
	    InvocationTargetException {
	// Creo objetos
	Valvula valv = new Valvula();
	TareaManual tm = new TareaManual();
	HiloTarea ht = new HiloTarea(tm);
	// Inicializo Irrisoft
	MiIrrisoft.window = new MiIrrisoft();
	MiIrrisoft.window.valvsbt2 = new ListaValvBt2();
	MiIrrisoft.window.valvsbt2.addvalvbt2(valv);
	// Defino parametros del metodo
	int tipo = 5;
	boolean abrir = true;
	// Defino variables
	tm.setCodelecvalv("1001");
	valv.setCodelecvalv("1001");
	tm.setIdtarea(1);
	valv.setTareaasoc(1);
	ht.setValvu(valv);
	// Acceswo Privado
	// Method metodo = ht.getClass().getDeclaredMethod("gestvalv",
	// int.class,boolean.class);
	// metodo.setAccessible(true);
	// metodo.invoke(ht, tipo, abrir);
	// Acceso Publico
	// ht.gestvalv(tipo, abrir);
    }

    // @Test //Echo y Bien:ACABADO, Cambiar metodo gestvalv a PUBLIC
    public void gestvalv1() {
	// Creo Objetos
	Valvula valv = new Valvula();
	TareaManual tm = new TareaManual();
	HiloTarea ht = new HiloTarea(tm);
	// Inicializo Irrisoft
	MiIrrisoft.window = new MiIrrisoft();
	MiIrrisoft.window.valvsbt2 = new ListaValvBt2();
	MiIrrisoft.window.valvsbt2.addvalvbt2(valv);
	// Defino Parametros del Metodo
	int tipo = 5;
	boolean abrir = false;
	// Defino Variables
	tm.setCodelecvalv("1001");
	valv.setCodelecvalv("1001");
	ht.setValvu(valv);
	// Acceso Publico
	// ht.gestvalv(tipo,abrir);
    }

}
