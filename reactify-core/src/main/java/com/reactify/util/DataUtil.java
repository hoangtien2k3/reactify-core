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
package com.reactify.util;

import static com.reactify.constants.CommonConstant.DATE_FORMAT_YM2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.reactify.constants.CommonConstant;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.factory.ObjectMapperFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.regex.Pattern;
import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.ObjectUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import reactor.core.publisher.Mono;

/**
 * Utility class for data manipulation and processing. This class contains
 * static methods for various data-related operations.
 */
@Slf4j
public class DataUtil {

    /** Constant <code>FORMAT_YMD</code> */
    public static final SimpleDateFormat FORMAT_YMD = new SimpleDateFormat(CommonConstant.DATE_FORMAT_YMD);
    /** Constant <code>FORMAT_DMY</code> */
    public static final SimpleDateFormat FORMAT_DMY = new SimpleDateFormat(CommonConstant.DATE_FORMAT_DMY);
    /** Constant <code>FORMAT_S_MY</code> */
    public static final SimpleDateFormat FORMAT_S_MY = new SimpleDateFormat(CommonConstant.DATE_FORMAT_S_MY);
    /** Constant <code>FORMAT_S_YM</code> */
    public static final SimpleDateFormat FORMAT_S_YM = new SimpleDateFormat(CommonConstant.DATE_FORMAT_S_YM);
    /** Constant <code>FORMAT_HH_MM</code> */
    public static final SimpleDateFormat FORMAT_HH_MM = new SimpleDateFormat(CommonConstant.DATE_FORMAT_HH_MM);
    /** Constant <code>FORMAT_HH_MM_24</code> */
    public static final SimpleDateFormat FORMAT_HH_MM_24 = new SimpleDateFormat(CommonConstant.DATE_FORMAT_HM);

