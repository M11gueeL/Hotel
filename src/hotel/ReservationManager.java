/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import hotel.Utils;
public class ReservationManager extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    
    public ReservationManager(){
        setTitle("Reservaciones");
        setSize(800, 400);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new String[]{"Reserva", "Cliente", "Habitación", "Fecha de entrada", "Fecha de salida", }, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
             JButton btnEdit = new JButton("Editar");
        JButton btnDelete = new JButton("Eliminar");
        JButton btnAdd = new JButton("Añadir reserva");
        JButton btnExit = new JButton("Salir");
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnExit);
        buttonPanel.add(btnAdd);
        add(buttonPanel, BorderLayout.SOUTH);
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana principal
            }
        });  
        btnAdd.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            ReservationForm reservationForm = new ReservationForm();
            reservationForm.setVisible(true);
            dispose(); // Cierra la ventana principal

                  
        }
        });
        
        btnDelete.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int idCliente = (int) tableModel.getValueAt(selectedRow, 0);
                    deleteUser("reservas", idCliente);
                    loadReservations("reservas");
                }
            }
        });

        btnEdit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int idReserva = (int) tableModel.getValueAt(selectedRow, 0);
                    String[] fields = {"id_habitaciones", "fecha_entrada", "fecha_salida"};
                    new Utils().editRecord("reservas", idReserva, "id_reserva", fields, "Reserva");
                    loadReservations("reservas");
                }
            }
        });
        loadReservations("reservas");
    }
    
    public void deleteUser(String tableName, int idReserva) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM " + tableName + " WHERE id_reserva = ?")) { // Cambiado a "id_empleado"
            pstmt.setInt(1, idReserva);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
        public void loadReservations(String tableName) {
        tableModel.setRowCount(0); // Clear existing data
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id_reserva"),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_habitaciones"),
                        rs.getString("fecha_entrada"),
                        rs.getString("fecha_salida"),
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}