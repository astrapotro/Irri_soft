package sensoresTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sensorespack.HiloHumedadSuelo;
import sensorespack.Sensor;

import irrisoftpack.SerialDriver;

public class MiHiloHumedadSuelo extends HiloHumedadSuelo{
    
    private static Logger logger = LogManager.getLogger(MiHiloHumedadSuelo.class
	    .getName());

    public MiHiloHumedadSuelo(SerialDriver serialDriver, int tipo, Sensor sens) {
	super(serialDriver, sens);
	// TODO Auto-generated constructor stub
    }
    
    public boolean humedadBT = false;
    public boolean humedadGON = false;
    public boolean interrupt = false;
    
    public void leehumedadbt(){
	logger.warn("Entra en lee HumedadBT de MiHiloHumedadSuelo");
	humedadBT = true;
    }
    
    public void leehumedadgon(){
	logger.warn("Entra en lee HumedadGON de MiHiloHumedadSuelo");
	humedadGON = true;
    }
    
    public void interruptor(int accion){
	logger.warn("Entra en interruptor de MiHiloHumedadSuelo");
	interrupt = true;
    }

}
