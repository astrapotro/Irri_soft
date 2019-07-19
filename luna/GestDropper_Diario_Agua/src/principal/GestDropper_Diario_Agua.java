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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.Dialog.ModalExclusionType;

import javax.swing.SwingConstants;

import java.awt.Font;

import com.toedter.calendar.JDateChooser;

public class GestDropper_Diario_Agua {

	private JFrame frame;
	private JButton btnNewButton;
	private Connection connr;
	private static JTextField programador;
	private JLabel lblprog, lblconectado;
	private JLabel lblresult;
	private JLabel lblNewLabel;
	private ArrayList<Consumo> consumos = new ArrayList<Consumo>();

	public static DateTime hoy;
	public static DateTime ayer;
	public boolean estado = false, existe = false, conectador = false;
	private JLabel lblNewLabel_1;
	private JDateChooser dateChooser;
	private JLabel lblFecha;
	protected DateTime ahora;
	private JLabel lblgotita;
	private JProgressBar progressBar;
	private Consumo consum;
	private String codprogramador;
	private static final String IMG_GOTITA = "/principal/gotita.png";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GestDropper_Diario_Agua window = new GestDropper_Diario_Agua();
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
	public GestDropper_Diario_Agua() {

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
		frame.setBounds(100, 100, 448, 293);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				GestDropper_Diario_Agua.class.getResource(IMG_GOTITA)));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		programador = new JTextField();
		programador.setBounds(250, 95, 114, 19);
		frame.getContentPane().add(programador);
		programador.setColumns(10);

		lblprog = new JLabel("ID Programador :");
		lblprog.setBounds(111, 97, 121, 17);
		frame.getContentPane().add(lblprog);

		lblconectado = new JLabel("");
		lblconectado.setBounds(250, 68, 114, 15);
		frame.getContentPane().add(lblconectado);

		lblNewLabel = new JLabel("BBDD remota:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setBounds(107, 67, 125, 15);
		frame.getContentPane().add(lblNewLabel);

		lblresult = new JLabel("");
		lblresult.setHorizontalAlignment(SwingConstants.CENTER);
		lblresult.setBounds(12, 197, 424, 25);
		frame.getContentPane().add(lblresult);

		btnNewButton = new JButton("Consultar Consumo");
		btnNewButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				lblresult.setText("Realizando consulta. Espere por favor !");
				lblresult.repaint();
				progressBar.setValue(0);
				frame.getContentPane().repaint();
				frame.repaint();

				// if (!programador.getText().isEmpty()) {
				if (dateChooser.getDate() != null) {
					ahora = new DateTime(dateChooser.getDate());

					hoy = ahora.withHourOfDay(12).withMinuteOfHour(0)
							.withSecondOfMinute(0);
					ayer = hoy.minusDays(1);

					Thread hilo_consulta = new hilo_consulta(programador
							.getText());
					hilo_consulta.setName("Hilo_consulta");
					hilo_consulta.start();
				} else {
					lblresult.setText("Introduzca una fecha !");
				}
				// }
				// else {
				// lblresult.setText("Introduzca una ID de programador!");
				// }

			}
		});
		btnNewButton.setBounds(144, 157, 204, 25);
		frame.getContentPane().add(btnNewButton);

		lblNewLabel_1 = new JLabel("GESTDROPPER_Diario_Agua");
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 17));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(107, 29, 298, 26);
		frame.getContentPane().add(lblNewLabel_1);

		dateChooser = new JDateChooser();
		dateChooser.setBounds(250, 126, 124, 19);
		frame.getContentPane().add(dateChooser);

		lblFecha = new JLabel("Fecha: ");
		lblFecha.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFecha.setBounds(111, 128, 121, 17);
		frame.getContentPane().add(lblFecha);

		lblgotita = new JLabel("");
		lblgotita.setBounds(21, 12, 58, 55);
		frame.getContentPane().add(lblgotita);
		redimensionado_jlabel(lblgotita, "gotita.png");

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setForeground(new Color(50, 205, 50));
		progressBar.setBounds(44, 234, 373, 25);
		frame.getContentPane().add(progressBar);

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
			estado = consultadiario(codprog);

			if (!existe) {
				lblresult.setText("No hay registros de consumos!");
				return;
			}

			if (estado && Integer.parseInt(codprogramador)==-1) 
				generarCsv();
			
		}

	}

	public boolean consultadiario(String codprog) {

		boolean ok = false;

		if (codprog.equals("")){
			codprog = "-1";
			System.out.println("CODPROG: "+codprog);
			codprogramador="-1";
		}
		else
			codprogramador=codprog;
		
		
		// return ok;
		btnNewButton.setEnabled(false);
		ok = consulta(Integer.parseInt(codprog));

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
						.prepareStatement("select * from t28_regconsumo WHERE FECHA >= '"
								+ ayer + "' AND FECHA <= '" + hoy + "'");
			else
				sentenciapre = connr
						.prepareStatement("select * from t28_regconsumo where CODPROG="
								+ idprog
								+ " AND FECHA >= '"
								+ ayer
								+ "' AND FECHA <= '" + hoy + "'");

			resultados = sentenciapre.executeQuery();
			
			int i = 0;
			
			if (idprog == -1) {
				consumos.clear();
				int size = 0;
				if (resultados.last()) {
					size = resultados.getRow();
					resultados.beforeFirst();
				}
				System.out.println("resultados: " + size);
				progressBar.setMaximum(size);
			}

			while (resultados.next()) {
				i++;

				ok = true;
				existe = true;

				// Si es para un programador en concreto
				if (idprog != -1) {
					Double consumo = resultados.getDouble(4);
					lblresult.setText("El consumo ha sido de "
							+ Double.toString(consumo) + " litros");

				}
				// Si es para TODOS los programadores
				else {
					consum = new Consumo();
					consum.setCodProg(Integer.parseInt(resultados.getString(2)));
					consum.setFechaAlerta(resultados.getTimestamp(3));
					consum.setConsumo((int)(resultados.getDouble(4)));
					lblresult.setText("Realizando consulta. Espere por favor ... "
							+ i);
					lblresult.repaint();
					System.out
							.println("Consumo:\t" + consum.getCodProg() + " , "
									+ consum.getFechaAlerta() + " , "
									+ Integer.toString(i));
					consumos.add(consum);

					progressBar.setValue(i);
					progressBar.repaint();
				}

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

	protected void generarCsv() {
		String nombre = null;
		String prog = null;

		if (programador.getText().contentEquals(""))
			prog = "";
		else
			prog = "_" + programador.getText();

		nombre = "Diario_Agua_GestDropper" + prog + "_" + hoy.getDayOfMonth() + "-"
				+ hoy.getMonthOfYear() + "-" + hoy.getYear() + "_"
				+ ayer.getDayOfMonth() + "-" + ayer.getMonthOfYear() + "-"
				+ ayer.getYear() + ".csv";

		try {
			FileWriter writer = new FileWriter(nombre);

			writer.append('\n');
			writer.append('\t');
			writer.append("CONSUMOS GESTDROPPER ");
			writer.append('\n');
			writer.append('\n');
			writer.append('\t');
			writer.append("CodProg");
			writer.append('\t');
			writer.append("Consumo");
			writer.append('\t');
			writer.append("Fecha");
			writer.append('\t');
			
			

			for (int i = 0; i < consumos.size(); i++) {
				writer.append('\n');
				writer.append('\t');
				writer.append(Integer.toString(consumos.get(i).getCodProg()));
				writer.append('\t');
				writer.append(Integer.toString(consumos.get(i).getConsumo()));
				writer.append('\t');
				writer.append(consumos.get(i).getFechaAlerta().toString());
				writer.append('\t');
			}

			writer.flush();
			writer.close();
			
			lblresult.setText("Archivo .csv generado correctamente");
			
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

	protected static void generarCsv(ArrayList<String> consumo) {
		String nombre = null;
		String prog = null;

		if (programador.getText().contentEquals(""))
			prog = "";
		else
			prog = "_" + programador.getText();

		nombre = "Consumo_Agua_GestDropper" + prog + "_" + hoy.getDayOfMonth()
				+ "-" + hoy.getMonthOfYear() + "-" + hoy.getYear() + "_"
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

			// for (int i = 0; i < alertas.size(); i++) {
			// writer.append('\n');
			// writer.append('\t');
			// writer.append(Integer.toString(alertas.get(i).getCodProg()));
			// writer.append('\t');
			// writer.append(Integer.toString(alertas.get(i).getId()));
			// writer.append('\t');
			// writer.append(Integer.toString(alertas.get(i).getCodAlerta()));
			// writer.append('\t');
			// writer.append(alertas.get(i).getDes());
			// writer.append('\t');
			// writer.append(alertas.get(i).getFechaAlerta().toString());
			// writer.append('\t');
			// writer.append(alertas.get(i).getDescripcion());
			// }

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Redimensionado jlabels
	 * 
	 * @param jlabel
	 * @param ruta
	 */
	public void redimensionado_jlabel(JLabel jlabel, String ruta) {
		ImageIcon img = new ImageIcon(
				GestDropper_Diario_Agua.class.getResource(ruta));
		Icon icono = null;

		icono = new ImageIcon(img.getImage().getScaledInstance(
				jlabel.getWidth(), jlabel.getHeight(), Image.SCALE_SMOOTH));

		jlabel.setIcon(icono);
	}

}
