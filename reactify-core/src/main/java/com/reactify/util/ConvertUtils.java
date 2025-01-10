package com.reactify.util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;

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
