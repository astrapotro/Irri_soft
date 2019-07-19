package alertasTest;

import static org.junit.Assert.*;

import java.util.Date;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import alertaspack.Alerta;

/**
 * Esta clase se utiliza para realizar los teses de la clase Alerta.
 * Test:
 * Metodos get y set.
 * toString(int).
 * @author alberto
 *
 */
public class AlertaTest {
    
    private static Logger logger = LogManager.getLogger(AlertaTest.class
	    .getName());
    
    /**
     * Pruebo el testtoString.
     * Introduciendo un numero me devuelve la 
     * descripcion del error del fichero 
     * messages.properties.
     */
    //@Test
    public void testAlertas(){
	Alerta alerta = new Alerta();
	alerta.setCodAlerta(2001);
	String a = alerta.toString();
	assertEquals(a,"2001: Fuga de agua en estaciones.");
    }
    
    /**
     * Pruebo el test del metodo getCodAlerta.
     */
    //@Test
    public void testgetcodAlerta(){
	Alerta alerta = new Alerta();
	alerta.setCodAlerta(1);
	assertTrue(alerta.getCodAlerta()==1);
	
    }
    
    /**
     * Pruebo el test del metodo setCodAlerta.
     */
    //@Test
    public void testsetcodAlerta(){
	Alerta alerta = new Alerta();
	int a = 1;
	alerta.setCodAlerta(1);
	assertEquals(a,alerta.getCodAlerta());
	
    }
    
    /**
     * Pruebo el test del metodo getDescripcion.
     */
    //@Test
    public void testgetDescripcion(){
	Alerta alerta = new Alerta();
	alerta.setDescripcion("prueba");
	assertTrue(alerta.getDescripcion()=="prueba");
	
    }
    
    /**
     * Pruebo el test del metodo setDescripcion.
     */
    //@Test
    public void testsetDescripcion(){
	Alerta alerta = new Alerta();
	alerta.setDescripcion("prueba");
	String a = "prueba";
	assertEquals(a,alerta.getDescripcion());
	
    }
    
    /**
     * Pruebo el test del metodo getFechaAlerta.
     */
    //@Test
    public void testgetfechaAlerta(){
	Alerta alerta = new Alerta();
	Date a = new Date(10);
	//alerta.setFechaAlerta(a);
	assertTrue(alerta.getFechaAlerta() == a);
    }
    
    /**
     * Pruebo el test del metodo setFechaAlerta
     */
    //@Test
    public void testsetfechaAlerta(){
	Alerta alerta = new Alerta();
	Date a = new Date();
	//alerta.setFechaAlerta(a);
	assertEquals(a,alerta.getDescripcion());
	
    }
    
   
    
    

}
