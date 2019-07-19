package volcadopack;

import java.sql.Date;

//import java.sql.Timestamp;

public class Programacion {

    private int Idprograma;
    private String Codprograma;
    private String Dsprograma;
    private Date Fcinicio;
    private Date FcFin;
    private String Activo;
    private String Codprog;
    private String tipo;
    private int dial, diam, diax, diaj, diav, dias, diad;
    private String modo;
    private String modoini;
    private int Pbloque;
    private int Cuota;
    private String Leido;
    private String Enmarcha;

    // local y tal
    private int Idprogramal;
    // private Timestamp Fcloc;
    private Date dia;
    private String horaini, horafin;

    // Getters y Setters

    public int getIdprograma() {
	return Idprograma;
    }

    public void setIdprograma(int idprograma) {
	Idprograma = idprograma;
    }

    public String getCodprograma() {
	return Codprograma;
    }

    public void setCodprograma(String codprograma) {
	Codprograma = codprograma;
    }

    public String getDsprograma() {
	return Dsprograma;
    }

    public void setDsprograma(String dsprograma) {
	Dsprograma = dsprograma;
    }

    public Date getFcinicio() {
	return Fcinicio;
    }

    public void setFcinicio(Date fcinicio) {
	Fcinicio = fcinicio;
    }

    public Date getFcFin() {
	return FcFin;
    }

    public void setFcFin(Date fcFin) {
	FcFin = fcFin;
    }

    public String getActivo() {
	return Activo;
    }

    public void setActivo(String activo) {
	Activo = activo;
    }

    public String getCodprog() {
	return Codprog;
    }

    public void setCodprog(String codprog) {
	Codprog = codprog;
    }

    public String getTipo() {
	return tipo;
    }

    public void setTipo(String tipo) {
	this.tipo = tipo;
    }

    public int getDial() {
	return dial;
    }

    public void setDial(int dial) {
	this.dial = dial;
    }

    public int getDiam() {
	return diam;
    }

    public void setDiam(int diam) {
	this.diam = diam;
    }

    public int getDiax() {
	return diax;
    }

    public void setDiax(int diax) {
	this.diax = diax;
    }

    public int getDiaj() {
	return diaj;
    }

    public void setDiaj(int diaj) {
	this.diaj = diaj;
    }

    public int getDiav() {
	return diav;
    }

    public void setDiav(int diav) {
	this.diav = diav;
    }

    public int getDias() {
	return dias;
    }

    public void setDias(int dias) {
	this.dias = dias;
    }

    public int getDiad() {
	return diad;
    }

    public void setDiad(int diad) {
	this.diad = diad;
    }

    public String getModo() {
	return modo;
    }

    public void setModo(String modo) {
	this.modo = modo;
    }

    public String getModoini() {
	return modoini;
    }

    public void setModoini(String modoini) {
	this.modoini = modoini;
    }

    public int getPbloque() {
	return Pbloque;
    }

    public void setPbloque(int pbloque) {
	Pbloque = pbloque;
    }

    public int getCuota() {
	return Cuota;
    }

    public void setCuota(int cuota) {
	Cuota = cuota;
    }

    public String getLeido() {
	return Leido;
    }

    public void setLeido(String leido) {
	Leido = leido;
    }

    public String getEnmarcha() {
	return Enmarcha;
    }

    public void setEnmarcha(String enmarcha) {
	Enmarcha = enmarcha;
    }

    public int getIdprogramal() {
	return Idprogramal;
    }

    public void setIdprogramal(int idprogramal) {
	Idprogramal = idprogramal;
    }

    public Date getDia() {
	return dia;
    }

    public void setDia(Date dia) {
	this.dia = dia;
    }

    public String getHoraini() {
	return horaini;
    }

    public void setHoraini(String horaini) {
	this.horaini = horaini;
    }

    public String getHorafin() {
	return horafin;
    }

    public void setHorafin(String horafin) {
	this.horafin = horafin;
    }

}
