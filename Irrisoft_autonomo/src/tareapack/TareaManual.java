package tareapack;

import java.sql.Timestamp;
import java.util.Calendar;

public class TareaManual implements Tarea {

    // Nueva tarea que tenga que realizar
    // private Valvula valv;
    private int Idtarea;
    private String Codprog;
    private String Codelecvalv;
    private int Iddstarea;
    private Timestamp Fcexec;
    private int Valor;
    private int Valorstr;
    private String Codtarea;
    private String Dstarea;
    private boolean regando;
    private String leida;
    private int tiemporegado;

    // Para ver si son nuevas o nop
    private Timestamp Fcloc;

    // Duracion de la abertura de valvula
    private int duracion;

    // Hilo asociado a la tarea;
    public Thread hilotar;

    // public Valvula getValv() {
    // return valv;
    // }
    // public void setValv(Valvula valv) {
    // this.valv = valv;
    // }

    @Override
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
	if (fcexec == null) {
	    Fcexec = new Timestamp(Calendar.getInstance().getTime().getTime());
	} else {
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

    public void setFcloc(Timestamp fclocal) {
	Fcloc = fclocal;
    }

    public int getDuracion() {
	return duracion;
    }

    public Thread getHilotar() {
	return hilotar;
    }

    public void setDuracion(int duracion) {
	this.duracion = duracion;
    }

    public void setHilotar(Thread hilotar) {
	this.hilotar = hilotar;
    }

    public int getValorstr() {
	return Valorstr;
    }

    public void setValorstr(int valorstr) {
	Valorstr = valorstr;
    }

    public boolean isRegando() {
	return regando;
    }

    public void setRegando(boolean regando) {
	this.regando = regando;
    }
    
    public String getLeida() {
        return leida;
    }

    public int getTiemporegado() {
        return tiemporegado;
    }

    public void setLeida(String leida) {
        this.leida = leida;
    }

    public void setTiemporegado(int tiempoexec) {
        this.tiemporegado = tiempoexec;
    }

}
