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

    private LogField(Builder builder) {
        this.traceId = builder.traceId;
        this.requestId = builder.requestId;
        this.service = builder.service;
        this.duration = builder.duration;
        this.logType = builder.logType;
        this.actionType = builder.actionType;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.clientAddress = builder.clientAddress;
        this.title = builder.title;
        this.inputs = builder.inputs;
        this.response = builder.response;
        this.result = builder.result;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String traceId;
        private String requestId;
        private String service;
        private Long duration;
        private String logType;
        private String actionType;
        private Long startTime;
        private Long endTime;
        private String clientAddress;
        private String title;
        private String inputs;
        private String response;
        private String result;

        public Builder traceId(String traceId) {
            this.traceId = traceId;
            return this;
        }

        public Builder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder service(String service) {
            this.service = service;
            return this;
        }

        public Builder duration(Long duration) {
            this.duration = duration;
            return this;
        }

        public Builder logType(String logType) {
            this.logType = logType;
            return this;
        }

        public Builder actionType(String actionType) {
            this.actionType = actionType;
            return this;
        }

        public Builder startTime(Long startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(Long endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder clientAddress(String clientAddress) {
            this.clientAddress = clientAddress;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder inputs(String inputs) {
            this.inputs = inputs;
            return this;
        }

        public Builder response(String response) {
            this.response = response;
            return this;
        }

        public Builder result(String result) {
            this.result = result;
            return this;
        }

        public LogField build() {
            return new LogField(this);
        }
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
}
