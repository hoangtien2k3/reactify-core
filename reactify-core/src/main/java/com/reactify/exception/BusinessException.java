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

import com.reactify.util.Translator;
import java.util.Arrays;
import java.util.Objects;

/**
 * <p>
 * Custom exception class representing business-related errors within the
 * application.
 * </p>
 *
 * <p>
 * This exception is thrown to indicate specific application-related conditions
 * that are not covered by standard exceptions. It includes an error code, a
 * message (localized to Vietnamese), and optional message parameters to provide
 * detailed error context.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 *
 * <pre>
 * {@code
 * throw new BusinessException("404", "Resource not found");
 * throw new BusinessException("500", "Unexpected error occurred", "User ID", "Order ID");
 * }
 * </pre>
 *
 * <p>
 * This class uses Lombok annotations for boilerplate code:
 * </p>
 * <ul>
 * <li>{@code @EqualsAndHashCode(callSuper = true)} - Ensures hashCode and
 * equals include fields from {@code RuntimeException}.</li>
 * <li>{@code @Data} - Generates getters, setters, toString, and other
 * methods.</li>
 * <li>{@code @NoArgsConstructor} - Provides a no-arguments constructor.</li>
 * </ul>
 *
 * @see RuntimeException
 * @see Translator
 * @see Arrays
 * @see String
 * @version 1.0
 * @since 1.0
 * @author hoangtien2k3
 */
public class BusinessException extends RuntimeException {

    /** Error code associated with the exception */
    private final String errorCode;

    /** Localized message describing the error */
    private final String message;

    /** Optional parameters for dynamic message formatting */
    private Object[] paramsMsg;

    /**
     * Constructor initializing the exception with an error code and message.
     *
     * @param errorCode
     *            a {@link java.lang.String} representing the error code.
     * @param message
     *            a {@link java.lang.String} representing the error message, which
     *            will be localized.
     */
    public BusinessException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = Translator.toLocaleVi(message);
    }

    /**
     * Constructor initializing the exception with an error code, message, and
     * message parameters.
     *
     * <p>
     * Parameters will be used to format the message dynamically, enhancing the
     * readability of error logs and user-facing messages.
     * </p>
     *
     * @param errorCode
     *            a {@link java.lang.String} representing the error code.
     * @param message
     *            a {@link java.lang.String} representing the error message, which
     *            will be localized.
     * @param paramsMsg
     *            a {@link java.lang.String} array of message parameters for dynamic
     *            message localization.
     */
    public BusinessException(String errorCode, String message, String... paramsMsg) {
        this.errorCode = errorCode;
        this.paramsMsg = Arrays.stream(paramsMsg).map(Translator::toLocaleVi).toArray(String[]::new);
        this.message = Translator.toLocaleVi(message, this.paramsMsg);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public Object[] getParamsMsg() {
        return paramsMsg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BusinessException that)) return false;
        return Objects.equals(getErrorCode(), that.getErrorCode())
                && Objects.equals(getMessage(), that.getMessage())
                && Arrays.equals(getParamsMsg(), that.getParamsMsg());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getErrorCode(), getMessage());
        result = 31 * result + Arrays.hashCode(getParamsMsg());
        return result;
    }
}
