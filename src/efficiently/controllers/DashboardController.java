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
import efficiently.config.Messages;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.IOException;
import efficiently.models.User;
import efficiently.utils.ValidationException;
import efficiently.views.MainLayout;
import efficiently.views.dashboard.Admin;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
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
    
    public static void handleStudentAppointmentsTableUpdate(JTable appointmentsTable) throws SQLException, IOException {
        String sqlQuery = "SELECT appointments.subject, appointments.message, appointments.response, dates.date, users.name, appointments.isClosed FROM appointments, dates, users WHERE (appointments.user=? AND appointments.date=dates.date_id AND users.user_id=dates.user) ORDER BY date ASC";
        
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
                ArrayList<String> tableList = new ArrayList<>();
                
                for (int i = 1; i <= c; i++) {
                    tableList.add(rs.getString("subject"));
                    tableList.add(rs.getString("message"));
                    
                    if (rs.getString("response") == null) {
                        tableList.add("No response yet");
                    } else {
                        tableList.add(rs.getString("response"));
                    }
                    
                    tableList.add(rs.getString("date"));
                    tableList.add(rs.getString("name"));
                    
                    if (rs.getInt("isClosed") == 0) {
                        tableList.add("No");
                    } else {
                        tableList.add("Yes");
                    }
                }
                
                Object[] tableObject = tableList.toArray();
                Df.addRow(tableObject);
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
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
        String closed = Df.getValueAt(selectedIndex, 5).toString();
        
        JOptionPane.showMessageDialog(MainLayout.getJPanel(), "<html><body><div style='width: 450px;'><p>Subject: " + subject + "</p><br><p>Message: " + message + "</p><br><p>Response: " + response + "</p><br><p>Date: " + date + "</p><br><p>Correspondent: " + name + "</p><br><p>Closed: " + closed + "</p></div></body></html>", "Appointment Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void handleStudentBookAppointment(String subject, String message, JComboBox<String> dateTimeComboBox, JList<String> dateTimeList) {   
        int userId = User.getUserId();
        
        String sqlSelectQuery = "SELECT COUNT(*) as appointment_count FROM appointments, dates WHERE (appointments.user=? AND appointments.isClosed=0 AND dates.date_id=appointments.date AND dates.date>=NOW() AND dates.isTaken<>0)";
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlSelectQuery)) {

            pstmt.setInt(1, userId);
            
            ResultSet rs = pstmt.executeQuery();
            
            int appointmentCount = 0;
            while(rs.next()) {
                appointmentCount = rs.getInt("appointment_count");
            }
            
            int maxActiveAppointmentsCount = 2;
            if (appointmentCount >= maxActiveAppointmentsCount) {
                throw new ValidationException(Messages.getError(5));
            }
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
            return;
        } catch (ValidationException ve) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), ve.getMessage());
            return;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
            return;
        }
        
        int option = JOptionPane.showConfirmDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(5), "book", "appointment"), String.format(Messages.getGeneral(6), "Book", "appointment"), JOptionPane.YES_NO_OPTION);
        if (option != 0) {
           return;
        }
        
        int comboBoxIndex = dateTimeComboBox.getSelectedIndex();
            
        DefaultListModel model = (DefaultListModel)dateTimeList.getModel();

        int dateId = Integer.parseInt((String) model.get(comboBoxIndex));
        
        String sqlAppointmentQuery = "INSERT INTO appointments (user, subject, message, date) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlAppointmentQuery)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, subject);
            pstmt.setString(3, message);
            pstmt.setInt(4, dateId);
            
            pstmt.execute();
            
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(7), "Appointment", "booked"));
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
        }
        
        String sqlDateQuery = "UPDATE dates SET isTaken=1 WHERE date_id=?";

        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlDateQuery)) {
            
            pstmt.setInt(1, dateId);

            pstmt.executeUpdate();
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
        }
    }
    
    @SuppressWarnings("unchecked")
    public static void handleStudentAppointmentsDatesUpdate(JComboBox<String> dateTimeComboBox, JList<String> dateTimeList) {
        String sqlQuery = "SELECT dates.date_id, dates.date, users.name FROM dates, users WHERE (dates.date>=NOW() AND dates.isTaken=0 AND dates.user=users.user_id) ORDER BY dates.date ASC";
        
        DefaultListModel model = (DefaultListModel)dateTimeList.getModel();
        
        dateTimeComboBox.removeAllItems();
        model.removeAllElements();
        
        try (Connection conn = Database.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sqlQuery)) {

            if (rs.next() == false) {
                dateTimeComboBox.addItem("There are no available dates & times");
            } else {
                do {
                    dateTimeComboBox.addItem(rs.getString("date") + " - " + rs.getString("name"));
                    model.addElement(String.valueOf(rs.getInt("date_id")));
                } while(rs.next());
            }
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
        }
    }
    
    public static void handleStudentQueueUpdate(JLabel queueLabel) throws SQLException, IOException {
        String usersAppointmentQuery = "SELECT dates.date_id, dates.user FROM dates, appointments WHERE (dates.isTaken<>0 AND dates.date >= NOW() AND dates.date_id=appointments.date AND appointments.user=? AND appointments.isClosed=0) ORDER BY dates.date ASC LIMIT 1";
        
        int dateId = 0;
        int correspondentUserId = 0;
        int userId = User.getUserId();
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(usersAppointmentQuery)) {
            
            pstmt.setInt(1, userId);
            
            ResultSet rs = pstmt.executeQuery();
           
            if (rs.next() == false) {
                queueLabel.setText("<html><body><p style='width: 115px; text-align: center;'>" + Messages.getGeneral(9) + "</p></body></html>");
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
        
        String sqlCountQuery = "SELECT COUNT(*) as total FROM dates, appointments WHERE (dates.isTaken<>0 AND dates.date>=NOW() AND dates.date<=(SELECT date FROM dates WHERE date_id=?) AND dates.user=? AND appointments.date=dates.date_id AND appointments.user<>? AND appointments.isClosed=0)";
        
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
                queueLabel.setText("<html><body><p style='width: 115px; text-align: center;'>" + Messages.getGeneral(10) + "</p></body></html>");
            } else if (count == 1) {
                queueLabel.setText("<html><body><p style='width: 115px; text-align: center;'>" + String.format(Messages.getGeneral(11), "is", String.valueOf(count), "appointment") + "</p></body></html>");
            } else {
                queueLabel.setText("<html><body><p style='width: 115px; text-align: center;'>" + String.format(Messages.getGeneral(11), "are", String.valueOf(count), "appointments") + "</p></body></html>");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    } 
    
    public static void handleCorrespondentAppointmentsTableUpdate(JTable appointmentsTable) throws SQLException, IOException {
        String sqlQuery = "SELECT appointments.appointment_id, appointments.subject, appointments.message, appointments.response, dates.date, appointments.isClosed, users.ais_id, users.name FROM appointments, dates, users WHERE (dates.user=? AND appointments.date=dates.date_id AND appointments.user=users.user_id) ORDER BY dates.date ASC";
        
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
                ArrayList<Object> tableList = new ArrayList<>();
                
                for (int i = 1; i <= c; i++) {
                    tableList.add(rs.getInt("appointment_id"));
                    tableList.add(rs.getString("name"));
                    tableList.add(rs.getInt("ais_id"));
                    tableList.add(rs.getString("subject"));
                    tableList.add(rs.getString("message"));
                    
                    if (rs.getString("response") == null) {
                        tableList.add("No response yet");
                    } else {
                        tableList.add(rs.getString("response"));
                    }
                    
                    tableList.add(rs.getString("date"));
                    
                    if (rs.getInt("isClosed") == 0) {
                        tableList.add("No");
                    } else {
                        tableList.add("Yes");
                    }
                }
                
                Object[] tableObject = tableList.toArray();
                Df.addRow(tableObject);
            }
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
        }
    }
    
    public static void handleCorrespondentTableRowClick(JTable appointmentsTable, JTextField nameField, JTextField aisIdField, JTextField subjectField, JTextArea messageTextArea, JTextArea responseTextArea, JLabel dateTimeLabel, JCheckBox closedCheckBox, JTextArea manageResponseTextArea, JCheckBox manageClosedCheckBox) {
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
        dateTimeLabel.setText(Df.getValueAt(selectedIndex, 6).toString());
        closedCheckBox.setSelected(isSelected);
        
        String manageResponse;
        switch (response) {
            case "No response yet":
                manageResponse = "";
                break;
            default:
                manageResponse = response;
        }
        
        manageResponseTextArea.setText(manageResponse);
        manageClosedCheckBox.setSelected(isSelected);
    }
    
    public static void handleCorrespondentAppointmentUpdate(JTable appointmentsTable, JTextArea responseTextArea, JCheckBox closedCheckBox, String response, JCheckBox manageClosedCheckBox) throws SQLException, IOException {
        int option = JOptionPane.showConfirmDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(5), "update", "appointment"), String.format(Messages.getGeneral(6), "Update", "appointment"), JOptionPane.YES_NO_OPTION);
        if (option != 0) {
           return;
        }
        
        String sqlQuery = "UPDATE appointments SET response=?, isClosed=? WHERE appointment_id=?";

        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            DefaultTableModel Df = (DefaultTableModel)appointmentsTable.getModel();
            int selectedIndex = appointmentsTable.getSelectedRow();

            int appointmentId = (int) Df.getValueAt(selectedIndex, 0);
            
            boolean isSelected = manageClosedCheckBox.isSelected();
            int isClosed;
            if (isSelected) {
                isClosed = 1;
            } else {
                isClosed = 0;
            }
            
            if (response.isEmpty()) {
                response = null;
            }
            
            pstmt.setString(1, response);
            pstmt.setInt(2, isClosed);
            pstmt.setInt(3, appointmentId);

            pstmt.executeUpdate();
            
            responseTextArea.setText(response);
            closedCheckBox.setSelected(isSelected);

            JOptionPane.showMessageDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(7), "User", "updated"));
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getGeneral(0));
        }
    }
    
    public static void handleCorrespondentAppointmentDelete(JTable appointmentsTable, JTabbedPane manageTabbedPane, JTextField nameField, JTextField aisIdField, JTextField subjectField, JTextArea messageTextArea, JTextArea responseTextArea, JLabel dateTimeLabel, JCheckBox closedCheckBox, JTextArea manageResponseTextArea, JCheckBox manageClosedCheckBox) throws SQLException, IOException {
        int option = JOptionPane.showConfirmDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(5), "delete", "appointment"), String.format(Messages.getGeneral(6), "Delete", "appointment"), JOptionPane.YES_NO_OPTION);
        if (option != 0) {
           return;
        }
        
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
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
        }
        
        String sqlDeleteQuery = "DELETE FROM appointments WHERE appointment_id=?";
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlDeleteQuery)) {

            pstmt.setInt(1, appointmentId);
            
            pstmt.execute();
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
        }

        JOptionPane.showMessageDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(7), "Appointment", "deleted"));
    }
    
    public static void handleCorrespondentDateTimeAdd(String dateTime) {        
        String sqlSelectQuery = "SELECT date FROM dates WHERE (user=? AND date=?)";
        
        int userId = User.getUserId();
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlSelectQuery)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, dateTime);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next() != false) {
                JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getValidationError(2));
                return;
            }
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
            return;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
            return;
        }
        
        String sqlInsertQuery = "INSERT INTO dates (user, date) VALUES (?, ?)";
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlInsertQuery)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, dateTime);
            
            pstmt.execute();
            
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(7), "Date & time", "added")); 
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
        }
    }
    
    @SuppressWarnings("unchecked")
    public static void handleCorrespondentDateTimeUpdate(JComboBox<String> deleteDateTimeComboBox, JList<String> deleteDateTimeList) {
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
                deleteDateTimeComboBox.addItem(Messages.getGeneral(8));
            } else {
                do {
                    deleteDateTimeComboBox.addItem(rs.getString("date"));
                    model.addElement(String.valueOf(rs.getInt("date_id")));
                } while (rs.next());
            }
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
        }
    }
    
    public static void handleCorrespondentDateTimeDelete(JComboBox<String> deleteDateTimeComboBox, JList<String> deleteDateTimeList) {
        int option = JOptionPane.showConfirmDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(5), "delete", "date & time"), String.format(Messages.getGeneral(6), "Delete", "date & time"), JOptionPane.YES_NO_OPTION);
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
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
        }
        
        String sqlDeleteDateQuery = "DELETE FROM dates WHERE date_id=?";
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlDeleteDateQuery)) {

            pstmt.setInt(1, dateId);
            
            pstmt.execute();
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
        }
            
        JOptionPane.showMessageDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(7), "Date & time", "deleted"));
    }
    
    public static void handleAdminUsersTableUpdate(JTable usersTable) throws SQLException, IOException {
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
                ArrayList<Object> tableList = new ArrayList<>();
                
                for (int i = 1; i <= c; i++) { 
                    tableList.add(rs.getInt("user_id"));
                    tableList.add(rs.getInt("ais_id"));
                    tableList.add(rs.getString("name"));
                    tableList.add(rs.getString("role"));
                }
                
                Object[] tableObject = tableList.toArray();
                Df.addRow(tableObject);
            }
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
        }
    }
    
    public static void handleAdminUsersTableRowClick(JTable usersTable, JTextField idField, JTextField aisIdField, JTextField nameField, JComboBox<String> roleComboBox) {
        DefaultTableModel Df = (DefaultTableModel)usersTable.getModel();
        int selectedIndex = usersTable.getSelectedRow();
        
        idField.setText(Df.getValueAt(selectedIndex, 0).toString());
        aisIdField.setText(Df.getValueAt(selectedIndex, 1).toString());
        nameField.setText(Df.getValueAt(selectedIndex, 2).toString());
        roleComboBox.setSelectedItem(Df.getValueAt(selectedIndex, 3).toString());
    }
    
    public static void handleAdminUserUpdate(JTable usersTable, JTextField idField, int aisId, String name, JComboBox<String> roleComboBox) throws SQLException, IOException {
        int option = JOptionPane.showConfirmDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(5), "update", "user"), String.format(Messages.getGeneral(6), "Update", "user"), JOptionPane.YES_NO_OPTION);
        if (option != 0) {
           return;
        }
        
        String sqlQuery = "UPDATE users SET ais_id=?, name=?, role=? WHERE user_id=?";

        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
            
            int role;
            String userRole = roleComboBox.getItemAt(roleComboBox.getSelectedIndex());
            switch (userRole) {
                case "student":
                    role = 1;
                    break;
                case "correspondent":
                    role = 2;
                    break;
                case "admin":
                    role = 3;
                    break;
                default:
                    role = 1;
            }
           
            int userId = Integer.parseInt(idField.getText());
            
            pstmt.setInt(1, aisId);
            pstmt.setString(2, name);
            pstmt.setInt(3, role);
            pstmt.setInt(4, userId);

            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(7), "User", "updated"));
        } catch (SQLIntegrityConstraintViolationException sicve) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), String.format(Messages.getValidationError(0), aisId)); 
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
        }
    }
    
    public static void handleAdminStudentDelete(int userId) {
        String sqlSelectQuery = "SELECT appointments.date FROM appointments, dates WHERE (appointments.user=? AND dates.date_id=appointments.date AND dates.date >= NOW())";
        
        List<Integer> datesList = new ArrayList<>(); 
        
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
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
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
                JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
            }
        }
        
        String sqlDeleteDatesQuery = "DELETE FROM appointments WHERE user=?";
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlDeleteDatesQuery)) {
            pstmt.setInt(1, userId);
            
            pstmt.execute();
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
        }
    }
    
    public static void handleAdminCorrespondentDelete(int userId) {
        String sqlSelectQuery = "SELECT date_id FROM dates WHERE (user=? AND isTaken<>0)";
        
        List<Integer> appointmentsList = new ArrayList<>(); 
        
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
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
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
                JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
            }
        }
        
        String sqlDeleteDatesQuery = "DELETE FROM dates WHERE user=?";
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlDeleteDatesQuery)) {
            pstmt.setInt(1, userId);
            
            pstmt.execute();
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
        }
    }
    
    public static void handleAdminUserDelete(JTable usersTable, JTextField idField, JTextField aisIdField, JTextField nameField, JComboBox<String> roleComboBox) {
        int option = JOptionPane.showConfirmDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(5), "delete", "user"), String.format(Messages.getGeneral(6), "Delete", "user"), JOptionPane.YES_NO_OPTION);
        if (option != 0) {
           return;
        }
        
        int userId = Integer.parseInt(idField.getText());
        
        String userRole = null;
        String sqlSelectQuery = "SELECT user_role.role FROM user_role, users WHERE (users.user_id=? AND user_role.role_id=users.role)";
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlSelectQuery)) {
            
            pstmt.setInt(1, userId);
            
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next()) {
                userRole = rs.getString("role");
            }
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
        }
        
        switch (userRole) {
            case "student":
                handleAdminStudentDelete(userId);
                break;
            case "correspondent":
                handleAdminCorrespondentDelete(userId);
                break;
        }
        
        String sqlDeleteQuery = "DELETE FROM users WHERE user_id=?";
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlDeleteQuery)) {
            
            pstmt.setInt(1, userId);
            
            pstmt.execute();
            
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(7), "User", "deleted"));
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0));
        }
    }
}
