package hotel;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;

public class ServiceSelector extends JFrame {
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
    private static final Color PRIMARY_COLOR = new Color(0, 102, 204);
    private static final Color SECONDARY_COLOR = new Color(51, 51, 51);
    private static final Color TEXT_COLOR = new Color(33, 33, 33);

    private int idReserva;

    public ServiceSelector(int idReserva) {
        setTitle("Selector de servicios");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(BACKGROUND_COLOR);
        add(panel);
        placeComponents(panel);
        setVisible(true);
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
        // Aquí puedes agregar lógica adicional si es necesario
        // Por ejemplo, actualizar la interfaz con información específica de la reserva
    }

    private void placeComponents(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Selector de servicios", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        gbc.gridwidth = 1;

        JComboBox<String> servicioComboBox = new JComboBox<>();
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(servicioComboBox, gbc);
        loadComboBoxServicio(servicioComboBox);

        JButton generateButton = createStyledButton("Asignar servicio");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(generateButton, gbc);
        generateButton.addActionListener(e -> {
            try {
                String servicio = (String) servicioComboBox.getSelectedItem();
                asignarServicio(servicio, idReserva);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese números válidos en todos los campos.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void loadComboBoxServicio(JComboBox<String> comboBox) {
        String query = "SELECT nombre FROM servicios";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            comboBox.removeAllItems(); // Limpiar el ComboBox antes de agregar nuevos elementos
            while (rs.next()) {
                comboBox.addItem(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    private void asignarServicio(String nombreServicio, int idReserva) {
        String queryServicio = "SELECT id_servicio FROM servicios WHERE nombre = ?";
        String queryInsert = "INSERT INTO reservas_servicios (id_reserva, id_servicio) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmtServicio = conn.prepareStatement(queryServicio)) {

            pstmtServicio.setString(1, nombreServicio);
            try (ResultSet rs = pstmtServicio.executeQuery()) {
                if (rs.next()) {
                    int idServicio = rs.getInt("id_servicio");
                    try (PreparedStatement pstmtInsert = conn.prepareStatement(queryInsert)) {
                        pstmtInsert.setInt(1, idReserva);
                        pstmtInsert.setInt(2, idServicio);
                        System.out.println("Un nuevo servicio fue insertado exitosamente! " + idReserva + " " + nombreServicio + " " + idServicio);

                        int rowsInserted = pstmtInsert.executeUpdate();
                        if (rowsInserted > 0) {
                            JOptionPane.showMessageDialog(this, "El servicio fue agregado correctamente a la reservación.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Servicio no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
