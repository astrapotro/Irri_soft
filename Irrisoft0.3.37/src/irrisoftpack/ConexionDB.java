package irrisoftpack;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

import programapack.HiloPrograma;
import programapack.ListaProgsaexec;
import programapack.Programacion;
import programapack.TareaProg;



import tareapack.HiloTarea;
import tareapack.ListaTareasaexec;
import tareapack.Tarea;
import valvulaspack.ListaValvBt2;
import valvulaspack.ListaValvBt22;
import valvulaspack.ListaValvMci;
import valvulaspack.ListaValvMci2;
import valvulaspack.Valvula;



public class ConexionDB {

	protected Tarea tarea;
	protected TareaProg tar;
	protected Connection conn = null;
	protected Timestamp fechapri,fechactual;
	protected Programacion prog;

	
	
	protected HiloTarea hilotarea;
	protected HiloPrograma hiloprog;
	protected boolean conectado = false;
	protected LinkedList<Programacion> listaprogs=ListaProgsaexec.getInstance().getprogramas();
	protected SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	protected ArrayList<Integer> diass = new ArrayList<Integer>();
	protected ArrayList<Date> diasd = new ArrayList<Date>();
	protected PreparedStatement sentenciapre;
	protected ResultSet resultados;
	protected int id, idds;
	
	public String hoyes;
	protected boolean apagarvalvs;



	// ///////////////////////////////////////////////////////////////////
	//
	// Gestión conexiones a las BBDD

