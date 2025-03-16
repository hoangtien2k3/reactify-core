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

import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.model.response.DataResponse;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

/**
 * Utility class for handling responses in the application. This class provides
 * static methods to create and manipulate response objects.
 */
public class ResponseUtils {

    /**
     * Creates a ResponseEntity with a DataResponse containing the given data and a
     * success message.
     *
     * @param data
     *            the data to be included in the response
     * @return a ResponseEntity containing the DataResponse
     */
    public static ResponseEntity<DataResponse<?>> ok(Object data) {
        return ResponseEntity.ok(new DataResponse<>(Translator.toLocale("success"), data));
    }

    /**
     * Creates a ResponseEntity with a DataResponse containing the given data and a
     * custom message.
     *
     * @param data
     *            the data to be included in the response
     * @param message
     *            the custom message to be included in the response
     * @return a ResponseEntity containing the DataResponse
     */
    public static ResponseEntity<DataResponse<?>> ok(Object data, String message) {
        return ResponseEntity.ok(new DataResponse<>(Translator.toLocale(message), data));
    }

    /**
     * Processes an Optional DataResponse and returns a Mono containing the data. If
     * the DataResponse is empty or contains an error code, a BusinessException is
     * thrown.
     *
     * @param response
     *            the Optional DataResponse to be processed
     * @param extraInfo
     *            additional information to be included in the error message
     * @return a Mono containing the data from the DataResponse
     */
    public static Mono<Object> getResponse(Optional<DataResponse> response, String extraInfo) {
        return getResponseWithoutData(response, extraInfo)
                .switchIfEmpty(Mono.error(new BusinessException(
                        CommonErrorCode.INTERNAL_SERVER_ERROR, extraInfo + " data is null " + response)))
                .flatMap(data -> {
                    if (data == null) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, extraInfo + " data is null " + response));
                    }
                    return Mono.just(data);
                });
    }

    /**
     * Processes an Optional DataResponse and returns a Mono containing the data
     * without any additional processing. If the DataResponse is empty or contains
     * an error code, a BusinessException is thrown.
     *
     * @param response
     *            the Optional DataResponse to be processed
     * @param extraInfo
     *            additional information to be included in the error message
     * @return a Mono containing the data from the DataResponse
     */
    public static Mono<Object> getResponseWithoutData(Optional<DataResponse> response, String extraInfo) {
        if (response.isEmpty()) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, extraInfo + " response empty"));
        }
        var data = response.get();
        if (data.getErrorCode() != null) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INTERNAL_SERVER_ERROR, extraInfo + " errorCode exist " + data.getErrorCode()));
        }
        return Mono.justOrEmpty(data.getData());
    }
}
