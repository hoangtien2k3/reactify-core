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
 * <ul>
 * <li>{@link #BAD_REQUEST} - Indicates that the request was invalid or
 * malformed.</li>
 * <li>{@link #NOT_FOUND} - Indicates that the requested resource was not
 * found.</li>
 * <li>{@link #INVALID_PARAMS} - Indicates that the parameters provided in the
 * request are invalid.</li>
 * <li>{@link #EXIST_DATA} - Indicates that the data already exists in the
 * system.</li>
 * <li>{@link #NOT_EXIST_DATA} - Indicates that the data does not exist in the
 * system.</li>
 * <li>{@link #INTERNAL_SERVER_ERROR} - Indicates that an unexpected internal
 * server error has occurred.</li>
 * <li>{@link #UN_AUTHORIZATION} - Indicates that the user is not authorized to
 * perform the requested action.</li>
 * <li>{@link #NO_PERMISSION} - Indicates that the user does not have permission
 * to access the requested resource.</li>
 * <li>{@link #ACCESS_DENIED} - Indicates that access to the requested resource
 * is denied.</li>
 * <li>{@link #PARSE_TOKEN_ERROR} - Indicates an error occurred while parsing
 * the authentication token.</li>
 * <li>{@link #SQL_ERROR} - Indicates that an SQL error occurred while
 * processing a database operation.</li>
 * <li>{@link #TRUST_MST_01} - Specific error code related to trust management
 * (MST 01).</li>
 * <li>{@link #TRUST_MST_02} - Specific error code related to trust management
 * (MST 02).</li>
 * <li>{@link #COMPANY_NOT_FOUND_TRUST_INDENTITY} - Indicates that the company
 * could not be found in the trust identity records.</li>
 * <li>{@link #GROUP_INFO_NOT_FOUND} - Indicates that the group information
 * could not be found.</li>
 * <li>{@link #GROUP_INFO_NOT_FOUND_2} - Indicates that a second instance of
 * group information could not be found.</li>
 * <li>{@link #SUCCESS} - Indicates a successful operation.</li>
 * <li>{@link #UN_DESERIALIZE} - Indicates an error occurred during
 * deserialization.</li>
 * <li>{@link #HASHING_PASSWORD_FAULT} - Indicates an error occurred while
 * hashing a password.</li>
 * <li>{@link #CALL_SOAP_ERROR} - Indicates an error occurred when calling a
 * SOAP service.</li>
 * </ul>
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

    /**
     * Constructs a new instance of {@code CommonErrorCode}.
     */
    public CommonErrorCode() {}

    /** Constant <code>BAD_REQUEST="bad_request"</code> */
    public static final String BAD_REQUEST = "bad_request";
    /** Constant <code>NOT_FOUND="not_found"</code> */
    public static final String NOT_FOUND = "not_found";
    /** Constant <code>INVALID_PARAMS="invalid_params"</code> */
    public static final String INVALID_PARAMS = "invalid_params";
    /** Constant <code>EXIST_DATA="exist_data"</code> */
    public static final String EXIST_DATA = "exist_data";
    /** Constant <code>NOT_EXIST_DATA="not_exist_data"</code> */
    public static final String NOT_EXIST_DATA = "not_exist_data";
    /** Constant <code>INTERNAL_SERVER_ERROR="internal_error"</code> */
    public static final String INTERNAL_SERVER_ERROR = "internal_error";
    /** Constant <code>UN_AUTHORIZATION="un_auth"</code> */
    public static final String UN_AUTHORIZATION = "un_auth";
    /** Constant <code>NO_PERMISSION="no_permission"</code> */
    public static final String NO_PERMISSION = "no_permission";
    /** Constant <code>ACCESS_DENIED="access_denied"</code> */
    public static final String ACCESS_DENIED = "access_denied";
    /** Constant <code>PARSE_TOKEN_ERROR="parse_token_failed"</code> */
    public static final String PARSE_TOKEN_ERROR = "parse_token_failed";
    /** Constant <code>SQL_ERROR="sql"</code> */
    public static final String SQL_ERROR = "sql";
    /** Constant <code>TRUST_MST_01="trust_mst_01"</code> */
    public static final String TRUST_MST_01 = "trust_mst_01";
    /** Constant <code>TRUST_MST_02="trust_mst_02"</code> */
    public static final String TRUST_MST_02 = "trust_mst_02";
    /**
     * Constant
     * <code>COMPANY_NOT_FOUND_TRUST_INDENTITY="company_not_found_trust_indentity"</code>
     */
    public static final String COMPANY_NOT_FOUND_TRUST_INDENTITY = "company_not_found_trust_indentity";
    /** Constant <code>GROUP_INFO_NOT_FOUND="group_info_not_found"</code> */
    public static final String GROUP_INFO_NOT_FOUND = "group_info_not_found";
    /** Constant <code>GROUP_INFO_NOT_FOUND_2="group_info_not_found_2"</code> */
    public static final String GROUP_INFO_NOT_FOUND_2 = "group_info_not_found_2";
    /** Constant <code>SUCCESS="success"</code> */
    public static final String SUCCESS = "success";
    /** Constant <code>UN_DESERIALIZE="un_deserialize"</code> */
    public static final String UN_DESERIALIZE = "un_deserialize";
    /** Constant <code>HASHING_PASSWORD_FAULT="hashing_password_fault"</code> */
    public static final String HASHING_PASSWORD_FAULT = "hashing_password_fault";
    /** Constant <code>CALL_SOAP_ERROR="Exception when call soap: "</code> */
    public static final String CALL_SOAP_ERROR = "Exception when call soap: ";
}
