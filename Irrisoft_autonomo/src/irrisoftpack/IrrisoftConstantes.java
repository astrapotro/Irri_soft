package irrisoftpack;

public interface IrrisoftConstantes {
    
    static final String IMG_OFF = "/irrisoftpack/imagenes/multioff.png";
    
    static final String IMG_ON = "/irrisoftpack/imagenes/multion.png";
    
    static final String IMG_GOTITA = "/irrisoftpack/imagenes/gotita.png";
    
    static final String IMG_LOGO = "/irrisoftpack/imagenes/Raspberry-Pi-logo.png";
    
    static final String IMG_GESTDROPPER = "/irrisoftpack/imagenes/gestdropper.png";
    
    static final String IMG_GEST = "/irrisoftpack/imagenes/irrigestlife.png";
    
    static final String IMG_MCI = "/irrisoftpack/imagenes/placamci.png";
    
    static final String IMG_BT2 = "/irrisoftpack/imagenes/placabt2.png";
    
    static final String IMG_SAMCLA = "/irrisoftpack/imagenes/samcla.png";
    
    static final String IMG_AJUSTES = "/irrisoftpack/imagenes/engranajes.png";
    
    static final String IMG_INFO = "/irrisoftpack/imagenes/info.png";
    
    static final String ARGISOFT_LOG = "/Irrisoft.log";

    static final String PROG_DIARIA = "D";
    
    static final String PROG_SEMANAL = "S";

    static final String PROGRAMA_ACTIVO = "S";
    
    static final String BLOQUE_PARALELO = "P";
    
    static final String BLOQUE_SERIE = "S";

    static final int BT2_1000 = 1000;

    static final int BT2_2000 = 2000;

    static final int BT2_3000 = 3000;

    static final int BT2_4000 = 4000;

    static final int SAMCLA = 9000;
    
    static final int MCI_100 = 100;

    static final int MCI_200 = 200;

    static final int MCI_300 = 300;

    static final int MCI_400 = 400;

    static final int MCI_500 = 500;

    //TAREAS
    static final int CONFIG_ESTACION = 18;

    static final int CONFIG_SENSORES = 17;

    static final int CONFIG_PROGRAMADOR = 16;
    
    static final int CANCELAR_PROGRAMA = 12;

    static final int APAGAR_PROGRAMADOR = 4;

    static final int RECALCULO_PROGRAMACION = 6;

    static final int CERRAR_ESTACION = 2;

    static final int ABRIR_ESTACION = 1;

    static final int LEER_PROGRAMACION = 3;
    /////
    
    //TIPO PLACAS SENSORES
    static final CharSequence PLACA_TIPO_SENSORES = "SENSORES";

    static final CharSequence PLACA_TIPO_RPI = "raspberry";

    static final CharSequence PLACA_TIPO_CONTROLADORA = "CONTROL";

    static final int PLACA_1 = 1;
    
    static final int PLACA_2 = 2;
    
    static final int PLACA_3 = 3;
    
    static final int PLACA_4 = 4;

    static final int PLACA_SENSORES_0 = 0;
    
    static final int VALVS_ABIERTAS_TOT = 0;
    
    //PLACA ELECTROVÁLVULAS
    static final int PLACA_MCI_1 = 1;
    
    static final int PLACA_MCI_2 = 2;
    
    static final int PLACA_MCI_3 = 3;
    
    static final int PLACA_MCI_4 = 4;
    
    static final int PLACA_BT2_5 = 5;
    
    static final int PLACA_BT2_6 = 6;
    
    static final int PLACA_BT2_7 = 7;
    
    static final int PLACA_BT2_8 = 8;

    static final int PLACA_SAMCLA = -1;
    //
    static final  String PUERTO_SAMCLA = "eth0";

    static final int TAREA_HECHA = 1;

    static final CharSequence SERIAL_TIPO_CONTROLADORA = "controladora";

    static final CharSequence SERIAL_TIPO_SENSORES = "sensores";

    static final int SENSOR_CAUDALIMETRO = 1;
    
    static final int SENSOR_HIGROMETRO = 2;
    
    static final int SENSOR_AMPERIMETRO = 3;
    
    static final int SENSOR_PLUVIOMETRO = 4;
    
    static final int SENSOR_TERMOMETRO = 5;
    
    static final int SENSOR_ANEMOMETRO = 6;
    
    static final int SENSOR_FLUJO = 7;
    
    static final int SENSOR_INTRUSION = 8;

    static final long DELAY_LEVANTAR_PROGRAMA_10SEG = 10000;
    
    static final long DELAY_TAREA_PROGRAMASEG = 7000;
    
    static final long DELAY_TAREA_5SEG = 5000;
    
    static final int VALIDATION_TIMEOUT_10 = 10;
    
    static final long TIEMPO_EJECUCION_MEDIO_SEG = 500;
    
    static final int TIEMPO_EJECUCION_10SEC = 10;
    
    static final long TIEMPO_EJECUCION_APARCAR_HILO_10SEG = 10000000000L;
    
    static final int TIEMPO_EJECUCION_2SEG = 2;
    
    static final long TIEMPO_EJECUCION__APARCAR_HILO_2SEG = 2000;
    
    static final long DELAY_SOLTAR_SEMAFORO = 150;
    
    static final long DELAY_100MSEG = 100;
    
    static final long DELAY_200MSEG = 200;
    
    static final long DELAY_TAREA_ABRIR = 800;
    
    static final long DELAY_LEERCONF_SENSORES = 500;
    
    static final long DELAY_ACTUABT_150MSEG = 150;
    
    static final long DELAY_LEERESP_50MSEG = 50;
    
    static final long DELAY_CONSULTCON_200MSEG = 200;
    
    static final long DELAY_ACTIVABT_60MSEG = 60;
    
    static final long DELAY_PA_LEC_10SEG = 10000;
    
    static final long DELAY_SENSOR_RUN = 5000;
    
    static final long DELAY_ESLAHORA_10SEG = 10000;
    
    static final long DELAY_CIERRAVALV = 1500;
    
    static final long DELAY_REGANDO = 10000;
    
    static final long DELAY_FREC_LECT = 1000;
    
    static final long DELAY_LLENADO_CIRCUITO = 50000;
    
    static final long DELAY_EST_CIRCUITO = 60000;
    
    static final long DELAY_SENSOR_30SEG = 30000;
    
    static final long DELAY_SENSOR_20SEG = 20000;
    
    static final long DELAY_SENSOR_10SEG = 10000;
    
    static final long DELAY_SENSOR_5SEG = 5000;
    
    static final long DELAY_SENSOR_3SEG = 3000;
    
    static final long DELAY_SENSOR_25SEG = 2500; 
    
    static final long DELAY_SENSOR_2SEG = 2000;
    
    static final String PASSWORD = "CIFRADO";

    static final Object ACERCA_DE = "Irrisoft version 0.3.51\n"
	    + "Programa que gestiona el riego automático integrado en un SIG/GIS.\n"
	    + "Autores:\tMikel Merino <astrapotro@gmail.com>\n"
	    + "\tAlberto Díez <alberto.diez.lejarazu@gmail.com>\n"
	    + "\tDiego Alonso <diego.lonso.gonzalez@gmail.com>"
	    + "\n\nTodos los derechos reservados 2015 \n\n";
    
}
