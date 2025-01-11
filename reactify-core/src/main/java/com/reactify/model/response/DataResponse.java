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
package com.reactify.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.reactify.constants.MessageConstant;
import com.reactify.util.Translator;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Represents a standardized response structure for API responses.
 *
 * @param <T>
 *            the type of the response data
 * @author hoangtien2k3
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataResponse<T> implements Serializable {
    /**
     * Error code representing the status of the response.
     */
    private String errorCode;

    /**
     * Message providing additional information about the response.
     */
    private String message;

    /**
     * The actual data to be included in the response.
     */
    private T data;

    /**
     * Constructs a DataResponse with a message and data.
     *
     * @param message
     *            the message to be included in the response
     * @param data
     *            the data to be included in the response
     */
    public DataResponse(String message, T data) {
        this.message = Translator.toLocaleVi(message);
        this.data = data;
    }

    /**
     * Constructs a DataResponse with a message.
     *
     * @param message
     *            the message to be included in the response
     */
    public DataResponse(String message) {
        this.message = Translator.toLocaleVi(message);
    }

    /**
     * Creates a successful DataResponse with the provided data.
     *
     * @param data
     *            the data to be included in the response
     * @param <T>
     *            the type of the response data
     * @return a DataResponse indicating success
     */
    public static <T> DataResponse<T> success(T data) {
        return DataResponse.<T>builder()
                .errorCode(MessageConstant.ERROR_CODE_SUCCESS)
                .message(MessageConstant.SUCCESS)
                .data(data)
                .build();
    }

    /**
     * Creates a failed DataResponse with the provided data.
     *
     * @param data
     *            the data to be included in the response
     * @param <T>
     *            the type of the response data
     * @return a DataResponse indicating failure
     */
    public static <T> DataResponse<T> failed(T data) {
        return DataResponse.<T>builder()
                .errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .message(MessageConstant.FAIL)
                .data(data)
                .build();
    }
}
