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


public class  SerialDriver {

	protected SerialPort serialPort;
	public OutputStream out;
	public InputStream in;
	protected int consumo;
	protected byte[] bufferconsum = new byte[6];
	protected byte[] buffer = new byte[6];

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
                
                
                try {
					out = serialPort.getOutputStream();
					in = serialPort.getInputStream();
					
					
				} catch (IOException e) {
					// 
					e.printStackTrace();
				}

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
    		
    		System.out.println("Codele: "+codelecvalv);
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
			System.out.println("Cierravalvbt2: "+(Integer.parseInt(codelecvalv)-1001));
    		if(Irrisoft.window.valvsbt2.getvalvbt2(Integer.parseInt(codelecvalv)-1001).isAbierta())
    			actuabt2(codelecvalv,false,2);
    	}else if (tipo==4){
    		System.out.println("Cierravalvbt22: "+(Integer.parseInt(codelecvalv)-2001));
    		if(Irrisoft.window.valvsbt22.getvalvbt2(Integer.parseInt(codelecvalv)-2001).isAbierta())
    			actuabt2(codelecvalv,false,2);
    		
    		
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
		System.out.println("CHECKSUM: "+TXCHK);
		
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


public synchronized int leeresp(boolean consum){
	
	//byte[] buffer = new byte[6];
    int leo;
    int len = 0;
    
    Irrisoft.window.panelbt2.lblconsum.setText("");
    Irrisoft.window.panelbt2.repaint();
    
	try
    {
		//System.out.println("Consum?: "+consum);
		
		 if(!consum){
			 Irrisoft.window.panelbt2.lblInfo.setText("");
			 Irrisoft.window.panelbt2.repaint();
    		while(len<5)
    		{
    				leo=in.read();
    				buffer[len]=(byte) leo;

    				//Sólo si no es lectura de consumo
	        				
	        	        	len++;
    			}
    			
    			if (len>0){
    					//TODO
    					//Para pasar el byte a binario
//    					String s =("0000000" + Integer.toBinaryString(0xFF & buffer[3])).replaceAll(".*(.{8})$", "$1");
//    					String s1 =("0000000" + Integer.toBinaryString(0xFF & buffer[4])).replaceAll(".*(.{8})$", "$1");
    				
    				 String s = String.format("%8s", Integer.toBinaryString(buffer[3] & 0xFF)).replace(' ', '0' );
//    				  System.out.println("byte respuesta cpl: "+ s);
//    				  System.out.println("buffer 3: "+buffer[3]);
//    				  System.out.println("bit 0: "+s.charAt(0));
    				
    					//Control de fallos CPL
    					if (Integer.parseInt(String.valueOf((s.charAt(7))))==0){
    						Irrisoft.window.panelbt2.lblInfo.setText("Error. No se ha podido completar la operación.");
    					}
    					if (Integer.parseInt(String.valueOf((s.charAt(5))))==1){
    						Irrisoft.window.panelbt2.lblInfo.setText("Error. Already on/off, doesn’t exist in database, 1-126");
    					}
    					
    					if (Integer.parseInt(String.valueOf((s.charAt(4))))==1){
    						Irrisoft.window.panelbt2.lblInfo.setText("Field line reseteable fuse has been operated");
    					} 
    					
    					if (Integer.parseInt(String.valueOf((s.charAt(3))))==1){
    						Irrisoft.window.panelbt2.lblInfo.setText("Sobrecarga en la linea > 1.6A , "+TXCMD);
    					}	
	
    			}
    		
    		
	    		}else{
	    			
	    			while(len<6)
	        		{
	        				leo=in.read();
	        				bufferconsum[len]=(byte) leo;
//        				System.out.println("CONSUMO");
//        				System.out.println(leo);
//        				System.out.println(bufferconsum.toString());
	        				//Sólo si no es lectura de consumo
	        				len++;
	        		}		  
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
			
			//consumo = Integer.toString(bufferconsum[3]).concat(Integer.toString(Math.abs(bufferconsum[4])));
			//Integer.parseInt(consumo,16);
			//System.out.println(Integer.toString(bufferconsum[3]));
			//System.out.println(Integer.toString(bufferconsum[4]));
			consumo = (Integer.parseInt(Integer.toString(bufferconsum[3]),16)*256)+Integer.parseInt(Integer.toString(Math.abs(bufferconsum[4])),16);
			
		//Irrisoft.window.panelbt2.lblconsum.setText("Consumo: "+Integer.parseInt(Integer.toString(buffer[4]),16)+" mA");
		Irrisoft.window.panelbt2.lblconsum.setText("Consumo acomulado en la línea: "+consumo+" mA");
	}
	System.out.println("COnsumo en leeresp: "+consumo);
	return consumo;
}


public synchronized void  consultconsum (){
    
	byte[] buftrans = { (byte) 0x0B,(byte) 0x00,(byte) 0x01, 
			   (byte)0x0C };
	
	try {
		out.write(buftrans);
	} catch (Exception e) {
		         System.out.println(e.getMessage());
	}	

}



public synchronized void ponedelayresp(){

//A 50 ms
	byte[] buftrans = { (byte) 0x0C,(byte) 0x00,(byte) 0x03, 
			   (byte)0x00, (byte)0x32, (byte)0x41 }; 

	//A 90 ms
//	byte[] buftrans = { (byte) 0x0C,(byte) 0x00,(byte) 0x03, 
//			   (byte)0x00, (byte) 0x5A, (byte) 0x69 }; 
	
	try {
		out.write(buftrans);
		
	} catch (Exception e) {
		         System.out.println(e.getMessage());
	}	
	
}



public synchronized void leeponedelayresp(){
	int leo = 0;
    int len = 0;
    byte[] bufferresp = new byte[4];
   
    while(len<4)
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
   	
}



public synchronized void preguntadelayresp(){

	byte[] buftrans = { (byte) 0x0D,(byte) 0x00,(byte) 0x01, (byte) 0x0E }; 
	
	try {
		
		out.write(buftrans);
		
	} catch (Exception e) {
		         System.out.println(e.getMessage());
	}	
}



public synchronized void leerespdelay() {
	int leo = 0;
    int len = 0;
    byte[] bufferdelay = new byte[6];

		//bufferedInputStream.skip(4);
    	//in.skip(4);

    while(len<6)
	{
    			
			try {
				leo=in.read();
			} catch (IOException e) {
				// 
				e.printStackTrace();
			}
			
			bufferdelay[len]=(byte) leo;

			//Sólo si no es lectura de consumo
			len++;
	}
    
    System.out.println("Respuesta delay BT2: "+(Integer.parseInt(Integer.toString(bufferdelay[0])))+" "+
    											+(Integer.parseInt(Integer.toString(bufferdelay[1])))+" "
    											+(Integer.parseInt(Integer.toString(bufferdelay[2])))+" "
    											+Integer.parseInt(Integer.toString(bufferdelay[3]))+" "+
    											+(Integer.parseInt(Integer.toString(bufferdelay[4])))+" "+
    											Integer.parseInt(Integer.toString(Math.abs(bufferdelay[5]))));

}

	
		
}







