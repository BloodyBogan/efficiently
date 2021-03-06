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
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * <h1>DashboardController Class</h1>
 * Governs the dashboard functionality
 *
 * @author Michal Kaštan <github.com/BloodyBogan> & Ladislav Capalaj
 * @version 1.0.0
 * @since 2020-11-23
 */
public class DashboardController {

    /**
     * Logs the user out by resetting User's variables and showing the main menu screen
     */
    public static void logout() {
        User.logout();
        
        MainLayout.showMenuScreen();
    }
    
    /**
     * Populates the student's appointments table
     *
     * @param appointmentsTable
     */
    public static void handleStudentAppointmentsTableUpdate(JTable appointmentsTable) {
        String sqlRetrieveAppointments = "SELECT appointments.subject, appointments.message, appointments.response, dates.date, users.name, appointments.isClosed FROM appointments, dates, users WHERE (appointments.user=? AND appointments.date=dates.date_id AND users.user_id=dates.user) ORDER BY date ASC";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlRetrieveAppointments)) {
            
            int userId = User.getUserId();
       
            pstmt.setInt(1, userId);
            
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            
            int c;
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
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Displays an easy-to-read informational message to the user with all the relevant information about a given appointment
     *
     * @param appointmentsTable
     */
    public static void handleStudentAppointmentsTableRowClick(JTable appointmentsTable) {
        DefaultTableModel Df = (DefaultTableModel)appointmentsTable.getModel();
        
        int selectedIndex = appointmentsTable.getSelectedRow();
        
        String subject = Df.getValueAt(selectedIndex, 0).toString();
        String message = Df.getValueAt(selectedIndex, 1).toString();
        String response = Df.getValueAt(selectedIndex, 2).toString();
        String date = Df.getValueAt(selectedIndex, 3).toString();
        String name = Df.getValueAt(selectedIndex, 4).toString();
        String closed = Df.getValueAt(selectedIndex, 5).toString();
        
        JOptionPane.showMessageDialog(MainLayout.getJPanel(), "<html><body><div style='width: 450px;'><p>Subject: " + subject + "</p><br><p>Message: " + message + "</p><br><p>Response: " + response + "</p><br><p>Date: " + date + "</p><br><p>Correspondent: " + name + "</p><br><p>Closed: " + closed + "</p></div></body></html>", "Appointment" + Messages.getHeaders(1), JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Checks whether the user crossed the maximum appointments count limit
     * Inserts the newly-created appointment to the database
     * Updates the appointment's date
     *
     * @param subject
     * @param message
     * @param dateTimeComboBox
     * @param dateTimeList
     */
    public static void handleStudentBookAppointment(String subject, String message, JComboBox<String> dateTimeComboBox, JList<String> dateTimeList) {   
        int userId = User.getUserId();
        
        String sqlRetrieveAppointmentsCount = "SELECT COUNT(*) as appointment_count FROM appointments, dates WHERE (appointments.user=? AND appointments.isClosed=0 AND dates.date_id=appointments.date AND dates.date>=NOW() AND dates.isTaken<>0)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlRetrieveAppointmentsCount)) {

            pstmt.setInt(1, userId);
            
            int appointmentCount = 0;
            
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                appointmentCount = rs.getInt("appointment_count");
            }
            
            int maxActiveAppointmentsCount = 2;
            if (appointmentCount >= maxActiveAppointmentsCount) {
                throw new ValidationException(Messages.getError(5));
            }
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ValidationException ve) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), ve.getMessage(), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int option = JOptionPane.showConfirmDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(5), "book", "appointment"), String.format(Messages.getGeneral(6), "Book", "appointment"), JOptionPane.YES_NO_OPTION);
        if (option != 0) {
           return;
        }
        
        int comboBoxIndex = dateTimeComboBox.getSelectedIndex();
            
        DefaultListModel model = (DefaultListModel)dateTimeList.getModel();
        int dateId = Integer.parseInt((String) model.get(comboBoxIndex));
        
        String sqlInsertAppointment = "INSERT INTO appointments (user, subject, message, date) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlInsertAppointment)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, subject);
            pstmt.setString(3, message);
            pstmt.setInt(4, dateId);
            
            pstmt.execute();
            
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(7), "Appointment", "booked"), Messages.getHeaders(1), JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String sqlUpdateDate = "UPDATE dates SET isTaken=1 WHERE date_id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdateDate)) {
            
            pstmt.setInt(1, dateId);

            pstmt.executeUpdate();
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Makes sure the user is provided with the current available dates
     *
     * @param dateTimeComboBox
     * @param dateTimeList
     */
    @SuppressWarnings("unchecked")
    public static void handleStudentAppointmentsDatesUpdate(JComboBox<String> dateTimeComboBox, JList<String> dateTimeList) {
        dateTimeComboBox.removeAllItems();
        
        DefaultListModel model = (DefaultListModel)dateTimeList.getModel();
        model.removeAllElements();
        
        String sqlRetrieveDatesAndNames = "SELECT dates.date_id, dates.date, users.name FROM dates, users WHERE (dates.date>=NOW() AND dates.isTaken=0 AND dates.user=users.user_id) ORDER BY dates.date ASC";
        try (Connection conn = Database.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sqlRetrieveDatesAndNames)) {

            if (rs.next() == false) {
                dateTimeComboBox.addItem(Messages.getGeneral(4));
            } else {
                do {
                    dateTimeComboBox.addItem(rs.getString("date") + " - " + rs.getString("name"));
                    
                    model.addElement(String.valueOf(rs.getInt("date_id")));
                } while(rs.next());
            }
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Tells the user how many valid appointments are before the user's one for the user's first appointment's correspondent
     *
     * @param queueLabel
     */
    public static void handleStudentQueueUpdate(JLabel queueLabel) {
        int dateId = 0;
        int correspondentUserId = 0;
        int userId = User.getUserId();
        
        String sqlRetrieveDate = "SELECT dates.date_id, dates.user FROM dates, appointments WHERE (dates.isTaken<>0 AND dates.date >= NOW() AND dates.date_id=appointments.date AND appointments.user=? AND appointments.isClosed=0) ORDER BY dates.date ASC LIMIT 1";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlRetrieveDate)) {
            
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
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
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
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        }
    } 
    
    /**
     * Populates the correspondent's appointments table
     *
     * @param appointmentsTable
     */
    public static void handleCorrespondentAppointmentsTableUpdate(JTable appointmentsTable) {
        String sqlRetrieveAppointments = "SELECT appointments.appointment_id, appointments.subject, appointments.message, appointments.response, dates.date, appointments.isClosed, users.ais_id, users.name FROM appointments, dates, users WHERE (dates.user=? AND appointments.date=dates.date_id AND appointments.user=users.user_id) ORDER BY dates.date ASC";    
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlRetrieveAppointments)) {
            
            int userId = User.getUserId();
            
            pstmt.setInt(1, userId);
            
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            
            int c;
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
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Populates all the relevant fields with information about the selected appointment
     *
     * @param appointmentsTable
     * @param nameField
     * @param aisIdField
     * @param subjectField
     * @param messageTextArea
     * @param responseTextArea
     * @param dateTimeLabel
     * @param closedCheckBox
     * @param manageResponseTextArea
     * @param manageClosedCheckBox
     */
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
        
        nameField.setText(Df.getValueAt(selectedIndex, 1).toString());
        aisIdField.setText(Df.getValueAt(selectedIndex, 2).toString());
        subjectField.setText(Df.getValueAt(selectedIndex, 3).toString());
        messageTextArea.setText(Df.getValueAt(selectedIndex, 4).toString());
        
        String response = Df.getValueAt(selectedIndex, 5).toString();
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
    
    /**
     * Updates the selected appointment
     *
     * @param appointmentsTable
     * @param responseTextArea
     * @param closedCheckBox
     * @param response
     * @param manageClosedCheckBox
     */
    public static void handleCorrespondentAppointmentUpdate(JTable appointmentsTable, JTextArea responseTextArea, JCheckBox closedCheckBox, String response, JCheckBox manageClosedCheckBox) {
        int option = JOptionPane.showConfirmDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(5), "update", "appointment"), String.format(Messages.getGeneral(6), "Update", "appointment"), JOptionPane.YES_NO_OPTION);
        if (option != 0) {
           return;
        }
        
        String sqlUpdateAppointments = "UPDATE appointments SET response=?, isClosed=? WHERE appointment_id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdateAppointments)) {
            
            int isClosed;
            boolean isSelected = manageClosedCheckBox.isSelected();
            if (isSelected) {
                isClosed = 1;
            } else {
                isClosed = 0;
            }
            
            if (response.isEmpty()) {
                response = null;
            }
            
            DefaultTableModel Df = (DefaultTableModel)appointmentsTable.getModel();
            int selectedIndex = appointmentsTable.getSelectedRow();
            int appointmentId = (int) Df.getValueAt(selectedIndex, 0);
            
            pstmt.setString(1, response);
            pstmt.setInt(2, isClosed);
            pstmt.setInt(3, appointmentId);

            pstmt.executeUpdate();
            
            responseTextArea.setText(response);
            
            closedCheckBox.setSelected(isSelected);

            JOptionPane.showMessageDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(7), "User", "updated"), Messages.getHeaders(1), JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Updates the date that is tied to the appointment at question
     * Deletes the selected appointment
     *
     * @param appointmentsTable
     */
    public static void handleCorrespondentAppointmentDelete(JTable appointmentsTable) {
        int option = JOptionPane.showConfirmDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(5), "delete", "appointment"), String.format(Messages.getGeneral(6), "Delete", "appointment"), JOptionPane.YES_NO_OPTION);
        if (option != 0) {
           return;
        }
        
        DefaultTableModel Df = (DefaultTableModel)appointmentsTable.getModel();
        int selectedIndex = appointmentsTable.getSelectedRow();

        int appointmentId = (int) Df.getValueAt(selectedIndex, 0);
        
        String sqlUpdateDates = "UPDATE dates SET isTaken=0 WHERE (date_id=(SELECT date FROM appointments WHERE appointment_id=?) AND date>=NOW())";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdateDates)) {
            
            pstmt.setInt(1, appointmentId);
            
            pstmt.executeUpdate();
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String sqlDeleteAppointment = "DELETE FROM appointments WHERE appointment_id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlDeleteAppointment)) {

            pstmt.setInt(1, appointmentId);
            
            pstmt.execute();
            
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(7), "Appointment", "deleted"), Messages.getHeaders(1), JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Makes sure the current correspondent haven't already made the date available already
     * Inserts the new date into the database
     *
     * @param dateTime
     */
    public static void handleCorrespondentDateTimeAdd(String dateTime) {        
        int userId = User.getUserId();
        
        String sqlRetrieveDate = "SELECT date FROM dates WHERE (user=? AND date=?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlRetrieveDate)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, dateTime);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() != false) {
                JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getValidationError(2), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String sqlInsertDate = "INSERT INTO dates (user, date) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlInsertDate)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, dateTime);
            
            pstmt.execute();
            
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(7), "Date & time", "added"), Messages.getHeaders(1), JOptionPane.INFORMATION_MESSAGE); 
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Displays all correspondent's dates
     *
     * @param deleteDateTimeComboBox
     * @param deleteDateTimeList
     */
    @SuppressWarnings({"unchecked", "unchecked"})
    public static void handleCorrespondentDateTimeUpdate(JComboBox<String> deleteDateTimeComboBox, JList<String> deleteDateTimeList) {
        int userId = User.getUserId();
        
        deleteDateTimeComboBox.removeAllItems();
        
        DefaultListModel model = (DefaultListModel)deleteDateTimeList.getModel();
        model.removeAllElements();

        String sqlRetrieveDates = "SELECT * FROM dates WHERE user=? ORDER BY date ASC";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlRetrieveDates)) {

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
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Deletes the appointment that is tied to the date
     * Deletes the date
     *
     * @param deleteDateTimeComboBox
     * @param deleteDateTimeList
     */
    public static void handleCorrespondentDateTimeDelete(JComboBox<String> deleteDateTimeComboBox, JList<String> deleteDateTimeList) {
        int option = JOptionPane.showConfirmDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(5), "delete", "date & time"), String.format(Messages.getGeneral(6), "Delete", "date & time"), JOptionPane.YES_NO_OPTION);
        if (option != 0) {
           return;
        }
        
        int comboBoxIndex = deleteDateTimeComboBox.getSelectedIndex();
        DefaultListModel model = (DefaultListModel)deleteDateTimeList.getModel();

        int dateId = Integer.parseInt((String) model.get(comboBoxIndex));
        
        String sqlDeleteAppointment = "DELETE FROM appointments WHERE date=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlDeleteAppointment)) {

            pstmt.setInt(1, dateId);
            
            pstmt.execute();
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String sqlDeleteDate = "DELETE FROM dates WHERE date_id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlDeleteDate)) {

            pstmt.setInt(1, dateId);
            
            pstmt.execute();
            
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(7), "Date & time", "deleted"), Messages.getHeaders(1), JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Populates the admin's users table
     *
     * @param usersTable
     */
    public static void handleAdminUsersTableUpdate(JTable usersTable) {
        String sqlRetrieveUsers = "SELECT users.user_id, users.ais_id, users.name, user_role.role FROM users, user_role WHERE (users.user_id<>? AND users.role=user_role.role_id) ORDER BY users.ais_id ASC";
        
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlRetrieveUsers)) {
            
            int userId = User.getUserId();
            
            pstmt.setInt(1, userId);
            
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            
            int c;
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
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Populates all the relevant fields with information about the selected user
     *
     * @param usersTable
     * @param idField
     * @param aisIdField
     * @param nameField
     * @param roleComboBox
     */
    public static void handleAdminUsersTableRowClick(JTable usersTable, JTextField idField, JTextField aisIdField, JTextField nameField, JComboBox<String> roleComboBox) {
        int selectedIndex = usersTable.getSelectedRow();
        DefaultTableModel Df = (DefaultTableModel)usersTable.getModel();
        
        idField.setText(Df.getValueAt(selectedIndex, 0).toString());
        aisIdField.setText(Df.getValueAt(selectedIndex, 1).toString());
        nameField.setText(Df.getValueAt(selectedIndex, 2).toString());
        
        roleComboBox.setSelectedItem(Df.getValueAt(selectedIndex, 3).toString());
    }
    
    /**
     * Updates the selected user
     *
     * @param idField
     * @param aisId
     * @param name
     * @param roleComboBox
     */
    public static void handleAdminUserUpdate(JTextField idField, int aisId, String name, JComboBox<String> roleComboBox) {
        int option = JOptionPane.showConfirmDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(5), "update", "user"), String.format(Messages.getGeneral(6), "Update", "user"), JOptionPane.YES_NO_OPTION);
        if (option != 0) {
           return;
        }
        
        String sqlUpdateUser = "UPDATE users SET ais_id=?, name=?, role=? WHERE user_id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdateUser)) {
            
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

            JOptionPane.showMessageDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(7), "User", "updated"), Messages.getHeaders(1), JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLIntegrityConstraintViolationException sicve) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), String.format(Messages.getValidationError(0), aisId), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE); 
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Selects all the dates that the student has booked so far
     * Makes some of them available
     * Deletes all the user's appointments
     *
     * @param userId
     */
    public static void handleAdminStudentDelete(int userId) { 
        List<Integer> datesList = new ArrayList<>(); 
        
        String sqlRetrieveDates = "SELECT appointments.date FROM appointments, dates WHERE (appointments.user=? AND dates.date_id=appointments.date AND dates.date >= NOW())";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlRetrieveDates)) {
            
            pstmt.setInt(1, userId);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() != false) {
                do {
                    datesList.add(rs.getInt("date"));
                } while (rs.next());
            }
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!datesList.isEmpty()) {
            Integer[] appointmentsArray = datesList.toArray(new Integer[0]);
            
            StringBuilder sqlUpdateDates = new StringBuilder(1024);
            sqlUpdateDates.append("UPDATE dates SET isTaken=0 WHERE date_id IN (");
            
            for(int i = 0; i < appointmentsArray.length; i++) {
                if(i > 0) {
                    sqlUpdateDates.append(",");
                }
                
                sqlUpdateDates.append(" ?");
            }
            
            sqlUpdateDates.append(")");
            
            try (Connection conn = Database.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sqlUpdateDates.toString())) {

                for(int i=0; i < appointmentsArray.length; i++) {
                    pstmt.setInt(i + 1, appointmentsArray[i]);
                }

                pstmt.executeUpdate();
            } catch (SQLException se) {
                JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
                return;
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
                return;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        String sqlDeleteAppointments = "DELETE FROM appointments WHERE user=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlDeleteAppointments)) {
            
            pstmt.setInt(1, userId);
            
            pstmt.execute();
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Selects all correspondent's booked dates
     * Deletes appointments tied to those dates
     * Deletes all of the correspondent's dates 
     *
     * @param userId
     */
    public static void handleAdminCorrespondentDelete(int userId) {
        List<Integer> appointmentsList = new ArrayList<>();
        
        String sqlRetrieveDates = "SELECT date_id FROM dates WHERE (user=? AND isTaken<>0)"; 
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlRetrieveDates)) {
            
            pstmt.setInt(1, userId);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() != false) {
                do {
                    appointmentsList.add(rs.getInt("date_id"));
                } while (rs.next());
            }
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!appointmentsList.isEmpty()) {
            Integer[] appointmentsArray = appointmentsList.toArray(new Integer[0]);
            
            StringBuilder sqlDeleteAppointments = new StringBuilder(1024);
            sqlDeleteAppointments.append("DELETE FROM appointments WHERE date IN (");
            
            for(int i = 0; i < appointmentsArray.length; i++) {
                if(i > 0) {
                    sqlDeleteAppointments.append(",");
                }
                
                sqlDeleteAppointments.append(" ?");
            }
            
            sqlDeleteAppointments.append(")");
            
            try (Connection conn = Database.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sqlDeleteAppointments.toString())) {

                for(int i = 0;i < appointmentsArray.length; i++) {
                    pstmt.setInt(i + 1, appointmentsArray[i]);
                }

                pstmt.execute();
            } catch (SQLException se) {
                JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
                return;
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
                return;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        String sqlDeleteDates = "DELETE FROM dates WHERE user=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlDeleteDates)) {
            
            pstmt.setInt(1, userId);
            
            pstmt.execute();
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Retrieves the user's role from the database
     * Handles each user type differently
     * Deletes the user itself
     *
     * @param idField
     */
    public static void handleAdminUserDelete(JTextField idField) {
        int option = JOptionPane.showConfirmDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(5), "delete", "user"), String.format(Messages.getGeneral(6), "Delete", "user"), JOptionPane.YES_NO_OPTION);
        if (option != 0) {
           return;
        }
        
        int userId = Integer.parseInt(idField.getText());
        
        String userRole = null;
        
        String sqlRetrieveRole = "SELECT user_role.role FROM user_role, users WHERE (users.user_id=? AND user_role.role_id=users.role)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlRetrieveRole)) {
            
            pstmt.setInt(1, userId);
            
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                userRole = rs.getString("role");
            }
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        switch (userRole) {
            case "student":
                handleAdminStudentDelete(userId);
                
                break;
            case "correspondent":
                handleAdminCorrespondentDelete(userId);
                
                break;
            default:
                JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
                return;
        }
        
        String sqlDeleteUser = "DELETE FROM users WHERE user_id=?"; 
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlDeleteUser)) {
            
            pstmt.setInt(1, userId);
            
            pstmt.execute();
            
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), String.format(Messages.getGeneral(7), "User", "deleted"), Messages.getHeaders(1), JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        }
    }
}