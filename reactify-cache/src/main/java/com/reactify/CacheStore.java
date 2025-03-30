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
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
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

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;

/**
 * <p>
 * CacheStore class.
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

    private static final HashMap<String, Cache<Object, Object>> caches = new HashMap<>();
    private static final Set<Method> autoLoadMethods = new HashSet<>();
    private static String reflectionPath;

    @PostConstruct
    private static void init() {
        log.info("=====> Start initializing cache <=====");
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
     * Clear localCache by serviceName
     *
     * @param serviceName
     *            String
     * @return count of cleared caches
     */
    public static int clearCachesInServiceName(String serviceName) {
        return (int) caches.entrySet().stream()
                .filter(entry -> entry.getKey().contains(serviceName))
                .peek(entry -> entry.getValue().invalidateAll())
                .count();
    }

    /**
     * clear localCache by cacheName
     *
     * @param cacheName
     *            String
     * @return count of cleared caches
     */
    public static int clearCachesByName(String cacheName) {
        return (int) caches.entrySet().stream()
                .filter(entry -> entry.getKey().equals(cacheName))
                .peek(entry -> entry.getValue().invalidateAll())
                .count();
    }

    /**
     * Clear all localCache for all services.
     *
     * @return count of cleared caches
     */
    public static int clearAllCaches() {
        log.info("Before clearing, cache size: {}", caches.size());
        int count = 0;
        for (Map.Entry<String, Cache<Object, Object>> entry : caches.entrySet()) {
            count ++;
            entry.getValue().invalidateAll();
        }
        log.info("After clearing, cleared {} caches", count);
        return count;
    }

    /**
     * <p>
     * autoLoad.
     * </p>
     *
     * @param event
     *            a {@link ContextRefreshedEvent} object
     */
    @Async
    @EventListener
    public void autoLoad(ContextRefreshedEvent event) {
        if (!autoLoadMethods.isEmpty()) {
            log.info("=====> Start auto load {} cache <=====", autoLoadMethods.size());
            for (Method method : autoLoadMethods) {
                CacheUtils.invokeMethod(method);
            }
            log.info("=====> Finish auto load cache <=====");
        }
    }

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
