package irrisoftTest;

import static org.junit.Assert.*;

import irrisoftpack.Irrisoft;
import irrisoftpack.Semaforo;
import irrisoftpack.SerialDriver;

import java.awt.Panel;

import javax.swing.JFrame;

import jssc.SerialPort;
import jssc.SerialPortException;

import org.junit.Test;

import panelespack.PanelBt2;
import panelespack.Paneltest;

import sensorespack.Sensor;
import valvulaspack.ListaValvBt2;
import valvulaspack.Valvula;

public class SerialDriverTest {

    //@Test //Echo y Bien:ACABADO
    public void testconectaserial() throws SerialPortException {
	// Creo Objeto
	MiSerialDriver sd1 = new MiSerialDriver();
	// Defino puerto serie
	String puerto = "ftdi";
	MiSerialPort serial = new MiSerialPort(puerto);
	sd1.setSerialPort(serial);
	// Defino parametros del metodo
	int tipo = 5;
	sd1.conectaserial(tipo);
	//assert
	//assertTrue(sd1.conectado);
	assertTrue(sd1.semaforosoltar);

    }

    //@Test //Echo y Bien:ACABADO
    public void testdesconectarserial() throws SerialPortException {
	// Creo objeto
	MiSerialDriver sd1 = new MiSerialDriver();
	// Defino puerto serie
	String puerto = ("port");
	MiSerialPort serial = new MiSerialPort(puerto);
	sd1.setSerialPort(serial);
	serial.openPort();
	// Defino parametros del metodo
	int tipo = 5;
	sd1.desconectaserial(tipo);
	assertFalse(sd1.conectado);
    }

    // @Test //Echo y Bien:ACABADO
    public void testabrevalv() {
	// Creo Objetos
	MiSerialDriver sd1 = new MiSerialDriver();
	Valvula valv = new Valvula();
	// Inicializo Variables
	valv.setNum_placa(5);
	valv.setCodelecvalv("1001");
	valv.setAbierta(false);
	// Defino puerto serie
	String puerto = "port";
	MiSerialPort serial = new MiSerialPort(puerto);
	sd1.setSerialPort(serial);
	assertTrue(sd1.abrevalv(valv));

    }

     //@Test //Echo y Bien:ACABADO
    //CASO de valvula abierta
    public void testcierravalv1() {
	// Creo Objetos
	MiSerialDriver sd1 = new MiSerialDriver();
	Valvula valv = new Valvula();
	// Inicializo Irrisoft
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.valvsbt2 = new ListaValvBt2();
	Irrisoft.window.valvsbt2.addvalvbt2(valv);
	valv.setCodelecvalv("1001");
	valv.setAbierta(true);
	// Defino parametros del metodo
	String codelec = "1001";
	int tipo = 5;
	assertTrue(sd1.cierravalv(codelec, tipo));

    }

     //@Test// Echo y Bien:ACABADO
    //CASO de valvula no abierta
    public void testcierravalv2() {
	// Creo Objetos
	MiSerialDriver sd1 = new MiSerialDriver();
	Valvula valv = new Valvula();
	// Inicializo Irrisoft
	Irrisoft.window = new Irrisoft();
	MiConexionDB cone = new MiConexionDB();
	Irrisoft.window.hiloescucha.setConnDB(cone);
	Irrisoft.window.valvsbt2 = new ListaValvBt2();
	Irrisoft.window.valvsbt2.addvalvbt2(valv);
	// Defino variables
	valv.setCodelecvalv("1001");
	valv.setAbierta(false);
	// Defino parametros del metodo
	String codelec = "1001";
	int tipo = 5;
	// Acceso Publico
	assertFalse(sd1.cierravalv(codelec, tipo));

    }

    //@Test//Echo y bien:ACABADO
    public void testinterruptormci() {
	// Creo Objetos
	Valvula valv = new Valvula();
	MiSerialDriver sd = new MiSerialDriver();
	// Inicializo puerto serie
	String puerto = "port";
	MiSerialPort serialp = new MiSerialPort(puerto);
	sd.setSerialPort(serialp);
	// Defino Variables
	valv.setCodelecvalv("101");
	valv.setNum_placa(1);
	// Defino parametros del metodo
	int comando = 1;
	// Acceso Publico
	//sd.interruptormci(comando, valv);
	assertTrue(sd.semaforosoltar);
    }

     //@Test //Echo y Bien:ACABADO
    public void testreset() {
	// Creo objeto
	MiSerialDriver sd1 = new MiSerialDriver();
	// Defino puerto serie
	String puerto = "port";
	MiSerialPort serial = new MiSerialPort(puerto);
	sd1.setSerialPort(serial);
	// Inicializo Irrisoft
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.panelbt2 = PanelBt2.getInstance();
	// Defino parametros del metodo
	int tipo = 5;
	sd1.reset(tipo);
	// assert
	assertTrue(sd1.semaforosoltar);

    }

     //@Test // Echo y Bien:ACABADO
    public void testresetinsemaforo() throws SerialPortException {
	// Creo Objeto
	MiSerialDriver sd1 = new MiSerialDriver();
	// Defino puerto serie
	String puerto = "port";
	MiSerialPort serial = new MiSerialPort(puerto);
	sd1.setSerialPort(serial);
	serial.openPort();
	// Defino parametro del metodo
	int tipo = 5;
	sd1.resetsinsemaforo(tipo);
	// assert
	assertTrue(serial.isOpened());
    }

