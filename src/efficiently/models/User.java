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
package efficiently.models;

import efficiently.config.Database;
import efficiently.config.Messages;
import efficiently.views.MainLayout;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JOptionPane;

/**
 *
 * @author Michal Kaštan <github.com/BloodyBogan> & Ladislav Capalaj
 */
public class User {
    private static int userId;
    private static int aisId;
    private static String name;
    private static String role;
    private static String roleFromDatabase;
    private static LocalDateTime lastAction;
    
    public static int getUserId() {
        return userId;
    }
    
    public static int getAisId() {
        return aisId;
    }
    
    public static String getName() {
        return name;
    }
        
    public static String getRole() {
        return role;
    }

    public static LocalDateTime getLastAction() {
        return lastAction;
    }
    
    public static void setUserId(int zUserId) {
        userId = zUserId;
    }
        
    public static void setAisId (int zAisId) {
        aisId = zAisId;
    }
    
    public static void setName (String zName) {
        name = zName;
    }
    
    public static void setRole (String zRole) {
        role = zRole;
    }
    
    public static void setLastAction () {
        String sqlRetrieveDateTime = "SELECT now() as date_time";
        
        lastAction = LocalDateTime.now().minusYears(100);
        try (Connection conn = Database.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sqlRetrieveDateTime)) {
           
            while (rs.next()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                lastAction = LocalDateTime.parse(rs.getString("date_time"), formatter);  
            }
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException | NullPointerException dtpe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static boolean doesUserExist () {
        String sqlRetrieveAisId = "SELECT ais_id FROM users WHERE user_id=?";
        
        boolean valid = false;
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlRetrieveAisId)) {
           
            pstmt.setInt(1, userId);
            
            ResultSet rs = pstmt.executeQuery();
            
            valid = rs.next() != false;
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        }
        
        return valid;
    }
    
    private static String setRoleFromDatabase () {
        String sqlRetrieveUserRole = "SELECT user_role.role FROM user_role, users WHERE (users.user_id=? AND user_role.role_id=users.role)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlRetrieveUserRole)) {
           
            pstmt.setInt(1, userId);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next() == false) {
                roleFromDatabase = "";
            } else {
                roleFromDatabase = rs.getString("role");
            }
        } catch (SQLException se) {
            roleFromDatabase = "";
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            roleFromDatabase = "";
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            roleFromDatabase = "";
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        }
        
        return roleFromDatabase;
    }
    
    public static boolean isSessionValid (String ACCESS_LEVEL) {
        String sqlRetrieveDateTime = "SELECT now() as date_time";
        
        LocalDateTime time = LocalDateTime.now().minusYears(100);
        try (Connection conn = Database.getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sqlRetrieveDateTime)) {
           
            while (rs.next()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                time = LocalDateTime.parse(rs.getString("date_time"), formatter);  
            }
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException | NullPointerException dtpe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        }
        
        LocalDateTime t = time.minusMinutes(30);
        
        setRoleFromDatabase();
        
        return t.isBefore(lastAction) && doesUserExist() && (role.equals(roleFromDatabase)) && (ACCESS_LEVEL.equals(role)) && (ACCESS_LEVEL.equals(roleFromDatabase)) && ((userId != -1) && (aisId != -1) && (!name.isEmpty()) && (!role.isEmpty()) && (!roleFromDatabase.isEmpty()));
    }
    
    public static void logout() {
        userId = -1;
        aisId = -1;
        
        name = "";
        role = "";
        roleFromDatabase = "";
        
        lastAction = LocalDateTime.now().minusYears(100);
    }
}
