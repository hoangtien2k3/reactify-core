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
 * The MessageConstant class defines a set of constant message strings that are
 * used throughout the application to represent various states and error
 * conditions. These message constants provide a centralized location for
 * managing message strings, ensuring consistency in messaging across the
 * application.
 * </p>
 *
 * <p>
 * Each constant in this class represents a specific message or status indicator
 * that can be referenced throughout the application. They are particularly
 * useful in logging, user feedback, and error handling to provide meaningful
 * information to users and developers.
 * </p>
 *
 * Example usage:
 *
 * <pre>
 * if (operationSuccessful) {
 * 	return MessageConstant.SUCCESS;
 * } else {
 * 	return MessageConstant.FAIL;
 * }
 * </pre>
 *
 * @author hoangtien2k3
 */
public class MessageConstant {

    /**
     * Constant <code>SUCCESS="success"</code>
     *
     * <p>
     * This message indicates that an operation has completed successfully. It can
     * be used in response messages to inform users of the successful completion of
     * a task.
     * </p>
     */
    public static final String SUCCESS = "success";

    /**
     * Constant <code>FAIL="fail"</code>
     *
     * <p>
     * This message indicates that an operation has failed. It can be used in
     * response messages to inform users that the intended action was not completed
     * successfully.
     * </p>
     */
    public static final String FAIL = "fail";

    /**
     * Constant <code>ERROR_CODE_SUCCESS="0"</code>
     *
     * <p>
     * This constant represents a success code for error handling. It is typically
     * used to indicate that a process has completed without any errors.
     * </p>
     */
    public static final String ERROR_CODE_SUCCESS = "0";

    /**
     * Constant <code>PARAMS_INVALID="params.invalid"</code>
     *
     * <p>
     * This message indicates that the parameters provided for an operation are
     * invalid. It can be used for validation checks to inform users when the input
     * parameters do not meet the required criteria.
     * </p>
     */
    public static final String PARAMS_INVALID = "params.invalid";

    /**
     * Constant <code>QUERY_CART_ITEM_NOT_FOUND="query.cartItem.not.found"</code>
     *
     * <p>
     * This message indicates that a requested cart item could not be found. It is
     * used when querying for items in a shopping cart and none exist matching the
     * query criteria.
     * </p>
     */
    public static final String QUERY_CART_ITEM_NOT_FOUND = "query.cartItem.not.found";

    /**
     * Constant <code>ORGANIZATION_INVALID="organization.invalid"</code>
     *
     * <p>
     * This message indicates that the organization being referenced is invalid. It
     * can be used in contexts where operations require a valid organization
     * reference.
     * </p>
     */
    public static final String ORGANIZATION_INVALID = "organization.invalid";

    /**
     * Constant <code>ORGANIZATION_NOT_FOUND="organization.notFound"</code>
     *
     * <p>
     * This message indicates that a requested organization could not be found. It
     * is used in scenarios where operations depend on the existence of an
     * organization.
     * </p>
     */
    public static final String ORGANIZATION_NOT_FOUND = "organization.notFound";

    /**
     * Constant
     * <code>USER_NOT_IN_ORGANIZATION="user.notFound.in.organization"</code>
     *
     * <p>
     * This message indicates that a user could not be found within a specified
     * organization. It is relevant when checking user memberships in organizations.
     * </p>
     */
    public static final String USER_NOT_IN_ORGANIZATION = "user.notFound.in.organization";

    /**
     * Constant
     * <code>ORGANIZATION_LEVEL1_NOT_FOUND="organization.level1.notFound"</code>
     *
     * <p>
     * This message indicates that an expected organization level 1 entity could not
     * be found. It is used in hierarchical organization structures where level 1
     * units are essential.
     * </p>
     */
    public static final String ORGANIZATION_LEVEL1_NOT_FOUND = "organization.level1.notFound";

    /**
     * Constant <code>POSITION_NOT_EXISTS="postion.not.exists"</code>
     *
     * <p>
     * This message indicates that the specified position does not exist. It is used
     * in scenarios where users attempt to reference or manipulate positions in the
     * organization.
     * </p>
     */
    public static final String POSITION_NOT_EXISTS = "postion.not.exists";

    /**
     * Constant <code>USER_NAME_NOT_EXISTS="user.name.not.exists"</code>
     *
     * <p>
     * This message indicates that the specified user name does not exist in the
     * system. It can be used for user validation and existence checks.
     * </p>
     */
    public static final String USER_NAME_NOT_EXISTS = "user.name.not.exists";

    /**
     * Constant <code>CODE_NOT_EXISTS="employee.code.not.exists"</code>
     *
     * <p>
     * This message indicates that the specified employee code does not exist. It is
     * relevant for operations that require validation of employee identities.
     * </p>
     */
    public static final String CODE_NOT_EXISTS = "employee.code.not.exists";

    /**
     * Constant <code>ORGANIZATION_NOT_EXISTS="organization.not.exists"</code>
     *
     * <p>
     * This message indicates that the specified organization does not exist. It is
     * used in situations where an organization must be validated before performing
     * certain operations.
     * </p>
     */
    public static final String ORGANIZATION_NOT_EXISTS = "organization.not.exists";

    /**
     * Constant <code>MANAGER_NOT_EXISTS="manager.not.exists"</code>
     *
     * <p>
     * This message indicates that the specified manager does not exist. It can be
     * used in validation scenarios where manager information is required for
     * operations.
     * </p>
     */
    public static final String MANAGER_NOT_EXISTS = "manager.not.exists";

    /**
     * Constant
     * <code>EMPLOYEE_ORGANIZATION_IS_EXISTS="employee.organization.is.exists"</code>
     *
     * <p>
     * This message indicates that an employee is already associated with an
     * organization. It can be used in situations where an attempt to add an
     * employee to an organization is made while they are already linked.
     * </p>
     */
    public static final String EMPLOYEE_ORGANIZATION_IS_EXISTS = "employee.organization.is.exists";

    /**
     * Constant
     * <code>EMPLOYEE_ORGANIZATION_NOT_EXISTS="organization.employee.is.exists"</code>
     *
     * <p>
     * This message indicates that there is an organization that does not have any
     * associated employees. It can be used for validation and reporting in contexts
     * where employee-organization relationships are important.
     * </p>
     */
    public static final String EMPLOYEE_ORGANIZATION_NOT_EXISTS = "organization.employee.is.exists";
}
