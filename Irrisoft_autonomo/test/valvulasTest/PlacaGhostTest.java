package valvulasTest;

import static org.junit.Assert.*;

import org.junit.Test;

import valvulaspack.PlacaGhost;

public class PlacaGhostTest {

//     @Test //Echo y Bien:ACABADO
    public void testsetgetNumPlaca() {
	PlacaGhost pg = new PlacaGhost();
	int testNumPlaca = 10;
	assertEquals(0, 0, 0);
	pg.setNumplaca(testNumPlaca);
	assertEquals(testNumPlaca, pg.getNumplaca(), 0);

    }

//     @Test//Echo y Bien:ACABADO
    public void testsetgetTieneamp() {
	PlacaGhost pg = new PlacaGhost();
	boolean testTieneamp = false;
	// assertFalse(false);
	pg.setTieneamp(testTieneamp);
	assertEquals(testTieneamp, pg.isTieneamp());
    }

//     @Test//Echo y Bien:ACABADO
    public void testgetNumplaca() {
	PlacaGhost pg = new PlacaGhost();
	pg.setNumplaca(3);
	assertTrue(pg.getNumplaca() == 3);
    }

//     @Test//Echo y Bien:ACABADO
    public void testsetNumplaca() {
	PlacaGhost pg = new PlacaGhost();
	pg.setNumplaca(3);
	assertTrue(pg.getNumplaca() == 3);

    }

//     @Test//Echo y Bien:ACABADO
    public void testisTieneamp() {
	PlacaGhost pg = new PlacaGhost();
	pg.setTieneamp(false);
	assertTrue(pg.isTieneamp() == false);

    }

//     @Test//Echo y Bien:ACABADO
    public void testsetTieneamp() {
	PlacaGhost pg = new PlacaGhost();
	pg.setTieneamp(false);
	assertTrue(pg.isTieneamp() == false);

    }

}
