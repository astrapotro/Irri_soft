package alertaspack;


import java.sql.Timestamp;
import java.util.Date;

public class Alerta {

    private int codAlerta;
    private Timestamp fechaAlerta;
    private String descripcion;
    
    public Alerta(){

	
    }
    
    /**
     * Obtiene el código de la alerta. (int).
     * @return
     */
    public int getCodAlerta() {
        return codAlerta;
    }

    /**
     * Coloca el valor de la alerta (int).
     * @param codAlerta
     */
    public void setCodAlerta(int codAlerta) {
        this.codAlerta = codAlerta;
    }
    
    /**
     * Obtiene la descripción de la alerta (String).
     * @return
     */
    public String getDescripcion(){
	return descripcion;
    }
    
    /**
     * Coloca la descripción de la alerta (String).
     * @param desc
     */
    public void setDescripcion(String desc){
	this.descripcion=desc;
    }
    
    /**
     * Obtiene el momento de la alerta (Timestamp).
     * @return
     */
    public Timestamp getFechaAlerta() {
        return fechaAlerta;
    }

    /**
     * Coloca el momento de la alerta (Timestamp).
     * @param fechaAlerta
     */
    public void setFechaAlerta(Timestamp fechaAlerta) {
        this.fechaAlerta = fechaAlerta;
    }

   
    /**
     * Paso el codigo de alerta a una 
     * descripción, mediante una tabla properties.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return Integer.toString(codAlerta)+": "+ Messages.getString("Alerta."+codAlerta); 
    }



}
