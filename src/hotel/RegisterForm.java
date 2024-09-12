package hotel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class RegisterForm extends JFrame {
    private JTextField fieldName, fieldLastName, fieldTelefono, fieldCedula, userField, emailField, rolField;
    private JPasswordField passwordField;
    private JButton exitButton, registerButton, backButton;

    public RegisterForm() {
        
        setTitle("Registro de Usuario");
        setSize(400, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ExitHandler.addExitListener(this);

        JPanel mainPanel = createMainPanel();
        JPanel formPanel = new JPanel(new GridLayout(8, 1, 10, 10));
        formPanel.setBackground(new Color(240, 240, 240));

        addFormField(formPanel, "Nombre", fieldName = createStyledTextField());
        addFormField(formPanel, "Apellido", fieldLastName = createStyledTextField());
        addFormField(formPanel, "Cédula", fieldCedula = createStyledTextField());
        addFormField(formPanel, "Teléfono", fieldTelefono = createStyledTextField());
        addFormField(formPanel, "Usuario", userField = createStyledTextField());
        addFormField(formPanel, "Email", emailField = createStyledTextField());
        addFormField(formPanel, "Rol", rolField = createStyledTextField());
        addFormField(formPanel, "Contraseña", passwordField = createStyledPasswordField());

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(240, 240, 240));

        registerButton = createStyledButton("Registrar");
        exitButton = createStyledButton("Salir");
        backButton = createStyledButton("Regresar");

        registerButton.addActionListener(e -> registerButtonActionPerformed());
        exitButton.addActionListener(e -> exitButtonActionPerformed());
        backButton.addActionListener(e -> backButtonActionPerformed());

        buttonPanel.add(registerButton);
        buttonPanel.add(exitButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 240, 240));
        return mainPanel;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(200, 30));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setPreferredSize(new Dimension(200, 30));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return field;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private void addFormField(JPanel panel, String label, JComponent field) {
        JPanel fieldPanel = new JPanel(new BorderLayout(5, 5));
        fieldPanel.setBackground(new Color(240, 240, 240));
        
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        fieldPanel.add(jLabel, BorderLayout.NORTH);
        fieldPanel.add(field, BorderLayout.CENTER);
        
        panel.add(fieldPanel);
    }

    private void registerButtonActionPerformed() {
        if (validateFields()) {
            registerUser();
        }
    }

    private void exitButtonActionPerformed() {
        ExitHandler.showExitConfirmation(this);
    }

    private void backButtonActionPerformed() {
        MainWindowHome x = new MainWindowHome();
        this.setVisible(false);
        x.setVisible(true);
    }
    
    private boolean validateFields() {
        if (fieldName.getText().trim().isEmpty() ||
            fieldLastName.getText().trim().isEmpty() ||
            fieldCedula.getText().trim().isEmpty() ||
            fieldTelefono.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty() ||
            rolField.getText().trim().isEmpty() ||
            userField.getText().trim().isEmpty() ||
            passwordField.getPassword().length == 0) {

            javax.swing.JOptionPane.showMessageDialog(this, 
                "Todos los campos deben estar llenos", 
                "Error de validación", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!isValidEmail(emailField.getText().trim())) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "El correo electrónico no es válido", 
                "Error de validación", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
    
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pattern.matcher(email).matches();
    }

    private void registerUser() {
        String nombre = fieldName.getText();
        String apellido = fieldLastName.getText();
        String cedula = fieldCedula.getText();
        String telefono = fieldTelefono.getText();
        String email = emailField.getText();
        String rol = rolField.getText();
        String usuario = userField.getText();
        String password = new String(passwordField.getPassword());

        String sql = "INSERT INTO usuarios (nombre, apellido, cedula, telefono, email, rol, usuario, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, cedula);
            pstmt.setString(4, telefono);
            pstmt.setString(5, email);
            pstmt.setString(6, rol);
            pstmt.setString(7, usuario);
            pstmt.setString(8, password);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente");
                clearFields();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Error al registrar el usuario");
            }

        } catch (SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error de base de datos: " + e.getMessage());
        }
    }

    private void clearFields() {
        fieldName.setText("");
        fieldLastName.setText("");
        fieldCedula.setText(""); 
        fieldTelefono.setText("");        
        emailField.setText("");
        rolField.setText("");
        userField.setText("");
        passwordField.setText("");
    }
    
    

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new RegisterForm().setVisible(true));
    }
}

