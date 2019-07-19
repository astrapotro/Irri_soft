package sensoresTest;

import static org.junit.Assert.*;

import java.awt.Panel;
import java.util.LinkedHashSet;

import jssc.SerialPortException;

import irrisoftTest.MiIrrisoft;
import irrisoftTest.MiSerialDriver;
import irrisoftTest.MiSerialPort;
import irrisoftpack.Irrisoft;
import irrisoftpack.SerialDriver;

import org.junit.Test;

import panelespack.HiloTest;
import panelespack.Paneltest;
import sensorespack.HiloCaudalimetro;
import sensorespack.Sensor;

public class HiloCaudalimetroTest {

    // @Test //Echo y Bien:ACABADO
    // Entra en un while, para el caso Irrisoft.window.test = true;
    public void testrun() {
	//Objetos y constructor y Inicialiar Irrisoft
	MiSerialDriver sd = new MiSerialDriver();
	int tipo = 0;
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.test = true;
	MiHiloCaudalimetro hc = new MiHiloCaudalimetro(sd, tipo, tipovalv, sens);
	//Creo puerto serie
	String puerto = "port";
	MiSerialPort serialp = new MiSerialPort(puerto);
	sd.setSerialPort(serialp);
	//Variables
	hc.setVuelta(30);
	hc.setPulsostot(30);
	//Llamada al metodo
	hc.run();
	//Assert
	assertTrue(hc.testea);
	assertTrue(hc.calculaCaudal);
    }

    // @Test //Echo y Bien:ACABADO
    // Entra en un while, para el caso Irrisoft.window.test = false;
    public void testrun1() {
	//Objetos y constructors y Inicializar Irrisoft
	MiSerialDriver sd = new MiSerialDriver();
	int tipo = 5;
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.test = false;
	MiHiloCaudalimetro hc = new MiHiloCaudalimetro(sd, tipo, tipovalv, sens);
	//Creo puerto serie
	String puerto = "port";
	MiSerialPort serialp = new MiSerialPort(puerto);
	sd.setSerialPort(serialp);
	//Llamada al metodo
	hc.run();
	//Assert
	assertTrue(hc.automatic);

    }

    // @Test //Echo y Bien:ACABADO
    public void testautomatico() {
	//Objetos y constructor y Inicializar Irrisoft
	SerialDriver sd = new SerialDriver();
	int tipo = 5;
	int tipovalv = 1;
	Sensor sens = new Sensor();
	MiIrrisoft.window = new MiIrrisoft();
	MiIrrisoft.window.test = false;
	MiHiloCaudalimetro hc = new MiHiloCaudalimetro(sd, tipo, tipovalv, sens);
	//Llamada al metodo
	hc.automatico();
    }

    // @Test //Echo y Bien:ACABADO
    public void testtest() {
	//Objetos, constructor y Inicializar Irrisoft
	SerialDriver sd = new SerialDriver();
	int tipo = 0;
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.paneltest = Paneltest.getInstance();
	MiHiloCaudalimetro hc = new MiHiloCaudalimetro(sd, tipo, tipovalv, sens);
	//Llamada al metodo
	hc.test();
	//Assert
	assertEquals(hc.cuentapulsoslapso(), 1);

    }

    // @Test //Echo y Bien:ACABADO
    // Caso 1: BT2 con 0 o 1 vuelta.
    public void testcuentapulsos() throws SerialPortException {
	//Objetos, constructor y Inicializar Irrisoft
	MiSerialDriver sd = new MiSerialDriver();
	int tipo = 5; // Tipo de placa.
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	HiloCaudalimetro hc = new HiloCaudalimetro(sd, tipo, tipovalv, sens);
	//Creo puerto serie
	String puerto = "port";
	MiSerialPort serialp = new MiSerialPort(puerto);
	sd.setSerialPort(serialp);
	serialp.openPort();
	//Llamada al metodo y Assert
	assertEquals(hc.cuentapulsos(), 2);
    }

