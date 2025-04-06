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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.factory.ObjectMapperFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import reactor.core.publisher.Mono;

/**
 * Utility class for data manipulation and processing. This class contains
 * static methods for various data-related operations.
 */
public class DataUtil {

    /**
     * A static logger instance for logging messages
     */
    private static final Logger log = LoggerFactory.getLogger(DataUtil.class);

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
        if (isNullOrEmpty(json)) {
            log.info("JSON input is null or empty.");
            return false;
        }
        try {
            ObjectMapperFactory.getInstance().readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
            return false;
        }
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
