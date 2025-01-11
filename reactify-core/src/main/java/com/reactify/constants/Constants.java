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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.slf4j.MDC;
import org.springframework.http.MediaType;

/**
 * <p>
 * Constants class contains a collection of constant values used across the
 * application. These constants include patterns for validation, media types,
 * logging titles, and other fixed values.
 * </p>
 *
 * @author hoangtien2k3
 */
public final class Constants {

    /**
     * Constructs a new instance of {@code Constants}.
     */
    public Constants() {}

    /**
     * Regular expression pattern for validating names.
     * <p>
     * The pattern allows uppercase and lowercase letters, as well as Vietnamese
     * characters, and whitespace.
     * </p>
     */
    public static final String NAME_PATTERN =
            "^[a-zA-ZàáãạảăắằẳẵặâấầẩẫậèéẹẻẽêềếểễệđìíĩỉịòóõọỏôốồổỗộơớờởỡợùúũụủưứừửữựỳỵỷỹýÀÁÃẠẢĂẮẰẲẴẶÂẤẦẨẪẬÈÉẸẺẼÊỀẾỂỄỆĐÌÍĨỈỊÒÓÕỌỎÔỐỒỔỖỘƠỚỜỞỠỢÙÚŨỤỦƯỨỪỬỮỰỲỴỶỸÝ\\s]+$";

