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
 * @author hoangtien2k3
 */
public class LoggerDTO {
    private final AtomicReference<Context> contextRef;
    private final Span newSpan;
    private final String service;
    private final Long startTime;
    private final Long endTime;
    private final String result;
    private final Object response;
    private final String logType;
    private final String actionType;
    private final Object[] args;
    private final String title;

    private LoggerDTO(Builder builder) {
        this.contextRef = builder.contextRef;
        this.newSpan = builder.newSpan;
        this.service = builder.service;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.result = builder.result;
        this.response = builder.response;
        this.logType = builder.logType;
        this.actionType = builder.actionType;
        this.args = builder.args;
        this.title = builder.title;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private AtomicReference<Context> contextRef;
        private Span newSpan;
        private String service;
        private Long startTime;
        private Long endTime;
        private String result;
        private Object response;
        private String logType;
        private String actionType;
        private Object[] args;
        private String title;

        public Builder contextRef(AtomicReference<Context> contextRef) {
            this.contextRef = contextRef;
            return this;
        }

        public Builder newSpan(Span newSpan) {
            this.newSpan = newSpan;
            return this;
        }

        public Builder service(String service) {
            this.service = service;
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

        public Builder result(String result) {
            this.result = result;
            return this;
        }

        public Builder response(Object response) {
            this.response = response;
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

        public Builder args(Object[] args) {
            this.args = args;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public LoggerDTO build() {
            return new LoggerDTO(this);
        }
    }

    public AtomicReference<Context> getContextRef() {
        return contextRef;
    }

    public Span getNewSpan() {
        return newSpan;
    }

    public String getService() {
        return service;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public String getResult() {
        return result;
    }

    public Object getResponse() {
        return response;
    }

    public String getLogType() {
        return logType;
    }

    public String getActionType() {
        return actionType;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getTitle() {
        return title;
    }
}
