package irrisoftpack;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class HiloPrograma implements Runnable{
		
		private TareaProg prog;
		
		private String numvalv;
		
		private Integer valv;
		int i;
		
		//Constructor para poder pasarle la tarea como argumento
		 public HiloPrograma (TareaProg prog) {
		        this.prog = prog;
		    }
		
		@Override
		public synchronized void run() {

			//COMPRUEBO para que PLACA es la tarea !!
			valv = Integer.parseInt(prog.getCodelecvalv());
			numvalv = prog.getCodelecvalv();
			
			//Compruebo si es anterior a la hora actual y la desecho, o sino, la pongo a esperar hasta que le toque ejecutarse
			eslahora(prog);						
		
		}

		//
		//	La tarea espera el tiempo necesario hasta su ejecución !!
		//
		//////////////////////////////
		public boolean eslahora (TareaProg prog){
			 	
			 Date date1=null;
			 Date date2=null;
			 
			//Hacer un bucle para dormirme cada 30 segs, consultar en la local la hora de finalización 
			// Volcado y RECALCULAR 
			// TODO Cada vez que despierto consulto la horaprog 
			 
			 	//Para calcular el delay de espera de la tarea hasta su ejecución
			 	String horact = (new Timestamp(Calendar.getInstance().getTime().getTime())).toString();
			 	horact=horact.substring(11, 19);			 	
			 	String horaprog =prog.getHorafin().toString().substring(0, 8);
			 	
			 	SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
				try {
			 
					date1 = formatter.parse(horact);
					date2 = formatter.parse(horaprog);
			 
				} catch (ParseException e) {
					e.printStackTrace();
				}
			 	
			 	long delay = (date2.getTime())-(date1.getTime());
			 	
			 	delay =(new Date(delay)).getTime();
			 	
			 	//Compruebo si la hora es anterior a la actual para no ejecutar las que sean anteriores !
			 	if (date2.after(date1)){
			 		Irrisoft.window.textArea
					.append("\n   "+prog.getHorafin().toString().substring(0, 5)+" hrs, " +
							"abrir valvula "+prog.getCodelecvalv()+" "+prog.duracion+" segundos");
					Irrisoft.window.frmIrrisoft.repaint();
					
			 		
				 	//Duermo el hilo hasta que sea la hora 
				 	try {
					
						if (delay>0){
							
							
							System.out.println("ESLAHORA: "+horact+" "+ horaprog);
						 	System.out.println(delay);
						 	
							Thread.sleep(delay+Irrisoft.progs.anadedelay);
							System.out.println("Ya es la hora, y tengo que dormir "+prog.getDuracion());
						 	
						 	
							//llamo a abrirvalvulas
							if(valv<=28){
								accionvalv(prog,Irrisoft.window.config.getMci(),1);
							}else if (valv>28 && valv<1000){
								accionvalv(prog,Irrisoft.window.config.getMci2(),2);
							}else if(valv>1000 && valv <2000){
								accionvalv(prog,Irrisoft.window.config.getBt2(),3);
							}else if (valv >2000){
								accionvalv(prog,Irrisoft.window.config.getBt22(),4);
							}
						}
							
					} catch (InterruptedException e) {
						//Auto-generated catch block
						e.printStackTrace();
					}
			 	}
			 return false;
		 }
		 
		 
		 public void accionvalv(TareaProg prog, String puerto,int tipoplaca){
			 
			SerialDriver serialcon = new SerialDriver();
			
			//Lo separo en dos métodos para tener un semaforo en la escritura y otro en la lectura !!!
			abrevalv(prog,puerto,tipoplaca,serialcon);
			
			cierravalv(prog,puerto,tipoplaca,serialcon);
		 }
		 
		 
		private void abrevalv(TareaProg prog, String puerto,int tipoplaca,SerialDriver serialcon) {
			
			//TODO SECCION CRÍTICA A TRATAR CON EL/LOS SEMAFOROS
			
			try {
				//Llamo al semaforo cerrojo solo si es bt2
				if (tipoplaca==3 || tipoplaca==4)
					Irrisoft.window.semaforo1.take();
				serialcon.conecta(puerto, tipoplaca);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			serialcon.abrevalv(prog.getCodelecvalv(),tipoplaca);
			gestvalv(tipoplaca,true);
	
			// //////////////////////////////////////////////////////////////////////////
			
			
			serialcon.cierra();
			System.out.println("Tarea: "+prog.getIdtareaprog());
			System.out.println("Hilo "+prog.getHilotar().getId() +" durmiendo: " + prog.duracion);
		
			
			try {
							System.out.println("Hilo "+prog.getHilotar().getId() +" durmiendo: " + prog.duracion);
							
							//Suelto el semarofo cerrojo
							if (tipoplaca==3 || tipoplaca==4)
								Irrisoft.window.semaforo1.release();
							
							//Duermo el proceso tanto como dure la tarea
							Thread.currentThread();
							Thread.sleep((prog.duracion*1000)); //SON MILISEGUNDOOOOOOOOOOOOOOS	
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						} 
		}

		private void cierravalv(TareaProg prog, String puerto,int tipoplaca,SerialDriver serialcon){
			
			//TODO SECCION CRÍTICA A TRATAR CON EL/LOS SEMAFOROS
			
			try {
				//Llamo al semaforo cerrojo solo si es bt2
				if (tipoplaca==3 || tipoplaca==4)
					Irrisoft.window.semaforo2.take();
				serialcon.conecta(puerto,tipoplaca);
			} catch (Exception e) {
				e.printStackTrace();
			}
				
				
				serialcon.cierravalv(prog.getCodelecvalv(),tipoplaca);
				gestvalv(tipoplaca,false);
		
			 
				serialcon.cierra();
				//Llamo a borratarea
				Irrisoft.window.hiloescucha.connDB.actualizatareaprog(prog);
				
				//Suelto el semarofo cerrojo
				try {
					if (tipoplaca==3 || tipoplaca==4)
						Irrisoft.window.semaforo2.release();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		private void gestvalv(int tipo,boolean abierta){
			
			if (tipo==1 || tipo==2){
					
				//Si está cerrada!!
				if (abierta){
					//Actualizo estado de la valvula
					ListaValvMci.getInstance().getvalvmci(Integer.parseInt(prog.getCodelecvalv())).setAbierta(false);
					//Repinto
					Irrisoft.window.panelmci.interruptor(ListaValvMci.getInstance().getvalvmci(prog.getCodelecvalv()),
													ListaValvMci.getInstance().getvalvmci(Integer.parseInt(numvalv)-1).getImgasoc(),true);
					
					
				}else{
					//Asocio una idtarea con la valvula
					ListaValvMci.getInstance().getvalvmci(Integer.parseInt(prog.getCodelecvalv())).setTareaasoc(prog.getIdtareaprog());
					ListaValvMci.getInstance().getvalvmci(Integer.parseInt(prog.getCodelecvalv())).setAbierta(true);
					
					//Repinto
					Irrisoft.window.panelmci.interruptor(ListaValvMci.getInstance().getvalvmci(prog.getCodelecvalv()),
													ListaValvMci.getInstance().getvalvmci(Integer.parseInt(numvalv)-1).getImgasoc(),true);
					
				}
				
			}else if (tipo==3 || tipo==4){
				
				int codelecvalv = Integer.parseInt(prog.getCodelecvalv())-1001;
				if (abierta){
				//Actualizo estado de la valvula
				ListaValvBt2.getInstance().getvalvbt2(codelecvalv).setAbierta(true);
				}else{
					//Actualizo estado de la valvula
					ListaValvBt2.getInstance().getvalvbt2(codelecvalv).setAbierta(false);
					//Asocio una idtarea con la valvula
					ListaValvBt2.getInstance().getvalvbt2(codelecvalv).setTareaasoc(prog.getIdtareaprog());
				}
				//SUSCEPTIBLE DE IMPLEMENTAR ventana gráfica de control de válvulas bt2
			}
			
				
			
			
		}
}
