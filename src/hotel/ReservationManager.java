package hotel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ReservationManager extends JFrame {
    // Definición de colores para el tema
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 240); // Gris claro
    private static final Color PRIMARY_COLOR = new Color(0, 102, 204); // Azul
    private static final Color SECONDARY_COLOR = new Color(51, 51, 51); // Negro grisáceo
    private static final Color TEXT_COLOR = new Color(33, 33, 33); // Negro oscuro

    private JTable table;
    private DefaultTableModel tableModel;

    public ReservationManager() {
        setTitle("Reservaciones");
        setResizable(false);
        setSize(800, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Configuración del modelo de tabla y la tabla
        tableModel = new DefaultTableModel(new String[]{"Reserva", "Cliente", "Habitación", "Fecha de entrada", "Fecha de salida"}, 0) {
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
        JButton btnEdit = createStyledButton("Editar");
        JButton btnDelete = createStyledButton("Eliminar");
        JButton btnAdd = createStyledButton("Añadir reserva");
        JButton btnExit = createStyledButton("Salir");
        JButton btnExportarPDF = createStyledButton("Exportar a PDF");

        buttonPanel.add(btnDelete);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(btnEdit);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(btnAdd);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(btnExportarPDF);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(btnExit);
        add(buttonPanel, BorderLayout.SOUTH);

        // Acciones de los botones
        btnExit.addActionListener(e -> dispose());
        btnAdd.addActionListener(e -> openReservationForm());
        btnDelete.addActionListener(e -> deleteSelectedReservation());
        btnEdit.addActionListener(e -> editSelectedReservation());
        btnExportarPDF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreTabla = "reservas";
                CreatePDF.exportarTablaPDF(nombreTabla);
                JOptionPane.showMessageDialog(null, "PDF exportado exitosamente al escritorio.");
            }
        });

        loadReservations("reservas");
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

    private void openReservationForm() {
        ReservationForm reservationForm = new ReservationForm();
        reservationForm.setVisible(true);
        dispose();
    }

    private void deleteSelectedReservation() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int idReserva = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar esta reserva?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteReservation("reservas", idReserva);
                loadReservations("reservas");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una reserva para eliminar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editSelectedReservation() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int idReserva = (int) tableModel.getValueAt(selectedRow, 0);
            String[] fields = {"id_habitaciones", "fecha_entrada", "fecha_salida"};
            new Utils().editRecord("reservas", idReserva, "id_reserva", fields, "Reserva");
            loadReservations("reservas");
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una reserva para editar.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void deleteReservation(String tableName, int idReserva) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM " + tableName + " WHERE id_reserva = ?")) {
            pstmt.setInt(1, idReserva);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Reserva eliminada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar la reserva: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadReservations(String tableName) {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id_reserva"),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_habitaciones"),
                        rs.getString("fecha_entrada"),
                        rs.getString("fecha_salida"),
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar las reservas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new ReservationManager().setVisible(true));
    }
}