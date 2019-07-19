package packradio;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;




public class Astraradio {


	    public Astraradio()
	    {
	        super();
	    }
	    
	    void connect ( String portName ) throws Exception
	    {
	        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
	        if ( portIdentifier.isCurrentlyOwned() )
	        {
	            System.out.println("Error: Port is currently in use");
	        }
	        else
	        {
           CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
	            
	            if ( commPort instanceof SerialPort )
	            {
	                SerialPort serialPort = (SerialPort) commPort;
	                serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
	                
	                InputStream in = serialPort.getInputStream();
	                OutputStream out = serialPort.getOutputStream();
	                
	                (new Thread(new SerialReader(in))).start();
	                (new Thread(new SerialWriter(out))).start();

	            }
	            else
	            {
	                System.out.println("Error: Only serial ports are handled by this example.");
	            }
	        }     
	    }
	    
	    /** */
	    public  class SerialReader implements Runnable 
	    {
	       
	        InputStream in;

	        
	        public SerialReader ( InputStream in )
	        {
	            this.in = in;
	            
	        }
	        
	        public void run ()
	        {
	            //byte[] buffer = new byte[180];
	            byte len = -1;
	          
	          
	                try {
						while ( ( len = (byte) this.in.read()) > -1 )
						{
							//String z = Arrays.toString(buffer);
							//String s = new String(buffer,Charset.forName("UTF8"));
							
//							for (int i=0; i<buffer.length;i++){
//								//System.out.print(Byte.toString(buffer[i])+" , ");
//								if (buffer[i]!=0)
//									System.out.print(buffer[i]+" , ");
//							}
//							for (int i=0; i<buffer.length;i++){
//								//System.out.print(Byte.toString(buffer[i])+" , ");
//								if (buffer[i]!=0)
//									buffer[i]=0;
//							}
							
							//String text = new String(len, 0,len.SIZE, "ASCII");
							char ch = (char) len;
							System.out.print(ch);
							System.out.print(" ");
//							  for (int i=0; i < buffer.length; i++) {
//							      if (buffer[i]!=0)
//								  result +=buffer[i];
//							          //Integer.toString( ( buffer[i] & 0xff ) + 0x100, 16).substring( 1 );
//							  }
							//System.out.println(result);
							
							
							//String s = new String(buffer,0,len);
							//System.out.println(s);
							
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                
	        }       
	    }
	    

	    /** */
	    public class SerialWriter implements Runnable 
	    {
	        OutputStream out;
	        
	        public SerialWriter ( OutputStream out )
	        {
	            this.out = out;
	        }
	        
	        public void run ()
	        {
	            try
	            {                
	                byte c = 0;
	                while ( ( c = (byte) System.in.read()) > -1 )
	                {
	                    this.out.write(c);
	                }                
	            }
	            catch ( IOException e )
	            {
	                e.printStackTrace();
	            }            
	        }
	    }
	    
	    public static void main ( String[] args )
	    {
	        try
	        {
	            (new Astraradio()).connect("/dev/ttyUSB0");
	        }
	        catch ( Exception e )
	        {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
	}








	
	
	
	
	

