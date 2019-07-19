package valvulasTest;

import static org.junit.Assert.*;
import irrisoftpack.Semaforo;
import irrisoftpack.SerialDriver;

import javax.swing.JLabel;

import org.junit.Test;

import valvulaspack.Valvula;

public class ValvulaTest {

    private JLabel imgasoc = new JLabel();
    private Semaforo semaforo;
    private SerialDriver serie;

//     @Test//Echo y Bien:ACABADO
    public void testgetCodelecvalv() {
	Valvula v = new Valvula();
	v.setCodelecvalv("test");
	assertTrue(v.getCodelecvalv() == "test");

    }

//     @Test//Echo y Bien:ACABADO
    public void testsetCodelecvalv() {
	Valvula v = new Valvula();
	v.setCodelecvalv("test");
	assertTrue(v.getCodelecvalv() == "test");

    }

//     @Test//Echo y Bien:ACABADO
    public void testgetBloque() {
	Valvula v = new Valvula();
	v.setBloque(1);
	assertTrue(v.getBloque() == 1);

    }

//     @Test//Echo y Bien:ACABADO
    public void testsetBloque() {
	Valvula v = new Valvula();
	v.setBloque(1);
	assertTrue(v.getBloque() == 1);

    }

//     @Test//Echo y Bien:ACABADO
    public void testisAbierta() {
	Valvula v = new Valvula();
	v.setAbierta(false);
	assertTrue(v.isAbierta() == false);

    }

//     @Test//Echo y Bien:ACABADO
    public void testsetAbierta() {
	Valvula v = new Valvula();
	v.setAbierta(false);
	assertTrue(v.isAbierta() == false);

    }

//     @Test//Echo y Bien:ACABADO
    public void testgetTareaasoc() {
	Valvula v = new Valvula();
	v.setTareaasoc(2);
	assertTrue(v.getTareaasoc() == 2);

    }

//     @Test//Echo y Bien:ACABADO
    public void testsetTareaasoc() {
	Valvula v = new Valvula();
	v.setTareaasoc(2);
	assertTrue(v.getTareaasoc() == 2);

    }

//     @Test//Echo y Bien:ACABADO
    public void testgetImgasoc() {
	Valvula v = new Valvula();
	v.setImgasoc(imgasoc);
	assertTrue(v.getImgasoc() == imgasoc);
    }

//     @Test//Echo y Bien:ACABADO
    public void testsetImgasoc() {
	Valvula v = new Valvula();
	v.setImgasoc(imgasoc);
	assertTrue(v.getImgasoc() == imgasoc);

    }

//     @Test//Echo y Bien:ACABADO
    public void testgetProgasoc() {
	Valvula v = new Valvula();
	v.setProgasoc(3);
	assertTrue(v.getProgasoc() == 3);

    }

//     @Test//Echo y Bien:ACABADO
    public void testsetProgasoc() {
	Valvula v = new Valvula();
	v.setProgasoc(3);
	assertTrue(v.getProgasoc() == 3);

    }

//     @Test//Echo y Bien:ACABADO
    public void testgetDuracion() {
	Valvula v = new Valvula();
	v.setDuracion(4);
	assertTrue(v.getDuracion() == 4);

    }

//     @Test//Echo y Bien:ACABADO
    public void testsetDuracion() {
	Valvula v = new Valvula();
	v.setDuracion(4);
	assertTrue(v.getDuracion() == 4);

    }

//     @Test//Echo y Bien:ACABADO
    public void testgetPluviometria() {
	Valvula v = new Valvula();
	v.setPluviometria(15);
	assertTrue(v.getPluviometria() == 15);

    }

//     @Test//Echo y Bien:ACABADO
    public void testsetPluviometria() {
	Valvula v = new Valvula();
	v.setPluviometria(15);
	assertTrue(v.getPluviometria() == 15);

    }

//     @Test//Echo y Bien:ACABADO
    public void testisGoteo() {
	Valvula v = new Valvula();
	v.setGoteo(8);
	assertTrue(v.isGoteo() == 8);
    }

//     @Test//Echo y Bien:ACABADO
    public void testsetGoteo() {
	Valvula v = new Valvula();
	v.setGoteo(8);
	assertTrue(v.isGoteo() == 8);
    }

//     @Test//Echo y Bien:ACABADO
    public void testgetCaudalmod() {
	Valvula v = new Valvula();
	v.setCaudalmod(1);
	assertTrue(v.getCaudalmod() == 1);

    }

//     @Test//Echo y Bien:ACABADO
    public void testsetCaudalmod() {
	Valvula v = new Valvula();
	v.setCaudalmod(1.1f);
	assertTrue(v.getCaudalmod() == 1.1f);

    }

//     @Test//Echo y Bien:ACABADO
    public void testgetIntensidamod() {
	Valvula v = new Valvula();
	v.setIntensidadmod(12);
	assertTrue(v.getIntensidadmod() == 12);

    }

//     @Test//Echo y Bien:ACABADO
    public void testsetIntensidadmod() {
	Valvula v = new Valvula();
	v.setIntensidadmod(12);
	assertTrue(v.getIntensidadmod() == 12);

    }

//     @Test//Echo y Bien:ACABADO
    public void testisLatch() {
	Valvula v = new Valvula();
	v.setLatch(6);
	assertTrue(v.isLatch() == 6);
    }

//     @Test//Echo y Bien:ACABADO
    public void testsetLatch() {
	Valvula v = new Valvula();
	v.setLatch(6);
	assertTrue(v.isLatch() == 6);
    }

//     @Test//Echo y Bien:ACABADO
    public void testisMaestra() {
	Valvula v = new Valvula();
	v.setMaestra(9);
	assertTrue(v.isMaestra() == 9);

    }

//     @Test//Echo y Bien:ACABADO
    public void testsetMaestra() {
	Valvula v = new Valvula();
	v.setMaestra(9);
	assertTrue(v.isMaestra() == 9);

    }

//     @Test//Echo y Bien:ACABADO
    public void testgetNum_placa() {
	Valvula v = new Valvula();
	v.setNum_placa(5);
	assertTrue(v.getNum_placa() == 5);

    }

//     @Test//Echo y Bien:ACABADO
    public void testsetNum_placa() {
	Valvula v = new Valvula();
	v.setNum_placa(5);
	assertTrue(v.getNum_placa() == 5);

    }

//     @Test//Echo y Bien:ACABADO
    public void testgetPuerto() {
	Valvula v = new Valvula();
	v.setPuerto("test");
	assertTrue(v.getPuerto() == "test");

    }

//     @Test//Echo y Bien:ACABADO
    public void testsetPuerto() {
	Valvula v = new Valvula();
	v.setPuerto("test");
	assertTrue(v.getPuerto() == "test");

    }

//     @Test//Echo y Bien:ACABADO
    public void testgetSemaforo() {
	Valvula v = new Valvula();
	v.setSemaforo(semaforo);
	assertTrue(v.getSemaforo() == semaforo);

    }

//     @Test//Echo y Bien:ACABADO
    public void testsetSemaforo() {
	Valvula v = new Valvula();
	v.setSemaforo(semaforo);
	assertTrue(v.getSemaforo() == semaforo);

    }

//     @Test//Echo y Bien:ACABADO
    public void testgetIndex() {
	Valvula v = new Valvula();
	v.setIndex(6);
	assertTrue(v.getIndex() == 6);

    }

//     @Test//Echo y Bien:ACABADO
    public void testsetIndex() {
	Valvula v = new Valvula();
	v.setIndex(6);
	assertTrue(v.getIndex() == 6);

    }

//     @Test//Echo y Bien:ACABADO
    public void testgetDeco() {
	Valvula v = new Valvula();
	v.setDeco(7);
	assertTrue(v.getDeco() == 7);

    }

//     @Test//Echo y Bien:ACABADO
    public void testsetDeco() {
	Valvula v = new Valvula();
	v.setDeco(7);
	assertTrue(v.getDeco() == 7);

    }

//     @Test//Echo y Bien:ACABADO
    public void testgetIndexabiertas() {
	Valvula v = new Valvula();
	v.setIndexabiertas(9);
	assertTrue(v.getIndexabiertas() == 9);

    }

//     @Test//Echo y Bien:ACABADO
    public void testsetIndexabiertas() {
	Valvula v = new Valvula();
	v.setIndexabiertas(9);
	assertTrue(v.getIndexabiertas() == 9);

    }

//     @Test//Echo y Bien:ACABADO
    public void testgetSerie() {
	Valvula v = new Valvula();
	v.setSerie(serie);
	assertTrue(v.getSerie() == serie);

    }

     //@Test//Echo y Bien:ACABADO
    public void testsetSerie() {
	Valvula v = new Valvula();
	v.setSerie(serie);
	assertTrue(v.getSerie() == serie);

    }
}
