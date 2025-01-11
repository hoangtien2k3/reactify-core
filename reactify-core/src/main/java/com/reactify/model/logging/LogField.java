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
package com.reactify.model.logging;

/**
 * Record representing log fields for tracking service requests and responses.
 *
 * @param traceId
 *            a unique identifier for tracing the request across services.
 * @param requestId
 *            a unique identifier for the request within the service.
 * @param service
 *            the name of the service handling the request.
 * @param duration
 *            the time taken to process the request in milliseconds.
 * @param logType
 *            the type of log (e.g., INFO, ERROR).
 * @param actionType
 *            the type of action performed (e.g., CREATE, UPDATE, DELETE).
 * @param startTime
 *            the timestamp when the request started processing.
 * @param endTime
 *            the timestamp when the request finished processing.
 * @param clientAddress
 *            the address of the client making the request.
 * @param title
 *            a brief title or description of the log entry.
 * @param inputs
 *            the inputs or parameters received in the request.
 * @param response
 *            the response returned by the service.
 * @param result
 *            the outcome of the request processing (e.g., SUCCESS, FAILURE).
 * @author hoangtien2k3
 */
public record LogField(
        String traceId,
        String requestId,
        String service,
        Long duration,
        String logType,
        String actionType,
        Long startTime,
        Long endTime,
        String clientAddress,
        String title,
        String inputs,
        String response,
        String result) {}
