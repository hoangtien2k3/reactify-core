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
package com.reactify.annotations.cache.request;

/**
 * <p>
 * Request object for clearing cache in the application.
 * </p>
 *
 * <p>
 * This class is used to specify the cache clearing operation, allowing users to
 * delete cache at different levels based on the provided type.
 * </p>
 *
 * <ul>
 * <li><b>SERVICE_LEVEL</b>: Clears all cached entries related to a specific
 * service.</li>
 * <li><b>METHOD_LEVEL</b>: Clears a specific cache by its unique name (absolute
 * method path).</li>
 * <li><b>ALL_LEVEL</b>: Clears all caches across all services in the
 * system.</li>
 * </ul>
 *
 * <p>
 * The `nameCache` field is required when using `SERVICE_LEVEL` or
 * `METHOD_LEVEL` type.
 * </p>
 *
 * @author hoangtien2k3
 */
public class LocalCacheRequest {

    /**
     * The type of cache clearing operation.
     * <p>
     * Accepted values:
     * <ul>
     * <li><b>SERVICE_LEVEL</b> - Clear cache for a specific service.</li>
     * <li><b>METHOD_LEVEL</b> - Clear a specific cache by its absolute method
     * path.</li>
     * <li><b>ALL_LEVEL</b> - Clear all caches in the system.</li>
     * </ul>
     * </p>
     */
    private String type;

    /**
     * The name of the cache to be cleared.
     * <p>
     * This field is required when `type` is either:
     * <ul>
     * <li><b>SERVICE_LEVEL</b> - Specifies which service’s cache to clear.</li>
     * <li><b>METHOD_LEVEL</b> - Specifies the exact cache entry to remove.</li>
     * </ul>
     * For `ALL_LEVEL`, this field can be null.
     * </p>
     */
    private String nameCache;

    public String getType() {
        return type;
    }

    public String getNameCache() {
        return nameCache;
    }
}
