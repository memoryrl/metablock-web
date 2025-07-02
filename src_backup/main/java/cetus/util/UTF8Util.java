package cetus.util;

import java.io.UnsupportedEncodingException;

/**
 * UTF-8 Util
 *
 * @author  
 * @date    2014-10-27
 * @since   2.0
 */
public class UTF8Util {

    private static String UTF_8 = "UTF-8";
    /**
     * UTF_8 인코딩에 따른 문자열 길이를 반환.
     */
    public static int length(String src) {

        if (StringUtil.isEmpty(src)) return 0;

        try {
            return src.getBytes(UTF_8).length;
        }
        catch(UnsupportedEncodingException e) {
        }
        return src.getBytes().length;
    }

    /**
     * 지정한 길이를 초과한 문자열을 자른 뒤 지정한 접미어를 붙여 반환.
     */
    public static String fixLength(String src, int limit) {
        return fixLength(src, limit, "..");
    }
    public static String fixLength(String src, int limit, String postfix) {

        if (src == null) return "";

        if (limit <= 0) return src;

        try {
            byte[] strbyte = src.getBytes(UTF_8);

            if (strbyte.length <= limit) {
                return src;
            }

            if (StringUtil.isNotEmpty(postfix)) {
                limit -= postfix.length();
            }

            char[] charArray = src.toCharArray();

            int checkLimit = limit;
            for ( int i = 0 ; i < charArray.length ; i++ ) {

                if (charArray[i] < 256) {
                    checkLimit -= 1;
                }
                else {
                    checkLimit -= 3;
                }

                if (checkLimit <= 0) {
                    break;
                }
            }

            // 대상 문자열 마지막 자리가 2바이트의 중간일 경우 제거함
            if ( checkLimit == -1 ) checkLimit = -2;
            else if ( checkLimit == -2 ) checkLimit = -1;

            int checkLength = limit + checkLimit;

            if ( checkLength < 0 ) checkLength = 0;

            byte[] newByte = new byte[checkLength];

            for ( int i = 0 ; i < newByte.length ; i++ ) {
                newByte[i] = strbyte[i];
            }

            return new String(newByte, UTF_8) + postfix;
        }
        catch (UnsupportedEncodingException e) {
            return src;
        }
    }
}

