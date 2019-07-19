package panelespack;
import irrisoftpack.Irrisoft;
import panelespack.ModelComboMci;
import panelespack.ModelComboBt2;
import sensorespack.HiloCaudalimetro;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import java.awt.Label;
import javax.swing.JComboBox;
import java.awt.Color;

import javax.swing.border.LineBorder;
import javax.swing.JOptionPane;

import valvulaspack.ListaValvBt2;
import valvulaspack.ListaValvMci;
import javax.swing.JProgressBar;


public class Paneltest extends JPanel {

	private static Paneltest instance;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public Label lblPulsosDat;
	public  Label lblCauDat;
	public  Label lblIntenDat;
	public  JLabel lbltesteando;
	public  Label lblintensidad;
	public  Label lblamp;
	public  Label lblls;
	public  Label lblpul;
	public  Label lblvalv;
	public JButton btnAtras;
	public JButton btnTestearBT2;
	public JButton btnTestearMCI;
	public JButton btntesteoini;
	public JProgressBar progressBar;
	private ModelComboMci modelomci= new ModelComboMci();
	private ModelComboBt2 modelobt2 = new ModelComboBt2();
	//private int numvalv;
	
	public HiloTest HiloTest;
	public Thread hilotest;
	
	public JComboBox<String> comboBoxMCI;
	public JComboBox<String> comboBoxBt2;
	
	private int i=0;
	
	
public static Paneltest getInstance(){
		
		if (instance ==null){
			return new Paneltest();
		}
		
		return instance;
		
	}

