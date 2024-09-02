/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel;

/**
 *
 * @author UsuarioX
 */
public class Hotel {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (DatabaseConnection.testConnection()) {
            System.out.println("La conexión a la base de datos funciona correctamente.");
        } else {
            System.out.println("No se pudo establecer la conexión a la base de datos.");
        }
    
    }
    
}
