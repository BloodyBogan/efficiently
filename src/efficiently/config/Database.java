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
package efficiently.config;

import efficiently.views.MainLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

/**
 *
 * @author Michal Kaštan <github.com/BloodyBogan> & Ladislav Capalaj
 */
public class Database {
    private final static String CURRENT_DIRECTORY = System.getProperty("user.dir");
    
    private final static File DATABASE_PROPERTIES_PATH = new File(CURRENT_DIRECTORY + "/resources/database.properties");
    
    public static Connection getConnection() throws SQLException, IOException {        
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("196048a3d48f928e02b156f9a74dd3f58b371da2359f2e063b33ae686d82b191"); // could be got from web, env variable...
        
        Connection conn = null;
        try (FileInputStream f = new FileInputStream(DATABASE_PROPERTIES_PATH)) {
            Properties props = new EncryptableProperties(encryptor);
            props.load(f);

            String url = props.getProperty("url");
            String user = props.getProperty("user");
            String password = props.getProperty("password");
            
            conn = DriverManager.getConnection(url, user, password);
        } catch (FileNotFoundException fnfe) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            System.exit(0); 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainLayout.getJPanel(), Messages.getError(0), Messages.getHeaders(0), JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        
        return conn;
    } 
}
