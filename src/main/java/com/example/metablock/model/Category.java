package com.example.metablock.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category {
    private String id;
    private String title;
    private String slug;
} 