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
package com.reactify.filter.webclient;

import com.reactify.exception.CustomWebClientResponseException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

/**
 * <p>
 * The ErrorHandlingFilter class implements the ExchangeFilterFunction interface
 * to handle errors during HTTP requests made with a reactive WebClient. This
 * filter checks for client and server errors in the responses and converts them
 * into a custom exception.
 * </p>
 *
 * <p>
 * When an error response (4xx or 5xx) is detected, it reads the response body
 * and wraps it in a
 * {@link com.reactify.exception.CustomWebClientResponseException}, allowing the
 * error information to be easily propagated to the caller.
 * </p>
 *
 * @author hoangtien2k3
 */
public class ErrorHandlingFilter implements ExchangeFilterFunction {

    /**
     * {@inheritDoc}
     *
     * <p>
     * Filters the client request and handles the response, transforming error
     * responses into a custom exception.
     * </p>
     */
    @NotNull
    @Override
    public Mono<ClientResponse> filter(@NotNull ClientRequest request, ExchangeFunction next) {
        return next.exchange(request).flatMap(clientResponse -> {
            // Check if the response status code indicates an error (4xx or 5xx)
            if (clientResponse.statusCode().is5xxServerError()
                    || clientResponse.statusCode().is4xxClientError()) {
                return clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                    HttpStatus status =
                            HttpStatus.resolve(clientResponse.statusCode().value());
                    // Default to INTERNAL_SERVER_ERROR if status is null
                    if (status == null) {
                        status = HttpStatus.INTERNAL_SERVER_ERROR;
                    }
                    // Create and return a custom exception
                    return Mono.error(new CustomWebClientResponseException(errorBody, status));
                });
            } else {
                // Return the successful client response
                return Mono.just(clientResponse);
            }
        });
    }
}
