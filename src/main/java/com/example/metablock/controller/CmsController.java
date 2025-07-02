package com.example.metablock.controller;

import com.example.metablock.model.Page;
import com.example.metablock.model.Post;
import com.example.metablock.service.ContentRenderingService;
import com.example.metablock.service.PayloadCmsService;
import com.example.metablock.util.LexicalHtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class CmsController {
    
    private static final Logger log = LoggerFactory.getLogger(CmsController.class);
    
    private final PayloadCmsService cmsService;
    private final ContentRenderingService contentRenderingService;
    private final LexicalHtmlRenderer lexicalHtmlRenderer;
    
    public CmsController(PayloadCmsService cmsService, ContentRenderingService contentRenderingService, LexicalHtmlRenderer lexicalHtmlRenderer) {
        this.cmsService = cmsService;
        this.contentRenderingService = contentRenderingService;
        this.lexicalHtmlRenderer = lexicalHtmlRenderer;
    }
    
    @GetMapping("/cms")
    public String cmsHome(Model model) {
        return "redirect:/cms/home";
    }

    @GetMapping("/cms/home")
    public String cmsHomePage(Model model) {
        List<Post> posts = cmsService.getAllPosts("", 1, 10);
        List<Object> users = cmsService.getAllUsers();
        boolean cmsAvailable = cmsService.isCmsAvailable();
        String serverInfo = cmsService.getCmsServerInfo();
        
        model.addAttribute("posts", posts);
        model.addAttribute("users", users);
        model.addAttribute("cmsAvailable", cmsAvailable);
        model.addAttribute("serverInfo", serverInfo);
        model.addAttribute("title", "Payload CMS 3.40.0 + PostgreSQL");
        
        return "cms/home";
    }
    
    @GetMapping("/cms/page/{id}")
    public String dynamicPage(@PathVariable String id, Model model) {
        Optional<Page> pageOpt = cmsService.getPageById(id);
        
        if (pageOpt.isEmpty()) {
            model.addAttribute("error", "페이지를 찾을 수 없습니다.");
            return "error/404";
        }
        
        Page page = pageOpt.get();
        
        model.addAttribute("page", page);
        model.addAttribute("title", page.getTitle());
        
        return "cms/dynamic-page";
    }
    
    @GetMapping("/cms/posts")
    public String postList(
            @RequestParam(defaultValue = "") String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int limit,
            Model model) {
        
        List<Post> posts = cmsService.getAllPosts(category, page, limit);
        
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("category", category);
        model.addAttribute("title", "포스트 목록");
        
        return "cms/post-list";
    }
    
    @GetMapping("/cms/post/{id}")
    public String postDetail(@PathVariable String id, Model model) {
        Optional<Post> postOpt = cmsService.getPostById(id);
        
        if (postOpt.isEmpty()) {
            model.addAttribute("error", "포스트를 찾을 수 없습니다.");
            return "error/404";
        }
        
        Post post = postOpt.get();
        
        // 콘텐츠를 HTML로 변환
        String htmlBody = lexicalHtmlRenderer.render(post.getContent());

        model.addAttribute("post", post);
        model.addAttribute("htmlBody", htmlBody);
        model.addAttribute("title", post.getTitle());
        
        return "cms/post-detail";
    }
    
    @GetMapping("/cms/status")
    public String cmsStatus(Model model) {
        boolean available = cmsService.isCmsAvailable();
        String serverInfo = cmsService.getCmsServerInfo();
        List<Post> posts = cmsService.getAllPosts("", 1, 100);
        List<Object> users = cmsService.getAllUsers();
        
        model.addAttribute("cmsAvailable", available);
        model.addAttribute("serverInfo", serverInfo);
        model.addAttribute("postsCount", posts.size());
        model.addAttribute("usersCount", users.size());
        model.addAttribute("title", "CMS 상태 및 연결 정보");
        
        return "cms/status";
    }
} 