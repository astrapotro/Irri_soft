package valvulasTest;

import static org.junit.Assert.*;

import irrisoftpack.Irrisoft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import programapack.HiloPrograma;
import valvulaspack.ListaValvBt2;
import valvulaspack.Valvula;

public class ListaValvBt2Test {
    
    private static Logger logger = LogManager.getLogger(ListaValvBt2Test.class.getName());
    

//     @Test //Echo y Bien:ACABADO
    public void testaddvalvbt2() {

	ListaValvBt2 LvBt2 = new ListaValvBt2();
	Valvula valv = new Valvula();
	LvBt2.addvalvbt2(valv);
	int i = 1;
	assertEquals(i, LvBt2.getsizeof());

    }

//    @Test// Echo y Bien:ACABADO
    public void testgetvalvbt2_1() {

	ListaValvBt2 LvBt2 = new ListaValvBt2();
	Valvula valv = new Valvula();
	LvBt2.addvalvbt2(valv);
	LvBt2.addvalvbt2(valv);
	assertNotNull(LvBt2.getvalvbt2(1));
    }

//     @Test //Echo y Bien:ACABADO, Se puede realizar de las dos maneras
    public void testgetvalvbt2_2() {
	 Valvula valv = new Valvula();
	 valv.setCodelecvalv("1001"); 
	 Irrisoft.window = new Irrisoft();
	 ListaValvBt2 LvBt2 = new ListaValvBt2();
	 Irrisoft.window.valvsbt2 = LvBt2;
//	 Irrisoft.window.valvsbt2.addvalvbt2(valv);
//	 Irrisoft.window.valvsbt2.getvalvbt2("1001");
	 LvBt2.addvalvbt2(valv);
	 assertNotNull(LvBt2.getvalvbt2("1001"));
	
	
     	}

//     @Test //Echo y Bien:ACABADO 
    public void testgetvalvbt2_3() {
	Valvula valv = new Valvula();
	valv.setCodelecvalv("1001");
	valv.setDeco(1);
	Irrisoft.window = new Irrisoft();
	ListaValvBt2 LvBt2 = new ListaValvBt2();
	Irrisoft.window.valvsbt2 = LvBt2;
	Irrisoft.window.valvsbt2.addvalvbt2(valv);
	assertNotNull(LvBt2.getvalvbt2(5, 1));

    }

//     @Test //Echo y Bien:ACABADO
    public void testgetsizeof() {
	ListaValvBt2 LvBt2 = new ListaValvBt2();
	Valvula val = new Valvula();
	LvBt2.addvalvbt2(val);
	LvBt2.addvalvbt2(val);
	int i = 2;
	assertEquals(i, LvBt2.getsizeof());

    }

}
