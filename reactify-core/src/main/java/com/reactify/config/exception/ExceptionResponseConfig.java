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
package com.reactify.config.exception;

import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.model.response.TraceErrorResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.Translator;
import io.micrometer.tracing.Tracer;
import io.r2dbc.spi.R2dbcException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.io.buffer.DataBufferLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

/**
 * <p>
 * ExceptionResponseConfig class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
@RestControllerAdvice
public class ExceptionResponseConfig {

    private final Tracer tracer;

    /**
     * Constructs a new instance of {@code ExceptionResponseConfig}.
     *
     * @param tracer
     *            the Tracer instance used for tracing exceptions.
     */
    public ExceptionResponseConfig(Tracer tracer) {
        this.tracer = tracer;
    }

    /**
     * <p>
     * runtimeException.
     * </p>
     *
     * @param ex
     *            a {@link java.lang.RuntimeException} object
     * @param serverWebExchange
     *            a {@link org.springframework.web.server.ServerWebExchange} object
     * @return a {@link reactor.core.publisher.Mono} object
     */
    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<TraceErrorResponse<Object>>> runtimeException(
            RuntimeException ex, ServerWebExchange serverWebExchange) {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        log.error("Runtime exception trace-id {} , error ", traceId, ex);
        return Mono.just(new ResponseEntity<>(
                new TraceErrorResponse<>(CommonErrorCode.INTERNAL_SERVER_ERROR, "Server error", null, traceId),
                HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * <p>
     * r2dbcException.
     * </p>
     *
     * @param ex
     *            a {@link io.r2dbc.spi.R2dbcException} object
     * @param serverWebExchange
     *            a {@link org.springframework.web.server.ServerWebExchange} object
     * @return a {@link reactor.core.publisher.Mono} object
     */
    @ExceptionHandler(R2dbcException.class)
    public Mono<ResponseEntity<TraceErrorResponse<Object>>> r2dbcException(
            R2dbcException ex, ServerWebExchange serverWebExchange) {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        log.error("R2dbc trace-id {} , error ", traceId, ex);
        return Mono.just(new ResponseEntity<>(
                new TraceErrorResponse<>(CommonErrorCode.SQL_ERROR, "Server error", null, traceId),
                HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * <p>
     * accessDeniedException.
     * </p>
     *
     * @param ex
     *            a {@link java.nio.file.AccessDeniedException} object
     * @param serverWebExchange
     *            a {@link org.springframework.web.server.ServerWebExchange} object
     * @return a {@link reactor.core.publisher.Mono} object
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Mono<ResponseEntity<TraceErrorResponse<Object>>> accessDeniedException(
            AccessDeniedException ex, ServerWebExchange serverWebExchange) {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        log.error("Access denied trace-id {} , error ", traceId, ex);
        return Mono.just(new ResponseEntity<>(
                new TraceErrorResponse<>(CommonErrorCode.ACCESS_DENIED, "Access denied", null, traceId),
                HttpStatus.FORBIDDEN));
    }

    /**
     * <p>
     * dataBufferLimitException.
     * </p>
     *
     * @param ex
     *            a
     *            {@link org.springframework.core.io.buffer.DataBufferLimitException}
     *            object
     * @return a {@link reactor.core.publisher.Mono} object
     */
    @ExceptionHandler(DataBufferLimitException.class)
    public Mono<ResponseEntity<TraceErrorResponse<Object>>> dataBufferLimitException(DataBufferLimitException ex) {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        log.error("DataBuffer limit trace-id {} , error ", traceId, ex);
        return Mono.just(new ResponseEntity<>(
                new TraceErrorResponse<>(
                        CommonErrorCode.BAD_REQUEST, Translator.toLocale("request.databuffer.limit"), null, traceId),
                HttpStatus.BAD_REQUEST));
    }

    /**
     * <p>
     * serverInputException.
     * </p>
     *
     * @param ex
     *            a {@link org.springframework.web.server.ServerWebInputException}
     *            object
     * @param serverWebExchange
     *            a {@link org.springframework.web.server.ServerWebExchange} object
     * @return a {@link reactor.core.publisher.Mono} object
     */
    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<TraceErrorResponse<Object>>> serverInputException(
            ServerWebInputException ex, ServerWebExchange serverWebExchange) {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        log.error("Request Input invalid format trace-id {} , error ", traceId, ex);
        return Mono.just(new ResponseEntity<>(
                new TraceErrorResponse<>(CommonErrorCode.INVALID_PARAMS, ex.getReason(), null, traceId),
                HttpStatus.BAD_REQUEST));
    }

    /**
     * <p>
     * serverInputException.
     * </p>
     *
     * @param ex
     *            a
     *            {@link org.springframework.web.bind.support.WebExchangeBindException}
     *            object
     * @param serverWebExchange
     *            a {@link org.springframework.web.server.ServerWebExchange} object
     * @return a {@link reactor.core.publisher.Mono} object
     */
    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<TraceErrorResponse<Object>>> serverInputException(
            WebExchangeBindException ex, ServerWebExchange serverWebExchange) {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .map(Translator::toLocaleVi)
                .collect(Collectors.toList());

        String errorValue = String.join(", ", errors);
        if (errorValue.contains("Failed to convert property value")) {
            return Mono.just(new ResponseEntity<>(
                    new TraceErrorResponse<>(
                            CommonErrorCode.INVALID_PARAMS,
                            Translator.toLocaleVi("params.invalid.format"),
                            null,
                            traceId),
                    HttpStatus.BAD_REQUEST));
        }
        return Mono.just(new ResponseEntity<>(
                new TraceErrorResponse<>(CommonErrorCode.INVALID_PARAMS, errorValue, null, traceId),
                HttpStatus.BAD_REQUEST));
    }

    /**
     * <p>
     * businessException.
     * </p>
     *
     * @param ex
     *            a {@link com.reactify.exception.BusinessException} object
     * @param serverWebExchange
     *            a {@link org.springframework.web.server.ServerWebExchange} object
     * @return a {@link reactor.core.publisher.Mono} object
     */
    @ExceptionHandler(BusinessException.class)
    public Mono<ResponseEntity<TraceErrorResponse<Object>>> businessException(
            BusinessException ex, ServerWebExchange serverWebExchange) {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        String errorCode = ex.getErrorCode();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        if (!DataUtil.isNullOrEmpty(errorCode)) {
            if (errorCode.equals(CommonErrorCode.NOT_FOUND)) {
                httpStatus = HttpStatus.NOT_FOUND;
            } else if (errorCode.equals(CommonErrorCode.NO_PERMISSION)) {
                httpStatus = HttpStatus.FORBIDDEN;
            }
        }
        return Mono.just(new ResponseEntity<>(
                new TraceErrorResponse<>(ex.getErrorCode(), ex.getMessage(), null, traceId), httpStatus));
    }
}
