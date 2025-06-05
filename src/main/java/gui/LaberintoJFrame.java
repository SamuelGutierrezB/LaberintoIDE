package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * JFrame que contiene la interfaz del Laberinto IDE
 * Se encarga únicamente de la estructura visual y delegación de eventos
 */
public class LaberintoJFrame extends JFrame {
    
    private LaberintoGUI laberintoGUI;
    private JPanel panelLaberinto;
    private JButton btnGuardar, btnCargar;
    private JTextArea consoleArea;
    private JTextArea errorArea;
    private JTextField commandInput;
    private JPanel consolePanel;
    
    public LaberintoJFrame() {
        // Crear la instancia del controlador/lógica
        laberintoGUI = new LaberintoGUI(this);
        
        initializeFrame();
        setupComponents();
        setupLayout();
        setupEventListeners();
    }
    
    private void initializeFrame() {
        this.setTitle("Laberinto IDE");
        this.setSize(1000, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
    }
    
    private void setupComponents() {
        // Panel del laberinto
        panelLaberinto = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                laberintoGUI.dibujarLaberinto(g);
            }
        };
        
        // Botones de control
        btnGuardar = new JButton("Guardar");
        btnCargar = new JButton("Cargar");
        
        // Área de errores
        errorArea = new JTextArea(3, 40);
        errorArea.setEditable(false);
        errorArea.setBackground(new Color(255, 230, 230));
        errorArea.setForeground(Color.RED);
        errorArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        errorArea.setBorder(BorderFactory.createTitledBorder("Errores"));
        
        // Consola
        consoleArea = new JTextArea();
        consoleArea.setEditable(false);
        consoleArea.setBackground(new Color(30, 30, 30));
        consoleArea.setForeground(Color.WHITE);
        consoleArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        // Input de comandos
        commandInput = new JTextField();
        commandInput.setFont(new Font("Monospaced", Font.PLAIN, 12));
    }
    
    private void setupLayout() {
        // Panel de controles
        JPanel panelControles = new JPanel();
        panelControles.add(btnGuardar);
        panelControles.add(btnCargar);
        
        // Panel inferior
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(panelControles, BorderLayout.NORTH);
        panelInferior.add(new JScrollPane(errorArea), BorderLayout.CENTER);
        
        // Panel de consola
        consolePanel = new JPanel(new BorderLayout());
        consolePanel.setPreferredSize(new Dimension(300, 0));
        consolePanel.setBorder(BorderFactory.createTitledBorder("Consola de Comandos"));
        consolePanel.add(new JScrollPane(consoleArea), BorderLayout.CENTER);
        consolePanel.add(commandInput, BorderLayout.SOUTH);
        
        // Agregar a la ventana principal
        add(panelLaberinto, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
        add(consolePanel, BorderLayout.EAST);
    }
    
    private void setupEventListeners() {
        // Eventos de botones - delegados al controlador
        btnGuardar.addActionListener(e -> laberintoGUI.guardarLaberinto());
        btnCargar.addActionListener(e -> laberintoGUI.cargarLaberinto());
        
        // Evento de input de comandos
        commandInput.addActionListener(e -> laberintoGUI.procesarComando(commandInput.getText()));
        
        // Evento de mouse en el panel del laberinto
        panelLaberinto.addMouseListener(laberintoGUI.getMouseListener());
        
        // Focus en el input cuando se muestra la ventana
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                commandInput.requestFocusInWindow();
            }
        });
    }
    
    // Métodos para que el controlador pueda interactuar con la vista
    
    public void actualizarTamanoPanelLaberinto(int ancho, int alto, int celdaSize) {
        if (panelLaberinto != null) {
            panelLaberinto.setPreferredSize(new Dimension(ancho * celdaSize, alto * celdaSize));
            panelLaberinto.revalidate();
        }
    }
    
    public void repintarLaberinto() {
        if (panelLaberinto != null) {
            panelLaberinto.repaint();
        }
    }
    
    public void repintarVentana() {
        repaint();
        revalidate();
    }
    
    public void mostrarEnConsola(String mensaje) {
        if (consoleArea != null) {
            consoleArea.append(mensaje + "\n");
            consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
        }
    }
    
    public void mostrarError(String mensaje) {
        if (errorArea != null) {
            errorArea.append("✖ " + mensaje + "\n");
            errorArea.setCaretPosition(errorArea.getDocument().getLength());
        }
    }
    
    public void mostrarExito(String mensaje) {
        if (errorArea != null) {
            errorArea.append("✓ " + mensaje + "\n");
            errorArea.setCaretPosition(errorArea.getDocument().getLength());
        }
    }
    
    public void limpiarErrores() {
        if (errorArea != null) {
            errorArea.setText("");
        }
    }
    
    public void limpiarInputComando() {
        if (commandInput != null) {
            commandInput.setText("");
            commandInput.requestFocus();
        }
    }
    
    public LaberintoJFrame getFrame() {
        return this;
    }
    
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        LaberintoJFrame frame = new LaberintoJFrame();
        frame.setVisible(true);
    });
}

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 532, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
