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
 * <h1>SignupValidation Class</h1>
 * Governs the signup validation
 *
 * @author Michal Kaštan <github.com/BloodyBogan> & Ladislav Capalaj
 * @version 1.0.0
 * @since 2020-11-28
 */
public class SignupValidation {
    private static Object[] values;
    
    /**
     * Makes sure all the input is correct
     * Resets input throughout as needed
     *
     * @param aisIdField
     * @param nameField
     * @param passwordField
     * @param confirmPasswordField
     * 
     * @return the validated input
     * 
     * @throws ValidationException if the input is incorrect
     */
    public static Object[] validate (JTextField aisIdField, JTextField nameField, JPasswordField passwordField, JPasswordField confirmPasswordField) throws ValidationException {
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
        } catch(NumberFormatException nfe) {
            aisIdField.setText("");
            
            aisIdField.requestFocus();
            
            throw new ValidationException(Messages.getInputValidationError(2));
        }
        
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            nameField.setText("");
            
            nameField.requestFocus();
            
            throw new ValidationException(String.format(Messages.getInputValidationError(0), "Name"));
        }
                
        int nameMax = 50;
        if (name.length() > nameMax) {
            nameField.setText("");
            
            nameField.requestFocus();
            
            throw new ValidationException(String.format(Messages.getInputValidationError(4), "Name", nameMax));
        }
        
        char[] charPassword = passwordField.getPassword();
        String stringPassword = String.valueOf(charPassword).trim();
        if (stringPassword.isEmpty()) {
            passwordField.setText("");
            confirmPasswordField.setText("");
            
            passwordField.requestFocus();
            
            throw new ValidationException(String.format(Messages.getInputValidationError(0), "Password"));
        }
                
        int passwordMin = 8;
        if (stringPassword.length() < passwordMin) {
            passwordField.setText("");
            confirmPasswordField.setText("");
            
            passwordField.requestFocus();
            
            throw new ValidationException(String.format(Messages.getInputValidationError(3), "Password", passwordMin));
        }
        
        int passwordMax = 60;
        if (stringPassword.length() > passwordMax) {
            passwordField.setText("");
            confirmPasswordField.setText("");
            
            passwordField.requestFocus();
            
            throw new ValidationException(String.format(Messages.getInputValidationError(4), "Password", passwordMax));
        }
        char[] password = stringPassword.toCharArray();
        
        char[] confirmPassword = confirmPasswordField.getPassword();
        String stringConfirmPassword = String.valueOf(confirmPassword);
        if (!stringPassword.equals(stringConfirmPassword)) {
            passwordField.setText("");
            confirmPasswordField.setText("");
            
            passwordField.requestFocus();
            
            throw new ValidationException(Messages.getInputValidationError(5));
        }
        
        values = new Object[] { aisId, name, password };
        return values;
    }
}
