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
		lblId.setBounds(77, 68, 26, 15);
		add(lblId);
		
		textId = new JTextField();
		textId.setBounds(116, 66, 89, 19);
		add(textId);
		textId.setColumns(10);
		textId.setText(Integer.toString(Irrisoft.window.config.getIdrasp()));
		
		JLabel lblHost = new JLabel("HOST");
		lblHost.setBounds(21, 108, 38, 15);
		add(lblHost);
		
		textHost = new JTextField();
		textHost.setColumns(10);
		textHost.setBounds(76, 104, 133, 19);
		add(textHost);
		textHost.setText(Irrisoft.window.config.getHost());
		
		
		JLabel lblPuerto = new JLabel("Puerto");
		lblPuerto.setBounds(22, 141, 48, 15);
		add(lblPuerto);
		
		textPuerto = new JTextField();
		textPuerto.setColumns(10);
		textPuerto.setBounds(77, 137, 133, 19);
		add(textPuerto);
		textPuerto.setText(Integer.toString(Irrisoft.window.config.getPuerto()));
		
		JLabel lblDB = new JLabel("DB");
		lblDB.setBounds(21, 168, 26, 15);
		add(lblDB);
		
		textDB = new JTextField();
		textDB.setColumns(10);
		textDB.setBounds(76, 168, 133, 19);
		add(textDB);
		textDB.setText(Irrisoft.window.config.getDb());
		
		JLabel lblUser = new JLabel("Usuario");
		lblUser.setBounds(21, 197, 55, 15);
		add(lblUser);
		
		textUsuario = new JTextField();
		textUsuario.setColumns(10);
		textUsuario.setBounds(86, 195, 123, 19);
		add(textUsuario);
		textUsuario.setText(Irrisoft.window.config.getUsuario());
		
		JLabel lblPass = new JLabel("Contraseña");
		lblPass.setBounds(223, 67, 101, 15);
		add(lblPass);
		
		textPass = new JTextField();
		textPass.setColumns(10);
		textPass.setBounds(316, 65, 130, 19);
		add(textPass);
		textPass.setText(Irrisoft.window.config.getPass());
		
		JLabel lblMci = new JLabel("MCI");
		lblMci.setBounds(268, 106, 26, 15);
		add(lblMci);
		
		textMci = new JTextField();
		textMci.setColumns(10);
		textMci.setBounds(316, 104, 110, 19);
		add(textMci);
		textMci.setText(Irrisoft.window.config.getMci());
		
		JLabel lblBT2 = new JLabel("BT2");
		lblBT2.setBounds(268, 135, 38, 15);
		add(lblBT2);
		
		textBT2 = new JTextField();
		textBT2.setColumns(10);
		textBT2.setBounds(316, 133, 110, 19);
		add(textBT2);
		textBT2.setText(Irrisoft.window.config.getBt2());
		
		JLabel lblMci2 = new JLabel("MCI2");
		lblMci2.setBounds(268, 168, 38, 15);
		add(lblMci2);
		
		textMci2 = new JTextField();
		textMci2.setColumns(10);
		textMci2.setBounds(316, 166, 110, 19);
		add(textMci2);
		textMci2.setText(Irrisoft.window.config.getMci2());
		
		JLabel lblBT22 = new JLabel("BT22");
		lblBT22.setBounds(268, 197, 38, 15);
		add(lblBT22);
		
		textBT22 = new JTextField();
		textBT22.setColumns(10);
		textBT22.setBounds(316, 195, 110, 19);
		add(textBT22);
		textBT22.setText(Irrisoft.window.config.getBt22());
		
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
				Irrisoft.leerconf(Irrisoft.window.config);
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
	      fichero.write("HOST:"+textHost.getText()+ "\r\n");
	      fichero.write("PUERTO:"+textPuerto.getText()+ "\r\n");
	      fichero.write("DB:"+textDB.getText()+ "\r\n");
	      fichero.write("USUARIO:"+textUsuario.getText()+ "\r\n");
	      fichero.write("PASS:"+textPass.getText()+ "\r\n");
	      fichero.write("MCI:"+textMci.getText()+ "\r\n");
	      fichero.write("BT2:"+textBT2.getText()+ "\r\n");
	      fichero.write("mcidos:"+textMci2.getText()+ "\r\n");
	      fichero.write("bt2dos:"+textBT22.getText()+ "\r\n");
	     
	      
	      //cerramos el fichero
	      fichero.close();

	    }catch(Exception ex){
	      ex.printStackTrace();
	      Irrisoft.window.textArea.append("\nNo se ha podido guardar la configuración!!");
	    }
	 
	}
}
