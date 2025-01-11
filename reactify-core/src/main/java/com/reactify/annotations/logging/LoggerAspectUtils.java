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
package com.reactify.annotations.logging;

import brave.Span;
import brave.Tracer;
import com.reactify.annotations.LogPerformance;
import com.reactify.exception.BusinessException;
import com.reactify.util.DataUtil;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.PostConstruct;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/**
 * <p>
 * The {@code LoggerAspectUtils} class is a utility component that provides
 * logging functionalities for performance monitoring in the application. It
 * captures method execution details, including input parameters, output
 * results, and performance metrics, utilizing Spring's AOP capabilities.
 * </p>
 *
 * <p>
 * This class is annotated with {@link org.springframework.stereotype.Component}
 * to allow Spring to manage it as a bean, and it is initialized with a
 * {@link brave.Tracer} for distributed tracing support. The logging is
 * performed using different loggers for performance metrics and general
 * logging.
 * </p>
 *
 * <p>
 * The performance logging can be configured through the {@code detailException}
 * property, which determines whether to log detailed exceptions.
 * </p>
 *
 * @author hoangtien2k3
 */
@Component
public class LoggerAspectUtils {

    private static final Logger logPerf = LoggerFactory.getLogger("perfLogger");
    private static final Logger log = LoggerFactory.getLogger("LoggerAspect");

    private final Tracer tracer;

    @Value("${debug.detailException:true}")
    private boolean detailException;

    /**
     * Constructs a new instance of {@code LoggerAspectUtils}.
     *
     * @param tracer
     *            the tracer used for logging and tracing operations.
     */
    public LoggerAspectUtils(Tracer tracer) {
        this.tracer = tracer;
    }

    @PostConstruct
    private void init() {}

    /**
     * <p>
     * Intercepts method executions to log performance metrics and other relevant
     * information. This method processes the method's input and output, and logs
     * performance data, including execution time.
     * </p>
     *
     * @param joinPoint
     *            a {@link org.aspectj.lang.ProceedingJoinPoint} object representing
     *            the intercepted method call
     * @return a {@link java.lang.Object} representing the result of the method
     *         execution
     * @throws java.lang.Throwable
     *             if any error occurs during the execution of the method
     */
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogPerformance logPerformance = method.getAnnotation(LogPerformance.class);

        long start = System.currentTimeMillis();
        String name = joinPoint.getTarget().getClass().getSimpleName() + "."
                + joinPoint.getSignature().getName();

        String logType = joinPoint.getTarget().getClass().getName();
        String actionType = joinPoint.getTarget().getClass().getSimpleName();
        boolean logOutput = true;
        boolean logInput = true;
        String title = null;
        if (logPerformance != null) {
            if (!DataUtil.isNullOrEmpty(logPerformance.logType())) {
                logType = logPerformance.logType();
            }
            if (!DataUtil.isNullOrEmpty(logPerformance.actionType())) {
                actionType = logPerformance.actionType();
            }
            logOutput = logPerformance.logOutput();
            logInput = logPerformance.logInput();
            title = logPerformance.title();
        }

        Span newSpan = tracer.nextSpan().name(name);

