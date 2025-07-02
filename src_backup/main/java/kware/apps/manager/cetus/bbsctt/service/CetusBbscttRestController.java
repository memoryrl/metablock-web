package kware.apps.manager.cetus.bbsctt.service;


import cetus.bean.Page;
import cetus.bean.Pageable;
import kware.apps.manager.cetus.bbsctt.dto.request.*;
import kware.apps.manager.cetus.bbsctt.dto.response.BbscttList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cetus/api/bbsctt")
public class CetusBbscttRestController {

    private final CetusBbscttService service;

    @GetMapping
    public ResponseEntity findAllBbscttPage(@Valid BbscttSearch search, Pageable pageable) {
        Page<BbscttList> allBbscttPage = service.findAllBbscttPage(search, pageable);
        return ResponseEntity.ok(allBbscttPage);
    }

    @PostMapping
    public ResponseEntity saveBbsctt(@RequestBody @Valid BbscttSave request) {
        service.saveBbsctt(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{uid}")
    public ResponseEntity changeBbsctt(@PathVariable("uid") Long uid, @RequestBody @Valid BbscttChange request) {
        service.changeBbsctt(uid, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity deleteBbsctt(@PathVariable("uid") Long uid) {
        service.deleteBbsctt(uid);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change-openAt")
    public ResponseEntity changeBbscttOpenAt(@RequestBody @Valid BbscttChangeOpenAt request) {
        service.changeBbscttOpenAt(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/delete-several")
    public ResponseEntity deleteBbsctts(@RequestBody @Valid BbscttDelete request) {
        service.deleteBbsctts(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/excel")
    public ResponseEntity renderBbscttExcelPage(@Valid BbscttExcelSearch search) {
        service.renderEXCEL(search);
        return ResponseEntity.ok().build();
    }
}
