package volcadoTest;

import static org.junit.Assert.*;

import org.junit.Test;

import volcadopack.Valvula;

public class ValvulaTest {

    // /////Metodo get y set de Codelecval
//    @Test
    // Echo y Bien:ACABADO
    public void testsetgetCodelecvalv() {
	Valvula v1 = new Valvula();
	String palabra = "test";
	assertNull(v1.getCodelecvalv());
	v1.setCodelecvalv(palabra);
	assertEquals(palabra, v1.getCodelecvalv());
    }

    // ////Metodo get y set de Duracion
//    @Test
    // Echo y Bien:ACABADO
    public void testsetgetDuracion() {
	Valvula v1 = new Valvula();
	int testDuracion = 100;
	assertEquals(0, 0, 0);
	v1.setDuracion(testDuracion);
	assertEquals(testDuracion, v1.getDuracion(), 0);
    }

    // ////Metodo get y set de Bloque
//    @Test
    // Echo y Bien:ACABADO
    public void testsetgetBloque() {
	Valvula v1 = new Valvula();
	int testbloque = 50;
	assertEquals(0, 0, 0);
	v1.setBloque(testbloque);
	assertEquals(testbloque, v1.getBloque(), 0);
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetCodelecvalv() {
	Valvula v1 = new Valvula();
	v1.setCodelecvalv("test");
	assertTrue(v1.getCodelecvalv() == "test");
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetCodelecvalv() {
	Valvula v2 = new Valvula();
	v2.setCodelecvalv("test");
	assertTrue(v2.getCodelecvalv() == "test");
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetDuracion() {
	Valvula v3 = new Valvula();
	v3.setDuracion(5);
	assertTrue(v3.getDuracion() == 5);
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetDuracion() {
	Valvula v4 = new Valvula();
	v4.setDuracion(5);
	assertTrue(v4.getDuracion() == 5);
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetBloque() {
	Valvula v5 = new Valvula();
	v5.setBloque(6);
	assertTrue(v5.getBloque() == 6);
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetBloque() {
	Valvula v6 = new Valvula();
	v6.setBloque(6);
	assertTrue(v6.getBloque() == 6);
    }

}
