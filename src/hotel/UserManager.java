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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                    editUser("usuarios", idEmpleado);
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

    public void editUser(String tableName, int idEmpleado) {
        JFrame editFrame = new JFrame("Editar Usuario");
        editFrame.setSize(400, 300);
        editFrame.setLayout(new GridLayout(10, 2));
        editFrame.setLocationRelativeTo(null);

        JTextField txtNombre = new JTextField();
        JTextField txtApellido = new JTextField();
        JTextField txtCedula = new JTextField();
        JTextField txtTelefono = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtRol = new JTextField();
        JTextField txtUsuario = new JTextField();
        JTextField txtContrasena = new JTextField();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE id_empleado = ?")) { // Cambiado a "id_empleado"
            pstmt.setInt(1, idEmpleado);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                txtNombre.setText(rs.getString("nombre"));
                txtApellido.setText(rs.getString("apellido"));
                txtCedula.setText(rs.getString("cedula"));
                txtTelefono.setText(rs.getString("telefono"));
                txtEmail.setText(rs.getString("email"));
                txtRol.setText(rs.getString("rol"));
                txtUsuario.setText(rs.getString("usuario"));
                txtContrasena.setText(rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        editFrame.add(new JLabel("Nombre:"));
        editFrame.add(txtNombre);
        editFrame.add(new JLabel("Apellido:"));
        editFrame.add(txtApellido);
        editFrame.add(new JLabel("Cédula:"));
        editFrame.add(txtCedula);
        editFrame.add(new JLabel("Teléfono:"));
        editFrame.add(txtTelefono);
        editFrame.add(new JLabel("Email:"));
        editFrame.add(txtEmail);
        editFrame.add(new JLabel("Rol:"));
        editFrame.add(txtRol);
        editFrame.add(new JLabel("Usuario:"));
        editFrame.add(txtUsuario);
        editFrame.add(new JLabel("password:"));
        editFrame.add(txtContrasena);

        JButton btnSave = new JButton("Guardar");
        editFrame.add(btnSave);
        
        JButton btnClose = new JButton("Cerrar");
        editFrame.add(btnClose);

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(
                             "UPDATE " + tableName + " SET nombre = ?, apellido = ?, cedula = ?, telefono = ?, email = ?, rol = ?, usuario = ?, password = ? WHERE id_empleado = ?")) { // Cambiado a "id_empleado"
                    pstmt.setString(1, txtNombre.getText());
                    pstmt.setString(2, txtApellido.getText());
                    pstmt.setString(3, txtCedula.getText());
                    pstmt.setString(4, txtTelefono.getText());
                    pstmt.setString(5, txtEmail.getText());
                    pstmt.setString(6, txtRol.getText());
                    pstmt.setString(7, txtUsuario.getText());
                    pstmt.setString(8, txtContrasena.getText());
                    pstmt.setInt(9, idEmpleado);
                    pstmt.executeUpdate();
                    loadUsers("usuarios");
                    editFrame.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editFrame.dispose();
            }
        });

        editFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserManager userManager = new UserManager();
            userManager.setVisible(true);
        });
    }
}