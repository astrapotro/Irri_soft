package valvulasTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import valvulaspack.ListaPlacasGhost;
import valvulaspack.PlacaGhost;

public class ListaPlacasGhostTest {

//     @Test //Echo y Bien:ACABADO
    public void testaddplaca() {
	ListaPlacasGhost Lpg = new ListaPlacasGhost();
	PlacaGhost plac = new PlacaGhost();
	Lpg.addplaca(plac);
	int i = 1;
	assertEquals(i, Lpg.getsizeof());
    }

//     @Test //Echo y Bien:ACABADO
    public void testgetplaca() {
	ListaPlacasGhost Lpg = new ListaPlacasGhost();
	PlacaGhost plac = new PlacaGhost();
	Lpg.addplaca(plac);
	assertNotNull(Lpg.getplaca(0));

    }

//     @Test //Echo y Bien:ACABADO
    public void testgetsizeof() {
	ListaPlacasGhost Lpg = new ListaPlacasGhost();
	PlacaGhost plac = new PlacaGhost();
	Lpg.addplaca(plac);
	Lpg.addplaca(plac);
	assertEquals(2, Lpg.getsizeof());

    }

}