        var result = joinPoint.proceed();
        if (result instanceof Mono) {
            return logMonoResult(
                    joinPoint, start, (Mono<?>) result, newSpan, name, logType, actionType, logOutput, logInput, title);
        }
        if (result instanceof Flux) {
            return logFluxResult(
                    joinPoint, start, (Flux<?>) result, newSpan, name, logType, actionType, logOutput, logInput, title);
        } else {
            return result;
        }
    }

    /**
     * <p>
     * Handles logging for methods that return a {@link Mono}. It records the input
     * and output details as well as performance metrics.
     * </p>
     *
     * @param joinPoint
     *            a {@link ProceedingJoinPoint} object representing the intercepted
     *            method call
     * @param start
     *            the start time of the method execution in milliseconds
     * @param result
     *            the {@link Mono} result from the method execution
     * @param newSpan
     *            the {@link Span} representing the trace span for the execution
     * @param name
     *            the name of the method being executed
     * @param logType
     *            the type of logging to be used
     * @param actionType
     *            the action type for the logging
     * @param logOutput
     *            flag indicating whether to log output
     * @param logInput
     *            flag indicating whether to log input
     * @param title
     *            a title for the log entry
     * @return a {@link Mono} containing the logged result
     */
    private Mono<?> logMonoResult(
            ProceedingJoinPoint joinPoint,
            long start,
            Mono<?> result,
            Span newSpan,
            String name,
            String logType,
            String actionType,
            boolean logOutput,
            boolean logInput,
            String title) {
        var contextRef = new AtomicReference<Context>();
        return result.doOnSuccess(o -> {
                    Object[] args = null;
                    if (logInput) {
                        args = joinPoint.getArgs();
                    }
                    if (logOutput) {
                        logPerf(contextRef, newSpan, name, start, "0", o, logType, actionType, args, title);
                    } else {
                        logPerf(contextRef, newSpan, name, start, "0", null, logType, actionType, args, title);
                    }
                })
                .contextWrite(context -> {
                    contextRef.set(context);
                    return context;
                })
                .doOnError(o -> {
                    if (detailException) log.error(" ", o);
                    else log.error(o.toString());

                    if (o instanceof BusinessException) {
                        logPerf(contextRef, newSpan, name, start, "0", o, logType, actionType, null, title);
                    } else {
                        logPerf(contextRef, newSpan, name, start, "1", o, logType, actionType, null, title);
                    }
                });
    }

    /**
     * <p>
     * Handles logging for methods that return a {@link Flux}. It records the
     * completion and error details along with performance metrics.
     * </p>
     *
     * @param joinPoint
     *            a {@link ProceedingJoinPoint} object representing the intercepted
     *            method call
     * @param start
     *            the start time of the method execution in milliseconds
     * @param result
     *            the {@link Flux} result from the method execution
     * @param newSpan
     *            the {@link Span} representing the trace span for the execution
     * @param name
     *            the name of the method being executed
     * @param logType
     *            the type of logging to be used
     * @param actionType
     *            the action type for the logging
     * @param logOutput
     *            flag indicating whether to log output
     * @param logInput
     *            flag indicating whether to log input
     * @param title
     *            a title for the log entry
     * @return a {@link Flux} containing the logged result
     */
    private Flux<?> logFluxResult(
            ProceedingJoinPoint joinPoint,
            long start,
            Flux<?> result,
            Span newSpan,
            String name,
            String logType,
            String actionType,
            boolean logOutput,
            boolean logInput,
            String title) {
        var contextRef = new AtomicReference<Context>();
        return result.doFinally(
                        o -> logPerf(contextRef, newSpan, name, start, "1", null, logType, actionType, null, title))
                .contextWrite(context -> {
                    contextRef.set(context);
                    return context;
                })
                .doOnError(o -> logPerf(contextRef, newSpan, name, start, "0", o, logType, actionType, null, title));
    }

    /**
     * <p>
     * Logs performance metrics for successful method execution. It records the
     * duration and the result of the method call.
     * </p>
     *
     * @param contextRef
     *            an {@link AtomicReference} containing the current context
     * @param newSpan
     *            the {@link Span} representing the trace span for the execution
     * @param name
     *            the name of the method being executed
     * @param start
     *            the start time of the method execution in milliseconds
     * @param result
     *            a string indicating the result status ("0" for success, "1" for
     *            failure)
     * @param o
     *            the output object from the method execution, may be {@code null}
     */
    private void logPerf(
            AtomicReference<Context> contextRef, Span newSpan, String name, Long start, String result, Object o) {
        newSpan.finish();
        long duration = System.currentTimeMillis() - start;
        if (duration < 50) return;
        logPerf.info("{} {} {} M2 {}", name, duration, result, o == null ? "-" : o.toString());
    }

    /**
     * <p>
     * Logs performance metrics, including the method's execution details, input
     * parameters, output results, and action type. The logging is performed only if
     * the execution duration exceeds a specified threshold.
     * </p>
     *
     * @param contextRef
     *            an {@link AtomicReference} containing the current context
     * @param newSpan
     *            the {@link Span} representing the trace span for the execution
     * @param name
     *            the name of the method being executed
     * @param startTime
     *            the start time of the method execution in milliseconds
     * @param result
     *            a string indicating the result status ("0" for success, "1" for
     *            failure)
     * @param obj
     *            the output object from the method execution, may be {@code null}
     * @param logType
     *            the type of logging to be used
     * @param actionType
     *            the action type for the logging
     * @param args
     *            the input arguments for the method execution
     * @param title
     *            a title for the log entry
     */
    private void logPerf(
            AtomicReference<Context> contextRef,
            Span newSpan,
            String name,
            Long startTime,
            String result,
            Object obj,
            String logType,
            String actionType,
            Object[] args,
            String title) {
        newSpan.finish();
        long endTime = System.currentTimeMillis();
        if (endTime - startTime > 50) {
            LoggerQueue.getInstance()
                    .addQueue(
                            contextRef,
                            newSpan,
                            name,
                            startTime,
                            endTime,
                            result,
                            obj,
                            logType,
                            actionType,
                            args,
                            title);
        }
    }
}
