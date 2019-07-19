package sensoresTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sensorespack.HiloAmperimetroGhost;
import sensorespack.Sensor;

public class MiHiloAmperimetroGhost extends HiloAmperimetroGhost{
    
    private static Logger logger = LogManager.getLogger(MiHiloAmperimetroGhost.class
	    .getName());
    
    public boolean testea = false;
    public boolean calculatest = false;
    public boolean escrito = false;
    public boolean sobrEscrito = false;

    public MiHiloAmperimetroGhost(Sensor sens) {
	super(sens);
	// TODO Auto-generated constructor stub
    }
    
    public void test(){
	logger.warn("Entra en test de MiHiloAmperimetroGhost");
	testea = true;
    }
    
//    public void calculaintest(){
//	logger.warn("Entra el calculaintest de MiHiloAmperimetroGhost");
//	calculatest = true;
//    }
    
    public void escribeconsum(String Valvula){
	logger.warn("Entra en escribeconsum de MiHiloAmperimetroGhost");
	escrito = true;
    }
    
    public void sobrescribeconsum(String Valvula){
	logger.warn("Entra en sobrescribeconsum de MiHiloAmperimetroGhost");
	sobrEscrito = true;
    }
    

}
