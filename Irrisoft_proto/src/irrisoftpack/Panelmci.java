package irrisoftpack;



import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;



public class Panelmci extends JPanel {

	private static final long serialVersionUID = 1L;
	//SINGLETON
	private  static Panelmci instance;
	
	private int tipoplaca;
	
	
	
	public static Panelmci getInstance(){
		
		if (instance ==null){
			return new Panelmci();
		}
		return instance;
	}
	
	
	private Panelmci() {
		super();
		this.setBounds(10, 84, 465, 291);
		setLayout(null);
		
		
		JLabel placa1 = new JLabel("PLACA MCI 1");
		placa1.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		placa1.setHorizontalAlignment(SwingConstants.CENTER);
		placa1.setBounds(192, 0, 102, 32);
		add(placa1);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(16, 136, 432, 2);
		add(separator);
		
		JLabel placa2 = new JLabel("PLACA MCI 2");
		placa2.setHorizontalAlignment(SwingConstants.CENTER);
		placa2.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		placa2.setBounds(192, 142, 102, 32);
		add(placa2);
		
		
		
		
		//BOTON ATRAS
		JButton btnAtras = new JButton("Atras");
		btnAtras.setBounds(177, 261, 135, 25);
		btnAtras.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Panelmci.this.setVisible(false);
				Irrisoft.window.panelpral.setVisible(true);
			}
		});
		add(btnAtras);

	
	}
	

//
//
///////////////////////////////// Inicializacion valvulas
protected void inicializavalvs(){
	
	//Dimensiones y posición de las imágenes.
	int x=16,y=48,w=34,z=15;
	
	//Dimensiones y posición de los labels.
	int a=16,b=33,c=34,d=15;
	
	int ant = 0,ante = 0;
	
	
	//
	///////////////// MCIs
	
	//El 25 es susceptible de cambiarse (será 28)!!
	for (Integer i=0;i<8;i++){

		Irrisoft.window.valvsmci.addvalvmci(new Valvula());
		Irrisoft.window.valvsmci.getvalvmci(i).getImgasoc().setIcon(new ImageIcon(Irrisoft.class.getResource(Irrisoft.window.rutaoff)));

		if (i<9){
			Irrisoft.window.valvsmci.getvalvmci(i).setCodelecvalv("0"+Integer.toString(i+1));
			Irrisoft.window.valvsmci.getvalvmci(i).getImgasoc().setName("imglbl0"+(i+1));
		}else{
			Irrisoft.window.valvsmci.getvalvmci(i).setCodelecvalv(Integer.toString(i+1));
		}
		
		
		if (i==0){
			Irrisoft.window.valvsmci.getvalvmci(i).getImgasoc().setBounds(x, y, w, z);
		}else if (i!=0 && i<12){
			ant = ant +36;
			Irrisoft.window.valvsmci.getvalvmci(i).getImgasoc().setBounds(x+ant, y, w, z);
		}
		
		//Irrisoft.window.redimensionado(Irrisoft.window.valvsmci.getvalvmci(i).getImgasoc(), Irrisoft.window.rutaoff);
		
		if (Irrisoft.window.valvsmci.getvalvmci(i).isAbierta()){
			Irrisoft.window.redimensionado(Irrisoft.window.valvsmci.getvalvmci(i).getImgasoc(),  Irrisoft.window.rutaon);
			Irrisoft.window.panelmci.add(Irrisoft.window.valvsmci.getvalvmci(i).getImgasoc());
		}else{
			Irrisoft.window.redimensionado(Irrisoft.window.valvsmci.getvalvmci(i).getImgasoc(),  Irrisoft.window.rutaoff);
			Irrisoft.window.panelmci.add(Irrisoft.window.valvsmci.getvalvmci(i).getImgasoc());
			
		}

		Irrisoft.window.valvsmci.getvalvmci(i).getImgasoc().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//OJO --> No me ha quedado más remedio que hacer esto para saber a qué valvula está haciendo click !!
				String num = e.getSource().toString().substring(26, 27);
				int numvalv = Integer.parseInt(num)-1;
				Irrisoft.window.panelmci.interruptor(Irrisoft.window.valvsmci.getvalvmci(numvalv),Irrisoft.window.valvsmci.getvalvmci(numvalv).getImgasoc());
				
			}	
		});
		
		JLabel lbl = new JLabel();
		lbl.setText(Integer.toString(i+1));
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		if (i==0){
			lbl.setBounds(a, b, c, d);
			
		}else if (i!=0 && i<12){
			
			ante = ante +36;
			lbl.setBounds(a+ant, b, c, d);
		}
		
		Irrisoft.window.panelmci.add(lbl);
		
	}
	
}


protected void interruptor (Valvula valv, JLabel img){
	
	SerialDriver conserie = new SerialDriver();
	
	//Para saber si es la mci o la mci2
	if (Integer.parseInt(valv.getCodelecvalv())<=28)
		tipoplaca=1;
	else
		tipoplaca=2;
	
	if (tipoplaca==1){
		try {
			conserie.conecta(Irrisoft.window.config.getMci(),tipoplaca);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			System.out.println("No se ha podido conectar con la placa MCI !!");
		}
	}else{
		try {
			conserie.conecta(Irrisoft.window.config.getMci2(),tipoplaca);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			System.out.println("No se ha podido conectar con la placa MCI2 !!");
		}
	}
	
	if (valv.isAbierta()){
			System.out.println("Está abierta y la cierro con la placa "+ tipoplaca);
			conserie.cierravalv(valv.getCodelecvalv(),tipoplaca);
			valv.setAbierta(false);
			Irrisoft.window.redimensionado(img, Irrisoft.window.rutaoff);
			add(img);
			repaint();
	}else{
		System.out.println("Está cerrada y la abro con la placa "+ tipoplaca);
			conserie.abrevalv(valv.getCodelecvalv(),tipoplaca);
			valv.setAbierta(true);
			Irrisoft.window.redimensionado(img, Irrisoft.window.rutaon);
			add(img);
			repaint();	
	}
	//Cierro el puerto serie
	conserie.cierra();
}


protected void interruptor (Valvula valv, JLabel img, boolean solopinta){


	if (valv.isAbierta()){
			
			Irrisoft.window.redimensionado(img, Irrisoft.window.rutaoff);
			add(img);
			Irrisoft.window.panelmci.repaint();
	}else{

			Irrisoft.window.redimensionado(img, Irrisoft.window.rutaon);
			add(img);
			Irrisoft.window.panelmci.repaint();	
	}
}



}
