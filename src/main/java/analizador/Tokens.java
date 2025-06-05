package analizador;

/**
 * Interface que define todos los tokens y constantes utilizados en el analizador léxico
 * Incluye tokens para comandos, números y mensajes de error
 */
public interface Tokens {
    // Palabras reservadas (comandos)
    int ROOM = 1;
    int WALL = 2;
    int START = 3;
    int END = 4;
    int DOOR = 5;
    int MONSTER = 6;
    int GO = 7;
    int CLEAR_PATH = 8;

    // Token para números enteros (coordenadas)
    int NUMBER = 9;

    // Tokens especiales
    int EOF = -1;
    int ERROR = -2;
    
    // Mensajes de error predefinidos
    String[] ERROR_MESSAGES = {
        "Token no reconocido",
        "Número esperado",
        "Comando no válido",
        "Fin de archivo inesperado",
        "Error de sintaxis"
    };
    
    // Comandos como cadenas para validación
    String[] COMMAND_STRINGS = {
        "ROOM",
        "WALL", 
        "START",
        "END",
        "DOOR",
        "MONSTER",
        "GO",
        "CLEAR_PATH"
    };
    
    // Límites de coordenadas (pueden ser modificados según necesidades)
    int MIN_COORDINATE = 0;
    int MAX_COORDINATE = 100;
    int MIN_ROOM_SIZE = 1;
    int MAX_ROOM_SIZE = 50;
    
    /**
     * Método para obtener el nombre del token dado su código
     * @param tokenCode código del token
     * @return nombre del token como String
     */
    static String getTokenName(int tokenCode) {
        switch(tokenCode) {
            case ROOM: return "ROOM";
            case WALL: return "WALL";
            case START: return "START";
            case END: return "END";
            case DOOR: return "DOOR";
            case MONSTER: return "MONSTER";
            case GO: return "GO";
            case CLEAR_PATH: return "CLEAR_PATH";
            case NUMBER: return "NUMBER";
            case EOF: return "EOF";
            case ERROR: return "ERROR";
            default: return "UNKNOWN_TOKEN";
        }
    }
    
    /**
     * Método para validar si una cadena es un comando válido
     * @param command cadena a validar
     * @return código del token si es válido, ERROR si no lo es
     */
    static int getTokenCode(String command) {
        if (command == null) return ERROR;
        
        switch(command.toUpperCase()) {
            case "ROOM": return ROOM;
            case "WALL": return WALL;
            case "START": return START;
            case "END": return END;
            case "DOOR": return DOOR;
            case "MONSTER": return MONSTER;
            case "GO": return GO;
            case "CLEAR_PATH": return CLEAR_PATH;
            default: return ERROR;
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