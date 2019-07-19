package irrisoftpack;


import java.awt.EventQueue;import java.awt.Toolkit;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JScrollPane;
import javax.swing.text.DefaultCaret;

import panelespack.PanelBt2;
import panelespack.Panelconf;
import panelespack.Panelmci;
import panelespack.Paneltest;
import programapack.ListaProgsaexec;
import sensorespack.HiloCaudalimetro;
import sensorespack.HiloHumedadSueloBt2;
import sensorespack.Hilosensores;
import sensorespack.Hilosenstemp;
import tareapack.ListaTareasaexec;
import valvulaspack.ListaValvBt2;
import valvulaspack.ListaValvBt22;
import valvulaspack.ListaValvMci;
import valvulaspack.ListaValvMci2;
import valvulaspack.Valvula;
import volcadopack.Volcado;


public class Irrisoft extends Thread{

	//Instancia principal
	public static Irrisoft window;
	//Ventana principal
	public JFrame frmIrrisoft;
	protected JButton btnEmpezar;
	//Panel Principal 
	public JPanel panelpral = new JPanel();
	//Panel MCI
	public  Panelmci panelmci;
	//Panel Bt2
	public  PanelBt2 panelbt2;
	//Panel Conf
	public  Panelconf panelconf;
	//Panel Test
	public Paneltest paneltest;
	//Area de texto log
	public JTextArea textArea;
	//Estado
	protected final JLabel lblstatus = new JLabel();
	
	
	private ConexionDB conn = new ConexionDB();
	
	//Hilo de escucha a la BBDD local
	public HiloEscucha hiloescucha = new HiloEscucha();
	protected Thread hiloesc;
	
	//Hilo Volcado
	public Volcado volcado;
	protected Thread hilovolcado;
	
	
	//
	/// SENSORES ////////////////
	//Hilo de Escucha caudalimetro BT2
	public HiloCaudalimetro hilocaudal;
	protected Thread hilocaudalbt2;
	//Hilo de Escucha caudalimetro BT2
	public HiloHumedadSueloBt2 hilohumedadsuelo;
	protected Thread hilohumedadsuelobt2;
	//Hilo de escritura/lectura placa sensores gontzi
	public static Hilosensores hilosens =  Hilosensores.getInstance(); 
	protected Thread hilosensgon;
	//Hilo sensor temp
	public  Hilosenstemp hilotemp;
	protected Thread hilotemperatura;
	
	//BORNAS sensores
	public static final byte bornapluvio=1;
	public static final byte bornacaudal=2;
	public static final byte bornaanemo=3;
	public static final byte bornahumedad=15;
	public static final byte bornaamp=16;
	public static final byte bornatemp=17;
	

	//
	//// SEMAFOROS de los hilos
	public Semaforo semaforobt2 = new Semaforo(1);
	public Semaforo semaforobt22 = new Semaforo(1);
	public Semaforo semaforomci = new Semaforo(1);
	public Semaforo semaforomci2 = new Semaforo(1);
	public Semaforo semaforosens = new Semaforo(1);
	
	//
	///// LISTAS
	public static ListaTareasaexec tareas = ListaTareasaexec.getInstance();
	//Variable para guardar el id de la ultima tarea ejecutada o en ejecucion
	protected  int idtareaexec = -1;
	//Lista programas
	public ListaProgsaexec progs = ListaProgsaexec.getInstance();
	//Listavalvulasmci
	public ListaValvMci valvsmci = ListaValvMci.getInstance();
	public ListaValvMci2 valvsmci2 = ListaValvMci2.getInstance();
	public ArrayList <String> mcicombo = new ArrayList <String>();
	public final static int NUMVALVSMCI = 3;
	//ListavalvulasBT2
	public ListaValvBt2 valvsbt2 = ListaValvBt2.getInstance();
	public ListaValvBt22 valvsbt22 = ListaValvBt22.getInstance();
	public ArrayList<String> bt2combo = new ArrayList<String>();
	public final static int NUMVALVSBT2 = 3;
	//Lista Abiertas
	public LinkedList<Valvula> valvsabiertas = new LinkedList<Valvula>();
	
	
	//Constante caudalimetro (me vendrá en la configuración inicial !!
	public int K=1;
	
