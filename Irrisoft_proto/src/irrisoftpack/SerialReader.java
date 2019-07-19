package irrisoftpack;

import java.io.IOException;
import java.io.InputStream;


public  class SerialReader implements Runnable 
{
   private InputStream in;
   private int tipocmd;
    
    public SerialReader ( InputStream in, int tipocmd)
    {
        this.in = in;
        this.tipocmd = tipocmd;
    }
    
    public synchronized void run ()
    {
    	byte[] buffer = new byte[5];
        int leo;
        int len = 0;
        try
        {
        	
        	
        		
        		while(len<5)
        		{
        				leo=in.read();
        				buffer[len]=(byte) leo;
        				
        				//Si no es consumo eléctrico(comando11), miro el byte CPL
        	        	if (tipocmd!=11){
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
	        				}
        				
        				len++;
        		}
        	    
        	    

                Irrisoft.window.panelbt2.textRx.setText(Irrisoft.window.getHex(buffer,true));
              
        	
        		
        
        		}      
        }
        catch ( IOException e )
        {
        	System.out.println(("Excepcion en la respuesta de bt2"));
            e.printStackTrace();
        }    
    }
}