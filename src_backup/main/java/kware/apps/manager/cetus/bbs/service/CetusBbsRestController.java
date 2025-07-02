package kware.apps.manager.cetus.bbs.service;


import cetus.bean.Page;
import cetus.bean.Pageable;
import kware.apps.manager.cetus.bbs.dto.request.BbsChange;
import kware.apps.manager.cetus.bbs.dto.request.BbsSave;
import kware.apps.manager.cetus.bbs.dto.request.BbsSearch;
import kware.apps.manager.cetus.bbs.dto.response.BbsList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cetus/api/bbs")
public class CetusBbsRestController {

    private final CetusBbsService service;

    @PostMapping
    public ResponseEntity saveBbs(@RequestBody @Valid BbsSave request) {
        service.saveBbs(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity findAllBbsPage(@Valid BbsSearch search, Pageable pageable) {
        Page<BbsList> allBbsPage = service.findAllBbsPage(search, pageable);
        return ResponseEntity.ok(allBbsPage);
    }

    @PutMapping("/{uid}")
    public ResponseEntity changeBbs(@PathVariable("uid") Long uid, @RequestBody @Valid BbsChange request) {
        service.changeBbs(uid, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity deleteBbs(@PathVariable("uid") Long uid) {
        service.deleteBbs(uid);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity findAllWorkplaceBbs() {
        List<BbsList> allWorkplaceBbs = service.findAllWorkplaceBbs();
        return ResponseEntity.ok(allWorkplaceBbs);
    }

    @GetMapping("/{uid}/child")
    public ResponseEntity findBbscttCountByBbsUid(@PathVariable("uid") Long uid) {
        int countByBbsUid = service.findBbscttCountByBbsUid(uid);
        return ResponseEntity.ok(countByBbsUid);
    }
}
