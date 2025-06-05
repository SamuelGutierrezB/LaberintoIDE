package modelo;

public class NodoDijkstra implements Comparable<NodoDijkstra> {
    public Celda celda;
    public int distancia;
    public NodoDijkstra anterior;

    public NodoDijkstra(Celda celda, int distancia) {
        this.celda = celda;
        this.distancia = distancia;
    }

    @Override
    public int compareTo(NodoDijkstra otro) {
        return Integer.compare(this.distancia, otro.distancia);
    }
}