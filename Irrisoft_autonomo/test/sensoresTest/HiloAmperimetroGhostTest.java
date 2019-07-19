package sensoresTest;

import static org.junit.Assert.*;
import irrisoftTest.MiConexionDB;
import irrisoftTest.MiIrrisoft;
import irrisoftpack.Irrisoft;

import org.junit.Test;

import panelespack.Paneltest;
import sensorespack.HiloAmperimetroGhost;
import sensorespack.Sensor;

public class HiloAmperimetroGhostTest {

    // @Test //Echo y Bien:ACABADO //Entra en el bucle que no acaba.
    public void testrun() {
	//Objetos, constructor y Inicializar Irrisoft
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.test = true;
	MiHiloAmperimetroGhost hag = new MiHiloAmperimetroGhost(sens);
	//Llamada al metodo
	hag.run();
	//Assert
	assertTrue(hag.testea);
    }

    //@Test  // Echo y Bien:ACABADO
    //Caso de que no haya caudalimetro.
    public void testtest1() {
	//Objetos, constructor y Inicializar Irrisoft
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.haycaudalimetro = false;
	MiHiloAmperimetroGhost hag = new MiHiloAmperimetroGhost(sens);
	//Variables
	sens.setValvula(1001);
	Thread h1 = new Thread();
	sens.setHilosens(h1);
	//Llamada al metodo
	hag.test();
	//Assert
	assertTrue(hag.calculatest);
    }
    
    //@Test //Echo y Bien:ACABADO
    //Caso de que haya caudalimetro, se queda esperando en el wait.
    public void testtes2(){
	//Objetos, constructor y Inicializar Irrisoft
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.haycaudalimetro = true;
	HiloAmperimetroGhost hag = new HiloAmperimetroGhost(sens);
	//Variables
	Thread h2 = new Thread();
	sens.setHilosens(h2);
	//Llamada al metodo
	hag.test();
    }

     //@Test //Echo y Bien:ACABADO
    public void testcalculaintest1() {
	//Objetos, constructor y Inicializar Irrisoft
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.paneltest = Paneltest.getInstance();
	Irrisoft.window.inicial = true;
	MiHiloAmperimetroGhost hag = new MiHiloAmperimetroGhost(sens);
	//Variables
	sens.setLecturacau(1f);
	sens.setValvula(1001);
	//Llamada al metodo
	hag.calculaintest();
	//Assert
	assertTrue(hag.escrito);
    }
    
    //@Test //Echo y Bien:ACABADO 
    //Cambiar a PUBLIC metodo "sobrescribeconsum".
    public void testcalculaintest2(){
	//Objetos, constructor y Inicializar Irrisoft
	Sensor sens = new Sensor();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.paneltest = Paneltest.getInstance();
	MiHiloAmperimetroGhost hag = new MiHiloAmperimetroGhost(sens);
	//Variables
	sens.setLecturacau(2f);
	sens.setValvula(1001);
	//Llamada al metodo
	hag.calculaintest();
	//Assert
	assertTrue(hag.sobrEscrito);
    }

    // @Test //Echo y Bien:ACABADO
    public void testescribeconsum() {
	//Objetos, constructor y Inicializar Irrisoft
	Sensor sens = new Sensor();
	MiIrrisoft.window = new MiIrrisoft();
	MiConexionDB cone = new MiConexionDB();
	MiIrrisoft.window.setConn(cone);
	HiloAmperimetroGhost hag = new HiloAmperimetroGhost(sens);
	//Variables
	String valvula = "1001";
	//Llamada al metodo
	hag.escribeconsum(valvula);
    }

    // @Test //Echo y Bien:ACABADO
    // Poner el metodo sobreescribeconsum a PUBLIC
    public void testsobrescribeconsum() {
	//Objetos, constructor y Inicializar Irrisoft
	Sensor sens = new Sensor();
	MiIrrisoft.window = new MiIrrisoft();
	MiConexionDB cone = new MiConexionDB();
	MiIrrisoft.window.setConn(cone);
	HiloAmperimetroGhost hag = new HiloAmperimetroGhost(sens);
	//Variables
	String valvula = "1001";
	//Llamada al metodo
	// hag.sobrescribeconsum(valvula);
    }

}
