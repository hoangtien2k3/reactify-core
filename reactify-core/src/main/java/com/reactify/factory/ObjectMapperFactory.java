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
package com.reactify.factory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;

/**
 * <p>
 * Factory class for providing different configurations of
 * {@link com.fasterxml.jackson.databind.ObjectMapper} instances.
 * </p>
 *
 * <p>
 * This class centralizes the creation of
 * {@link com.fasterxml.jackson.databind.ObjectMapper} instances with various
 * settings that are often reused across the application. Each method provides
 * an {@link com.fasterxml.jackson.databind.ObjectMapper} with specific
 * configurations for handling JSON data deserialization and serialization.
 * </p>
 *
 * <p>
 * Usage example:
 * </p>
 *
 * <pre>
 * {@code
 * ObjectMapper mapper = ObjectMapperFactory.getInstance();
 * MyObject obj = mapper.readValue(jsonString, MyObject.class);
 * }
 * </pre>
 *
 * @see ObjectMapper
 * @see DeserializationFeature
 * @see SimpleModule
 * @see JavaTimeModule
 * @since 1.0
 * @version 1.0
 * @author hoangtien2k3
 */
public class ObjectMapperFactory {

    /**
     * Singleton instance for a custom-configured {@link ObjectMapper}.
     */
    private static volatile ObjectMapper objectMapper;

    /**
     * Singleton instance for a secondary {@link ObjectMapper} with alternative
     * settings.
     */
    private static final ObjectMapper objectMapperV2 = new ObjectMapper();

    /**
     * Singleton instance for a default configuration of {@link ObjectMapper}.
     */
    private static final ObjectMapper defaultGetInstance = new ObjectMapper();

    /**
     * <p>
     * Provides a custom-configured
     * {@link com.fasterxml.jackson.databind.ObjectMapper} instance.
     * </p>
     *
     * <p>
     * Configurations include disabling failure on unknown properties, accepting
     * single values as arrays, and registering a custom deserializer for boolean
     * values represented as numeric strings.
     * </p>
     *
     * @return a {@link com.fasterxml.jackson.databind.ObjectMapper} instance with
     *         custom configurations
     */
    public static ObjectMapper getInstance() {
        if (objectMapper == null) {
            synchronized (ObjectMapperFactory.class) {
                if (objectMapper == null) {
                    objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                    objectMapper.configure(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS, true);

                    // Register custom deserializer for boolean values represented as numbers
                    SimpleModule module = new SimpleModule();
                    module.addDeserializer(boolean.class, new NumericBooleanDeserializer());
                    module.addDeserializer(Boolean.class, new NumericBooleanDeserializer());

                    // Register modules for Java time handling
                    objectMapper.registerModule(new JavaTimeModule());
                    objectMapper.registerModule(module);
                }
            }
        }
        return objectMapper;
    }

    /**
     * <p>
     * Provides an alternative {@link com.fasterxml.jackson.databind.ObjectMapper}
     * instance with a simplified configuration.
     * </p>
     *
     * <p>
     * This instance is configured to ignore unknown properties and accept single
     * values as arrays, but without additional modules or custom deserializers.
     * </p>
     *
     * @return a {@link com.fasterxml.jackson.databind.ObjectMapper} instance with
     *         simplified configurations
     */
    public static ObjectMapper getInstance2() {
        objectMapperV2.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapperV2.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return objectMapperV2;
    }

    /**
     * <p>
     * Provides a default {@link com.fasterxml.jackson.databind.ObjectMapper}
     * instance with standard configuration settings.
     * </p>
     *
     * <p>
     * Configurations include ignoring unknown properties, accepting single values
     * as arrays, and excluding null values from serialization. This instance also
     * attempts to find and register any additional modules available on the
     * classpath.
     * </p>
     *
     * @return a {@link com.fasterxml.jackson.databind.ObjectMapper} instance with
     *         default settings
     */
    public static ObjectMapper defaultGetInstance() {
        defaultGetInstance.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        defaultGetInstance.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        defaultGetInstance.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        defaultGetInstance.findAndRegisterModules();
        return defaultGetInstance;
    }

    /**
     * Custom deserializer for boolean values represented as numeric strings.
     * <p>
     * This deserializer interprets "1" as {@code true}, "0" as {@code false}, and
     * also handles standard "true" and "false" strings.
     * </p>
     */
    private static class NumericBooleanDeserializer extends JsonDeserializer<Boolean> {
        @Override
        public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String text = p.getText().trim();
            return switch (text) {
                case "1", "true" -> Boolean.TRUE;
                case "0", "false" -> Boolean.FALSE;
                default -> null;
            };
        }
    }
}
