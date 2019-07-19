package panelespack;


import irrisoftpack.Irrisoft;
import irrisoftpack.SerialDriver;

import java.awt.event.ActionEvent;import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JRadioButton;

import valvulaspack.Valvula;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;




public class PanelBt2 extends JPanel {


		private static final long serialVersionUID = 1L;
		
		//SINGLETON
		private  static PanelBt2 instance;
		
		private JTextField numvalv;
		public JTextField textRx;
		public JTextField textTx;
		public JLabel lblInfo;
		public JLabel lblconsum;
		public JLabel lblEstado;
		protected SerialDriver conserie;
		private JRadioButton rdbtnStn1,rdbtnStn2;
		private JButton btnAbrir,btnCerrar,btnReset;
		private boolean retonno;

		
		
		
		public static PanelBt2 getInstance(){
			
			if (instance ==null){
				return new PanelBt2();
			}
			return instance;
		}
		
		
		private PanelBt2() {
			super();
			//this.setName("Panelmci");
			this.setBounds(10, 84, 465, 291);
			setLayout(null);
			
			
			JLabel placa1 = new JLabel("Blind Translator 2");
			placa1.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 14));
			placa1.setHorizontalAlignment(SwingConstants.CENTER);
			placa1.setBounds(169, 3, 146, 32);
			add(placa1);
		
			
			
			//BOTON ATRAS
			JButton btnAtras = new JButton("Atras");
			btnAtras.setBounds(186, 260, 117, 25);
			btnAtras.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					PanelBt2.this.setVisible(false);
					Irrisoft.window.panelpral.setVisible(true);
				}
			});
			add(btnAtras);
			
			JLabel lblestacion = new JLabel("Estación: ");
			lblestacion.setBounds(35, 48, 69, 15);
			add(lblestacion);
			
			numvalv = new JTextField();
			numvalv.setBounds(107, 46, 114, 19);
			add(numvalv);
			numvalv.setColumns(10);
			
			//filtro por si el usuario mete un numvalv que no corresponde. A REVISAR !!
			filtro();
			
			rdbtnStn1 = new JRadioButton("STN1");
			rdbtnStn1.setSelected(true);
			rdbtnStn1.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					rdbtnStn1.setSelected(true);
					rdbtnStn2.setSelected(false);
				}
			});
			rdbtnStn1.setBounds(270, 42, 60, 23);
			add(rdbtnStn1);
			
			
			rdbtnStn2 = new JRadioButton("STN2");
			rdbtnStn2.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					rdbtnStn2.setSelected(true);
					rdbtnStn1.setSelected(false);
				}
			});
			rdbtnStn2.setBounds(359, 42, 60, 23);
			add(rdbtnStn2);
			
			
			
			btnAbrir = new JButton("Abrir");
			btnAbrir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
				
					try {
						filtro();
						
						int valv;
						if (rdbtnStn1.isSelected()){
							rango(1);
							valv =Integer.parseInt(numvalv.getText())-1001;
							interruptor(Irrisoft.window.valvsbt2.getvalvbt2(valv),1,3);
						}
						else {
							rango(2);
							valv =Integer.parseInt(numvalv.getText())-2001;
							System.out.println("VALV: "+ numvalv.getText());
							interruptor(Irrisoft.window.valvsbt22.getvalvbt2(valv),1,4);
						}
							

						
					} catch (NumberFormatException e) {
						
					}	
				}
				
			});
			btnAbrir.setIcon(null);
			btnAbrir.setBounds(56, 81, 108, 25);
			add(btnAbrir);
			
			btnCerrar = new JButton("Cerrar");
			btnCerrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//TODO Aki hay que llamar a comprobar formato de numero de valvula !!
					try {
						filtro();
						int valv;
						if (rdbtnStn1.isSelected()){
							rango(1);
							valv =Integer.parseInt(numvalv.getText())-1001;
							interruptor(Irrisoft.window.valvsbt2.getvalvbt2(valv),2,3);
						}
						else {
							rango(2);
							valv =Integer.parseInt(numvalv.getText())-2001;
							interruptor(Irrisoft.window.valvsbt22.getvalvbt2(valv),2,4);
						}
						
					} catch (NumberFormatException e1) {
						
					}
				}
			});
			btnCerrar.setBounds(187, 81, 108, 25);
			add(btnCerrar);
			
			btnReset = new JButton("Reset");
			btnReset.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (rdbtnStn1.isSelected()){
						interruptor(Irrisoft.window.valvsbt2.getvalvbt2(0),3,3);
					}else{
						interruptor(Irrisoft.window.valvsbt2.getvalvbt2(0),3,4);
					}
				}
			});
			btnReset.setBounds(318, 81, 108, 25);
			add(btnReset);
			
			JLabel lblRx = new JLabel("RX");
			lblRx.setBounds(101, 156, 28, 15);
			add(lblRx);
			
			textRx = new JTextField();
			textRx.setEditable(false);
			textRx.setBounds(131, 154, 238, 19);
			add(textRx);
			textRx.setColumns(10);
			
			JLabel lblTx = new JLabel("TX");
			lblTx.setBounds(101, 127, 28, 15);
			add(lblTx);
			
			textTx = new JTextField();
			textTx.setEditable(false);
			textTx.setColumns(10);
			textTx.setBounds(131, 125, 238, 19);
			add(textTx);
			
			lblInfo = new JLabel("");
			lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
			lblInfo.setFont(new Font("Dialog", Font.PLAIN, 12));
			lblInfo.setBounds(56, 210, 363, 15);
			add(lblInfo);
			
			lblconsum = new JLabel("");
			lblconsum.setHorizontalAlignment(SwingConstants.CENTER);
			lblconsum.setFont(new Font("Dialog", Font.PLAIN, 12));
			lblconsum.setBounds(66, 234, 353, 15);
			add(lblconsum);
			
			lblEstado = new JLabel("");
			lblEstado.setHorizontalAlignment(SwingConstants.CENTER);
			lblEstado.setFont(new Font("Dialog", Font.PLAIN, 12));//Para saber si es la mci o la mci2