    // @Test//Echo y Bien:ACABADO
    // Caso 2: BT2 con 2 vueltas
    public void testcuentapulsos2() throws SerialPortException {
	//Objetos, constructor y Inicializar Irrisoft
	MiSerialDriver sd = new MiSerialDriver();
	int tipo = 5; // Tipo de placa.
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	HiloCaudalimetro hc = new HiloCaudalimetro(sd, tipo, tipovalv, sens);
	//Creo puerto serie
	String puerto = "port";
	MiSerialPort serialp = new MiSerialPort(puerto);
	sd.setSerialPort(serialp);
	serialp.openPort();
	//Variables
	hc.setVuelta(2);
	//Llamada al metodo y Assert
	assertEquals(hc.cuentapulsos(), 2);

    }

    // @Test //Echo y Bien:ACABADO
    // CASO 3: MCI y 0 o 1 vuelta.
    public void testcuentapulsos3() throws SerialPortException {
	//Objetos, constructor y Inicializar Irrisoft
	MiSerialDriver sd = new MiSerialDriver();
	int tipo = 2; // Tipo de placa.
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	HiloCaudalimetro hc = new HiloCaudalimetro(sd, tipo, tipovalv, sens);
	//Creo puerto serie
	String puerto = "port";
	MiSerialPort serialp = new MiSerialPort(puerto);
	sd.setSerialPort(serialp);
	serialp.openPort();
	//Llamada al metodo y Assert
	assertEquals(hc.cuentapulsos(), 131328);

    }

    // @Test //Echo y Bien:ACABADO
    // CASO 4:MCI con 2 vueltas.
    public void testcuentapulsos4() throws SerialPortException {
	//Objetos, constructor y Inicializar Irrisoft
	MiSerialDriver sd = new MiSerialDriver();
	int tipo = 2; // Tipo de placa.
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	HiloCaudalimetro hc = new HiloCaudalimetro(sd, tipo, tipovalv, sens);
	//Creo puerto serie
	String puerto = "port";
	MiSerialPort serialp = new MiSerialPort(puerto);
	sd.setSerialPort(serialp);
	serialp.openPort();
	//Variables
	hc.setVuelta(2);
	//Llamada al metodo y Assert
	assertEquals(hc.cuentapulsos(), 131328);
    }

     //@Test //Echo y Bien:ACABADO
    public void testcuentapulsoslapso() throws SerialPortException {
	//Objetos, constructor y Inicializar Irrisoft
	MiSerialDriver sd = new MiSerialDriver();
	int tipo = 5;
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	MiHiloCaudalimetro hc = new MiHiloCaudalimetro(sd, tipo, tipovalv, sens);
	//Llamada al metodo y Assert
	assertEquals(hc.cuentapulsoslapso(), 1);
    }

     //@Test //REVISAR
    public void testcalculacaudaltest() {
	//Objetos, constructor y Inicializar Irrisoft
	SerialDriver sd = new SerialDriver();
	int tipo = 5;
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.paneltest = Paneltest.getInstance();
	Irrisoft.window.paneltest.HiloTest = new HiloTest(true, tipo);
	HiloCaudalimetro hc = new HiloCaudalimetro(sd, tipo, tipovalv, sens);
	//Llamada al metodo
	hc.calculacaudaltest(tipovalv);
    }

    // @Test //Echo y Bien:ACABADO
    public void testcalculacaudalauto() {
	//Objetos,constructor y Inicializar Irrisoft
	SerialDriver sd = new SerialDriver();
	int tipo = 5;
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.valvsabiertastot = new LinkedHashSet<>();
	HiloCaudalimetro hc = new HiloCaudalimetro(sd, tipo, tipovalv, sens);
	//Variables
	sens.setK(5);
	//Llamada al metodo
	hc.calculacaudalauto();
    }

    // //////////////////////////////////////Getter y Setter

    // @Test //Echo y Bien:ACABADO
    public void testgetTerminar() {
	SerialDriver sd = new SerialDriver();
	int tipo = 0;
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	HiloCaudalimetro hc = new HiloCaudalimetro(sd, tipo, tipovalv, sens);
	//hc.setTerminar(false);
	//assertEquals(hc.getTerminar(), false);

    }

