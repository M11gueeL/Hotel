package hotel;

public class Hotel {
    
    public static void main(String[] args) {
        if (DatabaseConnection.testConnection()) {
            System.out.println("La conexión a la base de datos funciona correctamente.");
            MainWindowHome gui = new MainWindowHome();
            gui.setVisible(true); // Make the GUI visible
        } else {
            System.out.println("No se pudo establecer la conexión a la base de datos.");
        }
   
    }    
    
}
