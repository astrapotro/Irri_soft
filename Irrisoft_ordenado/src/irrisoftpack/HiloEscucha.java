package irrisoftpack;



public class HiloEscucha implements Runnable {

	// Conexion
	public ConexionDB connDB = new ConexionDB();


	private boolean terminar = false;

	@Override
	public void run() {
		
		abreDBhilo();
		while (terminar!=true) {
			
				// TODO Aki hay que llamar al interruptor del mci !!
				try {
					connDB.tarea();
					Thread.sleep(3000);	//Lo duermo 3 seg
					connDB.programa();
					System.out.println("Hiloescucha durmiendo");

					
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
	}

	public void abreDBhilo() {
		if (connDB.conecta()) {
			Irrisoft.window.lblstatus.setText("     Conectado !");
			Irrisoft.window.btnEmpezar.setText("Parar");
			Irrisoft.window.textArea
							.append("\nConexión con la bbdd local establecida !!");
		}
	}

	public void cierraDBhilo() {
		connDB.cierra();
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
