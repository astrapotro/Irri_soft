package panelespack;

import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sensorespack.HiloAmperimetro;
import sensorespack.Sensor;

public class Panelecturasmci extends JPanel implements PropertyChangeListener{

    
    private static Logger logger = LogManager.getLogger(Panelecturasmci.class
	    .getName());
   

    // SINGLETON
    private static Panelecturasmci instance;
    
    //JLabels de las lecturas
    private JLabel lbltitulo, lbllecturamp,lblectbr28,lblectbr27,lblectbr26,lblectbr25;
    
//    private boolean actualizar = true;
    
    public int tipo = -1;


    //A kitar !!! para ello hay que implementar los listeners en grafico_cau.java

    public int[] listaconsumosamp = new int[4];
    public int[] listapulsoscau = new int[4];
    public float[] listacaudales = new float [4];
    private Irrisoft IR;

    private ArrayList<JLabel> listalabelslect = new ArrayList<JLabel>();
    
    public static Panelecturasmci getInstance() {

   	if (instance == null) {
   	    return new Panelecturasmci();
   	}
   	return instance;
       }

    private Panelecturasmci() {
   	super();
   	this.IR = Irrisoft.window;
   	this.setBounds(10, 84, 465, 344);
   	setLayout(null);
   		
   		
   		lbltitulo = new JLabel("");
   		lbltitulo.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 15));
   		lbltitulo.setHorizontalAlignment(SwingConstants.CENTER);
   		lbltitulo.setBounds(12, 12, 441, 25);
   		add(lbltitulo);
   		
   		JLabel lblamperimetro = new JLabel("Amperímetro:");
   		lblamperimetro.setHorizontalAlignment(SwingConstants.RIGHT);
   		lblamperimetro.setBounds(38, 80, 104, 15);
   		add(lblamperimetro);
   		
   		JLabel lblbr25 = new JLabel("Br25:");
   		lblbr25.setHorizontalAlignment(SwingConstants.RIGHT);
   		lblbr25.setBounds(38, 107, 104, 15);
   		add(lblbr25);
   		
   		JLabel lblbr26 = new JLabel("Br26:");
   		lblbr26.setHorizontalAlignment(SwingConstants.RIGHT);
   		lblbr26.setBounds(38, 134, 104, 15);
   		add(lblbr26);
   		
   		JLabel lblbr27 = new JLabel("Br27:");
   		lblbr27.setHorizontalAlignment(SwingConstants.RIGHT);
   		lblbr27.setBounds(38, 161, 104, 15);
   		add(lblbr27);
   		
   		JLabel lblbr28 = new JLabel("Br28:");
   		lblbr28.setHorizontalAlignment(SwingConstants.RIGHT);
   		lblbr28.setBounds(38, 188, 104, 15);
   		add(lblbr28);
   		
   		lbllecturamp = new JLabel("");
   		lbllecturamp.setBounds(154, 80, 285, 15);
   		add(lbllecturamp);
   		lbllecturamp.setName("lbllecturamp");
   		listalabelslect.add(lbllecturamp);
   		
   		lblectbr25 = new JLabel("");
   		lblectbr25.setBounds(154, 107, 285, 15);
   		add(lblectbr25);
   		lblectbr25.setName("lblectbr25");
   		listalabelslect.add(lblectbr25);
   		
   		lblectbr26 = new JLabel("");
   		lblectbr26.setBounds(154, 134, 285, 15);
   		add(lblectbr26);
   		lblectbr26.setName("lblectbr26");
   		listalabelslect.add(lblectbr26);

   		lblectbr27 = new JLabel("");
   		lblectbr27.setBounds(154, 161, 285, 15);
   		add(lblectbr27);
   		lblectbr27.setName("lblectbr27");
   		listalabelslect.add(lblectbr27);
   		
   		lblectbr28 = new JLabel("");
   		lblectbr28.setBounds(154, 188, 285, 15);
   		add(lblectbr28);
   		lblectbr28.setName("lblectbr28");
   		listalabelslect.add(lblectbr28);
   		
   		
   		// BOTON ATRAS
   		JButton btnAtras = new JButton("Atras");
   		btnAtras.setBounds(178, 296, 117, 36);
   		btnAtras.addActionListener(new ActionListener() {
   		    public void actionPerformed(ActionEvent arg0) {
   			 //Buclo por los sensores para borrar los listeners
   			    for (int i=0; i<IR.sensores.size();i++){
       		
   		        	    	IR.sensores.get(i).removePropertyChangeListener("lectura", IR.panelecturasmci);
   		        	    	IR.sensores.get(i).removePropertyChangeListener("pulsos", IR.panelecturasmci);
   		        	    
   			    }
   			//dejará de acutalizar las lecturas
//   			Panelecturasmci.this.actualizar=false;
   			Panelecturasmci.this.setVisible(false);
   			IR.panelecturas.setVisible(true);
   		    }
   		});
   		add(btnAtras);
   
   		
  
   		
       }
    
    //Consigo el JLabel asociado a la borna del sensor
    public JLabel dalabel (int numborna){
	
	JLabel label = null;
	
	for (int i=0; i<listalabelslect .size();i++){	    
	    if (listalabelslect.get(i).getName().contains(Integer.toString(numborna))){
		
		label = listalabelslect.get(i);
		break;
	    }
	}
	
	return label;
	
    }
    

    //GETTERS y SETTERS
    public JLabel getLbltitulo() {
        return lbltitulo;
    }

    public void setLbltitulo(JLabel lbltitulo) {
        this.lbltitulo = lbltitulo;
    }
    
    public JLabel getLbllecturamp() {
        return lbllecturamp;
    }

    public JLabel getLblectbr28() {
        return lblectbr28;
    }

    public JLabel getLblectbr27() {
        return lblectbr27;
    }

    public JLabel getLblectbr26() {
        return lblectbr26;
    }

    public JLabel getLblectbr25() {
        return lblectbr25;
    }

    public void setLbllecturamp(JLabel lbllecturamp) {
        this.lbllecturamp = lbllecturamp;
    }

    public void setLblectbr28(JLabel lblectbr28) {
        this.lblectbr28 = lblectbr28;
    }

    public void setLblectbr27(JLabel lblectbr27) {
        this.lblectbr27 = lblectbr27;
    }

    public void setLblectbr26(JLabel lblectbr26) {
        this.lblectbr26 = lblectbr26;
    }

    public void setLblectbr25(JLabel lblectbr25) {
        this.lblectbr25 = lblectbr25;
    }

    //PAra que los cambios en las lecturas de los sensores se muestren en el panel correspondiente
    @Override
    public void propertyChange(PropertyChangeEvent e) {
	
	String nombreCampo = e.getPropertyName();
	
	Sensor sens = (Sensor) e.getSource();
	
	//Para las lecturas analógicas o resultados
	if("lectura".equals(nombreCampo)){
	    
	    if(sens.getTipo()== IrrisoftConstantes.SENSOR_AMPERIMETRO)
	    {
		this.lbllecturamp.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
	    }
	    else  if(sens.getTipo()== IrrisoftConstantes.SENSOR_CAUDALIMETRO)
	    {
		this.lblectbr28.setText(this.lblectbr28.getText().concat(" - "+(String)e.getNewValue()+" "+sens.getUni_med()));
	    }
	    else if(sens.getNum_borna()== 27)
	    {
		this.lblectbr27.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
	    }
	    else if(sens.getNum_borna()== 26)
	    {
		this.lblectbr26.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
	    }
	    else if(sens.getNum_borna()== 25)
	    {
		this.lblectbr25.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
	    }
	}
	//Para las lecturas digitales (pulsos)
	else if ("pulsos".equals(nombreCampo)){
	    
	    if(sens.getTipo()== IrrisoftConstantes.SENSOR_CAUDALIMETRO)
	    {
		
		this.lblectbr28.setText(sens.getNum_sensor()+" , "+e.getNewValue()+" pulsos");
	    }
	    else  if(sens.getTipo()== IrrisoftConstantes.SENSOR_ANEMOMETRO)
	    {
		
		this.lblectbr28.setText(sens.getNum_sensor()+" , "+e.getNewValue()+" pulsos");
	    }
	    else  if(sens.getTipo()== IrrisoftConstantes.SENSOR_PLUVIOMETRO)
	    {
		
		this.lblectbr28.setText(sens.getNum_sensor()+" , "+e.getNewValue()+" pulsos");
	    }
	    
	}
	
	
    }

}
