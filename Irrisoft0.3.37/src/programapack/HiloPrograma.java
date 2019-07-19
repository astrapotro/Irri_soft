package programapack;


import irrisoftpack.Irrisoft;
import irrisoftpack.SerialDriver;


import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

import valvulaspack.ListaValvBt2;
import valvulaspack.ListaValvBt22;
import valvulaspack.ListaValvMci;
import valvulaspack.ListaValvMci2;


public class HiloPrograma implements Runnable{
		
		private TareaProg tar;
		private Programacion prog;
		private int indicelista;

		private String horact;
		private String horaprog;

		private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		private Date ahora=null;
		private Date horaprogini=null;

		private int tiempoexec;

		private String numvalvu;
		//private boolean ultima = false;
		
//Constructor para poder pasarle la tarea como argumento
public HiloPrograma (TareaProg tar, int z,Programacion prog) {
		        this.tar = tar;
		        this.indicelista = z;
		        this.prog=prog;

		    }
		
		@Override
public synchronized void run() {	
			
			
			//Es BT2, aquí le pongo la limitación de n abiertas
			
			if (Integer.parseInt(tar.getCodelecvalv())>1000){
				
				int abiertasbt2 = 0;
				
				do{
					abiertasbt2 = 0;
					for (int i=0;i<ListaValvBt2.getInstance().getsizeof();i++){
						if (ListaValvBt2.getInstance().getvalvbt2(i).isAbierta())
							abiertasbt2++;
					}
					if (abiertasbt2>=Irrisoft.config.getLimitebt()){
						JOptionPane.showMessageDialog(Irrisoft.window.frmIrrisoft, "Límite de valvulas abiertas en la BT2 alcanzado !\nPruebe a cerrar una válvula si quiere abrir otra\n", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}while(abiertasbt2 >=Irrisoft.config.getLimitebt());	
				
				//Segunda BT2
			}else if (Integer.parseInt(tar.getCodelecvalv())>2000){
				int abiertasbt22 = 0;
				do{
					abiertasbt22 = 0;
					for (int i=0;i<ListaValvBt22.getInstance().getsizeof();i++){
						if (ListaValvBt2.getInstance().getvalvbt2(i).isAbierta())
							abiertasbt22++;
					}
				}while(abiertasbt22 >=Irrisoft.config.getLimitebt());
			}
			
			adminbloque(tar,indicelista);				
		}

private void adminbloque(TareaProg tar,int z) {
			
			System.out.println("TAREA: "+prog.getTareasprog().get(z).getIdtareaprog()+" , bloque" + prog.getTareasprog().get(z).getBloque());			
			
			try {
			//miro si es en serie o en paralelo
			if (tar.getTipobloque().equalsIgnoreCase("S")){
				System.out.println("Es SERIEs");
				//Si es la primera de la lista la ejecuto
				if (z==0){
					eslahora(tar);
				}	
				else{
						//Si es MCI la dejo esperando hasta que la anterior termine
					 if (tar.getTipoplaca()==1 ||tar.getTipoplaca()==2){ //ES MCI
						while(prog.getTareasprog().get(z-1).isHecha()==0){
							Thread.currentThread();
							Thread.sleep(50);
						}
						//Retardo preventivo para la creación de hilos (modificar en rasp???)
//						try {
//							Thread.sleep(100);
//						} catch (InterruptedException e1) {
//							// Auto-generated catch block
//							e1.printStackTrace();
//						}
					
							eslahora(tar);	
							
					 }else if (tar.getTipoplaca()==3 ||tar.getTipoplaca()==4){ //ES BT2
						 //Si es BT2 la dejo esperando hasta que a la anterior le queden 3 segundos para terminar
						 while((prog.getTareasprog().get(z-1).getDuracionini()-prog.getTareasprog().get(z-1).getTiemporegado())>5){
							 //si la anterior está hecha y no ha regado queire decir que se ha pasado de ventana con lo que se borra de tareasprogdia
							 if(prog.getTareasprog().get(z-1).isHecha()==1 && prog.getTareasprog().get(z-1).getTiemporegado()==0){
									break;
								}
								Thread.currentThread();
								Thread.sleep(100);
									
							}
						 eslahora(tar);	
						
					 }

				}
				
				//Es paralelo(s)
			}else if (tar.getTipobloque().equalsIgnoreCase("P")){
				System.out.println("Es PARALELOs");
				 
				//Si es la primera de la lista la ejecuto
				if (z==0){
					eslahora(tar);
				}
				else{
													
					//Sino miro si tiene el mismo bloque que la anterior y si lo tiene la ejecuto
					 if (prog.getTareasprog().get(z).getBloque()==prog.getTareasprog().get(z-1).getBloque()){
						 	eslahora(tar);
					 }else{
						 System.out.println("NO está hecha la anterior y me quedo esperando");
						 if (tar.getTipoplaca()==3 ||tar.getTipoplaca()==4){
							 while((prog.getTareasprog().get(z-1).getDuracionini()-prog.getTareasprog().get(z-1).getTiemporegado())>5){
								 //si la anterior está hecha y no ha regado queire decir que se ha pasado de ventana con lo que se borra de tareasprogdia
									if(prog.getTareasprog().get(z-1).isHecha()==1 && prog.getTareasprog().get(z-1).getTiemporegado()==0){
										break;
									}
									Thread.currentThread();
									Thread.sleep(100);
								}
						 }else if (tar.getTipoplaca()==1 ||tar.getTipoplaca()==2){
							 while(prog.getTareasprog().get(z-1).isHecha()==0){
									Thread.currentThread();
									Thread.sleep(50);
								} 
						 }
							
							eslahora(tar);
					 }
					
				}

			}
			
		} catch (Exception e1) {
			// Auto-generated catch block
			e1.printStackTrace();
		}

	}

//	La tarea espera el tiempo necesario hasta su ejecución !!
//////////////////////////////
public void eslahora (TareaProg prog){
			 		 
			 	//Para calcular el delay de espera de la tarea hasta su ejecución
			 	horact = (new Timestamp(Calendar.getInstance().getTime().getTime())).toString();
			 	horact=horact.substring(11, 19);			 	
			 	horaprog =prog.getHoraini().toString().substring(0, 8);
			 	
				try {
					ahora = formatter.parse(horact);
					horaprogini = formatter.parse(horaprog);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			 	
			 	long delay = (horaprogini.getTime())-(ahora.getTime());
			 	delay =(new Date(delay)).getTime();
			 	long queda=delay;
			 	
			 	//calculohorafin(prog);
//			 	System.out.println("tarea "+prog.getIdtareaprog() +" "+prog.getCodelecvalv()+" "+prog.getIdprog());
//			 	System.out.println("Prog horainieslahora: "+prog.getHoraini());
//			 	System.out.println("Prog horafineslahora: "+prog.getHorafin());
			 	
			 	//Compruebo si la hora de inicio es anterior a la actual para no ejecutar las que sean anteriores !
			 	if (horaprogini.after(ahora)){
			 		pintainfo(prog,false,horact);
				 	//Duermo el hilo hasta que sea la hora 
				 	try {
						if (delay>0L){
							System.out.println("Duermo hasta horaini "+prog.getCodelecvalv());
							
							//TODO MIRAR LA HORA cada 10 segs y si es menos dormir lo que quede...
							
							while (queda>0L ){
								horact = (new Timestamp(Calendar.getInstance().getTime().getTime())).toString();
							 	horact=horact.substring(11, 19);
							 	ahora = formatter.parse(horact);
							 	
							 	System.out.println("IEEEE" +ahora +", " + horaprogini);
							 	System.out.println("queda: "+queda);
							 	
							 	if (horaprogini.compareTo(ahora)<=0){
							 		System.out.println("\nBreak\n");
							 		break;
							 	}
							 	
								if (queda>10000L){
									
									Thread.sleep(10000);
									queda=queda-10000L;
								}else {
									System.out.println("\nquedan menos de 10 segs\n");
									Thread.sleep(queda);
								}
							}
								
							System.out.println("Primera valvula del bloque: "+prog.getCodelecvalv());
							//llamo a abrirvalvulas
							 accionvalv(prog);
						}
							
					} catch (InterruptedException e) {
						//Auto-generated catch block
						e.printStackTrace();
					} catch (ParseException e) {
						// 
						e.printStackTrace();
					}
				 	
			 	}else if (prog.getHorafin().after(ahora))   //Si Hora final despues de hora actual
			 	{
			 		System.out.println(prog.getHoraini());
			 		//Pongo el tiempo que lleva la tarea regado si es que lleva
			 		
			 		pintainfo(prog,true,horact);
			 		
//				 		System.out.println("Prog horaini: "+prog.getHoraini());
//					 	System.out.println("Prog horafin: "+prog.getHorafin());
//					 	System.out.println("Duracion: "+prog.getDuracion());
					 						 		
						 		if (valvabierta(prog)){
						 			regando(prog);
						 			semaforo(prog.getTipoplaca(),true);
									SerialDriver serialcon = new SerialDriver();
						 			cierravalv(prog,serialcon);	
						 			serialcon=null;
						 			
						 		}else{
						 			
						 			accionvalv(prog);
						 		}
					
					 	
				 }else{
					 	///TODO?????
					 
				 		if (valvabierta(prog)){
				 			System.out.println("ABIERTA");
				 			semaforo(prog.getTipoplaca(),true);
							
							SerialDriver serialcon = new SerialDriver();
				 			cierravalv(prog,serialcon);	
				 			serialcon=null;
				 		}else
				 		{
				 			System.out.println("CERRADA");
				 			Irrisoft.window.hiloescucha.connDB.actualizatareaprogpasadas(prog);
							
				 		}
				 }

			 	//Aki se ha pasado de hora con lo que la marco a hecha
			 	Irrisoft.window.hiloescucha.connDB.actualizatareaprogpasadas(prog);
				
		 }
		 
public void accionvalv(TareaProg prog){
			  
			// SECCION CRÍTICA A TRATAR CON EL/LOS SEMAFOROS
			
			//Cojo el semaforo-cerrojo
			semaforo(prog.getTipoplaca(),true);
					
			SerialDriver serialcon = new SerialDriver();
			
			//Lo separo en dos métodos para tener un semaforo en la escritura y otro en la lectura !!!
			abrevalv(prog,serialcon);		

			cierravalv(prog,serialcon);	
			
			serialcon = null;
			
		 }
		 
private void abrevalv(TareaProg prog,SerialDriver serialcon) {
			
			try {
				
				//System.out.println("hilo ha cogido el semaforo: "+prog.getHilotar().getId());
				
				if (prog.getTipoplaca()==3 || prog.getTipoplaca()==4) {
					Thread.currentThread();
					Thread.sleep(500);
				}
				serialcon.conectaserial(prog.getPuertoserie(), prog.getTipoplaca());
				
				System.out.println("Intento abrir el puerto "+prog.getCodelecvalv());
				
			} catch (Exception e) {
				//e.printStackTrace();
				//Si no hay conexion con la placa marco la tareaprog a hecha!!
				if (prog.getTipoplaca()==1){
					Irrisoft.window.textArea.append("\nApertura de la valv "+prog.getCodelecvalv()+" cancelada.");
					Irrisoft.window.textArea.append("\nConecte la primera placa MCI !");
					Irrisoft.window.hiloescucha.connDB.actualizatareaprogpasadas(prog);
					return;
				}else if (prog.getTipoplaca()==2){
					Irrisoft.window.textArea.append("\nApertura de la valv "+prog.getCodelecvalv()+" cancelada.");
					Irrisoft.window.textArea.append("\nConecte la segunda placa MCI !");
					Irrisoft.window.hiloescucha.connDB.actualizatareaprogpasadas(prog);
					return;
				}else if (prog.getTipoplaca()==3){
					Irrisoft.window.textArea.append("\nApertura de la valv "+prog.getCodelecvalv()+" cancelada.");
					Irrisoft.window.textArea.append("\nConecte la primera placa BT2 !");
					Irrisoft.window.hiloescucha.connDB.actualizatareaprogpasadas(prog);
					return;
				}else if (prog.getTipoplaca()==4){
					Irrisoft.window.textArea.append("\nApertura de la valv "+prog.getCodelecvalv()+" cancelada.");
					Irrisoft.window.textArea.append("\nConecte la segunda placa BT2 !");
					Irrisoft.window.hiloescucha.connDB.actualizatareaprogpasadas(prog);
					return;
				}	
			}
			
				//System.out.println(("He abierto el puerto "+prog.getCodelecvalv()));
				serialcon.abrevalv(prog.getCodelecvalv(),prog.getTipoplaca());
				actestvalv(prog,true);			
				serialcon.desconectaserial();
				regando(prog);
			
		}

private void cierravalv(TareaProg prog, SerialDriver serialcon){
			
			//SECCION CRÍTICA A TRATAR CON EL/LOS SEMAFOROS
			
			try {
				//Cojo al semaforo cerrojo 
				semaforo(prog.getTipoplaca(),true);
				
					if (prog.getTipoplaca()==3 || prog.getTipoplaca()==4) {
						Thread.currentThread();
						Thread.sleep(500);
					}
					
				serialcon.conectaserial(prog.getPuertoserie(),prog.getTipoplaca());
			} catch (Exception e) { 
				//e.printStackTrace();
				//Si no hay conexion con la placa marco la tareaprog a hecha!!
				if (prog.getTipoplaca()==1){
					Irrisoft.window.textArea.append("\nCierre de la valv "+prog.getCodelecvalv()+" cancelada.");
					Irrisoft.window.textArea.append("\nConecte la primera placa MCI !");
					Irrisoft.window.hiloescucha.connDB.actualizatareaprogpasadas(prog);
					return;
				}else if (prog.getTipoplaca()==2){
					Irrisoft.window.textArea.append("\nCierre de la valv "+prog.getCodelecvalv()+" cancelada.");
					Irrisoft.window.textArea.append("\nConecte la segunda placa MCI !");
					Irrisoft.window.hiloescucha.connDB.actualizatareaprogpasadas(prog);
					return;
				}else if (prog.getTipoplaca()==3){
					Irrisoft.window.textArea.append("\nCierre de la valv "+prog.getCodelecvalv()+" cancelada.");
					Irrisoft.window.textArea.append("\nConecte la primera placa BT2 !");
					Irrisoft.window.hiloescucha.connDB.actualizatareaprogpasadas(prog);
					return;
				}else if (prog.getTipoplaca()==4){
					Irrisoft.window.textArea.append("\nACierre de la valv "+prog.getCodelecvalv()+" cancelada.");
					Irrisoft.window.textArea.append("\nConecte la segunda placa BT2 !");
					Irrisoft.window.hiloescucha.connDB.actualizatareaprogpasadas(prog);
					return;
				}
			}
				
				
				serialcon.cierravalv(prog.getCodelecvalv (),prog.getTipoplaca());
				actestvalv(prog,false);
			 
				try {
					
					serialcon.desconectaserial();
					
					//Finalmente marco a 1 (hecho) la tarea en la tabla tareasprogdia;
					Irrisoft.window.hiloescucha.connDB.actualizatareaprog(prog);
					
					//Suelto el semarofo cerrojo
					semaforo(prog.getTipoplaca(),false);
						
				} catch (Exception e) {
					//Auto-generated catch block
					e.printStackTrace();
				}
				
		
		}
		
private boolean valvabierta(TareaProg prog){
			boolean estado_apertura=false;
			
			for (int i=0;i<Irrisoft.window.progs.getprogramas().size();i++){
	 			if (prog.getIdprog()==Irrisoft.window.progs.getprogramas().get(i).getIdprograma()){
	 				for(int j=0;j<Irrisoft.window.progs.getprogramas().get(i).getValvuprog().size();j++){
	 					if(prog.getCodelecvalv().equals(Irrisoft.window.progs.getprogramas().get(i).getValvuprog().get(j).getCodelecvalv())){
	 						if (Irrisoft.window.progs.getprogramas().get(i).getValvuprog().get(j).isAbierta()){
	 							estado_apertura=true;
	 						}
	 					}
	 				}
	 			}	
	 		}
			return estado_apertura;
		}
		
private void regando(TareaProg prog){
			
			//System.out.println("Tarea: "+prog.getIdtareaprog());
			//System.out.println("Hilo "+prog.getHilotar().getId() +" durmiendo: " + prog.duracion);
			int posval =0;
			
			//Saco la posición de la valvula en su lista !!
			if (prog.getTipoplaca()==1 ){
				posval=Integer.parseInt(prog.getCodelecvalv())-1;
			}
			else if (prog.getTipoplaca()==2){
				posval=Integer.parseInt(prog.getCodelecvalv())-1;
			
			}else if (prog.getTipoplaca()==3){
				posval=Integer.parseInt(prog.getCodelecvalv())-1001;
			}else if (prog.getTipoplaca()==4){
				posval=Integer.parseInt(prog.getCodelecvalv())-2001;
			}
			
			try {
							//Suelto el semarofo cerrojo
							 semaforo(prog.getTipoplaca(),false);
							 tiempoexec=0;
							 
							// Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
							 
							while(calculoduracion(prog)>0)
							{
								horact = (new Timestamp(Calendar.getInstance().getTime().getTime())).toString();
							 	horact=horact.substring(11, 19);
							 	
							 	if (tiempoexec==0){
							 		tiempoexec=prog.getTiemporegado();
							 	}
							 	
								try {
									ahora = formatter.parse(horact);
								} catch (ParseException e) {
									// 
									e.printStackTrace();
								}
							
								//se ha pasado de la hora final de riego, lo paro !!
								if (ahora.after(prog.getHorafin())){
									System.out.println("Corto porque se ha salido de la ventana de riego");
									prog.setDuracion(0);
									//TODO Enviar informe o alerta
								}
								
								
									if (prog.getDuracion()>=10){
										//semaforo(69,false);
											Thread.sleep((10000)); //SON MILISEGUNDOOOOOOOOOOOOOOS	
											tiempoexec=tiempoexec+10;
											prog.setTiemporegado(tiempoexec);
											//Solo miro y actualizo si la valvula está abierta
											if (prog.getTipoplaca()==1){
												if (Irrisoft.window.valvsmci.getvalvmci(posval).isAbierta()){
													System.out.println(ListaValvMci.getInstance());
													System.out.println(Irrisoft.window.valvsmci);
													Irrisoft.window.hiloescucha.connDB.ponetiemporegado(prog,prog.getTiemporegado());

												}
											}else if (prog.getTipoplaca()==2){
												if (ListaValvMci2.getInstance().getvalvmci(posval).isAbierta())
													Irrisoft.window.hiloescucha.connDB.ponetiemporegado(prog,prog.getTiemporegado());
											}else if (prog.getTipoplaca()==3){
												if (ListaValvBt2.getInstance().getvalvbt2(posval).isAbierta())
													Irrisoft.window.hiloescucha.connDB.ponetiemporegado(prog,prog.getTiemporegado());
											}else if (prog.getTipoplaca()==4){
												if (ListaValvBt22.getInstance().getvalvbt2(posval).isAbierta())
													Irrisoft.window.hiloescucha.connDB.ponetiemporegado(prog,prog.getTiemporegado());
											}
										
									}else if(prog.getDuracion()<10){
												Thread.sleep(prog.getDuracion()*1000); //SON MILISEGUNDOOOOOOOOOOOOOOS	
												tiempoexec=tiempoexec+(int)prog.getDuracion();
												prog.setTiemporegado(tiempoexec);
												
												//Solo miro y actualizo si la valvula está abierta
												if (prog.getTipoplaca()==1){
													if (Irrisoft.window.valvsmci.getvalvmci(posval).isAbierta())
														Irrisoft.window.hiloescucha.connDB.ponetiemporegado(prog,prog.getTiemporegado());
												}else if (prog.getTipoplaca()==2){
													if (ListaValvMci2.getInstance().getvalvmci(posval).isAbierta())
														Irrisoft.window.hiloescucha.connDB.ponetiemporegado(prog,prog.getTiemporegado());
												}else if (prog.getTipoplaca()==3){
													if (ListaValvBt2.getInstance().getvalvbt2(posval).isAbierta())
														Irrisoft.window.hiloescucha.connDB.ponetiemporegado(prog,prog.getTiemporegado());
												}else if (prog.getTipoplaca()==4){
													if (ListaValvBt22.getInstance().getvalvbt2(posval).isAbierta())
														Irrisoft.window.hiloescucha.connDB.ponetiemporegado(prog,prog.getTiemporegado());
												}
											
											}
								}
								//Sería necesario???
//								else{
//									prog.setDuracion(0);
//								}
							
							
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				
		}
						
private int calculoduracion(TareaProg prog) {
			System.out.println("Duracion antes "+prog.getDuracion());
			
			if (tiempoexec==0){
				//Si es de la BT2 le resto unos segundos  a la duracion para que no se me pase de madre por el retardo físico
				if (Integer.parseInt(prog.getCodelecvalv())>1000)
					prog.setDuracionini((int)(prog.getDuracion()-prog.getTiemporegado())-4); //-4
				else
					prog.setDuracionini((int)(prog.getDuracion()-prog.getTiemporegado())-3); //-2
			}	
			else{
				//Cuotas 
				System.out.println("cuota en duracion: "+prog.getCuota());
				if(prog.getDuracion()!=0){
					prog.setDuracion(((prog.getDuracionini()*prog.getCuota())/100)-prog.getTiemporegado());
					System.out.println("Duracion con cuota: "+prog.getDuracion());
					
				}
			}
			
			return (int) prog.getDuracion();
			//double duracion = redondeoduracion(prog);			
		}

private void semaforo (int tipoplaca, boolean coger){		   
			if (coger){
				 if (tipoplaca == 1 || tipoplaca ==2)
				 {
					 try {
							Irrisoft.window.semaforomci.take();
						} catch (InterruptedException e) {
							// Auto-generated catch block
							e.printStackTrace();
						} 
				 }else if (tipoplaca == 3 || tipoplaca ==4){
					 
					 try {
							Irrisoft.window.semaforobt2.take();
						 } catch (InterruptedException e) {
							// Auto-generated catch block
							e.printStackTrace();
						 } 
				 }else if (tipoplaca==69){
					//semaforo para el for ????
				 }
				 
			}else if (!coger){
				
				if (tipoplaca == 1 || tipoplaca ==2)
				 {
					 try {
							Irrisoft.window.semaforomci.release();
							} catch (InterruptedException e) {
							// Auto-generated catch block
							e.printStackTrace();
						} 
				}else if (tipoplaca == 3 || tipoplaca ==4){
			 
					try {
						Irrisoft.window.semaforobt2.release();
						} catch (InterruptedException e) {
						// Auto-generated catch block
						e.printStackTrace();
					} 
			}else if (tipoplaca==69){
				//semaforo para el for ???
			 }
		}		
	  }

public void actestvalv(TareaProg tar,boolean accionabrir){
			numvalvu = tar.getCodelecvalv();
			
			if (tar.getTipoplaca()==1 || tar.getTipoplaca()==2){
				if(tar.getTipoplaca()==1){//Si es la primera placa MCI
					if (accionabrir){
						System.out.println(ListaValvMci.getInstance());
						System.out.println(Irrisoft.window.valvsmci);
						//Actualizo estado de la valvula y la asocio con la tarea (id) y a un programa
						ListaValvMci.getInstance().getvalvmci(Integer.parseInt(tar.getCodelecvalv())-1).setAbierta(true);
						ListaValvMci.getInstance().getvalvmci(Integer.parseInt(tar.getCodelecvalv())-1).setTareaasoc(tar.getIdtareaprog());
						ListaValvMci.getInstance().getvalvmci(Integer.parseInt(tar.getCodelecvalv())-1).setProgasoc(tar.getIdprog());
						//Repinto
						Irrisoft.window.panelmci.interruptor(ListaValvMci.getInstance().getvalvmci(Integer.parseInt(tar.getCodelecvalv())-1),
						ListaValvMci.getInstance().getvalvmci(Integer.parseInt(numvalvu)-1).getImgasoc(),true);
					}else{
						//Reinicio idtarea de la valvula
						ListaValvMci.getInstance().getvalvmci(Integer.parseInt(tar.getCodelecvalv())-1).setTareaasoc(0);
						ListaValvMci.getInstance().getvalvmci(Integer.parseInt(tar.getCodelecvalv())-1).setProgasoc(0);
						ListaValvMci.getInstance().getvalvmci(Integer.parseInt(tar.getCodelecvalv())-1).setAbierta(false);
						
						
						//Repinto
						Irrisoft.window.panelmci.interruptor(ListaValvMci.getInstance().getvalvmci(Integer.parseInt(tar.getCodelecvalv())-1),
														ListaValvMci.getInstance().getvalvmci(Integer.parseInt(numvalvu)-1).getImgasoc(),true);
						
					}
			 }else{//Si es la segunda placa MCI
				 
					if (accionabrir){
						//Actualizo estado de la valvula
						ListaValvMci2.getInstance().getvalvmci(Integer.parseInt(tar.getCodelecvalv())-1).setAbierta(true);
						ListaValvMci2.getInstance().getvalvmci(Integer.parseInt(tar.getCodelecvalv())-1).setTareaasoc(tar.getIdtareaprog());
						ListaValvMci2.getInstance().getvalvmci(Integer.parseInt(tar.getCodelecvalv())-1).setProgasoc(tar.getIdprog());
						
						//Repinto
						Irrisoft.window.panelmci.interruptor(ListaValvMci2.getInstance().getvalvmci(Integer.parseInt(tar.getCodelecvalv())-1),
														ListaValvMci2.getInstance().getvalvmci(Integer.parseInt(numvalvu)-1).getImgasoc(),true);

						
					}else{
						//Asocio una idtarea con la valvula
						ListaValvMci2.getInstance().getvalvmci(Integer.parseInt(tar.getCodelecvalv())-1).setTareaasoc(0);
						ListaValvMci2.getInstance().getvalvmci(Integer.parseInt(tar.getCodelecvalv())-1).setProgasoc(0);
						ListaValvMci2.getInstance().getvalvmci(Integer.parseInt(tar.getCodelecvalv())-1).setAbierta(false);
						
						//Repinto
						Irrisoft.window.panelmci.interruptor(ListaValvMci.getInstance().getvalvmci(Integer.parseInt(tar.getCodelecvalv())-1),
														ListaValvMci.getInstance().getvalvmci(Integer.parseInt(numvalvu)-1).getImgasoc(),true);
					} 
			 }
				
			}else if (tar.getTipoplaca()==3 || tar.getTipoplaca()==4){	//Para la  BT2
				int codelecvalv=0;
				
				if(tar.getTipoplaca()==3){
					codelecvalv = Integer.parseInt(tar.getCodelecvalv())-1001;
					if (accionabrir){
						//Actualizo estado de la valvula
						ListaValvBt2.getInstance().getvalvbt2(codelecvalv).setAbierta(true);
						ListaValvBt2.getInstance().getvalvbt2(codelecvalv).setTareaasoc(tar.getIdtareaprog());
						ListaValvBt2.getInstance().getvalvbt2(codelecvalv).setProgasoc(tar.getIdprog());

					}else{
							//Actualizo estado de la valvula
							ListaValvBt2.getInstance().getvalvbt2(codelecvalv).setAbierta(false);
							ListaValvBt2.getInstance().getvalvbt2(codelecvalv).setTareaasoc(0);
							ListaValvBt2.getInstance().getvalvbt2(codelecvalv).setProgasoc(0);
							//Asocio una idtarea con la valvula
							ListaValvBt2.getInstance().getvalvbt2(codelecvalv).setTareaasoc(tar.getIdtareaprog());
						}
				}else{
					codelecvalv = Integer.parseInt(tar.getCodelecvalv())-2001;
				
					if (accionabrir){
					//Actualizo estado de la valvula
					ListaValvBt22.getInstance().getvalvbt2(codelecvalv).setAbierta(true);
					ListaValvBt22.getInstance().getvalvbt2(codelecvalv).setTareaasoc(tar.getIdtareaprog());
					ListaValvBt22.getInstance().getvalvbt2(codelecvalv).setProgasoc(tar.getIdprog());
					}else{
						//Actualizo estado de la valvula
						ListaValvBt22.getInstance().getvalvbt2(codelecvalv).setAbierta(false);
						ListaValvBt22.getInstance().getvalvbt2(codelecvalv).setTareaasoc(0);
						ListaValvBt22.getInstance().getvalvbt2(codelecvalv).setProgasoc(0);
					}
					//SUSCEPTIBLE DE IMPLEMENTAR ventana gráfica de control de válvulas bt2
					
				}
			}
		  }
	
private void pintainfo(TareaProg prog,boolean pasada,String horactual ){
			Time horaini= null;
			
			if (pasada)
				horaini= Time.valueOf(horactual);
			else
				horaini=prog.getHoraini();
			
			System.out.println(horaini.toString().substring(0, 8)+" hrs, " +
					"abrir valvula "+prog.getCodelecvalv()+" "+prog.duracion+" segundos");
	 		
	 		Irrisoft.window.textArea
			.append("\n   "+horaini.toString().substring(0, 8)+" hrs, " +
					"abrir valvula "+prog.getCodelecvalv()+" "+prog.duracion+" segundos");
			Irrisoft.window.frmIrrisoft.repaint();
			
			prog=null;
			horactual=null;
			horaini = null;
		}
}
