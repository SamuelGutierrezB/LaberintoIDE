package modelo;

public abstract class Entidad {
    protected int x, y;
    protected String tipo;

    public Entidad(int x, int y, String tipo) {
        this.x = x;
        this.y = y;
        this.tipo = tipo;
    }

    // Métodos comunes
    public int getX() { return x; }
    public int getY() { return y; }
    public String getTipo() { return tipo; }
    public abstract String getSimbolo();

    // Subclases concretas
    public static class Puerta extends Entidad {
        public Puerta(int x, int y) {
            super(x, y, "PUERTA");
        }
        @Override
        public String getSimbolo() { return "D"; }
    }

    public static class Monstruo extends Entidad {
        public Monstruo(int x, int y) {
            super(x, y, "MONSTRUO");
        }
        @Override
        public String getSimbolo() { return "M"; }
    }
    
    
}