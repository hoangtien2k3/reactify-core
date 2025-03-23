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

import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import java.util.List;
import org.springframework.http.HttpMethod;

/**
 * <p>
 * The RetryProperties class is a record that holds configuration properties for
 * retrying HTTP requests. It specifies whether retries are enabled, the number
 * of retry attempts, the HTTP methods to which retries apply, and the types of
 * exceptions that will trigger a retry.
 * </p>
 *
 * <p>
 * The default constructor initializes retries to be enabled with a count of 2,
 * applying to GET, PUT, and DELETE methods, and set to trigger on connection
 * and read timeout exceptions.
 * </p>
 *
 * @param isEnable
 *            a boolean indicating whether retries are enabled
 * @param count
 *            the number of retry attempts
 * @param methods
 *            a list of HTTP methods to which retries apply
 * @param exceptions
 *            a list of exception classes that will trigger a retry
 * @author hoangtien2k3
 */
public record RetryProperties(
        boolean isEnable, int count, List<HttpMethod> methods, List<Class<? extends Exception>> exceptions) {

    /**
     * <p>
     * Constructor for RetryProperties.
     * </p>
     */
    public RetryProperties() {
        this(
                true, // Default to enabling retries
                2, // Default number of retry attempts
                List.of(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE), // Default HTTP methods for retries
                List.of(ConnectTimeoutException.class, ReadTimeoutException.class)); // Default exceptions for retry
    }
}
