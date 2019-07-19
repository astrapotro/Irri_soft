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



import javax.swing.JScrollPane;


import panelespack.PanelBt2;
import panelespack.Panelconf;
import panelespack.Panelmci;
import programapack.ListaProgsaexec;
import tareapack.ListaTareasaexec;
import valvulaspack.ListaValvBt2;
import valvulaspack.ListaValvBt22;
import valvulaspack.ListaValvMci;


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
	
	
	//Hilo de escucha a la BBDD local
	public HiloEscucha hiloescucha = new HiloEscucha();
	protected Thread hiloesc;
	//Semaforos de los hilos
	//Escritura abrir prog
	public Semaforo semaforo1 = new Semaforo(1);
	//Lectura cerrar prog
	public Semaforo semaforo2 = new Semaforo(1);
	//Para leer el consumo
	public Semaforo semaforo3 = new Semaforo(1);

	//Lista tareas
	public static ListaTareasaexec tareas = ListaTareasaexec.getInstance();
	//Variable para guardar el id de la ultima tarea ejecutada o en ejecucion
	protected int idtareaexec = -1;
	
	//Lista programas
	public static ListaProgsaexec progs = ListaProgsaexec.getInstance();

	
	//Listavalvulasmci
	public ListaValvMci valvsmci = ListaValvMci.getInstance();
	
	//ListavalvulasBT2
	public ListaValvBt2 valvsbt2 = ListaValvBt2.getInstance();
	public ListaValvBt22 valvsbt22 = ListaValvBt22.getInstance();
	

	//Estado
	protected final JLabel lblstatus = new JLabel();
	
	//Area de texto log
	public JTextArea textArea;
	
	//Configuración con la RUTA al archivo !!!
	public Conf config = new Conf();
	public static final String rutaconf = "/home/mikel/conf_irrisoft.txt"; 
		
	
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
					
					//Construyo el panelmci
					window.panelmci = Panelmci.getInstance();
					
					//Construyo el panelbt2
					window.panelbt2 = PanelBt2.getInstance();
					
					//Cargo la lista de valvulas !
					window.panelmci.inicializavalvs();
					window.panelbt2.inicializavalvs();
					
					
					//Abro otro hilo con la escucha a la BBDD local
					window.llamaescucha();

					
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
				
		//Leo la configuración
				leerconf(config);

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
		
		frmIrrisoft.getContentPane().add(textArea);
		textArea.setBackground(UIManager.getColor("CheckBox.background"));
		textArea.setEditable(false);
		
		JScrollPane scroll = new JScrollPane(textArea);
		//scroll.setAutoscrolls(true);
		textArea.scrollRectToVisible(scroll.getViewportBorderBounds());
		scroll.setViewportBorder(null);
		scroll.setBounds(491, 82, 339, 296);
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
		lblEstado.setBounds(89, 58, 67, 15);
		panelpral.add(lblEstado);
		
		lblstatus.setBounds(172, 58, 188, 15);
		panelpral.add(lblstatus);
		lblstatus.setText("     Desconectado");
		
		btnEmpezar = new JButton("Empezar");
		btnEmpezar.setBounds(89, 12, 271, 34);
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
		imginfo.setBounds(386, 216, 67, 63);
		ruta ="/irrisoftpack/imagenes/info.png";
		redimensionado(imginfo,ruta);
		panelpral.add(imginfo);					

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
				e.printStackTrace();
				System.out
						.println("No existe el fichero de configuración !! \n Se sale !");

			}
			BufferedReader bf = new BufferedReader(fr);

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
					}else if (linea.contains("BT2")){
						int i = linea.indexOf(":") + 1;
						config.setBt2((linea.substring(i)));
					}else if (linea.contains("bt2dos")){
						int i = linea.indexOf(":") + 1;
						config.setBt22((linea.substring(i)));
						//System.out.println(config.getBt22());
					}

				}

			} catch (IOException e) {
				// 
				e.printStackTrace();
				System.out.println("No se ha podido leer la linea ! \n Se sale !");

			}
			try {
				fr.close();
				bf.close();
			} catch (IOException e) {
				// 
				e.printStackTrace();
				System.out
						.println("No se ha podido cerrar la lectura de archivo !!");
				
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
