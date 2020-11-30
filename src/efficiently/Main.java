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
package efficiently;

import efficiently.config.Database;
import efficiently.config.Messages;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;
import efficiently.controllers.MenuController;
import efficiently.models.User;
import efficiently.views.MainLayout;
import javax.swing.JOptionPane;

/**
 * <h1>Main Class</h1>
 * Governs initialization
 * 
 * <p>Efficiently manage your study department visits</p>
 * 
 * @author Michal Kaštan <github.com/BloodyBogan> & Ladislav Capalaj
 * @version 1.0.0
 * @since 2020-11-23
 */
public class Main {
    /**
     * Initializes messages and tries to connect to the database
     * If that's successful, the user model is reset and the main menu screen is shown
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Messages.init();
        
        try (Connection conn = Database.getConnection()) {
            System.out.println(String.format("Connected to database %s "
                    + "successfully.", conn.getCatalog()));
            
            User.logout();
            
            MenuController.init();
        } catch (SQLException se) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getGeneral(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            System.exit(0); 
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            System.exit(0); 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            System.exit(0); 
        }
    }
}
