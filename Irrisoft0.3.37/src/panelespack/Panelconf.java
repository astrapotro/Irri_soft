package panelespack;



import irrisoftpack.Irrisoft;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import javax.swing.JPasswordField;


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
	private JTextField textBT2;
	private JTextField textMci2;
	private JTextField textBT22;
	private JTextField textLimite;
	
public static Panelconf getInstance(){
		
		if (instance ==null){
			return new Panelconf();
		}
		
		return instance;
		
	}


	private Panelconf() {
		super();
		this.setBounds(10, 84, 465, 291);
		setLayout(null);
		
		JLabel lblId = new JLabel("ID");
		lblId.setBounds(77, 47, 26, 15);
		add(lblId);
		
		textId = new JTextField();
		textId.setBounds(116, 45, 89, 19);
		add(textId);
		textId.setColumns(10);
		textId.setText(Integer.toString(Irrisoft.config.getIdrasp()));
		
		JLabel lblHost = new JLabel("HOST");
		lblHost.setBounds(21, 87, 38, 15);
		add(lblHost);
		
		textHost = new JTextField();
		textHost.setColumns(10);
		textHost.setBounds(76, 83, 133, 19);
		add(textHost);
		textHost.setText(Irrisoft.config.getHost());
		
		
		JLabel lblPuerto = new JLabel("Puerto");
		lblPuerto.setBounds(22, 120, 48, 15);
		add(lblPuerto);
		
		textPuerto = new JTextField();
		textPuerto.setColumns(10);
		textPuerto.setBounds(77, 116, 133, 19);
		add(textPuerto);
		textPuerto.setText(Integer.toString(Irrisoft.config.getPuerto()));
		
		JLabel lblDB = new JLabel("DB");
		lblDB.setBounds(21, 147, 26, 15);
		add(lblDB);
		
		textDB = new JTextField();
		textDB.setColumns(10);
		textDB.setBounds(76, 147, 133, 19);
		add(textDB);
		textDB.setText(Irrisoft.config.getDb());
		
		JLabel lblUser = new JLabel("Usuario");
		lblUser.setBounds(21, 176, 55, 15);
		add(lblUser);
		
		textUsuario = new JTextField();
		textUsuario.setColumns(10);
		textUsuario.setBounds(86, 174, 123, 19);
		add(textUsuario);
		textUsuario.setText(Irrisoft.config.getUsuario());
		
		JLabel lblPass = new JLabel("Contraseña");
		lblPass.setBounds(223, 46, 101, 15);
		add(lblPass);
		
		textPass = new JPasswordField();
		textPass.setColumns(10);
		textPass.setBounds(316, 44, 130, 19);
		add(textPass);
		textPass.setText(Irrisoft.config.getPass());
		
		JLabel lblMci = new JLabel("MCI");
		lblMci.setBounds(268, 85, 26, 15);
		add(lblMci);
		
		textMci = new JTextField();
		textMci.setColumns(10);
		textMci.setBounds(316, 83, 110, 19);
		add(textMci);
		textMci.setText(Irrisoft.config.getMci());
		
		JLabel lblBT2 = new JLabel("BT2");
		lblBT2.setBounds(268, 114, 38, 15);
		add(lblBT2);
		
		textBT2 = new JTextField();
		textBT2.setColumns(10);
		textBT2.setBounds(316, 112, 110, 19);
		add(textBT2);
		textBT2.setText(Irrisoft.config.getBt2());
		
		JLabel lblMci2 = new JLabel("MCI2");
		lblMci2.setBounds(268, 147, 38, 15);
		add(lblMci2);
		
		textMci2 = new JTextField();
		textMci2.setColumns(10);
		textMci2.setBounds(316, 145, 110, 19);
		add(textMci2);
		textMci2.setText(Irrisoft.config.getMci2());
		
		JLabel lblBT22 = new JLabel("BT22");
		lblBT22.setBounds(268, 176, 38, 15);
		add(lblBT22);
		
		textBT22 = new JTextField();
		textBT22.setColumns(10);
		textBT22.setBounds(316, 174, 110, 19);
		add(textBT22);
		textBT22.setText(Irrisoft.config.getBt22());
		
		JLabel lbllimitebt2 = new JLabel("Limite Valvs BT");
		lbllimitebt2.setBounds(199, 209, 110, 15);
		add(lbllimitebt2);
		
		textLimite = new JTextField();
		textLimite.setBounds(316, 205, 114, 19);
		add(textLimite);
		textLimite.setColumns(10);
		textLimite.setText(Integer.toString(Irrisoft.config.getLimitebt()));
		

		JLabel lblConfig = new JLabel("Configuración");
		lblConfig.setHorizontalAlignment(SwingConstants.CENTER);
		lblConfig.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 15));
		lblConfig.setBounds(0, 0, 465, 27);
		add(lblConfig);
		
		
		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			//TODO AKI Tengo que llamar a guardar en el .txt
				guardarconf();
				Irrisoft.leerconf(Irrisoft.config);
				//
				Irrisoft.leerpuertosbt();
				//
				Irrisoft.window.textArea.append("\nConfiguración guardada correctamente.");
			}
		});
		btnGuardar.setBounds(109, 259, 117, 25);
		add(btnGuardar);
		
		JButton btnAtras = new JButton("Atras");
		btnAtras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Panelconf.this.setVisible(false);
				Irrisoft.window.panelpral.setVisible(true);
			}
		});
		btnAtras.setBounds(245, 259, 117, 25);
		add(btnAtras);	
	}
	


	private void  guardarconf() {

	    try{

	      FileWriter fichero = new FileWriter(Irrisoft.rutaconf);

	      //Insertamos el texto creado y si trabajamos
	      //en Windows terminaremos cada línea con "\r\n"
	      fichero.write("ID:"+textId.getText()+ "\r\n");
	      Irrisoft.config.setIdrasp(Integer.parseInt(textId.getText()));
	      fichero.write("HOST:"+textHost.getText()+ "\r\n");
	      Irrisoft.config.setHost(textHost.getText());
	      fichero.write("PUERTO:"+textPuerto.getText()+ "\r\n");
	      Irrisoft.config.setPuerto(Integer.parseInt(textPuerto.getText()));
	      fichero.write("DB:"+textDB.getText()+ "\r\n");
	      Irrisoft.config.setDb(textDB.getText());
	      fichero.write("USUARIO:"+textUsuario.getText()+ "\r\n");
	      Irrisoft.config.setUsuario(textUsuario.getText());
	      fichero.write("PASS:"+textPass.getText()+ "\r\n");
	      Irrisoft.config.setPass(textPass.getText());
	      fichero.write("MCI:"+textMci.getText()+ "\r\n");
	      Irrisoft.config.setMci(textMci.getText());
	      fichero.write("BT2:"+textBT2.getText()+ "\r\n");
	      Irrisoft.config.setBt2(textBT2.getText());
	      fichero.write("mcidos:"+textMci2.getText()+ "\r\n");
	      Irrisoft.config.setMci2(textMci2.getText());
	      fichero.write("bt2dos:"+textBT22.getText()+ "\r\n");
	      Irrisoft.config.setBt22(textBT22.getText());
	      fichero.write("limitebt:"+textLimite.getText()+ "\r\n");
	      Irrisoft.config.setLimitebt(Integer.parseInt(textLimite.getText()));
	      
	      //cerramos el fichero
	      fichero.close();

	    }catch(Exception ex){
	      ex.printStackTrace();
	      Irrisoft.window.textArea.append("\nNo se ha podido guardar la configuración!!");
	    }
	    
	    Irrisoft.window.panelconf.repaint();
	 
	}
}
