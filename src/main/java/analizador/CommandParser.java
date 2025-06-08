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
            throw new IllegalArgumentException("Comando vacío");
        }

        StringTokenizer tokenizer = new StringTokenizer(commandText.trim().toUpperCase());
        String command = tokenizer.nextToken();

        try {
            switch (command) {
                case "ROOM":
                    if (tokenizer.countTokens() != 2) {
                        throw new IllegalArgumentException("ROOM requiere exactamente 2 parámetros: ancho alto");
                    }
                    
                    try {
                        int ancho = Integer.parseInt(tokenizer.nextToken());
                        int alto = Integer.parseInt(tokenizer.nextToken());
                        System.out.println("Creando laberinto " + ancho + "x" + alto); // Debug
                        crearLaberinto(ancho, alto);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Los parámetros de ROOM deben ser números enteros");
                    }
                    break;
                    
                    
                 case "WALL":
                    validarXY(tokenizer, "WALL");
                    togglePared(Integer.parseInt(tokenizer.nextToken()), 
                              Integer.parseInt(tokenizer.nextToken()));
                    break;
                    
                case "START":
                    validarXY(tokenizer, "START");
                    setCeldaInicio(Integer.parseInt(tokenizer.nextToken()), 
                                 Integer.parseInt(tokenizer.nextToken()));
                    break;
                    
                case "END":
                    validarXY(tokenizer, "END");
                    setCeldaFin(Integer.parseInt(tokenizer.nextToken()), 
                              Integer.parseInt(tokenizer.nextToken()));
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
            throw new IllegalArgumentException("Parámetros deben ser números");
        }
    }


    private void validarXY(StringTokenizer tokenizer, String comando) {
        if (tokenizer.countTokens() != 2) {
            throw new IllegalArgumentException(comando + " requiere 2 coordenadas (ej: " + comando + " 5 5)");
        }
    }

private void crearLaberinto(int ancho, int alto) {
    // Limpiar laberinto existente
    if (this.laberinto != null) {
        this.laberinto = null;
    }
    
    // Crear nuevo laberinto
    this.laberinto = new Laberinto(ancho, alto);
    
    if (listener != null) {
        listener.onLaberintoChanged(this.laberinto);
    }
}
private void validarLaberintoExiste() {
    if (laberinto == null) {
        throw new IllegalStateException("Primero debes crear un laberinto con ROOM");
    }
}

    private void togglePared(int x, int y) {
        validarLaberintoExiste();
        validarCoordenadas(x, y);
        laberinto.togglePared(x, y);
    }

private void validarCoordenadas(int x, int y) {
    if (laberinto == null) {
        throw new IllegalStateException("Primero debes crear un laberinto (comando: ROOM ancho alto)");
    }
        if (x < 0 || x >= laberinto.getAncho() || y < 0 || y >= laberinto.getAlto()) {
            throw new IllegalArgumentException("Coordenadas fuera de rango (0-" + 
                (laberinto.getAncho()-1) + ", 0-" + (laberinto.getAlto()-1) + ")");
        }
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