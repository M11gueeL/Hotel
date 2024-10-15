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
    private JTextField personasReservaField;
    private JLabel capacidadLabel;
    private JButton agregarReservaButton;
    private Map<String, Integer> clientesMap;
    private Map<String, int[]> habitacionesMap;
    private JList<String> clientesList;
    private DefaultListModel<String> clientesListModel;
    private JButton agregarClienteButton;
Color backgroundColor = new Color(70, 130, 180); // Ejemplo de un color azul claro


    public ReservationForm() {
        try {
        // Inicializar componentes
        clientesMap = getClientesMap();
        habitacionesMap = getHabitacionesMap();
        clientesComboBox = new JComboBox<>(getClientes().toArray(new String[0]));
        habitacionesComboBox = new JComboBox<>(getHabitaciones().toArray(new String[0]));
        fechaEntradaField = new JTextField(10);
        fechaSalidaField = new JTextField(10);
        personasReservaField = new JTextField(5);
        capacidadLabel = new JLabel("Capacidad de la habitación: ");
        agregarReservaButton = new JButton("Agregar Reserva");
        clientesListModel = new DefaultListModel<>();
        for (String cliente : getClientes()) {
            clientesListModel.addElement(cliente);
        }
        clientesList = new JList<>(clientesListModel);
        clientesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane clientesScrollPane = new JScrollPane(clientesList);
        clientesScrollPane.setPreferredSize(new Dimension(200, 150)); // Ajusta estos valores según necesites
        agregarClienteButton = new JButton("Agregar Cliente");

        // Configurar JFrame
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout()); // Cambiamos a BorderLayout

        // Crear panel de título
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Formulario de Reservas");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        // Agregar el panel de título al norte del BorderLayout
        add(titlePanel, BorderLayout.NORTH);

        // Crear un panel para el contenido principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
                        ReservationManager reservationManager = new ReservationManager();
                        reservationManager.setVisible(true);
        // Agregar componentes al panel principal
        addComponent(mainPanel, gbc, 0, 0, new JLabel("Clientes:"), 1, 1);
        addComponent(mainPanel, gbc, 1, 0, clientesScrollPane, 1, 3);
        addComponent(mainPanel, gbc, 2, 0, agregarClienteButton, 1, 1);
        addComponent(mainPanel, gbc, 0, 3, new JLabel("Habitación:"), 1, 1);
        addComponent(mainPanel, gbc, 1, 3, habitacionesComboBox, 1, 1);
        addComponent(mainPanel, gbc, 0, 4, new JLabel("Fecha de Entrada:"), 1, 1);
        addComponent(mainPanel, gbc, 1, 4, fechaEntradaField, 1, 1);
        addComponent(mainPanel, gbc, 0, 5, new JLabel("Fecha de Salida:"), 1, 1);
        addComponent(mainPanel, gbc, 1, 5, fechaSalidaField, 1, 1);
        addComponent(mainPanel, gbc, 0, 6, capacidadLabel, 2, 1);
        addComponent(mainPanel, gbc, 1, 7, agregarReservaButton, 1, 1, GridBagConstraints.CENTER);

        // Agregar el panel principal al centro del BorderLayout
        add(mainPanel, BorderLayout.CENTER);
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;


        // Personalizar componentes
        customizeScrollPane(clientesScrollPane);
        customizeComboBox(habitacionesComboBox);
        customizeTextField(fechaEntradaField);
        customizeTextField(fechaSalidaField);
        customizeButton(agregarReservaButton);
        customizeButton(agregarClienteButton);

            // Agregar ItemListener al habitacionesComboBox
            habitacionesComboBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String selectedHabitacion = (String) e.getItem();
                        int capacidad = habitacionesMap.get(selectedHabitacion)[1];
                        capacidadLabel.setText("Capacidad de la habitación: " + capacidad);
                    }
                }
            });
            
        agregarClienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterClient registerClient = new RegisterClient();
                registerClient.setVisible(true);
                registerClient.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        if (registerClient.isClienteRegistrado()) {
                            int nuevoClienteId = registerClient.getClienteId();
                            String nuevoClienteNombre = registerClient.getClienteNombre();
                            if (nuevoClienteId != -1 && nuevoClienteNombre != null && !nuevoClienteNombre.isEmpty()) {
                                clientesMap.put(nuevoClienteNombre, nuevoClienteId);
                                clientesListModel.addElement(nuevoClienteNombre);
                            } else {
                                JOptionPane.showMessageDialog(null, "Error al obtener los datos del nuevo cliente.");
                            }
                        }
                    }
                });
            }
        });
            // Agregar acción al botón
agregarReservaButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (validarFechas(fechaEntradaField.getText(), fechaSalidaField.getText())) {
                List<String> selectedClientes = clientesList.getSelectedValuesList();
                if (selectedClientes.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione al menos un cliente.");
                    return;
                }

                String selectedHabitacion = (String) habitacionesComboBox.getSelectedItem();
                int habitacionId = habitacionesMap.get(selectedHabitacion)[0];
                int capacidad = habitacionesMap.get(selectedHabitacion)[1];

                if (selectedClientes.size() > capacidad) {
                    JOptionPane.showMessageDialog(null, "El número de clientes seleccionados excede la capacidad de la habitación.");
                    return;
                }

                if (isHabitacionDisponible(habitacionId)) {
                    int reservaId = agregarReserva(habitacionId, fechaEntradaField.getText(), fechaSalidaField.getText(), selectedClientes);
                    if (reservaId != -1) {
                        for (int i = 0; i < selectedClientes.size(); i++) {
                            int clienteId = clientesMap.get(selectedClientes.get(i));
                            agregarClienteAReserva(reservaId, clienteId, i == 0);
                        }
                        JOptionPane.showMessageDialog(null, "Reserva agregada exitosamente!");
                        dispose();
                        ReservationManager reservationManager = new ReservationManager();
                        reservationManager.setVisible(true);
                    }
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
    private void registrarClientesAdicionales(int reservaId, int clientesAdicionales) {
        if (clientesAdicionales > 0) {
            RegisterClient registerClient = new RegisterClient();
            registerClient.setVisible(true);
            registerClient.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    if (registerClient.isClienteRegistrado()) {
                        try {
                            int nuevoClienteId = registerClient.getClienteId();
                            agregarClienteAReserva(reservaId, nuevoClienteId, false);
                            registrarClientesAdicionales(reservaId, clientesAdicionales - 1);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Error al agregar cliente a la reserva: " + ex.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Registro de cliente adicional cancelado.");
                    }
                }
            });
        } else {
            dispose();
        }
    }

    private void agregarClienteAReserva(int reservaId, int clienteId, boolean esTitular) throws SQLException {
        String query = "INSERT INTO reservas_clientes (id_reserva, id_cliente, es_titular) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, reservaId);
            pstmt.setInt(2, clienteId);
            pstmt.setBoolean(3, esTitular);
            pstmt.executeUpdate();
        }
    }
private int agregarReserva(int habitacionId, String fechaEntrada, String fechaSalida, List<String> clientes) throws SQLException {
    Connection conn = null;
    int reservaId = -1;
    try {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);

        // Insertar la nueva reserva
        String insertReservaQuery = "INSERT INTO reservas (id_habitaciones, fecha_entrada, fecha_salida, numero_personas) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertReservaQuery, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, habitacionId);
            pstmt.setString(2, fechaEntrada);
            pstmt.setString(3, fechaSalida);
            pstmt.setInt(4, clientes.size());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reservaId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating reservation failed, no ID obtained.");
                }
            }
        }

        // Actualizar la disponibilidad de la habitación
        String updateHabitacionQuery = "UPDATE habitaciones SET disponibilidad = 0 WHERE id_habitacion = ?";
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
    return reservaId;
}
private void addComponent(JPanel panel, GridBagConstraints gbc, int gridx, int gridy, JComponent component, int gridwidth, int gridheight) {
    gbc.gridx = gridx;
    gbc.gridy = gridy;
    gbc.gridwidth = gridwidth;
    gbc.gridheight = gridheight;
    panel.add(component, gbc);
}

private void addComponent(JPanel panel, GridBagConstraints gbc, int gridx, int gridy, JComponent component, int gridwidth, int gridheight, int anchor) {
    gbc.gridx = gridx;
    gbc.gridy = gridy;
    gbc.gridwidth = gridwidth;
    gbc.gridheight = gridheight;
    gbc.anchor = anchor;
    panel.add(component, gbc);
}
// Métodos para personalizar componentes

private void customizeScrollPane(JScrollPane scrollPane) {
    scrollPane.setBorder(BorderFactory.createTitledBorder("Clientes disponibles"));
}

private void customizeComboBox(JComboBox<String> comboBox) {
    comboBox.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value != null) {
                String habitacion = (String) value;
                int capacidad = habitacionesMap.get(habitacion)[1];
                setText(habitacion + " (Capacidad: " + capacidad + ")");
            }
            return this;
        }
    });
}

private void customizeTextField(JTextField textField) {
    textField.setPreferredSize(new Dimension(150, 25));
    textField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.GRAY),
        BorderFactory.createEmptyBorder(2, 5, 2, 5)));
}
private void customizeButton(JButton button) {
    button.setBackground(new Color(70, 130, 180));
    button.setForeground(Color.WHITE);  // Cambiado a Color.WHITE
    button.setFocusPainted(false);
    button.setFont(new Font("Arial", Font.BOLD, 14));
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
    
  private Map<String, int[]> getHabitacionesMap() throws SQLException {
        Map<String, int[]> habitacionesMap = new HashMap<>();
        String query = "SELECT id_habitacion, nombre, capacidad FROM habitaciones WHERE disponibilidad = true";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet resultSet = pstmt.executeQuery()) {
            while (resultSet.next()) {
                habitacionesMap.put(resultSet.getString("nombre"), 
                    new int[]{resultSet.getInt("id_habitacion"), resultSet.getInt("capacidad")});
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
        Timer timer = new Timer(60000, new ActionListener() { // Actualiza cada 60 segundos
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarComboBoxHabitaciones();
            }
        });
        timer.start();
    }
    private class CheckboxListCellRenderer extends JCheckBox implements ListCellRenderer<String> {
        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            setComponentOrientation(list.getComponentOrientation());
            setFont(list.getFont());
            setBackground(list.getBackground());
            setForeground(list.getForeground());
            setSelected(isSelected);
            setText(value);
            return this;
        }
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