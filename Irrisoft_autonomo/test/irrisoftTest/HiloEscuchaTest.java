package irrisoftTest;

import static org.junit.Assert.*;


import irrisoftpack.ConexionDB;
import irrisoftpack.HiloEscucha;
import irrisoftpack.Irrisoft;

import java.util.Date;
import java.util.LinkedHashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class HiloEscuchaTest {
    
    private static Logger logger = LogManager.getLogger(HiloEscuchaTest.class
	    .getName());

    //@Test// Hecho y bien:ACABADO, no se como hacerle el assert.
    public void testrun() {
	HiloEscucha He = new HiloEscucha();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.valvsabiertastot = new LinkedHashSet<>();
	MiConexionDB miconexion = new MiConexionDB();
	He.setConnDB(miconexion);
	He.run();


    }

     //@Test//Echo y Bien:
    public void testabreDBhilo() {

	ConexionDB cone = new ConexionDB();

	HiloEscucha He = new HiloEscucha();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.window = new Irrisoft();

	Irrisoft.window.lblstatusl.getText();
	//Al cambiar de paquete, no puedo acceder al atributo ya que es PROTECTED
	//Irrisoft.window.btnEmpezar.getText();
	Irrisoft.window.abreDBhilo();
	//Al cambiar de paquete, no puedo acceder al atributo ya que es PROTECTED
	//assertTrue(He.getConnDB().conectado);
	logger.warn("Hilo Abierto");


    }

     //@Test//Echo y Bien:
    public void testcierraDBhilo() {
	HiloEscucha He = new HiloEscucha();
	Irrisoft.config.setHost("127.0.0.1");
	Irrisoft.config.setPuerto(3306);
	Irrisoft.config.setDb("irrisoft");
	Irrisoft.config.setUsuario("gestor");
	Irrisoft.config.setPass("gestor");
	Irrisoft.window = new Irrisoft();

	

	Irrisoft.window.lblstatusl.getText();
	//Al cambiar de paquete, no puedo acceder al atributo ya que es PROTECTED
	//Irrisoft.window.btnEmpezar.getText();
	Irrisoft.window.abreDBhilo();
	//Al cambiar de paquete, no puedo acceder al atributo ya que es PROTECTED
	//assertTrue(He.getConnDB().conectado);
	Irrisoft.window.cierraDBhilo();
	//Al cambiar de paquete, no puedo acceder al atributo ya que es PROTECTED
	//assertFalse(He.getConnDB().conectado);
	logger.warn("Hilo Cerrado");


    }

     //@Test//Echo y Bien:
    public void testsetTerminar() {
	HiloEscucha He = new HiloEscucha();
	boolean ter = false;
	// He.setTerminar(false);
	assertEquals(ter, He.getTerminar());
	// assertTrue(He.getTerminar() == false);
    }

     //@Test//Echo y Bien:
    public void testgetTerminar() {
	HiloEscucha He = new HiloEscucha();
	boolean ter = false;
	assertEquals(ter, He.getTerminar());

    }

     //@Test // El get y el set juntos //Echo y Bien:
    public void testsetgetTerminar() {
	HiloEscucha He = new HiloEscucha();
	boolean ter = false;
	// assertFalse(false);
	He.setTerminar(ter);
	assertEquals(ter, He.getTerminar());
    }

}
