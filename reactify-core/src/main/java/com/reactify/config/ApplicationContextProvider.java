/*
 * Copyright 2024-2025 the original author Hoàng Anh Tiến
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
package com.reactify.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <p>
 * The {@code ApplicationContextProvider} class provides a static method to
 * access the Spring {@link org.springframework.context.ApplicationContext}. It
 * implements the {@link org.springframework.context.ApplicationContextAware}
 * interface to receive the application context during initialization.
 * </p>
 *
 * <p>
 * This class can be used to retrieve beans from the application context
 * statically without needing to inject them directly. This is particularly
 * useful in scenarios where dependency injection is not available.
 * </p>
 *
 * <p>
 * The application context is set during the initialization phase when the
 * Spring container creates the beans, allowing it to be accessed throughout the
 * application.
 * </p>
 *
 * @author hoangtien2k3
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;

    /**
     * <p>
     * getApplicationContext.
     * </p>
     *
     * @return a {@link org.springframework.context.ApplicationContext} object
     */
    public static ApplicationContext getApplicationContext() {
        return context;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("squid:S2696")
    @Override
    public void setApplicationContext(@NotNull ApplicationContext ac) throws BeansException {
        context = ac;
    }
}
