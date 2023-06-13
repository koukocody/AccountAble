/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Login;

import Client.Client;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import static com.plaid.InitializeFirestore.db;
import com.plaid.quickstart.QuickstartApplication;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

/**
 *
 * @author cody6
 */
public class LoginForm extends javax.swing.JFrame {

    public int visibilityCounter = 0;
    public int matchingUsername = 0;
    public String username = "";
    public String password = "";    
    
    /**
     * Creates new form LoginForm
     */
    public LoginForm() {
        initComponents();
        visibilityCounter = 0;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        accountableHeader = new javax.swing.JLabel();
        loginHeader = new javax.swing.JLabel();
        usernameLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        loginButton = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        registrationButton = new javax.swing.JLabel();
        usernameEntryField = new javax.swing.JTextField();
        passwordEntryField = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        setSize(new java.awt.Dimension(750, 650));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setMinimumSize(new java.awt.Dimension(750, 650));

        jLabel1.setBackground(new java.awt.Color(51, 255, 51));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(75, 75, 75));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("X");
        jLabel1.setFocusable(false);
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        accountableHeader.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        accountableHeader.setForeground(new java.awt.Color(75, 75, 75));
        accountableHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        accountableHeader.setText("AccountAble");
        accountableHeader.setFocusable(false);

        loginHeader.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        loginHeader.setForeground(new java.awt.Color(75, 75, 75));
        loginHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loginHeader.setText("Login");
        loginHeader.setFocusable(false);

        usernameLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        usernameLabel.setForeground(new java.awt.Color(75, 75, 75));
        usernameLabel.setText("Username");
        usernameLabel.setFocusable(false);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setIcon(new javax.swing.ImageIcon("C:\\Users\\cody6\\Documents\\NetBeansProjects\\AccountAble\\java\\src\\main\\java\\Login\\icons8_Account_40px.png"));

        passwordLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        passwordLabel.setForeground(new java.awt.Color(75, 75, 75));
        passwordLabel.setText("Password");
        passwordLabel.setFocusable(false);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setIcon(new javax.swing.ImageIcon("C:\\Users\\cody6\\Documents\\NetBeansProjects\\AccountAble\\java\\src\\main\\java\\Login\\icons8_Invisible_30px_1.png"));
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });

        loginButton.setBackground(new java.awt.Color(75, 75, 75));
        loginButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        loginButton.setForeground(new java.awt.Color(255, 255, 255));
        loginButton.setText("LOGIN");
        loginButton.setBorder(null);
        loginButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginButton.setFocusable(false);
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loginButtonMouseClicked(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(75, 75, 75));
        jLabel10.setText("First time user?");
        jLabel10.setFocusable(false);

        registrationButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        registrationButton.setForeground(new java.awt.Color(75, 75, 75));
        registrationButton.setText("Create an account");
        registrationButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        registrationButton.setFocusable(false);
        registrationButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                registrationButtonMouseClicked(evt);
            }
        });

        usernameEntryField.setBackground(new java.awt.Color(75, 75, 75));
        usernameEntryField.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        usernameEntryField.setForeground(new java.awt.Color(255, 255, 255));
        usernameEntryField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                usernameEntryFieldKeyTyped(evt);
            }
        });

        passwordEntryField.setBackground(new java.awt.Color(75, 75, 75));
        passwordEntryField.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        passwordEntryField.setForeground(new java.awt.Color(255, 255, 255));
        passwordEntryField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                passwordEntryFieldKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(loginHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(accountableHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(loginButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(usernameEntryField)
                            .addComponent(passwordEntryField)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(usernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(passwordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(50, 50, 50))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(266, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(registrationButton)
                .addGap(265, 265, 265))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accountableHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(loginHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(usernameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(usernameEntryField, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passwordLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(passwordEntryField, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(87, 87, 87)
                .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(registrationButton))
                .addContainerGap(79, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(750, 650));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        // Swaps between hidden and visible icons/text field
        visibilityCounter++;
        if (visibilityCounter % 2 == 0) {
            jLabel9.setIcon(new javax.swing.ImageIcon("C:\\Users\\cody6\\Documents\\NetBeansProjects\\AccountAble\\java\\src\\main\\java\\Login\\icons8_Invisible_30px_1.png"));
            passwordEntryField.setEchoChar('*');
        } else {
            jLabel9.setIcon(new javax.swing.ImageIcon("C:\\Users\\cody6\\Documents\\NetBeansProjects\\AccountAble\\java\\src\\main\\java\\Login\\icons8_Eye_30px.png"));
            passwordEntryField.setEchoChar((char)0);
        }
    }//GEN-LAST:event_jLabel9MouseClicked

    private void usernameEntryFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_usernameEntryFieldKeyTyped
        loginHeader.setText("Login");
        // Allow only alphanumeric char in username
        if (!(Character.isLetterOrDigit(evt.getKeyChar()))){
            evt.consume();
        }  
    }//GEN-LAST:event_usernameEntryFieldKeyTyped

    private void passwordEntryFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordEntryFieldKeyTyped
        loginHeader.setText("Login");
        // Disallow spaces in password
        if (Character.isSpaceChar(evt.getKeyChar())){
                evt.consume();
        }  
    }//GEN-LAST:event_passwordEntryFieldKeyTyped

    private void registrationButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_registrationButtonMouseClicked
        // Change to Registration form
        this.setVisible(false);
        new RegistrationForm().setVisible(true);
    }//GEN-LAST:event_registrationButtonMouseClicked

    private void loginButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginButtonMouseClicked
        try {
            String temp = "";
            username = usernameEntryField.getText().toLowerCase();
            password = String.valueOf(passwordEntryField.getPassword());
            matchingUsername = 0;
            ApiFuture<QuerySnapshot> future = db.collection("users").get();
            List<QueryDocumentSnapshot> users = future.get().getDocuments();
            for (QueryDocumentSnapshot user : users) {
                String userId = user.getId().toLowerCase();
                // Ends search if matching username in database
                if (username.equals(userId)){
                   matchingUsername = 1; 
                   temp = user.get("password").toString();
                   break;
                } 
            }           
            if (matchingUsername == 0) {
                loginHeader.setText("Error - please re-enter information and try again");
            }
            else if (matchingUsername == 1){
                if (password.equals(temp)){
                    QuickstartApplication.userID = usernameEntryField.getText();
                    JDialog jDialog = new JDialog();
                    jDialog.setLayout(new GridBagLayout());
                    JLabel label = new JLabel();
                    label.setIcon(new javax.swing.ImageIcon("C:\\Users\\cody6\\Documents\\NetBeansProjects\\AccountAble\\java\\src\\main\\java\\Login\\Spin-1.4s-175px.gif"));
                    jDialog.add(label);
                    jDialog.setMinimumSize(new Dimension(175, 175));
                    jDialog.setResizable(false);
                    jDialog.setModal(false);
                    jDialog.setUndecorated(true);
                    jDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                    jDialog.setLocationRelativeTo(null);
                    jDialog.setVisible(true);
                    SwingWorker worker = new SwingWorker(){
                        @Override
                        protected Void doInBackground() throws Exception {
                            new Client().setVisible(true);
                            jDialog.dispose();
                            return null;
                        }
                    };
                    worker.execute();
                    this.dispose();
                }
                else {
                    loginHeader.setText("Error - please re-enter information and try again");
                }      
            }   
        } catch (InterruptedException | ExecutionException  ex) {
            Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_loginButtonMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main() {
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
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel accountableHeader;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton loginButton;
    private javax.swing.JLabel loginHeader;
    private javax.swing.JPasswordField passwordEntryField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JLabel registrationButton;
    private javax.swing.JTextField usernameEntryField;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
}
