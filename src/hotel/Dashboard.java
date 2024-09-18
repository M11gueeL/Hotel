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
        setTitle("Panel Principal");
        this.username = username;
        welcomeLabel.setText("¡Bienvenido " + username + "!");
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        ExitHandler.addExitListener(this);
        setManageUserButtonVisibility();
        updateLabels();
    }
    
    private void updateLabels() {
        int totalClients = Utils.countRecords("clientes");
        Clients.setText("" + totalClients);
        
        int totalRooms = Utils.countRecords("habitaciones");
        Rooms.setText("" + totalRooms);
        
        int totalReservations = Utils.countRecords("reservas");
        reservations.setText("" + totalReservations);;
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
        GenerateRooms = new javax.swing.JButton();
        ManageReservation = new javax.swing.JButton();
        RegisterClient = new javax.swing.JButton();
        ManageClients = new javax.swing.JButton();
        LogOutButton = new javax.swing.JButton();
        ManageRooms = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        welcomeLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        reservations = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        Clients = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Rooms = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(730, 520));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        ManageUsers.setBackground(new java.awt.Color(102, 102, 102));
        ManageUsers.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ManageUsers.setForeground(new java.awt.Color(255, 255, 255));
        ManageUsers.setText("Administrar usuarios");
        ManageUsers.setBorder(null);
        ManageUsers.setFocusPainted(false);
        ManageUsers.setFocusable(false);
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

        GenerateRooms.setBackground(new java.awt.Color(102, 102, 102));
        GenerateRooms.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        GenerateRooms.setForeground(new java.awt.Color(255, 255, 255));
        GenerateRooms.setText("Habitaciones");
        GenerateRooms.setBorder(null);
        GenerateRooms.setFocusPainted(false);
        GenerateRooms.setFocusable(false);
        GenerateRooms.setIconTextGap(12);
        GenerateRooms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GenerateRoomsActionPerformed(evt);
            }
        });

        ManageReservation.setBackground(new java.awt.Color(102, 102, 102));
        ManageReservation.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ManageReservation.setForeground(new java.awt.Color(255, 255, 255));
        ManageReservation.setText("Reservas");
        ManageReservation.setBorder(null);
        ManageReservation.setFocusPainted(false);
        ManageReservation.setFocusable(false);
        ManageReservation.setIconTextGap(12);
        ManageReservation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ManageReservationActionPerformed(evt);
            }
        });

        RegisterClient.setBackground(new java.awt.Color(102, 102, 102));
        RegisterClient.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        RegisterClient.setForeground(new java.awt.Color(255, 255, 255));
        RegisterClient.setText("Registrar cliente");
        RegisterClient.setBorder(null);
        RegisterClient.setFocusPainted(false);
        RegisterClient.setFocusable(false);
        RegisterClient.setIconTextGap(12);
        RegisterClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RegisterClientActionPerformed(evt);
            }
        });

        ManageClients.setBackground(new java.awt.Color(102, 102, 102));
        ManageClients.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ManageClients.setForeground(new java.awt.Color(255, 255, 255));
        ManageClients.setText("Administrar clientes");
        ManageClients.setBorder(null);
        ManageClients.setFocusPainted(false);
        ManageClients.setFocusable(false);
        ManageClients.setIconTextGap(12);
        ManageClients.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ManageClientsActionPerformed(evt);
            }
        });

        LogOutButton.setBackground(new java.awt.Color(0, 102, 204));
        LogOutButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        LogOutButton.setForeground(new java.awt.Color(255, 255, 255));
        LogOutButton.setText("Cerrar Sesión");
        LogOutButton.setBorder(null);
        LogOutButton.setFocusPainted(false);
        LogOutButton.setFocusable(false);
        LogOutButton.setIconTextGap(12);
        LogOutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogOutButtonActionPerformed(evt);
            }
        });

        ManageRooms.setBackground(new java.awt.Color(102, 102, 102));
        ManageRooms.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ManageRooms.setForeground(new java.awt.Color(255, 255, 255));
        ManageRooms.setText("Administrar habitaciones");
        ManageRooms.setBorder(null);
        ManageRooms.setFocusPainted(false);
        ManageRooms.setFocusable(false);
        ManageRooms.setIconTextGap(12);
        ManageRooms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ManageRoomsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(LogOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(ManageRooms, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                    .addComponent(RegisterClient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(GenerateRooms, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ManageReservation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ManageUsers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ManageClients, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ManageReservation, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(GenerateRooms, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RegisterClient, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ManageRooms, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ManageClients, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ManageUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 137, Short.MAX_VALUE)
                .addComponent(LogOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 200, 420));

        jPanel2.setBackground(new java.awt.Color(0, 102, 255));
        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        welcomeLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        welcomeLabel.setForeground(new java.awt.Color(255, 255, 255));
        welcomeLabel.setText("jLabel1");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(welcomeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(397, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(welcomeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 730, 60));

        jPanel3.setBackground(new java.awt.Color(0, 102, 255));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Reservas ");

        reservations.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        reservations.setForeground(new java.awt.Color(255, 255, 255));
        reservations.setText("jLabel6");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(reservations, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reservations, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                .addGap(25, 25, 25))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 330, 220, 130));

        jPanel4.setBackground(new java.awt.Color(0, 102, 255));

        Clients.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        Clients.setForeground(new java.awt.Color(255, 255, 255));
        Clients.setText("jLabel4");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Clientes Registrados");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(Clients, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Clients, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 210, 220, 110));

        jPanel5.setBackground(new java.awt.Color(0, 102, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Habitaciones Registradas");

        Rooms.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        Rooms.setForeground(new java.awt.Color(255, 255, 255));
        Rooms.setText("jLabel2");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(Rooms, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Rooms, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 70, 220, 130));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void GenerateRoomsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GenerateRoomsActionPerformed
        RoomsGenerator roomsGenerator = new RoomsGenerator();
        roomsGenerator.setVisible(true);
    }//GEN-LAST:event_GenerateRoomsActionPerformed

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

    }//GEN-LAST:event_ManageUsersFocusGained

              private void ManageRoomsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ManageRoomsActionPerformed
                            RoomsManager roomsManager = new RoomsManager();
                            roomsManager.setVisible(true);
              }//GEN-LAST:event_ManageRoomsActionPerformed

    
    public static void main(String args[]) {
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Clients;
    private javax.swing.JButton GenerateRooms;
    private javax.swing.JButton LogOutButton;
    private javax.swing.JButton ManageClients;
    private javax.swing.JButton ManageReservation;
    private javax.swing.JButton ManageRooms;
    private javax.swing.JButton ManageUsers;
    private javax.swing.JButton RegisterClient;
    private javax.swing.JLabel Rooms;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel reservations;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}
