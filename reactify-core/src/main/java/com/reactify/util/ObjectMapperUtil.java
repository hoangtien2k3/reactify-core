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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Utility class for handling JSON serialization and deserialization using
 * Jackson. Provides methods to convert objects to JSON strings and vice versa.
 * Also supports conversion of lists and byte arrays to objects.
 *
 * @since 1.3.0
 * @author hoangtien2k3
 */
@Component
public class ObjectMapperUtil {

    /**
     * A static logger instance for logging messages
     */
    private static final Logger log = LoggerFactory.getLogger(ObjectMapperUtil.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final DateFormat dateFormatYMdHms = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Constructs a new instance of {@code ObjectMapperUtil}.
     */
    public ObjectMapperUtil() {}

    /**
     * Converts an object to a JSON string.
     *
     * @param object
     *            the object to be converted to JSON
     * @return the JSON string representation of the object, or null if the object
     *         is null or conversion fails
     */
    public static String convertObjectToJson(Object object) {
        if (DataUtil.isNullOrEmpty(object)) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            log.error("Convert to Object error ", ex);
            return null;
        }
    }

    /**
     * Converts a JSON string to an object of the specified type.
     *
     * @param objectString
     *            the JSON string to be converted
     * @param valueType
     *            the class of the object to be returned
     * @param <T>
     *            the type of the object
     * @return the object represented by the JSON string, or null if the string is
     *         null or conversion fails
     */
    public <T> T convertStringToObject(String objectString, Class<T> valueType) {
        if (DataUtil.isNullOrEmpty(objectString)) {
            return null;
        }
        try {
            return objectMapper.readValue(objectString, valueType);
        } catch (Exception ex) {
            log.error("Convert to Object error ", ex);
            return null;
        }
    }

    /**
     * Converts a list of JSON strings to a list of objects of the specified type.
     *
     * @param stringList
     *            the list of JSON strings to be converted
     * @param targetClass
     *            the class of the objects to be returned
     * @param <T>
     *            the type of the objects
     * @return the list of objects represented by the JSON strings, or null if the
     *         list is null or conversion fails
     */
    public <T> List<T> convertStringListToObjectList(List<?> stringList, Class<T> targetClass) {
        if (DataUtil.isNullOrEmpty(stringList)) {
            return null;
        }
        List<T> objectList = new ArrayList<>();
        for (Object item : stringList) {
            T object = objectMapper.convertValue(item, targetClass);
            objectList.add(object);
        }
        return objectList;
    }

    /**
     * Converts a byte array to an object of the specified type.
     *
     * @param byteArray
     *            the byte array to be converted
     * @param valueType
     *            the class of the object to be returned
     * @param <T>
     *            the type of the object
     * @return the object represented by the byte array
     */
    public <T> T convertToObject(byte[] byteArray, Class<T> valueType) {
        try {
            return objectMapper.readValue(byteArray, valueType);
        } catch (Exception ex) {
            throw new BusinessException(CommonErrorCode.UN_DESERIALIZE, ex.getMessage());
        }
    }

    /**
     * Converts a list of objects to a list of objects of the specified type.
     *
     * @param input
     *            the list of objects to be converted
     * @param valueType
     *            the class of the objects to be returned
     * @param <T>
     *            the type of the objects
     * @return the list of objects represented by the input list
     */
    public <T> List<T> convertToObject(List<Object> input, Class<T> valueType) {
        try {
            List<T> results = new ArrayList<>();
            for (Object object : input) {
                results.add(objectMapper.convertValue(object, valueType));
            }
            return results;
        } catch (Exception ex) {
            throw new BusinessException(CommonErrorCode.UN_DESERIALIZE, ex.getMessage());
        }
    }

    /**
     * Converts an object to an object of the specified type.
     *
     * @param input
     *            the object to be converted
     * @param valueType
     *            the class of the object to be returned
     * @param <T>
     *            the type of the object
     * @return the object represented by the input object
     */
    public <T> T convertToObject(Object input, Class<T> valueType) {
        try {
            return objectMapper.convertValue(input, valueType);
        } catch (Exception ex) {
            throw new BusinessException(CommonErrorCode.UN_DESERIALIZE, ex.getMessage());
        }
    }

    /**
     * Converts an object to an object of the specified type using a static method.
     *
     * @param input
     *            the object to be converted
     * @param valueType
     *            the class of the object to be returned
     * @param <T>
     *            the type of the object
     * @return the object represented by the input object
     */
    public static <T> T convertObject(Object input, Class<T> valueType) {
        return objectMapper.convertValue(input, valueType);
    }

    /**
     * Converts an object to a JSON string with a specific date format for
     * LocalDateTime.
     *
     * @param object
     *            the object to be converted to JSON
     * @return the JSON string representation of the object
     */
    public static String convertObjectToJsonForLocalDateTime(Object object) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(dateFormatYMdHms);
        return convertObjectToJson(object);
    }
}
