package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * JFrame que contiene la interfaz del Laberinto IDE
 * Diseñado para usar con NetBeans Visual Designer
 */
public class LaberintoJFrame extends javax.swing.JFrame {
    
    private LaberintoGUI laberintoGUI;
    private JPanel panelLaberinto; // Panel personalizado para el dibujo
    
    public LaberintoJFrame() {
        initComponents();
        laberintoGUI = new LaberintoGUI(this);
        laberintoGUI.crearLaberintoPorDefecto(10, 10); // ejemplo
        
        setupFrame();
        setupEventListeners();
        setupPanelLaberinto(); // Configurar el panel de dibujo
    }
    
    public JPanel getPanelLaberinto() {
        return panelLaberinto;
    }
    
    public Dimension getPanelLaberintoSize() {
        return panelLaberinto != null ? panelLaberinto.getSize() : new Dimension(0, 0);
    }

    private void setupFrame() {
        this.setTitle("Laberinto IDE");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        
        // Configurar áreas de texto
        if (errorArea != null) {
            errorArea.setEditable(false);
            errorArea.setBackground(new Color(255, 230, 230));
            errorArea.setForeground(Color.RED);
            errorArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        }
        
        if (consoleArea != null) {
            consoleArea.setEditable(false);
            consoleArea.setBackground(new Color(30, 30, 30));
            consoleArea.setForeground(Color.WHITE);
            consoleArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        }
        
        if (commandInput != null) {
            commandInput.setFont(new Font("Monospaced", Font.PLAIN, 12));
            commandInput.setBackground(new Color(240, 240, 240));
        }
        
        // Ajustar el tamaño de la ventana
        this.setSize(800, 600);
        this.setResizable(true);
        this.setLocationRelativeTo(null);
    }
    
    private void setupEventListeners() {
        // Configurar eventos de botones
        if (btnGuardar != null) {
            btnGuardar.addActionListener(e -> laberintoGUI.guardarLaberinto());
        }
        
        if (btnCargar != null) {
            btnCargar.addActionListener(e -> laberintoGUI.cargarLaberinto());
        }
        
        if (commandInput != null) {
            commandInput.addActionListener(e -> {
                laberintoGUI.procesarComando(commandInput.getText());
                commandInput.setText("");
            });
        }
        
        // Focus en el input cuando se muestra la ventana
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                if (commandInput != null) {
                    commandInput.requestFocusInWindow();
                }
            }
        });
    }
    
    private void setupPanelLaberinto() {
        // Crear el panel personalizado para dibujar el laberinto
        panelLaberinto = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (laberintoGUI != null && laberintoGUI.getLaberinto() != null) {
                    laberintoGUI.dibujarLaberinto(g);
                }
            }
        };
        
        panelLaberinto.setBackground(Color.WHITE);
        panelLaberinto.setPreferredSize(new Dimension(400, 400));
        
        // Configurar el layout del jPanel1 y agregar el panelLaberinto
        jPanel1.setLayout(new BorderLayout());
        jPanel1.removeAll();
        jPanel1.add(panelLaberinto, BorderLayout.CENTER);
        
        // Si necesitas scroll para laberintos grandes
        JScrollPane scrollPane = new JScrollPane(panelLaberinto);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        jPanel1.add(scrollPane, BorderLayout.CENTER);
        jPanel1.revalidate();
        jPanel1.repaint();
    }
    
    public void actualizarTamanoPanelLaberinto(int ancho, int alto, int celdaSize) {
        if (panelLaberinto != null) {
            panelLaberinto.setPreferredSize(new Dimension(ancho * celdaSize, alto * celdaSize));
            panelLaberinto.revalidate();
            panelLaberinto.repaint();
        }
    }
    
    public void repintarLaberinto() {
        if (panelLaberinto != null) {
            panelLaberinto.repaint();
        }
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
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LaberintoJFrame frame = new LaberintoJFrame();
            frame.setVisible(true);
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
                      
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGuardar = new javax.swing.JButton();
        btnCargar = new javax.swing.JButton();
        titulo = new javax.swing.JLabel();
        commandInput = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        consoleArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        errorArea = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();

        btnGuardar.setText("Guardar");

        btnCargar.setText("Cargar");

        titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titulo.setText("Dark and mapper");

        consoleArea.setColumns(20);
        consoleArea.setRows(5);
        jScrollPane1.setViewportView(consoleArea);

        errorArea.setColumns(20);
        errorArea.setRows(5);
        jScrollPane2.setViewportView(errorArea);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 542, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 264, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(130, 130, 130)
                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnCargar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(commandInput, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(titulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCargar, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commandInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCargar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JTextField commandInput;
    private javax.swing.JTextArea consoleArea;
    private javax.swing.JTextArea errorArea;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables
}
