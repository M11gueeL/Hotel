package hotel;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ExitHandler {
    
    // Método para confirmar salida en el boton superior izquierdo de todas las ventanas
    public static void addExitListener(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                showExitConfirmation(frame);
            }
        });
    }
    
    // Método para confirmar salida en botones de salir
    public static void showExitConfirmation(JFrame frame) {
        int option = JOptionPane.showConfirmDialog(
            frame,
            "¿Seguro que quieres salir?",
            "Confirmar salida",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
