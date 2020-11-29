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

import com.github.lgooddatepicker.components.DateTimePicker;
import efficiently.config.Messages;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 *
 * @author Michal Kaštan <github.com/BloodyBogan> & Ladislav Capalaj
 */
public class CorrespondentDateTimeAddValidation {
    private static Object[] values;
    
    public static Object[] validate (DateTimePicker addDateTimePicker) throws ValidationException {
        String date = addDateTimePicker.getDatePicker().toString();
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException | NullPointerException e) {
            throw new ValidationException(String.format(Messages.getInputValidationError(6), "date"));
        }
        
        String time = addDateTimePicker.getTimePicker().toString();
        try {
            DateTimeFormatter strictTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);
            LocalTime.parse(time, strictTimeFormatter);
        } catch (DateTimeParseException | NullPointerException e) {
            throw new ValidationException(String.format(Messages.getInputValidationError(6), "time"));
        }
        
        String dateTime = date + " " + time;

        LocalDateTime t = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if (t.isAfter(LocalDateTime.parse(dateTime, formatter))) {
            throw new ValidationException(Messages.getInputValidationError(7));
        }
        
        values = new Object[] { dateTime };
        return values;
    }
}
