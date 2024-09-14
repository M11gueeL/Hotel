/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author juand
 */
public class Utils {
    
    public void editRecord(String tableName, int id, String idFieldName, String[] fields, String label) {
        JFrame editFrame = new JFrame("Editar " + label);
        editFrame.setSize(400, (fields.length + 1) * 30);
        editFrame.setLayout(new GridLayout(fields.length + 1, 2));
        editFrame.setLocationRelativeTo(null);

         JTextField[] textFields = new JTextField[fields.length];

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE " + idFieldName + " = ?")) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                for (int i = 0; i < fields.length; i++) {
                    JLabel fieldLabel = new JLabel(fields[i] + ":");
                    textFields[i] = new JTextField(rs.getString(fields[i]));
                    editFrame.add(fieldLabel);
                    editFrame.add(textFields[i]);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JButton saveButton = new JButton("Guardar");
        saveButton.addActionListener(e -> {
            updateRecord(tableName, id, idFieldName, fields, textFields);
            editFrame.dispose();
        });

    editFrame.add(new JLabel());
    editFrame.add(saveButton);

    editFrame.setVisible(true);
    }
    private void updateRecord(String tableName, int id, String idFieldName, String[] fields, JTextField[] textFields) {
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        for (int i = 0; i < fields.length; i++) {
            sql.append(fields[i]).append(" = ?");
            if (i < fields.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(" WHERE ").append(idFieldName).append(" = ?");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < fields.length; i++) {
                pstmt.setString(i + 1, textFields[i].getText());
            }
            pstmt.setInt(fields.length + 1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static int countRecords(String tableName){
        int totalRecords = 0;
        String consult = "SELECT COUNT(*) AS total FROM " + tableName;
        
        try (Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(consult)) {
            
            if (result.next()) {
                totalRecords = result.getInt("Total");
            }
            
        } catch (SQLException e) {
            System.out.println("Error al contar registros " + e.getMessage());
            e.printStackTrace();
        }
        return totalRecords;
    }
}
