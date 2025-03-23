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
package com.reactify.annotations.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.reactify.annotations.LocalCache;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * <p>
 * The {@code CacheStore} class is responsible for managing and initializing
 * caches for methods annotated with
 * {@link com.reactify.annotations.LocalCache}. It uses the Caffeine caching
 * library to provide an efficient caching mechanism, supporting features like
 * auto-loading of cache entries based on method annotations.
 * </p>
 *
 * <p>
 * This class implements
 * {@link org.springframework.context.ApplicationContextAware} to gain access to
 * the Spring application context, allowing it to dynamically discover and
 * initialize caches at startup. It automatically loads caches for methods that
 * have the {@code @LocalCache} annotation and meet the specified criteria.
 * </p>
 *
 * <p>
 * The cache initialization process occurs during the post-construct phase,
 * where it scans for methods with the {@code @LocalCache} annotation and
 * configures the caches according to the parameters specified in the
 * annotation.
 * </p>
 *
 * <p>
 * The class also listens for context refresh events to trigger the auto-loading
 * of caches, ensuring that the caches are populated with initial data as
 * needed.
 * </p>
 *
 * <p>
 * The caches are stored in a static {@link java.util.HashMap} for easy access
 * based on the method name and class.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
@Component
public class CacheStore implements ApplicationContextAware {

    private static final HashMap<String, Cache<Object, Object>> caches = new HashMap<>();
    private static final Set<Method> autoLoadMethods = new HashSet<>();
    private static String reflectionPath;

    /**
     * <p>
     * Initializes the cache store by scanning for methods annotated with
     * {@link LocalCache} and creating caches according to the annotation's
     * parameters. This method is called after the bean's construction.
     * </p>
     */
    @PostConstruct
    private static void init() {
        log.info("Start initializing cache");
        Reflections reflections = new Reflections(reflectionPath, Scanners.MethodsAnnotated);
        Set<Method> methods =
                reflections.get(Scanners.MethodsAnnotated.with(LocalCache.class).as(Method.class));
        for (Method method : methods) {
            String className = method.getDeclaringClass().getSimpleName();
            LocalCache localCache = method.getAnnotation(LocalCache.class);
            int maxRecord = localCache.maxRecord();
            int durationInMinute = localCache.durationInMinute();
            String cacheName = className + "." + method.getName();
            boolean autoLoad = localCache.autoCache();
            Cache<Object, Object> cache;
            if (autoLoad && (method.getParameterCount() == 0)) {
                cache = Caffeine.newBuilder()
                        .scheduler(Scheduler.systemScheduler())
                        .expireAfterWrite(Duration.ofMinutes(durationInMinute))
                        .recordStats()
                        .maximumSize(maxRecord)
                        .removalListener(new CustomizeRemovalListener(method))
                        .build();
                autoLoadMethods.add(method);
            } else {
                cache = Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofMinutes(durationInMinute))
                        .recordStats()
                        .maximumSize(maxRecord)
                        .build();
            }
            caches.put(cacheName, cache);
        }
        log.info("Finish initializing {} cache", caches.size());
    }

    /**
     * <p>
     * Retrieves a cache by its name.
     * </p>
     *
     * @param key
     *            a {@link java.lang.String} object representing the cache name.
     * @return a {@link com.github.benmanes.caffeine.cache.Cache} object
     *         corresponding to the specified name, or {@code null} if no cache
     *         exists for the given key.
     */
    public static Cache<Object, Object> getCache(String key) {
        return caches.get(key);
    }

    /**
     * <p>
     * Automatically loads cache entries for methods that are configured to
     * auto-load. This method is triggered by the
     * {@link org.springframework.context.event.ContextRefreshedEvent}.
     * </p>
     *
     * @param event
     *            a {@link org.springframework.context.event.ContextRefreshedEvent}
     *            object indicating the application context has been refreshed.
     */
    @Async
    @EventListener
    public void autoLoad(ContextRefreshedEvent event) {
        if (!autoLoadMethods.isEmpty()) {
            log.info("Start auto load {} cache", autoLoadMethods.size());
            for (Method method : autoLoadMethods) {
                CacheUtils.invokeMethod(method);
            }
            log.info("Finish auto load cache");
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Sets the application context for this component, allowing it to access beans
     * and application context resources. This method is called by Spring during the
     * bean lifecycle.
     * </p>
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Class<?> mainApplicationClass = applicationContext
                .getBeansWithAnnotation(SpringBootApplication.class)
                .values()
                .iterator()
                .next()
                .getClass();
        reflectionPath = mainApplicationClass.getPackageName();
    }
}
