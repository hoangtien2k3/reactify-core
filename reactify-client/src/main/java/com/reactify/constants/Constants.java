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
     * @return a {@link List} of sensitive header names.
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
}
