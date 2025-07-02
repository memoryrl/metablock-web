package cetus.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlUtil {

    public static String stripHtmlPreserveLines(String html) {
        if (html == null || html.isEmpty()) return "";

        Document doc = Jsoup.parse(html);
        // <br>을 줄바꿈으로, <p> 태그는 블록 단위니까 개행 처리
        doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
        doc.select("br").append("\\n");
        doc.select("p").prepend("\\n");

        String text = doc.text().replace("\\n", "\n").trim();
        return text;
    }
}