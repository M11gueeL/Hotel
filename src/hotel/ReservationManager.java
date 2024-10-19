package hotel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ReservationManager extends JFrame {
    // Definición de colores para el tema
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
    private static final Color PRIMARY_COLOR = new Color(0, 102, 204);
    private static final Color SECONDARY_COLOR = new Color(51, 51, 51);
    private static final Color TEXT_COLOR = new Color(33, 33, 33);

    private JTable table;
    private DefaultTableModel tableModel;

    public ReservationManager() {
        setTitle("Reservaciones");
        setResizable(false);
        setSize(800, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Configuración del modelo de tabla y la tabla
        tableModel = new DefaultTableModel(new String[]{"ID Reserva", "Cliente", "Habitación", "Fecha de entrada", "Fecha de salida", "Monto"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
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
        JButton btnRefresh = createStyledButton("Refrescar");
        JButton btnAdd = createStyledButton("Añadir reserva");
        JButton btnExit = createStyledButton("Salir");
        JButton btnExportarPDF = createStyledButton("Exportar a PDF");
        JButton btnService = createStyledButton("Añadir servicio");
        
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(btnRefresh);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(btnService);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(btnAdd);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(btnExportarPDF);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(btnExit);
        add(buttonPanel, BorderLayout.SOUTH);

        // Acciones de los botones
        btnRefresh.addActionListener(e -> refreshTable());
        btnExit.addActionListener(e -> dispose());
        btnAdd.addActionListener(e -> openReservationForm());
        btnService.addActionListener(e -> openServiceSelector());
        btnExportarPDF.addActionListener(e -> {
            PdfReportGenerator.exportarHistorialReservas();
        });

        loadReservations();
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

    private void openServiceSelector() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int idReserva = (int) tableModel.getValueAt(selectedRow, 0);
            ServiceSelector serviceSelector = new ServiceSelector(idReserva);
            serviceSelector.setIdReserva(idReserva);
            serviceSelector.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una reserva para añadir un servicio.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void loadReservations() {
        tableModel.setRowCount(0);
        String query = "SELECT r.id_reserva, " +
                       "GROUP_CONCAT(DISTINCT CONCAT(c.nombre, ' ', c.apellido) ORDER BY rc.es_titular DESC SEPARATOR ', ') AS clientes, " +
                       "h.nombre AS nombre_habitacion, r.id_habitacion, r.fecha_entrada, r.fecha_salida, r.monto " +
                       "FROM reservas r " +
                       "LEFT JOIN reservas_clientes rc ON r.id_reserva = rc.id_reserva " +
                       "LEFT JOIN clientes c ON rc.id_cliente = c.id_cliente " +
                       "LEFT JOIN habitaciones h ON r.id_habitacion = h.id_habitacion " +
                       "GROUP BY r.id_reserva, h.nombre, r.id_habitacion, r.fecha_entrada, r.fecha_salida, r.monto";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id_reserva"),
                    rs.getString("clientes"),
                    rs.getString("nombre_habitacion") + " (Id: " + rs.getInt("id_habitacion") + ")",
                    rs.getString("fecha_entrada"),
                    rs.getString("fecha_salida"),
                    rs.getInt("monto") +("$")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar las reservas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Método para refrescar la tabla
    private void refreshTable() {
        loadReservations();
        JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.", "Actualización", JOptionPane.INFORMATION_MESSAGE);
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