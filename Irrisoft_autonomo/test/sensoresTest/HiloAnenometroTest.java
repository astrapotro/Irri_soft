package sensoresTest;

import jssc.SerialPortException;
import irrisoftTest.MiConexionDB;
import irrisoftTest.MiIrrisoft;
import irrisoftTest.MiSerialDriver;
import irrisoftTest.MiSerialPort;
import irrisoftpack.HiloEscucha;
import irrisoftpack.SerialDriver;

import org.junit.Test;

import sensorespack.HiloAnemometro;
import sensorespack.Sensor;

public class HiloAnenometroTest {
    
    //@Test//Echo y Bien:ACABADO
    //Entra un while que no acaba
    //He modificado el metodo "readBytes" de MiSerialPort
    public void testrun() throws SerialPortException{
	//Creo objetos e incializo Irrisoft
	MiSerialDriver sd = new MiSerialDriver();
	Sensor sens = new Sensor();
	int tipoplaca = 5;
	MiIrrisoft.window = new MiIrrisoft();
	MiConexionDB cone = new MiConexionDB();
	MiIrrisoft.window.hiloescucha = new HiloEscucha();
	MiIrrisoft.window.hiloescucha.setConnDB(cone);
	HiloAnemometro ane = new HiloAnemometro(sd, sens);
	//Creo puerto serie
	String puerto = "port";
	MiSerialPort serialp = new MiSerialPort(puerto);
	sd.setSerialPort(serialp);
	serialp.openPort();
	//Variables
	sens.setNum_borna(1);
	sens.setNum_sensor("1");
	//Llamada al metodo
	ane.run();
    }

}
