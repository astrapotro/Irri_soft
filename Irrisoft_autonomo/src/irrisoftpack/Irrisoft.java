package irrisoftpack;

//import gnu.io.NoSuchPortException;

import graficaspack.grafico_cau;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

import jssc.SerialPort;
import jssc.SerialPortException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.util.text.BasicTextEncryptor;

import panelespack.PanelBt2;
import panelespack.PanelSamcla;
import panelespack.Panelconf;
import panelespack.Panelecturas;
import panelespack.Panelecturasbt2;
import panelespack.Panelecturasens;
import panelespack.Panelecturasmci;
import panelespack.Panelmci;
import panelespack.Paneltest;
import programapack.ListaProgsaexec;
import sensorespack.HiloAmperimetro;
import sensorespack.HiloAnemometro;
import sensorespack.HiloCaudalimetro;
import sensorespack.HiloHumedadSuelo;
import sensorespack.HiloPluviometro;
import sensorespack.HiloTemperatura;
import sensorespack.ListaSensores;
import sensorespack.Sensor;
import valvulaspack.ListaPlacasGhost;
import valvulaspack.ListaValvBt2;
import valvulaspack.ListaValvMci;
import valvulaspack.ListaValvSamcla;
import valvulaspack.Valvula;
import volcadopack.Volcado;
//import sensorespack.HiloAmperimetroGhost;

public class Irrisoft extends Thread {

    private static Logger logger = LogManager.getLogger(Irrisoft.class
	    .getName());

    // Instancia principal
    public static Irrisoft window;
    // Hilo principal
    public Thread principoide;

    // Ventana principal
    public JFrame frmIrrisoft;
    public JButton btnEmpezar;
    // Panel Principal
    public JPanel panelpral = new JPanel();
    // Panel MCI
    public Panelmci panelmci;
    // Panel Bt2
    public PanelBt2 panelbt2;
    // Panel Samcla
    public PanelSamcla panelsamcla;
    // Panel Conf
    public Panelconf panelconf;
    // Panel Test
    public Paneltest paneltest;
    // Paneles Lecturas sensores
    public Panelecturas panelecturas;
    public Panelecturasmci panelecturasmci;
    public Panelecturasbt2 panelecturasbt2;
    public Panelecturasens panelecturasens;

    // Gráfica
    public grafico_cau vent_graf_cau = new grafico_cau();

    // Area de texto log
    // public JTextArea textArea;
    public JTextPane textPane;
    public StyledDocument doc;
    public SimpleAttributeSet negrita, italic, normal;

    // Estado conexiones
    public final JLabel lblstatusl = new JLabel();
    public final JLabel lblstatusr = new JLabel();

    // Estado maestras
    public JLabel lblmaestra1;
    public JLabel lblmaestra2;
    // Color verde para ON

    // Botón Test
    private JButton Testbutton;

    // Botones de placas
    private JLabel imgmci, imgbt2, imgsamcla;

    // /////////////////////////
    // BBDD
    private ConexionDB conn;

    // Hilo de escucha a la BBDD local
    public HiloEscucha hiloescucha;

    protected Thread hiloesc;

    // Hilo Volcado
    public Volcado volcado = new Volcado();
    protected Thread hilovolcado;

    // IP
    public String ip;

    // //////////////
    // VARIABLES AUXILIARES
    // Consumo test
    // public int consumo;
    // Esta el test en ejecucion ?
    public boolean test = false;
    // Es el test inicial o particular?
    public boolean inicial = false;

    // Existe caudalimetro?
    public boolean haycaudalimetro = false;
    // Por ahora sólo va a haber un caudalimetro, TODO HABRÁ que cambiarlo para
    // que se puedan poner más !!!
    public HiloCaudalimetro hilocau;
    // Existe amperimetro?
    public boolean hayamperimetro = false;
    // Existe amp en la placa de sensores???
    // public boolean hayampplacasensores = false;
    // Existe la placa de sensores
    public boolean hayplacasens;

    // Inicializados los puertos
    public boolean iniports = false;

    // Para las alarmas
    // public GestorAlarmas gestorAlarma = new GestorAlarmas();

    // //////////////
    // Puertos Serie

    protected SerialDriver serie1, serie2, serie3, serie4, serie5, serie6,
	    serie7, serie8, seriesens, seriesamcla;

    public ArrayList<SerialDriver> series = new ArrayList<SerialDriver>();

    // public ArrayList<String> puertos = new ArrayList<String>();
    // public ArrayList<String> devices = new ArrayList<String>();

    // ///////////////////
    // // SEMAFOROS para los puertos de las diferentes placas
    public Semaforo semaforobt2, semaforobt22, semaforobt23, semaforobt24;
    public Semaforo semaforomci, semaforomci2, semaforomci3, semaforomci4;
    public Semaforo semaforosamcla;
    public Semaforo semaforosens;

    // /////////////////
    // /// LISTAS
    // public static ListaTareasaexec tareas = ListaTareasaexec.getInstance();
    // Variable para guardar el id de la ultima tarea ejecutada o en ejecucion
    protected int idtareaexec = -1;
    // Lista programas
    public ListaProgsaexec progs = ListaProgsaexec.getInstance();
    // Listavalvulasmci
    public ListaValvMci valvsmci, valvsmci2, valvsmci3, valvsmci4;
    public ArrayList<String> mcicombo = new ArrayList<String>();
    // ListavalvulasBT2
    public ListaValvBt2 valvsbt2, valvsbt22, valvsbt23, valvsbt24;
    public ArrayList<String> bt2combo = new ArrayList<String>();
    // ListavalvulasSamcla
    public ListaValvSamcla valvsamcla;
    public ArrayList<String> samclacombo = new ArrayList<String>();

    // ListaValvulasMaestras
    public ArrayList<Valvula> valvsmaestras;

    public LinkedHashSet<Valvula> valvsabiertasmci, valvsabiertasmci2,
	    valvsabiertasmci3, valvsabiertasmci4, valvsabiertasbt2,
	    valvsabiertasbt22, valvsabiertasbt23, valvsabiertasbt24,
	    valvsabiertasamcla, valvsabiertastot;

    // Lista de sensores conectados
    public LinkedList<Sensor> sensores = ListaSensores.getInstance().getsens();

    // Lista de placas mci para amperimetros fantasma (QUITAR cuando gon
    // fabrique sus placas controladoras y de relés)
    public ListaPlacasGhost placas;

    // ///////////
    // Valvulas MAESTRAS
    public Valvula maestra, maestra1;
    public int maestras = 0;

    // Configuración con RUTAS !!!
    public static String home = null;
    public static Conf config = new Conf();
    public static final String comandoapaga = home + "/apaga.sh";
    public static final String puertosbt = home + "/puertosbt.txt";

    // Dia
    public String hoyes;
    public Date hoy;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    // Fecha actual
    public JLabel lblfecha;

    // Flag de rearme de irrisoft
    public boolean rearmar = false;

    
    // Flags para saber si hay sensores en cada placa
    public boolean sensbt2, sensbt22, sensbt23, sensbt24, sensmci, sensmci2,
	    sensmci3, sensmci4;
    
    //Símbolo copyright (buuuuuuuh !!)
    String copyright = "\u00A9"; 

