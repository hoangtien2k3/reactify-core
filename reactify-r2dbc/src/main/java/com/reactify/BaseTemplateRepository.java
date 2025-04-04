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

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <p>
 * The BaseTemplateRepository class serves as a base class for repository
 * implementations that interact with the database using R2DBC. It provides
 * utility methods for executing SQL queries and converting results into
 * specified types using Jackson's ObjectMapper.
 * </p>
 *
 * <p>
 * This class is designed to facilitate the execution of custom SQL queries
 * while abstracting the common functionalities needed for database operations.
 * </p>
 *
 * @author hoangtien2k3
 */
public class BaseTemplateRepository {

    @Autowired
    private R2dbcEntityTemplate entityTemplate;

    private final ObjectMapper objectMapper;

    /**
     * <p>
     * Constructor for BaseTemplateRepository that initializes the ObjectMapper with
     * specific configurations.
     * </p>
     */
    public BaseTemplateRepository() {
        objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .build();
    }

    /**
     * <p>
     * Executes a list query with the specified SQL and parameters, mapping the
     * result to the specified type.
     * </p>
     *
     * @param sql
     *            a {@link String} object representing the SQL query
     * @param params
     *            a {@link Map} object containing the query parameters
     * @param type
     *            a {@link Class} object representing the type to map the results to
     * @param <T>
     *            a generic type parameter
     * @return a {@link Flux} object containing the results mapped to the specified
     *         type
     */
    protected <T> Flux<T> listQuery(String sql, Map<String, Object> params, Class<T> type) {
        DatabaseClient.GenericExecuteSpec spec =
                entityTemplate.getDatabaseClient().sql(sql);
        if (params != null && !params.isEmpty()) {
            for (String param : params.keySet()) {
                spec = spec.bind(param, params.get(param));
            }
        }
        return spec.fetch().all().map(raw -> objectMapper.convertValue(raw, type));
    }

    /**
     * <p>
     * Executes a count query with the specified SQL and parameters, returning the
     * total count.
     * </p>
     *
     * @param sql
     *            a {@link String} object representing the SQL query
     * @param params
     *            a {@link Map} object containing the query parameters
     * @return a {@link Mono} object containing the total count
     */
    protected Mono<Long> countQuery(String sql, Map<String, Object> params) {
        String query = "select count(*) as common_count_col from (" + sql + ") as common_count_alias";
        DatabaseClient.GenericExecuteSpec spec =
                entityTemplate.getDatabaseClient().sql(query);
        if (params != null && !params.isEmpty()) {
            for (String param : params.keySet()) {
                spec = spec.bind(param, params.get(param));
            }
        }
        return spec.fetch()
                .first()
                .map(result -> result.get("common_count_col"))
                .cast(Long.class);
    }
}
