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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method for local caching to enhance performance by reducing redundant
 * computations or repeated data retrievals from external sources.
 *
 * <p>
 * This annotation enables flexible caching configurations through the following
 * properties:
 * <ul>
 * <li><strong>durationInMinute</strong>: Specifies the validity duration of the
 * cached result, in minutes.</li>
 * <li><strong>maxRecord</strong>: Defines the maximum number of records the
 * cache can store.</li>
 * <li><strong>autoCache</strong>: A flag that determines whether the caching
 * should activate automatically upon method invocation.</li>
 * </ul>
 *
 * <p>
 * This annotation is best applied to methods where caching can notably improve
 * efficiency by retaining data that does not require frequent recalculation.
 *
 * <h3>Example Usage:</h3>
 *
 * <pre>
 * &#64;LocalCache(durationInMinute = 10, maxRecord = 500, autoCache = true)
 * public MyObject fetchData(String param) {
 * 	// Implementation that fetches or computes data
 * }
 * </pre>
 *
 * <h3>Annotation Properties:</h3>
 * <dl>
 * <dt><strong>durationInMinute</strong></dt>
 * <dd>Defines the cache retention time in minutes. Default is 120 minutes.</dd>
 *
 * <dt><strong>maxRecord</strong></dt>
 * <dd>Limits the number of records that can be cached simultaneously. Default
 * is 1000 entries.</dd>
 *
 * <dt><strong>autoCache</strong></dt>
 * <dd>When set to <code>true</code>, the method result is automatically cached
 * on execution. Default is <code>false</code>.</dd>
 * </dl>
 *
 * <p>
 * This annotation is ideal for performance-critical applications aiming to
 * reduce latency and optimize resource usage through local caching.
 * </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalCache {
    int durationInMinute() default 120;

    int maxRecord() default 1000;

    boolean autoCache() default false;
}
