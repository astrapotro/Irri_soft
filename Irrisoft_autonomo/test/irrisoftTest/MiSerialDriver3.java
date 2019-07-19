package irrisoftTest;

import irrisoftpack.SerialDriver;
import jssc.SerialPort;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sensorespack.Sensor;

//Se encarga de las clases:
//reconecta:
public class MiSerialDriver3 extends SerialDriver{
    
    private static Logger logger = LogManager.getLogger(MiSerialDriver3.class
	    .getName());
    
    public boolean reseteado = false;
    public boolean activado = false;
    public boolean cerradas = false;
    public boolean reconectado = false;

    public MiSerialDriver3() {
	// TODO Auto-generated constructor stub
    }
    
    public void resetsinsemaforo(int tipo){
	logger.warn("Entra en resetsinsemaforo de MiSerialDriver 3");
	reseteado = true;
    }
    
    public void activabt(SerialPort serial, Sensor sens){
	logger.warn("Entra en activabt de MiSerialDriver3 ");
	activado = true;
	
    }
    public void cerrarAbiertasAlInicio(SerialPort serialp, int tipo){
	logger.warn("Entra en cerrarAbiertasAlInicio de MiSerialDriver3");
	cerradas = true;
    }
    
    public void purga_puerto(int tipop, String puerto){
	logger.warn("Entra en reconecta de MiSerialDriver3");
	reconectado = true;
    }

}