//			if (Integer.parseInt(valv.getCodelecvalv())<1120)
//			tipoplaca=3;
//		else
//			tipoplaca=4;
			lblEstado.setBounds(56, 190, 363, 15);
			add(lblEstado);
		
		}

		
protected boolean filtro(){
		
		numvalv.addKeyListener(new KeyAdapter() {
	    @Override
	    public void keyTyped(KeyEvent e) {

		
		char caracter = e.getKeyChar();
		
			if (rdbtnStn1.isSelected()) {
				
				try {
						  	//Enter
							if ((int)caracter==10){
								
							}else{
							  // Verificar si la tecla pulsada no es un digito
						      if(((caracter < '0') ||
						         (caracter > '9')) &&
						         (caracter != '\b' /*corresponde a BACK_SPACE*/))
						      {
						         e.consume();  // ignora el evento de teclado
						         aviso(1);
						         numvalv.setText(null);
						        
						      }
					
						}
				}catch (NumberFormatException e1) {	 
					
			 }	      
						      
		   }else if (rdbtnStn2.isSelected()){
			   try {
				 
				   //Enter
				   if ((int)caracter==10){
						
					}else{
				      // Verificar si la tecla pulsada no es un digito
				      if(((caracter < '0') ||
				         (caracter > '9')) &&
				         (caracter != '\b' /*corresponde a BACK_SPACE*/))
				      {
				         e.consume();  // ignora el evento de teclado
				         aviso(2);
				         numvalv.setText(null);
				        
				      }
					}
			 }catch (NumberFormatException e1) {	
			}
		   }
	    }
	   });
		
		return retonno;	

}

