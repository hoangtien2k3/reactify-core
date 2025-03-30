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
package com.reactify.annotations.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.reactify.annotations.LocalCache;
import com.reactify.util.DataUtil;
import java.util.Optional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;
import reactor.cache.CacheMono;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

/**
 * <p>
 * Aspect for handling caching via @LocalCache annotation.
 * </p>
 *
 * <p>
 * This aspect intercepts methods annotated with {@link LocalCache} and provides
 * caching functionality using Caffeine.
 * </p>
 *
 * @author hoangtien2k3
 */
@Aspect
@Configuration
public class CacheAspect {

    /**
     * A static logger instance for logging messages
     */
    private static final Logger log = LoggerFactory.getLogger(CacheAspect.class);

    @Pointcut("@annotation(com.reactify.annotations.LocalCache)")
    private void processAnnotation() {}

    /**
     * <p>
     * Handles caching logic for methods annotated with {@link LocalCache}.
     * </p>
     *
     * @param joinPoint
     *            the intercepted method call
     * @return cached or computed result
     * @throws Throwable
     *             if an exception occurs during method execution
     */
    @Around("processAnnotation()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object key = SimpleKeyGenerator.generateKey(args);
        String nameCache = ClassUtils.getUserClass(joinPoint.getTarget().getClass())
                        .getSimpleName() + "." + joinPoint.getSignature().getName();
        Cache<Object, Object> cache = CacheStore.getCache(nameCache);
        log.debug("Checking cache for method: {} with key: {}", nameCache, key);
        return CacheMono.lookup(
                        k -> Mono.justOrEmpty(!DataUtil.isNullOrEmpty(cache) ? cache.getIfPresent(key) : Mono.empty())
                                .map(Signal::next),
                        key)
                .onCacheMissResume(Mono.defer(() -> {
                    try {
                        Object result = joinPoint.proceed(args);
                        if (!(result instanceof Mono<?>)) {
                            log.warn(
                                    "Method {} must return a Mono<?> but got: {}",
                                    nameCache,
                                    result.getClass().getSimpleName());
                            return Mono.error(new IllegalStateException("Method must return Mono<?>"));
                        }
                        @SuppressWarnings("unchecked")
                        var resultCast = (Mono<Object>) result;
                        return resultCast;
                    } catch (Throwable ex) {
                        log.error("Execution error in {} - {}", nameCache, ex.getMessage(), ex);
                        return Mono.error(ex);
                    }
                }))
                .andWriteWith((k, sig) -> Mono.fromRunnable(() -> {
                    Optional.ofNullable(sig)
                            .map(Signal::get)
                            .filter(value -> !(value instanceof Optional && ((Optional<?>) value).isEmpty()))
                            .ifPresent(value -> {
                                cache.put(k, value);
                                log.debug("Cached value for key: {} in method: {}", k, nameCache);
                            });
                }));
    }
}
