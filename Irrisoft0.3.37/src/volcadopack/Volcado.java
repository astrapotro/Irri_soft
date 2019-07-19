package volcadopack;

import irrisoftpack.Irrisoft;



public class Volcado implements Runnable {

	//////////////////////////////////////////
	//LA diferencia con el volcado normal es que ésta mantiene la conexión con la remota ABIERTA para mejorar el consumo
	// de ancho de banda !!!
	//
	// HAce falta una tabla nueva en la pasarela que asocie programaciones con cuotas y COPIAR su contenido a la local cada minuto
	

	@Override
	public void run() {
	
		 ConexionVolc con=new ConexionVolc();
		 Runtime r = Runtime.getRuntime();
		 //long memory;
			
		//COnexión es la clase principal !!!		
		
		while (true){
			
			if (con==null){
				con= new ConexionVolc();
			}
			
			con.conectar();					//Conecto con las BBDD
			con.conectal();
			
			//Compruebo que la conexión de red está viva
			try {
					if (!con.connr.isValid(5)) {
						Irrisoft.window.textArea
						.append(("\nConexión con la BBDD remota perdida"));
						con.conectador=false;
						con.cierrar();
						
					}
				
					if (!con.connl.isValid(5)){
						Irrisoft.window.textArea
						.append(("\nConexión con la BBDD local perdida"));
						con.conectadol=false;
						con.cierral();
					}
				} catch (Exception e) {
					//Auto-generated catch block
					e.printStackTrace();
			}

			
				
				if (con.conectadol==true && con.conectador==true)
				{
					con.tarea();//Compruebo si hay tareas y programaciones pendientes !
				}
				
			
			try {
				Thread.sleep(con.config.getTiempo()*1000);	//Aki el proceso duerme lo que diga el archivo de configuración
				
			} catch (InterruptedException e) {
				//Auto-generated catch block
				e.printStackTrace();
			}		
			

			//Invoco al garbage collector para liberar memoria !
			r.gc();
			
//			 memory = r.totalMemory() - r.freeMemory();
//			 System.out.println("Memoria usada por la jvm en megas: " +memory/(1024*1024)+
//			 		"\nmemtot("+r.totalMemory()+") - memlibre("+r.freeMemory()+")");
			

			
		}

	}

}
