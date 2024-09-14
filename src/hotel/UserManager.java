package hotel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UserManager extends JFrame {
    // Definición de colores para el tema
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 240); // Gris claro
    private static final Color PRIMARY_COLOR = new Color(0, 102, 204); // Azul
    private static final Color SECONDARY_COLOR = new Color(51, 51, 51); // Negro grisáceo
    private static final Color TEXT_COLOR = new Color(33, 33, 33); // Negro oscuro

    private JTable table;
    private DefaultTableModel tableModel;

    public UserManager() {
        setTitle("Gestión de Usuarios");
        setSize(800, 400); // Aumentado el tamaño para mejor visualización
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Configuración del modelo de tabla y la tabla
        tableModel = new DefaultTableModel(new String[]{"ID", "Nombre", "Apellido", "Cédula", "Teléfono", "Email", "Rol", "Usuario", "Contraseña"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hace que la tabla no sea editable
            }
        };
        table = new JTable(tableModel);
        customizeTable(table);

        // Panel de desplazamiento para la tabla
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        JButton btnDelete = createStyledButton("Eliminar");
        JButton btnEdit = createStyledButton("Editar");
        JButton btnExit = createStyledButton("Salir");
        buttonPanel.add(btnDelete);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Espacio entre botones
        buttonPanel.add(btnEdit);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Espacio entre botones
        buttonPanel.add(btnExit);
        add(buttonPanel, BorderLayout.SOUTH);

        // Acciones de los botones
        btnDelete.addActionListener(e -> deleteSelectedUser());
        btnEdit.addActionListener(e -> editSelectedUser());
        btnExit.addActionListener(e -> dispose());

        loadUsers("usuarios");
    }

    // Método para personalizar la apariencia de la tabla
    private void customizeTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setSelectionBackground(PRIMARY_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = table.getTableHeader();
        header.setBackground(SECONDARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBorder(BorderFactory.createEmptyBorder());
    }

    // Método para crear botones estilizados
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(button.getBackground().darker());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        return button;
    }

    // Método para cargar usuarios
    public void loadUsers(String tableName) {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id_empleado"),
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
            JOptionPane.showMessageDialog(this, "Error al cargar los usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para eliminar usuario seleccionado
    private void deleteSelectedUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int idEmpleado = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este usuario?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteUser("usuarios", idEmpleado);
                loadUsers("usuarios");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un usuario para eliminar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Método para editar usuario seleccionado
    private void editSelectedUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int idEmpleado = (int) tableModel.getValueAt(selectedRow, 0);
            String[] fields = {"nombre", "apellido", "cedula", "telefono", "email"};
            new Utils().editRecord("usuarios", idEmpleado, "id_empleado", fields, "Usuarios");
            loadUsers("usuarios");
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un usuario para editar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Método para eliminar usuario de la base de datos
    public void deleteUser(String tableName, int idEmpleado) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM " + tableName + " WHERE id_empleado = ?")) {
            pstmt.setInt(1, idEmpleado);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Usuario eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar el usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new UserManager().setVisible(true));
    }
}