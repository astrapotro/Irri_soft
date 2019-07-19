package programapack;

import java.sql.Time;
import java.util.Date;





public class TareaProg implements Comparable<TareaProg>{

	// Nueva tarea que tenga que realizar
	private int Idprog;
	private int Idtareaprog;
	private int cuota;
	private int bloque;
	private String tipobloque;

	private int tipoplaca;
	
	private Date Fechaini;
	private Time Horaini;
	private Time Horafin;
	private int Tiemporegado;
	private String Codelecvalv;
	//0 si no hecha, 1 si hecha
	private int hecha;
	//private boolean abierta;
	
	private String puertoserie;
	
	//Duracion de la abertura de valvula
	protected  long duracion;
	protected int duracionini;
	
	//Hilo asociado a la tarea;
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
	public int getIdtareaprog() {
		return Idtareaprog;
	}
	public void setIdtareaprog(int idtareaprog) {
		Idtareaprog = idtareaprog;
	}
	public int isHecha() {
		return hecha;
	}
	public void setHecha(int i) {
		hecha=i;
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
	
	public int compareTo(TareaProg otratar) { 
        return Horaini.compareTo(otratar.Horaini);
    }
//	public boolean isAbierta() {
//		return abierta;
//	}
//	public void setAbierta(boolean abierta) {
//		this.abierta = abierta;
//	}
//	

	

}
