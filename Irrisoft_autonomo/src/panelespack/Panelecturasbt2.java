package panelespack;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;

import javax.swing.JButton;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import java.io.File;
import org.jfree.chart.plot.*;
import java.io.*;
import java.text.AttributedCharacterIterator;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeriesCollection;

import sensorespack.Sensor;

import java.awt.Panel;
import java.awt.Canvas;
import java.awt.Button;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class Panelecturasbt2 extends JPanel implements PropertyChangeListener{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static Logger logger = LogManager.getLogger(Panelecturasbt2.class
	    .getName());

    // SINGLETON
    private static Panelecturasbt2 instance;
   
    // JLabels de las lecturas
    private JLabel lbltitulo, lbllecturamp, lblecturapulsos, lblectsensor1, lblecturacau,
	    lblectsensor2, lblectsensor3, lblectsensor4, lblectsensor5, lblectsensor6, lblectsensor7;

    //private boolean actualizar = true;

    public int tipo = -1;

    public int[] listaconsumosamp = new int[4];
    public int[] listapulsoscau = new int[4];
    public float[] listacaudales = {0,0,0,0};
    private Irrisoft IR;

    //public muestrainfo hilomuestrainfo;

    public Thread hiloinfo;

    public static Panelecturasbt2 getInstance() {

	if (instance == null) {
	    return new Panelecturasbt2();
	}
	return instance;
    }

    private Panelecturasbt2() {
	super();	
	this.IR = Irrisoft.window;
	this.setBounds(10, 84, 465, 348);
	setLayout(null);

	lbltitulo = new JLabel("");
	lbltitulo.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 15));
	lbltitulo.setHorizontalAlignment(SwingConstants.CENTER);
	lbltitulo.setBounds(12, 12, 441, 25);
	add(lbltitulo);

	lbltitulo = new JLabel("");
	lbltitulo.setFont(new Font("Dialog", Font.BOLD, 13));
	lbltitulo.setHorizontalAlignment(SwingConstants.CENTER);
	lbltitulo.setBounds(12, 12, 441, 25);
	add(lbltitulo);

	JLabel lblamperimetro = new JLabel("Amperímetro:");
	lblamperimetro.setHorizontalAlignment(SwingConstants.RIGHT);
	lblamperimetro.setBounds(32, 47, 104, 15);
	add(lblamperimetro);

	JLabel lblcaudalimetro = new JLabel("Caudalímetro:");
	lblcaudalimetro.setHorizontalAlignment(SwingConstants.RIGHT);
	lblcaudalimetro.setBounds(32, 74, 104, 15);
	add(lblcaudalimetro);

	lbllecturamp = new JLabel("");
	lbllecturamp.setBounds(148, 47, 285, 15);
	add(lbllecturamp);

	lblecturapulsos = new JLabel("");
	lblecturapulsos.setHorizontalAlignment(SwingConstants.LEFT);
	lblecturapulsos.setBounds(148, 70, 160, 19);
	add(lblecturapulsos);
	
	lblecturacau = new JLabel("");
	lblecturacau.setHorizontalAlignment(SwingConstants.LEFT);
	lblecturacau.setBounds(148, 95, 160, 21);
	add(lblecturacau);

	JLabel lblsensor1 = new JLabel("Sensor1:");
	lblsensor1.setHorizontalAlignment(SwingConstants.RIGHT);
	lblsensor1.setBounds(32, 128, 104, 15);
	add(lblsensor1);

	JLabel lblsensor2 = new JLabel("Sensor2:");
	lblsensor2.setHorizontalAlignment(SwingConstants.RIGHT);
	lblsensor2.setBounds(32, 155, 104, 15);
	add(lblsensor2);

	JLabel lblsensor3 = new JLabel("Sensor3:");
	lblsensor3.setHorizontalAlignment(SwingConstants.RIGHT);
	lblsensor3.setBounds(32, 182, 104, 15);
	add(lblsensor3);

	lblectsensor1 = new JLabel("");
	lblectsensor1.setBounds(148, 128, 285, 15);
	add(lblectsensor1);

	lblectsensor2 = new JLabel("");
	lblectsensor2.setBounds(148, 155, 285, 15);
	add(lblectsensor2);

	lblectsensor3 = new JLabel("");
	lblectsensor3.setBounds(148, 182, 285, 15);
	add(lblectsensor3);

	// BOTON ATRAS
	JButton btnAtras = new JButton("Atras");
	btnAtras.setBounds(178, 304, 117, 36);
	btnAtras.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		    for (int i=0; i<IR.sensores.size();i++){
	       		
	        	    	IR.sensores.get(i).removePropertyChangeListener("lectura", IR.panelecturasbt2);
	        	    	IR.sensores.get(i).removePropertyChangeListener("pulsos", IR.panelecturasbt2);
	        	    
		    }
		// dejará de acutalizar las lecturas
		//setActualizar(false);
		Panelecturasbt2.this.setVisible(false);
		IR.panelecturas.setVisible(true);
	    }
	});
	add(btnAtras);
	
	JButton btnGrafica = new JButton("Grafica caudal");
	btnGrafica.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		    
		  Thread hilografica = new Thread (IR.vent_graf_cau);
		  IR.vent_graf_cau.setPlaca(2);
		  IR.vent_graf_cau.setTipo(tipo);
		  IR.vent_graf_cau.setCaudal(true);  
	
		  //PAro el timer si está dado
		  if (IR.vent_graf_cau.timer!=null)
		      IR.vent_graf_cau.timer.stop();
		  
		  hilografica.start();
