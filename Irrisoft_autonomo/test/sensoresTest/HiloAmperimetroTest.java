package sensoresTest;

import static org.junit.Assert.*;

import java.awt.Panel;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import jssc.SerialPortException;

import irrisoftTest.MiConexionDB;
import irrisoftTest.MiIrrisoft;
import irrisoftTest.MiSerialDriver;
import irrisoftTest.MiSerialPort;
import irrisoftpack.Irrisoft;
import irrisoftpack.SerialDriver;

import org.junit.Test;

import panelespack.Paneltest;
import sensorespack.HiloAmperimetro;
import sensorespack.Sensor;
import valvulaspack.Valvula;

public class HiloAmperimetroTest {

    // @Test //Echo y Bien:ACABADO
    // Entra en un while que no acaba.
    public void testrun1() throws SerialPortException {
	//Objetos, constructor y Inicializar Irrisoft
	MiSerialDriver sd = new MiSerialDriver();
	Sensor sens = new Sensor();
	int tipo = 5;
	MiIrrisoft.window = new MiIrrisoft();
	MiIrrisoft.window.test = true;
	MiHiloAmperimetro hamp = new MiHiloAmperimetro(sd, sens, tipo);
	//Creo puerto serie
	String puerto = "Port";
	MiSerialPort serialp = new MiSerialPort(puerto);
	sd.setSerialPort(serialp);
	serialp.openPort();
	//Llamada al metodo
	hamp.run();

    }

    // @Test //Echo y Bien:ACABADO
    // Entra en un while que no acaba.
    public void testrun2() throws SerialPortException {
	//Objetos, constructos y Inicializar Irrisoft
	MiSerialDriver sd = new MiSerialDriver();
	Sensor sens = new Sensor();
	int tipo = 5;
	MiIrrisoft.window = new MiIrrisoft();
	MiIrrisoft.window.test = false;
	MiHiloAmperimetro hamp = new MiHiloAmperimetro(sd, sens, tipo);
	//Creo puerto serie
	String puerto = "port";
	MiSerialPort serialp = new MiSerialPort(puerto);
	sd.setSerialPort(serialp);
	serialp.openPort();
	//Llamada al metodo
	hamp.run();
    }

    // @Test //Echo y Bien:ACABADO
    public void testautomatico() {
	//Obejtos, constructor y Inicializar Irrisoft
	SerialDriver sd = new SerialDriver();
	Sensor sens = new Sensor();
	int tipo = 0;
	Irrisoft.window = new Irrisoft();
	MiHiloAmperimetro hamp = new MiHiloAmperimetro(sd, sens, tipo);
	//Variables
	sens.setNum_sensor("1");
	sens.setTipo_placa("MCI");
	sens.setNum_placa(1);
	//Llamada al metodo
	hamp.automatico();
	//Assert
	assertTrue(hamp.calculaAuto);
    }

     @Test //Echo y Bien:REVISAR
    public void testcalculaintauto() throws SerialPortException {
	//Objetos, constructor y Inicializar Irrisoft
	MiSerialDriver sd = new MiSerialDriver();
	Sensor sens = new Sensor();
	int tipo = 5;
	Valvula val = new Valvula();
	MiIrrisoft.window = new MiIrrisoft();
	MiIrrisoft.window.test = false;
	HiloAmperimetro hamp = new HiloAmperimetro(sd, sens, tipo);
	//Creo puerto serie
	String puerto = "port";
	MiSerialPort serialp = new MiSerialPort(puerto);
	sd.setSerialPort(serialp);
	serialp.openPort();
	//Llamada al metodo
	hamp.calculaintauto();

    }

    // @Test //Echo y Bien: Caso de que Irrisoft.window.test = true;
    public void testcalculaintauto1() {
	//Objetos, constructor y Inicializar Irrisoft
	SerialDriver sd = new MiSerialDriver();
	Sensor sens = new Sensor();
	int tipo = 5;
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.test = true;
	HiloAmperimetro hamp = new HiloAmperimetro(sd, sens, tipo);
	//Llamada al metodo
	hamp.calculaintauto();
	//Assert
	assertTrue(Irrisoft.window.test);
    }

