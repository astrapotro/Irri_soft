package volcadoTest;

import static org.junit.Assert.*;

import org.junit.Test;

import volcadopack.Conf;

public class ConfTest {

    // @Test //Echo y Bien:ACABADO
    public void testgetIdrasp() {
	Conf c = new Conf();
	c.setIdrasp("1");
	assertTrue(c.getIdrasp().contentEquals("1"));
    }

    // @Test //Echo y Bien:ACABADO
    public void testsetIdrasp() {
	Conf c = new Conf();
	c.setIdrasp("1");
	assertTrue(c.getIdrasp().contentEquals("1"));

    }

    // @Test //Echo y Bien:ACABADO
    public void testgetHostr() {
	Conf c = new Conf();
	c.setHostr("test");
	assertTrue(c.getHostr() == "test");

    }

    // @Test //Echo y Bien:ACABADO
    public void testsetHostr() {
	Conf c = new Conf();
	c.setHostr("test");
	assertTrue(c.getHostr() == "test");

    }

//    // @Test //Echo y Bien:ACABADO
//    public void testgetPuertor() {
//	Conf c = new Conf();
//	c.setPuertor(2);
//	assertTrue(c.getPuertor() == 2);
//
//    }
//
//    // @Test //Echo y Bien:ACABADO
//    public void testsetPuertor() {
//	Conf c = new Conf();
//	c.setPuertor(2);
//	assertTrue(c.getPuertor() == 2);
//
//    }

    // @Test //Echo y Bien:ACABADO
    public void testgetDbr() {
	Conf c = new Conf();
	c.setDbr("test");
	assertTrue(c.getDbr() == "test");

    }

    // @Test //Echo y Bien:ACABADO
    public void testsetDbr() {
	Conf c = new Conf();
	c.setDbr("test");
	assertTrue(c.getDbr() == "test");

    }

    // @Test //Echo y Bien:ACABADO
    public void testgetUserr() {
	Conf c = new Conf();
	c.setUserr("test");
	assertTrue(c.getUserr() == "test");

    }

    // @Test //Echo y Bien:ACABADO
    public void testsetUserr() {
	Conf c = new Conf();
	c.setUserr("test");
	assertTrue(c.getUserr() == "test");

    }

    // @Test //Echo y Bien:ACABADO
    public void testgetPassr() {
	Conf c = new Conf();
	c.setPassr("test");
	assertTrue(c.getPassr() == "test");

    }

    // @Test //Echo y Bien:ACABADO
    public void testsetPassr() {
	Conf c = new Conf();
	c.setPassr("test");
	assertTrue(c.getPassr() == "test");

    }

    // @Test //Echo y Bien:ACABADO
    public void testgetHostl() {
	Conf c = new Conf();
	c.setHostl("test");
	assertTrue(c.getHostl() == "test");

    }

    // @Test //Echo y Bien:ACABADO
    public void testsetHostl() {
	Conf c = new Conf();
	c.setHostl("test");
	assertTrue(c.getHostl() == "test");

    }

//    // @Test //Echo y Bien:ACABADO
//    public void testgetPuertol() {
//	Conf c = new Conf();
//	c.setPuertol(8);
//	assertTrue(c.getPuertol() == 8);
//
//    }
//
//    // @Test //Echo y Bien:ACABADO
//    public void testsetPuertol() {
//	Conf c = new Conf();
//	c.setPuertol(8);
//	assertTrue(c.getPuertol() == 8);
//
//    }

    // @Test //Echo y Bien:ACABADO
    public void testgetDbl() {
	Conf c = new Conf();
	c.setDbl("test");
	assertTrue(c.getDbl() == "test");

    }

    // @Test //Echo y Bien:ACABADO
    public void testsetDbl() {
	Conf c = new Conf();
	c.setDbl("test");
	assertTrue(c.getDbl() == "test");

    }

    // @Test //Echo y Bien:ACABADO
    public void testgetUsuariol() {
	Conf c = new Conf();
	c.setUsuariol("test");
	assertTrue(c.getUsuariol() == "test");

    }

    // @Test //Echo y Bien:
    public void testsetUsuariol() {
	Conf c = new Conf();
	c.setUsuariol("test");
	assertTrue(c.getUsuariol() == "test");

    }

    // @Test //Echo y Bien:
    public void testgetPassl() {
	Conf c = new Conf();
	c.setPassl("test");
	assertTrue(c.getPassl() == "test");

    }

    // @Test //Echo y Bien:ACABADO
    public void testsetPassl() {
	Conf c = new Conf();
	c.setPassl("test");
	assertTrue(c.getPassl() == "test");

    }

    @Test
    // Echo y Bien:ACABADO
    public void testgetTiempo() {
	Conf c = new Conf();
	c.setTiempo(10);
	assertTrue(c.getTiempo() == 10);

    }

    @Test
    // Echo y Bien:ACABADO
    public void testsetTiempo() {
	Conf c = new Conf();
	c.setTiempo(10);
	assertTrue(c.getTiempo() == 10);

    }

}