    // @Test //Echo y Bien:ACABADO
    public void testsetTerminar() {
	SerialDriver sd = new SerialDriver();
	int tipo = 0;
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	HiloCaudalimetro hc = new HiloCaudalimetro(sd, tipo, tipovalv, sens);
	//hc.setTerminar(false);
	//assertEquals(hc.getTerminar(), false);

    }

    // @Test //Echo y Bien:ACABADO
    public void testisTest() {
	SerialDriver sd = new SerialDriver();
	int tipo = 0;
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	HiloCaudalimetro hc = new HiloCaudalimetro(sd, tipo, tipovalv, sens);
//	hc.setTest(false);
//	assertEquals(hc.isTest(), false);

    }

    // @Test //Echo y Bien:ACABADO
    public void testsetTest() {
	SerialDriver sd = new SerialDriver();
	int tipo = 0;
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	HiloCaudalimetro hc = new HiloCaudalimetro(sd, tipo, tipovalv, sens);
//	hc.setTest(false);
//	assertEquals(hc.isTest(), false);

    }

    // @Test //Echo y Bien:ACABADO
    public void testgetPulsotot() {
	SerialDriver sd = new SerialDriver();
	int tipo = 0;
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	HiloCaudalimetro hc = new HiloCaudalimetro(sd, tipo, tipovalv, sens);
	hc.setPulsostot(0);
	assertEquals(hc.getPulsostot(), 0);

    }

    // @Test //Echo y Bien:ACABADO
    public void testsetPulsotot() {
	SerialDriver sd = new SerialDriver();
	int tipo = 0;
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	HiloCaudalimetro hc = new HiloCaudalimetro(sd, tipo, tipovalv, sens);
	hc.setPulsostot(1);
	assertEquals(hc.getPulsostot(), 1);

    }

    // @Test //Echo y Bien:ACABADO
    public void testisMci() {
	SerialDriver sd = new SerialDriver();
	int tipo = 0;
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	HiloCaudalimetro hc = new HiloCaudalimetro(sd, tipo, tipovalv, sens);
//	hc.setMci(true);
//	assertEquals(hc.isMci(), true);

    }

    // @Test //Echo y Bien:ACABADO
    public void testsetMci() {
	SerialDriver sd = new SerialDriver();
	int tipo = 0;
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	HiloCaudalimetro hc = new HiloCaudalimetro(sd, tipo, tipovalv, sens);
//	hc.setMci(true);
//	assertEquals(hc.isMci(), true);

    }

    // @Test //Echo y Bien:ACABADO
    public void testgetVuelta() {
	SerialDriver sd = new SerialDriver();
	int tipo = 0;
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	HiloCaudalimetro hc = new HiloCaudalimetro(sd, tipo, tipovalv, sens);
	int vuelta = 1;
	hc.setVuelta(1);
	assertEquals(hc.getVuelta(), vuelta);

    }

    // @Test //Echo y Bien:ACABADO
    public void testsetVuelta() {
	SerialDriver sd = new SerialDriver();
	int tipo = 0;
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	HiloCaudalimetro hc = new HiloCaudalimetro(sd, tipo, tipovalv, sens);
	hc.setVuelta(2);
	assertEquals(hc.getVuelta(), 2);

    }

    // @Test //Echo y Bien:ACABADO
    public void testgetValvsabiertas() {
	SerialDriver sd = new SerialDriver();
	int tipo = 0;
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	HiloCaudalimetro hc = new HiloCaudalimetro(sd, tipo, tipovalv, sens);
	hc.setValvsabiertas(3);
	assertEquals(hc.getValvsabiertas(), 3);

    }

    // @Test //Echo y Bien:ACABADO
    public void testsetValvsabiertas() {
	SerialDriver sd = new SerialDriver();
	int tipo = 0;
	int tipovalv = 1;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	HiloCaudalimetro hc = new HiloCaudalimetro(sd, tipo, tipovalv, sens);
	hc.setValvsabiertas(3);
	assertEquals(hc.getValvsabiertas(), 3);

    }

}
