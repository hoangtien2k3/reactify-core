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

import static com.reactify.ValidateUtils.CAMELCASE;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SortingUtils {

    public static final String SPLIT_OPERATOR = ",";
    public static final String MINUS_OPERATOR = "-";
    public static final String PLUS_OPERATOR = "+";
    public static final String DESC = "desc";
    public static final String ASC = "asc";
    public static final String FILED_DISPLAY = "$1_$2";

    /**
     * <p>
     * parseSorting.
     * </p>
     *
     * @param sortConfig
     *            a {@link java.lang.String} object
     * @param objectClass
     *            a {@link java.lang.Class} object
     * @return a {@link java.lang.String} object
     */
    public static String parseSorting(String sortConfig, Class<?> objectClass) {
        List<String> convertSorting = convertSorting(sortConfig.replaceAll(CAMELCASE, FILED_DISPLAY), objectClass);
        if (convertSorting == null || convertSorting.isEmpty()) {
            return null;
        }
        return String.join(
                SPLIT_OPERATOR,
                convertSorting.toString().toLowerCase().replace("[", "").replace("]", ""));
    }

    /**
     * <p>
     * convertSorting.
     * </p>
     *
     * @param sortConfig
     *            a {@link java.lang.String} object
     * @param objectClass
     *            a {@link java.lang.Class} object
     * @return a {@link java.util.List} object
     */
    public static List<String> convertSorting(String sortConfig, Class<?> objectClass) {
        if (DataUtil.isNullOrEmpty(sortConfig)) {
            return null;
        }
        List<String> filedNames = getFieldsOfClass(objectClass);
        String[] sortElements = sortConfig.split(SPLIT_OPERATOR);
        List<String> orderList = new LinkedList<>();
        for (String element : sortElements) {
            if (element == null) {
                continue;
            }
            if (element.startsWith(MINUS_OPERATOR)) {
                handleElement(element, MINUS_OPERATOR, filedNames, orderList);
            } else if (element.startsWith(PLUS_OPERATOR)) {
                handleElement(element, PLUS_OPERATOR, filedNames, orderList);
            }
        }
        return orderList;
    }

    private static void handleElement(String element, String operator, List<String> fields, List<String> orderList) {
        if (element.length() <= operator.length()) {
            return;
        }
        String field = element.substring(element.indexOf(operator) + operator.length());
        if (fields.contains(field)) {
            orderList.add(field + " " + convertOperator(operator));
        }
    }

    private static List<String> getFieldsOfClass(Class<?> object) {
        List<String> filedNames = new ArrayList<>();
        for (Field field : getAllFields(object)) {
            filedNames.add(field.getName().replaceAll(CAMELCASE, FILED_DISPLAY));
        }
        return filedNames;
    }

    private static String convertOperator(String operator) {
        if (MINUS_OPERATOR.equals(operator)) {
            return DESC;
        } else {
            return ASC;
        }
    }

    private static Field[] getAllFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            Field[] superFields = getAllFields(superClass);
            Field[] allFields = new Field[fields.length + superFields.length];
            System.arraycopy(fields, 0, allFields, 0, fields.length);
            System.arraycopy(superFields, 0, allFields, fields.length, superFields.length);
            fields = allFields;
        }
        return fields;
    }
}
