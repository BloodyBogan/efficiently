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
import efficiently.views.dashboard.Student;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * <h1>StudentBookAppointmentValidation Class</h1>
 * Governs the student book appointment validation
 *
 * @author Michal Kaštan <github.com/BloodyBogan> & Ladislav Capalaj
 * @version 1.0.0
 * @since 2020-11-28
 */
public class StudentBookAppointmentValidation {
    private static Object[] values;
    
    /**
     * Makes sure all the input is correct
     * Resets input throughout as needed
     *
     * @param subjectField
     * @param messageTextArea
     * @param dateTimeComboBox
     * 
     * @return the validated input
     * 
     * @throws ValidationException if the input is incorrect
     */
    public static Object[] validate (JTextField subjectField, JTextArea messageTextArea, JComboBox<String> dateTimeComboBox) throws ValidationException {
        String subject = subjectField.getText().trim();        
        if (subject.isEmpty()) {
            subjectField.setText("");
            
            subjectField.requestFocus();
            
            throw new ValidationException(String.format(Messages.getInputValidationError(0), "Subject"));
        }
        
        int subjectMax = 255;
        if (subject.length() > subjectMax) {
            subjectField.setText("");
            
            subjectField.requestFocus();
            
            throw new ValidationException(String.format(Messages.getInputValidationError(4), "Subject", subjectMax));
        }

        String message = messageTextArea.getText().trim();        
        if (message.isEmpty()) {
            messageTextArea.setText("");
            
            messageTextArea.requestFocus();
            
            throw new ValidationException(String.format(Messages.getInputValidationError(0), "Message"));
        }
        
        int messageMax = 500;
        if (message.length() > messageMax) {
            messageTextArea.setText("");
            
            messageTextArea.requestFocus();
            
            throw new ValidationException(String.format(Messages.getInputValidationError(4), "Message", messageMax));
        }
        
        String comboBoxItem = dateTimeComboBox.getSelectedItem().toString();
        if (comboBoxItem.equals(Messages.getGeneral(4))) {
            Student.refresh();       
            
            throw new ValidationException(Messages.getError(3));
        }
        
        values = new Object[] { subject, message };
        return values;
    }
}
