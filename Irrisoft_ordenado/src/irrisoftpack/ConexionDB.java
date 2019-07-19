package irrisoftpack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import programapack.HiloPrograma;
import programapack.Programacion;
import programapack.TareaProg;

import tareapack.HiloTarea;
import tareapack.ListaTareasaexec;
import tareapack.Tarea;
import valvulaspack.ListaValvBt2;
import valvulaspack.ListaValvBt22;
import valvulaspack.ListaValvMci;
import valvulaspack.Valvula;



public class ConexionDB {

	protected Tarea tarea;
	protected Connection conn = null;
	protected Timestamp fechapri,fechactual;
	protected Programacion prog;

	protected int post = 0;
	protected int posp = 0;
	protected HiloTarea hilotarea;
	protected HiloPrograma hiloprog;
	protected boolean conectado = false;
	


	// ///////////////////////////////////////////////////////////////////
	//
	// Gestión conexiones a las BBDD

	protected boolean conecta() {
		
		while (conectado == false) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				// LOCAL
				String urllocal = "jdbc:mysql://"
						+ Irrisoft.window.config.getHost() + ":"
						+ Irrisoft.window.config.getPuerto() + "/"
						+ Irrisoft.window.config.getDb();
				conn = DriverManager.getConnection(urllocal,
						Irrisoft.window.config.getUsuario(),
						Irrisoft.window.config.getPass());
				
				conectado = true;
			} catch (Exception e) {
				
				Irrisoft.window.textArea
						.append("\nNo se ha podido conectar con la BBDD local!!");
				Irrisoft.window.textArea
				.append("\nCompruebe en la configuración el nombre\nde la bbdd, usuario, password...");
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e2) {
					// 
					e2.printStackTrace();
				}
				Irrisoft.window.textArea
						.append("\nReintentando conexion en 10 segundos.");
				Irrisoft.window.textArea
				.append("\n");
				int i = 0;
				while (i < 10) {
					try {
						Thread.sleep(500);
						Irrisoft.window.textArea.append(".");
						Thread.sleep(500);
						Irrisoft.window.textArea.append(".");
						i++;
					} catch (InterruptedException e1) {
						Irrisoft.window.textArea
								.append("\nLa BBDD local está caida! Contácte con su administrador.");
					}
				}
			}
		}
		return conectado;

	}

	protected boolean cierra() {
		boolean cerrado = false;
		if (conn != null) {
			try {
				conn.close();
				Irrisoft.window.textArea
						.append("\nConexión terminada con la bbdd local.");
				cerrado = true;
				conectado=false;
			} catch (Exception e) { /* ignore close errors */
				cerrado = false;
			}
		}
		return cerrado;
	}

	
	
	// ////////////////////////////////////////////////////////////////////////////
	//
	// Comprueba TAREAS pendientes, synchronized para que no puedan acceder varioshilos a la vez
	//
	protected synchronized void tarea() {

		PreparedStatement sentenciapre;
		ResultSet resultados;

		try {
			
			// Consulto tareas pendientes de ejecutar
			sentenciapre = conn
					.prepareStatement("select * from tarea where CODPROG="
							+ Irrisoft.window.config.getIdrasp());
			resultados = sentenciapre.executeQuery();

			
			while (resultados.next()) {
				
				tarea =  new Tarea();
				
				// Cojo sólo ésto de la tarea para evitar campos nulls y
				// controlar si es tarea manual o programación.
				tarea.setIddstarea(resultados.getInt(4));
				tarea.setIdtarea(resultados.getInt(1));
				
				//Compruebo que es una tarea nueva !!
				if (Irrisoft.window.idtareaexec<tarea.getIdtarea()){
					
					Irrisoft.window.idtareaexec=tarea.getIdtarea();
					
					//Si son tareas manuales
					if (tarea.getIddstarea()==1 || tarea.getIddstarea()==2) {	
	
						// aki el resto de los campos de tarea
						tarea.setCodprog(resultados.getString(2));
						tarea.setCodelecvalv(resultados.getString(3));
						tarea.setFcexec(resultados.getTimestamp(5));
						tarea.setValor(resultados.getInt(6));
						tarea.setCodtarea(resultados.getString(7));
						tarea.setDstarea(resultados.getString(8));
							
						
						if (Integer.parseInt(tarea.getCodelecvalv()) < 10) {
							tarea.setCodelecvalv("0" + tarea.getCodelecvalv());
						}
						
						if (tarea.getIddstarea() == 1) {
							fechapri = tarea.getFcexec();
							// ACTUO sobre la tarea manual
						} else if (tarea.getIddstarea() == 2) {
							tarea.setFcloc(fechapri);
							//añado la tarea a la lista de tareas a ejecutar !!
							Irrisoft.tareas.addtarea(tarea);
							
							ejecutarea(Irrisoft.tareas.gettarea(post),post);
							
							//Lo pongo para que le de tiempo a ejecutar tareas simultaneas (Ajustable)(A REVISAR) !!
							try {
								Thread.sleep(800);
							} catch (InterruptedException e) {
								// 
								e.printStackTrace();
							}				
							post++;
							 
							}
						 }
						}
					}
			
				} catch (SQLException e) {
			
				e.printStackTrace();
				System.out.println("Error 165");
	
			}

	}

	// //////////////////////////////////////////////////////////////////////////
	//
	// Ejecuta tareas manuales pendientes
	//
	protected synchronized void ejecutarea(Tarea tar, int posi) {
		
		
		// Para hacer la diferencia y saber el tiempo de riego
			tar.duracion = (tar.getFcexec().getTime() - tar.getFcloc().getTime())+2000;
		
		
			Irrisoft.window.textArea
			.append("\nUna nueva tarea ("+tar.getIdtarea()+") se ha encontrado:");
			Irrisoft.window.frmIrrisoft.repaint();
			
			
			if (Integer.parseInt(tar.getCodelecvalv())<1000){
				//Compruebo si la valvula ya está abierta por otra tarea y la desecho!!
				if (ListaValvMci.getInstance().getvalvmci(Integer.parseInt(tar.getCodelecvalv())).isAbierta()){
					
					Irrisoft.window.textArea
					.append("\nLa válvula "+tar.getCodelecvalv()+" ya se encuentra abierta por \nla tarea " +
								Irrisoft.window.valvsmci.getvalvmci(tar.getCodelecvalv()).getTareaasoc());
				}else{
					ejecutatareahilo(tar,posi);
				}
				//Compruebo si la valvula ya está abierta por otra tarea y la desecho!!
			}else {
				int codelecvalv = Integer.parseInt(tar.getCodelecvalv())-1001;
				if (ListaValvBt2.getInstance().getvalvbt2(codelecvalv).isAbierta()){
					
					Irrisoft.window.textArea
					.append("\nLa válvula "+tar.getCodelecvalv()+" ya se encuentra abierta por \nla tarea " +
								Irrisoft.window.valvsmci.getvalvmci(tar.getCodelecvalv()).getTareaasoc());
				}else {
					ejecutatareahilo(tar,posi);
				}
			}
	}

	
	public synchronized boolean Borratarea(Tarea tar, int posi) {

		PreparedStatement sentenciapre;
		System.out.println("Borro tareas en la tabla tarea con IDs "
				+ tarea.getIdtarea()+" y "+(tarea.getIdtarea()-1));
		try {
			sentenciapre = conn
					.prepareStatement("DELETE FROM tarea WHERE idtarea="
							+ tarea.getIdtarea());
			sentenciapre.executeUpdate();
			sentenciapre = conn
					.prepareStatement("DELETE FROM tarea WHERE idtarea="
							+ (tarea.getIdtarea()-1));
			sentenciapre.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out
					.println("No se han podido borrar las tareas en la BBDD local.");
				
		
	//Aki tengo que borrar de ListaHilos y de ListaTareas para liberar memoria
		ListaHilos.getInstance().delhilo(posi);
		System.out.println("Hilo borrado del arraylist");
		ListaTareasaexec.getInstance().delhilotar(posi);
		System.out.println("Tarea borrada del arraylist");
	
		return true;
		}
	}
	
	private synchronized void ejecutatareahilo(Tarea tar, int posi){
		// ejecuto la tarea en un nuevo hilo
		hilotarea = new HiloTarea(tar,posi);
		
		ListaHilos.getInstance().addhilo(new Thread(hilotarea));
		
		tar.setHilotar(ListaHilos.getInstance().gethilo(posi));
//		
//		System.out.println("Ejecutatarea hilo: " + ListaTareasaexec.getInstance().gettarea(posi).hilotar.getId());
//		System.out.println("Ejecutatarea listahilos: " + ListaHilos.getInstance().gethilo(posi).getId());
		
		tar.hilotar.start();
	}
	
	///////////////////////////////////////////////////////////////////////////////
	
	
	/////////////////////////////////////////////////////////////////////////////
	//
	//Comprueba PROGRAMACIONES pendientes y si las hay las copia a la local y borra la tarea y demases de la paserela
	protected  void programa(){
		
		
		
		PreparedStatement sentenciapre;
		ResultSet resultados;
		Date hoy = null;
		String hoyes = new Timestamp(Calendar.getInstance().getTime().getTime()).toString();
		hoyes=hoyes.substring(0, 10);
		

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
	 
			hoy = formatter.parse(hoyes);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	
		try {
		//Consulto programaciones pendientes de ejecutar
		sentenciapre = conn.prepareStatement("select * from programa where CODPROG="+Irrisoft.window.config.getIdrasp());
		resultados = sentenciapre.executeQuery();
		
		
			while (resultados.next()) {
			 
				prog =  new Programacion();
				
				//Controlo que la prog no ha sido tratada y si no la trato
				prog.setUltdeberes(resultados.getDate(22));
				prog.setIdprograma(resultados.getInt(1));
				
				if ((prog.getUltdeberes()==null) || (prog.getUltdeberes().getTime()<hoy.getTime())){	
			
					System.out.println("HOLAAAA: " +prog.getIdprograma());
					
					prog.setLeido(resultados.getString(20));
					
				    prog.setFcinicio(resultados.getDate(4));
					prog.setCodprograma(resultados.getString(2));
					prog.setDsprograma(resultados.getString(3));
					prog.setFcFin(resultados.getDate(5));
					prog.setActivo(resultados.getString(6));
					prog.setCodprog(resultados.getString(7));
					prog.setTipo(resultados.getString(8));
					prog.setDial(resultados.getInt(9));
					prog.setDiam(resultados.getInt(10));
					prog.setDiax(resultados.getInt(11));
					prog.setDiaj(resultados.getInt(12));
					prog.setDiav(resultados.getInt(13));
					prog.setDias(resultados.getInt(14));
					prog.setDiad(resultados.getInt(15));
					prog.setModo(resultados.getString(16));
					prog.setModoini(resultados.getString(17));
					prog.setPbloque(resultados.getInt(18));
					prog.setCuota(resultados.getInt(19));
					prog.setEnmarcha(resultados.getString(21));
					
					
						
							//Si es una programación DIARIA !!
							if (prog.getTipo().contains("D")){
								
								//Consulto y guardo las valvulas asociadas a la programacion
								sentenciapre = conn.prepareStatement("select * from valvulas where IDPROGRAMA="+prog.getIdprograma());
								ResultSet result = sentenciapre.executeQuery();
								int i=0;
								
								while (result.next()) {
									Valvula valv = new Valvula();
									valv.setCodelecvalv(result.getString(5));
									valv.setProgasoc(prog.getIdprograma());
									valv.setDuracion(result.getInt(6));
									prog.getValvuprog().add(valv);
									System.out.println(prog.getValvuprog().get(i).getCodelecvalv());
									i++;
								}
								
								//Consulto y guardo los días de ejecución
								sentenciapre = conn.prepareStatement("select * from dias where IDPROGRAMA="+prog.getIdprograma());
								result = sentenciapre.executeQuery();
								i=0;
								while (result.next()) {
									prog.getDays().add(result.getDate(5));
									System.out.println(prog.getDays().get(i).toString());
									i++;
								}
								
								//Consulto y guardo las horas de ejecución
								sentenciapre = conn.prepareStatement("select * from horas where IDPROGRAMA="+prog.getIdprograma());
								result = sentenciapre.executeQuery();
								i=0;
								while (result.next()) {
									prog.getHoras().add(result.getTime(5));
									System.out.println(prog.getHoras().get(i).toString());
									i++;
								}	
								
								//Compruebo si la tareaprog es para hoy
								eshoytarprog(prog,posp,false);
								
							}else{
								//Es una progamacion SEMANAL
								
							}
							
							//añado la prog a la lista de progs!!
							Irrisoft.progs.addprog(prog);
							
				 		posp++;
					 }
					}
			
					} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("Error 406");
					System.out.println("No estás conectado con la BBDD !!");
					}
		
			
	}
	
	
	private boolean eshoytarprog(Programacion prog, int posi, boolean leida){
		
		boolean existe=false;
		String fechahoy = new String();
		fechahoy=new Timestamp(Calendar.getInstance().getTime().getTime()).toString();
		PreparedStatement sentenciapre;
		ResultSet resultados;
		int cont = 0;
		
		
		fechahoy = fechahoy.substring(0, 10);
		
		
		for (int i=0;i<prog.getDays().size();i++){
			
			//Si la tarea es de hoy
			if (((prog.getDays().get(i)).toString()).equals(fechahoy)){
				
				Irrisoft.window.textArea
				.append("\nNueva programación encontrada para hoy ("+prog.getIdprograma()+"):");
				Irrisoft.window.frmIrrisoft.repaint();
								
						for (int k=0; k<prog.getValvuprog().size();k++){
						
							for (int j=0;j<prog.getHoras().size();j++) {
							
								TareaProg tar = new TareaProg();
								tar.setIdprog(prog.getIdprograma());
								tar.setCodelecvalv(prog.getValvuprog().get(k).getCodelecvalv());
								tar.setFechaini(prog.getDays().get(i));
								tar.setHorafin(prog.getHoras().get(j));
								tar.setDuracion(prog.getValvuprog().get(k).getDuracion());
								
								if (Integer.parseInt(tar.getCodelecvalv()) < 10) {
									tar.setCodelecvalv("0" + tar.getCodelecvalv());
								}
								
								//Si no está leida INSERTO TAREA en la tabla tareasprogdia
								
								try {
									
										sentenciapre = conn.prepareStatement("INSERT INTO tareasprogdia (idprog, horaini, duracion,"+
																			   " hecha, codelecvalv) " +
																			   "VALUES ('"+tar.getIdprog()+
																			   	"','"+tar.getHorafin()+
																			   	"','"+tar.getDuracion()+
																			   	"','"+tar.isHecha()+
																			   	"','"+tar.getCodelecvalv()+"' )") ;
										sentenciapre.executeUpdate();
										
										
										
										// Consulto tareas pendientes de ejecutar ¿?
										System.out.println(tar.getIdprog());
										sentenciapre = conn
												.prepareStatement("select * from tareasprogdia where IDPROG="
														+ tar.getIdprog());
										resultados = sentenciapre.executeQuery();
										
										while (resultados.next()) {
											tar.setIdtareaprog( +resultados.getInt(1));
											
										}
										
										
										
									} catch (SQLException e) {
										e.printStackTrace();
										System.out.println("No se ha podido actualizar la tareaprog en la BBDD local.");
										
									}
								
								
								//Actualizo la fecha de la ultima actualizacion de la programacion
								actualizadeberes(prog,fechahoy);
								
								
								
								
								//HEY, posiblemente tenga que añadirlo en la raspi para que no se entorpezcan en el for
								
								try {
									Thread.sleep(600);
								} catch (InterruptedException e) {
									// Auto-generated catch block
									e.printStackTrace();
								}
								//Añado un delay para que no se pisen los hilos
								//Irrisoft.progs.anadedelay=Irrisoft.progs.anadedelay+100;
								
								ejecutareaprog(tar);
								cont++;
								
								}
						}	
				}
			  
			}
		return existe;
	}
	

	// //////////////////////////////////////////////////////////////////////////
	//
	// Ejecuta tareas de programaciones de hoy
	//
	protected synchronized void ejecutareaprog(TareaProg tar) {		
		
			//SI es MCI
			if (Integer.parseInt(tar.getCodelecvalv())<1000){
				//Compruebo si la valvula ya está abierta por otra tarea y la desecho!!
				if (ListaValvMci.getInstance().getvalvmci(Integer.parseInt(tar.getCodelecvalv())).isAbierta()){
					
					Irrisoft.window.textArea
					.append("\nLa válvula "+tar.getCodelecvalv()+" ya se encuentra abierta por \nla tarea " +
								Irrisoft.window.valvsmci.getvalvmci(tar.getCodelecvalv()).getTareaasoc());
				}else{
					ejecutatareaproghilo(tar);
				}
				//Compruebo si la valvula ya está abierta por otra tarea y la desecho!!
			//Si es BT2
			}else {
				//Es la BT2
				if (Integer.parseInt(tar.getCodelecvalv()) <2000){
					int codelecvalv = Integer.parseInt(tar.getCodelecvalv())-1001;
					if (ListaValvBt22.getInstance().getvalvbt2(codelecvalv).isAbierta()){
						Irrisoft.window.textArea
						.append("\nLa válvula "+tar.getCodelecvalv()+" ya se encuentra abierta por \nla tarea " +
									Irrisoft.window.valvsmci.getvalvmci(codelecvalv).getTareaasoc());
					}else {
						ejecutatareaproghilo(tar);
					}
				}else{
					//Es la Bt22
					int codelecvalv = Integer.parseInt(tar.getCodelecvalv())-2001;
					if (ListaValvBt22.getInstance().getvalvbt2(codelecvalv).isAbierta()){
						Irrisoft.window.textArea
						.append("\nLa válvula "+tar.getCodelecvalv()+" ya se encuentra abierta por \nla tarea " +
									Irrisoft.window.valvsmci.getvalvmci(codelecvalv).getTareaasoc());
					}else {
						ejecutatareaproghilo(tar);
					}
				}
			}
	}
	
	
	private void ejecutatareaproghilo(TareaProg tar){		
		
		// ejecuto la tarea en un nuevo hilo
		hiloprog = new HiloPrograma(tar);
		
		System.out.println(tar.getDuracion());
		
		//ListaHilos.getInstance().addhilo(new Thread(hiloprog));
		
		//tar.setHilotar(ListaHilos.getInstance().gethilo(posi));
		

		tar.setHilotar(new Thread(hiloprog));
		//tar.getHilotar().setPriority(newPriority)
//		
//		System.out.println("Ejecutatarea hilo: " + ListaTareasaexec.getInstance().gettarea(posi).hilotar.getId());
//		System.out.println("Ejecutatarea listahilos: " + ListaHilos.getInstance().gethilo(posi).getId());
	
		tar.hilotar.start();
		
		
		
		}
	
	
	public synchronized void actualizatareaprog (TareaProg tar){
		PreparedStatement sentenciapre;
		try {
				conecta();
				sentenciapre = conn.prepareStatement("UPDATE tareasprogdia set hecha='1' WHERE idtarea="+tar.getIdtareaprog());
				sentenciapre.executeUpdate();
				tar.setHecha(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("No se ha podido actualizar la tarea de la programacion.");		
		}	
	}
	
	
	protected synchronized void actualizadeberes (Programacion prog, String fechahoy){
		PreparedStatement sentenciapre;
		conecta();
		
		try {
			sentenciapre = conn.prepareStatement("UPDATE programa set ultdeberes='"+fechahoy+"' WHERE IDPROGRAMA="+prog.getIdprograma());
			sentenciapre.executeUpdate();
		} catch (SQLException e) {
			// Auto-generated catch block
			e.printStackTrace();
			System.out.println("No se ha podido actualizar la progracmacion a leida.");	
		}
		
		
	
	}
}
