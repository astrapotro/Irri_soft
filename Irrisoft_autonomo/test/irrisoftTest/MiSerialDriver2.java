package irrisoftTest;

import irrisoftpack.SerialDriver;
import jssc.SerialPort;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sensorespack.Sensor;

//Se encarga de las clases:
//actuabt2,activabt,cualesabiertasbt,cualesabiertasinicio:
public class MiSerialDriver2 extends SerialDriver{
    private static Logger logger = LogManager.getLogger(MiSerialDriver2.class
	    .getName());
    
    public boolean semaforocoger2 = false;
    public boolean semaforosoltar2 = false;
    public boolean respuesta = true;
    public boolean conectado = false;
    public boolean consulta = false;

    public MiSerialDriver2() {
	// TODO Auto-generated constructor stub
    }
    
    public void cogesemaforo(int tipo){
	logger.warn("Entra en cogesemaforo de MiSerialDriver 2");
	semaforocoger2 = true;
    }
    
    public void sueltasemaforo(int tipo){
	logger.warn("Entra en sueltasemaforo de MiSerialDriver 2");
	semaforosoltar2 = true;
    }
    
    public void purga_puerto(int tipop, String puerto){
	logger.warn("Entra en reconecta de MiSerialDriver 2");
	conectado = true;
    }
    public boolean leeresp(boolean consum, int tipo,boolean abrir, String codelecvalv, int maes){
	logger.warn("Entra en leeresp de MiSerialDriver 2");
	return respuesta;
	
    }
    
    public void consultconsum(SerialPort serial, Sensor sensor){
	logger.warn("Entra en consultconsum de MiSerialDriver 2");
	consulta = true;
    }
}
