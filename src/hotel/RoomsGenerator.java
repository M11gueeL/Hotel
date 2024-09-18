package hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RoomsGenerator extends JFrame {
    private JTextField normalRoomsText;
    private JTextField vipRoomsText;
    private JTextField vipRoomsPriceText;
    private JTextField normalRoomsPriceText;
    
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 240); // Gris claro
    private static final Color PRIMARY_COLOR = new Color(0, 102, 204); // Azul
    private static final Color SECONDARY_COLOR = new Color(51, 51, 51); // Negro grisáceo
    private static final Color TEXT_COLOR = new Color(33, 33, 33); // Negro oscuro

    public RoomsGenerator() {
        setTitle("Generador de Habitaciones");
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

    private void placeComponents(JPanel panel) {
   GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(10, 10, 10, 10);

    JLabel titleLabel = new JLabel("Generador de Habitaciones", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titleLabel.setForeground(PRIMARY_COLOR);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    panel.add(titleLabel, gbc);

    gbc.gridwidth = 1;

    addLabelAndTextField(panel, "Habitaciones Normales:", 1, normalRoomsText = createStyledTextField());
    addLabelAndTextField(panel, "Habitaciones VIP:", 2, vipRoomsText = createStyledTextField());
    addLabelAndTextField(panel, "Precio de las Habitaciones VIP:", 3, vipRoomsPriceText = createStyledTextField());
    addLabelAndTextField(panel, "Precio de las Habitaciones normales:", 4, normalRoomsPriceText = createStyledTextField());

    JButton generateButton = createStyledButton("Generar Habitaciones");
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
    gbc.insets = new Insets(10, 10, 10, 10);

    JLabel label = new JLabel(labelText);
    label.setFont(new Font("Arial", Font.BOLD, 14));
    label.setForeground(SECONDARY_COLOR);
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
    
    private JTextField createStyledTextField() {
              JTextField textField = new JTextField(10);
              textField.setFont(new Font("Arial", Font.PLAIN, 14));
              textField.setBorder(BorderFactory.createCompoundBorder(
                  BorderFactory.createLineBorder(PRIMARY_COLOR),
                  BorderFactory.createEmptyBorder(5, 5, 5, 5)));
              return textField;
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
          
          

    public static void main(String[] args) {
              try {
                  UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
              } catch (Exception e) {
                  e.printStackTrace();
              }
              SwingUtilities.invokeLater(RoomsGenerator::new);
          }
}