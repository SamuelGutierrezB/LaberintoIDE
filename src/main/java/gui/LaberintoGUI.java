package gui;

import analizador.CommandParser;
import modelo.Laberinto;
import modelo.Celda;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class LaberintoGUI extends JFrame {
    private Laberinto laberinto;
    private JPanel panelLaberinto;
    private JButton btnGuardar, btnCargar;
    private int celdaSize = 40;
    private JTextArea consoleArea;
    private JTextArea errorArea;
    private CommandParser commandParser;
    private JTextField commandInput;

    public LaberintoGUI() {
        setTitle("Laberinto IDE");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inicializar laberinto y parser
        laberinto = new Laberinto(10, 10);
        commandParser = new CommandParser(laberinto);

        // Panel del laberinto
        panelLaberinto = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarLaberinto(g);
            }
        };
        panelLaberinto.setPreferredSize(new Dimension(laberinto.getAncho() * celdaSize, laberinto.getAlto() * celdaSize));
        panelLaberinto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                manejarClickMouse(e);
            }
        });

        // Configuración de la interfaz
        configurarControles();
        initComponents();

        // Asegurar focus en el input
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                commandInput.requestFocusInWindow();
            }
        });
    }

    private void configurarControles() {
        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarLaberinto());
        
        btnCargar = new JButton("Cargar");
        btnCargar.addActionListener(e -> cargarLaberinto());

        JPanel panelControles = new JPanel();
        panelControles.add(btnGuardar);
        panelControles.add(btnCargar);

        // Panel de errores
        errorArea = new JTextArea(3, 40);
        errorArea.setEditable(false);
        errorArea.setBackground(new Color(255, 230, 230));
        errorArea.setForeground(Color.RED);
        errorArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        errorArea.setBorder(BorderFactory.createTitledBorder("Errores"));

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(panelControles, BorderLayout.NORTH);
        panelInferior.add(new JScrollPane(errorArea), BorderLayout.CENTER);

        add(panelLaberinto, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void initComponents() {
        consoleArea = new JTextArea();
        consoleArea.setEditable(false);
        consoleArea.setBackground(new Color(30, 30, 30));
        consoleArea.setForeground(Color.WHITE);
        consoleArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        commandInput = new JTextField();
        commandInput.setFont(new Font("Monospaced", Font.PLAIN, 12));
        commandInput.addActionListener(e -> procesarComando());

        JPanel consolePanel = new JPanel(new BorderLayout());
        consolePanel.setPreferredSize(new Dimension(300, 0));
        consolePanel.setBorder(BorderFactory.createTitledBorder("Consola de Comandos"));
        consolePanel.add(new JScrollPane(consoleArea), BorderLayout.CENTER);
        consolePanel.add(commandInput, BorderLayout.SOUTH);

        add(consolePanel, BorderLayout.EAST);
    }

    private void procesarComando() {
        String command = commandInput.getText().trim();
        if (!command.isEmpty()) {
            consoleArea.append("> " + command + "\n");
            try {
                commandParser.executeCommand(command);
                panelLaberinto.repaint();
                errorArea.setText("");
            } catch (Exception ex) {
                mostrarError(ex.getMessage());
            }
            commandInput.setText("");
            commandInput.requestFocus();
        }
    }

    private void mostrarError(String mensaje) {
        errorArea.append("✖ " + mensaje + "\n");
        errorArea.setCaretPosition(errorArea.getDocument().getLength());
    }

private void dibujarLaberinto(Graphics g) {
    for (int y = 0; y < laberinto.getAlto(); y++) {
        for (int x = 0; x < laberinto.getAncho(); x++) {
            Celda celda = laberinto.getCelda(x, y);
            
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
        if (SwingUtilities.isLeftMouseButton(e)) {
            laberinto.togglePared(x, y);
        } else if (SwingUtilities.isRightMouseButton(e)) {
            laberinto.setCeldaInicio(x, y);
        }
        repaint();
    }

    private void guardarLaberinto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar laberinto");
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                laberinto.guardar(file.getAbsolutePath());
                mostrarError("✓ Laberinto guardado correctamente");
            } catch (Exception ex) {
                mostrarError("✖ Error al guardar: " + ex.getMessage());
            }
        }
    }

    private void cargarLaberinto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Cargar laberinto");
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                laberinto = Laberinto.cargar(file.getAbsolutePath());
                panelLaberinto.setPreferredSize(new Dimension(laberinto.getAncho() * celdaSize, laberinto.getAlto() * celdaSize));
                repaint();
                mostrarError("✓ Laberinto cargado correctamente");
            } catch (Exception ex) {
                mostrarError("✖ Error al cargar: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LaberintoGUI gui = new LaberintoGUI();
            gui.setVisible(true);
        });
    }
}