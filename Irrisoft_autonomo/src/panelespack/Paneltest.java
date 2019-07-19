package panelespack;

import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Paneltest extends JPanel {

    private static Paneltest instance;
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private static Logger logger = LogManager.getLogger(Paneltest.class
	    .getName());

    public Label lblPulsosDat;
    public Label lblCauDat;
    public Label lblIntenDat;
    public JLabel lbltesteando;
    public JLabel lblintensidad;
    public Label lblamp;
    public Label lblls;
    public Label lblpul;
    public Label lblvalv;
    public JButton btnAtras;
    public JButton btnTestearBT2;
    public JButton btnTestearMCI;
    public JButton btntesteoini;
    public JProgressBar progressBar;
    private ModelComboMci modelomci = new ModelComboMci();
    private ModelComboBt2 modelobt2 = new ModelComboBt2();
    //private int numvalv;

    public HiloTest HiloTest;
    public Thread hilotest;

    public JComboBox<String> comboBoxMCI;
    public JComboBox<String> comboBoxBt2;

    private int i = 0;
    
    private Irrisoft IR;

    public static Paneltest getInstance() {

	if (instance == null) {
	    return new Paneltest();
	}

	return instance;

    }

    private Paneltest() {
	super();
	this.IR = Irrisoft.window;
	this.setBounds(10, 84, 465, 351);
	setLayout(null);

	JLabel lblTest = new JLabel("Testeos");
	lblTest.setHorizontalAlignment(SwingConstants.CENTER);
	lblTest.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 18));
	lblTest.setBounds(0, 0, 465, 27);
	add(lblTest);

	btnAtras = new JButton("Atras");
	btnAtras.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {

		if (btnAtras.getText().equalsIgnoreCase("Atras")) {
		    Paneltest.this.setVisible(false);
		    IR.panelpral.setVisible(true);
		} else if (btnAtras.getText().equalsIgnoreCase("Cancelar")) {
		    if (IR.paneltest.HiloTest.inicial)
			i = JOptionPane
				.showConfirmDialog(
					IR.frmIrrisoft,
					"Seguro que quiere cancelar el testeo inicial?",
					"", JOptionPane.YES_NO_OPTION);
		    else if (IR.paneltest.HiloTest.inicial == false)
			i = JOptionPane
				.showConfirmDialog(
					IR.frmIrrisoft,
					"Seguro que quiere cancelar el testeo particular?",
					"", JOptionPane.YES_NO_OPTION);

		    if (i == 0) {

			// if (IR.haycaudalimetro){

			// Si es una valvulamci
			if (IR.paneltest.HiloTest.numvalv < IrrisoftConstantes.BT2_1000) {
			    // valv=(String)IR.paneltest.getModelomci().getSelectedItem();
			    // cierro la valvula requerida
			    if (IrrisoftConstantes.PLACA_MCI_1 == IR.paneltest.HiloTest.numplaca)
				IR.panelmci.interruptor(
					IR.valvsmci
						.getvalvmci(IR.paneltest.HiloTest.numvalv - 101),
					IR.valvsmci
						.getvalvmci(
							IR.paneltest.HiloTest.numvalv - 101)
						.getImgasoc(),
					IR.panelmci.panel1);
			    if (IrrisoftConstantes.PLACA_MCI_2 == IR.paneltest.HiloTest.numplaca)
				IR.panelmci.interruptor(
					IR.valvsmci2
						.getvalvmci(IR.paneltest.HiloTest.numvalv - 201),
					IR.valvsmci2
						.getvalvmci(
							IR.paneltest.HiloTest.numvalv - 201)
						.getImgasoc(),
					IR.panelmci.panel2);
			    if (IrrisoftConstantes.PLACA_MCI_3 == IR.paneltest.HiloTest.numplaca)
				IR.panelmci.interruptor(
					IR.valvsmci3
						.getvalvmci(IR.paneltest.HiloTest.numvalv - 301),
					IR.valvsmci3
						.getvalvmci(
							IR.paneltest.HiloTest.numvalv - 301)
						.getImgasoc(),
					IR.panelmci.panel3);
			    if (IrrisoftConstantes.PLACA_MCI_4 == IR.paneltest.HiloTest.numplaca)
				IR.panelmci.interruptor(
					IR.valvsmci4
						.getvalvmci(IR.paneltest.HiloTest.numvalv - 401),
					IR.valvsmci4
						.getvalvmci(
							IR.paneltest.HiloTest.numvalv - 401)
						.getImgasoc(),
					IR.panelmci.panel4);
			}

			// Si es una valvulabt2
			else {
			    // valv=(String)IR.paneltest.getModelobt2().getSelectedItem();
			    // cierro la valvula requerida
			    if (IrrisoftConstantes.BT2_2000 > IR.paneltest.HiloTest.numvalv )
				IR.panelbt2.interruptor(
					IR.valvsbt2
						.getvalvbt2(Integer
							.toString(IR.paneltest.HiloTest.numvalv)),
					2, 5);
			    if (IrrisoftConstantes.BT2_2000 < IR.paneltest.HiloTest.numvalv
				    && IrrisoftConstantes.BT2_3000 > IR.paneltest.HiloTest.numvalv)
				IR.panelbt2.interruptor(
					IR.valvsbt22
						.getvalvbt2(Integer
							.toString(IR.paneltest.HiloTest.numvalv)),
					2, 6);
			    if (IrrisoftConstantes.BT2_3000 < IR.paneltest.HiloTest.numvalv
				    && IrrisoftConstantes.BT2_4000 > IR.paneltest.HiloTest.numvalv)
				IR.panelbt2.interruptor(
					IR.valvsbt23
						.getvalvbt2(Integer
							.toString(IR.paneltest.HiloTest.numvalv)),
					2, 7);
			    if (IrrisoftConstantes.BT2_4000 < IR.paneltest.HiloTest.numvalv)
				IR.panelbt2.interruptor(
					IR.valvsbt24
						.getvalvbt2(Integer
							.toString(IR.paneltest.HiloTest.numvalv)),
					2, 8);
			    // }
			}

			//Cierro la(s) maestra(s) si es que existe(n)
			if (IR.maestras>0){
			    IR.panelbt2.cierramaestra();
			}
			
			// Inicializo todo
			IR.paneltest.HiloTest
				.inicializavars(false);

		    }

		}

	    }
	});
	btnAtras.setBounds(333, 302, 117, 25);
	add(btnAtras);

	JLabel label = new JLabel("MCI");
	label.setBounds(25, 102, 26, 15);
	add(label);

	JLabel label_1 = new JLabel("BT2");
	label_1.setBounds(25, 140, 26, 15);
	add(label_1);

	JLabel lblParticular = new JLabel("Testeo Particular");
	lblParticular.setHorizontalAlignment(SwingConstants.CENTER);
	lblParticular.setFont(new Font("Dialog", Font.ITALIC, 14));
	lblParticular.setBounds(25, 43, 207, 25);
	add(lblParticular);

	btnTestearMCI = new JButton("Testear");
	// Lo hago invisible si no hay placa multicable
	if (IR.valvsmci == null)
	    btnTestearMCI.setEnabled(false);

	btnTestearMCI.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {

		String aux = (String) getModelomci().getSelectedItem();
		String numplaca = aux.substring(0, 1);

		HiloTest = new HiloTest(false, Integer.parseInt(numplaca));
		hilotest = new Thread(HiloTest);
		hilotest.setName("Hilo Test: " +hilotest.getId());
		IR.paneltest.HiloTest.numvalv = Integer
			.parseInt((String) getModelomci().getSelectedItem());
		hilotest.start();

	    }
	});
	
	btnTestearMCI.setFont(new Font("Dialog", Font.ITALIC, 11));
	btnTestearMCI.setBounds(150, 97, 82, 24);
	add(btnTestearMCI);

	JLabel lblPulsos = new JLabel("Pulsos");
	lblPulsos.setBounds(25, 181, 49, 21);
	add(lblPulsos);

	JLabel lblCaudal = new JLabel("Caudal");
	lblCaudal.setBounds(25, 209, 49, 21);
	add(lblCaudal);

	JLabel lblIntensidad = new JLabel("Intensidad");
	lblIntensidad.setBounds(25, 240, 89, 21);
	add(lblIntensidad);

	lblPulsosDat = new Label("");
	lblPulsosDat.setBounds(80, 180, 141, 22);
	add(lblPulsosDat);

	lblCauDat = new Label("");
	lblCauDat.setBounds(80, 208, 141, 21);
	add(lblCauDat);

	lblIntenDat = new Label("");
	lblIntenDat.setBounds(120, 240, 105, 21);
	add(lblIntenDat);

	//
	comboBoxMCI = new JComboBox<String>(modelomci);

	// Lo hago invisible si no hay placa multicable
	if (IR.valvsmci == null)
	    comboBoxMCI.setEnabled(false);

	// comboBoxMCI.addActionListener(new ActionListener() {
	// public void actionPerformed(ActionEvent arg0) {
	// //Saco el num de valvula del combobox
	// //if (getModelomci().getSelectedItem()!=null)
	// // String aux = (String)getModelomci().getSelectedItem();
	// // String numplaca = aux.substring(0, 1);
	// // String numvalv = aux.substring(2, aux.length());
	// //
	// if(logger.isWarnEnabled())
	// {
	// logger.warn("NUMPLACA:"+numplaca);
	// logger.warn("NUMVALV:"+numvalv);
	// }

	// }
	// });
	
	comboBoxMCI.setBounds(59, 97, 79, 24);
	add(comboBoxMCI);

	comboBoxBt2 = new JComboBox<String>(modelobt2);
	// Lo hago invisible si no hay placa bt2
	if (IR.valvsbt2 == null)
	    comboBoxBt2.setEnabled(false);

	// comboBoxBt2.addActionListener(new ActionListener() {
	// public void actionPerformed(ActionEvent e) {
	// //
	// // //Saco el num de valvula del combobox
	// // if (IR.haycaudalimetro)
	// // IR.hilocau.numvalv =
	// Integer.parseInt((String)getModelobt2().getSelectedItem());
	// //
	// }
	// });
	
	comboBoxBt2.setBounds(58, 135, 80, 24);
	add(comboBoxBt2);

	btnTestearBT2 = new JButton("Testear");

	// Lo hago invisible si no hay placa bt2
	if (IR.valvsbt2 == null)
	    btnTestearBT2.setEnabled(false);

	btnTestearBT2.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		String aux = (String) getModelobt2().getSelectedItem();
		String numplaca = aux.substring(0, 1);
		numplaca = Integer.toString((Integer.parseInt(numplaca) + 4));

		if (logger.isInfoEnabled()) {
		    logger.info("NUMPLACA: " + numplaca);
		}

		HiloTest = new HiloTest(false, Integer.parseInt(numplaca));
		hilotest = new Thread(HiloTest);
		hilotest.setName("Hilo Test: " +hilotest.getId());
		IR.paneltest.HiloTest.numvalv = Integer
			.parseInt((String) getModelobt2().getSelectedItem());
		hilotest.start();
	    }
	});
	btnTestearBT2.setFont(new Font("Dialog", Font.ITALIC, 11));
	btnTestearBT2.setBounds(150, 136, 82, 24);
	add(btnTestearBT2);

	Box verticalBox = Box.createVerticalBox();
	verticalBox.setBorder(new LineBorder(Color.GRAY));
	verticalBox.setBounds(14, 39, 227, 239);
	add(verticalBox);

	JLabel lblInicial = new JLabel("Testeo Inicial");
	lblInicial.setHorizontalAlignment(SwingConstants.CENTER);
	lblInicial.setFont(new Font("Dialog", Font.ITALIC, 14));
	lblInicial.setBounds(259, 39, 191, 28);
	add(lblInicial);

	btntesteoini = new JButton("Testear");
	btntesteoini.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		i = JOptionPane
			.showConfirmDialog(
				IR.frmIrrisoft,
				"Cuidado, se borrarán todos los datos de la tabla de consumos modelo ! \nSeguro que quiere empezar el testeo inicial?",
				"", JOptionPane.YES_NO_OPTION);
		if (i==0){
        		IR.inicial = true;
        		IR.test = true;
        		HiloTest = new HiloTest(true, 0);
        		hilotest = new Thread(HiloTest);
        		hilotest.setName("Hilo Test: " +hilotest.getId());
        		hilotest.start();
		}
		
	    }
	});
	btntesteoini.setFont(new Font("Dialog", Font.ITALIC, 11));
	btntesteoini.setBounds(307, 98, 82, 24);
	add(btntesteoini);
	
	lblintensidad = new JLabel("Intensidad");
	lblintensidad.setBounds(259, 240, 82, 21);
	add(lblintensidad);

	JLabel lblcaudal = new JLabel("Caudal");
	lblcaudal.setBounds(259, 209, 49, 21);
	add(lblcaudal);

	JLabel lblpulsos = new JLabel("Pulsos");
	lblpulsos.setBounds(259, 181, 49, 21);
	add(lblpulsos);;

	lblamp = new Label("");
	lblamp.setBounds(347, 240, 82, 22);
	add(lblamp);

	lblls = new Label("");
	lblls.setBounds(322, 208, 117, 21);
	add(lblls);

	lblpul = new Label("");
	lblpul.setBounds(324, 181, 105, 21);
	add(lblpul);

	JLabel lablElecvalv = new JLabel("Electroválvula");
	lablElecvalv.setBounds(259, 150, 105, 21);
	add(lablElecvalv);

	lblvalv = new Label("");
	lblvalv.setBounds(379, 150, 59, 22);
	add(lblvalv);

	Box verticalBox_1 = Box.createVerticalBox();
	verticalBox_1.setBorder(new LineBorder(Color.GRAY));
	verticalBox_1.setBounds(253, 39, 197, 239);
	add(verticalBox_1);


	lbltesteando = new JLabel("");
	lbltesteando.setFont(new Font("Dialog", Font.BOLD, 9));
	lbltesteando.setBounds(14, 302, 158, 25);
	add(lbltesteando);

	progressBar = new JProgressBar();
	progressBar.setStringPainted(true);
	progressBar.setForeground(new Color(50, 205, 50));
	progressBar.setBounds(173, 302, 141, 25);
	progressBar.setMaximum(25);
	add(progressBar);

	//this.validate();
	
	//this.repaint();
	
	//IR.frmIrrisoft.repaint();
	
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