    /** Constant <code>FORMAT_DMYHMS_HYPHEN</code> */
    public static final SimpleDateFormat FORMAT_DMYHMS_HYPHEN =
            new SimpleDateFormat(CommonConstant.DATE_FORMAT_DMY_HMS);
    /** Constant <code>FORMAT_MDYHMS_12_HOUR</code> */
    public static final SimpleDateFormat FORMAT_MDYHMS_12_HOUR =
            new SimpleDateFormat(CommonConstant.DATE_FORMAT_MDY_HMS_12_HOUR);
    /** Constant <code>NUMBER_DF</code> */
    public static final DecimalFormat NUMBER_DF = new DecimalFormat("#.##");
    /** Constant <code>FORMAT_DMY_HYPHEN</code> */
    public static final SimpleDateFormat FORMAT_DMY_HYPHEN =
            new SimpleDateFormat(CommonConstant.FORMAT_DATE_DMY_HYPHEN);
    /** Constant <code>FORMAT_DMYHMS</code> */
    public static final SimpleDateFormat FORMAT_DMYHMS = new SimpleDateFormat(CommonConstant.DATE_FORMAT_DMYHMS);
    /** Constant <code>FORMAT_DMYHM</code> */
    public static final SimpleDateFormat FORMAT_DMYHM = new SimpleDateFormat(CommonConstant.DATE_FORMAT_DMYHM);
    /** Constant <code>FORMAT_DATE_FORMAT_YM2</code> */
    public static final SimpleDateFormat FORMAT_DATE_FORMAT_YM2 = new SimpleDateFormat(DATE_FORMAT_YM2);
    /** Constant <code>FORMAT_DATE_FORMAT_SHORT_YYYY</code> */
    public static final SimpleDateFormat FORMAT_DATE_FORMAT_SHORT_YYYY =
            new SimpleDateFormat(CommonConstant.DATE_FORMAT_SHORT_YYYY);
    /** Constant <code>FORMAT_YMD_T_HH_MM_SS</code> */
    public static final SimpleDateFormat FORMAT_YMD_T_HH_MM_SS =
            new SimpleDateFormat(CommonConstant.DATE_FORMAT_YMD_T_HH_MM_SS);
    /** Constant <code>FORMAT_YMD_T_HMS</code> */
    public static final SimpleDateFormat FORMAT_YMD_T_HMS = new SimpleDateFormat(CommonConstant.DATE_FORMAT_YMD_T_HMS);
    /** Constant <code>FORMAT_HMS</code> */
    public static final SimpleDateFormat FORMAT_HMS = new SimpleDateFormat(CommonConstant.DATE_FORMAT_HMS);
    /** Constant <code>FORMAT_SHORT</code> */
    public static final SimpleDateFormat FORMAT_SHORT = new SimpleDateFormat(CommonConstant.DATE_FORMAT_SHORT);
    /** Constant <code>FORMAT_HMS_NORMAL</code> */
    public static final SimpleDateFormat FORMAT_HMS_NORMAL =
            new SimpleDateFormat(CommonConstant.DATE_FORMAT_HMS_NORMAL);
    /** Constant <code>FORMAT_YDM_INSTANT</code> */
    public static final SimpleDateFormat FORMAT_YDM_INSTANT =
            new SimpleDateFormat(CommonConstant.DATE_FORMAT_YDM_INSTANT);
    /** Constant <code>FORMAT_DMY_HMS</code> */
    public static final SimpleDateFormat FORMAT_DMY_HMS = new SimpleDateFormat(CommonConstant.DATE_FORMAT_DMY_HMS);
    /** Constant <code>FORMAT_DATE</code> */
    public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat(CommonConstant.DATE_FORMAT);
    /** Constant <code>FORMAT_DATE_2</code> */
    public static final SimpleDateFormat FORMAT_DATE_2 = new SimpleDateFormat(CommonConstant.DATE_FORMAT_2);
    /** Constant <code>FORMAT_DATE_3</code> */
    public static final SimpleDateFormat FORMAT_DATE_3 = new SimpleDateFormat(CommonConstant.DATE_FORMAT_3);
    /** Constant <code>FORMAT_YMD_HMS</code> */
    public static final SimpleDateFormat FORMAT_YMD_HMS = new SimpleDateFormat(CommonConstant.DATE_FORMAT_YMD_HMS);
    /** Constant <code>FORMAT_YMDHMS</code> */
    public static final SimpleDateFormat FORMAT_YMDHMS = new SimpleDateFormat(CommonConstant.DATE_FORMAT_YMDHMS);
    /** Constant <code>FORMAT_YMDH</code> */
    public static final SimpleDateFormat FORMAT_YMDH = new SimpleDateFormat(CommonConstant.DATE_FORMAT_YMDH);
    /** Constant <code>FORMAT_HMDMY</code> */
    public static final SimpleDateFormat FORMAT_HMDMY = new SimpleDateFormat(CommonConstant.DATE_TIME_FORMAT_HMDMY);
    /** Constant <code>FORMAT_YM2</code> */
    public static final SimpleDateFormat FORMAT_YM2 = new SimpleDateFormat(DATE_FORMAT_YM2);
    /** Constant <code>FORMAT_MD_HMS_END_DAY</code> */
    public static final SimpleDateFormat FORMAT_MD_HMS_END_DAY =
            new SimpleDateFormat(CommonConstant.DATE_FORMAT_END_DAY);
    /** Constant <code>FORMAT_YMD_HMS_BEGIN_DAY</code> */
    public static final SimpleDateFormat FORMAT_YMD_HMS_BEGIN_DAY =
            new SimpleDateFormat(CommonConstant.DATE_FORMAT_BEGIN_DAY);
    /** Constant <code>FORMAT_YMDTHMS_ZER0</code> */
    public static final SimpleDateFormat FORMAT_YMDTHMS_ZER0 =
            new SimpleDateFormat(CommonConstant.DATE_FORMAT_YMDTHMS_ZER0);
    /** Constant <code>FORMAT_YMDTHMS_ZER0_24HRS</code> */
    public static final SimpleDateFormat FORMAT_YMDTHMS_ZER0_24HRS =
            new SimpleDateFormat(CommonConstant.DATE_FORMAT_YMDTHMS_ZERO_24HRS);
    /** Constant <code>FORMAT_HM_DMY</code> */
    public static final SimpleDateFormat FORMAT_HM_DMY = new SimpleDateFormat(CommonConstant.DATE_FORMAT_HM_DMY);
    /** Constant <code>FORMAT_HM_DMY1</code> */
    public static final SimpleDateFormat FORMAT_HM_DMY1 = new SimpleDateFormat(CommonConstant.DATE_FORMAT_HM_DMY1);
    /** Constant <code>FORMAT_S_YMD</code> */
    public static final SimpleDateFormat FORMAT_S_YMD = new SimpleDateFormat(CommonConstant.DATE_FORMAT_S_YMD);
    /** Constant <code>FORMAT_S_YMD_HMS</code> */
    public static final SimpleDateFormat FORMAT_S_YMD_HMS = new SimpleDateFormat(CommonConstant.DATE_FORMAT_S_YMD_HMS);
    /** Constant <code>FORMAT_YMDTHMS_GMT_7</code> */
    public static final SimpleDateFormat FORMAT_YMDTHMS_GMT_7 =
            new SimpleDateFormat(CommonConstant.DATE_FORMAT_YMDTHMS_GMT_7);
    /** Constant <code>FORMAT_YMDTHMS_GMT_7_2</code> */
    public static final SimpleDateFormat FORMAT_YMDTHMS_GMT_7_2 =
            new SimpleDateFormat(CommonConstant.DATE_FORMAT_YMDTHMS_GMT_7_2);
    /** Constant <code>FORMAT_DATE_DMY_HM</code> */
    public static final SimpleDateFormat FORMAT_DATE_DMY_HM = new SimpleDateFormat(CommonConstant.DATE_FORMAT_DMY_HM);
    /** Constant <code>FORMAT_FORMAT_HM_DMY</code> */
    public static final SimpleDateFormat FORMAT_FORMAT_HM_DMY = new SimpleDateFormat(CommonConstant.DATE_FORMAT_HM_DMY);
    /** Constant <code>DATE_TIME_YMD</code> */
    public static final DateTimeFormatter DATE_TIME_YMD = DateTimeFormatter.ofPattern(CommonConstant.DATE_FORMAT_YMD);
    /** Constant <code>DATE_TIME_DMY_HMS</code> */
    public static final DateTimeFormatter DATE_TIME_DMY_HMS =
            DateTimeFormatter.ofPattern(CommonConstant.DATE_FORMAT_DMY_HMS);
    /** Constant <code>DATE_TIME_DMY</code> */
    public static final DateTimeFormatter DATE_TIME_DMY = DateTimeFormatter.ofPattern(CommonConstant.DATE_FORMAT_DMY);
    /** Constant <code>DATE_FORMAT_DMYHM</code> */
    public static final DateTimeFormatter DATE_FORMAT_DMYHM =
            DateTimeFormatter.ofPattern(CommonConstant.DATE_FORMAT_DMYHM);
    /** Constant <code>DATE_TIME_YMDTHMS_ZEO_24HRS</code> */
    public static final DateTimeFormatter DATE_TIME_YMDTHMS_ZEO_24HRS =
            DateTimeFormatter.ofPattern(CommonConstant.DATE_FORMAT_YMDTHMS_ZERO_24HRS);
    /** Constant <code>PATTERN_REGEX_PHONE_ASTERISK</code> */
    public static final Pattern PATTERN_REGEX_PHONE_ASTERISK = Pattern.compile(CommonConstant.REGEX_PHONE_ASTERISK);
    /** Constant <code>PATTERN_REGEX_PATTERN_CHECK</code> */
    public static final Pattern PATTERN_REGEX_PATTERN_CHECK = Pattern.compile(CommonConstant.PATTER_CHECK);
    /** Constant <code>PATTERN_REGEX_NUMBER_PREFIX_CHECK</code> */
    public static final Pattern PATTERN_REGEX_NUMBER_PREFIX_CHECK =
            Pattern.compile(CommonConstant.COMMON_PREFIX.NUMBER_PREFIX);
    /** Constant <code>PATTERN_REGEX_ONLY_NUMBER_CHECK</code> */
    public static final Pattern PATTERN_REGEX_ONLY_NUMBER_CHECK =
            Pattern.compile(CommonConstant.COMMON_PREFIX.REGEX_ONLY_NUMBER);
    /** Constant <code>FORMAT_DATE_FORMAT</code> */
    public static final SimpleDateFormat FORMAT_DATE_FORMAT = new SimpleDateFormat(CommonConstant.DATE_FORMAT);
    /** Constant <code>FORMAT_DATE_FORMAT_MILI</code> */
    public static final SimpleDateFormat FORMAT_DATE_FORMAT_MILI =
            new SimpleDateFormat(CommonConstant.DATE_FORMAT_MILI);
    /** Constant <code>FORMAT_DATE_FORMAT_HM</code> */
    public static final SimpleDateFormat FORMAT_DATE_FORMAT_HM = new SimpleDateFormat(CommonConstant.DATE_FORMAT_HM);
    /** Constant <code>DATE_FORMAT_END_DAY</code> */
    public static final SimpleDateFormat DATE_FORMAT_END_DAY = new SimpleDateFormat(CommonConstant.DATE_FORMAT_END_DAY);

