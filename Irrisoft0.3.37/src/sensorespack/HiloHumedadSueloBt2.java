package sensorespack;

import irrisoftpack.Irrisoft;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;


public class HiloHumedadSueloBt2 extends Sensor implements Runnable {
	
	protected SerialPort serialPort;
	protected OutputStream out;
	protected InputStream in;
	protected String puerto;
	protected int tipo,len,leo;

	protected byte[] bufferresp = new byte[6];
	protected byte[] buftrans = { (byte) 0x06,(byte) 0x00,(byte) 0x03, 
			   (byte)0x00, (byte)0x02, (byte)0x0B };
	
	
	public HiloHumedadSueloBt2 (String puerto, int tipo) {
       
        this.puerto=puerto;
        this.tipo=tipo;

    }
	
	
public void run() {
		
		id=2;
		ListaSensores.getInstance().addsensor(this);
		System.out.println("Id sensor: "+ListaSensores.getInstance().getsens().get(id-1).id);
			
			
			while(true){
				
				
					try {
						
							System.out.println(("Estoy en hilohumedadsuelo"));
							
							//Tiempo variable
							Thread.sleep(11000);
							
								Irrisoft.window.semaforobt2.take();
								 System.out.println("puerto:"+puerto);
									conectaserial(puerto,tipo);
									leehumedad();
									desconectaserial();
								Irrisoft.window.semaforobt2.release();
							
							
							
					} catch (Exception e) {
						// e.printStackTrace();
						Irrisoft.window.textArea.append("\nCancelada lectura de humedad de suelo");
						return;
						
						
					}
					
				}
				
					
}
			
	
public synchronized void  conectaserial ( String puerto,int tipo) throws NoSuchPortException
    {
	   System.out.println("PUERTOOOO: "+puerto);
        System.setProperty("gnu.io.rxtx.SerialPorts", puerto);
        CommPortIdentifier portIdentifier = null;
		
        
        //Por si la placa no tiene conexión propago el error y no ejecuto nada.
        while (portIdentifier == null){
			try {
				portIdentifier = CommPortIdentifier.getPortIdentifier(puerto);
			} catch (NoSuchPortException e1) {
				//TODO Controlar en el panelpral que hay conexión con las placas antes de que el error se propague!!!
				
				Irrisoft.window.textArea.append("\nNo hay conexion con el puerto "+puerto+"\nEl usb de la placa está conectado?");
				throw e1;
			}
		
        }
        
        
        
		if ( portIdentifier.isCurrentlyOwned() ){
            System.out.println("Error: El puerto "+puerto+ " está siendo utilizado por otro proceso");
		}
        else
        {
            CommPort commPort = null;
			try {
				commPort = portIdentifier.open(this.getClass().getName(),5000);
			} catch (PortInUseException e) {
				// 
				e.printStackTrace();
			}
            
            if ( commPort instanceof SerialPort )
            {
                
                serialPort = (SerialPort) commPort;
                
                
                //TODO Si algo falla con los serie QUITAR
                serialPort.disableReceiveTimeout();
                try {
        			serialPort.enableReceiveThreshold(1);
        		} catch (UnsupportedCommOperationException e) {
        			// 
        			e.printStackTrace();
        		}
                
                
                
                //
                //CONFIGURACION de LA CONEXION SERIE con varios ifs para cada placa !!
                try {
                	if (tipo==1 || tipo ==2){
    					serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                	}else if (tipo==3 || tipo ==4){
    					serialPort.setSerialPortParams(1200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                	}
				} catch (UnsupportedCommOperationException e) {
					// 
					e.printStackTrace();
				}
                
                //InputStream in = serialPort.getInputStream();
                try {
					out = serialPort.getOutputStream();
					in = serialPort.getInputStream();
					//OJO CON ESTO
					
					
				} catch (IOException e) {
					// 
					e.printStackTrace();
				}
                
//                (new Thread(new SerialReader(in))).start();
//                (new Thread(new SerialWriter(out))).start();

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }
    
    
public synchronized void desconectaserial (){
    	serialPort.close();
    	
    }
	
		
public void leehumedad(){
		
		System.out.println("Hola toy en leehumedad");
				try {
						out.write(buftrans);
						out.flush();
						len=0;
						
					    while(len<6)
						{			
								try {
									leo=in.read();
								} catch (IOException e) {
									// 
									e.printStackTrace();
								}
							
									bufferresp[len]=(byte) leo;
								
								//Sólo si no es lectura de consumo
								len++;
						}
					    
					} catch (Exception e) {
						System.out.println(e.getMessage());
				        System.out.println(e.getStackTrace());
					}	
				
					System.out.println("La humedad del suelo es de :"+bufferresp[4]+"%");

}

	
	
} 