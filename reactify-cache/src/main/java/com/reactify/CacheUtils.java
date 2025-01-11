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

import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * <p>
 * The CacheUtils class provides utility methods for working with caching
 * functionality in the application. It contains methods to invoke cached
 * methods dynamically at runtime, allowing for efficient cache management and
 * retrieval.
 * </p>
 *
 * <p>
 * This class is particularly useful for autoloading cache entries based on the
 * methods annotated with {@link LocalCache} and managing their execution in a
 * reactive manner using Project Reactor.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CacheUtils {

    /**
     * <p>
     * Invokes the specified method and subscribes to its result, enabling the
     * execution of the method in a reactive context. This method retrieves the bean
     * instance of the declaring class from the Spring application context and calls
     * the specified method.
     * </p>
     *
     * @param method
     *            a {@link Method} object representing the method to be invoked
     */
    public static void invokeMethod(Method method) {
        try {
            Class<?> declaringClass = method.getDeclaringClass();
            Object t = ApplicationContextProvider.getApplicationContext().getBean(declaringClass);
            Mono<Object> rs = (Mono<Object>) method.invoke(t);
            rs.subscribe();
        } catch (Exception exception) {
            log.error(
                    "Error when autoload cache {}.{}.{}",
                    method.getDeclaringClass().getSimpleName(),
                    method.getName(),
                    exception.getMessage(),
                    exception);
        }
    }
}
