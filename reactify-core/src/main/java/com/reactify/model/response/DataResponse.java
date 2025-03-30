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
import java.util.Objects;
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
public class DataResponse<T> implements Serializable {
    private String errorCode;
    private String message;
    private T data;

    public static class Builder<T> {
        private String errorCode;
        private String message;
        private T data;

        public Builder<T> errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public DataResponse<T> build() {
            DataResponse<T> response = new DataResponse<>(this.message, this.data);
            response.setErrorCode(this.errorCode);
            return response;
        }
    }

    public static <T> DataResponse<T> success(T data) {
        return DataResponse.<T>builder()
                .errorCode(MessageConstant.ERROR_CODE_SUCCESS)
                .message(MessageConstant.SUCCESS)
                .data(data)
                .build();
    }

    public static <T> DataResponse<T> failed(T data) {
        return DataResponse.<T>builder()
                .errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .message(MessageConstant.FAIL)
                .data(data)
                .build();
    }

    public DataResponse(String errorCode, String message, T data) {
        this.errorCode = errorCode;
        this.message = Translator.toLocaleVi(message);
        this.data = data;
    }

    public DataResponse(String message, T data) {
        this.message = Translator.toLocaleVi(message);
        this.data = data;
    }

    public DataResponse(String message) {
        this.message = Translator.toLocaleVi(message);
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataResponse<?> that)) return false;
        return Objects.equals(getErrorCode(), that.getErrorCode())
                && Objects.equals(getMessage(), that.getMessage())
                && Objects.equals(getData(), that.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getErrorCode(), getMessage(), getData());
    }
}
