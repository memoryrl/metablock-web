package cetus.support;

import java.util.regex.Pattern;

import cetus.exception.ParameterInvalidException;


public class Asserts {

    public static void notNull(String fieldLabel, String fieldName, Object value) {
        if(value == null || String.valueOf(value).isEmpty()) {
            throw ParameterInvalidException.REQUIRED(fieldLabel, fieldName);
        }
    }

    public static void range(String fieldLabel, String fieldName, Object fieldValue, long min, long max) {
        if(fieldValue == null) return;
        long value = Long.parseLong(fieldValue.toString());
        if(value < min || value > max) {
            throw ParameterInvalidException.RANGE(fieldLabel, fieldName, min, max);
        }
    }

    public static void maxLength(String fieldLabel, String fieldName, Object fieldValue, int maxlength) {
        if(fieldValue == null) return;
        int valueLength = fieldValue.toString().length();
        if(valueLength > maxlength) {
            throw ParameterInvalidException.MAX_LENGTH(fieldLabel, fieldName, maxlength);
        }
    }

    public static void pattern(String fieldLabel, String fieldName, Object fieldValue, String name, String regex) {
        if(fieldValue == null) return;
        if(!Pattern.matches(regex, fieldValue.toString())) {
            throw ParameterInvalidException.PATTERN(fieldLabel, fieldName, name);
        }
    }

    public static void length(String fieldLabel, String fieldName, Object fieldValue, int length) {
        int valueLength = fieldValue.toString().length();
        if(valueLength != length) {
            throw ParameterInvalidException.LENGTH(fieldLabel, fieldName, length);
        }
    }

    public static void onlyNumber(String fieldLabel, String fieldName, Object fieldValue) {
        if(!isNumeric(fieldValue.toString())) {
            throw ParameterInvalidException.ONLY_NUMBER(fieldLabel, fieldName);
        }
    }

    private static boolean isNumeric(String strNum) {
        return strNum.matches("-?\\d+(\\.\\d+)?");
    }
}
