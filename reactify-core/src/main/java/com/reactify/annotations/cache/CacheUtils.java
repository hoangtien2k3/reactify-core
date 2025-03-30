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

import com.reactify.config.ApplicationContextProvider;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * <p>
 * The {@code CacheUtils} class provides utility methods for invoking methods
 * that are used for cache management, particularly for methods annotated with
 * {@link com.reactify.annotations.LocalCache}. This class is designed to
 * facilitate the auto-loading of cache entries by invoking the specified
 * methods in the appropriate application context.
 * </p>
 *
 * <p>
 * This class is annotated with {@link org.springframework.stereotype.Component}
 * to indicate that it is a Spring-managed bean, and it uses constructor
 * injection to obtain any required dependencies.
 * </p>
 *
 * @author hoangtien2k3
 */
@Component
public class CacheUtils {

    /**
     * A static logger instance for logging messages
     */
    private static final Logger log = LoggerFactory.getLogger(CacheUtils.class);

    /**
     * <p>
     * Invokes a specified method and subscribes to the returned
     * {@link reactor.core.publisher.Mono} object to trigger any asynchronous cache
     * loading operations. This method retrieves the bean of the declaring class
     * from the application context and executes the method, logging any exceptions
     * that occur during invocation.
     * </p>
     *
     * @param method
     *            a {@link java.lang.reflect.Method} object representing the method
     *            to be invoked for cache loading.
     */
    public static void invokeMethod(Method method) {
        try {
            Class<?> declaringClass = method.getDeclaringClass();
            Object t = ApplicationContextProvider.getApplicationContext().getBean(declaringClass);
            @SuppressWarnings("unchecked")
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
