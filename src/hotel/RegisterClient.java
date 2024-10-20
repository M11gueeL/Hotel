package hotel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class RegisterClient extends JDialog {
   private JTextField nombreField, apellidoField, cedulaField, telefonoField;
    private JButton registerButton, cancelButton;
    private boolean clienteRegistrado = false;
    private int clienteId = -1; // Nueva variable para almacenar el ID del cliente
    private String clienteNombre; // Nueva variable para almacenar el nombre del cliente

    public RegisterClient() {
        setTitle("Registrar Nuevo Cliente");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);

        JPanel mainPanel = createMainPanel();
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        formPanel.setBackground(new Color(240, 240, 240));

        addFormField(formPanel, "Nombre", nombreField = createStyledTextField());
        addFormField(formPanel, "Apellido", apellidoField = createStyledTextField());
        addFormField(formPanel, "Cédula", cedulaField = createStyledTextField());
        addFormField(formPanel, "Teléfono", telefonoField = createStyledTextField());

        mainPanel.add(formPanel, BorderLayout.CENTER);

        registerButton = createStyledButton("Registrar");
        cancelButton = createStyledButton("Cancelar");
        registerButton.addActionListener(e -> registerClients());
        cancelButton.addActionListener(e -> cancel());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
    // Métodos reutilizables para crear componentes estilizados
    public static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 240, 240));
        return mainPanel;
    }

    public static JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(150, 30));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return field;
    }
private void registerClients() {
        String nombre = nombreField.getText();
        String apellido = apellidoField.getText();
        String cedula = cedulaField.getText();
        String telefono = telefonoField.getText();

        if (nombre.isEmpty() || apellido.isEmpty() || cedula.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO clientes (nombre, apellido, cedula, telefono) VALUES (?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, cedula);
            pstmt.setString(4, telefono);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        clienteId = generatedKeys.getInt(1);
                        clienteNombre = nombre + " " + apellido; // Guardamos el nombre completo
                        JOptionPane.showMessageDialog(this, "Cliente registrado con éxito. ID: " + clienteId, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        clienteRegistrado = true;
                        dispose();
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar el cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Nuevo método para obtener el nombre del cliente
    public String getClienteNombre() {
        return clienteNombre;
    }

    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    public static void addFormField(JPanel panel, String label, JTextField field) {
        JPanel fieldPanel = new JPanel(new BorderLayout(5, 5));
        fieldPanel.setBackground(new Color(240, 240, 240));
        
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        fieldPanel.add(jLabel, BorderLayout.NORTH);
        fieldPanel.add(field, BorderLayout.CENTER);
        
        panel.add(fieldPanel);
    }

    public boolean isClienteRegistrado() {
        return clienteRegistrado;
    }

    // Nuevo método para obtener el ID del cliente
    public int getClienteId() {
        return clienteId;
    }
    private void cancel() {
        clienteRegistrado = false;
        dispose();
    }
}