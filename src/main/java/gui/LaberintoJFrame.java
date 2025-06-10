package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
        this.getContentPane().setBackground(Color.black);
        
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

        jPanel2 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnCargar = new javax.swing.JButton();
        titulo = new javax.swing.JLabel();
        commandInput = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        consoleArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        errorArea = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();

        setTitle("Dark and Mapper - generador de laberintos");
        setBackground(new java.awt.Color(0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setSize(new java.awt.Dimension(830, 500));

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        btnGuardar.setBackground(new java.awt.Color(102, 0, 0));
        btnGuardar.setFont(new java.awt.Font("Georgia", 3, 12)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("Guardar Mapa");
        btnGuardar.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), new java.awt.Color(51, 0, 0)));

        btnCargar.setBackground(new java.awt.Color(102, 0, 0));
        btnCargar.setFont(new java.awt.Font("Georgia", 3, 12)); // NOI18N
        btnCargar.setForeground(new java.awt.Color(255, 255, 255));
        btnCargar.setText("Cargar Mapa");
        btnCargar.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), new java.awt.Color(51, 0, 0)));

        titulo.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        titulo.setForeground(new java.awt.Color(255, 255, 255));
        titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titulo.setText("Dark and Mapper");

        consoleArea.setBackground(new java.awt.Color(204, 204, 204));
        consoleArea.setColumns(20);
        consoleArea.setFont(new java.awt.Font("Georgia", 1, 14)); // NOI18N
        consoleArea.setForeground(new java.awt.Color(255, 255, 255));
        consoleArea.setRows(5);
        jScrollPane1.setViewportView(consoleArea);

        errorArea.setBackground(new java.awt.Color(204, 204, 204));
        errorArea.setColumns(20);
        errorArea.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        errorArea.setForeground(new java.awt.Color(255, 0, 0));
        errorArea.setRows(5);
        jScrollPane2.setViewportView(errorArea);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67)
                        .addComponent(btnCargar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(commandInput, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(213, 213, 213)
                    .addComponent(titulo)
                    .addContainerGap(446, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCargar, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(commandInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(titulo)
                    .addContainerGap(549, Short.MAX_VALUE)))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCargar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JTextField commandInput;
    private javax.swing.JTextArea consoleArea;
    private javax.swing.JTextArea errorArea;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel titulo;
    // End of variables declaration//GEN-END:variables
}
