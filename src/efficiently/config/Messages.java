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
 * <h1>Messages Class</h1>
 * Governs setting and returning various types of messages used throughout
 *
 * @author Michal Kaštan <github.com/BloodyBogan> & Ladislav Capalaj
 * @version 1.0.0
 * @since 2020-11-23
 */
public class Messages {
    private static ArrayList<String> general;
    private static ArrayList<String> errors;
    private static ArrayList<String> validationErrors;
    private static ArrayList<String> inputValidationErrors;
    private static ArrayList<String> headers;
    
    /**
     * Initializes ArrayLists ans calls their corresponding setters
     */
    public static void init() {
        general = new ArrayList<>();
        setGeneral();
        
        errors = new ArrayList<>();
        setErrors();
        
        validationErrors = new ArrayList<>();
        setValidationErrors();
        
        inputValidationErrors = new ArrayList<>();
        setInputValidationErrors();
        
        headers = new ArrayList<>();
        setHeaders();
    }
    
    /**
     * Adds all the general messages to its corresponding ArrayList
     */
    private static void setGeneral () {
        general.add("Unable to connect to the database");                                           // 0
        general.add("You have been successfully signed up! Please log in");                         // 1
        general.add("You have been successfully logged in");                                        // 2
        general.add("Hello, %s");                                                                   // 3
        general.add("There are no available dates & times");                                        // 4
        general.add("Are you sure you want to %s this %s?");                                        // 5
        general.add("%s %s");                                                                       // 6
        general.add("%s has been successfully %s");                                                 // 7
        general.add("You haven't made any dates & times available yet");                            // 8
        general.add("You have no appointments booked yet");                                         // 9
        general.add("It's your turn");                                                              // 10
        general.add("There %s %s %s before yours");                                                 // 11
    };
    
    /**
     * Adds all the error messages to its corresponding ArrayList
     */
    private static void setErrors () {
        errors.add("There was an error");                                                           // 0
        errors.add("Database error");                                                               // 1
        errors.add("Your session has expired");                                                     // 2
        errors.add("You can't book an appointment now as there are no available dates & times");    // 3
        errors.add("You can't delete any date & time now as you haven't made any available yet");   // 4
        errors.add("You can't have more than 2 active appointments");                               // 5
    }
    
    /**
     * Adds all the validation error messages to its corresponding ArrayList
     */
    private static void setValidationErrors () {
        validationErrors.add("AIS ID (%d) is already in use");              // 0
        validationErrors.add("Incorrect credentials");                      // 1
        validationErrors.add("You have already added this date & time");    // 2
    }
    
    /**
     * Adds all the input validation error messages to its corresponding ArrayList
     */
    private static void setInputValidationErrors () {
        inputValidationErrors.add("%s must not be emtpy");                          // 0
        inputValidationErrors.add("AIS ID must not be longer than 6 digits");       // 1
        inputValidationErrors.add("AIS ID must be of a type number");               // 2
        inputValidationErrors.add("%s must be at least %d characters long");        // 3
        inputValidationErrors.add("%s must not be longer than %d characters");      // 4
        inputValidationErrors.add("Passwords do not match");                        // 5 
        inputValidationErrors.add("Please enter a valid %s");                       // 6 
        inputValidationErrors.add("You can't travel back in time");                 // 7
        inputValidationErrors.add("You must select a row to %s %s");                // 8
    }
    
    /**
     * Adds all the header messages to its corresponding ArrayList
     */
    private static void setHeaders () {
        headers.add("Error");               // 0
        headers.add("Information");         // 1
    }
    
    /**
     * Returns the requested general message at the requested index
     * 
     * @param index of the general messages in the corresponding ArrayList
     * 
     * @return requested general message at the given index
     */
    public static String getGeneral (int index) {
        return general.get(index);
    }
    
    /**
     * Returns the requested error message at the requested index
     * 
     * @param index of the error messages in the corresponding ArrayList
     * 
     * @return requested error message at the given index
     */
    public static String getError (int index) {
        return errors.get(index);
    }
    
    /**
     * Returns the requested validation error message at the requested index
     * 
     * @param index of the validation error messages in the corresponding ArrayList
     * 
     * @return requested validation error message at the given index
     */
    public static String getValidationError (int index) {
        return validationErrors.get(index);
    }
    
    /**
     * Returns the requested input validation error message at the requested index
     * 
     * @param index of the input validation error messages in the corresponding ArrayList
     * 
     * @return requested input validation error message at the given index
     */
    public static String getInputValidationError (int index) {
        return inputValidationErrors.get(index);
    }
    
    /**
     * Returns the requested header message at the requested index
     * 
     * @param index of the header messages in the corresponding ArrayList
     * 
     * @return requested header message at the given index
     */
    public static String getHeaders (int index) {
        return headers.get(index);
    }
}