	public boolean conectal() {
		
		while (conectado == false) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				// LOCAL
				String urllocal = "jdbc:mysql://"
						+ Irrisoft.config.getHost() + ":"
						+ Irrisoft.config.getPuerto() + "/"
						+ Irrisoft.config.getDb();
				conn = DriverManager.getConnection(urllocal,
						Irrisoft.config.getUsuario(),
						Irrisoft.config.getPass());
				
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

	public boolean desconectal() {
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

	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	
	
	// ////////////////////////////////////////////////////////////////////////////
	//
	// Comprueba TAREAS pendientes, synchronized para que no puedan acceder varioshilos a la vez
	//
	protected synchronized void tarea() {

		try {
			
			// Consulto tareas pendientes de ejecutar
			sentenciapre = conn
					.prepareStatement("select * from tarea where CODPROG="
							+ Irrisoft.config.getIdrasp());
			resultados = sentenciapre.executeQuery();

			
			while (resultados.next()) {
				
				// controlar si es tarea manual o programación.
				idds = resultados.getInt(4);
				id = resultados.getInt(1);
				
				//Compruebo que es una tarea nueva !!
				if (Irrisoft.window.idtareaexec<id){
					
					if(idds!=6)
						Irrisoft.window.idtareaexec=id;
					
					//Si son tareas manuales
					if (idds==1 || idds==2 || idds==6 || idds==4) {
						
						System.out.println("Hola nueva tarea !!!");	
						tarea =  new Tarea();
						
						tarea.setIddstarea(resultados.getInt(4));
						tarea.setIdtarea(resultados.getInt(1));
						tarea.setCodprog(resultados.getString(2));
						tarea.setCodelecvalv(resultados.getString(3));
						tarea.setFcexec(resultados.getTimestamp(5));
						tarea.setValor(resultados.getInt(6));
						tarea.setValorstr((resultados.getInt(7)));
						tarea.setCodtarea(resultados.getString(8));
						tarea.setDstarea(resultados.getString(9));
							
						Irrisoft.window.textArea.append("\nNueva tarea recibida: "+tarea.getIddstarea()+" "+tarea.getDstarea());
						
						
						if (idds !=4 ){
							//if((tarea.getCodelecvalv() != null) && !tarea.getCodelecvalv().equalsIgnoreCase("null")){
							if(tarea.getCodelecvalv().equalsIgnoreCase("null")){
								if (idds ==2){
									apagarvalvs = true;
								}
							}else{
								if (Integer.parseInt(tarea.getCodelecvalv()) < 10) 
									tarea.setCodelecvalv("0" + tarea.getCodelecvalv());
							}
						}
						
						if (tarea.getIddstarea() == 1) {
							fechapri = tarea.getFcexec();
							tarea.setFcloc(fechapri);
								
							ejecutarea(tarea);
								
								//Lo pongo para que le de tiempo a ejecutar tareas simultaneas (Ajustable)(A REVISAR) !!
								try {
									Thread.sleep(800);
								} catch (InterruptedException e) {
									// 
									e.printStackTrace();
								}				
							
							
						} else if (tarea.getIddstarea() == 2) { //Es  cerrar valvulas
							
								if (apagarvalvs){//Si es apagar todas las estaciones !!!
									
									Irrisoft.window.textArea.append("\nAtención se van a cerrar todas las valvulas!");
									Irrisoft.window.frmIrrisoft.repaint();
									cierratodasvalvs();
								}
								
							 
							}else if (tarea.getIddstarea() ==4){	//Es apagar el programador...
								System.out.println(("APAGANDO el sistema..."));
								Irrisoft.window.textArea.append("\nAtención se va a apagar el sistema!");
								Irrisoft.window.textArea.append("\nSe cerrarán todas las valvulas!");
								Irrisoft.window.frmIrrisoft.repaint();
								try {
										//TODO Habría que guardar el estado antes de apagar o algo ¿?
									cierratodasvalvs();
									
									String[] cmd = new String[]{"/bin/sh", Irrisoft.comandoapaga};
									Runtime.getRuntime().exec(cmd);
									
								} catch (Exception e) {
									//
									e.printStackTrace();
								}
								
							}else if (tarea.getIddstarea() == 6) { //Es un cambio de cuota
								System.out.println("\nEs un cambio de cuota!!\n");
								//Recorro la lista de programas y asocio la nueva cuota a este programa.
								for (int i=0;i<listaprogs.size();i++){
									System.out.println("Progs: "+listaprogs.get(i).getIdprograma());
									if (listaprogs.get(i).getIdprograma()==tarea.getValorstr()){
										System.out.println("Cuota actual: "+listaprogs.get(i).getCuota());
										System.out.println("Nueva cuota ("+tarea.getValor()+") encontrada para la programacion "+listaprogs.get(i).getIdprograma());
										
										Irrisoft.window.textArea.append("\nNueva cuota ("+tarea.getValor()+") encontrada para la prog "+listaprogs.get(i).getIdprograma());
										listaprogs.get(i).setCuota(tarea.getValor());
										
										for(int j=0; j<listaprogs.get(i).getTareasprog().size();j++){
											listaprogs.get(i).getTareasprog().get(j).setCuota(tarea.getValor());
										}
											
										System.out.println("Cuota cambiada: "+listaprogs.get(i).getCuota());
									}
									
									
								}
								//llamar a borrartarea...
								Borratarea(tarea);
							}
						 }
					tarea=null;
						}
					}
				sentenciapre.close();
				resultados.close();
			
				} catch (SQLException e) {
			
				e.printStackTrace();
				System.out.println("Error 265");
	
			}

	}
	
	protected void cierratodasvalvs(){	
		
	try {
			
//			Irrisoft.window.valvsabiertas();
			
//			System.out.println("LISTA VALVS ABIERTAS SIZE: "+Irrisoft.window.valvsabiertas.size());
			
//			for (int i = 0; i<Irrisoft.window.valvsabiertas.size();i++){
//				
//				if(Irrisoft.window.valvsabiertas.get(i).isAbierta()){
//					//MCI
//					if (Integer.parseInt(Irrisoft.window.valvsabiertas.get(i).getCodelecvalv())<24){
//						
//						Irrisoft.window.semaforomci.take();
//						Irrisoft.window.panelmci.interruptor(Irrisoft.window.valvsabiertas.get(i),Irrisoft.window.valvsabiertas.get(i).getImgasoc());
//						Irrisoft.window.semaforomci.release();
//					}
//					//MCI2
//					else if (Integer.parseInt(Irrisoft.window.valvsabiertas.get(i).getCodelecvalv())>24 && Integer.parseInt(Irrisoft.window.valvsabiertas.get(i).getCodelecvalv())<100){
//						
//						Irrisoft.window.semaforomci2.take();
//						Irrisoft.window.panelmci.interruptor(Irrisoft.window.valvsabiertas.get(i),Irrisoft.window.valvsabiertas.get(i).getImgasoc());
//						Irrisoft.window.semaforomci2.release();
//					}
//					//BT2
//					else if (Integer.parseInt(Irrisoft.window.valvsabiertas.get(i).getCodelecvalv())>1000 && Integer.parseInt(Irrisoft.window.valvsabiertas.get(i).getCodelecvalv())<2000){
//						Irrisoft.window.panelbt2.interruptor(Irrisoft.window.valvsabiertas.get(i),2,3);
//						
//					}
//					//BT22
//					else if (Integer.parseInt(Irrisoft.window.valvsabiertas.get(i).getCodelecvalv())>2000){
//						Irrisoft.window.panelbt2.interruptor(Irrisoft.window.valvsabiertas.get(i),2,4);
//						
//					}
//					
//				}
//				
//			}
		
			//Vacío lista de valvulas abiertas
//			Irrisoft.window.valvsabiertas.clear();
			
			
			
			//serie.conectaserial(Irrisoft.config.getMci(), 1);
			for (int i=0;i<Irrisoft.window.valvsmci.getsizeof();i++){
				if (Irrisoft.window.valvsmci.getvalvmci(i).isAbierta()){
					//cierravalv(Irrisoft.window.valvsmci.getvalvmci(i).getCodelecvalv(), 1);
					Irrisoft.window.semaforomci.take();
					Irrisoft.window.panelmci.interruptor(Irrisoft.window.valvsmci.getvalvmci(i),Irrisoft.window.valvsmci.getvalvmci(i).getImgasoc());
					Irrisoft.window.semaforomci.release();
				}
			}
			//serie.desconectaserial();
			
			
			/////////////////
			
			
			if(Irrisoft.config.getMci2()!=null){
				
				//serie.conectaserial(Irrisoft.config.getMci2(), 2);
				for (int i=0;i<Irrisoft.window.valvsmci2.getsizeof();i++){
					if (Irrisoft.window.valvsmci2.getvalvmci(i).isAbierta()){
					 //serie.cierravalv(Irrisoft.window.valvsmci2.getvalvmci(i).getCodelecvalv(), 2);
						Irrisoft.window.semaforomci2.take();
						Irrisoft.window.panelmci.interruptor(Irrisoft.window.valvsmci2.getvalvmci(i),Irrisoft.window.valvsmci2.getvalvmci(i).getImgasoc());
						Irrisoft.window.semaforomci2.release();
					}
				}
				//serie.desconectaserial();
				
			}
			/////////////////////
			
			
			//serie.conectaserial(Irrisoft.config.getBt2(), 3);
			for (int i=0;i<Irrisoft.window.valvsbt2.getsizeof();i++){
				if (Irrisoft.window.valvsbt2.getvalvbt2(i).isAbierta()){
					 //serie.cierravalv(Irrisoft.window.valvsbt2.getvalvbt2(i).getCodelecvalv(), 3);
						
						Irrisoft.window.panelbt2.interruptor(Irrisoft.window.valvsbt2.getvalvbt2(i),2,3);
						
				}
			}
			//serie.desconectaserial();
			
			
			
			//////////////////////////
			if(Irrisoft.config.getBt22()!=null){
				
				//serie.conectaserial(Irrisoft.config.getBt22(), 4);
				for (int i=0;i<Irrisoft.window.valvsbt22.getsizeof();i++){
					if (Irrisoft.window.valvsbt22.getvalvbt2(i).isAbierta()){
						 //serie.cierravalv(Irrisoft.window.valvsbt22.getvalvbt2(i).getCodelecvalv(), 4);
						
						Irrisoft.window.panelbt2.interruptor(Irrisoft.window.valvsbt22.getvalvbt2(i),2,4);
						
					}
				}
				//serie.desconectaserial();
				
			}
	
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		//Borro la tarea de cerrar las programaciones.
		//Además borro todas las tareas de apertura que haya en la tabla tarea !!
		Borratarea(tarea);
		Borratarea();
	}


	// //////////////////////////////////////////////////////////////////////////
	//
	// Ejecuta tareas manuales pendientes
	//
	protected synchronized void ejecutarea(Tarea tar) {
		
		
		// Para hacer la diferencia y saber el tiempo de riego
			//tar.duracion = (tar.getFcexec().getTime() - tar.getFcloc().getTime());
		tar.setDuracion(tar.getValor());
		
//			Irrisoft.window.textArea
//			.append("\nUna nueva tarea ("+tar.getIdtarea()+") se ha encontrado:");
//			Irrisoft.window.frmIrrisoft.repaint();
			
			
			if (Integer.parseInt(tar.getCodelecvalv())<1000){
				//Compruebo si la valvula ya está abierta por otra tarea y la desecho!!
				if (ListaValvMci.getInstance().getvalvmci(Integer.parseInt(tar.getCodelecvalv())-1).isAbierta()){
					
					Irrisoft.window.textArea
					.append("\nLa válvula "+tar.getCodelecvalv()+" ya se encuentra abierta por \nla tarea " +
								Irrisoft.window.valvsmci.getvalvmci(tar.getCodelecvalv()).getTareaasoc());
				}else{
					ejecutatareahilo(tar);
				}
				//Compruebo si la valvula ya está abierta por otra tarea y la desecho!!
			}else {
				
				int codelecvalv;
				
				if (Integer.parseInt(tar.getCodelecvalv())<2000)
					codelecvalv = Integer.parseInt(tar.getCodelecvalv())-1001;
				else
					codelecvalv = Integer.parseInt(tar.getCodelecvalv())-2001;
				
				if (ListaValvBt2.getInstance().getvalvbt2(codelecvalv).isAbierta()){
					Irrisoft.window.textArea
					.append("\nLa válvula "+tar.getCodelecvalv()+" ya se encuentra abierta por \nla tarea " +
								Irrisoft.window.valvsmci.getvalvmci(tar.getCodelecvalv()).getTareaasoc());
				}else {
					ejecutatareahilo(tar);
				}
			}
	}
	
	public synchronized boolean Borratarea(Tarea tar) {

		//Cuidado con borrar tareas diferentes a las solicitadas
		PreparedStatement sentenciapre=null;
		Boolean borrada=false;
				  
		  try {
				sentenciapre = conn
						.prepareStatement("DELETE FROM tarea WHERE idtarea="
								+ tar.getIdtarea());
				sentenciapre.executeUpdate();
				borrada =  true;
		  } catch (SQLException e) {
				e.printStackTrace();
				System.out
						.println("No se ha podido borrar la tarea en la BBDD local.");;
		  }
		  
		try {
			sentenciapre.close();
		} catch (SQLException e) {
			//
			e.printStackTrace();
		}
		
		for (int i = 0; i<ListaTareasaexec.getInstance().getTareas().size(); i++){
			if (ListaTareasaexec.getInstance().getTareas().get(i).getIdtarea()==tar.getIdtarea())
				ListaTareasaexec.getInstance().getTareas().remove(i);
		}
		
		tar = null;
		return borrada;
	}

	public synchronized boolean Borratarea() {

		//Borro todas las tareas de apertura (1) que estén pendientes de ejecutar
		PreparedStatement sentenciapre=null;
		Boolean borrada=false;
				  
		  try {
				sentenciapre = conn
						.prepareStatement("DELETE FROM tarea WHERE iddstarea='1' AND codprog="+Irrisoft.config.getIdrasp());

				sentenciapre.executeUpdate();
				borrada =  true;
		  } catch (SQLException e) {
				e.printStackTrace();
				System.out
						.println("No se han podido borrar las tareas en la BBDD local.");;
		  }
		  
		try {
			sentenciapre.close();
		} catch (SQLException e) {
			//
			e.printStackTrace();
		}
		
		return borrada;
	}
	
	private synchronized void ejecutatareahilo(Tarea tar){
		// ejecuto la tarea en un nuevo hilo
		hilotarea = new HiloTarea(tar);
		
		//ListaHilos.getInstance().addhilo(new Thread(hilotarea));
		ListaTareasaexec.getInstance().addtarea(tar);
		tar.setHilotar(new Thread(hilotarea));
//		
//		System.out.println("Ejecutatarea hilo: " + ListaTareasaexec.getInstance().gettarea(posi).hilotar.getId());
//		System.out.println("Ejecutatarea listahilos: " + ListaHilos.getInstance().gethilo(posi).getId());
		
		tar.hilotar.start();
	}
	
	
	///////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////
	
	
	
	
	/////////////////////////////////////////////////////////////////////////////
	//
	//Comprueba PROGRAMACIONES pendientes y si las hay las copia a la local y borra la tarea y demases de la paserela
	protected  void programa(){
		
		PreparedStatement sentenciapre;
		ResultSet resultados;
		
		Date hoy = null;
		
		hoyes = new Timestamp(Calendar.getInstance().getTime().getTime()).toString();
		hoyes=hoyes.substring(0, 10);
	
		try {
	 
			hoy = formatter.parse(hoyes);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
	
		try {
			
		//Consulto programaciones pendientes de ejecutar
		sentenciapre = conn.prepareStatement("select * from programa where CODPROG="+Irrisoft.config.getIdrasp());
		resultados = sentenciapre.executeQuery();
		
		
			while (resultados.next()) {
				boolean existe=false;
				diass.clear();
				
				//Controlo que la prog no ha sido tratada y si no la trato
				//Poner a null al final para liberar memoria!!!!!
				int id = resultados.getInt(1);
				Date fechain = resultados.getDate(4);
				Date fechafin = resultados.getDate(5);
				Date ultimosdeberes = resultados.getDate(22);
				String tipo = resultados.getString(8);
				
				
				 //System.out.println("hoy: "+ hoy);
				
				for (int i=0;i<7;i++){
					diass.add(resultados.getInt(i+9));
				}
				
				//Solo entro si el programa está activo (está entre los rangos de fecha)
				if((hoy.getTime()>=fechain.getTime()) && (hoy.getTime()<=fechafin.getTime())){
					
					//System.out.println("Ultimosdeberes de prog: "+ id+" " +ultimosdeberes);
					
					//Miro si el programa existe en la listaprogs
					 for(int i=0;i<listaprogs.size();i++){
						 //System.out.println("Programa en listaprogs: "+listaprogs.get(i).getIdprograma());
						 if(listaprogs.get(i).getIdprograma()==id){
							 existe=true;
						 }
					 }
					 
					 //Si la programacion no existe en memoria
					 if(!existe){
						if (esfecha(id,tipo,fechain, fechafin,hoy,diass)){
							
							if (ultimosdeberes==null || (!ultimosdeberes.equals(hoy))){
								    	
									System.out.println("Creo objeto prog");
									prog =  new Programacion();
									prog.setIdprograma(resultados.getInt(1));
									prog.setFcinicio(resultados.getDate(4));
									prog.setFcFin(resultados.getDate(5));
									prog.setLeido(resultados.getString(20));
								    prog.setCodprograma(resultados.getString(2));
									prog.setDsprograma(resultados.getString(3));
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
								    prog.setUltdeberes(resultados.getDate(22));
									
								datosprog(prog);
								tipoprog(prog,hoy);
							}
								 
						}
						
					 }else { //SI existe en memoria (se ha tratado)
						
						 	//Borro las tareas ya hechas
//						 	sentenciapre = conn.prepareStatement("DELETE from tareasprogdia WHERE idprog="+id+" AND hecha='1'");
//						 	sentenciapre.executeUpdate();
//						 	
//							 System.out.println(("Se han borrado las tareas de tareasprogdia del programa: "+id));
							
							 for(int i=0;i<listaprogs.size();i++){
								 
								 if(listaprogs.get(i).getIdprograma()==id){
									 //Recorro lista tareas
									 System.out.println("SIZE listatareasProg de prog "+listaprogs.get(i).getIdprograma()+": "+listaprogs.get(i).getTareasprog().size());
									  int conthecha = 0;
									  for(int j=0;j<listaprogs.get(i).getTareasprog().size();j++){
										  
										  if (listaprogs.get(i).getTareasprog().get(j).isHecha()==1){
											  
											  conthecha++;
										  } 
									  }
									  //if ((conthecha!=0 && conthecha==listaprogs.get(i).getTareasprog().size()) || listaprogs.get(i).getTareasprog().size()==0 ){
									  if ((conthecha!=0 && conthecha==listaprogs.get(i).getTareasprog().size())){
										  
										  System.out.println("Borro lista tareas, prog "+listaprogs.get(i).getIdprograma());
										  System.out.println("Conthecha = "+conthecha);
										  
										
										  
										  //borro BBDD local
										  actualizadeberes (listaprogs.get(i).getIdprograma(), hoyes);
										  borratareaprog(id);
										  System.out.println(("Programa borrado: "+ id));
										//Para que el gc me los quite de la memoria
										  listaprogs.get(i).getValvuprog().clear();
										  listaprogs.get(i).getDays().clear();
										  listaprogs.get(i).getHorasfin().clear();
										  listaprogs.get(i).getHorasini().clear();
										  listaprogs.get(i).getTareasprog().clear();
										  listaprogs.set(i, null);
										  listaprogs.remove(i);
										  
									  }
									
								 }
							 
							 
						 }
							//sentenciapre.close();
					}
		 		
				}
				
				if (fechafin.before(hoy))
					//ZUMBARME programacion de la BBDD 
					borraprogramacion(id);
				
						   
			}//del while
			sentenciapre.close();
			resultados.close();
			
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Error 440");
				System.out.println("No estás conectado con la BBDD !!");
			}
		
			hoyes = null;
	}
	
	private boolean esfecha(int id,String tipo, Date fechain, Date fechafin, Date hoy, ArrayList<Integer> dias){
		
		boolean eshoy=false;
		PreparedStatement sentenciapre;
	
			if (tipo.compareTo("D")==0){
				//System.out.println("Es diaria esfecha");
				
				//Recorro días de programacion y miro si la fecha es hoy
				//OJO tengo que hacer la consulta aki
				//Consulto y guardo los días de ejecución
				try {
						diasd.clear();
						sentenciapre = conn.prepareStatement("select * from dias where IDPROGRAMA="+id);
						ResultSet result = sentenciapre.executeQuery();
						while (result.next()) {
							diasd.add(result.getDate(5));
						}
						sentenciapre.close();
						result.close();
				} catch (SQLException e1) {
					//Auto-generated catch block
					e1.printStackTrace();
				}
				
				for (int e=0;e<diasd.size();e++){
					//Si la tarea es de hoy
					if (((diasd.get(e)).equals(hoy))){ 
						eshoy=true;
						
						}
					
					}
					
				}else if (tipo.compareTo("S")==0){
					
						    Calendar calendar = Calendar.getInstance();
					        calendar.setTime(hoy);
					        int dia = calendar.get(Calendar.DAY_OF_WEEK);
					      
					        //System.out.println("Es semanal,día: "+dia);
					        
					      //Bucle para saber el día de la semana:
					        for (int i=0;i<dias.size();i++){
					        	if (dias.get(i)==1){
					        		if (dia==1){
					        			if(dias.get(6)==1)
					        				eshoy=true;
					        		}else if(dia==i+2){
					        			eshoy=true;
					        		}
					        			
					        		
					        	}
					        }
					
				}	
			return eshoy;
	}
	
 	private void tipoprog(Programacion prog, Date hoy){
		
		if (prog.getTipo().compareTo("D")==0){
			//System.out.println("Es diaria");
			//Recorro días de programacion y miro si la fecha es hoy
			for (int e=0;e<prog.getDays().size();e++){
				//Si la tarea es de hoy
				
				if (((prog.getDays().get(e)).equals(hoy))){
					 creatareasprog(prog);
					}	
				}
			
			}else if (prog.getTipo().compareTo("S")==0){
				//System.out.println("es SEMANAL");
				
				//Compruebo si el día de hoy está marcado para hacer
				if ((prog.getFcinicio().before(hoy) && prog.getFcFin().after(hoy)) || (prog.getFcinicio().equals(hoy)) || (prog.getFcFin().equals(hoy))){
					
					    Calendar calendar = Calendar.getInstance();
				        calendar.setTime(hoy);
				        int dia = calendar.get(Calendar.DAY_OF_WEEK);
				       // System.out.println("Dia: "+dia);
				        
				        //Switch para saber el día:
				        if (prog.getDiad()==1){
				        	//Domingo
				        	if (dia==1){creatareasprog(prog);}
				        }
				        if(prog.getDial()==1){
				        	//Lunes
				        	if (dia==2){creatareasprog(prog);}
				   
				        }
				        if(prog.getDiam()==1){
				        	//Martes
				        	if (dia==3){creatareasprog(prog);}	
				        }
				        if(prog.getDiax()==1){
				        	//Miercoles
				        	if (dia==4){creatareasprog(prog);}
				        }
				        if(prog.getDiaj()==1){
				        	//Jueves
				        	if (dia==5){creatareasprog(prog);}
				        }
				        if(prog.getDiav()==1){
				        	//Viernes
				        	if (dia==6){creatareasprog(prog);}
				        }
				        if(prog.getDias()==1){
				        	//Sabado
				        	if (dia==7){creatareasprog(prog);}
				        }

				}
				
			}
	}
	
 	private void datosprog(Programacion prog){
		
		PreparedStatement sentenciapre;
		
		
		try {
			//Consulto y guardo las valvulas asociadas a la programacion
			sentenciapre = conn.prepareStatement("select * from valvulas where IDPROGRAMA="+prog.getIdprograma());
			ResultSet result = sentenciapre.executeQuery();
			
			
			while (result.next()) {//A mejorar poniendo sólo la electrovalvula y recorriendo la lista de 
										  //valvulas correspondiente
				
				Valvula valv = new Valvula();
				valv.setCodelecvalv(result.getString(5));
				valv.setProgasoc(prog.getIdprograma());
				valv.setDuracion(result.getInt(6));
				valv.setBloque(result.getInt(7));
				
				prog.getValvuprog().add(valv);
				//System.out.println(prog.getValvuprog().get(i).getCodelecvalv());
				
			}
			
			//Consulto y guardo los días de ejecución
			sentenciapre = conn.prepareStatement("select * from dias where IDPROGRAMA="+prog.getIdprograma());
			result = sentenciapre.executeQuery();
			
			while (result.next()) {
				prog.getDays().add(result.getDate(5));
				//System.out.println(prog.getDays().get(i).toString());
				
			}
			
			//Consulto y guardo las horas de ejecución
			sentenciapre = conn.prepareStatement("select * from horas where IDPROGRAMA="+prog.getIdprograma());
			result = sentenciapre.executeQuery();
			
			while (result.next()) {
				prog.getHorasini().add(result.getTime(5));
				prog.getHorasfin().add(result.getTime(6));
//				System.out.println(prog.getHorasini().get(i).toString());
//				System.out.println(prog.getHorasfin().get(i).toString());
			
			}
			sentenciapre.close();
			result.close();
		} catch (SQLException e) {
			// 
			e.printStackTrace();
		}	
	
	}
	
	protected synchronized void creatareasprog(Programacion prog){
		
		PreparedStatement sentenciapre;	
		
		//System.out.println("Estoy en tareasprog");
		for (int k=0; k<prog.getValvuprog().size();k++){
			for (int j=0;j<prog.getHorasini().size();j++) {
				tar = new TareaProg();
				tar.setIdprog(prog.getIdprograma());
				tar.setCodelecvalv(prog.getValvuprog().get(k).getCodelecvalv());
				tar.setFechaini(prog.getFcinicio());
				tar.setHoraini(prog.getHorasini().get(j));
				//Le sumo medio segundo a la hora final para que le de tiempo a hacerse
				tar.setHorafin(new Time(prog.getHorasfin().get(j).getTime()));
				tar.setDuracion(prog.getValvuprog().get(k).getDuracion());
				tar.setDuracionini(prog.getValvuprog().get(k).getDuracion());
				tar.setHecha(0);
				tar.setBloque(prog.getValvuprog().get(k).getBloque());
				tar.setTipobloque(prog.getModoini());
				tar.setCuota(prog.getCuota());
				
				
				
				if (Integer.parseInt(tar.getCodelecvalv()) < 10) {
					tar.setCodelecvalv("0" + tar.getCodelecvalv());
				}
				
				if(Integer.parseInt(tar.getCodelecvalv())<=28){
					tar.setTipoplaca(1);
					tar.setPuertoserie(Irrisoft.config.getMci());
				}else if (Integer.parseInt(tar.getCodelecvalv())>28 && Integer.parseInt(tar.getCodelecvalv())<1000){
					tar.setTipoplaca(2);
					tar.setPuertoserie(Irrisoft.config.getMci2());
				}else if(Integer.parseInt(tar.getCodelecvalv())>1000 && Integer.parseInt(tar.getCodelecvalv()) <2000){
					tar.setTipoplaca(3);
					tar.setPuertoserie(Irrisoft.config.getBt2());
				}else if (Integer.parseInt(tar.getCodelecvalv()) >2000){
					tar.setTipoplaca(4);
					tar.setPuertoserie(Irrisoft.config.getBt22());
				}
				
//				if (tar.getHoraini().compareTo(ahora)>0){
					//System.out.println("Añade a listatareas en mem");
					prog.getTareasprog().add(tar);
				//}
			 }
			
		}
			
		//Ordeno la lista de tareas por horasini
		Collections.sort(prog.getTareasprog());
		
		try {
		//Tareaprogdia tiene la programación ???
		sentenciapre = conn.prepareStatement("select * from tareasprogdia where idprog="+prog.getIdprograma());
		ResultSet result = sentenciapre.executeQuery();
		
		//Si no hay nada para esta programacion en tareaprogdia inserto
		
			if (!result.next()){
				
				System.out.println("Programación ("+prog.getIdprograma()+") encontrada hoy, "+hoyes);
				
				 Irrisoft.window.textArea
					.append("\nProgramación ("+prog.getIdprograma()+") encontrada hoy, "+hoyes+": ");
					Irrisoft.window.frmIrrisoft.repaint();
				
		
				//Actualizo tareaprogdia
				for(int d=0;d<prog.getTareasprog().size();d++){
				
						try {
							
								sentenciapre = conn.prepareStatement("INSERT INTO tareasprogdia (idprog, horaini, duracion,"+
																	   " hecha, codelecvalv) " +
																	   "VALUES ('"+prog.getTareasprog().get(d).getIdprog()+
																	   	"','"+prog.getTareasprog().get(d).getHoraini()+
																	   	"','"+prog.getTareasprog().get(d).getDuracion()+
																	   	"','"+prog.getTareasprog().get(d).isHecha()+
																	   	"','"+prog.getTareasprog().get(d).getCodelecvalv()+"' )") ;
								sentenciapre.executeUpdate();		
						
							} catch (SQLException e2) {
								e2.printStackTrace();
								System.out.println("No se ha podido actualizar la tareaprog en la BBDD local.");
								
							}
					}
				 }
	
			//Asocio id de tareaprogdia con la tarea en memoria
			sentenciapre = conn.prepareStatement("select * from tareasprogdia where idprog="+prog.getIdprograma());
			result = sentenciapre.executeQuery();
		
		//Recorro la lista de tareas para ejecutarlas o no
		for(int z=0;z<prog.getTareasprog().size();z++){
			
			if (result.next()) 
				prog.getTareasprog().get(z).setIdtareaprog(result.getInt(1));
			
			cogetiemporegado(prog.getTareasprog().get(z));
			System.out.println("tar: "+tar.getIdtareaprog()+", "+tar.getTiemporegado());
			
			//Está pendiente de hacer??
			sentenciapre = conn.prepareStatement("select hecha from tareasprogdia where idtarea="+prog.getTareasprog().get(z).getIdtareaprog());
			ResultSet resultSet = sentenciapre.executeQuery();
			
			if (resultSet.next()) 
				prog.getTareasprog().get(z).setHecha(resultSet.getInt(1));
			resultSet.close();
			
			//Si no está hecha
			if(prog.getTareasprog().get(z).isHecha()==0){
				
						//Compruebo si la tarea tiene un hilo asociado
							if (prog.getTareasprog().get(z).getHilotar()==null){
									ejecutareaprog(prog.getTareasprog().get(z),z,prog);
							}
				}
				
			}
		sentenciapre.close();
		result.close();
		
		 //Añado el prog a la lista de programas
		listaprogs.add(prog);
		
		 } catch (SQLException e) {
				// Auto-generated catch block
				e.printStackTrace();
		 }
	}
	

	// //////////////////////////////////////////////////////////////////////////
	//
	// Gestión tareas de programaciones  y de programaciones de hoy
	//
	//
	
	protected synchronized void ejecutareaprog(TareaProg tar,int z,Programacion prog) {		
			
			//SI es MCI
			if (Integer.parseInt(tar.getCodelecvalv())<1000){
				//Compruebo si la valvula ya está abierta por otra tarea y la desecho!!
				if (ListaValvMci.getInstance().getvalvmci(Integer.parseInt(tar.getCodelecvalv())).isAbierta()){
					
					Irrisoft.window.textArea
					.append("\nLa válvula "+tar.getCodelecvalv()+" ya se encuentra abierta por \nla tarea " +
								Irrisoft.window.valvsmci.getvalvmci(tar.getCodelecvalv()).getTareaasoc());
				}else{
					ejecutareaproghilo(tar,z,prog);
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
						ejecutareaproghilo(tar,z,prog);
					}
				}else{
					//Es la Bt22
					int codelecvalv = Integer.parseInt(tar.getCodelecvalv())-2001;
					if (ListaValvBt22.getInstance().getvalvbt2(codelecvalv).isAbierta()){
						Irrisoft.window.textArea
						.append("\nLa válvula "+tar.getCodelecvalv()+" ya se encuentra abierta por \nla tarea " +
									Irrisoft.window.valvsmci.getvalvmci(codelecvalv).getTareaasoc());
					}else {
						ejecutareaproghilo(tar,z,prog);
					}
				}
			}
	}
	
	private void ejecutareaproghilo(TareaProg tar,int z, Programacion prog){		
		
		// ejecuto la tarea en un nuevo hilo
		hiloprog = new HiloPrograma(tar,z,prog);
		
		tar.setHilotar(new Thread(hiloprog));
		
		
		//System.out.println("Hilo Tareaprog numero: "+tar.getHilotar().getId());
	
		tar.hilotar.start();
		
		}
	
	public void actualizatareaprog (TareaProg tar){
		PreparedStatement sentenciapre;
		try {
				tar.setHecha(1);
				conectal();
				sentenciapre = conn.prepareStatement("UPDATE tareasprogdia set hecha='1' WHERE idtarea="+tar.getIdtareaprog());
				sentenciapre.executeUpdate();
				
				sentenciapre.close();
				//TODO Esto es el informe a tratar después 
				//Hay que cambiar para solo añadirla la bt2
				System.out.println("INFORME: Tarea "+tar.getIdtareaprog()+" terminada OK\nValvula "+tar.getCodelecvalv()+" ha regado durante "+(tar.getTiemporegado()+2)+" sgs.");
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("No se ha podido actualizar la tarea de la programacion a hecha.");		
		}
	}
	
	public void borratareaprog (int id){
		PreparedStatement sentenciapre;
		try {
				conectal();
				sentenciapre = conn.prepareStatement("DELETE from tareasprogdia WHERE idprog="+id);
				sentenciapre.executeUpdate();
				sentenciapre.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("No se ha podido actualizar la tarea de la programacion a hecha.");		
		}	
	}
	
	public void borraprogramacion (int id){
		PreparedStatement sentenciapre;
		try {
				conectal();
				sentenciapre = conn.prepareStatement("DELETE from programa WHERE CODPROG="+id);
				sentenciapre.executeUpdate();
				sentenciapre.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("No se ha podido borrar la programacion de la bbdd local.");		
		}	
		
		//Borro programacion de listaprogs en memoria
		for(int i=0;i<listaprogs.size();i++){
			if(listaprogs.get(i).getIdprograma()==id){
				listaprogs.remove(i);
			}
		}
	}

	protected synchronized void actualizadeberes (int id, String fechahoy){
		PreparedStatement sentenciapre;
		try {
			conectal();
			sentenciapre = conn.prepareStatement("UPDATE programa set ultdeberes='"+fechahoy+"' WHERE IDPROGRAMA="+id);
			sentenciapre.executeUpdate();
			sentenciapre.close();
		} catch (SQLException e) {
			// Auto-generated catch block
			e.printStackTrace();
			System.out.println("No se ha podido actualizar la programacion a ultdeberes.");	
		}
	}

	public void actualizatareaprogpasadas(TareaProg tar) {
		PreparedStatement sentenciapre;
		try {
				System.out.println("ACTUALIZO: " +tar.getHoraini());
				tar.setHecha(1);
				conectal();
				sentenciapre = conn.prepareStatement("UPDATE tareasprogdia set hecha='1' WHERE idtarea='"+tar.getIdtareaprog()+"'");
				sentenciapre.executeUpdate();
				sentenciapre.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("No se ha podido actualizar la tarea de la programacion a hecha.");		
		}
	}
	
	public void cogetiemporegado(TareaProg tar) {
			PreparedStatement sentenciapre;
			try {
					conectal();
					sentenciapre = conn.prepareStatement("select tiempoexec from tareasprogdia WHERE idtarea="+tar.getIdtareaprog());
					sentenciapre.executeQuery();
					ResultSet result = sentenciapre.executeQuery();
					while (result.next()) {
						tar.setTiemporegado(result.getInt(1));
					}
					sentenciapre.close();
					result.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("No se ha podido actualizar la tarea de la programacion a hecha.");		
			}
	}
		
	public void ponetiemporegado(TareaProg tar, int tiempoexec) {
			PreparedStatement sentenciapre;
			try {
					
					conectal();
					sentenciapre = conn.prepareStatement("UPDATE tareasprogdia set tiempoexec="+tiempoexec+" WHERE idtarea="+tar.getIdtareaprog());
					sentenciapre.executeUpdate();
					sentenciapre.close();	
					
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("No se ha podido actualizar la tarea de la programacion a hecha.");		
			}
			
		
		
	}
	
	
	
	// //////////////////////////////////////////////////////////////////////////
	//
	// Gestión de datos de consumos (modelo)
	//
	//
	public void borraconsumtest(){
		try{
			conectal();
			sentenciapre = conn.prepareStatement("DELETE from modelconsum");
			sentenciapre.executeUpdate();
			sentenciapre.close();
	
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("No se ha podido insertar los consumos");		
		}
	}
	public void poneconsumostest(String valvula, float caudal, int intensidad){
		
		PreparedStatement sentenciapre;
		try {
				conectal();
				sentenciapre = conn.prepareStatement("INSERT INTO modelconsum (CODPROG, CODELECVALV, CAUDAL," +
						"INTENSIDAD, FECHA) VALUES ('"+Integer.toString(Irrisoft.config.getIdrasp())+
						"','"+valvula+"','"+caudal+"','"+intensidad+
						"','"+new Timestamp(Calendar.getInstance().getTime().getTime())+"' )");
				sentenciapre.executeUpdate();
				sentenciapre.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("No se ha podido insertar los consumos");		
		}
		
	}
	public void sobrescribeconsumostest (String valvula, float caudal, int intensidad){
		
		PreparedStatement sentenciapre;
		try {
				
				conectal();
				sentenciapre = conn.prepareStatement("UPDATE modelconsum set "+
													"CAUDAL='"+caudal+
													"', INTENSIDAD='"+intensidad+
													"', FECHA='"+new Timestamp(Calendar.getInstance().getTime().getTime())+
													"' WHERE CODELECVALV="+valvula);
													
						
				sentenciapre.executeUpdate();
				sentenciapre.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("No se ha podido insertar los consumos");		
		}
		
	}
	public void recogeconsummod(int pos, String valv){
		
		try{
			
			conectal();
			
			sentenciapre = conn.prepareStatement("SELECT CAUDAL , INTENSIDAD from modelconsum  "+
					"WHERE CODELECVALV="+valv+" AND CODPROG="+Irrisoft.config.getIdrasp());
					
	
			resultados=sentenciapre.executeQuery();
		
		
			while (resultados.next()){
				//Si es MCI
				if (Integer.parseInt(valv)<= 27){
					Irrisoft.window.valvsmci.getvalvmci(valv).setCaudalmod(resultados.getFloat(1));
					Irrisoft.window.valvsmci.getvalvmci(valv).setIntensidadmod(resultados.getInt(2));
				}
				//Si es MCI2
				else if ((Integer.parseInt(valv)>27 && Integer.parseInt(valv)<1000)){
					Irrisoft.window.valvsmci2.getvalvmci(valv).setCaudalmod(resultados.getFloat(1));
					Irrisoft.window.valvsmci2.getvalvmci(valv).setIntensidadmod(resultados.getInt(2));

				}
				//Si es BT2
				else if ((Integer.parseInt(valv)>1000 && Integer.parseInt(valv)<2000)){
					Irrisoft.window.valvsbt2.getvalvbt2(valv).setCaudalmod(resultados.getFloat(1));
					Irrisoft.window.valvsbt2.getvalvbt2(valv).setIntensidadmod(resultados.getInt(2));

				}
				//Si es BT22
				else if (Integer.parseInt(valv)>2000 ){
					Irrisoft.window.valvsbt22.getvalvbt2(valv).setCaudalmod(resultados.getFloat(1));
					Irrisoft.window.valvsbt22.getvalvbt2(valv).setIntensidadmod(resultados.getInt(2));

				}
				
			}
			
			
		} catch (SQLException e) {
		e.printStackTrace();
		System.out.println("No se han podido leer los consumos");		
		}
		
		
		
	}


}
