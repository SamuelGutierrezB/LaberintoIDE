package gui;


import analizador.CommandParser;
import modelo.Laberinto;
import modelo.Celda;
import modelo.Entidad;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class LaberintoGUI extends JFrame {
    private Laberinto laberinto;
    private JPanel panelLaberinto;
    private JButton btnGuardar, btnCargar;
    private int celdaSize = 40; // Tamaño de cada celda en píxeles
    private JTextArea consoleArea;
    private CommandParser commandParser;

    public LaberintoGUI() {
        // Configuración básica de la ventana
        setTitle("Laberinto IDE");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inicializar laberinto (10x10 por defecto)
        laberinto = new Laberinto(10, 10);
        commandParser = new CommandParser(laberinto); // ¡INICIALIZAR ANTES DE USAR!

        // Panel para dibujar el laberinto
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
                int x = e.getX() / celdaSize;
                int y = e.getY() / celdaSize;
                if (SwingUtilities.isLeftMouseButton(e)) {
                    laberinto.togglePared(x, y); // Alternar muro
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    laberinto.setCeldaInicio(x, y); // Establecer inicio
                }
                repaint();
            }
        });

        // Botones para guardar/cargar
        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarLaberinto());
        
        btnCargar = new JButton("Cargar");
        btnCargar.addActionListener(e -> cargarLaberinto());

        // Panel de controles
        JPanel panelControles = new JPanel();
        panelControles.add(btnGuardar);
        panelControles.add(btnCargar);
         initComponents(); // ¡ESTA ES LA LÍNEA CLAVE QUE FALTABA!
        // Añadir componentes a la ventana
        add(panelLaberinto, BorderLayout.CENTER);
        add(panelControles, BorderLayout.SOUTH);
    }

    private void dibujarLaberinto(Graphics g) {
        for (int y = 0; y < laberinto.getAlto(); y++) {
            for (int x = 0; x < laberinto.getAncho(); x++) {
                Celda celda = laberinto.getCelda(x, y);
                Color color;

                if (celda.esInicio()) {
                    color = Color.GREEN; // Celda de inicio
                } else if (celda.esFin()) {
                    color = Color.RED;   // Celda de fin
                } else if (celda.esPared()) {
                    color = Color.BLACK; // Muro
                } else {
                    color = Color.WHITE; // Espacio vacío
                }

                g.setColor(color);
                g.fillRect(x * celdaSize, y * celdaSize, celdaSize, celdaSize);
                g.setColor(Color.GRAY);
                g.drawRect(x * celdaSize, y * celdaSize, celdaSize, celdaSize);
            }
        }
    }

    private void guardarLaberinto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar laberinto");
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // Lógica para guardar en formato .pave (implementar en modelo.Laberinto)
            laberinto.guardar(file.getAbsolutePath());
        }
    }
    

    private void cargarLaberinto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Cargar laberinto");
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // Lógica para cargar desde .pave (implementar en modelo.Laberinto)
            laberinto = Laberinto.cargar(file.getAbsolutePath());
            panelLaberinto.setPreferredSize(new Dimension(laberinto.getAncho() * celdaSize, laberinto.getAlto() * celdaSize));
            repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LaberintoGUI gui = new LaberintoGUI();
            gui.setVisible(true);
        });
    }
    
private void initComponents() {
    // Consola de comandos
    consoleArea = new JTextArea();
    consoleArea.setEditable(false);
    consoleArea.setBackground(new Color(30, 30, 30));
    consoleArea.setForeground(Color.WHITE);
    consoleArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
  
    JTextField commandInput = new JTextField();
    commandInput.setFont(new Font("Monospaced", Font.PLAIN, 12));
    commandInput.addActionListener(e -> {
        String command = commandInput.getText().trim();
        if (!command.isEmpty()) {
            consoleArea.append("> " + command + "\n");
            try {
                commandParser.executeCommand(command);
                panelLaberinto.repaint();
            } catch (Exception ex) {
                consoleArea.append("Error: " + ex.getMessage() + "\n");
            }
            commandInput.setText("");
        }
    });

    JPanel consolePanel = new JPanel(new BorderLayout());
    consolePanel.setPreferredSize(new Dimension(300, 0));
    consolePanel.setBorder(BorderFactory.createTitledBorder("Consola de Comandos"));
  
    consolePanel.add(new JScrollPane(consoleArea), BorderLayout.CENTER);
    consolePanel.add(commandInput, BorderLayout.SOUTH);

    add(consolePanel, BorderLayout.EAST);
}
}