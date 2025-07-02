package cetus.bean;
import java.util.Locale;

import org.springframework.context.MessageSource;

import cetus.support.SuffixReplacer;

public class Message {
    private static MessageSource messageSource;

    public static void setMessageSource(MessageSource m) {
        messageSource = m;
    }

    public static String get(String key) {
        return get(key, null);
    }

    public static String get(String key, Object[] args) {
        String message = messageSource.getMessage(key, args, Locale.getDefault());
        return SuffixReplacer.replace(message);
    }

    public static String response(int code) {
        return get("response." + String.valueOf(code));
    }

    public static String response(int code, Object... args) {
        return get("response." + String.valueOf(code), args);
    }
}
