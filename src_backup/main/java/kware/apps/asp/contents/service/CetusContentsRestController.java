package kware.apps.asp.contents.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import kware.apps.asp.contents.dto.request.CommentsSearch;
import kware.apps.asp.contents.dto.response.CommentsPage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cetus.bean.Page;
import cetus.bean.Pageable;
import kware.apps.asp.contents.domain.CetusCategories;
import kware.apps.asp.contents.domain.CetusContentsComment;
import kware.apps.asp.contents.dto.request.ContentsSearch;
import kware.apps.asp.contents.dto.response.ContentsPage;
import kware.apps.asp.contents.dto.request.ContentChange;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cetus/api/contents")
public class CetusContentsRestController {

    private final CetusContentsService service;

    @GetMapping("/categories")
    public ResponseEntity categoriesList() {
        List<CetusCategories> categoriesList = service.categoriesList();
        return ResponseEntity.ok(categoriesList);
    }

    @GetMapping
    public ResponseEntity getContentPageList(ContentsSearch search, Pageable pageable) {
        Page<ContentsPage> contentPageList = service.getContentPageList(search, pageable);
        return ResponseEntity.ok(contentPageList);
    }

    @PutMapping("/{uid}")
    public ResponseEntity changeContent(@PathVariable("uid") Long uid, @RequestBody @Valid ContentChange request) {
        service.changeContent(uid, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{uid}/comments")
    public ResponseEntity getContentComments(@PathVariable("uid") Long uid) {
        List<CetusContentsComment> contentComments = service.listComments(uid);
        return ResponseEntity.ok(contentComments);
    }

    @PostMapping("/comments")
    public ResponseEntity insertContentComment(@RequestBody @Valid CetusContentsComment comment) {
        service.insertComment(comment);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/comments/page")
    public ResponseEntity findAllCommentsPage(CommentsSearch search, Pageable pageable) {
        Page<CommentsPage> allCommentsPage = service.findAllCommentsPage(search, pageable);
        return ResponseEntity.ok(allCommentsPage);
    }

    @GetMapping("/comments/type-cnt")
    public ResponseEntity findCommentsTypeCnt(CommentsSearch search) {
        Map<String, Integer> map = service.findAllCommentCntByType(search);
        return ResponseEntity.ok(map);
    }
}
