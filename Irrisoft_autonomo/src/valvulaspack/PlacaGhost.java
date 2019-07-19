package valvulaspack;

//Clase parche para crear sensor fantasma que de lecturas a 0 para la sincronización en el TEST

public class PlacaGhost {

    private int numplaca;
    private boolean tieneamp;

    public int getNumplaca() {
	return numplaca;
    }

    public boolean isTieneamp() {
	return tieneamp;
    }

    public void setNumplaca(int numplaca) {
	this.numplaca = numplaca;
    }

    public void setTieneamp(boolean tieneamp) {
	this.tieneamp = tieneamp;
    }

}
