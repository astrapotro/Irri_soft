package irrisoftpack;


import java.sql.Timestamp;
import java.util.Calendar;



public class Tarea  {

	// Nueva tarea que tenga que realizar
	private int Idtarea;
	private String Codprog;
	private String Codelecvalv;
	private int Iddstarea;
	private Timestamp Fcexec;
	private int Valor;		
	private String Codtarea;
	private String Dstarea;
	
	//Para ver si son nuevas o nop
	private Timestamp Fcloc;
	
	//Duracion de la abertura de valvula
	protected  long duracion;
	
	//Hilo asociado a la tarea;
	protected Thread hilotar;
	
		

	
	public int getIdtarea() {
		return Idtarea;
	}
	public void setIdtarea(int idtareaexec) {
		Idtarea = idtareaexec;
	}
	public String getCodprog() {
		return Codprog;
	}
	public void setCodprog(String codprog) {
		Codprog = codprog;
	}
	public String getCodelecvalv() {
		return Codelecvalv;
	}
	public void setCodelecvalv(String codelecvalv) {
		Codelecvalv = codelecvalv;
	}
	public int getIddstarea() {
		return Iddstarea;
	}
	public void setIddstarea(int iddstarea) {
		Iddstarea = iddstarea;
	}
	public Timestamp getFcexec() {
		return Fcexec;
	}
	public void setFcexec(Timestamp fcexec) {
		// si es null pongo la fecha actual !!!
		if (fcexec == null){
			Fcexec =  new Timestamp(Calendar.getInstance().getTime().getTime());
		}else{
			Fcexec = fcexec;
		}
		
	}
	public int getValor() {
		return Valor;
	}
	public void setValor(int valor) {
		Valor = valor;
	}

	public String getCodtarea() {
		return Codtarea;
	}
	public void setCodtarea(String codtarea) {
		Codtarea = codtarea;
	}
	public String getDstarea() {
		return Dstarea;
	}
	public void setDstarea(String dstarea) {
		Dstarea = dstarea;
	}
	public Timestamp getFcloc() {
		return Fcloc;
	}
	public void setFcloc (Timestamp fclocal) {
		Fcloc = fclocal;
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
	

}
