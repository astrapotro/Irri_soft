package programaTest;

import static org.junit.Assert.*;

import irrisoftpack.ConexionDB;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import programapack.Programacion;
import programapack.TareaProg;

import valvulaspack.Valvula;

public class ProgramacionTest {

    private static Logger logger = LogManager.getLogger(ProgramacionTest.class
	    .getName());

    // @Test//Echo y Bien:ACABADO
    public void testgetIdprograma() {
	Programacion pro = new Programacion();
	pro.setIdprograma(1);
	assertTrue(pro.getIdprograma() == 1);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetIdprograma() {
	Programacion pro = new Programacion();
	pro.setIdprograma(1);
	assertTrue(pro.getIdprograma() == 1);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetCodprograma() {
	Programacion pro = new Programacion();
	pro.setCodprograma("test");
	assertTrue(pro.getCodprograma() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetCodprograma() {
	Programacion pro = new Programacion();
	pro.setCodprograma("test");
	assertTrue(pro.getCodprograma() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetDsprograma() {
	Programacion pro = new Programacion();
	pro.setDsprograma("test");
	assertTrue(pro.getDsprograma() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetDsprograma() {
	Programacion pro = new Programacion();
	pro.setDsprograma("test");
	assertTrue(pro.getDsprograma() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetFcinicio() {
	Programacion pro = new Programacion();
	Date fi = new Date(10);
	pro.setFcinicio(fi);
	assertEquals(pro.getFcinicio(), fi);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetFcinicio() {
	Programacion pro = new Programacion();
	Date fi = new Date(10);
	pro.setFcinicio(fi);
	assertTrue(pro.getFcinicio() == fi);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetFcfin() {
	Programacion pro = new Programacion();
	Date ff = new Date(10);
	pro.setFcFin(ff);
	assertTrue(pro.getFcFin() == ff);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetFcfin() {
	Programacion pro = new Programacion();
	Date ff = new Date(10);
	pro.setFcFin(ff);
	assertTrue(pro.getFcFin() == ff);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetActivo() {
	Programacion pro = new Programacion();
	pro.setActivo("test");
	assertTrue(pro.getActivo() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetActivo() {
	Programacion pro = new Programacion();
	pro.setActivo("test");
	assertTrue(pro.getActivo() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetCodprog() {
	Programacion pro = new Programacion();
	pro.setCodprog("test");
	assertTrue(pro.getCodprog() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetCodprog() {
	Programacion pro = new Programacion();
	pro.setCodprog("test");
	assertTrue(pro.getCodprog() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetTipo() {
	Programacion pro = new Programacion();
	pro.setTipo("test");
	assertTrue(pro.getTipo() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetTipo() {
	Programacion pro = new Programacion();
	pro.setTipo("test");
	assertTrue(pro.getTipo() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetDial() {
	Programacion pro = new Programacion();
	pro.setDial(10);
	assertTrue(pro.getDial() == 10);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetDial() {
	Programacion pro = new Programacion();
	pro.setDial(10);
	assertTrue(pro.getDial() == 10);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetDiam() {
	Programacion pro = new Programacion();
	pro.setDiam(11);
	assertTrue(pro.getDiam() == 11);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetDiam() {
	Programacion pro = new Programacion();
	pro.setDiam(11);
	assertTrue(pro.getDiam() == 11);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetDiax() {
	Programacion pro = new Programacion();
	pro.setDiax(12);
	assertTrue(pro.getDiax() == 12);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetDiax() {
	Programacion pro = new Programacion();
	pro.setDiax(12);
	assertTrue(pro.getDiax() == 12);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetDiaj() {
	Programacion pro = new Programacion();
	pro.setDiaj(13);
	assertTrue(pro.getDiaj() == 13);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetDiaj() {
	Programacion pro = new Programacion();
	pro.setDiaj(13);
	assertTrue(pro.getDiaj() == 13);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetDiav() {
	Programacion pro = new Programacion();
	pro.setDiav(14);
	assertTrue(pro.getDiav() == 14);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetDiav() {
	Programacion pro = new Programacion();
	pro.setDiav(14);
	assertTrue(pro.getDiav() == 14);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetDias() {
	Programacion pro = new Programacion();
	pro.setDias(15);
	assertTrue(pro.getDias() == 15);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetDias() {
	Programacion pro = new Programacion();
	pro.setDias(15);
	assertTrue(pro.getDias() == 15);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetDiad() {
	Programacion pro = new Programacion();
	pro.setDiad(16);
	assertTrue(pro.getDiad() == 16);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetDiad() {
	Programacion pro = new Programacion();
	pro.setDiad(16);
	assertTrue(pro.getDiad() == 16);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetModo() {
	Programacion pro = new Programacion();
	pro.setModo("test");
	assertTrue(pro.getModo() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetModo() {
	Programacion pro = new Programacion();
	pro.setModo("test");
	assertTrue(pro.getModo() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetModoini() {
	Programacion pro = new Programacion();
	pro.setModoini("test");
	assertTrue(pro.getModoini() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetModoini() {
	Programacion pro = new Programacion();
	pro.setModoini("test");
	assertTrue(pro.getModoini() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetPbloque() {
	Programacion pro = new Programacion();
	pro.setPbloque(17);
	assertTrue(pro.getPbloque() == 17);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetPbloque() {
	Programacion pro = new Programacion();
	pro.setPbloque(17);
	assertTrue(pro.getPbloque() == 17);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetCuota() {
	Programacion pro = new Programacion();
	pro.setCuota(18);
	assertTrue(pro.getCuota() == 18);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetCuota() {
	Programacion pro = new Programacion();
	pro.setCuota(18);
	assertTrue(pro.getCuota() == 18);

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetLeido() {
	Programacion pro = new Programacion();
	pro.setLeido("test");
	assertTrue(pro.getLeido() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetLeido() {
	Programacion pro = new Programacion();
	pro.setLeido("test");
	assertTrue(pro.getLeido() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetEnmarcha() {
	Programacion pro = new Programacion();
	pro.setEnmarcha("test");
	assertTrue(pro.getEnmarcha() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetEnmarcha() {
	Programacion pro = new Programacion();
	pro.setEnmarcha("test");
	assertTrue(pro.getEnmarcha() == "test");

    }

    // @Test//Echo y Bien:ACABADO
    public void testgetUltdeberes() {
	Programacion pro = new Programacion();
	Date deb = new Date(10);
	pro.setUltdeberes(deb);
	assertTrue(pro.getUltdeberes() == deb);

    }

    // @Test//Echo y Bien:ACABADO
    public void testsetUltdeberes() {
	Programacion pro = new Programacion();
	Date deb = new Date(10);
	pro.setUltdeberes(deb);
	assertTrue(pro.getUltdeberes() == deb);

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetValvuprog() {
	Programacion pro = new Programacion();
	Valvula val = new Valvula();
	ArrayList<Valvula>valv = new ArrayList<Valvula>();
	valv.add(val);
	pro.setValvuprog(valv);
	assertEquals(valv, pro.getValvuprog());

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetValvuprog() {
	Programacion pro = new Programacion();
	Valvula val = new Valvula();
	ArrayList<Valvula>valv = new ArrayList<Valvula>();
	valv.add(val);
	pro.setValvuprog(valv);
	assertEquals(valv, pro.getValvuprog());

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetDays() {
	Programacion pro = new Programacion();
	Date dia = new Date(0);
	ArrayList<Date>dias = new ArrayList<Date>();
	dias.add(dia);
	pro.setDays(dias);
	assertEquals(dias, pro.getDays());

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetDays() {
	Programacion pro = new Programacion();
	Date dia = new Date(0);
	ArrayList<Date>dias = new ArrayList<Date>();
	dias.add(dia);
	pro.setDays(dias);
	assertEquals(dias, pro.getDays());

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetTareasprog() {
	Programacion pro = new Programacion();
	TareaProg tar = new TareaProg();
	ArrayList<TareaProg>tareas = new ArrayList<TareaProg>();
	tareas.add(tar);
	pro.setTareasprog(tareas);
	assertEquals(tareas, pro.getTareasprog());

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetTareasprog() {
	Programacion pro = new Programacion();
	TareaProg tar = new TareaProg();
	ArrayList<TareaProg>tareas = new ArrayList<TareaProg>();
	tareas.add(tar);
	pro.setTareasprog(tareas);
	assertEquals(tareas, pro.getTareasprog());

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetHorasfin() {
	Programacion pro = new Programacion();
	Time hof = new Time(0);
	ArrayList<Time>hora_f = new ArrayList<Time>();
	hora_f.add(hof);
	pro.setHorasfin(hora_f);
	assertEquals(hora_f, pro.getHorasfin());

    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetHorasfin() {
	Programacion pro = new Programacion();
	Time hof = new Time(0);
	ArrayList<Time>hora_f = new ArrayList<Time>();
	hora_f.add(hof);
	pro.setHorasfin(hora_f);
	assertEquals(hora_f, pro.getHorasfin());
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testgetHorasini() {
	Programacion pro = new Programacion();
	Time hoi = new Time(0);
	ArrayList<Time>hora_i = new ArrayList<Time>();
	hora_i.add(hoi);
	pro.setHorasini(hora_i);
	assertEquals(hora_i, pro.getHorasini());
    }

//    @Test
    // Echo y Bien:ACABADO
    public void testsetHorasini() {
	Programacion pro = new Programacion();
	Time hoi = new Time(0);
	ArrayList<Time>hora_i = new ArrayList<Time>();
	hora_i.add(hoi);
	pro.setHorasini(hora_i);
	assertEquals(hora_i, pro.getHorasini());
    }
}
