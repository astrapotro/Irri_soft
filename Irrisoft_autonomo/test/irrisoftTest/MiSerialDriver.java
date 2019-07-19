package irrisoftTest;

import irrisoftpack.SerialDriver;
import jssc.SerialPort;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sensorespack.Sensor;
import valvulaspack.Valvula;

//Se encarga de las clases:
//conectaserial,desconectaserial,abrevalv,cierravalv, reset,resetsinsemaforo,leeresp,consultonsum:
public class MiSerialDriver extends SerialDriver {
    private static Logger logger = LogManager.getLogger(MiSerialDriver.class
	    .getName());

    public boolean semaforocoger = false;
    public boolean semaforosoltar = false;
    public boolean actua = true;
    public boolean conectado = false;
    public boolean valvAbierta = true;
    public boolean valvCerrada = true;
    public boolean conectadoS = false;
    public boolean consultado = false;
    public boolean leido = true;

    public MiSerialDriver() {
	// TODO Auto-generated constructor stub
    }
    
    public void conectaserial(int tipo){
	logger.warn("Entra en conectaserial de MiSerialDriver");
	conectadoS = true;
	
    }

    public void cogesemaforo(int tipo) {
	logger.warn("Entra en cogesemaforo de MiSerialDriver");
	semaforocoger = true;
    }

    public void sueltasemaforo(int tipo) {
	logger.warn("Entra en sueltasemaforo de MiSerialDriver");
	semaforosoltar = true;

    }

    public boolean interruptorbt2(String codelecvalv, int dec, boolean abrir,
	    int tipo, int maes) {
	logger.warn("Entra en actua de MiSerialDriver");
	return actua;

    }

    public void purga_puerto(int tipop, String puerto) {
	logger.warn("Entra en reconecta de MiSerialDriver");
	conectado = true;
    }

    public boolean abrevalv(Valvula valv) {
	logger.warn("Entra en abrevalv de MiSerialDriver");
	return valvAbierta;
    }

//    public boolean cierravalv(String codelecvalv, int tipo) {
//	logger.warn("Entra en cierravalv de MiSerialDriver");
//	return valvCerrada;
//
//    }
    public void consultconsum(SerialPort serial, Sensor sensor){
	logger.warn("Entra en consultconsum de MiSerialDriver");
	consultado = true;
    }
    
    public boolean leeresp(boolean consum, int tipo,
	    boolean abrir, String codelecvalv, int maes){
	logger.warn("Entra en leeresp de MiSerialDriver");
	return leido;
    }

}
