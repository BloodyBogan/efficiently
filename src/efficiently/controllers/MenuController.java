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
import efficiently.models.User;
import efficiently.utils.Capitalize;
import efficiently.utils.UserException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import javax.swing.JOptionPane;

/**
 *
 * @author Michal Kaštan <github.com/BloodyBogan> & Ladislav Capalaj
 */
public class MenuController {

    public static void init() throws SQLException, IOException {
        MainLayout Main = new MainLayout();
        Main.setVisible(true);
    }
    
    public static void signup(int aisId, String name, char[] password) throws SQLException, IOException {
        String formattedName = Capitalize.capitalizeName(name);
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password);
        
        String sqlQuery = "INSERT INTO users (ais_id, name, password) VALUES (?, ?, ?)";
        
        try (Connection conn = Database.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {

            pstmt.setInt(1, aisId);
            pstmt.setString(2, formattedName);
            pstmt.setString(3, hashedPassword);
            
            pstmt.execute();
            
            JOptionPane.showMessageDialog(null, "Registration Successful");
            MainLayout.showLoginScreen();
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "AIS ID (" + aisId + ") is already registered");
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
    }
    
    public static void login(int aisId, char[] password) throws SQLException, IOException {
        String sqlQuery = "SELECT users.user_id, users.ais_id, users.name, users.password, user_role.role from users, user_role WHERE (ais_id=? AND users.role=user_role.role_id) LIMIT 1";
        try (Connection conn = Database.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            
            pstmt.setInt(1, aisId);
        
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                BCrypt.Result result = BCrypt.verifyer().verify(password, hashedPassword);

                if (!result.verified) {
                    throw new UserException("Incorrect Credentials");
                }
                
                User.setUserId(rs.getInt("user_id"));
                User.setAisId(rs.getInt("ais_id"));
                User.setName(rs.getString("name"));
                User.setRole(rs.getString("role"));
                User.setLastAction();
            } else {
                throw new UserException("Incorrect Credentials");
            }
            
            JOptionPane.showMessageDialog(null, "Login Successful");
            switch(User.getRole()) {
                case "student":
                    MainLayout.showStudentDashboard();
                    break;
                case "referent":
                    MainLayout.showReferentDashboard();
                    break;
                case "admin":
                    MainLayout.showAdminDashboard();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "There was an error. Try again");
                    DashboardController.logout();
            }
        } catch (SQLException se) {
            se.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error. Try again");
        } catch (UserException ue) {
            ue.printStackTrace();
            JOptionPane.showMessageDialog(null, ue.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "There was an error. Try again");
        }
    }
}
