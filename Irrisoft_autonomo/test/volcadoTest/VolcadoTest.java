package volcadoTest;

import static org.junit.Assert.*;

import javax.swing.JLabel;

import irrisoftpack.Irrisoft;

import org.junit.Test;

import alertaspack.GestorAlertas;

import volcadopack.ConexionVolc;
import volcadopack.Volcado;

public class VolcadoTest {

    public ConexionVolc con;
    private static GestorAlertas ga;
     @Test //Echo y Bien:
    public void testRun() {
	Volcado vol = new Volcado();
	Irrisoft.window = new Irrisoft();
	Irrisoft.window.lblstatusr.getForeground();
	Irrisoft.window.lblstatusr.getText();
	MiConexionVolc miconV = new MiConexionVolc();
	vol.setCon(miconV);
	vol.run();	
	

    }

}
