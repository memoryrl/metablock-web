package com.example.metablock.service;

import com.example.metablock.model.Page;
import com.example.metablock.model.Post;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

@Service
public class PayloadCmsService {
    
    private static final Logger log = LoggerFactory.getLogger(PayloadCmsService.class);
    
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;
    
    public PayloadCmsService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClientBuilder = webClientBuilder;
        this.objectMapper = objectMapper;
    }
    
    @Value("${cms.api.url:http://localhost:3001/api}")
    private String cmsApiUrl;
    
    private WebClient getWebClient() {
        return webClientBuilder.baseUrl(cmsApiUrl).build();
    }
    
    @Cacheable("pages")
    public List<Page> getAllPages() {
        try {
            WebClient webClient = getWebClient();
            
            Mono<JsonNode> response = webClient
                    .get()
                    .uri("/pages")
                    .retrieve()
                    .bodyToMono(JsonNode.class);
            
            JsonNode result = response.block();
            if (result != null && result.has("docs")) {
                JsonNode docs = result.get("docs");
                return objectMapper.convertValue(docs, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Page.class));
            }
            
            return List.of();
        } catch (Exception e) {
            log.error("Error fetching pages from CMS", e);
            return List.of();
        }
    }
    
    @Cacheable("page")
    public Optional<Page> getPageById(String id) {
        try {
            WebClient webClient = getWebClient();
            
            Mono<JsonNode> response = webClient
                    .get()
                    .uri("/pages/{id}", id)
                    .retrieve()
                    .bodyToMono(JsonNode.class);
            
            JsonNode result = response.block();
            if (result != null) {
                return Optional.of(objectMapper.convertValue(result, Page.class));
            }
            
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error fetching page by id: " + id, e);
            return Optional.empty();
        }
    }
    
    // 캐시 비활성화를 위해 주석 처리
    // @Cacheable("posts")
    public List<Post> getAllPosts(String category, int page, int limit) {
        try {
            WebClient webClient = getWebClient();
            
            String uri = "/posts?where[_status][equals]=published&page=" + page + "&limit=" + limit;
            if (category != null && !category.isEmpty()) {
                uri += "&where[categories][contains]=" + category;
            }
            
            Mono<JsonNode> response = webClient
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(JsonNode.class);
            
            JsonNode result = response.block();
            if (result != null && result.has("docs")) {
                JsonNode docs = result.get("docs");
                return objectMapper.convertValue(docs, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Post.class));
            }
            
            return List.of();
        } catch (Exception e) {
            log.error("Error fetching posts from CMS", e);
            return List.of();
        }
    }
    
    // 캐시 비활성화를 위해 주석 처리
    // @Cacheable("post")
    public Optional<Post> getPostBySlug(String slug) {
        try {
            WebClient webClient = getWebClient();
            
            Mono<JsonNode> response = webClient
                    .get()
                    .uri("/posts?where[slug][equals]={slug}&where[_status][equals]=published", slug)
                    .retrieve()
                    .bodyToMono(JsonNode.class);
            
            JsonNode result = response.block();
            if (result != null && result.has("docs") && result.get("docs").size() > 0) {
                JsonNode postData = result.get("docs").get(0);
                return Optional.of(objectMapper.convertValue(postData, Post.class));
            }
            
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error fetching post by slug: " + slug, e);
            return Optional.empty();
        }
    }

    // 캐시 비활성화를 위해 주석 처리
    // @Cacheable("postById")
    public Optional<Post> getPostById(String id) {
        try {
            WebClient webClient = getWebClient();
            
            Mono<JsonNode> response = webClient
                    .get()
                    .uri("/posts/{id}", id)
                    .retrieve()
                    .bodyToMono(JsonNode.class);
            
            JsonNode result = response.block();
            if (result != null) {
                return Optional.of(objectMapper.convertValue(result, Post.class));
            }
            
            return Optional.empty();
        } catch (Exception e) {
            log.error("Error fetching post by id: " + id, e);
            return Optional.empty();
        }
    }
    
    // 사용자 정보 가져오기
    public List<Object> getAllUsers() {
        try {
            WebClient webClient = getWebClient();
            
            Mono<JsonNode> response = webClient
                    .get()
                    .uri("/users")
                    .retrieve()
                    .bodyToMono(JsonNode.class);
            
            JsonNode result = response.block();
            if (result != null && result.has("docs")) {
                JsonNode docs = result.get("docs");
                return objectMapper.convertValue(docs, 
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Object.class));
            }
            
            return List.of();
        } catch (Exception e) {
            log.error("Error fetching users from CMS", e);
            return List.of();
        }
    }
    
    // CMS 연결 상태 확인
    public boolean isCmsAvailable() {
        try {
            WebClient webClient = getWebClient();
            
            Mono<String> response = webClient
                    .get()
                    .uri("/posts?limit=1")
                    .retrieve()
                    .bodyToMono(String.class);
            
            String result = response.block();
            return result != null;
        } catch (Exception e) {
            log.warn("CMS is not available: " + e.getMessage());
            return false;
        }
    }
    
    // CMS 서버 정보 가져오기
    public String getCmsServerInfo() {
        try {
            WebClient webClient = webClientBuilder.baseUrl("http://localhost:3001").build();
            
            Mono<JsonNode> response = webClient
                    .get()
                    .uri("/api/server-info")
                    .retrieve()
                    .bodyToMono(JsonNode.class);
            
            JsonNode result = response.block();
            if (result != null) {
                return result.toString();
            }
            
            return "CMS API 응답 없음";
        } catch (Exception e) {
            log.error("Error fetching CMS server info", e);
            return "CMS 연결 실패: " + e.getMessage();
        }
    }
} 