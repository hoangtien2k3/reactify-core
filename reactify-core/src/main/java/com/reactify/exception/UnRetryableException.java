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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Exception indicating that a specific operation is non-retryable.
 * </p>
 *
 * <p>
 * This exception extends {@link com.reactify.exception.BusinessException} and
 * is used when an error occurs that should not be retried by the calling method
 * or process.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 *
 * <pre>
 * {@code
 * if (someConditionFails()) {
 * 	throw new UnRetryableException("ERROR_CODE_001", "Operation cannot be retried due to ...");
 * }
 * }
 * </pre>
 *
 * @see BusinessException
 * @version 1.0
 * @since 1.0
 * @author hoangtien2k3
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UnRetryableException extends BusinessException {

    /**
     * Constructs a new {@code UnRetryableException} with the specified error code
     * and message.
     *
     * @param errorCode
     *            a {@link java.lang.String} representing the unique error code.
     * @param message
     *            a {@link java.lang.String} describing the error in detail.
     */
    public UnRetryableException(String errorCode, String message) {
        super(errorCode, message);
    }
}
