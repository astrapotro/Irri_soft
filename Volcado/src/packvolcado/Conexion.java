package packvolcado;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Conexion {

	//Parámetros de conexión y conexiones
	 Connection connr = null;
	 Connection connl = null;
	 Conf config = new Conf();
	 Tarea tarea = new Tarea();
	 Valvula valv = new Valvula();
	 Programacion prog = new Programacion();
	 public boolean conectado = false;
	 private final String rutaconf = "/home/mikel/conf_volcado.txt"; //Ruta al archivo de configuración
	
	 
	 
	////////////////////////////////////////////////////////////////////////////// 
	//
	//Comprueba TAREAS pendientes y si las hay las copia a la local y las borra de la pasarela
	protected void tarea(){
		
		PreparedStatement sentenciapre, sentenciaprel;
		ResultSet resultadosr, resultadosl;
		
		try {
				//Consulto tareas pendientes de ejecutar en remoto
				sentenciapre = connr.prepareStatement("select * from v02_tareaexec where CODPROG="+config.getIdrasp());
				resultadosr = sentenciapre.executeQuery();
	
				while (resultadosr.next()) {

				//Cojo sólo ésto de la tarea para evitar campos nulls y controlar si es tarea manual o programación.
				tarea.setIddstarea(resultadosr.getInt(4));
				tarea.setIdtareaexec(resultadosr.getInt(1));
				
				
				//Si la tarea es leer una programación (3) llamo a programa para que la gestione.
				if(tarea.getIddstarea()==3) 
					programa(tarea);
				//Si no es una tarea manual
				if ((tarea.getIddstarea())==1 || (tarea.getIddstarea())==2){ 
						
					//aki el resto de los campos de tarea
						tarea.setCodprog(resultadosr.getString(2));
						tarea.setCodelecvalv(resultadosr.getString(3));
						tarea.setFcexec(resultadosr.getTimestamp(5));
						tarea.setValor(resultadosr.getInt(6));
						tarea.setCodtarea(resultadosr.getString(7));
						tarea.setDstarea(resultadosr.getString(8));
						
						//Compruebo que la tarea no existe en local !!
						//Cojo la última tarea de la tabla local
						sentenciaprel = connl.prepareStatement("SELECT MAX(idtarea),fcexec FROM tarea");
						resultadosl = sentenciaprel.executeQuery();
						while (resultadosl.next()) {
						tarea.setIdtarealocal(resultadosl.getInt(1));
						tarea.setFcloc(resultadosl.getTimestamp(2));
						
						//Si la tarealocal es menor es que la tareaexec no está en la bbdd local, con lo que la inserto
						//Comparao las fechas también por si la pasarela se ha reiniciado
						if (tarea.getIdtarealocal()<tarea.getIdtareaexec() || tarea.getFcloc().before(tarea.getFcexec()))
						{
							if(Insertarea(sentenciaprel))
							{
								//Aki tendría que borrar de la t02_
								if (Borratarea(tarea));
									System.out.println("Tarea borrada de la pasarela correctamente !");
							}
							else{
								System.out.println("Error al insertar la tarea de la bbdd remota a la local !!");
							}
						}
					
					 }
				
					}
				}

	
	} catch (SQLException e) {
		// 
		e.printStackTrace();
		System.out.println("Error 93");
	}
	
		
		
}
	protected boolean Borratarea(Tarea tar){
		PreparedStatement sentenciapre;
		System.out.println("Borro tarea en la t02 con ID " + tar.getIdtareaexec());
		try {
			sentenciapre = connr.prepareStatement("DELETE FROM t02_tareaexec WHERE idtareaexec="+tar.getIdtareaexec());
			sentenciapre.executeUpdate();
			sentenciapre.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("No se ha podido borrar la tarea en la BBDD remota.");
			return false;
		}
	}
	protected boolean Insertarea(PreparedStatement sentenciaprel){
		
		System.out.println("Inserto tarea "+tarea.getIdtareaexec()+" en la local");
		try {
			sentenciaprel = connl.prepareStatement("INSERT INTO tarea (idtarea, codprog, codelecvalv, iddstarea, " +
												   "fcexec, valor, codtarea,dstarea) " +
												   "VALUES ('"+tarea.getIdtareaexec()  +
												   	"','"+tarea.getCodprog()+
												   	"','"+tarea.getCodelecvalv()+
												   	"','"+tarea.getIddstarea()+
												   	"','"+tarea.getFcexec()+
												   	"','"+tarea.getValor()+
												   	"','"+tarea.getCodtarea()+
												   	"','"+tarea.getDstarea()+"' )") ;
			sentenciaprel.executeUpdate();
			return true;
		} catch (SQLException e) {
			// 
			e.printStackTrace();
			System.out.println("No se ha podido actualizar la tarea en la BBDD local.");
			return false;
		}
		
		
		
	}
	
	
	/////////////////////////////////////////////////////////////////////////////
	//
	//Comprueba PROGRAMACIONES pendientes y si las hay las copia a la local y borra la tarea y demases de la paserela
	protected  void programa(Tarea tar){
		PreparedStatement sentenciapre, sentenciaprel;
		ResultSet resultadosr, resultadosl;
		
		try {
			//Consulto programaciones pendientes de ejecutar en remoto
			sentenciapre = connr.prepareStatement("select * from v10_programaexec where CODPROG="+config.getIdrasp());
			resultadosr = sentenciapre.executeQuery();
			
	
			while (resultadosr.next()) {
		
				prog.setIdprograma(resultadosr.getInt(1));
				prog.setCodprograma(resultadosr.getString(2));
				prog.setDsprograma(resultadosr.getString(3));
				prog.setFcinicio(resultadosr.getDate(4));
				prog.setFcFin(resultadosr.getDate(5));
				prog.setActivo(resultadosr.getString(6));
				prog.setCodprog(resultadosr.getString(7));
				prog.setTipo(resultadosr.getString(8));
				prog.setDial(resultadosr.getInt(9));
				prog.setDiam(resultadosr.getInt(10));
				prog.setDiax(resultadosr.getInt(11));
				prog.setDiaj(resultadosr.getInt(12));
				prog.setDiav(resultadosr.getInt(13));
				prog.setDias(resultadosr.getInt(14));
				prog.setDiad(resultadosr.getInt(15));
				prog.setModo(resultadosr.getString(16));
				prog.setModoini(resultadosr.getString(17));
				prog.setPbloque(resultadosr.getInt(18));
				prog.setCuota(resultadosr.getInt(19));
				prog.setLeido(resultadosr.getString(20));
				prog.setEnmarcha(resultadosr.getString(21));
				
				
				System.out.println("\nDescripcion: "  + prog.getDsprograma());
				System.out.println(prog.getCodprograma());
				System.out.println(prog.getFcinicio());
			

				//Compruebo que la programacion no existe en local !!
				//Cojo la última progamacion de la tabla
				sentenciaprel = connl.prepareStatement("SELECT MAX(idprograma)  FROM programa");
				resultadosl = sentenciaprel.executeQuery();
				while (resultadosl.next()) {
				prog.setIdprogramal(resultadosl.getInt(1));
					
					//Cojo el dia 
					sentenciapre = connr.prepareStatement("select fecha from v11_progdiasexec where idprograma="+prog.getIdprograma());
					resultadosr = sentenciapre.executeQuery();
					while (resultadosr.next()) {
						prog.setDia(resultadosr.getDate(1));
						System.out.println("Volcado guarda el dia " + prog.getDia());
						//Aki llamo a las inserts de las dias, horas y valvulas;
						if (!insertadia(sentenciaprel)){
							System.out.println("Error al insertar el dia en la local");
						}else{
							//borradia(sentenciapre);
						}
					}
					
					//Cojo las horas y las guardo
					sentenciapre = connr.prepareStatement("select hrinicio,hrfin from v12_proghorasexec where idprograma="+prog.getIdprograma());
					resultadosr = sentenciapre.executeQuery();
					while (resultadosr.next()) {
						prog.setHoraini(resultadosr.getString(1));
						prog.setHorafin(resultadosr.getString(2));
						System.out.println("Volcado guarda las horas " + prog.getHoraini() + " "+prog.getHorafin());
						if (!insertahoras(sentenciaprel)){
							System.out.println("Error al insertar las horas en la local");
						}else{
							//borrahoras(sentenciapre);
						}
					
					}
					
					//Cojo las valvulas y las guardo
					sentenciapre = connr.prepareStatement("select CODELECVALV,DURACION,BLOQUE from v13_progvalvexec where idprograma="+prog.getIdprograma());
					resultadosr = sentenciapre.executeQuery();
					while (resultadosr.next()) {
						 valv.setCodelecvalv(resultadosr.getString(1));
						 valv.setDuracion(resultadosr.getInt(2));
						 valv.setBloque(resultadosr.getInt(3));
						System.out.println("Volcado guarda las valvulas " + valv.getCodelecvalv());
	
						if (!insertavalv(sentenciaprel)){
							System.out.println("Error al insertar las horas en la local");
						}else{
							//borravalv(sentenciapre);
						}
						
					}
					
					
						//Si la proglocal es menor es que la programaexec no está en la bbdd local, con lo que la inserto
						if (prog.getIdprogramal()<prog.getIdprograma())
						{
							if(insertaprog(sentenciaprel))
							{
								//Debería borrar la tarea de la programación una vez EJECUTADA si acaso!!!!
//									if (Borratarea(sentenciapre))
//										System.out.println("Tarea de programacion borrada de la tabla t02 en la pasarela correctamente !");
									borraprog(sentenciapre);
									Borratarea(tar);
							}
							else{
								System.out.println("Error al insertar la tarea de la bbdd remota a la local !!");
							}
						}
							
					  }
			}
	
	} catch (SQLException e) {
		e.printStackTrace();
		System.out.println("Error 261");
	}
						
}
	protected boolean insertaprog(PreparedStatement sentenciaprel){
			
		System.out.println("Aki inserto programacion en la local");
		try {
			sentenciaprel = connl.prepareStatement("INSERT INTO programa (idprograma, codprograma, dsprograma, fcinicio," +
												   "fcfin, activo, codprog,tipo, dial,diam,diax,diaj,diav,dias,diad, " +
												   "modo,modoini,pbloque,cuota,leido,enmarcha)"+
												   "VALUES ('"+prog.getIdprograma()  +
												   	"','"+prog.getCodprograma()+
												   	"','"+prog.getDsprograma()+
												   	"','"+prog.getFcinicio()+
												   	"','"+prog.getFcFin()+
												   	"','"+prog.getActivo()+
												   	"','"+prog.getCodprog()+
												   	"','"+prog.getTipo()+
												   	"','"+prog.getDial()+
												   	"','"+prog.getDiam()+
												   	"','"+prog.getDiax()+
												   	"','"+prog.getDiaj()+
												   	"','"+prog.getDiav()+
												   	"','"+prog.getDias()+
												   	"','"+prog.getDiad()+
												   	"','"+prog.getModo()+
												   	"','"+prog.getModoini()+
												   	"','"+prog.getPbloque()+
												   	"','"+prog.getCuota()+
												   	"','"+prog.getLeido()+
												   	"','"+prog.getEnmarcha()+"' )") ;
			sentenciaprel.executeUpdate();
			
			//sentenciaprel = connl
			return true;
		} catch (SQLException e) {
			// 
			e.printStackTrace();
			System.out.println("No se ha podido actualizar la programacion en la BBDD local.");
			return false;
		}
		
		
		
	}
	protected boolean borraprog(PreparedStatement sentenciapre){
		
		try {
			//Actualizo a leido en la t10 para que me la borre de pendientes !!
			sentenciapre = connr.prepareStatement("UPDATE t10_programa set leido='S' WHERE idprograma="+prog.getIdprograma());
			sentenciapre.executeUpdate();
			System.out.println("Programa ya leido y borrado de la pasarela");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("No se ha podido borrar el programa en la BBDD remota.");
			return false;
		}
	}	
	
	protected boolean insertadia(PreparedStatement sentenciaprel){
		System.out.println("Aki inserto dias en la local");
		try {
			sentenciaprel = connl.prepareStatement("INSERT INTO dias (codprog,idprograma,fecha)"+
												   "VALUES ('"+config.getIdrasp()  +
												   	"','"+prog.getIdprograma()+
												   	"','"+prog.getDia()+"' )") ;
			sentenciaprel.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			// 
			e.printStackTrace();
			System.out.println("No se ha podido actualizar el dia en la BBDD local.");
			return false;
		}
		
	}

	
	protected boolean insertahoras(PreparedStatement sentenciaprel){
		System.out.println("Aki inserto horas en la local");
		try {
			
			sentenciaprel = connl.prepareStatement("INSERT INTO horas (codprog,idprograma,hrinicio,hrfin)"+
												   "VALUES ('"+config.getIdrasp()  +
												    "','"+prog.getIdprograma()+
												   	"','"+prog.getHoraini()+
												   	"','"+prog.getHorafin()+"' )") ;
			sentenciaprel.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			// 
			e.printStackTrace();
			System.out.println("No se ha podido actualizar las horas en la BBDD local.");
			return false;
		}
		
	}

	protected boolean insertavalv(PreparedStatement sentenciaprel){
		System.out.println("Aki inserto valvulas en la local");
		try {
			
			sentenciaprel = connl.prepareStatement("INSERT INTO valvulas (codprog,idprograma,codelecvalv,duracion,bloque)"+
												   "VALUES ('"+config.getIdrasp()  +
												   "','"+prog.getIdprograma()+	
												   "','"+valv.getCodelecvalv()+
												   "','"+valv.getDuracion()+
												   "','"+valv.getBloque()+"' )") ;
			sentenciaprel.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("No se ha podido actualizar valvulas en la BBDD local.");
			return false;
		}
		
	}

	
	
	
	/////////////////////////////////////////////////////////////////////////////
	//
	// Gestión conexiones a las BBDD
	protected void conectar (){
		
		
		while (conectado==false)
		{
			try {
				Class.forName("com.mysql.jdbc.Driver");
							
				// REMOTA
				String urlremota = "jdbc:mysql://"+config.getHostr()+":"+config.getPuertor()+"/"+config.getDbr();
				connr = DriverManager.getConnection(urlremota,config.getUserr(),config.getPassr());
				System.out.println("Conexión con la pasarela establecida !!");
				conectado = true;
			}
			catch (Exception e) {
				//e.printStackTrace();
			
				System.out.println("\n");
				System.out.println("No se ha podido conectar con la BBDD remota!!");
				System.out.println("Reintentando conexion en 10 segundos.");
				int i=0;
				while ( i<10){
					try {
						Thread.sleep(500);
						System.out.print(".");
						Thread.sleep(500);
						System.out.print(".");
						i++;
					} catch (InterruptedException e1) {
						System.out.println("La BBDD remota está caida! Contácte con su administrador.");
					}
				}
			}
		}
	}
	
	protected void conectal (){
		
		boolean conectado = false;
		
		while (conectado==false)
		{
			try {
				Class.forName("com.mysql.jdbc.Driver");
				
				// LOCAL
				String urllocal = "jdbc:mysql://"+config.getHostl()+":"+config.getPuertol()+"/"+config.getDbl();
				connl = DriverManager.getConnection(urllocal, config.getUsuariol(), config.getPassl());
				System.out.println("Conexión con la bbdd local establecida !!");
				conectado = true;
			}
			catch (Exception e) {
				//e.printStackTrace();
				
				System.out.println("\n");
				System.out.println("No se ha podido conectar con la BBDD local!!");
				System.out.println("Reintentando conexion en 10 segundos.");
				int i=0;
				while ( i<10){
					try {
						Thread.sleep(500);
						System.out.print(".");
						Thread.sleep(500);
						System.out.print(".");
						i++;
					} catch (InterruptedException e1) {
						System.out.println("La BBDD local está caida! Contácte con su administrador.");
					}
				}
			}
		}
		
	}//Fin COnectaL

	protected void cierrar (){
		if (connr != null) {
			try {
				connr.close();
				System.out.println("Conexión terminada con la pasarela.");
			} catch (Exception e) { /* ignore close errors */
			}
		}
	}
	
	protected void cierral (){
		if (connl != null) {
			try {
				connl.close();
				System.out.println("Conexión terminada con la bbdd local.");
			} catch (Exception e) { /* ignore close errors */
			}
		}		
	}

	
	//////////////////////////////////////////////////////////////////////////////
	//
	//OJO Lee archivo de configuración conf_volcado.txt !!!
	protected void leerconf(Conf config){
			
			FileReader fr = null;
			String linea = null;
			
			
			try {
				fr = new FileReader(rutaconf); 	//Ruta al archivo de configuración
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("No existe el fichero de configuración !! \n Se sale !" ); 
				
			}
			BufferedReader bf = new BufferedReader(fr);
			
			
			//
			//Leo línea línea el fichero de configuración y lo guardo todo en la clase conf
			//
			try {
				while ((linea = bf.readLine())!=null) {
					
					if (linea.contains("ID")){
						int i = linea.indexOf(":") +1;
						config.setIdrasp(Integer.parseInt(linea.substring(i)));
						//System.out.println(config.getIdrasp());
					}
					else if (linea.contains("HOSTR")){
						int i = linea.indexOf(":") +1;
						config.setHostr((linea.substring(i)));
						//System.out.println(config.getHostr());
					}
					else if (linea.contains("PUERTOR")){
						int i = linea.indexOf(":") +1;
						config.setPuertor(Integer.parseInt(linea.substring(i)));
						//System.out.println(config.getPuertor());
					}
					else if (linea.contains("DBR")){
						int i = linea.indexOf(":") +1;
						config.setDbr((linea.substring(i)));
						//System.out.println(config.getDbr());
					}
					else if (linea.contains("USUARIOR")){
						int i = linea.indexOf(":") +1;
						config.setUserr((linea.substring(i)));
						//System.out.println(config.getUserr());
					}
					else if (linea.contains("PASSR")){
						int i = linea.indexOf(":") +1;
						config.setPassr((linea.substring(i)));
						//System.out.println(config.getPassr());
					}
					else if (linea.contains("HOSTL")){
						int i = linea.indexOf(":") +1;
						config.setHostl((linea.substring(i)));
						//System.out.println(config.getHostl());
					}
					else if (linea.contains("PUERTOL")){
						int i = linea.indexOf(":") +1;
						config.setPuertol(Integer.parseInt(linea.substring(i)));
						//System.out.println(config.getPuertol());
					}
					else if (linea.contains("DBL")){
						int i = linea.indexOf(":") +1;
						config.setDbl((linea.substring(i)));
						//System.out.println(config.getDbl());
					}
					else if (linea.contains("USUARIOL")){
						int i = linea.indexOf(":") +1;
						config.setUsuariol((linea.substring(i)));
						//System.out.println(config.getUsuariol());
					}
					else if (linea.contains("PASSL")){
						int i = linea.indexOf(":") +1;
						config.setPassl((linea.substring(i)));
						//System.out.println(config.getPassl());
					}
					else if (linea.contains("TIEMPO")){
						int i = linea.indexOf(":") +1 ;
						config.setTiempo(Integer.parseInt(linea.substring(i)));
						//System.out.println(config.getTiempo());
					}		
					
				}
				
			} catch (IOException e) {
				// 
				e.printStackTrace();
				System.out.println("No se ha podido leer la linea ! \n Se sale !" ); 

			}
			try {
				fr.close();
				bf.close();
			} catch (IOException e) {
				// 
				e.printStackTrace();
				System.out.println("No se ha podido cerrar la lectura de archivo !!" ); 
			}
		}
		
		
	
}
