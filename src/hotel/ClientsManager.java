package hotel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ClientsManager extends JFrame {
    // Definición de colores para el tema
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 240); // Gris claro
    private static final Color PRIMARY_COLOR = new Color(0, 102, 204); // Azul
    private static final Color SECONDARY_COLOR = new Color(51, 51, 51); // Negro grisáceo
    private static final Color TEXT_COLOR = new Color(33, 33, 33); // Negro oscuro

    private JTable table;
    private DefaultTableModel tableModel;

    public ClientsManager() {
        setTitle("Gestión de Clientes");
        setSize(800, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Configuración del modelo de tabla y la tabla
        tableModel = new DefaultTableModel(new String[]{"ID", "Nombre", "Apellido", "Cédula", "Teléfono"}, 0) {
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
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JButton btnDelete = createStyledButton("Eliminar");
        JButton btnEdit = createStyledButton("Editar");
        JButton btnRegister = createStyledButton("Registrar Nuevo Cliente");
        JButton btnExit = createStyledButton("Salir");

        buttonPanel.add(btnDelete);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0))); // Espacio entre botones
        buttonPanel.add(btnEdit);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(btnRegister);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(btnExit);
        add(buttonPanel, BorderLayout.SOUTH);

        // Acciones de los botones
        btnDelete.addActionListener(e -> deleteSelectedClient());
        btnEdit.addActionListener(e -> editSelectedClient());
        btnRegister.addActionListener(e -> openRegisterClientForm());
        btnExit.addActionListener(e -> dispose());

        loadClients("clientes");
    }

    // Método para personalizar la apariencia de la tabla
    private void customizeTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setSelectionBackground(PRIMARY_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = table.getTableHeader();
        header.setBackground(SECONDARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBorder(BorderFactory.createEmptyBorder());
    }

    // Método para crear botones estilizados
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
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

    private void deleteSelectedClient() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int idCliente = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este cliente?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteUser("clientes", idCliente);
                loadClients("clientes");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un cliente para eliminar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editSelectedClient() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int idCliente = (int) tableModel.getValueAt(selectedRow, 0);
            String[] fields = {"nombre", "apellido", "cedula", "telefono"};
            new Utils().editRecord("clientes", idCliente, "id_cliente", fields, "Cliente");
            loadClients("clientes");
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un cliente para editar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openRegisterClientForm() {
        RegisterClient registerClientForm = new RegisterClient();
        registerClientForm.setVisible(true);
    }

    public void deleteUser(String tableName, int idCliente) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM " + tableName + " WHERE id_cliente = ?")) {
            pstmt.setInt(1, idCliente);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cliente eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar el cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadClients(String tableName) {
        tableModel.setRowCount(0);
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
            JOptionPane.showMessageDialog(this, "Error al cargar los clientes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new ClientsManager().setVisible(true));
    }
}