    /**
     * Regular expression pattern for validating email addresses.
     * <p>
     * The pattern matches common email formats.
     * </p>
     */
    public static final String EMAIL_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    /** Constant <code>DATE_PATTERN="\\d{2}[/]\\d{2}[/]\\d{4}"</code> */
    public static final String DATE_PATTERN = "\\d{2}[/]\\d{2}[/]\\d{4}";
    /** Constant <code>ID_NO_PATTERN="^[0-9\\-]+$"</code> */
    public static final String ID_NO_PATTERN = "^[0-9\\-]+$";
    /** Constant <code>NUMBER_PATTERN="^[0-9]+$"</code> */
    public static final String NUMBER_PATTERN = "^[0-9]+$";
    /** Constant <code>USERNAME_PATTERN="^[A-Za-z0-9_-]+$"</code> */
    public static final String USERNAME_PATTERN = "^[A-Za-z0-9_-]+$";
    /** Constant <code>IMAGE_EXTENSION_LIST</code> */
    public static final List<String> IMAGE_EXTENSION_LIST =
            Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "tiff", "tif", "svg", "raw", "psd", "ai", "eps");
    /** Constant <code>MAX_FILE_SIZE_MB=3</code> */
    public static final int MAX_FILE_SIZE_MB = 3;
    /** Constant <code>EMPLOYEE_CODE_LENGTH=6</code> */
    public static final int EMPLOYEE_CODE_LENGTH = 6;

    /** Constant <code>EMPLOYEE_CODE_MIN="000001"</code> */
    public static final String EMPLOYEE_CODE_MIN = "000001";

    /** List of visible media types for API responses. */
    public static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
            MediaType.TEXT_XML,
            MediaType.APPLICATION_XML,
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_JSON_UTF8,
            MediaType.TEXT_PLAIN,
            MediaType.TEXT_XML,
            MediaType.MULTIPART_FORM_DATA);

    private static final List<String> SENSITIVE_HEADERS = Arrays.asList("authorization", "proxy-authorization");

    /**
     * Retrieves a list of sensitive headers.
     *
     * @return a {@link java.util.List} of sensitive header names.
     */
    public static List<String> getSensitiveHeaders() {
        return SENSITIVE_HEADERS;
    }

    /**
     * Contains constants related to SOAP headers.
     */
    public interface SoapHeaderConstant {
        /** The key for the B3 Trace ID in the SOAP header. */
        String X_B3_TRACEID = "X-B3-TRACEID";

        /** The value for the B3 Trace ID retrieved from the MDC context. */
        String X_B3_TRACEID_VALUE_SOAP = MDC.get("X-B3-TraceId");

        /** The content type for XML with UTF-8 charset. */
        String TYPE_XML_CHARSET_UTF8 = "text/xml; charset=utf-8";

        /** The general content type for XML. */
        String TYPE_XML = "text/xml";

        /** Placeholder constant, purpose to be defined later. */
        String XYZ = "xyz";
    }

    /**
     * Contains common HTTP header types.
     */
    public interface HeaderType {
        /** The HTTP header key for content type. */
        String CONTENT_TYPE = "Content-Type";

        /** The HTTP header key for API key. */
        String X_API_KEY = "x-api-key";
    }

    /**
     * Contains date and time format patterns.
     */
    public interface DateTimePattern {
        /** Date format for day/month/year. */
        String DMY = "dd/MM/yyyy";

        /** Date and time format for day/month/year with hours, minutes, and seconds. */
        String DMY_HMS = "dd/MM/yyyy HH:mm:ss";

        /** ISO 8601 format for local date and time. */
        String LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

        /** Date format for year/month/day. */
        String YYYYMMDD = "yyyy-MM-dd";
    }

    /**
     * Contains activation status constants.
     */
    public interface Activation {
        /** Constant representing the active status. */
        Integer ACTIVE = 1;

        /** Constant representing the inactive status. */
        Integer INACTIVE = 0;
    }

    /**
     * Contains security-related constants.
     */
    public interface Security {
        /** The HTTP header for authorization. */
        String AUTHORIZATION = "Authorization";

        /** The prefix for Bearer tokens. */
        String BEARER = "Bearer";

        /** The default registration ID for OpenID Connect. */
        String DEFAULT_REGISTRATION_ID = "oidc";
    }

    /**
     * Contains properties related to token information.
     */
    public interface TokenProperties {
        /** Property for the preferred username in the token. */
        String USERNAME = "preferred_username";

        /** Subject identifier for the user in the token. */
        String ID = "sub";

        /** Property for the full name of the user in the token. */
        String NAME = "name";

        /** Property for the email of the user in the token. */
        String EMAIL = "email";

        /** Property for the individual ID in the token. */
        String INDIVIDUAL_ID = "individual_id";

        /** Property for the organization ID in the token. */
        String ORGANIZATION_ID = "organization_id";
    }

    /**
     * Contains error constants for Keycloak.
     */
    public interface KeyCloakError {
        /** Error code for invalid grants. */
        String INVALID_GRANT = "INVALID_GRANT";

        /** Error code for disabled users or clients. */
        String DISABLED = "DISABLED";

        /** Error code for invalid requests or data. */
        String INVALID = "INVALID";
    }

    /**
     * Contains XML-related constants.
     */
    public interface XmlConst {
        /** Opening tag for the return element in XML. */
        String TAG_OPEN_RETURN = "<return>";

        /** Closing tag for the return element in XML. */
        String TAG_CLOSE_RETURN = "</return>";

        /** String for the less than symbol encoded in XML. */
        String AND_LT_SEMICOLON = "&lt;";

        /** String for the greater than symbol encoded in XML. */
        String AND_GT_SEMICOLON = "&gt;";

        /** Less than character in XML. */
        String LT_CHARACTER = "<";

        /** Greater than character in XML. */
        String GT_CHARACTER = ">";
    }

    /**
     * Contains logging-related titles and constants.
     */
    public interface LoggingTitle {
        /** Title for the request log section. */
        String REQUEST = "\n-- REQUEST --\n";

        /** Title for the request header log section. */
        String REQUEST_HEADER = "\n-- REQUEST HEADER --\n";

        /** Title for the request parameter log section. */
        String REQUEST_PARAM = "-- REQUEST PARAM --\n";

        /** Title for the request body log section. */
        String REQUEST_BODY = "-- REQUEST BODY --\n";

        /** Title for the response log section. */
        String RESPONSE = "\n-- RESPONSE --\n";

        /** Prefix for logging entries. */
        String PREFIX = "|>";

        /** Maximum size for request body logs. */
        Integer BODY_SIZE_REQUEST_MAX = 1000;

        /** Maximum size for response body logs. */
        Integer BODY_SIZE_RESPONSE_MAX = 1000;
    }

    /**
     * Contains constants related to OTP (One-Time Password) functionality.
     */
    public interface Otp {
        /** Action type for OTP registration. */
        String REGISTER = "REGISTER";

        /** Action type for OTP during forgot password process. */
        String FORGOT_PASSWORD = "FORGOT_PASSWORD";

        /** Content description for OTP used for forgot password. */
        String FORGOT_PASSWORD_CONTENT = "OTP for forgot password user";

        /** Content description for OTP used for registration. */
        String REGISTER_CONTENT = "OTP for register user";

        /** Expiration time for OTP in minutes. */
        Integer EXP_MINUTE = 2;

        /** Expiration time for OTP during morning hours in minutes. */
        Integer EXP_OTP_AM_MINUTE = 5;
    }

    /**
     * Contains role name constants.
     */
    public interface RoleName {
        /** Role name for system administrators. */
        String SYSTEM = "system";

        /** Role name for administrators. */
        String ADMIN = "admin";

        /** Role name for general users. */
        String USER = "user";
    }

    /**
     * Contains constants for sorting.
     */
    public interface Sorting {
        /** Operator used to split sorting criteria. */
        String SPLIT_OPERATOR = ",";

        /** Operator indicating descending sort order. */
        String MINUS_OPERATOR = "-";

        /** Operator indicating ascending sort order. */
        String PLUS_OPERATOR = "+";

        /** Constant representing descending sort order. */
        String DESC = "desc";

        /** Constant representing ascending sort order. */
        String ASC = "asc";

        /** Format for displaying field names in sorting. */
        String FILED_DISPLAY = "$1_$2";
    }

    /**
     * Contains template names for email notifications.
     */
    public interface TemplateMail {
        /** Template name for forgot password emails. */
        String FORGOT_PASSWORD = "FORGOT_PASSWORD";

        /** Template name for sign-up emails. */
        String SIGN_UP = "SIGN_UP";

        /** Template name for successful customer activation emails. */
        String CUSTOMER_ACTIVE_SUCCESS = "CUSTOMER_ACTIVE_SUCCESS";

        /** Template name for successful customer registration emails. */
        String CUSTOMER_REGISTER_SUCCESS = "CUSTOMER_REGISTER_SUCCESS";

        /** Template name for successful employee registration emails. */
        String EMPLOYEE_REGISTER_SUCCESS = "EMPLOYEE_REGISTER_SUCCESS";

        /** Template name for account activation emails. */
        String ACCOUNT_ACTIVE = "ACCOUNT_ACTIVE";

        /** Template name for sign-up password emails. */
        String SIGN_UP_PASSWORD = "SIGN_UP_PASSWORD";

        /** Template name for successful account verification emails. */
        String VERIFY_ACCOUNT_SUCESS = "VERIFY_ACCOUNT_SUCESS";

        /** Template name for notifications about account verification. */
        String NOTI_VERIFY_ACCOUNT = "NOTI_VERIFY_ACCOUNT";
    }

    /**
     * Contains actions related to user operations.
     */
    public interface ActionUser {
        /** Action type for system operations. */
        String SYSTEM = "system";
    }

    /**
     * Contains common status constants.
     */
    public interface COMMON {
        /** Constant representing active status. */
        Integer STATUS_ACTIVE = 1;

        /** Constant representing inactive status. */
        Integer STATUS_INACTIVE = 0;

        /** Constant representing invalid status. */
        Integer STATUS_INVALID = -1;

        /** Constant representing null status. */
        Integer STATUS_NULL = -2;

        /** String representation of inactive status. */
        String STR_STATUS_INACTIVE = "0";
    }

    /**
     * <p>
     * STATUS class contains constants representing various status values used
     * throughout the application. These constants are used to indicate the current
     * state of entities such as users, orders, and other resources.
     * </p>
     */
    public interface STATUS {
        /** Represents an active status. */
        Integer ACTIVE = 1;
        /** Represents an inactive status. */
        Integer INACTIVE = 0;
        /** Represents a deleted status. */
        Integer DELETE = -1;
    }

    /**
     * <p>
     * STATE class contains constants representing various state values used in the
     * application. Similar to STATUS, these constants reflect the operational state
     * of different components or entities.
     * </p>
     */
    public interface STATE {
        /** Represents an active state. */
        Integer ACTIVE = 1;
        /** Represents an inactive state. */
        Integer INACTIVE = 0;
        /** Represents a deleted state. */
        Integer DELETE = 3;
    }

    /**
     * <p>
     * PERMISSION_TYPE class defines constants representing types of permissions
     * used within the application. These constants help in managing access control
     * by categorizing permissions into roles and groups.
     * </p>
     */
    public interface PERMISSION_TYPE {
        /** Represents a permission type for roles. */
        String ROLE = "ROLE";
        /** Represents a permission type for groups. */
        String GROUP = "GROUP";
    }

    /**
     * <p>
     * EXCLUDE_LOGGING_ENDPOINTS is a constant HashSet that contains a list of API
     * endpoints that should be excluded from logging. This can be useful for
     * avoiding clutter in logs for health check endpoints or other non-essential
     * requests.
     * </p>
     */
    public static final HashSet<String> EXCLUDE_LOGGING_ENDPOINTS = new HashSet<>(List.of("/actuator/health"));

    /**
     * <p>
     * MAX_BYTE is a constant representing the maximum number of bytes allowed for
     * certain operations in the application. This can be used to set limits on data
     * size, such as in file uploads or data transfers.
     * </p>
     */
    public static final int MAX_BYTE = 4096;

    /**
     * <p>
     * POOL class contains constants related to connection pools used for managing
     * resource consumption efficiently. In this case, it specifies the name of the
     * connection pool for REST clients.
     * </p>
     */
    public interface POOL {
        /** Name of the REST client pool for HTTPS proxy. */
        String REST_CLIENT_POLL = "Rest-client-Pool";
    }

    public interface MINIO_BUCKET_MARKET_INFO {
        String URL_IMAGE = "market-info";
    }

    public interface MINIO_BUCKET_MARKET_SECTION {
        String MARKET_SECTION = "market-section";
    }

    public static final String NULL_IMAGE_SRC = null;

    public static final class ArrayLimit {
        public static final Integer COMMON = 100;
    }
}
