package irrisoftpack;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//////////
//////////
//////////
////////////////
//////////////// Sólo funciona en linux con la libreria rxtx-2.2pre2-bins. Con inferiores casca el .so !!!!!!
//////////////// Con linux socat -d -d pty,raw pty,raw para emular puertos serie. 
//////////////// Cada terminal abierta se abre en un pts nuevo. OJO !!

/////////////// TO-DO Hay que cambiarlo para lectura/escritura orientada a eventos
public class  SerialDriver {

	protected SerialPort serialPort;
	protected OutputStream out;
	protected InputStream in;

	
	//Bytes para la BT2
	//Para decirle
	protected String TXLEN="04", TXSTN2, TXCHK, TXSLV, TXCMD;
	//Para recibir
    protected byte RXLEN, RXSTN1, RXSTN2, RXCHK, RXSLV, RXHDL, RXCMD, RXCPL;
	
    protected byte[] reset = { (byte) 0x00,(byte) 0x00,(byte) 0x00, 
			   				   (byte) 0x00, (byte) 0x00, (byte) 0x00,
			   				   (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
    
    
    public SerialDriver()
    {
        super();
    }
    
   public synchronized void  conecta ( String puerto,int tipo) 
    {
	   System.out.println("PUERTOOOO: "+puerto);
        System.setProperty("gnu.io.rxtx.SerialPorts", puerto);
        CommPortIdentifier portIdentifier = null;
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier(puerto);
		} catch (NoSuchPortException e1) {
			//TODO Controlar en el panelpral que hay conexión con las placas antes de que el error se propague!!!
			
			Irrisoft.window.textArea.append("\nNo hay conexion con el puerto "+puerto+"\nEl usb de la placa está conectado?");
			
		}
        if ( portIdentifier.isCurrentlyOwned() )
        {
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
    
    
   public synchronized void cierra (){
    	serialPort.close();
    	System.out.println("cerrado puerto");
    }


//    /** */
//    public static class SerialWriter implements Runnable 
//    {
//        OutputStream out;
//        
//        public SerialWriter ( OutputStream out )
//        {
//            this.out = out;
//        }
//        
//        public void run ()
//        {
//            try
//            {                
//                int c = 0;
//                while ( ( c = System.in.read()) > -1 )
//                {
//                    this.out.write(c);
//                }                
//            }
//            catch ( IOException e )
//            {
//                e.printStackTrace();
//            }            
//        }
//    }
    
  
    synchronized public void  abrevalv(String codelecvalv,int tipo){
   	
    	System.out.println("TIPOOOO: "+ tipo);
    	System.out.println("Codigo elect: "+codelecvalv);
    	
    	if (tipo==1 || tipo==2){
	    	String comando = "01" + codelecvalv;
	    	System.out.println(comando);
	    	try {
				out.write(comando.getBytes());
			} catch (IOException e) {
				// 
				e.printStackTrace();
			}
    	}else if (tipo==3){
    		
    		if(!Irrisoft.window.valvsbt2.getvalvbt2(Integer.parseInt(codelecvalv)-1001).isAbierta())
    			actuabt2(codelecvalv,true,1);
    		
    			
    	}else if (tipo==4){
    		System.out.println("Abrevalvbt22: "+(Integer.parseInt(codelecvalv)-2001));
    		if(!Irrisoft.window.valvsbt22.getvalvbt2(Integer.parseInt(codelecvalv)-2001).isAbierta())
    			actuabt2(codelecvalv,true,1);
    		
    	}
    	
    }
    
    
    synchronized public void cierravalv(String codelecvalv, int tipo){
   		
    	if (tipo==1 || tipo==2){
	   	 	String comando = "02" + codelecvalv;
	   	 	
	    	try {
				out.write(comando.getBytes()); 
				System.out.println("COmando cierre: " +comando);
			
			} catch (IOException e) {
				// 
				e.printStackTrace();
			}	
    	}else if (tipo==3){
    		if(Irrisoft.window.valvsbt2.getvalvbt2(Integer.parseInt(codelecvalv)-1001).isAbierta())
    			actuabt2(codelecvalv,false,2);
    	}else if (tipo==4){
    		System.out.println("Cierravalvbt22: "+(Integer.parseInt(codelecvalv)-2001));
    		if(Irrisoft.window.valvsbt22.getvalvbt2(Integer.parseInt(codelecvalv)-2001).isAbierta())
    			
    			actuabt2(codelecvalv,false,2);
    		//Liberar el semaforo
    		
    	}
    	
   }
    
    
   
    
public synchronized void reset(){

		try {
			out.write(reset);
			Irrisoft.window.panelbt2.textTx.setText(Irrisoft.window.getHex(reset,true));
			Irrisoft.window.panelbt2.textRx.setText(Irrisoft.window.getHex(reset,true));
			Irrisoft.window.panelbt2.lblInfo.setText("Bt2 reseteada !");
			Irrisoft.window.panelbt2.lblconsum.setText("");
		} catch (Exception e) {
			         System.out.println(e.getMessage());
		}
		
    } 

synchronized protected void actuabt2(String codelecvalv, boolean abrir, int tipocmd){
		
	//BACALAO de la bt2
	
		TXSLV="00";
		if (abrir){
			TXCMD="03";
		}else{
			TXCMD="04";
		}
		System.out.println("Codigo electrovalvula: "+codelecvalv);
		
		//////////////////////////// OJO
		//Calculo la valvula que quiero abrir con la bt2 o la bt22
		if (Integer.parseInt(codelecvalv)<2000)
			TXSTN2=Integer.toString(Integer.parseInt(codelecvalv)-1000);
		else
			TXSTN2=Integer.toString(Integer.parseInt(codelecvalv)-2000);
		
		TXCHK = Integer.toString(Integer.parseInt(TXCMD)+4+Integer.parseInt(TXSTN2)+Integer.parseInt(TXSLV));
		System.out.println(TXCHK);
		
		byte[] buftrans = { (byte) Integer.parseInt(TXCMD),(byte) Integer.parseInt(TXSLV),(byte) Integer.parseInt(TXLEN), 
					   (byte) 0x00, (byte) Integer.parseInt(TXSTN2), (byte) 0x00,
					   (byte) Integer.parseInt(TXCHK) };
		


		Irrisoft.window.panelbt2.textTx.setText(Irrisoft.window.getHex(buftrans,true));

			
		
			try {
				out.write(buftrans);
			} catch (Exception e) {
				         System.out.println(e.getMessage());
			}	
			
			 leeresp(false);
	
			consultconsum();
			
			leeresp(true);
			
			//Lanzo el hilo que escucha la respuesta (comentado porque el rendimiento dado no es el esperado)
			//new Thread(new SerialReader(in)).start();
			
			
			if (abrir)
				Irrisoft.window.panelbt2.lblEstado.setText("\nValvula "+codelecvalv+ " en la placa "+TXSLV+" abierta OK");
			else
				Irrisoft.window.panelbt2.lblEstado.setText("\nValvula "+codelecvalv+  " en la placa "+TXSLV+"  cerrada OK");
			
			
	
	}

private synchronized void leeresp(boolean consum){
	
	byte[] buffer = new byte[5];
    int leo;
    int len = 0;
	
	try
    {
		System.out.println("Consum?: "+consum);
		
    		while(len<5)
    		{
    				leo=in.read();
    				buffer[len]=(byte) leo;
    				System.out.println(leo);
    				//Sólo si no es lectura de consumo
    				   if(!consum){
	        				//Compruebo el byte de CPL
	        				if (len==3){
	        					
	        					//Para pasar el byte a binario
	        					String s =("0000000" + Integer.toBinaryString(0xFF & buffer[3])).replaceAll(".*(.{8})$", "$1");
	        					
	        					//Control de fallos CPL
	        					if (Integer.parseInt(String.valueOf((s.charAt(7))))==0){
	        						Irrisoft.window.panelbt2.lblInfo.setText("Error. No se ha podido completar la operación.");
	        					}
	        					if (Integer.parseInt(String.valueOf((s.charAt(5))))==1){
	        						Irrisoft.window.panelbt2.lblInfo.setText("Error. Already on/off, doesn’t exist in database, 1-126");
	        					}
	        					
	        					if (Integer.parseInt(String.valueOf((s.charAt(4))))==1){
	        						Irrisoft.window.panelbt2.lblInfo.setText("Error. Field line reseteable fuse has been operated");
	        					} 
	        					
	        					if (Integer.parseInt(String.valueOf((s.charAt(3))))==1){
	        						Irrisoft.window.panelbt2.lblInfo.setText("Sobrecarga en la linea > 1.6A");
	        					} 
	        					
//	        					//Control de fallos CPL
//	        					if (Integer.parseInt(String.valueOf((s.charAt(0))))==0){
//	        						Irrisoft.window.panelbt2.lblInfo.setText("Error. No se ha podido completar la operación.");
//	        					}
//	        					if (Integer.parseInt(String.valueOf((s.charAt(2))))==1){
//	        						Irrisoft.window.panelbt2.lblInfo.setText("Error. Already on/off, doesn’t exist in database, 1-126");
//	        					}
//	        					
//	        					if (Integer.parseInt(String.valueOf((s.charAt(3))))==1){
//	        						Irrisoft.window.panelbt2.lblInfo.setText("Error. Field line reseteable fuse has been operated");
//	        					} 
//	        					
//	        					if (Integer.parseInt(String.valueOf((s.charAt(4))))==1){
//	        						Irrisoft.window.panelbt2.lblInfo.setText("Sobrecarga en la linea > 1.6A");
//	        					} 
	        				}
    					}
        		
    	        	len++;
    	  
    		}
    		
    		
    }
    catch ( IOException e )
    {
    	System.out.println(("Excepcion en la respuesta de bt2"));
        e.printStackTrace();
    }  
	
	
	//Si no hay que leer consumo
	if (consum==false){
		Irrisoft.window.panelbt2.textRx.setText(Irrisoft.window.getHex(buffer,true));
	}
	
	else{
		Irrisoft.window.panelbt2.lblconsum.setText("Consumo: "+Integer.parseInt(Integer.toString(buffer[4]),16)+" mA");
	}
}


private  synchronized void  consultconsum (){
    
	byte[] buftrans = { (byte) 0x0B,(byte) 0x00,(byte) 0x01, 
			   (byte)0x0C };
	
	try {
		out.write(buftrans);
	} catch (Exception e) {
		         System.out.println(e.getMessage());
	}	

}

	
	
		
	
}







