package hotel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ReservationServiceManager extends JFrame {
    // Definición de colores para el tema
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
    private static final Color PRIMARY_COLOR = new Color(0, 102, 204);
    private static final Color SECONDARY_COLOR = new Color(51, 51, 51);
    private static final Color TEXT_COLOR = new Color(33, 33, 33);

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton exportPDF;

    public ReservationServiceManager() {
        setTitle("Gestión de Reservas y Servicios");
        setResizable(false);
        setSize(800, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        initComponents();
        loadData();
    }

    private void initComponents() {
        tableModel = new DefaultTableModel(new String[]{"ID Reserva", "ID", "Habitación", "Fecha Entrada", "Fecha Salida", "Cliente", "Servicio"}, 0) {
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
        exportPDF = createStyledButton("Exportar a PDF");
        JButton btnRefresh = createStyledButton("Refrescar");

        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(btnRefresh);
        buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonPanel.add(exportPDF);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action listeners
        exportPDF.addActionListener(e -> {
            PdfReportGenerator.exportarReservasServicios();
        });
        btnRefresh.addActionListener(e -> refreshTable());
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

    private void loadData() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT rs.id_reserva_servicio, r.id_reserva, h.nombre AS habitacion, r.fecha_entrada, r.fecha_salida, " +
                             "CONCAT(c.nombre, ' ', c.apellido) AS cliente, s.nombre AS servicio " +
                             "FROM reservas_servicios rs " +
                             "JOIN reservas r ON rs.id_reserva = r.id_reserva " +
                             "JOIN habitaciones h ON r.id_habitacion = h.id_habitacion " +
                             "JOIN reservas_clientes rc ON r.id_reserva = rc.id_reserva " +
                             "JOIN clientes c ON rc.id_cliente = c.id_cliente " +
                             "JOIN servicios s ON rs.id_servicio = s.id_servicio " +
                             "WHERE rc.es_titular = 1")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id_reserva"),
                        rs.getInt("id_reserva_servicio"),
                        rs.getString("habitacion"),
                        rs.getDate("fecha_entrada"),
                        rs.getDate("fecha_salida"),
                        rs.getString("cliente"),
                        rs.getString("servicio")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable() {
        loadData();
        JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.", "Actualización", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new ReservationServiceManager().setVisible(true));
    }
}