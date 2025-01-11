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

import brave.Span;
import java.util.concurrent.atomic.AtomicReference;
import reactor.util.context.Context;

/**
 * Record representing log information within the system.
 *
 * @param contextRef
 *            A reference to the current context.
 * @param newSpan
 *            A new Span object to track the operation.
 * @param service
 *            The name of the service processing the request.
 * @param startTime
 *            The timestamp (in milliseconds) when logging started.
 * @param endTime
 *            The timestamp (in milliseconds) when logging ended.
 * @param result
 *            The result of the operation (e.g., SUCCESS, FAILURE).
 * @param response
 *            The response object from the service.
 * @param logType
 *            The type of log (e.g., INFO, ERROR).
 * @param actionType
 *            The type of action being performed (e.g., CREATE, UPDATE, DELETE).
 * @param args
 *            The arguments passed into the action.
 * @param title
 *            A brief title or description of the log.
 * @author hoangtien2k3
 */
public record LoggerDTO(
        AtomicReference<Context> contextRef,
        Span newSpan,
        String service,
        Long startTime,
        Long endTime,
        String result,
        Object response,
        String logType,
        String actionType,
        Object[] args,
        String title) {}