    /** Constant <code>NUMBER_SEPARATOR_SYMBOL_FORMAT</code> */
    public static final DecimalFormatSymbols NUMBER_SEPARATOR_SYMBOL_FORMAT = new DecimalFormatSymbols();
    /** Constant <code>NUMBER_SEPARATOR_SYMBOL</code> */
    public static final DecimalFormat NUMBER_SEPARATOR_SYMBOL = new DecimalFormat("", NUMBER_SEPARATOR_SYMBOL_FORMAT);
    /** Constant <code>DECIMAL_FORMAT_NUMBER_SEPERATOR</code> */
    public static final DecimalFormat DECIMAL_FORMAT_NUMBER_SEPERATOR = new DecimalFormat("#,##0");

    /** Constant <code>DECIMAL_FORMAT_SYMBOLS</code> */
    public static final DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols(Locale.GERMAN);
    /** Constant <code>DECIMAL_FORMAT_NUMBER_COMMA</code> */
    public static final DecimalFormat DECIMAL_FORMAT_NUMBER_COMMA =
            new DecimalFormat("#,###,###,###.###", DECIMAL_FORMAT_SYMBOLS);

    /** Constant <code>RANDOM</code> */
    public static final SecureRandom RANDOM = new SecureRandom();

    /** Constant <code>TEXT_TIMEZONE_VN="Asia/Ha_Noi"</code> */
    public static final String TEXT_TIMEZONE_VN = "Asia/Ha_Noi";
    /** Constant <code>TIMEZONE_VN</code> */
    public static final TimeZone TIMEZONE_VN = TimeZone.getTimeZone(TEXT_TIMEZONE_VN);
    /** Constant <code>CALENDAR</code> */
    public static Calendar CALENDAR = Calendar.getInstance();

