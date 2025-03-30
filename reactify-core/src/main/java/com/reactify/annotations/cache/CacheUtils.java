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

import com.reactify.annotations.LocalCache;
import com.reactify.config.ApplicationContextProvider;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Utility class for cache management.
 * <p>
 * This class provides methods to dynamically invoke cached methods at runtime,
 * supporting autoloading and execution in a reactive manner using Project
 * Reactor.
 * </p>
 *
 * <p>
 * It is particularly useful for managing cache entries annotated with
 * {@link LocalCache}.
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
            Object beanInstance = ApplicationContextProvider.getBean(declaringClass);
            Object result = method.invoke(beanInstance);
            String methodName = declaringClass.getSimpleName() + "." + method.getName();

            if (result instanceof Mono<?> monoResult) {
                monoResult.subscribe(
                        success -> log.debug("Successfully executed {}", methodName),
                        error -> log.error("Error executing {}", methodName, error));
            } else {
                log.warn("Method {} does not return a Mono<?>", methodName);
            }
        } catch (IllegalAccessException e) {
            log.error("Access violation when invoking method {}: {}", method.getName(), e.getMessage(), e);
        } catch (Exception e) {
            log.error(
                    "Error when autoload cache {}.{}.{}",
                    method.getDeclaringClass().getSimpleName(),
                    method.getName(),
                    e.getMessage(),
                    e);
        }
    }
}
