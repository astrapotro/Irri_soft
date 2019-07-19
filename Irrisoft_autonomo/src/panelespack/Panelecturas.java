package panelespack;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sensorespack.HiloAmperimetro;

import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;

import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Panelecturas extends JPanel {

    private static Logger logger = LogManager.getLogger(Panelecturas.class
	    .getName());

    // SINGLETON
    private static Panelecturas instance;
    private Irrisoft IR;

    public static Panelecturas getInstance() {

	if (instance == null) {
	    return new Panelecturas();
	}
	return instance;
    }

    // Botones
    private JButton btnmci1, btnmci2, btnmci3, btnmci4, btnbt21, btnbt22,
	    btnbt23, btnbt24, btnplaca_sens;

    private Panelecturas() {
	super();
	this.IR = Irrisoft.window;
	this.setBounds(10, 84, 465, 347);
	setLayout(null);

	btnmci1 = new JButton("MCI1");
	btnmci1.setBounds(12, 72, 87, 25);
	add(btnmci1);

	btnmci2 = new JButton("MCI2");
	btnmci2.setBounds(131, 72, 87, 25);
	add(btnmci2);

	btnmci3 = new JButton("MCI3");
	btnmci3.setBounds(248, 72, 87, 25);
	add(btnmci3);

	btnmci4 = new JButton("MCI4");
	btnmci4.setBounds(366, 72, 87, 25);
	add(btnmci4);

	btnbt21 = new JButton("BT2-1");
	btnbt21.setBounds(12, 150, 87, 25);
	add(btnbt21);

	btnbt22 = new JButton("BT2-2");
	btnbt22.setBounds(131, 150, 87, 25);
	add(btnbt22);

	btnbt23 = new JButton("BT2-3");
	btnbt23.setBounds(248, 150, 87, 25);
	add(btnbt23);

	btnbt24 = new JButton("BT2-4");
	btnbt24.setBounds(366, 150, 87, 25);
	add(btnbt24);

	btnplaca_sens = new JButton("Placa sensores");
	btnplaca_sens.setBounds(12, 203, 439, 67);
	add(btnplaca_sens);

	JSeparator separator = new JSeparator();
	separator.setBounds(12, 187, 441, 2);
	add(separator);

	JSeparator separator_1 = new JSeparator();
	separator_1.setBounds(12, 109, 441, 2);
	add(separator_1);

	JLabel lblPlacasmci = new JLabel("Placas MCI");
	lblPlacasmci.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
	lblPlacasmci.setBounds(12, 45, 111, 15);
	add(lblPlacasmci);

	JLabel lblPlacasBt = new JLabel("Placas BT2");
	lblPlacasBt.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
	lblPlacasBt.setBounds(12, 123, 111, 15);
	add(lblPlacasBt);

	JLabel lblTitulo = new JLabel("Lecturas Sensores");
	lblTitulo.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 14));
	lblTitulo.setBounds(162, 8, 157, 25);
	add(lblTitulo);

	JSeparator separator_2 = new JSeparator();
	separator_2.setBounds(12, 34, 441, 2);
	add(separator_2);

	JSeparator separator_3 = new JSeparator();
	separator_3.setBounds(12, 282, 441, 2);
	add(separator_3);

	// BOTON ATRAS
	JButton btnAtras = new JButton("Atras");
	btnAtras.setBounds(178, 296, 117, 36);
	btnAtras.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		Panelecturas.this.setVisible(false);
		IR.panelpral.setVisible(true);
	    }
	});
	add(btnAtras);

    }

    /**
     * Para habilitar o deshabilitar los botones de placas dependiendo de la
     * info de config
     **/
    public void habilitabotones() {

	if (IR.sensmci == true) {
	    btnmci1.setEnabled(true);
	    btnmci1.addMouseListener(new MouseAdapter() {

		public void mouseClicked(MouseEvent e) {
		    //Instancio el panel lecturas (MCI-1) al pulsar el boton.
		    IR.panelecturasmci = Panelecturasmci.getInstance();
		    // Buclo popr los sensores para añadir los listeners
		    // necesarios
		    for (int i = 0; i < IR.sensores.size(); i++) {
			if (IR.sensores.get(i).getNum_placa() == IrrisoftConstantes.PLACA_MCI_1) {
			    IR.sensores.get(i).setLectura("");
			    IR.sensores.get(i).setPulsos(-1);
			    IR.sensores.get(i).addPropertyChangeListener(
				    "pulsos", IR.panelecturasmci);
			    IR.sensores.get(i).addPropertyChangeListener(
				    "lectura", IR.panelecturasmci);
			}
		    }
		    // Aki se llama al panelecturas
		    IR.panelecturas.setVisible(false);
		    IR.panelecturasmci.getLbltitulo().setText("PLACA MCI 1");
		    IR.panelecturasmci.repaint();
		    IR.frmIrrisoft.getContentPane().add(IR.panelecturasmci);
		    IR.panelecturasmci.setVisible(true);

		}
	    });
	} else
	    btnmci1.setEnabled(false);

	if (IR.sensmci2 == true) {
	    btnmci2.setEnabled(true);
	    btnmci2.addMouseListener(new MouseAdapter() {

		public void mouseClicked(MouseEvent e) {

		    //Instancio el panel lecturas (MCI-2) al pulsar el boton.
		    IR.panelecturasmci = Panelecturasmci.getInstance();
		    // Buclo popr los sensores para añadir los listeners
		    // necesarios
		    for (int i = 0; i < IR.sensores.size(); i++) {
			if (IR.sensores.get(i).getNum_placa() == IrrisoftConstantes.PLACA_MCI_2) {
			    IR.sensores.get(i).setLectura("");
			    IR.sensores.get(i).setPulsos(-1);
			    IR.sensores.get(i).addPropertyChangeListener(
				    "pulsos", IR.panelecturasmci);
			    IR.sensores.get(i).addPropertyChangeListener(
				    "lectura", IR.panelecturasmci);
			}
		    }
		    // Aki se llama al panelecturas

		    IR.panelecturas.setVisible(false);
		    IR.panelecturasmci.getLbltitulo().setText("PLACA MCI 2");
		    IR.panelecturasmci.repaint();
		    IR.frmIrrisoft.getContentPane().add(IR.panelecturasmci);
		    IR.panelecturasmci.setVisible(true);

		}
	    });
	} else
	    btnmci2.setEnabled(false);

	if (IR.sensmci3 == true) {
	    btnmci3.setEnabled(true);
	    btnmci3.addMouseListener(new MouseAdapter() {

		
		public void mouseClicked(MouseEvent e) {

		    //Instancio el panel lecturas (MCI-3) al pulsar el boton.
		    IR.panelecturasmci = Panelecturasmci.getInstance();
		    // Buclo popr los sensores para añadir los listeners
		    // necesarios
		    for (int i = 0; i < IR.sensores.size(); i++) {
			if (IR.sensores.get(i).getNum_placa() == IrrisoftConstantes.PLACA_MCI_3) {
			    IR.sensores.get(i).setLectura("");
			    IR.sensores.get(i).setPulsos(-1);
			    IR.sensores.get(i).addPropertyChangeListener(
				    "pulsos", IR.panelecturasmci);
			    IR.sensores.get(i).addPropertyChangeListener(
				    "lectura", IR.panelecturasmci);
			}
		    }
		    // Aki se llama al panelecturas

		    IR.panelecturas.setVisible(false);
		    IR.panelecturasmci.getLbltitulo().setText("PLACA MCI 3");
		    IR.panelecturasmci.repaint();
		    IR.frmIrrisoft.getContentPane().add(IR.panelecturasmci);
		    IR.panelecturasmci.setVisible(true);

		}
	    });
	} else
	    btnmci3.setEnabled(false);

	if (IR.sensmci4 == true) {
	    btnmci4.setEnabled(true);
	    btnmci4.addMouseListener(new MouseAdapter() {

		public void mouseClicked(MouseEvent e) {

		    //Instancio el panel lecturas (MCI-4) al pulsar el boton.
		    IR.panelecturasmci = Panelecturasmci.getInstance();
		    // Buclo popr los sensores para añadir los listeners
		    // necesarios
		    for (int i = 0; i < IR.sensores.size(); i++) {
			if (IR.sensores.get(i).getNum_placa() == IrrisoftConstantes.PLACA_MCI_4) {
			    IR.sensores.get(i).setLectura("");
			    IR.sensores.get(i).setPulsos(-1);
			    IR.sensores.get(i).addPropertyChangeListener(
				    "pulsos", IR.panelecturasmci);
			    IR.sensores.get(i).addPropertyChangeListener(
				    "lectura", IR.panelecturasmci);
			}
		    }
		    // Aki se llama al panelecturas

		    IR.panelecturas.setVisible(false);
		    IR.panelecturasmci.getLbltitulo().setText("PLACA MCI 4");
		    IR.panelecturasmci.repaint();
		    IR.frmIrrisoft.getContentPane().add(IR.panelecturasmci);
		    IR.panelecturasmci.setVisible(true);

		}
	    });
	} else
	    btnmci4.setEnabled(false);

	if (IR.sensbt2 == true) { // Revisar comportamiento y
				  // necesidad del hiloinfo en bt2

	    btnbt21.setEnabled(true);
	    btnbt21.addMouseListener(new MouseAdapter() {

		public void mouseClicked(MouseEvent e) {
		    
		    //Instancio el panel lecturas (BT2-1) al pulsar el boton.
		    IR.panelecturasbt2 = Panelecturasbt2.getInstance();
		    // Buclo popr los sensores para añadir los listeners
		    // necesarios
		    for (int i = 0; i < IR.sensores.size(); i++) {
			if (IR.sensores.get(i).getNum_placa() == IrrisoftConstantes.PLACA_BT2_5) {
			    IR.sensores.get(i).setLectura("");
			    IR.sensores.get(i).setPulsos(-1);
			    IR.sensores.get(i).addPropertyChangeListener(
				    "pulsos", IR.panelecturasbt2);
			    IR.sensores.get(i).addPropertyChangeListener(
				    "lectura", IR.panelecturasbt2);
			}
		    }

		    // Aki se llama al panelecturas
		    IR.panelecturas.setVisible(false);
		    IR.panelecturasbt2.getLbltitulo().setText("PLACA BT2-1");
		    //IR.panelecturasbt2.tipo = 0;
		    // IR.panelecturasbt2.setActualizar(true);
		    IR.panelecturasbt2.repaint();
		    IR.frmIrrisoft.getContentPane().add(IR.panelecturasbt2);
		    IR.panelecturasbt2.setVisible(true);
		    // IR.panelecturasbt2
		    // .setHilomuestrainfo(IR.panelecturasbt2
		    // .getHilomuestrainfo());
		    // IR.panelecturasbt2.hiloinfo = new Thread(
		    // IR.panelecturasbt2.hilomuestrainfo);
		    // IR.panelecturasbt2.hiloinfo.start();

		}
	    });
	} else
	    btnbt21.setEnabled(false);

	if (IR.sensbt22 == true) {
	    btnbt22.setEnabled(true);
	    btnbt22.addMouseListener(new MouseAdapter() {

		public void mouseClicked(MouseEvent e) {

		    //Instancio el panel lecturas (BT2-2) al pulsar el boton.
		    IR.panelecturasbt2 = Panelecturasbt2.getInstance();
		    // Buclo popr los sensores para añadir los listeners
		    // necesarios
		    for (int i = 0; i < IR.sensores.size(); i++) {
			if (IR.sensores.get(i).getNum_placa() == IrrisoftConstantes.PLACA_BT2_6) {
			    IR.sensores.get(i).setLectura("");
			    IR.sensores.get(i).setPulsos(-1);
			    IR.sensores.get(i).addPropertyChangeListener(
				    "pulsos", IR.panelecturasbt2);
			    IR.sensores.get(i).addPropertyChangeListener(
				    "lectura", IR.panelecturasbt2);
			}
		    }
		    // Aki se llama al panelecturas

		    IR.panelecturas.setVisible(false);
		    IR.panelecturasbt2.getLbltitulo().setText("PLACA BT2-2");
		    //IR.panelecturasbt2.tipo = 0;
		    IR.panelecturasbt2.repaint();
		    IR.frmIrrisoft.getContentPane().add(IR.panelecturasbt2);
		    IR.panelecturasbt2.setVisible(true);
		    // IR.panelecturasmci.repaint();
		    // IR.frmIrrisoft.getContentPane().add(
		    // IR.panelecturasmci);
		    // IR.panelecturasmci.setVisible(true);

		}
	    });
	} else
	    btnbt22.setEnabled(false);

	if (IR.sensbt23 == true) {
	    btnbt23.setEnabled(true);
	    btnbt23.addMouseListener(new MouseAdapter() {

		public void mouseClicked(MouseEvent e) {

		    //Instancio el panel lecturas (BT2-3) al pulsar el boton.
		    IR.panelecturasbt2 = Panelecturasbt2.getInstance();
		    // Buclo popr los sensores para añadir los listeners
		    // necesarios
		    for (int i = 0; i < IR.sensores.size(); i++) {
			if (IR.sensores.get(i).getNum_placa() == IrrisoftConstantes.PLACA_BT2_7) {
			    IR.sensores.get(i).setLectura("");
			    IR.sensores.get(i).setPulsos(-1);
			    IR.sensores.get(i).addPropertyChangeListener(
				    "pulsos", IR.panelecturasbt2);
			    IR.sensores.get(i).addPropertyChangeListener(
				    "lectura", IR.panelecturasbt2);
			}
		    }
		    // Aki se llama al panelecturas

		    IR.panelecturas.setVisible(false);
		    IR.panelecturasbt2.getLbltitulo().setText("PLACA BT2-3");
		    //IR.panelecturasbt2.tipo = 0;
		    IR.panelecturasbt2.repaint();
		    IR.frmIrrisoft.getContentPane().add(IR.panelecturasbt2);
		    IR.panelecturasbt2.setVisible(true);
		    // IR.panelecturasmci.repaint();
		    // IR.frmIrrisoft.getContentPane().add(
		    // IR.panelecturasmci);
		    // IR.panelecturasmci.setVisible(true);

		}
	    });
	} else
	    btnbt23.setEnabled(false);

	if (IR.sensbt24 == true) {
	    btnbt24.setEnabled(true);
	    btnbt24.addMouseListener(new MouseAdapter() {

		public void mouseClicked(MouseEvent e) {

		    //Instancio el panel lecturas (BT2-4) al pulsar el boton.
		    IR.panelecturasbt2 = Panelecturasbt2.getInstance();
		    // Buclo popr los sensores para añadir los listeners
		    // necesarios
		    for (int i = 0; i < IR.sensores.size(); i++) {
			if (IR.sensores.get(i).getNum_placa() == IrrisoftConstantes.PLACA_BT2_8){
			    IR.sensores.get(i).setLectura("");
			    IR.sensores.get(i).setPulsos(-1);
			    IR.sensores.get(i).addPropertyChangeListener("pulsos", IR.panelecturasbt2);
			IR.sensores.get(i).addPropertyChangeListener("lectura",
				IR.panelecturasbt2);
			}
		    }
		    // Aki se llama al panelecturas

		    IR.panelecturas.setVisible(false);
		    IR.panelecturasbt2.getLbltitulo().setText("PLACA BT2-4");
		    //IR.panelecturasbt2.tipo = 0;
		    IR.panelecturasbt2.repaint();
		    IR.frmIrrisoft.getContentPane().add(IR.panelecturasbt2);
		    IR.panelecturasbt2.setVisible(true);
		    // IR.panelecturasmci.repaint();
		    // IR.frmIrrisoft.getContentPane().add(
		    // IR.panelecturasmci);
		    // IR.panelecturasmci.setVisible(true);

		}
	    });
	} else
	    btnbt24.setEnabled(false);

	if (IR.hayplacasens) {
	    btnplaca_sens.setEnabled(true);
	    btnplaca_sens.addMouseListener(new MouseAdapter() {

		public void mouseClicked(MouseEvent e) {

		    //Instancio el panel lecturas (Sensores) al pulsar el boton.
		  IR.panelecturasens = Panelecturasens.getInstance();
		    // Buclo popr los sensores para añadir los listeners
		    // necesarios
		    for (int i = 0; i < IR.sensores.size(); i++) {
			if (IR.sensores.get(i).getNum_placa() == IrrisoftConstantes.PLACA_SENSORES_0) {
			    IR.sensores.get(i).setLectura("");
			    IR.sensores.get(i).setPulsos(-1);
			    IR.sensores.get(i).addPropertyChangeListener(
				    "pulsos", IR.panelecturasens);
			    IR.sensores.get(i).addPropertyChangeListener(
				    "lectura", IR.panelecturasens);
			  
			}
		    }

		    // Aki se llama al panelecturas
		    IR.panelecturas.setVisible(false);
		    IR.panelecturasens.repaint();
		    IR.frmIrrisoft.getContentPane().add(IR.panelecturasens);
		    IR.panelecturasens.setVisible(true);
		}
	    });
	} else
	    btnplaca_sens.setEnabled(false);

    }
}