    static {
        FORMAT_YMD.setTimeZone(TIMEZONE_VN);
        FORMAT_S_MY.setTimeZone(TIMEZONE_VN);
        FORMAT_S_YM.setTimeZone(TIMEZONE_VN);
        FORMAT_DMY.setTimeZone(TIMEZONE_VN);
        FORMAT_HH_MM.setTimeZone(TIMEZONE_VN);
        FORMAT_DMYHMS.setTimeZone(TIMEZONE_VN);
        FORMAT_DMY_HYPHEN.setTimeZone(TIMEZONE_VN);
        FORMAT_HMS.setTimeZone(TIMEZONE_VN);
        FORMAT_YDM_INSTANT.setTimeZone(TIMEZONE_VN);
        FORMAT_DMYHMS_HYPHEN.setTimeZone(TIMEZONE_VN);
        FORMAT_DATE.setTimeZone(TIMEZONE_VN);
        FORMAT_DATE_2.setTimeZone(TIMEZONE_VN);
        FORMAT_YMD_HMS.setTimeZone(TIMEZONE_VN);
        FORMAT_YMDHMS.setTimeZone(TIMEZONE_VN);
        FORMAT_YMDH.setTimeZone(TIMEZONE_VN);
        FORMAT_HMDMY.setTimeZone(TIMEZONE_VN);
        FORMAT_MD_HMS_END_DAY.setTimeZone(TIMEZONE_VN);
        FORMAT_FORMAT_HM_DMY.setTimeZone(TIMEZONE_VN);
        FORMAT_DATE_FORMAT_YM2.setTimeZone(TIMEZONE_VN);
        FORMAT_DATE_FORMAT_SHORT_YYYY.setTimeZone(TIMEZONE_VN);
        FORMAT_HM_DMY.setTimeZone(TIMEZONE_VN);
        FORMAT_HM_DMY1.setTimeZone(TIMEZONE_VN);
        FORMAT_DATE_DMY_HM.setTimeZone(TIMEZONE_VN);
        FORMAT_DATE_FORMAT.setTimeZone(TIMEZONE_VN);
        FORMAT_DATE_FORMAT_MILI.setTimeZone(TIMEZONE_VN);
        FORMAT_YMD_HMS_BEGIN_DAY.setTimeZone(TIMEZONE_VN);
        FORMAT_DATE_FORMAT_HM.setTimeZone(TIMEZONE_VN);
        NUMBER_SEPARATOR_SYMBOL_FORMAT.setDecimalSeparator('.');
        NUMBER_SEPARATOR_SYMBOL_FORMAT.setGroupingSeparator(',');
        DECIMAL_FORMAT_NUMBER_SEPERATOR.setGroupingSize(3);
        DECIMAL_FORMAT_SYMBOLS.setDecimalSeparator(',');
        DECIMAL_FORMAT_SYMBOLS.setGroupingSeparator('.');
        CALENDAR.setTimeZone(TIMEZONE_VN);
    }

