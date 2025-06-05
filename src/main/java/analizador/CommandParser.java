package analizador;

import modelo.Laberinto;
import modelo.Entidad;
import modelo.Celda;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Clase que actúa como interfaz entre el parser y el modelo
 * Mantiene la funcionalidad de executeCommand para la GUI
 */
public class CommandParser {
    private Laberinto laberinto;

    public CommandParser() {
        this.laberinto = null;
    }

    public CommandParser(Laberinto laberinto) {
        this.laberinto = laberinto;
    }

    /**
     * Ejecuta un comando de texto parseando la cadena
     * @param commandText el comando como texto (ej: "ROOM 10 10")
     * @throws IllegalArgumentException si el comando es inválido
     */
    public void executeCommand(String commandText) {
        if (commandText == null || commandText.trim().isEmpty()) {
            throw new IllegalArgumentException("Comando vacío");
        }

        StringTokenizer tokenizer = new StringTokenizer(commandText.trim().toUpperCase());
        if (!tokenizer.hasMoreTokens()) {
            throw new IllegalArgumentException("Comando vacío");
        }

        String command = tokenizer.nextToken();
        
        try {
            switch (command) {
                case "ROOM":
                    if (tokenizer.countTokens() != 2) {
                        throw new IllegalArgumentException("ROOM requiere 2 parámetros: ancho alto");
                    }
                    int ancho = Integer.parseInt(tokenizer.nextToken());
                    int alto = Integer.parseInt(tokenizer.nextToken());
                    crearLaberinto(ancho, alto);
                    break;
                    
                case "WALL":
                    if (tokenizer.countTokens() != 2) {
                        throw new IllegalArgumentException("WALL requiere 2 parámetros: x y");
                    }
                    int wallX = Integer.parseInt(tokenizer.nextToken());
                    int wallY = Integer.parseInt(tokenizer.nextToken());
                    togglePared(wallX, wallY);
                    break;
                    
                case "START":
                    if (tokenizer.countTokens() != 2) {
                        throw new IllegalArgumentException("START requiere 2 parámetros: x y");
                    }
                    int startX = Integer.parseInt(tokenizer.nextToken());
                    int startY = Integer.parseInt(tokenizer.nextToken());
                    setCeldaInicio(startX, startY);
                    break;
                    
                case "END":
                    if (tokenizer.countTokens() != 2) {
                        throw new IllegalArgumentException("END requiere 2 parámetros: x y");
                    }
                    int endX = Integer.parseInt(tokenizer.nextToken());
                    int endY = Integer.parseInt(tokenizer.nextToken());
                    setCeldaFin(endX, endY);
                    break;
                    
                case "DOOR":
                    if (tokenizer.countTokens() != 2) {
                        throw new IllegalArgumentException("DOOR requiere 2 parámetros: x y");
                    }
                    int doorX = Integer.parseInt(tokenizer.nextToken());
                    int doorY = Integer.parseInt(tokenizer.nextToken());
                    agregarPuerta(doorX, doorY);
                    break;
                    
                case "MONSTER":
                    if (tokenizer.countTokens() != 2) {
                        throw new IllegalArgumentException("MONSTER requiere 2 parámetros: x y");
                    }
                    int monsterX = Integer.parseInt(tokenizer.nextToken());
                    int monsterY = Integer.parseInt(tokenizer.nextToken());
                    agregarMonstruo(monsterX, monsterY);
                    break;
                    
                case "GO":
                    if (tokenizer.hasMoreTokens()) {
                        throw new IllegalArgumentException("GO no requiere parámetros");
                    }
                    encontrarCamino();
                    break;
                    
                case "CLEAR_PATH":
                case "CLEAR":
                    if (tokenizer.hasMoreTokens()) {
                        throw new IllegalArgumentException("CLEAR_PATH no requiere parámetros");
                    }
                    limpiarCaminos();
                    break;
                    
                default:
                    throw new IllegalArgumentException("Comando no reconocido: " + command);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Parámetros numéricos inválidos en: " + commandText);
        }
    }

    // Métodos privados para ejecutar comandos (mantenidos para executeCommand)
    private void crearLaberinto(int ancho, int alto) {
        if (ancho <= 0 || alto <= 0) {
            throw new IllegalArgumentException("Las dimensiones deben ser positivas");
        }
        if (ancho > Tokens.MAX_ROOM_SIZE || alto > Tokens.MAX_ROOM_SIZE) {
            throw new IllegalArgumentException("Dimensiones demasiado grandes (máximo " + Tokens.MAX_ROOM_SIZE + ")");
        }
        laberinto = new Laberinto(ancho, alto);
    }

    private void togglePared(int x, int y) {
        validarCoordenadas(x, y);
        laberinto.togglePared(x, y);
    }

    private void setCeldaInicio(int x, int y) {
        validarCoordenadas(x, y);
        laberinto.setCeldaInicio(x, y);
    }

    private void setCeldaFin(int x, int y) {
        validarCoordenadas(x, y);
        laberinto.setCeldaFin(x, y);
    }

    private void agregarPuerta(int x, int y) {
        validarCoordenadas(x, y);
        laberinto.getCelda(x, y).setEntidad(new Entidad.Puerta(x, y));
    }

    private void agregarMonstruo(int x, int y) {
        validarCoordenadas(x, y);
        laberinto.getCelda(x, y).setEntidad(new Entidad.Monstruo(x, y));
    }

    private void encontrarCamino() {
        if (laberinto == null) {
            throw new IllegalArgumentException("Laberinto no inicializado. Use ROOM primero.");
        }
        laberinto.limpiarCaminos();
        List<Celda> camino = laberinto.encontrarCaminoDijkstra();
        if (camino.isEmpty()) {
            throw new IllegalArgumentException("No hay camino posible o falta START/END");
        }
        for (Celda celda : camino) {
            celda.setEnCamino(true);
        }
    }

    private void limpiarCaminos() {
        if (laberinto == null) {
            throw new IllegalArgumentException("Laberinto no inicializado. Use ROOM primero.");
        }
        laberinto.limpiarCaminos();
    }

    private void validarCoordenadas(int x, int y) {
        if (laberinto == null) {
            throw new IllegalArgumentException("Laberinto no inicializado. Use ROOM primero.");
        }
        if (x < 0 || x >= laberinto.getAncho() || y < 0 || y >= laberinto.getAlto()) {
            throw new IllegalArgumentException("Coordenadas fuera de rango (0-" + (laberinto.getAncho()-1) + ", 0-" + (laberinto.getAlto()-1) + ")");
        }
    }

    /**
     * Obtiene el laberinto actual
     * @return el laberinto o null si no se ha inicializado
     */
    public Laberinto getLaberinto() {
        return laberinto;
    }

    /**
     * Establece el laberinto
     * @param laberinto el laberinto a establecer
     */
    public void setLaberinto(Laberinto laberinto) {
        this.laberinto = laberinto;
    }

    /**
     * Verifica si el laberinto está inicializado
     * @return true si el laberinto existe
     */
    public boolean isLaberintoInicializado() {
        return laberinto != null;
    }

    /**
     * Reinicia el command parser (limpia el laberinto)
     */
    public void reset() {
        this.laberinto = null;
    }
}