	private Paneltest() {
		super();
		this.setBounds(10, 84, 465, 291);
		setLayout(null);		
		
		JLabel lblTest = new JLabel("Testeo");
		lblTest.setHorizontalAlignment(SwingConstants.CENTER);
		lblTest.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 18));
		lblTest.setBounds(0, 0, 465, 27);
		add(lblTest);
		
		btnAtras = new JButton("Atras");
		btnAtras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
						
				if (btnAtras.getText().equalsIgnoreCase("Atras")){
					Paneltest.this.setVisible(false);
					Irrisoft.window.panelpral.setVisible(true);
				}
				else if (btnAtras.getText().equalsIgnoreCase("Cancelar")){
					if (Irrisoft.window.paneltest.HiloTest.inicial)
						 i =JOptionPane.showConfirmDialog(Irrisoft.window.frmIrrisoft, "Seguro que quiere cancelar el testeo inicial?", "", JOptionPane.YES_NO_OPTION);
					else if (Irrisoft.window.paneltest.HiloTest.inicial==false)
						 i =JOptionPane.showConfirmDialog(Irrisoft.window.frmIrrisoft, "Seguro que quiere cancelar el testeo particular?", "", JOptionPane.YES_NO_OPTION);

					if (i==0){
						//Si es una valvulamci
						if (Irrisoft.window.hilocaudal.tipovalv==1){
							//valv=(String)Irrisoft.window.paneltest.getModelomci().getSelectedItem();
							//cierro la valvula requerida
							Irrisoft.window.panelmci.interruptor(ListaValvMci.getInstance().getvalvmci(Irrisoft.window.paneltest.HiloTest.numvalv), ListaValvMci.getInstance().getvalvmci(Irrisoft.window.paneltest.HiloTest.numvalv).getImgasoc());
						}
						//Si es una valvulabt2
						else if (Irrisoft.window.hilocaudal.tipovalv==2){
							//valv=(String)Irrisoft.window.paneltest.getModelobt2().getSelectedItem();
							//cierro la valvula requerida 
							Irrisoft.window.panelbt2.interruptor(ListaValvBt2.getInstance().getvalvbt2(Integer.toString(Irrisoft.window.paneltest.HiloTest.numvalv)), 2,3);
						}
						
						//Inicializo todo
					Irrisoft.window.paneltest.HiloTest.inicializavars(false);
				
				}
				
			  }
			
			}
		});
		btnAtras.setBounds(336, 253, 117, 25);
		add(btnAtras);	
		
		JLabel label = new JLabel("MCI");
		label.setBounds(25, 85, 26, 15);
		add(label);
		
		JLabel label_1 = new JLabel("BT2");
		label_1.setBounds(25, 123, 26, 15);
		add(label_1);
		
		JLabel lblParticular = new JLabel("Testeo Particular");
		lblParticular.setFont(new Font("Dialog", Font.ITALIC, 12));
		lblParticular.setBounds(85, 53, 110, 15);
		add(lblParticular);
		
		btnTestearMCI = new JButton("Testear");
		btnTestearMCI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				HiloTest = new HiloTest(false,3,1);
				hilotest = new Thread (HiloTest);
				Irrisoft.window.paneltest.HiloTest.numvalv=Integer.parseInt((String)getModelomci().getSelectedItem())-1;
				hilotest.start();
				
			}
		});
		btnTestearMCI.setFont(new Font("Dialog", Font.ITALIC, 11));
		btnTestearMCI.setBounds(150, 80, 82, 24);
		add(btnTestearMCI);
		
		Label lblPulsos = new Label("Pulsos");
		lblPulsos.setBounds(25, 161, 49, 21);
		add(lblPulsos);
		
		Label lblCaudal = new Label("Caudal");
		lblCaudal.setBounds(26, 185, 49, 21);
		add(lblCaudal);
		
		Label lblIntensidad = new Label("Intensidad");
		lblIntensidad.setBounds(26, 209, 74, 21);
		add(lblIntensidad);
		
		lblPulsosDat = new Label("");
		lblPulsosDat.setBounds(80, 160, 141, 22);
		add(lblPulsosDat);
		
		lblCauDat = new Label("");
		lblCauDat.setBounds(81, 184, 141, 21);
		add(lblCauDat);
		
		lblIntenDat = new Label("");
		lblIntenDat.setBounds(104, 209, 118, 21);
		add(lblIntenDat);
		
		//
		comboBoxMCI = new JComboBox<String>(modelomci);
		comboBoxMCI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Saco el num de valvula del combobox
				//if (getModelomci().getSelectedItem()!=null)
						Irrisoft.window.hilocaudal.numvalv= Integer.parseInt((String)getModelomci().getSelectedItem())-1;
						
			}
		});
		comboBoxMCI.setBounds(59, 80, 79, 24);
		add(comboBoxMCI);
		
		comboBoxBt2 = new JComboBox<String>(modelobt2);
		comboBoxBt2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Saco el num de valvula del combobox
				Irrisoft.window.hilocaudal.numvalv = Integer.parseInt((String)getModelobt2().getSelectedItem());
				
			}
		});
		comboBoxBt2.setBounds(58, 118, 80, 24);
		add(comboBoxBt2);
		
		btnTestearBT2 = new JButton("Testear");
		btnTestearBT2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HiloTest = new HiloTest(false,3,2);
				hilotest = new Thread (HiloTest);
				Irrisoft.window.paneltest.HiloTest.numvalv=Integer.parseInt((String)getModelobt2().getSelectedItem());
				hilotest.start();
			}
		});
		btnTestearBT2.setFont(new Font("Dialog", Font.ITALIC, 11));
		btnTestearBT2.setBounds(150, 119, 82, 24);
		add(btnTestearBT2);
		
		Box verticalBox = Box.createVerticalBox();
		verticalBox.setBorder(new LineBorder(Color.GRAY));
		verticalBox.setBounds(14, 39, 230, 203);
		add(verticalBox);
		
		JLabel lblInicial = new JLabel("Testeo Inicial");
		lblInicial.setFont(new Font("Dialog", Font.ITALIC, 12));
		lblInicial.setBounds(309, 52, 91, 15);
		add(lblInicial);
		
		btntesteoini = new JButton("Testear");
		btntesteoini.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Irrisoft.window.hilocaudal.inicial=true;
				HiloTest = new HiloTest(true,3,0);
				hilotest = new Thread (HiloTest);
				Irrisoft.window.paneltest.HiloTest.numvalv=Irrisoft.window.hilocaudal.numvalv;
				hilotest.start();
			}
		});
		btntesteoini.setFont(new Font("Dialog", Font.ITALIC, 11));
		btntesteoini.setBounds(309, 79, 82, 24);
		add(btntesteoini);
		
		lblintensidad = new Label("Intensidad");
		lblintensidad.setBounds(267, 209, 74, 21);
		add(lblintensidad);
		
		Label lblcaudal = new Label("Caudal");
		lblcaudal.setBounds(267, 182, 49, 21);
		add(lblcaudal);
		
		Label lblpulsos = new Label("Pulsos");
		lblpulsos.setBounds(268, 154, 49, 21);
		add(lblpulsos);
		
		lblamp = new Label("");
		lblamp.setBounds(344, 208, 94, 22);
		add(lblamp);
		
		lblls = new Label("");
		lblls.setBounds(322, 181, 117, 21);
		add(lblls);
		
		lblpul = new Label("");
		lblpul.setBounds(325, 155, 105, 21);
		add(lblpul);
		
		Label lablElecvalv = new Label("Electroválvula");
		lablElecvalv.setBounds(267, 124, 94, 21);
		add(lablElecvalv);
		
		lblvalv = new Label("");
		lblvalv.setBounds(367, 123, 75, 22);
		add(lblvalv);
		
		Box verticalBox_1 = Box.createVerticalBox();
		verticalBox_1.setBorder(new LineBorder(Color.GRAY));
		verticalBox_1.setBounds(255, 39, 197, 203);
		add(verticalBox_1);
		
		lbltesteando = new JLabel("");
		lbltesteando.setFont(new Font("Dialog", Font.PLAIN, 12));
		lbltesteando.setBounds(14, 252, 146, 25);
		add(lbltesteando);
		
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setForeground(new Color(50, 205, 50));
		progressBar.setBounds(168, 253, 148, 25);
		add(progressBar);
	}
	

	public ModelComboMci getModelomci() {
		return modelomci;
	}

	public ModelComboBt2 getModelobt2() {
		return modelobt2;
	}

	public void setModelomci(ModelComboMci modelomci) {
		this.modelomci = modelomci;
	}

	public void setModelobt2(ModelComboBt2 modelobt2) {
		this.modelobt2 = modelobt2;
	}


}