    /**
     * Checks if an object is null or its string representation is empty.
     *
     * @param ob
     *            the object to check
     * @return true if the object is null or its string representation is empty,
     *         false otherwise
     */
    public static boolean isNullOrEmpty(Object ob) {
        return ob == null || ob.toString().trim().isEmpty();
    }

    /**
     * Checks if a CharSequence is null or empty.
     *
     * @param cs
     *            the CharSequence to check
     * @return true if the CharSequence is null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a collection is null or empty.
     *
     * @param collection
     *            the collection to check
     * @return true if the collection is null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Checks if an array is null or empty.
     *
     * @param collection
     *            the array to check
     * @return true if the array is null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(final Object[] collection) {
        return collection == null || collection.length == 0;
    }

    /**
     * Checks if a map is null or empty.
     *
     * @param map
     *            the map to check
     * @return true if the map is null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Converts an object to a string, returning a default value if the object is
     * null.
     *
     * @param obj
     *            the object to convert
     * @param defaultValue
     *            the default value to return if the object is null
     * @return the string representation of the object or the default value if the
     *         object is null
     */
    public static String safeToString(Object obj, String defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        return obj.toString();
    }

    /**
     * Converts an object to a string, returning an empty string if the object is
     * null.
     *
     * @param obj
     *            the object to convert
     * @return the string representation of the object or an empty string if the
     *         object is null
     */
    public static String safeToString(Object obj) {
        return safeToString(obj, "");
    }

    /**
     * Converts an object to an integer, returning a default value if the conversion
     * fails.
     *
     * @param obj1
     *            the object to convert
     * @param defaultValue
     *            the default value to return if the conversion fails
     * @return the integer representation of the object or the default value if the
     *         conversion fails
     */
    public static int safeToInt(Object obj1, int defaultValue) {
        int result = defaultValue;
        if (obj1 != null) {
            try {
                result = Integer.parseInt(obj1.toString());
            } catch (Exception ignored) {
                log.error("safeToInt error: ", ignored);
            }
        }
        return result;
    }

    /**
     * Converts an object to an integer, returning 0 if the conversion fails.
     *
     * @param obj1
     *            the object to convert
     * @return the integer representation of the object or 0 if the conversion fails
     */
    public static int safeToInt(Object obj1) {
        return safeToInt(obj1, 0);
    }

    /**
     * Converts an object to a boolean.
     *
     * @param obj1
     *            the object to convert
     * @return the boolean representation of the object or null if the conversion
     *         fails
     */
    public static Boolean safeToBoolean(Object obj1) {
        Boolean result = null;
        try {
            result = obj1 == null ? null : (Boolean) obj1;
        } catch (Exception ex) {
            log.error("safeToBoolean error ", ex);
        }
        return result;
    }

    /**
     * Converts an object to a long, returning a default value if the conversion
     * fails.
     *
     * @param obj1
     *            the object to convert
     * @param defaultValue
     *            the default value to return if the conversion fails
     * @return the long representation of the object or the default value if the
     *         conversion fails
     */
    public static Long safeToLong(Object obj1, Long defaultValue) {
        Long result = defaultValue;
        if (obj1 != null) {
            switch (obj1) {
                case BigDecimal bigDecimal -> {
                    return bigDecimal.longValue();
                }
                case BigInteger bigInteger -> {
                    return bigInteger.longValue();
                }
                case Double v -> {
                    return v.longValue();
                }
                default -> {}
            }

            try {
                result = Long.parseLong(obj1.toString());
            } catch (Exception e) {
                log.error("safeToInt error: ", e);
            }
        }
        return result;
    }

    /**
     * Converts an object to a long, returning 0 if the conversion fails.
     *
     * @param obj1
     *            the object to convert
     * @return the long representation of the object or 0 if the conversion fails
     */
    public static Long safeToLong(Object obj1) {
        return safeToLong(obj1, 0L);
    }

