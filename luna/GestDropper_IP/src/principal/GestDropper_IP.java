package principal;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;

import java.awt.Dialog.ModalExclusionType;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestDropper_IP {

	private JFrame frame;
	private JButton btn;
	private Connection connr;
	private JTextField textField;
	private JLabel lblconectado, lblNewLabel_1;
	private JTextField lblIP;
	private String ip;
	private boolean existe = false, conectador = false;
	private JLabel lblerror;
	private JLabel lblgotita;
	private static final String IMG_GOTITA = "/principal/gotita.png";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GestDropper_IP window = new GestDropper_IP();
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GestDropper_IP() {
		initialize();
		conectar();

		Thread hilo_con = new hilo_con();
		hilo_con.setName("Hilo_con");
		hilo_con.start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		frame.setBounds(100, 100, 406, 300);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(GestDropper_IP.class.getResource(IMG_GOTITA)));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("GestDropper_IP");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 18));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(91, 23, 256, 32);
		frame.getContentPane().add(lblNewLabel);

		lblconectado = new JLabel("");
		lblconectado.setBounds(190, 85, 140, 15);
		frame.getContentPane().add(lblconectado);

		JLabel lblProg = new JLabel("ID Programador: ");
		lblProg.setHorizontalAlignment(SwingConstants.RIGHT);
		lblProg.setBounds(52, 123, 126, 15);
		frame.getContentPane().add(lblProg);

		textField = new JTextField();
		textField.setBounds(190, 121, 140, 19);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		lblNewLabel_1 = new JLabel("La última ip es:");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1.setBounds(52, 215, 120, 24);
		lblNewLabel_1.setVisible(false);
		frame.getContentPane().add(lblNewLabel_1);

		btn = new JButton("Consultar IP");
		btn.addActionListener(new ActionListener() {
			boolean ok = false;

			public void actionPerformed(ActionEvent e) {

				if (textField.getText().contentEquals("")) {
					lblerror.setText("ERROR ! Debes introducir una ID de programador !");
					return;
				} else
					ok = consulta(Integer.parseInt(textField.getText()));

				if (existe && ok) {
					lblerror.setText("");
					lblNewLabel_1.setVisible(true);
					lblIP.setVisible(true);
					lblIP.setText(ip);
					StringSelection stringSelection = new StringSelection(lblIP
							.getText());
					Clipboard clpbrd = Toolkit.getDefaultToolkit()
							.getSystemClipboard();
					clpbrd.setContents(stringSelection, null);
				}

				if (!existe)
					lblerror.setText("ERROR ! El programador "
							+ textField.getText() + " no existe!");

			}
		});
		btn.setBounds(134, 163, 164, 25);
		btn.setEnabled(false);
		frame.getContentPane().add(btn);

		lblIP = new JTextField("");
		lblIP.setEditable(false);
		lblIP.setVisible(false);
		lblIP.setFont(new Font("Dialog", Font.BOLD, 13));
		lblIP.setHorizontalAlignment(SwingConstants.CENTER);
		lblIP.setBounds(190, 214, 164, 25);
		frame.getContentPane().add(lblIP);

		JLabel label = new JLabel("BBDD remota:");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(52, 85, 126, 15);
		frame.getContentPane().add(label);

		lblerror = new JLabel("");
		lblerror.setForeground(Color.RED);
		lblerror.setFont(new Font("Dialog", Font.BOLD, 10));
		lblerror.setHorizontalAlignment(SwingConstants.CENTER);
		lblerror.setBounds(62, 251, 292, 15);
		frame.getContentPane().add(lblerror);

		lblgotita = new JLabel("");
		lblgotita.setBounds(21, 12, 58, 55);
		frame.getContentPane().add(lblgotita);
		redimensionado_jlabel(lblgotita, IMG_GOTITA);

	}

	protected boolean conectar() {

		if (!conectador) {
			try {
				Class.forName("com.mysql.jdbc.Driver");

				String urlremota = "jdbc:mysql://176.58.9.39:5528/gestdropper"
						+ "?autoReconnect=true";
				connr = DriverManager.getConnection(urlremota, "irrigest",
						"13riego");

				lblconectado.setForeground(new Color(0, 128, 0));
				lblconectado.setText("Conectado");
				System.out.println("CONECTADO REMOTA VOLC");
				// Aviso a los Listener de que hay conexion a la BBDD
				// Remota(GIS).
				conectador = true;
				btn.setEnabled(true);
				this.frame.repaint();

			} catch (ClassNotFoundException | SQLException
					| NullPointerException e) {
				if (e instanceof ClassNotFoundException) {
					System.out.println("Error de Conectividad: "
							+ e.getMessage());
				} else if (e instanceof SQLException) {
					System.out.println("Error de sentencia SQL: "
							+ e.getMessage());
				} else if (e instanceof NullPointerException) {
					System.out.println("Error de NullPointerException: "
							+ e.getMessage());
				}

				if (conectador)
					try {
						connr.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				conectador = false;

				lblconectado.setForeground(Color.RED);
				lblconectado.setText("Desconectado");

			}
		}
		
		

		return conectador;

	}

	protected boolean consulta(int idprog) {

		boolean ok = true;
		existe = false;
		String prog;

		PreparedStatement sentenciapre = null;
		ResultSet resultados = null;

		if (idprog < 1000) {
			prog = "0" + Integer.toString(idprog);
			textField.setText(prog);
		} else
			prog = Integer.toString(idprog);

		try {

			sentenciapre = connr
					.prepareStatement("select IPPROG from t03_programador WHERE CODPROG = "
							+ prog);

			resultados = sentenciapre.executeQuery();

			while (resultados.next()) {

				ok = true;
				existe = true;
				ip = resultados.getString(1);
				System.out.println("IP " + prog + " :\t" + ip);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ok = false;
		}

		return ok;
	}

	private void compruebacon() {

		try {

			if (connr == null) {
				btn.setEnabled(false);
				conectar();
				return;
			}

			if (!connr.isValid(6)) {
				connr.close();
				lblconectado.setForeground(Color.RED);
				lblconectado.setText("Desconectado");
				btn.setEnabled(false);
				conectador = false;
				conectar();
				
			}

		} catch (SQLException | NullPointerException e) {
			if (e instanceof SQLException) {
				System.out.println("Error en compruebacon: " + e.getMessage());
			} else if (e instanceof NullPointerException) {
				System.out.println("Error de NullPointerException: "
						+ e.getMessage());
			}
		}

	}

	// SubClase hilo para comprobar la conexión automaticamente
	public class hilo_con extends Thread {

		public hilo_con() {
		}

		public void run() {

			while (true) {
				compruebacon();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * Redimensionado jlabels
	 * 
	 * @param jlabel
	 * @param ruta
	 */
	public void redimensionado_jlabel(JLabel jlabel, String ruta) {
		ImageIcon img = new ImageIcon(GestDropper_IP.class.getResource(ruta));
		Icon icono = null;

		icono = new ImageIcon(img.getImage().getScaledInstance(
				jlabel.getWidth(), jlabel.getHeight(), Image.SCALE_SMOOTH));

		jlabel.setIcon(icono);
	}

}
