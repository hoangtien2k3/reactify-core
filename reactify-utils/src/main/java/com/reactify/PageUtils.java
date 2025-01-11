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

public class PageUtils {

    /**
     * Calculates the offset for pagination based on the current page number and
     * page size.
     * <p>
     * This method computes the starting index for the given page number and page
     * size. The offset is calculated as (page - 1) * size. If the page or size is
     * null or not positive, it returns 0.
     * </p>
     *
     * @param page
     *            The current page number. Must be greater than 0.
     * @param size
     *            The number of items per page. Must be greater than 0.
     * @return The calculated offset. Returns 0 if page or size is null or less than
     *         or equal to 0.
     */
    public static int getOffset(Integer page, Integer size) {
        if (page == null || page <= 0 || size == null || size <= 0) return 0;
        return (page - 1) * size;
    }
}
