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
package com.reactify.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for enabling local caching on method results to enhance
 * performance by reducing redundant computations and minimizing repeated data
 * retrievals from external sources such as databases or APIs.
 *
 * <p>
 * This annotation provides a flexible caching mechanism with configurable
 * properties:
 * <ul>
 * <li><strong>durationInMinute</strong>: Defines the lifespan of cached entries
 * in minutes.</li>
 * <li><strong>maxRecord</strong>: Specifies the maximum number of entries that
 * can be stored in the cache.</li>
 * <li><strong>autoCache</strong>: Determines whether caching should be
 * automatically applied when the method is invoked.</li>
 * </ul>
 * </p>
 *
 * <h3>Key Benefits:</h3>
 * <ul>
 * <li>Reduces redundant computations by caching method results.</li>
 * <li>Minimizes database or API calls, improving response times.</li>
 * <li>Optimizes resource usage in performance-critical applications.</li>
 * </ul>
 *
 * <h3>Usage Example:</h3>
 *
 * <pre>
 * {@code
 * @LocalCache(durationInMinute = 15, maxRecord = 200, autoCache = true)
 * public List<User> fetchActiveUsers() {
 * 	// Retrieves a list of active users from the database
 * }
 * }
 * </pre>
 *
 * <h3>Annotation Properties:</h3>
 * <dl>
 * <dt><strong>durationInMinute</strong></dt>
 * <dd>Specifies how long (in minutes) the cache entry remains valid. Default is
 * 120 minutes.</dd>
 *
 * <dt><strong>maxRecord</strong></dt>
 * <dd>Limits the number of records stored in the cache at any given time.
 * Default is 1000 entries.</dd>
 *
 * <dt><strong>autoCache</strong></dt>
 * <dd>If set to <code>true</code>, caching is applied automatically whenever
 * the method is executed. Default is <code>false</code>.</dd>
 * </dl>
 *
 * <h3>Best Practices:</h3>
 * <ul>
 * <li>Use on methods that return frequently accessed and computationally
 * expensive results.</li>
 * <li>Avoid applying to methods with frequently changing data to prevent stale
 * cache issues.</li>
 * <li>Adjust `durationInMinute` and `maxRecord` according to system load and
 * data update frequency.</li>
 * </ul>
 *
 * <p>
 * This annotation is particularly useful in microservices and high-performance
 * applications where minimizing latency and optimizing resource utilization are
 * crucial.
 * </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalCache {

    /**
     * Defines the duration (in minutes) for which a cached entry remains valid.
     * After this time, the entry will expire and be removed from the cache.
     *
     * @return cache duration in minutes (default: 120)
     */
    int durationInMinute() default 120;

    /**
     * Specifies the maximum number of records that can be stored in the cache. Once
     * this limit is reached, older entries may be evicted based on cache policies.
     *
     * @return maximum cache size (default: 1000)
     */
    int maxRecord() default 1000;

    /**
     * Indicates whether caching should be automatically applied when the method is
     * invoked. If enabled, the method execution result will be stored in the cache
     * for subsequent calls.
     *
     * @return <code>true</code> to enable automatic caching, <code>false</code>
     *         otherwise (default: false)
     */
    boolean autoCache() default false;
}
