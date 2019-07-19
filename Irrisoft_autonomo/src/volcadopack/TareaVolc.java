package volcadopack;

import java.sql.Timestamp;

public class TareaVolc {

    // Nueva tarea que tenga que realizar
    private int Idtareaexec;
    private String Codprog;
    private String Codelecvalv;
    private int Iddstarea;
    private Timestamp Fcexec;
    private int Valor, Valor2;
    private String Codtarea;
    private String Dstarea;
    // private String Programa;

    // Para comparar con la remota
    private int Idtarealocal;
    private Timestamp Fcloc;

    public int getIdtareaexec() {
	return Idtareaexec;
    }

    public void setIdtareaexec(int idtareaexec) {
	Idtareaexec = idtareaexec;
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
	    java.util.Date date = new java.util.Date();
	    Fcexec = new Timestamp(date.getTime());

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

    public int getIdtarealocal() {
	return Idtarealocal;
    }

    public void setIdtarealocal(int idtarealocal) {
	Idtarealocal = idtarealocal;
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

    public void setFcloc(Timestamp fcloc) {
	Fcloc = fcloc;
    }

    public int getValor2() {
	return Valor2;
    }

    public void setValor2(int valor2) {
	Valor2 = valor2;
    }

    // public String getPrograma() {
    // return Programa;
    // }
    // public void setPrograma(String programa) {
    // Programa = programa;
    // }

    public String getCodtarea() {
	return Codtarea;
    }

    public void setCodtarea(String codtarea) {
	Codtarea = codtarea;
    }

}