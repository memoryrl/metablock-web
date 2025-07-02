package cetus.support;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.Getter;
/**
 * 한국어 단어에 맞게 조사를 설정한다
 *
 * @author jcyoon
 *
 */
public class SuffixReplacer {

    private static String regex = "";
    private static Map<String, SuffixPair> pairs = new HashMap<>();
    static {
        pairs.put("(이)가", new SuffixPair("이", "가"));
        pairs.put("(이)가", new SuffixPair("이", "가"));
        pairs.put("(와)과", new SuffixPair("과", "와"));
        pairs.put("(을)를", new SuffixPair("을", "를"));
        pairs.put("(은)는", new SuffixPair("은", "는"));
        pairs.put("(아)야", new SuffixPair("아", "야"));
        pairs.put("(이)여", new SuffixPair("이여", "여"));
        pairs.put("(으)로", new SuffixPair("으로", "로"));
        pairs.put("(이)라", new SuffixPair("이라", "라"));

        regex = pairs.keySet().stream()
            .map(s -> s.replaceAll("\\(", "\\\\("))
            .map(s -> s.replaceAll("\\)", "\\\\)"))
            .collect(Collectors.joining("|"));
    }

    /**
     * 조사를 변경한다.
     * @param src 변경대상 문자열
     * @return 변경된 문자열
     */
    public static String replace(String src) {
        StringBuilder strBuilder = new StringBuilder(src.length());
        int lastHeadIndex = 0;

        Matcher m = Pattern.compile(regex).matcher(src);
        while (m.find()) {

            SuffixPair pair = pairs.get(m.group());
            strBuilder.append(src, lastHeadIndex, m.start());
            if(m.start() > 0) {
                char prevChar = src.charAt(m.start() - 1);
                if(hasJong(prevChar) && !m.group().equals("(으)로") || hasJongExceptRieul(prevChar) && m.group().equals("(으)로")) {
                    strBuilder.append(pair.getFirst());
                } else {
                    strBuilder.append(pair.getSecond());
                }
            } else {
                strBuilder.append(pair.getFirst());
            }

            lastHeadIndex = m.end();
        }
        strBuilder.append(src, lastHeadIndex, src.length());
        return strBuilder.toString();
    }

    private static boolean hasJong(char inChar) {
        if (inChar >= 0xAC00 && inChar <= 0xD7A3) {// 가 ~ 힣
            int localCode = inChar - 0xAC00; // 가~ 이후 로컬 코드
            int jongCode = localCode % 28;
            return jongCode > 0;
        } else {
            return false;
        }
    }

    private static boolean hasJongExceptRieul(char inChar) {
        if (inChar >= 0xAC00 && inChar <= 0xD7A3) {
            int localCode = inChar - 0xAC00;
            int jongCode = localCode % 28;
            return !(jongCode == 8 || jongCode == 0);
        } else {
            return false;
        }
    }

    @Getter
    private static class SuffixPair {
        private String first;
        private String second;

        SuffixPair(String first, String second) {
            this.first = first;
            this.second = second;
        }
    }
}
