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

import efficiently.views.MainLayout;
import at.favre.lib.crypto.bcrypt.BCrypt;
import efficiently.config.Database;
import efficiently.config.Messages;
import efficiently.models.User;
import efficiently.utils.Capitalize;
import efficiently.utils.ValidationException;
import efficiently.views.dashboard.Admin;
import efficiently.views.dashboard.Correspondent;
import efficiently.views.dashboard.Student;
import efficiently.views.menu.Login;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import javax.swing.JOptionPane;

/**
 * <h1>MenuController Class</h1>
 * Governs the menu functionality
 *
 * @author Michal Kaštan <github.com/BloodyBogan> & Ladislav Capalaj
 * @version 1.0.0
 * @since 2020-11-23
 */
public class MenuController {

    /**
     * Initializes the MainLayout for all the other views to be displayed in
     * Sets MainLayout visible 
     */
    public static void init() {
        MainLayout Main = new MainLayout();
        
        Main.setVisible(true);
    }
    
    /**
     * Inserts a new user into the database
     *
     * @param aisId
     * @param name
     * @param password
     */
    public static void signup(int aisId, String name, char[] password) {
        String formattedName = Capitalize.capitalizeName(name);
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password);
        
        String sqlInsertUser = "INSERT INTO users (ais_id, name, password) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlInsertUser)) {

            pstmt.setInt(1, aisId);
            pstmt.setString(2, formattedName);
            pstmt.setString(3, hashedPassword);
            
            pstmt.execute();
            
            MainLayout.showLoginScreen();
            
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getGeneral(1), Messages.getHeaders(1), JOptionPane.INFORMATION_MESSAGE);
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
     * Logs in the user
     * Checks whether the user even exists or not
     * Checks whether the password matches the one of the user
     * Calls User's setters
     * Displays a different dashboard depending on user's role
     * 
     * @param aisId
     * @param password
     */
    public static void login(int aisId, char[] password) {
        String sqlRetrieveUser = "SELECT users.user_id, users.ais_id, users.name, users.password, user_role.role from users, user_role WHERE (ais_id=? AND users.role=user_role.role_id) LIMIT 1";
        try (Connection conn = Database.getConnection()) {
             PreparedStatement pstmt = conn.prepareStatement(sqlRetrieveUser);
             
            pstmt.setInt(1, aisId);
        
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                BCrypt.Result result = BCrypt.verifyer().verify(password, hashedPassword);

                if (!result.verified) {
                    throw new ValidationException(Messages.getValidationError(1));
                }
                
                User.setUserId(rs.getInt("user_id"));
                User.setAisId(rs.getInt("ais_id"));
                User.setName(rs.getString("name"));
                User.setRole(rs.getString("role"));
                
                User.setLastAction();
            } else {
                throw new ValidationException(Messages.getValidationError(1));
            }
            
            switch(User.getRole()) {
                case "student":
                    MainLayout.showStudentDashboard();
                    
                    Student.setUserName();
                    Student.refresh();
                    
                    break;
                case "correspondent":
                    MainLayout.showCorrespondentDashboard();
                    
                    Correspondent.setUserName();
                    Correspondent.refresh();
                    
                    break;
                case "admin":
                    MainLayout.showAdminDashboard();
                    
                    Admin.setUserName();
                    Admin.refresh();
                    
                    break;
                default:
                    JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
                    
                    DashboardController.logout();
            }
            
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getGeneral(2), Messages.getHeaders(1), JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(1), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (ValidationException ve) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), ve.getMessage(), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
        } finally {
            Login.reset();
        }
    }
}
