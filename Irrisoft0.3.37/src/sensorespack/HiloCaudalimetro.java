package sensorespack;

import irrisoftpack.ConexionDB;
import irrisoftpack.Irrisoft;
import irrisoftpack.SerialDriver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import panelespack.Paneltest;

import valvulaspack.ListaValvBt2;
import valvulaspack.ListaValvMci;
import valvulaspack.Valvula;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;


public class HiloCaudalimetro extends Sensor implements Runnable {
	

	protected SerialDriver serialcon = new SerialDriver();
	protected String puerto;
	protected int tipoplaca,len,leo,i,repeticion=0;
	protected int valvsabiertasant=0,valvsabiertas=0;
	protected int[] pulsos = new int [2];
	protected byte[] bufferresp = new byte[6];
	protected byte[] buftrans = { (byte) 0x06,(byte) 0x00,(byte) 0x03, 
			   (byte)0x00, (byte)0x01, (byte)0x0A };
	
	//Mascara para desechar el bit 7 de la respuesta de la bt2
	protected static byte mascara = (byte) 0b01111111;
	
	protected int vueltasens =0;
	private boolean terminar = false;
	private int tiempoduerme = 0;
	private double lapsobt2 = 0;
	private boolean test = false;
	private boolean regando = false;
	private boolean mci = true;
	public boolean inicial=false;
	public int pulsostot = 0;
	public int vuelta = 0;
	public int tipovalv;
	public int numvalv;
	private long tiempoini;
	public Valvula valv;
	
	private int consumotot=0,consumoresidual=0;
	private float caudal;
	
