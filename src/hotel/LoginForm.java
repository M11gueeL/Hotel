package hotel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class LoginForm extends JFrame {
    private JTextField userField;
    private JPasswordField passwordField;
    private JButton loginButton, exitButton, backButton;

    public LoginForm() {
        setTitle("Iniciar Sesión");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ExitHandler.addExitListener(this);

        JPanel mainPanel = createMainPanel();
        JPanel formPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        formPanel.setBackground(new Color(240, 240, 240));

        addFormField(formPanel, "Usuario", userField = createStyledTextField());
        addFormField(formPanel, "Contraseña", passwordField = createStyledPasswordField());

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(240, 240, 240));

        loginButton = createStyledButton("Iniciar Sesión");
        exitButton = createStyledButton("Salir");
        backButton = createStyledButton("Regresar");

        loginButton.addActionListener(e -> loginButtonActionPerformed());
        exitButton.addActionListener(e -> exitButtonActionPerformed());
        backButton.addActionListener(e -> backButtonActionPerformed());

        buttonPanel.add(loginButton);
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
    
    private JPanel createButtonPanel() {
    JPanel buttonPanel = new JPanel(new BorderLayout());

    JPanel loginButtonPanel = new JPanel(new BorderLayout());
    loginButtonPanel.setBackground(new Color(240, 240, 240));
    loginButtonPanel.add(loginButton, BorderLayout.CENTER);

    JPanel exitBackButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
    exitBackButtonPanel.setBackground(new Color(240, 240, 240));
    exitBackButtonPanel.add(exitButton);
    exitBackButtonPanel.add(backButton);

    buttonPanel.add(loginButtonPanel, BorderLayout.NORTH);
    buttonPanel.add(exitBackButtonPanel, BorderLayout.SOUTH);

    return buttonPanel;
}
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(100, 10));
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

    private void loginButtonActionPerformed() {
        String username = userField.getText();
        String password = new String(passwordField.getPassword());

        if (validateCredentials(username, password)) {
            openWelcomeWindow(username);
        } else {
            showErrorMessage();
        }
    }
    
    private void LoginButtonActionPerformed(java.awt.event.ActionEvent evt) {                                            
        String username = userField.getText();
        String password = new String(passwordField.getPassword());

        if (validateCredentials(username, password)) {
            openWelcomeWindow(username);
        } else {
            showErrorMessage();
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
    
    private boolean validateCredentials(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Si hay un resultado, las credenciales son válidas
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void openWelcomeWindow(String username) {
        Dashboard dashboard = new Dashboard(username);
        dashboard.setVisible(true);
        this.dispose(); // Cierra la ventana de login
    }

    private void showErrorMessage() {
        JOptionPane.showMessageDialog(this, 
            "Usuario o contraseña incorrectos", 
            "Error de inicio de sesión", 
            JOptionPane.ERROR_MESSAGE);
    }
}