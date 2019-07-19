package Proto_gon;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

import java.awt.Color;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingConstants;

import java.awt.Font;

import javax.swing.JRadioButton;
import javax.swing.JSeparator;

public class ProtoGON {

	private JFrame frame;
	protected SerialPort serialPort;
	private static boolean conectado;
	private JTextField textField;
	private JLabel lbldatolectura;

	byte[] buffer = new byte[6];
	int[] bufferint = new int[6];
	byte[] churro = new byte[6];
	int len = 0;
	int leo = 0;
	byte comando = 1;
	byte borna;
	byte parametro1 = 0;
	byte parametro2 = 0;
	byte parametro3 = 0;
	byte checksum;
	// double voltaje_ref = 4.998;
	Double voltaje_ref;
	private JTextField textField_1;
	private JLabel lblNewLabel;
	private JRadioButton rdbtnControladora;
	private JRadioButton rdbtnSensores;
	private JTextField orden;
	private JLabel respuestacommand;
	private JTextField answer;
	private JButton btnProbarComando;
	private JTextField comborna;
	private JTextField comparametro1;
	private JTextField comparametro2;
	private JTextField comparametro3;
	private JLabel command;
	private JSeparator separator_1;
	
	static private String comando_reset = "/home/mikel/resetusb";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProtoGON window = new ProtoGON();
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
	public ProtoGON() {
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 450, 364);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblBorna = new JLabel("Numero de Borna: ");
		lblBorna.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBorna.setBounds(55, 89, 158, 15);
		frame.getContentPane().add(lblBorna);

		textField = new JTextField();
		textField.setBounds(262, 89, 139, 19);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		lbldatolectura = new JLabel("");
		lbldatolectura.setHorizontalAlignment(SwingConstants.CENTER);
		lbldatolectura.setBounds(39, 151, 399, 19);
		frame.getContentPane().add(lbldatolectura);

		JButton btnNewButton = new JButton("Pedir lectura");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				lbldatolectura.setForeground(Color.black);
				lbldatolectura.setText("");
				conecta();

				borna = (byte) Integer.parseInt(textField.getText());

				voltaje_ref = Double.parseDouble(textField_1.getText());

				len = 0;

				churro[0] = comando;
				churro[1] = borna;
				churro[2] = parametro1;
				churro[3] = parametro2;
				churro[4] = parametro3;
				churro[5] = checksum;

				checksum = (byte) (comando + borna + parametro1 + parametro2 + parametro3);

				System.out.println("COMANDO churro = " + comando + ", " + borna
						+ ", " + parametro1 + ", " + parametro2 + ", "
						+ parametro3 + ", " + checksum);

