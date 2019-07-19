package sensoresTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sensorespack.HiloCaudalimetro;
import sensorespack.Sensor;

import irrisoftpack.SerialDriver;

public class MiHiloCaudalimetro extends HiloCaudalimetro{
    
    private static Logger logger = LogManager.getLogger(MiHiloCaudalimetro.class
	    .getName());
    
    public boolean calculaCaudal = false;
    public boolean testea = false;
    public boolean automatic = false;

    public MiHiloCaudalimetro(SerialDriver serial, int tipo, int tipovalv,
	    Sensor sens) {
	super(serial, tipo, tipovalv, sens);
	// TODO Auto-generated constructor stub
    }
    
//    public int cuentapulsoslapso(){
//	logger.warn("Entra en cuentapulsoslapso de MiHiloCaudalimetro");
//	int pulsosLapso= 1;
//	return pulsosLapso;
//    }
    
    public void calculacaudaltest(int tipovalv){
	logger.warn("Entra en calcula caudal test de MiHiloCaudalimetro");
	calculaCaudal = true;
    }
    
    public void automatico(){
	logger.warn("Entra en automatico de MiHiloCaudalimetro");
	automatic = true;
    }
    
    public void test(){
	logger.warn("Entra en test de MiHiloCaudalimetro");
	testea = true;
    }
    
    public int cuentapulsos(){
	logger.warn("Entra en cuentapulsos de MiHiloCaudalimetro");
	int pulsos = 1;
	return pulsos;
    }

}
