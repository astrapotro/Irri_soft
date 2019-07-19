package panelespack;

import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;
import irrisoftpack.SerialDriver;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import alertaspack.GestorAlertas;

import valvulaspack.Valvula;

public class PanelSamcla extends JPanel {

    private static Logger logger = LogManager.getLogger(PanelSamcla.class
	    .getName());
    private static final long serialVersionUID = 1L;

    // SINGLETON
    private static PanelSamcla instance;

    private JTextField numvalv;
    public JLabel lblver, bateria, respuesta, serie;
    protected SerialDriver conserie;
    private JButton btnAbrir, btnCerrar;
    private boolean retonno;
    private Irrisoft IR;

    private GestorAlertas gestorAlertas;
    private JTextField dura;

    
    public static PanelSamcla getInstance() {

	if (instance == null) {
	    return new PanelSamcla();
	}
	return instance;
    }

    private PanelSamcla() {
	super();

	gestorAlertas = GestorAlertas.getInstance();

	this.IR = Irrisoft.window;


	this.setBounds(10, 84, 465, 344);
	setLayout(null);

	JLabel imgsamcla = new JLabel("");
	String ruta = IrrisoftConstantes.IMG_SAMCLA;
	imgsamcla.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 14));
	imgsamcla.setHorizontalAlignment(SwingConstants.CENTER);
	imgsamcla.setBounds(160, 14, 146, 37);
	IR.redimensionado(imgsamcla, ruta);
	add(imgsamcla);

	lblver = new JLabel("");
	lblver.setFont(new Font("Dialog", Font.PLAIN, 9));
	lblver.setBounds(349, 11, 108, 15);
	add(lblver);

	// BOTON ATRAS
	JButton btnAtras = new JButton("Atras");
	btnAtras.setBounds(178, 307, 117, 25);
	btnAtras.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		PanelSamcla.this.setVisible(false);
		IR.panelpral.setVisible(true);
	    }
	});
	add(btnAtras);

	JLabel lblestacion = new JLabel("Estación : ");
	lblestacion.setBounds(143, 84, 75, 19);
	add(lblestacion);

	numvalv = new JTextField();
	numvalv.setBounds(223, 84, 123, 23);
	add(numvalv);
	numvalv.setColumns(10);

	// filtro por si el usuario mete un numvalv que no corresponde. A
	// REVISAR !!
	filtro();

	btnAbrir = new JButton("Abrir");
	btnAbrir.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {

		try {
		    filtro();
		    rango();
		    Thread hilo_samcla = new hilo_interruptor_samcla(true,
			    IR.valvsamcla.getvalvsamcla(numvalv.getText()),
			    dura.getText());
		    hilo_samcla.setName("Hilo_Samcla_Manual_on");
		    hilo_samcla.start();

		} catch (NumberFormatException e) {
		    if (logger.isErrorEnabled()) {
			logger.error(e.getMessage());
		    }
		}
	    }

	});
	btnAbrir.setIcon(null);
	btnAbrir.setBounds(109, 168, 108, 25);
	add(btnAbrir);

	btnCerrar = new JButton("Cerrar");
	btnCerrar.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent e) {

		try {
		    filtro();
		    rango();
		    Thread hilo_samcla = new hilo_interruptor_samcla(false,
			    IR.valvsamcla.getvalvsamcla(numvalv.getText()),
			    dura.getText());
		    hilo_samcla.setName("Hilo_Samcla_Manual_off");
		    hilo_samcla.start();

		} catch (NumberFormatException e1) {
		    if (logger.isErrorEnabled()) {
			logger.error(e1.getMessage());
		    }
		}

	    }
	});
	btnCerrar.setBounds(238, 168, 108, 25);
	add(btnCerrar);

	JLabel duracion = new JLabel("Duración (HH:MM) : ");
	duracion.setBounds(79, 127, 139, 15);
	add(duracion);

	dura = new JTextField();
	dura.setColumns(10);
	dura.setBounds(223, 123, 123, 25);
	add(dura);

	bateria = new JLabel("");
	bateria.setHorizontalAlignment(SwingConstants.CENTER);
	bateria.setFont(new Font("Dialog", Font.PLAIN, 12));
	bateria.setBounds(23, 269, 415, 20);
	add(bateria);

	respuesta = new JLabel("");
	respuesta.setHorizontalAlignment(SwingConstants.CENTER);
	respuesta.setFont(new Font("Dialog", Font.PLAIN, 12));
	respuesta.setBounds(23, 243, 415, 15);
	add(respuesta);

	serie = new JLabel("");
	serie.setHorizontalAlignment(SwingConstants.CENTER);
	serie.setFont(new Font("Dialog", Font.PLAIN, 12));
	serie.setBounds(23, 216, 415, 15);
	add(serie);

    }

    protected boolean filtro() {

	numvalv.addKeyListener(new KeyAdapter() {
	    @Override
	    public void keyTyped(KeyEvent e) {

		char caracter = e.getKeyChar();

		try {
		    // Enter
		    if ((int) caracter == 10) {

		    } else {
			// Verificar si la tecla pulsada no es un digito
			if (((caracter < '0') || (caracter > '9'))
				&& (caracter != '\b' /*
						      * corresponde a BACK_SPACE
						      */)) {
			    e.consume(); // ignora el evento de teclado
			    aviso();
			    numvalv.setText(null);

			}

		    }
		} catch (NumberFormatException e1) {

		}

	    }
	});

	return retonno;

    }

    protected boolean rango() {

	boolean retonno = false;

	if (numvalv.getText() == "") {
	    try {
		int z = Integer.parseInt(numvalv.getText());
		if (logger.isInfoEnabled()) {
		    logger.info("A ver rangor: " + z);
		}

		if (z < 9001 || z > 9999) {
		    aviso();
		    numvalv.setText(null);
		    retonno = false;
		} else
		    retonno = true;

	    } catch (NumberFormatException e1) {
		if (logger.isErrorEnabled()) {
		    logger.error(e1.getMessage());
		}
	    }
	}
	return retonno;
    }

    protected void aviso() {

	JOptionPane.showMessageDialog(IR.frmIrrisoft,
		"Introduzca un numero entre 9001 y 9999 (ambos incluidos)", //$NON-NLS-1$
		"Error", JOptionPane.ERROR_MESSAGE);

    }

    // SubClase hilo para cuando se da al botón
    public class hilo_interruptor_samcla extends Thread {

	boolean accion;
	Valvula valv;
	String dura;

	public hilo_interruptor_samcla(boolean accion, Valvula valv, String dura) {
	    this.accion = accion;
	    this.valv = valv;
	    this.dura = dura;
	}

	public void run() {

	    interruptor(accion, valv, dura,false);

	}

    }

    public boolean interruptor(boolean abrir, Valvula valv, String dura, boolean tareaprog) {

	if (valv.getCodelecvalv() == null
		|| Integer.parseInt(valv.getCodelecvalv()) == -1) {
	    JOptionPane.showMessageDialog(IR.frmIrrisoft, "La estación "
		    + numvalv.getText()
		    + " no existe !\nPruebe a abrir una que exista.\n",
		    "Error", JOptionPane.ERROR_MESSAGE);
	    numvalv.setText("");
	    return false;

	}

	boolean fallo = false;

	// int abiertas = 0;

	if (logger.isInfoEnabled()) {
	    logger.info("Valvula: " + valv.getCodelecvalv());
	}


	// Si es abrir
	if (abrir) {
	    
	    cogesemaforo(true);
	    
	    // Saco la duración de lo que tengo que dormir
	    String[] duracion = dura.split(":");
	    int dormir;
	    try {
		dormir = (Integer.parseInt(duracion[0]) * 3600)
			+ (Integer.parseInt(duracion[1]) * 60);
	    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e1) {
		respuesta.setText("La electroválvula " + valv.getCodelecvalv()
			+ " no se ha podido abrir.");
		bateria.setText("Error! Revisad el formato de la duración (HH:MM) ?");
		// devuelvo error
		return true;
	    }

	    if (!valv.isAbierta()) {

		// abiertas = 0;

		// Abro la maestra
		if (IR.haycaudalimetro) {
		    if (!IR.test)
			IR.panelbt2.abremaestra();
		} else
		    IR.panelbt2.abremaestra();

		fallo = samcla(true, valv.getNumserie(), dura, valv);
		// Salgo si ha fallado
		if (fallo){
		    cogesemaforo(false);
		    return fallo;
		}

		if (!fallo && valv.isMaestra() == 0) {
		    valv.setAbierta(true);
		    // La añado a la lista de abiertas
		    if (!tareaprog)
		    IR.addvalvsabiertas(valv, valv.getNum_placa());

		    // Genero alarma: Encendido manual de estacion (IRRISOFT)
		    if (valv.getProgasoc() == 0)
			gestorAlertas.insertarAlarma(3019);

		    // Actualizo estado en pasarela
		    IR.hiloescucha.connDB.acualizaestvalv(
			    valv.getCodelecvalv(), 1);

		}

		if (valv.isMaestra() == 0) {

		    IR.escribetextPane("\n Valv. " + valv.getCodelecvalv()
			    + " abierta, prog " + valv.getProgasoc()
			    + " \t\t\t", IR.normal, true);

		    IR.escribelog("Valv. " + valv.getCodelecvalv()
			    + " abierta.\t Prog " + valv.getProgasoc() + " , ");

		} else if (valv.isMaestra() == 1) {

		    IR.escribetextPane("\n uifdghusiokjgkufjsa Valv. maestra "
			    + valv.getCodelecvalv() + " abierta. " + "\t\t",
			    IR.normal, true);

		    IR.escribelog("Valv. maestra " + valv.getCodelecvalv()
			    + " abierta.\t Prog " + valv.getProgasoc() + " , ");
		}
		
		cogesemaforo(false);
		
		if (!tareaprog){
		    
        		try {
        		    Thread.sleep((dormir * 1000) - 8000);
        		} catch (InterruptedException e) {
        		    // TODO Auto-generated catch block
        		    e.printStackTrace();
        		    
        		}
        
        		if (valv.isAbierta())
        		    abrir = false;
		}
		
	    } else {
		JOptionPane.showMessageDialog(IR.frmIrrisoft, "La válvula "
			+ valv.getCodelecvalv()
			+ " ya se encuentra abierta !\n", "",
			JOptionPane.INFORMATION_MESSAGE);

	    }
	}

	// Si es cerrar
	if (abrir == false) {

	    if (valv.isAbierta()) {

		cogesemaforo(true);
		
		if (logger.isInfoEnabled()) {
		    logger.info("Cod electrovalvula a cerrar: "
			    + valv.getCodelecvalv());
		}
		if (valv.isMaestra() == 0)
		    samcla(false, valv.getNumserie(), dura, valv);

		if (!fallo && valv.isMaestra() == 0) {
		    valv.setAbierta(false);
		    // La quito de la lista de abiertas
		    if (!tareaprog)
			IR.quitarvalvabiertas(valv, valv.getNum_placa());

		    // Genero alarma: Apagado manual de estacion (IRRISOFT)
		    if (valv.getProgasoc() == 0 & valv.isMaestra() == 0)
			gestorAlertas.insertarAlarma(3020);

		    // Actualizo estado en la pasarela
		    IR.hiloescucha.connDB.acualizaestvalv(
			    valv.getCodelecvalv(), 0);
		}
		if (logger.isInfoEnabled()) {
		    logger.info("VALVULA antes de cerrar maestra: "
			    + valv.getCodelecvalv());
		}

		if (valv.isMaestra() == 0) {

		    IR.escribetextPane("\n Valv. " + valv.getCodelecvalv()
			    + " cerrrada, prog " + valv.getProgasoc()
			    + " \t\t\t", IR.normal, true);

		    IR.escribelog("Valv. " + valv.getCodelecvalv()
			    + " cerrada.\t Prog " + valv.getProgasoc() + " , ");

		} else if (valv.isMaestra() == 1) {

		    IR.escribetextPane("\n uifdghusiokjgkufjsa Valv. maestra "
			    + valv.getCodelecvalv() + " cerrada. " + "\t\t",
			    IR.normal, true);

		    IR.escribelog("Valv. maestra " + valv.getCodelecvalv()
			    + " cerrada.\t Prog " + valv.getProgasoc() + " , ");
		}

		

	    } else {

		JOptionPane.showMessageDialog(IR.frmIrrisoft, "La válvula "
			+ valv.getCodelecvalv()
			+ " ya se encuentra cerrada !\n", "",
			JOptionPane.INFORMATION_MESSAGE);

	    }

	}

	if (!tareaprog){
        	// Cierro la valvula maestra
        	if (!IR.inicial) {
        	    if (IR.maestra != null) {
        		if (!valv.getCodelecvalv().contentEquals(
        			IR.maestra.getCodelecvalv()))
        		    ;
        		IR.panelbt2.cierramaestra();
        	    }
        	}
	}
	
	cogesemaforo(false);
	
	return fallo;

    }

    public boolean samcla(boolean abrir, Long numserie, String dura,
	    Valvula valvu) {

	boolean estado = false;

	try {
	    Runtime rt = Runtime.getRuntime();
	    String duracion, comando;
	    String telnet = "telnet 192.168.0.3 9000";
	    // Ejecuto el comando del sistema
	    Process pr = rt.exec(telnet);

	    BufferedReader salidapr = new BufferedReader(new InputStreamReader(
		    pr.getInputStream()));
	    BufferedWriter entradapr = new BufferedWriter(
		    new OutputStreamWriter(pr.getOutputStream()));

	    if (abrir) {
		duracion = dura + ";4";
		comando = "SBP_IMMIRRIG " + numserie.toString() + " "
			+ duracion + "\n";
	    } else {
		duracion = "00:00;4";
		comando = "SBP_IMMIRRIG " + numserie.toString() + " "
			+ duracion + "\n";
	    }

	    entradapr.write(comando);
	    entradapr.flush();

	    int conteoLinea = 0;
	    String linea_ok = "";

	    serie.setText("Nº de serie: " + valvu.getNumserie());

	    while (true) {
		String line = salidapr.readLine();

		if (line == null)
		    break;
		logger.info(++conteoLinea + ": " + line);

		if (conteoLinea == 6) {
		    linea_ok = line;
		}
		if (conteoLinea == 7) {
		    if (line.contains("OK")) {
			String[] resp = linea_ok.split(";");
			// System.out.println(resp[0]+ " "+ resp[1]);
			bateria.setText("El nivel de batería de la válvula "
				+ valvu.getCodelecvalv() + " es: " + resp[0]
				+ "%");
			if (abrir)
			    respuesta.setText("La electroválvula "
				    + valvu.getCodelecvalv()
				    + " se ha abierto OK.");
			else
			    respuesta.setText("La electroválvula "
				    + valvu.getCodelecvalv()
				    + " se ha cerrado OK.");
			estado = false;

		    } else {

			if (abrir)
			    respuesta.setText("La electroválvula "
				    + valvu.getCodelecvalv()
				    + " no se ha podido abrir.");
			else
			    respuesta.setText("La electroválvula "
				    + valvu.getCodelecvalv()
				    + " no se ha podido cerrar.");

			bateria.setText("Error: "
				+ linea_ok
				+ " . Revisad el formato de la duración (HH:MM) ?");
			estado = true;
		    }
		}
	    }

	    entradapr.close();
	    salidapr.close();
	    pr.waitFor();

	} catch (Exception x) {
	    x.printStackTrace();
	}
	return estado;
    }

    public String duracion_samcla(int segundos) {

	String minuts = new String();
	String dura = new String();
	int mins = segundos / 60;
	Double horas = mins / 60D;
	int hora = mins / 60;
	Double minutos = horas - hora;

	// Saco los minutos
	mins = (int) (minutos * 60);

	//System.out.println("minutos: " + minutos);
	String num = new String(Double.toString(horas));
	//System.out.println("NUM: " + num);
	String[] nums = num.split("\\.");
	//System.out.println("DURACION SAMCLA HORAS: " + nums[0]);

	// Saco las horas
	if (Integer.parseInt(nums[0]) < 10)
	    nums[0] = "0" + nums[0];

	// Saco los minutos
	if (mins < 10)
	    minuts = "0" + Integer.toString(mins);
	else
	    minuts = Integer.toString(mins);

	dura = nums[0] + ":" + minuts;

	//System.out.println(dura);

	return (dura);

    }

    public void cogesemaforo(boolean coger){
	
	try {
        	if (coger){
        	    
        		IR.semaforosamcla.take();
        	    
        	}else{
        	    IR.semaforosamcla.release();
        	}
	}catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	
    }
}
