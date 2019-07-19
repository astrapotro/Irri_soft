package programapack;

import java.sql.Time;
import java.util.Date;





public class TareaProg {

	// Nueva tarea que tenga que realizar
	private int Idprog;
	private int Idtareaprog;
	private int cuota;

	
	private Date Fechaini;
	private Time Horafin;
	private String Codelecvalv;
	//0 si no hecha, 1 si hecha
	private int hecha;
	
	//Duracion de la abertura de valvula
	protected  long duracion;
	
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
	public Time getHorafin() {
		return Horafin;
	}
	public void setHorafin(Time horafin) {
		Horafin = horafin;
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
	

	

}
