package principal;

import java.sql.Timestamp;

import com.mysql.jdbc.Messages;

public class Consumo {

	private int codProg;
	private Timestamp fechaAlerta;

	private int consumo;

	public Consumo() {

	}


	/**
	 * Obtiene el momento de la alerta (Timestamp).
	 * 
	 * @return
	 */
	public Timestamp getFechaAlerta() {
		return fechaAlerta;
	}

	/**
	 * Coloca el momento de la alerta (Timestamp).
	 * 
	 * @param fechaAlerta
	 */
	public void setFechaAlerta(Timestamp fechaAlerta) {
		this.fechaAlerta = fechaAlerta;
	}

	public int getCodProg() {
		return codProg;
	}

	public void setCodProg(int codProg) {
		this.codProg = codProg;
	}

	public int getConsumo() {
		return consumo;
	}

	public void setConsumo(int consumo) {
		this.consumo = consumo;
	}

}
