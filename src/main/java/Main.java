import gui.LaberintoJFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LaberintoJFrame frame = new LaberintoJFrame();
            frame.setVisible(true);
        });
    }
}