package cetus.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;


/**
 * Datetime Util
 * 
 * @author  finkle
 * @date    2014-10-24
 * @since   3.0
 */
public class DateTimeUtil {

    public static final SimpleDateFormat ISO_FULL_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final SimpleDateFormat ISO_SHORT_FORMAT = new SimpleDateFormat("yyyyMMdd");

    public static final SimpleDateFormat KR_FULL_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat KR_SHORT_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    
    /**
     * 지정한 날짜를 지정한 포맷 형식으로 변환.
     */
    public static String totDateFormat(Date date, String format) {
        
        if (date == null) return "";
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        
        return dateFormat.format(date);
    }
    public static String totDateFormat(Calendar cal, String format) {
        return totDateFormat(cal.getTime(), format);
    }
    public static String totDateFormat(long miliseconds, String format) {
        return totDateFormat(new Date(miliseconds), format);
    }
    /*
    public static String totDateFormat(String date, String format) {
        return totDateFormat(toCalendar(date), format);
    }
    */
    
    /**
     * 지정한 날짜를 yyyy-MM-dd HH:mm:ss 형식으로 변환.
     */
    public static String toDateFull(Date date) {
        return date == null ? "" : KR_FULL_FORMAT.format(date);
    }
    public static String toDateFull(Calendar cal) {
        return toDateFull(cal.getTime());
    }
    public static String toDateFull(long miliseconds) {
        return toDateFull(new Date(miliseconds));
    }
    /*
    public static String toDateFull(String date) {
        return toDateFull(toCalendar(date));
    }
    */

    /**
     * 지정한 날짜를 yyyy-MM-dd 형식으로 변환.
     */
    public static String totDateShort(Date date) {
        return date == null ? "" : KR_SHORT_FORMAT.format(date);
    }
    public static String totDateShort(Calendar cal) {
        return totDateShort(cal.getTime());
    }
    public static String totDateShort(long miliseconds) {
        return totDateShort(new Date(miliseconds));
    }
    /*
    public static String totDateShort(String date) {
        return totDateShort(toCalendar(date));
    }
    */
    
    /**
     * 지정한 날짜를 yyyyMMddHHmmss 형식으로 변환.
     */
    public static String toDateFullISO(Date date) {
        return date == null ? "" : ISO_FULL_FORMAT.format(date);
    }
    public static String toDateFullISO(Calendar cal) {
        return toDateFullISO(cal.getTime());
    }
    public static String toDateFullISO(long miliseconds) {
        return toDateFullISO(new Date(miliseconds));
    }
    /*
    public static String toDateFullISO(String date) {
        return toDateFullISO(toCalendar(date));
    }
    */

    /**
     * 지정한 날짜를 yyyyMMdd 형식으로 변환.
     */
    public static String totDateShortISO(Date date) {
        return date == null ? "" : ISO_SHORT_FORMAT.format(date);
    }
    public static String totDateShortISO(Calendar cal) {
        return totDateShortISO(cal.getTime());
    }
    public static String totDateShortISO(long miliseconds) {
        return totDateShortISO(new Date(miliseconds));
    }
    /*
    public static String totDateShortISO(String date) {
        return totDateShortISO(toCalendar(date));
    }
    */

    /**
     * 오늘 날짜를 yyyyMMddHHmmss 형식으로 변환.
     */
    public static String getToday() {
        return getTodayFull();
    }
    public static String getTodayFull() {
        return ISO_FULL_FORMAT.format(new Date());
    }

    /**
     * 오늘 날짜를 yyyyMMdd 형식으로 변환.
     */
    public static String getTodayShort() {
        return ISO_SHORT_FORMAT.format(new Date());
    }
    
    /**
     * 지정한 시간이 얼마나 경과되었는지 반환.
     */
    public static String getDuration(long time) {

        long duration = Calendar.getInstance().getTimeInMillis() - time;
        
        return "";
        
        //return DurationFormatUtils.formatDuration(duration, "dd'일' HH'시간' mm'분'", false);
    }
    
    /**
     * 지정한 날짜에 대시('-')를 삽입
     */
    public static String appendDash(String src) {
        
        if ( StringUtil.isEmpty(src) ) return src;
        
        if ( src.length() >= 8 ) {
            return src.substring(0, 4) + "-" + src.substring(4, 6) + "-" + src.substring(6, 8);
        }
        return src;
    }
    
    /**
     * 지정한 날짜에 대시('-')를 제거
     */
    public static String removeDash(String src) {
        
        if ( StringUtil.isEmpty(src) ) return src;
        
        return StringUtil.replace(src, "-", "");
    }
    
    /**
     * 지정한 날짜를 Calendar로 변환
     */
    /*
    public static Calendar toCalendar(String src) {

        if ( StringUtil.isEmpty(src) ) return Calendar.getInstance();
        
        if (src.length() < 8) return Calendar.getInstance();
        
        if ( !NumberUtils.isNumber(src) ) return Calendar.getInstance();
        
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,
                Integer.parseInt(StringUtil.substring(src, 0, 4)));
        calendar.set(Calendar.MONTH,
                Integer.parseInt(StringUtil.substring(src, 4, 6)) - 1);
        calendar.set(Calendar.DAY_OF_MONTH,
                Integer.parseInt(StringUtil.substring(src, 6, 8)));
        
        if (src.length() == 14) {
            calendar.set(Calendar.HOUR_OF_DAY, 
                    Integer.parseInt(StringUtil.substring(src, 8, 10)));
            calendar.set(Calendar.MINUTE, 
                    Integer.parseInt(StringUtil.substring(src, 10, 12)));
            calendar.set(Calendar.SECOND, 
                    Integer.parseInt(StringUtil.substring(src, 12, 14)));
        }
        
        return calendar;
    }
    */
    
    /**
     * 지정한 날짜에서 '-', ':', '공백'을 제거한 숫자를 반환
     */
    public static String toDigitString(String src) {
        
        if ( StringUtil.isEmpty(src) ) return "";
        
        if (src.indexOf("-") != -1)
            src = StringUtil.replace(src, "-", "");
        if (src.indexOf(":") != -1)
            src = StringUtil.replace(src, ":", "");
        if (src.indexOf(" ") != -1)
            src = StringUtil.replace(src, " ", "");
        
        return src;
    }
    
    
    /**
     * 지정한 두 날짜의 차이를 계산
     */
    /*
    public static Period diff(Date start, Date end) {

        JDateTime jdt1 = new JDateTime(start);
        JDateTime jdt2 = new JDateTime(end);
        
        return new Period(jdt1, jdt2);
    }
    */

    public static Map<String, String> dateToEng(String dateString) {
        LocalDate date = LocalDate.parse(dateString); // "2024-05-20"
        String year = String.valueOf(date.getYear());
        String month = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH); // May
        String day = String.format("%02d", date.getDayOfMonth());

        Map<String, String> result = new HashMap<>();
        result.put("year", year);
        result.put("month", month);
        result.put("day", day);
        return result;
    }

}
