package irrisoftTest;

import static org.junit.Assert.*;

import irrisoftpack.ConexionDB;
import irrisoftpack.Conf;
import irrisoftpack.Irrisoft;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import programapack.TareaProg;

import sensorespack.HiloCaudalimetro;
import sensorespack.ListaSensores;
import sensorespack.Sensor;
import valvulaspack.ListaPlacasGhost;
import valvulaspack.ListaValvBt2;
import valvulaspack.ListaValvMci;
import valvulaspack.PlacaGhost;
import valvulaspack.Valvula;

public class IrrisoftTest {
    
    private static Logger logger = LogManager.getLogger(IrrisoftTest.class
	    .getName());

    public ConexionDB con;
    public HiloCaudalimetro hilo;
    public LinkedHashSet<Valvula> valvsabiertasbt2;

     //@Test //Echo y Bien:ACABADO, falta assert
    public void testleerconfirri() {
	Conf config = new Conf();
	String salida ="7777";
	Irrisoft irri = new Irrisoft();
	irri.leerconfirri(config);
	assertEquals(salida,config.getIdrasp());
	logger.warn("Correcta lectura de irrisoft.txt");
    }

     //@Test //Echo y Bien:
    public void testleerpuertosbt() {
	Irrisoft irri = new Irrisoft();
	String salida = "/dev/ftdi0";
	Irrisoft.config = new Conf();
	irri.leerpuertosbt();
	assertEquals(Irrisoft.config.getBt2(),salida);
	logger.warn("Correcta lectura de puertosbt.txt");

    }
    
    //@Test //Echo y Bien:ACABADO, cambiar el metodo miraabiertasbt a PUBLICO
    public void testmirarabiertasbt(){
	Irrisoft irri = new Irrisoft();
	MiSerialDriver3 serie5 = new MiSerialDriver3();
	int tipo = 5;
	//irri.setSerie5(serie5);
	Valvula valv = new Valvula();
	irri.valvsbt2 = new ListaValvBt2();
	irri.valvsbt2.addvalvbt2(valv);
	//irri.miraabiertasbt();
	assertTrue(serie5.cerradas);
	logger.warn("Hay valvulas abiertas");
    }

     //@Test //Entra en el siguiente metodo
//    public void testleerconf_ini_prog() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
//	Irrisoft irri = new Irrisoft();
//	Irrisoft.config.setHost("127.0.0.1");
//	Irrisoft.config.setPuerto(3306);
//	Irrisoft.config.setDb("irrisoft");
//	Irrisoft.config.setUsuario("gestor");
//	Irrisoft.config.setPass("gestor");
//	Irrisoft.config.setIdrasp("9999");
//	Irrisoft.window = new Irrisoft();
//	Irrisoft.window.lblstatusl.getForeground();
//	Irrisoft.window.lblstatusl.getText();
//	Method metodo = irri.getClass().getDeclaredMethod("leerconf_ini_prog");
//	metodo.setAccessible(true);
//	metodo.invoke(irri);
//	
//	
//
//    }

//    @Test  //REVISAR//REVISAR
//    public void testponerconfBT() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
//	ListaValvBt2 lbt2 = new ListaValvBt2();
//	int a =1;
//	String valvula="1001";
//	int deco = 5;
//	int maestra =1;
//	int lacth=1;
//	int goteo=1;
//	int numeroplac=5;
//	Irrisoft irri = new Irrisoft();
//	Method metodo = irri.getClass().getDeclaredMethod("ponerconfBT",ListaValvBt2.class,int.class,String.class,int.class,int.class,int.class,int.class,int.class);
//	metodo.setAccessible(true);
//	metodo.invoke(irri, lbt2,a,valvula,deco,maestra,lacth,goteo,numeroplac);

//    }

     //@Test //REVISAR//REVISAR
//    public void testponerconfMCI() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
//	ListaValvMci lMci = new ListaValvMci();
//	int a = 1;
//	int maestra = 1;
//	int lacth = 1;
//	int goteo = 1;
//	int numeroplac = 1;
//	String valvula = "1001";
//	Irrisoft irri = new Irrisoft();
//	Method metodo = irri.getClass().getDeclaredMethod("ponerconfMCI",ListaValvMci.class,int.class,String.class,int.class,int.class,int.class,int.class);
//	metodo.setAccessible(true);
//	metodo.invoke(irri,lMci,a,valvula,maestra,lacth,goteo,numeroplac);
//
//	
//
//    }

     //@Test  //Echo Y Bien: Sige a los siguientes metodos.
//    public void testleerconf_ini_sens() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
//	Irrisoft irri = new Irrisoft();
//	Irrisoft.config.setHost("127.0.0.1");
//	Irrisoft.config.setPuerto(3306);
//	Irrisoft.config.setDb("irrisoft");
//	Irrisoft.config.setUsuario("gestor");
//	Irrisoft.config.setPass("gestor");
//	Irrisoft.config.setIdrasp("9999");
//	Irrisoft.window = new Irrisoft();
//	Irrisoft.window.lblstatusl.getForeground();
//	Irrisoft.window.lblstatusl.getText();
//	Method metodo = irri.getClass().getDeclaredMethod("leerconf_ini_sens");
//	metodo.setAccessible(true);
//	metodo.invoke(irri);
//
//    }

     //@Test  //REVISAR//REVISAR
//    public void testponerconfsens() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
//	Irrisoft irri = new Irrisoft();
//	int a = 5;
//	Sensor s = new Sensor();
//	
//	//LinkedList<Sensor> sensores = ListaSensores.getInstance().getsens();
//	LinkedList<Sensor> ssss= ListaSensores.getInstance().getsens();
//	s.setNum_sensor("Temp");
//	//Acceso Privado
////	Method metodo = irri.getClass().getDeclaredMethod("ponerconfsens",int.class);
////	metodo.setAccessible(true);
////	metodo.invoke(irri, a);
//	//Acceso Publico
//	irri.ponerconfsens(a);
//	
//	
//    }

