package sensorespack;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import irrisoftpack.Irrisoft;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class Hilosensores implements Runnable{

	private static Hilosensores mhilosensores;
	
	protected SerialPort serialPort;
	protected OutputStream out;
	protected InputStream in;
	private static String puerto;
	protected int tipo,len,leo;

	//protected byte[] bufferresp = new byte[6];
	
	
public synchronized static Hilosensores getInstance()
	{
		if (mhilosensores==null)
			mhilosensores=new Hilosensores();
		return mhilosensores;
	}
	
	
public void run() {
			
			conectaserial(getPuerto());
			System.out.println(("Estoy en hilosensores"));
									
}
			
	
public synchronized void  conectaserial ( String puerto) 
    {
	   System.out.println("PUERTOOOO: "+puerto);
        System.setProperty("gnu.io.rxtx.SerialPorts", puerto);
        CommPortIdentifier portIdentifier = null;
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier(puerto);
		} catch (NoSuchPortException e1) {
			//Controlar en el panelpral que hay conexión con las placas antes de que el error se propague!!!
			
			Irrisoft.window.textArea.append("\nNo hay conexion con el puerto "+puerto+"\nEl usb de la placa está conectado?");
			
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
                
                
                //Si algo falla con los serie QUITAR
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
    					serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                	
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
                
                (new Thread(new Leesens(in))).start();
               //(new Thread(new Escribepuerto(out))).start();

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

/** */
public static class Leesens implements Runnable {

    InputStream in;
    String respuesta;
    
    public Leesens ( InputStream in )
    {
        this.in = in;
    }
    
    public void run ()
    {
        byte[] buffer = new byte[1024];
        int len = -1;
        try
        {
            while (( len = this.in.read(buffer)) > -1 )
            {
            	respuesta = new String(buffer,0,len);
                System.out.print(respuesta);
                analizoresp(buffer);
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }            
    }
    

    //Se analiza la respuesta recibida 
    private int analizoresp(byte[] resp) {
    	int ret=0;
    	
    	//GON MANDA LECTURA
        if (resp[0]==3){
    		
        	//Si la borna es de contador
        	if (resp[1]>=1 && resp[1]<=5 ){
        		
        	}
        	//Si la borna es de sensor analógico
        	else if (resp[1]>=6 && resp[1]<=17 ){
        		
        	}
        	//Si la borna es de sensor digital
        	else if (resp[1]>=18 && resp[1]<=29 ){
        		
        	}
        	
    	}
    	//GON MANDA ACK/NACK
    	else if (resp[0]==4){
    		if (resp[1]==255){
    			System.out.println("Respuesta de la placa de sensores de gon recibida OK");
    		}else if (resp[1]==0){
    			System.out.println("Respuesta de la placa de senores de gon recibida KO");
    			ret=1;
    			//tendría que mandar otra vez la petición
    		}
    		
    	}
    	//GON MANDA LECTURA auto
    	else if (resp[0]==5){
    		
    		
    		
    	}
        
        return ret;
    			
    }

}



public synchronized void Escribesens (byte[] buffer) {
	
	try {
		out.write(buffer);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
}

public static String getPuerto() {
	return puerto;
}

public static void setPuerto(String puerto) {
	Hilosensores.puerto = puerto;
}



	
	
	
	
	
	
}
