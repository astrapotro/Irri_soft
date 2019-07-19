package programaTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import programapack.HiloPrograma;
import programapack.Programacion;
import programapack.TareaProg;

import irrisoftpack.SerialDriver;

public class MiHiloPrograma3 extends HiloPrograma {
    private static Logger logger = LogManager.getLogger(MiHiloPrograma3.class
	    .getName());

    public boolean abrir = false;
    public boolean regar = false;
    public boolean cerrar = false;
    public boolean pintado = false;
    public boolean accionar = false;

    public MiHiloPrograma3(TareaProg tar, int z, Programacion prog) {
	super(tar, z, prog);
	// TODO Auto-generated constructor stub
    }

    public void abrevalv(TareaProg tar, SerialDriver serialcon) {
	logger.warn("Entra en abrevalv de MiHiloPrograma 3");
	abrir = true;
    }

    public void regando(TareaProg tar) {
	logger.warn("Entra en regando de MiHiloPrograma 3");
	regar = true;
    }

    public void cierravalv(TareaProg tar, SerialDriver serialcon) {
	logger.warn("Entra en cierravalv de MiHiloPrograma 3");
	cerrar = true;

    }

    public void pintainfo(TareaProg tar, boolean pasada, String horactual) {
	logger.warn("Entra en pintainfo de MiHiloPrograma 3");
	pintado = true;

    }
    
    public void accionvalv(TareaProg tar){
	logger.warn("Entra en accionvalv de MiHiloPrograma 3");
	accionar = true;
	
    }

}
