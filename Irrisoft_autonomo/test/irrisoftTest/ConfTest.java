package irrisoftTest;

import static org.junit.Assert.*;
import irrisoftpack.Conf;

import org.junit.Test;

public class ConfTest {

    /**
     * Test de getIdrasp:
     */
    //@Test 
    public void testgetIdrasp() {
	Conf con = new Conf();
	con.setIdrasp("1");
	assertTrue(con.getIdrasp().contentEquals("1"));

    }

    /**
     * Test de setIdrasp:
     */
    //@Test
    public void testsetIdrasp() {
	Conf con = new Conf();
	con.setIdrasp("1");
	assertTrue(con.getIdrasp().contentEquals("1"));

    }

    /**
     * Test de getHost:
     */
    //@Test
    public void testgetHost() {
	Conf con = new Conf();
	con.setHost("test");
	assertTrue(con.getHost() == "test");

    }

    /**
     * Test de setHost:
     */
    //@Test
    public void testsetHost() {
	Conf con = new Conf();
	con.setHost("test");
	assertTrue(con.getHost() == "test");

    }

    /**
     * Test de getPuerto:
     */
    //@Test
    public void testgetPuerto() {
	Conf con = new Conf();
	con.setPuerto(2);
	assertTrue(con.getPuerto() == 2);

    }

    // @Test//Echo y Bien:
    public void testsetPuerto() {
	Conf con = new Conf();
	con.setPuerto(2);
	assertTrue(con.getPuerto() == 2);

    }

    // @Test//Echo y Bien:
    public void testgetDb() {
	Conf con = new Conf();
	con.setDb("test");
	assertTrue(con.getDb() == "test");

    }

    // @Test//Echo y Bien:
    public void testsetDb() {
	Conf con = new Conf();
	con.setDb("test");
	assertTrue(con.getDb() == "test");

    }

    // @Test//Echo y Bien:
    public void testgetUsuario() {
	Conf con = new Conf();
	con.setUsuario("test");
	assertTrue(con.getUsuario() == "test");

    }

    // @Test//Echo y Bien:
    public void testsetUsuario() {
	Conf con = new Conf();
	con.setUsuario("test");
	assertTrue(con.getUsuario() == "test");

    }

    // @Test//Echo y Bien:
    public void testgetPass() {
	Conf con = new Conf();
	con.setPass("test");
	assertTrue(con.getPass() == "test");

    }

    // @Test//Echo y Bien:
    public void testsetPass() {
	Conf con = new Conf();
	con.setPass("test");
	assertTrue(con.getPass() == "test");

    }

    // @Test//Echo y Bien:
    public void testgetMci() {
	Conf con = new Conf();
	con.setMci("test");
	assertTrue(con.getMci() == "test");

    }

    // @Test//Echo y Bien:
    public void testsetMci() {
	Conf con = new Conf();
	con.setMci("test");
	assertTrue(con.getMci() == "test");

    }

    // @Test//Echo y Bien:
    public void testgetMci2() {
	Conf con = new Conf();
	con.setMci2("test");
	assertTrue(con.getMci2() == "test");

    }

    // @Test//Echo y Bien:
    public void testsetMci2() {
	Conf con = new Conf();
	con.setMci2("test");
	assertTrue(con.getMci2() == "test");

    }

    // @Test//Echo y Bien:
    public void testgetMci3() {
	Conf con = new Conf();
	con.setMci3("test");
	assertTrue(con.getMci3() == "test");

    }

    // @Test//Echo y Bien:
    public void testsetMci3() {
	Conf con = new Conf();
	con.setMci3("test");
	assertTrue(con.getMci3() == "test");

    }

    // @Test//Echo y Bien:
    public void testgetMci4() {
	Conf con = new Conf();
	con.setMci4("test");
	assertTrue(con.getMci4() == "test");

    }

    // @Test//Echo y Bien:
    public void testsetMci4() {
	Conf con = new Conf();
	con.setMci4("test");
	assertTrue(con.getMci4() == "test");

    }

    // @Test//Echo y Bien:
    public void testgetBt2() {
	Conf con = new Conf();
	con.setBt2("test");
	assertTrue(con.getBt2() == "test");

    }

    // @Test//Echo y Bien:
    public void testsetBt2() {
	Conf con = new Conf();
	con.setBt2("test");
	assertTrue(con.getBt2() == "test");

    }

    // @Test//Echo y Bien:
    public void testgetBt22() {
	Conf con = new Conf();
	con.setBt22("test");
	assertTrue(con.getBt22() == "test");

    }

    // @Test//Echo y Bien:
    public void testsetBt22() {
	Conf con = new Conf();
	con.setBt22("test");
	assertTrue(con.getBt22() == "test");

    }

    // @Test//Echo y Bien:
    public void testgetBt23() {
	Conf con = new Conf();
	con.setBt23("test");
	assertTrue(con.getBt23() == "test");

    }

    // @Test//Echo y Bien:
    public void testsetBt23() {
	Conf con = new Conf();
	con.setBt23("test");
	assertTrue(con.getBt23() == "test");

    }

    // @Test//Echo y Bien:
    public void testgetBt24() {
	Conf con = new Conf();
	con.setBt24("test");
	assertTrue(con.getBt24() == "test");

    }

    // @Test//Echo y Bien:
    public void testsetBt24() {
	Conf con = new Conf();
	con.setBt24("test");
	assertTrue(con.getBt24() == "test");

    }

    // @Test//Echo y Bien:
    public void testgetLimitebt() {
	Conf con = new Conf();
	con.setLimitebt(20);
	assertTrue(con.getLimitebt() == 20);

    }

    // @Test//Echo y Bien:
    public void testsetLimitebt() {
	Conf con = new Conf();
	con.setLimitebt(20);
	assertTrue(con.getLimitebt() == 20);

    }

}
