package analizador;

/**
 * Interface que define todos los tokens y constantes utilizados en el analizador léxico
 * Incluye tokens para comandos, números y mensajes de error
 */
public interface Tokens {
    // Palabras reservadas (comandos)
    String ROOM = "ROOM";
    String WALL = "WALL";
    String START = "START";
    String END = "END";
    String DOOR = "DOOR";
    String MONSTER = "MONSTER";
    String GO = "GO";
    String CLEAR_PATH = "CLEAR_PATH";
    String CLEAR = "CLEAR"; // Alias para CLEAR_PATH

    // Token para números enteros (coordenadas)
    String NUMBER = "NUMBER";

    // Tokens especiales
    String EOF = "EOF";
    String ERROR = "ERROR";
    
    // Mensajes de error predefinidos
    String ERROR_UNRECOGNIZED = "Token no reconocido";
    String ERROR_NUMBER_EXPECTED = "Número esperado";
    String ERROR_INVALID_COMMAND = "Comando no válido";
    String ERROR_UNEXPECTED_EOF = "Fin de archivo inesperado";
    String ERROR_SYNTAX = "Error de sintaxis";
    String ERROR_NO_PARAMS_NEEDED = " no requiere parámetros";
    String ERROR_REQUIRES_2_PARAMS = " requiere 2 parámetros: x y";
    String ERROR_ROOM_REQUIRES_2_PARAMS = "ROOM requiere exactamente 2 parámetros: ancho alto";
    String ERROR_INVALID_NUMBER = "Los parámetros deben ser números enteros";
    String ERROR_CLEAR_COMPLETE = "CLEAR: Laberinto reiniciado a estado inicial";
    
    // Límites de coordenadas (pueden ser modificados según necesidades)
    int MIN_COORDINATE = 0;
    int MAX_COORDINATE = 100;
    int MIN_ROOM_SIZE = 1;
    int MAX_ROOM_SIZE = 50;
    
    /**
     * Método para validar si una cadena es un comando válido
     * @param command cadena a validar
     * @return true si es un comando válido
     */
    static boolean isValidCommand(String command) {
        if (command == null) return false;
        
        switch(command.toUpperCase()) {
            case ROOM:
            case WALL:
            case START:
            case END:
            case DOOR:
            case MONSTER:
            case GO:
            case CLEAR_PATH:
            case CLEAR:
                return true;
            default:
                return false;
        }
    }
    
    /**
     * Método para validar coordenadas
     * @param coordinate coordenada a validar
     * @return true si está en el rango válido
     */
    static boolean isValidCoordinate(int coordinate) {
        return coordinate >= MIN_COORDINATE && coordinate <= MAX_COORDINATE;
    }
    
    /**
     * Método para validar tamaño de habitación
     * @param size tamaño a validar
     * @return true si está en el rango válido
     */
    static boolean isValidRoomSize(int size) {
        return size >= MIN_ROOM_SIZE && size <= MAX_ROOM_SIZE;
    }
}