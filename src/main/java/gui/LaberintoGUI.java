package gui;

import analizador.LaberintoChangeListener;
import analizador.CommandParser;
import modelo.Laberinto;
import modelo.Celda;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

/**
 * Controlador que maneja la lógica del Laberinto IDE
 * Se encarga de la lógica de negocio y comunicación entre modelo y vista
 */
public class LaberintoGUI implements LaberintoChangeListener {
    
    private Laberinto laberinto;
    private CommandParser commandParser;
    private LaberintoJFrame vista;
    private int celdaSize = 40;
    private MouseListener mouseListener;
    
    @Override
    public void onLaberintoChanged(Laberinto nuevoLaberinto) {
        this.laberinto = nuevoLaberinto;
        actualizarVistaLaberinto();
    }

    private void actualizarVistaLaberinto() {
        if (laberinto != null) {
            // Calcular tamaño óptimo de celda basado en el tamaño del contenedor
            Dimension panelSize = vista.getPanelLaberintoSize();
            int maxCeldaSize = Math.min(
                panelSize.width / laberinto.getAncho(),
                panelSize.height / laberinto.getAlto()
            );
            this.celdaSize = Math.max(20, Math.min(maxCeldaSize, 50)); // Entre 20 y 50 píxeles
            
            vista.actualizarTamanoPanelLaberinto(laberinto.getAncho(), laberinto.getAlto(), celdaSize);
            vista.repintarLaberinto();
        }
    }

    public LaberintoGUI(LaberintoJFrame vista) {
        this.vista = vista;
        this.commandParser = new CommandParser();
        //this.laberinto = new Laberinto(10, 10); // Laberinto por defecto
        setupMouseListener();
    }
    
    public Laberinto getLaberinto() {
        return laberinto;
    }
    
    public void crearLaberintoPorDefecto(int ancho, int alto) {
        this.laberinto = new Laberinto(ancho, alto);
        actualizarVistaLaberinto();
    }
    
    private void setupMouseListener() {
        mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                manejarClickMouse(e);
            }
        };
    }
    
    public MouseListener getMouseListener() {
        return mouseListener;
    }
    
    public void dibujarLaberinto(Graphics g) {
        if (laberinto == null) {
            // Dibujar estado vacío
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, vista.getPanelLaberinto().getWidth(), vista.getPanelLaberinto().getHeight());
            g.setColor(Color.BLACK);
            g.drawString("Crea un laberinto con 'ROOM ancho alto'", 20, 20);
            return;
        }
        
        for (int y = 0; y < laberinto.getAlto(); y++) {
            for (int x = 0; x < laberinto.getAncho(); x++) {
                Celda celda = laberinto.getCelda(x, y);
                
                // Establecer color de fondo
                if (celda.estaEnCamino()) {
                    g.setColor(new Color(255, 165, 0)); // Naranja para camino
                } else {
                    g.setColor(obtenerColorCelda(celda));
                }
                
                g.fillRect(x * celdaSize, y * celdaSize, celdaSize, celdaSize);
                
                // Dibujar entidades
                if (celda.getEntidad() != null) {
                    g.setColor(Color.BLUE);
                    g.drawString(celda.getEntidad().getSimbolo(), 
                               x * celdaSize + celdaSize/2 - 4, 
                               y * celdaSize + celdaSize/2 + 4);
                }
                
                // Dibujar borde
                g.setColor(Color.GRAY);
                g.drawRect(x * celdaSize, y * celdaSize, celdaSize, celdaSize);
            }
        }
    }
    
    private Color obtenerColorCelda(Celda celda) {
        if (celda.esInicio()) return Color.GREEN;
        if (celda.esFin()) return Color.RED;
        if (celda.esPared()) return Color.BLACK;
        return Color.WHITE;
    }
    
    private void manejarClickMouse(MouseEvent e) {
        if (laberinto == null) return;
        
        int x = e.getX() / celdaSize;
        int y = e.getY() / celdaSize;
        
        // Verificar que las coordenadas estén dentro del laberinto
        if (x >= 0 && x < laberinto.getAncho() && y >= 0 && y < laberinto.getAlto()) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                laberinto.togglePared(x, y);
            } else if (SwingUtilities.isRightMouseButton(e)) {
                laberinto.setCeldaInicio(x, y);
            }
            vista.repintarLaberinto();
        }
    }
    
    public void procesarComando(String command) {
    try {
        // Mostrar el comando en la consola
        vista.mostrarEnConsola("> " + command);
        
        commandParser.executeCommand(command);
        if (command.toUpperCase().startsWith("ROOM")) {
            this.laberinto = commandParser.getLaberinto();
            actualizarVistaLaberinto();
        }
        vista.repintarLaberinto();
    } catch (Exception ex) {
        vista.mostrarError(ex.getMessage());
    }
}
    
    public void guardarLaberinto() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Guardar laberinto");
    fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
    
    // Cambiar vista.getFrame() por vista directamente
    if (fileChooser.showSaveDialog(vista) == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        
        // Agregar extensión si no la tiene
        if (!file.getName().toLowerCase().endsWith(".lab")) {
            file = new File(file.getAbsolutePath() + ".lab");
        }
        
        try {
            laberinto.guardar(file.getAbsolutePath());
            vista.mostrarExito("Laberinto guardado correctamente en: " + file.getName());
        } catch (Exception ex) {
            vista.mostrarError("Error al guardar: " + ex.getMessage());
        }
    }
}
    
    public void cargarLaberinto() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(vista) == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try {
                Laberinto nuevoLaberinto = Laberinto.cargar(archivo.getAbsolutePath());
                this.laberinto = nuevoLaberinto;
                this.commandParser.setLaberinto(nuevoLaberinto);
                actualizarVistaLaberinto();
            } catch (Exception ex) {
                vista.mostrarError("Error al cargar: " + ex.getMessage());
            }
        }
    }
    
    public void actualizarLaberinto(Laberinto nuevoLaberinto) {
        this.laberinto = nuevoLaberinto;
        commandParser.setLaberinto(nuevoLaberinto);
        actualizarVistaLaberinto();
    }
    
    public int getCeldaSize() {
        return celdaSize;
    }
    
    public void setCeldaSize(int nuevoTamano) {
        if (nuevoTamano > 0 && nuevoTamano <= 100) {
            this.celdaSize = nuevoTamano;
            vista.actualizarTamanoPanelLaberinto(laberinto.getAncho(), laberinto.getAlto(), celdaSize);
            vista.repintarLaberinto();
        }
    }
}