package valvulasTest;

import static org.junit.Assert.*;

import irrisoftpack.Irrisoft;

import java.util.List;
import org.junit.Test;

import valvulaspack.ListaValvMci;
import valvulaspack.Valvula;

public class ListaValvMciTest {

    // @Test // Echo y Bien:ACABADO
    public void testaddvalvmci() {
	ListaValvMci Lmci = new ListaValvMci();
	Valvula val = new Valvula();
	Lmci.addvalvmci(val);
	int i = 1;
	assertEquals(i, Lmci.getsizeof());

    }

    // @Test //Echo y Bien:ACABADO
    public void testgetvalvmci1() {
	ListaValvMci Lmci = new ListaValvMci();
	Valvula va = new Valvula();
	Lmci.addvalvmci(va);
	Lmci.addvalvmci(va);
	assertNotNull(Lmci.getvalvmci(1));
    }

    // @Test //Echo y Bien:ACABADO
    public void testgetvalvmci2() {

	Valvula val = new Valvula();
	val.setCodelecvalv("100");
	Irrisoft.window = new Irrisoft();
	ListaValvMci Lmci = new ListaValvMci();
	Irrisoft.window.valvsmci = Lmci;
	Irrisoft.window.valvsmci.addvalvmci(val);
	assertNotNull(Lmci.getvalvmci("100"));
    }

    // @Test //Echo y Bien:ACABADO
    public void testgetsizeof() {
	ListaValvMci Lmci = new ListaValvMci();
	Valvula val = new Valvula();
	Lmci.addvalvmci(val);
	Lmci.addvalvmci(val);
	int i = 2;
	assertEquals(i, Lmci.getsizeof());
    }

}
