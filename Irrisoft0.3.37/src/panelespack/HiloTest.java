package panelespack;

import javax.swing.JOptionPane;

import irrisoftpack.Irrisoft;
import valvulaspack.ListaValvBt2;
import valvulaspack.ListaValvMci;

public class HiloTest implements Runnable{

	public boolean inicial;
	private int tipoplaca;
	public int tipovalv;
	public int numvalv=0;

	public  HiloTest (boolean inicial, int tipoplaca, int tipovalv) {
	       
        this.inicial=inicial;
        this.tipoplaca=tipoplaca;
        this.tipovalv=tipovalv;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		empiezatest(inicial,tipoplaca,tipovalv);
	}
	
public void empiezatest (boolean inicial, int tipoplaca, int tipovalv){
		
		//Paro la lectura automática del caudalimetro
		//TODO En realidad el contador de pulsos está ahora en la bt2, pero puede estar en la MCI (según configuración inicial)
		inicializavars(true);
		
		try {
			
			//Es un testeo particular
			if (!inicial){
				//Es una valvula mci
				if (tipovalv==1){
					Irrisoft.window.hilocaudal.tipovalv=1;
					//abro la valvula requerida
					Irrisoft.window.panelmci.interruptor(ListaValvMci.getInstance().getvalvmci(numvalv), ListaValvMci.getInstance().getvalvmci(numvalv).getImgasoc());	
				}
				//Es una valvula bt2
				else if (tipovalv==2){
					
					Irrisoft.window.hilocaudal.tipovalv=2;
					//Irrisoft.window.semaforobt2.take();
					//abro la valvula requerida 
					Irrisoft.window.panelbt2.interruptor(ListaValvBt2.getInstance().getvalvbt2(Integer.toString(numvalv)), 1,3);
					//Irrisoft.window.semaforobt2.release();
					
				}
				Irrisoft.window.paneltest.lbltesteando.setText("Testeando, espere ...");
				Irrisoft.window.hilocaudal.setTest(true);
				synchronized (Irrisoft.window.paneltest.hilotest){
					Irrisoft.window.paneltest.hilotest.wait();
				}
				inicializavars(false);
			//Es el testeo inicial
			}else if (inicial){
				
					//Borro el contenido de la tabla consumtest (ya que se va a hacer un testeo inicial)
					Irrisoft.window.hilocaudal.connDB.borraconsumtest();
				
					for (int i=0;i<ListaValvMci.getInstance().getsizeof();i++){
						inicializavars(true);
						numvalv=i;
						Irrisoft.window.hilocaudal.tipovalv=1;
						//abro la valvula requerida
						Irrisoft.window.panelmci.interruptor(ListaValvMci.getInstance().getvalvmci(i), ListaValvMci.getInstance().getvalvmci(i).getImgasoc());
					
						Irrisoft.window.paneltest.lbltesteando.setText("Testeando, espere ...");
						
						Irrisoft.window.hilocaudal.setTest(true);
						Irrisoft.window.paneltest.lblvalv.setText(Integer.toString(i+1));
						
						synchronized (Irrisoft.window.paneltest.hilotest){
							Irrisoft.window.paneltest.hilotest.wait();
						}
						
					}
					
					for (int i=0;i<ListaValvBt2.getInstance().getsizeof();i++){
						inicializavars(true);
						Irrisoft.window.hilocaudal.tipovalv=2;
						numvalv= Integer.parseInt(ListaValvBt2.getInstance().getvalvbt2(i).getCodelecvalv());
						
						
						//abro la valvula requerida 
						Irrisoft.window.panelbt2.interruptor(ListaValvBt2.getInstance().getvalvbt2(i), 1,3);
						Irrisoft.window.paneltest.lbltesteando.setText("Testeando, espere ...");
						
						Irrisoft.window.hilocaudal.setTest(true);
						Irrisoft.window.paneltest.lblvalv.setText(Integer.toString(numvalv));
						
						synchronized (Irrisoft.window.paneltest.hilotest){
							Irrisoft.window.paneltest.hilotest.wait();
						}
					}	
					inicializavars(false);
					JOptionPane.showMessageDialog(Irrisoft.window.frmIrrisoft, "Testeo inicial finalizado correctamente!\nSe han escrito los caudales en la tabla modelo.");
					
				}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}

public void inicializavars(boolean empezar){
	
	//Inicializa las variables del paneltest
	
	if (empezar){
		Irrisoft.window.paneltest.lbltesteando.setText("Testeando, espere...");
		Irrisoft.window.paneltest.lblPulsosDat.setText("");
		Irrisoft.window.paneltest.lblCauDat.setText("");
		Irrisoft.window.paneltest.lblIntenDat.setText("");
		Irrisoft.window.paneltest.lblls.setText("");
		Irrisoft.window.paneltest.lblamp.setText("");
		Irrisoft.window.paneltest.lblpul.setText("");
		Irrisoft.window.paneltest.lblvalv.setText("");
		Irrisoft.window.paneltest.lbltesteando.setText("");
		Irrisoft.window.hilocaudal.setMci(false);
		Irrisoft.window.hilocaudal.setTerminar(true);		
		Irrisoft.window.hilocaudal.setPulsostot(0);
		Irrisoft.window.hilocaudal.setVuelta(0);
		Irrisoft.window.paneltest.btnAtras.setText("Cancelar");
		Irrisoft.window.paneltest.btnTestearMCI.setEnabled(false);
		Irrisoft.window.paneltest.btntesteoini.setEnabled(false);
		Irrisoft.window.paneltest.btnTestearBT2.setEnabled(false);
		Irrisoft.window.paneltest.comboBoxMCI.setEnabled(false);
		Irrisoft.window.paneltest.comboBoxBt2.setEnabled(false);
		Irrisoft.window.paneltest.progressBar.setMaximum(25);
	}else{
		
		if (inicial){
			Irrisoft.window.paneltest.lblPulsosDat.setText("");
			Irrisoft.window.paneltest.lblCauDat.setText("");
			Irrisoft.window.paneltest.lblIntenDat.setText("");
			Irrisoft.window.paneltest.lblls.setText("");
			Irrisoft.window.paneltest.lblamp.setText("");
			Irrisoft.window.paneltest.lblpul.setText("");
			Irrisoft.window.paneltest.lblvalv.setText("");
			Irrisoft.window.paneltest.lbltesteando.setText("");
			Irrisoft.window.paneltest.lblvalv.setText("");
			Irrisoft.window.paneltest.lbltesteando.setText("");
		}
		Irrisoft.window.paneltest.btnAtras.setEnabled(true);
		Irrisoft.window.paneltest.btnTestearMCI.setEnabled(true);
		Irrisoft.window.paneltest.btntesteoini.setEnabled(true);
		Irrisoft.window.paneltest.btnTestearBT2.setEnabled(true);
		Irrisoft.window.paneltest.comboBoxMCI.setEnabled(true);
		Irrisoft.window.paneltest.comboBoxBt2.setEnabled(true);
		Irrisoft.window.paneltest.progressBar.setValue(0);
		Irrisoft.window.paneltest.btnAtras.setText("Atras");
		Irrisoft.window.paneltest.HiloTest.tipovalv=0;
	
		Irrisoft.window.hilocaudal.vuelta=0;
		Irrisoft.window.hilocaudal.pulsostot=0;
		Irrisoft.window.hilocaudal.inicial=false;
		Irrisoft.window.hilocaudal.setTerminar(false);
		Irrisoft.window.hilocaudal.setTest(false);
	}
	
	Irrisoft.window.paneltest.repaint();
}

}
