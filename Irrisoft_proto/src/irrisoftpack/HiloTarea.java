package irrisoftpack;





public class HiloTarea implements Runnable{
		
		private Tarea tarea;
		private int pos;
		private String numvalv;
		
		//Constructor para poder pasarle la tarea como argumento
		 public HiloTarea (Tarea tarea, int pos) {
		        this.tarea = tarea;
		        this.pos = pos;
		    }
		
		@Override
		public synchronized void run() {
			
			Irrisoft.window.textArea
			.append("\nAbrir valvula "+tarea.getCodelecvalv()+" durante "+tarea.duracion/1000+" segundos");
			System.out.println("NULL:"+tarea.getCodelecvalv());
			//COMPRUEBO para que PLACA es la tarea !!
			Integer valv = Integer.parseInt(tarea.getCodelecvalv());
			numvalv = tarea.getCodelecvalv();
			
			if(valv<=28){
				accionvalv(tarea,Irrisoft.window.config.getMci(),1);
			}else if (valv>28 && valv<1000){
				accionvalv(tarea,Irrisoft.window.config.getMci2(),2);
			}else if(valv>1000 && valv <2000){
				accionvalv(tarea,Irrisoft.window.config.getBt2(),3);
			}else if (valv >2000){
				accionvalv(tarea,Irrisoft.window.config.getBt22(),4);
			}
			
						
		}

		
		 public synchronized boolean accionvalv(Tarea tar, String puerto,int tipoplaca){
			 
			SerialDriver serialcon = new SerialDriver();
			
			
			try {
				serialcon.conecta(puerto, tipoplaca);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			serialcon.abrevalv(tarea.getCodelecvalv(),tipoplaca);
			gestvalv(tipoplaca,true);
			
			
			serialcon.cierra();
		
			
			if (!ListaTareasaexec.getInstance().getTareas().isEmpty()){

						try {
							System.out.println("Hilo "+Irrisoft.tareas.gettarea(pos).getHilotar().getId()+" durmiendo: " + tarea.duracion);
							//Duermo el proceso tanto como sea + ajuste de segundos (A REVISAR)
							Thread.sleep(tarea.duracion+4000);	
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						} 
					
						try {
							serialcon.conecta(puerto,tipoplaca);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
							serialcon.cierravalv(Irrisoft.tareas.gettarea(pos).getCodelecvalv(),tipoplaca);
							gestvalv(tipoplaca,false);
							serialcon.cierra();
			
							//Llamo a borratarea
							if (Irrisoft.window.hiloescucha.connDB.Borratarea(tar, pos)){
								Irrisoft.window.textArea
								.append("\nTareas "+tarea.getIdtarea()+" y "+(tarea.getIdtarea()-1)+" borradas correctamente.");
							}
			}
			
			return true;
		 }
		 
		 
		 
		private void gestvalv(int tipo,boolean abierta){
			
			if (tipo==1 || tipo==2){
					
				if (abierta){
					//Actualizo estado de la valvula
					ListaValvMci.getInstance().getvalvmci(Integer.parseInt(tarea.getCodelecvalv())).setAbierta(false);
					//Repinto
					Irrisoft.window.panelmci.interruptor(ListaValvMci.getInstance().getvalvmci(tarea.getCodelecvalv()),
													ListaValvMci.getInstance().getvalvmci(Integer.parseInt(numvalv)-1).getImgasoc(),true);

					
				}else{
					//Asocio una idtarea con la valvula
					ListaValvMci.getInstance().getvalvmci(Integer.parseInt(tarea.getCodelecvalv())).setTareaasoc(tarea.getIdtarea());
					ListaValvMci.getInstance().getvalvmci(Integer.parseInt(tarea.getCodelecvalv())).setAbierta(true);
					
					//Repinto
					Irrisoft.window.panelmci.interruptor(ListaValvMci.getInstance().getvalvmci(tarea.getCodelecvalv()),
													ListaValvMci.getInstance().getvalvmci(Integer.parseInt(numvalv)-1).getImgasoc(),true);
					
				}
				
			}else if (tipo==3 || tipo==4){
				
				int codelecvalv = Integer.parseInt(tarea.getCodelecvalv())-1001;
				if (abierta){
				//Actualizo estado de la valvula
				ListaValvBt2.getInstance().getvalvbt2(codelecvalv).setAbierta(false);
				}else{
					//Actualizo estado de la valvula
					ListaValvBt2.getInstance().getvalvbt2(codelecvalv).setAbierta(true);
					//Asocio una idtarea con la valvula
					ListaValvBt2.getInstance().getvalvbt2(codelecvalv).setTareaasoc(tarea.getIdtarea());
				}
				//SUSCEPTIBLE DE IMPLEMENTAR ventana gráfica de control de válvulas bt2
		}
			
		}
}
