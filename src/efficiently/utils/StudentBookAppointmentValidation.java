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
import efficiently.views.MainLayout;
import efficiently.views.dashboard.Student;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Michal Kaštan <github.com/BloodyBogan> & Ladislav Capalaj
 */
public class StudentBookAppointmentValidation {
    private static Object[] values;
    
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
