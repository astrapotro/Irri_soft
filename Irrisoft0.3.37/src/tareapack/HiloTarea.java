package tareapack;

import valvulaspack.ListaValvBt2;
import valvulaspack.ListaValvBt22;
import valvulaspack.ListaValvMci;
import valvulaspack.ListaValvMci2;
import irrisoftpack.Irrisoft;
import irrisoftpack.SerialDriver;


public class HiloTarea implements Runnable{
		
		private Tarea tarea;
		
		private String numvalv;
		
		
		//Constructor para poder pasarle la tarea como argumento
public HiloTarea (Tarea tarea) {
		        this.tarea = tarea;
		        ListaTareasaexec.getInstance().addtarea(tarea);
		    }
		
		@Override
public synchronized void run() {
			
			Irrisoft.window.textArea
			.append("\n   Válvula "+tarea.getCodelecvalv()+" durante "+tarea.getDuracion()+" segundos");
			System.out.println("NULL:"+tarea.getCodelecvalv());
			//COMPRUEBO para que PLACA es la tarea !!
			Integer valv = Integer.parseInt(tarea.getCodelecvalv());
			numvalv = tarea.getCodelecvalv();
			
			if(valv<=28){
				accionvalv(Irrisoft.config.getMci(),1);
			}else if (valv>28 && valv<1000){
				accionvalv(Irrisoft.config.getMci2(),2);
			}else if(valv>1000 && valv <2000){
				accionvalv(Irrisoft.config.getBt2(),3);
			}else if (valv >2000){
				accionvalv(Irrisoft.config.getBt22(),4);
			}			
		}
	
public synchronized boolean accionvalv(String puerto,int tipoplaca){
			SerialDriver serialcon = new SerialDriver();
			
			try {
				serialcon.conectaserial(puerto, tipoplaca);
			} catch (Exception e) {
				//e.printStackTrace();
				//Si no hay conexion con la placa 
				if (tipoplaca==1){
					Irrisoft.window.textArea.append("\nApertura de la valv "+numvalv+" cancelada.");
					Irrisoft.window.textArea.append("\nConecte la primera placa MCI !");
					return false;
				}else if (tipoplaca==2){
					Irrisoft.window.textArea.append("\nApertura de la valv "+numvalv+" cancelada.");
					Irrisoft.window.textArea.append("\nConecte la segunda placa MCI !");
					return false;
				}else if (tipoplaca==3){
					Irrisoft.window.textArea.append("\nApertura de la valv "+numvalv+" cancelada.");
					Irrisoft.window.textArea.append("\nConecte la primera placa BT2 !");
					return false;
				}else if (tipoplaca==4){
					Irrisoft.window.textArea.append("\nApertura de la valv "+numvalv+" cancelada.");
					Irrisoft.window.textArea.append("\nConecte la segunda placa BT2 !");
					return false;
				}
			}
			
			serialcon.abrevalv(tarea.getCodelecvalv(),tipoplaca);
			gestvalv(tipoplaca,true);
			
			
			
			serialcon.desconectaserial();
			
			if (!ListaTareasaexec.getInstance().getTareas().isEmpty()){

						try {
							System.out.println("Hilo "+tarea.getHilotar()+" durmiendo: " + tarea.getDuracion());
							//Duermo el proceso tanto como sea + ajuste de segundos (A REVISAR)
							//while (tarea.getDuracion()>0){
							//TODO QUizás abrá que guardar la duración de tiempo de regado por si se va la luz 
							//en medio de la ejecución
								Thread.sleep(tarea.getDuracion()*1000); 	//SON  MILISEGUNDOS !!!
							//}
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						} 
					
						try {
							serialcon.conectaserial(puerto,tipoplaca);
						} catch (Exception e) {
							//e.printStackTrace();
							if (tipoplaca==1){
								Irrisoft.window.textArea.append("\nConecte la primera placa MCI !");
							}else if (tipoplaca==2){
								Irrisoft.window.textArea.append("\nConecte la segunda placa MCI !");
							}else if (tipoplaca==3){
								Irrisoft.window.textArea.append("\nConecte la primera placa BT2 !");
							}else if (tipoplaca==4){
								Irrisoft.window.textArea.append("\nConecte la segunda placa BT2 !");
							}	
						}
						
							serialcon.cierravalv(tarea.getCodelecvalv(),tipoplaca);
							gestvalv(tipoplaca,false);
							serialcon.desconectaserial();
							
							
							//Llamo a borratarea TODO
							if (Irrisoft.window.hiloescucha.connDB.Borratarea(tarea)){
									Irrisoft.window.textArea.append("\nTarea "+tarea.getIdtarea()+" borrada correctamente.");
								
							}
			}
			
			return true;
		 }
		 	 	 
private void gestvalv(int tipo,boolean accionabrir){
	
	if (tipo==1 || tipo==2){
		if(tipo==1){//Si es la primera placa MCI
			if (accionabrir){
				tarea.setRegando(true);
				//Actualizo estado de la valvula y la ascio con la tarea (id)
				ListaValvMci.getInstance().getvalvmci(Integer.parseInt(tarea.getCodelecvalv())-1).setAbierta(true);
				ListaValvMci.getInstance().getvalvmci(Integer.parseInt(tarea.getCodelecvalv())-1).setTareaasoc(tarea.getIdtarea());
				//Repinto
				Irrisoft.window.panelmci.interruptor(ListaValvMci.getInstance().getvalvmci(Integer.parseInt(numvalv)-1),
												ListaValvMci.getInstance().getvalvmci(Integer.parseInt(numvalv)-1).getImgasoc(),true);
				//La meto en Valvsabiertas
				Irrisoft.window.valvsabiertas();
			
			}else{
				tarea.setRegando(false);
				//Reinicio idtarea de la valvula
				ListaValvMci.getInstance().getvalvmci(Integer.parseInt(tarea.getCodelecvalv())-1).setTareaasoc(0);
				ListaValvMci.getInstance().getvalvmci(Integer.parseInt(tarea.getCodelecvalv())-1).setAbierta(false);
				
				//Repinto
				Irrisoft.window.panelmci.interruptor(ListaValvMci.getInstance().getvalvmci(Integer.parseInt(numvalv)-1),
												ListaValvMci.getInstance().getvalvmci(Integer.parseInt(numvalv)-1).getImgasoc(),true);
				
				//Sea la valvula que sea la quito de la lista de valvulas abiertas 
				Irrisoft.window.quitarvalvabiertas(tarea.getCodelecvalv());
				//Tb quito la tarea de la lista de tareasaexec
				ListaTareasaexec.getInstance().delhilotar(tarea);
			}
	 }else{//Si es la segunda placa MCI
		 
			if (accionabrir){
				tarea.setRegando(true);
				//Actualizo estado de la valvula
				ListaValvMci2.getInstance().getvalvmci(Integer.parseInt(tarea.getCodelecvalv())-1).setAbierta(true);
				ListaValvMci2.getInstance().getvalvmci(Integer.parseInt(tarea.getCodelecvalv())-1).setTareaasoc(tarea.getIdtarea());

				
				//Repinto
				Irrisoft.window.panelmci.interruptor(ListaValvMci2.getInstance().getvalvmci(Integer.parseInt(numvalv)-1),
												ListaValvMci2.getInstance().getvalvmci(Integer.parseInt(numvalv)-1).getImgasoc(),true);
				
				//La meto en Valvsabiertas
				Irrisoft.window.valvsabiertas();
				
			}else{
				tarea.setRegando(false);
				//Asocio una idtarea con la valvula
				ListaValvMci2.getInstance().getvalvmci(Integer.parseInt(tarea.getCodelecvalv())-1).setTareaasoc(0);
				ListaValvMci2.getInstance().getvalvmci(Integer.parseInt(tarea.getCodelecvalv())-1).setAbierta(false);
				
				//Repinto
				Irrisoft.window.panelmci.interruptor(ListaValvMci.getInstance().getvalvmci(Integer.parseInt(numvalv)-1),
												ListaValvMci.getInstance().getvalvmci(Integer.parseInt(numvalv)-1).getImgasoc(),true);
				//Sea la valvula que sea la quito de la lista de valvulas abiertas
				Irrisoft.window.quitarvalvabiertas(tarea.getCodelecvalv());
				//Tb quito la tarea de la lista de tareasaexec
				ListaTareasaexec.getInstance().delhilotar(tarea);
			} 
	 }
		
	}else if (tipo==3 || tipo==4){	//Para la  BT2
		int codelecvalv=0;
		
		if(tipo==3){
			codelecvalv = Integer.parseInt(tarea.getCodelecvalv())-1001;
			if (accionabrir){
				tarea.setRegando(true);
				//Actualizo estado de la valvula
				ListaValvBt2.getInstance().getvalvbt2(codelecvalv).setAbierta(true);
				ListaValvBt2.getInstance().getvalvbt2(codelecvalv).setTareaasoc(tarea.getIdtarea());
				//La meto en Valvsabiertas
				Irrisoft.window.valvsabiertas();
				}else{
					tarea.setRegando(false);
					//Actualizo estado de la valvula
					ListaValvBt2.getInstance().getvalvbt2(codelecvalv).setAbierta(false);
					ListaValvBt2.getInstance().getvalvbt2(codelecvalv).setTareaasoc(0);
					//Asocio una idtarea con la valvula
					//ListaValvBt2.getInstance().getvalvbt2(codelecvalv).setTareaasoc(tarea.getIdtarea());
					
					//Sea la valvula que sea la quito de la lista de valvulas abiertas
					Irrisoft.window.quitarvalvabiertas(tarea.getCodelecvalv());
					//Tb quito la tarea de la lista de tareasaexec
					ListaTareasaexec.getInstance().delhilotar(tarea);
				}
		}else{
			codelecvalv = Integer.parseInt(tarea.getCodelecvalv())-2001;
		
			if (accionabrir){
				tarea.setRegando(true);
			//Actualizo estado de la valvula
			ListaValvBt22.getInstance().getvalvbt2(codelecvalv).setAbierta(true);
			ListaValvBt22.getInstance().getvalvbt2(codelecvalv).setTareaasoc(tarea.getIdtarea());
			//La meto en Valvsabiertas
			Irrisoft.window.valvsabiertas();
			
			}else{
				tarea.setRegando(false);
				//Actualizo estado de la valvula
				ListaValvBt22.getInstance().getvalvbt2(codelecvalv).setAbierta(false);
				ListaValvBt22.getInstance().getvalvbt2(codelecvalv).setTareaasoc(0);
				//Sea la valvula que sea la quito de la lista de valvulas abiertas
				Irrisoft.window.quitarvalvabiertas(tarea.getCodelecvalv());
				//Tb quito la tarea de la lista de tareasaexec
				ListaTareasaexec.getInstance().delhilotar(tarea);
			}
			//SUSCEPTIBLE DE IMPLEMENTAR ventana gráfica de control de válvulas bt2
			
		}
		
	
	}
	
	
}


}
