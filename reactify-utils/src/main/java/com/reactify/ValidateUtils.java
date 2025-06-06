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
package com.reactify;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtils {

    public static final String PHONE_REGEX = "((\\+84|84|0)+(3|5|7|8|9|1|2[2|4|6|8|9]))+([0-9]{8,9})\\b";
    public static final String NUMBER_REGEX = "\\d+";
    public static final String EMAIL_REGEX = "^(?![-_.@])[\\w.-]+(?<![-_.@])@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9]{2,}$";
    public static final String OTP_REGEX = "^\\d{6}$";
    public static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";
    public static final String UTF8_REGEX = "^[ -~]{8,}$";
    public static final String CAMELCASE = "([a-z])([A-Z]+)";
    public static final String DATE_FORMAT = "^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$";
    public static final String PRODUCT_ID = "^[a-zA-Z0-9\\s-]*$";
    public static final String LINK =
            "^(http:\\/\\/|https:\\/\\/)(www\\.)?[a-zA-Z0-9@:%._\\+~#=]{2,256}(\\.[a-z]{2,6})?(:\\d+)?\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)$";
    public static final String TAX_CODE_REGEX = "^\\d\\d{9}$|^\\d\\d{12}$";
    public static final String PHONE_NUMBER_FORMAT =
            "^8496\\d{7}$|^8497\\d{7}$|^8498\\d{7}$|^8416\\d{8}$|0?96\\d{7}$|0?97\\d{7}$|^0?98\\d{7}$|^0?16\\d{8}$";

    /**
     * Validates the input string against the given regular expression pattern.
     *
     * @param input
     *            the input string to be validated
     * @param regexPattern
     *            the regular expression pattern to validate against
     * @return true if the input matches the pattern, false otherwise
     */
    public static boolean validateRegex(String input, String regexPattern) {
        return !DataUtil.isNullOrEmpty(input) && input.matches(regexPattern);
    }

    /**
     * Validates the given phone number. Checks if the phone number is numeric and
     * has a length between 9 and 11 characters.
     *
     * @param phone
     *            the phone number to be validated
     * @return true if the phone number is valid, false otherwise
     */
    public static boolean validatePhone(String phone) {
        if (DataUtil.isNullOrEmpty(phone)) {
            return true;
        }
        if (!phone.matches(NUMBER_REGEX)) {
            return false;
        }

        return phone.length() >= 9 && phone.length() <= 11;
    }

    /**
     * Validates the given UUID string. Checks if the string is a valid UUID.
     *
     * @param uuidValue
     *            the UUID string to be validated
     * @return true if the UUID is valid, false otherwise
     */
    public static boolean validateUUID(String uuidValue) {
        if (DataUtil.isNullOrEmpty(uuidValue)) {
            return false;
        }
        try {
            return DataUtil.safeEqual(UUID.fromString(uuidValue).toString(), uuidValue);
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Validates the given URL string. Checks if the string matches the URL pattern.
     *
     * @param link
     *            the URL string to be validated
     * @return true if the URL is valid, false otherwise
     */
    public static boolean validateLink(String link) {
        if (DataUtil.isNullOrEmpty(link)) {
            return false;
        }

        Pattern pattern = Pattern.compile(LINK);
        Matcher matcher = pattern.matcher(link);

        return matcher.matches();
    }
}
