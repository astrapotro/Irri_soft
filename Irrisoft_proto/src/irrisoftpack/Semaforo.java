package irrisoftpack;

public class Semaforo {

	//Semaforo para controlar que los hilos sólo puedan acceder a 
	
		  private int signals = 0;
		  private int bound   = 0;

		  public Semaforo(int upperBound){
		    this.bound = upperBound;
		  }

		  public synchronized void take() throws InterruptedException{
		    while(this.signals == bound) wait();
		    this.signals++;
		    this.notifyAll();
		  }

		  public synchronized void release() throws InterruptedException{
		    while(this.signals == 0) wait();
		    this.signals--;
		    this.notifyAll();
		  }
		
	
}
