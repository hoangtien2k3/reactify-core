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

import brave.Tracer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

/**
 * <p>
 * The TraceIdWebClientFilter class implements the ExchangeFilterFunction
 * interface to enrich HTTP requests with trace information. Specifically, it
 * adds the current trace ID as a header to outgoing requests, enabling better
 * observability and traceability in distributed systems.
 * </p>
 *
 * <p>
 * This filter uses Spring Cloud Sleuth's Tracer to access the current span's
 * trace ID and includes it in the request headers, allowing tracing systems to
 * correlate requests across different services.
 * </p>
 *
 * @param tracer
 *            the {@link Tracer} instance used to retrieve the current trace ID
 *            for adding to request headers
 * @author hoangtien2k3
 */
@Component
public record TraceIdWebClientFilter(Tracer tracer) implements ExchangeFilterFunction {

    /**
     * {@inheritDoc}
     *
     * <p>
     * Filters the client request to add the trace ID from the current span to the
     * request headers. If no span is present, the original request is sent without
     * modification.
     * </p>
     */
    @NotNull
    @Override
    public Mono<ClientResponse> filter(@NotNull ClientRequest request, @NotNull ExchangeFunction next) {
        return Mono.defer(() -> {
            var span = tracer.currentSpan();
            // Check if there is a current span to extract the trace ID
            if (span != null) {
                // Create a modified request with the trace ID header
                ClientRequest modifiedRequest = ClientRequest.from(request)
                        .header("X-B3-TRACE-ID", span.context().traceIdString())
                        .build();
                // Exchange the modified request
                return next.exchange(modifiedRequest);
            } else {
                // No span, exchange the original request
                return next.exchange(request);
            }
        });
    }
}
