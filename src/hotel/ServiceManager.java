package hotel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ServiceManager extends JFrame {
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
    private static final Color PRIMARY_COLOR = new Color(0, 102, 204);
    private static final Color SECONDARY_COLOR = new Color(51, 51, 51);
    private static final Color TEXT_COLOR = new Color(33, 33, 33);

    private JTable table;
    private DefaultTableModel tableModel;

    public ServiceManager() {
        setTitle("Gestión de Servicios");
        setResizable(false);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        tableModel = new DefaultTableModel(new String[]{"ID", "Nombre", "Precio"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        customizeTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JButton btnAdd = createStyledButton("Añadir");
        JButton btnEdit = createStyledButton("Editar");
        JButton btnDelete = createStyledButton("Eliminar");
        JButton btnExit = createStyledButton("Salir");

        buttonPanel.add(btnAdd);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(btnEdit);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(btnDelete);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(btnExit);
        add(buttonPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addService());
        btnEdit.addActionListener(e -> editSelectedService());
        btnDelete.addActionListener(e -> deleteSelectedService());
        btnExit.addActionListener(e -> dispose());

        loadServices();
    }

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

    private void addService() {
        JTextField nombreField = new JTextField();
        JTextField precioField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nombre:"));
        panel.add(nombreField);
        panel.add(new JLabel("Precio:"));
        panel.add(precioField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Añadir Servicio",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String nombre = nombreField.getText();
            String precio = precioField.getText();
            
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("INSERT INTO servicios (nombre, precio) VALUES (?, ?)")) {
                pstmt.setString(1, nombre);
                pstmt.setDouble(2, Double.parseDouble(precio));
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Servicio añadido con éxito.");
                loadServices();
            } catch (SQLException | NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Error al añadir el servicio: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editSelectedService() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String nombre = (String) tableModel.getValueAt(selectedRow, 1);
            double precio = (double) tableModel.getValueAt(selectedRow, 2);

            JTextField nombreField = new JTextField(nombre);
            JTextField precioField = new JTextField(String.valueOf(precio));

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Nombre:"));
            panel.add(nombreField);
            panel.add(new JLabel("Precio:"));
            panel.add(precioField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Editar Servicio",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement("UPDATE servicios SET nombre = ?, precio = ? WHERE id_servicio = ?")) {
                    pstmt.setString(1, nombreField.getText());
                    pstmt.setDouble(2, Double.parseDouble(precioField.getText()));
                    pstmt.setInt(3, id);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Servicio actualizado con éxito.");
                    loadServices();
                } catch (SQLException | NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el servicio: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un servicio para editar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteSelectedService() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este servicio?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement("DELETE FROM servicios WHERE id_servicio = ?")) {
                    pstmt.setInt(1, id);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Servicio eliminado con éxito.");
                    loadServices();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el servicio: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un servicio para eliminar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadServices() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM servicios")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id_servicio"),
                        rs.getString("nombre"),
                        rs.getDouble("precio")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los servicios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ServiceManager().setVisible(true));
    }
}