package programaTest;

import irrisoftpack.SerialDriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import programapack.HiloPrograma;
import programapack.Programacion;
import programapack.TareaProg;

public class MiHiloPrograma extends HiloPrograma {

    private static Logger logger = LogManager.getLogger(MiHiloPrograma.class
	    .getName());
    public boolean administrar = false;
    public int calculado = 5;
    public boolean cerrado = false;
    public boolean actualizado = false;
   

    public MiHiloPrograma(TareaProg tar, int z, Programacion prog) {
	// TODO Auto-generated constructor stub
	super(tar, z, prog);
    }

    public void adminbloque(TareaProg tar, int z) {
	logger.warn("Entra en adminbloque de MiHiloPrograma");
	administrar = true;
    }
    //El resultado del metodo varia para diferentes pruebas.
    //Metodo "regando()".
    public int calculoduracion(TareaProg tar) {
	logger.warn("Entra en calculoduracion de MiHiloPrograma");
	
	return calculado;
    }
    //Metodo "regando()"
    public void cierravalv(TareaProg tar, SerialDriver serialcon) {
	logger.warn("Entra en cierravalv de MiHiloPrograma");
	cerrado = true;
    }
    
    public void actestvalv(TareaProg tar, boolean accionabrir){
	logger.warn("Entra en actestvalv de MiHiloPrograma");
	actualizado = true;
    }

}
