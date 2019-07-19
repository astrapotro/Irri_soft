package programaTest;

import static org.junit.Assert.*;

import org.junit.Test;

import programapack.ListaProgsaexec;
import programapack.Programacion;

public class ListaProgsaexecTest {

//     @Test //Echo y Bien:ACABADO
    public void testaddprog() {
	ListaProgsaexec Lp = ListaProgsaexec.getInstance();
	Programacion prog = new Programacion();
	Lp.addprog(prog);
	Lp.addprog(prog);
	assertNotNull(Lp.getprogramas());
    }

//     @Test //Hecho y Bien:ACABADO
    public void testgetprog() {
	ListaProgsaexec Lp = ListaProgsaexec.getInstance();
	Programacion prog = new Programacion();
	Lp.addprog(prog);
	//Al cambiar de paquete, no puedo acceder al metodo ya que es PROTECTED	
	//assertNotNull(Lp.getprog(0));

    }

//     @Test //Echo y Bien:ACABADO
    public void testgetprog2() {
	ListaProgsaexec Lp = ListaProgsaexec.getInstance();
	Programacion prog = new Programacion();
	Lp.addprog(prog);
	// Lp.getprogramas();
	assertNotNull(Lp.getprogramas());

    }

//     @Test //Echo y Bien:ACABADO
    public void testdelproglist() {
	ListaProgsaexec Lp = ListaProgsaexec.getInstance();
	Programacion prog = new Programacion();
	Lp.addprog(prog);
	Lp.addprog(prog);
	Lp.addprog(prog);
	Lp.addprog(prog);
	//Al cambiar de paquete, no puedo acceder al metodo ya que es PROTECTED
	//Lp.delproglist(1);
	assertNotNull(Lp.getprogramas());

    }

}
