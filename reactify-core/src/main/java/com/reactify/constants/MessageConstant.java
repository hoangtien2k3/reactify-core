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
}
