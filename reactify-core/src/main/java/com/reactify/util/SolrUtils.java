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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for building Solr query strings. This class supports adding
 * standard Solr query parameters such as start, rows, q, and sort.
 */
@Log4j2
public class SolrUtils {

    private Map<String, String> queryParams = new HashMap<>();

    /**
     * List of allowed Solr parameters.
     */
    public static final List<String> ALLOW_PARAMS = List.of("start", "rows", "q", "sort");

    /**
     * The query parameter key used for search queries.
     */
    public static final String QUERY_PARAM = "q";

    /**
     * The query parameter key used to specify the starting index for pagination.
     */
    public static final String START_PARAM = "start";

    /**
     * The query parameter key used to specify the maximum number of results to
     * return.
     */
    public static final String LIMIT_PARAM = "rows";

    /**
     * The query parameter key used to specify the sorting criteria.
     */
    public static final String SORT_PARAM = "sort";

    /**
     * The value used to specify ascending order in sorting.
     */
    public static final String SORT_ASC = "ASC";

    /**
     * The value used to specify descending order in sorting.
     */
    public static final String SORT_DESC = "desc";

    /**
     * Private constructor to initialize the SolrUtils with query parameters.
     *
     * @param queryParams
     *            a map containing the query parameters
     */
    private SolrUtils(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    /**
     * Constructs the Solr query string by encoding each parameter and joining them
     * with '&'.
     *
     * @return the encoded Solr query string
     */
    private String getQuery() {
        return queryParams.keySet().stream()
                .map(key -> key + "=" + encodeValue(queryParams.get(key)))
                .collect(Collectors.joining("&"));
    }

    /**
     * Encodes a query parameter value using UTF-8 encoding.
     *
     * @param value
     *            the value to be encoded
     * @return the encoded value, or {@code null} if an encoding error occurs
     */
    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            log.error("Exception when encoding value: " + value, e);
            return null;
        }
    }

    /**
     * Builder class for constructing Solr queries. This class provides methods to
     * add various Solr parameters.
     */
    public static class SolrQueryBuilder {
        private final Map<String, String> queryParams = new HashMap<>();

        /**
         * Default constructor for {@link SolrQueryBuilder}.
         */
        public SolrQueryBuilder() {}

        /**
         * Adds a query parameter 'q' with the specified value.
         *
         * @param value
         *            the query value
         * @return the current {@link SolrQueryBuilder} instance
         */
        public SolrQueryBuilder addQuery(String value) {
            queryParams.put(QUERY_PARAM, value);
            return this;
        }

        /**
         * Adds a start parameter indicating the starting index of results.
         *
         * @param start
         *            the starting index (zero-based)
         * @return the current {@link SolrQueryBuilder} instance
         */
        public SolrQueryBuilder addStart(int start) {
            queryParams.put(START_PARAM, String.valueOf(start));
            return this;
        }

        /**
         * Adds a limit parameter specifying the number of results to return.
         *
         * @param limit
         *            the maximum number of results to return
         * @return the current {@link SolrQueryBuilder} instance
         */
        public SolrQueryBuilder addLimit(int limit) {
            queryParams.put(LIMIT_PARAM, String.valueOf(limit));
            return this;
        }

        /**
         * Adds a sort parameter to sort the results by a specified column and order.
         *
         * @param sortColumn
         *            the column by which to sort
         * @param des
         *            the sorting order ("ASC" for ascending, "desc" for descending)
         * @return the current {@link SolrQueryBuilder} instance
         */
        public SolrQueryBuilder addSort(String sortColumn, String des) {
            if (StringUtils.isNotBlank(des)) {
                if (des.equals(SORT_ASC)) {
                    queryParams.put(SORT_PARAM, sortColumn + " " + SORT_ASC);
                } else {
                    queryParams.put(SORT_PARAM, sortColumn + " " + SORT_DESC);
                }
            } else {
                queryParams.put(SORT_PARAM, sortColumn);
            }
            return this;
        }

        /**
         * Adds a custom parameter to the query.
         *
         * @param key
         *            the parameter name
         * @param value
         *            the parameter value
         * @return the current {@link SolrQueryBuilder} instance
         */
        public SolrQueryBuilder addCustomParam(String key, Object value) {
            queryParams.put(key, String.valueOf(value));
            return this;
        }

        /**
         * Builds and returns the final Solr query string.
         *
         * @return the constructed Solr query string
         */
        public String build() {
            var solrQuery = new SolrUtils(queryParams);
            return solrQuery.getQuery();
        }
    }
}
