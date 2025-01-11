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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConvertUtils {

    private static final Logger log = LogManager.getLogger(ConvertUtils.class);
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.###");

    public static String convertStringJsonRawValue(String valueRaw) {
        return !DataUtil.isNullOrEmpty(valueRaw) ? "\"".concat(valueRaw).concat("\"") : "\"\"";
    }

    public static String stripTags(String str) {
        int startPosition = str.indexOf('<');
        int endPosition;
        while (startPosition != -1) {
            endPosition = str.indexOf('>', startPosition);
            str = str.substring(0, startPosition) + (endPosition != -1 ? str.substring(endPosition + 1) : "");
            startPosition = str.indexOf('<');
        }
        return str;
    }

    public static String convertNumber(Number number) {
        if (number == null) {
            return "0";
        } else {
            DecimalFormat decimalFormat = new DecimalFormat();
            DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
            symbols.setGroupingSeparator('.');
            symbols.setDecimalSeparator(',');
            decimalFormat.setDecimalFormatSymbols(symbols);
            return decimalFormat.format(number);
        }
    }

    public static Number convertNumber(String number) {
        if (number == null) {
            return 0;
        } else {
            try {
                return DECIMAL_FORMAT.parse(number);
            } catch (ParseException e) {
                return 0;
            }
        }
    }

    public static boolean compareSpecial(Object obj, String target) {
        return obj != null && obj.toString().equals(target);
    }

    public static int getRegPackage(String code, List<String> packUsing, List<String> canceledList) {
        if (canceledList.contains(code)) {
            return 3;
        } else if (packUsing.contains(code)) {
            return 1;
        } else {
            return 0;
        }
    }
}
