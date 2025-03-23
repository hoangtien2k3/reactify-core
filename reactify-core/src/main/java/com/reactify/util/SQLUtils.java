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
package com.reactify.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for data manipulation and processing. This class contains
 * static methods for various data-related operations.
 */
public class SQLUtils {

    /**
     * <p>
     * replaceSpecialDigit.
     * </p>
     *
     * @param value
     *            a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public static String replaceSpecialDigit(String value) {
        if (!StringUtils.isEmpty(value)) {
            value = value.replace("%", "\\%").replace("_", "\\_");
            return value;
        }
        return "";
    }
}
