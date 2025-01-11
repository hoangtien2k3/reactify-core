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

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an error response with trace information, extending DataResponse.
 *
 * @param <T>
 *            the type of the response data
 * @author hoangtien2k3
 */
@Getter
@Setter
public class TraceErrorResponse<T> extends DataResponse<T> {

    /**
     * Unique identifier for tracing the specific request.
     */
    private String requestId;

    /**
     * Constructs a TraceErrorResponse with error code, message, data, and request
     * ID.
     *
     * @param errorCode
     *            the error code to be included in the response
     * @param message
     *            the message to be included in the response
     * @param data
     *            the data to be included in the response
     * @param requestId
     *            the request ID to be included in the response
     */
    public TraceErrorResponse(String errorCode, String message, T data, String requestId) {
        super(errorCode, message, data);
        this.requestId = requestId;
    }
}
