package panelespack;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sensorespack.Sensor;

import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;

import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class Panelecturasens extends JPanel implements PropertyChangeListener {

    private static Logger logger = LogManager.getLogger(Panelecturasens.class
	    .getName());

    // SINGLETON
    private static Panelecturasens instance;

    // JLabels de las lecturas
    private JLabel lbltitulo;

    private boolean actualizar = true;

    public int tipo = -1;

    public int[] listaconsumosamp = new int[4];
    public int[] listapulsoscau = new int[4];
    public float[] listacaudales = new float[4];
    public ArrayList<JLabel> listalabelslect = new ArrayList<JLabel>();
    private Irrisoft IR;
    
    private JLabel lblbr1, lblbr2, lblbr3, lblbr4, lblbr5, lblbr6, lblbr7,
	    lblbr8, lblbr9, lblbr10, lblbr11, lblbr12, lblbr13, lblbr14,
	    lblbr15;
    
    private JLabel lblectbr1, lblectbr2, lblectbr3, lblectbr4, lblectbr5,
	    lblectbr6, lblectbr7, lblectbr8, lblectbr9, lblectbr10, lblectbr11,
	    lblectbr12, lblectbr13, lblectbr14, lblectbr15;

    

    public static Panelecturasens getInstance() {

	if (instance == null) {
	    return new Panelecturasens();
	}
	return instance;
    }

    private Panelecturasens() {
	super();
	this.IR = Irrisoft.window;
	this.setBounds(10, 84, 465, 346);
	setLayout(null);

	lbltitulo = new JLabel("Placa de Sensores");
	lbltitulo.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 15));
	lbltitulo.setHorizontalAlignment(SwingConstants.CENTER);
	lbltitulo.setBounds(12, 12, 441, 21);
	add(lbltitulo);

	lblbr1 = new JLabel("Br1:");
	lblbr1.setHorizontalAlignment(SwingConstants.RIGHT);
	lblbr1.setBounds(0, 52, 36, 15);
	add(lblbr1);

	lblbr2 = new JLabel("Br2:");
	lblbr2.setHorizontalAlignment(SwingConstants.RIGHT);
	lblbr2.setBounds(0, 79, 36, 15);
	add(lblbr2);

	lblbr3 = new JLabel("Br3:");
	lblbr3.setHorizontalAlignment(SwingConstants.RIGHT);
	lblbr3.setBounds(0, 106, 36, 15);
	add(lblbr3);

	lblbr4 = new JLabel("Br4:");
	lblbr4.setHorizontalAlignment(SwingConstants.RIGHT);
	lblbr4.setBounds(0, 133, 36, 15);
	add(lblbr4);

	lblbr5 = new JLabel("Br5:");
	lblbr5.setHorizontalAlignment(SwingConstants.RIGHT);
	lblbr5.setBounds(0, 160, 36, 15);
	add(lblbr5);

	lblbr6 = new JLabel("Br6:");
	lblbr6.setHorizontalAlignment(SwingConstants.RIGHT);
	lblbr6.setBounds(0, 187, 36, 15);
	add(lblbr6);

	lblbr7 = new JLabel("Br7:");
	lblbr7.setHorizontalAlignment(SwingConstants.RIGHT);
	lblbr7.setBounds(0, 214, 36, 15);
	add(lblbr7);

	lblbr8 = new JLabel("Br8:");
	lblbr8.setHorizontalAlignment(SwingConstants.RIGHT);
	lblbr8.setBounds(243, 52, 36, 15);
	add(lblbr8);

	lblbr9 = new JLabel("Br9:");
	lblbr9.setHorizontalAlignment(SwingConstants.RIGHT);
	lblbr9.setBounds(243, 79, 36, 15);
	add(lblbr9);

	lblbr10 = new JLabel("Br10:");
	lblbr10.setHorizontalAlignment(SwingConstants.RIGHT);
	lblbr10.setBounds(243, 106, 36, 15);
	add(lblbr10);

	lblbr11 = new JLabel("Br11:");
	lblbr11.setHorizontalAlignment(SwingConstants.RIGHT);
	lblbr11.setBounds(243, 133, 36, 15);
	add(lblbr11);

	lblbr12 = new JLabel("Br12:");
	lblbr12.setHorizontalAlignment(SwingConstants.RIGHT);
	lblbr12.setBounds(243, 160, 36, 15);
	add(lblbr12);

	lblbr13 = new JLabel("Br13:");
	lblbr13.setHorizontalAlignment(SwingConstants.RIGHT);
	lblbr13.setBounds(243, 187, 36, 15);
	add(lblbr13);

	lblbr14 = new JLabel("Br14:");
	lblbr14.setHorizontalAlignment(SwingConstants.RIGHT);
	lblbr14.setBounds(243, 214, 36, 15);
	add(lblbr14);

	lblbr15 = new JLabel("Br15:");
	lblbr15.setHorizontalAlignment(SwingConstants.RIGHT);
	lblbr15.setBounds(243, 241, 36, 15);
	add(lblbr15);
	
	
	lblectbr1 = new JLabel("");
	lblectbr1.setFont(new Font("Dialog", Font.PLAIN, 11));
	lblectbr1.setBounds(48, 52, 183, 15);
	add(lblectbr1);
	lblectbr1.setName("lblectbr1");
	listalabelslect.add(lblectbr1);

	lblectbr2 = new JLabel("");
	lblectbr2.setFont(new Font("Dialog", Font.PLAIN, 11));
	lblectbr2.setBounds(48, 79, 183, 15);
	add(lblectbr2);
	lblectbr2.setName("lblectbr2");
	listalabelslect.add(lblectbr2);
	

	lblectbr3 = new JLabel("");
	lblectbr3.setFont(new Font("Dialog", Font.PLAIN, 11));
	lblectbr3.setBounds(48, 106, 183, 15);
	add(lblectbr3);
	lblectbr3.setName("lblectbr3");
	listalabelslect.add(lblectbr3);

	lblectbr4 = new JLabel("");
	lblectbr4.setFont(new Font("Dialog", Font.PLAIN, 11));
	lblectbr4.setBounds(48, 133, 183, 15);
	add(lblectbr4);
	lblectbr4.setName("lblectbr4");
	listalabelslect.add(lblectbr4);

	lblectbr5 = new JLabel("");
	lblectbr5.setFont(new Font("Dialog", Font.PLAIN, 11));
	lblectbr5.setBounds(48, 160, 183, 15);
	add(lblectbr5);
	lblectbr5.setName("lblectbr5");
	listalabelslect.add(lblectbr5);

	lblectbr6 = new JLabel("");
	lblectbr6.setFont(new Font("Dialog", Font.PLAIN, 11));
	lblectbr6.setBounds(48, 187, 183, 15);
	add(lblectbr6);
	lblectbr6.setName("lblectbr6");
	listalabelslect.add(lblectbr6);

	lblectbr7 = new JLabel("");
	lblectbr7.setFont(new Font("Dialog", Font.PLAIN, 11));
	lblectbr7.setBounds(48, 214, 183, 15);
	add(lblectbr7);
	lblectbr7.setName("lblectbr7");
	listalabelslect.add(lblectbr7);

	lblectbr8 = new JLabel("");
	lblectbr8.setFont(new Font("Dialog", Font.PLAIN, 11));
	lblectbr8.setBounds(291, 52, 171, 15);
	add(lblectbr8);
	lblectbr8.setName("lblectbr8");
	listalabelslect.add(lblectbr8);

	lblectbr9 = new JLabel("");
	lblectbr9.setFont(new Font("Dialog", Font.PLAIN, 11));
	lblectbr9.setBounds(291, 79, 170, 15);
	add(lblectbr9);
	lblectbr9.setName("lblectbr9");
	listalabelslect.add(lblectbr9);

	lblectbr10 = new JLabel("");
	lblectbr10.setFont(new Font("Dialog", Font.PLAIN, 11));
	lblectbr10.setBounds(291, 106, 170, 15);
	add(lblectbr10);
	lblectbr10.setName("lblectbr10");
	listalabelslect.add(lblectbr10);

	lblectbr11 = new JLabel("");
	lblectbr11.setFont(new Font("Dialog", Font.PLAIN, 11));
	lblectbr11.setBounds(291, 133, 170, 15);
	add(lblectbr11);
	lblectbr11.setName("lblectbr11");
	listalabelslect.add(lblectbr11);

	lblectbr12 = new JLabel("");
	lblectbr12.setFont(new Font("Dialog", Font.PLAIN, 11));
	lblectbr12.setBounds(291, 160, 170, 15);
	add(lblectbr12);
	lblectbr12.setName("lblectbr12");
	listalabelslect.add(lblectbr12);

	lblectbr13 = new JLabel("");
	lblectbr13.setFont(new Font("Dialog", Font.PLAIN, 11));
	lblectbr13.setBounds(291, 187, 170, 15);
	add(lblectbr13);
	lblectbr13.setName("lblectbr13");
	listalabelslect.add(lblectbr13);

	lblectbr14 = new JLabel("");
	lblectbr14.setFont(new Font("Dialog", Font.PLAIN, 11));
	lblectbr14.setBounds(291, 214, 170, 15);
	add(lblectbr14);
	lblectbr14.setName("lblectbr14");
	listalabelslect.add(lblectbr14);

	lblectbr15 = new JLabel("");
	lblectbr15.setFont(new Font("Dialog", Font.PLAIN, 11));
	lblectbr15.setBounds(291, 241, 170, 15);
	add(lblectbr15);
	lblectbr15.setName("lblectbr15");
	listalabelslect.add(lblectbr15);

	JSeparator separator = new JSeparator();
	separator.setOrientation(SwingConstants.VERTICAL);
	separator.setBounds(229, 45, 2, 227);
	add(separator);

	// BOTON ATRAS
	JButton btnAtras = new JButton("Atras");
	btnAtras.setBounds(178, 296, 117, 36);
	btnAtras.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		    for (int i=0; i<IR.sensores.size();i++){
	       		
	        	    	IR.sensores.get(i).removePropertyChangeListener("lectura", IR.panelecturasens);
	        	    	IR.sensores.get(i).removePropertyChangeListener("pulsos", IR.panelecturasens);
	        	    
		    }

		// dejará de actualizar las lecturas
		Panelecturasens.this.actualizar = false;
		Panelecturasens.this.setVisible(false);
		IR.panelecturas.setVisible(true);
	    }
	});
	add(btnAtras);

    }
    
    //Consigo el JLabel asociado a la borna del sensor
    public JLabel dalabel (int numborna){
	
	JLabel label = null;
	
	for (int i=0; i<listalabelslect.size();i++){	    
	    if (listalabelslect.get(i).getName().contains(Integer.toString(numborna))){
		
		label = listalabelslect.get(i);
		break;
	    }
	}
	
	return label;
	
    }

    // GETTERS y SETTERS
    public JLabel getLbltitulo() {
	return lbltitulo;
    }

    public void setLbltitulo(JLabel lbltitulo) {
	this.lbltitulo = lbltitulo;
    }

    public JLabel getLblectbr1() {
        return lblectbr1;
    }

    public JLabel getLblectbr2() {
        return lblectbr2;
    }

    public JLabel getLblectbr3() {
        return lblectbr3;
    }

    public JLabel getLblectbr4() {
        return lblectbr4;
    }

    public JLabel getLblectbr5() {
        return lblectbr5;
    }

    public JLabel getLblectbr6() {
        return lblectbr6;
    }

    public JLabel getLblectbr7() {
        return lblectbr7;
    }

    public JLabel getLblectbr8() {
        return lblectbr8;
    }

    public JLabel getLblectbr9() {
        return lblectbr9;
    }

    public JLabel getLblectbr10() {
        return lblectbr10;
    }

    public JLabel getLblectbr11() {
        return lblectbr11;
    }

    public JLabel getLblectbr12() {
        return lblectbr12;
    }

    public JLabel getLblectbr13() {
        return lblectbr13;
    }

    public JLabel getLblectbr14() {
        return lblectbr14;
    }

    public JLabel getLblectbr15() {
        return lblectbr15;
    }

    public void setLblectbr1(JLabel lblectbr1) {
        this.lblectbr1 = lblectbr1;
    }

    public void setLblectbr2(JLabel lblectbr2) {
        this.lblectbr2 = lblectbr2;
    }

    public void setLblectbr3(JLabel lblectbr3) {
        this.lblectbr3 = lblectbr3;
    }

    public void setLblectbr4(JLabel lblectbr4) {
        this.lblectbr4 = lblectbr4;
    }

    public void setLblectbr5(JLabel lblectbr5) {
        this.lblectbr5 = lblectbr5;
    }

    public void setLblectbr6(JLabel lblectbr6) {
        this.lblectbr6 = lblectbr6;
    }

    public void setLblectbr7(JLabel lblectbr7) {
        this.lblectbr7 = lblectbr7;
    }

    public void setLblectbr8(JLabel lblectbr8) {
        this.lblectbr8 = lblectbr8;
    }

    public void setLblectbr9(JLabel lblectbr9) {
        this.lblectbr9 = lblectbr9;
    }

    public void setLblectbr10(JLabel lblectbr10) {
        this.lblectbr10 = lblectbr10;
    }

    public void setLblectbr11(JLabel lblectbr11) {
        this.lblectbr11 = lblectbr11;
    }

    public void setLblectbr12(JLabel lblectbr12) {
        this.lblectbr12 = lblectbr12;
    }

    public void setLblectbr13(JLabel lblectbr13) {
        this.lblectbr13 = lblectbr13;
    }

    public void setLblectbr14(JLabel lblectbr14) {
        this.lblectbr14 = lblectbr14;
    }

    public void setLblectbr15(JLabel lblectbr15) {
        this.lblectbr15 = lblectbr15;
    }
    
    
    //PAra que los cambios en las lecturas de los sensores se muestren en el panel correspondiente
    @Override
    public void propertyChange(PropertyChangeEvent e) {
	

	
	String nombreCampo = e.getPropertyName();

	
	Sensor sens = (Sensor) e.getSource();
	
	//Para las lecturas analógicas o resultados
	if("lectura".equals(nombreCampo)){
	    //Sensores digitales
	    if(sens.getNum_borna()== 1)
	    {
		this.lblectbr1.setText(this.lblectbr1.getText().concat(" - "+(String)e.getNewValue()+" "+sens.getUni_med()));
	    }
	    else  if(sens.getNum_borna()== 2)
	    {
		this.lblectbr2.setText(this.lblectbr2.getText().concat(" - "+(String)e.getNewValue()+" "+sens.getUni_med()));
		
	    }
	    else if(sens.getNum_borna()== 3)
	    {
		this.lblectbr3.setText(this.lblectbr3.getText().concat(" - "+(String)e.getNewValue()+" "+sens.getUni_med()));
	    }
	    
	    //Sensores analógicos (10 voltios)
	    else if(sens.getNum_borna()== 4)
	    {
	
		this.lblectbr4.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
	    }
	    else if(sens.getNum_borna()== 5)
	    {
		this.lblectbr5.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
	    }
	    else if(sens.getNum_borna()== 6)
	    {
		this.lblectbr6.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
	    }
	    else if(sens.getNum_borna()== 7)
	    {
		this.lblectbr7.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
	    }
	    
	    //Sensores binarios TODO hay que revisar cómo imprimimos la información
	    else if(sens.getNum_borna()== 8)
	    {
		this.lblectbr8.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
	    }
	    else if(sens.getNum_borna()== 9)
	    {
		this.lblectbr9.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
	    }
	    else if(sens.getNum_borna()== 10)
	    {
		this.lblectbr10.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
	    }
	    else if(sens.getNum_borna()== 11)
	    {
		this.lblectbr11.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
	    }
	    //Sensores analógicos (5 voltios)
	    else if(sens.getNum_borna()== 12)
	    {
		this.lblectbr12.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
	    }
	    else if(sens.getNum_borna()== 13)
	    {
		this.lblectbr13.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
	    }
	    else if(sens.getNum_borna()== 14)
	    {
		this.lblectbr14.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
	    }
	    else if(sens.getNum_borna()== 15)
	    {
		this.lblectbr15.setText(sens.getNum_sensor()+" , "+(String)e.getNewValue()+ " "+sens.getUni_med());
	    }
	    
	    
	    
	}
	//Para las lecturas digitales (pulsos)
	else if ("pulsos".equals(nombreCampo)){
	    
	    if(sens.getNum_borna()== 1)
	    {
		this.lblectbr1.setText(sens.getNum_sensor()+" , "+e.getNewValue()+" pulsos");
	    }
	    else  if(sens.getNum_borna()== 2)
	    {
		this.lblectbr2.setText(sens.getNum_sensor()+" , "+e.getNewValue()+" pulsos");
	    }
	    else  if(sens.getNum_borna()== 3)
	    {
		this.lblectbr3.setText(sens.getNum_sensor()+" , "+e.getNewValue()+" pulsos");
	    }
	    
	}
	
	
    }


}
