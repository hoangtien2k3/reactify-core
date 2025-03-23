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

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * <p>
 * The {@code LoggerAspect} class is an aspect that handles logging for
 * performance monitoring across various components of the application. It
 * utilizes Spring AOP (Aspect-Oriented Programming) to intercept method calls
 * in controllers, services, repositories, and clients and logs performance
 * metrics accordingly.
 * </p>
 *
 * <p>
 * This aspect excludes methods from the Spring Boot Actuator package to prevent
 * unnecessary logging of internal application health checks.
 * </p>
 *
 * <p>
 * The class is annotated with {@link org.aspectj.lang.annotation.Aspect} to
 * define it as an aspect, and
 * {@link org.springframework.context.annotation.Configuration} to indicate that
 * it provides Spring configuration.
 * </p>
 *
 * <p>
 * <strong>Usage:</strong> To enable performance logging, annotate methods or
 * classes with {@link com.reactify.annotations.LogPerformance}.
 * </p>
 *
 * @author hoangtien2k3
 */
@Aspect
@Configuration
@RequiredArgsConstructor
public class LoggerAspect {

    private final LoggerAspectUtils loggerAspectUtils;

    /**
     * <p>
     * Pointcut definition for methods in the controller, service, repository, and
     * client packages that should be logged for performance metrics.
     * </p>
     */
    @Pointcut("execution(* com.reactify.*.controller..*(..)) || " + "execution(* com.reactify.*.service..*(..)) || "
            + "execution(* com.reactify.*.repository..*(..)) || " + "execution(* com.reactify.*.client..*(..)) && "
            + "!execution(* org.springframework.boot.actuate..*(..))")
    public void performancePointCut() {}

    /**
     * <p>
     * Pointcut for methods annotated with
     * {@link com.reactify.annotations.LogPerformance}.
     * </p>
     */
    @Pointcut("@annotation(com.reactify.annotations.LogPerformance)")
    private void logPerfMethods() {}

    /**
     * <p>
     * logAround.
     * </p>
     *
     * @param joinPoint
     *            a {@link org.aspectj.lang.ProceedingJoinPoint} object
     * @return a {@link java.lang.Object} object
     * @throws java.lang.Throwable
     *             if any.
     */
    @Around("performancePointCut() || logPerfMethods()")
    public Mono<Object> logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return loggerAspectUtils.logAround(joinPoint);
    }
}
