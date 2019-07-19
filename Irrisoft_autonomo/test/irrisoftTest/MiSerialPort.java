package irrisoftTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

public class MiSerialPort extends SerialPort {

    private static Logger logger = LogManager.getLogger(MiSerialPort.class
	    .getName());
    private boolean estado = false;
    private boolean estado1 = false;
    private boolean purgado = false;
    byte[] ba1 = {0,1,0,1,2,0};
    byte[] ba2 = {0,1,0,1,1,1};

    public MiSerialPort(String portName) {
	super(portName);
	// TODO Auto-generated constructor stub
    }

    public boolean openPort() throws jssc.SerialPortException {
	logger.warn("Entra en openPort de MiSerialPort");
	estado = true;
	return estado;
    }

    @Override
    public boolean isOpened() {
	logger.warn("Entra en isOpened de MiSerialPort");
	// TODO Auto-generated method stub
	return estado;
    }

    @Override
    public boolean writeBytes(byte[] buffer) throws SerialPortException {
	logger.warn("Entra en writeBytes de MiSerialPort");
	// TODO Auto-generated method stub
	//return super.writeBytes(buffer);
	estado1 = true;
	return estado1;
    }
    
    

    @Override
    public boolean purgePort(int flags) throws SerialPortException {
	logger.warn("Entra en purgePort de MiSerialPort");
	// TODO Auto-generated method stub
	//return super.purgePort(flags);
	purgado = true;
	return purgado;
    }

    @Override//MIRAR ESTE METODO PARA DIFERENTES READBYTES
    //Metodo run de HiloTemperatura, le paso un byte[6] = ba1
    public byte[] readBytes(int byteCount, int timeout)
	    throws SerialPortException, SerialPortTimeoutException {
	logger.warn("Entra en readBytes de MiSerialPort");
	// TODO Auto-generated method stub
//	return super.readBytes(byteCount, timeout);
	return ba2;
	
    }

    @Override
    public String getPortName() {
	logger.warn("Entra en getPortName de MiSerialPort");
	// TODO Auto-generated method stub
	String Puerto = "Puerto de Prueba";
//	return super.getPortName();
	return Puerto;
    }

    @Override
    public int getInputBufferBytesCount() throws SerialPortException {
	logger.warn("Entra en getInputBufferBytes de miserialPort");
	// TODO Auto-generated method stub
//	return super.getInputBufferBytesCount();
	return 6;
    }

    @Override
    public boolean setParams(int baudRate, int dataBits, int stopBits,
	    int parity) throws SerialPortException {
	logger.warn("Entra en setParams de MiSerialPort");
	// TODO Auto-generated method stub
	//return super.setParams(baudRate, dataBits, stopBits, parity);
	return true;
    }

    @Override
    public boolean setFlowControlMode(int mask) throws SerialPortException {
	logger.warn("Entra en setFlowControlMode de MiSerialPort");
	// TODO Auto-generated method stub
	//return super.setFlowControlMode(mask);
	return true;
    }

    @Override
    public boolean setDTR(boolean enabled) throws SerialPortException {
	logger.warn("Entra en setDTR de MiSerialPort");
	// TODO Auto-generated method stub
	//return super.setDTR(enabled);
	return true;
    }

    @Override
    public boolean closePort() throws SerialPortException {
	// TODO Auto-generated method stub
	//return super.closePort();
	return true;
    }
    
    
    
  

}
