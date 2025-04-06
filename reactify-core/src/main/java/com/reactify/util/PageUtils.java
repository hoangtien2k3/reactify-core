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

/**
 * Utility class for data manipulation and processing. This class contains
 * static methods for various data-related operations.
 */
public class PageUtils {

    /**
     * Calculates the offset for database queries based on page number and page
     * size.
     *
     * @param page
     *            the current page number (1-based index)
     * @param size
     *            the number of items per page
     * @return the calculated offset, or 0 if inputs are invalid
     */
    public static int getOffset(Integer page, Integer size) {
        if (page == null || page <= 0 || size == null || size <= 0) return 0;
        return (page - 1) * size;
    }

    /**
     * Normalizes the page number. If the input is null or less than 1, returns 1.
     *
     * @param page
     *            the page number to normalize
     * @return a valid page number starting from 1
     */
    public static int normalizePage(Integer page) {
        return (page == null || page < 1) ? 1 : page;
    }

    /**
     * Normalizes the page size. If input is invalid, returns the defaultSize. Caps
     * the value at maxSize.
     *
     * @param size
     *            the requested page size
     * @param defaultSize
     *            the default size to use if input is invalid
     * @param maxSize
     *            the maximum allowable size
     * @return a valid page size
     */
    public static int normalizeSize(Integer size, int defaultSize, int maxSize) {
        if (size == null || size < 1) return defaultSize;
        return Math.min(size, maxSize);
    }

    /**
     * Calculates the total number of pages based on total items and page size.
     *
     * @param totalItems
     *            the total number of items
     * @param pageSize
     *            the number of items per page
     * @return the total number of pages
     */
    public static int calculateTotalPages(long totalItems, int pageSize) {
        if (pageSize <= 0) return 0;
        return (int) Math.ceil((double) totalItems / pageSize);
    }

    /**
     * Checks whether the requested page exceeds the available total pages.
     *
     * @param currentPage
     *            the current page number (1-based index)
     * @param totalPages
     *            the total number of available pages
     * @return true if current page exceeds total pages, false otherwise
     */
    public static boolean isPageOutOfRange(int currentPage, int totalPages) {
        return currentPage > totalPages;
    }

    /**
     * Ensures page number and size are valid. Returns default values if necessary.
     *
     * @param page
     *            the input page number
     * @param size
     *            the input size
     * @param defaultPage
     *            default page number if input is invalid
     * @param defaultSize
     *            default size if input is invalid
     * @param maxSize
     *            maximum allowed size
     * @return a two-element array: [normalizedPage, normalizedSize]
     */
    public static int[] normalizePagingParams(
            Integer page, Integer size, int defaultPage, int defaultSize, int maxSize) {
        int p = (page == null || page < 1) ? defaultPage : page;
        int s = normalizeSize(size, defaultSize, maxSize);
        return new int[] {p, s};
    }
}
