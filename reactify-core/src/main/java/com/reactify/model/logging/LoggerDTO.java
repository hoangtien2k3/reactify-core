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
import java.util.Objects;
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

    public LoggerDTO(
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
            String title) {
        this.contextRef = contextRef;
        this.newSpan = newSpan;
        this.service = service;
        this.startTime = startTime;
        this.endTime = endTime;
        this.result = result;
        this.response = response;
        this.logType = logType;
        this.actionType = actionType;
        this.args = args;
        this.title = title;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoggerDTO loggerDTO = (LoggerDTO) o;
        return Objects.equals(contextRef, loggerDTO.contextRef)
                && Objects.equals(newSpan, loggerDTO.newSpan)
                && Objects.equals(service, loggerDTO.service)
                && Objects.equals(startTime, loggerDTO.startTime)
                && Objects.equals(endTime, loggerDTO.endTime)
                && Objects.equals(result, loggerDTO.result)
                && Objects.equals(response, loggerDTO.response)
                && Objects.equals(logType, loggerDTO.logType)
                && Objects.equals(actionType, loggerDTO.actionType)
                && Objects.deepEquals(args, loggerDTO.args)
                && Objects.equals(title, loggerDTO.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                contextRef, newSpan, service, startTime, endTime, result, response, logType, actionType, args, title);
    }

    @Override
    public String toString() {
        return "LoggerDTO{" + "contextRef=" + contextRef + ", newSpan=" + newSpan + ", service='" + service + '\''
                + ", startTime=" + startTime + ", endTime=" + endTime + ", result='" + result + '\'' + ", response="
                + response + ", logType='" + logType + '\'' + ", actionType='" + actionType + '\'' + ", args="
                + (args != null ? java.util.Arrays.toString(args) : "null") + ", title='" + title + '\'' + '}';
    }
}
