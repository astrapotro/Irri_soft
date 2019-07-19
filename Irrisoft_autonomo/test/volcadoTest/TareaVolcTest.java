package volcadoTest;

import static org.junit.Assert.*;

import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import programaTest.ProgramacionTest;
import volcadopack.TareaVolc;

public class TareaVolcTest {
    
    private static Logger logger = LogManager.getLogger(TareaVolcTest.class
	    .getName());
    //public Timestamp f;

    // @Test//Echo y Bien:ACABADO
    public void testgetIdtareaexec() {
	TareaVolc tv1 = new TareaVolc();
	tv1.setIdtareaexec(5);
	assertTrue(tv1.getIdtareaexec() == 5);
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetIdtareaexec() {
	TareaVolc tv2 = new TareaVolc();
	tv2.setIdtareaexec(5);
	assertTrue(tv2.getIdtareaexec() == 5);
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetCodprog() {
	TareaVolc tv3 = new TareaVolc();
	tv3.setCodprog("test");
	assertTrue(tv3.getCodprog() == "test");
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetCodprog() {
	TareaVolc tv4 = new TareaVolc();
	tv4.setCodprog("test");
	assertTrue(tv4.getCodprog() == "test");
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetCodelecvalv() {
	TareaVolc tv5 = new TareaVolc();
	tv5.setCodelecvalv("test");
	assertTrue(tv5.getCodelecvalv() == "test");
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetCodelecvalv() {
	TareaVolc tv6 = new TareaVolc();
	tv6.setCodelecvalv("test");
	assertTrue(tv6.getCodelecvalv() == "test");
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetIddstarea() {
	TareaVolc tv7 = new TareaVolc();
	tv7.setIddstarea(7);
	assertTrue(tv7.getIddstarea() == 7);
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetIddstarea() {
	TareaVolc tv8 = new TareaVolc();
	tv8.setIddstarea(7);
	assertTrue(tv8.getIddstarea() == 7);
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetValor() {
	TareaVolc tv = new TareaVolc();
	tv.setValor(8);
	assertTrue(tv.getValor() == 8);
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetValor() {
	TareaVolc tv = new TareaVolc();
	tv.setValor(8);
	assertTrue(tv.getValor() == 8);
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetValor2() {
	TareaVolc tv = new TareaVolc();
	tv.setValor2(9);
	assertTrue(tv.getValor2() == 9);
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetValor2() {
	TareaVolc tv = new TareaVolc();
	tv.setValor2(9);
	assertTrue(tv.getValor2() == 9);
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetIdtarealocal() {
	TareaVolc tv = new TareaVolc();
	tv.setIdtarealocal(10);
	assertTrue(tv.getIdtarealocal() == 10);
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetIdtarealocal() {
	TareaVolc tv = new TareaVolc();
	tv.setIdtarealocal(10);
	assertTrue(tv.getIdtarealocal() == 10);
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetDstarea() {
	TareaVolc tv = new TareaVolc();
	tv.setDstarea("test");
	assertTrue(tv.getDstarea() == "test");
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetDstarea() {
	TareaVolc tv = new TareaVolc();
	tv.setDstarea("test");
	assertTrue(tv.getDstarea() == "test");
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetCodtarea() {
	TareaVolc tv = new TareaVolc();
	tv.setCodtarea("test");
	assertTrue(tv.getCodtarea() == "test");
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetCodtarea() {
	TareaVolc tv = new TareaVolc();
	tv.setCodtarea("test");
	assertTrue(tv.getCodtarea() == "test");
    }

     @Test//Echo y Bien:ACABADO
    public void testgetFcexec() {
	java.util.Date date = new java.util.Date();
	Timestamp f= new Timestamp(date.getTime());
	TareaVolc tv = new TareaVolc();
	tv.setFcexec(f);
	assertEquals(tv.getFcexec(),f);
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetFcexec() {
	java.util.Date date = new java.util.Date();
	Timestamp f = new Timestamp(date.getTime());
	TareaVolc tv = new TareaVolc();
	tv.setFcexec(f);
	assertTrue(tv.getFcexec() == f);
    }

    // @Test//Echo y Bien:ACABADO
    public void testgetFcloc() {
	java.util.Date date = new java.util.Date();
	Timestamp f = new Timestamp(date.getTime());
	TareaVolc tv = new TareaVolc();
	tv.setFcloc(f);
	assertTrue(tv.getFcloc() == f);
    }

    // @Test//Echo y Bien:ACABADO
    public void testsetFcloc() {
	java.util.Date date = new java.util.Date();
	Timestamp f = new Timestamp(date.getTime());
	TareaVolc tv = new TareaVolc();
	tv.setFcloc(f);
	assertTrue(tv.getFcloc() == f);
    }

}
