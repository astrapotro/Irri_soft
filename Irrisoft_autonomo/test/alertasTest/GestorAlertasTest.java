package alertasTest;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import irrisoftpack.Conf;
import irrisoftpack.Irrisoft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;


import alertaspack.Alerta;
import alertaspack.GestorAlertas;

import volcadopack.ConexionVolc;

public class GestorAlertasTest extends ConexionVolc{

    private static Logger logger = LogManager.getLogger(GestorAlertasTest.class
	    .getName());
    
    GestorAlertas ga = GestorAlertas.getInstance();


    /**
     * Pruebo el test de insertar Alerta.
     * Introduzco un codigo de alerta y verifico 
     * que se ha introducido correctamente en la 
     * base de datos.
     * Caso:Para verificar que funciona correctament, 
     * hay que mirar el siguiente IDALERTA que va en 
     * la tabla t08_alerta. 
     */
    //@Test 
    public void testInsertarAlarma(){
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusr.getForeground();
	Irrisoft.window.lblstatusr.getText();
	this.conectar();
	Irrisoft.window.volcado.getCon().setConnr(this.getConnr());
	Irrisoft.config = new Conf();
	Irrisoft.config.setIdrasp("7777");
	int codigoAla = 3007;
	int idalerta = 790;
	ga.insertarAlarma(codigoAla);
	Connection conn = null;
	try{
	    Class.forName("com.mysql.jdbc.Driver");
	    
	    String urllocal = "jdbc:mysql://" + "www.serviciosgis.com" + ":"
		    + "5528" + "/" + "prugestdropper";

	    DriverManager.setLoginTimeout(5);

	    conn = DriverManager.getConnection(urllocal, "irrigest", "13riego");
	    PreparedStatement sentenciapre = conn
		    .prepareStatement("SELECT IDALERTA from t08_alerta where CODPROG=? AND IDDSALERTA=?");
	    sentenciapre.setString(1, "7777");
	    sentenciapre.setInt(2, 9);
	    ResultSet rs = sentenciapre.executeQuery();
	    while (rs.next()) {
		int a = rs.getInt(1);
		assertTrue(a == idalerta);
		logger.warn("Alarma verificada");
	    }
	    rs.close();
	    sentenciapre.close();
	    
	}catch(SQLException e){
	    if(logger.isErrorEnabled()){
		logger.error(e.getMessage());
	    }
	    
	}catch(ClassNotFoundException e){
	    if(logger.isErrorEnabled()){
		logger.error(e.getMessage());
	    }
	    
	}
    }
    
    /**
     * Compruebo si hay conexion con la 
     * BBDD Remota, para luego mandar la alerta.
     * Caso:Hay conexion a la BBDD Remota.
     */
    //@Test
    public void testvalidar(){
	boolean r;
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusr.getForeground();
	Irrisoft.window.lblstatusr.getText();
	this.conectar();
	Irrisoft.window.volcado.getCon().setConnr(this.getConnr());

	r = GestorAlertas.validar();

	r = ga.validar();

	assertTrue(r==true);

    }

    
    /**
     * Compruebo si las alertas no enviadas
     * se envian cuando tengo conexion.
     */
    @Test
    public void testinsertarAlarmasNoEnviadas(){
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusr.getForeground();
	Irrisoft.window.lblstatusr.getText();
	//this.conectar();
	Irrisoft.window.volcado.getCon().setConnr(this.getConnr());
	Irrisoft.config = new Conf();
	Irrisoft.config.setIdrasp("7777");
	Alerta alerta = new Alerta();
	Alerta alerta1 = new Alerta();
	alerta.setCodAlerta(2009);
	alerta.setCodAlerta(2010);
	alerta.setFechaAlerta(null);
	alerta1.setFechaAlerta(null);
	ArrayList<Alerta> alertas = new ArrayList<>();
	alertas.add(alerta);
	alertas.add(alerta1);
	assertTrue(ga.insertaAlarmaNoEnviadas());
    
    }
}
