package irrisoftpack;


import javax.swing.JLabel;

public class Valvula {
		  
	 
	  private String Codelecvalv;
	  // private int Duracion;
	  //private int Bloque;
	  //Tendría que mirar el estado de la válvula y inicializarla a ese estado.
	  private boolean abierta =false;
	  
	  private int tareaasoc;
	  private int progasoc;
	  private int duracion;
	  
	  private JLabel imgasoc = new JLabel();
	  
	  
	  	public String getCodelecvalv() {
			return Codelecvalv;
		}
		public void setCodelecvalv(String codelecvalv) {
			Codelecvalv = codelecvalv;
		}
//		public int getDuracion() {
//			return Duracion;
//		}
//		public void setDuracion(int duracion) {
//			Duracion = duracion;
//		}
//		public int getBloque() {
//			return Bloque;
//		}
//		public void setBloque(int bloque) {
//			Bloque = bloque;
//		}n
		public boolean isAbierta() {
			return abierta;
		}
		public void setAbierta(boolean abierta) {
			this.abierta = abierta;
		}
		public int getTareaasoc() {
			return tareaasoc;
		}
		public void setTareaasoc(int tareaasoc) {
			this.tareaasoc = tareaasoc;
		}
	
		public JLabel getImgasoc() {
			
			return imgasoc;
		}
		public void setImgasoc(JLabel imgasoc) {
			this.imgasoc = imgasoc;
		}
		public int getProgasoc() {
			return progasoc;
		}
		public void setProgasoc(int progasoc) {
			this.progasoc = progasoc;
		}
		public int getDuracion() {
			return duracion;
		}
		public void setDuracion(int duracion) {
			this.duracion = duracion;
		}
	  
	
}
