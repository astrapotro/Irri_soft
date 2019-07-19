package programaTest;

import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.Time;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import programapack.TareaProg;

public class TareaProgTest {

    private static Logger logger = LogManager.getLogger(TareaProgTest.class
	    .getName());

    // @Test //Echo y Bien:ACABADO
    public void testgetIdprog() {
	TareaProg tp = new TareaProg();
	tp.setIdprog(1);
	assertTrue(tp.getIdprog() == 1);

    }

    // @Test //Echo y Bien:ACABADO
    public void testsetIdprog() {
	TareaProg tp = new TareaProg();
	tp.setIdprog(1);
	assertTrue(tp.getIdprog() == 1);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetDuracion() {
	TareaProg tp = new TareaProg();
	tp.setDuracion(2);
	assertTrue(tp.getDuracion() == 2);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetDuracion() {
	TareaProg tp = new TareaProg();
	tp.setDuracion(2);
	assertTrue(tp.getDuracion() == 2);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetHilotar() {
	TareaProg tp = new TareaProg();
	Thread h1 = new Thread();
	tp.setHilotar(h1);
	assertEquals(tp.getHilotar(), h1);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetHilotar() {
	TareaProg tp = new TareaProg();
	Thread h1 = new Thread();
	tp.setHilotar(h1);
	assertEquals(tp.getHilotar(), h1);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetFechaini() {
	TareaProg tp = new TareaProg();
	Date fi = new Date(1);
	tp.setFechaini(fi);
	assertTrue(tp.getFechaini() == fi);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetFechaini() {
	TareaProg tp = new TareaProg();
	Date fi = new Date(1);
	tp.setFechaini(fi);
	assertTrue(tp.getFechaini() == fi);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetHoraini() {
	TareaProg tp = new TareaProg();
	Time hf = new Time(10);
	tp.setHoraini(hf);
	assertTrue(tp.getHoraini() == hf);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetHoraini() {
	TareaProg tp = new TareaProg();
	Time hf = new Time(10);
	tp.setHoraini(hf);
	assertTrue(tp.getHoraini() == hf);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetCodelecvalv() {
	TareaProg tp = new TareaProg();
	tp.setCodelecvalv("test");
	assertTrue(tp.getCodelecvalv() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetCodelecvalv() {
	TareaProg tp = new TareaProg();
	tp.setCodelecvalv("test");
	assertTrue(tp.getCodelecvalv() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetIdtarea() {
	TareaProg tp = new TareaProg();
	tp.setIdtarea(3);
	assertTrue(tp.getIdtarea() == 3);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetIdtarea() {
	TareaProg tp = new TareaProg();
	tp.setIdtarea(3);
	assertTrue(tp.getIdtarea() == 3);

    }

    // @Test//Echo y Bien:ACABADO
    public void testisHecha() {
	TareaProg tp = new TareaProg();
	tp.setHecha(4);
	assertTrue(tp.isHecha() == 4);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetHecha() {
	TareaProg tp = new TareaProg();
	tp.setHecha(4);
	assertTrue(tp.isHecha() == 4);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetCouta() {
	TareaProg tp = new TareaProg();
	tp.setCuota(5);
	assertTrue(tp.getCuota() == 5);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetCouta() {
	TareaProg tp = new TareaProg();
	tp.setCuota(5);
	assertTrue(tp.getCuota() == 5);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetHorafin() {
	TareaProg tp = new TareaProg();
	Time hf = new Time(10);
	tp.setHorafin(hf);
	assertTrue(tp.getHorafin() == hf);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetHorafin() {
	TareaProg tp = new TareaProg();
	Time hf = new Time(10);
	tp.setHorafin(hf);
	assertTrue(tp.getHorafin() == hf);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetTipoplaca() {
	TareaProg tp = new TareaProg();
	tp.setTipoplaca(6);
	assertTrue(tp.getTipoplaca() == 6);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetTipoplaca() {
	TareaProg tp = new TareaProg();
	tp.setTipoplaca(6);
	assertTrue(tp.getTipoplaca() == 6);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetPuertoserie() {
	TareaProg tp = new TareaProg();
	tp.setPuertoserie("test");
	assertTrue(tp.getPuertoserie() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetPuertoserie() {
	TareaProg tp = new TareaProg();
	tp.setPuertoserie("test");
	assertTrue(tp.getPuertoserie() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetBloque() {
	TareaProg tp = new TareaProg();
	tp.setBloque(7);
	assertTrue(tp.getBloque() == 7);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetBloque() {
	TareaProg tp = new TareaProg();
	tp.setBloque(7);
	assertTrue(tp.getBloque() == 7);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetTipobloque() {
	TareaProg tp = new TareaProg();
	tp.setTipobloque("test");
	assertTrue(tp.getTipobloque() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetTipobloque() {
	TareaProg tp = new TareaProg();
	tp.setTipobloque("test");
	assertTrue(tp.getTipobloque() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetTiemporegado() {
	TareaProg tp = new TareaProg();
	tp.setTiemporegado(8);
	assertTrue(tp.getTiemporegado() == 8);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetTiemporegado() {
	TareaProg tp = new TareaProg();
	tp.setTiemporegado(8);
	assertTrue(tp.getTiemporegado() == 8);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetDuracionini() {
	TareaProg tp = new TareaProg();
	tp.setDuracionini(9);
	assertTrue(tp.getDuracionini() == 9);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetDuracionini() {
	TareaProg tp = new TareaProg();
	tp.setDuracionini(9);
	assertTrue(tp.getDuracionini() == 9);

    }

    // @Test//Echo y Bien:ACABADO
    public void testisAbierta() {
	TareaProg tp = new TareaProg();
	tp.setAbierta(false);
	assertTrue(tp.isAbierta() == false);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetAbierta() {
	TareaProg tp = new TareaProg();
	tp.setAbierta(false);
	assertTrue(tp.isAbierta() == false);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetNumplaca() {
	TareaProg tp = new TareaProg();
	tp.setNumplaca(10);
	assertTrue(tp.getNumplaca() == 10);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetNumplaca() {
	TareaProg tp = new TareaProg();
	tp.setNumplaca(10);
	assertTrue(tp.getNumplaca() == 10);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetTipopl() {
	TareaProg tp = new TareaProg();
	tp.setTipopl("test");
	assertTrue(tp.getTipopl() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetTipopl() {
	TareaProg tp = new TareaProg();
	tp.setTipopl("test");
	assertTrue(tp.getTipopl() == "test");

    }

}
