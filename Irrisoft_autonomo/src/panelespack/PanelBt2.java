package panelespack;

import irrisoftpack.Irrisoft;
import irrisoftpack.IrrisoftConstantes;
import irrisoftpack.SerialDriver;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import jssc.SerialPort;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import alertaspack.GestorAlertas;

import valvulaspack.Valvula;

public class PanelBt2 extends JPanel {

    private static Logger logger = LogManager.getLogger(PanelBt2.class
	    .getName());
    private static final long serialVersionUID = 1L;

    // SINGLETON
    private static PanelBt2 instance;

    private JTextField numvalv;
    public JTextField textRx;
    public JTextField textTx;
    public JLabel lblInfo;
    public JLabel lblconsum;
    public JLabel lblEstado;
    public JLabel lblver;
    protected SerialDriver conserie, conserie1;
    private JRadioButton rdbtnStn1, rdbtnStn2, rdbtnStn3, rdbtnStn4;
    private JButton btnAbrir, btnCerrar, btnReset;
    private boolean retonno;
    int stnselec = 5;
    private Irrisoft IR;

    private GestorAlertas gestorAlertas;

    // private boolean limitebt2;

    public static PanelBt2 getInstance() {

	if (instance == null) {
	    return new PanelBt2();
	}
	return instance;
    }

