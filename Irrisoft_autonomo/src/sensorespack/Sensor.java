package sensorespack;

import irrisoftpack.Irrisoft;
import irrisoftpack.SerialDriver;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import panelespack.Panelecturasbt2;
import panelespack.Panelecturasens;
import volcadopack.ConexionVolc;

public class Sensor {

    
    private static Logger logger = LogManager.getLogger(Sensor.class
	    .getName());
    // protected int id;
    private String codprog;
    private int error_inf;
    private int error_sup;
    private int frec_env;
    private int frec_lect;
    private boolean ghost;
    private Object instancia;
    private Thread hilosens;
    private int K;
    private String lectura;
    // private int tipo_placa_numvalv;
    private float lecturacau;
    private double med_umbral_max;
    private double med_umbral_min;
    //Atributo para el listener de lectura y pulsos
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    
    private int num_borna;
    private String num_est_asoc;
    private String num_est_prop;
    private int num_placa;
    private String num_sensor;
    private int num_sens;
    private int pulsos;
    private double rang_med_max;
    private double rang_med_min;
    private double rang_sal_max;
    private double rang_sal_min;
    private SerialDriver serial;
    private int t_max_riego;

    private int tipo;

    private String tipo_placa;
    private String uni_med;
    private String uni_sal;
    // Valvulas asociadas para el sensor de humedad;
    private ArrayList<Integer> valvsassoc = new ArrayList<Integer>();
    private int valvula;
    private double voltaje;

    // public int getId() {
    // return id;
    // }
    // public void setId(int id) {
    // this.id = id;
    // }

    public String getCodprog() {
	return codprog;
    }

    public int getError_inf() {
	return error_inf;
    }

    public int getError_sup() {
	return error_sup;
    }

    public int getFrec_env() {
	return frec_env;
    }

    public int getFrec_lect() {
	return frec_lect;
    }

    public Thread getHilosens() {
	return hilosens;
    }

    public int getK() {
	return K;
    }

    public String getLectura() {
	return lectura;
    }

    public float getLecturacau() {
	return lecturacau;
    }

    public double getMed_umbral_max() {
	return med_umbral_max;
    }

    public double getMed_umbral_min() {
	return med_umbral_min;
    }

    public int getNum_borna() {
	return num_borna;
    }

    public String getNum_est_asoc() {
	return num_est_asoc;
    }

    public String getNum_est_prop() {
	return num_est_prop;
    }

    public int getNum_placa() {
	return num_placa;
    }

    public String getNum_sensor() {
	return num_sensor;
    }

    public int getPulsos() {
	return pulsos;
    }

    public double getRang_med_max() {
	return rang_med_max;
    }

    public double getRang_med_min() {
	return rang_med_min;
    }

    public double getRang_sal_max() {
	return rang_sal_max;
    }

    public double getRang_sal_min() {
	return rang_sal_min;
    }

    public SerialDriver getSerial() {
	return serial;
    }

    public int getT_max_riego() {
	return t_max_riego;
    }

    public int getTipo() {
	return tipo;
    }

    public String getTipo_placa() {
	return tipo_placa;
    }

    public String getUni_med() {
	return uni_med;
    }

    public String getUni_sal() {
	return uni_sal;
    }

    public ArrayList<Integer> getValvsassoc() {
	return valvsassoc;
    }

    public int getValvula() {
	return valvula;
    }

    public double getVoltaje() {
	return voltaje;
    }

//    Sensor ponelecturas(Sensor sens) {
//
//	// Es controladora MCI
//	if (sens.getNum_placa() < 5 && sens.getNum_placa() > 0) {
//
//	    if (sens.getNum_borna() > 24 && sens.getNum_borna() < 28) // Sensores
//								      // analógicos
//		Irrisoft.window.panelecturasmci.dalabel(sens.getNum_borna())
//			.setText(
//				sens.getNum_sensor() + ", " + sens.getVoltaje()
//					+ "V, " + sens.getLectura() + " "
//					+ sens.getUni_med());
//	    else //Contador de pulsos
//
//		Irrisoft.window.panelecturasmci.dalabel(sens.getNum_borna())
//			.setText(
//				sens.getNum_sensor() + ", " + sens.getPulsos()
//					+ " pul, " + sens.getLectura() + " "
//					+ sens.getUni_med());
//	    
//	} else if (sens.getNum_placa() >= 5) { // Es una BT2
//
//	    Irrisoft.window.panelecturasbt2.getLbllecturamp().setText(
//		    "" + sens.getNum_sensor() + " , borna: "
//			    + sens.getNum_borna() + " , " + lectura + " "
//			    + sens.getUni_med());
//
//	} else if (sens.getNum_placa() == 0) { // Es la placa de sensores
//	    if (sens.getNum_borna() > 7 && sens.getNum_borna() < 12) // BINARIOS
//		Irrisoft.window.panelecturasens.dalabel(sens.getNum_borna())
//			.setText(
//				sens.getNum_sensor() + " , "
//					+ sens.getLectura() + " "
//					+ sens.getUni_med());
//	    else if (sens.getNum_borna() < 4) // PULSOS
//		Irrisoft.window.panelecturasens.dalabel(sens.getNum_borna())
//			.setText(
//				sens.getNum_sensor() + ", " + sens.getPulsos()
//					+ " pul, " + sens.getLectura() + " "
//					+ sens.getUni_med());
//
//	    else if (sens.getNum_borna() > 3)
//		Irrisoft.window.panelecturasens.dalabel(sens.getNum_borna())
//			.setText(
//				sens.getNum_sensor() + ", " + sens.getVoltaje()
//					+ "V, " + sens.getLectura() + " "
//					+ sens.getUni_med());
//
//	}
//	return sens;
//
//    }

