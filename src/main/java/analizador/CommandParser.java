package analizador;

import modelo.Laberinto;
import modelo.Entidad;
import modelo.Celda;
import java.util.List;

public class CommandParser {
    private Laberinto laberinto;

    public CommandParser(Laberinto laberinto) {
        this.laberinto = laberinto;
    }

    public void executeCommand(String command) throws IllegalArgumentException {
        String[] parts = command.trim().split("\\s+");
        
        if (parts.length == 0) {
            throw new IllegalArgumentException("Comando vacío");
        }

        try {
            switch (parts[0].toUpperCase()) {
                case "ROOM":
                    validarArgumentos(parts, 3);
                    int ancho = Integer.parseInt(parts[1]);
                    int alto = Integer.parseInt(parts[2]);
                    laberinto = new Laberinto(ancho, alto);
                    break;
                    
                case "WALL":
                    validarArgumentos(parts, 3);
                    int xWall = Integer.parseInt(parts[1]);
                    int yWall = Integer.parseInt(parts[2]);
                    validarCoordenadas(xWall, yWall);
                    laberinto.togglePared(xWall, yWall);
                    break;
                    
                case "START":
                    validarArgumentos(parts, 3);
                    int xStart = Integer.parseInt(parts[1]);
                    int yStart = Integer.parseInt(parts[2]);
                    validarCoordenadas(xStart, yStart);
                    laberinto.setCeldaInicio(xStart, yStart);
                    break;
                    
                case "END":
                    validarArgumentos(parts, 3);
                    int xEnd = Integer.parseInt(parts[1]);
                    int yEnd = Integer.parseInt(parts[2]);
                    validarCoordenadas(xEnd, yEnd);
                    laberinto.setCeldaFin(xEnd, yEnd);
                    break;
                    
                case "DOOR":
                    validarArgumentos(parts, 3);
                    int xDoor = Integer.parseInt(parts[1]);
                    int yDoor = Integer.parseInt(parts[2]);
                    validarCoordenadas(xDoor, yDoor);
                    laberinto.getCelda(xDoor, yDoor).setEntidad(new Entidad.Puerta(xDoor, yDoor));
                    break;
                    
                case "MONSTER":
                    validarArgumentos(parts, 3);
                    int xMonster = Integer.parseInt(parts[1]);
                    int yMonster = Integer.parseInt(parts[2]);
                    validarCoordenadas(xMonster, yMonster);
                    laberinto.getCelda(xMonster, yMonster).setEntidad(new Entidad.Monstruo(xMonster, yMonster));
                    break;
                    
                case "GO":
                    laberinto.limpiarCaminos();
                    List<Celda> camino = laberinto.encontrarCaminoDijkstra();
                    if (camino.isEmpty()) {
                        throw new IllegalArgumentException("No hay camino o falta START/END");
                    }
                    for (Celda celda : camino) {
                        celda.setEnCamino(true);
                    }
                    break;
                    
                case "CLEAR_PATH":
                    laberinto.limpiarCaminos();
                    break;
                    
                default:
                    throw new IllegalArgumentException("Comando desconocido: " + parts[0]);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Coordenadas deben ser números enteros");
        }
    }

    private void validarArgumentos(String[] parts, int numEsperado) {
        if (parts.length < numEsperado) {
            throw new IllegalArgumentException("Faltan argumentos. Uso: " + parts[0] + " X Y");
        }
    }

    private void validarCoordenadas(int x, int y) {
        if (x < 0 || x >= laberinto.getAncho() || y < 0 || y >= laberinto.getAlto()) {
            throw new IllegalArgumentException("Coordenadas fuera de rango (0-" + (laberinto.getAncho()-1) + ")");
        }
    }
}