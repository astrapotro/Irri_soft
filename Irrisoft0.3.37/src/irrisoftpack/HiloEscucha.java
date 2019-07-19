package irrisoftpack;



public class HiloEscucha implements Runnable {

	// Conexion
	public ConexionDB connDB = new ConexionDB();
	protected Runtime r = Runtime.getRuntime();
	protected long memory;
	protected int abiertasmci,abiertasmci2,abiertasbt2,abiertasbt22;

	private boolean terminar = false;

	@Override
	public void run() {
		
		abreDBhilo();
		
		while (terminar!=true) {
		
				try {
					connDB.tarea();
					Thread.sleep(3000);	//Lo duermo 3 seg
					connDB.programa();
					System.out.println("Hiloescucha durmiendo");
					
					
					//
					//Miro las valvulas que tengo y no tengo abiertas
//						abiertasmci=0;
//						abiertasmci2=0;
//						abiertasbt2=0;
//						abiertasbt22=0;
//					
//						for (int i=0;i<Irrisoft.window.valvsmci.getsizeof();i++){
//							if (Irrisoft.window.valvsmci.getvalvmci(i).isAbierta())
//								abiertasmci++;
//						}
//						
//						for (int i=0;i<Irrisoft.window.valvsmci2.getsizeof();i++){
//							if (Irrisoft.window.valvsmci2.getvalvmci(i).isAbierta())
//								abiertasmci2++;
//						}
//						
//						for (int i=0;i<Irrisoft.window.valvsbt2.getsizeof();i++){
//							if (Irrisoft.window.valvsbt2.getvalvbt2(i).isAbierta())
//								abiertasbt2++;
//						}
//						
//						for (int i=0;i<Irrisoft.window.valvsbt22.getsizeof();i++){
//							if (Irrisoft.window.valvsbt22.getvalvbt2(i).isAbierta())
//								abiertasbt22++;
//						}
//						
//						System.out.println("\n*************************************");
//						System.out.println("Valvulas abiertas mci:"+abiertasmci );
//						System.out.println("Valvulas abiertas mci2:"+abiertasmci2 );
//						System.out.println("Valvulas abiertas bt2:"+abiertasbt2 );
//						System.out.println("Valvulas abiertas bt22:"+abiertasbt22 );
//						System.out.println("*************************************\n");
					//
					/////////
					
					
					//Invoco al garbage collector para liberar memoria !!
					r.gc();
					memory=r.totalMemory()-r.freeMemory();
					
//					System.out.println("****************************  MEM  *******************************");
//					System.out.println("Memoria usada por la jvm en megas: " +memory/(1024*1024)+
//					 		"\nmemtot("+r.totalMemory()+") - memlibre("+r.freeMemory()+")");
//					System.out.println("*****************************************************************");
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
	}

	public void abreDBhilo() {
		if (connDB.conectal()) {
			Irrisoft.window.lblstatus.setText("     Conectado !");
			Irrisoft.window.btnEmpezar.setText("Parar");
			Irrisoft.window.textArea
							.append("Conexión con la BBDD local establecida!");
		}
	}

	public void cierraDBhilo() {
		connDB.desconectal();
		Irrisoft.window.lblstatus.setText("     Desconectado !");
		Irrisoft.window.btnEmpezar.setText("Empezar");
	}

	public void setTerminar(boolean terminar) {
		this.terminar = terminar;
	}

	public boolean getTerminar() {
		return terminar;
	}
}
