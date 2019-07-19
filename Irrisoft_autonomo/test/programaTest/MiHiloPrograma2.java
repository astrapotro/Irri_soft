package programaTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import programapack.HiloPrograma;
import programapack.Programacion;
import programapack.TareaProg;

public class MiHiloPrograma2 extends HiloPrograma {
    private static Logger logger = LogManager.getLogger(MiHiloPrograma2.class
	    .getName());

    public boolean eshora = false;
    public boolean runLlegado = false;

    public MiHiloPrograma2(TareaProg tar, int z, Programacion prog) {
	super(tar, z, prog);
	// TODO Auto-generated constructor stub
    }

    public void eslahora(TareaProg tar) {
	logger.warn("Entra en eslahora de MiHiloPrograma 2");
	eshora = true;
    }

    public void run() {
	logger.warn("Entra en run de MiHiloPrograma 2");
	runLlegado = true;

    }

}