	//Configuración con la RUTA al archivo !!!
	public static Conf config = new Conf();
	public static final String rutaconf = "/home/mikel/conf_irrisoft.txt"; 
	public static final String comandoapaga = "/home/pi/apaga.sh";
	public static final String puertosbt = "/home/mikel/puertosbt.txt";
			
	//Ruta imagenes
	public String rutaoff = "/irrisoftpack/imagenes/multioff.png";
	public String rutaon = "/irrisoftpack/imagenes/multion.png";
	
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					window = new Irrisoft();
					window.frmIrrisoft.setVisible(true);
					
					//Leo los puertos de la bt2
					leerpuertosbt();
					//Leo la configuración local 
					leerconf(config);
					
					
					//TODO leer la configuración hard del programador desde el GIS
					//leeerconfhardGIS();
					window.ponerconfhardGIS();
					
					//Construyo el panelmci
					window.panelmci = Panelmci.getInstance();
					
					//Construyo el panelbt2
					window.panelbt2 = PanelBt2.getInstance();
					
					//Cargo la lista de valvulas !
					window.panelmci.pintavalvsmci();
					
					
					
					//Abro otro hilo con la escucha a la BBDD local
					window.llamaescucha();
					
					//Abro VOLCADO
					window.volcado = new Volcado();
					window.hilovolcado = new Thread (window.volcado);
					window.hilovolcado.start();
						
					//Delay de la respuesta de la BT2 
//					window.hiloescucha.connDB.serial.conectaserial(Irrisoft.config.getBt2(), 3);
//					window.hiloescucha.connDB.serial.ponedelayresp();
//					window.hiloescucha.connDB.serial.leeponedelayresp();
//					//Thread.sleep(150);
//					window.hiloescucha.connDB.serial.preguntadelayresp();
//					window.hiloescucha.connDB.serial.leerespdelay();
//					window.hiloescucha.connDB.serial.desconectaserial();
					
					
					
					///////////////////////////////////////////////////
					//	SENSORES
					///////////////////////////////////////////////////
					
					//Aki voy a llamar al hilo de lectura del caudal de la BT2
					//y habría que añadir la segunda BT2 !!!!!
					window.hilocaudal = new HiloCaudalimetro(config.getBt2(),3,0);
					window.hilocaudalbt2 = new Thread (window.hilocaudal);
					window.hilocaudalbt2.start();  
//					
//					//Aki voy a llamar al hilo de humedad del suelo de la BT2
//					//y habría que añadir la segunda BT2 !!!!!
//					window.hilohumedadsuelo = new HiloHumedadSueloBt2(config.getBt2(),3);
//					window.hilohumedadsuelobt2 = new Thread (window.hilohumedadsuelo);
//					window.hilohumedadsuelobt2.start(); 	
					
