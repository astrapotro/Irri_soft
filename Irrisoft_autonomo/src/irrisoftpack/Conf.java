package irrisoftpack;


public class Conf {

    private String idrasp;

    // Datos conexion local
    private String host;
    private int puerto;
    private String db;
    private String usuario;
    private String pass;
    private int limitebt;
    private String correo;

    // Tiempo de espera a la conexión
    // private int tiempo;
    
    // Puertos
    private String mci, mci2, mci3, mci4;
    private String bt2, bt22, bt23, bt24;
    private String sens;

    public Conf() {

	Irrisoft.home = System.getProperty("user.home");


    }

    /**
     * Cojo el valor de la Idrasp (int).
     * @return
     */
    public String getIdrasp() {
	return idrasp;
    }

    /**
     * Coloco el valor de la Idrasp (int).
     * @param idrasp
     */
    public void setIdrasp(String idrasp) {
	this.idrasp = idrasp;
    }

    /**
     * Cojo el valor del Host (String).
     * @return
     */
    public String getHost() {
	return host;
    }

    /**
     * Coloco el valor del host (String).
     * @param host
     */
    public void setHost(String host) {
	this.host = host;
    }

    /**
     * Cojo el valor del puerto (int).
     * @return
     */
    public int getPuerto() {
	return puerto;
    }

    /**
     * Coloco el valor del puerto (int).
     * @param puerto
     */
    public void setPuerto(int puerto) {
	this.puerto = puerto;
    }

    /**
     * Cojo el valor de Db (String).
     * @return
     */
    public String getDb() {
	return db;
    }

    /**
     * Coloco el valor de Db (String).
     * @param db
     */
    public void setDb(String db) {
	this.db = db;
    }

    /**
     * Cojo el valor de Usuario (String).
     * @return
     */
    public String getUsuario() {
	return usuario;
    }

    /**
     * Coloco el valor de usuario (String).
     * @param usuario
     */
    public void setUsuario(String usuario) {
	this.usuario = usuario;
    }

    /**
     * Cojo el valor del Password (String).
     * @return
     */
    public String getPass() {
	return pass;
    }

    /**
     * Coloco el valor del Password (String).
     * @param pass
     */
    public void setPass(String pass) {
	this.pass = pass;
    }

    // public int getTiempo() {
    // return tiempo;
    // }
    //
    // public void setTiempo(int tiempo) {
    // this.tiempo = tiempo;
    // }

    /**
     * Cojo el valor de MCI (String).
     * @return
     */
    public String getMci() {
	return mci;
    }

    /**
     * Coloco valor de MCI (String).
     * @param mci
     */
    public void setMci(String mci) {
	this.mci = mci;
    }

    /**
     * Cojo el valor de MCI2 (String).
     * @return
     */
    public String getMci2() {
	return mci2;
    }

    /**
     * Coloco valor de MCI2 (String).
     * @param mci2
     */
    public void setMci2(String mci2) {
	this.mci2 = mci2;
    }

    /**
     * Cojo el valor de MCI3 (String).
     * @return
     */
    public String getMci3() {
	return mci3;
    }

    /**
     * Coloco valor de MCI3 (String).
     * @param mci3
     */
    public void setMci3(String mci3) {
	this.mci3 = mci3;
    }

    /**
     * Cojo el valor de MCI4 (String).
     * @return
     */
    public String getMci4() {
	return mci4;
    }

    /**
     * Coloco valor de MC4 (String).
     * @param mci4
     */
    public void setMci4(String mci4) {
	this.mci4 = mci4;
    }

    /**
     * Cojo el valor de BT2 (int).
     * @return
     */
    public String getBt2() {
	return bt2;
    }

    /**
     * Coloco valor de BT2 (String).
     * @param bt2
     */
    public void setBt2(String bt2) {
	this.bt2 = bt2;
    }

    /**
     * Cojo el valor de BT22 (int).
     * @return
     */
    public String getBt22() {
	return bt22;
    }

    /**
     * Coloco valor de BT22 (String).
     * @param bt22
     */
    public void setBt22(String bt22) {
	this.bt22 = bt22;
    }

    /**
     * Cojo el valor de BT23 (int).
     * @return
     */
    public String getBt23() {
	return bt23;
    }

    /**
     * Coloco valor de BT23 (String).
     * @param bt23
     */
    public void setBt23(String bt23) {
	this.bt23 = bt23;
    }

    /**
     * Cojo el valor de BT24 (int).
     * @return
     */
    public String getBt24() {
	return bt24;
    }

    /**
     * Coloco valor de BT24 (String).
     * @param bt24
     */
    public void setBt24(String bt24) {
	this.bt24 = bt24;
    }

    /**
     * Cojo el valor de limiteBT (int).
     * @return
     */
    public int getLimitebt() {
	return limitebt;
    }

    /**
     * Coloco valor de limiteBT (int).
     * @param limitebt
     */
    public void setLimitebt(int limitebt) {
	this.limitebt = limitebt;
    }
    
    /**
     * Cojo el valor de correo (String).
     * @return
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Coloco el valor de correo (String).
     * @param correo
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Cojo el valor del Puerto de sensores.
     * @return
     */
    public String getSens() {
        return sens;
    }

    /**
     * Coloco el valor del Puerto de sensores.
     * @param sens
     */
    public void setSens(String sens) {
        this.sens = sens;
    }

}
