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
package com.reactify;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;

public class TruncateUtils {

    /**
     * Truncates a string to fit within a specified byte length when encoded in
     * UTF-8.
     * <p>
     * This method first checks if the string is null or empty, and if so, returns
     * it as is. If the string is longer than the specified byte length, it is
     * truncated while preserving multi-byte characters.
     * </p>
     *
     * @param s
     *            The string to be truncated. Can be <code>null</code> or empty.
     * @param maxByte
     *            The maximum byte length for the truncated string.
     * @return A truncated version of the input string if it exceeds the specified
     *         byte length, otherwise the original string.
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
        } catch (Exception ignored) {
        }
        return s;
    }

    /**
     * Truncates a string to fit within a specified byte length when encoded in
     * UTF-8.
     * <p>
     * This method iterates over the characters in the string, calculating the byte
     * length for each character and truncating the string when the total byte
     * length exceeds the specified limit.
     * </p>
     *
     * @param s
     *            The string to be truncated.
     * @param maxByte
     *            The maximum byte length for the truncated string.
     * @return A truncated version of the input string if it exceeds the specified
     *         byte length, otherwise the original string.
     */
    public static String truncateBody(String s, int maxByte) {
        int b = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            // ranges from http://en.wikipedia.org/wiki/UTF-8
            int skip = 0;
            int more;
            if (c <= 0x007f) {
                more = 1;
            } else if (c <= 0x07FF) {
                more = 2;
            } else if (c <= 0xd7ff) {
                more = 3;
            } else if (c <= 0xDFFF) {
                // surrogate area, consume next char as well
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
     * Serializes an object to a JSON string and truncates it to fit within a
     * specified byte length.
     * <p>
     * This method uses Jackson's `ObjectMapper` to convert the object to a JSON
     * string and then truncates it. If serialization fails, a placeholder string is
     * returned.
     * </p>
     *
     * @param responseBody
     *            The object to be serialized and truncated.
     * @return A JSON representation of the object, truncated if necessary. Returns
     *         a placeholder string if serialization fails.
     */
    public static String truncateBody(Object responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(responseBody);
        } catch (Exception e) {
            return "Truncated and remove if has exception";
        }
    }
}
