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

import java.util.Objects;

/**
 * Record representing log fields for tracking service requests and responses.
 *
 * @author hoangtien2k3
 */
public class LogField {
    private final String traceId;
    private final String requestId;
    private final String service;
    private final Long duration;
    private final String logType;
    private final String actionType;
    private final Long startTime;
    private final Long endTime;
    private final String clientAddress;
    private final String title;
    private final String inputs;
    private final String response;
    private final String result;

    public LogField(
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
            String result) {
        this.traceId = traceId;
        this.requestId = requestId;
        this.service = service;
        this.duration = duration;
        this.logType = logType;
        this.actionType = actionType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.clientAddress = clientAddress;
        this.title = title;
        this.inputs = inputs;
        this.response = response;
        this.result = result;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getService() {
        return service;
    }

    public Long getDuration() {
        return duration;
    }

    public String getLogType() {
        return logType;
    }

    public String getActionType() {
        return actionType;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public String getTitle() {
        return title;
    }

    public String getInputs() {
        return inputs;
    }

    public String getResponse() {
        return response;
    }

    public String getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogField logField = (LogField) o;
        return Objects.equals(traceId, logField.traceId)
                && Objects.equals(requestId, logField.requestId)
                && Objects.equals(service, logField.service)
                && Objects.equals(duration, logField.duration)
                && Objects.equals(logType, logField.logType)
                && Objects.equals(actionType, logField.actionType)
                && Objects.equals(startTime, logField.startTime)
                && Objects.equals(endTime, logField.endTime)
                && Objects.equals(clientAddress, logField.clientAddress)
                && Objects.equals(title, logField.title)
                && Objects.equals(inputs, logField.inputs)
                && Objects.equals(response, logField.response)
                && Objects.equals(result, logField.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                traceId,
                requestId,
                service,
                duration,
                logType,
                actionType,
                startTime,
                endTime,
                clientAddress,
                title,
                inputs,
                response,
                result);
    }
}
