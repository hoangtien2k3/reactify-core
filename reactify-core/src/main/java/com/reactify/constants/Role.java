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
package com.reactify.constants;

/**
 * <p>
 * Enumeration representing different user roles within the application.
 * </p>
 *
 * <p>
 * Each role is associated with specific permissions and access levels within
 * the application.
 * </p>
 *
 * <ul>
 * <li>{@link #ROLE_ADMIN} - Administrator role, typically with the highest
 * access level, responsible for managing users and system settings.</li>
 * <li>{@link #ROLE_USER} - Standard user role, with limited access to
 * application features based on user-specific permissions.</li>
 * <li>{@link #ROLE_SYSTEM} - System role, usually used for system processes or
 * internal tasks.</li>
 * </ul>
 *
 * <p>
 * Example usage:
 * </p>
 *
 * <pre>
 * {@code
 * Role userRole = Role.ROLE_USER;
 * if (userRole == Role.ROLE_ADMIN) {
 * 	// Perform admin-specific tasks
 * }
 * }
 * </pre>
 *
 * @author hoangtien2k3
 */
public enum Role {
    /** Administrator role with high-level access permissions */
    ROLE_ADMIN,

    /** Standard user role with limited permissions */
    ROLE_USER,

    /** System role intended for internal or system-level tasks */
    ROLE_SYSTEM
}
