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
package com.reactify.exception;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * <p>
 * Custom exception class for handling HTTP responses with {@code WebClient}.
 * </p>
 *
 * <p>
 * This exception extends
 * {@link org.springframework.web.reactive.function.client.WebClientResponseException}
 * to include the response body and HTTP status code, allowing for detailed
 * logging and improved debugging of HTTP errors.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 *
 * <pre>
 * {@code
 * if (response.statusCode().isError()) {
 * 	throw new CustomWebClientResponseException(response.body(), response.statusCode());
 * }
 * }
 * </pre>
 *
 * @see WebClientResponseException
 * @see HttpStatus
 * @version 1.0
 * @since 1.0
 * @author hoangtien2k3
 */
@Getter
@Setter
public class CustomWebClientResponseException extends WebClientResponseException {

    /** The body of the error response as a string. */
    private final String errorBody;

    /** The HTTP status code of the error response. */
    private final HttpStatus statusCode;

    /**
     * Constructs a new {@code CustomWebClientResponseException} with the specified
     * error body and HTTP status code.
     *
     * @param errorBody
     *            a {@link java.lang.String} representing the response body.
     * @param statusCode
     *            a {@link org.springframework.http.HttpStatus} object representing
     *            the HTTP status.
     */
    public CustomWebClientResponseException(String errorBody, HttpStatus statusCode) {
        super(statusCode.value(), statusCode.getReasonPhrase(), null, errorBody.getBytes(), null);
        this.errorBody = errorBody;
        this.statusCode = statusCode;
    }

    /**
     * {@inheritDoc}
     *
     * Returns a formatted message containing the HTTP status code, reason phrase,
     * and error body.
     */
    @NotNull
    @Override
    public String getMessage() {
        return String.format(
                "CustomWebClientResponseException: HTTP %d %s - %s",
                statusCode.value(), statusCode.getReasonPhrase(), errorBody);
    }
}
