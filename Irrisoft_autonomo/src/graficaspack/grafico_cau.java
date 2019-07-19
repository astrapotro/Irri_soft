package graficaspack;

import irrisoftpack.Irrisoft;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.Timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeriesCollection;

import java.awt.Dialog.ModalExclusionType;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class grafico_cau extends JDialog implements Runnable {

    private static Logger logger = LogManager.getLogger(grafico_cau.class
	    .getName());

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JFreeChart chart;
    private DynamicTimeSeriesCollection dataset;
    private int tipo;
    private int placa; // 0 sensores,1 MCI, 2 BT2
    private boolean caudal;
    private String titulo, ejex, ejey;
    public Timer timer=null;
    private ChartPanel chartPanel;
    public String queplaca;

    public boolean terminar;
    
    // SINGLETON
    private static grafico_cau instance;

    public static grafico_cau getInstance() {

	if (instance == null) {
	    return new grafico_cau();
	}
	return instance;
    }

    /**
     * Create the frame.
     */
    public grafico_cau() {
	setType(Type.NORMAL);
	//setModal(true);
	setTitle("Grafica de lecturas del caudalimetro, "+queplaca);
	setResizable(false);
	setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	setBounds(100, 100, 800, 500);
	      
	//Para que cuando cierere la ventana s pare el timer
	this.addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(final WindowEvent event) {
	        timer.stop();
	    }
	});

    }

    @Override
    public void run() {

	// // Create a time series chart
	// org.jfree.data.time.TimeSeries pop = new
	// org.jfree.data.time.TimeSeries("Pulsos actuales", Day.class);
	// pop.add(new Day(2, 1, 2007), 100);
	// pop.add(new Day(2, 2, 2007), 150);
	// pop.add(new Day(2, 3, 2007), 200);
	// pop.add(new Day(2, 4, 2007), 250);
	// pop.add(new Day(2, 5, 2007), 300);
	// pop.add(new Day(2, 6, 2007), 1500);
	//
	// TimeSeriesCollection dataset = new TimeSeriesCollection();
	// dataset.addSeries(pop);
	//
	// chart = ChartFactory.createTimeSeriesChart(
	// "Pulsos actuales caudalimetro placa"+1,
	// "Fecha",
	// "Numero Pulsos",
	// dataset,
	// true,
	// true,
	// false);
	//
	// ChartPanel chPanel = new ChartPanel(chart); //creating the chart
	// panel, which extends JPanel
	// chPanel.setRefreshBuffer(true);
	// chPanel.setMouseZoomable(true);
	// chPanel.setPreferredSize(new Dimension(785, 440)); //size according
	// to my window
	// chPanel.setMouseWheelEnabled(true);

	grafica();

    }

    public void grafica() {

	
	XYPlot plot;
	ValueAxis axis;
	
	
	
	
	         if (caudal){
	             titulo="Caudal estimado del caudalimetro";
	             ejex="Caudal estimado l/s";
	             ejey="Hora";
	         }
	         else{
	             titulo="Pulsos nuevos caudalimetro";
	             ejex="Pulsos nuevos";
	             ejey="Hora";
	         }
		
		Calendar ahora = Calendar.getInstance();
		
		
        	  	dataset = new DynamicTimeSeriesCollection(1, 1440, new Minute());
        	        dataset.setTimeBase(new Minute(new Date(ahora.getTime().getTime()-(60*1438*1000)),ahora.getTimeZone(),new Locale(System.getProperty("user.language"), System.getProperty("user.country"))));
        	        dataset.addSeries(new float[1], 0, titulo);
        	        
        	        chart = ChartFactory.createTimeSeriesChart(
        	        	"", ejey, ejex , dataset, true,
        	            true, false);
        	        
        	        plot = chart.getXYPlot();
        	        
        
        	        axis = plot.getDomainAxis();	        
        	        axis.setAutoRange(true);
        	        axis.setFixedAutoRange(600000); // proportional to scroll speed
        	        axis = plot.getRangeAxis();
        	        
        	        
        
        	        chartPanel = new ChartPanel(chart);
        	        chartPanel.setPreferredSize(new Dimension(785, 440));
                	chartPanel.setRefreshBuffer(true);
                	chartPanel.setDoubleBuffered(true);
                	chartPanel.setMouseZoomable(true);
                	
                	
		
	        
	        timer = new Timer(60000, new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                EventQueue.invokeLater(new Runnable() {
	                    @Override
	                    public void run() {
	                	
	                     
	                   
	                	 
	                	//Aki muestro la lectura que corresponda con el tipo de placa (sens,mci,bt2) y el numero de placa (1,2,3,4);
	                	if (placa==0){
	                	    if (!caudal)
	                		update(Irrisoft.window.panelecturasens.listapulsoscau[tipo]);
	                	    else if (caudal)
	                		update(Irrisoft.window.panelecturasens.listacaudales[tipo]);
	                	    
	                	}else if (placa == 1){
	                	    if (!caudal)
	                		update(Irrisoft.window.panelecturasmci.listapulsoscau[tipo]);
	                	    else if (caudal)
	                		update(Irrisoft.window.panelecturasmci.listacaudales[tipo]);
	                	}else if (placa == 2){
	                	    if (!caudal)
	                		update(Irrisoft.window.panelecturasbt2.listapulsoscau[tipo]);
	                	    else if (caudal)
	                		update(Irrisoft.window.panelecturasbt2.listacaudales[tipo]);
	                	  	                
	                	}
	                     
	                	logger.info("TIMEEER");
	                	
	                	
	                    }
	                });
	            }
	        });
	        
	         
	     	 timer.start();
		        
//		try {
//		ChartUtilities.saveChartAsJPEG(new File("/home/mikel/TimeSeries.jpg"), chart, 500, 300);
	//	
//		} catch (IOException e) {
//		System.err.println("Error creando grafico.");
//		}
	       
	        add(chartPanel);	
	        setVisible(true);
	
    }

    /**
     * @param value
     */
    public void update(float value) {
	float[] newData = new float[1];
	newData[0] = value;
	dataset.advanceTime();
	dataset.appendData(newData);
    }

    public int getTipo() {
	return tipo;
    }

    public void setTipo(int tipo) {
	this.tipo = tipo;
    }

    public int getPlaca() {
	return placa;
    }

    public void setPlaca(int placa) {
	this.placa = placa;
    }

    public boolean isCaudal() {
	return caudal;
    }

    public void setCaudal(boolean caudal) {
	this.caudal = caudal;
    }
}
