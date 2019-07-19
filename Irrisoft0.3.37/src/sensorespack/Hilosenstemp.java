package sensorespack;

import irrisoftpack.Irrisoft;


public class Hilosenstemp extends Sensor implements Runnable {

	protected int medmin=-10, medmax=50, salmin=0,salmax=5;
	protected byte check= 1 + Irrisoft.bornatemp;
	
	protected byte[] buftrans = { (byte) 1, Irrisoft.bornatemp, check };
	
	
public void run() {
	
		id=1;
		ListaSensores.getInstance().addsensor(this);
		System.out.println("Id sensor: "+ListaSensores.getInstance().getsens().get(id-1).id);
	
		while(true){
			
			try {
				
					System.out.println(("Estoy en hilosenstemp"));
					
					//Cada hora pregunto cual es la temperatura
					//Thread.sleep(3600000); Cada hora la pido
					Thread.sleep(5000);
					Hilosensores.getInstance().Escribesens(buftrans);
					
			} catch (InterruptedException e) {
				// 
				e.printStackTrace();
			}
			
		}
		
	}
		
}
