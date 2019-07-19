package valvulaspack;

import java.math.BigInteger;

import irrisoftpack.Semaforo;
import irrisoftpack.SerialDriver;

import javax.swing.JLabel;

public class Valvula {

    private String Codelecvalv;
    private int bloque;
    // Tendría que mirar el estado de la válvula y inicializarla a ese estado.
    private boolean abierta = false;

    private int index;
    private int indexabiertas;
    private int tareaasoc;
    private int progasoc;
    private int duracion;
    private int pluviometria;

    // private float caudal;
    // private int intensidad;

    private float caudalmod;
    private int intensidadmod;
    private int deco;
    private int latch;
    private int maestra;
    private int goteo;
    private boolean propgot;
    private Long numserie;

    // private String codigo;

    // private int tipo;
    private int num_placa;
    private String puerto;
    private Semaforo semaforo;
    private SerialDriver serie;

    private JLabel imgasoc = new JLabel();

    public String getCodelecvalv() {
	return Codelecvalv;
    }

    public void setCodelecvalv(String codelecvalv) {
	Codelecvalv = codelecvalv;
    }

    public int getBloque() {
	return bloque;
    }

    public void setBloque(int bloque) {
	this.bloque = bloque;
    }

    public boolean isAbierta() {
	return abierta;
    }

    public void setAbierta(boolean abierta) {
	this.abierta = abierta;
    }

    public int getTareaasoc() {
	return tareaasoc;
    }

    public void setTareaasoc(int tareaasoc) {
	this.tareaasoc = tareaasoc;
    }

    public JLabel getImgasoc() {

	return imgasoc;
    }

    public void setImgasoc(JLabel imgasoc) {
	this.imgasoc = imgasoc;
    }

    public int getProgasoc() {
	return progasoc;
    }

    public void setProgasoc(int progasoc) {
	this.progasoc = progasoc;
    }

    public int getDuracion() {
	return duracion;
    }

    public void setDuracion(int duracion) {
	this.duracion = duracion;
    }

    public int getPluviometria() {
	return pluviometria;
    }

    public void setPluviometria(int consumo) {
	this.pluviometria = consumo;
    }

    public int isGoteo() {
	return goteo;
    }

    public void setGoteo(int goteo) {
	this.goteo = goteo;
    }

    public float getCaudalmod() {
	return caudalmod;
    }

    public void setCaudalmod(float caudalmod) {
	this.caudalmod = caudalmod;
    }

    public int getIntensidadmod() {
	return intensidadmod;
    }

    public void setIntensidadmod(int intensidadmod) {
	this.intensidadmod = intensidadmod;
    }

    public int isLatch() {
	return latch;
    }

    public void setLatch(int latch) {
	this.latch = latch;
    }

    // public float getCaudal() {
    // return caudal;
    // }
    // public int getIntensidad() {
    // return intensidad;
    // }
    // public void setCaudal(float caudal) {
    // this.caudal = caudal;
    // }
    // public void setIntensidad(int intensidad) {
    // this.intensidad = intensidad;
    // }

    public int isMaestra() {
	return maestra;
    }

    public void setMaestra(int maestra) {
	this.maestra = maestra;
    }

    // public int getTipo() {
    // return tipo;
    // }
    // public void setTipo(int tipo) {
    // this.tipo = tipo;
    // }
    public int getNum_placa() {
	return num_placa;
    }

    public void setNum_placa(int num_placa) {
	this.num_placa = num_placa;
    }

    public String getPuerto() {
	return puerto;
    }

    public void setPuerto(String puerto) {
	this.puerto = puerto;
    }

    public Semaforo getSemaforo() {
	return semaforo;
    }

    public void setSemaforo(Semaforo semaforo) {
	this.semaforo = semaforo;
    }

    public int getIndex() {
	return index;
    }

    public void setIndex(int index) {
	this.index = index;
    }

    public int getDeco() {
	return deco;
    }

    public void setDeco(int deco) {
	this.deco = deco;
    }

    public int getIndexabiertas() {
	return indexabiertas;
    }

    public void setIndexabiertas(int indexabiertas) {
	this.indexabiertas = indexabiertas;
    }

    public SerialDriver getSerie() {
	return serie;
    }

    public void setSerie(SerialDriver serie) {
	this.serie = serie;
    }

    public Long getNumserie() {
        return numserie;
    }

    public void setNumserie(Long numserie) {
        this.numserie = numserie;
    }

}
