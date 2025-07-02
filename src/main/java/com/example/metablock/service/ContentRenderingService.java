package com.example.metablock.service;

import com.example.metablock.model.ContentBlock;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContentRenderingService {
    
    private static final Logger log = LoggerFactory.getLogger(ContentRenderingService.class);
    
    private final ObjectMapper objectMapper;
    
    public ContentRenderingService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    public Map<String, Object> prepareContentForRendering(List<ContentBlock> contentBlocks) {
        Map<String, Object> renderingData = new HashMap<>();
        
        if (contentBlocks == null || contentBlocks.isEmpty()) {
            return renderingData;
        }
        
        for (int i = 0; i < contentBlocks.size(); i++) {
            ContentBlock block = contentBlocks.get(i);
            String blockId = "block_" + i;
            
            Map<String, Object> blockData = new HashMap<>();
            blockData.put("type", block.getBlockType());
            blockData.put("data", processBlockData(block));
            blockData.put("index", i);
            
            renderingData.put(blockId, blockData);
        }
        
        renderingData.put("totalBlocks", contentBlocks.size());
        return renderingData;
    }
    
    private Map<String, Object> processBlockData(ContentBlock block) {
        Map<String, Object> processedData = new HashMap<>();
        Object blockData = block.getBlockData();
        
        if (blockData == null) {
            return processedData;
        }
        
        try {
            // Object를 Map으로 변환
            Map<String, Object> dataMap = objectMapper.convertValue(blockData, Map.class);
            
            switch (block.getBlockType()) {
                case "heading":
                    processedData = processHeadingBlock(dataMap);
                    break;
                case "text":
                    processedData = processTextBlock(dataMap);
                    break;
                case "image":
                    processedData = processImageBlock(dataMap);
                    break;
                case "cardGrid":
                    processedData = processCardGridBlock(dataMap);
                    break;
                case "hero":
                    processedData = processHeroBlock(dataMap);
                    break;
                case "cta":
                    processedData = processCtaBlock(dataMap);
                    break;
                default:
                    processedData = dataMap;
            }
        } catch (Exception e) {
            log.error("Error processing block data for type: " + block.getBlockType(), e);
            processedData.put("error", "Failed to process block data");
        }
        
        return processedData;
    }
    
    private Map<String, Object> processHeadingBlock(Map<String, Object> data) {
        Map<String, Object> processed = new HashMap<>(data);
        
        // 기본값 설정
        processed.putIfAbsent("level", "h2");
        processed.putIfAbsent("className", "");
        
        // CSS 클래스 조합
        String level = (String) processed.get("level");
        String customClass = (String) processed.get("className");
        String combinedClass = level + " " + (customClass != null ? customClass : "");
        processed.put("cssClass", combinedClass.trim());
        
        return processed;
    }
    
    private Map<String, Object> processTextBlock(Map<String, Object> data) {
        Map<String, Object> processed = new HashMap<>(data);
        
        // 기본값 설정
        processed.putIfAbsent("alignment", "left");
        
        // 정렬에 따른 CSS 클래스
        String alignment = (String) processed.get("alignment");
        String alignmentClass = "text-" + alignment;
        processed.put("alignmentClass", alignmentClass);
        
        return processed;
    }
    
    private Map<String, Object> processImageBlock(Map<String, Object> data) {
        Map<String, Object> processed = new HashMap<>(data);
        
        // 기본값 설정
        processed.putIfAbsent("width", "100%");
        processed.putIfAbsent("height", "auto");
        
        // 스타일 문자열 생성
        String width = (String) processed.get("width");
        String height = (String) processed.get("height");
        String style = String.format("width: %s; height: %s;", width, height);
        processed.put("style", style);
        
        return processed;
    }
    
    private Map<String, Object> processCardGridBlock(Map<String, Object> data) {
        Map<String, Object> processed = new HashMap<>(data);
        
        // 기본값 설정
        processed.putIfAbsent("columns", "3");
        
        // Bootstrap 그리드 클래스 계산
        String columns = (String) processed.get("columns");
        int colCount = Integer.parseInt(columns);
        int bootstrapCol = 12 / colCount;
        String colClass = "col-md-" + bootstrapCol;
        processed.put("columnClass", colClass);
        
        return processed;
    }
    
    private Map<String, Object> processHeroBlock(Map<String, Object> data) {
        Map<String, Object> processed = new HashMap<>(data);
        
        // 배경 이미지 스타일 생성
        String backgroundImage = (String) processed.get("backgroundImage");
        if (backgroundImage != null && !backgroundImage.isEmpty()) {
            String backgroundStyle = String.format(
                "background-image: url('%s'); background-size: cover; background-position: center;", 
                backgroundImage
            );
            processed.put("backgroundStyle", backgroundStyle);
        }
        
        return processed;
    }
    
    private Map<String, Object> processCtaBlock(Map<String, Object> data) {
        Map<String, Object> processed = new HashMap<>(data);
        
        // 기본값 설정
        processed.putIfAbsent("style", "primary");
        processed.putIfAbsent("size", "md");
        
        // Bootstrap 버튼 클래스 생성
        String style = (String) processed.get("style");
        String size = (String) processed.get("size");
        
        String btnClass = "btn btn-" + style;
        if (!"md".equals(size)) {
            btnClass += " btn-" + size;
        }
        processed.put("buttonClass", btnClass);
        
        return processed;
    }
    
    public String generateBlockTemplate(String blockType) {
        return "components/" + blockType;
    }
} 