//		    
		  
		    
		}
	});
	btnGrafica.setBounds(314, 99, 139, 21);
	add(btnGrafica);
	
	JButton btnGraficaPulsos = new JButton("Grafica pulsos");
	btnGraficaPulsos.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent paramActionEvent) {
		    
		    	  Thread hilografica = new Thread (IR.vent_graf_cau);
		    	  IR.vent_graf_cau.queplaca = getLbltitulo().getText();
			  IR.vent_graf_cau.setPlaca(2);
			  IR.vent_graf_cau.setTipo(tipo);
			  IR.vent_graf_cau.setCaudal(false);
			  
			  //PAro el timer si está dado
			  if (IR.vent_graf_cau.timer!=null)
			      IR.vent_graf_cau.timer.stop();
			
			  hilografica.start();
			  
		    
		}
	});
	btnGraficaPulsos.setBounds(313, 68, 140, 20);
	add(btnGraficaPulsos);
	
	JLabel lblsensor4 = new JLabel("Sensor4:");
	lblsensor4.setHorizontalAlignment(SwingConstants.RIGHT);
	lblsensor4.setBounds(32, 209, 104, 15);
	add(lblsensor4);
	
	JLabel lblsensor5 = new JLabel("Sensor5:");
	lblsensor5.setHorizontalAlignment(SwingConstants.RIGHT);
	lblsensor5.setBounds(32, 230, 104, 15);
	add(lblsensor5);
	
	JLabel lblsesnor6 = new JLabel("Sensor6:");
	lblsesnor6.setHorizontalAlignment(SwingConstants.RIGHT);
	lblsesnor6.setBounds(32, 252, 104, 15);
	add(lblsesnor6);
	
	JLabel lblsensor7 = new JLabel("Sensor7:");
	lblsensor7.setHorizontalAlignment(SwingConstants.RIGHT);
	lblsensor7.setBounds(32, 271, 104, 15);
	add(lblsensor7);
	
	lblectsensor4 = new JLabel("");
	lblectsensor4.setBounds(148, 209, 285, 15);
	add(lblectsensor4);
	
	lblectsensor5 = new JLabel("");
	lblectsensor5.setBounds(148, 230, 285, 15);
	add(lblectsensor5);
	
	lblectsensor6 = new JLabel("");
	lblectsensor6.setBounds(148, 252, 285, 15);
	add(lblectsensor6);
	
	lblectsensor7 = new JLabel("");
	lblectsensor7.setBounds(148, 271, 285, 15);
	add(lblectsensor7);
	
    }

//    public class muestrainfo implements Runnable {
//
//	
//
//	@Override
//	public void run() {
//
//	    while (actualizar) {
//
//		logger.info("HOLA estoy en muestrainfo, "
//			+ listaconsumosamp[tipo]+" , "+listapulsoscau[tipo]);
//
//		lbllecturamp.setText("    "+listaconsumosamp[tipo] + " mA");
//		
//		lblecturapulsos.setText("    "+listapulsoscau[tipo] + " pulsos nuevos");
//		
//		lblecturacau.setText("    Caudal: "+listacaudales[tipo]+" l/s");
//
//		try {
//		    Thread.sleep(IrrisoftConstantes.DELAY_PA_LEC_10SEG);
//		} catch (InterruptedException e) {
//		    if(logger.isErrorEnabled()){
//			logger.error("Error, Hilo interrrumpido: " +e.getMessage());
//		    }
//		}
//
//	    }
//
//	}
//
//    }

