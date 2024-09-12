package hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RoomsManager extends JFrame {
    private JTextField normalRoomsText;
    private JTextField vipRoomsText;
    private JTextField vipRoomsPriceText;
    private JTextField normalRoomsPriceText;

    public RoomsManager() {
        setTitle("Generador de Habitaciones");
        setSize(800, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panel);

        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("Generador de Habitaciones", SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        addLabelAndTextField(panel, "Habitaciones Normales:", 1, normalRoomsText = new JTextField(10));
        addLabelAndTextField(panel, "Habitaciones VIP:", 2, vipRoomsText = new JTextField(10));
        addLabelAndTextField(panel, "Precio de las Habitaciones VIP:", 3, vipRoomsPriceText = new JTextField(10));
        addLabelAndTextField(panel, "Precio de las Habitaciones normales:", 4, normalRoomsPriceText = new JTextField(10));

        JButton generateButton = new JButton("Generar Habitaciones");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(generateButton, gbc);

        generateButton.addActionListener(e -> {
            try {
                int normalRooms = Integer.parseInt(normalRoomsText.getText());
                int vipRooms = Integer.parseInt(vipRoomsText.getText());
                int vipRoomsPrice = Integer.parseInt(vipRoomsPriceText.getText());
                int normalRoomsPrice = Integer.parseInt(normalRoomsPriceText.getText());
                generateRooms(normalRooms, vipRooms, vipRoomsPrice, normalRoomsPrice);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese números válidos en todos los campos.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void addLabelAndTextField(JPanel panel, String labelText, int row, JTextField textField) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel label = new JLabel(labelText);
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(textField, gbc);
    }

    private void generateRooms(int normalRooms, int vipRooms, int vipRoomsPrice, int normalRoomsPrice) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            for (int i = 1; i <= normalRooms; i++) {
                insertRoom(conn, "Normal Room " + i, "Normal", normalRoomsPrice);
            }
            for (int i = 1; i <= vipRooms; i++) {
                insertRoom(conn, "VIP Room " + i, "VIP", vipRoomsPrice);
            }
            JOptionPane.showMessageDialog(this, "Se han generado " + (normalRooms + vipRooms) + " habitaciones con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar habitaciones: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void insertRoom(Connection conn, String roomName, String roomType, int roomPrice) throws SQLException {
        String query = "INSERT INTO habitaciones (nombre, tipo, precio, disponibilidad) VALUES (?, ?, ?, ?);";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, roomName);
            pstmt.setString(2, roomType);
            pstmt.setInt(3, roomPrice);
            pstmt.setInt(4, 1);
            pstmt.executeUpdate();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RoomsManager::new);
    }
}