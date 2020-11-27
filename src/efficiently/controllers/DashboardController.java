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

import com.github.lgooddatepicker.components.DateTimePicker;
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
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
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
    
    public static void bookAppointment(JTextField subjectField, JTextArea messageTextArea, JComboBox<String> datetimeComboBox, JList<String> datetimeList) {   
        String comboBoxItem = datetimeComboBox.getSelectedItem().toString();
        if (comboBoxItem.equals("There are no available dates & times")) {
            JOptionPane.showMessageDialog(null, "You can't book an appointment now as there are no available dates & times");
            return;
        }
        
        int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to book this appointment?", "Book Appointment", JOptionPane.YES_NO_OPTION);
        if (option != 0) {
           return;
        }
        
        String appointmentSqlQuery = "INSERT INTO appointments (user, subject, message, date) VALUES (?, ?, ?, ?)";
        
        int userId = User.getUserId();
        String subject = subjectField.getText();
        String message = messageTextArea.getText();
        
        int comboBoxIndex = datetimeComboBox.getSelectedIndex();
            
        DefaultListModel model = (DefaultListModel)datetimeList.getModel();

        int dateId = Integer.parseInt((String) model.get(comboBoxIndex));
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(appointmentSqlQuery)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, subject);
            pstmt.setString(3, message);
            pstmt.setInt(4, dateId);
            
            pstmt.execute();
            
            JOptionPane.showMessageDialog(null, "Appointment booking successful");
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
        
        String dateSqlQuery = "UPDATE dates SET isTaken=? WHERE date_id=?";

        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(dateSqlQuery)) {
            
            pstmt.setInt(1, 1);
            pstmt.setInt(2, dateId);

            pstmt.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
    }
    
    public static void updateStudentAppointmentsTable(JTable appointmentsTable) throws SQLException, IOException {
        String sqlQuery = "SELECT appointments.subject, appointments.message, appointments.response, dates.date, users.name, appointments.isDone FROM appointments, dates, users WHERE (appointments.user=? AND appointments.date=dates.date_id AND users.user_id=dates.user) ORDER BY date ASC";
        
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
                    v2.add(rs.getString("name"));
                    
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
        } catch (Exception e) {
            e.printStackTrace();
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
        String name = Df.getValueAt(selectedIndex, 4).toString();
        String done = Df.getValueAt(selectedIndex, 5).toString();
        
        JOptionPane.showMessageDialog(null, "<html><body><div style='width: 450px;'><p>Subject: " + subject + "</p><br><p>Message: " + message + "</p><br><p>Response: " + response + "</p><br><p>Date: " + date + "</p><br><p>Correspondent: " + name + "</p><br><p>Done: " + done + "</p></div></body></html>", "Appointment Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void handleStudentAppointmentsDatesUpdate(JComboBox<String> datetimeComboBox, JList<String> datetimeList) {
        String sqlQuery = "SELECT dates.date_id, dates.date, users.name FROM dates, users WHERE (dates.date>=NOW() AND dates.isTaken=0 AND dates.user=users.user_id) ORDER BY dates.date ASC";
        
        DefaultListModel model = (DefaultListModel)datetimeList.getModel();
        
        datetimeComboBox.removeAllItems();
        model.removeAllElements();
        
        try (Connection conn = Database.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sqlQuery)) {

            if (rs.next() == false) {
                datetimeComboBox.addItem("There are no available dates & times");
            } else {
                do {
                    datetimeComboBox.addItem(rs.getString("date") + " - " + rs.getString("name"));
                    model.addElement(String.valueOf(rs.getInt("date_id")));
                } while(rs.next());
            }
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
    }
    
    public static void handleStudentQueueUpdate(JLabel queueLabel) throws SQLException, IOException {
        String usersAppointmentQuery = "SELECT dates.date_id, dates.user FROM dates, appointments WHERE (dates.isTaken<>0 AND dates.date >= NOW() AND dates.date_id=appointments.date AND appointments.user=? AND appointments.isDone=0) ORDER BY dates.date ASC LIMIT 1";
        
        int dateId = 0;
        int correspondentUserId = 0;
        int userId = User.getUserId();
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(usersAppointmentQuery)) {
            
            pstmt.setInt(1, userId);
            
            ResultSet rs = pstmt.executeQuery();
           
            if (rs.next() == false) {
                queueLabel.setText("<html><body><p style='width: 115px; text-align: center;'>You have no appointments booked yet</p></body></html>");
                return;
            } else {
                do {
                    dateId = rs.getInt("date_id");
                    correspondentUserId = rs.getInt("user");
                } while (rs.next());
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        String sqlCountQuery = "SELECT COUNT(*) as total FROM dates, appointments WHERE (dates.isTaken<>0 AND dates.date>=NOW() AND dates.date<=(SELECT date FROM dates WHERE date_id=?) AND dates.user=? AND appointments.date=dates.date_id AND appointments.user<>? AND appointments.isDone=0)";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlCountQuery)) {
            
            pstmt.setInt(1, dateId);
            pstmt.setInt(2, correspondentUserId);
            pstmt.setInt(3, userId);
            
            ResultSet rs = pstmt.executeQuery();
           
            int count = 0;
            while(rs.next()) {
                count = rs.getInt("total");
            }
            
            if (count == 0) {
                queueLabel.setText("<html><body><p style='width: 115px; text-align: center;'>It's your turn</p></body></html>");
            } else if (count == 1) {
                queueLabel.setText("<html><body><p style='width: 115px; text-align: center;'>There is " + String.valueOf(count) + " appointment before yours</p></body></html>");
            } else {
                queueLabel.setText("<html><body><p style='width: 115px; text-align: center;'>There are " + String.valueOf(count) + " appointments before yours</p></body></html>");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    } 
    
    public static void updateCorrespondentAppointmentsTable(JTable appointmentsTable) throws SQLException, IOException {
        String sqlQuery = "SELECT appointments.appointment_id, appointments.subject, appointments.message, appointments.response, dates.date, appointments.isDone, users.ais_id, users.name FROM appointments, dates, users WHERE (dates.user=? AND appointments.date=dates.date_id AND appointments.user=users.user_id) ORDER BY dates.date ASC";
        
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
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
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
            
            String response = manageResponseTextArea.getText().trim();
            if (response.isEmpty()) {
                response = null;
            }
            
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
        
        String sqlDeleteQuery = "DELETE FROM appointments WHERE appointment_id=?";
        
        DefaultTableModel Df = (DefaultTableModel)appointmentsTable.getModel();
        int selectedIndex = appointmentsTable.getSelectedRow();

        int appointmentId = (int) Df.getValueAt(selectedIndex, 0);
        
        String sqlUpdateQuery = "UPDATE dates SET isTaken=0 WHERE (date_id=(SELECT date FROM appointments WHERE appointment_id=?) AND date>=NOW())";
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlUpdateQuery)) {
            
            pstmt.setInt(1, appointmentId);
            
            pstmt.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlDeleteQuery)) {

            pstmt.setInt(1, appointmentId);
            
            pstmt.execute();
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }

        JOptionPane.showMessageDialog(null, "Appointment deleted successfully");
    }
    
    public static void handleCorrespondentDatetimeAdd(DateTimePicker addDateTimePicker) {
        String sqlQuery = "INSERT INTO dates (user, date) VALUES (?, ?)";
        
        int userId = User.getUserId();
        
        String date = addDateTimePicker.getDatePicker().toString();
        String time = addDateTimePicker.getTimePicker().toString();
        String datetime = date + " " + time;

        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, datetime);
            
            pstmt.execute();
            
            JOptionPane.showMessageDialog(null, "Date & time added successfully");
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
    }
    
    public static void handleCorrespondentDatetimeUpdate(JComboBox<String> deleteDateTimeComboBox, JList<String> deleteDateTimeList) {
        String sqlQuery = "SELECT * FROM dates WHERE user=? ORDER BY date ASC";
        
        DefaultListModel model = (DefaultListModel)deleteDateTimeList.getModel();
        
        deleteDateTimeComboBox.removeAllItems();
        model.removeAllElements();

        int userId = User.getUserId();
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next() == false) {
                deleteDateTimeComboBox.addItem("You don't have any available dates & times");
            } else {
                do {
                    deleteDateTimeComboBox.addItem(rs.getString("date"));
                    model.addElement(String.valueOf(rs.getInt("date_id")));
                } while (rs.next());
            }
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
    }
    
    public static void handleCorrespondentDatetimeDelete(JComboBox<String> deleteDateTimeComboBox, JList<String> deleteDateTimeList) {
        int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this date & time?", "Delete Date & Time", JOptionPane.YES_NO_OPTION);
        if (option != 0) {
           return;
        }
        
        String sqlDeleteAppointmentQuery = "DELETE FROM appointments WHERE date=?";
        
        int comboBoxIndex = deleteDateTimeComboBox.getSelectedIndex();
            
        DefaultListModel model = (DefaultListModel)deleteDateTimeList.getModel();

        int dateId = Integer.parseInt((String) model.get(comboBoxIndex));
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlDeleteAppointmentQuery)) {

            pstmt.setInt(1, dateId);
            
            pstmt.execute();
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
        
        String sqlDeleteDateQuery = "DELETE FROM dates WHERE date_id=?";
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlDeleteDateQuery)) {

            pstmt.setInt(1, dateId);
            
            pstmt.execute();
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
            
        JOptionPane.showMessageDialog(null, "Date & time deleted successfully");
    }
    
    public static void updateAdminTable(JTable usersTable) throws SQLException, IOException {
        String sqlQuery = "SELECT users.user_id, users.ais_id, users.name, user_role.role FROM users, user_role WHERE (users.user_id<>? AND users.role=user_role.role_id) ORDER BY users.ais_id ASC";
        
        int userId = User.getUserId();
        
        int c;
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            
            pstmt.setInt(1, userId);
            
            ResultSet rs = pstmt.executeQuery();
           
            ResultSetMetaData rsmd = rs.getMetaData();
            c = rsmd.getColumnCount();
            
            DefaultTableModel Df = (DefaultTableModel)usersTable.getModel();
            Df.setRowCount(0);
            
            while (rs.next()) {
                Vector v2 = new Vector();
                
                for (int i = 1; i <= c; i++) { 
                    v2.add(rs.getInt("user_id"));
                    v2.add(rs.getInt("ais_id"));
                    v2.add(rs.getString("name"));
                    v2.add(rs.getString("role"));
                }
                
                Df.addRow(v2);
            }
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
    }
    
    public static void handleAdminTableRowClick(JTable usersTable, JTextField idField, JTextField aisIdField, JTextField nameField, JComboBox<String> roleComboBox) {
        DefaultTableModel Df = (DefaultTableModel)usersTable.getModel();
        int selectedIndex = usersTable.getSelectedRow();
        
        idField.setText(Df.getValueAt(selectedIndex, 0).toString());
        aisIdField.setText(Df.getValueAt(selectedIndex, 1).toString());
        nameField.setText(Df.getValueAt(selectedIndex, 2).toString());
        roleComboBox.setSelectedItem(Df.getValueAt(selectedIndex, 3).toString());
    }
    
    public static void handleAdminUserUpdate(JTable usersTable, JTextField idField, JTextField aisIdField, JTextField nameField, JComboBox<String> roleComboBox) throws SQLException, IOException {
        int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to update this user?", "Update User", JOptionPane.YES_NO_OPTION);
        if (option != 0) {
           return;
        }
        
        String sqlQuery = "UPDATE users SET ais_id=?, name=?, role=? WHERE user_id=?";

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
            String userRole = roleComboBox.getItemAt(roleComboBox.getSelectedIndex());
            switch (userRole) {
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
            int userId = Integer.parseInt(idField.getText());
            
            pstmt.setInt(1, aisId);
            pstmt.setString(2, name);
            pstmt.setInt(3, role);
            pstmt.setInt(4, userId);

            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "User successfully updated");
        } catch (SQLIntegrityConstraintViolationException sicve) {
            JOptionPane.showMessageDialog(null, "AIS ID (" + aisIdField.getText() + ") is already in use");
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
    }
    
    public static void handleAdminStudentDelete(int userId) {
        String sqlSelectQuery = "SELECT appointments.date FROM appointments, dates WHERE (appointments.user=? AND dates.date_id=appointments.date AND dates.date >= NOW())";
        
        List<Integer> datesList = new ArrayList<Integer>(); 
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlSelectQuery)) {
            pstmt.setInt(1, userId);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next() != false) {
                do {
                    datesList.add(rs.getInt("date"));
                } while (rs.next());
            }
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
        
        if (!datesList.isEmpty()) {
            Integer[] appointmentsArray = datesList.toArray(new Integer[0]);
            
            StringBuilder sqlDeleteAppointmentsQuery = new StringBuilder(1024);
            sqlDeleteAppointmentsQuery.append("UPDATE dates SET isTaken=0 WHERE date_id IN (");
            
            for(int i=0; i < appointmentsArray.length; i++) {
                if(i > 0) {
                    sqlDeleteAppointmentsQuery.append(",");
                }
                sqlDeleteAppointmentsQuery.append(" ?");
            }
            sqlDeleteAppointmentsQuery.append(")");
            
            try (Connection conn = Database.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(sqlDeleteAppointmentsQuery.toString())) {

                for( int i=0; i < appointmentsArray.length; i++ ) {
                    pstmt.setInt(i + 1, appointmentsArray[i]);
                }

                pstmt.executeUpdate();
            } catch (SQLException se) {
                se.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database Error. Try again");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "There was an error. Try again");
            }
        }
        
        String sqlDeleteDatesQuery = "DELETE FROM appointments WHERE user=?";
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlDeleteDatesQuery)) {
            pstmt.setInt(1, userId);
            
            pstmt.execute();
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
    }
    
    public static void handleAdminCorrespondentDelete(int userId) {
        String sqlSelectQuery = "SELECT date_id FROM dates WHERE (user=? AND isTaken<>0)";
        
        List<Integer> appointmentsList = new ArrayList<Integer>(); 
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlSelectQuery)) {
            pstmt.setInt(1, userId);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next() != false) {
                do {
                    appointmentsList.add(rs.getInt("date_id"));
                } while (rs.next());
            }
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
        
        if (!appointmentsList.isEmpty()) {
            Integer[] appointmentsArray = appointmentsList.toArray(new Integer[0]);
            
            StringBuilder sqlDeleteAppointmentsQuery = new StringBuilder(1024);
            sqlDeleteAppointmentsQuery.append("DELETE FROM appointments WHERE date IN (");
            
            for(int i=0; i < appointmentsArray.length; i++) {
                if(i > 0) {
                    sqlDeleteAppointmentsQuery.append(",");
                }
                sqlDeleteAppointmentsQuery.append(" ?");
            }
            sqlDeleteAppointmentsQuery.append(")");
            
            try (Connection conn = Database.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(sqlDeleteAppointmentsQuery.toString())) {

                for( int i=0; i < appointmentsArray.length; i++ ) {
                    pstmt.setInt(i + 1, appointmentsArray[i]);
                }

                pstmt.execute();
            } catch (SQLException se) {
                se.printStackTrace();
                JOptionPane.showMessageDialog(null, "Database Error. Try again");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "There was an error. Try again");
            }
        }
        
        String sqlDeleteDatesQuery = "DELETE FROM dates WHERE user=?";
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlDeleteDatesQuery)) {
            pstmt.setInt(1, userId);
            
            pstmt.execute();
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
    }
    
    public static void handleAdminUserDelete(JTable usersTable, JTextField idField, JTextField aisIdField, JTextField nameField, JComboBox<String> roleComboBox) {
        int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this user?", "Delete User", JOptionPane.YES_NO_OPTION);
        if (option != 0) {
           return;
        }
        
        int userId = Integer.parseInt(idField.getText());
        
        String userRole = roleComboBox.getItemAt(roleComboBox.getSelectedIndex());
        switch (userRole) {
            case "student":
                handleAdminStudentDelete(userId);
                break;
            case "referent":
                handleAdminCorrespondentDelete(userId);
                break;
        }
        
        String sqlQuery = "DELETE FROM users WHERE user_id=?";
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            
            pstmt.setInt(1, userId);
            
            pstmt.execute();

            idField.setText("");
            aisIdField.setText("");
            nameField.setText("");
            roleComboBox.setSelectedItem(0);

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
