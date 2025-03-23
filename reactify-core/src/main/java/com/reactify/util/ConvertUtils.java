/*
 * Copyright 2024-2025 the original author Hoàng Anh Tiến
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.reactify.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class providing methods for string manipulation, number conversion,
 * and comparison operations.
 */
public class ConvertUtils {

    private static final Logger log = LogManager.getLogger(ConvertUtils.class);
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.###");

    /**
     * Converts a raw string value into a JSON string format. If the input is null
     * or empty, returns an empty JSON string.
     *
     * @param valueRaw
     *            the raw string to be converted
     * @return the converted JSON string
     */
    public static String convertStringJsonRawValue(String valueRaw) {
        return !DataUtil.isNullOrEmpty(valueRaw) ? "\"".concat(valueRaw).concat("\"") : "\"\"";
    }

    /**
     * Removes all HTML/XML tags from the given string.
     *
     * @param str
     *            the input string containing potential tags
     * @return the string without any tags
     */
    public static String stripTags(String str) {
        int startPosition = str.indexOf('<');
        int endPosition;
        while (startPosition != -1) {
            endPosition = str.indexOf('>', startPosition);
            str = str.substring(0, startPosition) + (endPosition != -1 ? str.substring(endPosition + 1) : "");
            startPosition = str.indexOf('<');
        }
        return str;
    }

    /**
     * Converts a {@link Number} to a formatted string with custom separators. The
     * grouping separator is '.' and the decimal separator is ','. Returns "0" if
     * the input is null.
     *
     * @param number
     *            the {@link Number} to be formatted
     * @return the formatted string representation of the number
     */
    public static String convertNumber(Number number) {
        if (number == null) {
            return "0";
        } else {
            DecimalFormat decimalFormat = new DecimalFormat();
            DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
            symbols.setGroupingSeparator('.');
            symbols.setDecimalSeparator(',');
            decimalFormat.setDecimalFormatSymbols(symbols);
            return decimalFormat.format(number);
        }
    }

    /**
     * Converts a string representation of a number to a {@link Number} object. If
     * the input is null or cannot be parsed, returns 0.
     *
     * @param number
     *            the string representation of the number
     * @return the parsed {@link Number} object
     */
    public static Number convertNumber(String number) {
        if (number == null) {
            return 0;
        } else {
            try {
                return DECIMAL_FORMAT.parse(number);
            } catch (ParseException e) {
                return 0;
            }
        }
    }

    /**
     * Compares an object with a target string for equality. Returns true if the
     * object is non-null and its string representation is equal to the target
     * string.
     *
     * @param obj
     *            the object to compare
     * @param target
     *            the target string to compare with
     * @return {@code true} if the object equals the target string, {@code false}
     *         otherwise
     */
    public static boolean compareSpecial(Object obj, String target) {
        return obj != null && obj.toString().equals(target);
    }

    /**
     * Determines the registration status of a package based on its code. Returns:
     * <ul>
     * <li>3 if the package is in the canceled list</li>
     * <li>1 if the package is in the currently used list</li>
     * <li>0 if the package is in neither list</li>
     * </ul>
     *
     * @param code
     *            the package code to check
     * @param packUsing
     *            the list of currently used packages
     * @param canceledList
     *            the list of canceled packages
     * @return the registration status of the package
     */
    public static int getRegPackage(String code, List<String> packUsing, List<String> canceledList) {
        if (canceledList.contains(code)) {
            return 3;
        } else if (packUsing.contains(code)) {
            return 1;
        } else {
            return 0;
        }
    }
}
