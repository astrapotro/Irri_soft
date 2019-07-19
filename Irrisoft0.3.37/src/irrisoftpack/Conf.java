package irrisoftpack;

public class Conf {
	
	private int idrasp;
	
	//Datos conexion local
	private String host;
	private int puerto;
	private String db;
	private String usuario;
	private String pass;
	private int limitebt;

	//Tiempo de espera a la conexión 
	//private int tiempo;
	
	//Puertos
	private String mci,mci2;
	private String bt2,bt22;
	
	
	
	public int getIdrasp() {
		return idrasp;
	}

	public void setIdrasp(int idrasp) {
		this.idrasp = idrasp;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPuerto() {
		return puerto;
	}

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

//	public int getTiempo() {
//		return tiempo;
//	}
//
//	public void setTiempo(int tiempo) {
//		this.tiempo = tiempo;
//	}

	public String getMci() {
		return mci;
	}

	public void setMci(String mci) {
		this.mci = mci;
	}

	public String getMci2() {
		return mci2;
	}

	public void setMci2(String mci2) {
		this.mci2 = mci2;
	}

	public String getBt2() {
		return bt2;
	}

	public void setBt2(String bt2) {
		this.bt2 = bt2;
	}

	public String getBt22() {
		return bt22;
	}

	public void setBt22(String bt22) {
		this.bt22 = bt22;
	}

	public int getLimitebt() {
		return limitebt;
	}

	public void setLimitebt(int limitebt) {
		this.limitebt = limitebt;
	}

	
	
}
