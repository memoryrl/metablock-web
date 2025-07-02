package kware.apps.manager.cetus.filelog.service;


import cetus.bean.Page;
import cetus.bean.Pageable;
import kware.apps.manager.cetus.filelog.dto.request.FileLogExcelSearch;
import kware.apps.manager.cetus.filelog.dto.request.FileLogSearch;
import kware.apps.manager.cetus.filelog.dto.response.FileLogList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cetus/api/file-log")
public class CetusFileLogRestController {

    private final CetusFileLogService service;
    private final CetusFileLogExcelService logExcelService;

    @GetMapping
    public ResponseEntity findAllFileLogPage(@Valid FileLogSearch search, Pageable pageable) {
        Page<FileLogList> allFileLogPage = service.findAllFileLogPage(search, pageable);
        return ResponseEntity.ok(allFileLogPage);
    }

    @GetMapping("/excel")
    public ResponseEntity renderUserExcelPage(@Valid FileLogExcelSearch search) {
        logExcelService.renderEXCEL(search);
        return ResponseEntity.ok().build();
    }
}
