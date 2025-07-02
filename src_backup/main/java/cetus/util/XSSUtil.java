package cetus.util;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;
import org.owasp.encoder.Encode;

public final class XSSUtil {

    public static String clean(String content) {
        // baseUri
        return Encode.forHtmlContent(content);
    }
}
