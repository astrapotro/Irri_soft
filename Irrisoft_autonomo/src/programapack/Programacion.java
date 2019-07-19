package programapack;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import org.joda.time.DateTime;

import valvulaspack.Valvula;

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
    private int dial = 0, diam = 0, diax = 0, diaj = 0, diav = 0, dias = 0,
	    diad = 0;
    private String modo;
    private String modoini;
    private int Pbloque;
    private int Cuota;
    private String Leido;
    private String Enmarcha;
    private Date ultdeberes;
    public boolean leidop = false;

    private ArrayList<Valvula> valvuprog;
    private ArrayList<Date> days;
    private ArrayList<Time> horasini;
    private ArrayList<Time> horasfin;
    private ArrayList<TareaProg> tareasprog;

    // private LinkedList<Thread> hilostareasprog;

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

    public ArrayList<Valvula> getValvuprog() {

	if (valvuprog == null) {
	    valvuprog = new ArrayList<Valvula>();
	    return valvuprog;
	} else
	    return valvuprog;
    }

    public void setValvuprog(ArrayList<Valvula> valvuprog) {
	this.valvuprog = valvuprog;
    }

    public ArrayList<Date> getDays() {
	if (days == null) {
	    days = new ArrayList<Date>();
	    return days;
	} else
	    return days;
    }

    public void setDays(ArrayList<Date> days) {
	this.days = days;
    }

    public ArrayList<TareaProg> getTareasprog() {
	if (tareasprog == null) {
	    tareasprog = new ArrayList<TareaProg>();
	    return tareasprog;
	} else
	    return tareasprog;
    }

    public void setTareasprog(ArrayList<TareaProg> tareasprog) {
	this.tareasprog = tareasprog;
    }

    public Date getUltdeberes() {
	return ultdeberes;
    }

    public void setUltdeberes(Date ultdeberes) {
	this.ultdeberes = ultdeberes;
    }

    // public LinkedList<Thread> getHilostareasprog() {
    // if (hilostareasprog==null){
    // hilostareasprog = new LinkedList<Thread>();
    // return hilostareasprog;
    // }else
    // return hilostareasprog;
    // }
    // public void setHilostareasprog(LinkedList<Thread> hilostareasprog) {
    // this.hilostareasprog = hilostareasprog;
    // }

    public ArrayList<Time> getHorasfin() {
	if (horasfin == null) {
	    horasfin = new ArrayList<Time>();
	    return horasfin;
	} else
	    return horasfin;
    }

    public ArrayList<Time> getHorasini() {
	if (horasini == null) {
	    horasini = new ArrayList<Time>();
	    return horasini;
	} else
	    return horasini;
    }

    public void setHorasini(ArrayList<Time> horas) {
	this.horasini = horas;
    }

    public void setHorasfin(ArrayList<Time> horasfin) {
	this.horasfin = horasfin;
    }

}
