package Proto_gon;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Font;

public class ProtoGON {

	private JFrame frame;
	protected SerialPort serialPort;
	private static boolean conectado;
	private JTextField textField;
	private JLabel lbldatolectura;
	
	byte[] buffer = new byte [6];
	int[] bufferint = new int [6];
	byte [] churro = new byte [6];
	int len = 0;
	int  leo = 0;
	byte comando = 1;
	byte borna;
	byte parametro1 = 0;
	byte parametro2 = 0;
	byte parametro3 = 0;
	byte checksum;
	//double voltaje_ref = 4.998;
	Double voltaje_ref;
	private JTextField textField_1;
	private JLabel lblNewLabel;
	
	

	
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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblBorna = new JLabel("Numero de Borna: ");
		lblBorna.setBounds(39, 68, 144, 15);
		frame.getContentPane().add(lblBorna);
		
		textField = new JTextField();
		textField.setBounds(246, 68, 114, 19);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		lbldatolectura = new JLabel("");
		lbldatolectura.setBounds(39, 149, 399, 15);
		frame.getContentPane().add(lbldatolectura);
		
		JButton btnNewButton = new JButton("Pedir lectura");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				conecta();
		
				borna = (byte) Integer.parseInt(textField.getText());
			
				voltaje_ref = Double.parseDouble(textField_1.getText());
				
				len = 0;
				
	    		churro[0]=comando;
	    		churro[1]= borna ;
	    		churro[2]=parametro1;
	    		churro[3]=parametro2;
	    		churro[4]=parametro3;
	    		churro[5]=checksum;
	    		
	    		checksum = (byte) (comando + borna + parametro1 +parametro2 +parametro3);
	    				
