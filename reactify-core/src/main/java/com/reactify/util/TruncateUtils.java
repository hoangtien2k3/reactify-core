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
package com.reactify.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;

/**
 * <p>
 * TruncateUtils class.
 * </p>
 *
 * @author hoangtien2k3
 */
public class TruncateUtils {

    /**
     * A static logger instance for logging messages
     */
    private static final Logger log = LoggerFactory.getLogger(TruncateUtils.class);

    /**
     * <p>
     * truncate.
     * </p>
     *
     * @param s
     *            a {@link java.lang.String} object
     * @param maxByte
     *            a int
     * @return a {@link java.lang.String} object
     */
    public static String truncate(String s, int maxByte) {
        try {
            if (DataUtil.isNullOrEmpty(s)) {
                return s;
            }
            final byte[] utf8Bytes = s.getBytes(StandardCharsets.UTF_8);
            if (utf8Bytes.length <= maxByte) {
                return s;
            }
            return truncateBody(s, maxByte);
        } catch (Exception ex) {
            log.error("Can't truncate ", ex);
        }
        return s;
    }

    /**
     * <p>
     * truncateBody.
     * </p>
     *
     * @param s
     *            a {@link java.lang.String} object
     * @param maxByte
     *            a int
     * @return a {@link java.lang.String} object
     */
    public static String truncateBody(String s, int maxByte) {
        int b = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int skip = 0;
            int more;
            if (c <= 0x007f) {
                more = 1;
            } else if (c <= 0x07FF) {
                more = 2;
            } else if (c <= 0xd7ff) {
                more = 3;
            } else if (c <= 0xDFFF) {
                more = 4;
                skip = 1;
            } else {
                more = 3;
            }
            if (b + more > maxByte) {
                return s.substring(0, i);
            }
            b += more;
            i += skip;
        }
        return s;
    }

    /**
     * <p>
     * truncateBody.
     * </p>
     *
     * @param responseBody
     *            a {@link java.lang.Object} object
     * @return a {@link java.lang.String} object
     */
    public static String truncateBody(Object responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(responseBody);
        } catch (Exception e) {
            log.error("Exception when parse response to string, ignore response", e);
            return "Truncated and remove if has exception";
        }
    }

    private String truncateBody(MultiValueMap<String, String> formData) {
        StringBuilder messageResponse = new StringBuilder();
        Set<String> keys = formData.keySet();
        for (String key : keys) {
            messageResponse.append(key).append(":").append(truncateBody(formData.get(key)));
        }
        return messageResponse.toString();
    }
}