	public ConexionDB connDB = new ConexionDB();
	
	
	


public HiloCaudalimetro (String puerto, int tipo, int tipovalv) {
       
        this.puerto=puerto;
        this.tipoplaca=tipo;
        this.tipovalv=tipovalv;

	}
	
	
public void run() {
		
		id=1;
		ListaSensores.getInstance().addsensor(this);
		System.out.println("Id sensor: "+ListaSensores.getInstance().getsens().get(id-1).id);
			
		pulsos[0]=0;
		pulsos[1]=0;	
			
		while (true){	
			
			if (test==false && regando==false){
				
				while(terminar!=true){
						automatico();
						
				}
			}else if (test==true || regando==true){
				
					
					//Si el contador de pulsos va a una MCI
					if (mci){
						
						if (pulsostot>=3){
							System.out.println("Vuelta: "+vuelta);
							calculacaudaltest(1);
						}else{
							
							if (test)
								test();
							else if (regando)
								regando();
						}		
						
					//Si el contador de pulsos va a una BT2
					}else if (!mci) {
						
						System.out.println("El contador de pulsos está acoplado a la BT2 !!!!!!!!!!");
						
						if (test)
							test();
						else if (regando)
							regando();
						
						
						System.out.println("TIEMPOOOOORL: "+lapsobt2*vuelta);
						
						if ((lapsobt2*vuelta)<=60){
							
							if (pulsostot >=25){
								System.out.println("< 60");
								if (test)
									calculacaudaltest(tipovalv);
								else if (regando)
									calculacaudalregando(valv);
							}
						}else if ((lapsobt2*vuelta)>60 && (lapsobt2*vuelta)<=120){
							
							if (pulsostot >=25){
								System.out.println("< 120");
								if (test)
									calculacaudaltest(tipovalv);
								else if (regando)
									calculacaudalregando(valv);
							}
						}else if ((lapsobt2*vuelta)>120){
						
							if (pulsostot >3){
								System.out.println(">120");
								if (test)
									calculacaudaltest(tipovalv);
								else if (regando)
									calculacaudalregando(valv);
							}
						}
					}
			}
			
			
			//TODO Aki para cuando está regando 
			
			if (regando){
				
			}
			
		}					
					
}


public void automatico (){
	
	try {
		
		System.out.println(("Estoy en hilocaudalimetro: AUTOMATICO"));
		
		valvsabiertas=0;
		//Tiempo variable (25 Ok)
		tiempoduerme=25;
		
		while (tiempoduerme>0){
			Thread.sleep(1000);
			tiempoduerme--;
			if (terminar==true)
				return;
		}
		
		tiempoduerme=25;
		
		// Mirar valvulas abiertas 
		valvsabiertas = Irrisoft.window.valvsabiertas();
//		for (i=0;i<Irrisoft.window.valvsbt2.getsizeof();i++){
//			if (Irrisoft.window.valvsbt2.getvalvbt2(i).isAbierta())
//				valvsabiertas++;
//		}
		
		if (valvsabiertasant==valvsabiertas){
			Irrisoft.window.semaforobt2.take();
			serialcon.conectaserial(puerto,tipoplaca);
				cuentapulsos();
			serialcon.desconectaserial();
			Irrisoft.window.semaforobt2.release();
		}
		//pasada++;
		
		
		} catch (Exception e) {
			// e.printStackTrace();
			Irrisoft.window.textArea.append("\nCancelada lectura de pulsos caudalimetro");
			return;
		}
		finally{
			System.out.println("Valvsabiertasant = "+valvsabiertasant);
			valvsabiertasant=valvsabiertas;
		}
}

public void test(){
	
	if (vuelta==0)
		tiempoini=System.nanoTime();
		
		
	System.out.println(("Estoy en hilocaudalimetro: TEST"));
	System.out.println("Pulsos TOTALES: "+pulsostot);
	vuelta++;
	Irrisoft.window.paneltest.progressBar.setValue(pulsostot);
		
	
	try {
		//Si el caudalimetro va acoplado a una BT2
		if (!mci){ 
			long tiempoini= (System.nanoTime());
			Irrisoft.window.semaforobt2.take();
				serialcon.conectaserial(puerto,tipoplaca);
				cuentapulsos();
				serialcon.desconectaserial();
			Irrisoft.window.semaforobt2.release();
			long lapso=System.nanoTime()-tiempoini;
			lapsobt2=lapso/Math.pow(10,9);
			System.out.println("Lapsosbt2: "+lapsobt2);
			//Thread.sleep(tiempoduerme*1000);
		}
		//Si el caudalimetro va acoplado a una mci
		else if (mci){
			//TODO
		}
		
		
	} catch (Exception e) {
		// e.printStackTrace();
		Irrisoft.window.textArea.append("\nCancelada lectura de pulsos caudalimetro");
		return;
	}	
}

public void regando (){
	
	
	if (vuelta==0){
		tiempoini=System.nanoTime();
		try {
			Irrisoft.window.semaforobt2.take();
			serialcon.conectaserial(puerto,tipoplaca);
			serialcon.consultconsum();
			consumoresidual= serialcon.leeresp(true);
			serialcon.desconectaserial();
			Irrisoft.window.semaforobt2.release();
		} catch (InterruptedException | NoSuchPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	System.out.println(("Estoy en hilocaudalimetro: REGANDO"));
	System.out.println("Pulsos TOTALES: "+pulsostot);
	vuelta++;
	//Irrisoft.window.paneltest.progressBar.setValue(pulsostot);
		
	try {
		//Si el caudalimetro va acoplado a una BT2
		if (!mci){ 
			long tiempoini= (System.nanoTime());
			Irrisoft.window.semaforobt2.take();
				serialcon.conectaserial(puerto,tipoplaca);
				cuentapulsos();
				serialcon.desconectaserial();
			Irrisoft.window.semaforobt2.release();
			long lapso=System.nanoTime()-tiempoini;
			lapsobt2=lapso/Math.pow(10,9);
			System.out.println("Lapsosbt2: "+lapsobt2);
			//Thread.sleep(tiempoduerme*1000);
		}
		//Si el caudalimetro va acoplado a una mci
		else if (mci){
			//TODO
		}
		
		
	} catch (Exception e) {
		// e.printStackTrace();
		Irrisoft.window.textArea.append("\nCancelada lectura de pulsos caudalimetro");
		return;
	}	
	
	
}

public void cuentapulsos(){
	
	float result = 0;
		
		System.out.println("Hola stoy en cuentapulsos");
		
					try {
						serialcon.out.write(buftrans);
						serialcon.out.flush();
						len=0;
						
					    while(len<6)
						{			
								try {
									leo=serialcon.in.read();
								} catch (IOException e) {
									// 
									e.printStackTrace();
								}
							
									bufferresp[len]=(byte) leo;
								
								//Sólo si no es lectura de consumo
								len++;
						}
					    

					    	pulsos[0]=pulsos[1];
					    	pulsos[1]=bufferresp[4] & mascara;
					   
					    
					    System.out.println("Respuesta de pulsos acomulados:" +pulsos[1]);
					    String s =("0000000" + Integer.toBinaryString(0xFF & pulsos[1])).replaceAll(".*(.{8})$", "$1");
					    System.out.println("Respuesta de pulsos acomulados (bits):" +s);
					 
					    result = (pulsos[1]-pulsos[0]);
					    
					    
					    //Si se ha pasado de vuelta, osea ha marcado ya 127 pulsos
					    //
					    if (result<0){
					    	vueltasens++;
					    	 result = 128 +(pulsos[1]-pulsos[0]);
					    	 
					    	 
					    	 //OOJOOO
					    	 result = Math.abs(result);
					    	 
						    System.out.println("Pulsos en este bucle (Vuelta "+vueltasens+" : "+result);
						    
						    //Pulsos totales TEST
						    if (test || regando){
						    	if (vuelta==1){
						    		pulsostot=0;
						    	}
							    	
							    else
							    	pulsostot=pulsostot +(int) result;
						    }
						    
						    
						    if (result==0){
						    	repeticion++; 
						    }else if (result>=1 && valvsabiertas==0){
						    	//hay una fuga de agua ya que no hay valvulas abiertas y estoy recibiendo pulsos
						    	System.out.println("HAY FUGA de AGUA");
						    }else if (result>=1 && valvsabiertas>0){
						    	
						    	System.out.println("Calcularcaudaaaaaaaaal");
						    	//Cálculo caudal
						    	// (K/repeticion*frecuenciade?)
						    	//Lo comparo con el sumatorio de los consumos teóricos 
						    	//de las válvulas abiertas, de tal forma que si la diferencia 
						    	//es mayor a un umbral, hay sobreconsumo o fuga. 
						    	//Y si la diferencia es menopulsos[1]r hay obstrucción en algún lugar del 
						    	//sistema
						    	
						    }
						    
					    }else{
						    System.out.println("Pulsos en este bucle: "+result);
						  //Pulsos totales TEST
						    if (test || regando){
						    	if (vuelta==1)
							    	pulsostot=0;
							    else
							    	pulsostot=pulsostot +(int) result;
						    }
						    //TODO HACER FUNCION PARA NO DUPLICAR EL CODIGO
						    if (result==0){
						    	repeticion++; 
						    }else if ((result>=1 && valvsabiertas==0) && !test){
						    	//hay una fuga de agua ya que no hay valvulas abiertas y estoy recibiendo pulsos
						    	System.out.println("HAY FUGA de AGUA en el sistema");
						    }else if ((result>=1 && valvsabiertas>0)&& !test){
						    	
						    	System.out.println("Calcularcaudaaaaaaaaal");
						    	
						    	//Cálculo caudal
						    	// (K/repeticion*frecuenciade?)
						    	//(10l/p entre repeticion)*25
						    	//Lo comparo con el sumatorio de los consumos teóricos 
						    	//de las válvulas abiertas, de tal forma que si la diferencia 
						    	//es mayor a un umbral, hay sobreconsumo o fuga. 
						    	//Y si la diferencia es menor hay obstrucción en algún lugar del 
						    	//sistema
						    		
						    }
					    }
					    
					} catch (Exception e) {
						System.out.println(e.getMessage());
				        System.out.println(e.getStackTrace());
					}	
				
					System.out.println("El caudal  es de :"+((result*Irrisoft.window.K)/(tiempoduerme+lapsobt2))+" l/seg");
					

}

public void calculacaudaltest(int tipovalv) {
	
	//String valv=null;
	numvalv= Irrisoft.window.paneltest.HiloTest.numvalv;
	//Calcular el System.nanotime para ajustar el tiempo y no lapsobt2 !!
	//caudal = (float) (Math.rint(((((pulsostot-1)*(Irrisoft.window.K))/(lapsobt2*vuelta))*1000))/1000);
	
	System.out.println("Caudal con tiempotestvalv: "+((System.nanoTime()-tiempoini)/Math.pow(10,9)));
	caudal = (float) (Math.rint(((((pulsostot-1)*(Irrisoft.window.K))/((System.nanoTime()-tiempoini)/Math.pow(10,9)))*1000))/1000);
			
	if (!inicial){
		Irrisoft.window.paneltest.lblPulsosDat.setText(Integer.toString(pulsostot));
		Irrisoft.window.paneltest.lblCauDat.setText(caudal+" l/seg");
	}else{
		Irrisoft.window.paneltest.lblpul.setText(Integer.toString(pulsostot));
		Irrisoft.window.paneltest.lblls.setText(caudal+" l/seg");
		
	}
	
	try {
			
			Irrisoft.window.semaforobt2.take();
			serialcon.conectaserial(puerto,tipoplaca);
			serialcon.consultconsum();
			consumotot= serialcon.leeresp(true);
			serialcon.desconectaserial();
			Irrisoft.window.semaforobt2.release();
			
			//Si es una valvulamci
			if (tipovalv==1){
				//valv=(String)Irrisoft.window.paneltest.getModelomci().getSelectedItem();
				//cierro la valvula requerida
				Irrisoft.window.panelmci.interruptor(ListaValvMci.getInstance().getvalvmci(numvalv), ListaValvMci.getInstance().getvalvmci(numvalv).getImgasoc());
			}
			//Si es una valvulabt2
			else if (tipovalv==2){
				//valv=(String)Irrisoft.window.paneltest.getModelobt2().getSelectedItem();
				//cierro la valvula requerida 
				Irrisoft.window.panelbt2.interruptor(ListaValvBt2.getInstance().getvalvbt2(Integer.toString(numvalv)), 2,3);
			}
			Irrisoft.window.semaforobt2.take();
			serialcon.conectaserial(puerto,tipoplaca);
			serialcon.consultconsum();
			consumoresidual= serialcon.leeresp(true);
			
			if (!inicial){
				Irrisoft.window.paneltest.lblIntenDat.setText(Integer.toString(consumotot-consumoresidual)+" mA");
				
				//Activo los botones
				Irrisoft.window.paneltest.btnAtras.setEnabled(true);
				Irrisoft.window.paneltest.btnTestearMCI.setEnabled(true);
				Irrisoft.window.paneltest.btntesteoini.setEnabled(true);
				Irrisoft.window.paneltest.btnTestearBT2.setEnabled(true);
				Irrisoft.window.paneltest.comboBoxMCI.setEnabled(true);
				Irrisoft.window.paneltest.comboBoxBt2.setEnabled(true);
				Irrisoft.window.paneltest.btnAtras.setText("Atras");
				
			}		
			else
				Irrisoft.window.paneltest.lblamp.setText(Integer.toString(consumotot-consumoresidual)+" mA");	
			
		
			
			serialcon.desconectaserial();
			Irrisoft.window.semaforobt2.release();
			
			Irrisoft.window.paneltest.progressBar.setValue(25);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	
	if (inicial){
		
		Irrisoft.window.hilocaudal.setTest(false);
		Irrisoft.window.paneltest.progressBar.setValue(25);
		
		//Escribir registro en la BBDD
		if (tipovalv==1)
			escribeconsum(Integer.toString(numvalv+1));
		else
			escribeconsum(Integer.toString(numvalv));
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}else{
		Irrisoft.window.paneltest.lbltesteando.setText("Testeo de valvula OK");
		//Sobreescribir registro en la BBDD
		if (tipovalv==1)
			sobrescribeconsum(Integer.toString(numvalv+1));
		else
			sobrescribeconsum(Integer.toString(numvalv));
	}

	
	synchronized (Irrisoft.window.paneltest.hilotest){
		Irrisoft.window.paneltest.hilotest.notify();
	}
	
	
}


public void calculacaudalregando(Valvula valv) {
	
	
	
	valv.setCaudal((float) (Math.rint(((((pulsostot-1)*(Irrisoft.window.K))/((System.nanoTime()-tiempoini)/Math.pow(10,9)))*1000))/1000));
	System.out.println("CAUDAL en VALV: "+valv.getCaudal()+ " l/s");		
	System.out.println("caudal MOD en VALV: "+valv.getCaudalmod()+ " l/s");

	float diferenciacau = valv.getCaudalmod()-valv.getCaudal();
	System.out.println("diferencia = "+diferenciacau);
	
	//Se calcula un margen de diferencia para las alarmas del 10% (variable??)
	if (Math.abs(diferenciacau) >= (valv.getCaudalmod()*10)/100){
		if (diferenciacau <0)
			System.out.println("Hay un problema en el consumo de agua. Estás consumiendo "+Math.abs(diferenciacau)+" l/s de más");
		else
			System.out.println("Hay un problema en el consumo de agua. Estás consumiendo "+Math.abs(diferenciacau)+" l/s de menos");		
	}
	
	
	
	try {
			Irrisoft.window.semaforobt2.take();
			serialcon.conectaserial(puerto,tipoplaca);
			serialcon.consultconsum();
			consumotot= serialcon.leeresp(true);
			serialcon.desconectaserial();
			Irrisoft.window.semaforobt2.release();
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	
	valv.setIntensidad(consumotot-consumoresidual);

	System.out.println("INTENSIDAD en VALV: "+valv.getIntensidad());
	System.out.println("intensidad MOD en VALV: "+valv.getIntensidadmod());
	int diferenciaint = valv.getIntensidadmod()-valv.getIntensidad();
	System.out.println("diferencia = "+diferenciaint);
	
	//Se calcula un margen de diferencia para las alarmas del 10% (variable??)
	if (Math.abs(diferenciaint) >= (valv.getIntensidadmod()*10)/100){
		if (diferenciaint <0)
			System.out.println("Hay un problema en el consumo de intensidad. Estás consumiendo "+Math.abs(diferenciaint)+" mA de más");
		else
			System.out.println("Hay un problema en el consumo de intensidad. Estás consumiendo "+Math.abs(diferenciaint)+" mA de menos");		
	}
		
	
	
	try {
		  Thread.currentThread();
		  Thread.sleep(5000);
	} 	catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	vuelta = 0;
	//Desactivo la lectura de regando
	Irrisoft.window.caudalregando(false);
}






///////////////////////// BBDD
public void escribeconsum(String valvula){
	
	connDB.poneconsumostest(valvula,caudal,consumotot-consumoresidual);
}

private void sobrescribeconsum(String valvula) {
	
	connDB.sobrescribeconsumostest(valvula,caudal,consumotot-consumoresidual);
	
}


///////////////////////////////////////////////////////////// SETTERS Y GETTERS

public void setTerminar(boolean terminar) {
	this.terminar = terminar;
}

public boolean getTerminar() {
	return terminar;
}

public boolean isTest() {
	return test;
}

public void setTest(boolean test) {
	this.test = test;
}

public int getPulsostot() {
	return pulsostot;
}

public void setPulsostot(int pulsostot) {
	this.pulsostot = pulsostot;
}

public boolean isMci() {
	return mci;
}

public void setMci(boolean mci) {
	this.mci = mci;
}

public int getVuelta() {
	return vuelta;
}

public void setVuelta(int vuelta) {
	this.vuelta = vuelta;
}



public boolean isRegando() {
	return regando;
}


public void setRegando(boolean regando) {
	this.regando = regando;
}


public int getValvsabiertas() {
	return valvsabiertas;
}


public void setValvsabiertas(int valvsabiertas) {
	this.valvsabiertas = valvsabiertas;
}


} 

	


	

