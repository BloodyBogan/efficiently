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

/**
 * <h1>Capitalize Class</h1>
 * Governs capitalization and formatting of user's name
 *
 * @author Michal Kaštan <github.com/BloodyBogan> & Ladislav Capalaj
 * @version 1.0.0
 * @since 2020-11-23
 */
public class Capitalize {

    /**
     * Capitalizes and formats user's name
     *
     * @param string
     * 
     * @return capitalized and formatted name
     */
    public static String capitalizeName(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        
        boolean found = false;
        
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') {
                found = false;
            }
        }
        
        return String.valueOf(chars);
    }
}
