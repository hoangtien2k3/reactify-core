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
 * The ErrorCodeConstant class defines a set of constant error codes that are
 * used throughout the application to represent specific error conditions
 * related to organization and user management. These error codes can be
 * utilized in exception handling and logging to provide more meaningful
 * messages and aid in troubleshooting.
 * </p>
 *
 * <p>
 * Each constant in this class is a string that represents a specific error
 * condition. They can be referenced in other parts of the application to ensure
 * consistent error handling and reporting.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 *
 * <pre>
 * if (user == null) {
 * 	throw new CustomException(ErrorCodeConstant.USER_NOT_IN_ORGANIZATION);
 * }
 * </pre>
 *
 * @author hoangtien2k3
 */
public class ErrorCodeConstant {

    /**
     * Constant <code>ORGANIZATION_INVALID="user_not_exist_organization"</code>
     *
     * <p>
     * This error code indicates that the user is not associated with any
     * organization. It is typically used when a user attempts to perform an action
     * that requires an organization context, but none exists.
     * </p>
     */
    public static final String ORGANIZATION_INVALID = "user_not_exist_organization";

    /**
     * Constant <code>ORGANIZATION_NOT_FOUND="not_exit_data"</code>
     *
     * <p>
     * This error code signifies that the requested organization could not be found
     * in the system. It is used in scenarios where an operation references an
     * organization that does not exist, such as fetching organization details.
     * </p>
     */
    public static final String ORGANIZATION_NOT_FOUND = "not_exit_data";

    /**
     * Constant <code>USER_NOT_IN_ORGANIZATION="user_not_exit_organization"</code>
     *
     * <p>
     * This error code is used to indicate that the user does not belong to the
     * specified organization. This is relevant in contexts where users need to be
     * part of an organization to access certain resources or perform specific
     * actions.
     * </p>
     */
    public static final String USER_NOT_IN_ORGANIZATION = "user_not_exit_organization";

    /**
     * Constant
     * <code>ORGANIZATION_LEVEL1_NOT_FOUND="organization_unit_level_1_not_exist"</code>
     *
     * <p>
     * This error code represents the condition where an expected organization unit
     * at level 1 does not exist. It is used in cases where the application expects
     * a hierarchical organization structure, and level 1 units are critical for
     * functionality.
     * </p>
     */
    public static final String ORGANIZATION_LEVEL1_NOT_FOUND = "organization_unit_level_1_not_exist";
}