					//Aki voy a llamar al hilo de sensores (placa gontzi)
//					Hilosensores.setPuerto("/dev/pts/4");
//					window.hilosensgon = new Thread (hilosens);
//					window.hilosensgon.start(); 
//					
//					//Aki voy a llamar al hilo del sensor temperatura
//					window.hilotemp = new Hilosenstemp();
//					window.hilotemperatura = new Thread (window.hilotemp);
//					window.hilotemperatura.start(); 
				
				
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}

	/**
	 * Create the application.
	 */
	public Irrisoft() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		
	
		
		frmIrrisoft = new JFrame();
		frmIrrisoft.setIconImage(Toolkit.getDefaultToolkit().getImage(Irrisoft.class.getResource("/irrisoftpack/imagenes/gotita.png")));
		frmIrrisoft.setTitle("I R R I S O F T");
		frmIrrisoft.setResizable(false);
		frmIrrisoft.setBounds(100, 100, 830, 400);
		frmIrrisoft.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmIrrisoft.getContentPane().setLayout(null);
		
		//Pinto la cabecera
				cabecera();
				
		//Pinto los botoncicos de selección
				principal();
		
	}
	
	
	//
	//
	////////////////////////////////// CABECERA con LOGOS
	private void cabecera(){
		JLabel imgraspi = new JLabel("");
		imgraspi.setBounds(0, 0, 86, 77);
		String ruta ="/irrisoftpack/imagenes/Raspberry-Pi-logo.png";
		redimensionado(imgraspi,ruta);
		frmIrrisoft.getContentPane().add(imgraspi);
		
		JLabel imgirrigestlife = new JLabel("");
		imgirrigestlife.setBounds(481, 0, 349, 77);
		ruta ="/irrisoftpack/imagenes/irrigestlife.png";
		redimensionado(imgirrigestlife,ruta);
		frmIrrisoft.getContentPane().add(imgirrigestlife);
		
		
		JLabel imggestdropper = new JLabel("");
		imggestdropper.setBounds(242, 0, 168, 77);
		ruta ="/irrisoftpack/imagenes/gestdropper.png";
		redimensionado(imggestdropper,ruta);
		frmIrrisoft.getContentPane().add(imggestdropper);
		
		JLabel imggotita = new JLabel("");
		imggotita.setBounds(163, 0, 67, 77);
		ruta ="/irrisoftpack/imagenes/gotita.png";
		redimensionado(imggotita,ruta);
		frmIrrisoft.getContentPane().add(imggotita);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 77, 830, 2);
		frmIrrisoft.getContentPane().add(separator);
		
		
		textArea = new JTextArea();
		textArea.setBounds(491, 82, 309, 296);
		//Para que haga scrolling automaticamente !!
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		frmIrrisoft.getContentPane().add(textArea);
		textArea.setBackground(UIManager.getColor("CheckBox.background"));
		textArea.setEditable(false);
		
		JScrollPane scroll = new JScrollPane(textArea);
	
		//textArea.scrollRectToVisible(scroll.getViewportBorderBounds());
		scroll.setViewportBorder(null);
		scroll.setBounds(488, 85, 338, 286);
		frmIrrisoft.getContentPane().add(scroll);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setBorder(null);
	
		
		
		JSeparator separator_vert = new JSeparator();
		separator_vert.setBounds(481, 77, 2, 301);
		frmIrrisoft.getContentPane().add(separator_vert);
		separator_vert.setOrientation(SwingConstants.VERTICAL);
		
	}

	//
	//
	/////////////////////////////////  Panel principal con botones de elección mci/bt2/conf/info
	private void principal(){
			
		
		panelpral.setBounds(10, 84, 465, 291);
		frmIrrisoft.getContentPane().add(panelpral);
		panelpral.setLayout(null);
		
	
		
		JLabel lblEstado = new JLabel("Estado:");
		lblEstado.setBounds(28, 58, 67, 15);
		panelpral.add(lblEstado);
		
		lblstatus.setBounds(107, 58, 140, 15);
		panelpral.add(lblstatus);
		lblstatus.setText("     Desconectado");
		
		btnEmpezar = new JButton("Empezar");
		btnEmpezar.setBounds(28, 12, 214, 34);
		panelpral.add(btnEmpezar);
		
		//BOTON EMPEZAR
		btnEmpezar.addActionListener(new ActionListener() {
			

			//Empieza o para la escucha de nuevas tareas/programaciones
			
			public void actionPerformed(ActionEvent arg0) {
				
				if (btnEmpezar.getText().contains("Empezar")){
						hiloescucha.setTerminar(false);
						hiloesc = new Thread (hiloescucha);
						hiloesc.start();  // Aquí empieza a ejecutarse el hilo	
						
				}
				else {
					hiloescucha.cierraDBhilo();
					hiloescucha.setTerminar(true);
					//Invoco al garbage collector para liberar memoria !!
					Runtime r = Runtime.getRuntime();
					r.gc();

				}	
			}
		});
		
		// Placa mci
		JLabel imgmci = new JLabel("");
		imgmci.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//Aki se llama al panelmci
				panelpral.setVisible(false);
				panelmci.repaint();
				frmIrrisoft.getContentPane().add(panelmci);
				panelmci.setVisible(true);
			}
		});
		
		imgmci.setBounds(0, 216, 123, 63);
		String ruta ="/irrisoftpack/imagenes/placamci.png";
		redimensionado(imgmci,ruta);
		panelpral.add(imgmci);
		
		
		// Placa Bt2
		JLabel imgbt2 = new JLabel("");
		imgbt2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//Aki se llama al panelbt2
				panelpral.setVisible(false);
				panelbt2.repaint();
				frmIrrisoft.getContentPane().add(panelbt2);
				panelbt2.setVisible(true);
					
			}
		});
		
		imgbt2.setBounds(144, 216, 123, 63);
		ruta ="/irrisoftpack/imagenes/placabt2.png";
		redimensionado(imgbt2,ruta);
		panelpral.add(imgbt2);
	
		
		//Configuración
		JLabel imgconf = new JLabel("");
		imgconf.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//Aki se llama al panel de configuración
				panelconf = Panelconf.getInstance();
				panelpral.setVisible(false);
				panelconf.repaint();
				frmIrrisoft.getContentPane().add(panelconf);
			}
		});
		imgconf.setBounds(279, 216, 88, 63);
		ruta ="/irrisoftpack/imagenes/engranajes.png";
		redimensionado(imgconf,ruta);
		panelpral.add(imgconf);
		
		//Acerca de
		JLabel imginfo = new JLabel("");
		imginfo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//Info
				JOptionPane.showMessageDialog(Irrisoft.window.frmIrrisoft, "Irrisoft version 0.3.35\nPrograma que gestiona el riego automático integrado en un GIS.\nAutor: Mikel Merino <astrapotro@gmail.com>\nTodos los derechos reservados 2013 \n\n", "Acerca de ...", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		imginfo.setBounds(386, 216, 67, 63);
		ruta ="/irrisoftpack/imagenes/info.png";
		redimensionado(imginfo,ruta);
		panelpral.add(imginfo);
		
		JButton Testbutton = new JButton("Test");
		Testbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Aki se llama al panel de TEST
				paneltest = Paneltest.getInstance();
				panelpral.setVisible(false);
				paneltest.repaint();
				frmIrrisoft.getContentPane().add(paneltest);
			}
		});
		Testbutton.setBounds(306, 12, 112, 34);
		panelpral.add(Testbutton);

	}
	
	
	//
	//
	///////////////////////////////// Redimensionado imagenes
	public void redimensionado (JLabel jlabel, String ruta){
		ImageIcon img = new ImageIcon(Irrisoft.class.getResource(ruta));
		Icon icono = new ImageIcon(img.getImage().getScaledInstance(jlabel.getWidth(), jlabel.getHeight(), Image.SCALE_SMOOTH));
		jlabel.setIcon(icono);
	}
	
	
	// ////////////////////////////////////////////////////////////////////////////
	//
	//  Lee archivo de configuración conf_irrisoft.txt !!!
	public static void leerconf(Conf config) {

			FileReader fr = null;
			String linea = null;

			try {
				fr = new FileReader(rutaconf); // Ruta al archivo de configuración

			} catch (FileNotFoundException e) {
				//e.printStackTrace();
				Irrisoft.window.textArea.append("No existe el fichero de configuración:\n"+rutaconf);
				System.out.println("No existe el fichero de configuración:\n"+rutaconf);
			}
			BufferedReader bf=null;
			try {
				bf = new BufferedReader(fr);
			} catch (Exception e1) {
				// 
				//e1.printStackTrace();
			}

			//
			// Leo línea línea el fichero de configuración y lo guardo todo en la
			// clase conf
			//
			try {
				while ((linea = bf.readLine()) != null) {

					if (linea.contains("ID")) {
						int i = linea.indexOf(":") + 1;
						config.setIdrasp(Integer.parseInt(linea.substring(i)));
						// System.out.println(config.getIdrasp());
					} else if (linea.contains("HOST")) {
						int i = linea.indexOf(":") + 1;
						config.setHost((linea.substring(i)));
						 //System.out.println(config.getHost());
					} else if (linea.contains("PUERTO")) {
						int i = linea.indexOf(":") + 1;
						config.setPuerto(Integer.parseInt(linea.substring(i)));
						 //System.out.println(config.getPuerto());
					} else if (linea.contains("DB")) {
						int i = linea.indexOf(":") + 1;
						config.setDb((linea.substring(i)));
						// System.out.println(config.getDbl());
					} else if (linea.contains("USUARIO")) {
						int i = linea.indexOf(":") + 1;
						config.setUsuario((linea.substring(i)));
						// System.out.println(config.getUsuariol());
					} else if (linea.contains("PASS")) {
						int i = linea.indexOf(":") + 1;
						config.setPass((linea.substring(i)));
					} else if (linea.contains("MCI")){
						int i = linea.indexOf(":") + 1;
						config.setMci((linea.substring(i)));
					}else if (linea.contains("mcidos")){
						int i = linea.indexOf(":") + 1;
						config.setMci2((linea.substring(i)));
					}else if (linea.contains("limite")){
						int i = linea.indexOf(":") + 1;
						config.setLimitebt(Integer.parseInt((linea.substring(i))));
						//System.out.println(config.getBt22());
					}
					
//					else if (linea.contains("BT2")){
//						int i = linea.indexOf(":") + 1;
//						config.setBt2((linea.substring(i)));
//					}else if (linea.contains("bt2dos")){
//						int i = linea.indexOf(":") + 1;
//						config.setBt22((linea.substring(i)));
//						//System.out.println(config.getBt22());
//						
				}

			} catch (Exception e) {
				// 
				//e.printStackTrace();
				System.out.println("No se ha podido leer la linea ! \n Se sale !");

			}
			try {
				fr.close();
				bf.close();
			} catch (Exception e) {
				// 
				//e.printStackTrace();
				System.out
						.println("No se ha podido cerrar la lectura de archivo !!");
				
			}
		}
	

	
	public static void leerpuertosbt (){
		FileReader fr = null;
		String linea = null;
		int i=0;
		String c=null,d=null,aux=null,aux1=null;

		try {
			fr = new FileReader(puertosbt); // Ruta al archivo de configuración

		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			Irrisoft.window.textArea.append("No existe el fichero de los puertos bt2 :\n"+puertosbt);
			System.out.println("No existe el fichero de los puertos bt2:\n"+puertosbt);
		}
		BufferedReader bf=null;
		try {
			bf = new BufferedReader(fr);
		} catch (Exception e1) {
			// 
			//e1.printStackTrace();
		}

		//
		// Leo línea línea el fichero de puertos 
		//
		
			try {
				
				while ((linea = bf.readLine()) != null) {
					
					//El script puertosbt.sh me pone un espacio al final !! yo se lo quito
					linea=linea.substring(0, 5);
					
					//Ordeno de menor a mayor
					if (i==0){
						c=linea.substring(4,5);
						aux=linea;
					}
					if (i==1){
						d=linea.substring(4,5);	
						aux1=linea;
					}
					i++;
				}
				
				if (i==2){
					if (Integer.parseInt(c)<Integer.parseInt(d)){
						config.setBt2("/dev/"+aux);
							config.setBt22("/dev/"+aux1);
					}else
					{
						config.setBt2("/dev/"+aux1);
							config.setBt22("/dev/"+aux);
					}
				}else if (i==1){
					config.setBt2("/dev/"+aux);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			

	}
	//
	//
	///////////////////////////////// Abre otro hilo con la escucha a la BBDD
	protected void llamaescucha(){
		//Abro conexión y me pongo a escuchar automáticamente a la local en otro hilo dedicado exclusivamente a ello
		hiloesc = new Thread (hiloescucha);
		hiloesc.start();  // Aquí empieza a ejecutarse el hilo	
	}
	
	
	//
	//
	///////////////////////////////////////// Aki recojo la configuración inicial del GIS y creo objetos !!!
	private void ponerconfhardGIS() {
		// Aki tengo que poner la configuracion previa que he leido del GIS
		
		//Inicializo las listas de válvulas
		//Inicializo las BT2
		for (int i=0; i<NUMVALVSBT2; i++){
			valvsbt2.addvalvbt2(new Valvula());
			valvsbt2.getvalvbt2(i).setAbierta(false);
			valvsbt2.getvalvbt2(i).setCodelecvalv(Integer.toString(1001+i));
			conn.recogeconsummod(i, Integer.toString(1001+i));
			
//			System.out.println("VALVULA: "+valvsbt2.getvalvbt2(i).getCodelecvalv()+
//					" , "+valvsbt2.getvalvbt2(i).getCaudalmod()+
//					" , "+valvsbt2.getvalvbt2(i).getIntensidadmod());
			
			bt2combo.add(valvsbt2.getvalvbt2(i).getCodelecvalv());
			
		}
		for (int i=0; i<NUMVALVSBT2; i++){
			valvsbt22.addvalvbt2(new Valvula());
			valvsbt22.getvalvbt2(i).setAbierta(false);
			valvsbt22.getvalvbt2(i).setCodelecvalv(Integer.toString(2001+i));	
			//Falta conn.recogeconsum
			bt2combo.add(valvsbt22.getvalvbt2(i).getCodelecvalv());
		}
		
		//Inicializo las MCI
		for (int i=0;i<NUMVALVSMCI;i++){
			valvsmci.addvalvmci(new Valvula());
			valvsmci.getvalvmci(i).setAbierta(false);
			valvsmci.getvalvmci(i).setCodelecvalv(Integer.toString(1+i));
			conn.recogeconsummod(i, Integer.toString(1+i));
			mcicombo.add(valvsmci.getvalvmci(i).getCodelecvalv());
//			System.out.println("VALVULA: "+valvsmci.getvalvmci(i).getCodelecvalv()+
//					" , "+valvsmci.getvalvmci(i).getCaudalmod()+
//					" , "+valvsmci.getvalvmci(i).getIntensidadmod());
			
		}
		for (int i=0;i<NUMVALVSMCI;i++){
			valvsmci2.addvalvmci(new Valvula());
			valvsmci2.getvalvmci(i).setAbierta(false);
			valvsmci2.getvalvmci(i).setCodelecvalv(Integer.toString(27+i));
			//Falta conn.recogeconsum
			mcicombo.add(valvsmci2.getvalvmci(i).getCodelecvalv());
		}
	
		//Cierro el resulset
		try {
			conn.resultados.close();
			conn.sentenciapre.close();
		} catch (SQLException e) {
			//
			e.printStackTrace();
		}
		
		//TODO Inicializar sensores y demases
		//Si tengo alguna LATCH tengo que bajar la sensibilidad de la BT2 a 30mA (comando 10H),
		// y cambiar el tipo de decoder a 2 Aquative plus o a un supuesto 3 (comando 13H)
		
	}

	
	
	//
	//
	//////////////////////////////////////// Recorro las listas de valvulas para saber cuales están abiertas en la invocación !!
	public int valvsabiertas(){
		
		int abiertas=0;
		valvsabiertas.clear();
			
			for (int i=0;i<valvsmci.getsizeof();i++){
				if (valvsmci.getvalvmci(i).isAbierta()){
					valvsabiertas.add(valvsmci.getvalvmci(i));
					abiertas++;
				}
				
			}
			
			for (int i=0;i<valvsmci2.getsizeof();i++){
				
				if (valvsmci2.getvalvmci(i).isAbierta()){
					valvsabiertas.add(valvsmci2.getvalvmci(i));
					abiertas++;
				}
				
			}
			
			for (int i=0;i<valvsbt2.getsizeof();i++){
				
				if (valvsbt2.getvalvbt2(i).isAbierta()){
					valvsabiertas.add(valvsbt2.getvalvbt2(i));
					abiertas++;
				}
				
				
			}
			
			for (int i=0;i<valvsbt22.getsizeof();i++){
				
				if (valvsbt22.getvalvbt2(i).isAbierta()){
					valvsabiertas.add(valvsbt22.getvalvbt2(i));
					abiertas++;
				}
			}
		
		
		return abiertas;
		
	}
		
		
	public void quitarvalvabiertas (String numvalv ){
		
		for (int i=0; i<valvsabiertas.size();i++){
			
			if (valvsabiertas.get(i).getCodelecvalv().contentEquals(numvalv))
				valvsabiertas.remove(i);
				
		}
	}
	
	
	//
	//
	/////////////////////////////////////// 
	//Para que actue el CAUDALIMETROOO
	public void caudalregando(boolean regando){
		
		//TODO Marcar que el caudalimetro está acoplado a una BT2. habrá que poner diferecncias según la placa !!
		Irrisoft.window.hilocaudal.setMci(false);
		
		
		if (regando){
			Irrisoft.window.hilocaudal.setRegando(true);
			Irrisoft.window.hilocaudal.setTest(false);
			Irrisoft.window.hilocaudal.setTerminar(true);
			
		}else if (!regando){
			Irrisoft.window.hilocaudal.setRegando(false);
			Irrisoft.window.hilocaudal.setTest(false);
			Irrisoft.window.hilocaudal.setTerminar(false);
		}
		
	}
	
	
	
	//
	//
	///////////////////////////////////////////////// Para pasar a hexadecimal !!
	synchronized public String getHex(byte[] bytes, boolean escribo) {
    StringBuilder result = new StringBuilder();
    int i=0;
    for (byte b : bytes) {
    	result.append(0);
    	result.append(Integer.toHexString(b));
    
    if (escribo){
    	if (i<bytes.length-1)
    		result.append(".");
    	
    	i++;            						
    }  
    }
    return result.toString();
}



}
