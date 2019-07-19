package sensoresTest;

import jssc.SerialPortException;
import irrisoftTest.MiConexionDB;
import irrisoftTest.MiIrrisoft;
import irrisoftTest.MiSerialDriver;
import irrisoftTest.MiSerialPort;
import irrisoftpack.HiloEscucha;
import irrisoftpack.Irrisoft;
import irrisoftpack.SerialDriver;

import org.junit.Test;

import sensorespack.HiloPluviometro;
import sensorespack.Sensor;

public class HiloPluviometroTest {
    
    @Test //Echo y Bien:ACABADO
    //Entra en un while que no acacba
    public void testrun() throws SerialPortException{
	//Inicialiazo Irrisoft y objetos
	MiSerialDriver Misd = new MiSerialDriver();
	int tipo = 5;
	Sensor sens = new Sensor();
	MiIrrisoft.window = new MiIrrisoft();
	MiConexionDB cone = new MiConexionDB();
	MiIrrisoft.window.hiloescucha = new HiloEscucha();
	MiIrrisoft.window.hiloescucha.setConnDB(cone);
	HiloPluviometro hp = new HiloPluviometro(Misd, sens);
	//Creo el puerto serie
	String puerto = "port";
	MiSerialPort serialp = new MiSerialPort(puerto);
	Misd.setSerialPort(serialp);
	serialp.openPort();
	//Variables
	sens.setNum_borna(4);
	sens.setNum_placa(5);
	sens.setK(1);
	sens.setNum_sensor("1");
	//Llamada al metodo
	hp.run();
    }

}