    public void setCodprog(String codprog) {
	this.codprog = codprog;
    }

    public void setError_inf(int error_inf) {
	this.error_inf = error_inf;
    }

    public void setError_sup(int error_sup) {
	this.error_sup = error_sup;
    }

    public void setFrec_env(int freq_env) {
	this.frec_env = freq_env;
    }

    public void setFrec_lect(int freq_lect) {
	this.frec_lect = freq_lect;
    }

    public void setghost(boolean b) {
	// TODO Auto-generated method stub

    }

    public void setHilosens(Thread hilosens) {
	this.hilosens = hilosens;
    }

    public void setK(int k) {
	K = k;
    }

    public void setLectura(String lectura) {
	String vieja=this.lectura;
	this.lectura = lectura;
	changeSupport.firePropertyChange("lectura", vieja, this.lectura);
	
    }

    public void setLectura(double lectura) {
	String s = Double.toString(lectura);
	setLectura(s);
    }
    
    public void setLectura(int lectura) {
	String s = Integer.toString(lectura);
	setLectura(s);
    }
    
    public void setLecturacau(float lecturacau) {
	this.lecturacau = lecturacau;
	
    }

    public void setMed_umbral_max(double med_umbral_max) {
	this.med_umbral_max = med_umbral_max;
    }

    public void setMed_umbral_min(double med_umbral_min) {
	this.med_umbral_min = med_umbral_min;
    }

    public void setNum_borna(int num_borna) {
	this.num_borna = num_borna;
    }

    public void setNum_est_asoc(String num_est_asoc) {
	this.num_est_asoc = num_est_asoc;
    }

    public void setNum_est_prop(String num_est_prop) {
	this.num_est_prop = num_est_prop;
    }

    public void setNum_placa(int num_placa) {
	this.num_placa = num_placa;
    }

    public void setNum_sensor(String num_sensor) {
	this.num_sensor = num_sensor;
    }

    public void setPulsos(int pulsos) {
	int  vieja=this.pulsos;
	this.pulsos = pulsos;
	changeSupport.firePropertyChange("pulsos", vieja, this.pulsos);

	
    }

    public void setRang_med_max(double rang_med_max) {
	this.rang_med_max = rang_med_max;
    }

    public void setRang_med_min(double rang_med_min) {
	this.rang_med_min = rang_med_min;
    }

    public void setRang_sal_max(double rang_sal_max) {
	this.rang_sal_max = rang_sal_max;
    }

    public void setRang_sal_min(double rang_sal_min) {
	this.rang_sal_min = rang_sal_min;
    }

    public void setSerial(SerialDriver serial) {
	this.serial = serial;
    }

    public void setT_max_riego(int t_max_riego) {
	this.t_max_riego = t_max_riego;
    }

    public void setTipo(int tipo) {
	this.tipo = tipo;
    }

    public void setTipo_placa(String tipo_placa) {
	this.tipo_placa = tipo_placa;
    }

    public void setUni_med(String uni_med) {
	this.uni_med = uni_med;
    }

    public void setUni_sal(String uni_sal) {
	this.uni_sal = uni_sal;
    }

    public void setValvsassoc(ArrayList<Integer> valvsassoc) {
	this.valvsassoc = valvsassoc;
    }

    public void setValvula(int valvula) {
	this.valvula = valvula;
    }

    // public int getTipo_placa_numvalv() {
    // return tipo_placa_numvalv;
    // }
    //
    // public void setTipo_placa_numvalv(int tipo_placa_numvalv) {
    // this.tipo_placa_numvalv = tipo_placa_numvalv;
    // }
    
    public void setVoltaje(double voltaje) {
	this.voltaje = voltaje;
    }
    
//    public int getNum_sens() {
//        return num_sens;
//    }
//
//    public void setNum_sens(int num_sen) {
//        this.num_sens = num_sen;
//    }

    public Object getInstancia() {
        return instancia;
    }

    public void setInstancia(Object objeto) {
        this.instancia = objeto;
    }
    


    public void addPropertyChangeListener(String campo, PropertyChangeListener listener)
    {
	logger.info("Estoy en addPropertyChangeListener");
	this.changeSupport.addPropertyChangeListener(campo, listener);
	
    }
    
    public void removePropertyChangeListener(String campo, PropertyChangeListener listener)
    {
	logger.info("Estoy en removePropertyChangeListener");
	this.changeSupport.removePropertyChangeListener(campo,listener);
    }
    
    public synchronized boolean ValidarConexion(){
 	boolean r = true;
 	if (Irrisoft.window.volcado.getCon().getConnr() == null) {
 	    r = false;
 	} else if (Irrisoft.window.volcado.getCon().conectador == false) {
 	    r = false;
 	}
 	return r;
     }

}
