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
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.reactify.annotations.LocalCache;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;
import javax.annotation.PostConstruct;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * The {@code CacheStore} class is responsible for managing local caches using
 * Caffeine. It automatically initializes caches based on methods annotated with
 * {@link LocalCache}, supports retrieving caches by name, and provides
 * functionalities to clear specific or all caches.
 * <p>
 * This class also supports auto-loading caches on application startup and
 * integrates with Spring's application context lifecycle events.
 * </p>
 *
 * @author hoangtien2k3
 */
@Component
public class CacheStore implements ApplicationContextAware {

    /**
     * A static logger instance for logging messages
     */
    private static final Logger log = LoggerFactory.getLogger(CacheStore.class);

    /** Stores the caches mapped by their names. */
    private static final HashMap<String, Cache<Object, Object>> caches = new HashMap<>();

    /**
     * Stores methods annotated with {@link LocalCache} that require auto-loading.
     */
    private static final Set<Method> autoLoadMethods = new HashSet<>();

    /** The base package for scanning cache-related methods. */
    private static String reflectionPath;

    @PostConstruct
    private static void init() {
        log.info("=====> Start initializing cache <=====");
        Reflections reflections = new Reflections(reflectionPath, Scanners.MethodsAnnotated);
        Set<Method> methods =
                reflections.get(Scanners.MethodsAnnotated.with(LocalCache.class).as(Method.class));
        log.info("Found {} methods annotated with @LocalCache", methods.size());
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
        log.info("=====> Finish initializing {} cache <=====", caches.size());
    }

    /**
     * <p>
     * get cache.
     * </p>
     *
     * @param key
     *            a {@link String} object
     * @return a {@link Cache} object
     */
    public static Cache<Object, Object> getCache(String key) {
        return caches.get(key);
    }

    /**
     * <p>
     * get list cache.
     * </p>
     *
     * @return list {@link List} String
     */
    public static List<String> getCaches() {
        return new ArrayList<>(caches.keySet());
    }

    /**
     * Clears the specified cache by name.
     *
     * @param cacheName
     *            the name of the cache to clear
     * @return the number of cleared caches (0 or 1)
     */
    public static int clearCachesByName(String cacheName) {
        log.info("Clearing cache: {}", cacheName);
        return (int) caches.entrySet().stream()
                .filter(entry -> entry.getKey().equals(cacheName))
                .peek(entry -> {
                    entry.getValue().invalidateAll();
                    log.info("Cache {} cleared", entry.getKey());
                })
                .count();
    }

    /**
     * Clear all localCache for all services.
     *
     * @return count of cleared caches
     */
    public static int clearAllCaches() {
        log.info("Clearing all caches");
        int count = 0;
        for (Map.Entry<String, Cache<Object, Object>> entry : caches.entrySet()) {
            count++;
            entry.getValue().invalidateAll();
            log.info("Cleared cache: {}", entry.getKey());
        }
        return count;
    }

    /**
     * Automatically loads caches for methods annotated with {@link LocalCache} that
     * support auto-loading.
     *
     * @param event
     *            the application context refresh event
     */
    @Async
    @EventListener
    public void autoLoad(ContextRefreshedEvent event) {
        log.info("Received ContextRefreshedEvent: {}", event.getApplicationContext());
        if (!autoLoadMethods.isEmpty()) {
            log.info("=====> Start auto-loading {} caches <=====", autoLoadMethods.size());
            for (Method method : autoLoadMethods) {
                CacheUtils.invokeMethod(method);
                log.info("Auto-loaded cache for method: {}", method.getName());
            }
            log.info("=====> Finished auto-loading caches <=====");
        }
    }

    /**
     * Sets the application context and determines the base package for scanning
     * cache methods.
     *
     * @param applicationContext
     *            the application context
     * @throws BeansException
     *             if an error occurs while setting the context
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
        log.info("Set reflection path for cache scanning: {}", reflectionPath);
    }
}