				try {
					serialPort.writeBytes(churro);
				} catch (SerialPortException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				System.out.println("Comando mandado");

				try {
					buffer = serialPort.readBytes(6, 5000);
				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SerialPortTimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					desconecta();
				}

				// Le quito el signo a los bytes
				for (int i = 0; i < 6; i++) {

					bufferint[i] = buffer[i] & 0xFF;
				}

				// System.out.println("BUFFER: "+buffer.toString());
				System.out
						.println("--------------------------------- RESPUESTA PLACA SENSORES");
				System.out.println("Comando: " + bufferint[0]);
				System.out.println("Borna: " + bufferint[1]);
				System.out.println("Parametro1: " + bufferint[2]);
				System.out.println("PArametro2: " + bufferint[3]);
				System.out.println("Parametro3: " + bufferint[4]);
				System.out.println("Checksum: " + bufferint[5]);

				// int pulsos = (buffer[4]*65536)+(buffer[3]*256)+buffer[2];

				if (borna < 4 || borna == 28) { // CONTADORES PULSOS

					int medicion = (bufferint[4] * 65536)
							+ (bufferint[3] * 256) + bufferint[2];

					lbldatolectura.setText("Pulsos recibidos: " + medicion);

				} else if (borna > 3 && borna < 8) // VOLTAJE de 0 a 10
				{

					int medicion = (bufferint[3] * 256) + bufferint[2];
					double voltaje = ((double) medicion / 1023) * voltaje_ref;
					lbldatolectura.setText("Voltaje recibido: " + voltaje);

				} else if (borna > 7 && borna < 12) // ESTADO BINARIO
				{

					if (bufferint[2] == 170) {
						lbldatolectura.setText("El estado es: 1");

					} else if (bufferint[2] == 85) {
						lbldatolectura.setText("El estado es: 0");

					} else
						lbldatolectura
								.setText("bufferint[2] = " + bufferint[2]);

				} else if (borna > 11 & borna < 28) // VOLTAJE DE 0 a 5
				{

					int medicion = (bufferint[3] * 256) + bufferint[2];
					double voltaje = ((double) medicion / 1023) * voltaje_ref;
					lbldatolectura.setText("Voltaje recibido: " + voltaje);

				}

				//
				// //Pasar el voltaje a la medida (el x2 se quitará más adelante
				// double medida =
				// (((rang_med_max-rang_med_min)/(rang_sal_max-rang_sal_min))*voltaje)+rang_med_min;

				// System.out.println("Total que la medicion es: "+medicion);
				// System.out.println("Voltaje recibido: "+ voltaje );
				// System.out.println("La temperatura actual es de: "+medida+" ºC");

				// System.out.println("PULSOS anemometro: "+pulsos);

				// Duermo para recibir otra lectura
				// try {
				// Thread.sleep(5000);
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// desconecta();

			}

		});
		btnNewButton.setBounds(145, 182, 180, 25);
		frame.getContentPane().add(btnNewButton);

		JLabel labelvoltaje = new JLabel("Voltaje de referencia: ");
		labelvoltaje.setHorizontalAlignment(SwingConstants.RIGHT);
		labelvoltaje.setBounds(55, 116, 158, 15);
		frame.getContentPane().add(labelvoltaje);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(262, 116, 139, 19);
		frame.getContentPane().add(textField_1);

		lblNewLabel = new JLabel("ProtoGON");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 19));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(12, 0, 426, 43);
		frame.getContentPane().add(lblNewLabel);

		rdbtnControladora = new JRadioButton("Placa Controladora");
		rdbtnControladora.setBounds(62, 44, 180, 25);
		rdbtnControladora.setSelected(true);
		frame.getContentPane().add(rdbtnControladora);
		rdbtnControladora.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				rdbtnControladora.setSelected(true);
				rdbtnSensores.setSelected(false);
			}
		});

		rdbtnSensores = new JRadioButton("Placa  Sensores");
		rdbtnSensores.setBounds(246, 44, 180, 25);
		frame.getContentPane().add(rdbtnSensores);

		command = new JLabel("Comando:");
		command.setHorizontalAlignment(SwingConstants.RIGHT);
		command.setBounds(80, 249, 85, 15);
		frame.getContentPane().add(command);

		orden = new JTextField();
		orden.setColumns(10);
		orden.setBounds(214, 247, 25, 19);
		frame.getContentPane().add(orden);

		respuestacommand = new JLabel("Respuesta:");
		respuestacommand.setHorizontalAlignment(SwingConstants.RIGHT);
		respuestacommand.setBounds(90, 276, 85, 15);
		frame.getContentPane().add(respuestacommand);

		answer = new JTextField();
		answer.setColumns(10);
		answer.setBounds(214, 273, 170, 19);
		frame.getContentPane().add(answer);

		btnProbarComando = new JButton("Probar comando");
		btnProbarComando.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				lbldatolectura.setForeground(Color.black);
				lbldatolectura.setText("");

				conecta();

				// borna = (byte) Integer.parseInt(textField.getText());
				//
				// voltaje_ref = Double.parseDouble(textField_1.getText());

				len = 0;

				churro[0] = (byte) Integer.parseInt(orden.getText());
				churro[1] = (byte) Integer.parseInt(comborna.getText());
				churro[2] = (byte) Integer.parseInt(comparametro1.getText());
				churro[3] = (byte) Integer.parseInt(comparametro2.getText());
				churro[4] = (byte) Integer.parseInt(comparametro3.getText());
				churro[5] = (byte) (churro[0] + churro[1] + churro[2]
						+ churro[3] + churro[4]);

				System.out.println("COMANDO churro = " + churro[0] + ", "
						+ churro[1] + ", " + churro[2] + ", " + churro[3]
						+ ", " + churro[4] + ", " + churro[5]);

				
				try {
					serialPort.writeBytes(churro);
				} catch (SerialPortException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				System.out.println("Comando mandado");

				try {
					buffer = serialPort.readBytes(6, 5000);
				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SerialPortTimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					desconecta();
				}

				// Le quito el signo a los bytes
				for (int i = 0; i < 6; i++) {

					bufferint[i] = buffer[i] & 0xFF;
				}

				
				System.out
						.println("--------------------------------- RESPUESTA PLACA SENSORES");
				System.out.println("Comando: " + bufferint[0]);
				System.out.println("Borna: " + bufferint[1]);
				System.out.println("Parametro1: " + bufferint[2]);
				System.out.println("PArametro2: " + bufferint[3]);
				System.out.println("Parametro3: " + bufferint[4]);
				System.out.println("Checksum: " + bufferint[5]);

				String respuesta = bufferint[0] + " , " + bufferint[1] + " , "
						+ bufferint[2] + " , " + bufferint[3] + " , "
						+ bufferint[4] + " , " + bufferint[5];
				answer.setText(respuesta);
			}
		});
		btnProbarComando.setBounds(145, 305, 180, 25);
		frame.getContentPane().add(btnProbarComando);

		JSeparator separator = new JSeparator();
		separator.setBounds(12, 219, 426, 2);
		frame.getContentPane().add(separator);

		comborna = new JTextField();
		comborna.setColumns(10);
		comborna.setBounds(251, 247, 25, 19);
		frame.getContentPane().add(comborna);

		comparametro1 = new JTextField();
		comparametro1.setColumns(10);
		comparametro1.setBounds(288, 247, 25, 19);
		frame.getContentPane().add(comparametro1);

		comparametro2 = new JTextField();
		comparametro2.setColumns(10);
		comparametro2.setBounds(325, 247, 25, 19);
		frame.getContentPane().add(comparametro2);

		comparametro3 = new JTextField();
		comparametro3.setColumns(10);
		comparametro3.setBounds(359, 247, 25, 19);
		frame.getContentPane().add(comparametro3);

		separator_1 = new JSeparator();
		separator_1.setBounds(12, 79, 426, 2);
		frame.getContentPane().add(separator_1);
		rdbtnSensores.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				rdbtnSensores.setSelected(true);
				rdbtnControladora.setSelected(false);
			}
		});

	}

	public void conecta() {

		String puerto = "";
		if (rdbtnControladora.isSelected()) {

			try {

				// build my command as a list of strings

				Process p = Runtime.getRuntime().exec("/bin/ls /dev");
				p.waitFor();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(p.getInputStream()));

				String line = "";

				while ((line = reader.readLine()) != null) {

					if (line.contains("placa_cont")) {
						puerto = "/dev/" + line;
						break;
					}
				}

			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("puerto: " + puerto);

		} else if (rdbtnSensores.isSelected())
			puerto = "/dev/placa_sensores";
		else {
			this.lbldatolectura.setForeground(Color.RED);
			this.lbldatolectura
					.setText("Elija el tipo de placa a la que desea preguntar !");
		}

		serialPort = new SerialPort(puerto);

		try {
			serialPort.openPort();
			serialPort.setParams(9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
			serialPort.setDTR(false);

			conectado = true;
			String port = serialPort.getPortName();

			System.out.println("PUERTO " + serialPort.getPortName()
					+ " abierto OK");

		} catch (SerialPortException ex) {

		} catch (NullPointerException ex) {

		}
	}

	public void desconecta() {

		try {

			if (serialPort.closePort()) {
				ejecutacomando();
			}

		} catch (SerialPortException e) {

			e.printStackTrace();
		}
		conectado = false;

	}

	public void ejecutacomando() {
		try {
			Runtime.getRuntime().exec(comando_reset);

		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
	}

}