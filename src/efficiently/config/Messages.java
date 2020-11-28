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

import java.util.ArrayList;

/**
 *
 * @author Michal Kaštan <github.com/BloodyBogan> & Ladislav Capalaj
 */
public class Messages {
    private static ArrayList<String> general;
    private static ArrayList<String> errors;
    private static ArrayList<String> validationErrors;
    private static ArrayList<String> inputValidationErrors;
    
    public static void init() {
        general = new ArrayList<>();
        setGeneral();
        
        errors = new ArrayList<>();
        setErrors();
        
        validationErrors = new ArrayList<>();
        setValidationErrors();
        
        inputValidationErrors = new ArrayList<>();
        setInputValidationErrors();
    }
    
    private static void setGeneral () {
        general.add("Unable to connect to the database!");              // 0
        general.add("Registration successful");                         // 1
        general.add("Login successful");                                // 2
        general.add("Hello, %s");                                       // 3
        general.add("There are no available dates & times");            // 4
        general.add("Are you sure you want to %s this %s?");            // 5
        general.add("%s %s");                                           // 6
        general.add("%s has been %s successfully");                     // 7
        general.add("You don't have any available dates & times");      // 8
        general.add("You have no appointments booked yet");             // 9
        general.add("It's your turn");                                  // 10
        general.add("There %s %s %s before yours");                     // 11
    };
    
    private static void setErrors () {
        errors.add("There was an error");                                                           // 0
        errors.add("Database error");                                                               // 1
        errors.add("Your session has expired");                                                     // 2
        errors.add("You can't book an appointment now as there are no available dates & times");    // 3
        errors.add("You can't delete the date & time now as you haven't set any yet");              // 4
    }
    
    private static void setValidationErrors () {
        validationErrors.add("AIS ID (%d) is already registered");          // 0
        validationErrors.add("Incorrect credentials");                      // 1
        validationErrors.add("You already have added this date & time");    // 2
    }
    
    private static void setInputValidationErrors () {
        inputValidationErrors.add("%s must not be emtpy");                          // 0
        inputValidationErrors.add("AIS ID must not be longer than 6 digits");       // 1
        inputValidationErrors.add("AIS ID must be of a type number");               // 2
        inputValidationErrors.add("%s must be at least %d characters long");        // 3
        inputValidationErrors.add("%s must not be longer than %d characters");      // 4
        inputValidationErrors.add("Passwords do not match");                        // 5 
        inputValidationErrors.add("Please enter a valid %s");                       // 6 
        inputValidationErrors.add("You can't travel back in time");                 // 7
    }
    
    public static String getGeneral (int index) {
        return general.get(index);
    }
    
    public static String getError (int index) {
        return errors.get(index);
    }
    
    public static String getValidationError (int index) {
        return validationErrors.get(index);
    }
    
    public static String getInputValidationError (int index) {
        return inputValidationErrors.get(index);
    }
}
