package sensoresTest;

import jssc.SerialPortException;
import irrisoftTest.MiConexionDB;
import irrisoftTest.MiSerialDriver;
import irrisoftTest.MiSerialPort;
import irrisoftpack.HiloEscucha;
import irrisoftpack.Irrisoft;
import irrisoftpack.SerialDriver;

import org.junit.Test;

import sensorespack.HiloTemperatura;
import sensorespack.Sensor;

public class HiloTemperaturaTest {
    
    
     //@Test //Echo y Bien:ACABADO
     //Entra un while que no acaba
    public void testrun() throws SerialPortException {
	//Inicializar Irrisoft y creacion de objetos
	MiSerialDriver Misd = new MiSerialDriver();
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	MiConexionDB cone = new MiConexionDB();
	Irrisoft.window.hiloescucha = new HiloEscucha();
	Irrisoft.window.hiloescucha.setConnDB(cone);
	HiloTemperatura hTemp = new HiloTemperatura(Misd,sens);
	//Creacion del puerto serie
	String puerto = "port";
	MiSerialPort serialp = new MiSerialPort(puerto);
	Misd.setSerialPort(serialp);
	serialp.openPort();
	//Variables
	sens.setNum_placa(5);
	sens.setNum_sensor("1");
	sens.setUni_med("1");
	sens.setFrec_env(0);
	//Llamada al metodo
	hTemp.run();
    }

}