     //@Test //Echo y Bien:ACABADO
    public void testactuabt2() {
	// Creo Objeto
	MiSerialDriver2 sd2 = new MiSerialDriver2();
	// Defino puerto serie
	String puerto = "port";
	MiSerialPort serial = new MiSerialPort(puerto);
	sd2.setSerialPort(serial);
	// Inicializo Irrisoft
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.panelbt2 = PanelBt2.getInstance();
	// Defino parametros del metodo
	String codelec = "1001";
	int deco = 10;
	boolean abrir = false;
	int tipo = 5;
	int maestra = 1;
	// Acceso y assert
	//Al cambiarlo de paquetes , es un metodo protected y no puedo acceder
	//assertTrue(sd2.actuabt2(codelec, deco, abrir, tipo, maestra));
    }

    // @Test //REVISAR LARGISIMO PERO ESTA BIEN
    public void testleeresp() throws SerialPortException {
	// Creo Objeto
	SerialDriver sd = new SerialDriver();
	// Defino puerto serie
	String puerto = "port";
	MiSerialPort serial = new MiSerialPort(puerto);
	sd.setSerialPort(serial);
	// Inicializo Irrisoft
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.panelbt2 = PanelBt2.getInstance();
//	//Instancio el paneltest para que aparezca
//	Irrisoft.window.paneltest = Paneltest.getInstance();
	// Defino parametros del metodo
	boolean consum = false;
	int tipo = 5;
	boolean abrir = true;
	String codelecvalv = "350";
	int maes = 0;
	// Acceso y assert
	assertFalse(sd.leeresp(consum, tipo, abrir, codelecvalv, maes));

    }

     //@Test //Echo y Bien:ACABADO
    public void testconsultconsum() throws SerialPortException {
	// Creo Objetos
	SerialDriver sd = new SerialDriver();
	Sensor sen = new Sensor();
	// Defino puerto serie
	String puerto = "ACM";
	MiSerialPort serial = new MiSerialPort(puerto);
	sd.setSerialPort(serial);
	// Inicializo variables
	sen.setNum_borna(5);
	// Acceso
	sd.consultconsum(serial, sen);
	//AL cambiar de paquete, es un atributo protected y no puedo acceder.
	//assertFalse(sd.respuestalost);

    }

     //@Test //Echo y Bien:ACABADO
    public void testreconecta() {
	// Creo objeto
	MiSerialDriver3 sd3 = new MiSerialDriver3();
	// Defino parametros del metodo
	int tipop = 5;
	String puerto = "port";
	MiSerialPort serial = new MiSerialPort(puerto);
	sd3.setSerialPort(serial);
	// Acceso
	sd3.purga_puerto(tipop, puerto);
	// assert
	assertTrue(sd3.reseteado);

    }

     //@Test //Echo y Bien:
     //Probrebla con la version, el metodo subString da algun error
    public void testactivabt() throws SerialPortException {
	// Creo objetos
	Sensor sens = new Sensor();
	MiSerialDriver2 sd2 = new MiSerialDriver2();
	// Defino puerto serie
	String puerto = "port";
	MiSerialPort serialp = new MiSerialPort(puerto);
	sd2.setSerialPort(serialp);
	serialp.openPort();
	// Acceso
	sd2.activabt(serialp, sens);
	// assert
	assertTrue(sd2.semaforosoltar2);
    }

     //@Test //Echo y Bien:ACABADO
    public void testcualesabiertasbt() {
	// Creo Objeto
	MiSerialDriver2 sd2 = new MiSerialDriver2();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.frmIrrisoft = new JFrame();
	// Defino puerto serie
	String puerto = "port";
	MiSerialPort serial = new MiSerialPort(puerto);
	sd2.setSerialPort(serial);
	// Defino parametros del metodo
	int tipo = 5;
	// Acceso
	sd2.cualesabiertasbt(serial, tipo);
	// assert
	assertTrue(sd2.semaforosoltar2);
    }

     //@Test
    // Echo y Bien:ACABADO
    public void testcerrarAbiertasAlInicio() throws SerialPortException {
	// Creo objeto
	MiSerialDriver2 sd2 = new MiSerialDriver2();
	// Defino puerto serie
	String puerto = "port";
	MiSerialPort serial = new MiSerialPort(puerto);
	sd2.setSerialPort(serial);
	serial.openPort();
	// Defino parametros del metodo
	int tipo = 5;
	// Acceso
	sd2.cerrarAbiertasAlInicio(serial, tipo);
	// assert
	assertTrue(sd2.semaforosoltar2);

    }

    // @Test //Echo y Bien:ACABADO
    public void testcogesemaforo() {
	// Creo Objeto
	SerialDriver sd = new SerialDriver();
	// Inicializo Irrisoft
	Irrisoft.window = new Irrisoft();
	// Inicializo variables
	int d = 2;
	Irrisoft.window.semaforobt2 = new Semaforo(d);
	// Defino parametros del metodo
	int tipo = 5;
	// Acceso
	sd.cogesemaforo(tipo);

    }

    // @Test //Echo y Bien:ACABADO
    public void testsueltasemaforo() {
	// Creo objetos
	SerialDriver sd = new SerialDriver();
	// Inicializo Irrisoft
	Irrisoft.window = new Irrisoft();
	// Inicializo variables
	int d = 2;
	Irrisoft.window.semaforobt2 = new Semaforo(d);
	// Defino parametros del metodo
	int tipo = 5;
	// Acceso
	sd.sueltasemaforo(tipo);
    }

}
