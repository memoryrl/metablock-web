package cetus.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StringUtil extends jodd.util.StringUtil {
    public static boolean isEmpty(Object object) {
        if ( object == null ) return true;

        if ( object instanceof String )
            return "".equals(object.toString().trim());
        else if ( object instanceof List )
            return ((List<?>)object).isEmpty();
        else if ( object instanceof Map )
            return ((Map<?,?>)object).isEmpty();
        else if ( object instanceof Object[] )
            return Array.getLength(object) == 0;
        else
            return object == null;
    }

    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    public static String[] commaToArray(String str) {
        String[] split = split(str, ",");
        trimAll(split);
        return split;
    }

    public static List<String> commaToList(String str) {
        String[] split = split(str, ",");
        trimAll(split);
        return Arrays.asList(split);
    }
    
    /**
     * 지정한 길이를 초과한 문자열을 자른 뒤 지정한 접미어를 붙여 반환.
     */
    public static String fixLength(String src, int limit) {
        return UTF8Util.fixLength(src, limit, "..");
    }
    public static String fixLength(String src, int limit, String postfix) {
        return UTF8Util.fixLength(src, limit, postfix);
    }

    /**
     * 숫자인지 여부 확인
     */
    public static boolean isNumeric(String strNum) {
        return strNum.matches("-?\\d+(\\.\\d+)?");
    }

    public static String toUpperUnderscore(String str) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            if(i != 0 && Character.isUpperCase(str.charAt(i))) {
                sb.append("_");
                sb.append(str.charAt(i));
            } else {
                sb.append(str.charAt(i));
            }
        }

        return sb.toString().toUpperCase();
    }
    
    public static String joining(String delemeter, List<String> list) {

        return list.stream()
                .map(str -> str.endsWith(delemeter) ? str.substring(0, str.length() - delemeter.length()) : str)
                .collect(Collectors.joining(delemeter));
    }

    public static String getYear() {
        Calendar c = Calendar.getInstance();
        return String.format("%04d", c.get(Calendar.YEAR));
    }

    public static String getDate() {
        return getDate(".");
    }

    public static String getDate(String delimiter) {
        Calendar c = Calendar.getInstance();
        return String.format("%s%s%02d%s%02d", getYear(), delimiter, c.get(Calendar.MONTH) + 1, delimiter, c.get(Calendar.DATE));
    }
    
    public static String getDate(String delimiter, int days ) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, days);
        return String.format("%s%s%02d%s%02d", getYear(), delimiter, c.get(Calendar.MONTH) + 1, delimiter, c.get(Calendar.DATE));
    }

    public static String getTime() {
        Calendar c = Calendar.getInstance();
        return String.format("%02d:%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
    }

    public static String now() {
        return getDate() + " " + getTime();
    }

    public static String trim(String src) {
        if(isEmpty(src)) {
            return "";
        }
        return src.trim();
    }

	public static String random(int len) {
        String[] src = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");

        List<String> list = new ArrayList<>();
        for(int i = 0; i < len; i++) {
            int index = ((Double)Math.floor(Math.random() * 100.0D)).intValue() % src.length;
            list.add(src[index]);
        }
        return StringUtil.joining("", list);
    }

	public static String nvl(String ctgCd) {
		return isEmpty(ctgCd) ? "" : ctgCd;
	}
	
	public static String nvlFill(String obj, String value) {
		return isEmpty(obj) ? value : obj;
	}
	
	public static String cleanHTML(String src) {

        if (isEmpty(src)) return "";
        return src.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
    }
	
	public static String capitalizeFirstLetter(String str) {
	    if (str == null || str.isEmpty()) {
	        return str;
	    }

	    // 첫 번째 문자를 대문자로 변환
	    char firstChar = Character.toUpperCase(str.charAt(0));

	    // 나머지 문자들을 소문자로 변환
	    String restOfString = str.substring(1).toLowerCase();

	    // 변환된 첫 번째 문자와 나머지 문자열을 합쳐서 결과 반환
	    return firstChar + restOfString;
	}
}

