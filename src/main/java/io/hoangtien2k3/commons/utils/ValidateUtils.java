package io.hoangtien2k3.commons.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.hoangtien2k3.commons.constants.Regex.LINK;
import static io.hoangtien2k3.commons.constants.Regex.NUMBER_REGEX;

/**
 * Utility class for validating various types of input.
 * Provides methods to validate strings against regular expressions, phone numbers, UUIDs, and URLs.
 */
@Slf4j
public class ValidateUtils {

    /**
     * Validates the input string against the given regular expression pattern.
     *
     * @param input the input string to be validated
     * @param regexPattern the regular expression pattern to validate against
     * @return true if the input matches the pattern, false otherwise
     */
    public static boolean validateRegex(String input, String regexPattern) {
        return !DataUtil.isNullOrEmpty(input) && input.matches(regexPattern);
    }

    /**
     * Validates the given phone number.
     * Checks if the phone number is numeric and has a length between 9 and 11 characters.
     *
     * @param phone the phone number to be validated
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
     * Validates the given UUID string.
     * Checks if the string is a valid UUID.
     *
     * @param uuidValue the UUID string to be validated
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
     * Validates the given URL string.
     * Checks if the string matches the URL pattern.
     *
     * @param link the URL string to be validated
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
