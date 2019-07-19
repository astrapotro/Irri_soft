package irrisoftTest;

import static org.junit.Assert.*;
import irrisoftpack.Semaforo;

import org.junit.Test;

public class SemaforoTest {

     //@Test //ACABADO, no tienen assert
    public void testtake() throws InterruptedException {
	int d = 2;
	Semaforo semaf = new Semaforo(d);
	semaf.take();

    }
     
     //@Test //ACABADO, no tienen assert
     public void testrelease() throws InterruptedException
     {
	 int d = 2;
	 Semaforo semaf = new Semaforo(d);
	 semaf.release();
     }
}
