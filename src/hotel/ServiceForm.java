package hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ServiceForm extends JFrame {
      private JTextField nombreField;
    private JTextField descripcionField;
    private JTextField costoField;
    private JComboBox tipoComboBox;
    private JButton agregarServicioButton;
    private String[] opciones = {"Comun", "VIP"};

    public ServiceForm() {
        
            // Inicializar componentes
            tipoComboBox = new JComboBox(opciones);
            nombreField = new JTextField(10);
            descripcionField = new JTextField(10);
            costoField = new JTextField(10);
            agregarServicioButton = new JButton("Agregar servicio");

            // Configurar JFrame
            setTitle("Formulario de servicio");
            setSize(600, 350);
            setResizable(false);
            setLocationRelativeTo(null);
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

            // Agregar componentes al JFrame
            addComponent(0, 0, new JLabel("Nombre:"));
            addComponent(1, 0, nombreField);
            addComponent(0, 1, new JLabel("Descripción:"));
            addComponent(1, 1, descripcionField);
            addComponent(0, 2, new JLabel("Costo:"));
            addComponent(1, 2, costoField);
            addComponent(0, 3, new JLabel("Tipo:"));
            addComponent(1, 3, tipoComboBox);
            addComponent(1, 4, 1, 1, GridBagConstraints.CENTER, agregarServicioButton);

            // Agregar acción al botón
            agregarServicioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String seleccion= (String) tipoComboBox.getSelectedItem();
                        String x = costoField.getText();
                        int costoServicio = Integer.parseInt(x);
                            agregarServicio(nombreField.getText(), descripcionField.getText(), costoServicio, seleccion);
                                JOptionPane.showMessageDialog(null, "Servicio agregada exitosamente!");
                                dispose();
                                ServiceManager sm = new ServiceManager();
                                sm.setVisible(true);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al agregar servicio: " + ex.getMessage());
                    }
                }
            });
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

    private void agregarServicio(String nombre, String descripcion, int costo, String tipo) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Insertar la nueva reserva
            String insertReservaQuery = "INSERT INTO servicios (nombre, descripcion, costo, tipo) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertReservaQuery)) {
                pstmt.setString(1, nombre);
                pstmt.setString(2, descripcion);
                pstmt.setInt(3, costo);
                pstmt.setString(4, tipo);
                pstmt.executeUpdate();
            }
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ServiceForm();
            }
        });
    }
}
