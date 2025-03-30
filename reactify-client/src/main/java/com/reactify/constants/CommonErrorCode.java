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
 * The {@code CommonErrorCode} class holds a collection of constant error codes
 * that are used throughout the application to standardize error handling.
 *
 * <p>
 * This class defines the following error codes:
 * </p>
 *
 * <p>
 * Usage example:
 * </p>
 *
 * <pre>
 * throw new CustomException(CommonErrorCode.BAD_REQUEST);
 * </pre>
 *
 * <p>
 * Note: This class is intended to be used as a holder for error codes and
 * should not be instantiated.
 * </p>
 *
 * @author hoangtien2k3
 */
public class CommonErrorCode {

    /** Indicates that the request was invalid or malformed. */
    public static final String BAD_REQUEST = "bad_request";

    /** Indicates that the requested resource was not found. */
    public static final String NOT_FOUND = "not_found";

    /** Indicates that the parameters provided in the request are invalid. */
    public static final String INVALID_PARAMS = "invalid_params";

    /** Indicates that the data already exists in the system. */
    public static final String EXIST_DATA = "exist_data";

    /** Indicates that the data does not exist in the system. */
    public static final String NOT_EXIST_DATA = "not_exist_data";

    /** Indicates that an unexpected internal server error has occurred. */
    public static final String INTERNAL_SERVER_ERROR = "internal_error";

    /**
     * Indicates that the user is not authorized to perform the requested action.
     */
    public static final String UN_AUTHORIZATION = "un_auth";

    /**
     * Indicates that the user does not have permission to access the requested
     * resource.
     */
    public static final String NO_PERMISSION = "no_permission";

    /** Indicates that access to the requested resource is denied. */
    public static final String ACCESS_DENIED = "access_denied";

    /** Indicates an error occurred while parsing the authentication token. */
    public static final String PARSE_TOKEN_ERROR = "parse_token_failed";

    /**
     * Indicates that an SQL error occurred while processing a database operation.
     * group information could not be found.
     */
    public static final String SQL_ERROR = "sql";

    /** Indicates a successful operation. */
    public static final String SUCCESS = "success";

    /** Indicates an error occurred during deserialization. */
    public static final String UN_DESERIALIZE = "un_deserialize";

    /** Indicates an error occurred while hashing a password. */
    public static final String HASHING_PASSWORD_FAULT = "hashing_password_fault";

    /** Indicates an error occurred when calling a SOAP service. */
    public static final String CALL_SOAP_ERROR = "Exception when call soap: ";
}
