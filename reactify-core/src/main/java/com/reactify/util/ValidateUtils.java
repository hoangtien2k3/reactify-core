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

import static com.reactify.constants.Regex.LINK;
import static com.reactify.constants.Regex.NUMBER_REGEX;

import java.util.UUID;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for validating various types of input. Provides methods to
 * validate strings against regular expressions, phone numbers, UUIDs, and URLs.
 *
 * @author hoangtien2k3
 */
@Slf4j
public class ValidateUtils {

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
            log.error("invalid uuid: ", ex);
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
        return Pattern.compile(LINK).matcher(link).matches();
    }
}
