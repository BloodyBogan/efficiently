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
package efficiently.controllers;

import efficiently.config.Database;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.IOException;
import efficiently.models.User;
import efficiently.views.MainLayout;
import java.util.Vector;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Michal Kaštan <github.com/BloodyBogan> & Ladislav Capalaj
 */
public class DashboardController {
    public static void logout() {
        User.logout();
        MainLayout.showMenuScreen();
    }
    
    public static void bookAppointment(JTextField subjectField, JTextArea messageTextArea, JComboBox<String> datetimeComboBox) {        
        String sqlQuery = "INSERT INTO appointments (user, subject, message, date) VALUES (?, ?, ?, ?)";
        
        int userId = User.getUserId();
        String subject = subjectField.getText();
        String message = messageTextArea.getText();
        String datetime =  datetimeComboBox.getItemAt(datetimeComboBox.getSelectedIndex());
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, subject);
            pstmt.setString(3, message);
            pstmt.setString(4, datetime);
            
            pstmt.execute();
            
            subjectField.setText("");
            messageTextArea.setText("");
            datetimeComboBox.setSelectedIndex(0);
            
            subjectField.requestFocus();
            
            JOptionPane.showMessageDialog(null, "Appointment booking successful");
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
    }
    
    public static void updateStudentAppointmentsTable(JTable appointmentsTable) throws SQLException, IOException {
        String sqlQuery = "SELECT subject, message, response, date, isDone FROM appointments WHERE user=? ORDER BY date ASC";
        
        int userId = User.getUserId();
        
        int c;
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            
            pstmt.setInt(1, userId);
            
            ResultSet rs = pstmt.executeQuery();
            
            ResultSetMetaData rsmd = rs.getMetaData();
            c = rsmd.getColumnCount();
            
            DefaultTableModel Df = (DefaultTableModel)appointmentsTable.getModel();
            Df.setRowCount(0);
            
            while (rs.next()) {
                Vector v2 = new Vector();
                
                for (int i = 1; i <= c; i++) {                    
                    v2.add(rs.getString("subject"));
                    v2.add(rs.getString("message"));
                    
                    if (rs.getString("response") == null) {
                        v2.add("No response yet");
                    } else {
                        v2.add(rs.getString("response"));
                    }
                    
                    v2.add(rs.getString("date"));
                    
                    if (rs.getInt("isDone") == 0) {
                        v2.add("No");
                    } else {
                        v2.add("Yes");
                    }
                }
                
                Df.addRow(v2);
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
    }
    
    public static void handleStudentAppointmentsTableRowClick(JTable appointmentsTable) {
        DefaultTableModel Df = (DefaultTableModel)appointmentsTable.getModel();
        int selectedIndex = appointmentsTable.getSelectedRow();
        
        String subject = Df.getValueAt(selectedIndex, 0).toString();
        String message = Df.getValueAt(selectedIndex, 1).toString();
        String response = Df.getValueAt(selectedIndex, 2).toString();
        String date = Df.getValueAt(selectedIndex, 3).toString();
        String done = Df.getValueAt(selectedIndex, 4).toString();
        
        JOptionPane.showMessageDialog(null, "<html><body><div style='width: 450px;'><p>Subject: " + subject + "</p><br><p>Message: " + message + "</p><br><p>Response: " + response + "</p><br><p>Date: " + date + "</p><br><p>Done: " + done + "</p></div></body></html>", "Appointment Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void updateCorrespondentAppointmentsTable(JTable appointmentsTable) throws SQLException, IOException {
        String sqlQuery = "SELECT appointments.appointment_id, appointments.subject, appointments.message, appointments.response, appointments.date, appointments.isDone, users.ais_id, users.name FROM appointments, users WHERE appointments.user=users.user_id ORDER BY appointments.date ASC";
        
        int c;
        
        try (Connection conn = Database.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sqlQuery)) {
            
            ResultSetMetaData rsmd = rs.getMetaData();
            c = rsmd.getColumnCount();
            
            DefaultTableModel Df = (DefaultTableModel)appointmentsTable.getModel();
            Df.setRowCount(0);
            
            while (rs.next()) {
                Vector v2 = new Vector();
                
                for (int i = 1; i <= c; i++) {  
                    v2.add(rs.getInt("appointment_id"));
                    v2.add(rs.getString("name"));
                    v2.add(rs.getInt("ais_id"));
                    v2.add(rs.getString("subject"));
                    v2.add(rs.getString("message"));
                    
                    if (rs.getString("response") == null) {
                        v2.add("No response yet");
                    } else {
                        v2.add(rs.getString("response"));
                    }
                    
                    v2.add(rs.getString("date"));
                    
                    if (rs.getInt("isDone") == 0) {
                        v2.add("No");
                    } else {
                        v2.add("Yes");
                    }
                }
                
                Df.addRow(v2);
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
    }
    
    public static void handleCorrespondentTableRowClick(JTable appointmentsTable, JTextField nameField, JTextField aisIdField, JTextField subjectField, JTextArea messageTextArea, JTextArea responseTextArea, JLabel datetimeLabel, JCheckBox doneCheckBox, JTextArea manageResponseTextArea, JCheckBox manageDoneCheckBox) {
        DefaultTableModel Df = (DefaultTableModel)appointmentsTable.getModel();
        int selectedIndex = appointmentsTable.getSelectedRow();
        
        boolean isSelected;
        switch (Df.getValueAt(selectedIndex, 7).toString()) {
            case "Yes":
                isSelected = true;
                break;
            case "No":
                isSelected = false;
                break;
            default:
                isSelected = false;
        }
        
        String response = Df.getValueAt(selectedIndex, 5).toString();
        
        nameField.setText(Df.getValueAt(selectedIndex, 1).toString());
        aisIdField.setText(Df.getValueAt(selectedIndex, 2).toString());
        subjectField.setText(Df.getValueAt(selectedIndex, 3).toString());
        messageTextArea.setText(Df.getValueAt(selectedIndex, 4).toString());
        responseTextArea.setText(response);
        datetimeLabel.setText(Df.getValueAt(selectedIndex, 6).toString());
        doneCheckBox.setSelected(isSelected);
        
        String manageResponse;
        switch (response) {
            case "No response yet":
                manageResponse = "";
                break;
            default:
                manageResponse = response;
        }
        
        manageResponseTextArea.setText(manageResponse);
        manageDoneCheckBox.setSelected(isSelected);
    }
    
    public static void handleCorrespondentAppointmentUpdate(JTable appointmentsTable, JTextArea responseTextArea, JCheckBox doneCheckBox, JTextArea manageResponseTextArea, JCheckBox manageDoneCheckBox) throws SQLException, IOException {
        int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to update this appointment?", "Update Appointment", JOptionPane.YES_NO_OPTION);
        if (option != 0) {
           return;
        }
        
        String sqlQuery = "UPDATE appointments SET response=?, isDone=? WHERE appointment_id=?";

        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            DefaultTableModel Df = (DefaultTableModel)appointmentsTable.getModel();
            int selectedIndex = appointmentsTable.getSelectedRow();

            int appointmentId = (int) Df.getValueAt(selectedIndex, 0);
            
            boolean isSelected = manageDoneCheckBox.isSelected();
            int isDone;
            if (isSelected) {
                isDone = 1;
            } else {
                isDone = 0;
            }
            
            String response = manageResponseTextArea.getText();
            
            pstmt.setString(1, response);
            pstmt.setInt(2, isDone);
            pstmt.setInt(3, appointmentId);

            pstmt.executeUpdate();
            
            responseTextArea.setText(response);
            doneCheckBox.setSelected(isSelected);
            
            manageResponseTextArea.requestFocus();

            JOptionPane.showMessageDialog(null, "User successfully updated");
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
    }
    
    public static void handleCorrespondentAppointmentDelete(JTable appointmentsTable, JTabbedPane manageTabbedPane, JTextField nameField, JTextField aisIdField, JTextField subjectField, JTextArea messageTextArea, JTextArea responseTextArea, JLabel datetimeLabel, JCheckBox doneCheckBox, JTextArea manageResponseTextArea, JCheckBox manageDoneCheckBox) throws SQLException, IOException {
        int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this appointment?", "Delete Appointment", JOptionPane.YES_NO_OPTION);
        if (option != 0) {
           return;
        }
        
        String sqlQuery = "DELETE FROM appointments WHERE appointment_id=?";
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            
            DefaultTableModel Df = (DefaultTableModel)appointmentsTable.getModel();
            int selectedIndex = appointmentsTable.getSelectedRow();

            int appointmentId = (int) Df.getValueAt(selectedIndex, 0);

            pstmt.setInt(1, appointmentId);
            
            pstmt.execute();
            
            manageResponseTextArea.setText("");
            manageDoneCheckBox.setSelected(false);
            
            nameField.setText("");
            aisIdField.setText("");
            subjectField.setText("");
            messageTextArea.setText("");
            responseTextArea.setText("");
            datetimeLabel.setText("");
            doneCheckBox.setSelected(false);
        
            manageTabbedPane.setSelectedIndex(0);
            
            JOptionPane.showMessageDialog(null, "Appointment deleted successfully");
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
    }
    
    public static void updateAdminTable(JTable usersTable) throws SQLException, IOException {
        String sqlQuery = "SELECT users.ais_id, users.name, user_role.role FROM users, user_role WHERE users.role=user_role.role_id";
        
        int c;
        
        try (Connection conn = Database.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sqlQuery)) {
           
            ResultSetMetaData rsmd = rs.getMetaData();
            c = rsmd.getColumnCount();
            
            DefaultTableModel Df = (DefaultTableModel)usersTable.getModel();
            Df.setRowCount(0);
            
            while (rs.next()) {
                Vector v2 = new Vector();
                
                for (int i = 1; i <= c; i++) {                    
                    v2.add(rs.getInt("ais_id"));
                    v2.add(rs.getString("name"));
                    v2.add(rs.getString("role"));
                }
                
                Df.addRow(v2);
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
    }
    
    public static void handleAdminTableRowClick(JTable usersTable, JTextField aisIdField, JTextField nameField, JComboBox<String> roleComboBox) {
        DefaultTableModel Df = (DefaultTableModel)usersTable.getModel();
        int selectedIndex = usersTable.getSelectedRow();
        
        aisIdField.setText(Df.getValueAt(selectedIndex, 0).toString());
        nameField.setText(Df.getValueAt(selectedIndex, 1).toString());
        roleComboBox.setSelectedItem(Df.getValueAt(selectedIndex, 2).toString());
    }
    
    public static void handleAdminUserUpdate(JTable usersTable, JTextField aisIdField, JTextField nameField, JComboBox<String> roleComboBox) throws SQLException, IOException {
        int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to update this user?", "Update User", JOptionPane.YES_NO_OPTION);
        if (option != 0) {
           return;
        }
        
        String sqlQuery = "UPDATE users SET ais_id=?, name=?, role=? WHERE ais_id=?";

        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            String stringAisId = aisIdField.getText();
            int aisId;
            try {
                aisId = Integer.parseInt(stringAisId);
            } catch(NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "AIS ID must be a number");
                aisIdField.setText("");
                aisIdField.requestFocus();
                return;
            }
            
            int role;
            String roleFromForm = roleComboBox.getItemAt(roleComboBox.getSelectedIndex());
            switch (roleFromForm) {
                case "student":
                    role = 1;
                    break;
                case "referent":
                    role = 2;
                    break;
                case "admin":
                    role = 3;
                    break;
                default:
                    role = 1;
            }
            
            String name = nameField.getText();
            
            pstmt.setInt(1, aisId);
            pstmt.setString(2, name);
            pstmt.setInt(3, role);
            pstmt.setInt(4, aisId);

            pstmt.executeUpdate();
            DashboardController.updateAdminTable(usersTable);
            
            aisIdField.setText("");
            nameField.setText("");
            roleComboBox.setSelectedItem("student");
            
            aisIdField.requestFocus();

            JOptionPane.showMessageDialog(null, "User successfully updated");
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
    }
    
    public static void handleAdminUserDelete(JTable usersTable, JTextField aisIdField, JTextField nameField, JComboBox<String> roleComboBox) throws SQLException, IOException {
        int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this user?", "Delete User", JOptionPane.YES_NO_OPTION);
        if (option != 0) {
           return;
        }
        
        String sqlQuery = "DELETE FROM users WHERE ais_id=?";
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            String stringAisId = aisIdField.getText();
            int aisId;
            try {
                aisId = Integer.parseInt(stringAisId);
            } catch(NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "AIS ID must be a number");
                aisIdField.setText("");
                aisIdField.requestFocus();
                return;
            }
            
            pstmt.setInt(1, aisId);
            
            pstmt.execute();
            
            DashboardController.updateAdminTable(usersTable);
            
            aisIdField.setText("");
            nameField.setText("");
            roleComboBox.setSelectedItem("student");
            
            aisIdField.requestFocus();
            
            JOptionPane.showMessageDialog(null, "User deleted successfully");
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
    }
}
