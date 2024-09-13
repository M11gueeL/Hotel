package hotel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;

public class Dashboard extends javax.swing.JFrame {

    private String username;

    public Dashboard(String username) {
        initComponents();
        this.username = username;
        welcomeLabel.setText("¡Bienvenido " + username + "!");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        ExitHandler.addExitListener(this);
        setManageUserButtonVisibility();
    }
    
    private void setManageUserButtonVisibility() {
        ManageClients.setVisible(isAdmin());
        ManageUsers.setVisible(isAdmin());
    }
    
    private boolean isAdmin() {
        String sql = "SELECT rol FROM usuarios WHERE usuario = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String rol = rs.getString("rol");
                    return "admin".equalsIgnoreCase(rol);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar el rol del usuario: " + e.getMessage());
        }
        return false;
    }
    
          
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        ManageUsers = new javax.swing.JButton();
        ManageClients = new javax.swing.JButton();
        ManageRooms = new javax.swing.JButton();
        ManageReservation = new javax.swing.JButton();
        RegisterClient = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        welcomeLabel = new javax.swing.JLabel();
        LogOutButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(730, 520));
        setMinimumSize(new java.awt.Dimension(730, 520));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        ManageUsers.setBackground(new java.awt.Color(8, 7, 7));
        ManageUsers.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ManageUsers.setForeground(new java.awt.Color(255, 255, 255));
        ManageUsers.setText("Administrar usuarios");
        ManageUsers.setBorder(null);
        ManageUsers.setBorderPainted(false);
        ManageUsers.setIconTextGap(12);
        ManageUsers.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ManageUsersFocusGained(evt);
            }
        });
        ManageUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ManageUsersActionPerformed(evt);
            }
        });

        ManageClients.setBackground(new java.awt.Color(8, 7, 7));
        ManageClients.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ManageClients.setForeground(new java.awt.Color(255, 255, 255));
        ManageClients.setText("Administrar clientes");
        ManageClients.setBorder(null);
        ManageClients.setBorderPainted(false);
        ManageClients.setIconTextGap(12);
        ManageClients.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ManageClientsActionPerformed(evt);
            }
        });

        ManageRooms.setBackground(new java.awt.Color(8, 7, 7));
        ManageRooms.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ManageRooms.setForeground(new java.awt.Color(255, 255, 255));
        ManageRooms.setText("Habitaciones");
        ManageRooms.setBorder(null);
        ManageRooms.setBorderPainted(false);
        ManageRooms.setIconTextGap(12);
        ManageRooms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ManageRoomsActionPerformed(evt);
            }
        });

        ManageReservation.setBackground(new java.awt.Color(8, 7, 7));
        ManageReservation.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ManageReservation.setForeground(new java.awt.Color(255, 255, 255));
        ManageReservation.setText("Reservas");
        ManageReservation.setBorder(null);
        ManageReservation.setBorderPainted(false);
        ManageReservation.setIconTextGap(12);
        ManageReservation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ManageReservationActionPerformed(evt);
            }
        });

        RegisterClient.setBackground(new java.awt.Color(8, 7, 7));
        RegisterClient.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        RegisterClient.setForeground(new java.awt.Color(255, 255, 255));
        RegisterClient.setText("Registrar cliente");
        RegisterClient.setBorder(null);
        RegisterClient.setBorderPainted(false);
        RegisterClient.setIconTextGap(12);
        RegisterClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RegisterClientActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ManageUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ManageRooms, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RegisterClient, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ManageReservation, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ManageClients, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(ManageUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ManageRooms, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RegisterClient, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ManageReservation, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ManageClients, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 730, 60));

        jPanel2.setBackground(new java.awt.Color(0, 102, 204));
        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        welcomeLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        welcomeLabel.setForeground(new java.awt.Color(255, 255, 255));
        welcomeLabel.setText("jLabel1");

        LogOutButton.setBackground(new java.awt.Color(0, 102, 204));
        LogOutButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        LogOutButton.setForeground(new java.awt.Color(255, 255, 255));
        LogOutButton.setText("Cerrar Sesión");
        LogOutButton.setBorder(null);
        LogOutButton.setBorderPainted(false);
        LogOutButton.setIconTextGap(12);
        LogOutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogOutButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(welcomeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 239, Short.MAX_VALUE)
                .addComponent(LogOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(welcomeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LogOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 730, 60));

        jPanel3.setBackground(new java.awt.Color(0, 102, 255));

        jLabel5.setText("Cantidad de Reservas ");

        jLabel6.setText("jLabel6");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 220, 130));

        jPanel4.setBackground(new java.awt.Color(0, 102, 255));

        jLabel4.setText("jLabel4");

        jLabel3.setText("Cantidad de Clientes Registrados");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 150, 210, 130));

        jPanel5.setBackground(new java.awt.Color(0, 102, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Cantidad de Habitaciones Registradas");

        jLabel2.setText("jLabel2");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 9, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 150, 230, 130));

        jPanel6.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 160, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 700, 160));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ManageRoomsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ManageRoomsActionPerformed
        RoomsManager roomsManager = new RoomsManager();
        roomsManager.setVisible(true);
    }//GEN-LAST:event_ManageRoomsActionPerformed

    private void ManageUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ManageUsersActionPerformed
        if (isAdmin()) {
            // Crear una instancia de UserManager y mostrarla
            UserManager userManager = new UserManager();
            userManager.setVisible(true);
            
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                "No tienes permisos para acceder a esta función.",
                "Acceso denegado",
                javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_ManageUsersActionPerformed

    private void ManageClientsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ManageClientsActionPerformed
            if (isAdmin()) {
                ClientsManager clientManager = new ClientsManager();
                clientManager.setVisible(true);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "No tienes permisos para acceder a esta función.",
                    "Acceso denegado",    
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            }
    }//GEN-LAST:event_ManageClientsActionPerformed

    private void ManageReservationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ManageReservationActionPerformed
       ReservationManager reservationManager = new ReservationManager();
       reservationManager.setVisible(true);
    }//GEN-LAST:event_ManageReservationActionPerformed

    private void RegisterClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegisterClientActionPerformed
        RegisterClient registerClient = new RegisterClient();
        registerClient.setVisible(true);
    }//GEN-LAST:event_RegisterClientActionPerformed

    private void LogOutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogOutButtonActionPerformed
        ExitHandler.showExitLogOutConfirmation(this);
    }//GEN-LAST:event_LogOutButtonActionPerformed

    private void ManageUsersFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ManageUsersFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_ManageUsersFocusGained

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // OJO al descomentar la siguiente linea, se podra acceder a esta ventana sin logearse, y no es la idea, en caso de querer probarla, tendran que pasarle un usuario como parametro entrecomillas
                // new Dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton LogOutButton;
    private javax.swing.JButton ManageClients;
    private javax.swing.JButton ManageReservation;
    private javax.swing.JButton ManageRooms;
    private javax.swing.JButton ManageUsers;
    private javax.swing.JButton RegisterClient;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}
