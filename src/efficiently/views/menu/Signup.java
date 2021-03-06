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
package efficiently.views.menu;

import efficiently.config.Messages;
import efficiently.controllers.MenuController;
import efficiently.utils.SignupValidation;
import efficiently.utils.ValidationException;
import efficiently.views.MainLayout;
import javax.swing.JOptionPane;

/**
 * <h1>Signup Class</h1>
 * Governs the signup screen view
 *
 * @author Michal Kaštan <github.com/BloodyBogan> & Ladislav Capalaj
 * @version 1.0.0
 * @since 2020-11-23
 */
public class Signup extends javax.swing.JPanel {
    /**
     * Creates new form Signup
     */
    public Signup() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        Container = new javax.swing.JPanel();
        title = new javax.swing.JLabel();
        aisIdLabel = new javax.swing.JLabel();
        aisIdField = new javax.swing.JTextField();
        nameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        confirmPasswordLabel = new javax.swing.JLabel();
        confirmPasswordField = new javax.swing.JPasswordField();
        submitButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(1280, 720));
        setMinimumSize(new java.awt.Dimension(1280, 720));
        setName("Signup"); // NOI18N

        Container.setMaximumSize(new java.awt.Dimension(1280, 720));
        Container.setMinimumSize(new java.awt.Dimension(1280, 720));
        Container.setName("Container"); // NOI18N
        Container.setPreferredSize(new java.awt.Dimension(1280, 720));
        Container.setLayout(new java.awt.GridBagLayout());

        title.setFont(new java.awt.Font("Open Sans", 1, 30)); // NOI18N
        title.setText("Sign Up");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 30, 0);
        Container.add(title, gridBagConstraints);

        aisIdLabel.setFont(new java.awt.Font("Open Sans", 0, 17)); // NOI18N
        aisIdLabel.setText("AIS ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        Container.add(aisIdLabel, gridBagConstraints);

        aisIdField.setFont(new java.awt.Font("Open Sans", 0, 17)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        Container.add(aisIdField, gridBagConstraints);

        nameLabel.setFont(new java.awt.Font("Open Sans", 0, 17)); // NOI18N
        nameLabel.setText("Your name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 3, 0);
        Container.add(nameLabel, gridBagConstraints);

        nameField.setFont(new java.awt.Font("Open Sans", 0, 17)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        Container.add(nameField, gridBagConstraints);

        passwordLabel.setFont(new java.awt.Font("Open Sans", 0, 17)); // NOI18N
        passwordLabel.setText("Your password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 3, 0);
        Container.add(passwordLabel, gridBagConstraints);

        passwordField.setFont(new java.awt.Font("Open Sans", 0, 17)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        Container.add(passwordField, gridBagConstraints);

        confirmPasswordLabel.setFont(new java.awt.Font("Open Sans", 0, 17)); // NOI18N
        confirmPasswordLabel.setText("Confirm password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 3, 0);
        Container.add(confirmPasswordLabel, gridBagConstraints);

        confirmPasswordField.setFont(new java.awt.Font("Open Sans", 0, 17)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        Container.add(confirmPasswordField, gridBagConstraints);

        submitButton.setFont(new java.awt.Font("Open Sans", 1, 17)); // NOI18N
        submitButton.setText("Submit");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(14, 0, 30, 0);
        Container.add(submitButton, gridBagConstraints);

        backButton.setFont(new java.awt.Font("Open Sans", 1, 17)); // NOI18N
        backButton.setText("Back to menu");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        Container.add(backButton, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Container, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Resets this view's elements and focuses on the first editable one
     */
    public static void reset() {
        aisIdField.setText("");
        nameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        
        aisIdField.requestFocus();
    }
    
    /**
     * Shows the main menu screen
     * 
     * @param evt unused
     */
    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        MainLayout.showMenuScreen();
    }//GEN-LAST:event_backButtonActionPerformed

    /**
     * Calls a method that validates user's input
     * If that's successful, signup method is called
     * 
     * @param evt unused
     */
    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        int aisId;
        String name;
        char[] password;
        
        try {
            Object[] values = SignupValidation.validate(aisIdField, nameField, passwordField, confirmPasswordField);
            
            aisId = (int) values[0];
            name = (String) values[1];
            password = (char[]) values[2];
        } catch (ValidationException ve) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), ve.getMessage(), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        }
  
        MenuController.signup(aisId, name, password);
    }//GEN-LAST:event_submitButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Container;
    private static javax.swing.JTextField aisIdField;
    private javax.swing.JLabel aisIdLabel;
    private javax.swing.JButton backButton;
    private static javax.swing.JPasswordField confirmPasswordField;
    private javax.swing.JLabel confirmPasswordLabel;
    private static javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private static javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JButton submitButton;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
