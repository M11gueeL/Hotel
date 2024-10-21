package hotel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RoomsManager extends JFrame {
    // Definición de colores para el tema
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
    private static final Color PRIMARY_COLOR = new Color(0, 102, 204);
    private static final Color SECONDARY_COLOR = new Color(51, 51, 51);

    private JTable roomsTable;
    private DefaultTableModel tableModel;
    private JButton editButton, deleteButton, availableRoomsReport, occupiedRoomsReport, desocuparButton;
    private Utils utils;

    public RoomsManager() {
        utils = new Utils();
        initComponents();
        loadRoomsData();
    }

    private void initComponents() {
        setTitle("Gestión de Habitaciones");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Table
        String[] columnNames = {"ID", "Disponibilidad", "Capacidad", "Precio", "Tipo", "Nombre"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        roomsTable = new JTable(tableModel);
        customizeTable(roomsTable);
        JScrollPane scrollPane = new JScrollPane(roomsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        editButton = createStyledButton("Editar");
        deleteButton = createStyledButton("Eliminar");
        availableRoomsReport = createStyledButton("Habitaciones disponibles");
        occupiedRoomsReport = createStyledButton("Habitaciones ocupadas");
        desocuparButton = createStyledButton("Desocupar Habitación");

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(availableRoomsReport);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(occupiedRoomsReport);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(desocuparButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        editButton.addActionListener(e -> editSelectedRoom());
        deleteButton.addActionListener(e -> deleteSelectedRooms());
        availableRoomsReport.addActionListener(e -> PdfReportGenerator.exportarHabitacionesDisponibles());
        occupiedRoomsReport.addActionListener(e -> PdfReportGenerator.exportarHabitacionesOcupadas());
        desocuparButton.addActionListener(e -> desocuparSelectedRoom());
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
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM habitaciones")) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id_habitacion"),
                    rs.getBoolean("disponibilidad"),
                    rs.getInt("precio") + ("$"),
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
                    rs.getInt("precio") + ("$"),
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
    
    private void desocuparSelectedRoom() {
    int selectedRow = roomsTable.getSelectedRow();
    if (selectedRow != -1) {
        int idHabitacion = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de que desea desocupar esta habitación?",
            "Confirmar desocupación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            desocuparHabitacion(idHabitacion);
        }
    } else {
        JOptionPane.showMessageDialog(this, "Por favor, seleccione una habitación para desocupar.");
    }
}
    
    public void desocuparHabitacion(int idHabitacion) {
              Connection conn = null;
              try {
                  conn = DatabaseConnection.getConnection();
                  conn.setAutoCommit(false);  // Iniciar transacción

                  // Actualizar la disponibilidad de la habitación a true
                  try (PreparedStatement pstmtUpdateRoom = conn.prepareStatement("UPDATE habitaciones SET disponibilidad = true WHERE id_habitacion = ?")) {
                      pstmtUpdateRoom.setInt(1, idHabitacion);
                      int rowsAffected = pstmtUpdateRoom.executeUpdate();

                      if (rowsAffected == 0) {
                          throw new SQLException("No se encontró la habitación con ID: " + idHabitacion);
                      }
                  }  

                  // Confirmar la transacción
                  conn.commit();
                  JOptionPane.showMessageDialog(this, "Habitación desocupada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                  // Refrescar la tabla de habitaciones
                  loadRoomsData();
              } catch (SQLException e) {
                  // Si algo salió mal, hacer rollback
                  if (conn != null) {
                      try {
                          conn.rollback();
                      } catch (SQLException ex) {
                          ex.printStackTrace();
                      }
                  }
                  e.printStackTrace();
                  JOptionPane.showMessageDialog(this, "Error al desocupar la habitación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
              } finally {
                  // Restaurar el auto-commit y cerrar la conexión
                  if (conn != null) {
                      try {
                          conn.setAutoCommit(true);
                          conn.close();
                      } catch (SQLException e) {
                          e.printStackTrace();
                      }
                  }
              }
          }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new RoomsManager().setVisible(true));
    }
}