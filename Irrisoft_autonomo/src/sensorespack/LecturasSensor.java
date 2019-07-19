package sensorespack;

import java.sql.Timestamp;

public class LecturasSensor {

    private Timestamp fecha;
    private double velocidadAnemometro = 0;
    private int lluvia = 0;
    private double temperatura = 0;
    private boolean intru = false;
    private int riego = 0;
    private String nombreSensor;
    private String unidad_Med;

    /**
     * Cojo la fecha de lectura del sensor.
     * 
     * @return
     */
    public Timestamp getFecha() {
	return fecha;
    }

    /**
     * Coloco la fecha de la lectura del sensor.
     * 
     * @param fecha
     */
    public void setFecha(Timestamp fecha) {
	this.fecha = fecha;
    }

    /**
     * Cojo la lectura de viento del sensor.
     * 
     * @return
     */
    public double getVelocidadAnemometro() {
	return velocidadAnemometro;
    }

    /**
     * Coloco el valor de la lectura viento del sensor.
     * 
     * @param velocidad
     */
    public void setVelocidadAnemometro(double velocidad) {
	this.velocidadAnemometro = velocidad;
    }

    /**
     * Cojo el valor de la lectura de lluvia del sensor.
     * 
     * @return
     */
    public int getLluvia() {
	return lluvia;
    }

    /**
     * Coloco el valor de la lectura de lluvia del sensor.
     * 
     * @param lluvia
     */
    public void setLluvia(int lluvia) {
	this.lluvia = lluvia;
    }

    /**
     * Cojo el valor de la lectura de temperatura del sensor.
     * 
     * @return
     */
    public double getTemperatura() {
	return temperatura;
    }

    /**
     * Coloco el valor de la lectura de lluvia del sensor.
     * 
     * @param medida
     */
    public void setTemperatura(double medida) {
	this.temperatura = medida;
    }

    /**
     * Cojo el valor de la lectura de intrusion del sensor.
     * 
     * @return
     */
    public boolean isIntru() {
	return intru;
    }

    /**
     * Coloco el valor de la lectura de intrusion del sensor.
     * 
     * @param intru
     */
    public void setIntru(boolean intru) {
	this.intru = intru;
    }

    /**
     * Cojo el valor de la lectura de riego(Litros de agua) del sensor.
     * 
     * @return
     */
    public int getRiego() {
	return riego;
    }

    /**
     * Coloco el valor de la lectura de riego (Litros de agua) del sensor.
     * 
     * @param riego
     */
    public void setRiego(int riego) {
	this.riego = riego;
    }

    /**
     * Cojo el nombre del sensor que coje la lectura.
     * 
     * @return
     */
    public String getNombreSensor() {
	return nombreSensor;
    }

    /**
     * Coloco el nombre del sensor.
     * 
     * @param nombreSensor
     */
    public void setNombreSensor(String nombreSensor) {
	this.nombreSensor = nombreSensor;
    }

    /**
     * Cojo la unida de medida de la lectura del sensor de temperatura.
     * 
     * @return
     */
    public String getUnidad_Med() {
	return unidad_Med;
    }

    /**
     * Coloco la unidad de medida del sensor de temperatura.
     * 
     * @param unidad_Med
     */
    public void setUnidad_Med(String unidad_Med) {
	this.unidad_Med = unidad_Med;
    }

}