//    public muestrainfo getHilomuestrainfo() {
//	
//	if (hilomuestrainfo==null){
//	    hilomuestrainfo= new muestrainfo();
//	}
//	return hilomuestrainfo;
//    }
//
//    public void setHilomuestrainfo(muestrainfo hilomuestrainfo) {
//	this.hilomuestrainfo = hilomuestrainfo;
//    }

    // GETTERS y SETTERS
    public JLabel getLbltitulo() {
	return lbltitulo;
    }

    public void setLbltitulo(JLabel lbltitulo) {
	this.lbltitulo = lbltitulo;
    }

    public JLabel getLbllecturamp() {
	return lbllecturamp;
    }

    public JLabel getLblecturacau() {
	return lblecturapulsos;
    }

    public JLabel getLblectsensor1() {
	return lblectsensor1;
    }

    public JLabel getLblectsensor2() {
	return lblectsensor2;
    }

    public JLabel getLblectsensor3() {
	return lblectsensor3;
    }

    public void setLbllecturamp(JLabel lbllecturamp) {
	this.lbllecturamp = lbllecturamp;
    }

    public void setLblecturacau(JLabel lblecturacau) {
	this.lblecturapulsos = lblecturacau;
    }

    public void setLblectsensor1(JLabel lblectsensor1) {
	this.lblectsensor1 = lblectsensor1;
    }

    public void setLblectsensor2(JLabel lblectsensor2) {
	this.lblectsensor2 = lblectsensor2;
    }

    public void setLblectsensor3(JLabel lblectsensor3) {
	this.lblectsensor3 = lblectsensor3;
    }

//    public boolean isActualizar() {
//        return actualizar;
//    }
//
//    public void setActualizar(boolean actualizar) {
//        this.actualizar = actualizar;
//    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {

	
	String nombreCampo = e.getPropertyName();

	
	Sensor sens = (Sensor) e.getSource();
	
	//Para las lecturas normales
	if("lectura".equals(nombreCampo)){
	    
	    if(sens.getTipo()== IrrisoftConstantes.SENSOR_AMPERIMETRO)
	    {
	
		this.lbllecturamp.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
	    }
	    else  if(sens.getTipo()== IrrisoftConstantes.SENSOR_CAUDALIMETRO)
	    {
		
		
		this.lblecturacau.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
		
	    }
	    else if(sens.getTipo()== IrrisoftConstantes.SENSOR_HIGROMETRO)
	    {
		if (lblectsensor1.getText()=="" || lblectsensor1.getText().contains(sens.getNum_sensor()))
		    this.lblectsensor1.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
		else if (lblectsensor2.getText()=="" || lblectsensor2.getText().contains(sens.getNum_sensor()))
		    this.lblectsensor2.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
		else if (lblectsensor3.getText()=="" || lblectsensor3.getText().contains(sens.getNum_sensor()))
		    this.lblectsensor3.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
		else if (lblectsensor4.getText()=="" || lblectsensor4.getText().contains(sens.getNum_sensor()))
		    this.lblectsensor4.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
		else if (lblectsensor5.getText()=="" || lblectsensor5.getText().contains(sens.getNum_sensor()))
		    this.lblectsensor5.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
		else if (lblectsensor6.getText()=="" || lblectsensor6.getText().contains(sens.getNum_sensor()))
		    this.lblectsensor6.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
		else if (lblectsensor7.getText()=="" || lblectsensor7.getText().contains(sens.getNum_sensor()))
		    this.lblectsensor7.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
		
	    }
	}
	//Para las lecturas digitales (pulsos)
	else if ("pulsos".equals(nombreCampo)){
	    
	    if(sens.getTipo()== IrrisoftConstantes.SENSOR_CAUDALIMETRO)
	    {
		
		this.lblecturapulsos.setText(sens.getNum_sensor()+" , "+e.getNewValue()+" pulsos");
	    }
	    
	    
	}
	
    }
}
