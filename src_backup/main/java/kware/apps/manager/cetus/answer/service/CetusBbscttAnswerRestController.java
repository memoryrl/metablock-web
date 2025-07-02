package kware.apps.manager.cetus.answer.service;


import cetus.bean.Page;
import cetus.bean.Pageable;
import kware.apps.manager.cetus.answer.dto.request.*;
import kware.apps.manager.cetus.answer.dto.response.AnswerList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cetus/api/answer")
public class CetusBbscttAnswerRestController {

    private final CetusBbscttAnswerService service;

    @PostMapping
    public ResponseEntity saveAnswer(@RequestBody @Valid AnswerSave request) {
        service.saveAnswer(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity deleteAnswer(@PathVariable("uid") Long uid) {
        service.deleteAnswer(uid);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{uid}")
    public ResponseEntity changeAnswer(@PathVariable("uid") Long uid, @RequestBody @Valid AnswerChange request) {
        service.changeAnswer(uid, request);
        return ResponseEntity.ok().build();
    }

    /**
     * {bbsUid} 에 엮이면서 {regUid}가 작성한 댓글 페이징 조회
     * */
    @GetMapping
    public ResponseEntity findAllAnswerPage(@Valid AnswerSearch search, Pageable pageable) {
        Page<AnswerList> allAnswerPage = service.findAllAnswerPage(search, pageable);
        return ResponseEntity.ok(allAnswerPage);
    }

    @PutMapping("/delete-several")
    public ResponseEntity deleteAnswers(@RequestBody @Valid AnswerDelete request) {
        service.deleteAnswers(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/excel")
    public ResponseEntity renderAnswerExcelPage(@Valid AnswerExcelSearch search) {
        service.renderEXCEL(search);
        return ResponseEntity.ok().build();
    }
}