	    		System.out.println("COMANDO churro = "+comando+", "+borna+", "+parametro1+", "+parametro2+", "+parametro3+", "+checksum);
		 
		    	
		    	try {
					serialPort.writeBytes(churro);
				} catch (SerialPortException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
		    	System.out.println("Comando mandado");
		    	
		    
	    				
							try {
								buffer=serialPort.readBytes(6,5000);
							} catch (SerialPortException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SerialPortTimeoutException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
						//Le quito el signo a los bytes
	    				for (int i=0;i<6;i++){
	    					
	    					bufferint[i]= buffer[i] & 0xFF;
	    				}
	    				
	  
	    				
	    		
		    	
		    	//System.out.println("BUFFER: "+buffer.toString());
		    	System.out.println("--------------------------------- RESPUESTA PLACA SENSORES");
		    	System.out.println("Comando: "+bufferint[0]);
		    	System.out.println("Borna: "+bufferint[1]);
		    	System.out.println("Parametro1: "+bufferint[2]);
		    	System.out.println("PArametro2: "+bufferint[3]);
		    	System.out.println("Parametro3: "+bufferint[4]);
		    	System.out.println("Checksum: "+bufferint[5]);
		    	
		    	
		    	//int pulsos = (buffer[4]*65536)+(buffer[3]*256)+buffer[2];
		    	
		    	
		    	
		    	if (borna<4 || borna ==28){	//CONTADORES PULSOS
		    		
		    		int medicion = (bufferint[4]*65536)+(bufferint[3]*256)+bufferint[2];
		    		
		    		lbldatolectura.setText("Pulsos recibidos: "+medicion);
		    		
		    	}
		    	else if (borna>3 && borna<8) //VOLTAJE de 0 a 10
		    	{
		    		
		    		
		    		int medicion = (bufferint[3]*256)+bufferint[2];
		    		double voltaje  = ((double)medicion/1023)*voltaje_ref;
		    		lbldatolectura.setText("Voltaje recibido: "+voltaje);
		    		
		    	}
		    	else if (borna >7 && borna <12) //ESTADO BINARIO
		    	{
		    		
		    		if (bufferint[2]==170){
		    			lbldatolectura.setText("El estado es: 1");
		    			
		    		}else if (bufferint[2]==85){
		    			lbldatolectura.setText("El estado es: 0");
		    			
		    		}
		    		else 
		    			lbldatolectura.setText("bufferint[2] = "+bufferint[2]);
		    		
		    		
		    		
		    	}
		    	else if (borna >11 & borna <28) //VOLTAJE DE 0 a 5
		    	{
		    		
		    		int medicion = (bufferint[3]*256)+bufferint[2];
		    		double voltaje  = ((double)medicion/1023)*voltaje_ref;
		    		lbldatolectura.setText("Voltaje recibido: "+voltaje);
		    		
		    		
		    	}
		    	
//		    	
//		    	//Pasar el voltaje a la medida (el x2 se quitará más adelante
		    	//double medida = (((rang_med_max-rang_med_min)/(rang_sal_max-rang_sal_min))*voltaje)+rang_med_min;
		    	
		    	
//		    	System.out.println("Total que la medicion es: "+medicion);
//		    	System.out.println("Voltaje recibido: "+ voltaje );
		    	//System.out.println("La temperatura actual es de: "+medida+" ºC");
		    	
		    	//System.out.println("PULSOS anemometro: "+pulsos);
		    	
		    	//Duermo para recibir otra lectura
//		    	try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
		    	desconecta();
		    	
		}
				
				
			
		});
		btnNewButton.setBounds(141, 203, 180, 43);
		frame.getContentPane().add(btnNewButton);
		
		JLabel labelvoltaje = new JLabel("Voltaje de referencia: ");
		labelvoltaje.setBounds(39, 108, 158, 15);
		frame.getContentPane().add(labelvoltaje);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(246, 108, 114, 19);
		frame.getContentPane().add(textField_1);
		
		lblNewLabel = new JLabel("ProtoGON");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 19));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(12, 0, 426, 43);
		frame.getContentPane().add(lblNewLabel);	
	
}


	public void conecta() {
		// TODO Auto-generated method stub
		
		String puerto="/dev/ttyACM0";
		serialPort = new SerialPort(puerto);
	

	    	try {
	    	    serialPort.openPort();
	    		serialPort.setParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
	    		serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
	    		serialPort.setDTR(false);

	    	    // int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS +
	    	    // SerialPort.MASK_DSR;//Preparo máscara
	    	    // serialPort.setEventsMask(mask);//Pongo la máscara
	    	    // serialPort.addEventListener(new SerialPortReader());//Añado
	    	    // listener de eventos en el puerto

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
			    }
			  
			} catch (SerialPortException e) {
			   
			    e.printStackTrace();
			}
			conectado = false;

}

}

	
////	VERSION CON RXTX en vez de JSSC
//	
//	
//	package Proto_gon;
//
//	import gnu.io.CommPort;
//	import gnu.io.CommPortIdentifier;
//	import gnu.io.NoSuchPortException;
//	import gnu.io.PortInUseException;
//	import gnu.io.SerialPort;
//	import gnu.io.UnsupportedCommOperationException;
//
//	import java.awt.EventQueue;
//	import java.io.IOException;
//	import java.io.InputStream;
//	import java.io.OutputStream;
//
//	import javax.swing.JFrame;
//	import javax.swing.JLabel;
//	import javax.swing.JTextField;
//	import javax.swing.JButton;
//	import java.awt.event.ActionListener;
//	import java.awt.event.ActionEvent;
//	import javax.swing.SwingConstants;
//	import java.awt.Font;
//
//	public class ProtoGON {
//
//		private JFrame frame;
//		
//		protected static SerialPort serialPort;
//		public static OutputStream out;
//		public static InputStream in;
//		private JTextField textField;
//		private JLabel lbldatolectura;
//		
//		int [] buffer = new int [6];
//		byte [] churro = new byte [6];
//		int len = 0;
//		int  leo = 0;
//		byte comando = 1;
//		byte borna;
//		byte parametro1 = 0;
//		byte parametro2 = 0;
//		byte parametro3 = 0;
//		byte checksum;
//		//double voltaje_ref = 4.998;
//		Double voltaje_ref;
//		private JTextField textField_1;
//		private JLabel lblNewLabel;
//		
//		
//
//		
//		/**
//		 * Launch the application.
//		 */
//		public static void main(String[] args) {
//			EventQueue.invokeLater(new Runnable() {
//				public void run() {
//					try {
//						ProtoGON window = new ProtoGON();
//						window.frame.setVisible(true);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			});
//		}
//
//		/**
//		 * Create the application.
//		 */
//		public ProtoGON() {
//			initialize();
//		}
//
//		/**
//		 * Initialize the contents of the frame.
//		 */
//		private void initialize() {
//			
//			frame = new JFrame();
//			frame.setBounds(100, 100, 450, 300);
//			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			frame.getContentPane().setLayout(null);
//			
//			JLabel lblBorna = new JLabel("Numero de Borna: ");
//			lblBorna.setBounds(39, 68, 144, 15);
//			frame.getContentPane().add(lblBorna);
//			
//			textField = new JTextField();
//			textField.setBounds(246, 68, 114, 19);
//			frame.getContentPane().add(textField);
//			textField.setColumns(10);
//			
//			lbldatolectura = new JLabel("");
//			lbldatolectura.setBounds(39, 149, 399, 15);
//			frame.getContentPane().add(lbldatolectura);
//			
//			JButton btnNewButton = new JButton("Pedir lectura");
//			btnNewButton.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent arg0) {
//					
//					
//					conecta();
//			
//					borna = (byte) Integer.parseInt(textField.getText());
//				
//					voltaje_ref = Double.parseDouble(textField_1.getText());
//					
//					len = 0;
//					
//		    		churro[0]=comando;
//		    		churro[1]= ((byte)borna) ;
//		    		churro[2]=parametro1;
//		    		churro[3]=parametro2;
//		    		churro[4]=parametro3;
//		    		churro[5]=checksum;
//		    		
//		    		checksum = (byte) (comando + borna + parametro1 +parametro2 +parametro3);
//		    				
//		    		System.out.println("COMANDO churro = "+comando+", "+borna+", "+parametro1+", "+parametro2+", "+parametro3+", "+checksum);
//			    	System.out.println(serialPort.getName());
//			    	
//			    	try {
//						out.write(churro);
//						//out.flush();
//						
//					} catch (IOException e) {
//						// 
//						e.printStackTrace();
//					}
//				
//			    	System.out.println("Comando mandado");
//			    	
//			    	while(len<6)
//		    		{
//			    		
//		    				try {
//								leo=in.read();
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//		    				
//		    				//Le quito el signo al byte
//		    				int num = leo  & 0xFF;
//		    		
//		    				buffer[len]=num;
//		  
//		    				len ++;
//		    		}
//			    	
//			    	//System.out.println("BUFFER: "+buffer.toString());
//			    	System.out.println("--------------------------------- RESPUESTA PLACA SENSORES");
//			    	System.out.println("Comando: "+buffer[0]);
//			    	System.out.println("Borna: "+buffer[1]);
//			    	System.out.println("Parametro1: "+buffer[2]);
//			    	System.out.println("PArametro2: "+buffer[3]);
//			    	System.out.println("Parametro3: "+buffer[4]);
//			    	System.out.println("Checksum: "+buffer[5]);
//			    	
//			    	
//			    	//int pulsos = (buffer[4]*65536)+(buffer[3]*256)+buffer[2];
//			    	
//			    	
//			    	
//			    	if (borna<4){	//CONTADORES PULSOS
//			    		
//			    		int medicion = (buffer[4]*65536)+(buffer[3]*256)+buffer[2];
//			    		
//			    		lbldatolectura.setText("Num de pulsos nuevos: "+medicion);
//			    		
//			    	}
//			    	else if (borna>4 && borna<8) //VOLTAJE de 0 a 10
//			    	{
//			    		
//			    		
//			    		int medicion = (buffer[3]*256)+buffer[2];
//			    		double voltaje  = ((double)medicion/1023)*voltaje_ref;
//			    		lbldatolectura.setText("Voltaje recibido: "+voltaje);
//			    		
//			    	}
//			    	else if (borna >7 && borna <12) //ESTADO BINARIO
//			    	{
//			    		
//			    		if (buffer[2]==170){
//			    			lbldatolectura.setText("El estado es: 1");
//			    			
//			    		}else if (buffer[2]==85){
//			    			lbldatolectura.setText("El estado es: 0");
//			    			
//			    		}
//			    			
//			    		
//			    		
//			    		
//			    	}
//			    	else if (borna >11 ) //VOLTAJE DE 0 a 5
//			    	{
//			    		
//			    		int medicion = (buffer[2]*256)+buffer[1];
//			    		double voltaje  = ((double)medicion/1023)*voltaje_ref;
//			    		lbldatolectura.setText("Voltaje recibido: "+voltaje);
//			    		
//			    		
//			    	}
//			    	
//			    	
////			    	
////			    	//Pasar el voltaje a la medida (el x2 se quitará más adelante
//			    	//double medida = (((rang_med_max-rang_med_min)/(rang_sal_max-rang_sal_min))*voltaje)+rang_med_min;
//			    	
//			    	
////			    	System.out.println("Total que la medicion es: "+medicion);
////			    	System.out.println("Voltaje recibido: "+ voltaje );
//			    	//System.out.println("La temperatura actual es de: "+medida+" ºC");
//			    	
//			    	//System.out.println("PULSOS anemometro: "+pulsos);
//			    	
//			    	//Duermo para recibir otra lectura
////			    	try {
////						Thread.sleep(5000);
////					} catch (InterruptedException e) {
////						// TODO Auto-generated catch block
////						e.printStackTrace();
////					}
//			    	serialPort.close();
//			    	
//			}
//					
//					
//				
//			});
//			btnNewButton.setBounds(141, 203, 180, 43);
//			frame.getContentPane().add(btnNewButton);
//			
//			JLabel labelvoltaje = new JLabel("Voltaje de referencia: ");
//			labelvoltaje.setBounds(39, 108, 158, 15);
//			frame.getContentPane().add(labelvoltaje);
//			
//			textField_1 = new JTextField();
//			textField_1.setColumns(10);
//			textField_1.setBounds(246, 108, 114, 19);
//			frame.getContentPane().add(textField_1);
//			
//			lblNewLabel = new JLabel("ProtoGON");
//			lblNewLabel.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 19));
//			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
//			lblNewLabel.setBounds(12, 0, 426, 43);
//			frame.getContentPane().add(lblNewLabel);
//			
//			
//			
//			
//
//			
//			
//			
//
//			
//			
//			
//			
//	    	
//		
//	}
//
//
//
//
//
//		private static void conecta() {
//			// TODO Auto-generated method stub
//			
//			String puerto="/dev/ttyACM1";
//			
//			 System.setProperty("gnu.io.rxtx.SerialPorts", puerto);
//		        CommPortIdentifier portIdentifier = null;
//		        
//		        //Por si la placa no tiene conexión propago el error y no ejecuto nada.
//		        while (portIdentifier == null){
//					try {
//						portIdentifier = CommPortIdentifier.getPortIdentifier(puerto);
//					} catch (NoSuchPortException e1) {
//						//TODO Controlar en el panelpral que hay conexión con las placas antes de que el error se propague!!!
//						
//						System.out.println("\nNo hay conexion con el puerto "+puerto+"\nEl usb de la placa está conectado?");
//						
//					}
//				
//		        }
//				
//				if ( portIdentifier.isCurrentlyOwned() ){
//		            System.out.println("Error: El puerto "+puerto+ " está siendo utilizado por otro proceso");
//				}
//		        else
//		        {
//		            CommPort commPort = null;
//					try {
//						commPort = portIdentifier.open(puerto,5000);
//					} catch (PortInUseException e) {
//						// 
//						e.printStackTrace();
//					}
//		            
//		            if ( commPort instanceof SerialPort )
//		            {
//		                
//		                serialPort = (SerialPort) commPort;
//		                
//		                
//		                //TODO Si algo falla con los serie QUITAR
//		                serialPort.disableReceiveTimeout();
//		                try {
//		        			serialPort.enableReceiveThreshold(1);
//		        		} catch (UnsupportedCommOperationException e) {
//		        			// 
//		        			e.printStackTrace();
//		        		}
//		                
//		                
//		                
//		                //
//		                //CONFIGURACION de LA CONEXION SERIE con varios ifs para cada placa !!
//		               
//		    					try {
//									serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
//								} catch (UnsupportedCommOperationException e1) {
//									// TODO Auto-generated catch block
//									e1.printStackTrace();
//								}
//		             
//		                
//		                try {
//							out = serialPort.getOutputStream();
//							in = serialPort.getInputStream();
//							
//							
//						} catch (IOException e) {
//							// 
//							e.printStackTrace();
//						}
//
//		            }
//		            else
//		            {
//		                System.out.println("Error: Only serial ports are handled by this example.");
//		            }
//		        }     
//			
//		}
//	}

		
	

