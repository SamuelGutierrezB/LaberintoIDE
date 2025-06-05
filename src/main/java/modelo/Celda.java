package modelo;

import modelo.Entidad;

public class Celda {
    private int x, y;
    private boolean pared;
    private boolean inicio;
    private boolean fin;
    private Entidad entidad; // Puede ser una puerta, monstruo, etc.
    private boolean enCamino;

    public Celda(int x, int y) {
        this.x = x;
        this.y = y;
        this.pared = false;
        this.inicio = false;
        this.fin = false;
        this.entidad = null;
    }

    // Getters y Setters
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean esPared() { return pared; }
    public void setPared(boolean pared) { this.pared = pared; }
    public boolean esInicio() { return inicio; }
    public void setInicio(boolean inicio) { this.inicio = inicio; }
    public boolean esFin() { return fin; }
    public void setFin(boolean fin) { this.fin = fin; }
    public Entidad getEntidad() { return entidad; }
    public void setEntidad(Entidad entidad) { this.entidad = entidad; }
    public boolean estaEnCamino() { return enCamino; }
    public void setEnCamino(boolean enCamino) { this.enCamino = enCamino; }

    @Override
    public String toString() {
        if (enCamino) return "o";
        if (inicio) return "S";
        if (fin) return "E";
        if (pared) return "#";
        if (entidad != null) return entidad.getSimbolo();
        return ".";
    }
}
