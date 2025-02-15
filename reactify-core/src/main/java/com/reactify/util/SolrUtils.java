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
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

@Log4j2
@Getter
@Setter
public class SolrUtils {

    private Map<String, String> queryParams = new HashMap<>();
    public static List<String> ALLOW_PARAMS = List.of("start", "rows", "q", "sort");
    public static String QUERY_PARAM = "q";
    public static String START_PARAM = "start";
    public static String LIMIT_PARAM = "rows";
    public static String SORT_PARAM = "sort";
    public static String SORT_ASC = "ASC";
    public static String SORT_DESC = "desc";

    private SolrUtils(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    private String getQuery() {
        return queryParams.keySet().stream()
                .map(key -> key + "=" + encodeValue(queryParams.get(key)))
                .collect(Collectors.joining("&"));
    }

    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            log.error("Exception when encode value " + value, e);
            return null;
        }
    }

    public static class SolrQueryBuilder {
        private final Map<String, String> queryParams = new HashMap<>();

        public SolrQueryBuilder() {}

        public SolrQueryBuilder addQuery(String value) {
            queryParams.put(QUERY_PARAM, value);
            return this;
        }

        public SolrQueryBuilder addStart(int start) {
            queryParams.put(START_PARAM, String.valueOf(start));
            return this;
        }

        public SolrQueryBuilder addLimit(int limit) {
            queryParams.put(LIMIT_PARAM, String.valueOf(limit));
            return this;
        }

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

        public SolrQueryBuilder addCustomParam(String key, Object value) {
            queryParams.put(key, String.valueOf(value));
            return this;
        }

        public String build() {
            var solrQuery = new SolrUtils(queryParams);
            return solrQuery.getQuery();
        }
    }
}
