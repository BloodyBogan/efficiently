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
    
    public static void updateAdminTable(JTable usersTable) throws SQLException, IOException {
        String sqlQuery = "SELECT users.ais_id, users.name, user_role.role FROM users, user_role WHERE users.role=user_role.role_id";
        
        int adminAisId = User.getAisId();
        
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
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
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
