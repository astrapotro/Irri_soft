package sensoresTest;

import static org.junit.Assert.*;

import java.util.ArrayList;

import irrisoftpack.SerialDriver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import programaTest.MiHiloPrograma;
import sensorespack.Sensor;

import valvulaspack.Valvula;

public class SensorTest {
    
    private static Logger logger = LogManager.getLogger(SensorTest.class
	    .getName());

    private SerialDriver serie;

    // @Test  //Echo y Bien:ACABADO
    public void testgetCodprog() {
	Sensor s = new Sensor();
	s.setCodprog("test");
	assertTrue(s.getCodprog() == "test");
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetCodprog() {
	Sensor s = new Sensor();
	s.setCodprog("test");
	assertTrue(s.getCodprog() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetNum_placa() {
	Sensor s = new Sensor();
	s.setNum_placa(1);
	assertTrue(s.getNum_placa() == 1);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetNum_placa() {
	Sensor s = new Sensor();
	s.setNum_placa(1);
	assertTrue(s.getNum_placa() == 1);
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetTipo_placa() {
	Sensor s = new Sensor();
	s.setTipo_placa("test");
	assertTrue(s.getTipo_placa() == "test");
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetTipo_placa() {
	Sensor s = new Sensor();
	s.setTipo_placa("test");
	assertTrue(s.getTipo_placa() == "test");
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetNum_sensor() {
	Sensor s = new Sensor();
	s.setNum_sensor("test");
	assertTrue(s.getNum_sensor() == "test");
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetNum_sensor() {
	Sensor s = new Sensor();
	s.setNum_sensor("test");
	assertTrue(s.getNum_sensor() == "test");
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetNum_borna() {
	Sensor s = new Sensor();
	s.setNum_borna(2);
	assertTrue(s.getNum_borna() == 2);
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetNum_borna() {
	Sensor s = new Sensor();
	s.setNum_borna(2);
	assertTrue(s.getNum_borna() == 2);
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetUni_med() {
	Sensor s = new Sensor();
	s.setUni_med("test");
	assertTrue(s.getUni_med() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetUni_med() {
	Sensor s = new Sensor();
	s.setUni_med("test");
	assertTrue(s.getUni_med() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetUni_sal() {
	Sensor s = new Sensor();
	s.setUni_sal("test");
	assertTrue(s.getUni_sal() == "test");
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetUni_sal() {
	Sensor s = new Sensor();
	s.setUni_sal("test");
	assertTrue(s.getUni_sal() == "test");
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetRang_med_min() {
	Sensor s = new Sensor();
	s.setRang_med_min(1.2);
	assertTrue(s.getRang_med_min() == 1.2);
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetRang_med_min() {
	Sensor s = new Sensor();
	s.setRang_med_min(1.2);
	assertTrue(s.getRang_med_min() == 1.2);
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetRang_med_max() {
	Sensor s = new Sensor();
	s.setRang_med_max(2.2);
	assertTrue(s.getRang_med_max() == 2.2);
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetRang_med_max() {
	Sensor s = new Sensor();
	s.setRang_med_max(2.2);
	assertTrue(s.getRang_med_max() == 2.2);
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetRang_sal_min() {
	Sensor s = new Sensor();
	s.setRang_sal_min(3.2);
	assertTrue(s.getRang_sal_min() == 3.2);
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetRang_sal_min() {
	Sensor s = new Sensor();
	s.setRang_sal_min(3.2);
	assertTrue(s.getRang_sal_min() == 3.2);
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetRang_sal_max() {
	Sensor s = new Sensor();
	s.setRang_sal_max(4.2);
	assertTrue(s.getRang_sal_max() == 4.2);
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetRang_sal_max() {
	Sensor s = new Sensor();
	s.setRang_sal_max(4.2);
	assertTrue(s.getRang_sal_max() == 4.2);
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetMed_umbral_min() {
	Sensor s = new Sensor();
	s.setMed_umbral_min(5.2);
	assertTrue(s.getMed_umbral_min() == 5.2);
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetMed_umbral_min() {
	Sensor s = new Sensor();
	s.setMed_umbral_min(5.2);
	assertTrue(s.getMed_umbral_min() == 5.2);
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetMed_umbral_max() {
	Sensor s = new Sensor();
	s.setMed_umbral_max(6.2);
	assertTrue(s.getMed_umbral_max() == 6.2);
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetMed_umbral_max() {
	Sensor s = new Sensor();
	s.setMed_umbral_max(6.2);
	assertTrue(s.getMed_umbral_max() == 6.2);
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetFrec_lect() {
	Sensor s = new Sensor();
	s.setFrec_lect(4);
	assertTrue(s.getFrec_lect() == 4);
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetFreq_lect() {
	Sensor s = new Sensor();
	s.setFrec_lect(4);
	assertTrue(s.getFrec_lect() == 4);
    }

     @Test//Echo y Bien:ACABADO
    public void testgetFreq_env() {
	Sensor s = new Sensor();
	s.setFrec_env(5);
	assertEquals(s.getFrec_env(),5);
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetFreq_env() {
	Sensor s = new Sensor();
	s.setFrec_env(5);
	assertTrue(s.getFrec_env() == 5);
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetK() {
	Sensor s = new Sensor();
	s.setK(7);
	assertTrue(s.getK() == 7);
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetK() {
	Sensor s = new Sensor();
	s.setK(7);
	assertTrue(s.getK() == 7);
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetError_sup() {
	Sensor s = new Sensor();
	s.setError_sup(8);
	assertTrue(s.getError_sup() == 8);
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetError_sup() {
	Sensor s = new Sensor();
	s.setError_sup(8);
	assertTrue(s.getError_sup() == 8);
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetError_inf() {
	Sensor s = new Sensor();
	s.setError_inf(9);
	assertTrue(s.getError_inf() == 9);
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetError_inf() {
	Sensor s = new Sensor();
	s.setError_inf(9);
	assertTrue(s.getError_inf() == 9);
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetT_max_riego() {
	Sensor s = new Sensor();
	s.setT_max_riego(10);
	assertTrue(s.getT_max_riego() == 10);
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetT_max_riego() {
	Sensor s = new Sensor();
	s.setT_max_riego(10);
	assertTrue(s.getT_max_riego() == 10);
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetNum_est_prop() {
	Sensor s = new Sensor();
	s.setNum_est_prop("test");
	assertTrue(s.getNum_est_prop() == "test");
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetNum_est_prop() {
	Sensor s = new Sensor();
	s.setNum_est_prop("test");
	assertTrue(s.getNum_est_prop() == "test");
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetNum_est_asoc() {
	Sensor s = new Sensor();
	s.setNum_est_asoc("test");
	assertTrue(s.getNum_est_asoc() == "test");
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetNum_est_asoc() {
	Sensor s = new Sensor();
	s.setNum_est_asoc("test");
	assertTrue(s.getNum_est_asoc() == "test");
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetTipo() {
	Sensor s = new Sensor();
	s.setTipo(11);
	assertTrue(s.getTipo() == 11);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetTipo() {
	Sensor s = new Sensor();
	s.setTipo(11);
	assertTrue(s.getTipo() == 11);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetSerial() {
	Sensor s = new Sensor();
	s.setSerial(serie);
	assertTrue(s.getSerial() == serie);
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetSerial() {
	Sensor s = new Sensor();
	s.setSerial(serie);
	assertTrue(s.getSerial() == serie);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetHilosens() {
	Sensor s = new Sensor();
	Thread h1 = new Thread();
	s.setHilosens(h1);
	assertEquals(s.getHilosens(),h1);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetHilosens() {
	Sensor s = new Sensor();
	Thread h1 = new Thread();
	s.setHilosens(h1);
	assertEquals(s.getHilosens(),h1);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetLecturacau() {
	Sensor s = new Sensor();
	s.setLecturacau(1.1f);
	assertTrue(s.getLecturacau() == 1.1f);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetLecturacau() {
	Sensor s = new Sensor();
	s.setLecturacau(1.1f);
	assertTrue(s.getLecturacau() == 1.1f);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetValvula() {
	Sensor s = new Sensor();
	s.setValvula(13);
	assertTrue(s.getValvula() == 13);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetValvula() {
	Sensor s = new Sensor();
	s.setValvula(13);
	assertTrue(s.getValvula() == 13);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetValvsassoc() {
	Sensor s = new Sensor();
	int a = 5;
	ArrayList<Integer>valv = new ArrayList<Integer>();
	valv.add(a);
	s.setValvsassoc(valv);
	assertEquals(valv, s.getValvsassoc());

    }

//     @Test//Echo y Bien:ACABADO
    public void testsetgetValvsassoc() {
	Sensor s = new Sensor();
	int a = 5;
	ArrayList<Integer>valv = new ArrayList<Integer>();
	valv.add(a);
	s.setValvsassoc(valv);
	assertEquals(valv, s.getValvsassoc());

    }

}
