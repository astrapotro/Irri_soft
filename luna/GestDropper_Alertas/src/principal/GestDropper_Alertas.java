package principal;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import org.joda.time.DateTime;

import com.toedter.calendar.JDateChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;

import principal.Alerta;

import java.awt.Dialog.ModalExclusionType;

import javax.swing.SwingConstants;

import java.awt.Font;

public class GestDropper_Alertas {

	private JFrame frame;
	private JButton btnNewButton;
	private Connection connr;
	private static JTextField programador;
	private JLabel lblprog, lblconectado;
	private JLabel lblresult;
	private JLabel lblNewLabel;
	private GestDropper_Alertas AS;
	private ArrayList<Alerta> alertas = new ArrayList<Alerta>();
	public static DateTime hoy;
	public static DateTime ayer;
	public boolean estado = false, existe = false, conectador = false;
	private JLabel lblNewLabel_1;
	private JDateChooser dateChooser;
	private DateTime ahora;
	private JProgressBar progressBar;
	private JLabel lblgotita;
	private static final String IMG_GOTITA = "/principal/gotita.png";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GestDropper_Alertas window = new GestDropper_Alertas();
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
	public GestDropper_Alertas() {
		this.AS = this;
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
		frame.setBounds(100, 100, 448, 314);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(GestDropper_Alertas.class.getResource(IMG_GOTITA)));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		programador = new JTextField();
		programador.setBounds(243, 111, 124, 19);
		frame.getContentPane().add(programador);
		programador.setColumns(10);

		lblprog = new JLabel("ID Programador :");
		lblprog.setBounds(104, 113, 121, 17);
		frame.getContentPane().add(lblprog);

		lblconectado = new JLabel("");
		lblconectado.setBounds(243, 75, 114, 15);
		frame.getContentPane().add(lblconectado);

		lblNewLabel = new JLabel("BBDD remota:");
		lblNewLabel.setBounds(104, 75, 105, 15);
		frame.getContentPane().add(lblNewLabel);

		lblresult = new JLabel("");
		lblresult.setHorizontalAlignment(SwingConstants.CENTER);
		lblresult.setBounds(12, 224, 424, 25);
		frame.getContentPane().add(lblresult);

		btnNewButton = new JButton("Exportar alertas");
		btnNewButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				lblresult.setForeground(Color.black);
				progressBar.setValue(0);
				lblresult.setText("Realizando consulta. Espere por favor ...");
				lblresult.repaint();
				frame.getContentPane().repaint();
				frame.repaint();