    // ////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	// executor = Executors.newFixedThreadPool(20);
	EventQueue.invokeLater(new Runnable() {

	    public void run() {

		// try{
		window = new Irrisoft();

		// Almaceno que día es hoy
		window.hoyes = new Timestamp(Calendar.getInstance().getTime()
			.getTime()).toString();
		window.hoyes = window.hoyes.substring(0, 10);
		try {
		    window.hoy = window.formatter.parse(window.hoyes);
		} catch (ParseException e) {
		    if (logger.isErrorEnabled()) {
			logger.error(e.getMessage());
		    }
		}

		window.frmIrrisoft.setVisible(true);

		// Leo los puertos de la bt2
		window.leerpuertosbt();

		// Leo la configuración local archivo conf_irrisoft.txt
		// (puertos,usuarios, host)
		window.leerconfirri(config);
		// window.leerpuertosbt();
		// Abro hilo con la escucha a la BBDD local
		window.conn = new ConexionDB();
		window.hiloescucha = new HiloEscucha();
		window.llamaescucha();

		// Abro VOLCADO
		window.volcado = new Volcado();
		window.hilovolcado = new Thread(window.volcado);
		window.hilovolcado.setName("Volcado "
			+ window.hilovolcado.getId());
		window.hilovolcado.start();
		// executor.execute(window.hilovolcado);

		// Asocio el devicenum con los puertos A QUITAR!
		// window.devicenumACM();

		// Leo la configuración inicial de placas,maestras,asigno
		// los puertos serie,sensores...
		window.leerconfini();

		// Delay de la respuesta de la BT2
		// window.hiloescucha.connDB.serial.conectaserial(Irrisoft.config.getBt2(),
		// 3);
		// window.hiloescucha.connDB.serial.ponedelayresp();
		// window.hiloescucha.connDB.serial.leeponedelayresp();
		// //Thread.sleep(150);
		// window.hiloescucha.connDB.serial.preguntadelayresp();
		// window.hiloescucha.connDB.serial.leerespdelay();
		// window.hiloescucha.connDB.serial.desconectaserial();

		// }catch(Exception e){
		// if(logger.isErrorEnabled()){
		// logger.error(e.getMessage());
		// }
		// }
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
	frmIrrisoft.setIconImage(Toolkit.getDefaultToolkit().getImage(
		Irrisoft.class.getResource(IrrisoftConstantes.IMG_GOTITA)));
	frmIrrisoft.setTitle("I R R I S O F T");
	frmIrrisoft.setResizable(false);
	frmIrrisoft.setBounds(100, 100, 879, 450);
	frmIrrisoft.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frmIrrisoft.getContentPane().setLayout(null);

	// Pinto la cabecera
	cabecera();

	// Pinto los botones de abajo
	principal();

    }

    /**
     * CABECERA con LOGOS
     */
    private void cabecera() {

	// Tabs del JTextPane
	TabStop[] tabStops = new TabStop[10];

	for (int pos = 0; pos < tabStops.length; pos++) {
	    tabStops[pos] = new TabStop(pos * 30, TabStop.ALIGN_LEFT,
		    TabStop.LEAD_DOTS);

	}
	TabSet tabSet = new TabSet(tabStops);

	// Estilo negrita JTextPane
	negrita = new SimpleAttributeSet();
	StyleConstants.setBold(negrita, true);
	StyleConstants.setFontFamily(negrita, "Dialog");
	StyleConstants.setFontSize(negrita, 11);
	StyleConstants.setTabSet(negrita, tabSet);

	// Estilo italic JTextPane
	italic = new SimpleAttributeSet();
	StyleConstants.setItalic(italic, true);
	StyleConstants.setFontFamily(italic, "Dialog");
	StyleConstants.setFontSize(italic, 12);
	StyleConstants.setTabSet(italic, tabSet);

	// Estilo normal
	normal = new SimpleAttributeSet();
	StyleConstants.setFontFamily(normal, "Dialog");
	StyleConstants.setFontSize(normal, 11);
	StyleConstants.setTabSet(normal, tabSet);

	JLabel imgraspi = new JLabel("");
	imgraspi.setBounds(0, 0, 86, 77);
	String ruta = IrrisoftConstantes.IMG_LOGO;
	redimensionado(imgraspi, ruta);
	frmIrrisoft.getContentPane().add(imgraspi);

	JLabel imgirrigestlife = new JLabel("");
	imgirrigestlife.setBounds(481, 2, 396, 76);
	ruta = IrrisoftConstantes.IMG_GEST;
	redimensionado(imgirrigestlife, ruta);
	frmIrrisoft.getContentPane().add(imgirrigestlife);

	JLabel imggestdropper = new JLabel("");
	imggestdropper.setBounds(242, 0, 168, 77);
	ruta = IrrisoftConstantes.IMG_GESTDROPPER;
	redimensionado(imggestdropper, ruta);
	frmIrrisoft.getContentPane().add(imggestdropper);

	JLabel imggotita = new JLabel("");
	imggotita.setBounds(163, 0, 67, 77);
	ruta = IrrisoftConstantes.IMG_GOTITA;
	redimensionado(imggotita, ruta);
	frmIrrisoft.getContentPane().add(imggotita);

	JScrollPane scroll = new JScrollPane();

	scroll.setViewportBorder(null);
	scroll.setBounds(488, 81, 385, 342);
	frmIrrisoft.getContentPane().add(scroll);
	scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	scroll.setBorder(null);

	// textArea = new JTextArea();
	textPane = new JTextPane();

	textPane.setParagraphAttributes(normal, true);

	doc = textPane.getStyledDocument();

	scroll.setViewportView(textPane);
	// panelpral.add(textArea);
	textPane.setEditable(false);
	textPane.addMouseListener(new ContextMenuMouseListener());
	// Para que haga scrolling automaticamente !!
	DefaultCaret caret = (DefaultCaret) textPane.getCaret();
	caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	textPane.setBackground(UIManager.getColor("CheckBox.background"));

	JSeparator separator_vert = new JSeparator();
	separator_vert.setBounds(481, 81, 3, 347);
	frmIrrisoft.getContentPane().add(separator_vert);
	separator_vert.setOrientation(SwingConstants.VERTICAL);

    }

    /**
     * Panel principal con botones de elección mci/bt2/conf/info/sensores
     */
    public void principal() {
	panelpral.setForeground(Color.RED);

	panelpral.setBounds(10, 82, 466, 341);
	frmIrrisoft.getContentPane().add(panelpral);
	panelpral.setLayout(null);

	JLabel lblEstado = new JLabel("BBDDLocal:");
	lblEstado.setBounds(29, 12, 88, 15);
	panelpral.add(lblEstado);

	lblstatusl.setBounds(138, 12, 103, 15);
	panelpral.add(lblstatusl);
	lblstatusl.setText("Desconectado");

	btnEmpezar = new JButton("Arrancar");
	btnEmpezar.setBounds(29, 63, 212, 34);
	panelpral.add(btnEmpezar);

	// BOTON EMPEZAR
	btnEmpezar.addActionListener(new ActionListener() {

	    // Empieza o para la escucha de nuevas tareas/programaciones

	    public void actionPerformed(ActionEvent arg0) {

		if (btnEmpezar.getText().contains("Arrancar")) {

		    hiloesc = new Thread(hiloescucha);
		    hiloesc.setName("Hiloescuha: " + hiloesc.getId());
		    hiloesc.start(); // Aquí empieza a ejecutarse el hilo
		    hilovolcado = new Thread(volcado);
		    hilovolcado.setName("Hilovolcado: " + hilovolcado.getId());
		    hilovolcado.start();

		    hiloescucha.setTerminar(false);
		    volcado.setTerminar(false);

		} else {
		    cierraDBhilo();
		    hiloescucha.setTerminar(true);
		    volcado.setTerminar(true);
		    // Invoco al garbage collector para liberar memoria !!
		    Runtime r = Runtime.getRuntime();
		    r.gc();

		}
	    }
	});

	// Placa mci
	imgmci = new JLabel("");
	imgmci.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {

		if (Irrisoft.window.valvsmci != null) {
		    // Aki se llama al panelmci
		    panelpral.setVisible(false);
		    panelmci.repaint();
		    frmIrrisoft.getContentPane().add(panelmci);
		    panelmci.setVisible(true);
		}
	    }
	});

	imgmci.setBounds(7, 265, 123, 63);
	String ruta = IrrisoftConstantes.IMG_MCI;
	redimensionado(imgmci, ruta);
	panelpral.add(imgmci);

	// Placa Bt2
	imgbt2 = new JLabel("");
	imgbt2.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {

		if (Irrisoft.window.valvsbt2 != null) {
		    // version firm bt
		    panelbt2.lblver.setText("Firmware ver: "
			    + Irrisoft.window.serie5.ver);
		    // Aki se llama al panelbt2
		    panelpral.setVisible(false);
		    panelbt2.repaint();
		    frmIrrisoft.getContentPane().add(panelbt2);
		    panelbt2.setVisible(true);
		}
	    }
	});

	imgbt2.setBounds(138, 265, 123, 63);
	ruta = IrrisoftConstantes.IMG_BT2;
	redimensionado(imgbt2, ruta);
	panelpral.add(imgbt2);

	// Placa Samcla
	imgsamcla = new JLabel("");
	imgsamcla.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {

		if (Irrisoft.window.valvsamcla != null) {
		    // version firm bt
		    // panelsamcla.lblver.setText("Firmware ver: "
		    // + Irrisoft.window.serie5.ver);
		    // Aki se llama al panelbt2
		    panelpral.setVisible(false);
		    panelsamcla.repaint();
		    frmIrrisoft.getContentPane().add(panelsamcla);
		    panelsamcla.setVisible(true);
		}
	    }
	});

	imgsamcla.setBounds(273, 282, 81, 24);
	ruta = IrrisoftConstantes.IMG_SAMCLA;
	redimensionado(imgsamcla, ruta);
	panelpral.add(imgsamcla);

	// Configuración
	JLabel imgconf = new JLabel("");
	imgconf.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		// Aki se llama al panel de configuración
		panelconf = Panelconf.getInstance();
		panelpral.setVisible(false);
		panelconf.repaint();
		frmIrrisoft.getContentPane().add(panelconf);
	    }
	});
	imgconf.setBounds(361, 278, 64, 50);
	ruta = IrrisoftConstantes.IMG_AJUSTES;
	redimensionado(imgconf, ruta);
	panelpral.add(imgconf);

	// Acerca de
	JLabel imginfo = new JLabel("");
	imginfo.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		// Info
		JOptionPane
			.showMessageDialog(
				Irrisoft.window.frmIrrisoft,
				"Irrisoft version 0.3.62\nPrograma que gestiona el riego automático integrado en un SIG/GIS.\n\nAutores:   Mikel Merino <astrapotro@gmail.com>\n                   Alberto Díez <alberto.diez.lejarazu@gmail.com>\n                   Diego Alonso <diego.alonso.gonzalez@gmail.com>\n\nTodos los derechos reservados 2013 "+copyright+"\n\n",
				"Acerca de ...",
				JOptionPane.INFORMATION_MESSAGE);
	    }
	});
	imginfo.setBounds(424, 298, 36, 30);
	ruta = IrrisoftConstantes.IMG_INFO;
	redimensionado(imginfo, ruta);
	panelpral.add(imginfo);

	Testbutton = new JButton("Testeos");
	Testbutton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		// Aki se llama al panel de TEST
		paneltest = Paneltest.getInstance();
		panelpral.setVisible(false);
		// paneltest.repaint();
		frmIrrisoft.getContentPane().add(paneltest);
	    }
	});
	Testbutton.setBounds(274, 63, 168, 34);
	panelpral.add(Testbutton);

	lblfecha = new JLabel("");
	lblfecha.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 15));
	Border border = LineBorder.createGrayLineBorder();
	lblfecha.setBorder(new LineBorder(Color.GRAY, 1, true));
	lblfecha.setHorizontalAlignment(SwingConstants.CENTER);
	lblfecha.setBounds(273, 12, 168, 35);
	panelpral.add(lblfecha);

	JLabel lblEstadoremoto = new JLabel("BBDDRemota:");
	lblEstadoremoto.setBounds(29, 32, 103, 15);
	panelpral.add(lblEstadoremoto);

	lblstatusr.setText("Desconectado");
	lblstatusr.setBounds(138, 32, 103, 15);
	panelpral.add(lblstatusr);

	JButton botonsensores = new JButton("Lecturas Sensores");
	botonsensores.setBounds(29, 121, 413, 50);
	panelpral.add(botonsensores);
	botonsensores.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {

		// Aki se llama al panelecturas
		panelpral.setVisible(false);
		panelecturas.repaint();
		frmIrrisoft.getContentPane().add(panelecturas);
		panelecturas.setVisible(true);

	    }
	});

	Border bordermaestras = LineBorder.createGrayLineBorder();
	lblmaestra1 = new JLabel("Válvula Maestra 1: OFF");
	lblmaestra1.setForeground(Color.RED);
	lblmaestra1.setHorizontalAlignment(SwingConstants.CENTER);
	lblmaestra1.setFont(new Font("Dialog", Font.BOLD, 12));
	lblmaestra1.setBounds(29, 199, 194, 34);
	lblmaestra1.setBorder(bordermaestras);
	panelpral.add(lblmaestra1);

	lblmaestra2 = new JLabel("Válvula Maestra 2: OFF");
	lblmaestra2.setForeground(Color.RED);
	lblmaestra2.setHorizontalAlignment(SwingConstants.CENTER);
	lblmaestra2.setFont(new Font("Dialog", Font.BOLD, 12));
	lblmaestra2.setBounds(247, 199, 194, 34);
	lblmaestra2.setBorder(bordermaestras);
	panelpral.add(lblmaestra2);

	JPanel paneliconos = new JPanel();
	paneliconos.setBorder(new LineBorder(new Color(0, 0, 0), 2));
	paneliconos.setBackground(Color.DARK_GRAY);
	paneliconos.setBounds(0, 0, 879, 80);
	frmIrrisoft.getContentPane().add(paneliconos);

    }

    /**
     * Redimensionado imagenes
     * 
     * @param jlabel
     * @param ruta
     */
    public void redimensionado(JLabel jlabel, String ruta) {
	ImageIcon img = new ImageIcon(Irrisoft.class.getResource(ruta));
	Icon icono = new ImageIcon(img.getImage().getScaledInstance(
		jlabel.getWidth(), jlabel.getHeight(), Image.SCALE_SMOOTH));
	jlabel.setIcon(icono);
    }

    /**
     * METODO REALIZADO PARA ARCHIVO CONFIGURACION.PROPERTIES Leo el archivo
     * Irrisoft.properties
     * 
     * @param config
     */
    public void leerconfirri(Conf config) {
	Properties propiedades = new Properties() {
	    @Override
	    public synchronized Enumeration<Object> keys() {
		return Collections.enumeration(new TreeSet<Object>(super
			.keySet()));
	    }
	};
	InputStream lecturaIrrisoft = null;
	BasicTextEncryptor textIrri = new BasicTextEncryptor();
	String a, b, c = null;

	try {

	    lecturaIrrisoft = new FileInputStream("Irrisoft.properties");
	    propiedades.load(lecturaIrrisoft);
	    lecturaIrrisoft.close();

	    config.setIdrasp(propiedades.getProperty("Aparato.ID"));
	    config.setCorreo(propiedades.getProperty("CORREO"));
	    config.setHost(propiedades.getProperty("Local.Conexion.HOST"));
	    config.setPuerto(Integer.parseInt(propiedades
		    .getProperty("Local.Conexion.PUERTO")));
	    config.setDb(propiedades.getProperty("Local.Conexion.DB"));
	    config.setUsuario(propiedades.getProperty("Local.Login.USUARIO"));

	    // if (config.getMci() == null) {
	    // config.setMci("");
	    // } else {config.setMci(config.getMci());}
	    // propiedades.setProperty("Puerto.mci", config.getMci());
	    //
	    // if (config.getMci2() == null) {
	    // config.setMci2("");
	    // } else {config.setMci2(config.getMci2());}
	    // propiedades.setProperty("Puerto.mci2", config.getMci2());
	    //
	    // if (config.getMci3() == null) {
	    // config.setMci3("");
	    // } else {config.setMci3(config.getMci3());}
	    // propiedades.setProperty("Puerto.mci3", config.getMci3());
	    //
	    // if (config.getMci4() == null) {
	    // config.setMci4("");
	    // } else {config.setMci4(config.getMci4());}
	    // propiedades.setProperty("Puerto.mci4", config.getMci4());
	    //
	    // if (config.getBt2() == null) {
	    // config.setBt2("");
	    // } else {config.setBt2(config.getBt2());}
	    // propiedades.setProperty("Puerto.bt2", config.getBt2());
	    //
	    // if (config.getBt22() == null) {
	    // config.setBt22("");
	    // } else {config.setBt22(config.getBt22());}
	    // propiedades.setProperty("Puerto.bt2-2", config.getBt22());
	    //
	    // if (config.getBt23() == null) {
	    // config.setBt23("");
	    // } else {config.setBt23(config.getBt23());}
	    // propiedades.setProperty("Puerto.bt2-3", config.getBt23());
	    //
	    // if (config.getBt24() == null) {
	    // config.setBt24("");
	    // } else {config.setBt24(config.getBt24());}
	    // propiedades.setProperty("Puerto.bt2-4", config.getBt24());

	    config.setLimitebt(Integer.parseInt(propiedades
		    .getProperty("BT2.limitebt")));

	    if (propiedades.getProperty("Senal.FLAG").equals("si")) {
		// Coloco el password
		textIrri.setPassword(IrrisoftConstantes.PASSWORD);
		// Cifro el flag que utilizo
		a = textIrri.encrypt(propiedades.getProperty("Senal.FLAG"));
		propiedades.setProperty("Senal.FLAG", a);
		// // Cifro la contraseña de Irrisoft.
		config.setPass(propiedades.getProperty("Local.Login.PASS"));
		b = textIrri.encrypt(propiedades
			.getProperty("Local.Login.PASS"));
		propiedades.setProperty("Local.Login.PASS", b);
		// Guardo en el archivo de salida
		OutputStream osIrri = new FileOutputStream(
			"Irrisoft.properties");
		propiedades.store(osIrri, null);
		osIrri.close();
	    } else {
		textIrri.setPassword(IrrisoftConstantes.PASSWORD);
		// Descifro la contraseña en memoria.
		c = textIrri.decrypt(propiedades
			.getProperty("Local.Login.PASS"));
		propiedades.setProperty("Local.Login.PASS", c);
		config.setPass(propiedades.getProperty("Local.Login.PASS"));
		b = textIrri.encrypt(propiedades
			.getProperty("Local.Login.PASS"));
		propiedades.setProperty("Local.Login.PASS", b);

		// Guardo en el archivo de salida
		OutputStream osIrri = new FileOutputStream(
			"Irrisoft.properties");
		propiedades.store(osIrri, null);
		osIrri.close();

	    }

	} catch (FileNotFoundException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("No existe el fichero de configuración !! \n Se sale !");
		logger.error(e.getMessage());
	    }
	} catch (IOException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido leer el archivo !! \n Se sale !");
		logger.error(e.getMessage());
	    }
	}
    }

    /**
     * Lee los puertos de la bt2 (ftdi) del archivo previamente guardado en el
     * arranque de la sbc
     */
    public void leerpuertosbt() {

	FileReader fr = null;
	String linea = null;
	// int i=0;
	String aux = null;

	try {
	    fr = new FileReader(puertosbt); // Ruta al archivo de configuración

	} catch (FileNotFoundException e1) {
	    if (logger.isErrorEnabled()) {
		logger.error(e1.getMessage());
	    }
	    try {
		Irrisoft.window.textPane
			.getStyledDocument()
			.insertString(
				doc.getLength(),
				("No existe el fichero de los puertos bt2 :\n" + puertosbt),
				null);
	    } catch (BadLocationException e) {
		if (logger.isErrorEnabled()) {
		    logger.error(e.getMessage());
		}
	    }

	    if (logger.isWarnEnabled()) {
		logger.warn("No existe el fichero de los puertos bt2:\n"
			+ puertosbt);
	    }
	}
	BufferedReader bf = null;
	try {
	    bf = new BufferedReader(fr);
	} catch (Exception e1) {
	    if (logger.isErrorEnabled()) {
		logger.error(e1.getMessage());
	    }
	}
	//
	// Leo línea línea el fichero de puertos
	//

	try {
	    ArrayList<String> puertosbt2 = new ArrayList<String>();
	    ArrayList<String> puertosmci = new ArrayList<String>();
	    ArrayList<String> puertosSens = new ArrayList<String>();

	    while ((linea = bf.readLine()) != null) {

		// El script puertosbt.sh me pone un espacio al final !! yo se
		// lo quito
		if (linea.contains("ftdi")) {
		    aux = linea.substring(0, 5);
		    puertosbt2.add(aux);
		} else if (linea.contains("placa_controladora")) {
		    aux = linea.substring(0, 19);
		    puertosmci.add(aux);
		} else if (linea.contains("placa_sensores")) {
		    aux = linea.substring(0, 14);
		    puertosSens.add(aux);

		}

	    }

	    Collections.sort(puertosbt2);
	    Collections.sort(puertosmci);
	    Collections.sort(puertosSens);

	    // // Asigno los puertos bt2
	    for (int z = 0; z < puertosbt2.size(); z++) {
		if (z == 0) {
		    config.setBt2("/dev/" + puertosbt2.get(0));
		} else if (z == 1) {
		    config.setBt22("/dev/" + puertosbt2.get(1));
		} else if (z == 2) {
		    config.setBt23("/dev/" + puertosbt2.get(2));
		} else if (z == 3) {
		    config.setBt24("/dev/" + puertosbt2.get(3));
		}
	    }
	    // Asigno los puertos mci
	    for (int z = 0; z < puertosmci.size(); z++) {
		if (z == 0)
		    config.setMci("/dev/" + puertosmci.get(0));
		else if (z == 1)
		    config.setMci2("/dev/" + puertosmci.get(1));
		else if (z == 2)
		    config.setMci3("/dev/" + puertosmci.get(2));
		else if (z == 3)
		    config.setMci4("/dev/" + puertosmci.get(3));
	    }
	    // // Asigno los puertos Sens
	    for (int z = 0; z < puertosSens.size(); z++) {
		if (z == 0)
		    config.setSens("/dev/" + puertosSens.get(0));
	    }

	    // Borro las listas
	    puertosbt2 = null;
	    puertosmci = null;
	    puertosSens = null;

	} catch (IOException e1) {
	    if (logger.isErrorEnabled()) {
		logger.error(e1.getMessage());
	    }
	}
	if (logger.isInfoEnabled()) {
	    logger.info("Mci: " + config.getMci());
	    logger.info("Mci2: " + config.getMci2());
	    logger.info("Mci3: " + config.getMci3());
	    logger.info("Mci4: " + config.getMci4());
	    logger.info("BT2: " + config.getBt2());
	    logger.info("BT22: " + config.getBt22());
	    logger.info("BT23: " + config.getBt23());
	    logger.info("BT24: " + config.getBt24());
	    logger.info("Sens: " + config.getSens());
	}
	try {
	    fr.close();
	    bf.close();

	} catch (Exception e) {
	    if (logger.isErrorEnabled()) {
		logger.error("No se ha podido cerrar la lectura de archivo !!");
		logger.error(e.getMessage());
	    }
	}

    }

    /**
     * Abro conexión y me pongo a escuchar automáticamente a la local en otro
     * hilo dedicado exclusivamente a ello.
     */
    protected void llamaescucha() {
	hiloesc = new Thread(hiloescucha);
	hiloesc.setName("HiloEscucha " + hiloesc.getId());
	hiloesc.start(); // Aquí empieza a ejecutarse el hilo

    }

    /**
     * Leo las configuraciones iniciales de los programadores
     */
    public void leerconfini() {

	// Cierro todos las conexiones serie
	comprobarSerie();

	// Configuración inicial Valvulas
	leerconf_ini_prog();

	// Configuración inicial sensores
	leerconf_ini_sens();

	Irrisoft.window.rearmar = false;

	// instancio los paneles de lecturas de sensores
	window.panelecturas = Panelecturas.getInstance();
	window.panelecturas.habilitabotones();

	// Miro las bt2 abiertas
	mirarAbiertasbt();

    }

    /**
     * Miro las valvulas BT abiertas que hay.
     */
    private void mirarAbiertasbt() {

	if (valvsbt2 != null) {
	    serie5.cerrarAbiertasAlInicio(serie5.serialPort, 5);
	}
	if (valvsbt22 != null) {
	    serie6.cerrarAbiertasAlInicio(serie6.serialPort, 6);
	}
	if (valvsbt23 != null) {
	    serie7.cerrarAbiertasAlInicio(serie7.serialPort, 7);
	}
	if (valvsbt24 != null) {
	    serie8.cerrarAbiertasAlInicio(serie8.serialPort, 8);
	}

    }

    /**
     * Leo la configuracion inicial de los programadores, las valvulas que
     * tienen asociadas.
     */
    private void leerconf_ini_prog() {

	// TODO A REVISAR (hacer como en leerconf_ini_sens, creando un objeto y
	// asignandole directamente los campos)

	if (valvsabiertastot == null)
	    valvsabiertastot = new LinkedHashSet<Valvula>();

	String valvula;
	int deco = 0, maestra, latch, goteo;
	BigInteger serie;

	PreparedStatement sentenciapre = null;
	ResultSet resultados = null;

	// Limpio las listas y los flags

	bt2combo.clear();
	mcicombo.clear();
	this.maestra = null;
	this.maestra1 = null;
	this.maestras = 0;

	if (placas != null)
	    placas.getplacas().clear();
	if (valvsbt2 != null)
	    valvsbt2.getvalvulas().clear();
	if (valvsbt22 != null)
	    valvsbt22.getvalvulas().clear();
	if (valvsbt23 != null)
	    valvsbt23.getvalvulas().clear();
	if (valvsbt24 != null)
	    valvsbt24.getvalvulas().clear();
	if (valvsmci != null)
	    valvsmci.getvalvulas().clear();
	if (valvsmci2 != null)
	    valvsmci2.getvalvulas().clear();
	if (valvsmci3 != null)
	    valvsmci3.getvalvulas().clear();
	if (valvsmci4 != null)
	    valvsmci4.getvalvulas().clear();
	if (valvsamcla != null)
	    valvsamcla.getvalvulas().clear();

	valvsmci = null;
	valvsmci2 = null;
	valvsmci3 = null;
	valvsmci4 = null;
	valvsbt2 = null;
	valvsbt22 = null;
	valvsbt23 = null;
	valvsbt24 = null;
	valvsamcla = null;
	valvsmaestras = null;

	try {

	    conn.conectal();

	    sentenciapre = conn.conn
		    .prepareStatement("select * from conf_ini_prog where CODPROG="
			    + Irrisoft.config.getIdrasp());

	    resultados = sentenciapre.executeQuery();

	    int i = 0, j = 0, k = 0, l = 0, w = 0, x = 0, y = 0, z = 0, s = 0;

	    while (resultados.next()) {

		// num_estacion
		valvula = resultados.getString(3);

		// cargo el num de deco sólo si es bt2 (podrá añadirse la MCI)
		deco = resultados.getInt(4);

		// maestra
		maestra = resultados.getInt(5);

		// latch
		latch = resultados.getInt(6);

		// goteo
		goteo = resultados.getInt(7);

		if (logger.isInfoEnabled()) {
		    logger.info("El programador tiene la valvula " + valvula
			    + "");
		}

		//
		// //////////////////////////////////////////// BT2s

		if (Integer.parseInt(valvula) > IrrisoftConstantes.BT2_1000
			&& Integer.parseInt(valvula) < IrrisoftConstantes.SAMCLA) {

		    if (Integer.parseInt(valvula) > IrrisoftConstantes.BT2_1000
			    && Integer.parseInt(valvula) < IrrisoftConstantes.BT2_2000) {

			if (valvsbt2 == null) {
			    valvsbt2 = new ListaValvBt2();
			    nuevoSerie(semaforobt2, serie5, config.getBt2(), 5);
			    activabt(serie5);
			}
			ponerconfBT(valvsbt2, i, valvula, deco, maestra, latch,
				goteo, 5);
			i++;
		    } else if (Integer.parseInt(valvula) > IrrisoftConstantes.BT2_2000
			    && Integer.parseInt(valvula) < IrrisoftConstantes.BT2_3000) {

			if (valvsbt22 == null) {
			    valvsbt22 = new ListaValvBt2();
			    nuevoSerie(semaforobt22, serie6, config.getBt22(),
				    6);
			    activabt(serie6);
			}

			ponerconfBT(valvsbt22, j, valvula, deco, maestra,
				latch, goteo, 6);
			j++;
		    } else if (Integer.parseInt(valvula) > IrrisoftConstantes.BT2_3000
			    && Integer.parseInt(valvula) < IrrisoftConstantes.BT2_4000) {

			if (valvsbt23 == null) {
			    valvsbt23 = new ListaValvBt2();
			    nuevoSerie(semaforobt23, serie7, config.getBt23(),
				    7);
			    activabt(serie7);
			}

			ponerconfBT(valvsbt23, k, valvula, deco, maestra,
				latch, goteo, 7);
			k++;

		    } else if (Integer.parseInt(valvula) > IrrisoftConstantes.BT2_4000) {

			if (valvsbt24 == null) {
			    valvsbt24 = new ListaValvBt2();
			    nuevoSerie(semaforobt24, serie8, config.getBt24(),
				    8);
			    activabt(serie8);
			}

			ponerconfBT(valvsbt24, l, valvula, deco, maestra,
				latch, goteo, 8);
			l++;

		    }

		    //
		    // //////////////////////////////////////////// MCIs

		} else if (Integer.parseInt(valvula) < IrrisoftConstantes.BT2_1000) {

		    if (Integer.parseInt(valvula) > IrrisoftConstantes.MCI_100
			    && Integer.parseInt(valvula) < IrrisoftConstantes.MCI_200) {

			if (valvsmci == null) {
			    valvsmci = new ListaValvMci();
			    nuevoSerie(semaforomci, serie1, config.getMci(), 1);
			    series.add(serie1);
			    // placa1 = new PlacaGhost();
			    // placa1.setNumplaca(1);
			    // placa1.setTieneamp(false);
			    // placas.addplaca(placa1);

			    // if (w == 0)
			    // placas = new ListaPlacasGhost();
			    // placas.addplaca(placa1);

			}
			ponerconfMCI(valvsmci, w, valvula, maestra, latch,
				goteo, 1);
			w++;

		    } else if (Integer.parseInt(valvula) > IrrisoftConstantes.MCI_200
			    && Integer.parseInt(valvula) < IrrisoftConstantes.MCI_300) {

			if (valvsmci2 == null) {
			    valvsmci2 = new ListaValvMci();
			    nuevoSerie(semaforomci2, serie2, config.getMci2(),
				    2);
			    series.add(serie2);
			    // placa2 = new PlacaGhost();
			    // placa2.setNumplaca(2);
			    // placa2.setTieneamp(false);
			    // placas.addplaca(placa2);

			}

			ponerconfMCI(valvsmci2, x, valvula, maestra, latch,
				goteo, 2);
			x++;
		    } else if (Integer.parseInt(valvula) > IrrisoftConstantes.MCI_300
			    && Integer.parseInt(valvula) < IrrisoftConstantes.MCI_400) {

			if (valvsmci3 == null) {
			    valvsmci3 = new ListaValvMci();
			    nuevoSerie(semaforomci3, serie3, config.getMci3(),
				    3);
			    series.add(serie3);
			    // placa3 = new PlacaGhost();
			    // placa3.setNumplaca(3);
			    // placa3.setTieneamp(false);
			    // placas.addplaca(placa3);

			}

			ponerconfMCI(valvsmci3, y, valvula, maestra, latch,
				goteo, 3);
			y++;

		    } else if (Integer.parseInt(valvula) > IrrisoftConstantes.MCI_400) {

			if (valvsmci4 == null) {
			    valvsmci4 = new ListaValvMci();
			    nuevoSerie(semaforomci4, serie4, config.getMci4(),
				    4);
			    series.add(serie4);
			    // placa4 = new PlacaGhost();
			    // placa4.setNumplaca(4);
			    // placa4.setTieneamp(false);
			    // placas.addplaca(placa4);
			}
			ponerconfMCI(valvsmci4, z, valvula, maestra, latch,
				goteo, 4);
			z++;
		    }

		}

		//
		// //////////////////////////////////////////// SAMCLA
		else if (Integer.parseInt(valvula) > IrrisoftConstantes.SAMCLA) {

		    if (valvsamcla == null) {
			valvsamcla = new ListaValvSamcla();
			nuevoSerie(semaforosamcla, seriesamcla,
				IrrisoftConstantes.PUERTO_SAMCLA, -1);
		    }
		    ponerconfSamcla(valvsamcla, s, valvula, deco, maestra,
			    latch, goteo, 1, resultados.getLong(8));
		    s++;
		}

	    }

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	}

	try {
	    sentenciapre.close();
	    resultados.close();
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	}

	// Lo dejo aki porque los métodos de las maestras residen en ese panel (
	// A CAMBIAR !!! TODO)
	window.panelbt2 = PanelBt2.getInstance();
	// Desactivo los botones de las placas si no hay válvulas
	// correspondientes e instancio los paneles correspondientes
	if (valvsmci == null)
	    imgmci.setEnabled(false);
	else {
	    window.panelmci = Panelmci.getInstance();
	    // Cargo la lista de iconos valvulas mci!
	    window.panelmci.pintavalvsmci();
	    imgmci.setEnabled(true);
	}
	if (valvsbt2 == null)
	    imgbt2.setEnabled(false);
	else {

	    imgbt2.setEnabled(true);
	}
	if (valvsamcla == null)
	    imgsamcla.setEnabled(false);
	else {
	    window.panelsamcla = PanelSamcla.getInstance();
	    imgsamcla.setEnabled(true);
	}
    }

    /**
     * Pongo la configuracion de las BT2
     * 
     * @param valvsbt
     * @param i
     * @param valv
     * @param deco
     * @param maes
     * @param latx
     * @param goteo
     * @param numplac
     */
    private void ponerconfBT(ListaValvBt2 valvsbt, int i, String valv,
	    int deco, int maes, int latx, int goteo, int numplac) {

	valvsbt.addvalvbt2(new Valvula());
	valvsbt.getvalvbt2(i).setAbierta(false);
	valvsbt.getvalvbt2(i).setCodelecvalv(valv);
	valvsbt.getvalvbt2(i).setDeco(deco);
	valvsbt.getvalvbt2(i).setMaestra(maes);
	valvsbt.getvalvbt2(i).setLatch(latx);
	valvsbt.getvalvbt2(i).setGoteo(goteo);

	conn.recogeconsummod(i, valv);

	if (valvsbt.getvalvbt2(i).isMaestra() == 1) {
	    if (maestra == null) {

		maestra = new Valvula();
		maestra = valvsbt.getvalvbt2(i);
		valvsmaestras = new ArrayList<Valvula>();
		valvsmaestras.add(maestra);
		maestras++;

	    } else if (maestra1 == null) {
		maestra1 = new Valvula();
		maestra1 = valvsbt.getvalvbt2(i);
		valvsmaestras.add(maestra1);
		maestras++;
	    }
	} else
	    bt2combo.add(valvsbt.getvalvbt2(i).getCodelecvalv());

	if (numplac == 5) {
	    valvsbt.getvalvbt2(i).setNum_placa(5);
	    valvsbt.getvalvbt2(i).setPuerto(Irrisoft.config.getBt2());
	    valvsbt.getvalvbt2(i).setSemaforo(semaforobt2);
	    valvsbt.getvalvbt2(i).setSerie(serie5);
	    valvsabiertasbt2 = new LinkedHashSet<Valvula>();

	}
	if (numplac == 6) {
	    valvsbt.getvalvbt2(i).setNum_placa(6);
	    valvsbt.getvalvbt2(i).setPuerto(Irrisoft.config.getBt22());
	    valvsbt.getvalvbt2(i).setSemaforo(semaforobt22);
	    valvsbt.getvalvbt2(i).setSerie(serie6);
	    valvsabiertasbt22 = new LinkedHashSet<Valvula>();

	}
	if (numplac == 7) {
	    valvsbt.getvalvbt2(i).setNum_placa(7);
	    valvsbt.getvalvbt2(i).setPuerto(Irrisoft.config.getBt23());
	    valvsbt.getvalvbt2(i).setSemaforo(semaforobt23);
	    valvsbt.getvalvbt2(i).setSerie(serie7);
	    valvsabiertasbt23 = new LinkedHashSet<Valvula>();

	}
	if (numplac == 8) {
	    valvsbt.getvalvbt2(i).setNum_placa(8);
	    valvsbt.getvalvbt2(i).setPuerto(Irrisoft.config.getBt24());
	    valvsbt.getvalvbt2(i).setSemaforo(semaforobt24);
	    valvsbt.getvalvbt2(i).setSerie(serie8);
	    valvsabiertasbt24 = new LinkedHashSet<Valvula>();
	}

	// //Si tengo alguna LATCH tengo que bajar la sensibilidad de la BT2 a
	// 30mA (comando 10H),
	// // y cambiar el tipo de decoder a 2 Aquative plus o a un supuesto 3
	// (comando 13H)

    }

    /**
     * Pongo la configuracion de las MCI
     * 
     * @param valvsmulti
     * @param i
     * @param valv
     * @param maes
     * @param latx
     * @param goteo
     * @param numplac
     */
    private void ponerconfMCI(ListaValvMci valvsmulti, int i, String valv,
	    int maes, int latx, int goteo, int numplac) {

	valvsmulti.addvalvmci(new Valvula());
	valvsmulti.getvalvmci(i).setAbierta(false);
	valvsmulti.getvalvmci(i).setCodelecvalv(valv);
	valvsmulti.getvalvmci(i).setMaestra(maes);
	valvsmulti.getvalvmci(i).setLatch(latx);
	valvsmulti.getvalvmci(i).setGoteo(goteo);

	if (valvsmulti.getvalvmci(i).isMaestra() == 1) {
	    if (maestra == null) {

		maestra = new Valvula();
		maestra = valvsmulti.getvalvmci(i);
		valvsmaestras = new ArrayList<Valvula>();
		valvsmaestras.add(maestra);
		maestras++;

	    } else if (maestra1 == null) {
		maestra1 = new Valvula();
		maestra1 = valvsmulti.getvalvmci(i);
		valvsmaestras.add(maestra1);
		maestras++;

	    }
	} else
	    mcicombo.add(valvsmci.getvalvmci(i).getCodelecvalv());

	if (numplac == 1) {
	    valvsmulti.getvalvmci(i).setNum_placa(1);
	    valvsmulti.getvalvmci(i).setPuerto(Irrisoft.config.getMci());
	    valvsmulti.getvalvmci(i).setSemaforo(semaforomci);
	    valvsmulti.getvalvmci(i).setSerie(serie1);
	    valvsabiertasmci = new LinkedHashSet<Valvula>();
	}
	if (numplac == 2) {
	    valvsmulti.getvalvmci(i).setNum_placa(2);
	    valvsmulti.getvalvmci(i).setPuerto(Irrisoft.config.getMci2());
	    valvsmulti.getvalvmci(i).setSemaforo(semaforomci2);
	    valvsmulti.getvalvmci(i).setSerie(serie2);
	    valvsabiertasmci2 = new LinkedHashSet<Valvula>();
	}
	if (numplac == 3) {
	    valvsmulti.getvalvmci(i).setNum_placa(3);
	    valvsmulti.getvalvmci(i).setPuerto(Irrisoft.config.getMci3());
	    valvsmulti.getvalvmci(i).setSemaforo(semaforomci3);
	    valvsmulti.getvalvmci(i).setSerie(serie3);
	    valvsabiertasmci3 = new LinkedHashSet<Valvula>();
	}
	if (numplac == 4) {
	    valvsmulti.getvalvmci(i).setNum_placa(4);
	    valvsmulti.getvalvmci(i).setPuerto(Irrisoft.config.getMci4());
	    valvsmulti.getvalvmci(i).setSemaforo(semaforomci4);
	    valvsmulti.getvalvmci(i).setSerie(serie4);
	    valvsabiertasmci4 = new LinkedHashSet<Valvula>();
	}

	conn.recogeconsummod(i, valv);

    }

    /**
     * Pongo la configuracion de las MCI
     * 
     * @param valvsmulti
     * @param i
     * @param valv
     * @param maes
     * @param latx
     * @param goteo
     * @param numplac
     */
    private void ponerconfSamcla(ListaValvSamcla valvs, int i, String valv,
	    int deco, int maes, int latx, int goteo, int numplac, Long serie) {

	valvs.addvalvsamcla(new Valvula());
	valvs.getvalvsamcla(i).setAbierta(false);
	valvs.getvalvsamcla(i).setCodelecvalv(valv);
	valvs.getvalvsamcla(i).setMaestra(maes);
	valvs.getvalvsamcla(i).setLatch(latx);
	valvs.getvalvsamcla(i).setGoteo(goteo);
	valvs.getvalvsamcla(i).setNumserie(serie);

	if (valvsamcla.getvalvsamcla(i).isMaestra() == 1) {
	    if (maestra == null) {
		maestra = new Valvula();
		maestra = valvsamcla.getvalvsamcla(i);
		valvsmaestras = new ArrayList<Valvula>();
		valvsmaestras.add(maestra);
		maestras++;

	    } else if (maestra1 == null) {
		maestra1 = new Valvula();
		maestra1 = valvsamcla.getvalvsamcla(i);
		valvsmaestras.add(maestra1);
		maestras++;

	    }
	} else
	    samclacombo.add(valvs.getvalvsamcla(i).getCodelecvalv());

	valvsamcla.getvalvsamcla(i).setNum_placa(-11);
	// valvsamcla.getvalvsamcla(i).setPuerto(Irrisoft.config.getMci());
	valvsamcla.getvalvsamcla(i).setSemaforo(semaforosamcla);
	// valvsamcla.getvalvsamcla(i).setSerie(serie1);
	valvsabiertasamcla = new LinkedHashSet<Valvula>();

	conn.recogeconsummod(i, valv);

    }

    /**
     * Leo la configuracion de sensores del programador.
     */
    private void leerconf_ini_sens() {

	// Limpio lista de sensores
	sensores.clear();
	// ponngo a false los flags de sensores
	Irrisoft.window.hayplacasens = false;
	Irrisoft.window.haycaudalimetro = false;
	sensmci = false;
	sensmci2 = false;
	sensmci3 = false;
	sensmci4 = false;
	sensbt2 = false;
	sensbt22 = false;
	sensbt23 = false;
	sensbt24 = false;

	// /////////////////////////////////
	// SENSOR FANTASMA DE AMPERIMETRO para placas mci de Jaime (a quitar)
	// !!!!
	// int lastghost = 0;
	// if (placas != null && !hayampplacasensores) {
	// for (int i = 0; i < placas.getsizeof(); i++) {
	// Sensor sens = new Sensor();
	// sens.setCodprog(Irrisoft.config.getIdrasp());
	// sens.setNum_placa(i + 1);
	// sens.setTipo(3);
	// sens.setghost(true);
	// sensores.add(sens);
	// hilos_sens_ghost(i);
	// }
	// lastghost = placas.getsizeof();
	// }

	PreparedStatement sentenciapre = null;
	ResultSet resultados = null;

	try {
	    conn.conectal();
	    sentenciapre = conn.conn
		    .prepareStatement("select * from conf_ini_sens where CODPROG="
			    + Irrisoft.config.getIdrasp());
	    resultados = sentenciapre.executeQuery();

	    while (resultados.next()) {

		Sensor sensor = new Sensor();

		sensor.setCodprog(resultados.getString(2));
		sensor.setNum_placa(resultados.getInt(3));
		sensor.setTipo_placa(resultados.getString(4));
		sensor.setNum_sensor(resultados.getString(5));
		sensor.setNum_borna(resultados.getInt(6));
		sensor.setUni_med(resultados.getString(7));
		sensor.setUni_sal(resultados.getString(8));
		sensor.setRang_med_min(resultados.getDouble(9));
		sensor.setRang_med_max(resultados.getDouble(10));
		sensor.setRang_sal_min(resultados.getDouble(11));
		sensor.setRang_sal_max(resultados.getDouble(12));
		sensor.setMed_umbral_min(resultados.getDouble(13));
		sensor.setMed_umbral_max(resultados.getDouble(14));
		sensor.setFrec_lect(resultados.getInt(15));
		sensor.setFrec_env(resultados.getInt(16));
		sensor.setK(resultados.getInt(17));
		sensor.setError_sup(resultados.getInt(18));
		sensor.setError_inf(resultados.getInt(19));
		sensor.setT_max_riego(resultados.getInt(20));
		sensor.setNum_est_prop(resultados.getString(21));
		sensor.setNum_est_asoc(resultados.getString(22));
		sensor.setLecturacau(0);
		// Saco el num de sensor del nombre del sensor

//		sensor.setNum_sens(Integer.parseInt(sensor.getNum_sensor()
//			.substring(4, 7)));

		// Añado el sensor a la lista de sensores
		sensores.add(sensor);

	    }

	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error en la sentencia SQL:" +e.getMessage());
	    }
	}

	try {
	    sentenciapre.close();
	    resultados.close();
	} catch (SQLException e) {
	    if (logger.isErrorEnabled()) {
		logger.error("Error a la hora de cerrar la conexion con la BBDD Local: " +e.getMessage());
	    }
	}

	for (int i = 0; i < sensores.size(); i++) {
	    if (logger.isInfoEnabled()) {
		logger.info("SENSOR :" + sensores.get(i).getNum_sensor());
	    }
	    ponerconfsens(i);

	    try {
		Thread.sleep(IrrisoftConstantes.DELAY_LEERCONF_SENSORES);
	    } catch (InterruptedException e) {
		if (logger.isErrorEnabled()) {
		    logger.error("Hilo interrumpido: " +e.getMessage());
		}
	    }
	}

	// Si no hay caudalimetro deshabilito el botón
	// TEST
	if (window.haycaudalimetro)
	    window.Testbutton.setEnabled(true);
	else
	    window.Testbutton.setEnabled(false);

    }

    /**
     * Pongo la configuracion de los sensores. Los sensores que hay son:
     * Contador de pulsos Sensor de humedad Amperimetro Pluviometro Sensor de
     * Temperatura Anemometro Sensor de Flujo Sensor de Intrusion
     * 
     * @param i
     */
    public void ponerconfsens(int i) {

	int tipo = 0;

	// Contador de Pulsos 1
	if (sensores.get(i).getNum_sensor().contains("Pu")) {

	    tipo = 1;
	    sensores.get(i).setTipo(1);
	    haycaudalimetro = true;

	}

	// Humedad de Suelo 2
	else if (sensores.get(i).getNum_sensor().contains("Hum")) {
	    logger.info("ponerconfsensores");
	    tipo = 2;
	    sensores.get(i).setTipo(2);

	}

	// Amperimetro 3
	else if (sensores.get(i).getNum_sensor().contains("Am")) {

	    tipo = 3;
	    sensores.get(i).setTipo(3);

	}

	// Pluviometro 4
	else if (sensores.get(i).getNum_sensor().contains("Pl")) {

	    tipo = 4;
	    sensores.get(i).setTipo(4);

	}

	// Temperatura 5
	else if (sensores.get(i).getNum_sensor().contains("Temp")) {

	    tipo = 5;
	    sensores.get(i).setTipo(5);

	}

	// Anemometro 6
	else if (sensores.get(i).getNum_sensor().contains("An")) {

	    tipo = 6;
	    sensores.get(i).setTipo(6);
	}

	// Flujo 7
	else if (sensores.get(i).getNum_sensor().contains("Flu")) {

	    tipo = 7;
	    sensores.get(i).setTipo(7);

	}

	// Intrusión 8
	else if (sensores.get(i).getNum_sensor().contains("Int")) {

	    tipo = 8;
	    sensores.get(i).setTipo(8);

	}

	// Creo los hilos de los sensores conectados
	hilos_sens(i, tipo);

    }

    /**
     * Asocio cada sensor con su placa y le asocio un hilo.
     * 
     * @param i
     * @param tipo
     */
    private void hilos_sens(int i, int tipo) {

	// ////////////////
	// El sensor va acoplado a una bt2
	if (sensores.get(i).getTipo_placa().contains("BT")) {

	    // Le asigno el puerto serie y el num de placa
	    if (IrrisoftConstantes.PLACA_1 == sensores.get(i).getNum_placa()) {
		if (serie5 == null) {
		    nuevoSerie(getSemaforobt2(), serie5,
			    Irrisoft.config.getBt2(), 5);
		    activabt(serie5);
		}
		sensores.get(i).setSerial(serie5);
		sensores.get(i).setNum_placa(5);
		sensbt2 = true;

	    } else if (IrrisoftConstantes.PLACA_2 == sensores.get(i)
		    .getNum_placa()) {
		if (serie6 == null) {
		    nuevoSerie(getSemaforobt22(), serie6,
			    Irrisoft.config.getBt22(), 6);
		    activabt(serie6);
		}
		sensores.get(i).setSerial(serie6);
		sensores.get(i).setNum_placa(6);
		sensbt22 = true;

	    } else if (IrrisoftConstantes.PLACA_3 == sensores.get(i)
		    .getNum_placa()) {

		if (serie7 == null) {
		    nuevoSerie(getSemaforobt23(), serie7,
			    Irrisoft.config.getBt23(), 7);
		    activabt(serie7);
		}
		sensores.get(i).setSerial(serie7);
		sensores.get(i).setNum_placa(7);
		sensbt23 = true;

	    } else if (IrrisoftConstantes.PLACA_4 == sensores.get(i)
		    .getNum_placa()) {

		if (serie8 == null) {
		    nuevoSerie(getSemaforobt24(), serie8,
			    Irrisoft.config.getBt24(), 8);
		    activabt(serie8);
		}
		sensores.get(i).setSerial(serie8);
		sensores.get(i).setNum_placa(8);
		sensbt24 = true;
	    }


	    // window.panelecturasbt2 = Panelecturasbt2.getInstance();


	    // Contador de Pulsos
	    if (IrrisoftConstantes.SENSOR_CAUDALIMETRO == tipo) {
		logger.warn("Creo HiloCaudalimetro de BT, placa: " +sensores.get(i).getNum_placa());

		hilocau = new HiloCaudalimetro(sensores.get(i).getSerial(),
			sensores.get(i).getNum_placa(), 0, sensores.get(i));

		//hilocau.setMci(false);
		sensores.get(i).setInstancia(hilocau);
//		Thread th = new Thread(hilocau);
//		th.setName("Caudalímetro " + th.getId());
//		sensores.get(i).setHilosens(th);
		sensores.get(i).setInstancia(hilocau);
		sensores.get(i).setHilosens(new Thread(hilocau));

		sensores.get(i).getHilosens().setName("Hilo Caudalimetro: "+sensores.get(i).getHilosens().getId());
		// Añado el listener para el que en el rearme me deje morir el hilo.
		Irrisoft.window.volcado.con.addPropertyChangeListener("false",
			(HiloCaudalimetro) sensores.get(i).getInstancia());
		sensores.get(i).getHilosens().start();

	    }
	    // Humedad
	    else if (IrrisoftConstantes.SENSOR_HIGROMETRO == tipo) {
		logger.warn("Creo HiloHumedad de BT, placa: " +sensores.get(i).getNum_placa());

		// meto la valvula propietaria a la lista
		sensores.get(i).getValvsassoc().add(Integer.parseInt(sensores.get(i).getNum_est_prop()));

		if (sensores.get(i).getNum_est_asoc() != null) {
		    // Tokenizo las valvulas asociadas
		    StringTokenizer token = new StringTokenizer(sensores.get(i)
			    .getNum_est_asoc(), ",");
		    while (token.hasMoreTokens()) {
			sensores.get(i).getValvsassoc()
				.add(Integer.parseInt(token.nextToken()));
		    }
		}
		// Creo el hilo
		HiloHumedadSuelo hilohum = new HiloHumedadSuelo(sensores.get(i)
			.getSerial(),sensores.get(i));
		sensores.get(i).setInstancia(hilohum);
		Thread th = new Thread(hilohum);
		th.setName("Higrómetro " + th.getId());
		sensores.get(i).setHilosens(th);
		// Añado el listener para el que en el rearme me deje morir el
		// hilo
		Irrisoft.window.volcado.con.addPropertyChangeListener("false",
			(HiloHumedadSuelo) sensores.get(i).getInstancia());
		sensores.get(i).getHilosens().start();

	    }

	    // Amperímetro
	    else if (IrrisoftConstantes.SENSOR_AMPERIMETRO == tipo) {
		logger.warn("Creo HiloAmpeimetro de BT, placa: " +sensores.get(i).getNum_placa());

		HiloAmperimetro hiloamp = new HiloAmperimetro(sensores.get(i)
			.getSerial(), sensores.get(i), sensores.get(i)
			.getNum_placa());
		hiloamp.setMci(false);
		sensores.get(i).setInstancia(hiloamp);
		Thread th = new Thread(hiloamp);
		th.setName("Amperímetro " + th.getId());
		sensores.get(i).setHilosens(th);
		// Añado el listener para el que en el rearme me deje morir el
		// hilo
		Irrisoft.window.volcado.con.addPropertyChangeListener("false",
			(HiloAmperimetro) sensores.get(i).getInstancia());
		sensores.get(i).getHilosens().start();

	    }

	}

	// ////////////
	// El sensor va acoplado a una placa de sensores
	else if (sensores.get(i).getTipo_placa()
		.contains(IrrisoftConstantes.PLACA_TIPO_SENSORES)) {

	    hayplacasens = true;

	    if (window.seriesens == null) {
		nuevoSerie(getSemaforosens(), seriesens,
			Irrisoft.config.getSens(), 0);
		window.series.add(window.seriesens);
	    }

	    sensores.get(i).setSerial(seriesens);
	    sensores.get(i).setNum_placa(0);


	    // window.panelecturasens = Panelecturasens.getInstance();


	    // Caudalimetro
	    if (IrrisoftConstantes.SENSOR_CAUDALIMETRO == tipo) {
		logger.warn("Creo HiloCaudalimetro de Placa Sensores.");

		hilocau = new HiloCaudalimetro(sensores.get(i).getSerial(),
			sensores.get(i).getNum_placa(), 0, sensores.get(i));
		//hilocau.setMci(false);
		sensores.get(i).setInstancia(hilocau);
		Thread th = new Thread(hilocau);
		th.setName("Caudalímetro " + th.getId());
		sensores.get(i).setHilosens(th);
		// Añado el listener para el que en el rearme me deje morir el
		// hilo
		Irrisoft.window.volcado.con.addPropertyChangeListener("false",
			(HiloCaudalimetro) sensores.get(i).getInstancia());
		sensores.get(i).getHilosens().start();

	    }
	    // Humedad
	    else if (IrrisoftConstantes.SENSOR_HIGROMETRO == tipo) {
		logger.warn("Creo HiloHigrometro de Placa Sensores.");

		// meto la valvula propietaria a la lista
		sensores.get(i)
			.getValvsassoc()
			.add(Integer
				.parseInt(sensores.get(i).getNum_est_prop()));

		if (sensores.get(i).getNum_est_asoc() != null) {
		    // Tokenizo las valvulas asociadas
		    StringTokenizer token = new StringTokenizer(sensores.get(i)
			    .getNum_est_asoc(), ",");
		    while (token.hasMoreTokens()) {
			sensores.get(i).getValvsassoc()
				.add(Integer.parseInt(token.nextToken()));
		    }
		}
		// Creo el hilo
		HiloHumedadSuelo hilohum = new HiloHumedadSuelo(sensores.get(i)
			.getSerial(),sensores.get(i));
		sensores.get(i).setInstancia(hilohum);
		Thread th = new Thread(hilohum);
		th.setName("Higrómetro " + th.getId());
		sensores.get(i).setHilosens(th);
		// Añado el listener para el que en el rearme me deje morir el
		// hilo
		Irrisoft.window.volcado.con.addPropertyChangeListener("false",
			(HiloHumedadSuelo) sensores.get(i).getInstancia());
		sensores.get(i).getHilosens().start();

	    }
	    // Amperimetro
	    else if (IrrisoftConstantes.SENSOR_AMPERIMETRO == tipo) {
		logger.warn("Creo HiloAmperimetro de Placa Sensores.");
		// hayampplacasensores = true;
		HiloAmperimetro hiloamp = new HiloAmperimetro(sensores.get(i)
			.getSerial(), sensores.get(i), sensores.get(i)
			.getNum_placa());
		hiloamp.setMci(true);
		sensores.get(i).setInstancia(hiloamp);
		Thread th = new Thread(hiloamp);
		th.setName("Amperímetro " + th.getId());
		sensores.get(i).setHilosens(th);
		// Añado el listener para el que en el rearme me deje morir el
		// hilo
		Irrisoft.window.volcado.con.addPropertyChangeListener("false",
			(HiloAmperimetro) sensores.get(i).getInstancia());
		sensores.get(i).getHilosens().start();

	    }
	    // Pluviometro
	    else if (IrrisoftConstantes.SENSOR_PLUVIOMETRO == tipo) {
		logger.warn("Creo HiloPluviometro de Placa de Sensores.");

		HiloPluviometro hiloplu = new HiloPluviometro(sensores.get(i).getSerial(), sensores.get(i));

		sensores.get(i).setInstancia(hiloplu);
		Thread th = new Thread(hiloplu);
		th.setName("Pluviómetro" + th.getId());
		sensores.get(i).setHilosens(th);
		// Añado el listener para el que en el rearme me deje morir el
		// hilo
		Irrisoft.window.volcado.con.addPropertyChangeListener("false",
			(HiloPluviometro) sensores.get(i).getInstancia());
		// Añado el listener para saber cuando tengo conexion a BBDD
		// Remota.
		Irrisoft.window.volcado.con.addPropertyChangeListener("true",
			(HiloPluviometro) sensores.get(i).getInstancia());
		sensores.get(i).getHilosens().start();

	    }
	    // Temperatura
	    else if (IrrisoftConstantes.SENSOR_TERMOMETRO == tipo) {
		logger.warn("Creo HiloTemperatura de Placa Sensores.");

		HiloTemperatura hilotemp = new HiloTemperatura(sensores.get(i)
			.getSerial(), sensores.get(i));
		sensores.get(i).setInstancia(hilotemp);
		Thread th = new Thread(hilotemp);
		th.setName("Termómetro " + th.getId());
		sensores.get(i).setHilosens(th);
		// Añado el listener para el que en el rearme me deje morir el
		// hilo

		Irrisoft.window.volcado.con.addPropertyChangeListener("false",(HiloTemperatura) sensores.get(i).getInstancia());
		//Añado el listener para saber cuando tengo conexion a BBDD Remota.
		Irrisoft.window.volcado.con.addPropertyChangeListener("true", (HiloTemperatura) sensores.get(i).getInstancia());

		sensores.get(i).getHilosens().start();

	    }
	    // Anemometro
	    else if (IrrisoftConstantes.SENSOR_ANEMOMETRO == tipo) {
		logger.warn("Creo HiloAnemometro de Placa Sensores.");

		HiloAnemometro hiloane = new HiloAnemometro(sensores.get(i)
			.getSerial(), sensores.get(i));
		sensores.get(i).setInstancia(hiloane);
		Thread th = new Thread(hiloane);
		th.setName("Anemómetro " + th.getId());
		sensores.get(i).setHilosens(th);
		// Añado el listener para el que en el rearme me deje morir el
		// hilo.
		Irrisoft.window.volcado.con.addPropertyChangeListener("false",
			(HiloAnemometro) sensores.get(i).getInstancia());
		// Añado el listener para saber cuando tengo conexion a BBDD
		// Remota.
		Irrisoft.window.volcado.con.addPropertyChangeListener("true",
			(HiloAnemometro) sensores.get(i).getInstancia());
		sensores.get(i).getHilosens().start();

	    }
	    // Flujo
	    else if (IrrisoftConstantes.SENSOR_FLUJO == tipo) {

		// TODO

	    }
	    // Intrusión
	    else if (IrrisoftConstantes.SENSOR_INTRUSION == tipo) {

		// TODO

	    }
	}

	// /////////////
	// El sensor va acoplado directamente a la raspberry por gpio
	else if (sensores.get(i).getTipo_placa()
		.contains(IrrisoftConstantes.PLACA_TIPO_RPI)) {

	    // Intrusión
	    if (IrrisoftConstantes.SENSOR_INTRUSION == tipo) {

	    }

	}

	// ////////////////
	// El sensor va acoplado a una controladora de relés
	else if (sensores.get(i).getTipo_placa()
		.contains(IrrisoftConstantes.PLACA_TIPO_CONTROLADORA)) {

	    // window.panelecturasmci = Panelecturasmci.getInstance();

	    // Le asigno el puerto serie y el num de placa
	    if (IrrisoftConstantes.PLACA_1 == sensores.get(i).getNum_placa()) {
		if (serie1 == null)
		    nuevoSerie(getSemaforomci(), serie1,
			    Irrisoft.config.getMci(), 1);
		sensores.get(i).setSerial(serie1);
		sensmci = true;

	    } else if (IrrisoftConstantes.PLACA_2 == sensores.get(i)
		    .getNum_placa()) {
		if (serie2 == null)
		    nuevoSerie(getSemaforomci2(), serie2,
			    Irrisoft.config.getMci2(), 2);
		sensores.get(i).setSerial(serie2);
		sensmci2 = true;

	    } else if (IrrisoftConstantes.PLACA_3 == sensores.get(i)
		    .getNum_placa()) {
		if (serie3 == null)
		    nuevoSerie(getSemaforomci3(), serie3,
			    Irrisoft.config.getMci3(), 3);
		sensores.get(i).setSerial(serie3);
		sensmci3 = true;
	    } else if (IrrisoftConstantes.PLACA_4 == sensores.get(i)
		    .getNum_placa()) {
		if (serie4 == null)
		    nuevoSerie(getSemaforomci4(), serie4,
			    Irrisoft.config.getMci4(), 4);
		sensores.get(i).setSerial(serie4);
		sensmci4 = true;
	    }

	    // Caudalimetro
	    if (IrrisoftConstantes.SENSOR_CAUDALIMETRO == tipo) {

		logger.warn("Creo HiloCaudalimetro de Placa Controladora, placa: " +sensores.get(i).getNum_placa());

		hilocau = new HiloCaudalimetro(sensores.get(i).getSerial(),
			sensores.get(i).getNum_placa(), 0, sensores.get(i));
		//hilocau.setMci(false);
		sensores.get(i).setInstancia(hilocau);
		Thread th = new Thread(hilocau);
		th.setName("Caudalímetro " + th.getId());
		sensores.get(i).setHilosens(th);
		// Añado el listener para el que en el rearme me deje morir el
		// hilo
		Irrisoft.window.volcado.con.addPropertyChangeListener("false",
			(HiloCaudalimetro) sensores.get(i).getInstancia());
		sensores.get(i).getHilosens().start();

	    }

	    // Humedad
	    else if (IrrisoftConstantes.SENSOR_HIGROMETRO == tipo) {
		logger.warn("Creo HiloHumedad de Placa Controladora, placa: " +sensores.get(i).getNum_placa());

		// //meto la valvula propietaria a la lista
		sensores.get(i)
			.getValvsassoc()
			.add(Integer
				.parseInt(sensores.get(i).getNum_est_prop()));

		if (sensores.get(i).getNum_est_asoc() != null) {
		    // Tokenizo las valvulas asociadas
		    StringTokenizer token = new StringTokenizer(sensores.get(i)
			    .getNum_est_asoc(), ",");
		    while (token.hasMoreTokens()) {
			sensores.get(i).getValvsassoc()
				.add(Integer.parseInt(token.nextToken()));
		    }
		}

		// Creo el hilo
		HiloHumedadSuelo hilohum = new HiloHumedadSuelo(sensores.get(i)
			.getSerial(),sensores.get(i));
		sensores.get(i).setInstancia(hilohum);
		Thread th = new Thread(hilohum);
		th.setName("Higrometro " + th.getId());
		sensores.get(i).setHilosens(th);
		// Añado el listener para el que en el rearme me deje morir el
		// hilo
		Irrisoft.window.volcado.con.addPropertyChangeListener("false",
			(HiloHumedadSuelo) sensores.get(i).getInstancia());
		sensores.get(i).getHilosens().start();

	    }

	    // Amperimetro
	    else if (IrrisoftConstantes.SENSOR_AMPERIMETRO == tipo) {
		logger.warn("Creo HiloAmperimetro de Placa Controladora, placa: " +sensores.get(i).getNum_placa());

		HiloAmperimetro hiloamp = new HiloAmperimetro(sensores.get(i)
			.getSerial(), sensores.get(i), sensores.get(i)
			.getNum_placa());
		hiloamp.setMci(true);
		sensores.get(i).setInstancia(hiloamp);

		Thread th=new Thread(hiloamp);
		th.setName("Amperímetro "+ th.getId());

		// Este es el listener del sensor.
		// hiloamp.addPropertyChangeListener("lectura",panelecturasmci);
		sensores.get(i).setHilosens(th);
		// Añado el listener para el que en el rearme me deje morir el
		// hilo
		Irrisoft.window.volcado.con.addPropertyChangeListener("false",
			(HiloAmperimetro) sensores.get(i).getInstancia());
		sensores.get(i).getHilosens().start();

	    }

	    // Pluviometro
	    else if (IrrisoftConstantes.SENSOR_PLUVIOMETRO == tipo) {
		logger.warn("Creo HiloPluviometro de Placa Controladora, placa: " +sensores.get(i).getNum_placa());

		HiloPluviometro hiloplu = new HiloPluviometro(sensores.get(i)
			.getSerial(), sensores.get(i));
		sensores.get(i).setInstancia(hiloplu);
		Thread th = new Thread(hiloplu);
		th.setName("Pluviometro " + th.getId());
		sensores.get(i).setHilosens(th);
		// Añado el listener para el que en el rearme me deje morir el
		// hilo
		Irrisoft.window.volcado.con.addPropertyChangeListener("false",
			(HiloPluviometro) sensores.get(i).getInstancia());
		// Añado el listener para controlar la conexion Remota
		Irrisoft.window.volcado.con.addPropertyChangeListener("true",
			(HiloPluviometro) sensores.get(i).getInstancia());
		sensores.get(i).getHilosens().start();

	    }
	    // Temperatura
	    else if (IrrisoftConstantes.SENSOR_TERMOMETRO == tipo) {
		logger.warn("Creo HiloTemperatura de Placa Controladora, placa: " +sensores.get(i).getNum_placa());

		HiloTemperatura hilotemp = new HiloTemperatura(sensores.get(i)
			.getSerial(), sensores.get(i));
		sensores.get(i).setInstancia(hilotemp);
		Thread th = new Thread(hilotemp);
		th.setName("Amperímetro " + th.getId());
		sensores.get(i).setHilosens(th);
		// Añado el listener para el que en el rearme me deje morir el
		// hilo
		Irrisoft.window.volcado.con.addPropertyChangeListener("false",
			(HiloTemperatura) sensores.get(i).getInstancia());
		// Añado el listener para controlar la conexion Remota
		Irrisoft.window.volcado.con.addPropertyChangeListener("true",
			(HiloTemperatura) sensores.get(i).getInstancia());
		sensores.get(i).getHilosens().start();

	    }
	    // Anemometro
	    else if (IrrisoftConstantes.SENSOR_ANEMOMETRO == tipo) {
		logger.warn("Creo HiloAnemometro de Placa Controladora, placa: " +sensores.get(i).getNum_placa());

		HiloAnemometro hiloane = new HiloAnemometro(sensores.get(i)
			.getSerial(), sensores.get(i));
		sensores.get(i).setInstancia(hiloane);
		Thread th = new Thread(hiloane);
		th.setName("Anemómetro " + th.getId());
		sensores.get(i).setHilosens(th);
		// Añado el listener para el que en el rearme me deje morir el
		// hilo
		Irrisoft.window.volcado.con.addPropertyChangeListener("false",
			(HiloAnemometro) sensores.get(i).getInstancia());
		// Añado el listener para controlar la conexion Remota
		Irrisoft.window.volcado.con.addPropertyChangeListener("true",
			(HiloAnemometro) sensores.get(i).getInstancia());
		sensores.get(i).getHilosens().start();

	    }
	    // Flujo
	    else if (IrrisoftConstantes.SENSOR_FLUJO == tipo) {
		// TODO
	    }
	    // Intrusión
	    else if (IrrisoftConstantes.SENSOR_INTRUSION == tipo) {
		// TODO
	    }

	}

    }

    /**
     * Recorro las listas de valvulas para saber cuales están abiertas en la
     * invocación !!
     * 
     * @param valv
     * @param tipo
     * @return
     */
    public synchronized int addvalvsabiertas(Valvula valv, int tipo) {

	int abiertas = 0;
	
	
	if (IrrisoftConstantes.PLACA_MCI_1 == tipo) {
	    Irrisoft.window.valvsabiertasmci.add(valv);
	    Irrisoft.window.valvsabiertastot.add(valv);
	    abiertas = Irrisoft.window.valvsabiertasmci.size();
	} else if (IrrisoftConstantes.PLACA_MCI_2 == tipo) {
	    Irrisoft.window.valvsabiertasmci2.add(valv);
	    Irrisoft.window.valvsabiertastot.add(valv);
	    abiertas = Irrisoft.window.valvsabiertasmci2.size();
	} else if (IrrisoftConstantes.PLACA_MCI_3 == tipo) {
	    Irrisoft.window.valvsabiertasmci3.add(valv);
	    Irrisoft.window.valvsabiertastot.add(valv);
	    abiertas = Irrisoft.window.valvsabiertasmci3.size();
	} else if (IrrisoftConstantes.PLACA_MCI_4 == tipo) {
	    Irrisoft.window.valvsabiertasmci4.add(valv);
	    Irrisoft.window.valvsabiertastot.add(valv);
	    abiertas = Irrisoft.window.valvsabiertasmci4.size();
	} else if (IrrisoftConstantes.PLACA_BT2_5 == tipo) {
	    Irrisoft.window.valvsabiertasbt2.add(valv);
	    Irrisoft.window.valvsabiertastot.add(valv);
	    abiertas = Irrisoft.window.valvsabiertasbt2.size();
	} else if (IrrisoftConstantes.PLACA_BT2_6 == tipo) {
	    Irrisoft.window.valvsabiertasbt22.add(valv);
	    Irrisoft.window.valvsabiertastot.add(valv);
	    abiertas = Irrisoft.window.valvsabiertasbt22.size();
	} else if (IrrisoftConstantes.PLACA_BT2_7 == tipo) {
	    Irrisoft.window.valvsabiertasbt23.add(valv);
	    Irrisoft.window.valvsabiertastot.add(valv);
	    abiertas = Irrisoft.window.valvsabiertasbt23.size();
	} else if (IrrisoftConstantes.PLACA_BT2_8 == tipo) {
	    Irrisoft.window.valvsabiertasbt24.add(valv);
	    Irrisoft.window.valvsabiertastot.add(valv);
	    abiertas = Irrisoft.window.valvsabiertasbt24.size();
	} else if (IrrisoftConstantes.PLACA_SAMCLA == tipo) {
	    Irrisoft.window.valvsabiertasamcla.add(valv);
	    Irrisoft.window.valvsabiertastot.add(valv);
	    abiertas = Irrisoft.window.valvsabiertasamcla.size();
	}
	
	if (logger.isInfoEnabled()) {
	    logger.info("AÑADO valvula a listavalvsabiertastot: " + valv.getCodelecvalv() + " , size: "+ Irrisoft.window.valvsabiertastot.size()+", tipo: "+tipo);
	}

	
	return abiertas;

    }

    /**
     * Recorro cada lista de valvulas, para saber cuantas valvulas estan
     * abiertas.
     * 
     * @param tipo
     * @return
     */
    public synchronized int valvsabiertas(int tipo) {

	int abiertas = 0;

	if (IrrisoftConstantes.VALVS_ABIERTAS_TOT == tipo) {
	    abiertas = Irrisoft.window.valvsabiertastot.size();
	} else if (IrrisoftConstantes.PLACA_MCI_1 == tipo) {
	    abiertas = Irrisoft.window.valvsabiertasmci.size();
	} else if (IrrisoftConstantes.PLACA_MCI_2 == tipo) {
	    abiertas = Irrisoft.window.valvsabiertasmci2.size();
	} else if (IrrisoftConstantes.PLACA_MCI_3 == tipo) {
	    abiertas = Irrisoft.window.valvsabiertasmci3.size();
	} else if (IrrisoftConstantes.PLACA_MCI_4 == tipo) {
	    abiertas = Irrisoft.window.valvsabiertasmci4.size();
	} else if (IrrisoftConstantes.PLACA_BT2_5 == tipo) {
	    abiertas = Irrisoft.window.valvsabiertasbt2.size();
	} else if (IrrisoftConstantes.PLACA_BT2_6 == tipo) {
	    abiertas = Irrisoft.window.valvsabiertasbt22.size();
	} else if (IrrisoftConstantes.PLACA_BT2_7 == tipo) {
	    abiertas = Irrisoft.window.valvsabiertasbt23.size();
	} else if (IrrisoftConstantes.PLACA_BT2_8 == tipo) {
	    abiertas = Irrisoft.window.valvsabiertasbt24.size();
	} else if (IrrisoftConstantes.PLACA_SAMCLA == tipo) {
	    abiertas = Irrisoft.window.valvsabiertasamcla.size();
	}

	return abiertas;

    }

    /**
     * Me da una lista con las valvulas asociadas a una placa (tipo).
     * 
     * @param tipo
     * @return
     */
    public synchronized LinkedHashSet<Valvula> listavalvsabiertas(int tipo) {

	LinkedHashSet<Valvula> lista = new LinkedHashSet<Valvula>();

	if (IrrisoftConstantes.PLACA_MCI_1 == tipo) {
	    lista = Irrisoft.window.valvsabiertasmci;
	} else if (IrrisoftConstantes.PLACA_MCI_2 == tipo) {
	    lista = Irrisoft.window.valvsabiertasmci2;
	} else if (IrrisoftConstantes.PLACA_MCI_3 == tipo) {
	    lista = Irrisoft.window.valvsabiertasmci3;
	} else if (IrrisoftConstantes.PLACA_MCI_4 == tipo) {
	    lista = Irrisoft.window.valvsabiertasmci4;
	} else if (IrrisoftConstantes.PLACA_BT2_5 == tipo) {
	    lista = Irrisoft.window.valvsabiertasbt2;
	} else if (IrrisoftConstantes.PLACA_BT2_6 == tipo) {
	    lista = Irrisoft.window.valvsabiertasbt22;
	} else if (IrrisoftConstantes.PLACA_BT2_7 == tipo) {
	    lista = Irrisoft.window.valvsabiertasbt23;
	} else if (IrrisoftConstantes.PLACA_BT2_8 == tipo) {
	    lista = Irrisoft.window.valvsabiertasbt24;
	} else if (IrrisoftConstantes.PLACA_SAMCLA == tipo) {
	    lista = Irrisoft.window.valvsabiertasamcla;
	}

	return lista;

    }

    /**
     * Quito valvulas abiertas de la lista que le corresponda.
     * 
     * @param valv
     * @param tipo
     */
    public synchronized void quitarvalvabiertas(Valvula valv, int tipo) {

	

	if (IrrisoftConstantes.PLACA_MCI_1 == tipo) {
	    Irrisoft.window.valvsabiertasmci.remove(valv);
	    Irrisoft.window.valvsabiertastot.remove(valv);
	} else if (IrrisoftConstantes.PLACA_MCI_2 == tipo) {
	    Irrisoft.window.valvsabiertasmci2.remove(valv);
	    Irrisoft.window.valvsabiertastot.remove(valv);
	} else if (IrrisoftConstantes.PLACA_MCI_3 == tipo) {
	    Irrisoft.window.valvsabiertasmci3.remove(valv);
	    Irrisoft.window.valvsabiertastot.remove(valv);
	} else if (IrrisoftConstantes.PLACA_MCI_4 == tipo) {
	    Irrisoft.window.valvsabiertasmci4.remove(valv);
	    Irrisoft.window.valvsabiertastot.remove(valv);
	} else if (IrrisoftConstantes.PLACA_BT2_5 == tipo) {
	    Irrisoft.window.valvsabiertasbt2.remove(valv);
	    Irrisoft.window.valvsabiertastot.remove(valv);
	} else if (IrrisoftConstantes.PLACA_BT2_6 == tipo) {
	    Irrisoft.window.valvsabiertasbt22.remove(valv);
	    Irrisoft.window.valvsabiertastot.remove(valv);
	} else if (IrrisoftConstantes.PLACA_BT2_7 == tipo) {
	    Irrisoft.window.valvsabiertasbt23.remove(valv);
	    Irrisoft.window.valvsabiertastot.remove(valv);
	} else if (IrrisoftConstantes.PLACA_BT2_8 == tipo) {
	    Irrisoft.window.valvsabiertasbt24.remove(valv);
	    Irrisoft.window.valvsabiertastot.remove(valv);
	} else if (IrrisoftConstantes.PLACA_SAMCLA == tipo) {
	    Irrisoft.window.valvsabiertasamcla.remove(valv);
	    Irrisoft.window.valvsabiertastot.remove(valv);
	}

	
	if (logger.isInfoEnabled()) {
	    logger.info("QUITO valvula de listavalvsabiertastot: " + valv.getCodelecvalv() + " , "+ Irrisoft.window.valvsabiertastot.size());
	}
	
    }

    /**
     * Consigue el device number del puerto ACM
     */
    public void devicenumACM() {

	try {
	    String[] cmd = { "sh", "-c",
		    "dmesg |grep ttyACM* |cut -d ] -f 2 |cut -d U -f 1 |cut -d m -f 2" };
	    Process p = Runtime.getRuntime().exec(cmd);
	    BufferedReader bri = new BufferedReader(new InputStreamReader(
		    p.getInputStream()));
	    String tmpLine = "";
	    String s3 = "";

	    while ((tmpLine = bri.readLine()) != null) {

		StringTokenizer tokenizer = new StringTokenizer(tmpLine, " ");

		int i = 0;

		while (tokenizer.hasMoreTokens()) {

		    String s = tokenizer.nextToken().toString();

		    if (i == 0) {
			if (logger.isInfoEnabled()) {
			    logger.info("TOKEN devicenum: " + s + " " + i + " "
				    + tokenizer.countTokens());
			}
			if (!s.contentEquals(s3)) {

			    String puerto = tokenizer.nextToken().toString();
			    puerto = puerto.substring(0, puerto.length() - 1);

			    for (int j = 0; j < series.size(); j++) {

				if (series.get(j).serialPort.getPortName()
					.contains(puerto)) {
				    series.get(j).device = s;
				    if (logger.isInfoEnabled()) {
					logger.info("series device "
						+ series.get(j).device);
				    }
				    s3 = s;
				}
			    }

			    i++;

			}

		    }
		    i++;

		}
	    }
	} catch (IOException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	}

	// Flag inicializados los puertos
	window.iniports = true;
    }

    /**
     * Consigue el device number del puerto BT
     */
    public void devicenumBT() {

	try {
	    String[] cmd = { "sh", "-c",
		    "dmesg |grep ftdi* |cut -d ] -f 2 |cut -d U -f 1 |cut -d m -f 2" };
	    Process p = Runtime.getRuntime().exec(cmd);
	    BufferedReader bri = new BufferedReader(new InputStreamReader(
		    p.getInputStream()));
	    String tmpLine = "";
	    String s3 = "";

	    while ((tmpLine = bri.readLine()) != null) {

		StringTokenizer tokenizer = new StringTokenizer(tmpLine, " ");

		int i = 0;

		while (tokenizer.hasMoreTokens()) {

		    String s = tokenizer.nextToken().toString();

		    if (i == 0) {
			if (logger.isInfoEnabled()) {
			    logger.info("TOKEN devicenum: " + s + " " + i + " "
				    + tokenizer.countTokens());
			}

			if (!s.contentEquals(s3)) {

			    String puerto = tokenizer.nextToken().toString();
			    puerto = puerto.substring(0, puerto.length() - 1);

			    for (int j = 0; j < series.size(); j++) {

				if (series.get(j).serialPort.getPortName()
					.contains(puerto)) {
				    series.get(j).device = s;
				    if (logger.isInfoEnabled()) {
					logger.info("series device "
						+ series.get(j).device);
				    }
				    s3 = s;
				}
			    }

			    i++;

			}

		    }
		    i++;

		}
	    }
	} catch (IOException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	}

	// Flag inicializados los puertos
	window.iniports = true;
    }

    /**
     * Remapea los puertos ACM
     */
    public void remapeaACM() {

	StringBuffer buffer = new StringBuffer();
	// String sviejo="";
	// boolean ultima;

	try {

	    // ArrayList<String> ports = new ArrayList<String>();

	    String[] cmd = { "sh", "-c",
		    "dmesg |tail |grep ttyACM* |cut -d ] -f 2 |cut -d U -f 1 |cut -d m -f 2" };
	    Process p = Runtime.getRuntime().exec(cmd);
	    BufferedReader bri = new BufferedReader(new InputStreamReader(
		    p.getInputStream()));
	    String tmpLine = "";

	    while ((tmpLine = bri.readLine()) != null) {
		buffer.append(tmpLine + "\n");
	    }

	    for (int j = 0; j < series.size(); j++) {

		int index = buffer.lastIndexOf(series.get(j).device);

		if (index != -1) {

		    String linea = buffer.substring(index);

		    StringTokenizer tokenizador = new StringTokenizer(linea,
			    "\n");
		    String churro = tokenizador.nextToken("\n");

		    StringTokenizer tokenizador1 = new StringTokenizer(churro,
			    " ");

		    tokenizador1.nextToken();
		    String puerto = tokenizador1.nextToken();
		    puerto = puerto.substring(0, puerto.length() - 1);
		    puerto = "/dev/" + puerto;

		    if (!series.get(j).serialPort.getPortName().contentEquals(
			    puerto)
			    && (series.get(j).serialPort.isOpened())) {
			if (logger.isInfoEnabled()) {
			    logger.info("Remapeo puerto" + index);
			}
			series.get(j).serialPort.closePort();
			SerialPort serialPort = new SerialPort(puerto);
			series.get(j).setSerialPort(serialPort);
			series.get(j).conectaserial(series.get(j).tipo);
		    }
		}
	    }

	} catch (IOException | SerialPortException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	}

    }

    /**
     * Escribe los append a irrisoft.log
     * 
     * @param texto
     */

    public void escribelog(String texto) {

	Timestamp time = new Timestamp(System.currentTimeMillis());
	String tiempo = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
		.format(time);

	String comando = "echo '" + texto + tiempo + "' >> " + Irrisoft.home
		+ "/irrisoft.log";
	String[] cmd = { "sh", "-c", comando };

	try {

	    Runtime.getRuntime().exec(cmd);

	} catch (IOException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	}

    }

    /**
     * Escribe en el TextArea
     * 
     * @param texto
     * @param formato
     * @param interruptor
     */
    public void escribetextPane(String texto, SimpleAttributeSet formato,
	    boolean interruptor) {

	Timestamp time = new Timestamp(System.currentTimeMillis());
	String tiempo = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
		.format(time);

	try {
	    if (interruptor) {
		Irrisoft.window.textPane.getStyledDocument().insertString(
			Irrisoft.window.doc.getLength(), texto + tiempo,
			formato);

	    } else {
		Irrisoft.window.textPane.getStyledDocument().insertString(
			Irrisoft.window.doc.getLength(), texto, formato);
	    }

	} catch (BadLocationException e1) {
	    if (logger.isErrorEnabled()) {
		logger.error(e1.getMessage());
	    }
	}

    }

    /**
     * Activa la BT2
     * 
     * @param serial
     */
    public void activabt(SerialDriver serial) {

	serial.activabt(serial.serialPort, new Sensor());

    }

    /**
     * Empezar las conexiones con las BBDD (remota y local) desde el botón
     */
    public void abreDBhilo() {
	if (Irrisoft.window.hiloescucha.getConnDB().conectal()
		&& Irrisoft.window.volcado.getCon().conectar()
		&& Irrisoft.window.volcado.getCon().conectal()) {
	    Irrisoft.window.lblstatusl.setText("Conectado");
	    Irrisoft.window.btnEmpezar.setText("Parar BBDDs");
	}
    }

    /**
     * Parar las conexiones con las BBDD (remota y local) desde el botón
     */
    public void cierraDBhilo() {
	Irrisoft.window.hiloescucha.getConnDB().desconectal();
	Irrisoft.window.volcado.getCon().cierral();
	Irrisoft.window.volcado.getCon().cierrar();
	Irrisoft.window.lblstatusl.setText("Desconectado");
	Irrisoft.window.btnEmpezar.setText("Arrancar BBDDs");

    }

    public void nuevoSerie(Semaforo sem, SerialDriver serial, String puerto,
	    int tipo) {

	sem = new Semaforo(1);
	serial = new SerialDriver();
	SerialPort serialPort = new SerialPort(puerto);
	serial.setSerialPort(serialPort);

	// Asocio cada puerto y semaforo a su correspondiente placa

	if (IrrisoftConstantes.PLACA_MCI_1 == tipo) {
	    setSerie1(serial);
	    setSemaforomci(sem);
	} else if (IrrisoftConstantes.PLACA_MCI_2 == tipo) {
	    setSerie2(serial);
	    setSemaforomci2(sem);
	} else if (IrrisoftConstantes.PLACA_MCI_3 == tipo) {
	    setSerie3(serial);
	    setSemaforomci3(sem);
	} else if (IrrisoftConstantes.PLACA_MCI_4 == tipo) {
	    setSerie4(serial);
	    setSemaforomci4(sem);
	} else if (IrrisoftConstantes.PLACA_BT2_5 == tipo) {
	    setSerie5(serial);
	    setSemaforobt2(sem);
	} else if (IrrisoftConstantes.PLACA_BT2_6 == tipo) {
	    setSerie6(serial);
	    setSemaforobt22(sem);
	} else if (IrrisoftConstantes.PLACA_BT2_7 == tipo) {
	    setSerie7(serial);
	    setSemaforobt23(sem);
	} else if (IrrisoftConstantes.PLACA_BT2_8 == tipo) {
	    setSerie8(serial);
	    setSemaforobt24(sem);
	} else if (IrrisoftConstantes.PLACA_SENSORES_0 == tipo) {
	    setSeriesens(serial);
	    setSemaforosens(sem);
	} else if (IrrisoftConstantes.PLACA_SAMCLA == tipo) {
	    setSeriesamcla(serial);
	    setSemaforosamcla(sem);
	}
	serial.conectaserial(tipo);
	series.add(serial);

    }

    public void comprobarSerie() {
	// Desconecto el serie correspondiente para el rearme
	for (int i = 0; i < series.size(); i++) {
	    if (series.get(i).serialPort.isOpened()) {
		try {
		    series.get(i).serialPort.closePort();
		    logger.warn("Puerto cerrado: "
			    + series.get(i).serialPort.getPortName());
		} catch (SerialPortException e) {
		    if (logger.isErrorEnabled()) {
			logger.error("Error con el Puerto Serie: "
				+ e.getMessage());
		    }
		}
	    }
	}
    }

    synchronized public String getHex(byte[] bytes, boolean escribo) {
	StringBuilder result = new StringBuilder();
	int i = 0;
	for (byte b : bytes) {
	    if (b < 10)
		result.append(0);
	    result.append(b);

	    if (escribo) {
		if (i < bytes.length - 1)
		    result.append(".");
		i++;
	    }
	}
	return result.toString();
    }

    // /////////////////////////////////////
    // Para el menú copy/paste del textarea
    // ////////////////////////////////////
    public static class ContextMenuMouseListener extends MouseAdapter {
	private JPopupMenu popup = new JPopupMenu();

	private Action cutAction;
	private Action copyAction;
	private Action pasteAction;
	private Action undoAction;
	private Action selectAllAction;

	private JTextComponent textComponent;
	private String savedString = "";
	private Actions lastActionSelected;

	private enum Actions {
	    DESHACER, CORTAR, COPIAR, PEGAR, SELECCIONAR_TODO
	}

	public ContextMenuMouseListener() {
	    undoAction = new AbstractAction("Deshacer") {

		/**
				 * 
				 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent ae) {
		    textComponent.setText("");
		    textComponent.replaceSelection(savedString);

		    lastActionSelected = Actions.DESHACER;
		}
	    };

	    popup.add(undoAction);
	    popup.addSeparator();

	    cutAction = new AbstractAction("Cortar") {

		/**
				 * 
				 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent ae) {
		    lastActionSelected = Actions.CORTAR;
		    savedString = textComponent.getText();
		    textComponent.cut();
		}
	    };

	    popup.add(cutAction);

	    copyAction = new AbstractAction("Copiar") {

		/**
				 * 
				 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent ae) {
		    lastActionSelected = Actions.COPIAR;
		    textComponent.copy();
		}
	    };

	    popup.add(copyAction);

	    pasteAction = new AbstractAction("Pegar") {

		/**
				 * 
				 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent ae) {
		    lastActionSelected = Actions.PEGAR;
		    savedString = textComponent.getText();
		    textComponent.paste();
		}
	    };

	    popup.add(pasteAction);
	    popup.addSeparator();

	    selectAllAction = new AbstractAction("Seleccionar todo") {

		/**
				 * 
				 */
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent ae) {
		    lastActionSelected = Actions.SELECCIONAR_TODO;
		    textComponent.selectAll();
		}
	    };

	    popup.add(selectAllAction);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	    if (e.getModifiers() == InputEvent.BUTTON3_MASK) {
		if (!(e.getSource() instanceof JTextComponent)) {
		    return;
		}

		textComponent = (JTextComponent) e.getSource();
		textComponent.requestFocus();

		boolean enabled = textComponent.isEnabled();
		boolean editable = textComponent.isEditable();
		boolean nonempty = !(textComponent.getText() == null || textComponent
			.getText().equals(""));
		boolean marked = textComponent.getSelectedText() != null;

		boolean pasteAvailable = Toolkit.getDefaultToolkit()
			.getSystemClipboard().getContents(null)
			.isDataFlavorSupported(DataFlavor.stringFlavor);

		undoAction
			.setEnabled(enabled
				&& editable
				&& (lastActionSelected == Actions.CORTAR || lastActionSelected == Actions.PEGAR));
		cutAction.setEnabled(enabled && editable && marked);
		copyAction.setEnabled(enabled && marked);
		pasteAction.setEnabled(enabled && editable && pasteAvailable);
		selectAllAction.setEnabled(enabled && nonempty);

		int nx = e.getX();

		if (nx > 500) {
		    nx = nx - popup.getSize().width;
		}

		popup.show(e.getComponent(), nx, e.getY()
			- popup.getSize().height);
	    }
	}
    }

    // //////////////// GETTERS Y SETTERS

    public ConexionDB getConn() {
	return conn;
    }

    public void setConn(ConexionDB conn) {
	this.conn = conn;
    }

    public HiloCaudalimetro getHilocau() {
	return hilocau;
    }

    public void setHilocau(HiloCaudalimetro hilocau) {
	this.hilocau = hilocau;
    }

    public SerialDriver getSerie1() {
	return serie1;
    }

    public SerialDriver getSerie2() {
	return serie2;
    }

    public SerialDriver getSerie3() {
	return serie3;
    }

    public SerialDriver getSerie4() {
	return serie4;
    }

    public SerialDriver getSerie5() {
	return serie5;
    }

    public SerialDriver getSerie6() {
	return serie6;
    }

    public SerialDriver getSerie7() {
	return serie7;
    }

    public SerialDriver getSerie8() {
	return serie8;
    }

    public SerialDriver getSeriesens() {
	return seriesens;
    }

    public SerialDriver getSeriesamcla() {
	return seriesamcla;
    }

    public Semaforo getSemaforobt2() {
	return semaforobt2;
    }

    public void setSemaforobt2(Semaforo semaforobt2) {
	this.semaforobt2 = semaforobt2;
    }

    public Semaforo getSemaforobt22() {
	return semaforobt22;
    }

    public Semaforo getSemaforobt23() {
	return semaforobt23;
    }

    public Semaforo getSemaforobt24() {
	return semaforobt24;
    }

    public Semaforo getSemaforomci() {
	return semaforomci;
    }

    public Semaforo getSemaforomci2() {
	return semaforomci2;
    }

    public Semaforo getSemaforomci3() {
	return semaforomci3;
    }

    public Semaforo getSemaforomci4() {
	return semaforomci4;
    }

    public Semaforo getSemaforosens() {
	return semaforosens;
    }

    public Semaforo getSemaforosamcla() {
	return semaforosamcla;
    }

    public void setSerie1(SerialDriver serie1) {
	this.serie1 = serie1;
    }

    public void setSerie2(SerialDriver serie2) {
	this.serie2 = serie2;
    }

    public void setSerie3(SerialDriver serie3) {
	this.serie3 = serie3;
    }

    public void setSerie4(SerialDriver serie4) {
	this.serie4 = serie4;
    }

    public void setSerie5(SerialDriver serie5) {
	this.serie5 = serie5;
    }

    public void setSerie6(SerialDriver serie6) {
	this.serie6 = serie6;
    }

    public void setSerie7(SerialDriver serie7) {
	this.serie7 = serie7;
    }

    public void setSerie8(SerialDriver serie8) {
	this.serie8 = serie8;
    }

    public void setSeriesens(SerialDriver seriesens) {
	this.seriesens = seriesens;
    }

    public void setSeriesamcla(SerialDriver seriesamcla) {
	this.seriesamcla = seriesamcla;
    }

    public void setSemaforobt22(Semaforo semaforobt22) {
	this.semaforobt22 = semaforobt22;
    }

    public void setSemaforobt23(Semaforo semaforobt23) {
	this.semaforobt23 = semaforobt23;
    }

    public void setSemaforobt24(Semaforo semaforobt24) {
	this.semaforobt24 = semaforobt24;
    }

    public void setSemaforomci(Semaforo semaforomci) {
	this.semaforomci = semaforomci;
    }

    public void setSemaforomci2(Semaforo semaforomci2) {
	this.semaforomci2 = semaforomci2;
    }

    public void setSemaforomci3(Semaforo semaforomci3) {
	this.semaforomci3 = semaforomci3;
    }

    public void setSemaforomci4(Semaforo semaforomci4) {
	this.semaforomci4 = semaforomci4;
    }

    public void setSemaforosens(Semaforo semaforosens) {
	this.semaforosens = semaforosens;
    }

    public void setSemaforosamcla(Semaforo semaforo) {
	this.semaforosamcla = semaforo;
    }

}