protected boolean rango(int i){
	
	boolean retonno=false;
	try {
		int z = Integer.parseInt(numvalv.getText());
		System.out.println("A ver rangor: "+z);
			if (i==1){
				if (z<1001 || z>1119){
					aviso(1);
					numvalv.setText(null);
					retonno=false;
				}else
					retonno=true;
			}else if (i==2){
				if (z<2001 || z>2119){
					aviso(2);
					numvalv.setText(null);
					retonno=false;//Para saber si es la mci o la mci2
//					if (Integer.parseInt(valv.getCodelecvalv())<1120)
//					tipoplaca=3;
//				else
//					tipoplaca=4;
				}
				else 
					retonno=true;
			}
	} catch (NumberFormatException e1) {
		// TODO Auto-generated catch block
		//e1.printStackTrace();
	}
	return retonno;
}

protected void aviso(int tipo){
	if (tipo==1){
		JOptionPane.showMessageDialog(Irrisoft.window.frmIrrisoft,
	
			"Introduzca un numero entre 1001 y 1119 (ambos incluidos)", //$NON-NLS-1$
			"Error", 
		    JOptionPane.ERROR_MESSAGE);
	 }
	else if (tipo==2){
		JOptionPane.showMessageDialog(Irrisoft.window.frmIrrisoft,
				
				"Introduzca un numero entre 2001 y 2119 (ambos incluidos)", //$NON-NLS-1$
				"Error", 
			    JOptionPane.ERROR_MESSAGE);
		 }
//	else if (tipo==3){
//				JOptionPane.showMessageDialog(Irrisoft.window.frmIrrisoft,
//				"Debe introducir un numero de estación !", //$NON-NLS-1$
//				"Error", 
//			    JOptionPane.ERROR_MESSAGE);
//		 }
	}
	



public void inicializavalvs(){
	
	//Para la placa Bt2
	for (int i=0;i<120;i++){
		Irrisoft.window.valvsbt2.addvalvbt2(new Valvula());
		Irrisoft.window.valvsbt2.getvalvbt2(i).setCodelecvalv(Integer.toString(1001+i));
	}
	//Para la placa Bt22
	for (int i=0;i<120;i++){
		Irrisoft.window.valvsbt22.addvalvbt2(new Valvula());
		Irrisoft.window.valvsbt22.getvalvbt2(i).setCodelecvalv(Integer.toString(2001+i));
	}

	
}

protected void interruptor (Valvula valv, int accion, int tipoplaca){
	
	conserie = new SerialDriver();
	System.out.println("Valvula: "+ valv.getCodelecvalv());
	
	
	if (tipoplaca == 3){
		try {
				conserie.conecta(Irrisoft.window.config.getBt2(),tipoplaca);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			System.out.println("Seguro que la bt2 está conectada??");
		}
	}else if (tipoplaca==4){
		try {
				conserie.conecta(Irrisoft.window.config.getBt22(),tipoplaca);
				
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			System.out.println("Seguro que la bt2 está conectada??");
		}
	}
	
		
		if (accion==1){
		
			if(!valv.isAbierta()){
			    conserie.abrevalv(valv.getCodelecvalv(),tipoplaca);
			    valv.setAbierta(true);
			   
			    }else{
			    	Irrisoft.window.textArea
					.append("\nLa válvula "+valv.getCodelecvalv()+" ya se encuentra abierta !");
			    }
			}
		
		if (accion==2){
			if (valv.isAbierta()){
				conserie.cierravalv(valv.getCodelecvalv(),tipoplaca);
			}else{
				Irrisoft.window.textArea
				.append("\nLa válvula "+valv.getCodelecvalv()+" ya se encuentra cerrada !");
			}
			valv.setAbierta(false);
			System.out.println("HOLA valvula "+valv.getCodelecvalv()+" está "+valv.isAbierta());
		}
		
		if (accion==3){
			conserie.reset();
		}
		
		//Cierro el puerto serie
				conserie.cierra();
		
}
}

