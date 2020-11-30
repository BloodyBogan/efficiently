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
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JTextArea;

/**
 * <h1>CorrespondentAppointmentUpdateValidation Class</h1>
 * Governs the correspondent appointment update validation
 *
 * @author Michal Kaštan <github.com/BloodyBogan> & Ladislav Capalaj
 * @version 1.0.0
 * @since 2020-11-28
 */
public class CorrespondentAppointmentUpdateValidation {
    private static Object[] values;
    
    /**
     * Makes sure all the input is correct
     * Resets input throughout as needed
     *
     * @param appointmentsTable
     * @param manageResponseTextArea
     * @param manageClosedCheckBox
     * 
     * @return the validated input
     * 
     * @throws ValidationException if the input is incorrect
     */
    public static Object[] validate (JTable appointmentsTable, JTextArea manageResponseTextArea, JCheckBox manageClosedCheckBox) throws ValidationException {
        boolean isRowNotSelected = appointmentsTable.getSelectionModel().isSelectionEmpty();
        if (isRowNotSelected) {
            manageResponseTextArea.setText("");
            
            manageClosedCheckBox.setSelected(false);
            
            manageResponseTextArea.requestFocus();
            
            throw new ValidationException(String.format(Messages.getInputValidationError(8), "update", "appointment"));
        }
        
        String response = manageResponseTextArea.getText().trim();        
        if (response.isEmpty()) {
            manageResponseTextArea.setText("");
            
            manageResponseTextArea.requestFocus();
            
            throw new ValidationException(String.format(Messages.getInputValidationError(0), "Response"));
        }
        
        int responseMax = 500;
        if (response.length() > responseMax) {
            manageResponseTextArea.setText("");
            
            manageResponseTextArea.requestFocus();
            
            throw new ValidationException(String.format(Messages.getInputValidationError(4), "Response", responseMax));
        }
        
        values = new Object[] { response };
        return values;
    }
}
