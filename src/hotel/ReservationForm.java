package hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class ReservationForm extends JFrame {
    private JComboBox<String> clientesComboBox;
    private JComboBox<String> habitacionesComboBox;
    private JTextField fechaEntradaField;
    private JTextField fechaSalidaField;
    private JButton agregarReservaButton;

    private Map<String, Integer> clientesMap;
    private Map<String, Integer> habitacionesMap;

    public ReservationForm() {
        try {
            // Inicializar componentes
            clientesMap = getClientesMap();
            habitacionesMap = getHabitacionesMap();
            clientesComboBox = new JComboBox<>(getClientes().toArray(new String[0]));
            habitacionesComboBox = new JComboBox<>(getHabitaciones().toArray(new String[0]));
            fechaEntradaField = new JTextField(10);
            fechaSalidaField = new JTextField(10);
            agregarReservaButton = new JButton("Agregar Reserva");

            // Configurar JFrame
            setTitle("Formulario de Reservas");
            setSize(600, 350);
            setResizable(false);
            setLocationRelativeTo(null);
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

            // Agregar componentes al JFrame
            addComponent(0, 0, new JLabel("Cliente:"));
            addComponent(1, 0, clientesComboBox);
            addComponent(0, 1, new JLabel("Habitación:"));
            addComponent(1, 1, habitacionesComboBox);
            addComponent(0, 2, new JLabel("Fecha de Entrada:"));
            addComponent(1, 2, fechaEntradaField);
            addComponent(0, 3, new JLabel("Fecha de Salida:"));
            addComponent(1, 3, fechaSalidaField);
            addComponent(1, 4, 1, 1, GridBagConstraints.CENTER, agregarReservaButton);

            // Agregar acción al botón
            agregarReservaButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (validarFechas(fechaEntradaField.getText(), fechaSalidaField.getText())) {
                            int clienteId = clientesMap.get(clientesComboBox.getSelectedItem());
                            int habitacionId = habitacionesMap.get(habitacionesComboBox.getSelectedItem());
                            
                            // Verificar si la habitación está disponible
                            if (isHabitacionDisponible(habitacionId)) {
                                agregarReserva(clienteId, habitacionId, fechaEntradaField.getText(), fechaSalidaField.getText());
                                JOptionPane.showMessageDialog(null, "Reserva agregada exitosamente!");
                                dispose();
                                ReservationManager reservationManager = new ReservationManager();
                                reservationManager.setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(null, "La habitación seleccionada no está disponible.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Las fechas ingresadas no son válidas. Deben estar en formato YYYY-MM-DD.");
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al agregar reserva: " + ex.getMessage());
                    }
                }
            });

            iniciarActualizacionPeriodica();

            setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addComponent(int gridx, int gridy, JComponent component) {
        addComponent(gridx, gridy, 1, 1, GridBagConstraints.CENTER, component);
    }

    private void addComponent(int gridx, int gridy, int gridwidth, int gridheight, int anchor, JComponent component) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = anchor;
        add(component, gbc);
    }

    private Map<String, Integer> getClientesMap() throws SQLException {
        Map<String, Integer> clientesMap = new HashMap<>();
        String query = "SELECT id_cliente, nombre FROM clientes";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet resultSet = pstmt.executeQuery()) {
            while (resultSet.next()) {
                clientesMap.put(resultSet.getString("nombre"), resultSet.getInt("id_cliente"));
            }
        }
        return clientesMap;
    }

    private Map<String, Integer> getHabitacionesMap() throws SQLException {
        Map<String, Integer> habitacionesMap = new HashMap<>();
        String query = "SELECT id_habitacion, nombre FROM habitaciones WHERE disponibilidad = true";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet resultSet = pstmt.executeQuery()) {
            while (resultSet.next()) {
                habitacionesMap.put(resultSet.getString("nombre"), resultSet.getInt("id_habitacion"));
            }
        }
        return habitacionesMap;
    }

    private List<String> getClientes() throws SQLException {
        List<String> clientes = new ArrayList<>();
        String query = "SELECT nombre FROM clientes";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet resultSet = pstmt.executeQuery()) {
            while (resultSet.next()) {
                clientes.add(resultSet.getString("nombre"));
            }
        }
        return clientes;
    }

    private List<String> getHabitaciones() throws SQLException {
        List<String> habitaciones = new ArrayList<>();
        String query = "SELECT nombre FROM habitaciones WHERE disponibilidad = true";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet resultSet = pstmt.executeQuery()) {
            while (resultSet.next()) {
                habitaciones.add(resultSet.getString("nombre"));
            }
        }
        return habitaciones;
    }

    private void agregarReserva(int clienteId, int habitacionId, String fechaEntrada, String fechaSalida) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Insertar la nueva reserva
            String insertReservaQuery = "INSERT INTO reservas (id_cliente, id_habitaciones, fecha_entrada, fecha_salida) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertReservaQuery)) {
                pstmt.setInt(1, clienteId);
                pstmt.setInt(2, habitacionId);
                pstmt.setString(3, fechaEntrada);
                pstmt.setString(4, fechaSalida);
                pstmt.executeUpdate();
            }

            // Actualizar la disponibilidad de la habitación
            String updateHabitacionQuery = "UPDATE habitaciones SET disponibilidad = false WHERE id_habitacion = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateHabitacionQuery)) {
                pstmt.setInt(1, habitacionId);
                pstmt.executeUpdate();
            }

            conn.commit();
            actualizarComboBoxHabitaciones();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
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

    private boolean validarFechas(String fechaEntrada, String fechaSalida) {
        try {
            LocalDate.parse(fechaEntrada, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate.parse(fechaSalida, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isHabitacionDisponible(int habitacionId) throws SQLException {
        String query = "SELECT disponibilidad FROM habitaciones WHERE id_habitacion = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, habitacionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("disponibilidad");
                }
            }
        }
        return false;
    }

    private void actualizarComboBoxHabitaciones() {
        try {
            habitacionesMap = getHabitacionesMap();
            habitacionesComboBox.removeAllItems();
            for (String habitacion : getHabitaciones()) {
                habitacionesComboBox.addItem(habitacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void iniciarActualizacionPeriodica() {
        Timer timer = new Timer(5000, new ActionListener() { // Actualiza cada 5 segundos
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarComboBoxHabitaciones();
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ReservationForm();
            }
        });
    }
}