    // @Test //Echo y Bien:ACABADO //Caso1: Que no haya Caudalimetro.
    public void testtest1() {
	//Objetos, constructor y Inicializar Irrisoft
	SerialDriver sd = new SerialDriver();
	Sensor sens = new Sensor();
	int tipo = 5;
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.haycaudalimetro = false;
	MiHiloAmperimetro hamp = new MiHiloAmperimetro(sd, sens, tipo);
	//Variables
	sens.setNum_placa(1);
	//Llamada al metodo
	hamp.test();
    }

    // @Test //Echo y Bien:ACABADO
    // Caso2: Se queda esperando en el wait hasta que le notifiquen.
    public void testtest2() {
	//Objetos, constructor y Inicializar Irrisoft
	SerialDriver sd = new SerialDriver();
	Sensor sens = new Sensor();
	int tipo = 5;
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.haycaudalimetro = true;
	HiloAmperimetro hamp = new HiloAmperimetro(sd, sens, tipo);
	//Variables
	sens.setNum_placa(1);
	Thread hilo1 = new Thread();
	sens.setHilosens(hilo1);
	//Llamada al metodo
	hamp.test();
    }

    // @Test //Echo y Bien:ACABADO
    // Va al metodo "sobrescribeconsum".
    // Cambio a PUBLIC el metodo "sobrescribeconsum".
    public void testcalculaintest1() throws SerialPortException {
	//Objetos, constructor y Inicializar Irrisoft
	MiSerialDriver sd = new MiSerialDriver();
	Sensor sens = new Sensor();
	int tipo = 5;
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.paneltest = Paneltest.getInstance();
	MiHiloAmperimetro hamp = new MiHiloAmperimetro(sd, sens, tipo);
	//Creo puerto serie
	String puerto = "port";
	MiSerialPort serialp = new MiSerialPort(puerto);
	sd.setSerialPort(serialp);
	serialp.openPort();
	//Variables
	sens.setValvula(1001);
	//Llamada al metodo
	hamp.calculaintest();
	//Assert
	assertTrue(hamp.sobrEscrito);
    }

    // @Test //Echo y Bien:ACABADO
    // Va al metodo "escribeconsum"
    public void testcalculaintest2() throws SerialPortException {
	//Objetos, constructor y Inicializar Irrisoft
	MiSerialDriver sd = new MiSerialDriver();
	Sensor sens = new Sensor();
	int tipo = 5;
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.paneltest = Paneltest.getInstance();
	Irrisoft.window.inicial = true;
	MiHiloAmperimetro hamp = new MiHiloAmperimetro(sd, sens, tipo);
	//Creo puerto serie
	String puerto = "port";
	MiSerialPort serialp = new MiSerialPort(puerto);
	sd.setSerialPort(serialp);
	serialp.openPort();
	//Variables
	sens.setValvula(1001);
	//Llamada al metodo
	hamp.calculaintest();
	//Assert
	assertTrue(hamp.escrito);

    }

    // @Test //Echo y Bien:ACABADO
    public void testescribeconsum() {
	//Objetos, constructor y Inicializar Irrisoft
	SerialDriver sd = new SerialDriver();
	Sensor sens = new Sensor();
	int tipo = 5;
	Irrisoft.window = new Irrisoft();
	MiConexionDB cone = new MiConexionDB();
	MiIrrisoft.window.setConn(cone);
	HiloAmperimetro hamp = new HiloAmperimetro(sd, sens, tipo);
	//Variables
	sens.setValvula(1001);
	sens.setLecturacau(1);
	String valvula = "1001";
	//Llamada al metodo
	hamp.escribeconsum(valvula);

    }

