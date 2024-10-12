package hotel;
import java.awt.Frame;
import javax.swing.JFrame;
public class MainWindowHome extends javax.swing.JFrame {
    //La siguiente variable que sea una variable de imagen

     
    
    public MainWindowHome() {
        initComponents();  
        setTitle("Java Resort Deluxe");
        setSize(600, 600); // Set the desired size of the window
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensure the application exits on close
        ExitHandler.addExitListener(this);
        String textoLargo = "Registrate o inicia sesión para descubrir un mundo de lujo y confort en cada estancia.";
        jLabel3.setText("<html><div style='text-align: center;'>" + textoLargo.replace("\n", "<br>") + "</div></html>");
    }
    
    @SuppressWarnings("unchecked")
              // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
              private void initComponents() {

                            jPanel1 = new javax.swing.JPanel();
                            ExitButton = new javax.swing.JButton();
                            jLabel3 = new javax.swing.JLabel();
                            LoginButton = new javax.swing.JButton();
                            jLabel4 = new javax.swing.JLabel();
                            jLabel1 = new javax.swing.JLabel();

                            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

                            jPanel1.setBackground(new java.awt.Color(25, 53, 76));

                            ExitButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
                            ExitButton.setForeground(new java.awt.Color(51, 51, 51));
                            ExitButton.setText("Salir");
                            ExitButton.setFocusPainted(false);
                            ExitButton.setFocusable(false);
                            ExitButton.addActionListener(new java.awt.event.ActionListener() {
                                          public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                        ExitButtonActionPerformed(evt);
                                          }
                            });

                            jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
                            jLabel3.setForeground(new java.awt.Color(255, 255, 255));
                            jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                            jLabel3.setText("Registrate o inicia sesión para descubrir un mundo de lujo y confort en cada estancia.");
                            jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

                            LoginButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
                            LoginButton.setForeground(new java.awt.Color(51, 51, 51));
                            LoginButton.setText("Iniciar Sesión");
                            LoginButton.setFocusPainted(false);
                            LoginButton.setFocusable(false);
                            LoginButton.addActionListener(new java.awt.event.ActionListener() {
                                          public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                        LoginButtonActionPerformed(evt);
                                          }
                            });

                            jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
                            jLabel4.setForeground(new java.awt.Color(255, 255, 255));
                            jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                            jLabel4.setText("Bienvenido a Java Resort Deluxe");

                            jLabel1.setForeground(new java.awt.Color(255, 255, 255));
                            jLabel1.setText("logo");
                            jLabel1.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/sinFondo.png")).getImage().getScaledInstance(307, 307, 0)));

                            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                            jPanel1.setLayout(jPanel1Layout);
                            jPanel1Layout.setHorizontalGroup(
                                          jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(102, 102, 102))
                                          .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                      .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                    .addGap(148, 148, 148)
                                                                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                      .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                    .addGap(85, 85, 85)
                                                                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                      .addGroup(jPanel1Layout.createSequentialGroup()
                                                                                    .addGap(232, 232, 232)
                                                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                  .addComponent(LoginButton)
                                                                                                  .addComponent(ExitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                        .addContainerGap(85, Short.MAX_VALUE))
                            );

                            jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {ExitButton, LoginButton});

                            jPanel1Layout.setVerticalGroup(
                                          jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                          .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addGap(41, 41, 41)
                                                        .addComponent(jLabel4)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jLabel3)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                                                        .addComponent(LoginButton)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(ExitButton)
                                                        .addGap(17, 17, 17))
                            );

                            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                            getContentPane().setLayout(layout);
                            layout.setHorizontalGroup(
                                          layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                          .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            );
                            layout.setVerticalGroup(
                                          layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                          .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            );

                            pack();
              }// </editor-fold>//GEN-END:initComponents

    private void LoginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoginButtonActionPerformed
        LoginForm x = new LoginForm();
        this.setVisible(false);
        x.setVisible(true);
    }//GEN-LAST:event_LoginButtonActionPerformed

    private void ExitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitButtonActionPerformed
        ExitHandler.showExitConfirmation(this);
    }//GEN-LAST:event_ExitButtonActionPerformed

    
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindowHome().setVisible(true);
            }
        });
    }

              // Variables declaration - do not modify//GEN-BEGIN:variables
              private javax.swing.JButton ExitButton;
              private javax.swing.JButton LoginButton;
              private javax.swing.JLabel jLabel1;
              private javax.swing.JLabel jLabel3;
              private javax.swing.JLabel jLabel4;
              private javax.swing.JPanel jPanel1;
              // End of variables declaration//GEN-END:variables

}
