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

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * <p>
 * The {@code CustomizeRemovalListener} class implements the
 * {@link com.github.benmanes.caffeine.cache.RemovalListener} interface from
 * Caffeine, providing custom logic for handling cache removal events. This
 * listener is triggered when cache entries are removed, allowing for additional
 * actions such as invoking a method to refresh or reload the cache.
 * </p>
 *
 * <p>
 * This class logs information about cache evictions, specifically indicating
 * which method's cache entry was evicted and the reason for the eviction.
 * </p>
 *
 * @author hoangtien2k3
 */
@Slf4j
public class CustomizeRemovalListener implements RemovalListener<Object, Object> {
    private final Method method;

    /**
     * Constructs a new instance of {@code CustomizeRemovalListener}.
     *
     * @param method
     *            the Method instance that this listener will use for removal
     *            operations.
     */
    public CustomizeRemovalListener(Method method) {
        this.method = method;
    }

    /** {@inheritDoc} */
    @Override
    public void onRemoval(@Nullable Object key, @Nullable Object value, @NonNull RemovalCause removalCause) {
        if (removalCause.wasEvicted()) {
            log.info(
                    "Cache {}.{} was evicted because {}",
                    method.getDeclaringClass().getSimpleName(),
                    method.getName(),
                    removalCause);
            CacheUtils.invokeMethod(method);
        }
    }
}
