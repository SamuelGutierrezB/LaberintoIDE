package gui;

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
public class LaberintoGUI {
    
    private Laberinto laberinto;
    private CommandParser commandParser;
    private LaberintoJFrame vista;
    private int celdaSize = 40;
    private MouseListener mouseListener;
    
    public LaberintoGUI(LaberintoJFrame vista) {
        this.vista = vista;
        
        // Inicializar modelo y parser
        laberinto = new Laberinto(10, 10);
        commandParser = new CommandParser(laberinto);
        
        // Configurar el tamaño inicial del panel
        vista.actualizarTamanoPanelLaberinto(laberinto.getAncho(), laberinto.getAlto(), celdaSize);
        
        // Crear el mouse listener
        setupMouseListener();
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
        for (int y = 0; y < laberinto.getAlto(); y++) {
            for (int x = 0; x < laberinto.getAncho(); x++) {
                Celda celda = laberinto.getCelda(x, y);
                
                // Establecer color de fondo
                if (celda.estaEnCamino()) {
                    g.setColor(new Color(255, 165, 0)); // Naranja para el camino
                } else {
                    g.setColor(obtenerColorCelda(celda));
                }
                
                g.fillRect(x * celdaSize, y * celdaSize, celdaSize, celdaSize);
                
                // Dibujar entidades
                if (celda.getEntidad() != null) {
                    g.setColor(Color.BLUE);
                    g.drawString(celda.getEntidad().getSimbolo(), 
                                x * celdaSize + celdaSize/2, 
                                y * celdaSize + celdaSize/2);
                }
                
                // Dibujar borde de la celda
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
        command = command.trim();
        if (!command.isEmpty()) {
            vista.mostrarEnConsola("> " + command);
            
            try {
                commandParser.executeCommand(command);
                vista.repintarLaberinto();
                vista.limpiarErrores();
            } catch (Exception ex) {
                vista.mostrarError(ex.getMessage());
            }
            
            vista.limpiarInputComando();
        }
    }
    
    public void guardarLaberinto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar laberinto");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        
        if (fileChooser.showSaveDialog(vista.getFrame()) == JFileChooser.APPROVE_OPTION) {
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
        fileChooser.setDialogTitle("Cargar laberinto");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        
        // Filtro para archivos de laberinto
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".lab");
            }
            
            @Override
            public String getDescription() {
                return "Archivos de Laberinto (*.lab)";
            }
        });
        
        if (fileChooser.showOpenDialog(vista.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            try {
                Laberinto nuevoLaberinto = Laberinto.cargar(file.getAbsolutePath());
                this.laberinto = nuevoLaberinto;
                
                // Actualizar el parser con el nuevo laberinto
                this.commandParser = new CommandParser(laberinto);
                
                // Actualizar el tamaño del panel
                vista.actualizarTamanoPanelLaberinto(laberinto.getAncho(), laberinto.getAlto(), celdaSize);
                
                // Repintar todo
                vista.repintarVentana();
                vista.mostrarExito("Laberinto cargado correctamente desde: " + file.getName());
                
            } catch (Exception ex) {
                vista.mostrarError("Error al cargar: " + ex.getMessage());
            }
        }
    }
    
    // Getters para acceso controlado desde la vista
    public Laberinto getLaberinto() {
        return laberinto;
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