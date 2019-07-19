package programapack;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import tareapack.Tarea;
import tareapack.TareaManual;

public class TareaProg implements Comparable<TareaProg>, Tarea {

    // Nueva tarea que tenga que realizar
    private int Idprog;
    private int Idtarea;
    private int cuota;
    private int bloque;
    private String tipobloque;

    private int tipoplaca;
    private int numplaca;
    private String tipopl;

    private Date Fechaini;
    private Date Fechafin;
    private Time Horaini;
    private Timestamp Fechaini_auto;
    private Time Horafin;
    private int Tiemporegado;
    private String Codelecvalv;
    // 0 si no hecha, 1 si hecha
    private int hecha;
    private boolean abierta;
    private boolean cambiocuota;

    private String puertoserie;

    // Duracion de la abertura de valvula
    protected long duracion;
    protected int duracionini;

    // Hilo asociado a la tarea;
    public Thread hilotar;

    public int getIdprog() {
	return Idprog;
    }

    public void setIdprog(int idtareaexec) {
	Idprog = idtareaexec;
    }

    public long getDuracion() {
	return duracion;
    }

    public Thread getHilotar() {
	return hilotar;
    }

    public void setDuracion(long duracion) {
	this.duracion = duracion;
    }

    public void setHilotar(Thread hilotar) {
	this.hilotar = hilotar;
    }

    public Date getFechaini() {
	return Fechaini;
    }

    public void setFechaini(Date fechaini) {
	Fechaini = fechaini;
    }

    public Date getFechafin() {
	return Fechafin;
    }

    public void setFechafin(Date fechafin) {
	Fechafin = fechafin;
    }

    public Time getHoraini() {
	return Horaini;
    }

    public void setHoraini(Time horaini) {
	Horaini = horaini;
    }

    public String getCodelecvalv() {
	return Codelecvalv;
    }

    public void setCodelecvalv(String codelecvalv) {
	Codelecvalv = codelecvalv;
    }

    @Override
    public int getIdtarea() {
	return Idtarea;
    }

    public void setIdtarea(int idtareaprog) {
	Idtarea = idtareaprog;
    }

    public int isHecha() {
	return hecha;
    }

    public void setHecha(int i) {
	hecha = i;
    }

    public int getCuota() {
	return cuota;
    }

    public void setCuota(int cuota) {
	this.cuota = cuota;
    }

    public Time getHorafin() {
	return Horafin;
    }

    public void setHorafin(Time horafin) {
	Horafin = horafin;
    }

    public int getTipoplaca() {
	return tipoplaca;
    }

    public void setTipoplaca(int tipoplaca) {
	this.tipoplaca = tipoplaca;
    }

    public String getPuertoserie() {
	return puertoserie;
    }

    public void setPuertoserie(String puertoserie) {
	this.puertoserie = puertoserie;
    }

    public int getBloque() {
	return bloque;
    }

    public void setBloque(int bloque) {
	this.bloque = bloque;
    }

    public String getTipobloque() {
	return tipobloque;
    }

    public void setTipobloque(String tipobloque) {
	this.tipobloque = tipobloque;
    }

    public int getTiemporegado() {
	return Tiemporegado;
    }

    public void setTiemporegado(int tiemporegado) {
	this.Tiemporegado = tiemporegado;
    }

    public int getDuracionini() {
	return duracionini;
    }

    public void setDuracionini(int duracionini) {
	this.duracionini = duracionini;
    }

    public boolean isAbierta() {
	return abierta;
    }

    public void setAbierta(boolean abierta) {
	this.abierta = abierta;
    }

    public int getNumplaca() {
	return numplaca;
    }

    public void setNumplaca(int numplaca) {
	this.numplaca = numplaca;
    }

    public String getTipopl() {
	return tipopl;
    }

    public void setTipopl(String tipopl) {
	this.tipopl = tipopl;
    }

    public int getHecha() {
	return hecha;
    }

    public Timestamp getFechaini_auto() {
	return Fechaini_auto;
    }

    public void setFechaini_auto(Timestamp stampa) {
	Fechaini_auto = stampa;
    }

    public boolean isCambiocuota() {
	return cambiocuota;
    }

    public void setCambiocuota(boolean cambiocuota) {
	this.cambiocuota = cambiocuota;
    }

    public int compareTo(TareaProg otratar) {
	return Horaini.compareTo(otratar.Horaini);
    }
    // public boolean isAbierta() {
    // return abierta;
    // }
    // public void setAbierta(boolean abierta) {
    // this.abierta = abierta;
    // }
    //

}