    // @Test //Echo y Bien:ACABADO
    // Hay que poner el metodo sobrescribeconsum en PUBLIC
    public void testsobrescribeconsum() {
	//Objetos, constructor y Inicializar Irrisoft
	SerialDriver sd = new SerialDriver();
	Sensor sens = new Sensor();
	int tipo = 5;
	MiIrrisoft.window = new MiIrrisoft();
	MiConexionDB cone = new MiConexionDB();
	MiIrrisoft.window.setConn(cone);
	HiloAmperimetro hamp = new HiloAmperimetro(sd, sens, tipo);
	//Variables
	sens.setValvula(1001);
	sens.setLecturacau(1);
	String valvula = "1001";
	// Llamada al metodo
	// hamp.sobrescribeconsum(valvula);
    }

    // //////////////////////////////////////////Getter y Setter

    // @Test //Echo y Bien:ACABADO
    public void testgetTerminar() {
	SerialDriver sd = new SerialDriver();
	Sensor sens = new Sensor();
	int tipo = 0;
	Irrisoft.window = new Irrisoft();
	HiloAmperimetro ha = new HiloAmperimetro(sd, sens, tipo);
	ha.setTerminar(false);
	assertTrue(ha.getTerminar() == false);

    }

    // @Test //Echo y Bien:ACABADO
    public void testsetTerminar() {
	SerialDriver sd = new SerialDriver();
	Sensor sens = new Sensor();
	int tipo = 0;
	Irrisoft.window = new Irrisoft();
	HiloAmperimetro ha = new HiloAmperimetro(sd, sens, tipo);
	ha.setTerminar(false);
	assertTrue(ha.getTerminar() == false);

    }

    // @Test //Echo y Bien:ACABADO
    public void testisTest() {
	SerialDriver sd = new SerialDriver();
	Sensor sens = new Sensor();
	int tipo = 0;
	Irrisoft.window = new Irrisoft();
	HiloAmperimetro ha = new HiloAmperimetro(sd, sens, tipo);
//	ha.setTest(false);
//	assertTrue(ha.isTest() == false);
    }

    // @Test //Echo y Bien:ACABADO
    public void testsetTest() {
	SerialDriver sd = new SerialDriver();
	Sensor sens = new Sensor();
	int tipo = 0;
	Irrisoft.window = new Irrisoft();
	HiloAmperimetro ha = new HiloAmperimetro(sd, sens, tipo);
//	ha.setTest(false);
//	assertTrue(ha.isTest() == false);
    }

    // @Test //Echo y Bien:ACABADO
    public void testisMci() {
	SerialDriver sd = new SerialDriver();
	Sensor sens = new Sensor();
	int tipo = 0;
	Irrisoft.window = new Irrisoft();
	HiloAmperimetro ha = new HiloAmperimetro(sd, sens, tipo);
	ha.setMci(false);
	assertTrue(ha.isMci() == false);
    }

    // @Test //Echo y Bien:ACABADO
    public void testsetMci() {
	SerialDriver sd = new SerialDriver();
	Sensor sens = new Sensor();
	int tipo = 0;
	Irrisoft.window = new Irrisoft();
	HiloAmperimetro ha = new HiloAmperimetro(sd, sens, tipo);
	ha.setMci(false);
	assertTrue(ha.isMci() == false);
    }

    // @Test //Echo y Bien:ACABADO
    public void testgetValvsabiertas() {
	SerialDriver sd = new SerialDriver();
	Sensor sens = new Sensor();
	int tipo = 0;
	Irrisoft.window = new Irrisoft();
	HiloAmperimetro ha = new HiloAmperimetro(sd, sens, tipo);
	ha.setValvsabiertas(4);
	assertTrue(ha.getValvsabiertas() == 4);

    }

    // @Test //Echo y Bien:ACABADO
    public void testsetValvsabiertas() {
	SerialDriver sd = new SerialDriver();
	Sensor sens = new Sensor();
	int tipo = 0;
	Irrisoft.window = new Irrisoft();
	HiloAmperimetro ha = new HiloAmperimetro(sd, sens, tipo);
	ha.setValvsabiertas(4);
	assertTrue(ha.getValvsabiertas() == 4);

    }

}
