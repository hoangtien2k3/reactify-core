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
package com.reactify.filter.http;

import brave.Span;
import brave.Tracer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reactify.constants.CommonConstant;
import com.reactify.model.GatewayContext;
import com.reactify.util.DataUtil;
import com.reactify.util.RequestUtils;
import com.reactify.util.TruncateUtils;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 * <p>
 * The <code>PerformanceLogFilter</code> class implements a WebFilter for
 * logging performance metrics of HTTP requests and responses in a Spring
 * WebFlux application. It utilizes the Sleuth Tracer for distributed tracing
 * and provides detailed logs for request processing times, statuses, and
 * headers while ensuring sensitive data is truncated for security.
 * <p>
 * This filter is designed to log performance metrics selectively based on the
 * application profile (e.g., excluding certain details in production) and to
 * handle logging for both requests and responses, including any errors that
 * occur during request processing.
 * </p>
 *
 * <p>
 * The main responsibilities of this filter include:
 * <ul>
 * <li>Measuring and logging the time taken to process requests.</li>
 * <li>Logging request and response details, including headers and bodies, with
 * truncation for safety.</li>
 * <li>Integrating with Sleuth for distributed tracing.</li>
 * </ul>
 *
 * <p>
 * This class is annotated with {@link org.springframework.stereotype.Component}
 * to allow Spring to manage it as a bean, and it implements the
 * {@link org.springframework.web.server.WebFilter} and
 * {@link org.springframework.core.Ordered} interfaces.
 * </p>
 *
 * @author hoangtien2k3
 */
@Component
public class PerformanceLogFilter implements WebFilter, Ordered {

    private final Tracer tracer;
    private static final Logger logPerf = LoggerFactory.getLogger("perfLogger");
    private static final Logger reqResLog = LoggerFactory.getLogger("reqResLogger");
    private static final int MAX_BYTE = 800; // Max byte allow to print
    private final Environment environment;

    /**
     * Constructs a new instance of {@code PerformanceLogFilter}.
     *
     * @param tracer
     *            the tracer used for tracing operations.
     * @param environment
     *            the environment information for the application.
     */
    public PerformanceLogFilter(Tracer tracer, Environment environment) {
        this.tracer = tracer;
        this.environment = environment;
    }

