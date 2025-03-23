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
package com.reactify.filter.properties;

import com.reactify.constants.Constants;
import java.util.List;

/**
 * <p>
 * The ClientLogProperties class is a record that encapsulates the configuration
 * properties related to client logging in an application. It provides options
 * to enable or disable logging and to specify which headers should be
 * obfuscated when logging.
 * </p>
 *
 * <p>
 * This class is designed to hold the logging configuration for clients, making
 * it easier to manage and utilize in various parts of the application.
 * </p>
 *
 * <p>
 * By default, client logging is enabled, and sensitive headers are obtained
 * from the Constants class to be obfuscated during logging.
 * </p>
 *
 * @param enable
 *            Indicates whether client logging is enabled or disabled.
 * @param obfuscateHeaders
 *            A list of headers that should be obfuscated in the logs.
 * @author hoangtien2k3
 */
public record ClientLogProperties(boolean enable, List<String> obfuscateHeaders) {

    /**
     * Default constructor that initializes the ClientLogProperties with default
     * values. The logging is enabled by default and sensitive headers are obtained
     * from the Constants class.
     */
    public ClientLogProperties() {
        this(true, Constants.getSensitiveHeaders());
    }
}
