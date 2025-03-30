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

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.util.concurrent.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

/**
 * <p>
 * The WebClientMonitoringFilter class implements the ExchangeFilterFunction
 * interface to provide monitoring capabilities for HTTP requests made through a
 * WebClient instance. It logs the execution time of the request and records
 * metrics using a MeterRegistry.
 * </p>
 *
 * <p>
 * This filter is particularly useful for tracking the performance of API calls
 * in a Spring application. It helps developers monitor request durations,
 * identify performance issues, and improve the overall responsiveness of their
 * applications.
 * </p>
 *
 * @author hoangtien2k3
 */
public class WebClientMonitoringFilter implements ExchangeFilterFunction {

    /**
     * A static logger instance for logging messages
     */
    private static final Logger log = LoggerFactory.getLogger(WebClientMonitoringFilter.class);

    /**
     * the MeterRegistry used to record metrics for the monitored WebClient requests
     */
    private final MeterRegistry meterRegistry;

    /**
     * Constant
     * <code>METRICS_WEBCLIENT_START_TIME="WebClientMonitoringFilter.class.getName"{trunked}</code>
     */
    private static final String METRICS_WEBCLIENT_START_TIME =
            WebClientMonitoringFilter.class.getName() + ".START_TIME";

    public WebClientMonitoringFilter(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Filters the client request to monitor the execution time and log the details
     * of the request and response. It captures the start time, executes the
     * request, and logs the outcome upon completion, including any errors that
     * occur.
     * </p>
     */
    @NotNull
    @Override
    public Mono<ClientResponse> filter(@NotNull ClientRequest clientRequest, ExchangeFunction exchangeFunction) {
        return exchangeFunction
                .exchange(clientRequest)
                .doOnEach(signal -> {
                    if (!signal.isOnComplete()) {
                        Long startTime = signal.getContextView().get(METRICS_WEBCLIENT_START_TIME);
                        ClientResponse clientResponse = signal.get();
                        Throwable throwable = signal.getThrowable();
                        if (throwable != null) {
                            log.error(
                                    "WebClient request to {} failed: {}", clientRequest.url(), throwable.getMessage());
                        } else {
                            assert clientResponse != null;
                            log.info(
                                    "WebClient request to {} completed with status code: {}",
                                    clientRequest.url(),
                                    clientResponse.statusCode());
                        }
                        // record the execution time
                        long duration = System.nanoTime() - startTime;
                        Timer.builder("http.client.requests")
                                .description("Timer for WebClient operations")
                                .publishPercentiles(0.95, 0.99)
                                .register(meterRegistry)
                                .record(duration, TimeUnit.NANOSECONDS);
                        log.info(
                                "Monitoring WebClient API {}: {} s",
                                clientRequest.url(),
                                (double) duration / Math.pow(10, 9));
                    }
                })
                .contextWrite((contextView) -> contextView.put(METRICS_WEBCLIENT_START_TIME, System.nanoTime()));
    }
}
