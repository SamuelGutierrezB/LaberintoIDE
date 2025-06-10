package analizador;

import modelo.Laberinto;
import modelo.Entidad;
import modelo.Celda;
import java.util.List;
import java.util.StringTokenizer;

public class CommandParser {
    private Laberinto laberinto;
    private LaberintoChangeListener listener;

    public CommandParser() {
        this.laberinto = null;
    }

    public CommandParser(Laberinto laberinto) {
        this.laberinto = laberinto;
    }

    public void setLaberintoChangeListener(LaberintoChangeListener listener) {
        this.listener = listener;
    }

    /**
     * Ejecuta un comando de texto
     * @param commandText el comando (ej: "ROOM 10 10")
     * @throws IllegalArgumentException si el comando es inválido
     */
    public void executeCommand(String commandText) {
        if (commandText == null || commandText.trim().isEmpty()) {
            throw new IllegalArgumentException(Tokens.ERROR_SYNTAX);
        }

        StringTokenizer tokenizer = new StringTokenizer(commandText.trim().toUpperCase());
        String command = tokenizer.nextToken();

        if (!Tokens.isValidCommand(command)) {
            throw new IllegalArgumentException(Tokens.ERROR_INVALID_COMMAND + ": " + command);
        }

        try {
            switch (command) {
                case Tokens.ROOM:
                    validarParametros(tokenizer, 2, Tokens.ERROR_ROOM_REQUIRES_2_PARAMS);
                    int ancho = Integer.parseInt(tokenizer.nextToken());
                    int alto = Integer.parseInt(tokenizer.nextToken());
                    if (!Tokens.isValidRoomSize(ancho) || !Tokens.isValidRoomSize(alto)) {
                        throw new IllegalArgumentException("Tamaño de habitación inválido. Rango: " + 
                            Tokens.MIN_ROOM_SIZE + "-" + Tokens.MAX_ROOM_SIZE);
                    }
                    crearLaberinto(ancho, alto);
                    break;
                    
                case Tokens.WALL:
                case Tokens.START:
                case Tokens.END:
                case Tokens.DOOR:
                case Tokens.MONSTER:
                    validarParametros(tokenizer, 2, command + Tokens.ERROR_REQUIRES_2_PARAMS);
                    int x = Integer.parseInt(tokenizer.nextToken());
                    int y = Integer.parseInt(tokenizer.nextToken());
                    validarCoordenadas(x, y);
                    
                    switch (command) {
                        case Tokens.WALL:
                            togglePared(x, y);
                            break;
                        case Tokens.START:
                            setCeldaInicio(x, y);
                            break;
                        case Tokens.END:
                            setCeldaFin(x, y);
                            break;
                        case Tokens.DOOR:
                            agregarPuerta(x, y);
                            break;
                        case Tokens.MONSTER:
                            agregarMonstruo(x, y);
                            break;
                    }
                    break;
                    
                case Tokens.GO:
                case Tokens.CLEAR_PATH:
                case Tokens.CLEAR:
                    validarParametros(tokenizer, 0, command + Tokens.ERROR_NO_PARAMS_NEEDED);
                    if (command.equals(Tokens.GO)) {
                        encontrarCamino();
                    } else {
                        limpiarCaminos();
                    }
                    break;
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(Tokens.ERROR_INVALID_NUMBER);
        }
    }

    private void validarParametros(StringTokenizer tokenizer, int expected, String errorMessage) {
        if (tokenizer.countTokens() != expected) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void crearLaberinto(int ancho, int alto) {
        if (this.laberinto != null) {
            this.laberinto = null;
        }
        
        this.laberinto = new Laberinto(ancho, alto);
        
        if (listener != null) {
            listener.onLaberintoChanged(this.laberinto);
        }
    }

    private void validarLaberintoExiste() {
        if (laberinto == null) {
            throw new IllegalStateException("Primero debes crear un laberinto con " + Tokens.ROOM);
        }
    }

    private void togglePared(int x, int y) {
        validarLaberintoExiste();
        laberinto.togglePared(x, y);
    }

    private void validarCoordenadas(int x, int y) {
        validarLaberintoExiste();
        if (x < 0 || x >= laberinto.getAncho() || y < 0 || y >= laberinto.getAlto()) {
            throw new IllegalArgumentException("Coordenadas fuera de rango (0-" + 
                (laberinto.getAncho()-1) + ", 0-" + (laberinto.getAlto()-1) + ")");
        }
    }

    private void setCeldaInicio(int x, int y) {
        laberinto.setCeldaInicio(x, y);
    }

    private void setCeldaFin(int x, int y) {
        laberinto.setCeldaFin(x, y);
    }

    private void agregarPuerta(int x, int y) {
        laberinto.getCelda(x, y).setEntidad(new Entidad.Puerta(x, y));
    }

    private void agregarMonstruo(int x, int y) {
        laberinto.getCelda(x, y).setEntidad(new Entidad.Monstruo(x, y));
    }

    private void encontrarCamino() {
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
        laberinto.limpiarCaminos();
        if (laberinto == null) {
            throw new IllegalArgumentException("Laberinto no inicializado. Use ROOM primero.");
        }
        
        // Limpiar todas las celdas
        for (int x = 0; x < laberinto.getAncho(); x++) {
            for (int y = 0; y < laberinto.getAlto(); y++) {
                Celda celda = laberinto.getCelda(x, y);
                celda.setPared(false);
                celda.setEntidad(null);
                celda.setEnCamino(false);
                celda.setInicio(false);
                celda.setFin(false);
            }
        }
        
        // Notificar al listener si existe
        if (listener != null) {
            listener.onLaberintoChanged(this.laberinto);
        }
    }

    public Laberinto getLaberinto() {
        return laberinto;
    }

    public void setLaberinto(Laberinto laberinto) {
        this.laberinto = laberinto;
    }

    public boolean isLaberintoInicializado() {
        return laberinto != null;
    }

    public void reset() {
        this.laberinto = null;
    }
}