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
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;
import reactor.cache.CacheMono;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

/**
 * <p>
 * The {@code CacheAspect} class provides caching functionality for methods
 * annotated with {@link com.reactify.annotations.LocalCache}. This aspect
 * intercepts method calls, checks the cache for existing results, and returns
 * cached results if available. If no cached result is found, the method is
 * executed and the result is stored in the cache for future calls.
 * </p>
 *
 * <p>
 * This class uses Spring AOP (Aspect-Oriented Programming) features to
 * implement caching logic around method executions, providing a way to enhance
 * performance by avoiding redundant computations or data retrievals.
 * </p>
 *
 * <p>
 * The class is annotated with {@link org.aspectj.lang.annotation.Aspect} and
 * {@link org.springframework.context.annotation.Configuration}, making it a
 * Spring managed bean that can intercept method calls. The logging is managed
 * using the Lombok {@link lombok.extern.slf4j.Slf4j} annotation.
 * </p>
 *
 * @author hoangtien2k3
 */
@Aspect
@Configuration
@Slf4j
public class CacheAspect {

    /**
     * Constructs a new instance of {@code CacheAspect}.
     */
    public CacheAspect() {}

    /**
     * <p>
     * Pointcut that matches methods annotated with
     * {@link com.reactify.annotations.LocalCache}.
     * </p>
     */
    @Pointcut("@annotation(com.reactify.annotations.LocalCache)")
    private void processAnnotation() {}

    /**
     * <p>
     * Around advice that intercepts method calls annotated with
     * {@link com.reactify.annotations.LocalCache}. This method checks for a cached
     * result using the generated key based on the method arguments. If a cached
     * result is found, it is returned; otherwise, the method is executed, and the
     * result is stored in the cache.
     * </p>
     *
     * @param joinPoint
     *            a {@link org.aspectj.lang.ProceedingJoinPoint} object representing
     *            the method execution context.
     * @return an {@link java.lang.Object} that is the result of the method
     *         execution or the cached result.
     * @throws java.lang.Throwable
     *             if any exception occurs during method execution or while
     *             accessing the cache.
     */
    @Around("processAnnotation()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object key = SimpleKeyGenerator.generateKey(args);
        String name = ClassUtils.getUserClass(joinPoint.getTarget().getClass()).getSimpleName() + "."
                + joinPoint.getSignature().getName();
        Cache<Object, Object> cache = CacheStore.getCache(name);
        // return cached mono
        return CacheMono.lookup(k -> Mono.justOrEmpty(cache.getIfPresent(key)).map(Signal::next), key)
                .onCacheMissResume((Mono<Object>) joinPoint.proceed(args))
                .andWriteWith((k, sig) -> Mono.fromRunnable(() -> {
                    if (sig != null && sig.get() != null) {
                        if (!(sig.get() instanceof Optional
                                && ((Optional<?>) Objects.requireNonNull(sig.get())).isEmpty())) {
                            cache.put(k, sig.get());
                        }
                    }
                }));
    }
}
