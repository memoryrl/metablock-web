package com.example.metablock.controller;

import com.example.metablock.model.Post;
import com.example.metablock.service.PayloadCmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {
    
    private static final Logger log = LoggerFactory.getLogger(ApiController.class);
    
    private final PayloadCmsService cmsService;
    
    public ApiController(PayloadCmsService cmsService) {
        this.cmsService = cmsService;
    }
    
    @GetMapping("/posts")
    public ResponseEntity<Map<String, Object>> getAllPosts(
            @RequestParam(defaultValue = "") String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        
        try {
            List<Post> posts = cmsService.getAllPosts(category, page, limit);
            
            Map<String, Object> response = new HashMap<>();
            response.put("docs", posts);
            response.put("totalDocs", posts.size());
            response.put("page", page);
            response.put("limit", limit);
            response.put("hasNextPage", false);
            response.put("hasPrevPage", page > 1);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching posts", e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch posts: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable String id) {
        try {
            return cmsService.getPostById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error fetching post by id: " + id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        try {
            List<Object> users = cmsService.getAllUsers();
            
            Map<String, Object> response = new HashMap<>();
            response.put("docs", users);
            response.put("totalDocs", users.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching users", e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch users: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    @GetMapping("/server-info")
    public ResponseEntity<Map<String, Object>> getServerInfo() {
        try {
            String serverInfo = cmsService.getCmsServerInfo();
            boolean available = cmsService.isCmsAvailable();
            
            Map<String, Object> response = new HashMap<>();
            response.put("serverInfo", serverInfo);
            response.put("cmsAvailable", available);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching server info", e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to fetch server info: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
} 