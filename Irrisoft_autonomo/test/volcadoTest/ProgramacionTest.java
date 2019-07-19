package volcadoTest;

import static org.junit.Assert.*;

import irrisoftpack.Irrisoft;

import java.sql.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import alertaspack.GestorAlertas;

import volcadopack.Programacion;

public class ProgramacionTest {
    
    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {	
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	Programacion p = new Programacion();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }


//    @Test
    // Echo y Bien:ACABADO
    public void testgetIdprograma() {
	Programacion p = new Programacion();
	p.setIdprograma(4);
	assertTrue(p.getIdprograma() == 4);
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetIdprograma() {
	Programacion p = new Programacion();
	p.setIdprograma(4);
	assertTrue(p.getIdprograma() == 4);
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetCodprograma() {
	Programacion p = new Programacion();
	p.setCodprograma("test");
	assertTrue(p.getCodprograma() == "test");
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetCodprograma() {
	Programacion p = new Programacion();
	p.setCodprograma("test");
	assertTrue(p.getCodprograma() == "test");
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetDsprograma() {
	Programacion p = new Programacion();
	p.setDsprograma("test");
	assertTrue(p.getDsprograma() == "test");
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetDsprograma() {
	Programacion p = new Programacion();
	p.setDsprograma("test");
	assertTrue(p.getDsprograma() == "test");
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetFcinicio() {

	Programacion p = new Programacion();
	Date fi = new Date(10);
	p.setFcinicio(fi);
	assertTrue(p.getFcinicio() == fi);
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetFcinicio() {
	Programacion p = new Programacion();
	Date fi = new Date(10);
	p.setFcinicio(fi);
	assertTrue(p.getFcinicio() == fi);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetFcFin() {
	Programacion p = new Programacion();
	Date fc = new Date(10);
	p.setFcFin(fc);
	assertTrue(p.getFcFin() == fc);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetFcFin() {
	Programacion p = new Programacion();
	Date fc = new Date(10);
	p.setFcFin(fc);
	assertTrue(p.getFcFin() == fc);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetActivo() {
	Programacion p = new Programacion();
	p.setActivo("test");
	assertTrue(p.getActivo() == "test");
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetActivo() {
	Programacion p = new Programacion();
	p.setActivo("test");
	assertTrue(p.getActivo() == "test");
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetCodprog() {
	Programacion p = new Programacion();
	p.setCodprog("test");
	assertTrue(p.getCodprog() == "test");
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetCodprog() {
	Programacion p = new Programacion();
	p.setCodprog("test");
	assertTrue(p.getCodprog() == "test");
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetTipo() {
	Programacion p = new Programacion();
	p.setTipo("test");
	assertTrue(p.getTipo() == "test");
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetTipo() {
	Programacion p = new Programacion();
	p.setTipo("test");
	assertTrue(p.getTipo() == "test");
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetDial() {
	Programacion p = new Programacion();
	p.setDial(1);
	assertTrue(p.getDial() == 1);
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetDial() {
	Programacion p = new Programacion();
	p.setDial(1);
	assertTrue(p.getDial() == 1);
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetDiam() {
	Programacion p = new Programacion();
	p.setDiam(1);
	assertTrue(p.getDiam() == 1);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetDiam() {
	Programacion p = new Programacion();
	p.setDiam(1);
	assertTrue(p.getDiam() == 1);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetDiax() {
	Programacion p = new Programacion();
	p.setDiax(3);
	assertTrue(p.getDiax() == 3);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetDiax() {
	Programacion p = new Programacion();
	p.setDiax(3);
	assertTrue(p.getDiax() == 3);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetDiaj() {
	Programacion p = new Programacion();
	p.setDiaj(4);
	assertTrue(p.getDiaj() == 4);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetDiaj() {
	Programacion p = new Programacion();
	p.setDiaj(4);
	assertTrue(p.getDiaj() == 4);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetDiav() {
	Programacion p = new Programacion();
	p.setDiav(5);
	assertTrue(p.getDiav() == 5);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetDiav() {
	Programacion p = new Programacion();
	p.setDiav(5);
	assertTrue(p.getDiav() == 5);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetDias() {
	Programacion p = new Programacion();
	p.setDias(6);
	assertTrue(p.getDias() == 6);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetDias() {
	Programacion p = new Programacion();
	p.setDias(6);
	assertTrue(p.getDias() == 6);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetDiad() {
	Programacion p = new Programacion();
	p.setDiad(7);
	assertTrue(p.getDiad() == 7);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetDiad() {
	Programacion p = new Programacion();
	p.setDiad(7);
	assertTrue(p.getDiad() == 7);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetModo() {
	Programacion p = new Programacion();
	p.setModo("test");
	assertTrue(p.getModo() == "test");

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetModo() {
	Programacion p = new Programacion();
	p.setModo("test");
	assertTrue(p.getModo() == "test");

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetModoini() {
	Programacion p = new Programacion();
	p.setModoini("test");
	assertTrue(p.getModoini() == "test");

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetModoini() {
	Programacion p = new Programacion();
	p.setModoini("test");
	assertTrue(p.getModoini() == "test");

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetPbloque() {
	Programacion p = new Programacion();
	p.setPbloque(11);
	assertTrue(p.getPbloque() == 11);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetPbloque() {
	Programacion p = new Programacion();
	p.setPbloque(11);
	assertTrue(p.getPbloque() == 11);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetCuota() {
	Programacion p = new Programacion();
	p.setCuota(12);
	assertTrue(p.getCuota() == 12);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetCuota() {
	Programacion p = new Programacion();
	p.setCuota(12);
	assertTrue(p.getCuota() == 12);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetLeido() {
	Programacion p = new Programacion();
	p.setLeido("test");
	assertTrue(p.getLeido() == "test");

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetLeido() {
	Programacion p = new Programacion();
	p.setLeido("test");
	assertTrue(p.getLeido() == "test");

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetEnmarcha() {
	Programacion p = new Programacion();
	p.setEnmarcha("test");
	assertTrue(p.getEnmarcha() == "test");

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetEnmarcha() {
	Programacion p = new Programacion();
	p.setEnmarcha("test");
	assertTrue(p.getEnmarcha() == "test");

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetIdprogramal() {
	Programacion p = new Programacion();
	p.setIdprogramal(15);
	assertTrue(p.getIdprogramal() == 15);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetIdprogramal() {
	Programacion p = new Programacion();
	p.setIdprogramal(15);
	assertTrue(p.getIdprogramal() == 15);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetDia() {
	Programacion p = new Programacion();
	Date dia = new Date(10);
	p.setDia(dia);
	assertTrue(p.getDia() == dia);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetDia() {
	Programacion p = new Programacion();
	Date dia = new Date(10);
	p.setDia(dia);
	assertTrue(p.getDia() == dia);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetHoraini() {
	Programacion p = new Programacion();
	p.setHoraini("test");
	assertTrue(p.getHoraini() == "test");

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetHoraini() {
	Programacion p = new Programacion();
	p.setHoraini("test");
	assertTrue(p.getHoraini() == "test");

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetHorafin() {
	Programacion p = new Programacion();
	p.setHorafin("test");
	assertTrue(p.getHorafin() == "test");

    }
//    @Test
    // Echo y Bien:ACABADO
    public void testsetHorafin() {
	Programacion p = new Programacion();
	p.setHorafin("test");
	assertTrue(p.getHorafin() == "test");

    }
}
