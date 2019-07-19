package volcadopack;

public class Conf {

    private String idrasp;

    // Datos conexion remota
    private String hostr;
    private int puertor;
    private String dbr;
    private String userr;
    private String passr;

    // Datos conexion local
    private String hostl;
    private int puertol;
    private String dbl;
    private String usuariol;
    private String passl;

    // Tiempo de espera a la conexión
    private int tiempo;

    public String getIdrasp() {
	return idrasp;
    }

    public void setIdrasp(String idrasp) {
	this.idrasp = idrasp;
    }

    public String getHostr() {
	return hostr;
    }

    public void setHostr(String hostr) {
	this.hostr = hostr;
    }

    public int getPuertor() {
	return puertor;
    }

    public void setPuertor(int puertor) {
	this.puertor = puertor;
    }

    public String getDbr() {
	return dbr;
    }

    public void setDbr(String dbr) {
	this.dbr = dbr;
    }

    public String getUserr() {
	return userr;
    }

    public void setUserr(String userr) {
	this.userr = userr;
    }

    public String getPassr() {
	return passr;
    }

    public void setPassr(String passr) {
	this.passr = passr;
    }

    public String getHostl() {
	return hostl;
    }

    public void setHostl(String hostl) {
	this.hostl = hostl;
    }

    public int getPuertol() {
	return puertol;
    }

    public void setPuertol(int puertol) {
	this.puertol = puertol;
    }

    public String getDbl() {
	return dbl;
    }

    public void setDbl(String dbl) {
	this.dbl = dbl;
    }

    public String getUsuariol() {
	return usuariol;
    }

    public void setUsuariol(String usuariol) {
	this.usuariol = usuariol;
    }

    public String getPassl() {
	return passl;
    }

    public void setPassl(String passl) {
	this.passl = passl;
    }

    public int getTiempo() {
	return tiempo;
    }

    public void setTiempo(int tiempo) {
	this.tiempo = tiempo;
    }

}
