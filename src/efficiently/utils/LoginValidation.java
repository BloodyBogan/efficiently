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
package efficiently.utils;

import efficiently.config.Messages;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Michal Kaštan <github.com/BloodyBogan> & Ladislav Capalaj
 */
public class LoginValidation {
    private static Object[] values;
    
    public static Object[] validate (JTextField aisIdField, JPasswordField passwordField) throws ValidationException {
        String stringAisId = aisIdField.getText().trim();        
        if (stringAisId.isEmpty()) {
            aisIdField.setText("");
            
            aisIdField.requestFocus();
            
            throw new ValidationException(String.format(Messages.getInputValidationError(0), "AIS ID"));
        }
        
        if (stringAisId.length() > 6) {
            aisIdField.setText("");
            
            aisIdField.requestFocus();
            
            throw new ValidationException(Messages.getInputValidationError(1));
        }

        int aisId;
        try {
            aisId = Integer.parseInt(stringAisId);
        } catch(NumberFormatException e) {
            aisIdField.setText("");
            
            aisIdField.requestFocus();
            
            throw new ValidationException(Messages.getInputValidationError(2));
        }
        
        char[] charPassword = passwordField.getPassword();
        String stringPassword = String.valueOf(charPassword).trim();
        if (stringPassword.isEmpty()) {
            passwordField.setText("");
            
            passwordField.requestFocus();
            
            throw new ValidationException(String.format(Messages.getInputValidationError(0), "Password"));
        }
              
        int passwordMin = 8;
        if (stringPassword.length() < passwordMin) {
            passwordField.setText("");
            
            passwordField.requestFocus();
            
            throw new ValidationException(String.format(Messages.getInputValidationError(3), "Password", passwordMin));
        }
        
        int passwordMax = 60;
        if (stringPassword.length() > passwordMax) {
            passwordField.setText("");
            
            passwordField.requestFocus();
            
            throw new ValidationException(String.format(Messages.getInputValidationError(4), "Password", passwordMax));
        }
        char[] password = stringPassword.toCharArray();
        
        values = new Object[] { aisId, password };
        return values;
    }
}
