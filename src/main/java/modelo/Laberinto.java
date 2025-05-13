package modelo;

import java.io.*;
import java.util.Scanner;

public class Laberinto {
    private Celda[][] celdas;
    private int ancho, alto;

    public Laberinto(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        celdas = new Celda[ancho][alto];
        // Inicializar todas las celdas como vacías
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                celdas[x][y] = new Celda(x, y);
            }
        }
    }

    public void togglePared(int x, int y) {
        celdas[x][y].setPared(!celdas[x][y].esPared());
    }

    public void setCeldaInicio(int x, int y) {
        // Limpiar inicio anterior
        for (Celda[] fila : celdas) {
            for (Celda celda : fila) {
                celda.setInicio(false);
            }
        }
        celdas[x][y].setInicio(true);
    }

    public void setCeldaFin(int x, int y) {
        // Limpiar fin anterior
        for (Celda[] fila : celdas) {
            for (Celda celda : fila) {
                celda.setFin(false);
            }
        }
        celdas[x][y].setFin(true);
    }

    public Celda getCelda(int x, int y) {
        return celdas[x][y];
    }

    public int getAncho() { return ancho; }
    public int getAlto() { return alto; }

    // Método para guardar el laberinto en un archivo .pave
    public void guardar(String ruta) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ruta))) {
            // Escribir dimensiones
            writer.println("DIM " + ancho + " " + alto);
            
            // Escribir celdas especiales
            for (int y = 0; y < alto; y++) {
                for (int x = 0; x < ancho; x++) {
                    Celda celda = celdas[x][y];
                    if (celda.esInicio()) {
                        writer.println("START " + x + " " + y);
                    } else if (celda.esFin()) {
                        writer.println("END " + x + " " + y);
                    } else if (celda.esPared()) {
                        writer.println("WALL " + x + " " + y);
                    }
                    
                    // Guardar entidades si existen
                    if (celda.getEntidad() != null) {
                        Entidad entidad = celda.getEntidad();
                        writer.println(entidad.getTipo() + " " + x + " " + y);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al guardar el laberinto: " + e.getMessage());
        }
    }

    // Método para cargar un laberinto desde un archivo .pave
    public static Laberinto cargar(String ruta) {
        try (Scanner scanner = new Scanner(new File(ruta))) {
            // Leer dimensiones
            String[] dim = scanner.nextLine().split(" ");
            if (!dim[0].equals("DIM")) {
                throw new IOException("Formato de archivo inválido");
            }
            int ancho = Integer.parseInt(dim[1]);
            int alto = Integer.parseInt(dim[2]);
            
            Laberinto laberinto = new Laberinto(ancho, alto);
            
            // Leer resto de líneas
            while (scanner.hasNextLine()) {
                String[] partes = scanner.nextLine().split(" ");
                int x = Integer.parseInt(partes[1]);
                int y = Integer.parseInt(partes[2]);
                
                switch (partes[0]) {
                    case "START":
                        laberinto.setCeldaInicio(x, y);
                        break;
                    case "END":
                        laberinto.setCeldaFin(x, y);
                        break;
                    case "WALL":
                        laberinto.getCelda(x, y).setPared(true);
                        break;
                    case "PUERTA":
                        laberinto.getCelda(x, y).setEntidad(new Entidad.Puerta(x, y));
                        break;
                    case "MONSTRUO":
                        laberinto.getCelda(x, y).setEntidad(new Entidad.Monstruo(x, y));
                        break;
                }
            }
            return laberinto;
        } catch (IOException e) {
            System.err.println("Error al cargar el laberinto: " + e.getMessage());
            return null;
        }
    }
}