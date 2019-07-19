package packvolcado;

public class Pral {

	
	public static void main(String[] args) {
	
		
		Conexion con = new Conexion();	//COnexión es la clase principal !!!
		con.leerconf(con.config);		//Cargo la configuracion del archivo conf_volcado.txt
		
		while (true){
			con.conectar();					//Conecto con las BBDD
			con.conectal();
			
			con.tarea();					//Compruebo si hay tareas pendientes !
			//con.programa();
			
			
			try {
				Thread.sleep(con.config.getTiempo()*1000);	//Aki el proceso duerme lo que diga el archivo de configuración
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			//Cierro conexion con BBDD
			//con.cierrar();
			con.cierral();
			
			if (con.connr == null) {
				con.conectado=false;
			}
			
			//Invoco al garbage collector para liberar memoria !!
			Runtime r = Runtime.getRuntime();
			r.gc();
		}

	}

}
