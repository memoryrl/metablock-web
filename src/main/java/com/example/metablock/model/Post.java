package com.example.metablock.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {
    private String id;
    private String title;
    private String slug;
    private List<Category> categories;
    private Object content; // Rich text content (Lexical JSON)
    private String excerpt; // Meta description or excerpt
    private String featuredImage;
    @JsonProperty("_status")
    private String status;
    @JsonProperty("publishedAt")
    private String publishedDate;
    private String createdAt;  // Changed to String to match API response
    private String updatedAt;  // Changed to String to match API response
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getFirstCategorySlug() {
        if (categories != null && !categories.isEmpty()) {
            return categories.get(0).getSlug();
        }
        return null;
    }
    
    // 템플릿 호환용 - body 필드를 계산해서 반환
    public String getBody() {
        if (excerpt != null) {
            return excerpt;
        }
        if (content != null) {
            return content.toString();
        }
        return "";
    }
} 