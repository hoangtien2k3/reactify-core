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

public class SQLUtils {

    /**
     * Replaces special characters in a string to prevent them from being
     * interpreted as SQL wildcards.
     * <p>
     * Specifically, the percent sign (`%`) and underscore (`_`) are replaced with
     * their escaped versions (`\%` and `\_` respectively). This is useful for
     * ensuring that these characters are treated as literals in SQL queries rather
     * than wildcards.
     * </p>
     *
     * @param value
     *            The string to be processed. Can be <code>null</code> or empty.
     * @return The processed string with special characters replaced. Returns an
     *         empty string if the input is <code>null</code> or empty.
     */
    public static String replaceSpecialDigit(String value) {
        if (!DataUtil.isNullOrEmpty(value)) {
            value = value.replace("%", "\\%").replace("_", "\\_");
            return value;
        }
        return "";
    }
}
