package tareaTest;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import programapack.Programacion;
import tareapack.TareaManual;
import valvulaspack.Valvula;

public class TareaManualTest {

    private static Logger logger = LogManager.getLogger(TareaManualTest.class
	    .getName());


    // @Test
    // Echo y Bien:ACABADO
    public void testgetIdtarea() {
	TareaManual tm = new TareaManual();
	tm.setIdtarea(1);
	assertTrue(tm.getIdtarea() == 1);

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testsetIdtarea() {
	TareaManual tm = new TareaManual();
	tm.setIdtarea(1);
	assertTrue(tm.getIdtarea() == 1);

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testgetCodprog() {
	TareaManual tm = new TareaManual();
	tm.setCodprog("test");
	assertTrue(tm.getCodprog() == "test");

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testsetCodprog() {
	TareaManual tm = new TareaManual();
	tm.setCodprog("test");
	assertTrue(tm.getCodprog() == "test");

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testgetCodelecvalv() {
	TareaManual tm = new TareaManual();
	tm.setCodelecvalv("test");
	assertTrue(tm.getCodelecvalv() == "test");

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testsetCodelecvalv() {
	TareaManual tm = new TareaManual();
	tm.setCodelecvalv("test");
	assertTrue(tm.getCodelecvalv() == "test");

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testgetIddstarea() {
	TareaManual tm = new TareaManual();
	tm.setIddstarea(3);
	assertTrue(tm.getIddstarea() == 3);

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testsetIddstarea() {
	TareaManual tm = new TareaManual();
	tm.setIddstarea(3);
	assertTrue(tm.getIddstarea() == 3);

    }

//     @Test
    // Mirar ya que son TIMESTAMP
    public void testgetFcexec() {
	TareaManual tm = new TareaManual();
	Calendar calendar = Calendar.getInstance();
	Date now = calendar.getTime();
	Timestamp fce = new Timestamp(now.getTime());
	tm.setFcexec(fce);
	assertEquals(fce, tm.getFcexec());

    }

    // @Test
    // Mirar ya que son TIMESTAMP
    public void testsetFcexec() {
	TareaManual tm = new TareaManual();
	Calendar calendar = Calendar.getInstance();
	Date now = calendar.getTime();
	Timestamp fce = new Timestamp(now.getTime());
	tm.setFcexec(fce);
	assertEquals(fce, tm.getFcexec());

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testgetValor() {
	TareaManual tm = new TareaManual();
	tm.setValor(4);
	assertTrue(tm.getValor() == 4);

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testsetValor() {
	TareaManual tm = new TareaManual();
	tm.setValor(4);
	assertTrue(tm.getValor() == 4);

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testgetCodtarea() {
	TareaManual tm = new TareaManual();
	tm.setCodtarea("test");
	assertTrue(tm.getCodtarea() == "test");

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testsetCodtarea() {
	TareaManual tm = new TareaManual();
	tm.setCodtarea("test");
	assertTrue(tm.getCodtarea() == "test");

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testgetDstarea() {
	TareaManual tm = new TareaManual();
	tm.setDstarea("test");
	assertTrue(tm.getDstarea() == "test");

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testsetDstarea() {
	TareaManual tm = new TareaManual();
	tm.setDstarea("test");
	assertTrue(tm.getDstarea() == "test");

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testgetFcloc() {
	TareaManual tm = new TareaManual();
	java.util.Date date = new java.util.Date();
	Timestamp fcl = new Timestamp(date.getTime());
	tm.setFcloc(fcl);
	assertTrue(tm.getFcloc() == fcl);

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testsetFcloc() {
	TareaManual tm = new TareaManual();
	java.util.Date date = new java.util.Date();
	Timestamp fcl = new Timestamp(date.getTime());
	tm.setFcloc(fcl);
	assertTrue(tm.getFcloc() == fcl);

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testgetDuracion() {
	TareaManual tm = new TareaManual();
	tm.setDuracion(5);
	assertTrue(tm.getDuracion() == 5);

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testsetDuracion() {
	TareaManual tm = new TareaManual();
	tm.setDuracion(5);
	assertTrue(tm.getDuracion() == 5);

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testgetHilotar() {
	TareaManual tm = new TareaManual();
	Thread h1 = new Thread();
	tm.setHilotar(h1);
	assertEquals(tm.getHilotar(), h1);

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testsetHilotar() {
	TareaManual tm = new TareaManual();
	Thread h1 = new Thread();
	tm.setHilotar(h1);
	assertEquals(tm.getHilotar(), h1);

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testgetValorstr() {
	TareaManual tm = new TareaManual();
	tm.setValorstr(6);
	assertTrue(tm.getValorstr() == 6);

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testsetValorstr() {
	TareaManual tm = new TareaManual();
	tm.setValorstr(6);
	assertTrue(tm.getValorstr() == 6);

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testisRegando() {
	TareaManual tm = new TareaManual();
	tm.setRegando(false);
	assertTrue(tm.isRegando() == false);

    }

    // @Test
    // Echo y Bien:ACABADO
    public void testsetRegando() {
	TareaManual tm = new TareaManual();
	tm.setRegando(false);
	assertTrue(tm.isRegando() == false);

    }

}
