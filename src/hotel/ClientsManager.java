package hotel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ClientsManager extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public ClientsManager() {
        setTitle("Gestión de Clientes");
        setSize(800, 400);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new String[]{"id_cliente", "nombre", "apellido", "cedula", "telefono"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton btnDelete = new JButton("Eliminar");
        JButton btnEdit = new JButton("Editar");
        JButton btnExit = new JButton("Salir");
        JButton btnRegister = new JButton("Registrar Nuevo Cliente"); 

        buttonPanel.add(btnDelete);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnRegister); // Añadir el nuevo botón al panel
        buttonPanel.add(btnExit);
        add(buttonPanel, BorderLayout.SOUTH);

        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana principal
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int idCliente = (int) tableModel.getValueAt(selectedRow, 0);
                    deleteUser("clientes", idCliente);
                    loadClients("clientes");
                }
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int idCliente = (int) tableModel.getValueAt(selectedRow, 0);
                    String[] fields = {"nombre", "apellido", "cedula", "telefono"};
                    new Utils().editRecord("clientes", idCliente, "id_cliente", fields, "Cliente");
                    loadClients("clientes");
                }
            }
        });

        btnRegister.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterClient registerClientForm = new RegisterClient();
                registerClientForm.setVisible(true); // Mostrar el formulario de registro
            }
        });

        loadClients("clientes");
    }

    public void deleteUser(String tableName, int idEmpleado) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM " + tableName + " WHERE id_cliente = ?")) {
            pstmt.setInt(1, idEmpleado);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadClients(String tableName) {
        tableModel.setRowCount(0); // Clear existing data
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("cedula"),
                        rs.getString("telefono"),
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
