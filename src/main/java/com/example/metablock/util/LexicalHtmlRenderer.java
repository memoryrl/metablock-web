package com.example.metablock.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Payload CMS 의 Lexical Rich-Text JSON 구조를 단순 HTML 로 변환한다.
 * 복잡한 노드는 아직 지원하지 않고, heading / paragraph / text 정도만 매핑한다.
 */
@Component
public class LexicalHtmlRenderer {

    private static final Logger log = LoggerFactory.getLogger(LexicalHtmlRenderer.class);

    private final ObjectMapper objectMapper;

    public LexicalHtmlRenderer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * @param lexicalJson Payload Rich-Text 필드(JSON) 를 Object 혹은 JSON 문자열로 전달
     * @return 변환된 HTML. 실패 시 원본 문자열을 그대로 반환.
     */
    public String render(Object lexicalJson) {
        if (lexicalJson == null) {
            return "";
        }
        try {
            JsonNode rootNode;
            if (lexicalJson instanceof String) {
                rootNode = objectMapper.readTree((String) lexicalJson);
            } else {
                rootNode = objectMapper.valueToTree(lexicalJson);
            }

            JsonNode root = rootNode.path("root");
            if (root.isMissingNode()) {
                // 예상 구조가 아니면 toString 반환
                return lexicalJson.toString();
            }
            StringBuilder html = new StringBuilder();
            ArrayNode children = (ArrayNode) root.path("children");
            renderChildren(children, html);
            return html.toString();
        } catch (Exception e) {
            log.error("Lexical JSON to HTML 변환 실패", e);
            return lexicalJson.toString();
        }
    }

    private void renderChildren(ArrayNode children, StringBuilder html) {
        if (children == null) return;
        for (JsonNode child : children) {
            String type = child.path("type").asText();
            switch (type) {
                case "paragraph":
                    html.append("<p>");
                    renderChildren((ArrayNode) child.path("children"), html);
                    html.append("</p>");
                    break;
                case "text":
                    html.append(escapeHtml(child.path("text").asText()));
                    break;
                case "heading":
                    String tag = "h" + child.path("tag").asText("2"); // default h2
                    html.append("<").append(tag).append(">");
                    renderChildren((ArrayNode) child.path("children"), html);
                    html.append("</").append(tag).append(">");
                    break;
                default:
                    // 지원 안 하는 노드는 내부 텍스트만 추출
                    renderChildren((ArrayNode) child.path("children"), html);
            }
        }
    }

    private String escapeHtml(String raw) {
        if (raw == null) return "";
        return raw.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
} 