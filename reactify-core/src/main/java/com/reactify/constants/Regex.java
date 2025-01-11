/*
 * Copyright 2024-2025 the original author Hoàng Anh Tiến.
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
package com.reactify.constants;

/**
 * <p>
 * This class provides a set of regular expressions for validating various types
 * of input data commonly used in applications, such as phone numbers, emails,
 * and URLs. The constants in this class are predefined patterns that can be
 * used directly for validation purposes in the application.
 * </p>
 *
 * <p>
 * Each regex pattern is documented to describe its purpose and the format it
 * validates.
 * </p>
 *
 * @author hoangtien2k3
 */
public class Regex {

    /**
     * Constructs a new instance of {@code Regex}.
     */
    public Regex() {}

    /**
     * Regular expression to validate Vietnamese phone numbers.
     * <p>
     * This regex supports phone numbers starting with "+84", "84", or "0" and
     * followed by a valid Vietnamese mobile phone prefix (e.g., 3, 5, 7, 8, 9, or
     * specific two-digit numbers).
     * </p>
     * Example matches: "+84987654321", "0987654321", "84876543210"
     */
    public static final String PHONE_REGEX = "((\\+84|84|0)+(3|5|7|8|9|1|2[2|4|6|8|9]))+([0-9]{8,9})\\b";

    /**
     * Regular expression to validate numerical inputs.
     * <p>
     * Matches any sequence of digits. Suitable for general numeric validation.
     * </p>
     * Example matches: "123", "45678"
     */
    public static final String NUMBER_REGEX = "\\d+";

    /**
     * Regular expression to validate email addresses.
     * <p>
     * Ensures the email starts and ends with alphanumeric characters, allowing
     * certain special characters like periods and hyphens in between. The domain
     * part must follow standard email domain formatting.
     * </p>
     * Example matches: "user@example.com", "my-email@domain.co"
     */
    public static final String EMAIL_REGEX = "^(?![-_.@])[\\w.-]+(?<![-_.@])@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9]{2,}$";

    /**
     * Regular expression to validate One-Time Passwords (OTP).
     * <p>
     * Matches exactly 6 digits, commonly used in OTP scenarios.
     * </p>
     * Example matches: "123456"
     */
    public static final String OTP_REGEX = "^\\d{6}$";

    /**
     * Regular expression to validate passwords.
     * <p>
     * Ensures the password contains at least one lowercase letter, one uppercase
     * letter, and one digit, and is a minimum of 8 characters in length.
     * </p>
     * Example matches: "Password1", "Abc12345"
     */
    public static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";

    /**
     * Regular expression for UTF-8 validation with ASCII characters.
     * <p>
     * Matches a sequence of visible ASCII characters with a minimum length of 8.
     * </p>
     * Example matches: "HelloWorld", "12345678"
     */
    public static final String UTF8_REGEX = "^[ -~]{8,}$";

    /**
     * Regular expression to identify camel case patterns.
     * <p>
     * This regex matches words in camel case format, often used for variable and
     * method names in code.
     * </p>
     * Example matches: "camelCase", "testStringExample"
     */
    public static final String CAMELCASE = "([a-z])([A-Z]+)";

    /**
     * Regular expression to validate date format in dd/MM/yyyy.
     * <p>
     * Ensures the date follows the day/month/year format with valid day and month
     * ranges.
     * </p>
     * Example matches: "12/12/2023", "01/01/2000"
     */
    public static final String DATE_FORMAT = "^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$";

    /**
     * Regular expression to validate product IDs.
     * <p>
     * Matches alphanumeric characters, spaces, and hyphens, allowing for
     * descriptive product codes.
     * </p>
     * Example matches: "Product-123", "A1B2 C3"
     */
    public static final String PRODUCT_ID = "^[a-zA-Z0-9\\s-]*$";

    /**
     * Regular expression to validate URLs.
     * <p>
     * Supports both "http" and "https" protocols, with optional "www", and ensures
     * a valid domain and path structure.
     * </p>
     * Example matches: "https://www.example.com", "http://domain.com/path"
     */
    public static final String LINK =
            "^(http:\\/\\/|https:\\/\\/)(www\\.)?[a-zA-Z0-9@:%._\\+~#=]{2,256}(\\.[a-z]{2,6})?(:\\d+)?\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)$";

    /**
     * Regular expression to validate tax codes.
     * <p>
     * Matches tax codes with either 10 or 13 digits, often used in various national
     * tax identification formats.
     * </p>
     * Example matches: "1234567890", "1234567890123"
     */
    public static final String TAX_CODE_REGEX = "^\\d\\d{9}$|^\\d\\d{12}$";

    /**
     * Regular expression to validate Viettel mobile numbers in Vietnam.
     * <p>
     * Supports both old and new prefixes for Viettel, including optional "0" at the
     * start.
     * </p>
     * Example matches: "84961234567", "0971234567"
     */
    public static final String VIETTEL_NUMBER_FORMAT =
            "^8496\\d{7}$|^8497\\d{7}$|^8498\\d{7}$|^8416\\d{8}$|0?96\\d{7}$|0?97\\d{7}$|^0?98\\d{7}$|^0?16\\d{8}$";
}