    //@Test //REVISAR
//    public void testhilos_sens() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//	Irrisoft irri = new Irrisoft();
//	int a = 1;
//	int tipo = 5;
//	Method metodo = irri.getClass().getDeclaredMethod("hilos_sens",int.class,int.class);
//	metodo.setAccessible(true);
//	metodo.invoke(irri, a,tipo);
//
//
//    }

     //@Test //REVISAR
//    public void testhilos_sens_ghost() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
//	Irrisoft irri = new Irrisoft();
//	int a = 1;
////	PlacaGhost placG = new PlacaGhost();
////	ListaPlacasGhost ghost = new ListaPlacasGhost();
////	ghost.addplaca(placG);
//	Method metodo = irri.getClass().getDeclaredMethod("hilos_sens_ghost",int.class);
//	metodo.setAccessible(true);
//	metodo.invoke(irri, a);
//	
//    }
    
    //@Test //Echo y Bien:ACABADO 
    public void testaddvalvsabiertas()
    {
	Irrisoft irri = new Irrisoft();
	int total = 2;
	int tipo = 5;
	Valvula valv = new Valvula();
	Valvula valv1 = new Valvula();
	irri.valvsabiertasbt2 = new LinkedHashSet<>();
	irri.valvsabiertasbt2.add(valv);
	irri.valvsabiertasbt2.add(valv1);
	irri.valvsabiertastot = new LinkedHashSet<>();
	assertEquals(total,irri.addvalvsabiertas(valv, tipo));
	logger.warn("Sumadas las valvulas abiertas");
	
    }

     //@Test //Echo y Bien:ACABADO
    public void testvalvsabiertas() {
	Irrisoft irri = new Irrisoft();
	int abiertasTotales = 2;
	int tipo = 5;
	Valvula valv = new Valvula();
	Valvula valv1 = new Valvula();
	irri.valvsabiertasbt2 = new LinkedHashSet<>();
	irri.valvsabiertasbt2.add(valv);
	irri.valvsabiertasbt2.add(valv1);
	assertEquals(abiertasTotales,irri.valvsabiertas(tipo));
	logger.warn("Valvulas abiertas: "+irri.valvsabiertas(tipo));

    }

     //@Test //Echo y Bien:ACABADO
    public void testvalvsabiertas2() {
	Irrisoft irri = new Irrisoft();
	LinkedHashSet<Valvula> lista = new LinkedHashSet<Valvula>();
	lista=valvsabiertasbt2;
	int tipo = 5;
	Valvula valv = new Valvula();
	Valvula valv1 = new Valvula();
	irri.valvsabiertasbt2 = new LinkedHashSet<>();
	irri.valvsabiertasbt2.add(valv);
	irri.valvsabiertasbt2.add(valv1);
	logger.warn(irri.listavalvsabiertas(tipo));
	assertNotNull(irri.valvsabiertasbt2);
	
    }

     //@Test //Echo y Bien:ACABADO
    public void testquitarvalvabiertas() {
	Irrisoft irri = new Irrisoft();
	int tipo = 5;
	LinkedHashSet<Valvula> lista = new LinkedHashSet<Valvula>();
	Valvula valv = new Valvula();
	valv.setCodelecvalv("1001");
	irri.valvsabiertasbt2 = new LinkedHashSet<>();
	irri.valvsabiertasbt2.add(valv);
	irri.valvsabiertastot = new LinkedHashSet<>();
	irri.valvsabiertastot.add(valv);
	irri.quitarvalvabiertas(valv, tipo);
	logger.warn(irri.valvsabiertasbt2);
	logger.warn(irri.valvsabiertastot);
	assertEquals(lista,irri.valvsabiertasbt2);
	assertEquals(lista,irri.valvsabiertastot);
	
    }


     //@Test //Echo y Bien:ACABADO, no tiene assert
    public void testescribelog() {
	String texto = "Salida en condiciones";
	Irrisoft irri = new Irrisoft();
	irri.escribelog(texto);
	

    }

     //@Test //Echo y Bien:ACABADO
    public void testactivabt() {
	MiSerialDriver3 sd = new MiSerialDriver3();
	Irrisoft irri = new Irrisoft();
	irri.activabt(sd);
	assertTrue(sd.activado);
    }

     //@Test //Echo y Bien:ACABADO
    public void testgetHex() {
	byte[] bytes = { 9, 10, 11, 15, 16 };
	boolean escribo = true;
	Irrisoft irri = new Irrisoft();
	assertEquals(irri.getHex(bytes, escribo), "09.10.11.15.16");

    }

    // ////////////////////////Getter y Setter ACABADO

     //@Test //Echo y Bien:
    public void testgetConn() {
	Irrisoft irri = new Irrisoft();
	irri.setConn(con);
	assertTrue(irri.getConn() == con);

    }

     //@Test //Echo y Bien: 
    public void testsetConn() {
	Irrisoft irri = new Irrisoft();
	irri.setConn(con);
	assertTrue(irri.getConn() == con);

    }

     //@Test //Echo y Bien:
    public void testgetHilocau() {
	Irrisoft irri = new Irrisoft();
	irri.setHilocau(hilo);
	assertTrue(irri.getHilocau() == hilo);

    }

     //@Test //Echo y Bien:
    public void testsetHilocau() {
	Irrisoft irri = new Irrisoft();
	irri.setHilocau(hilo);
	assertTrue(irri.getHilocau() == hilo);

    }

}
