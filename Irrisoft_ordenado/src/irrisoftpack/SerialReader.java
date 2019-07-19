package irrisoftpack;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public  class SerialReader implements Runnable 
{
	
	
   private OutputStream out;
   private InputStream in;

 
    
    public SerialReader ( InputStream in)
    {
        this.in = in;
        //this.out = out;
    }
    
    public synchronized void run ()
    {
    	//Recibo la respuesta de la apertura o cierre
    	leeresp(false);
    	
    	//Consulto y recibo el consumo de la operacion
//    	consultconsum();
//    	leeresp(true);
//    	
    }    
    
    
    private void leeresp(boolean consum){
    	
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
			System.out.println("HOLAAAAAA?????");
			Irrisoft.window.panelbt2.textRx.setText(Irrisoft.window.getHex(buffer,true));
		}
		
		else{
			Irrisoft.window.panelbt2.lblconsum.setText("Consumo: "+Integer.parseInt(Integer.toString(buffer[4]),16)+" mA");
		}
    }

    
    public  void  consultconsum (){
        
    	byte[] buftrans = { (byte) 0x0B,(byte) 0x00,(byte) 0x01, 
    			   (byte)0x0C };
    	
    	try {
    		out.write(buftrans);
    	} catch (Exception e) {
    		         System.out.println(e.getMessage());
    	}	
    
    }
    
}