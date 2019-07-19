package sensoresTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sensorespack.HiloAmperimetro;
import sensorespack.Sensor;

import irrisoftpack.SerialDriver;

public class MiHiloAmperimetro extends HiloAmperimetro{
    
    private static Logger logger = LogManager.getLogger(MiHiloAmperimetro.class
	    .getName());
    
    public boolean calculaAuto = false;
    public boolean calculaTest = false;
    public boolean automatic = false;
    public boolean testeado = false;
    public boolean sobrEscrito= false;
    public boolean escrito = false;

    public MiHiloAmperimetro(SerialDriver serial, Sensor sens, int tipo) {
	super(serial, sens, tipo);
	// TODO Auto-generated constructor stub
    }
    
    public void calculaintauto(){
	logger.warn("Entra en calculaintauto de MiHiloAmperimetro");
	calculaAuto = true;
    }
    
    public void calculaintest(){
	logger.warn("Entra en calculaintest de MiHiloAmperimetro");
	calculaTest = true;
    }
    
    public void automatico(){
	logger.warn("Entra en automatico de MiHiloAmperimetro");
	automatic = true;
    }
    
    public void test(){
	logger.warn("Entra en test de MiHiloAmperimetro");
	testeado = true;
    }
    
    public void escribeconsum(String valvula){
	logger.warn("Entra en escribeconsum de MiHiloAmperimetro");
	escrito = true;
    }
    
    public void sobrescribeconsum(String valvula){
	logger.warn("Entra en sobrescribeconsum de MiHiloAmperimetro");
	sobrEscrito = true;
	
    }

}
