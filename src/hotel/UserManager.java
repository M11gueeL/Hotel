package hotel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UserManager extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public UserManager() {
        setTitle("Gestión de Usuarios");
        setSize(800, 400);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new String[]{"id_empleado", "nombre", "apellido", "cedula", "telefono", "email", "rol", "usuario", "password"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton btnDelete = new JButton("Eliminar");
        JButton btnEdit = new JButton("Editar");
        JButton btnExit = new JButton("Salir");
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnExit);
        add(buttonPanel, BorderLayout.SOUTH);

        
        
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int idEmpleado = (int) tableModel.getValueAt(selectedRow, 0);
                    deleteUser("usuarios", idEmpleado);
                    loadUsers("usuarios");
                }
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int idEmpleado = (int) tableModel.getValueAt(selectedRow, 0);
                    String[] fields = {"nombre", "apellido", "cedula", "telefono", "email"};
                    new Utils().editRecord("usuarios", idEmpleado, "id_empleado", fields, "Usuarios");
                    loadUsers("usuarios");
                }
            }
        });
        
        // Acción para el botón de salir
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana principal
            }
        });

        loadUsers("usuarios");
    }

    public void loadUsers(String tableName) {
        tableModel.setRowCount(0); // Clear existing data
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id_empleado"), // Cambiado a "id_empleado"
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("cedula"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("rol"),
                        rs.getString("usuario"),
                        rs.getString("password")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String tableName, int idEmpleado) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM " + tableName + " WHERE id_empleado = ?")) { // Cambiado a "id_empleado"
            pstmt.setInt(1, idEmpleado);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserManager userManager = new UserManager();
            userManager.setVisible(true);
        });
    }
}