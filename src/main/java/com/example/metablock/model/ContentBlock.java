package com.example.metablock.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentBlock {
    private String blockType;
    private Map<String, Object> heading;
    private Map<String, Object> text;
    private Map<String, Object> image;
    private Map<String, Object> cardGrid;
    private Map<String, Object> hero;
    private Map<String, Object> cta;
    
    // 원본 JSON 데이터 보관
    private JsonNode rawData;
    
    public String getBlockType() {
        return blockType;
    }
    
    public void setBlockType(String blockType) {
        this.blockType = blockType;
    }
    
    public Object getBlockData() {
        switch (blockType) {
            case "heading":
                return heading;
            case "text":
                return text;
            case "image":
                return image;
            case "cardGrid":
                return cardGrid;
            case "hero":
                return hero;
            case "cta":
                return cta;
            default:
                return null;
        }
    }
} 