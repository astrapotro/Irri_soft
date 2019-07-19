package valvulaspack;


import javax.swing.JLabel;

public class Valvula {
		  
	 
	  private String Codelecvalv;
	  private int bloque;
	  //Tendría que mirar el estado de la válvula y inicializarla a ese estado.
	  private boolean abierta =false;
	  
	  private int tareaasoc;
	  private int progasoc;
	  private int duracion;
	  private int pluviometria;
	  private boolean goteo;
	  private float caudal;
	  private int intensidad;
	  private float caudalmod;
	  private int intensidadmod;
	  private boolean latch;
	
	  
	  private JLabel imgasoc = new JLabel();
	  
	
	  	public String getCodelecvalv() {
			return Codelecvalv;
		}
		public void setCodelecvalv(String codelecvalv) {
			Codelecvalv = codelecvalv;
		}
		public int getBloque() {
			return bloque;
		}
		public void setBloque(int bloque) {
			this.bloque = bloque;
		}
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
		public int getPluviometria() {
			return pluviometria;
		}
		public void setPluviometria(int consumo) {
			this.pluviometria = consumo;
		}
		public boolean isGoteo() {
			return goteo;
		}
		public float getCaudalmod() {
			return caudalmod;
		}
		public int getIntensidadmod() {
			return intensidadmod;
		}
		public void setGoteo(boolean goteo) {
			this.goteo = goteo;
		}
		public void setCaudalmod(float caudalmod) {
			this.caudalmod = caudalmod;
		}
		public void setIntensidadmod(int intensidadmod) {
			this.intensidadmod = intensidadmod;
		}
		public boolean isLatch() {
			return latch;
		}
		public void setLatch(boolean latch) {
			this.latch = latch;
		}
		public float getCaudal() {
			return caudal;
		}
		public int getIntensidad() {
			return intensidad;
		}
		public void setCaudal(float caudal) {
			this.caudal = caudal;
		}
		public void setIntensidad(int intensidad) {
			this.intensidad = intensidad;
		}

	
}
