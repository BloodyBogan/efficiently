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
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
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
    
    public static void updateAppointmentsTable(JTable appointmentsTable) throws SQLException, IOException {
        String sqlQuery = "SELECT subject, message, response, date, isDone FROM appointments WHERE user=? ORDER BY date DESC";
        
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
    
    public static void handleAppointmentsTableRowClick(JTable appointmentsTable) {
        DefaultTableModel Df = (DefaultTableModel)appointmentsTable.getModel();
        int selectedIndex = appointmentsTable.getSelectedRow();
        
        String subject = Df.getValueAt(selectedIndex, 0).toString();
        String message = Df.getValueAt(selectedIndex, 1).toString();
        String response = Df.getValueAt(selectedIndex, 2).toString();
        String date = Df.getValueAt(selectedIndex, 3).toString();
        String done = Df.getValueAt(selectedIndex, 4).toString();
        
        JOptionPane.showMessageDialog(null, "<html><body><div style='width: 450px;'><p>Subject: " + subject + "</p><br><p>Message: " + message + "</p><br><p>Response: " + response + "</p><br><p>Date: " + date + "</p><br><p>Done: " + done + "</p></div></body></html>", "Appointment Information", JOptionPane.INFORMATION_MESSAGE);
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
