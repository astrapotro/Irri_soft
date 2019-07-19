package sensoresTest;

import static org.junit.Assert.*;

import java.awt.Panel;
import java.util.ArrayList;

import jssc.SerialPortException;

import irrisoftTest.MiSerialDriver;
import irrisoftTest.MiSerialPort;
import irrisoftpack.Irrisoft;
import irrisoftpack.SerialDriver;

import org.junit.Test;

import panelespack.PanelBt2;
import sensorespack.HiloHumedadSuelo;
import sensorespack.Sensor;

import valvulaspack.ListaValvBt2;
import valvulaspack.Valvula;

public class HiloHumedadSueloTest {
    
    
    //@Test //Echo y Bien: ACABADO
    //Entra en un while que no finaliza.
    public void testrun1(){
	//Objetos y constructor
	SerialDriver sd = new SerialDriver();
	int tipo = 5;
	Sensor sens = new Sensor();
	MiHiloHumedadSuelo hhs = new MiHiloHumedadSuelo(sd, tipo, sens);
	//Llamada al metodo
	hhs.run();
	//Assert
	assertTrue(hhs.humedadBT);
    }
    
    //@Test //Echo y Bien:ACABADO
    //Poner el metodo leehumedadgon a PUBLIC
    //Entra en un while que no finaliza.
    public void testrun2(){
	//Creo objetos y constructor
	SerialDriver sd = new SerialDriver();
	int tipo = 3;
	Sensor sens = new Sensor();
	MiHiloHumedadSuelo hhs = new MiHiloHumedadSuelo(sd, tipo, sens);
	//Llamada al metodo
	hhs.run();
	//Assert
	assertTrue(hhs.humedadGON);
    }
    
//    @Test //Echo y Bien:ACABADO
    //Hay que poner el metodo leehumedadgon y interruptor a PUBLIC
    public void testleehumedadgon() throws SerialPortException{
	//Creo objetos y constructor
	MiSerialDriver sd = new MiSerialDriver();
	int tipo = 5;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	MiHiloHumedadSuelo hhs = new MiHiloHumedadSuelo(sd, tipo, sens);
	//Creo puerto serie
	String puerto = "port";
	MiSerialPort serialp = new MiSerialPort(puerto);
	sd.setSerialPort(serialp);
	serialp.openPort();
	//Valores para la variable medida
	sens.setRang_med_max(5);
	sens.setRang_med_min(2);
	sens.setRang_sal_max(5);
	sens.setRang_sal_min(2);
	sens.setMed_umbral_min(2);
	sens.setMed_umbral_max(2.5);
	sens.setT_max_riego(1);
	//Llamada al metodo
	//hhs.leehumedadgon();
	//Assert
	assertTrue(hhs.interrupt);
    }

//    @Test  //Echo y Bien:ACABADO
    //Poner el metodo interruptor a PUBLIC
    //CASO 1: La humedad esta en el rango adecuado
    public void testleehumedad1() throws SerialPortException {
	//Objetos y Constructor
	MiSerialDriver msd = new MiSerialDriver();
	int tipo = 5;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	MiHiloHumedadSuelo hhs = new MiHiloHumedadSuelo(msd, tipo, sens);
	//Creo puerto serie
	String puerto = "port";
	MiSerialPort serialp = new MiSerialPort(puerto);
	msd.setSerialPort(serialp);
	serialp.openPort();
	//Llamada al metodo
	hhs.leehumedadbt();

    }
    
    //@Test  //Echo y Bien:ACABADO
    //Poner el metodo interruptor a PUBLIC
    //CASO 2: TiempoRegado > 20
    public void testleehumedad2(){
	//Objeto y constructor
	MiSerialDriver sd = new MiSerialDriver();
	int tipo = 5;
	Sensor sens = new Sensor();
	MiHiloHumedadSuelo hhs = new MiHiloHumedadSuelo(sd, tipo, sens);
	//Creo puerto serie
	String puerto = "port";
	MiSerialPort serialp = new MiSerialPort(puerto);
	sd.setSerialPort(serialp);
	//Variables
	sens.setMed_umbral_min(1.2);
	sens.setMed_umbral_max(2.2);
	sens.setT_max_riego(21);
	//Llamada al metodo
	hhs.leehumedadbt();
	//Assert
	assertTrue(hhs.interrupt);
    }
    
    //@Test  //Echo y Bien:ACABADO
    //Poner el metodo interruptor a PUBLIC
    //Caso 2: TiempoRegado <20
    public void testleehumedad3() throws SerialPortException{
	//Objetos y constructor
	MiSerialDriver sd = new MiSerialDriver();
	int tipo = 5;
	Sensor sens = new Sensor();
	MiHiloHumedadSuelo hhs = new MiHiloHumedadSuelo(sd, tipo, sens);
	//Creo puerto serie
	String puerto = "port";
	MiSerialPort serialp = new MiSerialPort(puerto);
	sd.setSerialPort(serialp);
	serialp.openPort();
	//Variables
	sens.setMed_umbral_max(2.2);
	sens.setMed_umbral_min(1.2);
	sens.setT_max_riego(15);
	//Llamada al metodo
	hhs.leehumedadbt();
	//Assert
	assertTrue(hhs.interrupt);
    }
    
//    @Test  //Echo y Bien:ACABADO
    //Poner el metodo interruptor a PUBLIC
    public void testinterruptor(){
	//Objetos y constructor
	SerialDriver sd = new SerialDriver();
	int tipo = 5;
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.panelbt2 = PanelBt2.getInstance();
	Irrisoft.window.valvsbt2 = new ListaValvBt2();
	HiloHumedadSuelo hhs = new HiloHumedadSuelo(sd, sens);
	//Variables
	int accion = 1;
	ArrayList<Integer>valvs = new ArrayList<>();
	valvs.add(1001);
	sens.setValvsassoc(valvs);
	//Llamada al metodo
	//hhs.interruptor(accion);
    }

}