    private PanelBt2() {
	super();

	gestorAlertas = GestorAlertas.getInstance();

	this.IR = Irrisoft.window;

	// this.setName("Panelmci");
	this.setBounds(10, 84, 465, 344);
	setLayout(null);

	JLabel placa1 = new JLabel("Blind Translator 2");
	placa1.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 14));
	placa1.setHorizontalAlignment(SwingConstants.CENTER);
	placa1.setBounds(161, 0, 146, 32);
	add(placa1);

	lblver = new JLabel("");
	lblver.setFont(new Font("Dialog", Font.PLAIN, 9));
	lblver.setBounds(349, 11, 108, 15);
	add(lblver);

	// BOTON ATRAS
	JButton btnAtras = new JButton("Atras");
	btnAtras.setBounds(178, 307, 117, 25);
	btnAtras.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		PanelBt2.this.setVisible(false);
		IR.panelpral.setVisible(true);
	    }
	});
	add(btnAtras);

	JLabel lblestacion = new JLabel("Estación: ");
	lblestacion.setBounds(22, 49, 69, 15);
	add(lblestacion);

	numvalv = new JTextField();
	numvalv.setBounds(94, 47, 114, 19);
	add(numvalv);
	numvalv.setColumns(10);

	// filtro por si el usuario mete un numvalv que no corresponde. A
	// REVISAR !!
	filtro();

	rdbtnStn1 = new JRadioButton("STN1");
	rdbtnStn1.setSelected(true);

	rdbtnStn1.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		rdbtnStn1.setSelected(true);
		if (rdbtnStn2 != null)
		    rdbtnStn2.setSelected(false);
		if (rdbtnStn3 != null)
		    rdbtnStn3.setSelected(false);
		if (rdbtnStn4 != null)
		    rdbtnStn4.setSelected(false);
		stnselec = 5;
		lblver.setText("Firmware ver: " + IR.getSerie5().ver);
	    }
	});
	rdbtnStn1.setBounds(211, 44, 60, 23);
	add(rdbtnStn1);

	if (IR.valvsbt22 != null) {
	    rdbtnStn2 = new JRadioButton("STN2");
	    rdbtnStn2.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    rdbtnStn2.setSelected(true);
		    rdbtnStn1.setSelected(false);
		    if (rdbtnStn3 != null)
			rdbtnStn3.setSelected(false);
		    if (rdbtnStn4 != null)
			rdbtnStn4.setSelected(false);
		    stnselec = 6;
		    lblver.setText("Firmware ver: " + IR.getSerie6().ver);
		}
	    });
	    rdbtnStn2.setBounds(275, 45, 60, 23);
	    add(rdbtnStn2);
	}

	if (IR.valvsbt23 != null) {
	    rdbtnStn3 = new JRadioButton("STN3");
	    rdbtnStn3.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    rdbtnStn3.setSelected(true);
		    rdbtnStn1.setSelected(false);
		    rdbtnStn2.setSelected(false);
		    if (rdbtnStn4 != null)
			rdbtnStn4.setSelected(false);
		    stnselec = 7;
		    lblver.setText("Firmware ver: " + IR.getSerie7().ver);
		}
	    });
	    rdbtnStn3.setBounds(339, 45, 60, 23);
	    add(rdbtnStn3);
	}

	if (IR.valvsbt24 != null) {
	    rdbtnStn4 = new JRadioButton("STN4");
	    rdbtnStn4.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
		    rdbtnStn4.setSelected(true);
		    rdbtnStn1.setSelected(false);
		    rdbtnStn2.setSelected(false);
		    rdbtnStn3.setSelected(false);
		    stnselec = 8;
		    lblver.setText("Firmware ver: " + IR.getSerie8().ver);
		}
	    });
	    rdbtnStn4.setBounds(397, 45, 60, 23);
	    add(rdbtnStn4);
	}

	btnAbrir = new JButton("Abrir");
	btnAbrir.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {

		try {
		    filtro();
		    // int valv;

		    if (rdbtnStn1.isSelected()) {
			rango(1);
			if (!numvalv.getText().contentEquals("")) {
			    // valv =Integer.parseInt(numvalv.getText())-1001;
			    conserie = IR.getSerie5();
			    IR.valvsbt2.getvalvbt2(numvalv.getText())
				    .setProgasoc(0);
			    interruptor(
				    IR.valvsbt2.getvalvbt2(numvalv.getText()),
				    1, 5);
			}
		    } else if (rdbtnStn2.isSelected()) {
			rango(2);
			if (!numvalv.getText().contentEquals("")) {
			    // valv =Integer.parseInt(numvalv.getText())-2001;
			    conserie = IR.getSerie6();
			    IR.valvsbt22.getvalvbt2(numvalv.getText())
				    .setProgasoc(0);
			    interruptor(
				    IR.valvsbt22.getvalvbt2(numvalv.getText()),
				    1, 6);
			}

		    } else if (rdbtnStn3.isSelected()) {
			rango(3);
			if (!numvalv.getText().contentEquals("")) {
			    // valv =Integer.parseInt(numvalv.getText())-3001;
			    conserie = IR.getSerie7();
			    IR.valvsbt23.getvalvbt2(numvalv.getText())
				    .setProgasoc(0);
			    interruptor(
				    IR.valvsbt23.getvalvbt2(numvalv.getText()),
				    1, 7);
			}

		    } else if (rdbtnStn4.isSelected()) {
			rango(4);
			if (!numvalv.getText().contentEquals("")) {
			    // valv =Integer.parseInt(numvalv.getText())-4001;
			    conserie = IR.getSerie8();
			    IR.valvsbt24.getvalvbt2(numvalv.getText())
				    .setProgasoc(0);
			    interruptor(
				    IR.valvsbt24.getvalvbt2(numvalv.getText()),
				    1, 8);
			}

		    }

		} catch (NumberFormatException e) {
		    if (logger.isErrorEnabled()) {
			logger.error(e.getMessage());
		    }
		}
	    }

	});
	btnAbrir.setIcon(null);
	btnAbrir.setBounds(53, 82, 108, 25);
	add(btnAbrir);

	btnCerrar = new JButton("Cerrar");
	btnCerrar.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		// Aki hay que llamar a comprobar formato de numero de valvula
		// !!
		try {
		    filtro();
		    // int valv;

		    if (rdbtnStn1.isSelected()) {
			rango(1);
			if (!numvalv.getText().contentEquals("")) {
			    // valv =Integer.parseInt(numvalv.getText())-1001;
			    conserie = IR.getSerie5();
			    IR.valvsbt2.getvalvbt2(numvalv.getText())
				    .setProgasoc(0);
			    interruptor(
				    IR.valvsbt2.getvalvbt2(numvalv.getText()),
				    2, 5);

			}
		    } else if (rdbtnStn2.isSelected()) {
			rango(2);
			if (!numvalv.getText().contentEquals("")) {
			    // valv =Integer.parseInt(numvalv.getText())-2001;
			    conserie = IR.getSerie6();
			    IR.valvsbt22.getvalvbt2(numvalv.getText())
				    .setProgasoc(0);
			    interruptor(
				    IR.valvsbt22.getvalvbt2(numvalv.getText()),
				    2, 6);
			}

		    } else if (rdbtnStn3.isSelected()) {
			rango(3);
			if (!numvalv.getText().contentEquals("")) {
			    // valv =Integer.parseInt(numvalv.getText())-3001;
			    conserie = IR.getSerie7();
			    IR.valvsbt23.getvalvbt2(numvalv.getText())
				    .setProgasoc(0);
			    interruptor(
				    IR.valvsbt23.getvalvbt2(numvalv.getText()),
				    2, 7);
			}

		    } else if (rdbtnStn4.isSelected()) {
			rango(4);
			if (!numvalv.getText().contentEquals("")) {
			    // valv =Integer.parseInt(numvalv.getText())-4001;
			    conserie = IR.getSerie8();
			    IR.valvsbt24.getvalvbt2(numvalv.getText())
				    .setProgasoc(0);
			    interruptor(
				    IR.valvsbt24.getvalvbt2(numvalv.getText()),
				    2, 8);
			}

		    }

		} catch (NumberFormatException e1) {
		    if (logger.isErrorEnabled()) {
			logger.error(e1.getMessage());
		    }
		}

	    }
	});
	btnCerrar.setBounds(184, 82, 108, 25);
	add(btnCerrar);

	btnReset = new JButton("Reset");
	btnReset.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (rdbtnStn1.isSelected()) {
		    if (IR.valvsbt2 != null)
			interruptor(IR.valvsbt2.getvalvbt2(0), 3, 5);
		} else if (rdbtnStn2.isSelected()) {
		    if (IR.valvsbt22 != null)
			interruptor(IR.valvsbt22.getvalvbt2(0), 3, 6);
		} else if (rdbtnStn3.isSelected()) {
		    if (IR.valvsbt23 != null)
			interruptor(IR.valvsbt23.getvalvbt2(0), 3, 7);
		} else if (rdbtnStn4.isSelected()) {
		    if (IR.valvsbt24 != null)
			interruptor(IR.valvsbt24.getvalvbt2(0), 3, 8);
		}

	    }
	});
	btnReset.setBounds(315, 82, 108, 25);
	add(btnReset);

	JLabel lblRx = new JLabel("RX");
	lblRx.setBounds(94, 199, 28, 15);
	add(lblRx);

	textRx = new JTextField();
	textRx.setEditable(false);
	textRx.setBounds(124, 197, 238, 19);
	add(textRx);
	textRx.setColumns(10);

	JLabel lblTx = new JLabel("TX");
	lblTx.setBounds(94, 170, 28, 15);
	add(lblTx);

	textTx = new JTextField();
	textTx.setEditable(false);
	textTx.setColumns(10);
	textTx.setBounds(124, 168, 238, 19);
	add(textTx);

	lblInfo = new JLabel("");
	lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
	lblInfo.setFont(new Font("Dialog", Font.PLAIN, 12));
	lblInfo.setBounds(56, 254, 365, 15);
	add(lblInfo);

	lblconsum = new JLabel("");
	lblconsum.setHorizontalAlignment(SwingConstants.CENTER);
	lblconsum.setFont(new Font("Dialog", Font.PLAIN, 12));
	lblconsum.setBounds(58, 280, 363, 15);
	add(lblconsum);

	lblEstado = new JLabel("");
	lblEstado.setHorizontalAlignment(SwingConstants.CENTER);
	lblEstado.setFont(new Font("Dialog", Font.PLAIN, 12));
	// Para saber si es la mci o la mci2
	// if (Integer.parseInt(valv.getCodelecvalv())<1120)
	// tipoplaca=3;
	// else
	// tipoplaca=4;
	lblEstado.setBounds(56, 228, 365, 15);
	add(lblEstado);

	JButton btnabiertas = new JButton("Estaciones abiertas ?");
	btnabiertas.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {

		// TODO Aki hay que llamar al método cualesabiertas de
		// serialdriver

		if (rdbtnStn1.isSelected()) {
		    if (IR.valvsbt2 != null)
			IR.getSerie5().cualesabiertasbt(
				IR.getSerie5().serialPort, 5);
		} else if (rdbtnStn2.isSelected()) {
		    if (IR.valvsbt22 != null)
			IR.getSerie6().cualesabiertasbt(
				IR.getSerie6().serialPort, 6);
		} else if (rdbtnStn3.isSelected()) {
		    if (IR.valvsbt23 != null)
			IR.getSerie7().cualesabiertasbt(
				IR.getSerie7().serialPort, 7);
		} else if (rdbtnStn4.isSelected()) {
		    if (IR.valvsbt24 != null)
			IR.getSerie8().cualesabiertasbt(
				IR.getSerie8().serialPort, 8);
		}

	    }
	});
	btnabiertas.setBounds(53, 122, 370, 32);
	add(btnabiertas);

    }

    protected boolean filtro() {

	numvalv.addKeyListener(new KeyAdapter() {
	    @Override
	    public void keyTyped(KeyEvent e) {

		char caracter = e.getKeyChar();

		if (rdbtnStn1.isSelected()) {

		    try {
			// Enter
			if ((int) caracter == 10) {

			} else {
			    // Verificar si la tecla pulsada no es un digito
			    if (((caracter < '0') || (caracter > '9'))
				    && (caracter != '\b' /*
							  * corresponde a
							  * BACK_SPACE
							  */)) {
				e.consume(); // ignora el evento de teclado
				aviso(1);
				numvalv.setText(null);

			    }

			}
		    } catch (NumberFormatException e1) {

		    }

		} else if (rdbtnStn2.isSelected()) {
		    try {

			// Enter
			if ((int) caracter == 10) {

			} else {
			    // Verificar si la tecla pulsada no es un digito
			    if (((caracter < '0') || (caracter > '9'))
				    && (caracter != '\b' /*
							  * corresponde a
							  * BACK_SPACE
							  */)) {
				e.consume(); // ignora el evento de teclado
				aviso(2);
				numvalv.setText(null);

			    }
			}
		    } catch (NumberFormatException e1) {
		    }

		} else if (rdbtnStn3.isSelected()) {
		    try {

			// Enter
			if ((int) caracter == 10) {

			} else {
			    // Verificar si la tecla pulsada no es un digito
			    if (((caracter < '0') || (caracter > '9'))
				    && (caracter != '\b' /*
							  * corresponde a
							  * BACK_SPACE
							  */)) {
				e.consume(); // ignora el evento de teclado
				aviso(3);
				numvalv.setText(null);

			    }
			}
		    } catch (NumberFormatException e1) {
		    }
		} else if (rdbtnStn4.isSelected()) {
		    try {

			// Enter
			if ((int) caracter == 10) {

			} else {
			    // Verificar si la tecla pulsada no es un digito
			    if (((caracter < '0') || (caracter > '9'))
				    && (caracter != '\b' /*
							  * corresponde a
							  * BACK_SPACE
							  */)) {
				e.consume(); // ignora el evento de teclado
				aviso(4);
				numvalv.setText(null);

			    }
			}
		    } catch (NumberFormatException e1) {
		    }
		}

	    }
	});

	return retonno;

    }

    protected boolean rango(int i) {

	boolean retonno = false;

	try {

	    int z = Integer.parseInt(numvalv.getText());
	    if (logger.isInfoEnabled()) {
		logger.info("A ver rangor: " + z);
	    }
	    if (i == 1) {
		if (z < 1001 || z > 1119) {
		    aviso(1);
		    numvalv.setText(null);
		    retonno = false;
		} else
		    retonno = true;
	    } else if (i == 2) {
		if (z < 2001 || z > 2119) {
		    aviso(2);
		    numvalv.setText(null);
		    retonno = false;
		} else
		    retonno = true;
	    } else if (i == 3) {
		if (z < 3001 || z > 3119) {
		    aviso(3);
		    numvalv.setText(null);
		    retonno = false;
		} else
		    retonno = true;
	    } else if (i == 4) {
		if (z < 4001 || z > 4119) {
		    aviso(4);
		    numvalv.setText(null);
		    retonno = false;
		} else
		    retonno = true;
	    }

	} catch (NumberFormatException e1) {
	    if (logger.isErrorEnabled()) {
		logger.error(e1.getMessage());
	    }
	}

	return retonno;
    }

    protected void aviso(int tipo) {
	if (tipo == 1) {
	    JOptionPane.showMessageDialog(IR.frmIrrisoft,
		    "Introduzca un numero entre 1001 y 1119 (ambos incluidos)", //$NON-NLS-1$
		    "Error", JOptionPane.ERROR_MESSAGE);
	} else if (tipo == 2) {
	    JOptionPane.showMessageDialog(IR.frmIrrisoft,
		    "Introduzca un numero entre 2001 y 2119 (ambos incluidos)", //$NON-NLS-1$
		    "Error", JOptionPane.ERROR_MESSAGE);
	} else if (tipo == 3) {
	    JOptionPane.showMessageDialog(IR.frmIrrisoft,
		    "Introduzca un numero entre 3001 y 3119 (ambos incluidos)", //$NON-NLS-1$
		    "Error", JOptionPane.ERROR_MESSAGE);
	} else if (tipo == 4) {
	    JOptionPane.showMessageDialog(IR.frmIrrisoft,
		    "Introduzca un numero entre 4001 y 4119 (ambos incluidos)", //$NON-NLS-1$
		    "Error", JOptionPane.ERROR_MESSAGE);
	}
    }

    public void interruptor(Valvula valv, int accion, int tipoplaca) {

	if (valv.getCodelecvalv() == null
		|| Integer.parseInt(valv.getCodelecvalv()) == -1) {
	    JOptionPane.showMessageDialog(IR.frmIrrisoft, "La estación "
		    + numvalv.getText()
		    + " no existe !\nPruebe a abrir una que exista.\n",
		    "Error", JOptionPane.ERROR_MESSAGE);
	    numvalv.setText("");
	    return;

	}

	boolean fallo = false;
	int abiertasbt2 = 0;
	if (logger.isInfoEnabled()) {
	    logger.info("Valvula: " + valv.getCodelecvalv());
	}
	tipoplaca = valv.getNum_placa();
	if (logger.isInfoEnabled()) {
	    logger.info("Tipoplaca en interreptur panelbt2: " + tipoplaca);
	}
	conserie = valv.getSerie();

	// Compruebo que la conexion serie está viva. Sino reconecto
	try {
	    if (!conserie.serialPort.isOpened()) {
		SerialPort serialPort = new SerialPort(
			conserie.serialPort.getPortName());
		conserie.setSerialPort(serialPort);
		conserie.conectaserial(tipoplaca);
	    }
	} catch (Exception e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	}

	// Si es abrir
	if (accion == 1) {

	    if (!valv.isAbierta()) {

		abiertasbt2 = 0;

		for (Valvula v : IR.listavalvsabiertas(valv.getNum_placa())) {

		    if (v.getNum_placa() == valv.getNum_placa())
			abiertasbt2++;
		}
		if (abiertasbt2 > Irrisoft.config.getLimitebt()) {
		    JOptionPane
			    .showMessageDialog(
				    IR.frmIrrisoft,
				    "Límite de valvulas abiertas en la BT2 alcanzado !\nPruebe a cerrar una válvula si quiere abrir otra\n",
				    "Error", JOptionPane.ERROR_MESSAGE);

		    // Genero alarma: Maximo de estaciones encendidas
		    gestorAlertas.insertarAlarma(3012);

		} else {

		    compruebalimite(valv, abiertasbt2);

		}

	    } else {
		JOptionPane.showMessageDialog(IR.frmIrrisoft, "La válvula "
			+ valv.getCodelecvalv()
			+ " ya se encuentra abierta !\n", "",
			JOptionPane.INFORMATION_MESSAGE);

	    }

	    // Si es cerrar
	} else if (accion == 2) {

	    if (valv.isAbierta()) {

		if (logger.isInfoEnabled()) {
		    logger.info("Cod electrovalvula a cerrar: "
			    + valv.getCodelecvalv());
		}
		if (valv.isMaestra() == 0)
		    fallo = conserie.cierravalv(valv.getCodelecvalv(),
			    valv.getNum_placa());

		if (!fallo && valv.isMaestra() == 0) {
		    valv.setAbierta(false);
		    // La quito de la lista de abiertas
		    IR.quitarvalvabiertas(valv, valv.getNum_placa());
		}
		if (logger.isInfoEnabled()) {
		    logger.info("VALVULA antes de cerrar maestra: "
			    + valv.getCodelecvalv());
		}

		// Cierro la valvula maestra
		if (!IR.inicial) {
		    if (IR.maestra != null) {
			if (!valv.getCodelecvalv().contentEquals(
				IR.maestra.getCodelecvalv()))
			    ;
			IR.panelbt2.cierramaestra();
		    }
		}

	    } else {
		JOptionPane.showMessageDialog(IR.frmIrrisoft, "La válvula "
			+ valv.getCodelecvalv()
			+ " ya se encuentra cerrada !\n", "",
			JOptionPane.INFORMATION_MESSAGE);

	    }

	}

	// Si es resetear
	if (accion == 3) {

	    conserie.reset(tipoplaca);

	}

	// Cierro el puerto serie
	// conserie.desconectaserial();

    }

    public void compruebalimite(Valvula valv, int abiertasbt2) {

	boolean fallo = false;

	// TODO SOBRA porque ya está en interruptor
	if (abiertasbt2 >= Irrisoft.config.getLimitebt()) {
	    JOptionPane
		    .showMessageDialog(
			    IR.frmIrrisoft,
			    "Límite de valvulas abiertas en la BT2 "
				    + valv.getNum_placa()
				    + "alcanzado !\nPruebe a cerrar una válvula si quiere abrir otra\n",
			    "Error", JOptionPane.ERROR_MESSAGE);
	    if (logger.isWarnEnabled()) {
		logger.warn("Límite de valvulas abiertas en la BT2 alcanzado !");
	    }
	    //
	} else {

	    // Si no hay ninguna abierta abrir la maestraaaaa!!!
	    // if
	    // (!valv.getCodelecvalv().contentEquals(IR.maestra.getCodelecvalv())
	    // ||
	    // !valv.getCodelecvalv().contentEquals(IR.maestra1.getCodelecvalv()))

	    // if
	    // (!valv.getCodelecvalv().contentEquals(IR.maestra.getCodelecvalv()))
	    if (IR.haycaudalimetro) {
		if (!IR.test)
		    abremaestra();
	    } else
		abremaestra();

	    if (valv.isMaestra() == 0) {
		// Abro la valvula requerida
		fallo = conserie.abrevalv(valv);

		if (!fallo) {
		    valv.setAbierta(true);
		    // La meto en Valvsabiertas
		    IR.addvalvsabiertas(valv, valv.getNum_placa());
		}
	    }
	}

    }

    // ///////////////////
    // /// MAESTRAS
    public boolean abremaestra() {
	logger.warn("Entra en abremaestra");

	boolean fallo = false;
	// Si no hay ninguna abierta abro la maestra !!

	try {
	    Thread.sleep(IrrisoftConstantes.DELAY_200MSEG);

	    // Primera maestra
	    if (IR.maestra != null) {

		if (!IR.maestra.isAbierta()) {
		    // if (IR.maestra.getNum_placa() == tipoplaca) {

		    if (logger.isInfoEnabled()) {
			logger.info("La maestra esta en la misma placa");

			logger.info("MAEEES" + IR.maestra.getCodelecvalv());
		    }
		    if (!IR.valvsmaestras.get(0).isAbierta())
			fallo = IR.valvsmaestras.get(0).getSerie()
				.abrevalv(IR.maestra);

		    if (!fallo) {
			IR.maestra.setAbierta(true);

			// Repinto si es MCI
			if (IrrisoftConstantes.PLACA_MCI_1 == IR.maestra
				.getNum_placa())
			    IR.panelmci.interruptor(IR.maestra,
				    IR.maestra.getImgasoc(), true,
				    IR.panelmci.panel1);
			else if (IrrisoftConstantes.PLACA_MCI_2 == IR.maestra
				.getNum_placa())
			    IR.panelmci.interruptor(IR.maestra,
				    IR.maestra.getImgasoc(), true,
				    IR.panelmci.panel2);
			else if (IrrisoftConstantes.PLACA_MCI_3 == IR.maestra
				.getNum_placa())
			    IR.panelmci.interruptor(IR.maestra,
				    IR.maestra.getImgasoc(), true,
				    IR.panelmci.panel3);
			else if (IrrisoftConstantes.PLACA_MCI_4 == IR.maestra
				.getNum_placa())
			    IR.panelmci.interruptor(IR.maestra,
				    IR.maestra.getImgasoc(), true,
				    IR.panelmci.panel4);

			IR.addvalvsabiertas(IR.maestra,
				IR.maestra.getNum_placa());

			IR.lblmaestra1.setText("Válvula Maestra 1: ON");
			IR.lblmaestra1.setForeground(new Color(0, 128, 0));
			// Genero alarma: Electrovalvula maestra encendida
			gestorAlertas.insertarAlarma(3015);
		    }

		}
	    }

	    // Segunda maestra
	    if (IR.maestra1 != null) {
		// if (IR.maestra1.getNum_placa() == tipoplaca) {
		if (!IR.maestra1.isAbierta()) {

		    if (logger.isInfoEnabled()) {
			logger.info("LA SEGUNDA MAESTRA va a abrir");
		    }

		    if (!IR.valvsmaestras.get(1).isAbierta())
		    fallo = IR.valvsmaestras.get(1).getSerie()
			    .abrevalv(IR.maestra1);

		    if (!fallo) {
			IR.maestra1.setAbierta(true);
			// Repinto si es MCI
			if (IrrisoftConstantes.PLACA_MCI_1 == IR.maestra1
				.getNum_placa())
			    IR.panelmci.interruptor(IR.maestra1,
				    IR.maestra1.getImgasoc(), true,
				    IR.panelmci.panel1);
			else if (IrrisoftConstantes.PLACA_MCI_2 == IR.maestra1
				.getNum_placa())
			    IR.panelmci.interruptor(IR.maestra1,
				    IR.maestra1.getImgasoc(), true,
				    IR.panelmci.panel2);
			else if (IrrisoftConstantes.PLACA_MCI_3 == IR.maestra1
				.getNum_placa())
			    IR.panelmci.interruptor(IR.maestra1,
				    IR.maestra1.getImgasoc(), true,
				    IR.panelmci.panel3);
			else if (IrrisoftConstantes.PLACA_MCI_4 == IR.maestra1
				.getNum_placa())
			    IR.panelmci.interruptor(IR.maestra1,
				    IR.maestra1.getImgasoc(), true,
				    IR.panelmci.panel4);

			IR.addvalvsabiertas(IR.maestra1,
				IR.maestra1.getNum_placa());

			IR.lblmaestra2.setText("Válvula Maestra 2: ON");
			IR.lblmaestra2.setForeground(new Color(0, 128, 0));
			// Genero alarma: Electrovalvula maestra encendida
			gestorAlertas.insertarAlarma(3015);

		    }
		}

	    }
	    Thread.sleep(IrrisoftConstantes.DELAY_200MSEG);

	} catch (InterruptedException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	}

	return fallo;
    }

    public boolean cierramaestra() {

	boolean fallo = false;
	if (logger.isInfoEnabled()) {
	    logger.info("CIERRAMAESTRA");
	}

	try {
	    Thread.sleep(IrrisoftConstantes.DELAY_200MSEG);

	    // Primera Maestra

	    if (IR.maestra != null) {
		// Cierro la maestra si no hay ninguna más abierta
		if (IR.valvsabiertastot.size() <= IR.maestras) {

		    // if (IR.maestra.isAbierta()) {

		    if (logger.isInfoEnabled()) {
			logger.info("HOLA cierre MAESTRA ");
		    }
		    // if (IR.maestra.getNum_placa() == numplaca) {

		    if (IR.valvsmaestras.get(0).isAbierta())
			fallo = IR.valvsmaestras
				.get(0)
				.getSerie()
				.cierravalv(IR.maestra.getCodelecvalv(),
					IR.maestra.getNum_placa());

		    if (!fallo) {
			IR.maestra.setAbierta(false);
			// Repinto si es MCI
			if (IrrisoftConstantes.PLACA_MCI_1 == IR.maestra
				.getNum_placa())
			    IR.panelmci.interruptor(IR.maestra,
				    IR.maestra.getImgasoc(), true,
				    IR.panelmci.panel1);
			else if (IrrisoftConstantes.PLACA_MCI_2 == IR.maestra
				.getNum_placa())
			    IR.panelmci.interruptor(IR.maestra,
				    IR.maestra.getImgasoc(), true,
				    IR.panelmci.panel2);
			else if (IrrisoftConstantes.PLACA_MCI_3 == IR.maestra
				.getNum_placa())
			    IR.panelmci.interruptor(IR.maestra,
				    IR.maestra.getImgasoc(), true,
				    IR.panelmci.panel3);
			else if (IrrisoftConstantes.PLACA_MCI_4 == IR.maestra
				.getNum_placa())
			    IR.panelmci.interruptor(IR.maestra,
				    IR.maestra.getImgasoc(), true,
				    IR.panelmci.panel4);

			IR.quitarvalvabiertas(IR.maestra,
				IR.maestra.getNum_placa());

			IR.lblmaestra1.setText("Válvula Maestra 1: OFF");
			IR.lblmaestra1.setForeground(Color.RED);

		    }

		}
	    }
	    // }

	    // Segunda Maestra
	    if (IR.maestra1 != null) {

		if (IR.valvsabiertastot.size() <= IR.maestras) {

		    // if (IR.maestra1.getNum_placa() == numplaca) {
		    // if (IR.maestra1.isAbierta())

		    if (IR.valvsmaestras.get(1).isAbierta())
			fallo = IR.valvsmaestras
				.get(1)
				.getSerie()
				.cierravalv(IR.maestra1.getCodelecvalv(),
					IR.maestra1.getNum_placa());

		    if (!fallo) {
			IR.maestra1.setAbierta(false);
			// Repinto si es MCI
			if (IrrisoftConstantes.PLACA_MCI_1 == IR.maestra1
				.getNum_placa())
			    IR.panelmci.interruptor(IR.maestra1,
				    IR.maestra1.getImgasoc(), true,
				    IR.panelmci.panel1);
			else if (IrrisoftConstantes.PLACA_MCI_2 == IR.maestra1
				.getNum_placa())
			    IR.panelmci.interruptor(IR.maestra1,
				    IR.maestra1.getImgasoc(), true,
				    IR.panelmci.panel2);
			else if (IrrisoftConstantes.PLACA_MCI_3 == IR.maestra1
				.getNum_placa())
			    IR.panelmci.interruptor(IR.maestra1,
				    IR.maestra1.getImgasoc(), true,
				    IR.panelmci.panel3);
			else if (IrrisoftConstantes.PLACA_MCI_4 == IR.maestra1
				.getNum_placa())
			    IR.panelmci.interruptor(IR.maestra1,
				    IR.maestra1.getImgasoc(), true,
				    IR.panelmci.panel4);

			IR.quitarvalvabiertas(IR.maestra1,
				IR.maestra1.getNum_placa());
			IR.lblmaestra2.setText("Válvula Maestra 2: OFF");
			IR.lblmaestra2.setForeground(Color.RED);
			// Genero alarma: Electrovalvula maestra apagada.
			gestorAlertas.insertarAlarma(3016);
		    }

		}

	    }

	    Thread.sleep(IrrisoftConstantes.DELAY_200MSEG);

	} catch (InterruptedException e) {
	    if (logger.isErrorEnabled()) {
		logger.error(e.getMessage());
	    }
	}

	return fallo;
    }

    // /////

    public int getStnselec() {
	return stnselec;
    }

    public void setStnselec(int stnselec) {
	this.stnselec = stnselec;
    }
}