    /**
     * Converts an object to a double, returning a default value if the conversion
     * fails.
     *
     * @param obj
     *            the object to convert
     * @param defaultValue
     *            the default value to return if the conversion fails
     * @return the double representation of the object or the default value if the
     *         conversion fails
     */
    public static Double safeToDouble(Object obj, Double defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception ex) {
            log.error("safeToDouble error: ", ex);
            return defaultValue;
        }
    }

    /**
     * Converts an object to a double, returning 0.0 if the conversion fails.
     *
     * @param obj
     *            the object to convert
     * @return the double representation of the object or 0.0 if the conversion
     *         fails
     */
    public static Double safeToDouble(Object obj) {
        return safeToDouble(obj, 0.0D);
    }

    /**
     * Converts a string to a UUID.
     *
     * @param input
     *            the string to convert
     * @return the UUID representation of the string or null if the conversion fails
     */
    public static UUID safeToUUID(String input) {
        try {
            return UUID.fromString(input);
        } catch (Exception ex) {
            log.error("safeToUUID: ", ex);
            return null;
        }
    }

    /**
     * Checks if a string is a valid UUID.
     *
     * @param input
     *            the string to check
     * @return true if the string is a valid UUID, false otherwise
     */
    public static boolean isUUID(String input) {
        try {
            UUID uuid = UUID.fromString(input);
            return uuid.toString().equals(input);
        } catch (Exception ex) {
            log.error("isUUID: ", ex);
            return false;
        }
    }

    /**
     * Trims the string representation of an object.
     *
     * @param input
     *            the object to trim
     * @return the trimmed string representation of the object
     */
    public static String safeTrim(Object input) {
        return safeToString(input).trim();
    }

    /**
     * Trims a string.
     *
     * @param input
     *            the string to trim
     * @return the trimmed string
     */
    public static String safeTrim(String input) {
        return safeToString(input).trim();
    }

    /**
     * Checks if two objects are equal by comparing their string representations.
     *
     * @param obj1
     *            the first object to compare
     * @param obj2
     *            the second object to compare
     * @return true if the string representations of the objects are equal, false
     *         otherwise
     */
    public static boolean safeEqual(Object obj1, Object obj2) {
        return ((obj1 != null) && (obj2 != null) && obj2.toString().equals(obj1.toString()));
    }

    /**
     * Checks if two strings are equal.
     *
     * @param obj1
     *            the first string to compare
     * @param obj2
     *            the second string to compare
     * @return true if the strings are equal, false otherwise
     */
    public static boolean safeEqual(String obj1, String obj2) {
        return Objects.equals(obj1, obj2);
    }

    /**
     * Creates an ObjectMapper with specific configurations.
     *
     * @return the configured ObjectMapper
     */
    public static ObjectMapper convertObject() {
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .build();
    }

    /**
     * Checks if an object is equal to the string "true".
     *
     * @param obj
     *            the object to check
     * @return true if the object is equal to the string "true", false otherwise
     */
    public static boolean isTrue(Object obj) {
        return safeEqual(safeToString(obj), "true");
    }

    /**
     * Parses a JSON string to an object of the specified class.
     *
     * @param content
     *            the JSON string to parse
     * @param clz
     *            the class of the object to return
     * @param <T>
     *            the type of the object
     * @return the parsed object or a new instance of the class if parsing fails
     */
    public static <T> T parseStringToObject(String content, Class<?> clz) {
        if (isNullOrEmpty(content)) {
            return null;
        }
        try {
            return (T) ObjectMapperFactory.getInstance().readValue(safeToString(content), clz);
        } catch (JsonProcessingException e) {
            log.error("Parse json error", e);
        }
        try {
            return (T) clz.newInstance();
        } catch (Exception e) {
            log.error("cast object error: ", e);
            return (T) new Object();
        }
    }

    /**
     * Parses a JSON string to an object of the specified type.
     *
     * @param content
     *            the JSON string to parse
     * @param clz
     *            the TypeReference of the object to return
     * @param defaultValue
     *            the default value to return if parsing fails
     * @param <T>
     *            the type of the object
     * @return the parsed object or the default value if parsing fails
     */
    public static <T> T parseStringToObject(String content, TypeReference<T> clz, T defaultValue) {
        if (isNullOrEmpty(content)) {
            return null;
        }
        try {
            return ObjectMapperFactory.getInstance().readValue(safeToString(content), clz);
        } catch (JsonProcessingException e) {
            log.error("Parse json error: ", e);
            return defaultValue;
        }
    }

    /**
     * Converts an object to a JSON string.
     *
     * @param obj
     *            the object to convert
     * @return the JSON string representation of the object or an empty string if
     *         conversion fails
     */
    public static String parseObjectToString(Object obj) {
        if (obj == null) {
            return "";
        }
        try {
            return ObjectMapperFactory.getInstance().writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            log.error("parseObjectToString: ", ex);
            return "";
        }
    }

    /**
     * Converts a string to a LocalDateTime using the specified format.
     *
     * @param input
     *            the string to convert
     * @param format
     *            the format to use for conversion
     * @return the LocalDateTime representation of the string or null if conversion
     *         fails
     */
    public static LocalDateTime convertStringToLocalDateTime(String input, String format) {
        if (isNullOrEmpty(input)) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDateTime.parse(input, formatter);
        } catch (Exception ex) {
            log.error("convertStringToLocalDateTime error: ", ex);
            return null;
        }
    }

    /**
     * Formats a TemporalAccessor to a string using the specified format.
     *
     * @param date
     *            the TemporalAccessor to format
     * @param format
     *            the format to use
     * @param fallbackValue
     *            the value to return if the date is null
     * @return the formatted string or the fallback value if the date is null
     */
    public static String formatDate(TemporalAccessor date, String format, String fallbackValue) {
        if (isNullOrEmpty(date)) {
            return fallbackValue;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return df.format(date);
    }

    /**
     * Concatenates a list of strings.
     *
     * @param list
     *            the list of strings to concatenate
     * @return the concatenated string
     */
    public static String sumListString(String... list) {
        StringBuilder result = new StringBuilder();
        for (String str : list) {
            result.append(str);
        }
        return result.toString();
    }

    /**
     * Creates a SQL LIKE query string.
     *
     * @param str
     *            the string to use in the LIKE query
     * @return the SQL LIKE query string
     */
    public static String getLikeStr(String str) {
        if (str == null) {
            str = "";
        }
        return "%" + str + "%";
    }

    /**
     * Checks if a string is a valid JSON format.
     *
     * @param json
     *            the string to check
     * @return true if the string is a valid JSON format, false otherwise
     */
    public static boolean isValidFormatJson(String json) {
        try {
            new JSONObject(json);
        } catch (JSONException e) {
            log.info(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Converts a LocalDateTime to a string using the specified format.
     *
     * @param date
     *            the LocalDateTime to convert
     * @param format
     *            the format to use for conversion
     * @return the string representation of the LocalDateTime or null if conversion
     *         fails
     */
    public static String convertLocalDateToString(LocalDateTime date, String format) {
        try {
            if (ObjectUtils.isEmpty(date)) return null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return date.format(formatter);
        } catch (Exception e) {
            log.error("====> parse local date time to string ==> " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Converts a string to a LocalDateTime using the format "dd/MM/yyyy".
     *
     * @param dateString
     *            the string to convert
     * @return the LocalDateTime representation of the string or null if conversion
     *         fails
     */
    public static LocalDateTime convertStringToDateTime(String dateString) {
        try {
            // Define the date formatter for the input format
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Parse the date string to a LocalDate object
            LocalDate date = LocalDate.parse(dateString, dateFormatter);

            // Create a LocalTime object set to midnight (00:00:00)
            LocalTime time = LocalTime.MIDNIGHT;

            // Combine the date and time to create a LocalDateTime object
            return LocalDateTime.of(date, time);
        } catch (Exception ex) {
            log.error("Parse error: {}", ex.getMessage());
            return null;
        }
    }

    /**
     * Appends a SQL LIKE query wildcard to a string.
     *
     * @param field
     *            the string to append the wildcard to
     * @return the string with the appended wildcard
     */
    public static String appendLikeQuery(String field) {
        return "%" + field + "%";
    }

    /**
     * Wraps a Mono in an Optional.
     *
     * @param input
     *            the Mono to wrap
     * @param <T>
     *            the type of the Mono
     * @return a Mono of Optional containing the input Mono
     */
    public static <T> Mono<Optional<T>> optional(Mono<T> input) {
        return input.map(Optional::of).switchIfEmpty(Mono.just(Optional.empty()));
    }

    /**
     * Converts an object to an XML string.
     *
     * @param arg0
     *            the object to convert
     * @param name
     *            the name of the root element
     * @return the XML string representation of the object
     */
    public static String convertObjectToXMLString(Object arg0, String name) {
        String xml = "";
        try {
            StringWriter sw = new StringWriter();
            JAXB.marshal(arg0, sw);
            Document doc = convertStringToDocument(sw.toString());
            xml = convertDocumentToString(doc);
            if (!DataUtil.isNullOrEmpty(xml)) {
                xml = xml.replace("<" + name + ">", "");
                xml = xml.replace("</" + name + ">", "");
            }
        } catch (Exception ex) {
            return xml;
        }
        return xml;
    }

    /**
     * Converts a Document to a string.
     *
     * @param doc
     *            the Document to be converted
     * @return the string representation of the Document
     */
    private static String convertDocumentToString(Document doc) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            // below code to remove XML declaration
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            return output;
        } catch (TransformerException e) {
            return null;
        }
    }

    /**
     * Converts a string to a Document.
     *
     * @param xmlStr
     *            the string to be converted
     * @return the Document representation of the string
     * @throws Exception
     *             if an error occurs during conversion
     */
    private static Document convertStringToDocument(String xmlStr) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            InputSource inputSource = new InputSource(new StringReader(xmlStr));
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputSource);
            return doc;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Converts an object to a float.
     *
     * @param obj1
     *            the object to be converted
     * @return the float representation of the object
     */
    public static Float safeToFloat(Object obj1) {
        return safeToFloat(obj1, 0F);
    }

    /**
     * Converts an object to a float with a default value.
     *
     * @param obj1
     *            the object to be converted
     * @param defaultValue
     *            the default value to return if conversion fails
     * @return the float representation of the object or the default value if
     *         conversion fails
     */
    public static Float safeToFloat(Object obj1, Float defaultValue) {
        Float result = defaultValue;
        if (obj1 != null) {
            switch (obj1) {
                case BigDecimal bigDecimal -> {
                    return bigDecimal.floatValue();
                }
                case BigInteger bigInteger -> {
                    return bigInteger.floatValue();
                }
                case Double v -> {
                    return v.floatValue();
                }
                default -> {}
            }
            try {
                result = Float.parseFloat(obj1.toString());
            } catch (Exception e) {
                log.error("safeToInt error: ", e);
            }
        }
        return result;
    }

    /**
     * Validates the page size.
     *
     * @param pageSize
     *            the page size to be validated
     * @param defaultPageSize
     *            the default page size to use if pageSize is null
     * @return the validated page size
     */
    public static int validatePageSize(Integer pageSize, int defaultPageSize) {
        if (pageSize == null) {
            pageSize = defaultPageSize;
        } else if (pageSize <= 0) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.pageSize.invalid");
        }
        return pageSize;
    }

    /**
     * Validates the page index.
     *
     * @param pageIndex
     *            the page index to be validated
     * @param pageSize
     *            the page size to be used for validation
     * @return the validated page index
     */
    public static int validatePageIndex(Integer pageIndex, Integer pageSize) {
        int offset = 1;
        if (pageIndex == null) {
            offset = 1;
        } else if (pageIndex < 0) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.pageIndex.invalid");
        } else {
            offset = (pageIndex - 1) * pageSize;
        }
        return offset;
    }

    /**
     * Converts a date string to LocalDateTime.
     *
     * @param input
     *            the date string to be converted
     * @param format
     *            the format of the date string
     * @return the LocalDateTime representation of the date string
     */
    public static LocalDateTime convertDateStrToLocalDateTime(String input, String format) {
        if (isNullOrEmpty(input) || DataUtil.isNullOrEmpty(format)) {
            return null;
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
        DateTimeFormatter convertDateFormatter = new DateTimeFormatterBuilder()
                .append(dateFormatter)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter();
        try {
            return LocalDateTime.parse(input, convertDateFormatter);
        } catch (Exception ex) {
            log.error("convertDateStrToLocalDateTime error: ", ex);
            return null;
        }
    }

    /**
     * Converts a Date to a string in the format yyyyMMdd.
     *
     * @param value
     *            the Date to be converted
     * @return the string representation of the Date in the format yyyyMMdd
     */
    public static String convertDate2yyyyMMddStringNoSlash(Date value) {
        if (value != null) {
            SimpleDateFormat yyyymm = new SimpleDateFormat("yyyyMMdd");
            return yyyymm.format(value);
        } else {
            return "";
        }
    }
}
