/*
 * The MIT License
 *
 * Copyright 2020 Michal Kaštan <github.com/BloodyBogan> & Ladislav Capalaj.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package efficiently.views;

import efficiently.config.Messages;
import efficiently.views.dashboard.Admin;
import efficiently.views.dashboard.Correspondent;
import efficiently.views.dashboard.Student;
import efficiently.views.menu.Signup;
import efficiently.views.menu.Login;
import efficiently.views.menu.Main;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Michal Kaštan <github.com/BloodyBogan> & Ladislav Capalaj
 */
public class MainLayout extends javax.swing.JFrame {    
    private final static GridBagLayout layout = new GridBagLayout();
    
    private static Main menuScreen;
    private static Signup signupScreen;
    private static Login loginScreen;
    
    private static Student studentDashboard;
    private static Correspondent correspondentDashboard;
    private static Admin adminDashboard;

    /**
     * Creates new form MainLayout
     */
    public MainLayout() {
        initComponents();
        
        menuScreen = new Main();
        signupScreen = new Signup();
        loginScreen = new Login();
        
        studentDashboard = new Student();
        correspondentDashboard = new Correspondent();
        adminDashboard = new Admin();
        
        DynamicPanel.setLayout(layout);
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx=0;
        constraints.gridy=0;
        
        DynamicPanel.add(menuScreen, constraints);
        DynamicPanel.add(loginScreen, constraints);
        DynamicPanel.add(signupScreen, constraints);
        
        DynamicPanel.add(studentDashboard, constraints);
        DynamicPanel.add(correspondentDashboard, constraints);
        DynamicPanel.add(adminDashboard, constraints);
        
        showMenuScreen();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DynamicPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("efficiently");
        setMaximumSize(new java.awt.Dimension(1280, 720));
        setMinimumSize(new java.awt.Dimension(1280, 720));
        setName("MainLayout"); // NOI18N
        setResizable(false);

        DynamicPanel.setMaximumSize(new java.awt.Dimension(1280, 720));
        DynamicPanel.setMinimumSize(new java.awt.Dimension(1280, 720));
        DynamicPanel.setName("Container"); // NOI18N
        DynamicPanel.setPreferredSize(new java.awt.Dimension(1280, 720));
        DynamicPanel.setLayout(new java.awt.GridBagLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(DynamicPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(DynamicPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            java.util.logging.Logger.getLogger(MainLayout.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainLayout().setVisible(true);
        });
    }
    
    public static JPanel getJPanel() {
        return DynamicPanel;
    }
    
    public static void showMenuScreen() {
        menuScreen.setVisible(true);
        
        loginScreen.setVisible(false);
        signupScreen.setVisible(false);
        studentDashboard.setVisible(false);
        correspondentDashboard.setVisible(false);
        adminDashboard.setVisible(false);
    }
    
    public static void showLoginScreen() {
        loginScreen.setVisible(true);
        
        signupScreen.setVisible(false);
        menuScreen.setVisible(false);
        studentDashboard.setVisible(false);
        correspondentDashboard.setVisible(false);
        adminDashboard.setVisible(false);
        
        Login.reset();
    }
        
    public static void showSignupScreen() {
        signupScreen.setVisible(true);
        
        loginScreen.setVisible(false);
        menuScreen.setVisible(false);
        studentDashboard.setVisible(false);
        correspondentDashboard.setVisible(false);
        adminDashboard.setVisible(false);
        
        Signup.reset();
    }
    
    public static void showStudentDashboard() {
        studentDashboard.setVisible(true);
        
        signupScreen.setVisible(false);
        loginScreen.setVisible(false);
        menuScreen.setVisible(false);
        correspondentDashboard.setVisible(false);
        adminDashboard.setVisible(false);
    }
    
    public static void showCorrespondentDashboard() {
        correspondentDashboard.setVisible(true);
        
        signupScreen.setVisible(false);
        loginScreen.setVisible(false);
        menuScreen.setVisible(false);
        studentDashboard.setVisible(false);
        adminDashboard.setVisible(false);
    }
    
    public static void showAdminDashboard() {
        adminDashboard.setVisible(true);
        
        signupScreen.setVisible(false);
        loginScreen.setVisible(false);
        menuScreen.setVisible(false);
        studentDashboard.setVisible(false);
        correspondentDashboard.setVisible(false);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JPanel DynamicPanel;
    // End of variables declaration//GEN-END:variables
}
