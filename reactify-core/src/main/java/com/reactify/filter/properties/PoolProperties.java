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
package com.reactify.filter.properties;

/**
 * <p>
 * The PoolProperties class is a record that holds configuration properties for
 * a connection pool. It specifies the maximum number of connections in the pool
 * and the maximum number of pending requests to acquire a connection from the
 * pool.
 * </p>
 *
 * <p>
 * The default constructor initializes the pool with a maximum size of 2000
 * connections and allows 2000 pending requests to acquire a connection.
 * </p>
 *
 * @param maxSize
 *            the maximum number of connections in the pool
 * @param maxPendingAcquire
 *            the maximum number of pending requests to acquire a connection
 * @author hoangtien2k3
 */
public record PoolProperties(Integer maxSize, Integer maxPendingAcquire) {
    /**
     * <p>
     * Constructor for PoolProperties.
     * </p>
     */
    public PoolProperties() {
        this(2000, 2000);
    }
}
