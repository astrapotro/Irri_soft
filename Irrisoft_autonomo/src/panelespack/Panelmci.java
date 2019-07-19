package panelespack;

import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;
import irrisoftpack.SerialDriver;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import jssc.SerialPort;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import valvulaspack.ListaValvMci;
import valvulaspack.Valvula;

public class Panelmci extends JPanel {

    private static Logger logger = LogManager.getLogger(Panelmci.class
	    .getName());
    private static final long serialVersionUID = 1L;
    // SINGLETON
    private static Panelmci instance;

    private SerialDriver conserie = new SerialDriver();

    // private int tipoplaca;
    // private Integer i;

    // Posición y dimensiones de las imágenes.
    private int x, y, w, z, ypri;
    // Posición y dimensiones de los labels.
    private int a, b, c, d, bpri;
    private int ant, ante, baj, bajo;

    // Paneles para las valvulas
    public JPanel panel1, panel2, panel3, panel4;
    private JSeparator separator, separator1, separator2, separator3;
    private JLabel mci1, mci2, mci3, mci4;
    public JLabel lblamp;
    private Irrisoft IR;

    public static Panelmci getInstance() {

	if (instance == null) {
	    return new Panelmci();
	}
	return instance;
    }

    private Panelmci() {
	super();
	this.IR = Irrisoft.window;
	this.setBounds(10, 80, 468, 342);
	setLayout(null);

	// BOTON ATRAS
	JButton btnAtras = new JButton("Atras");
	btnAtras.setBounds(178, 317, 135, 25);
	btnAtras.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		Panelmci.this.setVisible(false);
		IR.panelpral.setVisible(true);
	    }
	});
	add(btnAtras);

	lblamp = new JLabel("");
	lblamp.setHorizontalAlignment(SwingConstants.RIGHT);
	lblamp.setFont(new Font("Dialog", Font.PLAIN, 9));
	lblamp.setBounds(282, 0, 186, 15);
	add(lblamp);

    }

    //
    //
    // /////////////////////////////// Inicializacion valvulas
    public void pintavalvsmci() {

	// Primera placa mci
	if (IR.valvsmci != null) {

	    panel1 = new JPanel();
	    panel1.setBounds(0, 0, 463, 80);
	    panel1.setLayout(null);

	    separator = new JSeparator();

	    mci1 = new JLabel("Placa MCI 1");
	    mci1.setBounds(210, 4, 75, 12);
	    mci1.setFont(new Font("Dialog", Font.ITALIC, 12));
	    panel1.add(mci1);

	    separator.setBounds(1, 80, 460, 1);
	    add(separator);

	    for (int i = 0; i < IR.valvsmci.getsizeof(); i++) {

		pintavalvsdinamic(IR.valvsmci, i, panel1);

	    }

	    add(panel1);
	}

	// Segunda placa mci
	if (IR.valvsmci2 != null) {

	    panel2 = new JPanel();
	    panel2.setBounds(0, 83, 463, 80);
	    panel2.setLayout(null);

	    separator1 = new JSeparator();

	    mci2 = new JLabel("Placa MCI 2");
	    mci2.setBounds(210, 2, 75, 12);
	    mci2.setFont(new Font("Dialog", Font.ITALIC, 12));
	    panel2.add(mci2);

	    separator1.setBounds(1, 158, 460, 1);
	    add(separator1);

	    for (int i = 0; i < IR.valvsmci2.getsizeof(); i++) {

		pintavalvsdinamic(IR.valvsmci2, i, panel2);

	    }

	    add(panel2);
	}

	// Tercera placa mci
	if (IR.valvsmci3 != null) {

	    panel3 = new JPanel();
	    panel3.setBounds(0, 160, 463, 80);
	    panel3.setLayout(null);

	    separator2 = new JSeparator();

	    mci3 = new JLabel("Placa MCI 3");
	    mci3.setBounds(210, 2, 75, 12);
	    mci3.setFont(new Font("Dialog", Font.ITALIC, 12));
	    panel3.add(mci3);

	    separator2.setBounds(1, 237, 460, 1);
	    add(separator2);

	    for (int i = 0; i < IR.valvsmci3.getsizeof(); i++) {

		pintavalvsdinamic(IR.valvsmci3, i, panel3);

	    }

	    add(panel3);
	}

	// Cuarta placa mci
	if (IR.valvsmci4 != null) {

	    panel4 = new JPanel();
	    panel4.setBounds(0, 240, 463, 80);
	    panel4.setLayout(null);

	    separator3 = new JSeparator();

	    mci4 = new JLabel("Placa MCI 4");
	    mci4.setBounds(210, 2, 75, 12);
	    mci4.setFont(new Font("Dialog", Font.ITALIC, 12));
	    panel4.add(mci4);

	    separator3.setBounds(1, 237, 460, 1);
	    add(separator3);

	    for (int i = 0; i < IR.valvsmci4.getsizeof(); i++) {

		pintavalvsdinamic(IR.valvsmci4, i, panel4);

	    }

	    add(panel4);
	}

	// Es susceptible de cambiarse (será 28)!!
	// for (i=0;i<Irrisoft.NUMVALVSMCI;i++){
	//
	// //IR.valvsmci.addvalvmci(new Valvula());
	// IR.valvsmci.getvalvmci(i).getImgasoc().setIcon(new
	// ImageIcon(Irrisoft.class.getResource(IR.rutaoff)));
	//
	// if (i<9){
	// IR.valvsmci.getvalvmci(i).setCodelecvalv("0"+Integer.toString(i+1));
	// IR.valvsmci.getvalvmci(i).getImgasoc().setName("imglbl0"+(i+1));
	// }else{
	// IR.valvsmci.getvalvmci(i).setCodelecvalv(Integer.toString(i+1));
	// IR.valvsmci.getvalvmci(i).getImgasoc().setName("imglbl"+(i+1));
	// }
	//
	//
	// if (i==0){
	// IR.valvsmci.getvalvmci(i).getImgasoc().setBounds(x, y,
	// w, z);
	// }else if (i!=0 && i<12){
	// ant = ant +36;
	// IR.valvsmci.getvalvmci(i).getImgasoc().setBounds(x+ant,
	// y, w, z);
	// }
	// else if (i!=0 && i>12){
	// w=75;
	// baj = baj +36;
	// IR.valvsmci.getvalvmci(i).getImgasoc().setBounds(x+baj,
	// y, w, z);
	// }
	//
	//
	// if (IR.valvsmci.getvalvmci(i).isAbierta()){
	// IR.redimensionado(IR.valvsmci.getvalvmci(i).getImgasoc(),
	// IR.rutaon);
	// IR.panelmci.add(IR.valvsmci.getvalvmci(i).getImgasoc());
	// }else{
	// IR.redimensionado(IR.valvsmci.getvalvmci(i).getImgasoc(),
	// IR.rutaoff);
	// IR.panelmci.add(IR.valvsmci.getvalvmci(i).getImgasoc());
	//
	// }
	//
	// IR.valvsmci.getvalvmci(i).getImgasoc().addMouseListener(new
	// MouseAdapter() {
	// @Override
	// public void mouseClicked(MouseEvent e) {
	// //OJO --> No me ha quedado más remedio que hacer esto para saber a
	// qué valvula está haciendo click !!
	// if(logger.isWarnEnabled())
	// {
	// logger.warn("Mouse clicked: "+e.getSource().toString());
	// }
	// String num;
	// if (i<10){
	// num = e.getSource().toString().substring(26, 27);
	// }else{
	// num = e.getSource().toString().substring(25, 27);
	// }
	//
	//
	// int numvalv = Integer.parseInt(num)-1;
	// IR.panelmci.interruptor(IR.valvsmci.getvalvmci(numvalv),IR.valvsmci.getvalvmci(numvalv).getImgasoc());
	//
	// }
	// });
	//
	// JLabel lbl = new JLabel();
	// lbl.setText(Integer.toString(i+1));
	// lbl.setHorizontalAlignment(SwingConstants.CENTER);
	// if (i==0){
	// lbl.setBounds(a, b, c, d);
	//
	// }else if (i!=0 && i<12){
	//
	// ante = ante +36;
	// lbl.setBounds(a+ante, b, c, d);
	// }else if (i!=0 && i>12){
	// c=75;
	// bajo = bajo +36;
	// lbl.setBounds(a+bajo, b, c, d);
	// }
	//
	// IR.panelmci.add(lbl);
	//
	// }

    }

    public void pintavalvsdinamic(final ListaValvMci valvs, final int i,
	    final JPanel panel) {

	// if (numplaca==1){
	// if (i==0){
	// //Posición y dimensiones de las imágenes.
	// x=10;y=28;w=33;z=15;ypri=59;
	//
	// //Posición y dimensiones de los labels.
	// a=10;b=15;c=33;d=15;bpri=46;
	//
	// ant = 0;ante = 0;baj =0;bajo=0;
	// }
	// }else if (numplaca==2){
	// if (i==0){
	// //Posición y dimensiones de las imágenes.
	// x=10;y=110;w=33;z=15;ypri=120;
	//
	// //Posición y dimensiones de los labels.
	// a=10;b=95;c=33;d=15;bpri=130;
	//
	// ant = 0;ante = 0;baj =0;bajo=0;
	// }
	// }interruptor
	//
	if (i == 0) {
	    // Posición y dimensiones de las imágenes.
	    x = 10;
	    y = 28;
	    w = 33;
	    z = 15;
	    ypri = 55;

	    // Posición y dimensiones de los labels.
	    a = 10;
	    b = 15;
	    c = 33;
	    d = 15;
	    bpri = 42;

	    ant = 0;
	    ante = 0;
	    baj = 0;
	    bajo = 0;
	}

	valvs.getvalvmci(i)
		.getImgasoc()
		.setIcon(
			new ImageIcon(Irrisoft.class
				.getResource(IrrisoftConstantes.IMG_OFF)));

	if (i < 9) {
	    // valvs.getvalvmci(i).setCodelecvalv("0"+Integer.toString(i+1));
	    valvs.getvalvmci(i).getImgasoc().setName("imglbl0" + (i + 1));
	} else {
	    // valvs.getvalvmci(i).setCodelecvalv(Integer.toString(i+1));
	    valvs.getvalvmci(i).getImgasoc().setName("imglbl" + (i + 1));
	}

	if (i == 0) {
	    valvs.getvalvmci(i).getImgasoc().setBounds(x, y, w, z);
	} else if (i != 0 && i < 12) {
	    ant = ant + 35;
	    valvs.getvalvmci(i).getImgasoc().setBounds(x + ant, y, w, z);
	} else if (i == 12) {
	    y = ypri;
	    valvs.getvalvmci(i).getImgasoc().setBounds(x, y, w, z);
	} else if (i != 0 && i > 12) {
	    y = ypri;
	    baj = baj + 35;
	    valvs.getvalvmci(i).getImgasoc().setBounds(x + baj, y, w, z);
	}

	if (valvs.getvalvmci(i).isAbierta()) {
	    IR.redimensionado(valvs.getvalvmci(i).getImgasoc(),
		   IrrisoftConstantes.IMG_ON);
	    panel.add(valvs.getvalvmci(i).getImgasoc());
	} else {
	    IR.redimensionado(valvs.getvalvmci(i).getImgasoc(),
		    IrrisoftConstantes.IMG_OFF);
	    panel.add(valvs.getvalvmci(i).getImgasoc());

	}

	valvs.getvalvmci(i).getImgasoc().addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		// OJO --> No me ha quedado más remedio que hacer esto para
		// saber a qué valvula está haciendo click !!
		// if(logger.isWarnEnabled())
		// {
		// logger.warn("Mouse clicked: "+e.getSource().toString());
		// }
		String num;
		if (i < 9) {
		    num = e.getSource().toString().substring(26, 27);
		} else {
		    num = e.getSource().toString().substring(25, 27);
		}

		int numvalv = Integer.parseInt(num) - 1;
		interruptor(valvs.getvalvmci(numvalv), valvs
			.getvalvmci(numvalv).getImgasoc(), panel);

	    }
	});

	JLabel lbl = new JLabel();
	lbl.setText(Integer.toString(Integer.parseInt(valvs.getvalvmci(i)
		.getCodelecvalv()) - (valvs.getvalvmci(i).getNum_placa() * 100)));
	lbl.setFont(new Font("Dialog", Font.PLAIN, 10));
	lbl.setHorizontalAlignment(SwingConstants.CENTER);
	if (i == 0) {
	    lbl.setBounds(a, b, c, d);

	} else if (i != 0 && i < 12) {

	    ante = ante + 35;
	    lbl.setBounds(a + ante, b, c, d);
	} else if (i == 12) {
	    b = bpri;
	    lbl.setBounds(a + bajo, b, c, d);
	} else if (i != 0 && i >= 12) {
	    b = bpri;
	    bajo = bajo + 35;
	    lbl.setBounds(a + bajo, b, c, d);
	}

	panel.add(lbl);

    }

    public void interruptor(Valvula valv, JLabel img, JPanel panel) {

	if (logger.isInfoEnabled()) {
	    logger.info("Interruptor, codelecvalv: " + valv.getCodelecvalv());
	}

	    try {

		// conserie.conectaserial(Irrisoft.config.getMci(),1);
		conserie = valv.getSerie();
	
	    } catch (Exception e1) {
		if(logger.isErrorEnabled()){
		    logger.error(e1.getMessage());
		}

		IR.escribetextPane("\n Apertura de la valv "
			+ valv + " cancelada.", IR.normal, false);

		IR.escribetextPane(
			"\n  Conecte la placa MCI "+valv.getNum_placa()+"!",
			IR.normal, false);

		return;
	    }
	

	// Compruebo que la conexion serie está viva. Sino la reconecto
	try {
	    if (!conserie.serialPort.isOpened()){
		SerialPort serialPort = new SerialPort(conserie.serialPort.getPortName());
		    conserie.setSerialPort(serialPort);
		conserie.conectaserial(valv.getNum_placa());}
	} catch (NullPointerException e) {
	    if(logger.isErrorEnabled()){
		logger.error(e.getMessage());
	    }
	}

	if (valv.isAbierta()) {
	    if (logger.isInfoEnabled()) {
		logger.info("Está abierta y la cierro con la placa "
			+ valv.getNum_placa());
	    }
	    
	   
	    conserie.cierravalv(valv.getCodelecvalv(), valv.getNum_placa());
	    
	    valv.setAbierta(false);
	    // //
	    // IR.valvsmci.getvalvmci(Integer.parseInt(valv.getCodelecvalv())-1).setAbierta(false);
	    //
	    // La quito de la lista de abiertas
	    IR.quitarvalvabiertas(valv, valv.getNum_placa());

	    // /MAESTRA
	    if (!IR.inicial) {
		if (IR.maestra != null) {
//		    if (!valv.getCodelecvalv().contentEquals(
//			    IR.maestra.getCodelecvalv()))
//			;
		    IR.panelbt2.cierramaestra();
		}
	    }
	    IR.redimensionado(img, IrrisoftConstantes.IMG_OFF);
	    panel.add(img);
	    panel.repaint();
	} else {

	    // /MAESTRA
	    if (IR.maestra != null) {
		if (!valv.getCodelecvalv().contentEquals(
			IR.maestra.getCodelecvalv()))
		    ;
		IR.panelbt2.abremaestra();
	    }
	    if (logger.isInfoEnabled()) {
		logger.info("Está cerrada y la abro con la placa "
			+ valv.getNum_placa());
	    }
	    
	
	    conserie.abrevalv(valv);
	    
	    valv.setAbierta(true);
	    // //
	    // IR.valvsmci.getvalvmci(Integer.parseInt(valv.getCodelecvalv())-1).setAbierta(true);
	    //

	    // La meto en Valvsabiertas
	    IR.addvalvsabiertas(valv, valv.getNum_placa());
	    IR.redimensionado(img, IrrisoftConstantes.IMG_ON);
	    panel.add(img);
	    panel.repaint();
	}

	// Cierro el puerto serie
	// conserie.desconectaserial(valv.getNum_placa());
    }

    public void interruptor(Valvula valv, JLabel img, boolean solopinta,
	    JPanel panel) {

	if (valv.isAbierta()) {

	    IR.redimensionado(img, IrrisoftConstantes.IMG_ON);
	    panel.add(img);
	    panel.repaint();
	} else {

	    IR.redimensionado(img, IrrisoftConstantes.IMG_OFF);
	    panel.add(img);
	    panel.repaint();
	}
    }
}
