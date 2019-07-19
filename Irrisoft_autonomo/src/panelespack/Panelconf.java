package panelespack;

import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeSet;

import javax.swing.JPasswordField;
import javax.swing.JSeparator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.util.text.BasicTextEncryptor;

import com.sun.imageio.plugins.common.InputStreamAdapter;

import volcadopack.Volcado;

public class Panelconf extends JPanel {
    
    private static Panelconf instance;
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private JTextField textId;
    private JTextField textHost;
    private JTextField textPuerto;
    private JTextField textDB;
    private JTextField textUsuario;
    private JTextField textPass;

    private JTextField textMci;
    private JTextField textMci2;
    private JTextField textMci3;
    private JTextField textMci4;

    private JTextField textBT2;
    private JTextField textBT22;
    private JTextField textBT23;
    private JTextField textBT24;

    private JTextField textLimite;

    public static Panelconf getInstance() {

	if (instance == null) {
	    return new Panelconf();
	}

	return instance;

    }

    private Panelconf() {
	super();
	this.setBounds(10, 84, 465, 342);
	setLayout(null);

	JLabel lblId = new JLabel("ID");
	lblId.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 18));
	lblId.setBounds(95, 57, 26, 23);
	add(lblId);

	textId = new JTextField();
	textId.setBounds(138, 58, 89, 19);
	add(textId);
	textId.setColumns(10);
	textId.setText(Irrisoft.config.getIdrasp());

	JLabel lblHost = new JLabel("HOST");
	lblHost.setBounds(20, 115, 38, 15);
	add(lblHost);

	textHost = new JTextField();
	textHost.setColumns(10);
	textHost.setBounds(75, 111, 152, 19);
	add(textHost);
	textHost.setText(Irrisoft.config.getHost());

	JLabel lblPuerto = new JLabel("Puerto");
	lblPuerto.setBounds(20, 141, 48, 15);
	add(lblPuerto);

	textPuerto = new JTextField();
	textPuerto.setColumns(10);
	textPuerto.setBounds(94, 139, 133, 19);
	add(textPuerto);
	textPuerto.setText(Integer.toString(Irrisoft.config.getPuerto()));

	JLabel lblDB = new JLabel("DB");
	lblDB.setBounds(20, 170, 26, 15);
	add(lblDB);

	textDB = new JTextField();
	textDB.setColumns(10);
	textDB.setBounds(75, 170, 152, 19);
	add(textDB);
	textDB.setText(Irrisoft.config.getDb());

	JLabel lblUser = new JLabel("Usuario");
	lblUser.setBounds(20, 201, 55, 15);
	add(lblUser);

	textUsuario = new JTextField();
	textUsuario.setColumns(10);
	textUsuario.setBounds(117, 199, 110, 19);
	add(textUsuario);
	textUsuario.setText(Irrisoft.config.getUsuario());

	JLabel lblPass = new JLabel("Contraseña");
	lblPass.setBounds(19, 229, 94, 15);
	add(lblPass);

	textPass = new JPasswordField();
	textPass.setColumns(10);
	textPass.setBounds(117, 228, 110, 20);
	add(textPass);
	textPass.setText(Irrisoft.config.getPass());

	JLabel lblMci = new JLabel("MCI");
	lblMci.setBounds(283, 56, 26, 15);
	add(lblMci);
	textMci = new JTextField();
	textMci.setColumns(10);
	textMci.setBounds(331, 54, 110, 19);
	//add(textMci);
	textMci.setText(Irrisoft.config.getMci());
	textMci.setEditable(false);
	add(textMci);
	
	JLabel lblMci2 = new JLabel("MCI2");
	lblMci2.setBounds(283, 85, 38, 15);
	add(lblMci2);
	textMci2 = new JTextField();
	textMci2.setColumns(10);
	textMci2.setBounds(331, 83, 110, 19);
	//add(textMci2);
	textMci2.setText(Irrisoft.config.getMci2());
	textMci2.setEditable(false);
	add(textMci2);
	
	JLabel lblMci_1 = new JLabel("MCI3");
	lblMci_1.setBounds(282, 114, 38, 15);
	add(lblMci_1);
	textMci3 = new JTextField();
	textMci3.setText((String) null);
	textMci3.setColumns(10);
	textMci3.setBounds(330, 112, 110, 19);
	//add(textMci3);
	textMci3.setText(Irrisoft.config.getMci3());
	textMci3.setEditable(false);
	add(textMci3);
	
	JLabel lblMci_3 = new JLabel("MCI4");
	lblMci_3.setBounds(283, 143, 38, 15);
	add(lblMci_3);
	textMci4 = new JTextField();
	textMci4.setText((String) null);
	textMci4.setColumns(10);
	textMci4.setBounds(331, 141, 110, 19);
	//add(textMci4);
	textMci4.setText(Irrisoft.config.getMci4());
	textMci4.setEditable(false);
	add(textMci4);
	
	JLabel lblBT2 = new JLabel("BT2");
	lblBT2.setBounds(283, 174, 38, 15);
	add(lblBT2);
	textBT2 = new JTextField();
	textBT2.setColumns(10);
	textBT2.setBounds(331, 172, 110, 19);
	//add(textBT2);
	textBT2.setText(Irrisoft.config.getBt2());
	textBT2.setEditable(false);
	add(textBT2);
	
	JLabel lblBT22 = new JLabel("BT22");
	lblBT22.setBounds(284, 203, 38, 15);
	add(lblBT22);
	textBT22 = new JTextField();
	textBT22.setColumns(10);
	textBT22.setBounds(331, 199, 110, 19);
	//add(textBT22);
	textBT22.setText(Irrisoft.config.getBt22());
	textBT22.setEditable(false);
	add(textBT22);
	
	JLabel lblBT23 = new JLabel("BT23");
	lblBT23.setBounds(283, 232, 38, 15);
	add(lblBT23);
	textBT23 = new JTextField();
	textBT23.setText((String) null);
	textBT23.setColumns(10);
	textBT23.setBounds(331, 227, 110, 19);
	add(textBT23);
	textBT23.setText(Irrisoft.config.getBt23());
	textBT23.setEditable(false);
	add(textBT23);
	
	JLabel lblBT24 = new JLabel("BT24");
	lblBT24.setBounds(282, 259, 38, 17);
	add(lblBT24);
	textBT24 = new JTextField();
	textBT24.setText((String) null);
	textBT24.setColumns(10);
	textBT24.setBounds(331, 258, 110, 19);
	//add(textBT24);
	textBT24.setText(Irrisoft.config.getBt24());
	textBT24.setEditable(false);
	add(textBT24);
	
	JLabel lbllimitebt2 = new JLabel("Límite Valvs BT");
	lbllimitebt2.setBounds(30, 260, 110, 15);
	add(lbllimitebt2);
	textLimite = new JTextField();
	textLimite.setBounds(158, 258, 69, 19);
	add(textLimite);
	textLimite.setColumns(10);
	textLimite.setText(Integer.toString(Irrisoft.config.getLimitebt()));

	JLabel lblConfig = new JLabel("Configuración");
	lblConfig.setHorizontalAlignment(SwingConstants.CENTER);
	lblConfig.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 15));
	lblConfig.setBounds(2, 6, 465, 27);
	add(lblConfig);

	JButton btnGuardar = new JButton("Guardar");
	btnGuardar.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		// TODO AKI Tengo que llamar a guardar en el .txt
		guardarconf();
		Irrisoft.window.leerconfirri(Irrisoft.config);
		Irrisoft.window.volcado.con.getConfig().setIdrasp(Irrisoft.config.getIdrasp());
		//
		Irrisoft.window.leerpuertosbt();
		//

		Irrisoft.window.escribetextPane(
			"\nConfiguración guardada correctamente.",
			Irrisoft.window.normal, false);
		
		
		//Rearmo Irrisoft con los datos nuevos
		Irrisoft.window.rearmar=true;
		//Irrisoft.window.leerconfini();

	    }
	});
	btnGuardar.setBounds(129, 305, 117, 25);
	add(btnGuardar);

	JButton btnAtras = new JButton("Atras");
	btnAtras.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		Panelconf.this.setVisible(false);
		Irrisoft.window.panelpral.setVisible(true);
	    }
	});
	btnAtras.setBounds(265, 305, 117, 25);
	add(btnAtras);

	JSeparator separator = new JSeparator();
	separator.setOrientation(SwingConstants.VERTICAL);
	separator.setBounds(256, 42, 3, 243);
	add(separator);

    }
    
    //METODO REALIZADO PARA ARCHIVO CONFIGURACION.PROPERTIES
    public void guardarconf(){
	Properties propiedades = new Properties(){
	    @Override
	    public synchronized Enumeration<Object> keys() {
	        return Collections.enumeration(new TreeSet<Object>(super.keySet()));
	    }
	};
	InputStream entrada = null;
	BasicTextEncryptor guardarConf = new BasicTextEncryptor();
	String a = null;
	
	try{
	    
	    entrada = new FileInputStream("Irrisoft.properties");
	    propiedades.load(entrada);

	}catch(IOException e){
	    e.getMessage();
	}
	
	try{
	    guardarConf.setPassword(IrrisoftConstantes.PASSWORD);
	    FileOutputStream confGuardar = new FileOutputStream("Irrisoft.properties");

	    Irrisoft.config.setIdrasp(propiedades.setProperty("Aparato.ID", textId.getText()).toString());
	    Irrisoft.config.setHost(propiedades.setProperty("Local.Conexion.HOST", textHost.getText()).toString());
	    Irrisoft.config.setPuerto(Integer.parseInt(propiedades.setProperty("Local.Conexion.PUERTO", textPuerto.getText()).toString()));
	    Irrisoft.config.setDb(propiedades.setProperty("Local.Conexion.DB", textDB.getText()).toString());
	    Irrisoft.config.setUsuario(propiedades.setProperty("Local.Login.USUARIO", textUsuario.getText()).toString());
	    Irrisoft.config.setPass(propiedades.setProperty("Local.Login.PASS", textPass.getText()).toString());
	    a = guardarConf.encrypt(textPass.getText().toString());
	    propiedades.setProperty("Local.Login.PASS", a);
//	    Irrisoft.config.setMci(propiedades.setProperty("Puerto.mci", textMci.getText()).toString());
//	    Irrisoft.config.setMci2(propiedades.setProperty("Puerto.mci2", textMci2.getText()).toString());
//	    Irrisoft.config.setMci3(propiedades.setProperty("Puerto.mci3", textMci3.getText()).toString());
//	    Irrisoft.config.setMci4(propiedades.setProperty("Puerto.mci4", textMci4.getText()).toString());
//	    Irrisoft.config.setBt2(propiedades.setProperty("Puerto.bt2", textBT2.getText()).toString());
//	    Irrisoft.config.setBt22(propiedades.setProperty("Puerto.bt2-2", textBT22.getText()).toString());
//	    Irrisoft.config.setBt23(propiedades.setProperty("Puerto.bt2-3", textBT23.getText()).toString());
//	    Irrisoft.config.setBt24(propiedades.setProperty("Puerto.bt2-4", textBT24.getText()).toString());
	    Irrisoft.config.setLimitebt(Integer.parseInt(propiedades.setProperty("BT2.limitebt", textLimite.getText()).toString()));
	    
	    propiedades.store(confGuardar,null);

	    
	}catch(Exception e){
	    e.getMessage();
	    
	    Irrisoft.window.escribetextPane(
		    "\nNo se ha podido guardar la configuración!!",
		    Irrisoft.window.normal, false);
	}
	
	Irrisoft.window.panelconf.repaint();
    }


    public JTextField getTextMci() {
	return textMci;
    }

    public JTextField getTextMci2() {
	return textMci2;
    }

    public JTextField getTextMci3() {
	return textMci3;
    }

    public JTextField getTextMci4() {
	return textMci4;
    }

    public JTextField getTextBT2() {
	return textBT2;
    }

    public JTextField getTextBT22() {
	return textBT22;
    }

    public JTextField getTextBT23() {
	return textBT23;
    }

    public JTextField getTextBT24() {
	return textBT24;
    }

    public void setTextMci(JTextField textMci) {
	this.textMci = textMci;
    }

    public void setTextMci2(JTextField textMci2) {
	this.textMci2 = textMci2;
    }

    public void setTextMci3(JTextField textMci3) {
	this.textMci3 = textMci3;
    }

    public void setTextMci4(JTextField textMci4) {
	this.textMci4 = textMci4;
    }

    public void setTextBT2(JTextField textBT2) {
	this.textBT2 = textBT2;
    }

    public void setTextBT22(JTextField textBT22) {
	this.textBT22 = textBT22;
    }

    public void setTextBT23(JTextField textBT23) {
	this.textBT23 = textBT23;
    }

    public void setTextBT24(JTextField textBT24) {
	this.textBT24 = textBT24;
    }
}