				ahora = new DateTime(dateChooser.getDate());
				if (dateChooser.getDate() != null) {
					hoy = ahora.withHourOfDay(12).withMinuteOfHour(0)
							.withSecondOfMinute(0);
					ayer = hoy.minusDays(1);

					AS.alertas.clear();

					Thread hilo_consulta = new hilo_consulta(programador
							.getText());
					hilo_consulta.setName("Hilo_consulta");
					hilo_consulta.start();
					progressBar.setVisible(true);
				} else{
					lblresult.setForeground(Color.red);
					lblresult.setText("Introduzca una fecha!");
					
				}

			}
		});
		btnNewButton.setBounds(143, 187, 171, 25);
		frame.getContentPane().add(btnNewButton);

		dateChooser = new JDateChooser();
		dateChooser.setBounds(243, 148, 124, 19);
		frame.getContentPane().add(dateChooser);

		lblNewLabel_1 = new JLabel("GESTDROPPER_ALERTAS");
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 17));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(87, 25, 309, 26);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel lblFecha = new JLabel("Fecha:");
		lblFecha.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFecha.setBounds(104, 150, 121, 17);
		frame.getContentPane().add(lblFecha);

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setForeground(new Color(50, 205, 50));
		progressBar.setBounds(41, 255, 373, 25);
		progressBar.setVisible(false);
		frame.getContentPane().add(progressBar);

		lblgotita = new JLabel("");
		lblgotita.setBounds(23, 12, 58, 55);
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
				btnNewButton.setEnabled(true);

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
		
		this.frame.repaint();

		return conectador;

	}

	// SubClase hilo para arir/cerrar las maestras
	public class hilo_consulta extends Thread {

		String codprog;

		public hilo_consulta(String codprog) {
			this.codprog = codprog;
		}

		public void run() {
			estado = consultalertas(codprog);

			if (!existe) {
				lblresult.setForeground(Color.red);
				lblresult.setText("No hay registros de alertas!");
				return;
			}
			if (estado && existe){
				lblresult.setForeground(Color.black);
				lblresult.setText("Archivo .csv generado correctamente!");
			}
			else if (!estado){
				lblresult.setForeground(Color.red);
				lblresult
						.setText("ERROR! Problemas al generar el archivo .csv");
			}
		}

	}

	public boolean consultalertas(String codprog) {

		boolean ok = false;

		if (codprog.equals(""))
			codprog = "-1";
		btnNewButton.setEnabled(false);
		ok = consulta(Integer.parseInt(codprog));

		if (ok && existe)
			generarCsv(alertas);

		btnNewButton.setEnabled(true);
		return ok;

	}

	protected boolean consulta(int idprog) {

		boolean ok = true;
		existe = false;

		PreparedStatement sentenciapre = null;
		ResultSet resultados = null;

		try {
			if (idprog == -1)
				sentenciapre = connr
						.prepareStatement("select * from t08_alerta WHERE FCALERTA >= '"
								+ ayer + "' AND FCALERTA <= '" + hoy + "'");
			else
				sentenciapre = connr
						.prepareStatement("select * from t08_alerta where CODPROG="
								+ idprog
								+ " AND FCALERTA >= '"
								+ ayer
								+ "' AND FCALERTA <= '" + hoy + "'");

			resultados = sentenciapre.executeQuery();
			int i = 0;
			int size = 0;
			if (resultados.last()) {
				size = resultados.getRow();
				resultados.beforeFirst();
			}
			System.out.println("resultados: " + size);
			progressBar.setMaximum(size);

			while (resultados.next()) {
				i++;
				ok = true;
				existe = true;

				Alerta alerta = new Alerta();
				alerta.setFechaAlerta(resultados.getTimestamp(2));
				alerta.setId(resultados.getInt(3));
				alerta.setCodProg(resultados.getInt(4));
				alerta.setDes(resultados.getString(5));
				consultaid(alerta);
				consultadesc(alerta);
				lblresult.setText("Realizando consulta. Espere por favor ... "
						+ i);
				lblresult.repaint();
				System.out
						.println("Alerta:\t" + alerta.getCodProg() + " , "
								+ alerta.getCodAlerta() + " , "
								+ alerta.getFechaAlerta() + " , "
								+ Integer.toString(i));
				alertas.add(alerta);

				progressBar.setValue(i);
				progressBar.repaint();
			}

			sentenciapre.close();
			resultados.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ok = false;
		}

		return ok;
	}

	protected void consultaid(Alerta alert) {

		PreparedStatement senten = null;
		ResultSet res = null;

		try {

			senten = connr
					.prepareStatement("select CODALERTA from t21_dsalerta where IDDSALERTA="
							+ alert.getId());

			res = senten.executeQuery();

			while (res.next()) {

				alert.setCodAlerta(res.getInt(1));
			}

			senten.close();
			res.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void consultadesc(Alerta alert) {

		PreparedStatement sen = null;
		ResultSet re = null;

		try {

			sen = connr
					.prepareStatement("select DSALERTA from t21_dsalerta where IDDSALERTA="
							+ alert.getId());

			re = sen.executeQuery();

			while (re.next()) {

				alert.setDescripcion(re.getString(1));
			}

			sen.close();
			re.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected static void generarCsv(ArrayList<Alerta> alertas) {
		String nombre = null;
		String prog = null;

		if (programador.getText().contentEquals(""))
			prog = "";
		else
			prog = "_" + programador.getText();

		nombre = "Alertas_GestDropper" + prog + "_" + hoy.getDayOfMonth() + "-"
				+ hoy.getMonthOfYear() + "-" + hoy.getYear() + "_"
				+ ayer.getDayOfMonth() + "-" + ayer.getMonthOfYear() + "-"
				+ ayer.getYear() + ".csv";

		try {
			FileWriter writer = new FileWriter(nombre);

			writer.append('\n');
			writer.append('\t');
			writer.append("ALERTAS GESTDROPPER ");
			writer.append('\n');
			writer.append('\n');
			writer.append('\t');
			writer.append("CodProg");
			writer.append('\t');
			writer.append("ID");
			writer.append('\t');
			writer.append("CodAlerta");
			writer.append('\t');
			writer.append("Ítems");
			writer.append('\t');
			writer.append("Fecha");
			writer.append('\t');
			writer.append("Descripción");

			for (int i = 0; i < alertas.size(); i++) {
				writer.append('\n');
				writer.append('\t');
				writer.append(Integer.toString(alertas.get(i).getCodProg()));
				writer.append('\t');
				writer.append(Integer.toString(alertas.get(i).getId()));
				writer.append('\t');
				writer.append(Integer.toString(alertas.get(i).getCodAlerta()));
				writer.append('\t');
				writer.append(alertas.get(i).getDes());
				writer.append('\t');
				writer.append(alertas.get(i).getFechaAlerta().toString());
				writer.append('\t');
				writer.append(alertas.get(i).getDescripcion());
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void compruebacon() {

		try {

			if (connr == null) {
				btnNewButton.setEnabled(false);
				conectar();
				return;
			}

			if (!connr.isValid(6)) {
				lblconectado.setForeground(Color.RED);
				lblconectado.setText("Desconectado");
				btnNewButton.setEnabled(false);
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
		ImageIcon img = new ImageIcon(GestDropper_Alertas.class.getResource(ruta));
		Icon icono = null;

		icono = new ImageIcon(img.getImage().getScaledInstance(
				jlabel.getWidth(), jlabel.getHeight(), Image.SCALE_SMOOTH));

		jlabel.setIcon(icono);
	}
	
}
