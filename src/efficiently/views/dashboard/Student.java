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
package efficiently.views.dashboard;

import efficiently.controllers.DashboardController;
import efficiently.models.User;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Michal Kaštan <github.com/BloodyBogan> & Ladislav Capalaj
 */
public class Student extends javax.swing.JPanel {

    /**
     * Creates new form Student
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public Student() throws SQLException, IOException {
        initComponents();
        datetimeList.setModel(new DefaultListModel());
        datetimeList.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        title = new javax.swing.JLabel();
        logoutButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        bookAppointmentPanel = new javax.swing.JPanel();
        subjectLabel = new javax.swing.JLabel();
        subjectField = new javax.swing.JTextField();
        messageLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        messageTextArea = new javax.swing.JTextArea();
        bookNowButton = new javax.swing.JButton();
        datetimeComboBox = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        datetimeScrollPane = new javax.swing.JScrollPane();
        datetimeList = new javax.swing.JList<>();
        appointmentsPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        appointmentsTable = new javax.swing.JTable();
        queuePanel = new javax.swing.JPanel();
        queueLabel = new javax.swing.JLabel();
        userNameLabel = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        setMaximumSize(new java.awt.Dimension(1280, 720));
        setMinimumSize(new java.awt.Dimension(1280, 720));
        setName("Student"); // NOI18N

        jPanel1.setMaximumSize(new java.awt.Dimension(1280, 720));
        jPanel1.setMinimumSize(new java.awt.Dimension(1280, 720));
        jPanel1.setName("Container"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(1280, 720));

        title.setFont(new java.awt.Font("Open Sans", 1, 35)); // NOI18N
        title.setText("Student Dashboard");

        logoutButton.setFont(new java.awt.Font("Open Sans", 1, 17)); // NOI18N
        logoutButton.setText("Log Out");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        refreshButton.setFont(new java.awt.Font("Open Sans", 1, 17)); // NOI18N
        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        bookAppointmentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Book Appointment", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Open Sans", 1, 24))); // NOI18N

        subjectLabel.setFont(new java.awt.Font("Open Sans", 0, 17)); // NOI18N
        subjectLabel.setText("Subject");

        subjectField.setFont(new java.awt.Font("Open Sans", 0, 17)); // NOI18N

        messageLabel.setFont(new java.awt.Font("Open Sans", 0, 17)); // NOI18N
        messageLabel.setText("Message");

        messageTextArea.setColumns(20);
        messageTextArea.setFont(new java.awt.Font("Open Sans", 0, 17)); // NOI18N
        messageTextArea.setLineWrap(true);
        messageTextArea.setRows(5);
        jScrollPane1.setViewportView(messageTextArea);

        bookNowButton.setFont(new java.awt.Font("Open Sans", 1, 17)); // NOI18N
        bookNowButton.setText("Book Now");
        bookNowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookNowButtonActionPerformed(evt);
            }
        });

        datetimeComboBox.setFont(new java.awt.Font("Open Sans", 0, 17)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Open Sans", 0, 17)); // NOI18N
        jLabel2.setText("Pick date & time");

        datetimeScrollPane.setEnabled(false);
        datetimeScrollPane.setFocusable(false);
        datetimeScrollPane.setMaximumSize(new java.awt.Dimension(0, 0));
        datetimeScrollPane.setMinimumSize(new java.awt.Dimension(0, 0));
        datetimeScrollPane.setPreferredSize(new java.awt.Dimension(0, 0));

        datetimeList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        datetimeList.setEnabled(false);
        datetimeList.setFocusable(false);
        datetimeList.setMaximumSize(new java.awt.Dimension(0, 0));
        datetimeList.setMinimumSize(new java.awt.Dimension(0, 0));
        datetimeScrollPane.setViewportView(datetimeList);

        javax.swing.GroupLayout bookAppointmentPanelLayout = new javax.swing.GroupLayout(bookAppointmentPanel);
        bookAppointmentPanel.setLayout(bookAppointmentPanelLayout);
        bookAppointmentPanelLayout.setHorizontalGroup(
            bookAppointmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bookAppointmentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bookAppointmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                    .addComponent(subjectField)
                    .addComponent(datetimeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(bookAppointmentPanelLayout.createSequentialGroup()
                        .addGroup(bookAppointmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(subjectLabel)
                            .addComponent(messageLabel)
                            .addComponent(jLabel2)
                            .addComponent(bookNowButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(datetimeScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        bookAppointmentPanelLayout.setVerticalGroup(
            bookAppointmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bookAppointmentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(subjectLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subjectField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(messageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(datetimeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(bookAppointmentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bookNowButton)
                    .addComponent(datetimeScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        appointmentsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Your Appointments", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Open Sans", 1, 24))); // NOI18N

        appointmentsTable.setFont(new java.awt.Font("Open Sans", 0, 17)); // NOI18N
        appointmentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Subject", "Message", "Response", "Date", "Correspondent", "Done"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        appointmentsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                appointmentsTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(appointmentsTable);

        javax.swing.GroupLayout appointmentsPanelLayout = new javax.swing.GroupLayout(appointmentsPanel);
        appointmentsPanel.setLayout(appointmentsPanelLayout);
        appointmentsPanelLayout.setHorizontalGroup(
            appointmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, appointmentsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
                .addContainerGap())
        );
        appointmentsPanelLayout.setVerticalGroup(
            appointmentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(appointmentsPanelLayout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        queuePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Queue", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Open Sans", 1, 24))); // NOI18N
        queuePanel.setMaximumSize(new java.awt.Dimension(121, 64));
        queuePanel.setLayout(new java.awt.GridBagLayout());

        queueLabel.setFont(new java.awt.Font("Open Sans", 1, 17)); // NOI18N
        queuePanel.add(queueLabel, new java.awt.GridBagConstraints());

        userNameLabel.setFont(new java.awt.Font("Open Sans", 0, 14)); // NOI18N
        userNameLabel.setEnabled(false);
        userNameLabel.setFocusable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(bookAppointmentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(appointmentsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(refreshButton)
                        .addGap(25, 25, 25)
                        .addComponent(logoutButton))
                    .addComponent(queuePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(472, 472, 472)
                        .addComponent(title))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(613, 613, 613)
                        .addComponent(userNameLabel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(title)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userNameLabel)
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(queuePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(refreshButton)
                            .addComponent(logoutButton)))
                    .addComponent(bookAppointmentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(appointmentsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    public static void refresh() throws SQLException, IOException {
        try {
            DashboardController.updateStudentAppointmentsTable(appointmentsTable);
            appointmentsTable.clearSelection();
            DashboardController.handleStudentAppointmentsDatesUpdate(datetimeComboBox, datetimeList);
            DashboardController.handleStudentQueueUpdate(queueLabel);
            resetRest();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void resetTable() {
        appointmentsTable.clearSelection();
        DefaultTableModel model = (DefaultTableModel) appointmentsTable.getModel();
        model.setRowCount(0);
        appointmentsTable.revalidate();
    }
    
    private static void resetRest() {
        subjectField.setText("");
            messageTextArea.setText("");
            datetimeComboBox.setSelectedIndex(0);
            
            subjectField.requestFocus();
    }
    
    public static void setUserName() {
        userNameLabel.setText("Hello, " + User.getName());
    }
    
    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        DashboardController.logout();
        resetTable();
        resetRest();
    }//GEN-LAST:event_logoutButtonActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        try {
            refresh();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void bookNowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookNowButtonActionPerformed
        try {
            DashboardController.bookAppointment(subjectField, messageTextArea, datetimeComboBox, datetimeList);
            refresh();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Student.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bookNowButtonActionPerformed

    private void appointmentsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_appointmentsTableMouseClicked
        DashboardController.handleStudentAppointmentsTableRowClick(appointmentsTable);
    }//GEN-LAST:event_appointmentsTableMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel appointmentsPanel;
    private static javax.swing.JTable appointmentsTable;
    private javax.swing.JPanel bookAppointmentPanel;
    private javax.swing.JButton bookNowButton;
    private static javax.swing.JComboBox<String> datetimeComboBox;
    private static javax.swing.JList<String> datetimeList;
    private javax.swing.JScrollPane datetimeScrollPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton logoutButton;
    private javax.swing.JLabel messageLabel;
    private static javax.swing.JTextArea messageTextArea;
    private static javax.swing.JLabel queueLabel;
    private javax.swing.JPanel queuePanel;
    private javax.swing.JButton refreshButton;
    private static javax.swing.JTextField subjectField;
    private javax.swing.JLabel subjectLabel;
    private javax.swing.JLabel title;
    private static javax.swing.JLabel userNameLabel;
    // End of variables declaration//GEN-END:variables
}
