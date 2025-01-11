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
package com.reactify;

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
 * CacheAspect class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Aspect
@Configuration
@Slf4j
public class CacheAspect {

    @Pointcut("@annotation(com.reactify.LocalCache)")
    private void processAnnotation() {}

    /**
     * <p>
     * aroundAdvice.
     * </p>
     *
     * @param joinPoint
     *            a {@link ProceedingJoinPoint} object
     * @return a {@link Object} object
     * @throws Throwable
     *             if any.
     */
    @Around("processAnnotation()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object key = SimpleKeyGenerator.generateKey(args);
        String name = ClassUtils.getUserClass(joinPoint.getTarget().getClass()).getSimpleName() + "."
                + joinPoint.getSignature().getName();
        Cache<Object, Object> cache = CacheStore.getCache(name);
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
