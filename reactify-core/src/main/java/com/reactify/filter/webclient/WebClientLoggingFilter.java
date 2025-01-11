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

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

/**
 * <p>
 * The WebClientLoggingFilter class implements the ExchangeFilterFunction
 * interface to provide logging capabilities for HTTP requests and responses
 * made through a WebClient instance. It logs the details of the request,
 * including method, URL, headers, and body, while obfuscating sensitive header
 * information as specified.
 * </p>
 *
 * <p>
 * This filter is particularly useful for debugging and monitoring API calls in
 * a Spring application. It allows developers to track the flow of requests and
 * responses while ensuring that sensitive information is protected through
 * obfuscation.
 * </p>
 *
 * @param obfuscateHeader
 *            A list of header names that should be obfuscated in the logs.
 * @author hoangtien2k3
 */
@Slf4j
public record WebClientLoggingFilter(List<String> obfuscateHeader) implements ExchangeFilterFunction {
    /** Constant <code>OBFUSCATE_HEADER="xxxxx"</code> */
    private static final String OBFUSCATE_HEADER = "xxxxx";

    /**
     * {@inheritDoc}
     *
     * <p>
     * Filters the client request to log details about the request and the response.
     * It logs the HTTP method, URL, headers, and body of the request, and also logs
     * the headers of the response. Sensitive headers can be obfuscated based on the
     * provided list.
     * </p>
     */
    @NotNull
    @Override
    public Mono<ClientResponse> filter(ClientRequest request, @NotNull ExchangeFunction next) {
        log.info("Start Call API - Method: {} {}", request.method(), request.url());
        if (request.headers().getContentLength() > 0) {
            log.info("body {}", request.body());
        }
        if (log.isDebugEnabled()) {
            request.headers()
                    .forEach((name, values) -> values.forEach(value -> log.debug(
                            "Request header: {}={}", name, obfuscateHeader.contains(name) ? OBFUSCATE_HEADER : value)));
        }
        return next.exchange(request).flatMap(clientResponse -> {
            if (log.isDebugEnabled()) {
                clientResponse
                        .headers()
                        .asHttpHeaders()
                        .forEach((name, values) -> values.forEach(value -> log.debug(
                                "Response header: {}={}",
                                name,
                                obfuscateHeader.contains(name) ? OBFUSCATE_HEADER : value)));
            }
            return Mono.just(clientResponse);
        });
    }
}