    /**
     * {@inheritDoc}
     *
     * Filters the incoming HTTP request and logs performance metrics.
     */
    @NotNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NotNull WebFilterChain chain) {
        long startMillis = System.currentTimeMillis();
        String name =
                exchange.getRequest().getPath().pathWithinApplication().value().substring(1);
        Span newSpan = tracer.nextSpan().name(name);

        if (exchange.getRequest().getPath().pathWithinApplication().value().contains("actuator")) {
            return chain.filter(exchange);
        }
        return chain.filter(exchange)
                .doOnSuccess(o -> logPerf(exchange, newSpan, name, startMillis, "Success", null))
                .doOnError(o -> logPerf(exchange, newSpan, name, startMillis, "Failed", o))
                .contextWrite(context -> {
                    setTraceIdFromContext(newSpan.context().traceIdString());
                    return context;
                })
                .then(Mono.fromRunnable(() -> {
                    if (!List.of(environment.getActiveProfiles()).contains("prod")) {
                        this.logReqResponse(exchange);
                    }
                }));
    }

    /**
     * Logs the performance metrics of the request.
     *
     * @param exchange
     *            the current ServerWebExchange
     * @param newSpan
     *            the Span associated with the request
     * @param name
     *            the name of the request
     * @param start
     *            the start time in milliseconds
     * @param result
     *            the result status (Success or Failed)
     * @param o
     *            the Throwable if an error occurred
     */
    private void logPerf(
            ServerWebExchange exchange, Span newSpan, String name, Long start, String result, Throwable o) {
        newSpan.finish();
        long duration = System.currentTimeMillis() - start;
        if (duration < 50 || name.equals("health")) return;

        setTraceIdInMDC(newSpan.context().traceIdString());
        String msisdn = exchange.getAttribute(CommonConstant.MSISDN_TOKEN);
        MDC.put(CommonConstant.MSISDN_TOKEN, !DataUtil.isNullOrEmpty(msisdn) ? msisdn : "-");
        String requestId = exchange.getRequest().getHeaders().getFirst("Request-Id");
        MDC.put(CommonConstant.REQUEST_ID, !DataUtil.isNullOrEmpty(requestId) ? requestId : "-");

        logPerf.info("{} {} {} A2 {}", name, duration, result, o == null ? "-" : o.getMessage());
    }

    /**
     * Logs the performance metrics of the request.
     *
     * @param contextRef
     *            the current ServerWebExchange
     * @param newSpan
     *            the Span associated with the request
     * @param name
     *            the name of the request
     * @param start
     *            the start time in milliseconds
     * @param result
     *            the result status (Success or Failed)
     * @param o
     *            the Throwable if an error occurred
     */
    private void logPerf(
            AtomicReference<Context> contextRef, Span newSpan, String name, Long start, String result, Throwable o) {
        newSpan.finish();
        long duration = System.currentTimeMillis() - start;
        if (duration < 50 || "health".equals(name)) return;

        setTraceIdInMDC(newSpan.context().traceIdString());
        if (contextRef.get() != null) {
            Context context = contextRef.get();
            if (context.hasKey(CommonConstant.MSISDN_TOKEN))
                MDC.put(CommonConstant.MSISDN_TOKEN, contextRef.get().get(CommonConstant.MSISDN_TOKEN));
            else MDC.put(CommonConstant.MSISDN_TOKEN, "-");
        }

        logPerf.info("{} {} {} A2 {}", name, duration, result, o == null ? "-" : o.getMessage());
    }

    /**
     * Logs the request and response details.
     *
     * @param exchange
     *            the current ServerWebExchange
     */
    private void logReqResponse(ServerWebExchange exchange) {
        List<String> logs = new ArrayList<>();
        logRequest(exchange, logs);
        logResponse(exchange, logs);
        reqResLog.info(String.join(" | ", logs));
    }

    /**
     * Logs the details of the HTTP request.
     *
     * @param exchange
     *            the current ServerWebExchange
     * @param logs
     *            the list to collect log messages
     */
    private void logRequest(ServerWebExchange exchange, List<String> logs) {
        ServerHttpRequest request = exchange.getRequest();
        URI requestURI = request.getURI();
        HttpHeaders headers = request.getHeaders();

        if (requestURI.getPath().startsWith("/"))
            logs.add(String.format("%s", requestURI.getPath().substring(1)));
        else logs.add(String.format("%s", requestURI.getPath()));
        logs.add(String.format("%s", request.getMethod()));
        logs.add(String.format("%s", RequestUtils.getIpAddress(request)));
        logs.add(String.format("%s", requestURI.getHost()));

        var logHeader = new StringBuilder();
        headers.forEach((key, value) -> logHeader.append(String.format("{%s:%s}", key, value)));
        if (!logHeader.isEmpty()) {
            logs.add(String.format("%s", logHeader));
        }

        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if (gatewayContext != null && !gatewayContext.getReadRequestData()) {
            reqResLog.debug("[RequestLogFilter]Properties Set Not To Read Request Data");
            return;
        }
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        if (!queryParams.isEmpty()) {
            var logQuery = new StringBuilder();
            queryParams.forEach((key, value) -> logQuery.append(String.format("{%s:%s}", key, value)));
            logs.add(String.format("%s", logQuery));
        } else {
            logs.add("-");
        }

        MediaType contentType = headers.getContentType();
        long length = headers.getContentLength();
        if (length > 0 && contentType != null) {
            if (!DataUtil.isNullOrEmpty(gatewayContext)) {
                if (contentType.includes(MediaType.APPLICATION_JSON)) {
                    String requestBody = gatewayContext.getRequestBody();
                    logs.add(String.format("%s", TruncateUtils.truncateBody(requestBody, MAX_BYTE)));
                } else if (contentType.includes(MediaType.APPLICATION_FORM_URLENCODED)) {
                    MultiValueMap<String, String> formData = gatewayContext.getFormData();
                    logs.add(String.format("%s", truncateBody(formData)));
                } else {
                    logs.add("-");
                }
            }
        } else {
            logs.add("-");
        }
    }

    /**
     * Truncates the body of a form data for logging.
     *
     * @param formData
     *            the MultiValueMap of form data
     * @return a truncated string representation of the form data
     */
    private String truncateBody(MultiValueMap<String, String> formData) {
        StringBuilder messageResponse = new StringBuilder();
        Set<String> keys = formData.keySet();
        for (String key : keys) {
            messageResponse.append(key).append(":").append(truncateBody(formData.get(key)));
        }
        return messageResponse.toString();
    }

    /**
     * Truncates the body of a list of messages for logging.
     *
     * @param messageList
     *            the list of messages
     * @return a truncated string representation of the messages
     */
    private String truncateBody(List<String> messageList) {
        StringBuilder response = new StringBuilder();
        messageList.forEach(
                item -> response.append(TruncateUtils.truncateBody(item, 200)).append(","));
        return response.toString();
    }

    /**
     * Logs the response details excluding the response body.
     *
     * @param exchange
     *            the current ServerWebExchange
     * @param logs
     *            the list to collect log messages
     */
    private void logResponse(ServerWebExchange exchange, List<String> logs) {
        ServerHttpResponse response = exchange.getResponse();
        logs.add(String.format(
                "%s", Objects.requireNonNull(response.getStatusCode()).value()));
        GatewayContext gatewayContext = exchange.getAttribute(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if (gatewayContext != null && gatewayContext.getReadResponseData()) {
            logs.add(String.format("%s", truncateBody(gatewayContext.getResponseBody())));
        }
    }

    /**
     * Returns the order value of this filter.
     *
     * @return the order value
     */
    private String truncateBody(Object responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(responseBody);
        } catch (Exception e) {
            reqResLog.error("Exception when parse response to string, ignore response", e);
            return "Truncated and remove if has exception";
        }
    }

    /**
     * Sets the trace ID in the MDC for logging.
     *
     * @param traceId
     *            the trace ID to set
     */
    private void setTraceIdInMDC(String traceId) {
        if (!DataUtil.isNullOrEmpty(traceId)) {
            MDC.put("X-B3-TraceId", traceId);
        }
    }

    /**
     * Sets the trace ID from the context.
     *
     * @param traceId
     *            the trace ID to set
     */
    private void setTraceIdFromContext(String traceId) {
        setTraceIdInMDC(traceId);
    }

    /**
     * {@inheritDoc}
     *
     * Returns the order value of this filter.
     */
    @Override
    public int getOrder() {
        return 7;
    }
}
