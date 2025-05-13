package analizador;

import modelo.Laberinto;
import modelo.Entidad;

public class CommandParser {
    private Laberinto laberinto;

    public CommandParser(Laberinto laberinto) {
        this.laberinto = laberinto;
    }

    public void executeCommand(String command) {
        String[] parts = command.split(" ");
        try {
            switch (parts[0].toUpperCase()) {
                case "ROOM":
                    laberinto = new Laberinto(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                    break;
                case "WALL":
                    laberinto.togglePared(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                    break;
                case "START":
                    laberinto.setCeldaInicio(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                    break;
                case "END":
                    laberinto.setCeldaFin(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                    break;
                case "DOOR":
                    laberinto.getCelda(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]))
                            .setEntidad(new Entidad.Puerta(Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
                    break;
                case "MONSTER":
                    laberinto.getCelda(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]))
                            .setEntidad(new Entidad.Monstruo(Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
                    break;
                case "SAVE":
                    laberinto.guardar(parts[1]);
                    break;
                case "LOAD":
                    laberinto = Laberinto.cargar(parts[1]);
                    break;
                default:
                    System.err.println("Comando desconocido: " + parts[0]);
            }
        } catch (Exception e) {
            System.err.println("Error al ejecutar comando: " + e.getMessage());
        }
    }
}