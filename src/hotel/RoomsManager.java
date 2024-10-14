package hotel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class RoomsManager extends JFrame {

    private JTable roomsTable;
    private DefaultTableModel tableModel;
    private JButton editButton, deleteButton, refreshButton, exitButton, generateButton;
    private Utils utils;

    public RoomsManager() {
        utils = new Utils();
        initComponents();
        loadRoomsData();
    }

    private void initComponents() {
        setTitle("Gestión de Habitaciones");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        // Set custom colors
        Color backgroundColor = new Color(240, 240, 240);
        Color buttonColor = new Color(70, 130, 180);
        Color textColor = Color.WHITE;

        // Table
        String[] columnNames = {"ID", "Disponibilidad", "Capacidad", "Precio", "Tipo", "Nombre"};
        tableModel = new DefaultTableModel(columnNames, 0);
        roomsTable = new JTable(tableModel);
        roomsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(roomsTable);
        
        // Buttons
        editButton = createStyledButton("Editar", buttonColor, textColor);
        deleteButton = createStyledButton("Eliminar", buttonColor, textColor);
        refreshButton = createStyledButton("Refrescar", buttonColor, textColor);
        exitButton = createStyledButton("Salir", buttonColor, textColor);
        generateButton = createStyledButton("Generador de habitaciones", buttonColor, textColor);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(generateButton);
        buttonPanel.add(exitButton);

        // Main layout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        editButton.addActionListener(e -> editSelectedRoom());
        deleteButton.addActionListener(e -> deleteSelectedRooms());
        refreshButton.addActionListener(e -> refreshTable());
        exitButton.addActionListener(e -> dispose());
        generateButton.addActionListener(e -> openRoomsGenerator());
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM habitaciones")) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_habitacion"),
                    rs.getBoolean("disponibilidad"),
                    rs.getInt("precio"),
                    rs.getString("tipo"),
                    rs.getString("nombre")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los datos: " + e.getMessage());
        }
    }

    private void loadRoomsData() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM habitaciones")) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_habitacion"),
                    rs.getBoolean("disponibilidad") ? "Disponible" : "No disponible",
                    rs.getInt("capacidad"),
                    rs.getInt("precio"),
                    rs.getString("tipo"),
                    rs.getString("nombre")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los datos: " + e.getMessage());
        
         }
    }

    private void editSelectedRoom() {
         int selectedRow = roomsTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String[] fields = {"disponibilidad", "precio", "tipo", "nombre"};
            utils.editRecord("habitaciones", id, "id_habitacion", fields, "Habitación");
            loadRoomsData(); // Refresh table after edit
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una habitación para editar.");
        }
    }

    private void deleteSelectedRooms() {
        int[] selectedRows = roomsTable.getSelectedRows();
        if (selectedRows.length > 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar " + selectedRows.length + " habitación(es)?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int modelRow = roomsTable.convertRowIndexToModel(selectedRows[i]);
                    int id = (int) tableModel.getValueAt(modelRow, 0);
                    deleteRoom(id);
                }
                loadRoomsData(); // Refresh table after deletes
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una o más habitaciones para eliminar.");
        }
    }
    private void deleteRoom(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM habitaciones WHERE id_habitacion = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar la habitación: " + e.getMessage());
        }
    }

    private void openRoomsGenerator() {
        SwingUtilities.invokeLater(() -> {
            RoomsGenerator generator = new RoomsGenerator();
            generator.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RoomsManager().setVisible(true);
        });
    }
}