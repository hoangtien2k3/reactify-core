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
package com.reactify.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <p>
 * The {@code ApplicationContextProvider} class serves as a utility for
 * accessing the {@link ApplicationContext} from anywhere within the
 * application.
 * </p>
 * <p>
 * This class implements {@link ApplicationContextAware} to store a static
 * reference to the {@link ApplicationContext}, enabling other components to
 * retrieve Spring beans programmatically without requiring dependency
 * injection.
 * </p>
 * <p>
 * Use the {@link #getBean(Class)} method to obtain a bean instance by its type.
 * </p>
 *
 * @author hoangtien2k3
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    /**
     * The application context instance, stored as a static volatile variable to
     * ensure thread safety when accessing the context across multiple threads.
     * <p>
     * The {@code volatile} keyword guarantees visibility of updates to the variable
     * across threads, and the double-checked locking mechanism ensures that the
     * context is initialized only once.
     * </p>
     */
    private static volatile ApplicationContext context;

    /**
     * Retrieves a bean from the {@link ApplicationContext} by its type.
     * <p>
     * This method allows fetching beans from the Spring container without using
     * {@code @Autowired} or other dependency injection mechanisms.
     * </p>
     *
     * @param clazz
     *            the class type of the desired bean
     * @param <T>
     *            the generic type of the bean
     * @return an instance of the requested bean
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     *             if no bean of the specified type is found
     */
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("squid:S2696")
    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        if (context == null) {
            synchronized (ApplicationContextProvider.class) {
                if (context == null) {
                    context = ac;
                }
            }
        }
    }
}
